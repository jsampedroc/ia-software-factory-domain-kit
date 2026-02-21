package com.application.domain.Billing.valueobject;

import com.application.domain.Shared.valueobject.ValueObject;

public record PaymentId(String value) implements ValueObject {
    public PaymentId {
        if (value == null || value.isBlank()) {
            throw new IllegalArgumentException("Payment ID cannot be null or blank");
        }
    }
}