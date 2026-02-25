package com.application.application.service;

import com.application.domain.model.Appointment;
import com.application.domain.model.DentistSchedule;
import com.application.domain.model.Patient;
import com.application.domain.model.User;
import com.application.domain.repository.AppointmentRepository;
import com.application.domain.repository.DentistScheduleRepository;
import com.application.domain.repository.PatientRepository;
import com.application.domain.repository.UserRepository;
import com.application.domain.valueobject.AppointmentId;
import com.application.domain.valueobject.PatientId;
import com.application.domain.valueobject.UserId;
import com.application.domain.enums.AppointmentStatus;
import com.application.domain.enums.AppointmentType;
import com.application.domain.enums.UserRole;
import com.application.domain.enums.InvoiceStatus;
import com.application.domain.model.Invoice;
import com.application.domain.repository.InvoiceRepository;
import com.application.application.service.BillingService;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public class SchedulingService {

    private final AppointmentRepository appointmentRepository;
    private final DentistScheduleRepository dentistScheduleRepository;
    private final PatientRepository patientRepository;
    private final UserRepository userRepository;
    private final InvoiceRepository invoiceRepository;
    private final BillingService billingService;

    public SchedulingService(AppointmentRepository appointmentRepository,
                             DentistScheduleRepository dentistScheduleRepository,
                             PatientRepository patientRepository,
                             UserRepository userRepository,
                             InvoiceRepository invoiceRepository,
                             BillingService billingService) {
        this.appointmentRepository = appointmentRepository;
        this.dentistScheduleRepository = dentistScheduleRepository;
        this.patientRepository = patientRepository;
        this.userRepository = userRepository;
        this.invoiceRepository = invoiceRepository;
        this.billingService = billingService;
    }

    public Appointment scheduleAppointment(UUID patientId,
                                           UUID dentistId,
                                           LocalDateTime proposedTime,
                                           Duration duration,
                                           AppointmentType type,
                                           String reason) {
        // 1. Validate patient exists and is active
        Patient patient = patientRepository.findById(new PatientId(patientId))
                .orElseThrow(() -> new IllegalArgumentException("Patient not found with ID: " + patientId));
        if (!patient.getStatus().isActive()) {
            throw new IllegalStateException("Cannot schedule appointment for an inactive or archived patient.");
        }

        // 2. Validate dentist exists, is active, and has DENTIST role
        User dentist = userRepository.findById(new UserId(dentistId))
                .orElseThrow(() -> new IllegalArgumentException("User not found with ID: " + dentistId));
        if (!dentist.isActive() || dentist.getRole() != UserRole.DENTIST) {
            throw new IllegalStateException("User is not an active dentist.");
        }

        // 3. Check patient financial eligibility (Critical Business Rule #7)
        if (!type.equals(AppointmentType.EMERGENCY)) {
            List<Invoice> overdueInvoices = invoiceRepository.findByPatientIdAndStatus(patientId, InvoiceStatus.OVERDUE);
            boolean hasSevereOverdue = overdueInvoices.stream()
                    .anyMatch(inv -> inv.getDueDate().isBefore(LocalDateTime.now().minusDays(90).toLocalDate()));
            if (hasSevereOverdue) {
                throw new IllegalStateException("Patient has overdue invoices > 90 days. Cannot schedule non-emergency appointments.");
            }
        }

        // 4. Check patient no-show history (Critical Business Rule #5)
        if (!type.equals(AppointmentType.EMERGENCY)) {
            LocalDateTime sixMonthsAgo = LocalDateTime.now().minusMonths(6);
            long noShowCount = appointmentRepository.countByPatientIdAndStatusAndScheduledTimeAfter(
                    patientId, AppointmentStatus.NO_SHOW, sixMonthsAgo);
            if (noShowCount >= 3) {
                // In a real system, we would check for a deposit here. For now, we block.
                throw new IllegalStateException("Patient has 3 or more NO_SHOW appointments in last 6 months. Deposit required.");
            }
        }

        // 5. Validate dentist availability (Critical Business Rules #1 & #2)
        DentistSchedule schedule = dentistScheduleRepository.findByDentistId(dentistId)
                .orElseThrow(() -> new IllegalStateException("No active schedule found for dentist ID: " + dentistId));
        if (!schedule.isActive()) {
            throw new IllegalStateException("Dentist schedule is not active.");
        }

        boolean isAvailable = schedule.isAvailable(proposedTime, duration);
        if (!isAvailable && !type.equals(AppointmentType.EMERGENCY)) {
            throw new IllegalArgumentException("Dentist is not available at the proposed time.");
        }

        // 6. Check for overlapping appointments for the dentist
        List<Appointment> existingAppointments = appointmentRepository.findByDentistIdAndStatusInAndScheduledTimeBetween(
                dentistId,
                List.of(AppointmentStatus.SCHEDULED, AppointmentStatus.CONFIRMED, AppointmentStatus.IN_PROGRESS),
                proposedTime.minus(duration),
                proposedTime.plus(duration)
        );
        if (!existingAppointments.isEmpty()) {
            throw new IllegalArgumentException("Dentist has a conflicting appointment at the proposed time.");
        }

        // 7. Create and persist the appointment
        Appointment appointment = Appointment.builder()
                .appointmentId(new AppointmentId(UUID.randomUUID()))
                .patientId(patientId)
                .dentistId(dentistId)
                .scheduledTime(proposedTime)
                .duration(duration)
                .type(type)
                .status(type.equals(AppointmentType.EMERGENCY) ? AppointmentStatus.SCHEDULED : AppointmentStatus.SCHEDULED)
                .reason(reason)
                .createdAt(LocalDateTime.now())
                .build();

        return appointmentRepository.save(appointment);
    }

    public Appointment rescheduleAppointment(UUID appointmentId, LocalDateTime newTime, Duration newDuration) {
        Appointment appointment = appointmentRepository.findById(new AppointmentId(appointmentId))
                .orElseThrow(() -> new IllegalArgumentException("Appointment not found with ID: " + appointmentId));

        // Check if appointment can be rescheduled (not in progress, completed, or cancelled)
        if (appointment.getStatus() == AppointmentStatus.IN_PROGRESS ||
            appointment.getStatus() == AppointmentStatus.COMPLETED ||
            appointment.getStatus() == AppointmentStatus.CANCELLED) {
            throw new IllegalStateException("Cannot reschedule an appointment that is " + appointment.getStatus());
        }

        // Validate new time with dentist availability
        DentistSchedule schedule = dentistScheduleRepository.findByDentistId(appointment.getDentistId())
                .orElseThrow(() -> new IllegalStateException("No active schedule found for dentist."));

        boolean isAvailable = schedule.isAvailable(newTime, newDuration);
        if (!isAvailable && appointment.getType() != AppointmentType.EMERGENCY) {
            throw new IllegalArgumentException("Dentist is not available at the new time.");
        }

        // Check for overlapping appointments (excluding the current one being rescheduled)
        List<Appointment> conflictingAppointments = appointmentRepository.findByDentistIdAndStatusInAndScheduledTimeBetween(
                appointment.getDentistId(),
                List.of(AppointmentStatus.SCHEDULED, AppointmentStatus.CONFIRMED, AppointmentStatus.IN_PROGRESS),
                newTime.minus(newDuration),
                newTime.plus(newDuration)
        ).stream()
                .filter(a -> !a.getAppointmentId().equals(appointment.getAppointmentId()))
                .toList();

        if (!conflictingAppointments.isEmpty()) {
            throw new IllegalArgumentException("Dentist has a conflicting appointment at the new time.");
        }

        // Update appointment
        appointment.reschedule(newTime, newDuration);
        return appointmentRepository.save(appointment);
    }

    public Appointment cancelAppointment(UUID appointmentId, String cancelledBy) {
        Appointment appointment = appointmentRepository.findById(new AppointmentId(appointmentId))
                .orElseThrow(() -> new IllegalArgumentException("Appointment not found with ID: " + appointmentId));

        // Check cancellation window (Critical Business Rule #4)
        if (appointment.getScheduledTime().minusHours(24).isAfter(LocalDateTime.now())) {
            // Cancellation is allowed without penalty
            appointment.cancel(cancelledBy);
        } else {
            // Late cancellation - mark as cancelled but may trigger penalty (e.g., invoice)
            appointment.cancel(cancelledBy);
            // In a full system, we might create a late cancellation fee invoice here
        }

        return appointmentRepository.save(appointment);
    }

    public Appointment updateAppointmentStatus(UUID appointmentId, AppointmentStatus newStatus) {
        Appointment appointment = appointmentRepository.findById(new AppointmentId(appointmentId))
                .orElseThrow(() -> new IllegalArgumentException("Appointment not found with ID: " + appointmentId));

        // Special handling for completion status
        if (newStatus == AppointmentStatus.COMPLETED) {
            // Critical Business Rule #6: Trigger invoice generation if not already billed
            Optional<Invoice> existingInvoice = invoiceRepository.findByAppointmentId(appointmentId);
            if (existingInvoice.isEmpty()) {
                billingService.generateInvoiceForAppointment(appointmentId);
            }
        }

        appointment.updateStatus(newStatus);
        return appointmentRepository.save(appointment);
    }

    public List<Appointment> findAppointmentsForPatient(UUID patientId, LocalDateTime start, LocalDateTime end) {
        return appointmentRepository.findByPatientIdAndScheduledTimeBetween(patientId, start, end);
    }

    public List<Appointment> findAppointmentsForDentist(UUID dentistId, LocalDateTime start, LocalDateTime end) {
        return appointmentRepository.findByDentistIdAndScheduledTimeBetween(dentistId, start, end);
    }

    public List<LocalDateTime> findAvailableSlots(UUID dentistId, LocalDateTime date, Duration duration) {
        // This is a simplified implementation. A real system would query the schedule,
        // existing appointments, and generate a list of available time slots.
        DentistSchedule schedule = dentistScheduleRepository.findByDentistId(dentistId)
                .orElseThrow(() -> new IllegalArgumentException("Dentist schedule not found."));

        // Delegate to the domain entity's logic for slot calculation
        return schedule.calculateAvailableSlots(date.toLocalDate(), duration);
    }
}