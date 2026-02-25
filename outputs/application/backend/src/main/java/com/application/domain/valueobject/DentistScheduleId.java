package com.application.domain.valueobject;

import com.application.domain.shared.ValueObject;

public record DentistScheduleId(String value) implements ValueObject {
    public DentistScheduleId {
        if (value == null || value.isBlank()) {
            throw new IllegalArgumentException("DentistScheduleId value cannot be null or blank");
        }
    }
}