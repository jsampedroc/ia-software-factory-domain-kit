package com.application.application.service;

import com.application.domain.model.*;
import com.application.domain.model.valueobject.*;
import com.application.domain.repository.*;
import com.application.application.dto.*;
import com.application.application.exception.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@Transactional
public class BillingService {

    private final BillingRepository billingRepository;
    private final PatientRepository patientRepository;
    private final TreatmentPlanRepository treatmentPlanRepository;
    private final InvoiceRepository invoiceRepository;

    public BillingService(BillingRepository billingRepository,
                          PatientRepository patientRepository,
                          TreatmentPlanRepository treatmentPlanRepository,
                          InvoiceRepository invoiceRepository) {
        this.billingRepository = billingRepository;
        this.patientRepository = patientRepository;
        this.treatmentPlanRepository = treatmentPlanRepository;
        this.invoiceRepository = invoiceRepository;
    }

    public BillingDTO createBilling(BillingRequestDTO request) {
        PatientId patientId = new PatientId(request.getPatientId());
        Patient patient = patientRepository.findById(patientId)
                .orElseThrow(() -> new ResourceNotFoundException("Patient not found"));

        TreatmentPlanId planId = new TreatmentPlanId(request.getTreatmentPlanId());
        TreatmentPlan plan = treatmentPlanRepository.findById(planId)
                .orElseThrow(() -> new ResourceNotFoundException("TreatmentPlan not found"));

        BillingId billingId = new BillingId(UUID.randomUUID());
        Money amount = new Money(request.getAmount(), request.getCurrency());
        BillingStatus status = BillingStatus.valueOf(request.getStatus().toUpperCase());

        Billing billing = new Billing(billingId, patientId, planId, amount, status, request.getDescription(), LocalDateTime.now());
        billingRepository.save(billing);

        return mapToDTO(billing);
    }

    public BillingDTO getBilling(UUID id) {
        BillingId billingId = new BillingId(id);
        Billing billing = billingRepository.findById(billingId)
                .orElseThrow(() -> new ResourceNotFoundException("Billing not found"));
        return mapToDTO(billing);
    }

    public List<BillingDTO> getBillingsByPatient(UUID patientId) {
        PatientId pid = new PatientId(patientId);
        List<Billing> billings = billingRepository.findByPatientId(pid);
        return billings.stream().map(this::mapToDTO).collect(Collectors.toList());
    }

    public BillingDTO updateBillingStatus(UUID id, String status) {
        BillingId billingId = new BillingId(id);
        Billing billing = billingRepository.findById(billingId)
                .orElseThrow(() -> new ResourceNotFoundException("Billing not found"));

        BillingStatus newStatus = BillingStatus.valueOf(status.toUpperCase());
        // Simplified: Direct status update. In a real scenario, use a domain method.
        // billing.updateStatus(newStatus);
        // For compilation, we create a new object with the updated status.
        Billing updatedBilling = new Billing(
                billing.getId(),
                billing.getPatientId(),
                billing.getTreatmentPlanId(),
                billing.getAmount(),
                newStatus,
                billing.getDescription(),
                billing.getBillingDate()
        );
        billingRepository.save(updatedBilling);

        return mapToDTO(updatedBilling);
    }

    public InvoiceDTO generateInvoice(UUID billingId) {
        BillingId bid = new BillingId(billingId);
        Billing billing = billingRepository.findById(bid)
                .orElseThrow(() -> new ResourceNotFoundException("Billing not found"));

        if (billing.getStatus() != BillingStatus.PAID) {
            throw new BusinessLogicException("Invoice can only be generated for PAID billings.");
        }

        InvoiceId invoiceId = new InvoiceId(UUID.randomUUID());
        Invoice invoice = new Invoice(invoiceId, bid, billing.getAmount(), LocalDateTime.now(), "INV-" + invoiceId.getId().toString().substring(0, 8));
        invoiceRepository.save(invoice);

        return new InvoiceDTO(
                invoice.getId().getId(),
                invoice.getBillingId().getId(),
                invoice.getAmount().getAmount(),
                invoice.getAmount().getCurrency(),
                invoice.getInvoiceDate(),
                invoice.getInvoiceNumber()
        );
    }

    private BillingDTO mapToDTO(Billing billing) {
        return new BillingDTO(
                billing.getId().getId(),
                billing.getPatientId().getId(),
                billing.getTreatmentPlanId().getId(),
                billing.getAmount().getAmount(),
                billing.getAmount().getCurrency(),
                billing.getStatus().name(),
                billing.getDescription(),
                billing.getBillingDate()
        );
    }
}