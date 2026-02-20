package com.application.domain.model.billing;

import com.application.domain.model.studentmanagement.Student;
import com.application.domain.model.studentmanagement.StudentId;
import com.application.domain.valueobject.Money;
import com.application.domain.enums.InvoiceStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.YearMonth;
import java.util.ArrayList;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Invoice {
    private InvoiceId id;
    private InvoiceNumber invoiceNumber;
    private StudentId studentId;
    private YearMonth billingMonth;
    private LocalDate issueDate;
    private LocalDate dueDate;
    @Builder.Default
    private Money subtotal = new Money();
    @Builder.Default
    private Money adjustments = new Money();
    @Builder.Default
    private Money totalAmount = new Money();
    @Builder.Default
    private InvoiceStatus status = InvoiceStatus.DRAFT;
    private String paymentMethod;
    @Builder.Default
    private List<Payment> payments = new ArrayList<>();

    public void calculateTotal() {
        this.totalAmount = new Money(
                this.subtotal.getAmount().add(this.adjustments.getAmount()),
                this.subtotal.getCurrency()
        );
    }

    public void addPayment(Payment payment) {
        if (payment == null) {
            throw new IllegalArgumentException("Payment cannot be null");
        }
        if (!payment.getInvoiceId().equals(this.id)) {
            throw new IllegalArgumentException("Payment does not belong to this invoice");
        }
        this.payments.add(payment);
        updateStatusBasedOnPayments();
    }

    private void updateStatusBasedOnPayments() {
        Money paidAmount = getTotalPaid();
        if (paidAmount.getAmount().compareTo(this.totalAmount.getAmount()) >= 0) {
            this.status = InvoiceStatus.PAID;
        } else if (paidAmount.getAmount().compareTo(java.math.BigDecimal.ZERO) > 0) {
            this.status = InvoiceStatus.PARTIALLY_PAID;
        } else if (this.status == InvoiceStatus.DRAFT && this.issueDate != null) {
            this.status = InvoiceStatus.ISSUED;
        }
        if (this.status == InvoiceStatus.ISSUED || this.status == InvoiceStatus.PARTIALLY_PAID) {
            if (LocalDate.now().isAfter(this.dueDate)) {
                this.status = InvoiceStatus.OVERDUE;
            }
        }
    }

    public Money getTotalPaid() {
        return this.payments.stream()
                .map(Payment::getAmount)
                .reduce(new Money(java.math.BigDecimal.ZERO, this.totalAmount.getCurrency()),
                        Money::add);
    }

    public Money getBalanceDue() {
        Money paid = getTotalPaid();
        return new Money(
                this.totalAmount.getAmount().subtract(paid.getAmount()),
                this.totalAmount.getCurrency()
        );
    }

    public void issue() {
        if (this.status != InvoiceStatus.DRAFT) {
            throw new IllegalStateException("Only DRAFT invoices can be issued");
        }
        if (this.issueDate == null) {
            this.issueDate = LocalDate.now();
        }
        if (this.dueDate == null) {
            this.dueDate = this.issueDate.plusDays(30);
        }
        calculateTotal();
        this.status = InvoiceStatus.ISSUED;
    }

    public void cancel() {
        if (this.status == InvoiceStatus.PAID || this.status == InvoiceStatus.PARTIALLY_PAID) {
            throw new IllegalStateException("Cannot cancel a paid or partially paid invoice");
        }
        this.status = InvoiceStatus.CANCELLED;
    }

    public void applyAdjustment(Money adjustment) {
        if (this.status == InvoiceStatus.ISSUED || this.status == InvoiceStatus.PARTIALLY_PAID || this.status == InvoiceStatus.OVERDUE) {
            throw new IllegalStateException("Cannot adjust an issued, partially paid, or overdue invoice");
        }
        this.adjustments = this.adjustments.add(adjustment);
        calculateTotal();
    }
}