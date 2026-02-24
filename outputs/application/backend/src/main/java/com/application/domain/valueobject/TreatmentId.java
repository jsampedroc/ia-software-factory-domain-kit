package com.application.domain.valueobject;

import com.application.domain.shared.ValueObject;
import java.util.UUID;

public record TreatmentId(UUID value) implements ValueObject {
    public TreatmentId {
        if (value == null) {
            throw new IllegalArgumentException("TreatmentId value cannot be null");
        }
    }
    
    public static TreatmentId generate() {
        return new TreatmentId(UUID.randomUUID());
    }
    
    public static TreatmentId fromString(String value) {
        return new TreatmentId(UUID.fromString(value));
    }
}