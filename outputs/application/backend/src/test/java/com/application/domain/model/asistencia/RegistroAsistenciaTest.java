package com.application.domain.model.asistencia;

import com.application.domain.shared.Entity;
import com.application.domain.valueobject.asistencia.EstadoAsistencia;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.time.LocalTime;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class RegistroAsistenciaTest {

    private RegistroAsistencia registro;
    private final LocalDate fechaValida = LocalDate.now().minusDays(1);
    private final LocalTime horaEntradaValida = LocalTime.of(9, 0);
    private final LocalTime horaSalidaValida = LocalTime.of(14, 0);
    private final EstadoAsistencia estadoPresente = EstadoAsistencia.PRESENTE;

    @BeforeEach
    void setUp() {
        registro = new RegistroAsistencia(
                new RegistroAsistencia.RegistroAsistenciaId(),
                fechaValida,
                horaEntradaValida,
                horaSalidaValida,
                estadoPresente,
                "Comentario de prueba"
        );
    }

    @Test
    void crearRegistroConParametrosValidos_DeberiaCrearInstancia() {
        assertNotNull(registro);
        assertEquals(fechaValida, registro.getFecha());
        assertEquals(horaEntradaValida, registro.getHoraEntrada());
        assertEquals(horaSalidaValida, registro.getHoraSalida());
        assertEquals(estadoPresente, registro.getEstado());
        assertEquals("Comentario de prueba", registro.getComentarios());
    }

    @Test
    void crearRegistroConFechaFutura_DeberiaLanzarExcepcion() {
        LocalDate fechaFutura = LocalDate.now().plusDays(1);

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () ->
                new RegistroAsistencia(
                        new RegistroAsistencia.RegistroAsistenciaId(),
                        fechaFutura,
                        horaEntradaValida,
                        horaSalidaValida,
                        estadoPresente,
                        null
                )
        );
        assertTrue(exception.getMessage().contains("futura"));
    }

    @Test
    void crearRegistroConHoraEntradaPosteriorASalida_DeberiaLanzarExcepcion() {
        LocalTime entrada = LocalTime.of(15, 0);
        LocalTime salida = LocalTime.of(10, 0);

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () ->
                new RegistroAsistencia(
                        new RegistroAsistencia.RegistroAsistenciaId(),
                        fechaValida,
                        entrada,
                        salida,
                        estadoPresente,
                        null
                )
        );
        assertTrue(exception.getMessage().contains("posterior"));
    }

    @Test
    void crearRegistroConEstadoNulo_DeberiaLanzarExcepcion() {
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () ->
                new RegistroAsistencia(
                        new RegistroAsistencia.RegistroAsistenciaId(),
                        fechaValida,
                        horaEntradaValida,
                        horaSalidaValida,
                        null,
                        null
                )
        );
        assertTrue(exception.getMessage().contains("estado"));
    }

    @Test
    void crearRegistroConHoraSalidaNula_DeberiaSerPermitido() {
        RegistroAsistencia reg = new RegistroAsistencia(
                new RegistroAsistencia.RegistroAsistenciaId(),
                fechaValida,
                horaEntradaValida,
                null,
                estadoPresente,
                null
        );
        assertNotNull(reg);
        assertNull(reg.getHoraSalida());
    }

    @Test
    void crearRegistroConComentariosNulos_DeberiaSerPermitido() {
        RegistroAsistencia reg = new RegistroAsistencia(
                new RegistroAsistencia.RegistroAsistenciaId(),
                fechaValida,
                horaEntradaValida,
                horaSalidaValida,
                estadoPresente,
                null
        );
        assertNotNull(reg);
        assertNull(reg.getComentarios());
    }

    @Test
    void crearRegistroConComentariosVacios_DeberiaSerPermitido() {
        RegistroAsistencia reg = new RegistroAsistencia(
                new RegistroAsistencia.RegistroAsistenciaId(),
                fechaValida,
                horaEntradaValida,
                horaSalidaValida,
                estadoPresente,
                ""
        );
        assertNotNull(reg);
        assertEquals("", reg.getComentarios());
    }

    @Test
    void registrarSalidaConHoraValida_DeberiaActualizarHoraSalida() {
        LocalTime nuevaSalida = LocalTime.of(17, 30);
        registro.registrarSalida(nuevaSalida);
        assertEquals(nuevaSalida, registro.getHoraSalida());
    }

    @Test
    void registrarSalidaConHoraAnteriorAEntrada_DeberiaLanzarExcepcion() {
        LocalTime salidaInvalida = LocalTime.of(8, 0);

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () ->
                registro.registrarSalida(salidaInvalida)
        );
        assertTrue(exception.getMessage().contains("anterior"));
    }

    @Test
    void justificarAusenciaConComentario_DeberiaCambiarEstadoAJustificadoYActualizarComentarios() {
        String justificacion = "Enfermedad certificada por médico.";
        registro.justificarAusencia(justificacion);

        assertEquals(EstadoAsistencia.JUSTIFICADO, registro.getEstado());
        assertEquals(justificacion, registro.getComentarios());
    }

    @Test
    void justificarAusenciaConComentarioNulo_DeberiaCambiarEstadoPeroMantenerComentarioAnterior() {
        registro.justificarAusencia(null);
        assertEquals(EstadoAsistencia.JUSTIFICADO, registro.getEstado());
        assertEquals("Comentario de prueba", registro.getComentarios());
    }

    @Test
    void justificarAusenciaConComentarioVacio_DeberiaCambiarEstadoPeroMantenerComentarioAnterior() {
        registro.justificarAusencia("");
        assertEquals(EstadoAsistencia.JUSTIFICADO, registro.getEstado());
        assertEquals("Comentario de prueba", registro.getComentarios());
    }

    @Test
    void marcarComoRetraso_DeberiaCambiarEstadoARetraso() {
        registro.marcarComoRetraso();
        assertEquals(EstadoAsistencia.RETRASO, registro.getEstado());
    }

    @Test
    void testRegistroAsistenciaIdEsValueObject() {
        RegistroAsistencia.RegistroAsistenciaId id1 = new RegistroAsistencia.RegistroAsistenciaId();
        RegistroAsistencia.RegistroAsistenciaId id2 = new RegistroAsistencia.RegistroAsistenciaId();
        assertNotEquals(id1, id2);
        assertNotNull(id1.value());
    }

    @Test
    void testRegistroAsistenciaExtiendeEntity() {
        assertTrue(Entity.class.isAssignableFrom(RegistroAsistencia.class));
    }

    @Test
    void testConstructorProtegidoDisponibleParaJPA() throws NoSuchMethodException {
        assertNotNull(RegistroAsistencia.class.getDeclaredConstructor());
    }
}