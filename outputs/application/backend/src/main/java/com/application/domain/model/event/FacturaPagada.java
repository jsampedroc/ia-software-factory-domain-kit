package com.application.domain.model.event;

import com.application.domain.shared.Entity;
import com.application.domain.shared.ValueObject;

import java.time.LocalDateTime;
import java.util.UUID;

public record FacturaPagada(
        UUID facturaId,
        LocalDateTime fechaPago,
        Dinero importePagado
) implements Entity<FacturaPagada.Id> {
    @Override
    public Id id() {
        return new Id(facturaId);
    }

    public record Id(UUID value) implements ValueObject {
    }

    public FacturaPagada {
        if (facturaId == null) {
            throw new IllegalArgumentException("El ID de la factura no puede ser nulo");
        }
        if (fechaPago == null) {
            throw new IllegalArgumentException("La fecha de pago no puede ser nula");
        }
        if (importePagado == null) {
            throw new IllegalArgumentException("El importe pagado no puede ser nulo");
        }
        if (fechaPago.isAfter(LocalDateTime.now())) {
            throw new IllegalArgumentException("La fecha de pago no puede ser futura");
        }
    }
}