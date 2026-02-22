package com.application.domain.valueobject.facturacion;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDate;

@ExtendWith(MockitoExtension.class)
class PeriodoTest {

    @Test
    void crearPeriodo_ConFechasValidas_DeberiaCrearInstancia() {
        LocalDate inicio = LocalDate.of(2024, 1, 1);
        LocalDate fin = LocalDate.of(2024, 1, 31);

        Periodo periodo = new Periodo(inicio, fin);

        assertNotNull(periodo);
        assertEquals(inicio, periodo.fechaInicio());
        assertEquals(fin, periodo.fechaFin());
    }

    @Test
    void crearPeriodo_ConFechaInicioIgualFechaFin_DeberiaCrearInstancia() {
        LocalDate fecha = LocalDate.of(2024, 1, 15);
        Periodo periodo = new Periodo(fecha, fecha);

        assertNotNull(periodo);
        assertEquals(fecha, periodo.fechaInicio());
        assertEquals(fecha, periodo.fechaFin());
    }

    @Test
    void crearPeriodo_ConFechaInicioPosteriorFechaFin_DeberiaLanzarExcepcion() {
        LocalDate inicio = LocalDate.of(2024, 2, 1);
        LocalDate fin = LocalDate.of(2024, 1, 31);

        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> new Periodo(inicio, fin)
        );
        assertTrue(exception.getMessage().contains("inicio") && exception.getMessage().contains("fin"));
    }

    @Test
    void crearPeriodo_ConFechaInicioNula_DeberiaLanzarExcepcion() {
        LocalDate fin = LocalDate.of(2024, 1, 31);

        NullPointerException exception = assertThrows(
                NullPointerException.class,
                () -> new Periodo(null, fin)
        );
        assertTrue(exception.getMessage().contains("fechaInicio"));
    }

    @Test
    void crearPeriodo_ConFechaFinNula_DeberiaLanzarExcepcion() {
        LocalDate inicio = LocalDate.of(2024, 1, 1);

        NullPointerException exception = assertThrows(
                NullPointerException.class,
                () -> new Periodo(inicio, null)
        );
        assertTrue(exception.getMessage().contains("fechaFin"));
    }

    @Test
    void equals_ConMismasFechas_DeberiaSerIgual() {
        Periodo periodo1 = new Periodo(LocalDate.of(2024, 1, 1), LocalDate.of(2024, 1, 31));
        Periodo periodo2 = new Periodo(LocalDate.of(2024, 1, 1), LocalDate.of(2024, 1, 31));

        assertEquals(periodo1, periodo2);
        assertEquals(periodo1.hashCode(), periodo2.hashCode());
    }

    @Test
    void equals_ConFechasDiferentes_DeberiaSerDiferente() {
        Periodo periodo1 = new Periodo(LocalDate.of(2024, 1, 1), LocalDate.of(2024, 1, 31));
        Periodo periodo2 = new Periodo(LocalDate.of(2024, 2, 1), LocalDate.of(2024, 2, 28));

        assertNotEquals(periodo1, periodo2);
    }

    @Test
    void equals_ConObjetoNoPeriodo_DeberiaSerDiferente() {
        Periodo periodo = new Periodo(LocalDate.of(2024, 1, 1), LocalDate.of(2024, 1, 31));
        Object otroObjeto = new Object();

        assertNotEquals(periodo, otroObjeto);
    }

    @Test
    void toString_DeberiaContenerFechas() {
        Periodo periodo = new Periodo(LocalDate.of(2024, 1, 1), LocalDate.of(2024, 1, 31));
        String resultado = periodo.toString();

        assertTrue(resultado.contains("2024-01-01"));
        assertTrue(resultado.contains("2024-01-31"));
    }
}