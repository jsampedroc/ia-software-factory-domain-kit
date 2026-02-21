package com.application.domain.Billing.valueobject;

import com.application.domain.Shared.ValueObject;

public record InvoiceItemId(String value) implements ValueObject {
    public InvoiceItemId {
        if (value == null || value.isBlank()) {
            throw new IllegalArgumentException("InvoiceItemId value cannot be null or blank");
        }
    }
}