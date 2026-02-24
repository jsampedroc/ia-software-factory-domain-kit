package com.application.interfaceadapter.controller;

import com.application.application.dto.MedicalHistoryDTO;
import com.application.application.service.MedicalHistoryService;
import com.application.domain.valueobject.MedicalHistoryId;
import com.application.domain.valueobject.PatientId;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/medical-histories")
public class MedicalHistoryController {

    private final MedicalHistoryService medicalHistoryService;

    public MedicalHistoryController(MedicalHistoryService medicalHistoryService) {
        this.medicalHistoryService = medicalHistoryService;
    }

    @GetMapping("/{id}")
    public ResponseEntity<MedicalHistoryDTO> getById(@PathVariable UUID id) {
        MedicalHistoryId historyId = new MedicalHistoryId(id);
        return medicalHistoryService.findById(historyId)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/patient/{patientId}")
    public ResponseEntity<MedicalHistoryDTO> getByPatientId(@PathVariable UUID patientId) {
        PatientId pid = new PatientId(patientId);
        return medicalHistoryService.findByPatientId(pid)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping
    public ResponseEntity<List<MedicalHistoryDTO>> getAll() {
        List<MedicalHistoryDTO> histories = medicalHistoryService.findAll();
        return ResponseEntity.ok(histories);
    }

    @PostMapping
    public ResponseEntity<MedicalHistoryDTO> create(@RequestBody MedicalHistoryDTO medicalHistoryDTO) {
        MedicalHistoryDTO created = medicalHistoryService.create(medicalHistoryDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    @PutMapping("/{id}")
    public ResponseEntity<MedicalHistoryDTO> update(@PathVariable UUID id, @RequestBody MedicalHistoryDTO medicalHistoryDTO) {
        MedicalHistoryId historyId = new MedicalHistoryId(id);
        MedicalHistoryDTO updated = medicalHistoryService.update(historyId, medicalHistoryDTO);
        return ResponseEntity.ok(updated);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable UUID id) {
        MedicalHistoryId historyId = new MedicalHistoryId(id);
        medicalHistoryService.delete(historyId);
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/{id}/allergies")
    public ResponseEntity<MedicalHistoryDTO> updateAllergies(@PathVariable UUID id, @RequestBody String allergies) {
        MedicalHistoryId historyId = new MedicalHistoryId(id);
        MedicalHistoryDTO updated = medicalHistoryService.updateAllergies(historyId, allergies);
        return ResponseEntity.ok(updated);
    }

    @PatchMapping("/{id}/conditions")
    public ResponseEntity<MedicalHistoryDTO> updateMedicalConditions(@PathVariable UUID id, @RequestBody String conditions) {
        MedicalHistoryId historyId = new MedicalHistoryId(id);
        MedicalHistoryDTO updated = medicalHistoryService.updateMedicalConditions(historyId, conditions);
        return ResponseEntity.ok(updated);
    }

    @PatchMapping("/{id}/medications")
    public ResponseEntity<MedicalHistoryDTO> updateMedications(@PathVariable UUID id, @RequestBody String medications) {
        MedicalHistoryId historyId = new MedicalHistoryId(id);
        MedicalHistoryDTO updated = medicalHistoryService.updateMedications(historyId, medications);
        return ResponseEntity.ok(updated);
    }

    @PatchMapping("/{id}/observations")
    public ResponseEntity<MedicalHistoryDTO> updateObservations(@PathVariable UUID id, @RequestBody String observations) {
        MedicalHistoryId historyId = new MedicalHistoryId(id);
        MedicalHistoryDTO updated = medicalHistoryService.updateObservations(historyId, observations);
        return ResponseEntity.ok(updated);
    }
}