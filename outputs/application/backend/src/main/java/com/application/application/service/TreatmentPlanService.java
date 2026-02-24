package com.application.application.service;

import com.application.domain.model.TreatmentPlan;
import com.application.domain.valueobject.TreatmentPlanId;
import com.application.domain.valueobject.PlanStatus;
import com.application.domain.port.TreatmentPlanRepositoryPort;
import com.application.application.dto.TreatmentPlanDTO;
import com.application.application.mapper.TreatmentPlanMapper;
import com.application.domain.exception.DomainException;
import com.application.domain.model.Patient;
import com.application.domain.valueobject.PatientId;
import com.application.domain.port.PatientRepositoryPort;
import com.application.domain.model.Treatment;
import com.application.domain.valueobject.TreatmentId;
import com.application.domain.port.TreatmentRepositoryPort;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class TreatmentPlanService {

    private final TreatmentPlanRepositoryPort treatmentPlanRepositoryPort;
    private final PatientRepositoryPort patientRepositoryPort;
    private final TreatmentRepositoryPort treatmentRepositoryPort;
    private final TreatmentPlanMapper treatmentPlanMapper;

    @Transactional(readOnly = true)
    public TreatmentPlanDTO findById(TreatmentPlanId id) {
        log.debug("Finding treatment plan by id: {}", id);
        TreatmentPlan treatmentPlan = treatmentPlanRepositoryPort.findById(id)
                .orElseThrow(() -> new DomainException("Treatment plan not found with id: " + id));
        return treatmentPlanMapper.toDTO(treatmentPlan);
    }

    @Transactional(readOnly = true)
    public List<TreatmentPlanDTO> findAll() {
        log.debug("Finding all treatment plans");
        return treatmentPlanRepositoryPort.findAll().stream()
                .map(treatmentPlanMapper::toDTO)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<TreatmentPlanDTO> findByPatientId(PatientId patientId) {
        log.debug("Finding treatment plans for patient id: {}", patientId);
        return treatmentPlanRepositoryPort.findByPatientId(patientId).stream()
                .map(treatmentPlanMapper::toDTO)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<TreatmentPlanDTO> findByStatus(PlanStatus status) {
        log.debug("Finding treatment plans by status: {}", status);
        return treatmentPlanRepositoryPort.findByStatus(status).stream()
                .map(treatmentPlanMapper::toDTO)
                .collect(Collectors.toList());
    }

    @Transactional
    public TreatmentPlanDTO create(TreatmentPlanDTO dto) {
        log.debug("Creating new treatment plan");
        validatePatient(dto.getPatientId());
        validateTreatments(dto.getTreatmentIds());

        TreatmentPlan treatmentPlan = treatmentPlanMapper.toDomain(dto);
        treatmentPlan.validate();

        TreatmentPlan saved = treatmentPlanRepositoryPort.save(treatmentPlan);
        log.info("Treatment plan created with id: {}", saved.getId());
        return treatmentPlanMapper.toDTO(saved);
    }

    @Transactional
    public TreatmentPlanDTO update(TreatmentPlanId id, TreatmentPlanDTO dto) {
        log.debug("Updating treatment plan with id: {}", id);
        TreatmentPlan existing = treatmentPlanRepositoryPort.findById(id)
                .orElseThrow(() -> new DomainException("Treatment plan not found with id: " + id));

        if (!existing.getStatus().equals(PlanStatus.BORRADOR)) {
            throw new DomainException("Only treatment plans in DRAFT status can be modified");
        }

        validatePatient(dto.getPatientId());
        validateTreatments(dto.getTreatmentIds());

        TreatmentPlan updated = treatmentPlanMapper.toDomain(dto);
        updated.validate();

        TreatmentPlan saved = treatmentPlanRepositoryPort.save(updated);
        log.info("Treatment plan updated with id: {}", saved.getId());
        return treatmentPlanMapper.toDTO(saved);
    }

    @Transactional
    public void delete(TreatmentPlanId id) {
        log.debug("Deleting treatment plan with id: {}", id);
        TreatmentPlan existing = treatmentPlanRepositoryPort.findById(id)
                .orElseThrow(() -> new DomainException("Treatment plan not found with id: " + id));

        if (!existing.getStatus().equals(PlanStatus.BORRADOR)) {
            throw new DomainException("Only treatment plans in DRAFT status can be deleted");
        }

        treatmentPlanRepositoryPort.deleteById(id);
        log.info("Treatment plan deleted with id: {}", id);
    }

    @Transactional
    public TreatmentPlanDTO activatePlan(TreatmentPlanId id) {
        log.debug("Activating treatment plan with id: {}", id);
        TreatmentPlan plan = treatmentPlanRepositoryPort.findById(id)
                .orElseThrow(() -> new DomainException("Treatment plan not found with id: " + id));

        if (!plan.getStatus().equals(PlanStatus.BORRADOR)) {
            throw new DomainException("Only treatment plans in DRAFT status can be activated");
        }

        if (plan.getFechaInicio().isBefore(LocalDate.now())) {
            throw new DomainException("Start date must be today or in the future");
        }

        plan.activate();
        TreatmentPlan saved = treatmentPlanRepositoryPort.save(plan);
        log.info("Treatment plan activated with id: {}", saved.getId());
        return treatmentPlanMapper.toDTO(saved);
    }

    @Transactional
    public TreatmentPlanDTO completePlan(TreatmentPlanId id) {
        log.debug("Completing treatment plan with id: {}", id);
        TreatmentPlan plan = treatmentPlanRepositoryPort.findById(id)
                .orElseThrow(() -> new DomainException("Treatment plan not found with id: " + id));

        if (!plan.getStatus().equals(PlanStatus.ACTIVO)) {
            throw new DomainException("Only ACTIVE treatment plans can be completed");
        }

        plan.complete();
        TreatmentPlan saved = treatmentPlanRepositoryPort.save(plan);
        log.info("Treatment plan completed with id: {}", saved.getId());
        return treatmentPlanMapper.toDTO(saved);
    }

    @Transactional
    public TreatmentPlanDTO cancelPlan(TreatmentPlanId id, String reason) {
        log.debug("Canceling treatment plan with id: {}", id);
        TreatmentPlan plan = treatmentPlanRepositoryPort.findById(id)
                .orElseThrow(() -> new DomainException("Treatment plan not found with id: " + id));

        if (plan.getStatus().equals(PlanStatus.COMPLETADO)) {
            throw new DomainException("COMPLETED treatment plans cannot be canceled");
        }

        plan.cancel(reason);
        TreatmentPlan saved = treatmentPlanRepositoryPort.save(plan);
        log.info("Treatment plan canceled with id: {}", saved.getId());
        return treatmentPlanMapper.toDTO(saved);
    }

    @Transactional
    public TreatmentPlanDTO addTreatment(TreatmentPlanId planId, TreatmentId treatmentId) {
        log.debug("Adding treatment {} to plan {}", treatmentId, planId);
        TreatmentPlan plan = treatmentPlanRepositoryPort.findById(planId)
                .orElseThrow(() -> new DomainException("Treatment plan not found with id: " + planId));

        if (!plan.getStatus().equals(PlanStatus.BORRADOR)) {
            throw new DomainException("Treatments can only be added to DRAFT plans");
        }

        Treatment treatment = treatmentRepositoryPort.findById(treatmentId)
                .orElseThrow(() -> new DomainException("Treatment not found with id: " + treatmentId));

        if (!treatment.isActivo()) {
            throw new DomainException("Cannot add inactive treatment to plan");
        }

        plan.addTreatment(treatment);
        TreatmentPlan saved = treatmentPlanRepositoryPort.save(plan);
        log.info("Treatment added to plan {}", planId);
        return treatmentPlanMapper.toDTO(saved);
    }

    @Transactional
    public TreatmentPlanDTO removeTreatment(TreatmentPlanId planId, TreatmentId treatmentId) {
        log.debug("Removing treatment {} from plan {}", treatmentId, planId);
        TreatmentPlan plan = treatmentPlanRepositoryPort.findById(planId)
                .orElseThrow(() -> new DomainException("Treatment plan not found with id: " + planId));

        if (!plan.getStatus().equals(PlanStatus.BORRADOR)) {
            throw new DomainException("Treatments can only be removed from DRAFT plans");
        }

        plan.removeTreatment(treatmentId);
        TreatmentPlan saved = treatmentPlanRepositoryPort.save(plan);
        log.info("Treatment removed from plan {}", planId);
        return treatmentPlanMapper.toDTO(saved);
    }

    @Transactional(readOnly = true)
    public BigDecimal calculateTotalCost(TreatmentPlanId id) {
        log.debug("Calculating total cost for plan: {}", id);
        TreatmentPlan plan = treatmentPlanRepositoryPort.findById(id)
                .orElseThrow(() -> new DomainException("Treatment plan not found with id: " + id));
        return plan.calculateTotalCost();
    }

    private void validatePatient(PatientId patientId) {
        Patient patient = patientRepositoryPort.findById(patientId)
                .orElseThrow(() -> new DomainException("Patient not found with id: " + patientId));
        if (!patient.isActivo()) {
            throw new DomainException("Cannot create treatment plan for inactive patient");
        }
    }

    private void validateTreatments(Set<TreatmentId> treatmentIds) {
        if (treatmentIds == null || treatmentIds.isEmpty()) {
            throw new DomainException("Treatment plan must have at least one treatment");
        }

        for (TreatmentId treatmentId : treatmentIds) {
            Treatment treatment = treatmentRepositoryPort.findById(treatmentId)
                    .orElseThrow(() -> new DomainException("Treatment not found with id: " + treatmentId));
            if (!treatment.isActivo()) {
                throw new DomainException("Cannot include inactive treatment in plan: " + treatmentId);
            }
        }
    }
}