package com.application.school.application.billing;

import com.application.school.application.billing.dto.GenerateInvoiceCommand;
import com.application.school.application.billing.dto.InvoiceResponse;
import com.application.school.application.billing.dto.RegisterPaymentCommand;
import com.application.school.domain.billing.model.Invoice;
import com.application.school.domain.billing.model.InvoiceId;
import com.application.school.domain.billing.model.Payment;
import com.application.school.domain.billing.repository.InvoiceRepository;
import com.application.school.domain.student.model.StudentId;
import com.application.school.domain.student.repository.StudentRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class BillingService {

    private final InvoiceRepository invoiceRepository;
    private final StudentRepository studentRepository;

    @Transactional
    public InvoiceResponse generateInvoice(GenerateInvoiceCommand command) {
        log.info("Generating invoice for student: {} monthYear: {}", command.getStudentId(), command.getMonthYear());

        StudentId studentId = new StudentId(command.getStudentId());
        studentRepository.findById(studentId)
                .orElseThrow(() -> new IllegalArgumentException("Student not found with id: " + command.getStudentId()));

        List<Invoice> existingInvoices = invoiceRepository.findByStudentIdAndMonthYear(studentId, command.getMonthYear());
        if (!existingInvoices.isEmpty()) {
            throw new IllegalStateException("An invoice already exists for student " + command.getStudentId() +
                    " and monthYear " + command.getMonthYear());
        }

        Invoice invoice = Invoice.create(
                studentId,
                command.getMonthYear(),
                command.getDueDate(),
                command.getItems().stream()
                        .map(item -> Invoice.ItemDetail.builder()
                                .description(item.getDescription())
                                .quantity(item.getQuantity())
                                .unitPrice(item.getUnitPrice())
                                .build())
                        .collect(Collectors.toList())
        );

        Invoice savedInvoice = invoiceRepository.save(invoice);
        log.info("Invoice generated with id: {}", savedInvoice.getId().getValue());

        return InvoiceResponse.builder()
                .invoiceId(savedInvoice.getId().getValue())
                .studentId(savedInvoice.getStudentId().getValue())
                .issueDate(savedInvoice.getIssueDate())
                .dueDate(savedInvoice.getDueDate())
                .monthYear(savedInvoice.getMonthYear())
                .totalAmount(savedInvoice.getTotalAmount())
                .status(savedInvoice.getStatus())
                .items(savedInvoice.getItems().stream()
                        .map(item -> InvoiceResponse.ItemDetail.builder()
                                .description(item.getDescription())
                                .quantity(item.getQuantity())
                                .unitPrice(item.getUnitPrice())
                                .subtotal(item.getSubtotal())
                                .build())
                        .collect(Collectors.toList()))
                .build();
    }

    @Transactional
    public InvoiceResponse registerPayment(RegisterPaymentCommand command) {
        log.info("Registering payment for invoice: {}", command.getInvoiceId());

        InvoiceId invoiceId = new InvoiceId(command.getInvoiceId());
        Invoice invoice = invoiceRepository.findById(invoiceId)
                .orElseThrow(() -> new IllegalArgumentException("Invoice not found with id: " + command.getInvoiceId()));

        Payment payment = Payment.create(
                command.getAmount(),
                command.getPaymentMethod(),
                command.getReference()
        );

        invoice.registerPayment(payment);
        Invoice updatedInvoice = invoiceRepository.save(invoice);
        log.info("Payment registered for invoice: {}", updatedInvoice.getId().getValue());

        return InvoiceResponse.builder()
                .invoiceId(updatedInvoice.getId().getValue())
                .studentId(updatedInvoice.getStudentId().getValue())
                .issueDate(updatedInvoice.getIssueDate())
                .dueDate(updatedInvoice.getDueDate())
                .monthYear(updatedInvoice.getMonthYear())
                .totalAmount(updatedInvoice.getTotalAmount())
                .status(updatedInvoice.getStatus())
                .paymentDate(updatedInvoice.getPaymentDate())
                .items(updatedInvoice.getItems().stream()
                        .map(item -> InvoiceResponse.ItemDetail.builder()
                                .description(item.getDescription())
                                .quantity(item.getQuantity())
                                .unitPrice(item.getUnitPrice())
                                .subtotal(item.getSubtotal())
                                .build())
                        .collect(Collectors.toList()))
                .payments(updatedInvoice.getPayments().stream()
                        .map(p -> InvoiceResponse.PaymentDetail.builder()
                                .paymentId(p.getId().getValue())
                                .amount(p.getAmount())
                                .paymentMethod(p.getPaymentMethod())
                                .transactionDate(p.getTransactionDate())
                                .reference(p.getReference())
                                .build())
                        .collect(Collectors.toList()))
                .build();
    }

    @Transactional(readOnly = true)
    public List<InvoiceResponse> getInvoicesByStudent(String studentIdStr) {
        log.info("Fetching invoices for student: {}", studentIdStr);
        StudentId studentId = new StudentId(studentIdStr);
        List<Invoice> invoices = invoiceRepository.findByStudentId(studentId);
        return invoices.stream()
                .map(invoice -> InvoiceResponse.builder()
                        .invoiceId(invoice.getId().getValue())
                        .studentId(invoice.getStudentId().getValue())
                        .issueDate(invoice.getIssueDate())
                        .dueDate(invoice.getDueDate())
                        .monthYear(invoice.getMonthYear())
                        .totalAmount(invoice.getTotalAmount())
                        .status(invoice.getStatus())
                        .paymentDate(invoice.getPaymentDate())
                        .items(invoice.getItems().stream()
                                .map(item -> InvoiceResponse.ItemDetail.builder()
                                        .description(item.getDescription())
                                        .quantity(item.getQuantity())
                                        .unitPrice(item.getUnitPrice())
                                        .subtotal(item.getSubtotal())
                                        .build())
                                .collect(Collectors.toList()))
                        .payments(invoice.getPayments().stream()
                                .map(p -> InvoiceResponse.PaymentDetail.builder()
                                        .paymentId(p.getId().getValue())
                                        .amount(p.getAmount())
                                        .paymentMethod(p.getPaymentMethod())
                                        .transactionDate(p.getTransactionDate())
                                        .reference(p.getReference())
                                        .build())
                                .collect(Collectors.toList()))
                        .build())
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public InvoiceResponse getInvoiceById(String invoiceIdStr) {
        log.info("Fetching invoice by id: {}", invoiceIdStr);
        InvoiceId invoiceId = new InvoiceId(invoiceIdStr);
        Invoice invoice = invoiceRepository.findById(invoiceId)
                .orElseThrow(() -> new IllegalArgumentException("Invoice not found with id: " + invoiceIdStr));

        return InvoiceResponse.builder()
                .invoiceId(invoice.getId().getValue())
                .studentId(invoice.getStudentId().getValue())
                .issueDate(invoice.getIssueDate())
                .dueDate(invoice.getDueDate())
                .monthYear(invoice.getMonthYear())
                .totalAmount(invoice.getTotalAmount())
                .status(invoice.getStatus())
                .paymentDate(invoice.getPaymentDate())
                .items(invoice.getItems().stream()
                        .map(item -> InvoiceResponse.ItemDetail.builder()
                                .description(item.getDescription())
                                .quantity(item.getQuantity())
                                .unitPrice(item.getUnitPrice())
                                .subtotal(item.getSubtotal())
                                .build())
                        .collect(Collectors.toList()))
                .payments(invoice.getPayments().stream()
                        .map(p -> InvoiceResponse.PaymentDetail.builder()
                                .paymentId(p.getId().getValue())
                                .amount(p.getAmount())
                                .paymentMethod(p.getPaymentMethod())
                                .transactionDate(p.getTransactionDate())
                                .reference(p.getReference())
                                .build())
                        .collect(Collectors.toList()))
                .build();
    }
}