package com.application.domain.model.billing;

import lombok.Builder;
import lombok.Value;
import java.util.UUID;

@Value
@Builder
public class PaymentId {
    UUID value;

    private PaymentId(UUID value) {
        this.value = value;
    }

    public static PaymentId of(UUID value) {
        return new PaymentId(value);
    }

    public static PaymentId generate() {
        return new PaymentId(UUID.randomUUID());
    }
}