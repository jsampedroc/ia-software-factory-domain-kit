package com.application.application.Billing.invoicing.application.generate;

import com.application.domain.Billing.valueobject.MonthlyInvoiceId;
import com.application.domain.SchoolManagement.valueobject.StudentId;
import com.application.domain.SchoolManagement.valueobject.SchoolId;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDate;
import java.time.YearMonth;

@Getter
@Builder
public class GenerateMonthlyInvoiceCommand {
    private final MonthlyInvoiceId invoiceId;
    private final StudentId studentId;
    private final YearMonth billingPeriod;
    private final LocalDate issueDate;
    private final LocalDate dueDate;
    private final SchoolId schoolId;
}