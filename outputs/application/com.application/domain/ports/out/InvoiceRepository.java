package com.application.domain.ports.out;

import com.application.domain.model.billing.Invoice;
import com.application.domain.model.billing.InvoiceId;
import com.application.domain.model.studentmanagement.StudentId;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface InvoiceRepository {
    Invoice save(Invoice invoice);
    Optional<Invoice> findById(InvoiceId invoiceId);
    List<Invoice> findByStudentId(StudentId studentId);
    List<Invoice> findByStudentIdAndBillingMonth(StudentId studentId, LocalDate billingMonth);
    List<Invoice> findByStatus(String status);
    void deleteById(InvoiceId invoiceId);
    boolean existsById(InvoiceId invoiceId);
    Optional<Invoice> findByInvoiceNumber(String invoiceNumber);
}