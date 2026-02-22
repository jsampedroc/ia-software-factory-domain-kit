package com.application.school.application.billing.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class GenerateInvoiceCommand {
    private String studentId;
    private LocalDate issueDate;
    private LocalDate dueDate;
    private String monthYear;
    private List<InvoiceItemCommand> items;

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class InvoiceItemCommand {
        private String description;
        private Integer quantity;
        private BigDecimal unitPrice;
    }
}