package com.application.domain.SchoolManagement.valueobject;

import com.application.domain.Shared.ValueObject;

public record SchoolId(String value) implements ValueObject {
    public SchoolId {
        if (value == null || value.isBlank()) {
            throw new IllegalArgumentException("School ID cannot be null or blank");
        }
    }
}