package com.application.infrastructure.persistence.adapter;

import com.application.domain.model.billing.Invoice;
import com.application.domain.model.billing.InvoiceId;
import com.application.domain.ports.out.InvoiceRepository;
import com.application.infrastructure.persistence.jpa.InvoiceJpaRepository;
import com.application.infrastructure.persistence.jpa.InvoiceEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class InvoiceRepositoryAdapter implements InvoiceRepository {

    private final InvoiceJpaRepository invoiceJpaRepository;
    private final BillingMapper billingMapper;

    @Override
    public Invoice save(Invoice invoice) {
        InvoiceEntity entity = billingMapper.toEntity(invoice);
        InvoiceEntity savedEntity = invoiceJpaRepository.save(entity);
        return billingMapper.toDomain(savedEntity);
    }

    @Override
    public Optional<Invoice> findById(InvoiceId invoiceId) {
        return invoiceJpaRepository.findById(invoiceId.getValue())
                .map(billingMapper::toDomain);
    }

    @Override
    public List<Invoice> findAll() {
        return invoiceJpaRepository.findAll()
                .stream()
                .map(billingMapper::toDomain)
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
    public List<Invoice> findByStudentIdAndStatus(com.application.domain.model.studentmanagement.StudentId studentId, com.application.domain.enums.InvoiceStatus status) {
        return invoiceJpaRepository.findByStudentEntity_StudentIdAndStatus(studentId.getValue(), status)
                .stream()
                .map(billingMapper::toDomain)
                .collect(Collectors.toList());
    }

    @Override
    public List<Invoice> findByBillingMonthAndYear(Integer month, Integer year) {
        return invoiceJpaRepository.findByBillingMonthAndBillingYear(month, year)
                .stream()
                .map(billingMapper::toDomain)
                .collect(Collectors.toList());
    }

    @Override
    public Optional<Invoice> findByInvoiceNumber(String invoiceNumber) {
        return invoiceJpaRepository.findByInvoiceNumber(invoiceNumber)
                .map(billingMapper::toDomain);
    }
}