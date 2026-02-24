package com.application.domain.valueobject;

import com.application.domain.shared.ValueObject;
import java.util.UUID;

public record SpecialtyId(UUID value) implements ValueObject {
    public SpecialtyId {
        if (value == null) {
            throw new IllegalArgumentException("SpecialtyId value cannot be null");
        }
    }

    public static SpecialtyId generate() {
        return new SpecialtyId(UUID.randomUUID());
    }

    public static SpecialtyId fromString(String value) {
        return new SpecialtyId(UUID.fromString(value));
    }
}