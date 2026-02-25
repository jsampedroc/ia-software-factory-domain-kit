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
import com.application.domain.enums.PatientStatus;
import com.application.domain.enums.UserRole;
import com.application.domain.exception.DomainException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.*;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class SchedulingServiceTest {

    @Mock
    private AppointmentRepository appointmentRepository;
    @Mock
    private DentistScheduleRepository dentistScheduleRepository;
    @Mock
    private PatientRepository patientRepository;
    @Mock
    private UserRepository userRepository;
    @Mock
    private BillingService billingService;

    @InjectMocks
    private SchedulingService schedulingService;

    @Captor
    private ArgumentCaptor<Appointment> appointmentCaptor;

    private UUID patientId;
    private UUID dentistId;
    private UUID adminId;
    private PatientId patientDomainId;
    private UserId dentistDomainId;
    private UserId adminDomainId;
    private LocalDateTime proposedTime;
    private Duration duration;
    private AppointmentType type;
    private String reason;

    @BeforeEach
    void setUp() {
        patientId = UUID.randomUUID();
        dentistId = UUID.randomUUID();
        adminId = UUID.randomUUID();
        patientDomainId = new PatientId(patientId);
        dentistDomainId = new UserId(dentistId);
        adminDomainId = new UserId(adminId);
        proposedTime = LocalDateTime.now().plusDays(1).withHour(10).withMinute(0);
        duration = Duration.ofMinutes(30);
        type = AppointmentType.CONSULTATION;
        reason = "Routine checkup";
    }

    @Test
    void scheduleAppointment_shouldSuccessfullyCreateAppointment() {
        // Given
        Patient mockPatient = mock(Patient.class);
        User mockDentist = mock(User.class);
        DentistSchedule mockSchedule = mock(DentistSchedule.class);
        Appointment mockSavedAppointment = mock(Appointment.class);

        when(patientRepository.findById(patientDomainId)).thenReturn(Optional.of(mockPatient));
        when(mockPatient.getStatus()).thenReturn(PatientStatus.ACTIVE);
        when(userRepository.findById(dentistDomainId)).thenReturn(Optional.of(mockDentist));
        when(mockDentist.isActive()).thenReturn(true);
        when(mockDentist.getRole()).thenReturn(UserRole.DENTIST);
        when(dentistScheduleRepository.findByDentistId(dentistId)).thenReturn(Optional.of(mockSchedule));
        when(mockSchedule.isActive()).thenReturn(true);
        when(mockSchedule.isAvailable(proposedTime, duration)).thenReturn(true);
        when(appointmentRepository.findByDentistIdAndTimeRange(eq(dentistId), any(LocalDateTime.class), any(LocalDateTime.class)))
                .thenReturn(Collections.emptyList());
        when(billingService.hasOverdueInvoiceExceedingDays(patientId, 90)).thenReturn(false);
        when(appointmentRepository.findNoShowAppointmentsForPatient(eq(patientId), any(LocalDateTime.class)))
                .thenReturn(Collections.emptyList());
        when(appointmentRepository.save(any(Appointment.class))).thenReturn(mockSavedAppointment);

        // When
        Appointment result = schedulingService.scheduleAppointment(patientId, dentistId, proposedTime, duration, type, reason, false, null);

        // Then
        assertThat(result).isEqualTo(mockSavedAppointment);
        verify(appointmentRepository).save(appointmentCaptor.capture());
        Appointment captured = appointmentCaptor.getValue();
        assertThat(captured.getPatientId()).isEqualTo(patientId);
        assertThat(captured.getDentistId()).isEqualTo(dentistId);
        assertThat(captured.getScheduledTime()).isEqualTo(proposedTime);
        assertThat(captured.getDuration()).isEqualTo(duration);
        assertThat(captured.getType()).isEqualTo(type);
        assertThat(captured.getStatus()).isEqualTo(AppointmentStatus.SCHEDULED);
        assertThat(captured.getReason()).isEqualTo(reason);
    }

    @Test
    void scheduleAppointment_shouldThrowWhenPatientNotFound() {
        when(patientRepository.findById(patientDomainId)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> schedulingService.scheduleAppointment(patientId, dentistId, proposedTime, duration, type, reason, false, null))
                .isInstanceOf(DomainException.class)
                .hasMessageContaining("Patient not found");
    }

    @Test
    void scheduleAppointment_shouldThrowWhenPatientIsArchived() {
        Patient mockPatient = mock(Patient.class);
        when(patientRepository.findById(patientDomainId)).thenReturn(Optional.of(mockPatient));
        when(mockPatient.getStatus()).thenReturn(PatientStatus.ARCHIVED);

        assertThatThrownBy(() -> schedulingService.scheduleAppointment(patientId, dentistId, proposedTime, duration, type, reason, false, null))
                .isInstanceOf(DomainException.class)
                .hasMessageContaining("archived patient");
    }

    @Test
    void scheduleAppointment_shouldThrowWhenDentistNotFound() {
        Patient mockPatient = mock(Patient.class);
        when(patientRepository.findById(patientDomainId)).thenReturn(Optional.of(mockPatient));
        when(mockPatient.getStatus()).thenReturn(PatientStatus.ACTIVE);
        when(userRepository.findById(dentistDomainId)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> schedulingService.scheduleAppointment(patientId, dentistId, proposedTime, duration, type, reason, false, null))
                .isInstanceOf(DomainException.class)
                .hasMessageContaining("Dentist not found");
    }

    @Test
    void scheduleAppointment_shouldThrowWhenUserIsNotActiveDentist() {
        Patient mockPatient = mock(Patient.class);
        User mockUser = mock(User.class);
        when(patientRepository.findById(patientDomainId)).thenReturn(Optional.of(mockPatient));
        when(mockPatient.getStatus()).thenReturn(PatientStatus.ACTIVE);
        when(userRepository.findById(dentistDomainId)).thenReturn(Optional.of(mockUser));
        when(mockUser.isActive()).thenReturn(false);
        when(mockUser.getRole()).thenReturn(UserRole.DENTIST);

        assertThatThrownBy(() -> schedulingService.scheduleAppointment(patientId, dentistId, proposedTime, duration, type, reason, false, null))
                .isInstanceOf(DomainException.class)
                .hasMessageContaining("not an active dentist");
    }

    @Test
    void scheduleAppointment_shouldThrowWhenDentistScheduleNotFound() {
        Patient mockPatient = mock(Patient.class);
        User mockDentist = mock(User.class);
        when(patientRepository.findById(patientDomainId)).thenReturn(Optional.of(mockPatient));
        when(mockPatient.getStatus()).thenReturn(PatientStatus.ACTIVE);
        when(userRepository.findById(dentistDomainId)).thenReturn(Optional.of(mockDentist));
        when(mockDentist.isActive()).thenReturn(true);
        when(mockDentist.getRole()).thenReturn(UserRole.DENTIST);
        when(dentistScheduleRepository.findByDentistId(dentistId)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> schedulingService.scheduleAppointment(patientId, dentistId, proposedTime, duration, type, reason, false, null))
                .isInstanceOf(DomainException.class)
                .hasMessageContaining("No schedule found");
    }

    @Test
    void scheduleAppointment_shouldThrowWhenDentistScheduleNotActive() {
        Patient mockPatient = mock(Patient.class);
        User mockDentist = mock(User.class);
        DentistSchedule mockSchedule = mock(DentistSchedule.class);
        when(patientRepository.findById(patientDomainId)).thenReturn(Optional.of(mockPatient));
        when(mockPatient.getStatus()).thenReturn(PatientStatus.ACTIVE);
        when(userRepository.findById(dentistDomainId)).thenReturn(Optional.of(mockDentist));
        when(mockDentist.isActive()).thenReturn(true);
        when(mockDentist.getRole()).thenReturn(UserRole.DENTIST);
        when(dentistScheduleRepository.findByDentistId(dentistId)).thenReturn(Optional.of(mockSchedule));
        when(mockSchedule.isActive()).thenReturn(false);

        assertThatThrownBy(() -> schedulingService.scheduleAppointment(patientId, dentistId, proposedTime, duration, type, reason, false, null))
                .isInstanceOf(DomainException.class)
                .hasMessageContaining("Dentist schedule is not active");
    }

    @Test
    void scheduleAppointment_shouldThrowWhenDentistNotAvailable() {
        Patient mockPatient = mock(Patient.class);
        User mockDentist = mock(User.class);
        DentistSchedule mockSchedule = mock(DentistSchedule.class);
        when(patientRepository.findById(patientDomainId)).thenReturn(Optional.of(mockPatient));
        when(mockPatient.getStatus()).thenReturn(PatientStatus.ACTIVE);
        when(userRepository.findById(dentistDomainId)).thenReturn(Optional.of(mockDentist));
        when(mockDentist.isActive()).thenReturn(true);
        when(mockDentist.getRole()).thenReturn(UserRole.DENTIST);
        when(dentistScheduleRepository.findByDentistId(dentistId)).thenReturn(Optional.of(mockSchedule));
        when(mockSchedule.isActive()).thenReturn(true);
        when(mockSchedule.isAvailable(proposedTime, duration)).thenReturn(false);

        assertThatThrownBy(() -> schedulingService.scheduleAppointment(patientId, dentistId, proposedTime, duration, type, reason, false, null))
                .isInstanceOf(DomainException.class)
                .hasMessageContaining("Dentist is not available");
    }

    @Test
    void scheduleAppointment_shouldThrowWhenOverlappingAppointmentExists() {
        Patient mockPatient = mock(Patient.class);
        User mockDentist = mock(User.class);
        DentistSchedule mockSchedule = mock(DentistSchedule.class);
        Appointment existingAppointment = mock(Appointment.class);
        when(patientRepository.findById(patientDomainId)).thenReturn(Optional.of(mockPatient));
        when(mockPatient.getStatus()).thenReturn(PatientStatus.ACTIVE);
        when(userRepository.findById(dentistDomainId)).thenReturn(Optional.of(mockDentist));
        when(mockDentist.isActive()).thenReturn(true);
        when(mockDentist.getRole()).thenReturn(UserRole.DENTIST);
        when(dentistScheduleRepository.findByDentistId(dentistId)).thenReturn(Optional.of(mockSchedule));
        when(mockSchedule.isActive()).thenReturn(true);
        when(mockSchedule.isAvailable(proposedTime, duration)).thenReturn(true);
        when(appointmentRepository.findByDentistIdAndTimeRange(eq(dentistId), any(LocalDateTime.class), any(LocalDateTime.class)))
                .thenReturn(List.of(existingAppointment));

        assertThatThrownBy(() -> schedulingService.scheduleAppointment(patientId, dentistId, proposedTime, duration, type, reason, false, null))
                .isInstanceOf(DomainException.class)
                .hasMessageContaining("already has an appointment");
    }

    @Test
    void scheduleAppointment_shouldThrowWhenPatientHasThreeNoShowsAndNotEmergency() {
        Patient mockPatient = mock(Patient.class);
        User mockDentist = mock(User.class);
        DentistSchedule mockSchedule = mock(DentistSchedule.class);
        Appointment noShowAppointment = mock(Appointment.class);
        when(patientRepository.findById(patientDomainId)).thenReturn(Optional.of(mockPatient));
        when(mockPatient.getStatus()).thenReturn(PatientStatus.ACTIVE);
        when(userRepository.findById(dentistDomainId)).thenReturn(Optional.of(mockDentist));
        when(mockDentist.isActive()).thenReturn(true);
        when(mockDentist.getRole()).thenReturn(UserRole.DENTIST);
        when(dentistScheduleRepository.findByDentistId(dentistId)).thenReturn(Optional.of(mockSchedule));
        when(mockSchedule.isActive()).thenReturn(true);
        when(mockSchedule.isAvailable(proposedTime, duration)).thenReturn(true);
        when(appointmentRepository.findByDentistIdAndTimeRange(eq(dentistId), any(LocalDateTime.class), any(LocalDateTime.class)))
                .thenReturn(Collections.emptyList());
        when(appointmentRepository.findNoShowAppointmentsForPatient(eq(patientId), any(LocalDateTime.class)))
                .thenReturn(List.of(noShowAppointment, noShowAppointment, noShowAppointment));

        assertThatThrownBy(() -> schedulingService.scheduleAppointment(patientId, dentistId, proposedTime, duration, type, reason, false, null))
                .isInstanceOf(DomainException.class)
                .hasMessageContaining("3 or more NO_SHOW appointments");
    }

    @Test
    void scheduleAppointment_shouldAllowEmergencyWhenPatientHasThreeNoShows() {
        Patient mockPatient = mock(Patient.class);
        User mockDentist = mock(User.class);
        User mockAdmin = mock(User.class);
        DentistSchedule mockSchedule = mock(DentistSchedule.class);
        Appointment noShowAppointment = mock(Appointment.class);
        Appointment mockSavedAppointment = mock(Appointment.class);

        when(patientRepository.findById(patientDomainId)).thenReturn(Optional.of(mockPatient));
        when(mockPatient.getStatus()).thenReturn(PatientStatus.ACTIVE);
        when(userRepository.findById(dentistDomainId)).thenReturn(Optional.of(mockDentist));
        when(mockDentist.isActive()).thenReturn(true);
        when(mockDentist.getRole()).thenReturn(UserRole.DENTIST);
        when(userRepository.findById(adminDomainId)).thenReturn(Optional.of(mockAdmin));
        when(mockAdmin.getRole()).thenReturn(UserRole.ADMINISTRATOR);
        when(dentistScheduleRepository.findByDentistId(dentistId)).thenReturn(Optional.of(mockSchedule));
        when(mockSchedule.isActive()).thenReturn(true);
        when(appointmentRepository.findByDentistIdAndTimeRange(eq(dentistId), any(LocalDateTime.class), any(LocalDateTime.class)))
                .thenReturn(Collections.emptyList());
        when(appointmentRepository.findNoShowAppointmentsForPatient(eq(patientId), any(LocalDateTime.class)))
                .thenReturn(List.of(noShowAppointment, noShowAppointment, noShowAppointment));
        when(billingService.hasOverdueInvoiceExceedingDays(patientId, 90)).thenReturn(false);
        when(appointmentRepository.save(any(Appointment.class))).thenReturn(mockSavedAppointment);

        Appointment result = schedulingService.scheduleAppointment(patientId, dentistId, proposedTime, duration, AppointmentType.EMERGENCY, "Toothache", true, adminId);

        assertThat(result).isEqualTo(mockSavedAppointment);
        verify(appointmentRepository).save(any(Appointment.class));
    }

    @Test
    void scheduleAppointment_shouldThrowWhenPatientHasOverdueInvoiceAndNotEmergency() {
        Patient mockPatient = mock(Patient.class);
        User mockDentist = mock(User.class);
        DentistSchedule mockSchedule = mock(DentistSchedule.class);
        when(patientRepository.findById(patientDomainId)).thenReturn(Optional.of(mockPatient));
        when(mockPatient.getStatus()).thenReturn(PatientStatus.ACTIVE);
        when(userRepository.findById(dentistDomainId)).thenReturn(Optional.of(mockDentist));
        when(mockDentist.isActive()).thenReturn(true);
        when(mockDentist.getRole()).thenReturn(UserRole.DENTIST);
        when(dentistScheduleRepository.findByDentistId(dentistId)).thenReturn(Optional.of(mockSchedule));
        when(mockSchedule.isActive()).thenReturn(true);
        when(mockSchedule.isAvailable(proposedTime, duration)).thenReturn(true);
        when(appointmentRepository.findByDentistIdAndTimeRange(eq(dentistId), any(LocalDateTime.class), any(LocalDateTime.class)))
                .thenReturn(Collections.emptyList());
        when(appointmentRepository.findNoShowAppointmentsForPatient(eq(patientId), any(LocalDateTime.class)))
                .thenReturn(Collections.emptyList());
        when(billingService.hasOverdueInvoiceExceedingDays(patientId, 90)).thenReturn(true);

        assertThatThrownBy(() -> schedulingService.scheduleAppointment(patientId, dentistId, proposedTime, duration, type, reason, false, null))
                .isInstanceOf(DomainException.class)
                .hasMessageContaining("overdue invoices exceeding 90 days");
    }

    @Test
    void scheduleAppointment_shouldThrowWhenEmergencyAppointmentWithoutAdminApproval() {
        Patient mockPatient = mock(Patient.class);
        User mockDentist = mock(User.class);
        User mockNonAdmin = mock(User.class);
        DentistSchedule mockSchedule = mock(DentistSchedule.class);
        when(patientRepository.findById(patientDomainId)).thenReturn(Optional.of(mockPatient));
        when(mockPatient.getStatus()).thenReturn(PatientStatus.ACTIVE);
        when(userRepository.findById(dentistDomainId)).