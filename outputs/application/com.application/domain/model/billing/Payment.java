package com.application.domain.model.billing;

import com.application.domain.valueobject.Money;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Payment {
    private PaymentId paymentId;
    private Invoice invoice;
    private Money amount;
    private LocalDateTime paymentDate;
    private String receivedBy;
    private String referenceNumber;
}