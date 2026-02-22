package com.application;

import com.application.infrastructure.persistence.jpa.alumno.AlumnoJpaEntity;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AlumnoJpaRepositoryTest {

    @Mock
    private AlumnoJpaRepository alumnoJpaRepository;

    private AlumnoJpaEntity alumnoJpaEntity;
    private final UUID testId = UUID.randomUUID();
    private final String testNumeroMatricula = "MAT-2024-001";

    @BeforeEach
    void setUp() {
        alumnoJpaEntity = new AlumnoJpaEntity();
        alumnoJpaEntity.setId(testId);
        alumnoJpaEntity.setNumeroMatricula(testNumeroMatricula);
        alumnoJpaEntity.setNombre("Juan");
        alumnoJpaEntity.setApellidos("Pérez López");
        alumnoJpaEntity.setFechaNacimiento(LocalDate.of(2015, 5, 10));
        alumnoJpaEntity.setFechaAlta(LocalDate.now());
        alumnoJpaEntity.setActivo(true);
    }

    @Test
    void testFindByNumeroMatricula() {
        when(alumnoJpaRepository.findByNumeroMatricula(testNumeroMatricula)).thenReturn(Optional.of(alumnoJpaEntity));

        Optional<AlumnoJpaEntity> result = alumnoJpaRepository.findByNumeroMatricula(testNumeroMatricula);

        assertThat(result).isPresent();
        assertThat(result.get().getNumeroMatricula()).isEqualTo(testNumeroMatricula);
        verify(alumnoJpaRepository, times(1)).findByNumeroMatricula(testNumeroMatricula);
    }

    @Test
    void testFindByNumeroMatricula_NotFound() {
        when(alumnoJpaRepository.findByNumeroMatricula("INVALIDO")).thenReturn(Optional.empty());

        Optional<AlumnoJpaEntity> result = alumnoJpaRepository.findByNumeroMatricula("INVALIDO");

        assertThat(result).isEmpty();
        verify(alumnoJpaRepository, times(1)).findByNumeroMatricula("INVALIDO");
    }

    @Test
    void testFindByActivoTrue() {
        Pageable pageable = PageRequest.of(0, 10);
        Page<AlumnoJpaEntity> page = new PageImpl<>(List.of(alumnoJpaEntity), pageable, 1);
        when(alumnoJpaRepository.findByActivoTrue(pageable)).thenReturn(page);

        Page<AlumnoJpaEntity> result = alumnoJpaRepository.findByActivoTrue(pageable);

        assertThat(result).isNotNull();
        assertThat(result.getContent()).hasSize(1);
        assertThat(result.getContent().get(0).isActivo()).isTrue();
        verify(alumnoJpaRepository, times(1)).findByActivoTrue(pageable);
    }

    @Test
    void testFindByActivoFalse() {
        alumnoJpaEntity.setActivo(false);
        Pageable pageable = PageRequest.of(0, 10);
        Page<AlumnoJpaEntity> page = new PageImpl<>(List.of(alumnoJpaEntity), pageable, 1);
        when(alumnoJpaRepository.findByActivoFalse(pageable)).thenReturn(page);

        Page<AlumnoJpaEntity> result = alumnoJpaRepository.findByActivoFalse(pageable);

        assertThat(result).isNotNull();
        assertThat(result.getContent()).hasSize(1);
        assertThat(result.getContent().get(0).isActivo()).isFalse();
        verify(alumnoJpaRepository, times(1)).findByActivoFalse(pageable);
    }

    @Test
    void testExistsByNumeroMatricula() {
        when(alumnoJpaRepository.existsByNumeroMatricula(testNumeroMatricula)).thenReturn(true);
        when(alumnoJpaRepository.existsByNumeroMatricula("OTRO")).thenReturn(false);

        boolean existsTrue = alumnoJpaRepository.existsByNumeroMatricula(testNumeroMatricula);
        boolean existsFalse = alumnoJpaRepository.existsByNumeroMatricula("OTRO");

        assertThat(existsTrue).isTrue();
        assertThat(existsFalse).isFalse();
        verify(alumnoJpaRepository, times(1)).existsByNumeroMatricula(testNumeroMatricula);
        verify(alumnoJpaRepository, times(1)).existsByNumeroMatricula("OTRO");
    }

    @Test
    void testSave() {
        when(alumnoJpaRepository.save(any(AlumnoJpaEntity.class))).thenReturn(alumnoJpaEntity);

        AlumnoJpaEntity savedEntity = alumnoJpaRepository.save(alumnoJpaEntity);

        assertThat(savedEntity).isNotNull();
        assertThat(savedEntity.getId()).isEqualTo(testId);
        verify(alumnoJpaRepository, times(1)).save(alumnoJpaEntity);
    }

    @Test
    void testFindById() {
        when(alumnoJpaRepository.findById(testId)).thenReturn(Optional.of(alumnoJpaEntity));

        Optional<AlumnoJpaEntity> result = alumnoJpaRepository.findById(testId);

        assertThat(result).isPresent();
        assertThat(result.get().getId()).isEqualTo(testId);
        verify(alumnoJpaRepository, times(1)).findById(testId);
    }

    @Test
    void testFindById_NotFound() {
        UUID nonExistentId = UUID.randomUUID();
        when(alumnoJpaRepository.findById(nonExistentId)).thenReturn(Optional.empty());

        Optional<AlumnoJpaEntity> result = alumnoJpaRepository.findById(nonExistentId);

        assertThat(result).isEmpty();
        verify(alumnoJpaRepository, times(1)).findById(nonExistentId);
    }

    @Test
    void testDeleteById() {
        doNothing().when(alumnoJpaRepository).deleteById(testId);

        alumnoJpaRepository.deleteById(testId);

        verify(alumnoJpaRepository, times(1)).deleteById(testId);
    }

    @Test
    void testFindAll() {
        Pageable pageable = PageRequest.of(0, 10);
        Page<AlumnoJpaEntity> page = new PageImpl<>(List.of(alumnoJpaEntity), pageable, 1);
        when(alumnoJpaRepository.findAll(pageable)).thenReturn(page);

        Page<AlumnoJpaEntity> result = alumnoJpaRepository.findAll(pageable);

        assertThat(result).isNotNull();
        assertThat(result.getContent()).hasSize(1);
        verify(alumnoJpaRepository, times(1)).findAll(pageable);
    }
}