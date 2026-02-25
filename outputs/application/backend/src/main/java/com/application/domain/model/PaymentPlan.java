package com.application.domain.model;

import com.application.domain.shared.Entity;
import com.application.domain.valueobject.PaymentPlanId;
import com.application.domain.enums.PaymentPlanStatus;
import lombok.Getter;
import lombok.ToString;
import lombok.experimental.SuperBuilder;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@Getter
@ToString(callSuper = true)
@SuperBuilder(toBuilder = true)
public class PaymentPlan extends Entity<PaymentPlanId> {

    private final UUID invoiceId;
    private final BigDecimal totalAmount;
    private final Integer installmentCount;
    private final BigDecimal installmentAmount;
    private final LocalDate startDate;
    private final List<LocalDate> dueDates;
    private PaymentPlanStatus status;

    public PaymentPlan(PaymentPlanId planId,
                       UUID invoiceId,
                       BigDecimal totalAmount,
                       Integer installmentCount,
                       BigDecimal installmentAmount,
                       LocalDate startDate,
                       List<LocalDate> dueDates,
                       PaymentPlanStatus status) {
        super(planId);
        this.invoiceId = invoiceId;
        this.totalAmount = totalAmount;
        this.installmentCount = installmentCount;
        this.installmentAmount = installmentAmount;
        this.startDate = startDate;
        this.dueDates = List.copyOf(dueDates);
        this.status = status;
        validate();
    }

    private void validate() {
        if (invoiceId == null) {
            throw new IllegalArgumentException("Invoice ID cannot be null");
        }
        if (totalAmount == null || totalAmount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Total amount must be positive");
        }
        if (installmentCount == null || installmentCount <= 0) {
            throw new IllegalArgumentException("Installment count must be positive");
        }
        if (installmentAmount == null || installmentAmount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Installment amount must be positive");
        }
        if (startDate == null) {
            throw new IllegalArgumentException("Start date cannot be null");
        }
        if (dueDates == null || dueDates.isEmpty()) {
            throw new IllegalArgumentException("Due dates cannot be null or empty");
        }
        if (dueDates.size() != installmentCount) {
            throw new IllegalArgumentException("Number of due dates must match installment count");
        }
        if (status == null) {
            throw new IllegalArgumentException("Status cannot be null");
        }
        BigDecimal calculatedTotal = installmentAmount.multiply(BigDecimal.valueOf(installmentCount));
        if (calculatedTotal.compareTo(totalAmount) != 0) {
            throw new IllegalArgumentException("Total amount must equal installment amount * installment count");
        }
        if (!dueDates.stream().allMatch(date -> !date.isBefore(startDate))) {
            throw new IllegalArgumentException("All due dates must be on or after the start date");
        }
    }

    public void markAsCompleted() {
        if (this.status != PaymentPlanStatus.ACTIVE) {
            throw new IllegalStateException("Only active payment plans can be marked as completed");
        }
        this.status = PaymentPlanStatus.COMPLETED;
    }

    public void markAsDefaulted() {
        if (this.status != PaymentPlanStatus.ACTIVE) {
            throw new IllegalStateException("Only active payment plans can be marked as defaulted");
        }
        this.status = PaymentPlanStatus.DEFAULTED;
    }

    public boolean isActive() {
        return this.status == PaymentPlanStatus.ACTIVE;
    }

    public LocalDate getNextDueDate() {
        LocalDate today = LocalDate.now();
        return dueDates.stream()
                .filter(date -> date.isAfter(today) || date.isEqual(today))
                .findFirst()
                .orElse(null);
    }

    public boolean isOverdue() {
        if (status != PaymentPlanStatus.ACTIVE) {
            return false;
        }
        LocalDate today = LocalDate.now();
        return dueDates.stream()
                .anyMatch(dueDate -> dueDate.isBefore(today));
    }
}