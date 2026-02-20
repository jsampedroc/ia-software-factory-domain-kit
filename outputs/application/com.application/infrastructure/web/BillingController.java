package com.application.infrastructure.web;

import com.application.application.dto.InvoiceDTO;
import com.application.application.dto.PaymentDTO;
import com.application.domain.model.billing.InvoiceId;
import com.application.domain.model.billing.InvoiceNumber;
import com.application.domain.model.studentmanagement.StudentId;
import com.application.domain.ports.in.BillingService;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.YearMonth;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/billing")
@RequiredArgsConstructor
public class BillingController {

    private final BillingService billingService;

    @PostMapping("/invoices/generate/{studentId}")
    public ResponseEntity<InvoiceDTO> generateInvoice(
            @PathVariable UUID studentId,
            @RequestParam @DateTimeFormat(pattern = "yyyy-MM") YearMonth billingMonth) {
        InvoiceDTO invoice = billingService.generateMonthlyInvoice(new StudentId(studentId), billingMonth);
        return ResponseEntity.status(HttpStatus.CREATED).body(invoice);
    }

    @GetMapping("/invoices/student/{studentId}")
    public ResponseEntity<List<InvoiceDTO>> getInvoicesByStudent(@PathVariable UUID studentId) {
        List<InvoiceDTO> invoices = billingService.getInvoicesByStudent(new StudentId(studentId));
        return ResponseEntity.ok(invoices);
    }

    @GetMapping("/invoices/{invoiceId}")
    public ResponseEntity<InvoiceDTO> getInvoiceById(@PathVariable UUID invoiceId) {
        return billingService.getInvoiceById(new InvoiceId(invoiceId))
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/invoices/number/{invoiceNumber}")
    public ResponseEntity<InvoiceDTO> getInvoiceByNumber(@PathVariable String invoiceNumber) {
        return billingService.getInvoiceByNumber(new InvoiceNumber(invoiceNumber))
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PatchMapping("/invoices/{invoiceId}/issue")
    public ResponseEntity<InvoiceDTO> issueInvoice(@PathVariable UUID invoiceId) {
        return billingService.issueInvoice(new InvoiceId(invoiceId))
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PatchMapping("/invoices/{invoiceId}/cancel")
    public ResponseEntity<InvoiceDTO> cancelInvoice(@PathVariable UUID invoiceId) {
        return billingService.cancelInvoice(new InvoiceId(invoiceId))
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping("/invoices/{invoiceId}/payments")
    public ResponseEntity<PaymentDTO> registerPayment(
            @PathVariable UUID invoiceId,
            @RequestBody PaymentDTO paymentDTO) {
        PaymentDTO registeredPayment = billingService.registerPayment(new InvoiceId(invoiceId), paymentDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(registeredPayment);
    }

    @GetMapping("/invoices/{invoiceId}/payments")
    public ResponseEntity<List<PaymentDTO>> getPaymentsForInvoice(@PathVariable UUID invoiceId) {
        List<PaymentDTO> payments = billingService.getPaymentsForInvoice(new InvoiceId(invoiceId));
        return ResponseEntity.ok(payments);
    }
}