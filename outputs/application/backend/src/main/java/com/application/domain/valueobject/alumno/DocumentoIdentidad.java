package com.application.domain.valueobject.alumno;

import com.application.domain.shared.ValueObject;

public record DocumentoIdentidad(String tipo, String numero, String paisEmision) implements ValueObject {
    public DocumentoIdentidad {
        if (tipo == null || tipo.isBlank()) {
            throw new IllegalArgumentException("El tipo de documento no puede estar vacío");
        }
        if (numero == null || numero.isBlank()) {
            throw new IllegalArgumentException("El número de documento no puede estar vacío");
        }
        if (paisEmision == null || paisEmision.isBlank()) {
            throw new IllegalArgumentException("El país de emisión no puede estar vacío");
        }
    }
}