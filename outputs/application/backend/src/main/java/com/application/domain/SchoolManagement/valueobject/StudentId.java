package com.application.domain.SchoolManagement.valueobject;

import com.application.domain.Shared.ValueObject;

public record StudentId(String value) implements ValueObject {
    public StudentId {
        if (value == null || value.isBlank()) {
            throw new IllegalArgumentException("Student ID cannot be null or blank");
        }
    }
}