package com.application.school.domain.billing.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PaymentId {
    private UUID value;

    public static PaymentId generate() {
        return PaymentId.builder()
                .value(UUID.randomUUID())
                .build();
    }

    public static PaymentId fromString(String uuid) {
        return PaymentId.builder()
                .value(UUID.fromString(uuid))
                .build();
    }
}