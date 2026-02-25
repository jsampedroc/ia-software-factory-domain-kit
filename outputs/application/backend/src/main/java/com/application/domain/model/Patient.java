package com.application.domain.model;

import com.application.domain.shared.Entity;
import com.application.domain.valueobject.PatientId;
import com.application.domain.valueobject.MedicalAlertId;
import com.application.domain.valueobject.AllergyId;
import com.application.domain.enums.PatientStatus;
import lombok.Getter;
import lombok.ToString;
import lombok.experimental.SuperBuilder;

import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

@Getter
@ToString(callSuper = true)
@SuperBuilder(toBuilder = true)
public class Patient extends Entity<PatientId> {

    private final PatientIdentity identity;
    private final Set<MedicalAlert> medicalAlerts;
    private final Set<Allergy> allergies;
    private final List<DigitalConsent> consents;
    private final PatientStatus status;

    protected Patient(PatientId patientId,
                     PatientIdentity identity,
                     Set<MedicalAlert> medicalAlerts,
                     Set<Allergy> allergies,
                     List<DigitalConsent> consents,
                     PatientStatus status) {
        super(patientId);
        this.identity = identity;
        this.medicalAlerts = medicalAlerts != null ? new LinkedHashSet<>(medicalAlerts) : new LinkedHashSet<>();
        this.allergies = allergies != null ? new LinkedHashSet<>(allergies) : new LinkedHashSet<>();
        this.consents = consents != null ? List.copyOf(consents) : List.of();
        this.status = status;
        validate();
    }

    private void validate() {
        if (identity == null) {
            throw new IllegalArgumentException("Patient identity cannot be null");
        }
        if (status == null) {
            throw new IllegalArgumentException("Patient status cannot be null");
        }
    }

    public Patient archive() {
        return this.toBuilder()
                .status(PatientStatus.ARCHIVED)
                .build();
    }

    public Patient deactivate() {
        return this.toBuilder()
                .status(PatientStatus.INACTIVE)
                .build();
    }

    public Patient activate() {
        return this.toBuilder()
                .status(PatientStatus.ACTIVE)
                .build();
    }

    public Patient addMedicalAlert(MedicalAlert alert) {
        Set<MedicalAlert> updatedAlerts = new LinkedHashSet<>(this.medicalAlerts);
        updatedAlerts.add(alert);
        return this.toBuilder()
                .medicalAlerts(updatedAlerts)
                .build();
    }

    public Patient removeMedicalAlert(MedicalAlertId alertId) {
        Set<MedicalAlert> updatedAlerts = new LinkedHashSet<>(this.medicalAlerts);
        updatedAlerts.removeIf(alert -> alert.getAlertId().equals(alertId));
        return this.toBuilder()
                .medicalAlerts(updatedAlerts)
                .build();
    }

    public Patient addAllergy(Allergy allergy) {
        Set<Allergy> updatedAllergies = new LinkedHashSet<>(this.allergies);
        updatedAllergies.add(allergy);
        return this.toBuilder()
                .allergies(updatedAllergies)
                .build();
    }

    public Patient removeAllergy(AllergyId allergyId) {
        Set<Allergy> updatedAllergies = new LinkedHashSet<>(this.allergies);
        updatedAllergies.removeIf(allergy -> allergy.getAllergyId().equals(allergyId));
        return this.toBuilder()
                .allergies(updatedAllergies)
                .build();
    }

    public Patient addConsent(DigitalConsent consent) {
        List<DigitalConsent> updatedConsents = new java.util.ArrayList<>(this.consents);
        updatedConsents.add(consent);
        return this.toBuilder()
                .consents(List.copyOf(updatedConsents))
                .build();
    }

    public boolean hasActiveHighSeverityAlerts() {
        return medicalAlerts.stream()
                .anyMatch(alert -> alert.isActive() && alert.getSeverity() == com.application.domain.enums.AlertSeverity.HIGH);
    }

    public boolean hasValidConsentFor(com.application.domain.enums.ConsentType consentType) {
        return consents.stream()
                .filter(c -> c.getConsentType() == consentType)
                .filter(c -> !c.isRevoked())
                .max(java.util.Comparator.comparing(DigitalConsent::getGivenAt))
                .isPresent();
    }

    public Set<Allergy> getActiveAllergies() {
        return allergies; // Allergies are considered active unless explicitly removed
    }

    public Set<MedicalAlert> getActiveMedicalAlerts() {
        return medicalAlerts.stream()
                .filter(MedicalAlert::isActive)
                .collect(java.util.stream.Collectors.toCollection(LinkedHashSet::new));
    }
}