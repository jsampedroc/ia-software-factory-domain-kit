package com.application.domain.valueobject;

import com.application.domain.shared.ValueObject;

public record PaymentPlanId(String value) implements ValueObject {
    public PaymentPlanId {
        if (value == null || value.isBlank()) {
            throw new IllegalArgumentException("PaymentPlanId value cannot be null or blank");
        }
    }
}