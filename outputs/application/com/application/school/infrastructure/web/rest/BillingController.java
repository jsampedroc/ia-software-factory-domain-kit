package com.application.school.infrastructure.web.rest;

import com.application.school.application.billing.BillingService;
import com.application.school.application.dtos.InvoiceDTO;
import com.application.school.application.dtos.InvoiceItemDTO;
import com.application.school.application.dtos.PaymentDTO;
import com.application.school.domain.valueobjects.Money;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/billing")
@RequiredArgsConstructor
@Slf4j
public class BillingController {

    private final BillingService billingService;

    @PostMapping("/invoices")
    public ResponseEntity<InvoiceDTO> createInvoice(@RequestBody InvoiceDTO invoiceDTO) {
        log.info("Creating invoice for student: {}", invoiceDTO.getStudentId());
        InvoiceDTO created = billingService.createInvoice(invoiceDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    @GetMapping("/invoices/{invoiceId}")
    public ResponseEntity<InvoiceDTO> getInvoice(@PathVariable String invoiceId) {
        log.info("Fetching invoice with id: {}", invoiceId);
        return billingService.getInvoice(invoiceId)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/invoices/student/{studentId}")
    public ResponseEntity<List<InvoiceDTO>> getInvoicesByStudent(@PathVariable String studentId) {
        log.info("Fetching invoices for student: {}", studentId);
        List<InvoiceDTO> invoices = billingService.getInvoicesByStudent(studentId);
        return ResponseEntity.ok(invoices);
    }

    @GetMapping("/invoices/status/{status}")
    public ResponseEntity<List<InvoiceDTO>> getInvoicesByStatus(@PathVariable String status) {
        log.info("Fetching invoices with status: {}", status);
        List<InvoiceDTO> invoices = billingService.getInvoicesByStatus(status);
        return ResponseEntity.ok(invoices);
    }

    @PutMapping("/invoices/{invoiceId}")
    public ResponseEntity<InvoiceDTO> updateInvoice(@PathVariable String invoiceId, @RequestBody InvoiceDTO invoiceDTO) {
        log.info("Updating invoice with id: {}", invoiceId);
        invoiceDTO.setInvoiceId(invoiceId);
        InvoiceDTO updated = billingService.updateInvoice(invoiceDTO);
        return ResponseEntity.ok(updated);
    }

    @DeleteMapping("/invoices/{invoiceId}")
    public ResponseEntity<Void> deleteInvoice(@PathVariable String invoiceId) {
        log.info("Deleting invoice with id: {}", invoiceId);
        billingService.deleteInvoice(invoiceId);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/invoices/{invoiceId}/items")
    public ResponseEntity<InvoiceItemDTO> addInvoiceItem(@PathVariable String invoiceId, @RequestBody InvoiceItemDTO itemDTO) {
        log.info("Adding item to invoice: {}", invoiceId);
        itemDTO.setInvoiceId(invoiceId);
        InvoiceItemDTO createdItem = billingService.addInvoiceItem(itemDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdItem);
    }

    @DeleteMapping("/invoices/{invoiceId}/items/{itemId}")
    public ResponseEntity<Void> removeInvoiceItem(@PathVariable String invoiceId, @PathVariable String itemId) {
        log.info("Removing item {} from invoice: {}", itemId, invoiceId);
        billingService.removeInvoiceItem(invoiceId, itemId);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/payments")
    public ResponseEntity<PaymentDTO> registerPayment(@RequestBody PaymentDTO paymentDTO) {
        log.info("Registering payment for invoice(s)");
        PaymentDTO registered = billingService.registerPayment(paymentDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(registered);
    }

    @GetMapping("/payments/{paymentId}")
    public ResponseEntity<PaymentDTO> getPayment(@PathVariable String paymentId) {
        log.info("Fetching payment with id: {}", paymentId);
        return billingService.getPayment(paymentId)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/payments/invoice/{invoiceId}")
    public ResponseEntity<List<PaymentDTO>> getPaymentsByInvoice(@PathVariable String invoiceId) {
        log.info("Fetching payments for invoice: {}", invoiceId);
        List<PaymentDTO> payments = billingService.getPaymentsByInvoice(invoiceId);
        return ResponseEntity.ok(payments);
    }

    @PutMapping("/payments/{paymentId}")
    public ResponseEntity<PaymentDTO> updatePayment(@PathVariable String paymentId, @RequestBody PaymentDTO paymentDTO) {
        log.info("Updating payment with id: {}", paymentId);
        paymentDTO.setPaymentId(paymentId);
        PaymentDTO updated = billingService.updatePayment(paymentDTO);
        return ResponseEntity.ok(updated);
    }

    @DeleteMapping("/payments/{paymentId}")
    public ResponseEntity<Void> deletePayment(@PathVariable String paymentId) {
        log.info("Deleting payment with id: {}", paymentId);
        billingService.deletePayment(paymentId);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/payments/{paymentId}/apply/{invoiceId}")
    public ResponseEntity<Void> applyPaymentToInvoice(@PathVariable String paymentId, @PathVariable String invoiceId, @RequestParam(required = false) Money amount) {
        log.info("Applying payment {} to invoice {} with amount: {}", paymentId, invoiceId, amount);
        billingService.applyPaymentToInvoice(paymentId, invoiceId, amount);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/invoices/{invoiceId}/balance")
    public ResponseEntity<Money> getInvoiceBalance(@PathVariable String invoiceId) {
        log.info("Calculating balance for invoice: {}", invoiceId);
        Money balance = billingService.calculateInvoiceBalance(invoiceId);
        return ResponseEntity.ok(balance);
    }
}