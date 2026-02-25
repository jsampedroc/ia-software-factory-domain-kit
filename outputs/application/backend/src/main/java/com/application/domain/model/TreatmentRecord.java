package com.application.domain.model;

import com.application.domain.model.base.AggregateRoot;
import com.application.domain.model.valueobject.TreatmentRecordId;
import com.application.domain.model.valueobject.PatientId;
import com.application.domain.model.valueobject.DentistId;
import com.application.domain.model.valueobject.TreatmentCode;
import com.application.domain.model.valueobject.ToothNumber;
import com.application.domain.model.valueobject.Money;
import java.time.LocalDateTime;
import java.util.Set;
import java.util.HashSet;
import java.util.Collections;

public class TreatmentRecord extends AggregateRoot<TreatmentRecordId> {
    private PatientId patientId;
    private DentistId dentistId;
    private TreatmentCode treatmentCode;
    private Set<ToothNumber> affectedTeeth;
    private Money cost;
    private LocalDateTime treatmentDate;
    private String clinicalNotes;
    private boolean isCompleted;
    private boolean isPaid;

    public TreatmentRecord(TreatmentRecordId id,
                           PatientId patientId,
                           DentistId dentistId,
                           TreatmentCode treatmentCode,
                           Set<ToothNumber> affectedTeeth,
                           Money cost,
                           LocalDateTime treatmentDate,
                           String clinicalNotes) {
        super(id);
        this.patientId = patientId;
        this.dentistId = dentistId;
        this.treatmentCode = treatmentCode;
        this.affectedTeeth = (affectedTeeth != null) ? new HashSet<>(affectedTeeth) : new HashSet<>();
        this.cost = cost;
        this.treatmentDate = treatmentDate;
        this.clinicalNotes = clinicalNotes;
        this.isCompleted = false;
        this.isPaid = false;
    }

    public PatientId getPatientId() { return patientId; }
    public DentistId getDentistId() { return dentistId; }
    public TreatmentCode getTreatmentCode() { return treatmentCode; }
    public Set<ToothNumber> getAffectedTeeth() { return Collections.unmodifiableSet(affectedTeeth); }
    public Money getCost() { return cost; }
    public LocalDateTime getTreatmentDate() { return treatmentDate; }
    public String getClinicalNotes() { return clinicalNotes; }
    public boolean isCompleted() { return isCompleted; }
    public boolean isPaid() { return isPaid; }

    public void markAsCompleted() { this.isCompleted = true; }
    public void markAsPaid() { this.isPaid = true; }
    public void updateClinicalNotes(String notes) { this.clinicalNotes = notes; }
    public void addAffectedTooth(ToothNumber tooth) { this.affectedTeeth.add(tooth); }
    public void removeAffectedTooth(ToothNumber tooth) { this.affectedTeeth.remove(tooth); }
}