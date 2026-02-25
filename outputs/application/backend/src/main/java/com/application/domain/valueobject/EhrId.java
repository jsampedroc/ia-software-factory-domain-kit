package com.application.domain.valueobject;

import com.application.domain.shared.ValueObject;

public record EhrId(String value) implements ValueObject {
    public EhrId {
        if (value == null || value.isBlank()) {
            throw new IllegalArgumentException("EhrId value cannot be null or blank");
        }
    }
}