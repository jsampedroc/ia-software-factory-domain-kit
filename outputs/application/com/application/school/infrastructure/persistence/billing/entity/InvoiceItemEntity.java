package com.application.school.infrastructure.persistence.billing.entity;

import com.application.school.domain.billing.model.InvoiceItemId;
import com.application.school.domain.shared.valueobject.Money;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.math.BigDecimal;

@Entity
@Table(name = "invoice_items")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class InvoiceItemEntity {
    @Id
    private String itemId;

    @Column(nullable = false)
    private String description;

    @Column(nullable = false)
    private Integer quantity;

    @Embedded
    @AttributeOverrides({
            @AttributeOverride(name = "amount", column = @Column(name = "unit_price_amount", nullable = false, precision = 19, scale = 4)),
            @AttributeOverride(name = "currency", column = @Column(name = "unit_price_currency", nullable = false, length = 3))
    })
    private Money unitPrice;

    @Embedded
    @AttributeOverrides({
            @AttributeOverride(name = "amount", column = @Column(name = "subtotal_amount", nullable = false, precision = 19, scale = 4)),
            @AttributeOverride(name = "currency", column = @Column(name = "subtotal_currency", nullable = false, length = 3))
    })
    private Money subtotal;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "invoice_id", nullable = false)
    private InvoiceEntity invoice;

    public InvoiceItemId getDomainId() {
        return new InvoiceItemId(itemId);
    }
}