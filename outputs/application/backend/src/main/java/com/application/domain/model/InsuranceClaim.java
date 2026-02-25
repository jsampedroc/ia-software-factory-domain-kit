package com.application.domain.model;

import com.application.domain.shared.Entity;
import com.application.domain.valueobject.InsuranceClaimId;
import com.application.domain.enums.ClaimStatus;
import lombok.Getter;
import lombok.ToString;
import lombok.experimental.SuperBuilder;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Objects;

@Getter
@ToString(callSuper = true)
@SuperBuilder(toBuilder = true)
public class InsuranceClaim extends Entity<InsuranceClaimId> {

    private final InsuranceClaimId claimId;
    private final String patientId;
    private final String invoiceId;
    private final String insuranceProvider;
    private final String policyNumber;
    private final BigDecimal claimedAmount;
    private ClaimStatus status;
    private final LocalDate submittedDate;
    private LocalDate responseDate;

    public InsuranceClaim(InsuranceClaimId claimId, String patientId, String invoiceId, String insuranceProvider,
                          String policyNumber, BigDecimal claimedAmount, ClaimStatus status,
                          LocalDate submittedDate, LocalDate responseDate) {
        super(claimId);
        this.claimId = Objects.requireNonNull(claimId, "Claim ID cannot be null");
        this.patientId = Objects.requireNonNull(patientId, "Patient ID cannot be null");
        this.invoiceId = Objects.requireNonNull(invoiceId, "Invoice ID cannot be null");
        this.insuranceProvider = Objects.requireNonNull(insuranceProvider, "Insurance provider cannot be null");
        this.policyNumber = Objects.requireNonNull(policyNumber, "Policy number cannot be null");
        this.claimedAmount = Objects.requireNonNull(claimedAmount, "Claimed amount cannot be null");
        this.status = Objects.requireNonNull(status, "Status cannot be null");
        this.submittedDate = Objects.requireNonNull(submittedDate, "Submitted date cannot be null");
        this.responseDate = responseDate;
        validateClaim();
    }

    private void validateClaim() {
        if (claimedAmount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Claimed amount must be greater than zero");
        }
        if (submittedDate.isAfter(LocalDate.now())) {
            throw new IllegalArgumentException("Submitted date cannot be in the future");
        }
        if (responseDate != null && responseDate.isBefore(submittedDate)) {
            throw new IllegalArgumentException("Response date cannot be before submitted date");
        }
    }

    public void updateStatus(ClaimStatus newStatus, LocalDate responseDate) {
        Objects.requireNonNull(newStatus, "New status cannot be null");
        this.status = newStatus;
        this.responseDate = responseDate;
        if (responseDate != null && responseDate.isBefore(submittedDate)) {
            throw new IllegalArgumentException("Response date cannot be before submitted date");
        }
    }

    public boolean isApproved() {
        return status == ClaimStatus.APPROVED || status == ClaimStatus.PAID;
    }

    public boolean isRejected() {
        return status == ClaimStatus.REJECTED;
    }

    public boolean isPending() {
        return status == ClaimStatus.SUBMITTED;
    }
}