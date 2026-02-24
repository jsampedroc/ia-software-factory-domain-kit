package com.application.domain.valueobject;

import com.application.domain.shared.ValueObject;

import java.util.UUID;

public record ClinicId(UUID value) implements ValueObject {
    public ClinicId {
        if (value == null) {
            throw new IllegalArgumentException("ClinicId value cannot be null");
        }
    }

    public static ClinicId generate() {
        return new ClinicId(UUID.randomUUID());
    }

    public static ClinicId fromString(String value) {
        try {
            return new ClinicId(UUID.fromString(value));
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Invalid UUID format for ClinicId: " + value, e);
        }
    }
}