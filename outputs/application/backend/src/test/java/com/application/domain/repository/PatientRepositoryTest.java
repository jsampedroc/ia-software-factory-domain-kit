package com.application.domain.repository;

import com.application.domain.model.Patient;
import com.application.domain.model.PatientIdentity;
import com.application.domain.valueobject.PatientId;
import com.application.domain.enums.PatientStatus;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class PatientRepositoryTest {

    @Mock
    private PatientRepository patientRepository;

    private PatientId patientId;
    private Patient patient;
    private LocalDate dateOfBirth;
    private String firstName;
    private String lastName;

    @BeforeEach
    void setUp() {
        patientId = new PatientId(UUID.randomUUID());
        dateOfBirth = LocalDate.of(1985, 5, 15);
        firstName = "John";
        lastName = "Doe";

        PatientIdentity identity = PatientIdentity.builder()
                .firstName(firstName)
                .lastName(lastName)
                .dateOfBirth(dateOfBirth)
                .nationalId("ID123456")
                .email("john.doe@example.com")
                .phoneNumber("+1234567890")
                .address("123 Main St")
                .build();

        patient = Patient.create(patientId, identity, PatientStatus.ACTIVE);
    }

    @Test
    void findById_shouldReturnPatient_whenPatientExists() {
        when(patientRepository.findById(patientId)).thenReturn(Optional.of(patient));

        Optional<Patient> result = patientRepository.findById(patientId);

        assertThat(result).isPresent();
        assertThat(result.get()).isEqualTo(patient);
        verify(patientRepository).findById(patientId);
    }

    @Test
    void findById_shouldReturnEmpty_whenPatientDoesNotExist() {
        when(patientRepository.findById(patientId)).thenReturn(Optional.empty());

        Optional<Patient> result = patientRepository.findById(patientId);

        assertThat(result).isEmpty();
        verify(patientRepository).findById(patientId);
    }

    @Test
    void save_shouldPersistPatient() {
        when(patientRepository.save(any(Patient.class))).thenReturn(patient);

        Patient savedPatient = patientRepository.save(patient);

        assertThat(savedPatient).isEqualTo(patient);
        verify(patientRepository).save(patient);
    }

    @Test
    void delete_shouldRemovePatient() {
        patientRepository.delete(patient);

        verify(patientRepository).delete(patient);
    }

    @Test
    void findAll_shouldReturnAllPatients() {
        List<Patient> patients = List.of(patient);
        when(patientRepository.findAll()).thenReturn(patients);

        List<Patient> result = patientRepository.findAll();

        assertThat(result).hasSize(1);
        assertThat(result).containsExactly(patient);
        verify(patientRepository).findAll();
    }

    @Test
    void findByIdentity_shouldReturnPatient_whenIdentityMatches() {
        when(patientRepository.findByIdentity(firstName, lastName, dateOfBirth))
                .thenReturn(Optional.of(patient));

        Optional<Patient> result = patientRepository.findByIdentity(firstName, lastName, dateOfBirth);

        assertThat(result).isPresent();
        assertThat(result.get()).isEqualTo(patient);
        verify(patientRepository).findByIdentity(firstName, lastName, dateOfBirth);
    }

    @Test
    void findByIdentity_shouldReturnEmpty_whenIdentityDoesNotMatch() {
        when(patientRepository.findByIdentity(firstName, lastName, dateOfBirth))
                .thenReturn(Optional.empty());

        Optional<Patient> result = patientRepository.findByIdentity(firstName, lastName, dateOfBirth);

        assertThat(result).isEmpty();
        verify(patientRepository).findByIdentity(firstName, lastName, dateOfBirth);
    }

    @Test
    void findByStatus_shouldReturnPatientsWithGivenStatus() {
        List<Patient> activePatients = List.of(patient);
        when(patientRepository.findByStatus(PatientStatus.ACTIVE)).thenReturn(activePatients);

        List<Patient> result = patientRepository.findByStatus(PatientStatus.ACTIVE);

        assertThat(result).hasSize(1);
        assertThat(result).containsExactly(patient);
        verify(patientRepository).findByStatus(PatientStatus.ACTIVE);
    }

    @Test
    void findByStatus_shouldReturnEmptyList_whenNoPatientsWithStatus() {
        when(patientRepository.findByStatus(PatientStatus.ARCHIVED)).thenReturn(List.of());

        List<Patient> result = patientRepository.findByStatus(PatientStatus.ARCHIVED);

        assertThat(result).isEmpty();
        verify(patientRepository).findByStatus(PatientStatus.ARCHIVED);
    }

    @Test
    void existsByIdentity_shouldReturnTrue_whenIdentityExists() {
        when(patientRepository.existsByIdentity(firstName, lastName, dateOfBirth)).thenReturn(true);

        boolean exists = patientRepository.existsByIdentity(firstName, lastName, dateOfBirth);

        assertThat(exists).isTrue();
        verify(patientRepository).existsByIdentity(firstName, lastName, dateOfBirth);
    }

    @Test
    void existsByIdentity_shouldReturnFalse_whenIdentityDoesNotExist() {
        when(patientRepository.existsByIdentity(firstName, lastName, dateOfBirth)).thenReturn(false);

        boolean exists = patientRepository.existsByIdentity(firstName, lastName, dateOfBirth);

        assertThat(exists).isFalse();
        verify(patientRepository).existsByIdentity(firstName, lastName, dateOfBirth);
    }

    @Test
    void existsById_shouldReturnTrue_whenPatientExists() {
        when(patientRepository.existsById(patientId)).thenReturn(true);

        boolean exists = patientRepository.existsById(patientId);

        assertThat(exists).isTrue();
        verify(patientRepository).existsById(patientId);
    }

    @Test
    void existsById_shouldReturnFalse_whenPatientDoesNotExist() {
        when(patientRepository.existsById(patientId)).thenReturn(false);

        boolean exists = patientRepository.existsById(patientId);

        assertThat(exists).isFalse();
        verify(patientRepository).existsById(patientId);
    }
}