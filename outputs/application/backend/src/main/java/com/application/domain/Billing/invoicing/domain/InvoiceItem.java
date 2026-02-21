package com.application.domain.Billing.invoicing.domain;

import com.application.domain.shared.Entity;
import com.application.domain.Billing.invoicing.valueobject.InvoiceItemId;
import com.application.domain.Billing.invoicing.valueobject.BillingConceptId;
import com.application.domain.Billing.invoicing.valueobject.MonthlyInvoiceId;
import com.application.domain.shared.valueobject.Money;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
@Builder
public class InvoiceItem extends Entity<InvoiceItemId> {
    private final MonthlyInvoiceId monthlyInvoiceId;
    private final BillingConceptId billingConceptId;
    private final String description;
    private final int quantity;
    private final Money unitPrice;
    private final Money subtotal;

    private InvoiceItem(InvoiceItemId id,
                       MonthlyInvoiceId monthlyInvoiceId,
                       BillingConceptId billingConceptId,
                       String description,
                       int quantity,
                       Money unitPrice,
                       Money subtotal) {
        super(id);
        this.monthlyInvoiceId = monthlyInvoiceId;
        this.billingConceptId = billingConceptId;
        this.description = description;
        this.quantity = quantity;
        this.unitPrice = unitPrice;
        this.subtotal = subtotal;
        validate();
    }

    private void validate() {
        if (monthlyInvoiceId == null) {
            throw new IllegalArgumentException("MonthlyInvoiceId is required");
        }
        if (billingConceptId == null) {
            throw new IllegalArgumentException("BillingConceptId is required");
        }
        if (description == null || description.trim().isEmpty()) {
            throw new IllegalArgumentException("Description is required");
        }
        if (quantity <= 0) {
            throw new IllegalArgumentException("Quantity must be greater than zero");
        }
        if (unitPrice == null) {
            throw new IllegalArgumentException("UnitPrice is required");
        }
        if (subtotal == null) {
            throw new IllegalArgumentException("Subtotal is required");
        }
        if (!subtotal.equals(unitPrice.multiply(quantity))) {
            throw new IllegalArgumentException("Subtotal must equal quantity * unitPrice");
        }
    }

    public static InvoiceItem create(InvoiceItemId id,
                                     MonthlyInvoiceId monthlyInvoiceId,
                                     BillingConceptId billingConceptId,
                                     String description,
                                     int quantity,
                                     Money unitPrice) {
        Money calculatedSubtotal = unitPrice.multiply(quantity);
        return new InvoiceItem(id, monthlyInvoiceId, billingConceptId, description, quantity, unitPrice, calculatedSubtotal);
    }
}