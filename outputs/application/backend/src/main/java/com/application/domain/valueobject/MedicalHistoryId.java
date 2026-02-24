package com.application.domain.valueobject;

import com.application.domain.shared.ValueObject;
import java.util.UUID;

public record MedicalHistoryId(UUID value) implements ValueObject {
    public MedicalHistoryId {
        if (value == null) {
            throw new IllegalArgumentException("MedicalHistoryId value cannot be null");
        }
    }

    public static MedicalHistoryId generate() {
        return new MedicalHistoryId(UUID.randomUUID());
    }

    public static MedicalHistoryId fromString(String value) {
        return new MedicalHistoryId(UUID.fromString(value));
    }
}