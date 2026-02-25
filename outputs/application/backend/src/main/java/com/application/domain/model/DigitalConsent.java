package com.application.domain.model;

import com.application.domain.model.base.AggregateRoot;
import com.application.domain.model.valueobject.ConsentId;
import com.application.domain.model.valueobject.PatientId;
import com.application.domain.model.valueobject.PractitionerId;
import java.time.LocalDateTime;

public class DigitalConsent extends AggregateRoot<ConsentId> {
    private final PatientId patientId;
    private final PractitionerId practitionerId;
    private final String consentFormType;
    private final String consentText;
    private final LocalDateTime grantedAt;
    private final LocalDateTime expiresAt;
    private final boolean isRevoked;

    public DigitalConsent(ConsentId id,
                          PatientId patientId,
                          PractitionerId practitionerId,
                          String consentFormType,
                          String consentText,
                          LocalDateTime grantedAt,
                          LocalDateTime expiresAt,
                          boolean isRevoked) {
        super(id);
        this.patientId = patientId;
        this.practitionerId = practitionerId;
        this.consentFormType = consentFormType;
        this.consentText = consentText;
        this.grantedAt = grantedAt;
        this.expiresAt = expiresAt;
        this.isRevoked = isRevoked;
    }

    public PatientId getPatientId() {
        return patientId;
    }

    public PractitionerId getPractitionerId() {
        return practitionerId;
    }

    public String getConsentFormType() {
        return consentFormType;
    }

    public String getConsentText() {
        return consentText;
    }

    public LocalDateTime getGrantedAt() {
        return grantedAt;
    }

    public LocalDateTime getExpiresAt() {
        return expiresAt;
    }

    public boolean isRevoked() {
        return isRevoked;
    }
}