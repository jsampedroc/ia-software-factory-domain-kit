package com.application.domain.Billing.payment.domain;

import com.application.domain.shared.Entity;
import com.application.domain.Billing.payment.valueobject.PaymentId;
import com.application.domain.Billing.invoicing.valueobject.MonthlyInvoiceId;
import com.application.domain.shared.valueobject.Money;
import com.application.domain.shared.DomainException;
import lombok.Data;
import lombok.Builder;

import java.time.LocalDate;

@Data
@Builder
public class Payment extends Entity<PaymentId> {
    private MonthlyInvoiceId monthlyInvoiceId;
    private Money amount;
    private LocalDate paymentDate;
    private PaymentMethod paymentMethod;
    private String transactionReference;
    private boolean confirmed;

    public enum PaymentMethod {
        CASH, BANK_TRANSFER, CREDIT_CARD, DEBIT_CARD
    }

    private Payment(PaymentId id,
                    MonthlyInvoiceId monthlyInvoiceId,
                    Money amount,
                    LocalDate paymentDate,
                    PaymentMethod paymentMethod,
                    String transactionReference,
                    boolean confirmed) {
        super(id);
        this.monthlyInvoiceId = monthlyInvoiceId;
        this.amount = amount;
        this.paymentDate = paymentDate;
        this.paymentMethod = paymentMethod;
        this.transactionReference = transactionReference;
        this.confirmed = confirmed;
        validate();
    }

    public static Payment create(PaymentId id,
                                 MonthlyInvoiceId monthlyInvoiceId,
                                 Money amount,
                                 LocalDate paymentDate,
                                 PaymentMethod paymentMethod,
                                 String transactionReference) {
        return new Payment(id, monthlyInvoiceId, amount, paymentDate, paymentMethod, transactionReference, false);
    }

    public void confirm() {
        if (this.confirmed) {
            throw new DomainException("Payment is already confirmed.");
        }
        if (amount.getAmount().signum() <= 0) {
            throw new DomainException("Payment amount must be greater than zero.");
        }
        this.confirmed = true;
    }

    private void validate() {
        if (monthlyInvoiceId == null) {
            throw new DomainException("MonthlyInvoiceId is required.");
        }
        if (amount == null) {
            throw new DomainException("Amount is required.");
        }
        if (amount.getAmount().signum() <= 0) {
            throw new DomainException("Payment amount must be greater than zero.");
        }
        if (paymentDate == null) {
            throw new DomainException("Payment date is required.");
        }
        if (paymentDate.isAfter(LocalDate.now())) {
            throw new DomainException("Payment date cannot be in the future.");
        }
        if (paymentMethod == null) {
            throw new DomainException("Payment method is required.");
        }
        if (transactionReference == null || transactionReference.isBlank()) {
            throw new DomainException("Transaction reference is required.");
        }
    }
}