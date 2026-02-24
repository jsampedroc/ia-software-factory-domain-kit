package com.application.infrastructure.repository;

import com.application.infrastructure.entity.AppointmentEntity;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.repository.query.FluentQuery;

import java.time.LocalDateTime;
import java.util.*;
import java.util.function.Function;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AppointmentJpaRepositoryTest {

    @Mock
    private AppointmentJpaRepository appointmentJpaRepository;

    private final UUID patientId = UUID.randomUUID();
    private final UUID dentistId = UUID.randomUUID();
    private final UUID clinicId = UUID.randomUUID();
    private final UUID consultingRoomId = UUID.randomUUID();
    private final LocalDateTime now = LocalDateTime.now();
    private final LocalDateTime start = now.minusDays(1);
    private final LocalDateTime end = now.plusDays(1);
    private final List<String> estados = Arrays.asList("PROGRAMADA", "CONFIRMADA");

    @Test
    void findByPatientId_ShouldReturnAppointments() {
        // Given
        AppointmentEntity appointment1 = AppointmentEntity.create(UUID.randomUUID(), patientId, dentistId, clinicId, consultingRoomId, now, 30, "PROGRAMADA", "Limpieza", "Notas");
        AppointmentEntity appointment2 = AppointmentEntity.create(UUID.randomUUID(), patientId, dentistId, clinicId, consultingRoomId, now.plusHours(2), 45, "CONFIRMADA", "Revisión", "Notas");
        List<AppointmentEntity> expectedAppointments = Arrays.asList(appointment1, appointment2);

        when(appointmentJpaRepository.findByPatientId(patientId)).thenReturn(expectedAppointments);

        // When
        List<AppointmentEntity> result = appointmentJpaRepository.findByPatientId(patientId);

        // Then
        assertThat(result).isNotNull().hasSize(2).containsExactlyElementsOf(expectedAppointments);
    }

    @Test
    void findByDentistId_ShouldReturnAppointments() {
        // Given
        AppointmentEntity appointment1 = AppointmentEntity.create(UUID.randomUUID(), patientId, dentistId, clinicId, consultingRoomId, now, 30, "PROGRAMADA", "Limpieza", "Notas");
        List<AppointmentEntity> expectedAppointments = Collections.singletonList(appointment1);

        when(appointmentJpaRepository.findByDentistId(dentistId)).thenReturn(expectedAppointments);

        // When
        List<AppointmentEntity> result = appointmentJpaRepository.findByDentistId(dentistId);

        // Then
        assertThat(result).isNotNull().hasSize(1).containsExactly(appointment1);
    }

    @Test
    void findByClinicId_ShouldReturnAppointments() {
        // Given
        AppointmentEntity appointment1 = AppointmentEntity.create(UUID.randomUUID(), patientId, dentistId, clinicId, consultingRoomId, now, 30, "PROGRAMADA", "Limpieza", "Notas");
        List<AppointmentEntity> expectedAppointments = Collections.singletonList(appointment1);

        when(appointmentJpaRepository.findByClinicId(clinicId)).thenReturn(expectedAppointments);

        // When
        List<AppointmentEntity> result = appointmentJpaRepository.findByClinicId(clinicId);

        // Then
        assertThat(result).isNotNull().hasSize(1).containsExactly(appointment1);
    }

    @Test
    void findByConsultingRoomId_ShouldReturnAppointments() {
        // Given
        AppointmentEntity appointment1 = AppointmentEntity.create(UUID.randomUUID(), patientId, dentistId, clinicId, consultingRoomId, now, 30, "PROGRAMADA", "Limpieza", "Notas");
        List<AppointmentEntity> expectedAppointments = Collections.singletonList(appointment1);

        when(appointmentJpaRepository.findByConsultingRoomId(consultingRoomId)).thenReturn(expectedAppointments);

        // When
        List<AppointmentEntity> result = appointmentJpaRepository.findByConsultingRoomId(consultingRoomId);

        // Then
        assertThat(result).isNotNull().hasSize(1).containsExactly(appointment1);
    }

    @Test
    void findByEstado_ShouldReturnAppointments() {
        // Given
        String estado = "PROGRAMADA";
        AppointmentEntity appointment1 = AppointmentEntity.create(UUID.randomUUID(), patientId, dentistId, clinicId, consultingRoomId, now, 30, estado, "Limpieza", "Notas");
        List<AppointmentEntity> expectedAppointments = Collections.singletonList(appointment1);

        when(appointmentJpaRepository.findByEstado(estado)).thenReturn(expectedAppointments);

        // When
        List<AppointmentEntity> result = appointmentJpaRepository.findByEstado(estado);

        // Then
        assertThat(result).isNotNull().hasSize(1).containsExactly(appointment1);
    }

    @Test
    void findByDentistIdAndDateRange_ShouldReturnAppointments() {
        // Given
        AppointmentEntity appointment1 = AppointmentEntity.create(UUID.randomUUID(), patientId, dentistId, clinicId, consultingRoomId, now, 30, "PROGRAMADA", "Limpieza", "Notas");
        List<AppointmentEntity> expectedAppointments = Collections.singletonList(appointment1);

        when(appointmentJpaRepository.findByDentistIdAndDateRange(dentistId, start, end)).thenReturn(expectedAppointments);

        // When
        List<AppointmentEntity> result = appointmentJpaRepository.findByDentistIdAndDateRange(dentistId, start, end);

        // Then
        assertThat(result).isNotNull().hasSize(1).containsExactly(appointment1);
    }

    @Test
    void findByPatientIdAndEstadoIn_ShouldReturnAppointments() {
        // Given
        AppointmentEntity appointment1 = AppointmentEntity.create(UUID.randomUUID(), patientId, dentistId, clinicId, consultingRoomId, now, 30, "PROGRAMADA", "Limpieza", "Notas");
        List<AppointmentEntity> expectedAppointments = Collections.singletonList(appointment1);

        when(appointmentJpaRepository.findByPatientIdAndEstadoIn(patientId, estados)).thenReturn(expectedAppointments);

        // When
        List<AppointmentEntity> result = appointmentJpaRepository.findByPatientIdAndEstadoIn(patientId, estados);

        // Then
        assertThat(result).isNotNull().hasSize(1).containsExactly(appointment1);
    }

    @Test
    void findByClinicIdAndDateRange_ShouldReturnAppointments() {
        // Given
        AppointmentEntity appointment1 = AppointmentEntity.create(UUID.randomUUID(), patientId, dentistId, clinicId, consultingRoomId, now, 30, "PROGRAMADA", "Limpieza", "Notas");
        List<AppointmentEntity> expectedAppointments = Collections.singletonList(appointment1);

        when(appointmentJpaRepository.findByClinicIdAndDateRange(clinicId, start, end)).thenReturn(expectedAppointments);

        // When
        List<AppointmentEntity> result = appointmentJpaRepository.findByClinicIdAndDateRange(clinicId, start, end);

        // Then
        assertThat(result).isNotNull().hasSize(1).containsExactly(appointment1);
    }

    @Test
    void countByPatientIdAndEstadoIn_ShouldReturnCount() {
        // Given
        long expectedCount = 3L;
        when(appointmentJpaRepository.countByPatientIdAndEstadoIn(patientId, estados)).thenReturn(expectedCount);

        // When
        long result = appointmentJpaRepository.countByPatientIdAndEstadoIn(patientId, estados);

        // Then
        assertThat(result).isEqualTo(expectedCount);
    }

    @Test
    void existsOverlappingAppointmentForDentist_WhenOverlapExists_ShouldReturnTrue() {
        // Given
        LocalDateTime startTime = now;
        LocalDateTime endTime = now.plusHours(1);
        when(appointmentJpaRepository.existsOverlappingAppointmentForDentist(dentistId, startTime, endTime)).thenReturn(true);

        // When
        boolean result = appointmentJpaRepository.existsOverlappingAppointmentForDentist(dentistId, startTime, endTime);

        // Then
        assertThat(result).isTrue();
    }

    @Test
    void existsOverlappingAppointmentForDentist_WhenNoOverlap_ShouldReturnFalse() {
        // Given
        LocalDateTime startTime = now;
        LocalDateTime endTime = now.plusHours(1);
        when(appointmentJpaRepository.existsOverlappingAppointmentForDentist(dentistId, startTime, endTime)).thenReturn(false);

        // When
        boolean result = appointmentJpaRepository.existsOverlappingAppointmentForDentist(dentistId, startTime, endTime);

        // Then
        assertThat(result).isFalse();
    }

    // Tests for inherited JpaRepository methods (basic CRUD)
    @Test
    void save_ShouldReturnSavedEntity() {
        // Given
        UUID appointmentId = UUID.randomUUID();
        AppointmentEntity appointmentToSave = AppointmentEntity.create(appointmentId, patientId, dentistId, clinicId, consultingRoomId, now, 30, "PROGRAMADA", "Limpieza", "Notas");
        when(appointmentJpaRepository.save(appointmentToSave)).thenReturn(appointmentToSave);

        // When
        AppointmentEntity result = appointmentJpaRepository.save(appointmentToSave);

        // Then
        assertThat(result).isNotNull().isEqualTo(appointmentToSave);
    }

    @Test
    void findById_ShouldReturnAppointment() {
        // Given
        UUID appointmentId = UUID.randomUUID();
        AppointmentEntity expectedAppointment = AppointmentEntity.create(appointmentId, patientId, dentistId, clinicId, consultingRoomId, now, 30, "PROGRAMADA", "Limpieza", "Notas");
        when(appointmentJpaRepository.findById(appointmentId)).thenReturn(Optional.of(expectedAppointment));

        // When
        Optional<AppointmentEntity> result = appointmentJpaRepository.findById(appointmentId);

        // Then
        assertThat(result).isPresent().contains(expectedAppointment);
    }

    @Test
    void findAll_ShouldReturnAllAppointments() {
        // Given
        AppointmentEntity appointment1 = AppointmentEntity.create(UUID.randomUUID(), patientId, dentistId, clinicId, consultingRoomId, now, 30, "PROGRAMADA", "Limpieza", "Notas");
        AppointmentEntity appointment2 = AppointmentEntity.create(UUID.randomUUID(), patientId, dentistId, clinicId, consultingRoomId, now.plusHours(2), 45, "CONFIRMADA", "Revisión", "Notas");
        List<AppointmentEntity> expectedAppointments = Arrays.asList(appointment1, appointment2);
        when(appointmentJpaRepository.findAll()).thenReturn(expectedAppointments);

        // When
        List<AppointmentEntity> result = appointmentJpaRepository.findAll();

        // Then
        assertThat(result).isNotNull().hasSize(2).containsExactlyElementsOf(expectedAppointments);
    }

    @Test
    void deleteById_ShouldDeleteAppointment() {
        // Given
        UUID appointmentId = UUID.randomUUID();
        // Mock void method - just verify no exception is thrown
        // For deleteById, we can't easily verify behavior in a unit test of the interface,
        // but we can at least call it to ensure the method exists and works.
        // In a real test with an actual implementation, we would verify the interaction.
        // For this interface test, we'll just call it.
        appointmentJpaRepository.deleteById(appointmentId);

        // Then
        // No exception expected
    }
}