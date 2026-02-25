package com.application.domain.valueobject;

import com.application.domain.shared.ValueObject;

public record PaymentId(String value) implements ValueObject {
    public PaymentId {
        if (value == null || value.isBlank()) {
            throw new IllegalArgumentException("Payment ID cannot be null or blank");
        }
    }
}