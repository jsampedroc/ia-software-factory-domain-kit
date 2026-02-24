package com.application.domain.port;

import com.application.domain.model.Invoice;
import com.application.domain.valueobject.InvoiceId;
import com.application.domain.valueobject.InvoiceStatus;
import com.application.domain.valueobject.PatientId;
import com.application.domain.shared.EntityRepository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface InvoiceRepositoryPort extends EntityRepository<Invoice, InvoiceId> {
    Optional<Invoice> findByInvoiceNumber(String invoiceNumber);
    List<Invoice> findByPatientId(PatientId patientId);
    List<Invoice> findByStatus(InvoiceStatus status);
    List<Invoice> findByIssueDateBetween(LocalDate startDate, LocalDate endDate);
    List<Invoice> findByDueDateBeforeAndStatusNot(LocalDate date, InvoiceStatus excludedStatus);
    boolean existsByInvoiceNumber(String invoiceNumber);
}