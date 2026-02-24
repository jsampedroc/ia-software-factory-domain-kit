package com.application.application.dto;

import com.application.domain.model.TreatmentPlan;
import com.application.domain.valueobject.PatientId;
import com.application.domain.valueobject.PlanStatus;
import com.application.domain.valueobject.TreatmentPlanId;
import com.application.domain.valueobject.TreatmentId;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

public record TreatmentPlanDTO(
        UUID planId,
        UUID patientId,
        LocalDate fechaCreacion,
        LocalDate fechaInicio,
        LocalDate fechaFinEstimada,
        PlanStatus estado,
        BigDecimal costoTotalEstimado,
        List<TreatmentPlanItemDTO> tratamientos
) {
    public static TreatmentPlanDTO fromDomain(TreatmentPlan plan) {
        List<TreatmentPlanItemDTO> items = plan.getTratamientos().stream()
                .map(TreatmentPlanItemDTO::fromDomain)
                .toList();

        return new TreatmentPlanDTO(
                plan.getId().value(),
                plan.getPatientId().value(),
                plan.getFechaCreacion(),
                plan.getFechaInicio(),
                plan.getFechaFinEstimada(),
                plan.getEstado(),
                plan.getCostoTotalEstimado(),
                items
        );
    }

    public TreatmentPlan toDomain() {
        TreatmentPlanId id = TreatmentPlanId.of(planId);
        PatientId patientIdObj = PatientId.of(patientId);

        return new TreatmentPlan(
                id,
                patientIdObj,
                fechaCreacion,
                fechaInicio,
                fechaFinEstimada,
                estado,
                costoTotalEstimado,
                tratamientos != null ? 
                    tratamientos.stream()
                        .map(TreatmentPlanItemDTO::toDomain)
                        .toList() 
                    : List.of()
        );
    }

    public record TreatmentPlanItemDTO(
            UUID treatmentId,
            String codigo,
            String nombre,
            Integer orden,
            LocalDate fechaProgramada,
            Boolean completado
    ) {
        public static TreatmentPlanItemDTO fromDomain(TreatmentPlan.TreatmentPlanItem item) {
            return new TreatmentPlanItemDTO(
                    item.treatmentId().value(),
                    item.codigo(),
                    item.nombre(),
                    item.orden(),
                    item.fechaProgramada(),
                    item.completado()
            );
        }

        public TreatmentPlan.TreatmentPlanItem toDomain() {
            return new TreatmentPlan.TreatmentPlanItem(
                    TreatmentId.of(treatmentId),
                    codigo,
                    nombre,
                    orden,
                    fechaProgramada,
                    completado
            );
        }
    }
}