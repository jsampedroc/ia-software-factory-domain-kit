package com.application.domain.Billing.invoicing.domain.repository;

import com.application.domain.Billing.invoicing.domain.MonthlyInvoice;
import com.application.domain.Billing.invoicing.valueobject.MonthlyInvoiceId;
import com.application.domain.SchoolManagement.valueobject.SchoolId;
import com.application.domain.SchoolManagement.valueobject.StudentId;

import java.time.YearMonth;
import java.util.List;
import java.util.Optional;

public interface MonthlyInvoiceRepository {
    MonthlyInvoice save(MonthlyInvoice invoice);
    Optional<MonthlyInvoice> findById(MonthlyInvoiceId id);
    List<MonthlyInvoice> findByStudentId(StudentId studentId);
    List<MonthlyInvoice> findBySchoolId(SchoolId schoolId);
    Optional<MonthlyInvoice> findByStudentIdAndBillingPeriod(StudentId studentId, YearMonth billingPeriod);
    boolean existsById(MonthlyInvoiceId id);
    void delete(MonthlyInvoice invoice);
}