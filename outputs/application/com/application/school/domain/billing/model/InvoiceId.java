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
public class InvoiceId {
    private UUID value;

    public static InvoiceId generate() {
        return InvoiceId.builder()
                .value(UUID.randomUUID())
                .build();
    }

    public static InvoiceId fromString(String uuid) {
        return InvoiceId.builder()
                .value(UUID.fromString(uuid))
                .build();
    }
}