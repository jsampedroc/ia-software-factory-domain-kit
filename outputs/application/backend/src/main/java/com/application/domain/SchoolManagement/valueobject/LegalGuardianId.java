package com.application.domain.SchoolManagement.valueobject;

import com.application.domain.Shared.ValueObject;

public record LegalGuardianId(String value) implements ValueObject {
    public LegalGuardianId {
        if (value == null || value.isBlank()) {
            throw new IllegalArgumentException("LegalGuardianId cannot be null or blank");
        }
    }
}