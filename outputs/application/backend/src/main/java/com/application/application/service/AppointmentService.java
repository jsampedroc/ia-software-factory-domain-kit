package com.application.application.service;

import com.application.domain.model.Appointment;
import com.application.domain.model.Patient;
import com.application.domain.model.Dentist;
import com.application.domain.model.Clinic;
import com.application.domain.model.Treatment;
import com.application.domain.valueobject.AppointmentId;
import com.application.domain.valueobject.PatientId;
import com.application.domain.valueobject.DentistId;
import com.application.domain.valueobject.ClinicId;
import com.application.domain.valueobject.TreatmentId;
import com.application.domain.valueobject.AppointmentStatus;
import com.application.domain.port.AppointmentRepositoryPort;
import com.application.domain.port.PatientRepositoryPort;
import com.application.domain.port.DentistRepositoryPort;
import com.application.domain.port.ClinicRepositoryPort;
import com.application.domain.port.TreatmentRepositoryPort;
import com.application.domain.exception.DomainException;
import com.application.application.dto.AppointmentDTO;
import com.application.application.mapper.AppointmentMapper;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Slf4j
public class AppointmentService {

    private final AppointmentRepositoryPort appointmentRepositoryPort;
    private final PatientRepositoryPort patientRepositoryPort;
    private final DentistRepositoryPort dentistRepositoryPort;
    private final ClinicRepositoryPort clinicRepositoryPort;
    private final TreatmentRepositoryPort treatmentRepositoryPort;
    private final AppointmentMapper appointmentMapper;

    @Transactional(readOnly = true)
    public AppointmentDTO findById(AppointmentId id) {
        log.debug("Finding appointment with id: {}", id);
        Appointment appointment = appointmentRepositoryPort.findById(id)
                .orElseThrow(() -> new DomainException("Appointment not found with id: " + id));
        return appointmentMapper.toDTO(appointment);
    }

    @Transactional(readOnly = true)
    public List<AppointmentDTO> findAll() {
        log.debug("Finding all appointments");
        return appointmentRepositoryPort.findAll().stream()
                .map(appointmentMapper::toDTO)
                .collect(Collectors.toList());
    }

    @Transactional
    public AppointmentDTO create(AppointmentDTO dto) {
        log.debug("Creating appointment with data: {}", dto);
        validateAppointmentData(dto);

        Patient patient = patientRepositoryPort.findById(dto.getPatientId())
                .orElseThrow(() -> new DomainException("Patient not found with id: " + dto.getPatientId()));
        Dentist dentist = dentistRepositoryPort.findById(dto.getDentistId())
                .orElseThrow(() -> new DomainException("Dentist not found with id: " + dto.getDentistId()));
        Clinic clinic = clinicRepositoryPort.findById(dto.getClinicId())
                .orElseThrow(() -> new DomainException("Clinic not found with id: " + dto.getClinicId()));

        validateBusinessRules(dto, patient, dentist, clinic);

        Appointment appointment = appointmentMapper.toDomain(dto);
        appointment.setStatus(AppointmentStatus.PROGRAMADA);

        if (dto.getTreatmentIds() != null && !dto.getTreatmentIds().isEmpty()) {
            Set<Treatment> treatments = dto.getTreatmentIds().stream()
                    .map(treatmentId -> treatmentRepositoryPort.findById(treatmentId)
                            .orElseThrow(() -> new DomainException("Treatment not found with id: " + treatmentId)))
                    .collect(Collectors.toSet());
            appointment.setTreatments(treatments);
        }

        Appointment savedAppointment = appointmentRepositoryPort.save(appointment);
        log.info("Appointment created successfully with id: {}", savedAppointment.getId());
        return appointmentMapper.toDTO(savedAppointment);
    }

