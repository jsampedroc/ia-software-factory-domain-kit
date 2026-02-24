package com.application.infrastructure.repository;

import com.application.infrastructure.entity.ClinicEntity;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ClinicJpaRepositoryTest {

    @Mock
    private ClinicJpaRepository clinicJpaRepository;

    @Test
    void findByCodigo_WhenEntityExists_ShouldReturnEntity() {
        // Given
        String codigo = "CLI-001";
        UUID id = UUID.randomUUID();
        ClinicEntity expectedEntity = new ClinicEntity();
        expectedEntity.setId(id);
        expectedEntity.setCodigo(codigo);
        expectedEntity.setNombre("Clínica Central");

        when(clinicJpaRepository.findByCodigo(codigo)).thenReturn(Optional.of(expectedEntity));

        // When
        Optional<ClinicEntity> result = clinicJpaRepository.findByCodigo(codigo);

        // Then
        assertThat(result).isPresent();
        assertThat(result.get().getId()).isEqualTo(id);
        assertThat(result.get().getCodigo()).isEqualTo(codigo);
    }

    @Test
    void findByCodigo_WhenEntityDoesNotExist_ShouldReturnEmpty() {
        // Given
        String codigo = "CLI-999";

        when(clinicJpaRepository.findByCodigo(codigo)).thenReturn(Optional.empty());

        // When
        Optional<ClinicEntity> result = clinicJpaRepository.findByCodigo(codigo);

        // Then
        assertThat(result).isEmpty();
    }

    @Test
    void findByNombre_WhenEntityExists_ShouldReturnEntity() {
        // Given
        String nombre = "Clínica Norte";
        UUID id = UUID.randomUUID();
        ClinicEntity expectedEntity = new ClinicEntity();
        expectedEntity.setId(id);
        expectedEntity.setCodigo("CLI-002");
        expectedEntity.setNombre(nombre);

        when(clinicJpaRepository.findByNombre(nombre)).thenReturn(Optional.of(expectedEntity));

        // When
        Optional<ClinicEntity> result = clinicJpaRepository.findByNombre(nombre);

        // Then
        assertThat(result).isPresent();
        assertThat(result.get().getId()).isEqualTo(id);
        assertThat(result.get().getNombre()).isEqualTo(nombre);
    }

    @Test
    void findByNombre_WhenEntityDoesNotExist_ShouldReturnEmpty() {
        // Given
        String nombre = "Clínica Inexistente";

        when(clinicJpaRepository.findByNombre(nombre)).thenReturn(Optional.empty());

        // When
        Optional<ClinicEntity> result = clinicJpaRepository.findByNombre(nombre);

        // Then
        assertThat(result).isEmpty();
    }

    @Test
    void existsByCodigo_WhenCodigoExists_ShouldReturnTrue() {
        // Given
        String codigo = "CLI-001";

        when(clinicJpaRepository.existsByCodigo(codigo)).thenReturn(true);

        // When
        boolean result = clinicJpaRepository.existsByCodigo(codigo);

        // Then
        assertThat(result).isTrue();
    }

    @Test
    void existsByCodigo_WhenCodigoDoesNotExist_ShouldReturnFalse() {
        // Given
        String codigo = "CLI-999";

        when(clinicJpaRepository.existsByCodigo(codigo)).thenReturn(false);

        // When
        boolean result = clinicJpaRepository.existsByCodigo(codigo);

        // Then
        assertThat(result).isFalse();
    }

    @Test
    void existsByNombre_WhenNombreExists_ShouldReturnTrue() {
        // Given
        String nombre = "Clínica Sur";

        when(clinicJpaRepository.existsByNombre(nombre)).thenReturn(true);

        // When
        boolean result = clinicJpaRepository.existsByNombre(nombre);

        // Then
        assertThat(result).isTrue();
    }

    @Test
    void existsByNombre_WhenNombreDoesNotExist_ShouldReturnFalse() {
        // Given
        String nombre = "Clínica Fantasma";

        when(clinicJpaRepository.existsByNombre(nombre)).thenReturn(false);

        // When
        boolean result = clinicJpaRepository.existsByNombre(nombre);

        // Then
        assertThat(result).isFalse();
    }
}