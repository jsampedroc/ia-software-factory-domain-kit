package com.application.school.domain.billing.model;

import lombok.Value;
import java.util.UUID;

@Value
public class PaymentId {
    UUID value;

    private PaymentId(UUID value) {
        this.value = value;
    }

    public static PaymentId generate() {
        return new PaymentId(UUID.randomUUID());
    }

    public static PaymentId fromString(String uuid) {
        return new PaymentId(UUID.fromString(uuid));
    }

    public static PaymentId fromUUID(UUID uuid) {
        return new PaymentId(uuid);
    }

    @Override
    public String toString() {
        return value.toString();
    }
}