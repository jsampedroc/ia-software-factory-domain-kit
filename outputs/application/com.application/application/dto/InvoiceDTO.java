package com.application.application.dto;

import com.application.domain.enums.InvoiceStatus;
import com.application.domain.model.billing.InvoiceId;
import com.application.domain.model.billing.InvoiceNumber;
import com.application.domain.model.studentmanagement.StudentId;
import com.application.domain.valueobject.Money;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;
import java.time.YearMonth;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class InvoiceDTO {
    @Valid
    private InvoiceId invoiceId;
    @NotNull
    @Valid
    private InvoiceNumber invoiceNumber;
    @NotNull
    @Valid
    private StudentId studentId;
    @NotNull
    private YearMonth billingMonth;
    @NotNull
    private LocalDate issueDate;
    @NotNull
    private LocalDate dueDate;
    @NotNull
    @Valid
    private Money subtotal;
    @Valid
    private Money adjustments;
    @NotNull
    @Valid
    private Money totalAmount;
    @NotNull
    private InvoiceStatus status;
    private String paymentMethod;
}