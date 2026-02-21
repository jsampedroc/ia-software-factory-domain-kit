package com.application.infrastructure.persistence.Billing.invoicing.entity;

import com.application.domain.Billing.invoicing.domain.BillingConcept;
import com.application.domain.Billing.valueobject.BillingConceptId;
import com.application.domain.shared.valueobject.Money;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

@Entity
@Table(name = "billing_concepts")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BillingConceptEntity {
    @Id
    private UUID id;

    @Column(nullable = false, unique = true)
    private String name;

    private String description;

    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal defaultAmount;

    @Column(nullable = false)
    private String currency;

    @Column(nullable = false)
    private String periodicity;

    private Integer applicableFromGrade;
    private Integer applicableToGrade;

    @Column(nullable = false)
    private Boolean active;

    @Column(nullable = false)
    private LocalDate createdAt;

    public static BillingConceptEntity fromDomain(BillingConcept concept) {
        if (concept == null) {
            return null;
        }
        return BillingConceptEntity.builder()
                .id(concept.getId().getValue())
                .name(concept.getName())
                .description(concept.getDescription())
                .defaultAmount(concept.getDefaultAmount().amount())
                .currency(concept.getDefaultAmount().currency())
                .periodicity(concept.getPeriodicity())
                .applicableFromGrade(concept.getApplicableFromGrade())
                .applicableToGrade(concept.getApplicableToGrade())
                .active(concept.getActive())
                .createdAt(concept.getCreatedAt())
                .build();
    }

    public BillingConcept toDomain() {
        return BillingConcept.builder()
                .id(new BillingConceptId(this.id))
                .name(this.name)
                .description(this.description)
                .defaultAmount(new Money(this.defaultAmount, this.currency))
                .periodicity(this.periodicity)
                .applicableFromGrade(this.applicableFromGrade)
                .applicableToGrade(this.applicableToGrade)
                .active(this.active)
                .createdAt(this.createdAt)
                .build();
    }
}