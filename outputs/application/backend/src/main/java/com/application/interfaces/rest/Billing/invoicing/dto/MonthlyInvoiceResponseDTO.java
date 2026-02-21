package com.application.interfaces.rest.Billing.invoicing.dto;

import com.application.domain.Billing.invoicing.domain.MonthlyInvoice;
import com.application.domain.Billing.invoicing.domain.InvoiceItem;
import com.application.domain.shared.valueobject.Money;
import lombok.Data;
import lombok.Builder;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Data
@Builder
public class MonthlyInvoiceResponseDTO {
    private String id;
    private String studentId;
    private String billingPeriod;
    private LocalDate issueDate;
    private LocalDate dueDate;
    private Money totalAmount;
    private String status;
    private String schoolId;
    private List<InvoiceItemResponseDTO> items;

    public static MonthlyInvoiceResponseDTO fromDomain(MonthlyInvoice invoice) {
        return MonthlyInvoiceResponseDTO.builder()
                .id(invoice.getId().getValue().toString())
                .studentId(invoice.getStudentId().getValue().toString())
                .billingPeriod(invoice.getBillingPeriod())
                .issueDate(invoice.getIssueDate())
                .dueDate(invoice.getDueDate())
                .totalAmount(invoice.getTotalAmount())
                .status(invoice.getStatus().name())
                .schoolId(invoice.getSchoolId().getValue().toString())
                .items(invoice.getItems().stream()
                        .map(InvoiceItemResponseDTO::fromDomain)
                        .collect(Collectors.toList()))
                .build();
    }

    @Data
    @Builder
    public static class InvoiceItemResponseDTO {
        private String id;
        private String billingConceptId;
        private String description;
        private Integer quantity;
        private Money unitPrice;
        private Money subtotal;

        public static InvoiceItemResponseDTO fromDomain(InvoiceItem item) {
            return InvoiceItemResponseDTO.builder()
                    .id(item.getId().getValue().toString())
                    .billingConceptId(item.getBillingConceptId().getValue().toString())
                    .description(item.getDescription())
                    .quantity(item.getQuantity())
                    .unitPrice(item.getUnitPrice())
                    .subtotal(item.getSubtotal())
                    .build();
        }
    }
}