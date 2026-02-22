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
public class InvoiceItemId {
    private UUID value;

    public static InvoiceItemId generate() {
        return new InvoiceItemId(UUID.randomUUID());
    }

    public static InvoiceItemId fromString(String uuid) {
        return new InvoiceItemId(UUID.fromString(uuid));
    }
}