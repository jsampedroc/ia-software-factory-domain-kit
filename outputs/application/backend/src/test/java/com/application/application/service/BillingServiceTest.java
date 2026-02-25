package com.application.application.service;

import com.application.domain.model.*;
import com.application.domain.valueobject.*;
import com.application.domain.enums.*;
import com.application.domain.repository.*;
import com.application.domain.exception.DomainException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class BillingServiceTest {

    @Mock
    private InvoiceRepository invoiceRepository;
    @Mock
    private PaymentPlanRepository paymentPlanRepository;
    @Mock
    private InsuranceClaimRepository insuranceClaimRepository;
    @Mock
    private PatientRepository patientRepository;
    @Mock
    private AppointmentRepository appointmentRepository;

    @Captor
    private ArgumentCaptor<Invoice> invoiceCaptor;
    @Captor
    private ArgumentCaptor<PaymentPlan> paymentPlanCaptor;
    @Captor
    private ArgumentCaptor<InsuranceClaim> insuranceClaimCaptor;

    private BillingService billingService;

    private UUID patientUuid;
    private UUID appointmentUuid;
    private UUID invoiceUuid;
    private UUID claimUuid;
    private PatientId patientId;
    private AppointmentId appointmentId;
    private InvoiceId invoiceId;
    private InsuranceClaimId claimId;

    private Patient activePatient;
    private Appointment completedAppointment;
    private InvoiceItem sampleItem;
    private List<InvoiceItem> sampleItems;
    private BigDecimal taxRate;

    @BeforeEach
    void setUp() {
        billingService = new BillingService(invoiceRepository, paymentPlanRepository, insuranceClaimRepository, patientRepository, appointmentRepository);

        patientUuid = UUID.randomUUID();
        appointmentUuid = UUID.randomUUID();
        invoiceUuid = UUID.randomUUID();
        claimUuid = UUID.randomUUID();

        patientId = new PatientId(patientUuid);
        appointmentId = new AppointmentId(appointmentUuid);
        invoiceId = new InvoiceId(invoiceUuid);
        claimId = new InsuranceClaimId(claimUuid);

        activePatient = Patient.builder()
                .patientId(patientId)
                .identity(PatientIdentity.builder()
                        .firstName("John")
                        .lastName("Doe")
                        .dateOfBirth(LocalDate.of(1980, 1, 1))
                        .build())
                .status(PatientStatus.ACTIVE)
                .build();

        completedAppointment = Appointment.builder()
                .appointmentId(appointmentId)
                .patientId(patientUuid)
                .status(AppointmentStatus.COMPLETED)
                .build();

        sampleItem = InvoiceItem.builder()
                .itemId(new InvoiceItemId(UUID.randomUUID()))
                .treatmentCode("FILL01")
                .description("Tooth Filling")
                .quantity(1)
                .unitPrice(new BigDecimal("150.00"))
                .amount(new BigDecimal("150.00"))
                .build();

        sampleItems = List.of(sampleItem);
        taxRate = new BigDecimal("0.10"); // 10%
    }

    @Test
    void generateInvoiceForAppointment_Success() {
        when(patientRepository.findById(patientId)).thenReturn(Optional.of(activePatient));
        when(appointmentRepository.findById(appointmentId)).thenReturn(Optional.of(completedAppointment));
        when(invoiceRepository.findByAppointmentId(appointmentUuid)).thenReturn(Optional.empty());
        when(invoiceRepository.save(any(Invoice.class))).thenAnswer(invocation -> invocation.getArgument(0));

        Invoice result = billingService.generateInvoiceForAppointment(patientUuid, appointmentUuid, sampleItems, taxRate);

        verify(invoiceRepository).save(invoiceCaptor.capture());
        Invoice savedInvoice = invoiceCaptor.getValue();

        assertThat(result).isSameAs(savedInvoice);
        assertThat(savedInvoice.getPatientId()).isEqualTo(patientUuid);
        assertThat(savedInvoice.getAppointmentId()).isEqualTo(appointmentUuid);
        assertThat(savedInvoice.getStatus()).isEqualTo(InvoiceStatus.ISSUED);
        assertThat(savedInvoice.getSubtotal()).isEqualByComparingTo("150.00");
        assertThat(savedInvoice.getTax()).isEqualByComparingTo("15.00");
        assertThat(savedInvoice.getTotal()).isEqualByComparingTo("165.00");
        assertThat(savedInvoice.getItems()).hasSize(1);
        assertThat(savedInvoice.getInvoiceNumber()).startsWith("INV-");
    }

    @Test
    void generateInvoiceForAppointment_PatientNotFound() {
        when(patientRepository.findById(patientId)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> billingService.generateInvoiceForAppointment(patientUuid, appointmentUuid, sampleItems, taxRate))
                .isInstanceOf(DomainException.class)
                .hasMessageContaining("Patient not found");
    }

    @Test
    void generateInvoiceForAppointment_PatientArchived() {
        Patient archivedPatient = Patient.builder()
                .patientId(patientId)
                .identity(PatientIdentity.builder().firstName("Archived").lastName("Patient").build())
                .status(PatientStatus.ARCHIVED)
                .build();
        when(patientRepository.findById(patientId)).thenReturn(Optional.of(archivedPatient));

        assertThatThrownBy(() -> billingService.generateInvoiceForAppointment(patientUuid, appointmentUuid, sampleItems, taxRate))
                .isInstanceOf(DomainException.class)
                .hasMessageContaining("archived patient");
    }

    @Test
    void generateInvoiceForAppointment_AppointmentNotCompleted() {
        Appointment pendingAppointment = Appointment.builder()
                .appointmentId(appointmentId)
                .patientId(patientUuid)
                .status(AppointmentStatus.SCHEDULED)
                .build();
        when(patientRepository.findById(patientId)).thenReturn(Optional.of(activePatient));
        when(appointmentRepository.findById(appointmentId)).thenReturn(Optional.of(pendingAppointment));

        assertThatThrownBy(() -> billingService.generateInvoiceForAppointment(patientUuid, appointmentUuid, sampleItems, taxRate))
                .isInstanceOf(DomainException.class)
                .hasMessageContaining("completed appointments");
    }

    @Test
    void generateInvoiceForAppointment_InvoiceAlreadyExists() {
        Invoice existingInvoice = Invoice.builder()
                .invoiceId(new InvoiceId(UUID.randomUUID()))
                .patientId(patientUuid)
                .appointmentId(appointmentUuid)
                .status(InvoiceStatus.ISSUED)
                .build();
        when(patientRepository.findById(patientId)).thenReturn(Optional.of(activePatient));
        when(appointmentRepository.findById(appointmentId)).thenReturn(Optional.of(completedAppointment));
        when(invoiceRepository.findByAppointmentId(appointmentUuid)).thenReturn(Optional.of(existingInvoice));

        assertThatThrownBy(() -> billingService.generateInvoiceForAppointment(patientUuid, appointmentUuid, sampleItems, taxRate))
                .isInstanceOf(DomainException.class)
                .hasMessageContaining("already exists");
    }

    @Test
    void generateManualInvoice_Success() {
        when(patientRepository.findById(patientId)).thenReturn(Optional.of(activePatient));
        when(invoiceRepository.save(any(Invoice.class))).thenAnswer(invocation -> invocation.getArgument(0));

        Invoice result = billingService.generateManualInvoice(patientUuid, sampleItems, taxRate, "Additional supplies");

        verify(invoiceRepository).save(invoiceCaptor.capture());
        Invoice savedInvoice = invoiceCaptor.getValue();

        assertThat(result).isSameAs(savedInvoice);
        assertThat(savedInvoice.getPatientId()).isEqualTo(patientUuid);
        assertThat(savedInvoice.getAppointmentId()).isNull();
        assertThat(savedInvoice.getStatus()).isEqualTo(InvoiceStatus.ISSUED);
    }

    @Test
    void recordPayment_SuccessPartial() {
        Invoice issuedInvoice = Invoice.builder()
                .invoiceId(invoiceId)
                .patientId(patientUuid)
                .total(new BigDecimal("200.00"))
                .status(InvoiceStatus.ISSUED)
                .build();
        when(invoiceRepository.findById(invoiceId)).thenReturn(Optional.of(issuedInvoice));
        when(invoiceRepository.save(any(Invoice.class))).thenAnswer(invocation -> invocation.getArgument(0));

        Payment result = billingService.recordPayment(invoiceUuid, new BigDecimal("100.00"), PaymentMethod.CARD, "TXN12345");

        verify(invoiceRepository).save(invoiceCaptor.capture());
        Invoice savedInvoice = invoiceCaptor.getValue();

        assertThat(result.getInvoiceId()).isEqualTo(invoiceUuid);
        assertThat(result.getAmount()).isEqualByComparingTo("100.00");
        assertThat(result.getMethod()).isEqualTo(PaymentMethod.CARD);
        assertThat(savedInvoice.getStatus()).isEqualTo(InvoiceStatus.PARTIAL_PAID);
    }

    @Test
    void recordPayment_SuccessFull() {
        Invoice issuedInvoice = Invoice.builder()
                .invoiceId(invoiceId)
                .patientId(patientUuid)
                .total(new BigDecimal("200.00"))
                .status(InvoiceStatus.ISSUED)
                .build();
        when(invoiceRepository.findById(invoiceId)).thenReturn(Optional.of(issuedInvoice));
        when(invoiceRepository.save(any(Invoice.class))).thenAnswer(invocation -> invocation.getArgument(0));

        Payment result = billingService.recordPayment(invoiceUuid, new BigDecimal("200.00"), PaymentMethod.CASH, "CASH001");

        verify(invoiceRepository).save(invoiceCaptor.capture());
        Invoice savedInvoice = invoiceCaptor.getValue();

        assertThat(result.getAmount()).isEqualByComparingTo("200.00");
        assertThat(savedInvoice.getStatus()).isEqualTo(InvoiceStatus.PAID);
    }

    @Test
    void recordPayment_ExceedsRemainingBalance() {
        Invoice issuedInvoice = Invoice.builder()
                .invoiceId(invoiceId)
                .patientId(patientUuid)
                .total(new BigDecimal("200.00"))
                .status(InvoiceStatus.ISSUED)
                .build();
        when(invoiceRepository.findById(invoiceId)).thenReturn(Optional.of(issuedInvoice));

        assertThatThrownBy(() -> billingService.recordPayment(invoiceUuid, new BigDecimal("250.00"), PaymentMethod.CARD, "TXN999"))
                .isInstanceOf(DomainException.class)
                .hasMessageContaining("exceeds invoice remaining balance");
    }

    @Test
    void createPaymentPlan_Success() {
        Invoice issuedInvoice = Invoice.builder()
                .invoiceId(invoiceId)
                .patientId(patientUuid)
                .total(new BigDecimal("1000.00"))
                .status(InvoiceStatus.ISSUED)
                .build();
        when(invoiceRepository.findById(invoiceId)).thenReturn(Optional.of(issuedInvoice));
        when(paymentPlanRepository.findByInvoiceId(invoiceUuid)).thenReturn(Optional.empty());
        when(paymentPlanRepository.save(any(PaymentPlan.class))).thenAnswer(invocation -> invocation.getArgument(0));
        when(invoiceRepository.save(any(Invoice.class))).thenAnswer(invocation -> invocation.getArgument(0));

        PaymentPlan result = billingService.createPaymentPlan(invoiceUuid, 4, LocalDate.now().plusDays(7));

        verify(paymentPlanRepository).save(paymentPlanCaptor.capture());
        PaymentPlan savedPlan = paymentPlanCaptor.getValue();

        assertThat(result).isSameAs(savedPlan);
        assertThat(savedPlan.getInvoiceId()).isEqualTo(invoiceUuid);
        assertThat(savedPlan.getTotalAmount()).isEqualByComparingTo("1000.00");
        assertThat(savedPlan.getInstallmentCount()).isEqualTo(4);
        assertThat(savedPlan.getStatus()).isEqualTo(PaymentPlanStatus.ACTIVE);
        verify(invoiceRepository).save(invoiceCaptor.capture());
        assertThat(invoiceCaptor.getValue().getPaymentPlanId()).isEqualTo(savedPlan.getPlanId().getValue());
    }

    @Test
    void createPaymentPlan_InvoiceNotIssued() {
        Invoice draftInvoice = Invoice.builder()
                .invoiceId(invoiceId)
                .patientId(patientUuid)
                .total(new BigDecimal("1000.00"))
                .status(InvoiceStatus.DRAFT)
                .build();
        when(invoiceRepository.findById(invoiceId)).thenReturn(Optional.of(draftInvoice));

        assertThatThrownBy(() -> billingService.createPaymentPlan(invoiceUuid, 4, LocalDate.now()))
                .isInstanceOf(DomainException.class)
                .hasMessageContaining("issued invoices");
    }

    @Test
    void createPaymentPlan_TotalTooLow() {
        Invoice issuedInvoice = Invoice.builder()
                .invoiceId(invoiceId)
                .patientId(patientUuid)
                .total(new BigDecimal("400.00"))
                .status(InvoiceStatus.ISSUED)
                .build();
        when(invoiceRepository.findById(invoiceId)).thenReturn(Optional.of(issuedInvoice));

        assertThatThrownBy(() -> billingService.createPaymentPlan(invoiceUuid, 4, LocalDate.now()))
                .isInstanceOf(DomainException.class)
                .hasMessageContaining("greater than $500");
    }

    @Test
    void createPaymentPlan_PlanAlreadyExists() {
        Invoice issuedInvoice = Invoice.builder()
                .invoiceId(invoiceId)
                .patientId(patientUuid)
                .total(new BigDecimal("1000.00"))
                .status(InvoiceStatus.ISSUED)
                .build();
        PaymentPlan existingPlan = PaymentPlan.builder()
                .planId(new PaymentPlanId(UUID.randomUUID()))
                .invoiceId(invoiceUuid)
                .status(PaymentPlanStatus.ACTIVE)
                .build();
        when(invoiceRepository.findById(invoiceId)).thenReturn(Optional.of(issuedInvoice));
        when(paymentPlanRepository.findByInvoiceId(invoiceUuid)).thenReturn(Optional.of(existingPlan));

        assertThatThrownBy(() -> billingService.createPaymentPlan(invoiceUuid, 4, LocalDate.now()))
                .isInstanceOf(DomainException.class)
                .hasMessageContaining("already exists");
    }

    @Test
    void submitInsuranceClaim_Success() {
        Invoice issuedInvoice = Invoice.builder()
                .invoiceId(invoiceId)
                .patientId(patientUuid)
                .total(new BigDecimal("500.00"))
                .status(InvoiceStatus.ISSUED)
                .build();
        when(patientRepository.findById(patientId)).thenReturn(Optional.of(activePatient));
        when(invoiceRepository.findById(invoiceId)).thenReturn(Optional.of(issuedInvoice));
        when(insuranceClaimRepository.save(any(InsuranceClaim.class))).thenAnswer(invocation -> invocation.getArgument(0));

        InsuranceClaim result = billingService.submitInsuranceClaim(patientUuid, invoiceUuid, "HealthPlus", "POL123", new BigDecimal("400.00"));

        verify(insuranceClaimRepository).save(insuranceClaimCaptor.capture());
        InsuranceClaim savedClaim = insuranceClaimCaptor.getValue();

        assertThat(result).isSameAs(savedClaim);
        assertThat(savedClaim.getPatientId()).isEqualTo(patientUuid);
        assertThat(savedClaim.getInvoiceId()).isEqualTo(invoiceUuid);
        assertThat(savedClaim.getInsuranceProvider()).isEqualTo("HealthPlus");
        assertThat(savedClaim.getStatus()).isEqualTo(ClaimStatus.SUBMITTED);
    }

    @Test
    void submitInsuranceClaim_PatientInvoiceMismatch() {
        UUID otherPatientUuid = UUID.randomUUID();
        Invoice issuedInvoice = Invoice.builder()
                .invoiceId(invoiceId)
                .patientId(otherPatientUuid) // Different patient
                .total(new BigDecimal("500.00"))
                .status(InvoiceStatus.ISSUED)
                .build();
        when(patientRepository.findById(patientId)).thenReturn(Optional.of(activePatient));
        when(invoiceRepository.findById(invoiceId)).thenReturn(Optional.of(issuedInvoice));

        assertThatThrownBy(() -> billingService.submitInsuranceClaim(patientUuid, invoiceUuid, "HealthPlus", "POL123", new BigDecimal("400.00")))
                .isInstanceOf(DomainException.class)
                .hasMessageContaining("does not belong");
    }

    @Test
    void updateClaimStatus_ToApproved_TriggersPayment() {
        InsuranceClaim submittedClaim = InsuranceClaim.builder()
                .claimId(claimId)
                .patientId(patientUuid)
                .invoiceId(invoiceUuid)
                .claimedAmount(new BigDecimal("300.00"))
                .status(ClaimStatus.SUBMITTED)
                .build();
        Invoice issuedInvoice = Invoice.builder()
                .invoiceId(invoiceId)
                .patientId(patientUuid)
                .total(new BigDecimal("500.00"))
                .status(InvoiceStatus.ISSUED)
                .build();

        when(insuranceClaimRepository.findById(claimId)).thenReturn(Optional.of(submittedClaim));
        when(invoiceRepository.findById(new InvoiceId(invoiceUuid))).thenReturn(Optional.of(issuedInvoice));
        when(insuranceClaimRepository.save(any(InsuranceClaim.class))).thenAnswer(invocation -> invocation.getArgument(0));
        when(invoiceRepository.save(any(Invoice.class))).thenAnswer(invocation -> invocation.getArgument(0));

        billingService.updateClaimStatus(claimUuid, ClaimStatus.APPROVED, LocalDate.now());

        verify(insuranceClaimRepository).save(insuranceClaimCaptor.capture());
        assertThat(insuranceClaimCaptor.getValue().getStatus()).isEqualTo(ClaimStatus.APPROVED);

        verify(invoiceRepository).save(invoiceCaptor.capture());
        Invoice updatedInvoice = invoiceCaptor.getValue();
        assertThat(updatedInvoice.getPayments()).isNotEmpty();
        Payment insurancePayment = updatedInvoice.getPayments().get(0);
        assertThat(insurancePayment.getMethod()).isEqualTo(PaymentMethod.INSURANCE);
        assertThat(insurancePayment.getAmount()).isEqualByComparingTo("300.