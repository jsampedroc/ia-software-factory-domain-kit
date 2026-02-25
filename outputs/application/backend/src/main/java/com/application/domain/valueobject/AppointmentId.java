package com.application.domain.valueobject;

import com.application.domain.shared.ValueObject;

public record AppointmentId(String value) implements ValueObject {
    public AppointmentId {
        if (value == null || value.isBlank()) {
            throw new IllegalArgumentException("AppointmentId value cannot be null or blank");
        }
    }
}