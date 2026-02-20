package com.application.school.domain.billing.model;

import com.application.school.domain.shared.Money;
import com.application.school.domain.student.model.StudentId;
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
    private InvoiceId invoiceId;
    private StudentId studentId;
    private LocalDate issueDate;
    private LocalDate dueDate;
    private YearMonth monthYear;
    private Money totalAmount;
    private InvoiceStatus status;
    private LocalDate paymentDate;

    @Builder.Default
    private List<InvoiceItem> items = new ArrayList<>();

    @Builder.Default
    private List<Payment> appliedPayments = new ArrayList<>();

    public void addItem(InvoiceItem item) {
        if (item == null) {
            throw new IllegalArgumentException("Invoice item cannot be null");
        }
        this.items.add(item);
        recalculateTotal();
    }

    public void removeItem(InvoiceItemId itemId) {
        if (itemId == null) {
            throw new IllegalArgumentException("Invoice item id cannot be null");
        }
        items.removeIf(item -> item.getItemId().equals(itemId));
        recalculateTotal();
    }

    public void applyPayment(Payment payment) {
        if (payment == null) {
            throw new IllegalArgumentException("Payment cannot be null");
        }
        if (payment.getAmount().getAmount().signum() <= 0) {
            throw new IllegalArgumentException("Payment amount must be positive");
        }
        this.appliedPayments.add(payment);
        updateStatus();
    }

    private void recalculateTotal() {
        Money sum = Money.zero();
        for (InvoiceItem item : items) {
            sum = sum.add(item.getSubtotal());
        }
        this.totalAmount = sum;
    }

    private void updateStatus() {
        if (status == InvoiceStatus.CANCELLED) {
            return;
        }

        Money paidAmount = Money.zero();
        for (Payment payment : appliedPayments) {
            paidAmount = paidAmount.add(payment.getAmount());
        }

        if (paidAmount.compareTo(totalAmount) >= 0) {
            this.status = InvoiceStatus.PAID;
            this.paymentDate = LocalDate.now();
        } else if (dueDate.isBefore(LocalDate.now()) && status == InvoiceStatus.PENDING) {
            this.status = InvoiceStatus.OVERDUE;
        }
    }

    public void markAsCancelled() {
        if (status == InvoiceStatus.PAID) {
            throw new IllegalStateException("Cannot cancel a paid invoice");
        }
        this.status = InvoiceStatus.CANCELLED;
    }

    public void validateForStudent(StudentId studentId) {
        if (!this.studentId.equals(studentId)) {
            throw new IllegalArgumentException("Invoice does not belong to the provided student");
        }
    }

    public boolean isForMonth(YearMonth month) {
        return this.monthYear.equals(month);
    }
}