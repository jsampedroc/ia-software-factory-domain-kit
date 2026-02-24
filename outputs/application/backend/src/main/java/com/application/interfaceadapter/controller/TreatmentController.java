package com.application.interfaceadapter.controller;

import com.application.application.dto.TreatmentDTO;
import com.application.application.service.TreatmentService;
import com.application.domain.valueobject.TreatmentId;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/treatments")
public class TreatmentController {

    private final TreatmentService treatmentService;

    public TreatmentController(TreatmentService treatmentService) {
        this.treatmentService = treatmentService;
    }

    @GetMapping
    public ResponseEntity<List<TreatmentDTO>> getAllTreatments() {
        List<TreatmentDTO> treatments = treatmentService.findAll();
        return ResponseEntity.ok(treatments);
    }

    @GetMapping("/{id}")
    public ResponseEntity<TreatmentDTO> getTreatmentById(@PathVariable UUID id) {
        TreatmentId treatmentId = new TreatmentId(id);
        return treatmentService.findById(treatmentId)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/active")
    public ResponseEntity<List<TreatmentDTO>> getActiveTreatments() {
        List<TreatmentDTO> activeTreatments = treatmentService.findActiveTreatments();
        return ResponseEntity.ok(activeTreatments);
    }

    @PostMapping
    public ResponseEntity<TreatmentDTO> createTreatment(@RequestBody TreatmentDTO treatmentDTO) {
        TreatmentDTO createdTreatment = treatmentService.create(treatmentDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdTreatment);
    }

    @PutMapping("/{id}")
    public ResponseEntity<TreatmentDTO> updateTreatment(
            @PathVariable UUID id,
            @RequestBody TreatmentDTO treatmentDTO) {
        TreatmentId treatmentId = new TreatmentId(id);
        TreatmentDTO updatedTreatment = treatmentService.update(treatmentId, treatmentDTO);
        return ResponseEntity.ok(updatedTreatment);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTreatment(@PathVariable UUID id) {
        TreatmentId treatmentId = new TreatmentId(id);
        treatmentService.delete(treatmentId);
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/{id}/activate")
    public ResponseEntity<TreatmentDTO> activateTreatment(@PathVariable UUID id) {
        TreatmentId treatmentId = new TreatmentId(id);
        TreatmentDTO activatedTreatment = treatmentService.activate(treatmentId);
        return ResponseEntity.ok(activatedTreatment);
    }

    @PatchMapping("/{id}/deactivate")
    public ResponseEntity<TreatmentDTO> deactivateTreatment(@PathVariable UUID id) {
        TreatmentId treatmentId = new TreatmentId(id);
        TreatmentDTO deactivatedTreatment = treatmentService.deactivate(treatmentId);
        return ResponseEntity.ok(deactivatedTreatment);
    }

    @GetMapping("/search")
    public ResponseEntity<List<TreatmentDTO>> searchTreatments(
            @RequestParam(required = false) String code,
            @RequestParam(required = false) String name) {
        List<TreatmentDTO> treatments = treatmentService.searchTreatments(code, name);
        return ResponseEntity.ok(treatments);
    }
}