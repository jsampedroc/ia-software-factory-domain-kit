package com.application.domain.valueobject;

import com.application.domain.shared.ValueObject;
import java.util.UUID;

public record PatientId(UUID value) implements ValueObject {
    public PatientId {
        if (value == null) {
            throw new IllegalArgumentException("PatientId value cannot be null");
        }
    }
    
    public static PatientId generate() {
        return new PatientId(UUID.randomUUID());
    }
    
    public static PatientId fromString(String uuid) {
        return new PatientId(UUID.fromString(uuid));
    }
}