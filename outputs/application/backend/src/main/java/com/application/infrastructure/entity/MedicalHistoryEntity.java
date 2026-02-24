package com.application.infrastructure.entity;

import com.application.domain.model.MedicalHistory;
import com.application.domain.valueobject.MedicalHistoryId;
import com.application.domain.valueobject.PatientId;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;

import static lombok.AccessLevel.PROTECTED;

@Entity
@Table(name = "medical_history")
@Getter
@NoArgsConstructor(access = PROTECTED)
public class MedicalHistoryEntity {

    @Id
    private UUID id;

    @Column(name = "patient_id", nullable = false, unique = true)
    private UUID patientId;

    @Column(name = "creation_date", nullable = false)
    private LocalDateTime creationDate;

    @Column(name = "last_update", nullable = false)
    private LocalDateTime lastUpdate;

    @Column(columnDefinition = "TEXT")
    private String allergies;

    @Column(name = "medical_conditions", columnDefinition = "TEXT")
    private String medicalConditions;

    @Column(columnDefinition = "TEXT")
    private String medications;

    @Column(name = "general_observations", columnDefinition = "TEXT")
    private String generalObservations;

    @OneToOne(fetch = FetchType.LAZY)
    @MapsId
    @JoinColumn(name = "patient_id", insertable = false, updatable = false)
    private PatientEntity patient;

    public static MedicalHistoryEntity fromDomain(MedicalHistory medicalHistory) {
        MedicalHistoryEntity entity = new MedicalHistoryEntity();
        entity.id = medicalHistory.getId().getValue();
        entity.patientId = medicalHistory.getPatientId().getValue();
        entity.creationDate = medicalHistory.getCreationDate();
        entity.lastUpdate = medicalHistory.getLastUpdate();
        entity.allergies = medicalHistory.getAllergies();
        entity.medicalConditions = medicalHistory.getMedicalConditions();
        entity.medications = medicalHistory.getMedications();
        entity.generalObservations = medicalHistory.getGeneralObservations();
        return entity;
    }

    public MedicalHistory toDomain() {
        return new MedicalHistory(
                MedicalHistoryId.of(id),
                PatientId.of(patientId),
                creationDate,
                lastUpdate,
                allergies,
                medicalConditions,
                medications,
                generalObservations
        );
    }

    public void updateFromDomain(MedicalHistory medicalHistory) {
        this.lastUpdate = medicalHistory.getLastUpdate();
        this.allergies = medicalHistory.getAllergies();
        this.medicalConditions = medicalHistory.getMedicalConditions();
        this.medications = medicalHistory.getMedications();
        this.generalObservations = medicalHistory.getGeneralObservations();
    }
}