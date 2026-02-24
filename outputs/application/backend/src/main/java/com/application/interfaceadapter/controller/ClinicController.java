package com.application.interfaceadapter.controller;

import com.application.application.dto.ClinicDTO;
import com.application.application.service.ClinicService;
import com.application.domain.valueobject.ClinicId;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/clinics")
public class ClinicController {

    private final ClinicService clinicService;

    public ClinicController(ClinicService clinicService) {
        this.clinicService = clinicService;
    }

    @GetMapping
    public ResponseEntity<List<ClinicDTO>> getAllClinics() {
        List<ClinicDTO> clinics = clinicService.findAll();
        return ResponseEntity.ok(clinics);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ClinicDTO> getClinicById(@PathVariable UUID id) {
        ClinicId clinicId = new ClinicId(id);
        return clinicService.findById(clinicId)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<ClinicDTO> createClinic(@RequestBody ClinicDTO clinicDTO) {
        ClinicDTO createdClinic = clinicService.create(clinicDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdClinic);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ClinicDTO> updateClinic(@PathVariable UUID id, @RequestBody ClinicDTO clinicDTO) {
        ClinicId clinicId = new ClinicId(id);
        ClinicDTO updatedClinic = clinicService.update(clinicId, clinicDTO);
        return ResponseEntity.ok(updatedClinic);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteClinic(@PathVariable UUID id) {
        ClinicId clinicId = new ClinicId(id);
        clinicService.delete(clinicId);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/active")
    public ResponseEntity<List<ClinicDTO>> getActiveClinics() {
        List<ClinicDTO> activeClinics = clinicService.findActiveClinics();
        return ResponseEntity.ok(activeClinics);
    }

    @PatchMapping("/{id}/activate")
    public ResponseEntity<ClinicDTO> activateClinic(@PathVariable UUID id) {
        ClinicId clinicId = new ClinicId(id);
        ClinicDTO activatedClinic = clinicService.activate(clinicId);
        return ResponseEntity.ok(activatedClinic);
    }

    @PatchMapping("/{id}/deactivate")
    public ResponseEntity<ClinicDTO> deactivateClinic(@PathVariable UUID id) {
        ClinicId clinicId = new ClinicId(id);
        ClinicDTO deactivatedClinic = clinicService.deactivate(clinicId);
        return ResponseEntity.ok(deactivatedClinic);
    }
}