package com.application.domain.model.facturacion;

import com.application.domain.model.event.FacturaGenerada;
import com.application.domain.model.event.FacturaPagada;
import com.application.domain.shared.Entity;
import com.application.domain.valueobject.facturacion.Dinero;
import com.application.domain.valueobject.facturacion.Periodo;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class FacturaTest {

    private FacturaId facturaId;
    private String numeroFactura;
    private LocalDate fechaEmision;
    private LocalDate fechaVencimiento;
    private Periodo periodoFacturado;
    private Dinero total;
    private Factura.Estado estado;
    private String concepto;
    private LineaFactura lineaFactura;
    private LineaFacturaId lineaFacturaId;

    @BeforeEach
    void setUp() {
        facturaId = new FacturaId(UUID.randomUUID());
        numeroFactura = "FAC-2024-001";
        fechaEmision = LocalDate.now();
        fechaVencimiento = fechaEmision.plusDays(30);
        periodoFacturado = new Periodo(fechaEmision.withDayOfMonth(1), fechaEmision.withDayOfMonth(fechaEmision.lengthOfMonth()));
        total = new Dinero(new BigDecimal("150.00"), "EUR");
        estado = Factura.Estado.GENERADA;
        concepto = "Mensualidad Enero 2024";
        lineaFacturaId = new LineaFacturaId(UUID.randomUUID());
        lineaFactura = new LineaFactura(lineaFacturaId, "Clase Regular", 1, new Dinero(new BigDecimal("150.00"), "EUR"));
    }

    @Test
    void crearFactura_ConDatosValidos_DeberiaCrearFactura() {
        Factura factura = Factura.crear(
                facturaId,
                numeroFactura,
                fechaEmision,
                fechaVencimiento,
                periodoFacturado,
                total,
                estado,
                concepto,
                List.of(lineaFactura)
        );

        assertThat(factura).isNotNull();
        assertThat(factura.getId()).isEqualTo(facturaId);
        assertThat(factura.getNumeroFactura()).isEqualTo(numeroFactura);
        assertThat(factura.getFechaEmision()).isEqualTo(fechaEmision);
        assertThat(factura.getFechaVencimiento()).isEqualTo(fechaVencimiento);
        assertThat(factura.getPeriodoFacturado()).isEqualTo(periodoFacturado);
        assertThat(factura.getTotal()).isEqualTo(total);
        assertThat(factura.getEstado()).isEqualTo(estado);
        assertThat(factura.getConcepto()).isEqualTo(concepto);
        assertThat(factura.getLineas()).hasSize(1).contains(lineaFactura);
        assertThat(factura.getDomainEvents()).hasSize(1);
        assertThat(factura.getDomainEvents().get(0)).isInstanceOf(FacturaGenerada.class);
    }

    @Test
    void crearFactura_ConNumeroFacturaNulo_DeberiaLanzarExcepcion() {
        assertThatThrownBy(() -> Factura.crear(
                facturaId,
                null,
                fechaEmision,
                fechaVencimiento,
                periodoFacturado,
                total,
                estado,
                concepto,
                List.of(lineaFactura)
        )).isInstanceOf(NullPointerException.class);
    }

    @Test
    void crearFactura_ConFechaVencimientoAnteriorAEmision_DeberiaLanzarExcepcion() {
        LocalDate vencimientoInvalido = fechaEmision.minusDays(1);
        assertThatThrownBy(() -> Factura.crear(
                facturaId,
                numeroFactura,
                fechaEmision,
                vencimientoInvalido,
                periodoFacturado,
                total,
                estado,
                concepto,
                List.of(lineasFactura)
        )).isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void crearFactura_ConTotalNegativo_DeberiaLanzarExcepcion() {
        Dinero totalNegativo = new Dinero(new BigDecimal("-10.00"), "EUR");
        assertThatThrownBy(() -> Factura.crear(
                facturaId,
                numeroFactura,
                fechaEmision,
                fechaVencimiento,
                periodoFacturado,
                totalNegativo,
                estado,
                concepto,
                List.of(lineaFactura)
        )).isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void crearFactura_ConListaLineasVacia_DeberiaLanzarExcepcion() {
        assertThatThrownBy(() -> Factura.crear(
                facturaId,
                numeroFactura,
                fechaEmision,
                fechaVencimiento,
                periodoFacturado,
                total,
                estado,
                concepto,
                List.of()
        )).isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void marcarComoPagada_DeberiaCambiarEstadoYAñadirEvento() {
        Factura factura = Factura.crear(
                facturaId,
                numeroFactura,
                fechaEmision,
                fechaVencimiento,
                periodoFacturado,
                total,
                Factura.Estado.ENVIADA,
                concepto,
                List.of(lineaFactura)
        );
        factura.clearDomainEvents();
        LocalDate fechaPago = LocalDate.now();

        factura.marcarComoPagada(fechaPago);

        assertThat(factura.getEstado()).isEqualTo(Factura.Estado.PAGADA);
        assertThat(factura.getDomainEvents()).hasSize(1);
        FacturaPagada evento = (FacturaPagada) factura.getDomainEvents().get(0);
        assertThat(evento.facturaId()).isEqualTo(facturaId);
        assertThat(evento.fechaPago()).isEqualTo(fechaPago);
        assertThat(evento.importePagado()).isEqualTo(total);
    }

    @Test
    void marcarComoPagada_ConFechaPagoNula_DeberiaLanzarExcepcion() {
        Factura factura = Factura.crear(
                facturaId,
                numeroFactura,
                fechaEmision,
                fechaVencimiento,
                periodoFacturado,
                total,
                Factura.Estado.ENVIADA,
                concepto,
                List.of(lineaFactura)
        );

        assertThatThrownBy(() -> factura.marcarComoPagada(null))
                .isInstanceOf(NullPointerException.class);
    }

    @Test
    void marcarComoPagada_ConEstadoNoPagable_DeberiaLanzarExcepcion() {
        Factura factura = Factura.crear(
                facturaId,
                numeroFactura,
                fechaEmision,
                fechaVencimiento,
                periodoFacturado,
                total,
                Factura.Estado.CANCELADA,
                concepto,
                List.of(lineaFactura)
        );

        assertThatThrownBy(() -> factura.marcarComoPagada(LocalDate.now()))
                .isInstanceOf(IllegalStateException.class);
    }

    @Test
    void calcularTotal_DeberiaSumarImportesDeLineas() {
        LineaFactura linea1 = new LineaFactura(new LineaFacturaId(UUID.randomUUID()), "Clase", 1, new Dinero(new BigDecimal("100.00"), "EUR"));
        LineaFactura linea2 = new LineaFactura(new LineaFacturaId(UUID.randomUUID()), "Material", 2, new Dinero(new BigDecimal("25.00"), "EUR"));
        List<LineaFactura> lineas = List.of(linea1, linea2);
        Dinero totalEsperado = new Dinero(new BigDecimal("150.00"), "EUR");

        Factura factura = Factura.crear(
                facturaId,
                numeroFactura,
                fechaEmision,
                fechaVencimiento,
                periodoFacturado,
                totalEsperado,
                estado,
                concepto,
                lineas
        );

        Dinero totalCalculado = factura.calcularTotal();
        assertThat(totalCalculado).isEqualTo(totalEsperado);
    }

    @Test
    void agregarLinea_DeberiaAñadirLineaYRecalcularTotal() {
        Factura factura = Factura.crear(
                facturaId,
                numeroFactura,
                fechaEmision,
                fechaVencimiento,
                periodoFacturado,
                total,
                estado,
                concepto,
                List.of(lineaFactura)
        );
        factura.clearDomainEvents();
        LineaFactura nuevaLinea = new LineaFactura(new LineaFacturaId(UUID.randomUUID()), "Actividad Extra", 1, new Dinero(new BigDecimal("30.00"), "EUR"));
        Dinero totalEsperado = new Dinero(new BigDecimal("180.00"), "EUR");

        factura.agregarLinea(nuevaLinea);

        assertThat(factura.getLineas()).hasSize(2).contains(nuevaLinea);
        assertThat(factura.getTotal()).isEqualTo(totalEsperado);
    }

    @Test
    void agregarLinea_ConLineaNula_DeberiaLanzarExcepcion() {
        Factura factura = Factura.crear(
                facturaId,
                numeroFactura,
                fechaEmision,
                fechaVencimiento,
                periodoFacturado,
                total,
                estado,
                concepto,
                List.of(lineaFactura)
        );

        assertThatThrownBy(() -> factura.agregarLinea(null))
                .isInstanceOf(NullPointerException.class);
    }

    @Test
    void equals_ConMismoId_DeberiaSerIgual() {
        Factura factura1 = Factura.crear(
                facturaId,
                numeroFactura,
                fechaEmision,
                fechaVencimiento,
                periodoFacturado,
                total,
                estado,
                concepto,
                List.of(lineaFactura)
        );
        Factura factura2 = Factura.crear(
                facturaId,
                "OTRO-NUM",
                fechaEmision.plusDays(1),
                fechaVencimiento.plusDays(1),
                periodoFacturado,
                new Dinero(BigDecimal.ONE, "USD"),
                Factura.Estado.CANCELADA,
                "Otro concepto",
                List.of()
        );

        assertThat(factura1).isEqualTo(factura2);
        assertThat(factura1.hashCode()).isEqualTo(factura2.hashCode());
    }

    @Test
    void equals_ConDistintoId_DeberiaSerDiferente() {
        FacturaId otroId = new FacturaId(UUID.randomUUID());
        Factura factura1 = Factura.crear(
                facturaId,
                numeroFactura,
                fechaEmision,
                fechaVencimiento,
                periodoFacturado,
                total,
                estado,
                concepto,
                List.of(lineaFactura)
        );
        Factura factura2 = Factura.crear(
                otroId,
                numeroFactura,
                fechaEmision,
                fechaVencimiento,
                periodoFacturado,
                total,
                estado,
                concepto,
                List.of(lineaFactura)
        );

        assertThat(factura1).isNotEqualTo(factura2);
    }
}