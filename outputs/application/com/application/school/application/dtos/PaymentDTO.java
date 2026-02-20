package com.application.school.application.dtos;

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
public class PaymentDTO {
    private String paymentId;
    private BigDecimal amount;
    private String paymentMethod;
    private String transactionReference;
    private LocalDateTime date;
    private String invoiceId;
}