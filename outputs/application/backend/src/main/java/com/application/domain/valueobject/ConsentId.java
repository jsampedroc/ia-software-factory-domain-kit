package com.application.domain.valueobject;

import com.application.domain.shared.ValueObject;

public record ConsentId(String value) implements ValueObject {
    public ConsentId {
        if (value == null || value.isBlank()) {
            throw new IllegalArgumentException("ConsentId value cannot be null or blank");
        }
    }
}