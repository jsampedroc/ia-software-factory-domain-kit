package com.application.application.service;

import com.application.domain.model.billing.*;
import com.application.domain.model.studentmanagement.Student;
import com.application.domain.model.schooladministration.GradeLevel;
import com.application.domain.ports.in.BillingService;
import com.application.domain.ports.out.InvoiceRepository;
import com.application.domain.ports.out.FeeStructureRepository;
import com.application.domain.ports.out.StudentRepository;
import com.application.domain.ports.out.SchoolRepository;
import com.application.domain.enums.InvoiceStatus;
import com.application.domain.valueobject.Money;
import com.application.application.dto.InvoiceDTO;
import com.application.application.dto.PaymentDTO;
import com.application.application.mapper.BillingMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.YearMonth;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@Transactional
@RequiredArgsConstructor
@Slf4j
public class BillingServiceImpl implements BillingService {

    private final InvoiceRepository invoiceRepository;
    private final FeeStructureRepository feeStructureRepository;
    private final StudentRepository studentRepository;
    private final SchoolRepository schoolRepository;
    private final BillingMapper billingMapper;

    @Override
    public InvoiceDTO generateMonthlyInvoice(UUID studentId, YearMonth billingMonth) {
        log.info("Generating monthly invoice for studentId: {} and month: {}", studentId, billingMonth);

        Student student = studentRepository.findById(new com.application.domain.model.studentmanagement.StudentId(studentId))
                .orElseThrow(() -> new IllegalArgumentException("Student not found with id: " + studentId));

        Enrollment activeEnrollment = student.getActiveEnrollment()
                .orElseThrow(() -> new IllegalStateException("Student does not have an active enrollment for billing"));

        GradeLevel gradeLevel = activeEnrollment.getGradeLevel();

        FeeStructure activeFee = feeStructureRepository.findActiveByGradeLevel(gradeLevel.getId())
                .orElseThrow(() -> new IllegalStateException("No active fee structure found for grade level: " + gradeLevel.getId()));

        Money subtotal = activeFee.getMonthlyTuition();

        Money adjustments = calculateAdjustments(studentId, billingMonth, subtotal);

        Money totalAmount = subtotal.add(adjustments);

        InvoiceNumber invoiceNumber = generateInvoiceNumber();

        Invoice invoice = Invoice.builder()
                .id(new InvoiceId(UUID.randomUUID()))
                .invoiceNumber(invoiceNumber)
                .student(student)
                .billingMonth(billingMonth)
                .issueDate(LocalDate.now())
                .dueDate(LocalDate.now().plusDays(15))
                .subtotal(subtotal)
                .adjustments(adjustments)
                .totalAmount(totalAmount)
                .status(InvoiceStatus.DRAFT)
                .build();

        Invoice savedInvoice = invoiceRepository.save(invoice);
        log.info("Invoice generated successfully with id: {}", savedInvoice.getId().getValue());
        return billingMapper.toDto(savedInvoice);
    }

    @Override
    public InvoiceDTO issueInvoice(UUID invoiceId) {
        log.info("Issuing invoice with id: {}", invoiceId);
        Invoice invoice = invoiceRepository.findById(new InvoiceId(invoiceId))
                .orElseThrow(() -> new IllegalArgumentException("Invoice not found with id: " + invoiceId));

        if (invoice.getStatus() != InvoiceStatus.DRAFT) {
            throw new IllegalStateException("Only DRAFT invoices can be issued. Current status: " + invoice.getStatus());
        }

        invoice.issue();
        Invoice updatedInvoice = invoiceRepository.save(invoice);
        return billingMapper.toDto(updatedInvoice);
    }

    @Override
    public PaymentDTO registerPayment(UUID invoiceId, BigDecimal amount, String referenceNumber, String receivedBy) {
        log.info("Registering payment for invoiceId: {}, amount: {}", invoiceId, amount);
        Invoice invoice = invoiceRepository.findById(new InvoiceId(invoiceId))
                .orElseThrow(() -> new IllegalArgumentException("Invoice not found with id: " + invoiceId));

        if (invoice.getStatus() == InvoiceStatus.CANCELLED) {
            throw new IllegalStateException("Cannot register payment for a CANCELLED invoice.");
        }

        Money paymentAmount = new Money(amount, invoice.getTotalAmount().getCurrency());

        Payment payment = Payment.builder()
                .id(new PaymentId(UUID.randomUUID()))
                .invoice(invoice)
                .amount(paymentAmount)
                .paymentDate(LocalDate.now())
                .receivedBy(receivedBy)
                .referenceNumber(referenceNumber)
                .build();

        invoice.registerPayment(payment);

        Invoice updatedInvoice = invoiceRepository.save(invoice);
        Payment savedPayment = updatedInvoice.getPayments().stream()
                .filter(p -> p.getReferenceNumber().equals(referenceNumber))
                .findFirst()
                .orElseThrow(() -> new RuntimeException("Payment not found after registration"));

        return billingMapper.toDto(savedPayment);
    }

