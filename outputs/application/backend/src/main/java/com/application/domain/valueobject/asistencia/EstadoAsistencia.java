package com.application.domain.valueobject.asistencia;

import com.application.domain.shared.ValueObject;

public record EstadoAsistencia(String valor) implements ValueObject {
    public EstadoAsistencia {
        if (valor == null || valor.isBlank()) {
            throw new IllegalArgumentException("El estado de asistencia no puede estar vacío.");
        }
        if (!valor.equals("PRESENTE") && !valor.equals("AUSENTE") && !valor.equals("JUSTIFICADO") && !valor.equals("RETRASO")) {
            throw new IllegalArgumentException("El estado de asistencia debe ser uno de los valores predefinidos: PRESENTE, AUSENTE, JUSTIFICADO, RETRASO.");
        }
    }
}