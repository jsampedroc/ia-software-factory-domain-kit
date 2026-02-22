package com.application.school.infrastructure.persistence.billing.entity;

import com.application.school.domain.shared.valueobject.Money;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

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
    @Column(name = "payment_id")
    private UUID paymentId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "invoice_id", nullable = false)
    private InvoiceEntity invoice;

    @Embedded
    @AttributeOverrides({
            @AttributeOverride(name = "amount", column = @Column(name = "amount", nullable = false)),
            @AttributeOverride(name = "currency", column = @Column(name = "currency", nullable = false, length = 3))
    })
    private Money amount;

    @Column(name = "payment_method", nullable = false, length = 50)
    private String paymentMethod;

    @Column(name = "reference", length = 100)
    private String reference;

    @CreationTimestamp
    @Column(name = "transaction_date", nullable = false, updatable = false)
    private LocalDateTime transactionDate;
}