package com.application.domain.model;

import com.application.domain.valueobject.PatientId;
import com.application.domain.valueobject.TreatmentPlanId;
import com.application.domain.valueobject.PlanStatus;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class TreatmentPlanTest {

    private final PatientId patientId = new PatientId(UUID.randomUUID());
    private final LocalDate fechaInicio = LocalDate.now().plusDays(1);
    private final LocalDate fechaFinEstimada = LocalDate.now().plusDays(30);
    private final BigDecimal costoTotalEstimado = new BigDecimal("1500.00");

    @Test
    void create_ShouldCreateTreatmentPlanWithCorrectInitialState() {
        TreatmentPlan plan = TreatmentPlan.create(patientId, fechaInicio, fechaFinEstimada, costoTotalEstimado);

        assertNotNull(plan);
        assertNotNull(plan.getId());
        assertEquals(patientId, plan.getPatientId());
        assertEquals(LocalDate.now(), plan.getFechaCreacion());
        assertEquals(fechaInicio, plan.getFechaInicio());
        assertEquals(fechaFinEstimada, plan.getFechaFinEstimada());
        assertEquals(PlanStatus.BORRADOR, plan.getEstado());
        assertEquals(costoTotalEstimado, plan.getCostoTotalEstimado());
    }

    @Test
    void create_WithInvalidDates_ShouldThrowException() {
        LocalDate invalidFechaInicio = LocalDate.now().plusDays(10);
        LocalDate invalidFechaFinEstimada = LocalDate.now().plusDays(5);

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                () -> TreatmentPlan.create(patientId, invalidFechaInicio, invalidFechaFinEstimada, costoTotalEstimado));

        assertTrue(exception.getMessage().contains("La fecha de inicio no puede ser posterior a la fecha fin estimada"));
    }

    @Test
    void create_WithZeroOrNegativeCost_ShouldThrowException() {
        BigDecimal invalidCost = BigDecimal.ZERO;

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                () -> TreatmentPlan.create(patientId, fechaInicio, fechaFinEstimada, invalidCost));

        assertTrue(exception.getMessage().contains("El costo total estimado debe ser mayor a cero"));
    }

    @Test
    void activate_FromDraftState_ShouldChangeStatusToActive() {
        TreatmentPlan plan = TreatmentPlan.create(patientId, fechaInicio, fechaFinEstimada, costoTotalEstimado);

        plan.activate();

        assertEquals(PlanStatus.ACTIVO, plan.getEstado());
    }

    @Test
    void activate_FromNonDraftState_ShouldThrowException() {
        TreatmentPlan plan = TreatmentPlan.create(patientId, fechaInicio, fechaFinEstimada, costoTotalEstimado);
        plan.activate();

        IllegalStateException exception = assertThrows(IllegalStateException.class,
                plan::activate);

        assertEquals("Solo un plan en estado BORRADOR puede ser activado", exception.getMessage());
    }

    @Test
    void activate_WithInvalidDates_ShouldThrowException() {
        LocalDate invalidFechaInicio = fechaFinEstimada.plusDays(1);
        TreatmentPlan plan = TreatmentPlan.create(patientId, invalidFechaInicio, fechaFinEstimada, costoTotalEstimado);

        IllegalStateException exception = assertThrows(IllegalStateException.class,
                plan::activate);

        assertTrue(exception.getMessage().contains("La fecha de inicio no puede ser posterior a la fecha fin estimada"));
    }

    @Test
    void complete_FromActiveState_ShouldChangeStatusToCompleted() {
        TreatmentPlan plan = TreatmentPlan.create(patientId, fechaInicio, fechaFinEstimada, costoTotalEstimado);
        plan.activate();

        plan.complete();

        assertEquals(PlanStatus.COMPLETADO, plan.getEstado());
    }

    @Test
    void complete_FromNonActiveState_ShouldThrowException() {
        TreatmentPlan plan = TreatmentPlan.create(patientId, fechaInicio, fechaFinEstimada, costoTotalEstimado);

        IllegalStateException exception = assertThrows(IllegalStateException.class,
                plan::complete);

        assertEquals("Solo un plan ACTIVO puede ser completado", exception.getMessage());
    }

    @Test
    void cancel_FromDraftState_ShouldChangeStatusToCancelled() {
        TreatmentPlan plan = TreatmentPlan.create(patientId, fechaInicio, fechaFinEstimada, costoTotalEstimado);

        plan.cancel();

        assertEquals(PlanStatus.CANCELADO, plan.getEstado());
    }

    @Test
    void cancel_FromActiveState_ShouldChangeStatusToCancelled() {
        TreatmentPlan plan = TreatmentPlan.create(patientId, fechaInicio, fechaFinEstimada, costoTotalEstimado);
        plan.activate();

        plan.cancel();

        assertEquals(PlanStatus.CANCELADO, plan.getEstado());
    }

    @Test
    void cancel_FromCompletedState_ShouldThrowException() {
        TreatmentPlan plan = TreatmentPlan.create(patientId, fechaInicio, fechaFinEstimada, costoTotalEstimado);
        plan.activate();
        plan.complete();

        IllegalStateException exception = assertThrows(IllegalStateException.class,
                plan::cancel);

        assertEquals("Un plan COMPLETADO no puede ser cancelado", exception.getMessage());
    }

    @Test
    void updateEstimatedCost_OnDraftPlan_ShouldUpdateCost() {
        TreatmentPlan plan = TreatmentPlan.create(patientId, fechaInicio, fechaFinEstimada, costoTotalEstimado);
        BigDecimal newCost = new BigDecimal("2000.00");

        plan.updateEstimatedCost(newCost);

        assertEquals(newCost, plan.getCostoTotalEstimado());
    }

    @Test
    void updateEstimatedCost_OnActivePlan_ShouldThrowException() {
        TreatmentPlan plan = TreatmentPlan.create(patientId, fechaInicio, fechaFinEstimada, costoTotalEstimado);
        plan.activate();
        BigDecimal newCost = new BigDecimal("2000.00");

        IllegalStateException exception = assertThrows(IllegalStateException.class,
                () -> plan.updateEstimatedCost(newCost));

        assertEquals("No se puede modificar el costo de un plan ACTIVO o COMPLETADO", exception.getMessage());
    }

    @Test
    void updateEstimatedCost_OnCompletedPlan_ShouldThrowException() {
        TreatmentPlan plan = TreatmentPlan.create(patientId, fechaInicio, fechaFinEstimada, costoTotalEstimado);
        plan.activate();
        plan.complete();
        BigDecimal newCost = new BigDecimal("2000.00");

        IllegalStateException exception = assertThrows(IllegalStateException.class,
                () -> plan.updateEstimatedCost(newCost));

        assertEquals("No se puede modificar el costo de un plan ACTIVO o COMPLETADO", exception.getMessage());
    }

    @Test
    void updateEstimatedCost_WithZeroOrNegativeCost_ShouldThrowException() {
        TreatmentPlan plan = TreatmentPlan.create(patientId, fechaInicio, fechaFinEstimada, costoTotalEstimado);
        BigDecimal invalidCost = BigDecimal.ZERO;

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                () -> plan.updateEstimatedCost(invalidCost));

        assertTrue(exception.getMessage().contains("El costo total estimado debe ser mayor a cero"));
    }

    @Test
    void updateDates_OnDraftPlan_ShouldUpdateDates() {
        TreatmentPlan plan = TreatmentPlan.create(patientId, fechaInicio, fechaFinEstimada, costoTotalEstimado);
        LocalDate newFechaInicio = LocalDate.now().plusDays(5);
        LocalDate newFechaFinEstimada = LocalDate.now().plusDays(40);

        plan.updateDates(newFechaInicio, newFechaFinEstimada);

        assertEquals(newFechaInicio, plan.getFechaInicio());
        assertEquals(newFechaFinEstimada, plan.getFechaFinEstimada());
    }

    @Test
    void updateDates_OnActivePlan_ShouldThrowException() {
        TreatmentPlan plan = TreatmentPlan.create(patientId, fechaInicio, fechaFinEstimada, costoTotalEstimado);
        plan.activate();
        LocalDate newFechaInicio = LocalDate.now().plusDays(5);
        LocalDate newFechaFinEstimada = LocalDate.now().plusDays(40);

        IllegalStateException exception = assertThrows(IllegalStateException.class,
                () -> plan.updateDates(newFechaInicio, newFechaFinEstimada));

        assertEquals("No se pueden modificar las fechas de un plan ACTIVO o COMPLETADO", exception.getMessage());
    }

    @Test
    void updateDates_WithInvalidDateRange_ShouldThrowException() {
        TreatmentPlan plan = TreatmentPlan.create(patientId, fechaInicio, fechaFinEstimada, costoTotalEstimado);
        LocalDate invalidFechaInicio = LocalDate.now().plusDays(50);
        LocalDate invalidFechaFinEstimada = LocalDate.now().plusDays(40);

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                () -> plan.updateDates(invalidFechaInicio, invalidFechaFinEstimada));

        assertTrue(exception.getMessage().contains("La fecha de inicio no puede ser posterior a la fecha fin estimada"));
    }

    @Test
    void validate_WithFutureCreationDate_ShouldThrowException() {
        TreatmentPlanId id = new TreatmentPlanId(UUID.randomUUID());
        LocalDate futureDate = LocalDate.now().plusDays(1);

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                () -> new TreatmentPlan(id, patientId, futureDate, fechaInicio, fechaFinEstimada, PlanStatus.BORRADOR, costoTotalEstimado));

        assertTrue(exception.getMessage().contains("La fecha de creación no puede ser futura"));
    }

    @Test
    void constructor_WithNullPatientId_ShouldThrowException() {
        TreatmentPlanId id = new TreatmentPlanId(UUID.randomUUID());

        NullPointerException exception = assertThrows(NullPointerException.class,
                () -> new TreatmentPlan(id, null, LocalDate.now(), fechaInicio, fechaFinEstimada, PlanStatus.BORRADOR, costoTotalEstimado));

        assertEquals("El ID del paciente no puede ser nulo", exception.getMessage());
    }

    @Test
    void constructor_WithNullStatus_ShouldThrowException() {
        TreatmentPlanId id = new TreatmentPlanId(UUID.randomUUID());

        NullPointerException exception = assertThrows(NullPointerException.class,
                () -> new TreatmentPlan(id, patientId, LocalDate.now(), fechaInicio, fechaFinEstimada, null, costoTotalEstimado));

        assertEquals("El estado no puede ser nulo", exception.getMessage());
    }
}