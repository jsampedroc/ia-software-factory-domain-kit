package com.application.school.infrastructure.persistence.jpa.billing;

import com.application.school.domain.billing.model.Invoice;
import com.application.school.domain.billing.model.InvoiceId;
import com.application.school.domain.billing.repository.InvoiceRepository;
import com.application.school.domain.student.model.StudentId;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.time.Month;
import java.time.Year;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Repository
@RequiredArgsConstructor
public class InvoiceJpaRepositoryAdapter implements InvoiceRepository {

    private final JpaInvoiceRepository jpaInvoiceRepository;
    private final InvoiceJpaMapper invoiceJpaMapper;

    @Override
    public Invoice save(Invoice invoice) {
        InvoiceJpaEntity entity = invoiceJpaMapper.toEntity(invoice);
        InvoiceJpaEntity savedEntity = jpaInvoiceRepository.save(entity);
        return invoiceJpaMapper.toDomain(savedEntity);
    }

    @Override
    public Optional<Invoice> findById(InvoiceId invoiceId) {
        return jpaInvoiceRepository.findById(invoiceId.getValue())
                .map(invoiceJpaMapper::toDomain);
    }

    @Override
    public void deleteById(InvoiceId invoiceId) {
        jpaInvoiceRepository.deleteById(invoiceId.getValue());
    }

    @Override
    public List<Invoice> findByStudentId(StudentId studentId) {
        return jpaInvoiceRepository.findByStudentId(studentId.getValue())
                .stream()
                .map(invoiceJpaMapper::toDomain)
                .collect(Collectors.toList());
    }

    @Override
    public Optional<Invoice> findByStudentIdAndMonthYear(StudentId studentId, Year year, Month month) {
        return jpaInvoiceRepository.findByStudentIdAndYearAndMonth(studentId.getValue(), year.getValue(), month.getValue())
                .map(invoiceJpaMapper::toDomain);
    }

    @Override
    public List<Invoice> findAllPending() {
        return jpaInvoiceRepository.findByStatus("PENDING")
                .stream()
                .map(invoiceJpaMapper::toDomain)
                .collect(Collectors.toList());
    }

    @Override
    public List<Invoice> findAllOverdue() {
        return jpaInvoiceRepository.findByStatus("OVERDUE")
                .stream()
                .map(invoiceJpaMapper::toDomain)
                .collect(Collectors.toList());
    }
}