package com.application.infrastructure.persistence.Billing.payment.entity;

import com.application.domain.Billing.payment.domain.Payment;
import com.application.domain.Billing.payment.domain.PaymentStatus;
import com.application.domain.Billing.valueobject.PaymentId;
import com.application.domain.Billing.valueobject.MonthlyInvoiceId;
import com.application.domain.shared.valueobject.Money;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "payments")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PaymentEntity {
    @Id
    private UUID id;

    @Column(name = "monthly_invoice_id", nullable = false)
    private UUID monthlyInvoiceId;

    @Column(nullable = false, precision = 19, scale = 4)
    private BigDecimal amount;

    @Column(nullable = false, length = 3)
    private String currency;

    @Column(name = "payment_date", nullable = false)
    private LocalDate paymentDate;

    @Column(name = "payment_method", nullable = false, length = 50)
    private String paymentMethod;

    @Column(name = "transaction_reference", length = 255)
    private String transactionReference;

    @Column(nullable = false)
    private Boolean confirmed;

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    public static PaymentEntity fromDomain(Payment payment) {
        return PaymentEntity.builder()
                .id(payment.getId().getValue())
                .monthlyInvoiceId(payment.getMonthlyInvoiceId().getValue())
                .amount(payment.getAmount().getAmount())
                .currency(payment.getAmount().getCurrency())
                .paymentDate(payment.getPaymentDate())
                .paymentMethod(payment.getPaymentMethod().name())
                .transactionReference(payment.getTransactionReference())
                .confirmed(payment.getConfirmed())
                .createdAt(payment.getCreatedAt())
                .updatedAt(payment.getUpdatedAt())
                .build();
    }

    public Payment toDomain() {
        return Payment.builder()
                .id(new PaymentId(this.id))
                .monthlyInvoiceId(new MonthlyInvoiceId(this.monthlyInvoiceId))
                .amount(new Money(this.amount, this.currency))
                .paymentDate(this.paymentDate)
                .paymentMethod(PaymentStatus.PaymentMethod.valueOf(this.paymentMethod))
                .transactionReference(this.transactionReference)
                .confirmed(this.confirmed)
                .createdAt(this.createdAt)
                .updatedAt(this.updatedAt)
                .build();
    }
}