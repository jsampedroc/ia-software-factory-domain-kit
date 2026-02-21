package com.application.domain.shared.valueobject;

import com.application.domain.shared.ValueObject;

public record Money(java.math.BigDecimal amount, String currency) implements ValueObject {
    public Money {
        if (amount == null) {
            throw new IllegalArgumentException("Amount cannot be null");
        }
        if (amount.compareTo(java.math.BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException("Amount cannot be negative");
        }
        if (currency == null || currency.isBlank()) {
            throw new IllegalArgumentException("Currency cannot be null or blank");
        }
        if (!currency.matches("[A-Z]{3}")) {
            throw new IllegalArgumentException("Currency must be a valid ISO 4217 code (e.g., USD)");
        }
    }
}