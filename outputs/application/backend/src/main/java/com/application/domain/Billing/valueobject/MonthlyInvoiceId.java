package com.application.domain.Billing.valueobject;

import com.application.domain.Shared.ValueObject;

public record MonthlyInvoiceId(String value) implements ValueObject {
    public MonthlyInvoiceId {
        if (value == null || value.isBlank()) {
            throw new IllegalArgumentException("MonthlyInvoiceId value cannot be null or blank");
        }
    }
}