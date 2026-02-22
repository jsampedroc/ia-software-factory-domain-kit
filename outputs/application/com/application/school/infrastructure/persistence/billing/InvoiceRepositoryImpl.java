package com.application.school.infrastructure.persistence.billing;

import com.application.school.domain.billing.model.Invoice;
import com.application.school.domain.billing.model.InvoiceId;
import com.application.school.domain.billing.repository.InvoiceRepository;
import com.application.school.infrastructure.persistence.billing.entity.InvoiceEntity;
import com.application.school.infrastructure.persistence.billing.mapper.InvoicePersistenceMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Repository
@RequiredArgsConstructor
public class InvoiceRepositoryImpl implements InvoiceRepository {

    private final InvoiceJpaRepository invoiceJpaRepository;
    private final InvoicePersistenceMapper invoicePersistenceMapper;

    @Override
    public Invoice save(Invoice invoice) {
        InvoiceEntity entity = invoicePersistenceMapper.toEntity(invoice);
        InvoiceEntity savedEntity = invoiceJpaRepository.save(entity);
        return invoicePersistenceMapper.toDomain(savedEntity);
    }

    @Override
    public Optional<Invoice> findById(InvoiceId invoiceId) {
        return invoiceJpaRepository.findById(invoiceId.getValue())
                .map(invoicePersistenceMapper::toDomain);
    }

    @Override
    public List<Invoice> findAll() {
        return invoiceJpaRepository.findAll()
                .stream()
                .map(invoicePersistenceMapper::toDomain)
                .collect(Collectors.toList());
    }

    @Override
    public void deleteById(InvoiceId invoiceId) {
        invoiceJpaRepository.deleteById(invoiceId.getValue());
    }

    @Override
    public boolean existsById(InvoiceId invoiceId) {
        return invoiceJpaRepository.existsById(invoiceId.getValue());
    }

    @Override
    public Optional<Invoice> findByStudentIdAndMonthYear(String studentIdValue, String monthYear) {
        return invoiceJpaRepository.findByStudentEntity_StudentIdAndMonthYear(studentIdValue, monthYear)
                .map(invoicePersistenceMapper::toDomain);
    }

    @Override
    public List<Invoice> findByStudentId(String studentIdValue) {
        return invoiceJpaRepository.findByStudentEntity_StudentId(studentIdValue)
                .stream()
                .map(invoicePersistenceMapper::toDomain)
                .collect(Collectors.toList());
    }

    @Override
    public List<Invoice> findByStatus(String status) {
        return invoiceJpaRepository.findByStatus(status)
                .stream()
                .map(invoicePersistenceMapper::toDomain)
                .collect(Collectors.toList());
    }
}