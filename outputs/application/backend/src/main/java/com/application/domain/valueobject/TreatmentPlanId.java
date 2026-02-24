package com.application.domain.valueobject;

import com.application.domain.shared.ValueObject;
import java.util.UUID;

public record TreatmentPlanId(UUID value) implements ValueObject {
    public TreatmentPlanId {
        if (value == null) {
            throw new IllegalArgumentException("TreatmentPlanId value cannot be null");
        }
    }

    public static TreatmentPlanId generate() {
        return new TreatmentPlanId(UUID.randomUUID());
    }

    public static TreatmentPlanId fromString(String value) {
        try {
            return new TreatmentPlanId(UUID.fromString(value));
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Invalid TreatmentPlanId format: " + value, e);
        }
    }
}