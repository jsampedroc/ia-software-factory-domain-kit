package com.application.domain.Billing.valueobject;

import com.application.domain.Shared.valueobject.ValueObject;

public record BillingConceptId(String value) implements ValueObject {
    public BillingConceptId {
        if (value == null || value.isBlank()) {
            throw new IllegalArgumentException("BillingConceptId value cannot be null or blank");
        }
    }
}