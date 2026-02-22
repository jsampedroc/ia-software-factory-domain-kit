package com.application.infrastructure.persistence.jpa.alumno;

import com.application.domain.model.alumno.Alumno;
import com.application.domain.model.alumno.Tutor;
import com.application.domain.valueobject.alumno.DocumentoIdentidad;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.List;
import java.util.Set;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AlumnoJpaEntityTest {

    @Mock
    private TutorJpaEntity tutorJpaEntityMock1;
    @Mock
    private TutorJpaEntity tutorJpaEntityMock2;

    private AlumnoJpaEntity alumnoJpaEntity;

    @BeforeEach
    void setUp() {
        alumnoJpaEntity = new AlumnoJpaEntity();
    }

    @Test
    void shouldMapFromDomainEntityCorrectly() {
        Alumno.AlumnoId domainId = new Alumno.AlumnoId(UUID.randomUUID());
        Tutor.TutorId tutorId1 = new Tutor.TutorId(UUID.randomUUID());
        Tutor.TutorId tutorId2 = new Tutor.TutorId(UUID.randomUUID());

        Tutor tutor1 = Tutor.builder()
                .id(tutorId1)
                .nombre("TutorNombre1")
                .apellidos("TutorApellido1")
                .documentoIdentidad(new DocumentoIdentidad("DNI", "12345678A", "ES"))
                .telefono("600111222")
                .email("tutor1@email.com")
                .relacionConAlumno("Padre")
                .build();

        Tutor tutor2 = Tutor.builder()
                .id(tutorId2)
                .nombre("TutorNombre2")
                .apellidos("TutorApellido2")
                .documentoIdentidad(new DocumentoIdentidad("DNI", "87654321B", "ES"))
                .telefono("600333444")
                .email("tutor2@email.com")
                .relacionConAlumno("Madre")
                .build();

        Alumno domainAlumno = Alumno.builder()
                .id(domainId)
                .numeroMatricula("MAT-2024-001")
                .nombre("Juan")
                .apellidos("Pérez García")
                .fechaNacimiento(LocalDate.of(2015, 5, 10))
                .fechaAlta(LocalDate.of(2024, 9, 1))
                .activo(true)
                .tutores(Set.of(tutor1, tutor2))
                .build();

        when(tutorJpaEntityMock1.getId()).thenReturn(tutorId1.value());
        when(tutorJpaEntityMock2.getId()).thenReturn(tutorId2.value());

        AlumnoJpaEntity result = AlumnoJpaEntity.fromDomainEntity(domainAlumno, List.of(tutorJpaEntityMock1, tutorJpaEntityMock2));

        assertThat(result.getId()).isEqualTo(domainId.value());
        assertThat(result.getNumeroMatricula()).isEqualTo("MAT-2024-001");
        assertThat(result.getNombre()).isEqualTo("Juan");
        assertThat(result.getApellidos()).isEqualTo("Pérez García");
        assertThat(result.getFechaNacimiento()).isEqualTo(LocalDate.of(2015, 5, 10));
        assertThat(result.getFechaAlta()).isEqualTo(LocalDate.of(2024, 9, 1));
        assertThat(result.isActivo()).isTrue();
        assertThat(result.getTutores()).containsExactlyInAnyOrder(tutorJpaEntityMock1, tutorJpaEntityMock2);
    }

    @Test
    void shouldMapToDomainEntityCorrectly() {
        UUID alumnoUuid = UUID.randomUUID();
        UUID tutorUuid1 = UUID.randomUUID();
        UUID tutorUuid2 = UUID.randomUUID();

        alumnoJpaEntity.setId(alumnoUuid);
        alumnoJpaEntity.setNumeroMatricula("MAT-2024-002");
        alumnoJpaEntity.setNombre("Ana");
        alumnoJpaEntity.setApellidos("López Martínez");
        alumnoJpaEntity.setFechaNacimiento(LocalDate.of(2016, 8, 15));
        alumnoJpaEntity.setFechaAlta(LocalDate.of(2024, 9, 1));
        alumnoJpaEntity.setActivo(true);

        TutorJpaEntity tutorJpa1 = new TutorJpaEntity();
        tutorJpa1.setId(tutorUuid1);
        tutorJpa1.setNombre("Tutor1");
        tutorJpa1.setApellidos("Apellido1");
        tutorJpa1.setTipoDocumento("DNI");
        tutorJpa1.setNumeroDocumento("11111111A");
        tutorJpa1.setPaisEmisionDocumento("ES");
        tutorJpa1.setTelefono("611222333");
        tutorJpa1.setEmail("tutor1@test.com");
        tutorJpa1.setRelacionConAlumno("Padre");

        TutorJpaEntity tutorJpa2 = new TutorJpaEntity();
        tutorJpa2.setId(tutorUuid2);
        tutorJpa2.setNombre("Tutor2");
        tutorJpa2.setApellidos("Apellido2");
        tutorJpa2.setTipoDocumento("DNI");
        tutorJpa2.setNumeroDocumento("22222222B");
        tutorJpa2.setPaisEmisionDocumento("ES");
        tutorJpa2.setTelefono("622333444");
        tutorJpa2.setEmail("tutor2@test.com");
        tutorJpa2.setRelacionConAlumno("Madre");

        alumnoJpaEntity.setTutores(List.of(tutorJpa1, tutorJpa2));

        Alumno result = alumnoJpaEntity.toDomainEntity();

        assertThat(result.getId().value()).isEqualTo(alumnoUuid);
        assertThat(result.getNumeroMatricula()).isEqualTo("MAT-2024-002");
        assertThat(result.getNombre()).isEqualTo("Ana");
        assertThat(result.getApellidos()).isEqualTo("López Martínez");
        assertThat(result.getFechaNacimiento()).isEqualTo(LocalDate.of(2016, 8, 15));
        assertThat(result.getFechaAlta()).isEqualTo(LocalDate.of(2024, 9, 1));
        assertThat(result.isActivo()).isTrue();
        assertThat(result.getTutores()).hasSize(2);

        List<Tutor> tutorList = result.getTutores().stream().toList();
        assertThat(tutorList.get(0).getId().value()).isEqualTo(tutorUuid1);
        assertThat(tutorList.get(0).getNombre()).isEqualTo("Tutor1");
        assertThat(tutorList.get(1).getId().value()).isEqualTo(tutorUuid2);
        assertThat(tutorList.get(1).getNombre()).isEqualTo("Tutor2");
    }

    @Test
    void shouldHandleInactiveAlumnoCorrectly() {
        UUID alumnoUuid = UUID.randomUUID();
        alumnoJpaEntity.setId(alumnoUuid);
        alumnoJpaEntity.setNumeroMatricula("MAT-2024-003");
        alumnoJpaEntity.setNombre("Inactivo");
        alumnoJpaEntity.setApellidos("Test");
        alumnoJpaEntity.setFechaNacimiento(LocalDate.of(2014, 1, 1));
        alumnoJpaEntity.setFechaAlta(LocalDate.of(2023, 9, 1));
        alumnoJpaEntity.setActivo(false);
        alumnoJpaEntity.setTutores(List.of());

        Alumno result = alumnoJpaEntity.toDomainEntity();

        assertThat(result.isActivo()).isFalse();
        assertThat(result.getTutores()).isEmpty();
    }

    @Test
    void shouldHandleEmptyTutorListCorrectly() {
        UUID alumnoUuid = UUID.randomUUID();
        alumnoJpaEntity.setId(alumnoUuid);
        alumnoJpaEntity.setNumeroMatricula("MAT-2024-004");
        alumnoJpaEntity.setNombre("SinTutor");
        alumnoJpaEntity.setApellidos("Apellido");
        alumnoJpaEntity.setFechaNacimiento(LocalDate.of(2017, 3, 20));
        alumnoJpaEntity.setFechaAlta(LocalDate.of(2024, 9, 1));
        alumnoJpaEntity.setActivo(true);
        alumnoJpaEntity.setTutores(List.of());

        Alumno result = alumnoJpaEntity.toDomainEntity();

        assertThat(result.getTutores()).isEmpty();
    }
}