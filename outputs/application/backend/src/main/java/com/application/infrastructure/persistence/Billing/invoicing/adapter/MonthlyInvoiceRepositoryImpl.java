package com.application.infrastructure.persistence.Billing.invoicing.adapter;

import com.application.domain.Billing.invoicing.domain.MonthlyInvoice;
import com.application.domain.Billing.invoicing.domain.repository.MonthlyInvoiceRepository;
import com.application.domain.Billing.invoicing.valueobject.MonthlyInvoiceId;
import com.application.domain.SchoolManagement.valueobject.StudentId;
import com.application.infrastructure.persistence.Billing.invoicing.entity.MonthlyInvoiceEntity;
import com.application.infrastructure.persistence.Billing.invoicing.mapper.MonthlyInvoiceEntityMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.time.YearMonth;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class MonthlyInvoiceRepositoryImpl implements MonthlyInvoiceRepository {

    private final MonthlyInvoiceJpaRepository monthlyInvoiceJpaRepository;
    private final MonthlyInvoiceEntityMapper monthlyInvoiceEntityMapper;

    @Override
    public MonthlyInvoice save(MonthlyInvoice monthlyInvoice) {
        MonthlyInvoiceEntity entity = monthlyInvoiceEntityMapper.toEntity(monthlyInvoice);
        MonthlyInvoiceEntity savedEntity = monthlyInvoiceJpaRepository.save(entity);
        return monthlyInvoiceEntityMapper.toDomain(savedEntity);
    }

    @Override
    public Optional<MonthlyInvoice> findById(MonthlyInvoiceId monthlyInvoiceId) {
        return monthlyInvoiceJpaRepository.findById(monthlyInvoiceId.getValue())
                .map(monthlyInvoiceEntityMapper::toDomain);
    }

    @Override
    public boolean existsByStudentIdAndBillingPeriod(StudentId studentId, YearMonth billingPeriod) {
        return monthlyInvoiceJpaRepository.existsByStudentIdAndBillingPeriod(studentId.getValue(), billingPeriod);
    }

    @Override
    public Optional<MonthlyInvoice> findByStudentIdAndBillingPeriod(StudentId studentId, YearMonth billingPeriod) {
        return monthlyInvoiceJpaRepository.findByStudentIdAndBillingPeriod(studentId.getValue(), billingPeriod)
                .map(monthlyInvoiceEntityMapper::toDomain);
    }
}