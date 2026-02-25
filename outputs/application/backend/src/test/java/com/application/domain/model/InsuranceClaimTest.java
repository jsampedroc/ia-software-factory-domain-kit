package com.application.domain.model;

import com.application.domain.valueobject.InsuranceClaimId;
import com.application.domain.valueobject.PatientId;
import com.application.domain.valueobject.InvoiceId;
import com.application.domain.enums.ClaimStatus;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@ExtendWith(MockitoExtension.class)
class InsuranceClaimTest {

    private final InsuranceClaimId claimId = new InsuranceClaimId(UUID.randomUUID());
    private final PatientId patientId = new PatientId(UUID.randomUUID());
    private final InvoiceId invoiceId = new InvoiceId(UUID.randomUUID());
    private final String insuranceProvider = "DentalCare Inc.";
    private final String policyNumber = "POL-123456";
    private final BigDecimal claimedAmount = new BigDecimal("1500.75");
    private final LocalDate submittedDate = LocalDate.now().minusDays(5);

    @Test
    void shouldCreateInsuranceClaimWithSubmittedStatus() {
        InsuranceClaim claim = InsuranceClaim.builder()
                .id(claimId)
                .patientId(patientId)
                .invoiceId(invoiceId)
                .insuranceProvider(insuranceProvider)
                .policyNumber(policyNumber)
                .claimedAmount(claimedAmount)
                .status(ClaimStatus.SUBMITTED)
                .submittedDate(submittedDate)
                .build();

        assertThat(claim).isNotNull();
        assertThat(claim.getId()).isEqualTo(claimId);
        assertThat(claim.getPatientId()).isEqualTo(patientId);
        assertThat(claim.getInvoiceId()).isEqualTo(invoiceId);
        assertThat(claim.getInsuranceProvider()).isEqualTo(insuranceProvider);
        assertThat(claim.getPolicyNumber()).isEqualTo(policyNumber);
        assertThat(claim.getClaimedAmount()).isEqualTo(claimedAmount);
        assertThat(claim.getStatus()).isEqualTo(ClaimStatus.SUBMITTED);
        assertThat(claim.getSubmittedDate()).isEqualTo(submittedDate);
        assertThat(claim.getResponseDate()).isNull();
        assertThat(claim.isPending()).isTrue();
        assertThat(claim.isApproved()).isFalse();
    }

    @Test
    void shouldApproveSubmittedClaim() {
        InsuranceClaim claim = InsuranceClaim.builder()
                .id(claimId)
                .patientId(patientId)
                .invoiceId(invoiceId)
                .insuranceProvider(insuranceProvider)
                .policyNumber(policyNumber)
                .claimedAmount(claimedAmount)
                .status(ClaimStatus.SUBMITTED)
                .submittedDate(submittedDate)
                .build();

        LocalDate responseDate = LocalDate.now();
        claim.approve(responseDate);

        assertThat(claim.getStatus()).isEqualTo(ClaimStatus.APPROVED);
        assertThat(claim.getResponseDate()).isEqualTo(responseDate);
        assertThat(claim.isPending()).isFalse();
        assertThat(claim.isApproved()).isTrue();
    }

    @Test
    void shouldThrowExceptionWhenApprovingNonSubmittedClaim() {
        InsuranceClaim claim = InsuranceClaim.builder()
                .id(claimId)
                .patientId(patientId)
                .invoiceId(invoiceId)
                .insuranceProvider(insuranceProvider)
                .policyNumber(policyNumber)
                .claimedAmount(claimedAmount)
                .status(ClaimStatus.APPROVED)
                .submittedDate(submittedDate)
                .responseDate(LocalDate.now().minusDays(1))
                .build();

        LocalDate responseDate = LocalDate.now();
        assertThatThrownBy(() -> claim.approve(responseDate))
                .isInstanceOf(IllegalStateException.class)
                .hasMessage("Only claims with status SUBMITTED can be approved.");
    }

    @Test
    void shouldRejectSubmittedClaim() {
        InsuranceClaim claim = InsuranceClaim.builder()
                .id(claimId)
                .patientId(patientId)
                .invoiceId(invoiceId)
                .insuranceProvider(insuranceProvider)
                .policyNumber(policyNumber)
                .claimedAmount(claimedAmount)
                .status(ClaimStatus.SUBMITTED)
                .submittedDate(submittedDate)
                .build();

        LocalDate responseDate = LocalDate.now();
        claim.reject(responseDate);

        assertThat(claim.getStatus()).isEqualTo(ClaimStatus.REJECTED);
        assertThat(claim.getResponseDate()).isEqualTo(responseDate);
        assertThat(claim.isPending()).isFalse();
        assertThat(claim.isApproved()).isFalse();
    }

    @Test
    void shouldThrowExceptionWhenRejectingNonSubmittedClaim() {
        InsuranceClaim claim = InsuranceClaim.builder()
                .id(claimId)
                .patientId(patientId)
                .invoiceId(invoiceId)
                .insuranceProvider(insuranceProvider)
                .policyNumber(policyNumber)
                .claimedAmount(claimedAmount)
                .status(ClaimStatus.REJECTED)
                .submittedDate(submittedDate)
                .responseDate(LocalDate.now().minusDays(1))
                .build();

        LocalDate responseDate = LocalDate.now();
        assertThatThrownBy(() -> claim.reject(responseDate))
                .isInstanceOf(IllegalStateException.class)
                .hasMessage("Only claims with status SUBMITTED can be rejected.");
    }

