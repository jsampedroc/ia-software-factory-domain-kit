package com.application.infrastructure.persistence.jpa;

import com.application.domain.model.billing.Invoice;
import com.application.domain.model.billing.InvoiceId;
import com.application.domain.model.billing.InvoiceNumber;
import com.application.domain.model.studentmanagement.Student;
import com.application.domain.model.studentmanagement.StudentId;
import com.application.domain.enums.InvoiceStatus;
import com.application.domain.valueobject.Money;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Currency;

@Entity
@Table(name = "invoices")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class InvoiceEntity {

    @Id
    private String invoiceId;

    @Embedded
    @AttributeOverrides({
            @AttributeOverride(name = "value", column = @Column(name = "invoice_number", unique = true, nullable = false))
    })
    private InvoiceNumberEmbeddable invoiceNumber;

    @Column(name = "student_id", nullable = false)
    private String studentId;

    @Column(name = "billing_month", nullable = false)
    private LocalDate billingMonth;

    @Column(name = "issue_date", nullable = false)
    private LocalDate issueDate;

    @Column(name = "due_date", nullable = false)
    private LocalDate dueDate;

    @Column(name = "subtotal_amount", nullable = false, precision = 19, scale = 4)
    private BigDecimal subtotalAmount;

    @Column(name = "subtotal_currency", nullable = false, length = 3)
    private String subtotalCurrency;

    @Column(name = "adjustments_amount", nullable = false, precision = 19, scale = 4)
    private BigDecimal adjustmentsAmount;

    @Column(name = "adjustments_currency", nullable = false, length = 3)
    private String adjustmentsCurrency;

    @Column(name = "total_amount", nullable = false, precision = 19, scale = 4)
    private BigDecimal totalAmount;

    @Column(name = "total_currency", nullable = false, length = 3)
    private String totalCurrency;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false, length = 20)
    private InvoiceStatus status;

    @Column(name = "payment_method", length = 50)
    private String paymentMethod;

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @Embeddable
    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class InvoiceNumberEmbeddable {
        @Column(name = "value", unique = true, nullable = false, updatable = false)
        private String value;
    }

    public static InvoiceEntity fromDomain(Invoice invoice) {
        if (invoice == null) {
            return null;
        }

        return InvoiceEntity.builder()
                .invoiceId(invoice.getInvoiceId() != null ? invoice.getInvoiceId().getValue().toString() : null)
                .invoiceNumber(InvoiceNumberEmbeddable.builder()
                        .value(invoice.getInvoiceNumber() != null ? invoice.getInvoiceNumber().getValue() : null)
                        .build())
                .studentId(invoice.getStudent() != null && invoice.getStudent().getStudentId() != null ?
                        invoice.getStudent().getStudentId().getValue().toString() : null)
                .billingMonth(invoice.getBillingMonth())
                .issueDate(invoice.getIssueDate())
                .dueDate(invoice.getDueDate())
                .subtotalAmount(invoice.getSubtotal() != null ? invoice.getSubtotal().getAmount() : null)
                .subtotalCurrency(invoice.getSubtotal() != null ? invoice.getSubtotal().getCurrency().getCurrencyCode() : null)
                .adjustmentsAmount(invoice.getAdjustments() != null ? invoice.getAdjustments().getAmount() : null)
                .adjustmentsCurrency(invoice.getAdjustments() != null ? invoice.getAdjustments().getCurrency().getCurrencyCode() : null)
                .totalAmount(invoice.getTotalAmount() != null ? invoice.getTotalAmount().getAmount() : null)
                .totalCurrency(invoice.getTotalAmount() != null ? invoice.getTotalAmount().getCurrency().getCurrencyCode() : null)
                .status(invoice.getStatus())
                .paymentMethod(invoice.getPaymentMethod())
                .createdAt(invoice.getCreatedAt())
                .updatedAt(invoice.getUpdatedAt())
                .build();
    }

    public Invoice toDomain() {
        Money subtotal = new Money(this.subtotalAmount, Currency.getInstance(this.subtotalCurrency));
        Money adjustments = new Money(this.adjustmentsAmount, Currency.getInstance(this.adjustmentsCurrency));
        Money total = new Money(this.totalAmount, Currency.getInstance(this.totalCurrency));

        return Invoice.builder()
                .invoiceId(new InvoiceId(this.invoiceId))
                .invoiceNumber(new InvoiceNumber(this.invoiceNumber != null ? this.invoiceNumber.getValue() : null))
                .student(Student.builder()
                        .studentId(new StudentId(this.studentId))
                        .build())
                .billingMonth(this.billingMonth)
                .issueDate(this.issueDate)
                .dueDate(this.dueDate)
                .subtotal(subtotal)
                .adjustments(adjustments)
                .totalAmount(total)
                .status(this.status)
                .paymentMethod(this.paymentMethod)
                .createdAt(this.createdAt)
                .updatedAt(this.updatedAt)
                .build();
    }
}