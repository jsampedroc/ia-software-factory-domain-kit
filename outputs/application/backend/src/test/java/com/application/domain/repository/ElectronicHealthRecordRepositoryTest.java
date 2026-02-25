package com.application.domain.repository;

import com.application.domain.model.ElectronicHealthRecord;
import com.application.domain.valueobject.EhrId;
import com.application.domain.valueobject.PatientId;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ElectronicHealthRecordRepositoryTest {

    @Mock
    private ElectronicHealthRecordRepository repository;

    private EhrId ehrId;
    private PatientId patientId;
    private ElectronicHealthRecord ehr;

    @BeforeEach
    void setUp() {
        ehrId = new EhrId(UUID.randomUUID());
        patientId = new PatientId(UUID.randomUUID());
        ehr = ElectronicHealthRecord.create(ehrId, patientId);
    }

    @Test
    void save_ShouldPersistElectronicHealthRecord() {
        when(repository.save(any(ElectronicHealthRecord.class))).thenReturn(ehr);

        ElectronicHealthRecord savedEhr = repository.save(ehr);

        verify(repository).save(ehr);
        assertThat(savedEhr).isEqualTo(ehr);
    }

    @Test
    void findById_WithExistingId_ShouldReturnElectronicHealthRecord() {
        when(repository.findById(ehrId)).thenReturn(Optional.of(ehr));

        Optional<ElectronicHealthRecord> foundEhr = repository.findById(ehrId);

        verify(repository).findById(ehrId);
        assertThat(foundEhr).isPresent().contains(ehr);
    }

    @Test
    void findById_WithNonExistingId_ShouldReturnEmpty() {
        when(repository.findById(ehrId)).thenReturn(Optional.empty());

        Optional<ElectronicHealthRecord> foundEhr = repository.findById(ehrId);

        verify(repository).findById(ehrId);
        assertThat(foundEhr).isEmpty();
    }

    @Test
    void delete_ShouldRemoveElectronicHealthRecord() {
        repository.delete(ehr);

        verify(repository).delete(ehr);
    }

    @Test
    void findByPatientId_WithExistingPatient_ShouldReturnElectronicHealthRecord() {
        when(repository.findByPatientId(patientId)).thenReturn(Optional.of(ehr));

        Optional<ElectronicHealthRecord> foundEhr = repository.findByPatientId(patientId);

        verify(repository).findByPatientId(patientId);
        assertThat(foundEhr).isPresent().contains(ehr);
    }

    @Test
    void findByPatientId_WithNonExistingPatient_ShouldReturnEmpty() {
        when(repository.findByPatientId(patientId)).thenReturn(Optional.empty());

        Optional<ElectronicHealthRecord> foundEhr = repository.findByPatientId(patientId);

        verify(repository).findByPatientId(patientId);
        assertThat(foundEhr).isEmpty();
    }

    @Test
    void existsByPatientId_WhenRecordExists_ShouldReturnTrue() {
        when(repository.existsByPatientId(patientId)).thenReturn(true);

        boolean exists = repository.existsByPatientId(patientId);

        verify(repository).existsByPatientId(patientId);
        assertThat(exists).isTrue();
    }

    @Test
    void existsByPatientId_WhenRecordDoesNotExist_ShouldReturnFalse() {
        when(repository.existsByPatientId(patientId)).thenReturn(false);

        boolean exists = repository.existsByPatientId(patientId);

        verify(repository).existsByPatientId(patientId);
        assertThat(exists).isFalse();
    }

    @Test
    void existsById_WithExistingId_ShouldReturnTrue() {
        when(repository.existsById(ehrId)).thenReturn(true);

        boolean exists = repository.existsById(ehrId);

        verify(repository).existsById(ehrId);
        assertThat(exists).isTrue();
    }

    @Test
    void existsById_WithNonExistingId_ShouldReturnFalse() {
        when(repository.existsById(ehrId)).thenReturn(false);

        boolean exists = repository.existsById(ehrId);

        verify(repository).existsById(ehrId);
        assertThat(exists).isFalse();
    }
}