    @Test
    void shouldMarkApprovedClaimAsPaid() {
        InsuranceClaim claim = InsuranceClaim.builder()
                .id(claimId)
                .patientId(patientId)
                .invoiceId(invoiceId)
                .insuranceProvider(insuranceProvider)
                .policyNumber(policyNumber)
                .claimedAmount(claimedAmount)
                .status(ClaimStatus.APPROVED)
                .submittedDate(submittedDate)
                .responseDate(LocalDate.now().minusDays(2))
                .build();

        claim.markAsPaid();

        assertThat(claim.getStatus()).isEqualTo(ClaimStatus.PAID);
        assertThat(claim.isPending()).isFalse();
        assertThat(claim.isApproved()).isTrue();
    }

    @Test
    void shouldThrowExceptionWhenMarkingNonApprovedClaimAsPaid() {
        InsuranceClaim claim = InsuranceClaim.builder()
                .id(claimId)
                .patientId(patientId)
                .invoiceId(invoiceId)
                .insuranceProvider(insuranceProvider)
                .policyNumber(policyNumber)
                .claimedAmount(claimedAmount)
                .status(ClaimStatus.SUBMITTED)
                .submittedDate(submittedDate)
                .build();

        assertThatThrownBy(claim::markAsPaid)
                .isInstanceOf(IllegalStateException.class)
                .hasMessage("Only claims with status APPROVED can be marked as PAID.");
    }

    @Test
    void isPendingShouldReturnTrueOnlyForSubmittedStatus() {
        InsuranceClaim submittedClaim = InsuranceClaim.builder()
                .id(claimId)
                .patientId(patientId)
                .invoiceId(invoiceId)
                .insuranceProvider(insuranceProvider)
                .policyNumber(policyNumber)
                .claimedAmount(claimedAmount)
                .status(ClaimStatus.SUBMITTED)
                .submittedDate(submittedDate)
                .build();

        InsuranceClaim approvedClaim = InsuranceClaim.builder()
                .id(new InsuranceClaimId(UUID.randomUUID()))
                .patientId(patientId)
                .invoiceId(invoiceId)
                .insuranceProvider(insuranceProvider)
                .policyNumber(policyNumber)
                .claimedAmount(claimedAmount)
                .status(ClaimStatus.APPROVED)
                .submittedDate(submittedDate)
                .responseDate(LocalDate.now())
                .build();

        assertThat(submittedClaim.isPending()).isTrue();
        assertThat(approvedClaim.isPending()).isFalse();
    }

    @Test
    void isApprovedShouldReturnTrueForApprovedAndPaidStatus() {
        InsuranceClaim approvedClaim = InsuranceClaim.builder()
                .id(claimId)
                .patientId(patientId)
                .invoiceId(invoiceId)
                .insuranceProvider(insuranceProvider)
                .policyNumber(policyNumber)
                .claimedAmount(claimedAmount)
                .status(ClaimStatus.APPROVED)
                .submittedDate(submittedDate)
                .responseDate(LocalDate.now())
                .build();

        InsuranceClaim paidClaim = InsuranceClaim.builder()
                .id(new InsuranceClaimId(UUID.randomUUID()))
                .patientId(patientId)
                .invoiceId(invoiceId)
                .insuranceProvider(insuranceProvider)
                .policyNumber(policyNumber)
                .claimedAmount(claimedAmount)
                .status(ClaimStatus.PAID)
                .submittedDate(submittedDate)
                .responseDate(LocalDate.now())
                .build();

        InsuranceClaim rejectedClaim = InsuranceClaim.builder()
                .id(new InsuranceClaimId(UUID.randomUUID()))
                .patientId(patientId)
                .invoiceId(invoiceId)
                .insuranceProvider(insuranceProvider)
                .policyNumber(policyNumber)
                .claimedAmount(claimedAmount)
                .status(ClaimStatus.REJECTED)
                .submittedDate(submittedDate)
                .responseDate(LocalDate.now())
                .build();

        assertThat(approvedClaim.isApproved()).isTrue();
        assertThat(paidClaim.isApproved()).isTrue();
        assertThat(rejectedClaim.isApproved()).isFalse();
    }

    @Test
    void shouldMaintainImmutableFieldsAfterStateChanges() {
        InsuranceClaim claim = InsuranceClaim.builder()
                .id(claimId)
                .patientId(patientId)
                .invoiceId(invoiceId)
                .insuranceProvider(insuranceProvider)
                .policyNumber(policyNumber)
                .claimedAmount(claimedAmount)
                .status(ClaimStatus.SUBMITTED)
                .submittedDate(submittedDate)
                .build();

        LocalDate responseDate = LocalDate.now();
        claim.approve(responseDate);
        claim.markAsPaid();

        assertThat(claim.getId()).isEqualTo(claimId);
        assertThat(claim.getPatientId()).isEqualTo(patientId);
        assertThat(claim.getInvoiceId()).isEqualTo(invoiceId);
        assertThat(claim.getInsuranceProvider()).isEqualTo(insuranceProvider);
        assertThat(claim.getPolicyNumber()).isEqualTo(policyNumber);
        assertThat(claim.getClaimedAmount()).isEqualTo(claimedAmount);
        assertThat(claim.getSubmittedDate()).isEqualTo(submittedDate);
        assertThat(claim.getResponseDate()).isEqualTo(responseDate);
    }
}