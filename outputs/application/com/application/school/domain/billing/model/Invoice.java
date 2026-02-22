package com.application.school.domain.billing.model;

import com.application.school.domain.shared.valueobject.Money;
import com.application.school.domain.shared.enumeration.InvoiceStatus;
import com.application.school.domain.student.model.StudentId;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.YearMonth;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Invoice {
    private InvoiceId id;
    private StudentId studentId;
    private LocalDate issueDate;
    private LocalDate dueDate;
    private YearMonth monthYear;
    @Builder.Default
    private Money totalAmount = new Money();
    private InvoiceStatus status;
    private LocalDate paymentDate;
    @Builder.Default
    private List<InvoiceItem> items = new ArrayList<>();
    @Builder.Default
    private List<Payment> payments = new ArrayList<>();

    public void addItem(InvoiceItem item) {
        this.items.add(item);
        recalculateTotal();
    }

    public void removeItem(InvoiceItemId itemId) {
        this.items.removeIf(item -> item.getId().equals(itemId));
        recalculateTotal();
    }

    public void addPayment(Payment payment) {
        this.payments.add(payment);
        updateStatus();
    }

    private void recalculateTotal() {
        Money newTotal = new Money();
        for (InvoiceItem item : items) {
            newTotal = newTotal.add(item.getSubtotal());
        }
        this.totalAmount = newTotal;
    }

    private void updateStatus() {
        Money paidAmount = payments.stream()
                .map(Payment::getAmount)
                .reduce(new Money(), Money::add);

        if (paidAmount.compareTo(totalAmount) >= 0) {
            this.status = InvoiceStatus.PAID;
            this.paymentDate = LocalDate.now();
        } else if (LocalDate.now().isAfter(dueDate) && this.status != InvoiceStatus.PAID) {
            this.status = InvoiceStatus.OVERDUE;
        } else {
            this.status = InvoiceStatus.PENDING;
        }
    }

    public boolean isPending() {
        return this.status == InvoiceStatus.PENDING;
    }

    public boolean isOverdue() {
        return this.status == InvoiceStatus.OVERDUE;
    }

    public boolean isPaid() {
        return this.status == InvoiceStatus.PAID;
    }

    public static Invoice create(StudentId studentId, YearMonth monthYear, LocalDate dueDate) {
        return Invoice.builder()
                .id(new InvoiceId(UUID.randomUUID()))
                .studentId(studentId)
                .issueDate(LocalDate.now())
                .dueDate(dueDate)
                .monthYear(monthYear)
                .status(InvoiceStatus.PENDING)
                .build();
    }
}