    @Override
    public List<InvoiceDTO> findInvoicesByStudent(UUID studentId) {
        log.debug("Finding invoices for studentId: {}", studentId);
        List<Invoice> invoices = invoiceRepository.findByStudentId(new com.application.domain.model.studentmanagement.StudentId(studentId));
        return invoices.stream()
                .map(billingMapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<InvoiceDTO> findInvoicesByStatus(InvoiceStatus status) {
        log.debug("Finding invoices by status: {}", status);
        List<Invoice> invoices = invoiceRepository.findByStatus(status);
        return invoices.stream()
                .map(billingMapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    public Optional<InvoiceDTO> findInvoiceById(UUID invoiceId) {
        log.debug("Finding invoice by id: {}", invoiceId);
        return invoiceRepository.findById(new InvoiceId(invoiceId))
                .map(billingMapper::toDto);
    }

    @Override
    public InvoiceDTO applyDiscount(UUID invoiceId, BigDecimal discountAmount, String reason) {
        log.info("Applying discount to invoiceId: {}, amount: {}, reason: {}", invoiceId, discountAmount, reason);
        Invoice invoice = invoiceRepository.findById(new InvoiceId(invoiceId))
                .orElseThrow(() -> new IllegalArgumentException("Invoice not found with id: " + invoiceId));

        if (invoice.getStatus() != InvoiceStatus.DRAFT && invoice.getStatus() != InvoiceStatus.ISSUED) {
            throw new IllegalStateException("Discount can only be applied to DRAFT or ISSUED invoices. Current status: " + invoice.getStatus());
        }

        Money discount = new Money(discountAmount.negate(), invoice.getTotalAmount().getCurrency());
        invoice.applyAdjustment(discount, reason);

        Invoice updatedInvoice = invoiceRepository.save(invoice);
        return billingMapper.toDto(updatedInvoice);
    }

    @Override
    public InvoiceDTO applyLateFee(UUID invoiceId, BigDecimal lateFeeAmount, String reason) {
        log.info("Applying late fee to invoiceId: {}, amount: {}, reason: {}", invoiceId, lateFeeAmount, reason);
        Invoice invoice = invoiceRepository.findById(new InvoiceId(invoiceId))
                .orElseThrow(() -> new IllegalArgumentException("Invoice not found with id: " + invoiceId));

        if (invoice.getStatus() != InvoiceStatus.DRAFT && invoice.getStatus() != InvoiceStatus.ISSUED) {
            throw new IllegalStateException("Late fee can only be applied to DRAFT or ISSUED invoices. Current status: " + invoice.getStatus());
        }

        Money lateFee = new Money(lateFeeAmount, invoice.getTotalAmount().getCurrency());
        invoice.applyAdjustment(lateFee, reason);

        Invoice updatedInvoice = invoiceRepository.save(invoice);
        return billingMapper.toDto(updatedInvoice);
    }

    @Override
    public void cancelInvoice(UUID invoiceId, String reason) {
        log.info("Cancelling invoice with id: {}, reason: {}", invoiceId, reason);
        Invoice invoice = invoiceRepository.findById(new InvoiceId(invoiceId))
                .orElseThrow(() -> new IllegalArgumentException("Invoice not found with id: " + invoiceId));

        invoice.cancel(reason);
        invoiceRepository.save(invoice);
    }

    private Money calculateAdjustments(UUID studentId, YearMonth billingMonth, Money baseTuition) {
        // Placeholder for complex adjustment logic (e.g., based on attendance summary, scholarships, etc.)
        // For now, returns zero adjustments.
        log.debug("Calculating adjustments for studentId: {}, month: {}", studentId, billingMonth);
        return new Money(BigDecimal.ZERO, baseTuition.getCurrency());
    }

    private InvoiceNumber generateInvoiceNumber() {
        // Simple implementation. In a real scenario, this would follow a specific sequence or pattern.
        String number = "INV-" + LocalDate.now().getYear() + "-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase();
        return new InvoiceNumber(number);
    }
}