package com.application.school.infrastructure.api.billing;

import com.application.school.application.billing.BillingService;
import com.application.school.application.billing.dto.GenerateInvoiceCommand;
import com.application.school.application.billing.dto.InvoiceResponse;
import com.application.school.application.billing.dto.RegisterPaymentCommand;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/billing")
@RequiredArgsConstructor
public class BillingController {

    private final BillingService billingService;

    @PostMapping("/invoices")
    public ResponseEntity<InvoiceResponse> generateInvoice(@RequestBody GenerateInvoiceCommand command) {
        InvoiceResponse invoice = billingService.generateInvoice(command);
        return ResponseEntity.status(HttpStatus.CREATED).body(invoice);
    }

    @GetMapping("/invoices/{invoiceId}")
    public ResponseEntity<InvoiceResponse> getInvoiceById(@PathVariable String invoiceId) {
        InvoiceResponse invoice = billingService.getInvoiceById(invoiceId);
        return ResponseEntity.ok(invoice);
    }

    @GetMapping("/students/{studentId}/invoices")
    public ResponseEntity<List<InvoiceResponse>> getInvoicesByStudent(@PathVariable String studentId) {
        List<InvoiceResponse> invoices = billingService.getInvoicesByStudent(studentId);
        return ResponseEntity.ok(invoices);
    }

    @PostMapping("/invoices/{invoiceId}/payments")
    public ResponseEntity<InvoiceResponse> registerPayment(
            @PathVariable String invoiceId,
            @RequestBody RegisterPaymentCommand command) {
        command.setInvoiceId(invoiceId);
        InvoiceResponse updatedInvoice = billingService.registerPayment(command);
        return ResponseEntity.ok(updatedInvoice);
    }

    @PatchMapping("/invoices/{invoiceId}/status")
    public ResponseEntity<InvoiceResponse> updateInvoiceStatus(
            @PathVariable String invoiceId,
            @RequestParam String status) {
        InvoiceResponse updatedInvoice = billingService.updateInvoiceStatus(invoiceId, status);
        return ResponseEntity.ok(updatedInvoice);
    }
}