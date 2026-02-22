package com.application;

import com.application.domain.model.event.AlumnoMatriculado;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class AlumnoMatriculadoTest {

    @Test
    void crearEvento_ConParametrosValidos_InstanciaCorrecta() {
        UUID alumnoId = UUID.randomUUID();
        String numeroMatricula = "MAT-2024-001";
        LocalDate fechaMatricula = LocalDate.now();
        UUID claseId = UUID.randomUUID();

        AlumnoMatriculado evento = new AlumnoMatriculado(alumnoId, numeroMatricula, fechaMatricula, claseId);

        assertNotNull(evento);
        assertEquals(alumnoId, evento.alumnoId());
        assertEquals(numeroMatricula, evento.numeroMatricula());
        assertEquals(fechaMatricula, evento.fechaMatricula());
        assertEquals(claseId, evento.claseId());
        assertNotNull(evento.ocurridoEn());
        assertFalse(evento.ocurridoEn().toString().isEmpty());
    }

    @Test
    void crearEvento_ConAlumnoIdNulo_LanzaExcepcion() {
        String numeroMatricula = "MAT-2024-001";
        LocalDate fechaMatricula = LocalDate.now();
        UUID claseId = UUID.randomUUID();

        assertThrows(NullPointerException.class, () -> {
            new AlumnoMatriculado(null, numeroMatricula, fechaMatricula, claseId);
        });
    }

    @Test
    void crearEvento_ConNumeroMatriculaNulo_LanzaExcepcion() {
        UUID alumnoId = UUID.randomUUID();
        LocalDate fechaMatricula = LocalDate.now();
        UUID claseId = UUID.randomUUID();

        assertThrows(NullPointerException.class, () -> {
            new AlumnoMatriculado(alumnoId, null, fechaMatricula, claseId);
        });
    }

    @Test
    void crearEvento_ConFechaMatriculaNula_LanzaExcepcion() {
        UUID alumnoId = UUID.randomUUID();
        String numeroMatricula = "MAT-2024-001";
        UUID claseId = UUID.randomUUID();

        assertThrows(NullPointerException.class, () -> {
            new AlumnoMatriculado(alumnoId, numeroMatricula, null, claseId);
        });
    }

    @Test
    void crearEvento_ConClaseIdNulo_LanzaExcepcion() {
        UUID alumnoId = UUID.randomUUID();
        String numeroMatricula = "MAT-2024-001";
        LocalDate fechaMatricula = LocalDate.now();

        assertThrows(NullPointerException.class, () -> {
            new AlumnoMatriculado(alumnoId, numeroMatricula, fechaMatricula, null);
        });
    }

    @Test
    void crearEvento_ConNumeroMatriculaVacio_LanzaExcepcion() {
        UUID alumnoId = UUID.randomUUID();
        LocalDate fechaMatricula = LocalDate.now();
        UUID claseId = UUID.randomUUID();

        assertThrows(IllegalArgumentException.class, () -> {
            new AlumnoMatriculado(alumnoId, "", fechaMatricula, claseId);
        });
    }

    @Test
    void crearEvento_ConFechaMatriculaFutura_NoLanzaExcepcion() {
        UUID alumnoId = UUID.randomUUID();
        String numeroMatricula = "MAT-2024-001";
        LocalDate fechaFutura = LocalDate.now().plusDays(1);
        UUID claseId = UUID.randomUUID();

        AlumnoMatriculado evento = new AlumnoMatriculado(alumnoId, numeroMatricula, fechaFutura, claseId);

        assertNotNull(evento);
        assertEquals(fechaFutura, evento.fechaMatricula());
    }

    @Test
    void equals_ConMismosIds_DevuelveTrue() {
        UUID alumnoId = UUID.randomUUID();
        String numeroMatricula = "MAT-2024-001";
        LocalDate fechaMatricula = LocalDate.now();
        UUID claseId = UUID.randomUUID();

        AlumnoMatriculado evento1 = new AlumnoMatriculado(alumnoId, numeroMatricula, fechaMatricula, claseId);
        AlumnoMatriculado evento2 = new AlumnoMatriculado(alumnoId, numeroMatricula, fechaMatricula, claseId);

        assertEquals(evento1, evento2);
        assertEquals(evento1.hashCode(), evento2.hashCode());
    }

    @Test
    void equals_ConIdsDiferentes_DevuelveFalse() {
        UUID alumnoId1 = UUID.randomUUID();
        UUID alumnoId2 = UUID.randomUUID();
        String numeroMatricula = "MAT-2024-001";
        LocalDate fechaMatricula = LocalDate.now();
        UUID claseId = UUID.randomUUID();

        AlumnoMatriculado evento1 = new AlumnoMatriculado(alumnoId1, numeroMatricula, fechaMatricula, claseId);
        AlumnoMatriculado evento2 = new AlumnoMatriculado(alumnoId2, numeroMatricula, fechaMatricula, claseId);

        assertNotEquals(evento1, evento2);
    }

    @Test
    void toString_ContieneInformacionRelevante() {
        UUID alumnoId = UUID.randomUUID();
        String numeroMatricula = "MAT-2024-001";
        LocalDate fechaMatricula = LocalDate.now();
        UUID claseId = UUID.randomUUID();

        AlumnoMatriculado evento = new AlumnoMatriculado(alumnoId, numeroMatricula, fechaMatricula, claseId);
        String toStringResult = evento.toString();

        assertTrue(toStringResult.contains(alumnoId.toString()));
        assertTrue(toStringResult.contains(numeroMatricula));
        assertTrue(toStringResult.contains(fechaMatricula.toString()));
        assertTrue(toStringResult.contains(claseId.toString()));
    }
}