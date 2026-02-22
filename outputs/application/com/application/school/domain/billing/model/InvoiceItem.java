package com.application.school.domain.billing.model;

import com.application.school.domain.shared.valueobject.Money;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class InvoiceItem {
    private InvoiceItemId id;
    private String description;
    private Integer quantity;
    private Money unitPrice;
    private Money subtotal;

    public void calculateSubtotal() {
        if (quantity == null || unitPrice == null) {
            this.subtotal = null;
            return;
        }
        this.subtotal = Money.builder()
                .amount(unitPrice.getAmount().multiply(new java.math.BigDecimal(quantity)))
                .currency(unitPrice.getCurrency())
                .build();
    }

    public static InvoiceItem create(String description, Integer quantity, Money unitPrice) {
        InvoiceItem item = InvoiceItem.builder()
                .id(new InvoiceItemId(UUID.randomUUID()))
                .description(description)
                .quantity(quantity)
                .unitPrice(unitPrice)
                .build();
        item.calculateSubtotal();
        return item;
    }
}