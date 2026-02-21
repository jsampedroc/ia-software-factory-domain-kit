package com.application.interfaces.rest.Billing.invoicing.dto;

import lombok.Data;
import lombok.Builder;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import java.time.YearMonth;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class GenerateInvoiceRequestDTO {
    private String studentId;
    private YearMonth billingPeriod;
    private String schoolId;
}