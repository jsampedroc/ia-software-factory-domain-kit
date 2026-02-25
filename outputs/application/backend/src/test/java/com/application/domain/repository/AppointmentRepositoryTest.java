package com.application.domain.repository;

import com.application.domain.model.Appointment;
import com.application.domain.valueobject.AppointmentId;
import com.application.domain.valueobject.PatientId;
import com.application.domain.valueobject.UserId;
import com.application.domain.enums.AppointmentStatus;
import com.application.domain.enums.AppointmentType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AppointmentRepositoryTest {

    @Mock
    private AppointmentRepository appointmentRepository;

    private AppointmentId appointmentId;
    private PatientId patientId;
    private UserId dentistId;
    private Appointment appointment;
    private LocalDateTime now;
    private LocalDate today;

    @BeforeEach
    void setUp() {
        appointmentId = new AppointmentId(UUID.randomUUID());
        patientId = new PatientId(UUID.randomUUID());
        dentistId = new UserId(UUID.randomUUID());
        now = LocalDateTime.now().withNano(0);
        today = now.toLocalDate();

        appointment = Appointment.create(
                appointmentId,
                patientId,
                dentistId,
                now.plusDays(1),
                Duration.ofMinutes(30),
                AppointmentType.CONSULTATION,
                AppointmentStatus.SCHEDULED,
                "Routine checkup"
        );
    }

    @Test
    void findByPatientId_ShouldReturnAppointmentsForPatient() {
        List<Appointment> expectedAppointments = List.of(appointment);
        when(appointmentRepository.findByPatientId(patientId)).thenReturn(expectedAppointments);

        List<Appointment> result = appointmentRepository.findByPatientId(patientId);

        assertThat(result).isEqualTo(expectedAppointments);
        verify(appointmentRepository).findByPatientId(patientId);
    }

    @Test
    void findByDentistId_ShouldReturnAppointmentsForDentist() {
        List<Appointment> expectedAppointments = List.of(appointment);
        when(appointmentRepository.findByDentistId(dentistId)).thenReturn(expectedAppointments);

        List<Appointment> result = appointmentRepository.findByDentistId(dentistId);

        assertThat(result).isEqualTo(expectedAppointments);
        verify(appointmentRepository).findByDentistId(dentistId);
    }

    @Test
    void findByPatientIdAndStatus_ShouldReturnFilteredAppointments() {
        List<Appointment> expectedAppointments = List.of(appointment);
        when(appointmentRepository.findByPatientIdAndStatus(patientId, AppointmentStatus.SCHEDULED))
                .thenReturn(expectedAppointments);

        List<Appointment> result = appointmentRepository.findByPatientIdAndStatus(patientId, AppointmentStatus.SCHEDULED);

        assertThat(result).isEqualTo(expectedAppointments);
        verify(appointmentRepository).findByPatientIdAndStatus(patientId, AppointmentStatus.SCHEDULED);
    }

    @Test
    void findByDentistIdAndStatus_ShouldReturnFilteredAppointments() {
        List<Appointment> expectedAppointments = List.of(appointment);
        when(appointmentRepository.findByDentistIdAndStatus(dentistId, AppointmentStatus.SCHEDULED))
                .thenReturn(expectedAppointments);

        List<Appointment> result = appointmentRepository.findByDentistIdAndStatus(dentistId, AppointmentStatus.SCHEDULED);

        assertThat(result).isEqualTo(expectedAppointments);
        verify(appointmentRepository).findByDentistIdAndStatus(dentistId, AppointmentStatus.SCHEDULED);
    }

    @Test
    void findByScheduledTimeBetween_ShouldReturnAppointmentsInTimeRange() {
        LocalDateTime start = today.atStartOfDay();
        LocalDateTime end = today.plusDays(1).atStartOfDay();
        List<Appointment> expectedAppointments = List.of(appointment);
        when(appointmentRepository.findByScheduledTimeBetween(start, end)).thenReturn(expectedAppointments);

        List<Appointment> result = appointmentRepository.findByScheduledTimeBetween(start, end);

        assertThat(result).isEqualTo(expectedAppointments);
        verify(appointmentRepository).findByScheduledTimeBetween(start, end);
    }

    @Test
    void findByScheduledDate_ShouldReturnAppointmentsForDate() {
        List<Appointment> expectedAppointments = List.of(appointment);
        when(appointmentRepository.findByScheduledDate(today)).thenReturn(expectedAppointments);

        List<Appointment> result = appointmentRepository.findByScheduledDate(today);

        assertThat(result).isEqualTo(expectedAppointments);
        verify(appointmentRepository).findByScheduledDate(today);
    }

    @Test
    void findByScheduledDateAndDentistId_ShouldReturnFilteredAppointments() {
        List<Appointment> expectedAppointments = List.of(appointment);
        when(appointmentRepository.findByScheduledDateAndDentistId(today, dentistId)).thenReturn(expectedAppointments);

        List<Appointment> result = appointmentRepository.findByScheduledDateAndDentistId(today, dentistId);

        assertThat(result).isEqualTo(expectedAppointments);
        verify(appointmentRepository).findByScheduledDateAndDentistId(today, dentistId);
    }

    @Test
    void findByTypeAndStatus_ShouldReturnFilteredAppointments() {
        List<Appointment> expectedAppointments = List.of(appointment);
        when(appointmentRepository.findByTypeAndStatus(AppointmentType.CONSULTATION, AppointmentStatus.SCHEDULED))
                .thenReturn(expectedAppointments);

        List<Appointment> result = appointmentRepository.findByTypeAndStatus(AppointmentType.CONSULTATION, AppointmentStatus.SCHEDULED);

        assertThat(result).isEqualTo(expectedAppointments);
        verify(appointmentRepository).findByTypeAndStatus(AppointmentType.CONSULTATION, AppointmentStatus.SCHEDULED);
    }

    @Test
    void findByAppointmentId_ShouldReturnAppointmentWhenExists() {
        when(appointmentRepository.findByAppointmentId(appointmentId)).thenReturn(Optional.of(appointment));

        Optional<Appointment> result = appointmentRepository.findByAppointmentId(appointmentId);

        assertThat(result).isPresent().contains(appointment);
        verify(appointmentRepository).findByAppointmentId(appointmentId);
    }

    @Test
    void findByAppointmentId_ShouldReturnEmptyWhenNotExists() {
        when(appointmentRepository.findByAppointmentId(appointmentId)).thenReturn(Optional.empty());

        Optional<Appointment> result = appointmentRepository.findByAppointmentId(appointmentId);

        assertThat(result).isEmpty();
        verify(appointmentRepository).findByAppointmentId(appointmentId);
    }

    @Test
    void existsByPatientIdAndScheduledTimeBetween_ShouldReturnTrueWhenExists() {
        LocalDateTime start = now.minusHours(1);
        LocalDateTime end = now.plusHours(1);
        when(appointmentRepository.existsByPatientIdAndScheduledTimeBetween(patientId, start, end)).thenReturn(true);

        boolean result = appointmentRepository.existsByPatientIdAndScheduledTimeBetween(patientId, start, end);

        assertThat(result).isTrue();
        verify(appointmentRepository).existsByPatientIdAndScheduledTimeBetween(patientId, start, end);
    }

    @Test
    void existsByDentistIdAndScheduledTimeBetween_ShouldReturnTrueWhenExists() {
        LocalDateTime start = now.minusHours(1);
        LocalDateTime end = now.plusHours(1);
        when(appointmentRepository.existsByDentistIdAndScheduledTimeBetween(dentistId, start, end)).thenReturn(true);

        boolean result = appointmentRepository.existsByDentistIdAndScheduledTimeBetween(dentistId, start, end);

        assertThat(result).isTrue();
        verify(appointmentRepository).existsByDentistIdAndScheduledTimeBetween(dentistId, start, end);
    }

    @Test
    void countByPatientIdAndStatusAndScheduledTimeAfter_ShouldReturnCorrectCount() {
        LocalDateTime since = now.minusMonths(6);
        long expectedCount = 2L;
        when(appointmentRepository.countByPatientIdAndStatusAndScheduledTimeAfter(patientId, AppointmentStatus.NO_SHOW, since))
                .thenReturn(expectedCount);

        long result = appointmentRepository.countByPatientIdAndStatusAndScheduledTimeAfter(patientId, AppointmentStatus.NO_SHOW, since);

        assertThat(result).isEqualTo(expectedCount);
        verify(appointmentRepository).countByPatientIdAndStatusAndScheduledTimeAfter(patientId, AppointmentStatus.NO_SHOW, since);
    }

    @Test
    void findOverlappingAppointments_ShouldReturnOverlappingAppointments() {
        LocalDateTime startTime = today.atTime(LocalTime.of(10, 0));
        LocalDateTime endTime = today.atTime(LocalTime.of(11, 0));
        List<Appointment> expectedOverlapping = List.of(appointment);
        when(appointmentRepository.findOverlappingAppointments(dentistId, startTime, endTime))
                .thenReturn(expectedOverlapping);

        List<Appointment> result = appointmentRepository.findOverlappingAppointments(dentistId, startTime, endTime);

        assertThat(result).isEqualTo(expectedOverlapping);
        verify(appointmentRepository).findOverlappingAppointments(dentistId, startTime, endTime);
    }

    @Test
    void save_ShouldPersistAppointment() {
        when(appointmentRepository.save(appointment)).thenReturn(appointment);

        Appointment saved = appointmentRepository.save(appointment);

        assertThat(saved).isEqualTo(appointment);
        verify(appointmentRepository).save(appointment);
    }

    @Test
    void delete_ShouldRemoveAppointment() {
        appointmentRepository.delete(appointment);

        verify(appointmentRepository).delete(appointment);
    }

    @Test
    void findById_ShouldReturnAppointment() {
        when(appointmentRepository.findById(appointmentId)).thenReturn(Optional.of(appointment));

        Optional<Appointment> result = appointmentRepository.findById(appointmentId);

        assertThat(result).isPresent().contains(appointment);
        verify(appointmentRepository).findById(appointmentId);
    }

    @Test
    void findAll_ShouldReturnAllAppointments() {
        List<Appointment> allAppointments = List.of(appointment);
        when(appointmentRepository.findAll()).thenReturn(allAppointments);

        List<Appointment> result = appointmentRepository.findAll();

        assertThat(result).isEqualTo(allAppointments);
        verify(appointmentRepository).findAll();
    }
}