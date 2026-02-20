package com.application.school.domain.billing.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import com.application.school.domain.shared.Money;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Payment {
    private PaymentId paymentId;
    private Money amount;
    private String paymentMethod;
    private String transactionReference;
    private LocalDateTime date;
}