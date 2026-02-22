package com.application.domain.model.event;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class AsistenciaRegistradaTest {

    @Test
    void crearEvento_ConParametrosValidos_InstanciaCorrecta() {
        UUID alumnoId = UUID.randomUUID();
        UUID registroId = UUID.randomUUID();
        LocalDate fecha = LocalDate.now();
        String estado = "PRESENTE";

        AsistenciaRegistrada evento = new AsistenciaRegistrada(alumnoId, registroId, fecha, estado);

        assertNotNull(evento);
        assertEquals(alumnoId, evento.alumnoId());
        assertEquals(registroId, evento.registroAsistenciaId());
        assertEquals(fecha, evento.fecha());
        assertEquals(estado, evento.estado());
        assertNotNull(evento.ocurridoEn());
    }

    @Test
    void crearEvento_ConAlumnoIdNulo_LanzaExcepcion() {
        UUID registroId = UUID.randomUUID();
        LocalDate fecha = LocalDate.now();
        String estado = "AUSENTE";

        assertThrows(NullPointerException.class, () -> new AsistenciaRegistrada(null, registroId, fecha, estado));
    }

    @Test
    void crearEvento_ConRegistroIdNulo_LanzaExcepcion() {
        UUID alumnoId = UUID.randomUUID();
        LocalDate fecha = LocalDate.now();
        String estado = "JUSTIFICADO";

        assertThrows(NullPointerException.class, () -> new AsistenciaRegistrada(alumnoId, null, fecha, estado));
    }

    @Test
    void crearEvento_ConFechaNula_LanzaExcepcion() {
        UUID alumnoId = UUID.randomUUID();
        UUID registroId = UUID.randomUUID();
        String estado = "RETRASO";

        assertThrows(NullPointerException.class, () -> new AsistenciaRegistrada(alumnoId, registroId, null, estado));
    }

    @Test
    void crearEvento_ConEstadoNulo_LanzaExcepcion() {
        UUID alumnoId = UUID.randomUUID();
        UUID registroId = UUID.randomUUID();
        LocalDate fecha = LocalDate.now();

        assertThrows(NullPointerException.class, () -> new AsistenciaRegistrada(alumnoId, registroId, fecha, null));
    }

    @Test
    void crearEvento_ConEstadoVacio_LanzaExcepcion() {
        UUID alumnoId = UUID.randomUUID();
        UUID registroId = UUID.randomUUID();
        LocalDate fecha = LocalDate.now();

        assertThrows(IllegalArgumentException.class, () -> new AsistenciaRegistrada(alumnoId, registroId, fecha, ""));
    }

    @Test
    void crearEvento_ConEstadoEnBlanco_LanzaExcepcion() {
        UUID alumnoId = UUID.randomUUID();
        UUID registroId = UUID.randomUUID();
        LocalDate fecha = LocalDate.now();

        assertThrows(IllegalArgumentException.class, () -> new AsistenciaRegistrada(alumnoId, registroId, fecha, "   "));
    }

    @Test
    void equals_ConMismosIds_DevuelveTrue() {
        UUID alumnoId = UUID.randomUUID();
        UUID registroId = UUID.randomUUID();
        LocalDate fecha = LocalDate.now();
        String estado = "PRESENTE";

        AsistenciaRegistrada evento1 = new AsistenciaRegistrada(alumnoId, registroId, fecha, estado);
        AsistenciaRegistrada evento2 = new AsistenciaRegistrada(alumnoId, registroId, fecha, estado);

        assertEquals(evento1, evento2);
        assertEquals(evento1.hashCode(), evento2.hashCode());
    }

    @Test
    void equals_ConIdsDiferentes_DevuelveFalse() {
        UUID alumnoId1 = UUID.randomUUID();
        UUID alumnoId2 = UUID.randomUUID();
        UUID registroId = UUID.randomUUID();
        LocalDate fecha = LocalDate.now();
        String estado = "PRESENTE";

        AsistenciaRegistrada evento1 = new AsistenciaRegistrada(alumnoId1, registroId, fecha, estado);
        AsistenciaRegistrada evento2 = new AsistenciaRegistrada(alumnoId2, registroId, fecha, estado);

        assertNotEquals(evento1, evento2);
    }

    @Test
    void toString_ContieneInformacionRelevante() {
        UUID alumnoId = UUID.randomUUID();
        UUID registroId = UUID.randomUUID();
        LocalDate fecha = LocalDate.now();
        String estado = "AUSENTE";

        AsistenciaRegistrada evento = new AsistenciaRegistrada(alumnoId, registroId, fecha, estado);
        String resultado = evento.toString();

        assertTrue(resultado.contains(alumnoId.toString()));
        assertTrue(resultado.contains(registroId.toString()));
        assertTrue(resultado.contains(fecha.toString()));
        assertTrue(resultado.contains(estado));
    }
}