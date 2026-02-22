package com.application.school.infrastructure.persistence.billing;

import com.application.school.infrastructure.persistence.billing.entity.InvoiceEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface InvoiceJpaRepository extends JpaRepository<InvoiceEntity, Long> {
    Optional<InvoiceEntity> findByInvoiceId(String invoiceId);
    List<InvoiceEntity> findByStudentId(String studentId);
    Optional<InvoiceEntity> findByStudentIdAndMonthYear(String studentId, String monthYear);
    List<InvoiceEntity> findByStatus(String status);
}