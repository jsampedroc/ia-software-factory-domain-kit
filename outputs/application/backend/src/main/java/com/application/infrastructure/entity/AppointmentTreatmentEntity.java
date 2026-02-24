package com.application.infrastructure.entity;

import com.application.domain.valueobject.AppointmentId;
import com.application.domain.valueobject.TreatmentId;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

import static lombok.AccessLevel.PROTECTED;

@Entity
@Table(name = "appointment_treatment")
@Getter
@NoArgsConstructor(access = PROTECTED)
public class AppointmentTreatmentEntity {

    @EmbeddedId
    private AppointmentTreatmentId id;

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("appointmentId")
    @JoinColumn(name = "appointment_id", nullable = false)
    private AppointmentEntity appointment;

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("treatmentId")
    @JoinColumn(name = "treatment_id", nullable = false)
    private TreatmentEntity treatment;

    @Column(name = "cantidad", nullable = false)
    private Integer cantidad;

    @Column(name = "costo_aplicado", nullable = false)
    private Double costoAplicado;

    @Column(name = "observaciones")
    private String observaciones;

    @Column(name = "fecha_registro", nullable = false)
    private LocalDateTime fechaRegistro;

    @Embeddable
    @Getter
    @NoArgsConstructor(access = PROTECTED)
    public static class AppointmentTreatmentId implements java.io.Serializable {
        @Column(name = "appointment_id", columnDefinition = "BINARY(16)")
        private AppointmentId appointmentId;

        @Column(name = "treatment_id", columnDefinition = "BINARY(16)")
        private TreatmentId treatmentId;

        public AppointmentTreatmentId(AppointmentId appointmentId, TreatmentId treatmentId) {
            this.appointmentId = appointmentId;
            this.treatmentId = treatmentId;
        }
    }
}