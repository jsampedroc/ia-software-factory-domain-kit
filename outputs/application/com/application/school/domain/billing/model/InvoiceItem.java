package com.application.school.domain.billing.model;

import com.application.school.domain.shared.Money;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class InvoiceItem {
    private InvoiceItemId id;
    private InvoiceConcept concept;
    private String description;
    private Integer quantity;
    private Money unitPrice;
    private Money subtotal;
    private LocalDateTime createdAt;

    public void calculateSubtotal() {
        if (quantity == null || unitPrice == null) {
            this.subtotal = null;
            return;
        }
        // Asumiendo que Money tiene un método multiply
        this.subtotal = unitPrice.multiply(quantity);
    }
}