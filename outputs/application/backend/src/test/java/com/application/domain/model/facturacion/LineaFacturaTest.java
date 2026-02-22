package com.application.domain.model.facturacion;

import com.application.domain.shared.Entity;
import com.application.domain.valueobject.facturacion.Dinero;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class LineaFacturaTest {

    private LineaFactura.LineaFacturaId id;
    private String concepto;
    private int cantidad;
    private Dinero precioUnitario;
    private Dinero importeEsperado;

    @BeforeEach
    void setUp() {
        id = new LineaFactura.LineaFacturaId(UUID.randomUUID());
        concepto = "Tarifa mensual Octubre 2024";
        cantidad = 1;
        precioUnitario = new Dinero(new BigDecimal("350.50"), "EUR");
        importeEsperado = new Dinero(new BigDecimal("350.50"), "EUR");
    }

    @Test
    void crearLineaFactura_ConDatosValidos_DeberiaCrearInstancia() {
        LineaFactura linea = LineaFactura.crear(id, concepto, cantidad, precioUnitario);

        assertThat(linea).isNotNull();
        assertThat(linea.getId()).isEqualTo(id);
        assertThat(linea.getConcepto()).isEqualTo(concepto);
        assertThat(linea.getCantidad()).isEqualTo(cantidad);
        assertThat(linea.getPrecioUnitario()).isEqualTo(precioUnitario);
        assertThat(linea.getImporte()).isEqualTo(importeEsperado);
        assertThat(linea.getImporte().cantidad()).isEqualByComparingTo("350.50");
    }

    @Test
    void crearLineaFactura_ConCantidadMayorAUno_DeberiaCalcularImporteCorrectamente() {
        cantidad = 3;
        importeEsperado = new Dinero(new BigDecimal("1051.50"), "EUR");

        LineaFactura linea = LineaFactura.crear(id, concepto, cantidad, precioUnitario);

        assertThat(linea.getImporte()).isEqualTo(importeEsperado);
        assertThat(linea.getImporte().cantidad()).isEqualByComparingTo("1051.50");
    }

    @Test
    void crearLineaFactura_ConCantidadCero_DeberiaLanzarExcepcion() {
        cantidad = 0;

        assertThatThrownBy(() -> LineaFactura.crear(id, concepto, cantidad, precioUnitario))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("cantidad");
    }

    @Test
    void crearLineaFactura_ConCantidadNegativa_DeberiaLanzarExcepcion() {
        cantidad = -5;

        assertThatThrownBy(() -> LineaFactura.crear(id, concepto, cantidad, precioUnitario))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("cantidad");
    }

    @Test
    void crearLineaFactura_ConPrecioUnitarioNull_DeberiaLanzarExcepcion() {
        precioUnitario = null;

        assertThatThrownBy(() -> LineaFactura.crear(id, concepto, cantidad, precioUnitario))
                .isInstanceOf(NullPointerException.class)
                .hasMessageContaining("precioUnitario");
    }

    @Test
    void crearLineaFactura_ConConceptoVacio_DeberiaLanzarExcepcion() {
        concepto = "";

        assertThatThrownBy(() -> LineaFactura.crear(id, concepto, cantidad, precioUnitario))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("concepto");
    }

    @Test
    void crearLineaFactura_ConConceptoNull_DeberiaLanzarExcepcion() {
        concepto = null;

        assertThatThrownBy(() -> LineaFactura.crear(id, concepto, cantidad, precioUnitario))
                .isInstanceOf(NullPointerException.class)
                .hasMessageContaining("concepto");
    }

    @Test
    void crearLineaFactura_ConIdNull_DeberiaLanzarExcepcion() {
        id = null;

        assertThatThrownBy(() -> LineaFactura.crear(id, concepto, cantidad, precioUnitario))
                .isInstanceOf(NullPointerException.class)
                .hasMessageContaining("id");
    }

    @Test
    void actualizarConcepto_ConNuevoConceptoValido_DeberiaActualizar() {
        LineaFactura linea = LineaFactura.crear(id, concepto, cantidad, precioUnitario);
        String nuevoConcepto = "Tarifa mensual Noviembre 2024";

        linea.actualizarConcepto(nuevoConcepto);

        assertThat(linea.getConcepto()).isEqualTo(nuevoConcepto);
    }

    @Test
    void actualizarConcepto_ConConceptoInvalido_DeberiaLanzarExcepcion() {
        LineaFactura linea = LineaFactura.crear(id, concepto, cantidad, precioUnitario);

        assertThatThrownBy(() -> linea.actualizarConcepto(""))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("concepto");
    }

    @Test
    void actualizarCantidadYprecio_ConDatosValidos_DeberiaActualizarImporte() {
        LineaFactura linea = LineaFactura.crear(id, concepto, cantidad, precioUnitario);
        int nuevaCantidad = 2;
        Dinero nuevoPrecio = new Dinero(new BigDecimal("300.00"), "EUR");
        Dinero nuevoImporteEsperado = new Dinero(new BigDecimal("600.00"), "EUR");

        linea.actualizarCantidadYprecio(nuevaCantidad, nuevoPrecio);

        assertThat(linea.getCantidad()).isEqualTo(nuevaCantidad);
        assertThat(linea.getPrecioUnitario()).isEqualTo(nuevoPrecio);
        assertThat(linea.getImporte()).isEqualTo(nuevoImporteEsperado);
    }

    @Test
    void actualizarCantidadYprecio_ConCantidadInvalida_DeberiaLanzarExcepcion() {
        LineaFactura linea = LineaFactura.crear(id, concepto, cantidad, precioUnitario);

        assertThatThrownBy(() -> linea.actualizarCantidadYprecio(0, precioUnitario))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("cantidad");
    }

    @Test
    void actualizarCantidadYprecio_ConPrecioNull_DeberiaLanzarExcepcion() {
        LineaFactura linea = LineaFactura.crear(id, concepto, cantidad, precioUnitario);

        assertThatThrownBy(() -> linea.actualizarCantidadYprecio(2, null))
                .isInstanceOf(NullPointerException.class)
                .hasMessageContaining("precioUnitario");
    }

    @Test
    void equals_ConMismoId_DeberiaSerIgual() {
        LineaFactura linea1 = LineaFactura.crear(id, concepto, cantidad, precioUnitario);
        LineaFactura linea2 = LineaFactura.crear(id, "Otro concepto", 5, new Dinero(BigDecimal.TEN, "USD"));

        assertThat(linea1).isEqualTo(linea2);
        assertThat(linea1.hashCode()).isEqualTo(linea2.hashCode());
    }

    @Test
    void equals_ConDistintoId_DeberiaSerDiferente() {
        LineaFactura.LineaFacturaId otroId = new LineaFactura.LineaFacturaId(UUID.randomUUID());
        LineaFactura linea1 = LineaFactura.crear(id, concepto, cantidad, precioUnitario);
        LineaFactura linea2 = LineaFactura.crear(otroId, concepto, cantidad, precioUnitario);

        assertThat(linea1).isNotEqualTo(linea2);
    }

    @Test
    void lineaFacturaId_ImplementaValueObject() {
        assertThat(id).isInstanceOf(com.application.domain.shared.ValueObject.class);
    }

    @Test
    void lineaFactura_ExtiendeEntity() {
        LineaFactura linea = LineaFactura.crear(id, concepto, cantidad, precioUnitario);
        assertThat(linea).isInstanceOf(Entity.class);
    }
}