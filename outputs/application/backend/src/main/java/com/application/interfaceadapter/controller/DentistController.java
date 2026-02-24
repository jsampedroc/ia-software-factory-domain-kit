package com.application.interfaceadapter.controller;

import com.application.application.dto.DentistDTO;
import com.application.application.service.DentistService;
import com.application.domain.valueobject.DentistId;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/dentists")
public class DentistController {

    private final DentistService dentistService;

    public DentistController(DentistService dentistService) {
        this.dentistService = dentistService;
    }

    @GetMapping
    public ResponseEntity<List<DentistDTO>> getAllDentists() {
        List<DentistDTO> dentists = dentistService.findAll();
        return ResponseEntity.ok(dentists);
    }

    @GetMapping("/active")
    public ResponseEntity<List<DentistDTO>> getActiveDentists() {
        List<DentistDTO> dentists = dentistService.findActiveDentists();
        return ResponseEntity.ok(dentists);
    }

    @GetMapping("/{id}")
    public ResponseEntity<DentistDTO> getDentistById(@PathVariable UUID id) {
        DentistId dentistId = new DentistId(id);
        return dentistService.findById(dentistId)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<DentistDTO> createDentist(@Valid @RequestBody DentistDTO dentistDTO) {
        DentistDTO createdDentist = dentistService.create(dentistDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdDentist);
    }

    @PutMapping("/{id}")
    public ResponseEntity<DentistDTO> updateDentist(
            @PathVariable UUID id,
            @Valid @RequestBody DentistDTO dentistDTO) {
        DentistId dentistId = new DentistId(id);
        DentistDTO updatedDentist = dentistService.update(dentistId, dentistDTO);
        return ResponseEntity.ok(updatedDentist);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteDentist(@PathVariable UUID id) {
        DentistId dentistId = new DentistId(id);
        dentistService.delete(dentistId);
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/{id}/activate")
    public ResponseEntity<DentistDTO> activateDentist(@PathVariable UUID id) {
        DentistId dentistId = new DentistId(id);
        DentistDTO activatedDentist = dentistService.activate(dentistId);
        return ResponseEntity.ok(activatedDentist);
    }

    @PatchMapping("/{id}/deactivate")
    public ResponseEntity<DentistDTO> deactivateDentist(@PathVariable UUID id) {
        DentistId dentistId = new DentistId(id);
        DentistDTO deactivatedDentist = dentistService.deactivate(dentistId);
        return ResponseEntity.ok(deactivatedDentist);
    }

    @GetMapping("/search")
    public ResponseEntity<List<DentistDTO>> searchDentists(
            @RequestParam(required = false) String license,
            @RequestParam(required = false) String name,
            @RequestParam(required = false) String specialty) {
        List<DentistDTO> dentists = dentistService.searchDentists(license, name, specialty);
        return ResponseEntity.ok(dentists);
    }

    @GetMapping("/clinic/{clinicId}")
    public ResponseEntity<List<DentistDTO>> getDentistsByClinic(@PathVariable UUID clinicId) {
        List<DentistDTO> dentists = dentistService.findDentistsByClinic(clinicId);
        return ResponseEntity.ok(dentists);
    }

    @GetMapping("/{id}/specialties")
    public ResponseEntity<List<String>> getDentistSpecialties(@PathVariable UUID id) {
        DentistId dentistId = new DentistId(id);
        List<String> specialties = dentistService.getDentistSpecialties(dentistId);
        return ResponseEntity.ok(specialties);
    }
}