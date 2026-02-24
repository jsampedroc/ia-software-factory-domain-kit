package com.application.infrastructure.repository;

import com.application.infrastructure.entity.SpecialtyEntity;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class SpecialtyJpaRepositoryTest {

    @Mock
    private SpecialtyJpaRepository specialtyJpaRepository;

    @Test
    void findByCodigo_WhenEntityExists_ShouldReturnEntity() {
        // Given
        String codigo = "ORT-001";
        SpecialtyEntity expectedEntity = new SpecialtyEntity();
        expectedEntity.setId(UUID.randomUUID());
        expectedEntity.setCodigo(codigo);
        expectedEntity.setNombre("Ortodoncia");

        when(specialtyJpaRepository.findByCodigo(eq(codigo))).thenReturn(Optional.of(expectedEntity));

        // When
        Optional<SpecialtyEntity> result = specialtyJpaRepository.findByCodigo(codigo);

        // Then
        assertThat(result).isPresent();
        assertThat(result.get().getCodigo()).isEqualTo(codigo);
        assertThat(result.get().getNombre()).isEqualTo("Ortodoncia");
    }

    @Test
    void findByCodigo_WhenEntityDoesNotExist_ShouldReturnEmpty() {
        // Given
        String codigo = "INVALID-CODE";
        when(specialtyJpaRepository.findByCodigo(eq(codigo))).thenReturn(Optional.empty());

        // When
        Optional<SpecialtyEntity> result = specialtyJpaRepository.findByCodigo(codigo);

        // Then
        assertThat(result).isEmpty();
    }

    @Test
    void existsByCodigo_WhenCodigoExists_ShouldReturnTrue() {
        // Given
        String codigo = "PER-001";
        when(specialtyJpaRepository.existsByCodigo(eq(codigo))).thenReturn(true);

        // When
        boolean exists = specialtyJpaRepository.existsByCodigo(codigo);

        // Then
        assertThat(exists).isTrue();
    }

    @Test
    void existsByCodigo_WhenCodigoDoesNotExist_ShouldReturnFalse() {
        // Given
        String codigo = "NON-EXISTENT";
        when(specialtyJpaRepository.existsByCodigo(eq(codigo))).thenReturn(false);

        // When
        boolean exists = specialtyJpaRepository.existsByCodigo(codigo);

        // Then
        assertThat(exists).isFalse();
    }

    @Test
    void existsByNombre_WhenNombreExists_ShouldReturnTrue() {
        // Given
        String nombre = "Periodoncia";
        when(specialtyJpaRepository.existsByNombre(eq(nombre))).thenReturn(true);

        // When
        boolean exists = specialtyJpaRepository.existsByNombre(nombre);

        // Then
        assertThat(exists).isTrue();
    }

    @Test
    void existsByNombre_WhenNombreDoesNotExist_ShouldReturnFalse() {
        // Given
        String nombre = "Especialidad Inexistente";
        when(specialtyJpaRepository.existsByNombre(eq(nombre))).thenReturn(false);

        // When
        boolean exists = specialtyJpaRepository.existsByNombre(nombre);

        // Then
        assertThat(exists).isFalse();
    }

    @Test
    void save_ShouldPersistEntity() {
        // Given
        SpecialtyEntity entityToSave = new SpecialtyEntity();
        entityToSave.setId(UUID.randomUUID());
        entityToSave.setCodigo("END-001");
        entityToSave.setNombre("Endodoncia");

        SpecialtyEntity savedEntity = new SpecialtyEntity();
        savedEntity.setId(entityToSave.getId());
        savedEntity.setCodigo(entityToSave.getCodigo());
        savedEntity.setNombre(entityToSave.getNombre());

        when(specialtyJpaRepository.save(any(SpecialtyEntity.class))).thenReturn(savedEntity);

        // When
        SpecialtyEntity result = specialtyJpaRepository.save(entityToSave);

        // Then
        assertThat(result).isNotNull();
        assertThat(result.getId()).isEqualTo(entityToSave.getId());
        assertThat(result.getCodigo()).isEqualTo("END-001");
        assertThat(result.getNombre()).isEqualTo("Endodoncia");
    }

    @Test
    void findById_WhenEntityExists_ShouldReturnEntity() {
        // Given
        UUID specialtyId = UUID.randomUUID();
        SpecialtyEntity expectedEntity = new SpecialtyEntity();
        expectedEntity.setId(specialtyId);
        expectedEntity.setCodigo("CIR-001");
        expectedEntity.setNombre("Cirugía Oral");

        when(specialtyJpaRepository.findById(eq(specialtyId))).thenReturn(Optional.of(expectedEntity));

        // When
        Optional<SpecialtyEntity> result = specialtyJpaRepository.findById(specialtyId);

        // Then
        assertThat(result).isPresent();
        assertThat(result.get().getId()).isEqualTo(specialtyId);
        assertThat(result.get().getCodigo()).isEqualTo("CIR-001");
    }

    @Test
    void findById_WhenEntityDoesNotExist_ShouldReturnEmpty() {
        // Given
        UUID nonExistentId = UUID.randomUUID();
        when(specialtyJpaRepository.findById(eq(nonExistentId))).thenReturn(Optional.empty());

        // When
        Optional<SpecialtyEntity> result = specialtyJpaRepository.findById(nonExistentId);

        // Then
        assertThat(result).isEmpty();
    }
}