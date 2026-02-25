package com.application.domain.enums;

/**
 * Enumeration defining types of digital consent.
 * Used by the DigitalConsent entity to categorize the nature of the consent.
 */
public enum ConsentType {
    /**
     * Consent for a specific dental treatment or procedure.
     */
    TREATMENT,

    /**
     * Consent for how the patient's personal and health data may be used.
     */
    DATA_USAGE,

    /**
     * Consent to the clinic's general privacy policy and terms of service.
     */
    PRIVACY_POLICY
}