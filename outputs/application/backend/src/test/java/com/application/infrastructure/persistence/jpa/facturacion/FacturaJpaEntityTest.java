package com.application.infrastructure.persistence.jpa.facturacion;

import com.application.domain.model.facturacion.Factura;
import com.application.domain.model.facturacion.LineaFactura;
import com.application.domain.valueobject.facturacion.Dinero;
import com.application.domain.valueobject.facturacion.Periodo;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Currency;
import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class FacturaJpaEntityTest {

    private FacturaJpaEntity facturaJpaEntity;

    @Mock
    private Factura domainFactura;

    @Mock
    private Factura.FacturaId domainFacturaId;

    @Mock
    private LineaFactura domainLinea;

    @Mock
    private LineaFactura.LineaFacturaId domainLineaId;

    @Mock
    private Dinero dineroTotal;

    @Mock
    private Periodo periodo;

    private final UUID testFacturaId = UUID.randomUUID();
    private final UUID testLineaId = UUID.randomUUID();
    private final LocalDateTime testFechaEmision = LocalDateTime.now().minusDays(1);
    private final LocalDate testFechaVencimiento = LocalDate.now().plusDays(30);
    private final LocalDate testPeriodoInicio = LocalDate.now().minusMonths(1);
    private final LocalDate testPeriodoFin = LocalDate.now();
    private final BigDecimal testCantidad = new BigDecimal("1");
    private final BigDecimal testPrecioUnitario = new BigDecimal("100.50");
    private final BigDecimal testImporte = new BigDecimal("100.50");
    private final BigDecimal testTotal = new BigDecimal("100.50");

    @BeforeEach
    void setUp() {
        facturaJpaEntity = new FacturaJpaEntity();
    }

    @Test
    void fromDomainEntity_ShouldMapAllFieldsCorrectly() {
        when(domainFactura.getId()).thenReturn(domainFacturaId);
        when(domainFacturaId.value()).thenReturn(testFacturaId);
        when(domainFactura.getNumeroFactura()).thenReturn("FAC-2024-001");
        when(domainFactura.getFechaEmision()).thenReturn(testFechaEmision);
        when(domainFactura.getFechaVencimiento()).thenReturn(testFechaVencimiento);
        when(domainFactura.getPeriodoFacturado()).thenReturn(periodo);
        when(periodo.fechaInicio()).thenReturn(testPeriodoInicio);
        when(periodo.fechaFin()).thenReturn(testPeriodoFin);
        when(domainFactura.getTotal()).thenReturn(dineroTotal);
        when(dineroTotal.cantidad()).thenReturn(testTotal);
        when(dineroTotal.divisa()).thenReturn(Currency.getInstance("EUR"));
        when(domainFactura.getEstado()).thenReturn("GENERADA");
        when(domainFactura.getConcepto()).thenReturn("Mensualidad Enero");
        when(domainFactura.getLineas()).thenReturn(List.of(domainLinea));

        when(domainLinea.getId()).thenReturn(domainLineaId);
        when(domainLineaId.value()).thenReturn(testLineaId);
        when(domainLinea.getConcepto()).thenReturn("Clase Regular");
        when(domainLinea.getCantidad()).thenReturn(testCantidad);
        when(domainLinea.getPrecioUnitario()).thenReturn(testPrecioUnitario);
        when(domainLinea.getImporte()).thenReturn(testImporte);

        FacturaJpaEntity result = FacturaJpaEntity.fromDomainEntity(domainFactura);

        assertThat(result).isNotNull();
        assertThat(result.getId()).isEqualTo(testFacturaId);
        assertThat(result.getNumeroFactura()).isEqualTo("FAC-2024-001");
        assertThat(result.getFechaEmision()).isEqualTo(testFechaEmision);
        assertThat(result.getFechaVencimiento()).isEqualTo(testFechaVencimiento);
        assertThat(result.getPeriodoFacturadoInicio()).isEqualTo(testPeriodoInicio);
        assertThat(result.getPeriodoFacturadoFin()).isEqualTo(testPeriodoFin);
        assertThat(result.getTotal()).isEqualTo(testTotal);
        assertThat(result.getDivisa()).isEqualTo("EUR");
        assertThat(result.getEstado()).isEqualTo("GENERADA");
        assertThat(result.getConcepto()).isEqualTo("Mensualidad Enero");

        assertThat(result.getLineas()).hasSize(1);
        LineaFacturaJpaEntity lineaJpa = result.getLineas().get(0);
        assertThat(lineaJpa.getId()).isEqualTo(testLineaId);
        assertThat(lineaJpa.getConcepto()).isEqualTo("Clase Regular");
        assertThat(lineaJpa.getCantidad()).isEqualTo(testCantidad);
        assertThat(lineaJpa.getPrecioUnitario()).isEqualTo(testPrecioUnitario);
        assertThat(lineaJpa.getImporte()).isEqualTo(testImporte);
        assertThat(lineaJpa.getFactura()).isSameAs(result);
    }

    @Test
    void fromDomainEntity_ShouldHandleNullLineas() {
        when(domainFactura.getId()).thenReturn(domainFacturaId);
        when(domainFacturaId.value()).thenReturn(testFacturaId);
        when(domainFactura.getNumeroFactura()).thenReturn("FAC-2024-002");
        when(domainFactura.getFechaEmision()).thenReturn(testFechaEmision);
        when(domainFactura.getFechaVencimiento()).thenReturn(testFechaVencimiento);
        when(domainFactura.getPeriodoFacturado()).thenReturn(periodo);
        when(periodo.fechaInicio()).thenReturn(testPeriodoInicio);
        when(periodo.fechaFin()).thenReturn(testPeriodoFin);
        when(domainFactura.getTotal()).thenReturn(dineroTotal);
        when(dineroTotal.cantidad()).thenReturn(BigDecimal.ZERO);
        when(dineroTotal.divisa()).thenReturn(Currency.getInstance("USD"));
        when(domainFactura.getEstado()).thenReturn("PENDIENTE");
        when(domainFactura.getConcepto()).thenReturn("Tarifa Administrativa");
        when(domainFactura.getLineas()).thenReturn(null);

        FacturaJpaEntity result = FacturaJpaEntity.fromDomainEntity(domainFactura);

        assertThat(result).isNotNull();
        assertThat(result.getId()).isEqualTo(testFacturaId);
        assertThat(result.getNumeroFactura()).isEqualTo("FAC-2024-002");
        assertThat(result.getTotal()).isEqualTo(BigDecimal.ZERO);
        assertThat(result.getDivisa()).isEqualTo("USD");
        assertThat(result.getEstado()).isEqualTo("PENDIENTE");
        assertThat(result.getLineas()).isNull();
    }

    @Test
    void toDomainEntity_ShouldMapAllFieldsCorrectly() {
        facturaJpaEntity.setId(testFacturaId);
        facturaJpaEntity.setNumeroFactura("FAC-2024-003");
        facturaJpaEntity.setFechaEmision(testFechaEmision);
        facturaJpaEntity.setFechaVencimiento(testFechaVencimiento);
        facturaJpaEntity.setPeriodoFacturadoInicio(testPeriodoInicio);
        facturaJpaEntity.setPeriodoFacturadoFin(testPeriodoFin);
        facturaJpaEntity.setTotal(testTotal);
        facturaJpaEntity.setDivisa("EUR");
        facturaJpaEntity.setEstado("ENVIADA");
        facturaJpaEntity.setConcepto("Mensualidad Febrero");

        LineaFacturaJpaEntity lineaJpa = new LineaFacturaJpaEntity();
        lineaJpa.setId(testLineaId);
        lineaJpa.setConcepto("Clase Avanzada");
        lineaJpa.setCantidad(testCantidad);
        lineaJpa.setPrecioUnitario(testPrecioUnitario);
        lineaJpa.setImporte(testImporte);
        lineaJpa.setFactura(facturaJpaEntity);
        facturaJpaEntity.setLineas(List.of(lineaJpa));

        Factura result = facturaJpaEntity.toDomainEntity();

        assertThat(result).isNotNull();
        assertThat(result.getId().value()).isEqualTo(testFacturaId);
        assertThat(result.getNumeroFactura()).isEqualTo("FAC-2024-003");
        assertThat(result.getFechaEmision()).isEqualTo(testFechaEmision);
        assertThat(result.getFechaVencimiento()).isEqualTo(testFechaVencimiento);
        assertThat(result.getPeriodoFacturado().fechaInicio()).isEqualTo(testPeriodoInicio);
        assertThat(result.getPeriodoFacturado().fechaFin()).isEqualTo(testPeriodoFin);
        assertThat(result.getTotal().cantidad()).isEqualTo(testTotal);
        assertThat(result.getTotal().divisa().getCurrencyCode()).isEqualTo("EUR");
        assertThat(result.getEstado()).isEqualTo("ENVIADA");
        assertThat(result.getConcepto()).isEqualTo("Mensualidad Febrero");

        assertThat(result.getLineas()).hasSize(1);
        LineaFactura resultLinea = result.getLineas().get(0);
        assertThat(resultLinea.getId().value()).isEqualTo(testLineaId);
        assertThat(resultLinea.getConcepto()).isEqualTo("Clase Avanzada");
        assertThat(resultLinea.getCantidad()).isEqualTo(testCantidad);
        assertThat(resultLinea.getPrecioUnitario()).isEqualTo(testPrecioUnitario);
        assertThat(resultLinea.getImporte()).isEqualTo(testImporte);
    }

    @Test
    void toDomainEntity_ShouldHandleEmptyLineas() {
        facturaJpaEntity.setId(testFacturaId);
        facturaJpaEntity.setNumeroFactura("FAC-2024-004");
        facturaJpaEntity.setFechaEmision(testFechaEmision);
        facturaJpaEntity.setFechaVencimiento(testFechaVencimiento);
        facturaJpaEntity.setPeriodoFacturadoInicio(testPeriodoInicio);
        facturaJpaEntity.setPeriodoFacturadoFin(testPeriodoFin);
        facturaJpaEntity.setTotal(BigDecimal.ZERO);
        facturaJpaEntity.setDivisa("USD");
        facturaJpaEntity.setEstado("CANCELADA");
        facturaJpaEntity.setConcepto("Factura Anulada");
        facturaJpaEntity.setLineas(List.of());

        Factura result = facturaJpaEntity.toDomainEntity();

        assertThat(result).isNotNull();
        assertThat(result.getId().value()).isEqualTo(testFacturaId);
        assertThat(result.getNumeroFactura()).isEqualTo("FAC-2024-004");
        assertThat(result.getTotal().cantidad()).isEqualTo(BigDecimal.ZERO);
        assertThat(result.getEstado()).isEqualTo("CANCELADA");
        assertThat(result.getLineas()).isEmpty();
    }

    @Test
    void gettersAndSetters_ShouldWorkCorrectly() {
        facturaJpaEntity.setId(testFacturaId);
        facturaJpaEntity.setNumeroFactura("TEST-001");
        facturaJpaEntity.setFechaEmision(testFechaEmision);
        facturaJpaEntity.setFechaVencimiento(testFechaVencimiento);
        facturaJpaEntity.setPeriodoFacturadoInicio(testPeriodoInicio);
        facturaJpaEntity.setPeriodoFacturadoFin(testPeriodoFin);
        facturaJpaEntity.setTotal(testTotal);
        facturaJpaEntity.setDivisa("GBP");
        facturaJpaEntity.setEstado("PAGADA");
        facturaJpaEntity.setConcepto("Test Concept");

        assertThat(facturaJpaEntity.getId()).isEqualTo(testFacturaId);
        assertThat(facturaJpaEntity.getNumeroFactura()).isEqualTo("TEST-001");
        assertThat(facturaJpaEntity.getFechaEmision()).isEqualTo(testFechaEmision);
        assertThat(facturaJpaEntity.getFechaVencimiento()).isEqualTo(testFechaVencimiento);
        assertThat(facturaJpaEntity.getPeriodoFacturadoInicio()).isEqualTo(testPeriodoInicio);
        assertThat(facturaJpaEntity.getPeriodoFacturadoFin()).isEqualTo(testPeriodoFin);
        assertThat(facturaJpaEntity.getTotal()).isEqualTo(testTotal);
        assertThat(facturaJpaEntity.getDivisa()).isEqualTo("GBP");
        assertThat(facturaJpaEntity.getEstado()).isEqualTo("PAGADA");
        assertThat(facturaJpaEntity.getConcepto()).isEqualTo("Test Concept");
    }
}