package com.application.infrastructure.persistence.Billing.invoicing.adapter;

import com.application.infrastructure.persistence.Billing.invoicing.entity.MonthlyInvoiceEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.YearMonth;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface MonthlyInvoiceJpaRepository extends JpaRepository<MonthlyInvoiceEntity, UUID> {
    Optional<MonthlyInvoiceEntity> findByStudentIdAndBillingPeriod(UUID studentId, YearMonth billingPeriod);
    List<MonthlyInvoiceEntity> findAllByStudentId(UUID studentId);
    List<MonthlyInvoiceEntity> findAllBySchoolId(UUID schoolId);
    List<MonthlyInvoiceEntity> findAllByStatus(String status);
}