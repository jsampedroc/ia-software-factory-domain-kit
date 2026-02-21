package com.application.domain.SchoolManagement.valueobject;

import com.application.domain.Shared.valueobject.ValueObject;

public record ClassroomId(String value) implements ValueObject {
    public ClassroomId {
        if (value == null || value.isBlank()) {
            throw new IllegalArgumentException("Classroom ID cannot be null or blank");
        }
    }
}