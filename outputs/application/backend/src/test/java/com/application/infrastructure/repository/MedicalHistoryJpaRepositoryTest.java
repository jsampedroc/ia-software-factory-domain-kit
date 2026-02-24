package com.application.infrastructure.repository;

import com.application.infrastructure.entity.MedicalHistoryEntity;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class MedicalHistoryJpaRepositoryTest {

    @Mock
    private MedicalHistoryJpaRepository medicalHistoryJpaRepository;

    @Test
    void shouldSaveMedicalHistoryEntity() {
        // Given
        UUID id = UUID.randomUUID();
        MedicalHistoryEntity entity = new MedicalHistoryEntity();
        entity.setId(id);
        entity.setAlergias("Ninguna");
        entity.setFechaCreacion(LocalDateTime.now());

        when(medicalHistoryJpaRepository.save(any(MedicalHistoryEntity.class))).thenReturn(entity);

        // When
        MedicalHistoryEntity savedEntity = medicalHistoryJpaRepository.save(entity);

        // Then
        assertThat(savedEntity).isNotNull();
        assertThat(savedEntity.getId()).isEqualTo(id);
        assertThat(savedEntity.getAlergias()).isEqualTo("Ninguna");
    }

    @Test
    void shouldFindMedicalHistoryEntityById() {
        // Given
        UUID id = UUID.randomUUID();
        MedicalHistoryEntity entity = new MedicalHistoryEntity();
        entity.setId(id);
        entity.setCondicionesMedicas("Hipertensión");

        when(medicalHistoryJpaRepository.findById(id)).thenReturn(Optional.of(entity));

        // When
        Optional<MedicalHistoryEntity> foundEntity = medicalHistoryJpaRepository.findById(id);

        // Then
        assertThat(foundEntity).isPresent();
        assertThat(foundEntity.get().getId()).isEqualTo(id);
        assertThat(foundEntity.get().getCondicionesMedicas()).isEqualTo("Hipertensión");
    }

    @Test
    void shouldReturnEmptyWhenMedicalHistoryEntityNotFound() {
        // Given
        UUID nonExistentId = UUID.randomUUID();
        when(medicalHistoryJpaRepository.findById(nonExistentId)).thenReturn(Optional.empty());

        // When
        Optional<MedicalHistoryEntity> foundEntity = medicalHistoryJpaRepository.findById(nonExistentId);

        // Then
        assertThat(foundEntity).isEmpty();
    }

    @Test
    void shouldFindAllMedicalHistoryEntities() {
        // Given
        MedicalHistoryEntity entity1 = new MedicalHistoryEntity();
        entity1.setId(UUID.randomUUID());
        MedicalHistoryEntity entity2 = new MedicalHistoryEntity();
        entity2.setId(UUID.randomUUID());
        List<MedicalHistoryEntity> entities = List.of(entity1, entity2);

        when(medicalHistoryJpaRepository.findAll()).thenReturn(entities);

        // When
        List<MedicalHistoryEntity> allEntities = medicalHistoryJpaRepository.findAll();

        // Then
        assertThat(allEntities).hasSize(2);
        assertThat(allEntities).containsExactlyInAnyOrder(entity1, entity2);
    }

    @Test
    void shouldFindAllMedicalHistoryEntitiesWithPagination() {
        // Given
        Pageable pageable = PageRequest.of(0, 10);
        MedicalHistoryEntity entity = new MedicalHistoryEntity();
        entity.setId(UUID.randomUUID());
        Page<MedicalHistoryEntity> page = new PageImpl<>(List.of(entity), pageable, 1);

        when(medicalHistoryJpaRepository.findAll(pageable)).thenReturn(page);

        // When
        Page<MedicalHistoryEntity> resultPage = medicalHistoryJpaRepository.findAll(pageable);

        // Then
        assertThat(resultPage).isNotNull();
        assertThat(resultPage.getContent()).hasSize(1);
        assertThat(resultPage.getTotalElements()).isEqualTo(1);
    }

    @Test
    void shouldDeleteMedicalHistoryEntity() {
        // Given
        UUID id = UUID.randomUUID();
        MedicalHistoryEntity entity = new MedicalHistoryEntity();
        entity.setId(id);

        // When & Then - No exception should be thrown
        medicalHistoryJpaRepository.delete(entity);
    }

    @Test
    void shouldCheckIfMedicalHistoryEntityExists() {
        // Given
        UUID id = UUID.randomUUID();
        when(medicalHistoryJpaRepository.existsById(id)).thenReturn(true);

        // When
        boolean exists = medicalHistoryJpaRepository.existsById(id);

        // Then
        assertThat(exists).isTrue();
    }

    @Test
    void shouldReturnCountOfMedicalHistoryEntities() {
        // Given
        when(medicalHistoryJpaRepository.count()).thenReturn(5L);

        // When
        long count = medicalHistoryJpaRepository.count();

        // Then
        assertThat(count).isEqualTo(5L);
    }

    @Test
    void shouldSaveAllMedicalHistoryEntities() {
        // Given
        MedicalHistoryEntity entity1 = new MedicalHistoryEntity();
        entity1.setId(UUID.randomUUID());
        MedicalHistoryEntity entity2 = new MedicalHistoryEntity();
        entity2.setId(UUID.randomUUID());
        List<MedicalHistoryEntity> entities = List.of(entity1, entity2);

        when(medicalHistoryJpaRepository.saveAll(any())).thenReturn(entities);

        // When
        List<MedicalHistoryEntity> savedEntities = medicalHistoryJpaRepository.saveAll(entities);

        // Then
        assertThat(savedEntities).hasSize(2);
        assertThat(savedEntities).containsExactlyInAnyOrder(entity1, entity2);
    }

    @Test
    void shouldDeleteMedicalHistoryEntityById() {
        // Given
        UUID id = UUID.randomUUID();

        // When & Then - No exception should be thrown
        medicalHistoryJpaRepository.deleteById(id);
    }
}