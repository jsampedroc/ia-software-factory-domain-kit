package com.application.domain.enums;

/**
 * Enumeration defining financial statuses of an invoice.
 * <p>
 * The lifecycle of an invoice typically follows:
 * DRAFT → ISSUED → [PARTIAL_PAID] → PAID or OVERDUE.
 * CANCELLED is a terminal state that can be reached from DRAFT or ISSUED.
 * </p>
 */
public enum InvoiceStatus {
    /**
     * Invoice is being prepared and has not been finalized.
     * Can be modified freely.
     */
    DRAFT,

    /**
     * Invoice has been finalized and sent to the patient/insurance.
     * No further modifications to items are allowed.
     */
    ISSUED,

    /**
     * Partial payment has been received, but the invoice is not fully paid.
     * The remaining balance is still due.
     */
    PARTIAL_PAID,

    /**
     * Invoice has been paid in full.
     * This is a terminal, successful state.
     */
    PAID,

    /**
     * Invoice due date has passed without full payment.
     * May trigger restrictions on patient scheduling.
     */
    OVERDUE,

    /**
     * Invoice has been voided and is no longer valid.
     * This is a terminal state.
     */
    CANCELLED
}