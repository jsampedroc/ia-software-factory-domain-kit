package com.application.domain.model.facturacion;

import com.application.domain.shared.Entity;
import com.application.domain.valueobject.facturacion.Dinero;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDate;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class TarifaTest {

    private Dinero precioValido;
    private LocalDate fechaInicioValida;
    private LocalDate fechaFinValida;

    @BeforeEach
    void setUp() {
        precioValido = new Dinero(new BigDecimal("150.50"), "EUR");
        fechaInicioValida = LocalDate.now().plusDays(1);
        fechaFinValida = fechaInicioValida.plusMonths(12);
    }

    @Test
    void crearTarifa_ConDatosValidos_DeberiaCrearInstancia() {
        Tarifa tarifa = new Tarifa(
                new Tarifa.TarifaId(),
                "Tarifa Básica",
                "Tarifa estándar mensual",
                precioValido,
                true,
                fechaInicioValida,
                fechaFinValida
        );

        assertThat(tarifa).isNotNull();
        assertThat(tarifa.getId()).isNotNull();
        assertThat(tarifa.getNombre()).isEqualTo("Tarifa Básica");
        assertThat(tarifa.getDescripcion()).isEqualTo("Tarifa estándar mensual");
        assertThat(tarifa.getPrecioMensual()).isEqualTo(precioValido);
        assertThat(tarifa.isActivo()).isTrue();
        assertThat(tarifa.getFechaInicio()).isEqualTo(fechaInicioValida);
        assertThat(tarifa.getFechaFin()).isEqualTo(fechaFinValida);
    }

    @Test
    void crearTarifa_ConPrecioCero_DeberiaLanzarExcepcion() {
        Dinero precioCero = new Dinero(BigDecimal.ZERO, "EUR");

        assertThatThrownBy(() -> new Tarifa(
                new Tarifa.TarifaId(),
                "Nombre",
                "Desc",
                precioCero,
                true,
                fechaInicioValida,
                fechaFinValida
        )).isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("precio");
    }

    @Test
    void crearTarifa_ConPrecioNegativo_DeberiaLanzarExcepcion() {
        Dinero precioNegativo = new Dinero(new BigDecimal("-10.00"), "EUR");

        assertThatThrownBy(() -> new Tarifa(
                new Tarifa.TarifaId(),
                "Nombre",
                "Desc",
                precioNegativo,
                true,
                fechaInicioValida,
                fechaFinValida
        )).isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("precio");
    }

    @Test
    void crearTarifa_ConFechaInicioPosteriorAFechaFin_DeberiaLanzarExcepcion() {
        LocalDate inicio = LocalDate.now().plusDays(10);
        LocalDate fin = inicio.minusDays(1);

        assertThatThrownBy(() -> new Tarifa(
                new Tarifa.TarifaId(),
                "Nombre",
                "Desc",
                precioValido,
                true,
                inicio,
                fin
        )).isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("fecha");
    }

    @Test
    void crearTarifa_ConFechaInicioIgualAFechaFin_DeberiaSerValido() {
        LocalDate mismaFecha = LocalDate.now().plusDays(5);

        Tarifa tarifa = new Tarifa(
                new Tarifa.TarifaId(),
                "Tarifa Diaria",
                "Desc",
                precioValido,
                true,
                mismaFecha,
                mismaFecha
        );

        assertThat(tarifa.getFechaInicio()).isEqualTo(mismaFecha);
        assertThat(tarifa.getFechaFin()).isEqualTo(mismaFecha);
    }

    @Test
    void crearTarifa_ConFechaInicioNull_DeberiaLanzarExcepcion() {
        assertThatThrownBy(() -> new Tarifa(
                new Tarifa.TarifaId(),
                "Nombre",
                "Desc",
                precioValido,
                true,
                null,
                fechaFinValida
        )).isInstanceOf(NullPointerException.class);
    }

    @Test
    void crearTarifa_ConFechaFinNull_DeberiaLanzarExcepcion() {
        assertThatThrownBy(() -> new Tarifa(
                new Tarifa.TarifaId(),
                "Nombre",
                "Desc",
                precioValido,
                true,
                fechaInicioValida,
                null
        )).isInstanceOf(NullPointerException.class);
    }

    @Test
    void crearTarifa_ConNombreNull_DeberiaLanzarExcepcion() {
        assertThatThrownBy(() -> new Tarifa(
                new Tarifa.TarifaId(),
                null,
                "Desc",
                precioValido,
                true,
                fechaInicioValida,
                fechaFinValida
        )).isInstanceOf(NullPointerException.class);
    }

    @Test
    void crearTarifa_ConDescripcionNull_DeberiaPermitirlo() {
        Tarifa tarifa = new Tarifa(
                new Tarifa.TarifaId(),
                "Nombre",
                null,
                precioValido,
                true,
                fechaInicioValida,
                fechaFinValida
        );

        assertThat(tarifa.getDescripcion()).isNull();
    }

    @Test
    void crearTarifa_ConPrecioMensualNull_DeberiaLanzarExcepcion() {
        assertThatThrownBy(() -> new Tarifa(
                new Tarifa.TarifaId(),
                "Nombre",
                "Desc",
                null,
                true,
                fechaInicioValida,
                fechaFinValida
        )).isInstanceOf(NullPointerException.class);
    }

    @Test
    void esVigenteEnFecha_FechaDentroDelRango_DeberiaDevolverTrue() {
        LocalDate inicio = LocalDate.of(2024, 1, 1);
        LocalDate fin = LocalDate.of(2024, 12, 31);
        Tarifa tarifa = new Tarifa(
                new Tarifa.TarifaId(),
                "Nombre",
                "Desc",
                precioValido,
                true,
                inicio,
                fin
        );

        assertThat(tarifa.esVigenteEnFecha(LocalDate.of(2024, 6, 15))).isTrue();
        assertThat(tarifa.esVigenteEnFecha(inicio)).isTrue();
        assertThat(tarifa.esVigenteEnFecha(fin)).isTrue();
    }

    @Test
    void esVigenteEnFecha_FechaFueraDelRango_DeberiaDevolverFalse() {
        LocalDate inicio = LocalDate.of(2024, 1, 1);
        LocalDate fin = LocalDate.of(2024, 12, 31);
        Tarifa tarifa = new Tarifa(
                new Tarifa.TarifaId(),
                "Nombre",
                "Desc",
                precioValido,
                true,
                inicio,
                fin
        );

        assertThat(tarifa.esVigenteEnFecha(LocalDate.of(2023, 12, 31))).isFalse();
        assertThat(tarifa.esVigenteEnFecha(LocalDate.of(2025, 1, 1))).isFalse();
    }

    @Test
    void esVigenteEnFecha_TarifaInactiva_DeberiaDevolverFalse() {
        LocalDate inicio = LocalDate.of(2024, 1, 1);
        LocalDate fin = LocalDate.of(2024, 12, 31);
        Tarifa tarifa = new Tarifa(
                new Tarifa.TarifaId(),
                "Nombre",
                "Desc",
                precioValido,
                false,
                inicio,
                fin
        );

        assertThat(tarifa.esVigenteEnFecha(LocalDate.of(2024, 6, 15))).isFalse();
    }

    @Test
    void esVigenteEnFecha_FechaNull_DeberiaLanzarExcepcion() {
        Tarifa tarifa = new Tarifa(
                new Tarifa.TarifaId(),
                "Nombre",
                "Desc",
                precioValido,
                true,
                fechaInicioValida,
                fechaFinValida
        );

        assertThatThrownBy(() -> tarifa.esVigenteEnFecha(null))
                .isInstanceOf(NullPointerException.class);
    }

    @Test
    void desactivar_TarifaActiva_DeberiaCambiarEstado() {
        Tarifa tarifa = new Tarifa(
                new Tarifa.TarifaId(),
                "Nombre",
                "Desc",
                precioValido,
                true,
                fechaInicioValida,
                fechaFinValida
        );

        tarifa.desactivar();

        assertThat(tarifa.isActivo()).isFalse();
    }

    @Test
    void activar_TarifaInactiva_DeberiaCambiarEstado() {
        Tarifa tarifa = new Tarifa(
                new Tarifa.TarifaId(),
                "Nombre",
                "Desc",
                precioValido,
                false,
                fechaInicioValida,
                fechaFinValida
        );

        tarifa.activar();

        assertThat(tarifa.isActivo()).isTrue();
    }

    @Test
    void actualizarPrecio_ConNuevoPrecioValido_DeberiaActualizar() {
        Tarifa tarifa = new Tarifa(
                new Tarifa.TarifaId(),
                "Nombre",
                "Desc",
                precioValido,
                true,
                fechaInicioValida,
                fechaFinValida
        );
        Dinero nuevoPrecio = new Dinero(new BigDecimal("175.75"), "EUR");

        tarifa.actualizarPrecio(nuevoPrecio);

        assertThat(tarifa.getPrecioMensual()).isEqualTo(nuevoPrecio);
    }

    @Test
    void actualizarPrecio_ConPrecioInvalido_DeberiaLanzarExcepcion() {
        Tarifa tarifa = new Tarifa(
                new Tarifa.TarifaId(),
                "Nombre",
                "Desc",
                precioValido,
                true,
                fechaInicioValida,
                fechaFinValida
        );
        Dinero precioInvalido = new Dinero(BigDecimal.ZERO, "EUR");

        assertThatThrownBy(() -> tarifa.actualizarPrecio(precioInvalido))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("precio");
    }

    @Test
    void actualizarPrecio_ConPrecioNull_DeberiaLanzarExcepcion() {
        Tarifa tarifa = new Tarifa(
                new Tarifa.TarifaId(),
                "Nombre",
                "Desc",
                precioValido,
                true,
                fechaInicioValida,
                fechaFinValida
        );

        assertThatThrownBy(() -> tarifa.actualizarPrecio(null))
                .isInstanceOf(NullPointerException.class);
    }

    @Test
    void actualizarFechasVigencia_ConFechasValidas_DeberiaActualizar() {
        Tarifa tarifa = new Tarifa(
                new Tarifa.TarifaId(),
                "Nombre",
                "Desc",
                precioValido,
                true,
                fechaInicioValida,
                fechaFinValida
        );
        LocalDate nuevoInicio = fechaInicioValida.plusMonths(1);
        LocalDate nuevoFin = nuevoInicio.plusMonths(6);

        tarifa.actualizarFechasVigencia(nuevoInicio, nuevoFin);

        assertThat(tarifa.getFechaInicio()).isEqualTo(nuevoInicio);
        assertThat(tarifa.getFechaFin()).isEqualTo(nuevoFin);
    }

    @Test
    void actualizarFechasVigencia_ConFechasInvalidas_DeberiaLanzarExcepcion() {
        Tarifa tarifa = new Tarifa(
                new Tarifa.TarifaId(),
                "Nombre",
                "Desc",
                precioValido,
                true,
                fechaInicioValida,
                fechaFinValida
        );
        LocalDate nuevoInicio = fechaInicioValida.plusMonths(2);
        LocalDate nuevoFin = nuevoInicio.minusDays(1);

        assertThatThrownBy(() -> tarifa.actualizarFechasVigencia(nuevoInicio, nuevoFin))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("fecha");
    }

    @Test
    void equals_ConMismoId_DeberiaSerTrue() {
        Tarifa.TarifaId id = new Tarifa.TarifaId();
        Tarifa tarifa1 = new Tarifa(id, "Nombre1", "Desc1", precioValido, true, fechaInicioValida, fechaFinValida);
        Tarifa tarifa2 = new Tarifa(id, "Nombre2", "Desc2", precioValido, false, fechaInicioValida.plusDays(1), fechaFinValida.plusDays(1));

        assertThat(tarifa1).isEqualTo(tarifa2);
        assertThat(tarifa1.hashCode()).isEqualTo(tarifa2.hashCode());
    }

    @Test
    void equals_ConDistintoId_DeberiaSerFalse() {
        Tarifa tarifa1 = new Tarifa(new Tarifa.TarifaId(), "Nombre", "Desc", precioValido, true, fechaInicioValida, fechaFinValida);
        Tarifa tarifa2 = new Tarifa(new Tarifa.TarifaId(), "Nombre", "Desc", precioValido, true, fechaInicioValida, fechaFinValida);

        assertThat(tarifa1).isNotEqualTo(tarifa2);
    }

    @Test
    void TarifaId_ImplementaValueObject() {
        Tarifa.TarifaId id1 = new Tarifa.TarifaId();
        assertThat(id1).isInstanceOf(com.application.domain.shared.ValueObject.class);
    }

    @Test
    void Tarifa_ExtiendeEntity() {
        Tarifa tarifa = new Tarifa(
                new Tarifa.TarifaId(),
                "Nombre",
                "Desc",
                precioValido,
                true,
                fechaInicioValida,
                fechaFinValida
        );
        assertThat(tarifa).isInstanceOf(Entity.class);
    }
}