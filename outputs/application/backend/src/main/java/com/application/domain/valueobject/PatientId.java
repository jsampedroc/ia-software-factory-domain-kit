package com.application.domain.valueobject;

import com.application.domain.shared.ValueObject;

public record PatientId(String value) implements ValueObject {
    public PatientId {
        if (value == null || value.isBlank()) {
            throw new IllegalArgumentException("Patient ID value cannot be null or blank");
        }
    }
}