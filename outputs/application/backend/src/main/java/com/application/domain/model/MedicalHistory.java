package com.application.domain.model;

import com.application.domain.shared.Entity;
import com.application.domain.valueobject.MedicalHistoryId;
import com.application.domain.valueobject.PatientId;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

import static lombok.AccessLevel.PROTECTED;

@Getter
@NoArgsConstructor(access = PROTECTED)
public class MedicalHistory extends Entity<MedicalHistoryId> {

    private PatientId patientId;
    private LocalDateTime fechaCreacion;
    private LocalDateTime ultimaActualizacion;
    private String alergias;
    private String condicionesMedicas;
    private String medicamentos;
    private String observacionesGenerales;

    private MedicalHistory(MedicalHistoryId id,
                           PatientId patientId,
                           LocalDateTime fechaCreacion,
                           LocalDateTime ultimaActualizacion,
                           String alergias,
                           String condicionesMedicas,
                           String medicamentos,
                           String observacionesGenerales) {
        super(id);
        this.patientId = patientId;
        this.fechaCreacion = fechaCreacion;
        this.ultimaActualizacion = ultimaActualizacion;
        this.alergias = alergias;
        this.condicionesMedicas = condicionesMedicas;
        this.medicamentos = medicamentos;
        this.observacionesGenerales = observacionesGenerales;
    }

    public static MedicalHistory create(MedicalHistoryId id,
                                        PatientId patientId,
                                        String alergias,
                                        String condicionesMedicas,
                                        String medicamentos,
                                        String observacionesGenerales) {
        LocalDateTime now = LocalDateTime.now();
        return new MedicalHistory(
                id,
                patientId,
                now,
                now,
                alergias,
                condicionesMedicas,
                medicamentos,
                observacionesGenerales
        );
    }

    public void update(String alergias,
                       String condicionesMedicas,
                       String medicamentos,
                       String observacionesGenerales) {
        this.alergias = alergias;
        this.condicionesMedicas = condicionesMedicas;
        this.medicamentos = medicamentos;
        this.observacionesGenerales = observacionesGenerales;
        this.ultimaActualizacion = LocalDateTime.now();
    }

    public void assignToPatient(PatientId patientId) {
        this.patientId = patientId;
        this.ultimaActualizacion = LocalDateTime.now();
    }

    public boolean hasCriticalAllergies() {
        return alergias != null &&
                (alergias.toLowerCase().contains("penicilina") ||
                 alergias.toLowerCase().contains("latex") ||
                 alergias.toLowerCase().contains("anestesia"));
    }

    public boolean hasChronicConditions() {
        return condicionesMedicas != null &&
                (condicionesMedicas.toLowerCase().contains("diabetes") ||
                 condicionesMedicas.toLowerCase().contains("hipertension") ||
                 condicionesMedicas.toLowerCase().contains("cardiopatia"));
    }
}