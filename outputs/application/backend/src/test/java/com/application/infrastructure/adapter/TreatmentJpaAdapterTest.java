package com.application.infrastructure.adapter;

import com.application.domain.model.Treatment;
import com.application.domain.valueobject.TreatmentId;
import com.application.infrastructure.entity.TreatmentEntity;
import com.application.infrastructure.repository.TreatmentJpaRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TreatmentJpaAdapterTest {

    @Mock
    private TreatmentJpaRepository treatmentJpaRepository;

    @InjectMocks
    private TreatmentJpaAdapter treatmentJpaAdapter;

    @Test
    void save_ShouldSaveAndReturnDomainTreatment() {
        // Given
        TreatmentId treatmentId = new TreatmentId(UUID.randomUUID());
        Treatment treatment = Treatment.create(
                treatmentId,
                "TR001",
                "Limpieza Dental",
                "Limpieza profesional de dientes",
                45,
                new BigDecimal("50.00"),
                true
        );
        TreatmentEntity entity = TreatmentEntity.fromDomain(treatment);
        when(treatmentJpaRepository.save(any(TreatmentEntity.class))).thenReturn(entity);

        // When
        Treatment savedTreatment = treatmentJpaAdapter.save(treatment);

        // Then
        assertThat(savedTreatment).isNotNull();
        assertThat(savedTreatment.getId()).isEqualTo(treatmentId);
        assertThat(savedTreatment.getCodigo()).isEqualTo("TR001");
        verify(treatmentJpaRepository).save(any(TreatmentEntity.class));
    }

    @Test
    void findById_WhenTreatmentExists_ShouldReturnTreatment() {
        // Given
        TreatmentId treatmentId = new TreatmentId(UUID.randomUUID());
        TreatmentEntity entity = mock(TreatmentEntity.class);
        Treatment expectedTreatment = mock(Treatment.class);
        when(treatmentJpaRepository.findById(treatmentId.getValue())).thenReturn(Optional.of(entity));
        when(entity.toDomain()).thenReturn(expectedTreatment);

        // When
        Optional<Treatment> result = treatmentJpaAdapter.findById(treatmentId);

        // Then
        assertThat(result).isPresent().contains(expectedTreatment);
        verify(treatmentJpaRepository).findById(treatmentId.getValue());
    }

    @Test
    void findById_WhenTreatmentDoesNotExist_ShouldReturnEmpty() {
        // Given
        TreatmentId treatmentId = new TreatmentId(UUID.randomUUID());
        when(treatmentJpaRepository.findById(treatmentId.getValue())).thenReturn(Optional.empty());

        // When
        Optional<Treatment> result = treatmentJpaAdapter.findById(treatmentId);

        // Then
        assertThat(result).isEmpty();
        verify(treatmentJpaRepository).findById(treatmentId.getValue());
    }

    @Test
    void findAll_ShouldReturnAllTreatments() {
        // Given
        TreatmentEntity entity1 = mock(TreatmentEntity.class);
        TreatmentEntity entity2 = mock(TreatmentEntity.class);
        Treatment treatment1 = mock(Treatment.class);
        Treatment treatment2 = mock(Treatment.class);
        when(treatmentJpaRepository.findAll()).thenReturn(List.of(entity1, entity2));
        when(entity1.toDomain()).thenReturn(treatment1);
        when(entity2.toDomain()).thenReturn(treatment2);

        // When
        List<Treatment> result = treatmentJpaAdapter.findAll();

        // Then
        assertThat(result).hasSize(2).containsExactly(treatment1, treatment2);
        verify(treatmentJpaRepository).findAll();
    }

    @Test
    void findAllActive_ShouldReturnActiveTreatments() {
        // Given
        TreatmentEntity entity1 = mock(TreatmentEntity.class);
        TreatmentEntity entity2 = mock(TreatmentEntity.class);
        Treatment treatment1 = mock(Treatment.class);
        Treatment treatment2 = mock(Treatment.class);
        when(treatmentJpaRepository.findByActivoTrue()).thenReturn(List.of(entity1, entity2));
        when(entity1.toDomain()).thenReturn(treatment1);
        when(entity2.toDomain()).thenReturn(treatment2);

        // When
        List<Treatment> result = treatmentJpaAdapter.findAllActive();

        // Then
        assertThat(result).hasSize(2).containsExactly(treatment1, treatment2);
        verify(treatmentJpaRepository).findByActivoTrue();
    }

    @Test
    void deleteById_ShouldCallRepositoryDelete() {
        // Given
        TreatmentId treatmentId = new TreatmentId(UUID.randomUUID());

        // When
        treatmentJpaAdapter.deleteById(treatmentId);

        // Then
        verify(treatmentJpaRepository).deleteById(treatmentId.getValue());
    }

    @Test
    void existsById_WhenTreatmentExists_ShouldReturnTrue() {
        // Given
        TreatmentId treatmentId = new TreatmentId(UUID.randomUUID());
        when(treatmentJpaRepository.existsById(treatmentId.getValue())).thenReturn(true);

        // When
        boolean result = treatmentJpaAdapter.existsById(treatmentId);

        // Then
        assertThat(result).isTrue();
        verify(treatmentJpaRepository).existsById(treatmentId.getValue());
    }

    @Test
    void existsById_WhenTreatmentDoesNotExist_ShouldReturnFalse() {
        // Given
        TreatmentId treatmentId = new TreatmentId(UUID.randomUUID());
        when(treatmentJpaRepository.existsById(treatmentId.getValue())).thenReturn(false);

        // When
        boolean result = treatmentJpaAdapter.existsById(treatmentId);

        // Then
        assertThat(result).isFalse();
        verify(treatmentJpaRepository).existsById(treatmentId.getValue());
    }

    @Test
    void findByCode_WhenTreatmentExists_ShouldReturnTreatment() {
        // Given
        String code = "TR001";
        TreatmentEntity entity = mock(TreatmentEntity.class);
        Treatment expectedTreatment = mock(Treatment.class);
        when(treatmentJpaRepository.findByCodigo(code)).thenReturn(Optional.of(entity));
        when(entity.toDomain()).thenReturn(expectedTreatment);

        // When
        Optional<Treatment> result = treatmentJpaAdapter.findByCode(code);

        // Then
        assertThat(result).isPresent().contains(expectedTreatment);
        verify(treatmentJpaRepository).findByCodigo(code);
    }

    @Test
    void findByCode_WhenTreatmentDoesNotExist_ShouldReturnEmpty() {
        // Given
        String code = "NONEXISTENT";
        when(treatmentJpaRepository.findByCodigo(code)).thenReturn(Optional.empty());

        // When
        Optional<Treatment> result = treatmentJpaAdapter.findByCode(code);

        // Then
        assertThat(result).isEmpty();
        verify(treatmentJpaRepository).findByCodigo(code);
    }
}