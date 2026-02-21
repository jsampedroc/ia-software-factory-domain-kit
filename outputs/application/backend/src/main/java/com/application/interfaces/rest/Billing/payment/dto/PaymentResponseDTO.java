package com.application.interfaces.rest.Billing.payment.dto;

import com.application.domain.Billing.payment.domain.Payment;
import com.application.domain.Billing.valueobject.PaymentId;
import com.application.domain.Billing.valueobject.MonthlyInvoiceId;
import com.application.domain.shared.valueobject.Money;
import lombok.Data;
import lombok.Builder;

import java.time.LocalDateTime;

@Data
@Builder
public class PaymentResponseDTO {
    private PaymentId id;
    private MonthlyInvoiceId monthlyInvoiceId;
    private Money amount;
    private LocalDateTime paymentDate;
    private String paymentMethod;
    private String transactionReference;
    private Boolean confirmed;

    public static PaymentResponseDTO fromDomain(Payment payment) {
        return PaymentResponseDTO.builder()
                .id(payment.getId())
                .monthlyInvoiceId(payment.getMonthlyInvoiceId())
                .amount(payment.getAmount())
                .paymentDate(payment.getPaymentDate())
                .paymentMethod(payment.getPaymentMethod())
                .transactionReference(payment.getTransactionReference())
                .confirmed(payment.getConfirmed())
                .build();
    }
}