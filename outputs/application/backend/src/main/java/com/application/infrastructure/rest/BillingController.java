package com.application.infrastructure.rest;

import com.application.application.service.BillingService;
import com.application.domain.model.Invoice;
import com.application.domain.model.Payment;
import com.application.domain.model.PaymentPlan;
import com.application.domain.model.InsuranceClaim;
import com.application.domain.valueobject.InvoiceId;
import com.application.domain.valueobject.PatientId;
import com.application.domain.enums.InvoiceStatus;
import com.application.domain.enums.ClaimStatus;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/billing")
public class BillingController {

    private final BillingService billingService;

    public BillingController(BillingService billingService) {
        this.billingService = billingService;
    }

    // ========== Invoice Endpoints ==========

    @GetMapping("/invoices")
    public ResponseEntity<List<Invoice>> getAllInvoices(
            @RequestParam(required = false) InvoiceStatus status,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fromDate,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate toDate) {
        List<Invoice> invoices = billingService.findInvoices(status, fromDate, toDate);
        return ResponseEntity.ok(invoices);
    }

    @GetMapping("/invoices/{invoiceId}")
    public ResponseEntity<Invoice> getInvoiceById(@PathVariable UUID invoiceId) {
        Invoice invoice = billingService.findInvoiceById(new InvoiceId(invoiceId));
        return ResponseEntity.ok(invoice);
    }

    @GetMapping("/patients/{patientId}/invoices")
    public ResponseEntity<List<Invoice>> getInvoicesByPatient(@PathVariable UUID patientId) {
        List<Invoice> invoices = billingService.findInvoicesByPatientId(new PatientId(patientId));
        return ResponseEntity.ok(invoices);
    }

    @PostMapping("/invoices")
    public ResponseEntity<Invoice> createInvoice(@RequestBody CreateInvoiceRequest request) {
        Invoice invoice = billingService.createInvoice(
                new PatientId(request.patientId()),
                request.appointmentId(),
                request.items(),
                request.taxRate()
        );
        return ResponseEntity.status(HttpStatus.CREATED).body(invoice);
    }

    @PutMapping("/invoices/{invoiceId}/issue")
    public ResponseEntity<Invoice> issueInvoice(@PathVariable UUID invoiceId) {
        Invoice invoice = billingService.issueInvoice(new InvoiceId(invoiceId));
        return ResponseEntity.ok(invoice);
    }

    @PutMapping("/invoices/{invoiceId}/cancel")
    public ResponseEntity<Invoice> cancelInvoice(@PathVariable UUID invoiceId, @RequestBody CancelInvoiceRequest request) {
        Invoice invoice = billingService.cancelInvoice(new InvoiceId(invoiceId), request.reason());
        return ResponseEntity.ok(invoice);
    }

    @PutMapping("/invoices/{invoiceId}/mark-overdue")
    public ResponseEntity<Invoice> markInvoiceOverdue(@PathVariable UUID invoiceId) {
        Invoice invoice = billingService.markInvoiceOverdue(new InvoiceId(invoiceId));
        return ResponseEntity.ok(invoice);
    }

    // ========== Payment Endpoints ==========

    @GetMapping("/invoices/{invoiceId}/payments")
    public ResponseEntity<List<Payment>> getPaymentsForInvoice(@PathVariable UUID invoiceId) {
        List<Payment> payments = billingService.findPaymentsByInvoiceId(new InvoiceId(invoiceId));
        return ResponseEntity.ok(payments);
    }

    @PostMapping("/payments")
    public ResponseEntity<Payment> recordPayment(@RequestBody RecordPaymentRequest request) {
        Payment payment = billingService.recordPayment(
                new InvoiceId(request.invoiceId()),
                request.amount(),
                request.method(),
                request.referenceNumber()
        );
        return ResponseEntity.status(HttpStatus.CREATED).body(payment);
    }

    // ========== Payment Plan Endpoints ==========

    @PostMapping("/payment-plans")
    public ResponseEntity<PaymentPlan> createPaymentPlan(@RequestBody CreatePaymentPlanRequest request) {
        PaymentPlan plan = billingService.createPaymentPlan(
                new InvoiceId(request.invoiceId()),
                request.installmentCount(),
                request.startDate()
        );
        return ResponseEntity.status(HttpStatus.CREATED).body(plan);
    }

    @GetMapping("/payment-plans/{planId}")
    public ResponseEntity<PaymentPlan> getPaymentPlan(@PathVariable UUID planId) {
        PaymentPlan plan = billingService.findPaymentPlanById(planId);
        return ResponseEntity.ok(plan);
    }

    @PutMapping("/payment-plans/{planId}/mark-defaulted")
    public ResponseEntity<PaymentPlan> markPaymentPlanDefaulted(@PathVariable UUID planId) {
        PaymentPlan plan = billingService.markPaymentPlanDefaulted(planId);
        return ResponseEntity.ok(plan);
    }

    // ========== Insurance Claim Endpoints ==========

    @PostMapping("/insurance-claims")
    public ResponseEntity<InsuranceClaim> submitInsuranceClaim(@RequestBody SubmitClaimRequest request) {
        InsuranceClaim claim = billingService.submitInsuranceClaim(
                new InvoiceId(request.invoiceId()),
                request.insuranceProvider(),
                request.policyNumber(),
                request.claimedAmount()
        );
        return ResponseEntity.status(HttpStatus.CREATED).body(claim);
    }

    @GetMapping("/insurance-claims")
    public ResponseEntity<List<InsuranceClaim>> getInsuranceClaims(
            @RequestParam(required = false) ClaimStatus status,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fromDate) {
        List<InsuranceClaim> claims = billingService.findInsuranceClaims(status, fromDate);
        return ResponseEntity.ok(claims);
    }

    @PutMapping("/insurance-claims/{claimId}/update-status")
    public ResponseEntity<InsuranceClaim> updateClaimStatus(
            @PathVariable UUID claimId,
            @RequestBody UpdateClaimStatusRequest request) {
        InsuranceClaim claim = billingService.updateClaimStatus(claimId, request.status(), request.responseDate());
        return ResponseEntity.ok(claim);
    }

    // ========== Request/Response Records ==========

    public record CreateInvoiceRequest(
            UUID patientId,
            UUID appointmentId,
            List<InvoiceItemRequest> items,
            BigDecimal taxRate
    ) {}

    public record InvoiceItemRequest(
            String treatmentCode,
            String description,
            Integer quantity,
            BigDecimal unitPrice
    ) {}

    public record CancelInvoiceRequest(String reason) {}

    public record RecordPaymentRequest(
            UUID invoiceId,
            BigDecimal amount,
            String method,
            String referenceNumber
    ) {}

    public record CreatePaymentPlanRequest(
            UUID invoiceId,
            Integer installmentCount,
            LocalDate startDate
    ) {}

    public record SubmitClaimRequest(
            UUID invoiceId,
            String insuranceProvider,
            String policyNumber,
            BigDecimal claimedAmount
    ) {}

    public record UpdateClaimStatusRequest(
            ClaimStatus status,
            LocalDate responseDate
    ) {}
}