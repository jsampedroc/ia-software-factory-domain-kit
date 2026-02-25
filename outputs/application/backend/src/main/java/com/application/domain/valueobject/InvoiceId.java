package com.application.domain.valueobject;

import com.application.domain.shared.ValueObject;

public record InvoiceId(String value) implements ValueObject {
    public InvoiceId {
        if (value == null || value.isBlank()) {
            throw new IllegalArgumentException("InvoiceId value cannot be null or blank");
        }
    }
}