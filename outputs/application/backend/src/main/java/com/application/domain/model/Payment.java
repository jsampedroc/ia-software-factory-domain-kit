package com.application.domain.model;

import com.application.domain.shared.Entity;
import com.application.domain.valueobject.PaymentId;
import com.application.domain.valueobject.InvoiceId;
import com.application.domain.enums.PaymentMethod;
import lombok.Getter;
import lombok.ToString;
import lombok.experimental.SuperBuilder;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@ToString(callSuper = true)
@SuperBuilder
public class Payment extends Entity<PaymentId> {

    private final InvoiceId invoiceId;
    private final BigDecimal amount;
    private final PaymentMethod method;
    private final LocalDateTime paidAt;
    private final String referenceNumber;

    public Payment(PaymentId paymentId, InvoiceId invoiceId, BigDecimal amount, PaymentMethod method, LocalDateTime paidAt, String referenceNumber) {
        super(paymentId);
        this.invoiceId = invoiceId;
        this.amount = amount;
        this.method = method;
        this.paidAt = paidAt;
        this.referenceNumber = referenceNumber;
        validate();
    }

    private void validate() {
        if (invoiceId == null) {
            throw new IllegalArgumentException("Invoice ID cannot be null");
        }
        if (amount == null || amount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Payment amount must be positive");
        }
        if (method == null) {
            throw new IllegalArgumentException("Payment method cannot be null");
        }
        if (paidAt == null) {
            throw new IllegalArgumentException("Paid date cannot be null");
        }
        if (referenceNumber == null || referenceNumber.isBlank()) {
            throw new IllegalArgumentException("Reference number cannot be blank");
        }
    }
}