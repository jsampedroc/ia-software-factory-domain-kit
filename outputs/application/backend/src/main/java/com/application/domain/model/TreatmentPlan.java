package com.application.domain.model;

import com.application.domain.shared.Entity;
import com.application.domain.valueobject.PatientId;
import com.application.domain.valueobject.TreatmentPlanId;
import com.application.domain.valueobject.PlanStatus;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Objects;

import static lombok.AccessLevel.PROTECTED;

@Getter
@NoArgsConstructor(access = PROTECTED)
public class TreatmentPlan extends Entity<TreatmentPlanId> {

    private PatientId patientId;
    private LocalDate fechaCreacion;
    private LocalDate fechaInicio;
    private LocalDate fechaFinEstimada;
    private PlanStatus estado;
    private BigDecimal costoTotalEstimado;

    private TreatmentPlan(TreatmentPlanId id,
                         PatientId patientId,
                         LocalDate fechaCreacion,
                         LocalDate fechaInicio,
                         LocalDate fechaFinEstimada,
                         PlanStatus estado,
                         BigDecimal costoTotalEstimado) {
        super(id);
        this.patientId = Objects.requireNonNull(patientId, "El ID del paciente no puede ser nulo");
        this.fechaCreacion = Objects.requireNonNull(fechaCreacion, "La fecha de creación no puede ser nula");
        this.fechaInicio = Objects.requireNonNull(fechaInicio, "La fecha de inicio no puede ser nula");
        this.fechaFinEstimada = Objects.requireNonNull(fechaFinEstimada, "La fecha fin estimada no puede ser nula");
        this.estado = Objects.requireNonNull(estado, "El estado no puede ser nulo");
        this.costoTotalEstimado = Objects.requireNonNull(costoTotalEstimado, "El costo total estimado no puede ser nulo");
        validate();
    }

    public static TreatmentPlan create(PatientId patientId,
                                       LocalDate fechaInicio,
                                       LocalDate fechaFinEstimada,
                                       BigDecimal costoTotalEstimado) {
        return new TreatmentPlan(
                TreatmentPlanId.generate(),
                patientId,
                LocalDate.now(),
                fechaInicio,
                fechaFinEstimada,
                PlanStatus.BORRADOR,
                costoTotalEstimado
        );
    }

    public void activate() {
        if (this.estado != PlanStatus.BORRADOR) {
            throw new IllegalStateException("Solo un plan en estado BORRADOR puede ser activado");
        }
        if (fechaInicio.isAfter(fechaFinEstimada)) {
            throw new IllegalStateException("La fecha de inicio no puede ser posterior a la fecha fin estimada");
        }
        if (costoTotalEstimado.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalStateException("El costo total estimado debe ser mayor a cero");
        }
        this.estado = PlanStatus.ACTIVO;
    }

    public void complete() {
        if (this.estado != PlanStatus.ACTIVO) {
            throw new IllegalStateException("Solo un plan ACTIVO puede ser completado");
        }
        this.estado = PlanStatus.COMPLETADO;
    }

    public void cancel() {
        if (this.estado == PlanStatus.COMPLETADO) {
            throw new IllegalStateException("Un plan COMPLETADO no puede ser cancelado");
        }
        this.estado = PlanStatus.CANCELADO;
    }

    public void updateEstimatedCost(BigDecimal newCost) {
        if (this.estado == PlanStatus.ACTIVO || this.estado == PlanStatus.COMPLETADO) {
            throw new IllegalStateException("No se puede modificar el costo de un plan ACTIVO o COMPLETADO");
        }
        if (newCost.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("El costo total estimado debe ser mayor a cero");
        }
        this.costoTotalEstimado = newCost;
    }

    public void updateDates(LocalDate newFechaInicio, LocalDate newFechaFinEstimada) {
        if (this.estado == PlanStatus.ACTIVO || this.estado == PlanStatus.COMPLETADO) {
            throw new IllegalStateException("No se pueden modificar las fechas de un plan ACTIVO o COMPLETADO");
        }
        if (newFechaInicio.isAfter(newFechaFinEstimada)) {
            throw new IllegalArgumentException("La fecha de inicio no puede ser posterior a la fecha fin estimada");
        }
        this.fechaInicio = newFechaInicio;
        this.fechaFinEstimada = newFechaFinEstimada;
    }

    private void validate() {
        if (fechaInicio.isAfter(fechaFinEstimada)) {
            throw new IllegalArgumentException("La fecha de inicio no puede ser posterior a la fecha fin estimada");
        }
        if (costoTotalEstimado.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("El costo total estimado debe ser mayor a cero");
        }
        if (fechaCreacion.isAfter(LocalDate.now())) {
            throw new IllegalArgumentException("La fecha de creación no puede ser futura");
        }
    }
}