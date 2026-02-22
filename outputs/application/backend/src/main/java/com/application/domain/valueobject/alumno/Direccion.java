package com.application.domain.valueobject.alumno;

import com.application.domain.shared.ValueObject;

public record Direccion(
        String linea1,
        String linea2,
        String codigoPostal,
        String ciudad,
        String provincia
) implements ValueObject {
    public Direccion {
        if (linea1 == null || linea1.isBlank()) {
            throw new IllegalArgumentException("La línea 1 de la dirección es obligatoria.");
        }
        if (codigoPostal == null || codigoPostal.isBlank()) {
            throw new IllegalArgumentException("El código postal es obligatorio.");
        }
        if (ciudad == null || ciudad.isBlank()) {
            throw new IllegalArgumentException("La ciudad es obligatoria.");
        }
        if (provincia == null || provincia.isBlank()) {
            throw new IllegalArgumentException("La provincia es obligatoria.");
        }
    }
}