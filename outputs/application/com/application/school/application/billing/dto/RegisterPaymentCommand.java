package com.application.school.application.billing.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RegisterPaymentCommand {
    private String invoiceId;
    private BigDecimal amount;
    private String paymentMethod;
    private LocalDateTime transactionDate;
    private String reference;
}