package com.application.domain.model.event;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(MockitoExtension.class)
class FacturaPagadaTest {

    @Test
    void shouldCreateFacturaPagadaEventWithCorrectAttributes() {
        UUID facturaId = UUID.randomUUID();
        LocalDateTime fechaPago = LocalDateTime.now();
        BigDecimal importePagado = new BigDecimal("150.75");

        FacturaPagada event = new FacturaPagada(facturaId, fechaPago, importePagado);

        assertThat(event.facturaId()).isEqualTo(facturaId);
        assertThat(event.fechaPago()).isEqualTo(fechaPago);
        assertThat(event.importePagado()).isEqualTo(importePagado);
    }

    @Test
    void shouldReturnSameValuesUsedInConstruction() {
        UUID facturaId = UUID.fromString("123e4567-e89b-12d3-a456-426614174000");
        LocalDateTime fechaPago = LocalDateTime.of(2024, 5, 15, 10, 30);
        BigDecimal importePagado = new BigDecimal("99.99");

        FacturaPagada event = new FacturaPagada(facturaId, fechaPago, importePagado);

        assertThat(event.facturaId()).isEqualTo(facturaId);
        assertThat(event.fechaPago()).isEqualTo(fechaPago);
        assertThat(event.importePagado()).isEqualTo(importePagado);
    }

    @Test
    void shouldBeEqualIfSameAttributeValues() {
        UUID facturaId = UUID.randomUUID();
        LocalDateTime fechaPago = LocalDateTime.now().minusDays(1);
        BigDecimal importePagado = new BigDecimal("200.00");

        FacturaPagada event1 = new FacturaPagada(facturaId, fechaPago, importePagado);
        FacturaPagada event2 = new FacturaPagada(facturaId, fechaPago, importePagado);

        assertThat(event1).isEqualTo(event2);
        assertThat(event1.hashCode()).isEqualTo(event2.hashCode());
    }

    @Test
    void shouldNotBeEqualIfDifferentFacturaId() {
        LocalDateTime fechaPago = LocalDateTime.now();
        BigDecimal importePagado = new BigDecimal("50.00");

        FacturaPagada event1 = new FacturaPagada(UUID.randomUUID(), fechaPago, importePagado);
        FacturaPagada event2 = new FacturaPagada(UUID.randomUUID(), fechaPago, importePagado);

        assertThat(event1).isNotEqualTo(event2);
    }

    @Test
    void shouldNotBeEqualIfDifferentFechaPago() {
        UUID facturaId = UUID.randomUUID();
        BigDecimal importePagado = new BigDecimal("50.00");
        LocalDateTime fechaPago1 = LocalDateTime.of(2024, 5, 15, 9, 0);
        LocalDateTime fechaPago2 = LocalDateTime.of(2024, 5, 16, 9, 0);

        FacturaPagada event1 = new FacturaPagada(facturaId, fechaPago1, importePagado);
        FacturaPagada event2 = new FacturaPagada(facturaId, fechaPago2, importePagado);

        assertThat(event1).isNotEqualTo(event2);
    }

    @Test
    void shouldNotBeEqualIfDifferentImportePagado() {
        UUID facturaId = UUID.randomUUID();
        LocalDateTime fechaPago = LocalDateTime.now();

        FacturaPagada event1 = new FacturaPagada(facturaId, fechaPago, new BigDecimal("100.00"));
        FacturaPagada event2 = new FacturaPagada(facturaId, fechaPago, new BigDecimal("100.01"));

        assertThat(event1).isNotEqualTo(event2);
    }

    @Test
    void shouldHaveInformativeToString() {
        UUID facturaId = UUID.fromString("550e8400-e29b-41d4-a716-446655440000");
        LocalDateTime fechaPago = LocalDateTime.of(2024, 5, 20, 14, 45);
        BigDecimal importePagado = new BigDecimal("300.50");

        FacturaPagada event = new FacturaPagada(facturaId, fechaPago, importePagado);
        String toStringResult = event.toString();

        assertThat(toStringResult).contains(facturaId.toString());
        assertThat(toStringResult).contains("2024-05-20T14:45");
        assertThat(toStringResult).contains("300.50");
    }
}