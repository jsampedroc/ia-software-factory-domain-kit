package com.application.domain.valueobject;

import com.application.domain.shared.ValueObject;

public record AllergyId(String value) implements ValueObject {
    public AllergyId {
        if (value == null || value.isBlank()) {
            throw new IllegalArgumentException("AllergyId value cannot be null or blank");
        }
    }
}