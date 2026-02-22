package com.application.domain.valueobject.colegio;

import com.application.domain.shared.ValueObject;

public record CodigoCentroEducativo(String codigo) implements ValueObject {
    public CodigoCentroEducativo {
        if (codigo == null || codigo.isBlank()) {
            throw new IllegalArgumentException("El código del centro educativo no puede estar vacío.");
        }
        // Aquí se podrían añadir más validaciones específicas del formato oficial
    }
}