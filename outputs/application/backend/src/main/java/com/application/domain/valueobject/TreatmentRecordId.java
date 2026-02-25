package com.application.domain.valueobject;

import com.application.domain.shared.ValueObject;

public record TreatmentRecordId(String value) implements ValueObject {
    public TreatmentRecordId {
        if (value == null || value.isBlank()) {
            throw new IllegalArgumentException("TreatmentRecordId value cannot be null or blank");
        }
    }
}