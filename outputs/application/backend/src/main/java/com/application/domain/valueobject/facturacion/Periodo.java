package com.application.domain.valueobject.facturacion;

import com.application.domain.shared.ValueObject;

import java.time.LocalDate;
import java.util.Objects;

public record Periodo(LocalDate fechaInicio, LocalDate fechaFin) implements ValueObject {

    public Periodo {
        Objects.requireNonNull(fechaInicio, "La fecha de inicio no puede ser nula");
        Objects.requireNonNull(fechaFin, "La fecha de fin no puede ser nula");
        if (fechaInicio.isAfter(fechaFin)) {
            throw new IllegalArgumentException("La fecha de inicio debe ser anterior o igual a la fecha de fin");
        }
    }
}