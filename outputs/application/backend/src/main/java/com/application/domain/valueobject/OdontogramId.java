package com.application.domain.valueobject;

import com.application.domain.shared.ValueObject;

public record OdontogramId(String value) implements ValueObject {
    public OdontogramId {
        if (value == null || value.isBlank()) {
            throw new IllegalArgumentException("OdontogramId value cannot be null or blank");
        }
    }
}