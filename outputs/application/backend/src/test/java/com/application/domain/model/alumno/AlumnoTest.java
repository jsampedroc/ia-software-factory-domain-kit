package com.application.domain.model.alumno;

import com.application.domain.model.event.AlumnoMatriculado;
import com.application.domain.shared.Entity;
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
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AlumnoTest {

    private Alumno.AlumnoId alumnoId;
    private String numeroMatricula;
    private String nombre;
    private String apellidos;
    private LocalDate fechaNacimiento;
    private LocalDate fechaAlta;
    @Mock
    private Tutor tutorMock1;
    @Mock
    private Tutor tutorMock2;

    @BeforeEach
    void setUp() {
        alumnoId = new Alumno.AlumnoId(UUID.randomUUID());
        numeroMatricula = "MAT-2024-001";
        nombre = "Juan";
        apellidos = "Pérez López";
        fechaNacimiento = LocalDate.of(2015, 6, 10);
        fechaAlta = LocalDate.now();
    }

    @Test
    void crearAlumno_ConDatosValidos_DeberiaCrearInstancia() {
        Alumno alumno = Alumno.crear(
                alumnoId,
                numeroMatricula,
                nombre,
                apellidos,
                fechaNacimiento,
                fechaAlta,
                Set.of(tutorMock1, tutorMock2)
        );

        assertThat(alumno).isNotNull();
        assertThat(alumno.getId()).isEqualTo(alumnoId);
        assertThat(alumno.getNumeroMatricula()).isEqualTo(numeroMatricula);
        assertThat(alumno.getNombre()).isEqualTo(nombre);
        assertThat(alumno.getApellidos()).isEqualTo(apellidos);
        assertThat(alumno.getFechaNacimiento()).isEqualTo(fechaNacimiento);
        assertThat(alumno.getFechaAlta()).isEqualTo(fechaAlta);
        assertThat(alumno.isActivo()).isTrue();
        assertThat(alumno.getTutores()).containsExactlyInAnyOrder(tutorMock1, tutorMock2);
    }

    @Test
    void crearAlumno_ConFechaAltaFutura_DeberiaLanzarExcepcion() {
        LocalDate fechaAltaFutura = LocalDate.now().plusDays(1);

        assertThatThrownBy(() -> Alumno.crear(
                alumnoId,
                numeroMatricula,
                nombre,
                apellidos,
                fechaNacimiento,
                fechaAltaFutura,
                Set.of(tutorMock1)
        )).isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("fecha de alta");
    }

    @Test
    void crearAlumno_SinTutores_DeberiaLanzarExcepcion() {
        assertThatThrownBy(() -> Alumno.crear(
                alumnoId,
                numeroMatricula,
                nombre,
                apellidos,
                fechaNacimiento,
                fechaAlta,
                Set.of()
        )).isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("tutor");
    }

    @Test
    void matricular_DeberiaCambiarEstadoYRegistrarEvento() {
        Alumno alumno = Alumno.crear(
                alumnoId,
                numeroMatricula,
                nombre,
                apellidos,
                fechaNacimiento,
                fechaAlta,
                Set.of(tutorMock1)
        );
        UUID claseId = UUID.randomUUID();

        alumno.matricular(claseId);

        assertThat(alumno.isActivo()).isTrue();
        List<Entity.DomainEvent> eventos = alumno.getDomainEvents();
        assertThat(eventos).hasSize(1);
        assertThat(eventos.get(0)).isInstanceOf(AlumnoMatriculado.class);
        AlumnoMatriculado evento = (AlumnoMatriculado) eventos.get(0);
        assertThat(evento.alumnoId()).isEqualTo(alumnoId.value());
        assertThat(evento.numeroMatricula()).isEqualTo(numeroMatricula);
        assertThat(evento.claseId()).isEqualTo(claseId);
    }

    @Test
    void desactivar_DeberiaCambiarEstadoActivoAFalse() {
        Alumno alumno = Alumno.crear(
                alumnoId,
                numeroMatricula,
                nombre,
                apellidos,
                fechaNacimiento,
                fechaAlta,
                Set.of(tutorMock1)
        );

        alumno.desactivar();

        assertThat(alumno.isActivo()).isFalse();
    }

    @Test
    void agregarTutor_DeberiaAnadirTutorALaColeccion() {
        Alumno alumno = Alumno.crear(
                alumnoId,
                numeroMatricula,
                nombre,
                apellidos,
                fechaNacimiento,
                fechaAlta,
                Set.of(tutorMock1)
        );
        when(tutorMock2.getId()).thenReturn(new Tutor.TutorId(UUID.randomUUID()));

        alumno.agregarTutor(tutorMock2);

        assertThat(alumno.getTutores()).containsExactlyInAnyOrder(tutorMock1, tutorMock2);
    }

    @Test
    void agregarTutor_ConTutorDuplicado_NoDeberiaAnadirlo() {
        Alumno alumno = Alumno.crear(
                alumnoId,
                numeroMatricula,
                nombre,
                apellidos,
                fechaNacimiento,
                fechaAlta,
                Set.of(tutorMock1)
        );
        when(tutorMock1.getId()).thenReturn(new Tutor.TutorId(UUID.randomUUID()));

        alumno.agregarTutor(tutorMock1);

        assertThat(alumno.getTutores()).containsExactly(tutorMock1);
    }

    @Test
    void removerTutor_DeberiaEliminarTutorDeLaColeccion() {
        Tutor.TutorId tutorId1 = new Tutor.TutorId(UUID.randomUUID());
        Tutor.TutorId tutorId2 = new Tutor.TutorId(UUID.randomUUID());
        when(tutorMock1.getId()).thenReturn(tutorId1);
        when(tutorMock2.getId()).thenReturn(tutorId2);
        Alumno alumno = Alumno.crear(
                alumnoId,
                numeroMatricula,
                nombre,
                apellidos,
                fechaNacimiento,
                fechaAlta,
                Set.of(tutorMock1, tutorMock2)
        );

        alumno.removerTutor(tutorId1);

        assertThat(alumno.getTutores()).containsExactly(tutorMock2);
    }

    @Test
    void removerTutor_IntentandoRemoverUltimoTutor_DeberiaLanzarExcepcion() {
        Tutor.TutorId tutorId1 = new Tutor.TutorId(UUID.randomUUID());
        when(tutorMock1.getId()).thenReturn(tutorId1);
        Alumno alumno = Alumno.crear(
                alumnoId,
                numeroMatricula,
                nombre,
                apellidos,
                fechaNacimiento,
                fechaAlta,
                Set.of(tutorMock1)
        );

        assertThatThrownBy(() -> alumno.removerTutor(tutorId1))
                .isInstanceOf(IllegalStateException.class)
                .hasMessageContaining("al menos un tutor");
    }

    @Test
    void actualizarInformacionPersonal_ConDatosValidos_DeberiaActualizarCampos() {
        Alumno alumno = Alumno.crear(
                alumnoId,
                numeroMatricula,
                nombre,
                apellidos,
                fechaNacimiento,
                fechaAlta,
                Set.of(tutorMock1)
        );
        String nuevoNombre = "Carlos";
        String nuevosApellidos = "García Ruiz";
        LocalDate nuevaFechaNacimiento = LocalDate.of(2016, 3, 15);

        alumno.actualizarInformacionPersonal(nuevoNombre, nuevosApellidos, nuevaFechaNacimiento);

        assertThat(alumno.getNombre()).isEqualTo(nuevoNombre);
        assertThat(alumno.getApellidos()).isEqualTo(nuevosApellidos);
        assertThat(alumno.getFechaNacimiento()).isEqualTo(nuevaFechaNacimiento);
    }

    @Test
    void actualizarInformacionPersonal_ConNombreNulo_DeberiaLanzarExcepcion() {
        Alumno alumno = Alumno.crear(
                alumnoId,
                numeroMatricula,
                nombre,
                apellidos,
                fechaNacimiento,
                fechaAlta,
                Set.of(tutorMock1)
        );

        assertThatThrownBy(() -> alumno.actualizarInformacionPersonal(null, apellidos, fechaNacimiento))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("nombre");
    }
}