    @Transactional
    public AppointmentDTO update(AppointmentId id, AppointmentDTO dto) {
        log.debug("Updating appointment with id: {} and data: {}", id, dto);
        Appointment existingAppointment = appointmentRepositoryPort.findById(id)
                .orElseThrow(() -> new DomainException("Appointment not found with id: " + id));

        if (!existingAppointment.getStatus().equals(AppointmentStatus.PROGRAMADA) &&
            !existingAppointment.getStatus().equals(AppointmentStatus.CONFIRMADA)) {
            throw new DomainException("Cannot update appointment with status: " + existingAppointment.getStatus());
        }

        validateAppointmentData(dto);
        Patient patient = patientRepositoryPort.findById(dto.getPatientId())
                .orElseThrow(() -> new DomainException("Patient not found with id: " + dto.getPatientId()));
        Dentist dentist = dentistRepositoryPort.findById(dto.getDentistId())
                .orElseThrow(() -> new DomainException("Dentist not found with id: " + dto.getDentistId()));
        Clinic clinic = clinicRepositoryPort.findById(dto.getClinicId())
                .orElseThrow(() -> new DomainException("Clinic not found with id: " + dto.getClinicId()));

        validateBusinessRules(dto, patient, dentist, clinic);

        Appointment updatedAppointment = appointmentMapper.toDomain(dto);
        updatedAppointment.setId(id);
        updatedAppointment.setStatus(existingAppointment.getStatus());

        if (dto.getTreatmentIds() != null && !dto.getTreatmentIds().isEmpty()) {
            Set<Treatment> treatments = dto.getTreatmentIds().stream()
                    .map(treatmentId -> treatmentRepositoryPort.findById(treatmentId)
                            .orElseThrow(() -> new DomainException("Treatment not found with id: " + treatmentId)))
                    .collect(Collectors.toSet());
            updatedAppointment.setTreatments(treatments);
        }

        Appointment savedAppointment = appointmentRepositoryPort.save(updatedAppointment);
        log.info("Appointment updated successfully with id: {}", savedAppointment.getId());
        return appointmentMapper.toDTO(savedAppointment);
    }

    @Transactional
    public void delete(AppointmentId id) {
        log.debug("Deleting appointment with id: {}", id);
        Appointment appointment = appointmentRepositoryPort.findById(id)
                .orElseThrow(() -> new DomainException("Appointment not found with id: " + id));

        if (appointment.getStatus().equals(AppointmentStatus.EN_CURSO) ||
            appointment.getStatus().equals(AppointmentStatus.COMPLETADA)) {
            throw new DomainException("Cannot delete appointment with status: " + appointment.getStatus());
        }

        appointmentRepositoryPort.deleteById(id);
        log.info("Appointment deleted successfully with id: {}", id);
    }

    @Transactional
    public AppointmentDTO updateStatus(AppointmentId id, AppointmentStatus newStatus) {
        log.debug("Updating status for appointment id: {} to {}", id, newStatus);
        Appointment appointment = appointmentRepositoryPort.findById(id)
                .orElseThrow(() -> new DomainException("Appointment not found with id: " + id));

        validateStatusTransition(appointment.getStatus(), newStatus);

        if (newStatus.equals(AppointmentStatus.CANCELADA)) {
            validateCancellationPolicy(appointment);
        }

        appointment.setStatus(newStatus);
        Appointment updatedAppointment = appointmentRepositoryPort.save(appointment);
        log.info("Appointment status updated to {} for id: {}", newStatus, id);
        return appointmentMapper.toDTO(updatedAppointment);
    }

