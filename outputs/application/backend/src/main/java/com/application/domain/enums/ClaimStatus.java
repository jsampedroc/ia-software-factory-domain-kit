package com.application.domain.enums;

/**
 * Enumeration defining processing statuses of an insurance claim.
 * <p>
 * The lifecycle of a claim typically progresses from SUBMITTED to either APPROVED or REJECTED.
 * An APPROVED claim may later transition to PAID once the insurance payment is received.
 * </p>
 */
public enum ClaimStatus {
    /**
     * Claim has been submitted to the insurance provider but not yet reviewed.
     */
    SUBMITTED,

    /**
     * Insurance provider has reviewed and approved the claim for payment.
     */
    APPROVED,

    /**
     * Insurance provider has reviewed and rejected the claim.
     */
    REJECTED,

    /**
     * Insurance provider has processed the payment for the approved claim.
     */
    PAID
}