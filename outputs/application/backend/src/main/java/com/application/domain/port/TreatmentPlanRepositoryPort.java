package com.application.domain.port;

import com.application.domain.model.TreatmentPlan;
import com.application.domain.valueobject.TreatmentPlanId;
import com.application.domain.valueobject.PatientId;
import com.application.domain.valueobject.PlanStatus;
import com.application.domain.shared.EntityRepository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface TreatmentPlanRepositoryPort extends EntityRepository<TreatmentPlan, TreatmentPlanId> {

    List<TreatmentPlan> findByPatientId(PatientId patientId);

    List<TreatmentPlan> findByStatus(PlanStatus status);

    List<TreatmentPlan> findByPatientIdAndStatus(PatientId patientId, PlanStatus status);

    List<TreatmentPlan> findActivePlansByDateRange(LocalDate startDate, LocalDate endDate);

    Optional<TreatmentPlan> findActivePlanByPatient(PatientId patientId);

    boolean existsActivePlanForPatient(PatientId patientId);
}