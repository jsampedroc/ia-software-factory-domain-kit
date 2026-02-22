package com.application.school.domain.billing.model;

import lombok.Value;
import java.util.UUID;

@Value
public class InvoiceId {
    UUID value;

    private InvoiceId(UUID value) {
        this.value = value;
    }

    public static InvoiceId generate() {
        return new InvoiceId(UUID.randomUUID());
    }

    public static InvoiceId fromString(String value) {
        return new InvoiceId(UUID.fromString(value));
    }

    public static InvoiceId fromUUID(UUID value) {
        return new InvoiceId(value);
    }

    public String toString() {
        return value.toString();
    }
}