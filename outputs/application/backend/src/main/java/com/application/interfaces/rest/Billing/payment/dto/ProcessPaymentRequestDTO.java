package com.application.interfaces.rest.Billing.payment.dto;

import lombok.Data;
import lombok.Builder;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProcessPaymentRequestDTO {
    private String monthlyInvoiceId;
    private BigDecimal amount;
    private LocalDate paymentDate;
    private String paymentMethod;
    private String transactionReference;
}