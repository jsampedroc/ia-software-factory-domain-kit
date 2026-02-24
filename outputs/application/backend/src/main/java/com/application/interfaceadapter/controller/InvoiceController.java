package com.application.interfaceadapter.controller;

import com.application.application.dto.InvoiceDTO;
import com.application.application.service.InvoiceService;
import com.application.domain.valueobject.InvoiceId;
import com.application.domain.valueobject.InvoiceStatus;
import com.application.domain.valueobject.PaymentMethod;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/invoices")
public class InvoiceController {

    private final InvoiceService invoiceService;

    public InvoiceController(InvoiceService invoiceService) {
        this.invoiceService = invoiceService;
    }

    @GetMapping("/{id}")
    public ResponseEntity<InvoiceDTO> getById(@PathVariable UUID id) {
        InvoiceId invoiceId = new InvoiceId(id);
        return invoiceService.findById(invoiceId)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping
    public ResponseEntity<List<InvoiceDTO>> getAll() {
        List<InvoiceDTO> invoices = invoiceService.findAll();
        return ResponseEntity.ok(invoices);
    }

    @GetMapping("/patient/{patientId}")
    public ResponseEntity<List<InvoiceDTO>> getByPatient(@PathVariable UUID patientId) {
        List<InvoiceDTO> invoices = invoiceService.findByPatientId(patientId);
        return ResponseEntity.ok(invoices);
    }

    @GetMapping("/status/{status}")
    public ResponseEntity<List<InvoiceDTO>> getByStatus(@PathVariable InvoiceStatus status) {
        List<InvoiceDTO> invoices = invoiceService.findByStatus(status);
        return ResponseEntity.ok(invoices);
    }

    @GetMapping("/date-range")
    public ResponseEntity<List<InvoiceDTO>> getByDateRange(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate) {
        List<InvoiceDTO> invoices = invoiceService.findByEmissionDateBetween(startDate, endDate);
        return ResponseEntity.ok(invoices);
    }

    @GetMapping("/overdue")
    public ResponseEntity<List<InvoiceDTO>> getOverdueInvoices() {
        List<InvoiceDTO> invoices = invoiceService.findOverdueInvoices();
        return ResponseEntity.ok(invoices);
    }

    @PostMapping
    public ResponseEntity<InvoiceDTO> create(@RequestBody InvoiceDTO invoiceDTO) {
        InvoiceDTO createdInvoice = invoiceService.create(invoiceDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdInvoice);
    }

    @PutMapping("/{id}")
    public ResponseEntity<InvoiceDTO> update(@PathVariable UUID id, @RequestBody InvoiceDTO invoiceDTO) {
        InvoiceId invoiceId = new InvoiceId(id);
        InvoiceDTO updatedInvoice = invoiceService.update(invoiceId, invoiceDTO);
        return ResponseEntity.ok(updatedInvoice);
    }

    @PatchMapping("/{id}/status")
    public ResponseEntity<InvoiceDTO> updateStatus(
            @PathVariable UUID id,
            @RequestParam InvoiceStatus status) {
        InvoiceId invoiceId = new InvoiceId(id);
        InvoiceDTO updatedInvoice = invoiceService.updateStatus(invoiceId, status);
        return ResponseEntity.ok(updatedInvoice);
    }

    @PatchMapping("/{id}/payment")
    public ResponseEntity<InvoiceDTO> registerPayment(
            @PathVariable UUID id,
            @RequestParam PaymentMethod paymentMethod,
            @RequestParam BigDecimal amount) {
        InvoiceId invoiceId = new InvoiceId(id);
        InvoiceDTO updatedInvoice = invoiceService.registerPayment(invoiceId, paymentMethod, amount);
        return ResponseEntity.ok(updatedInvoice);
    }

    @PatchMapping("/{id}/apply-discount")
    public ResponseEntity<InvoiceDTO> applyDiscount(
            @PathVariable UUID id,
            @RequestParam BigDecimal discountPercentage,
            @RequestParam String authorizationCode) {
        InvoiceId invoiceId = new InvoiceId(id);
        InvoiceDTO updatedInvoice = invoiceService.applyDiscount(invoiceId, discountPercentage, authorizationCode);
        return ResponseEntity.ok(updatedInvoice);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable UUID id) {
        InvoiceId invoiceId = new InvoiceId(id);
        invoiceService.delete(invoiceId);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{id}/generate-pdf")
    public ResponseEntity<byte[]> generatePdf(@PathVariable UUID id) {
        InvoiceId invoiceId = new InvoiceId(id);
        byte[] pdfContent = invoiceService.generatePdf(invoiceId);
        return ResponseEntity.ok()
                .header("Content-Type", "application/pdf")
                .header("Content-Disposition", "attachment; filename=\"invoice-" + id + ".pdf\"")
                .body(pdfContent);
    }

    @PostMapping("/{id}/send-email")
    public ResponseEntity<Void> sendByEmail(@PathVariable UUID id, @RequestParam String email) {
        InvoiceId invoiceId = new InvoiceId(id);
        invoiceService.sendByEmail(invoiceId, email);
        return ResponseEntity.accepted().build();
    }

    @GetMapping("/total-amount/{patientId}")
    public ResponseEntity<BigDecimal> getTotalAmountByPatient(@PathVariable UUID patientId) {
        BigDecimal totalAmount = invoiceService.getTotalAmountByPatient(patientId);
        return ResponseEntity.ok(totalAmount);
    }

    @GetMapping("/pending-amount/{patientId}")
    public ResponseEntity<BigDecimal> getPendingAmountByPatient(@PathVariable UUID patientId) {
        BigDecimal pendingAmount = invoiceService.getPendingAmountByPatient(patientId);
        return ResponseEntity.ok(pendingAmount);
    }
}