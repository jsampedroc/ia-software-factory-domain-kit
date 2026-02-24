package com.application.infrastructure.adapter;

import com.application.domain.model.MedicalHistory;
import com.application.domain.valueobject.MedicalHistoryId;
import com.application.domain.valueobject.PatientId;
import com.application.infrastructure.entity.MedicalHistoryEntity;
import com.application.infrastructure.repository.MedicalHistoryJpaRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class MedicalHistoryJpaAdapterTest {

    @Mock
    private MedicalHistoryJpaRepository medicalHistoryJpaRepository;

    @InjectMocks
    private MedicalHistoryJpaAdapter medicalHistoryJpaAdapter;

    @Test
    void save_shouldConvertEntityAndSave() {
        // Given
        MedicalHistoryId historyId = new MedicalHistoryId(UUID.randomUUID());
        PatientId patientId = new PatientId(UUID.randomUUID());
        LocalDateTime now = LocalDateTime.now();

        MedicalHistory domain = MedicalHistory.create(
                historyId,
                patientId,
                now,
                now,
                "Alergia a penicilina",
                "Hipertensión",
                "Lisinopril 10mg",
                "Observaciones generales"
        );

        MedicalHistoryEntity entity = MedicalHistoryEntity.fromDomain(domain);
        when(medicalHistoryJpaRepository.save(any(MedicalHistoryEntity.class))).thenReturn(entity);

        // When
        MedicalHistory result = medicalHistoryJpaAdapter.save(domain);

        // Then
        assertThat(result).isNotNull();
        assertThat(result.getId()).isEqualTo(historyId);
        verify(medicalHistoryJpaRepository).save(any(MedicalHistoryEntity.class));
    }

    @Test
    void findById_shouldReturnDomainWhenExists() {
        // Given
        MedicalHistoryId historyId = new MedicalHistoryId(UUID.randomUUID());
        PatientId patientId = new PatientId(UUID.randomUUID());
        LocalDateTime now = LocalDateTime.now();

        MedicalHistory domain = MedicalHistory.create(
                historyId,
                patientId,
                now,
                now,
                "Alergias",
                "Condiciones",
                "Medicamentos",
                "Observaciones"
        );

        MedicalHistoryEntity entity = MedicalHistoryEntity.fromDomain(domain);
        when(medicalHistoryJpaRepository.findById(historyId.getValue())).thenReturn(Optional.of(entity));

        // When
        Optional<MedicalHistory> result = medicalHistoryJpaAdapter.findById(historyId);

        // Then
        assertThat(result).isPresent();
        assertThat(result.get().getId()).isEqualTo(historyId);
        verify(medicalHistoryJpaRepository).findById(historyId.getValue());
    }

    @Test
    void findById_shouldReturnEmptyWhenNotExists() {
        // Given
        MedicalHistoryId historyId = new MedicalHistoryId(UUID.randomUUID());
        when(medicalHistoryJpaRepository.findById(historyId.getValue())).thenReturn(Optional.empty());

        // When
        Optional<MedicalHistory> result = medicalHistoryJpaAdapter.findById(historyId);

        // Then
        assertThat(result).isEmpty();
        verify(medicalHistoryJpaRepository).findById(historyId.getValue());
    }

    @Test
    void findAll_shouldReturnListOfDomains() {
        // Given
        MedicalHistoryId historyId1 = new MedicalHistoryId(UUID.randomUUID());
        PatientId patientId1 = new PatientId(UUID.randomUUID());
        MedicalHistoryId historyId2 = new MedicalHistoryId(UUID.randomUUID());
        PatientId patientId2 = new PatientId(UUID.randomUUID());
        LocalDateTime now = LocalDateTime.now();

        MedicalHistory domain1 = MedicalHistory.create(
                historyId1,
                patientId1,
                now,
                now,
                "Alergias1",
                "Condiciones1",
                "Medicamentos1",
                "Observaciones1"
        );
        MedicalHistory domain2 = MedicalHistory.create(
                historyId2,
                patientId2,
                now,
                now,
                "Alergias2",
                "Condiciones2",
                "Medicamentos2",
                "Observaciones2"
        );

        MedicalHistoryEntity entity1 = MedicalHistoryEntity.fromDomain(domain1);
        MedicalHistoryEntity entity2 = MedicalHistoryEntity.fromDomain(domain2);
        List<MedicalHistoryEntity> entities = List.of(entity1, entity2);

        when(medicalHistoryJpaRepository.findAll()).thenReturn(entities);

        // When
        List<MedicalHistory> result = medicalHistoryJpaAdapter.findAll();

        // Then
        assertThat(result).hasSize(2);
        assertThat(result.get(0).getId()).isEqualTo(historyId1);
        assertThat(result.get(1).getId()).isEqualTo(historyId2);
        verify(medicalHistoryJpaRepository).findAll();
    }

    @Test
    void deleteById_shouldCallRepositoryDelete() {
        // Given
        MedicalHistoryId historyId = new MedicalHistoryId(UUID.randomUUID());

        // When
        medicalHistoryJpaAdapter.deleteById(historyId);

        // Then
        verify(medicalHistoryJpaRepository).deleteById(historyId.getValue());
    }

    @Test
    void existsById_shouldReturnTrueWhenExists() {
        // Given
        MedicalHistoryId historyId = new MedicalHistoryId(UUID.randomUUID());
        when(medicalHistoryJpaRepository.existsById(historyId.getValue())).thenReturn(true);

        // When
        boolean result = medicalHistoryJpaAdapter.existsById(historyId);

        // Then
        assertThat(result).isTrue();
        verify(medicalHistoryJpaRepository).existsById(historyId.getValue());
    }

    @Test
    void existsById_shouldReturnFalseWhenNotExists() {
        // Given
        MedicalHistoryId historyId = new MedicalHistoryId(UUID.randomUUID());
        when(medicalHistoryJpaRepository.existsById(historyId.getValue())).thenReturn(false);

        // When
        boolean result = medicalHistoryJpaAdapter.existsById(historyId);

        // Then
        assertThat(result).isFalse();
        verify(medicalHistoryJpaRepository).existsById(historyId.getValue());
    }

    @Test
    void findByPatientId_shouldReturnDomainWhenExists() {
        // Given
        PatientId patientId = new PatientId(UUID.randomUUID());
        MedicalHistoryId historyId = new MedicalHistoryId(UUID.randomUUID());
        LocalDateTime now = LocalDateTime.now();

        MedicalHistory domain = MedicalHistory.create(
                historyId,
                patientId,
                now,
                now,
                "Alergias",
                "Condiciones",
                "Medicamentos",
                "Observaciones"
        );

        MedicalHistoryEntity entity = MedicalHistoryEntity.fromDomain(domain);
        when(medicalHistoryJpaRepository.findByPatientId(patientId.getValue())).thenReturn(Optional.of(entity));

        // When
        Optional<MedicalHistory> result = medicalHistoryJpaAdapter.findByPatientId(patientId);

        // Then
        assertThat(result).isPresent();
        assertThat(result.get().getId()).isEqualTo(historyId);
        verify(medicalHistoryJpaRepository).findByPatientId(patientId.getValue());
    }

    @Test
    void findByPatientId_shouldReturnEmptyWhenNotExists() {
        // Given
        PatientId patientId = new PatientId(UUID.randomUUID());
        when(medicalHistoryJpaRepository.findByPatientId(patientId.getValue())).thenReturn(Optional.empty());

        // When
        Optional<MedicalHistory> result = medicalHistoryJpaAdapter.findByPatientId(patientId);

        // Then
        assertThat(result).isEmpty();
        verify(medicalHistoryJpaRepository).findByPatientId(patientId.getValue());
    }
}