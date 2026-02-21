package com.application.infrastructure.persistence.Billing.invoicing.entity;

import com.application.domain.Billing.invoicing.domain.InvoiceItem;
import com.application.domain.Billing.valueobject.InvoiceItemId;
import com.application.domain.Billing.valueobject.MonthlyInvoiceId;
import com.application.domain.shared.valueobject.Money;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.UUID;

@Entity
@Table(name = "invoice_items")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class InvoiceItemEntity {
    @Id
    private UUID id;

    @Column(name = "monthly_invoice_id", nullable = false)
    private UUID monthlyInvoiceId;

    @Column(name = "billing_concept_id", nullable = false)
    private UUID billingConceptId;

    @Column(nullable = false)
    private String description;

    @Column(nullable = false)
    private Integer quantity;

    @Column(name = "unit_price_amount", nullable = false, precision = 19, scale = 4)
    private BigDecimal unitPriceAmount;

    @Column(name = "unit_price_currency", nullable = false, length = 3)
    private String unitPriceCurrency;

    @Column(name = "subtotal_amount", nullable = false, precision = 19, scale = 4)
    private BigDecimal subtotalAmount;

    @Column(name = "subtotal_currency", nullable = false, length = 3)
    private String subtotalCurrency;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "monthly_invoice_id", referencedColumnName = "id", insertable = false, updatable = false)
    private MonthlyInvoiceEntity monthlyInvoice;

    public static InvoiceItemEntity fromDomain(InvoiceItem invoiceItem) {
        return InvoiceItemEntity.builder()
                .id(invoiceItem.getId().getValue())
                .monthlyInvoiceId(invoiceItem.getMonthlyInvoiceId().getValue())
                .billingConceptId(invoiceItem.getBillingConceptId().getValue())
                .description(invoiceItem.getDescription())
                .quantity(invoiceItem.getQuantity())
                .unitPriceAmount(invoiceItem.getUnitPrice().getAmount())
                .unitPriceCurrency(invoiceItem.getUnitPrice().getCurrency())
                .subtotalAmount(invoiceItem.getSubtotal().getAmount())
                .subtotalCurrency(invoiceItem.getSubtotal().getCurrency())
                .build();
    }

    public InvoiceItem toDomain() {
        return InvoiceItem.builder()
                .id(new InvoiceItemId(this.id))
                .monthlyInvoiceId(new MonthlyInvoiceId(this.monthlyInvoiceId))
                .billingConceptId(new com.application.domain.Billing.valueobject.BillingConceptId(this.billingConceptId))
                .description(this.description)
                .quantity(this.quantity)
                .unitPrice(new Money(this.unitPriceAmount, this.unitPriceCurrency))
                .subtotal(new Money(this.subtotalAmount, this.subtotalCurrency))
                .build();
    }
}