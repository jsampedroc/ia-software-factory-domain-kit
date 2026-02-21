package com.application.infrastructure.persistence.Billing.invoicing.entity;

import com.application.domain.Billing.invoicing.domain.MonthlyInvoice;
import com.application.domain.Billing.invoicing.domain.InvoiceItem;
import com.application.domain.Billing.valueobject.MonthlyInvoiceId;
import com.application.domain.SchoolManagement.valueobject.StudentId;
import com.application.domain.SchoolManagement.valueobject.SchoolId;
import com.application.infrastructure.persistence.Billing.invoicing.entity.converter.StudentIdConverter;
import com.application.infrastructure.persistence.Billing.invoicing.entity.converter.SchoolIdConverter;
import com.application.infrastructure.persistence.Billing.invoicing.entity.converter.BillingPeriodConverter;
import com.application.infrastructure.persistence.Billing.invoicing.entity.converter.MoneyConverter;
import com.application.domain.shared.valueobject.Money;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.Builder;

import jakarta.persistence.*;
import java.time.LocalDate;
import java.time.YearMonth;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Entity
@Table(name = "monthly_invoices",
       uniqueConstraints = {
           @UniqueConstraint(columnNames = {"student_id", "billing_period"})
       })
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MonthlyInvoiceEntity {

    @Id
    @Column(name = "id")
    private UUID id;

    @Column(name = "student_id", nullable = false)
    @Convert(converter = StudentIdConverter.class)
    private StudentId studentId;

    @Column(name = "billing_period", nullable = false)
    @Convert(converter = BillingPeriodConverter.class)
    private YearMonth billingPeriod;

    @Column(name = "issue_date", nullable = false)
    private LocalDate issueDate;

    @Column(name = "due_date", nullable = false)
    private LocalDate dueDate;

    @Column(name = "total_amount", nullable = false)
    @Convert(converter = MoneyConverter.class)
    private Money totalAmount;

    @Column(name = "status", nullable = false)
    @Enumerated(EnumType.STRING)
    private MonthlyInvoice.InvoiceStatus status;

    @Column(name = "school_id", nullable = false)
    @Convert(converter = SchoolIdConverter.class)
    private SchoolId schoolId;

    @OneToMany(mappedBy = "monthlyInvoice", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    @Builder.Default
    private List<InvoiceItemEntity> items = new ArrayList<>();

    public static MonthlyInvoiceEntity fromDomain(MonthlyInvoice invoice) {
        if (invoice == null) {
            return null;
        }

        MonthlyInvoiceEntity entity = MonthlyInvoiceEntity.builder()
                .id(invoice.getId().getValue())
                .studentId(invoice.getStudentId())
                .billingPeriod(invoice.getBillingPeriod())
                .issueDate(invoice.getIssueDate())
                .dueDate(invoice.getDueDate())
                .totalAmount(invoice.getTotalAmount())
                .status(invoice.getStatus())
                .schoolId(invoice.getSchoolId())
                .build();

        List<InvoiceItemEntity> itemEntities = invoice.getItems().stream()
                .map(item -> InvoiceItemEntity.fromDomain(item, entity))
                .collect(Collectors.toList());
        entity.setItems(itemEntities);

        return entity;
    }

    public MonthlyInvoice toDomain() {
        MonthlyInvoiceId invoiceId = new MonthlyInvoiceId(this.id);
        List<InvoiceItem> domainItems = this.items.stream()
                .map(InvoiceItemEntity::toDomain)
                .collect(Collectors.toList());

        return MonthlyInvoice.builder()
                .id(invoiceId)
                .studentId(this.studentId)
                .billingPeriod(this.billingPeriod)
                .issueDate(this.issueDate)
                .dueDate(this.dueDate)
                .totalAmount(this.totalAmount)
                .status(this.status)
                .schoolId(this.schoolId)
                .items(domainItems)
                .build();
    }
}