package com.application.school.domain.billing.model;

import com.application.school.domain.shared.valueobject.Money;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Payment {
    private PaymentId id;
    private InvoiceId invoiceId;
    private Money amount;
    private String paymentMethod;
    private LocalDateTime transactionDate;
    private String reference;

    public static Payment create(InvoiceId invoiceId, Money amount, String paymentMethod, String reference) {
        return Payment.builder()
                .id(new PaymentId(UUID.randomUUID()))
                .invoiceId(invoiceId)
                .amount(amount)
                .paymentMethod(paymentMethod)
                .transactionDate(LocalDateTime.now())
                .reference(reference)
                .build();
    }
}