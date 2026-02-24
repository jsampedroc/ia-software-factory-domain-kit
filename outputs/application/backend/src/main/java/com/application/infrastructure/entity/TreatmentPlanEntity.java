package com.application.infrastructure.entity;

import com.application.domain.model.TreatmentPlan;
import com.application.domain.valueobject.PatientId;
import com.application.domain.valueobject.DentistId;
import com.application.domain.valueobject.ClinicId;
import com.application.domain.valueobject.PlanStatus;
import com.application.domain.valueobject.TreatmentPlanId;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static lombok.AccessLevel.PROTECTED;

@Entity
@Table(name = "treatment_plans")
@Getter
@NoArgsConstructor(access = PROTECTED)
public class TreatmentPlanEntity {

    @Id
    private UUID id;

    @Column(name = "patient_id", nullable = false)
    private UUID patientId;

    @Column(name = "dentist_id", nullable = false)
    private UUID dentistId;

    @Column(name = "clinic_id", nullable = false)
    private UUID clinicId;

    @Column(name = "creation_date", nullable = false)
    private LocalDate creationDate;

    @Column(name = "start_date")
    private LocalDate startDate;

    @Column(name = "estimated_end_date")
    private LocalDate estimatedEndDate;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private PlanStatus status;

    @Column(name = "estimated_total_cost", precision = 10, scale = 2)
    private BigDecimal estimatedTotalCost;

    @Column(name = "description", columnDefinition = "TEXT")
    private String description;

    @Column(name = "notes", columnDefinition = "TEXT")
    private String notes;

    @OneToMany(mappedBy = "treatmentPlan", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<PlanTreatmentEntity> planTreatments = new ArrayList<>();

    public static TreatmentPlanEntity fromDomain(TreatmentPlan treatmentPlan) {
        TreatmentPlanEntity entity = new TreatmentPlanEntity();
        entity.id = treatmentPlan.getId().getValue();
        entity.patientId = treatmentPlan.getPatientId().getValue();
        entity.dentistId = treatmentPlan.getDentistId().getValue();
        entity.clinicId = treatmentPlan.getClinicId().getValue();
        entity.creationDate = treatmentPlan.getCreationDate();
        entity.startDate = treatmentPlan.getStartDate();
        entity.estimatedEndDate = treatmentPlan.getEstimatedEndDate();
        entity.status = treatmentPlan.getStatus();
        entity.estimatedTotalCost = treatmentPlan.getEstimatedTotalCost();
        entity.description = treatmentPlan.getDescription();
        entity.notes = treatmentPlan.getNotes();
        return entity;
    }

    public TreatmentPlan toDomain() {
        return TreatmentPlan.builder()
                .id(TreatmentPlanId.of(id))
                .patientId(PatientId.of(patientId))
                .dentistId(DentistId.of(dentistId))
                .clinicId(ClinicId.of(clinicId))
                .creationDate(creationDate)
                .startDate(startDate)
                .estimatedEndDate(estimatedEndDate)
                .status(status)
                .estimatedTotalCost(estimatedTotalCost)
                .description(description)
                .notes(notes)
                .build();
    }

    public void updateFromDomain(TreatmentPlan treatmentPlan) {
        this.patientId = treatmentPlan.getPatientId().getValue();
        this.dentistId = treatmentPlan.getDentistId().getValue();
        this.clinicId = treatmentPlan.getClinicId().getValue();
        this.creationDate = treatmentPlan.getCreationDate();
        this.startDate = treatmentPlan.getStartDate();
        this.estimatedEndDate = treatmentPlan.getEstimatedEndDate();
        this.status = treatmentPlan.getStatus();
        this.estimatedTotalCost = treatmentPlan.getEstimatedTotalCost();
        this.description = treatmentPlan.getDescription();
        this.notes = treatmentPlan.getNotes();
    }
}