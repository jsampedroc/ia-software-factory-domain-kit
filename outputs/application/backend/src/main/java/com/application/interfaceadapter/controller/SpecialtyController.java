package com.application.interfaceadapter.controller;

import com.application.application.dto.SpecialtyDTO;
import com.application.application.service.SpecialtyService;
import com.application.domain.valueobject.SpecialtyId;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/specialties")
public class SpecialtyController {

    private final SpecialtyService specialtyService;

    public SpecialtyController(SpecialtyService specialtyService) {
        this.specialtyService = specialtyService;
    }

    @GetMapping
    public ResponseEntity<List<SpecialtyDTO>> getAllSpecialties() {
        List<SpecialtyDTO> specialties = specialtyService.findAll();
        return ResponseEntity.ok(specialties);
    }

    @GetMapping("/{id}")
    public ResponseEntity<SpecialtyDTO> getSpecialtyById(@PathVariable UUID id) {
        SpecialtyId specialtyId = new SpecialtyId(id);
        return specialtyService.findById(specialtyId)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<SpecialtyDTO> createSpecialty(@RequestBody SpecialtyDTO specialtyDTO) {
        SpecialtyDTO createdSpecialty = specialtyService.create(specialtyDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdSpecialty);
    }

    @PutMapping("/{id}")
    public ResponseEntity<SpecialtyDTO> updateSpecialty(
            @PathVariable UUID id,
            @RequestBody SpecialtyDTO specialtyDTO) {
        SpecialtyId specialtyId = new SpecialtyId(id);
        SpecialtyDTO updatedSpecialty = specialtyService.update(specialtyId, specialtyDTO);
        return ResponseEntity.ok(updatedSpecialty);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteSpecialty(@PathVariable UUID id) {
        SpecialtyId specialtyId = new SpecialtyId(id);
        specialtyService.delete(specialtyId);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/active")
    public ResponseEntity<List<SpecialtyDTO>> getActiveSpecialties() {
        List<SpecialtyDTO> activeSpecialties = specialtyService.findActiveSpecialties();
        return ResponseEntity.ok(activeSpecialties);
    }

    @GetMapping("/search")
    public ResponseEntity<List<SpecialtyDTO>> searchSpecialties(
            @RequestParam(required = false) String code,
            @RequestParam(required = false) String name) {
        List<SpecialtyDTO> specialties = specialtyService.searchByCriteria(code, name);
        return ResponseEntity.ok(specialties);
    }
}