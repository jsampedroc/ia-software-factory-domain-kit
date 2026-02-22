package com.application.infrastructure.persistence.adapter;

import com.application.domain.model.alumno.Alumno;
import com.application.domain.model.alumno.Tutor;
import com.application.domain.valueobject.alumno.DocumentoIdentidad;
import com.application.infrastructure.persistence.jpa.alumno.AlumnoJpaEntity;
import com.application.infrastructure.persistence.jpa.alumno.AlumnoJpaRepository;
import com.application.infrastructure.persistence.jpa.alumno.TutorJpaEntity;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AlumnoPersistenceAdapterTest {

    @Mock
    private AlumnoJpaRepository alumnoJpaRepository;

    @InjectMocks
    private AlumnoPersistenceAdapter underTest;

    private AlumnoJpaEntity alumnoJpaEntity;
    private Alumno alumnoDomain;

    @BeforeEach
    void setUp() {
        UUID alumnoId = UUID.randomUUID();
        UUID tutorId = UUID.randomUUID();

        TutorJpaEntity tutorJpa = new TutorJpaEntity();
        tutorJpa.setId(tutorId);
        tutorJpa.setNombre("Juan");
        tutorJpa.setApellidos("Pérez");
        tutorJpa.setDocumentoIdentidadTipo("DNI");
        tutorJpa.setDocumentoIdentidadNumero("12345678A");
        tutorJpa.setDocumentoIdentidadPaisEmision("ES");
        tutorJpa.setTelefono("600123456");
        tutorJpa.setEmail("juan@email.com");
        tutorJpa.setRelacionConAlumno("Padre");

        alumnoJpaEntity = new AlumnoJpaEntity();
        alumnoJpaEntity.setId(alumnoId);
        alumnoJpaEntity.setNumeroMatricula("MAT-2024-001");
        alumnoJpaEntity.setNombre("Carlos");
        alumnoJpaEntity.setApellidos("Gómez");
        alumnoJpaEntity.setFechaNacimiento(LocalDate.of(2015, 5, 10));
        alumnoJpaEntity.setFechaAlta(LocalDate.of(2024, 9, 1));
        alumnoJpaEntity.setActivo(true);
        alumnoJpaEntity.setTutores(Set.of(tutorJpa));
        tutorJpa.setAlumno(alumnoJpaEntity);

        Tutor tutorDomain = Tutor.builder()
                .id(new Tutor.TutorId(tutorId))
                .nombre("Juan")
                .apellidos("Pérez")
                .documentoIdentidad(new DocumentoIdentidad("DNI", "12345678A", "ES"))
                .telefono("600123456")
                .email("juan@email.com")
                .relacionConAlumno("Padre")
                .build();

        alumnoDomain = Alumno.builder()
                .id(new Alumno.AlumnoId(alumnoId))
                .numeroMatricula("MAT-2024-001")
                .nombre("Carlos")
                .apellidos("Gómez")
                .fechaNacimiento(LocalDate.of(2015, 5, 10))
                .fechaAlta(LocalDate.of(2024, 9, 1))
                .activo(true)
                .tutores(Set.of(tutorDomain))
                .build();
    }

    @Test
    void save_ShouldSaveAlumnoAndReturnDomainEntity() {
        when(alumnoJpaRepository.save(any(AlumnoJpaEntity.class))).thenReturn(alumnoJpaEntity);

        Alumno savedAlumno = underTest.save(alumnoDomain);

        assertThat(savedAlumno).isNotNull();
        assertThat(savedAlumno.getId().value()).isEqualTo(alumnoJpaEntity.getId());
        assertThat(savedAlumno.getNumeroMatricula()).isEqualTo(alumnoJpaEntity.getNumeroMatricula());
        verify(alumnoJpaRepository, times(1)).save(any(AlumnoJpaEntity.class));
    }

    @Test
    void findById_WhenAlumnoExists_ShouldReturnAlumno() {
        when(alumnoJpaRepository.findById(alumnoJpaEntity.getId())).thenReturn(Optional.of(alumnoJpaEntity));

        Optional<Alumno> result = underTest.findById(new Alumno.AlumnoId(alumnoJpaEntity.getId()));

        assertThat(result).isPresent();
        assertThat(result.get().getId().value()).isEqualTo(alumnoJpaEntity.getId());
        verify(alumnoJpaRepository, times(1)).findById(alumnoJpaEntity.getId());
    }

    @Test
    void findById_WhenAlumnoDoesNotExist_ShouldReturnEmpty() {
        UUID nonExistentId = UUID.randomUUID();
        when(alumnoJpaRepository.findById(nonExistentId)).thenReturn(Optional.empty());

        Optional<Alumno> result = underTest.findById(new Alumno.AlumnoId(nonExistentId));

        assertThat(result).isEmpty();
        verify(alumnoJpaRepository, times(1)).findById(nonExistentId);
    }

    @Test
    void findByNumeroMatricula_WhenAlumnoExists_ShouldReturnAlumno() {
        String matricula = "MAT-2024-001";
        when(alumnoJpaRepository.findByNumeroMatricula(matricula)).thenReturn(Optional.of(alumnoJpaEntity));

        Optional<Alumno> result = underTest.findByNumeroMatricula(matricula);

        assertThat(result).isPresent();
        assertThat(result.get().getNumeroMatricula()).isEqualTo(matricula);
        verify(alumnoJpaRepository, times(1)).findByNumeroMatricula(matricula);
    }

    @Test
    void findByNumeroMatricula_WhenAlumnoDoesNotExist_ShouldReturnEmpty() {
        String matricula = "NON-EXISTENT";
        when(alumnoJpaRepository.findByNumeroMatricula(matricula)).thenReturn(Optional.empty());

        Optional<Alumno> result = underTest.findByNumeroMatricula(matricula);

        assertThat(result).isEmpty();
        verify(alumnoJpaRepository, times(1)).findByNumeroMatricula(matricula);
    }

    @Test
    void findAllActivos_ShouldReturnListOfActiveAlumnos() {
        when(alumnoJpaRepository.findByActivoTrue()).thenReturn(List.of(alumnoJpaEntity));

        List<Alumno> result = underTest.findAllActivos();

        assertThat(result).hasSize(1);
        assertThat(result.get(0).isActivo()).isTrue();
        verify(alumnoJpaRepository, times(1)).findByActivoTrue();
    }

    @Test
    void existsByNumeroMatricula_WhenExists_ShouldReturnTrue() {
        String matricula = "MAT-2024-001";
        when(alumnoJpaRepository.existsByNumeroMatricula(matricula)).thenReturn(true);

        boolean result = underTest.existsByNumeroMatricula(matricula);

        assertThat(result).isTrue();
        verify(alumnoJpaRepository, times(1)).existsByNumeroMatricula(matricula);
    }

    @Test
    void existsByNumeroMatricula_WhenNotExists_ShouldReturnFalse() {
        String matricula = "MAT-2024-999";
        when(alumnoJpaRepository.existsByNumeroMatricula(matricula)).thenReturn(false);

        boolean result = underTest.existsByNumeroMatricula(matricula);

        assertThat(result).isFalse();
        verify(alumnoJpaRepository, times(1)).existsByNumeroMatricula(matricula);
    }

    @Test
    void delete_ShouldCallRepositoryDeleteById() {
        UUID idToDelete = alumnoJpaEntity.getId();
        Alumno.AlumnoId domainId = new Alumno.AlumnoId(idToDelete);

        underTest.delete(domainId);

        verify(alumnoJpaRepository, times(1)).deleteById(idToDelete);
    }
}