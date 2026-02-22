package com.application.domain.model.event;

import com.application.domain.model.facturacion.Factura;
import com.application.domain.model.facturacion.FacturaId;
import com.application.domain.model.alumno.AlumnoId;
import com.application.domain.valueobject.facturacion.Dinero;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class FacturaGeneradaTest {

    @Test
    void shouldCreateEventWithCorrectAttributes() {
        FacturaId facturaId = new FacturaId(UUID.randomUUID());
        String numeroFactura = "FAC-2024-001";
        AlumnoId alumnoId = new AlumnoId(UUID.randomUUID());
        Dinero total = new Dinero(new BigDecimal("150.50"), "EUR");
        LocalDate fechaVencimiento = LocalDate.now().plusDays(30);

        FacturaGenerada event = new FacturaGenerada(facturaId, numeroFactura, alumnoId, total, fechaVencimiento);

        assertThat(event.facturaId()).isEqualTo(facturaId);
        assertThat(event.numeroFactura()).isEqualTo(numeroFactura);
        assertThat(event.alumnoId()).isEqualTo(alumnoId);
        assertThat(event.total()).isEqualTo(total);
        assertThat(event.fechaVencimiento()).isEqualTo(fechaVencimiento);
        assertThat(event.occurredOn()).isNotNull();
    }

    @Test
    void shouldReturnCorrectEventName() {
        FacturaId facturaId = new FacturaId(UUID.randomUUID());
        String numeroFactura = "FAC-2024-001";
        AlumnoId alumnoId = new AlumnoId(UUID.randomUUID());
        Dinero total = new Dinero(new BigDecimal("150.50"), "EUR");
        LocalDate fechaVencimiento = LocalDate.now().plusDays(30);

        FacturaGenerada event = new FacturaGenerada(facturaId, numeroFactura, alumnoId, total, fechaVencimiento);

        assertThat(event.eventName()).isEqualTo("factura.generada");
    }

    @Test
    void shouldCreateEventFromFacturaAggregate() {
        FacturaId facturaId = new FacturaId(UUID.randomUUID());
        String numeroFactura = "FAC-2024-001";
        AlumnoId alumnoId = new AlumnoId(UUID.randomUUID());
        Dinero total = new Dinero(new BigDecimal("150.50"), "EUR");
        LocalDate fechaVencimiento = LocalDate.now().plusDays(30);

        Factura factura = Factura.builder()
                .id(facturaId)
                .numeroFactura(numeroFactura)
                .alumnoId(alumnoId)
                .total(total)
                .fechaVencimiento(fechaVencimiento)
                .build();

        FacturaGenerada event = FacturaGenerada.from(factura);

        assertThat(event.facturaId()).isEqualTo(facturaId);
        assertThat(event.numeroFactura()).isEqualTo(numeroFactura);
        assertThat(event.alumnoId()).isEqualTo(alumnoId);
        assertThat(event.total()).isEqualTo(total);
        assertThat(event.fechaVencimiento()).isEqualTo(fechaVencimiento);
        assertThat(event.occurredOn()).isNotNull();
    }

    @Test
    void shouldThrowExceptionWhenCreatingEventWithNullFacturaId() {
        String numeroFactura = "FAC-2024-001";
        AlumnoId alumnoId = new AlumnoId(UUID.randomUUID());
        Dinero total = new Dinero(new BigDecimal("150.50"), "EUR");
        LocalDate fechaVencimiento = LocalDate.now().plusDays(30);

        assertThrows(NullPointerException.class, () ->
                new FacturaGenerada(null, numeroFactura, alumnoId, total, fechaVencimiento)
        );
    }

    @Test
    void shouldThrowExceptionWhenCreatingEventWithNullNumeroFactura() {
        FacturaId facturaId = new FacturaId(UUID.randomUUID());
        AlumnoId alumnoId = new AlumnoId(UUID.randomUUID());
        Dinero total = new Dinero(new BigDecimal("150.50"), "EUR");
        LocalDate fechaVencimiento = LocalDate.now().plusDays(30);

        assertThrows(NullPointerException.class, () ->
                new FacturaGenerada(facturaId, null, alumnoId, total, fechaVencimiento)
        );
    }

    @Test
    void shouldThrowExceptionWhenCreatingEventWithNullAlumnoId() {
        FacturaId facturaId = new FacturaId(UUID.randomUUID());
        String numeroFactura = "FAC-2024-001";
        Dinero total = new Dinero(new BigDecimal("150.50"), "EUR");
        LocalDate fechaVencimiento = LocalDate.now().plusDays(30);

        assertThrows(NullPointerException.class, () ->
                new FacturaGenerada(facturaId, numeroFactura, null, total, fechaVencimiento)
        );
    }

    @Test
    void shouldThrowExceptionWhenCreatingEventWithNullTotal() {
        FacturaId facturaId = new FacturaId(UUID.randomUUID());
        String numeroFactura = "FAC-2024-001";
        AlumnoId alumnoId = new AlumnoId(UUID.randomUUID());
        LocalDate fechaVencimiento = LocalDate.now().plusDays(30);

        assertThrows(NullPointerException.class, () ->
                new FacturaGenerada(facturaId, numeroFactura, alumnoId, null, fechaVencimiento)
        );
    }

    @Test
    void shouldThrowExceptionWhenCreatingEventWithNullFechaVencimiento() {
        FacturaId facturaId = new FacturaId(UUID.randomUUID());
        String numeroFactura = "FAC-2024-001";
        AlumnoId alumnoId = new AlumnoId(UUID.randomUUID());
        Dinero total = new Dinero(new BigDecimal("150.50"), "EUR");

        assertThrows(NullPointerException.class, () ->
                new FacturaGenerada(facturaId, numeroFactura, alumnoId, total, null)
        );
    }

    @Test
    void shouldBeEqualWhenSameAttributes() {
        FacturaId facturaId = new FacturaId(UUID.randomUUID());
        String numeroFactura = "FAC-2024-001";
        AlumnoId alumnoId = new AlumnoId(UUID.randomUUID());
        Dinero total = new Dinero(new BigDecimal("150.50"), "EUR");
        LocalDate fechaVencimiento = LocalDate.now().plusDays(30);

        FacturaGenerada event1 = new FacturaGenerada(facturaId, numeroFactura, alumnoId, total, fechaVencimiento);
        FacturaGenerada event2 = new FacturaGenerada(facturaId, numeroFactura, alumnoId, total, fechaVencimiento);

        assertThat(event1).isEqualTo(event2);
        assertThat(event1.hashCode()).isEqualTo(event2.hashCode());
    }

    @Test
    void shouldNotBeEqualWhenDifferentAttributes() {
        FacturaId facturaId1 = new FacturaId(UUID.randomUUID());
        FacturaId facturaId2 = new FacturaId(UUID.randomUUID());
        String numeroFactura = "FAC-2024-001";
        AlumnoId alumnoId = new AlumnoId(UUID.randomUUID());
        Dinero total = new Dinero(new BigDecimal("150.50"), "EUR");
        LocalDate fechaVencimiento = LocalDate.now().plusDays(30);

        FacturaGenerada event1 = new FacturaGenerada(facturaId1, numeroFactura, alumnoId, total, fechaVencimiento);
        FacturaGenerada event2 = new FacturaGenerada(facturaId2, numeroFactura, alumnoId, total, fechaVencimiento);

        assertThat(event1).isNotEqualTo(event2);
    }
}