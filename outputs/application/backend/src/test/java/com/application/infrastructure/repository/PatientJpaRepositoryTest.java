package com.application.infrastructure.repository;

import com.application.infrastructure.entity.PatientEntity;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

@DataJpaTest
@ExtendWith({MockitoExtension.class, SpringExtension.class})
class PatientJpaRepositoryTest {

    @Mock
    private PatientJpaRepository patientJpaRepository;

    private PatientEntity activePatient;
    private PatientEntity inactivePatient;
    private final UUID activePatientId = UUID.randomUUID();
    private final UUID inactivePatientId = UUID.randomUUID();
    private final String testDni = "12345678A";
    private final String testEmail = "test@example.com";
    private final LocalDateTime cutoffDate = LocalDateTime.now().minusYears(2);

    @BeforeEach
    void setUp() {
        activePatient = PatientEntity.create(
                activePatientId,
                testDni,
                "Juan",
                "Pérez",
                LocalDate.of(1990, 1, 1),
                "600123456",
                testEmail,
                "Calle Falsa 123",
                LocalDateTime.now().minusMonths(6),
                true
        );

        inactivePatient = PatientEntity.create(
                inactivePatientId,
                "87654321B",
                "Ana",
                "García",
                LocalDate.of(1985, 5, 5),
                "600654321",
                "ana@example.com",
                "Avenida Real 456",
                LocalDateTime.now().minusYears(3),
                false
        );
    }

    @Test
    void findByDni_shouldReturnPatient_whenPatientExists() {
        when(patientJpaRepository.findByDni(testDni)).thenReturn(Optional.of(activePatient));

        Optional<PatientEntity> result = patientJpaRepository.findByDni(testDni);

        assertThat(result).isPresent();
        assertThat(result.get().getId()).isEqualTo(activePatientId);
        assertThat(result.get().getDni()).isEqualTo(testDni);
    }

    @Test
    void findByDni_shouldReturnEmpty_whenPatientDoesNotExist() {
        when(patientJpaRepository.findByDni(anyString())).thenReturn(Optional.empty());

        Optional<PatientEntity> result = patientJpaRepository.findByDni("NONEXISTENT");

        assertThat(result).isEmpty();
    }

    @Test
    void findByEmail_shouldReturnPatient_whenPatientExists() {
        when(patientJpaRepository.findByEmail(testEmail)).thenReturn(Optional.of(activePatient));

        Optional<PatientEntity> result = patientJpaRepository.findByEmail(testEmail);

        assertThat(result).isPresent();
        assertThat(result.get().getId()).isEqualTo(activePatientId);
        assertThat(result.get().getEmail()).isEqualTo(testEmail);
    }

    @Test
    void findByEmail_shouldReturnEmpty_whenPatientDoesNotExist() {
        when(patientJpaRepository.findByEmail(anyString())).thenReturn(Optional.empty());

        Optional<PatientEntity> result = patientJpaRepository.findByEmail("nonexistent@example.com");

        assertThat(result).isEmpty();
    }

    @Test
    void findByActivoTrue_shouldReturnOnlyActivePatients() {
        List<PatientEntity> activePatients = List.of(activePatient);
        when(patientJpaRepository.findByActivoTrue()).thenReturn(activePatients);

        List<PatientEntity> result = patientJpaRepository.findByActivoTrue();

        assertThat(result).hasSize(1);
        assertThat(result.get(0).isActivo()).isTrue();
        assertThat(result.get(0).getId()).isEqualTo(activePatientId);
    }

    @Test
    void findByActivoFalse_shouldReturnOnlyInactivePatients() {
        List<PatientEntity> inactivePatients = List.of(inactivePatient);
        when(patientJpaRepository.findByActivoFalse()).thenReturn(inactivePatients);

        List<PatientEntity> result = patientJpaRepository.findByActivoFalse();

        assertThat(result).hasSize(1);
        assertThat(result.get(0).isActivo()).isFalse();
        assertThat(result.get(0).getId()).isEqualTo(inactivePatientId);
    }

    @Test
    void findInactivePatientsSince_shouldReturnPatientsRegisteredBeforeCutoffAndActive() {
        List<PatientEntity> inactiveSince = List.of(activePatient);
        when(patientJpaRepository.findInactivePatientsSince(any(LocalDateTime.class))).thenReturn(inactiveSince);

        List<PatientEntity> result = patientJpaRepository.findInactivePatientsSince(cutoffDate);

        assertThat(result).hasSize(1);
        assertThat(result.get(0).isActivo()).isTrue();
        assertThat(result.get(0).getFechaRegistro()).isBefore(cutoffDate);
    }

    @Test
    void searchByNombreApellidoOrDni_shouldReturnMatchingPatients() {
        String searchQuery = "Pérez";
        List<PatientEntity> matchingPatients = List.of(activePatient);
        when(patientJpaRepository.searchByNombreApellidoOrDni(anyString())).thenReturn(matchingPatients);

        List<PatientEntity> result = patientJpaRepository.searchByNombreApellidoOrDni(searchQuery);

        assertThat(result).hasSize(1);
        assertThat(result.get(0).getApellido()).contains(searchQuery);
    }

    @Test
    void searchByNombreApellidoOrDni_shouldReturnEmptyList_whenNoMatches() {
        when(patientJpaRepository.searchByNombreApellidoOrDni(anyString())).thenReturn(List.of());

        List<PatientEntity> result = patientJpaRepository.searchByNombreApellidoOrDni("NOMATCH");

        assertThat(result).isEmpty();
    }

    @Test
    void existsByDni_shouldReturnTrue_whenDniExists() {
        when(patientJpaRepository.existsByDni(testDni)).thenReturn(true);

        boolean result = patientJpaRepository.existsByDni(testDni);

        assertThat(result).isTrue();
    }

    @Test
    void existsByDni_shouldReturnFalse_whenDniDoesNotExist() {
        when(patientJpaRepository.existsByDni(anyString())).thenReturn(false);

        boolean result = patientJpaRepository.existsByDni("NONEXISTENT");

        assertThat(result).isFalse();
    }

    @Test
    void existsByEmail_shouldReturnTrue_whenEmailExists() {
        when(patientJpaRepository.existsByEmail(testEmail)).thenReturn(true);

        boolean result = patientJpaRepository.existsByEmail(testEmail);

        assertThat(result).isTrue();
    }

    @Test
    void existsByEmail_shouldReturnFalse_whenEmailDoesNotExist() {
        when(patientJpaRepository.existsByEmail(anyString())).thenReturn(false);

        boolean result = patientJpaRepository.existsByEmail("nonexistent@example.com");

        assertThat(result).isFalse();
    }
}