package com.application.domain.valueobject;

import com.application.domain.shared.ValueObject;

public record UserId(String value) implements ValueObject {
    public UserId {
        if (value == null || value.isBlank()) {
            throw new IllegalArgumentException("User ID value cannot be null or blank");
        }
    }
}