package com.application.domain.Billing.invoicing.domain;

import com.application.domain.shared.Entity;
import com.application.domain.Billing.invoicing.valueobject.MonthlyInvoiceId;
import com.application.domain.SchoolManagement.valueobject.StudentId;
import com.application.domain.SchoolManagement.valueobject.SchoolId;
import com.application.domain.shared.valueobject.Money;
import com.application.domain.exception.DomainException;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.List;

@Data
@Builder
public class MonthlyInvoice extends Entity<MonthlyInvoiceId> {
    private StudentId studentId;
    private YearMonth billingPeriod;
    private LocalDate issueDate;
    private LocalDate dueDate;
    @Builder.Default
    private Money totalAmount = new Money(0, "USD");
    @Builder.Default
    private InvoiceStatus status = InvoiceStatus.PENDING;
    private SchoolId schoolId;
    @Builder.Default
    private List<InvoiceItem> items = new ArrayList<>();

    public enum InvoiceStatus {
        PENDING, PARTIALLY_PAID, PAID, OVERDUE, CANCELLED
    }

    private MonthlyInvoice(MonthlyInvoiceId id,
                          StudentId studentId,
                          YearMonth billingPeriod,
                          LocalDate issueDate,
                          LocalDate dueDate,
                          Money totalAmount,
                          InvoiceStatus status,
                          SchoolId schoolId,
                          List<InvoiceItem> items) {
        super(id);
        this.studentId = studentId;
        this.billingPeriod = billingPeriod;
        this.issueDate = issueDate;
        this.dueDate = dueDate;
        this.totalAmount = totalAmount;
        this.status = status;
        this.schoolId = schoolId;
        this.items = items != null ? items : new ArrayList<>();
        validate();
    }

    private void validate() {
        if (studentId == null) {
            throw new DomainException("MonthlyInvoice studentId cannot be null.");
        }
        if (billingPeriod == null) {
            throw new DomainException("MonthlyInvoice billingPeriod cannot be null.");
        }
        if (issueDate == null) {
            throw new DomainException("MonthlyInvoice issueDate cannot be null.");
        }
        if (dueDate == null) {
            throw new DomainException("MonthlyInvoice dueDate cannot be null.");
        }
        if (totalAmount == null) {
            throw new DomainException("MonthlyInvoice totalAmount cannot be null.");
        }
        if (status == null) {
            throw new DomainException("MonthlyInvoice status cannot be null.");
        }
        if (schoolId == null) {
            throw new DomainException("MonthlyInvoice schoolId cannot be null.");
        }
        if (dueDate.isBefore(issueDate) || dueDate.isEqual(issueDate)) {
            throw new DomainException("MonthlyInvoice dueDate must be after issueDate.");
        }
        if (!totalAmount.getCurrency().equals("USD")) {
            throw new DomainException("MonthlyInvoice currency must be USD.");
        }
        recalculateTotal();
    }

    public static MonthlyInvoice create(MonthlyInvoiceId id,
                                       StudentId studentId,
                                       String billingPeriodStr,
                                       LocalDate issueDate,
                                       LocalDate dueDate,
                                       SchoolId schoolId) {
        YearMonth billingPeriod;
        try {
            billingPeriod = YearMonth.parse(billingPeriodStr, DateTimeFormatter.ofPattern("yyyy-MM"));
        } catch (DateTimeParseException e) {
            throw new DomainException("Invalid billingPeriod format. Expected YYYY-MM.");
        }

        return new MonthlyInvoice(
                id,
                studentId,
                billingPeriod,
                issueDate,
                dueDate,
                new Money(0, "USD"),
                InvoiceStatus.PENDING,
                schoolId,
                new ArrayList<>()
        );
    }

    public void addItem(InvoiceItem item) {
        if (item == null) {
            throw new DomainException("InvoiceItem cannot be null.");
        }
        if (!this.getId().equals(item.getMonthlyInvoiceId())) {
            throw new DomainException("InvoiceItem does not belong to this invoice.");
        }
        items.add(item);
        recalculateTotal();
    }

    public void removeItem(InvoiceItemId itemId) {
        if (itemId == null) {
            throw new DomainException("InvoiceItemId cannot be null.");
        }
        boolean removed = items.removeIf(item -> item.getId().equals(itemId));
        if (removed) {
            recalculateTotal();
        }
    }

    private void recalculateTotal() {
        Money newTotal = new Money(0, "USD");
        for (InvoiceItem item : items) {
            newTotal = newTotal.add(item.getSubtotal());
        }
        this.totalAmount = newTotal;
        updateStatusBasedOnAmount();
    }

    private void updateStatusBasedOnAmount() {
        if (this.totalAmount.getAmount().signum() == 0) {
            this.status = InvoiceStatus.PAID;
        } else if (this.status == InvoiceStatus.PAID && this.totalAmount.getAmount().signum() > 0) {
            this.status = InvoiceStatus.PARTIALLY_PAID;
        }
    }

    public void markAsPaid() {
        if (this.totalAmount.getAmount().signum() == 0) {
            this.status = InvoiceStatus.PAID;
        } else {
            throw new DomainException("Cannot mark invoice as PAID when total amount is not zero.");
        }
    }

    public void markAsOverdue() {
        if (this.status == InvoiceStatus.PENDING || this.status == InvoiceStatus.PARTIALLY_PAID) {
            this.status = InvoiceStatus.OVERDUE;
        }
    }

    public void cancel() {
        if (this.status != InvoiceStatus.PAID) {
            this.status = InvoiceStatus.CANCELLED;
        } else {
            throw new DomainException("Cannot cancel an invoice that is already PAID.");
        }
    }

    public void applyPayment(Money paymentAmount) {
        if (paymentAmount == null) {
            throw new DomainException("Payment amount cannot be null.");
        }
        if (paymentAmount.getAmount().signum() <= 0) {
            throw new DomainException("Payment amount must be greater than zero.");
        }
        if (!paymentAmount.getCurrency().equals(this.totalAmount.getCurrency())) {
            throw new DomainException("Payment currency must match invoice currency.");
        }
        if (this.status == InvoiceStatus.PAID || this.status == InvoiceStatus.CANCELLED) {
            throw new DomainException("Cannot apply payment to an invoice with status " + this.status);
        }

        Money newTotal = this.totalAmount.subtract(paymentAmount);
        if (newTotal.getAmount().signum() < 0) {
            throw new DomainException("Payment amount exceeds invoice total.");
        }

        this.totalAmount = newTotal;
        if (this.totalAmount.getAmount().signum() == 0) {
            this.status = InvoiceStatus.PAID;
        } else {
            this.status = InvoiceStatus.PARTIALLY_PAID;
        }
    }
}