package com.application.domain.valueobject;

import com.application.domain.shared.ValueObject;
import java.util.UUID;

public record InvoiceId(UUID value) implements ValueObject {
    public InvoiceId {
        if (value == null) {
            throw new IllegalArgumentException("InvoiceId value cannot be null");
        }
    }
    
    public static InvoiceId generate() {
        return new InvoiceId(UUID.randomUUID());
    }
    
    public static InvoiceId fromString(String value) {
        return new InvoiceId(UUID.fromString(value));
    }
}