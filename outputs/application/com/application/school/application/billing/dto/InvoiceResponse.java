package com.application.school.application.billing.dto;

import com.application.school.domain.shared.enumeration.InvoiceStatus;
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
public class InvoiceResponse {
    private String invoiceId;
    private String studentId;
    private String studentName;
    private LocalDate issueDate;
    private LocalDate dueDate;
    private String monthYear;
    private BigDecimal totalAmount;
    private String currency;
    private InvoiceStatus status;
    private LocalDate paymentDate;
    private List<InvoiceItemResponse> items;
    private List<PaymentResponse> payments;

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class InvoiceItemResponse {
        private String itemId;
        private String description;
        private Integer quantity;
        private BigDecimal unitPrice;
        private BigDecimal subtotal;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class PaymentResponse {
        private String paymentId;
        private BigDecimal amount;
        private String paymentMethod;
        private LocalDate transactionDate;
        private String reference;
    }
}