    @Transactional(readOnly = true)
    public List<AppointmentDTO> findByPatientId(PatientId patientId) {
        log.debug("Finding appointments for patient id: {}", patientId);
        return appointmentRepositoryPort.findByPatientId(patientId).stream()
                .map(appointmentMapper::toDTO)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<AppointmentDTO> findByDentistId(DentistId dentistId) {
        log.debug("Finding appointments for dentist id: {}", dentistId);
        return appointmentRepositoryPort.findByDentistId(dentistId).stream()
                .map(appointmentMapper::toDTO)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<AppointmentDTO> findByClinicId(ClinicId clinicId) {
        log.debug("Finding appointments for clinic id: {}", clinicId);
        return appointmentRepositoryPort.findByClinicId(clinicId).stream()
                .map(appointmentMapper::toDTO)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<AppointmentDTO> findByStatus(AppointmentStatus status) {
        log.debug("Finding appointments with status: {}", status);
        return appointmentRepositoryPort.findByStatus(status).stream()
                .map(appointmentMapper::toDTO)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<AppointmentDTO> findByDateRange(LocalDateTime startDate, LocalDateTime endDate) {
        log.debug("Finding appointments between {} and {}", startDate, endDate);
        return appointmentRepositoryPort.findByDateRange(startDate, endDate).stream()
                .map(appointmentMapper::toDTO)
                .collect(Collectors.toList());
    }

    private void validateAppointmentData(AppointmentDTO dto) {
        if (dto.getFechaHora() == null) {
            throw new DomainException("Appointment date and time is required");
        }
        if (dto.getDuracionMinutos() == null || dto.getDuracionMinutos() <= 0) {
            throw new DomainException("Appointment duration must be positive");
        }
        if (dto.getPatientId() == null) {
            throw new DomainException("Patient ID is required");
        }
        if (dto.getDentistId() == null) {
            throw new DomainException("Dentist ID is required");
        }
        if (dto.getClinicId() == null) {
            throw new DomainException("Clinic ID is required");
        }
    }

    private void validateBusinessRules(AppointmentDTO dto, Patient patient, Dentist dentist, Clinic clinic) {
        // Rule 1: Appointment must be scheduled at least 24 hours in advance
        LocalDateTime now = LocalDateTime.now();
        if (dto.getFechaHora().isBefore(now.plusHours(24))) {
            throw new DomainException("Appointment must be scheduled at least 24 hours in advance");
        }

        // Rule 2: Dentist cannot have overlapping appointments
        LocalDateTime appointmentEnd = dto.getFechaHora().plusMinutes(dto.getDuracionMinutos());
        boolean dentistHasConflict = appointmentRepositoryPort.existsByDentistAndTimeRange(
                dentist.getId(), dto.getFechaHora(), appointmentEnd);
        if (dentistHasConflict) {
            throw new DomainException("Dentist already has an appointment scheduled for this time");
        }

        // Rule 3: Patient cannot have more than 3 pending appointments
        List<Appointment> patientPendingAppointments = appointmentRepositoryPort
                .findByPatientIdAndStatus(patient.getId(), AppointmentStatus.PROGRAMADA);
        if (patientPendingAppointments.size() >= 3) {
            throw new DomainException("Patient cannot have more than 3 pending appointments");
        }

        // Rule 4: Patient with invoices overdue for more than 60 days cannot schedule new appointments
        boolean hasOverdueInvoices = patient.hasOverdueInvoices(60);
        if (hasOverdueInvoices) {
            throw new DomainException("Patient has overdue invoices and cannot schedule new appointments");
        }

        // Rule 5: Dentist must have valid medical license
        if (!dentist.isLicenseValid()) {
            throw new DomainException("Dentist medical license is not valid");
        }

        // Rule 6: Clinic must be active
        if (!clinic.isActiva()) {
            throw new DomainException("Clinic is not active");
        }

        // Rule 7: Appointment duration must be between 15 and 180 minutes
        if (dto.getDuracionMinutos() < 15 || dto.getDuracionMinutos() > 180) {
            throw new DomainException("Appointment duration must be between 15 and 180 minutes");
        }

        // Rule 8: Appointment must be within clinic working hours
        if (!clinic.isWithinWorkingHours(dto.getFechaHora().toLocalTime())) {
            throw new DomainException("Appointment time is outside clinic working hours");
        }
    }

    private void validateStatusTransition(AppointmentStatus currentStatus, AppointmentStatus newStatus) {
        switch (currentStatus) {
            case PROGRAMADA:
                if (!newStatus.equals(AppointmentStatus.CONFIRMADA) &&
                    !newStatus.equals(AppointmentStatus.CANCELADA)) {
                    throw new DomainException("Invalid status transition from PROGRAMADA to " + newStatus);
                }
                break;
            case CONFIRMADA:
                if (!newStatus.equals(AppointmentStatus.EN_CURSO) &&
                    !newStatus.equals(AppointmentStatus.CANCELADA)) {
                    throw new DomainException("Invalid status transition from CONFIRMADA to " + newStatus);
                }
                break;
            case EN_CURSO:
                if (!newStatus.equals(AppointmentStatus.COMPLETADA)) {
                    throw new DomainException("Invalid status transition from EN_CURSO to " + newStatus);
                }
                break;
            case COMPLETADA:
            case CANCELADA:
                throw new DomainException("Cannot change status from " + currentStatus);
            default:
                throw new DomainException("Unknown appointment status: " + currentStatus);
        }
    }

    private void validateCancellationPolicy(Appointment appointment) {
        LocalDateTime now = LocalDateTime.now();
        long hoursUntilAppointment = ChronoUnit.HOURS.between(now, appointment.getFechaHora());
        
        if (hoursUntilAppointment < 24) {
            log.warn("Appointment {} cancelled with less than 24 hours notice. Late cancellation fee applies.", appointment.getId());
            // In a real implementation, this would trigger a late cancellation fee
            // For now, we just log it as per business rule 4
        }
    }
}