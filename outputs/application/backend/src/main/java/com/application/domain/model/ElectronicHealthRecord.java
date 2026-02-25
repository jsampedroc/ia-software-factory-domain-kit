package com.application.domain.model;

import com.application.domain.model.base.AggregateRoot;
import com.application.domain.model.valueobject.*;
import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.*;

@Entity
@Table(name = "electronic_health_records")
public class ElectronicHealthRecord extends AggregateRoot<ElectronicHealthRecordId> {

    @EmbeddedId
    private ElectronicHealthRecordId id;

    @Embedded
    private PatientId patientId;

    @ElementCollection
    @CollectionTable(name = "ehr_allergies", joinColumns = @JoinColumn(name = "ehr_id"))
    private Set<Allergy> allergies = new HashSet<>();

    @ElementCollection
    @CollectionTable(name = "ehr_medications", joinColumns = @JoinColumn(name = "ehr_id"))
    private Set<Medication> currentMedications = new HashSet<>();

    @OneToOne(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "odontogram_id")
    private Odontogram odontogram;

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    @JoinColumn(name = "ehr_id")
    private List<TreatmentRecord> treatmentHistory = new ArrayList<>();

    @Embedded
    private DigitalConsent digitalConsent;

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "last_updated_at", nullable = false)
    private LocalDateTime lastUpdatedAt;

    protected ElectronicHealthRecord() {}

    public ElectronicHealthRecord(ElectronicHealthRecordId id, PatientId patientId) {
        this.id = Objects.requireNonNull(id, "ElectronicHealthRecordId cannot be null");
        this.patientId = Objects.requireNonNull(patientId, "PatientId cannot be null");
        this.createdAt = LocalDateTime.now();
        this.lastUpdatedAt = this.createdAt;
        this.digitalConsent = new DigitalConsent(false, null, null);
    }

    public void addAllergy(Allergy allergy) {
        this.allergies.add(Objects.requireNonNull(allergy));
        this.lastUpdatedAt = LocalDateTime.now();
    }

    public void addMedication(Medication medication) {
        this.currentMedications.add(Objects.requireNonNull(medication));
        this.lastUpdatedAt = LocalDateTime.now();
    }

    public void setOdontogram(Odontogram odontogram) {
        this.odontogram = odontogram;
        this.lastUpdatedAt = LocalDateTime.now();
    }

    public void addTreatmentRecord(TreatmentRecord record) {
        this.treatmentHistory.add(Objects.requireNonNull(record));
        this.lastUpdatedAt = LocalDateTime.now();
    }

    public void updateDigitalConsent(boolean consented, LocalDateTime consentDate, String consentIpAddress) {
        this.digitalConsent = new DigitalConsent(consented, consentDate, consentIpAddress);
        this.lastUpdatedAt = LocalDateTime.now();
    }

    @Override
    public ElectronicHealthRecordId getId() {
        return id;
    }

    public PatientId getPatientId() {
        return patientId;
    }

    public Set<Allergy> getAllergies() {
        return Collections.unmodifiableSet(allergies);
    }

    public Set<Medication> getCurrentMedications() {
        return Collections.unmodifiableSet(currentMedications);
    }

    public Odontogram getOdontogram() {
        return odontogram;
    }

    public List<TreatmentRecord> getTreatmentHistory() {
        return Collections.unmodifiableList(treatmentHistory);
    }

    public DigitalConsent getDigitalConsent() {
        return digitalConsent;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public LocalDateTime getLastUpdatedAt() {
        return lastUpdatedAt;
    }
}