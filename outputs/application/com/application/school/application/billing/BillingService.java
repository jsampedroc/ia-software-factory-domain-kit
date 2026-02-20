package com.application.school.application.billing;

import com.application.school.domain.billing.model.Invoice;
import com.application.school.domain.billing.model.InvoiceId;
import com.application.school.domain.billing.model.InvoiceStatus;
import com.application.school.domain.billing.model.Payment;
import com.application.school.domain.billing.repository.InvoiceRepository;
import com.application.school.domain.student.model.StudentId;
import com.application.school.domain.student.repository.StudentRepository;
import com.application.school.application.dtos.InvoiceDTO;
import com.application.school.application.dtos.PaymentDTO;
import com.application.school.application.mappers.BillingMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class BillingService {

    private final InvoiceRepository invoiceRepository;
    private final StudentRepository studentRepository;
    private final BillingMapper billingMapper;

    @Transactional(readOnly = true)
    public List<InvoiceDTO> getInvoicesByStudent(StudentId studentId) {
        log.debug("Fetching invoices for student: {}", studentId);
        List<Invoice> invoices = invoiceRepository.findByStudentId(studentId);
        return invoices.stream()
                .map(billingMapper::toDto)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public Optional<InvoiceDTO> getInvoiceById(InvoiceId invoiceId) {
        log.debug("Fetching invoice by id: {}", invoiceId);
        return invoiceRepository.findById(invoiceId)
                .map(billingMapper::toDto);
    }

    @Transactional
    public InvoiceDTO createInvoice(InvoiceDTO invoiceDto) {
        log.info("Creating invoice for student: {}", invoiceDto.getStudentId());
        StudentId studentId = new StudentId(invoiceDto.getStudentId());

        // Business rule: Cannot create invoice for inactive student
        studentRepository.findById(studentId).ifPresent(student -> {
            if (!student.isActive()) {
                throw new IllegalStateException("Cannot create invoice for an inactive student.");
            }
        });

        // Business rule: Only one invoice per student per monthYear
        Optional<Invoice> existingInvoice = invoiceRepository.findByStudentIdAndMonthYear(
                studentId,
                invoiceDto.getMonthYear()
        );
        if (existingInvoice.isPresent()) {
            throw new IllegalStateException(
                    String.format("An invoice already exists for student %s and month %s",
                            studentId.getValue(),
                            invoiceDto.getMonthYear())
            );
        }

        Invoice invoice = billingMapper.toDomain(invoiceDto);
        invoice.calculateTotal(); // Ensure total is calculated from items
        invoice.updateStatus(); // Set initial status based on due date

        Invoice savedInvoice = invoiceRepository.save(invoice);
        log.info("Invoice created with id: {}", savedInvoice.getInvoiceId());
        return billingMapper.toDto(savedInvoice);
    }

    @Transactional
    public InvoiceDTO updateInvoiceStatus(InvoiceId invoiceId, InvoiceStatus newStatus) {
        log.info("Updating status for invoice: {} to {}", invoiceId, newStatus);
        Invoice invoice = invoiceRepository.findById(invoiceId)
                .orElseThrow(() -> new IllegalArgumentException("Invoice not found: " + invoiceId));

        // Business rule: Cannot modify a cancelled or paid invoice
        if (invoice.getStatus() == InvoiceStatus.CANCELLED || invoice.getStatus() == InvoiceStatus.PAID) {
            throw new IllegalStateException("Cannot modify a cancelled or paid invoice.");
        }

        invoice.setStatus(newStatus);
        Invoice updatedInvoice = invoiceRepository.save(invoice);
        return billingMapper.toDto(updatedInvoice);
    }

    @Transactional
    public PaymentDTO registerPayment(PaymentDTO paymentDto) {
        log.info("Registering payment: {}", paymentDto);
        Payment payment = billingMapper.toDomain(paymentDto);

        // Business rule: Payment amount cannot be negative
        if (payment.getAmount().isNegative()) {
            throw new IllegalArgumentException("Payment amount cannot be negative.");
        }

        // Apply payment to specified invoices
        List<InvoiceId> invoiceIds = paymentDto.getAppliedInvoiceIds().stream()
                .map(InvoiceId::new)
                .collect(Collectors.toList());

        List<Invoice> invoices = invoiceRepository.findAllById(invoiceIds);
        if (invoices.isEmpty()) {
            throw new IllegalArgumentException("No valid invoices found for payment application.");
        }

        // Apply payment logic (simplified: assign full payment to first invoice)
        // In a real scenario, you would have a more complex allocation strategy
        Invoice primaryInvoice = invoices.get(0);
        primaryInvoice.applyPayment(payment);
        primaryInvoice.updateStatus(); // Re-evaluate status after payment

        invoiceRepository.save(primaryInvoice);
        // Note: In a full implementation, you would save the payment entity via a PaymentRepository
        // and update all affected invoices.

        log.info("Payment registered and applied to invoice: {}", primaryInvoice.getInvoiceId());
        return billingMapper.toDto(payment);
    }

    @Transactional
    public void processOverdueInvoices() {
        log.info("Processing overdue invoices");
        List<Invoice> pendingInvoices = invoiceRepository.findByStatus(InvoiceStatus.PENDING);
        LocalDate today = LocalDate.now();

        pendingInvoices.stream()
                .filter(invoice -> invoice.getDueDate().isBefore(today))
                .forEach(invoice -> {
                    invoice.setStatus(InvoiceStatus.OVERDUE);
                    invoiceRepository.save(invoice);
                    log.debug("Invoice {} marked as OVERDUE", invoice.getInvoiceId());
                });
    }

    @Transactional(readOnly = true)
    public List<InvoiceDTO> getOverdueInvoices() {
        log.debug("Fetching overdue invoices");
        List<Invoice> overdueInvoices = invoiceRepository.findByStatus(InvoiceStatus.OVERDUE);
        return overdueInvoices.stream()
                .map(billingMapper::toDto)
                .collect(Collectors.toList());
    }
}