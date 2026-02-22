package com.application.infrastructure.persistence.jpa.facturacion;

import com.application.domain.model.facturacion.Factura;
import com.application.domain.model.facturacion.LineaFactura;
import com.application.domain.valueobject.facturacion.Dinero;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class LineaFacturaJpaEntityTest {

    private LineaFacturaJpaEntity lineaFacturaJpaEntity;

    @Mock
    private FacturaJpaEntity facturaJpaEntityMock;

    @Mock
    private Factura facturaDomainMock;

    private final UUID testId = UUID.randomUUID();
    private final UUID testFacturaId = UUID.randomUUID();
    private final String testConcepto = "Concepto de prueba";
    private final BigDecimal testCantidad = new BigDecimal("5.0");
    private final BigDecimal testPrecioUnitario = new BigDecimal("100.50");
    private final BigDecimal testImporte = new BigDecimal("502.50");

    @BeforeEach
    void setUp() {
        lineaFacturaJpaEntity = new LineaFacturaJpaEntity();
    }

    @Test
    void shouldMapFromDomainEntityCorrectly() {
        LineaFactura.Id lineaId = new LineaFactura.Id(testId);
        Factura.Id facturaId = new Factura.Id(testFacturaId);
        Dinero precioUnitarioDinero = new Dinero(testPrecioUnitario, "EUR");
        Dinero importeDinero = new Dinero(testImporte, "EUR");

        LineaFactura lineaFacturaDomain = LineaFactura.builder()
                .id(lineaId)
                .facturaId(facturaId)
                .concepto(testConcepto)
                .cantidad(testCantidad)
                .precioUnitario(precioUnitarioDinero)
                .importe(importeDinero)
                .build();

        when(facturaJpaEntityMock.getId()).thenReturn(testFacturaId);

        LineaFacturaJpaEntity result = LineaFacturaJpaEntity.from(lineaFacturaDomain, facturaJpaEntityMock);

        assertThat(result).isNotNull();
        assertThat(result.getId()).isEqualTo(testId);
        assertThat(result.getFactura()).isEqualTo(facturaJpaEntityMock);
        assertThat(result.getConcepto()).isEqualTo(testConcepto);
        assertThat(result.getCantidad()).isEqualByComparingTo(testCantidad);
        assertThat(result.getPrecioUnitario()).isEqualByComparingTo(testPrecioUnitario);
        assertThat(result.getDivisaPrecioUnitario()).isEqualTo("EUR");
        assertThat(result.getImporte()).isEqualByComparingTo(testImporte);
        assertThat(result.getDivisaImporte()).isEqualTo("EUR");
    }

    @Test
    void shouldMapToDomainEntityCorrectly() {
        lineaFacturaJpaEntity.setId(testId);
        lineaFacturaJpaEntity.setFactura(facturaJpaEntityMock);
        lineaFacturaJpaEntity.setConcepto(testConcepto);
        lineaFacturaJpaEntity.setCantidad(testCantidad);
        lineaFacturaJpaEntity.setPrecioUnitario(testPrecioUnitario);
        lineaFacturaJpaEntity.setDivisaPrecioUnitario("EUR");
        lineaFacturaJpaEntity.setImporte(testImporte);
        lineaFacturaJpaEntity.setDivisaImporte("EUR");

        when(facturaJpaEntityMock.getId()).thenReturn(testFacturaId);

        LineaFactura result = lineaFacturaJpaEntity.toDomain();

        assertThat(result).isNotNull();
        assertThat(result.getId().value()).isEqualTo(testId);
        assertThat(result.getFacturaId().value()).isEqualTo(testFacturaId);
        assertThat(result.getConcepto()).isEqualTo(testConcepto);
        assertThat(result.getCantidad()).isEqualByComparingTo(testCantidad);
        assertThat(result.getPrecioUnitario().cantidad()).isEqualByComparingTo(testPrecioUnitario);
        assertThat(result.getPrecioUnitario().divisa()).isEqualTo("EUR");
        assertThat(result.getImporte().cantidad()).isEqualByComparingTo(testImporte);
        assertThat(result.getImporte().divisa()).isEqualTo("EUR");
    }

    @Test
    void gettersAndSettersShouldWorkCorrectly() {
        lineaFacturaJpaEntity.setId(testId);
        lineaFacturaJpaEntity.setFactura(facturaJpaEntityMock);
        lineaFacturaJpaEntity.setConcepto(testConcepto);
        lineaFacturaJpaEntity.setCantidad(testCantidad);
        lineaFacturaJpaEntity.setPrecioUnitario(testPrecioUnitario);
        lineaFacturaJpaEntity.setDivisaPrecioUnitario("USD");
        lineaFacturaJpaEntity.setImporte(testImporte);
        lineaFacturaJpaEntity.setDivisaImporte("USD");

        assertThat(lineaFacturaJpaEntity.getId()).isEqualTo(testId);
        assertThat(lineaFacturaJpaEntity.getFactura()).isEqualTo(facturaJpaEntityMock);
        assertThat(lineaFacturaJpaEntity.getConcepto()).isEqualTo(testConcepto);
        assertThat(lineaFacturaJpaEntity.getCantidad()).isEqualByComparingTo(testCantidad);
        assertThat(lineaFacturaJpaEntity.getPrecioUnitario()).isEqualByComparingTo(testPrecioUnitario);
        assertThat(lineaFacturaJpaEntity.getDivisaPrecioUnitario()).isEqualTo("USD");
        assertThat(lineaFacturaJpaEntity.getImporte()).isEqualByComparingTo(testImporte);
        assertThat(lineaFacturaJpaEntity.getDivisaImporte()).isEqualTo("USD");
    }
}