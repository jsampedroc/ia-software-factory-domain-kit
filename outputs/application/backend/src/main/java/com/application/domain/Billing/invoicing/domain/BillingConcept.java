package com.application.domain.Billing.invoicing.domain;

import com.application.domain.shared.Entity;
import com.application.domain.shared.valueobject.Money;
import com.application.domain.Billing.valueobject.BillingConceptId;
import com.application.domain.exception.DomainException;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class BillingConcept extends Entity<BillingConceptId> {
    private String name;
    private String description;
    private Money defaultAmount;
    private Periodicity periodicity;
    private Integer applicableFromGrade;
    private Integer applicableToGrade;
    private boolean active;

    public enum Periodicity {
        MONTHLY,
        ANNUAL,
        ONE_TIME
    }

    private BillingConcept(BillingConceptId id,
                           String name,
                           String description,
                           Money defaultAmount,
                           Periodicity periodicity,
                           Integer applicableFromGrade,
                           Integer applicableToGrade,
                           boolean active) {
        super(id);
        this.name = name;
        this.description = description;
        this.defaultAmount = defaultAmount;
        this.periodicity = periodicity;
        this.applicableFromGrade = applicableFromGrade;
        this.applicableToGrade = applicableToGrade;
        this.active = active;
        validate();
    }

    private void validate() {
        if (name == null || name.trim().isEmpty()) {
            throw new DomainException("BillingConcept name cannot be null or empty.");
        }
        if (defaultAmount == null) {
            throw new DomainException("BillingConcept defaultAmount cannot be null.");
        }
        if (defaultAmount.getAmount().doubleValue() < 0) {
            throw new DomainException("BillingConcept defaultAmount cannot be negative.");
        }
        if (periodicity == null) {
            throw new DomainException("BillingConcept periodicity cannot be null.");
        }
        if (applicableFromGrade != null && applicableToGrade != null && applicableFromGrade > applicableToGrade) {
            throw new DomainException("BillingConcept applicableFromGrade cannot be greater than applicableToGrade.");
        }
    }

    public boolean isApplicableForGrade(Integer grade) {
        if (grade == null) {
            return false;
        }
        boolean fromCondition = applicableFromGrade == null || grade >= applicableFromGrade;
        boolean toCondition = applicableToGrade == null || grade <= applicableToGrade;
        return fromCondition && toCondition;
    }

    public void deactivate() {
        this.active = false;
    }

    public void updateDetails(String newName, String newDescription, Money newDefaultAmount) {
        if (newName != null && !newName.trim().isEmpty()) {
            this.name = newName;
        }
        if (newDescription != null) {
            this.description = newDescription;
        }
        if (newDefaultAmount != null) {
            if (newDefaultAmount.getAmount().doubleValue() < 0) {
                throw new DomainException("BillingConcept defaultAmount cannot be negative.");
            }
            this.defaultAmount = newDefaultAmount;
        }
        validate();
    }
}