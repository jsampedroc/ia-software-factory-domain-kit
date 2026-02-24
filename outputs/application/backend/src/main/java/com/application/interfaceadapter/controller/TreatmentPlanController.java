package com.application.interfaceadapter.controller;

import com.application.application.dto.TreatmentPlanDTO;
import com.application.application.service.TreatmentPlanService;
import com.application.domain.valueobject.PatientId;
import com.application.domain.valueobject.PlanStatus;
import com.application.domain.valueobject.TreatmentPlanId;
import com.application.domain.valueobject.TreatmentId;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/treatment-plans")
public class TreatmentPlanController {

    private final TreatmentPlanService treatmentPlanService;

    public TreatmentPlanController(TreatmentPlanService treatmentPlanService) {
        this.treatmentPlanService = treatmentPlanService;
    }

    @GetMapping
    public ResponseEntity<List<TreatmentPlanDTO>> getAllTreatmentPlans() {
        List<TreatmentPlanDTO> treatmentPlans = treatmentPlanService.findAll();
        return ResponseEntity.ok(treatmentPlans);
    }

    @GetMapping("/{id}")
    public ResponseEntity<TreatmentPlanDTO> getTreatmentPlanById(@PathVariable UUID id) {
        TreatmentPlanId treatmentPlanId = new TreatmentPlanId(id);
        return treatmentPlanService.findById(treatmentPlanId)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/patient/{patientId}")
    public ResponseEntity<List<TreatmentPlanDTO>> getTreatmentPlansByPatient(@PathVariable UUID patientId) {
        PatientId id = new PatientId(patientId);
        List<TreatmentPlanDTO> treatmentPlans = treatmentPlanService.findByPatientId(id);
        return ResponseEntity.ok(treatmentPlans);
    }

    @GetMapping("/status/{status}")
    public ResponseEntity<List<TreatmentPlanDTO>> getTreatmentPlansByStatus(@PathVariable String status) {
        PlanStatus planStatus = PlanStatus.valueOf(status.toUpperCase());
        List<TreatmentPlanDTO> treatmentPlans = treatmentPlanService.findByStatus(planStatus);
        return ResponseEntity.ok(treatmentPlans);
    }

    @PostMapping
    public ResponseEntity<TreatmentPlanDTO> createTreatmentPlan(@RequestBody TreatmentPlanDTO treatmentPlanDTO) {
        TreatmentPlanDTO createdTreatmentPlan = treatmentPlanService.create(treatmentPlanDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdTreatmentPlan);
    }

    @PutMapping("/{id}")
    public ResponseEntity<TreatmentPlanDTO> updateTreatmentPlan(
            @PathVariable UUID id,
            @RequestBody TreatmentPlanDTO treatmentPlanDTO) {
        TreatmentPlanId treatmentPlanId = new TreatmentPlanId(id);
        TreatmentPlanDTO updatedTreatmentPlan = treatmentPlanService.update(treatmentPlanId, treatmentPlanDTO);
        return ResponseEntity.ok(updatedTreatmentPlan);
    }

    @PatchMapping("/{id}/status")
    public ResponseEntity<TreatmentPlanDTO> updateTreatmentPlanStatus(
            @PathVariable UUID id,
            @RequestParam String status) {
        TreatmentPlanId treatmentPlanId = new TreatmentPlanId(id);
        PlanStatus planStatus = PlanStatus.valueOf(status.toUpperCase());
        TreatmentPlanDTO updatedTreatmentPlan = treatmentPlanService.updateStatus(treatmentPlanId, planStatus);
        return ResponseEntity.ok(updatedTreatmentPlan);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTreatmentPlan(@PathVariable UUID id) {
        TreatmentPlanId treatmentPlanId = new TreatmentPlanId(id);
        treatmentPlanService.delete(treatmentPlanId);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/{planId}/treatments/{treatmentId}")
    public ResponseEntity<TreatmentPlanDTO> addTreatmentToPlan(
            @PathVariable UUID planId,
            @PathVariable UUID treatmentId) {
        TreatmentPlanId treatmentPlanId = new TreatmentPlanId(planId);
        TreatmentId id = new TreatmentId(treatmentId);
        TreatmentPlanDTO updatedPlan = treatmentPlanService.addTreatment(treatmentPlanId, id);
        return ResponseEntity.ok(updatedPlan);
    }

    @DeleteMapping("/{planId}/treatments/{treatmentId}")
    public ResponseEntity<TreatmentPlanDTO> removeTreatmentFromPlan(
            @PathVariable UUID planId,
            @PathVariable UUID treatmentId) {
        TreatmentPlanId treatmentPlanId = new TreatmentPlanId(planId);
        TreatmentId id = new TreatmentId(treatmentId);
        TreatmentPlanDTO updatedPlan = treatmentPlanService.removeTreatment(treatmentPlanId, id);
        return ResponseEntity.ok(updatedPlan);
    }
}