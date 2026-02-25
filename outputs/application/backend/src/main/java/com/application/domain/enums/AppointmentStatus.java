package com.application.domain.enums;

/**
 * Enumeration defining lifecycle statuses of an appointment.
 * <p>
 * The status transitions are governed by business rules in the scheduling domain.
 * </p>
 */
public enum AppointmentStatus {
    /**
     * Appointment has been created but not yet confirmed by the clinic or patient.
     */
    SCHEDULED,
    /**
     * Appointment has been confirmed (e.g., via patient reminder or receptionist action).
     */
    CONFIRMED,
    /**
     * Patient has arrived and the appointment is currently in progress.
     */
    IN_PROGRESS,
    /**
     * Appointment has concluded successfully.
     */
    COMPLETED,
    /**
     * Appointment was cancelled before the scheduled time.
     * Business rule: Cancellation allowed up to 24 hours prior without penalty.
     */
    CANCELLED,
    /**
     * Patient did not arrive for the scheduled appointment.
     * Business rule: 3 or more NO_SHOWs within 6 months may restrict future scheduling.
     */
    NO_SHOW
}