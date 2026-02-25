package com.application.domain.repository;

import com.application.domain.shared.EntityRepository;
import com.application.domain.model.Invoice;
import com.application.domain.valueobject.InvoiceId;
import com.application.domain.valueobject.PatientId;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface InvoiceRepository extends EntityRepository<Invoice, InvoiceId> {

    Optional<Invoice> findByInvoiceNumber(String invoiceNumber);

    List<Invoice> findByPatientId(PatientId patientId);

    List<Invoice> findByStatus(String status);

    List<Invoice> findByIssueDateBetween(LocalDate startDate, LocalDate endDate);

    List<Invoice> findByDueDateBeforeAndStatusNot(LocalDate date, String status);

    boolean existsByInvoiceNumber(String invoiceNumber);
}