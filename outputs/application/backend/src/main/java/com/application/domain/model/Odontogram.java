package com.application.domain.model;

import com.application.domain.model.base.Entity;
import com.application.domain.model.valueobject.ConsentId;
import com.application.domain.model.valueobject.PatientId;
import com.application.domain.model.valueobject.PractitionerId;
import java.time.LocalDateTime;

public class DigitalConsent extends Entity<ConsentId> {
    private final PatientId patientId;
    private final PractitionerId practitionerId;
    private final String consentType;
    private final String documentReference;
    private final LocalDateTime grantedAt;
    private final LocalDateTime expiresAt;
    private final boolean isRevoked;

    public DigitalConsent(ConsentId id, PatientId patientId, PractitionerId practitionerId,
                          String consentType, String documentReference,
                          LocalDateTime grantedAt, LocalDateTime expiresAt, boolean isRevoked) {
        super(id);
        this.patientId = patientId;
        this.practitionerId = practitionerId;
        this.consentType = consentType;
        this.documentReference = documentReference;
        this.grantedAt = grantedAt;
        this.expiresAt = expiresAt;
        this.isRevoked = isRevoked;
    }

    public PatientId getPatientId() { return patientId; }
    public PractitionerId getPractitionerId() { return practitionerId; }
    public String getConsentType() { return consentType; }
    public String getDocumentReference() { return documentReference; }
    public LocalDateTime getGrantedAt() { return grantedAt; }
    public LocalDateTime getExpiresAt() { return expiresAt; }
    public boolean isRevoked() { return isRevoked; }
}