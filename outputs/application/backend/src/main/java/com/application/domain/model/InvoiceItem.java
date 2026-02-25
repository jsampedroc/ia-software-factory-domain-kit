package com.application.domain.model;

import com.application.domain.shared.ValueObject;
import com.application.domain.valueobject.InvoiceItemId;

import java.math.BigDecimal;
import java.util.Objects;

public record InvoiceItem(
        InvoiceItemId itemId,
        String treatmentCode,
        String description,
        int quantity,
        BigDecimal unitPrice,
        BigDecimal amount
) implements ValueObject {

    public InvoiceItem {
        Objects.requireNonNull(itemId, "Item ID cannot be null");
        Objects.requireNonNull(treatmentCode, "Treatment code cannot be null");
        Objects.requireNonNull(description, "Description cannot be null");
        Objects.requireNonNull(unitPrice, "Unit price cannot be null");
        Objects.requireNonNull(amount, "Amount cannot be null");

        if (treatmentCode.isBlank()) {
            throw new IllegalArgumentException("Treatment code cannot be blank");
        }
        if (description.isBlank()) {
            throw new IllegalArgumentException("Description cannot be blank");
        }
        if (quantity <= 0) {
            throw new IllegalArgumentException("Quantity must be positive");
        }
        if (unitPrice.compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException("Unit price cannot be negative");
        }
        if (amount.compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException("Amount cannot be negative");
        }
        // Business invariant: amount must equal quantity * unitPrice
        BigDecimal calculatedAmount = unitPrice.multiply(BigDecimal.valueOf(quantity));
        if (amount.compareTo(calculatedAmount) != 0) {
            throw new IllegalArgumentException("Amount must equal quantity * unitPrice");
        }
    }
}