package com.application.school.application.dtos;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class InvoiceDTO {
    private String invoiceId;
    private String studentId;
    private LocalDate issueDate;
    private LocalDate dueDate;
    private String monthYear;
    private String totalAmount;
    private String currency;
    private String status;
    private LocalDate paymentDate;
    private List<InvoiceItemDTO> items;
    private List<PaymentDTO> payments;
}