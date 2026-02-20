package com.application.application.dto;

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
    private String invoiceId;
    private BigDecimal amount;
    private LocalDateTime paymentDate;
    private String receivedBy;
    private String referenceNumber;
}