package com.application.school.domain.billing.repository;

import com.application.school.domain.billing.model.Invoice;
import com.application.school.domain.billing.model.InvoiceId;
import com.application.school.domain.student.model.StudentId;

import java.time.YearMonth;
import java.util.List;
import java.util.Optional;

public interface InvoiceRepository {
    Invoice save(Invoice invoice);
    Optional<Invoice> findById(InvoiceId invoiceId);
    List<Invoice> findByStudentId(StudentId studentId);
    Optional<Invoice> findByStudentIdAndMonthYear(StudentId studentId, YearMonth monthYear);
    List<Invoice> findAll();
    void delete(Invoice invoice);
    boolean existsById(InvoiceId invoiceId);
}