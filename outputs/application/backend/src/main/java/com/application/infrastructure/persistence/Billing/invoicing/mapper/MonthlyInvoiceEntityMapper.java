package com.application.infrastructure.persistence.Billing.invoicing.mapper;

import com.application.domain.Billing.invoicing.domain.MonthlyInvoice;
import com.application.domain.Billing.invoicing.domain.InvoiceItem;
import com.application.domain.Billing.invoicing.valueobject.MonthlyInvoiceId;
import com.application.domain.Billing.invoicing.valueobject.InvoiceItemId;
import com.application.domain.Billing.invoicing.valueobject.BillingConceptId;
import com.application.domain.SchoolManagement.valueobject.StudentId;
import com.application.domain.SchoolManagement.valueobject.SchoolId;
import com.application.domain.shared.valueobject.Money;
import com.application.infrastructure.persistence.Billing.invoicing.entity.MonthlyInvoiceEntity;
import com.application.infrastructure.persistence.Billing.invoicing.entity.InvoiceItemEntity;
import com.application.infrastructure.persistence.Billing.invoicing.entity.BillingConceptEntity;
import com.application.infrastructure.persistence.SchoolManagement.student.entity.StudentEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class MonthlyInvoiceEntityMapper {

    public MonthlyInvoiceEntity toEntity(MonthlyInvoice domain) {
        if (domain == null) {
            return null;
        }

        MonthlyInvoiceEntity entity = new MonthlyInvoiceEntity();
        entity.setId(domain.getId() != null ? domain.getId().getValue() : null);
        entity.setStudent(StudentEntity.builder().id(domain.getStudentId().getValue()).build());
        entity.setBillingPeriod(domain.getBillingPeriod());
        entity.setIssueDate(domain.getIssueDate());
        entity.setDueDate(domain.getDueDate());
        entity.setTotalAmount(domain.getTotalAmount().getAmount());
        entity.setCurrency(domain.getTotalAmount().getCurrency());
        entity.setStatus(domain.getStatus().name());
        entity.setSchoolId(domain.getSchoolId().getValue());
        entity.setInvoiceItems(toInvoiceItemEntities(domain.getInvoiceItems(), entity));
        return entity;
    }

    public MonthlyInvoice toDomain(MonthlyInvoiceEntity entity) {
        if (entity == null) {
            return null;
        }

        List<InvoiceItem> invoiceItems = toInvoiceItemDomains(entity.getInvoiceItems());

        return MonthlyInvoice.builder()
                .id(new MonthlyInvoiceId(entity.getId()))
                .studentId(new StudentId(entity.getStudent().getId()))
                .billingPeriod(entity.getBillingPeriod())
                .issueDate(entity.getIssueDate())
                .dueDate(entity.getDueDate())
                .totalAmount(new Money(entity.getTotalAmount(), entity.getCurrency()))
                .status(MonthlyInvoice.InvoiceStatus.valueOf(entity.getStatus()))
                .schoolId(new SchoolId(entity.getSchoolId()))
                .invoiceItems(invoiceItems)
                .build();
    }

    private List<InvoiceItemEntity> toInvoiceItemEntities(List<InvoiceItem> domainItems, MonthlyInvoiceEntity invoiceEntity) {
        if (domainItems == null) {
            return List.of();
        }
        return domainItems.stream()
                .map(item -> {
                    InvoiceItemEntity itemEntity = new InvoiceItemEntity();
                    itemEntity.setId(item.getId() != null ? item.getId().getValue() : null);
                    itemEntity.setMonthlyInvoice(invoiceEntity);
                    itemEntity.setBillingConcept(BillingConceptEntity.builder().id(item.getBillingConceptId().getValue()).build());
                    itemEntity.setDescription(item.getDescription());
                    itemEntity.setQuantity(item.getQuantity());
                    itemEntity.setUnitPrice(item.getUnitPrice().getAmount());
                    itemEntity.setCurrency(item.getUnitPrice().getCurrency());
                    itemEntity.setSubtotal(item.getSubtotal().getAmount());
                    return itemEntity;
                })
                .collect(Collectors.toList());
    }

    private List<InvoiceItem> toInvoiceItemDomains(List<InvoiceItemEntity> entityItems) {
        if (entityItems == null) {
            return List.of();
        }
        return entityItems.stream()
                .map(item -> InvoiceItem.builder()
                        .id(new InvoiceItemId(item.getId()))
                        .monthlyInvoiceId(new MonthlyInvoiceId(item.getMonthlyInvoice().getId()))
                        .billingConceptId(new BillingConceptId(item.getBillingConcept().getId()))
                        .description(item.getDescription())
                        .quantity(item.getQuantity())
                        .unitPrice(new Money(item.getUnitPrice(), item.getCurrency()))
                        .subtotal(new Money(item.getSubtotal(), item.getCurrency()))
                        .build())
                .collect(Collectors.toList());
    }
}