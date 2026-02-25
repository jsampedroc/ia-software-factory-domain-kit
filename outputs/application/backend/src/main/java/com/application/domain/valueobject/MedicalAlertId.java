package com.application.domain.valueobject;

import com.application.domain.shared.ValueObject;

public record MedicalAlertId(String value) implements ValueObject {
    public MedicalAlertId {
        if (value == null || value.isBlank()) {
            throw new IllegalArgumentException("MedicalAlertId value cannot be null or blank");
        }
    }
}