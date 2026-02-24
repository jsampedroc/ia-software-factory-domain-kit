package com.application.infrastructure.entity;

import com.application.domain.valueobject.TreatmentId;
import com.application.domain.valueobject.TreatmentPlanId;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import static lombok.AccessLevel.PROTECTED;

@Entity
@Table(name = "plan_treatments")
@Getter
@NoArgsConstructor(access = PROTECTED)
public class PlanTreatmentEntity {

    @EmbeddedId
    private PlanTreatmentId id;

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("treatmentPlanId")
    @JoinColumn(name = "treatment_plan_id", nullable = false, foreignKey = @ForeignKey(name = "fk_plan_treatment_treatment_plan"))
    private TreatmentPlanEntity treatmentPlan;

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("treatmentId")
    @JoinColumn(name = "treatment_id", nullable = false, foreignKey = @ForeignKey(name = "fk_plan_treatment_treatment"))
    private TreatmentEntity treatment;

    @Column(name = "orden", nullable = false)
    private Integer orden;

    @Column(name = "sesion_estimada")
    private Integer sesionEstimada;

    @Column(name = "costo_aplicado", precision = 10, scale = 2)
    private BigDecimal costoAplicado;

    @Column(name = "notas")
    private String notas;

    @Column(name = "fecha_agregado", nullable = false)
    private LocalDateTime fechaAgregado;

    @Column(name = "fecha_completado")
    private LocalDateTime fechaCompletado;

    @Embeddable
    @Getter
    @NoArgsConstructor(access = PROTECTED)
    public static class PlanTreatmentId implements java.io.Serializable {
        
        @Column(name = "treatment_plan_id")
        private TreatmentPlanId treatmentPlanId;

        @Column(name = "treatment_id")
        private TreatmentId treatmentId;

        public PlanTreatmentId(TreatmentPlanId treatmentPlanId, TreatmentId treatmentId) {
            this.treatmentPlanId = treatmentPlanId;
            this.treatmentId = treatmentId;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            PlanTreatmentId that = (PlanTreatmentId) o;
            return treatmentPlanId.equals(that.treatmentPlanId) && treatmentId.equals(that.treatmentId);
        }

        @Override
        public int hashCode() {
            int result = treatmentPlanId.hashCode();
            result = 31 * result + treatmentId.hashCode();
            return result;
        }
    }
}