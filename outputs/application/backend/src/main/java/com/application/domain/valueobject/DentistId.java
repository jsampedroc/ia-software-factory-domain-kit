package com.application.domain.valueobject;

import com.application.domain.shared.ValueObject;

import java.util.UUID;

public record DentistId(UUID value) implements ValueObject {
    public DentistId {
        if (value == null) {
            throw new IllegalArgumentException("DentistId value cannot be null");
        }
    }

    public static DentistId generate() {
        return new DentistId(UUID.randomUUID());
    }

    public static DentistId fromString(String value) {
        try {
            return new DentistId(UUID.fromString(value));
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Invalid DentistId format: " + value, e);
        }
    }
}