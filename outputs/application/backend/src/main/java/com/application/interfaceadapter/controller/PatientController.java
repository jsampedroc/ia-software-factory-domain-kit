package com.application.interfaceadapter.controller;

import com.application.application.dto.PatientDTO;
import com.application.application.service.PatientService;
import com.application.domain.valueobject.PatientId;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/patients")
public class PatientController {

    private final PatientService patientService;

    public PatientController(PatientService patientService) {
        this.patientService = patientService;
    }

    @GetMapping
    public ResponseEntity<List<PatientDTO>> getAllPatients() {
        List<PatientDTO> patients = patientService.findAll();
        return ResponseEntity.ok(patients);
    }

    @GetMapping("/{id}")
    public ResponseEntity<PatientDTO> getPatientById(@PathVariable UUID id) {
        PatientId patientId = new PatientId(id);
        return patientService.findById(patientId)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<PatientDTO> createPatient(@RequestBody PatientDTO patientDTO) {
        PatientDTO createdPatient = patientService.create(patientDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdPatient);
    }

    @PutMapping("/{id}")
    public ResponseEntity<PatientDTO> updatePatient(@PathVariable UUID id, @RequestBody PatientDTO patientDTO) {
        PatientId patientId = new PatientId(id);
        PatientDTO updatedPatient = patientService.update(patientId, patientDTO);
        return ResponseEntity.ok(updatedPatient);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePatient(@PathVariable UUID id) {
        PatientId patientId = new PatientId(id);
        patientService.delete(patientId);
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/{id}/activate")
    public ResponseEntity<PatientDTO> activatePatient(@PathVariable UUID id) {
        PatientId patientId = new PatientId(id);
        PatientDTO activatedPatient = patientService.activate(patientId);
        return ResponseEntity.ok(activatedPatient);
    }

    @PatchMapping("/{id}/deactivate")
    public ResponseEntity<PatientDTO> deactivatePatient(@PathVariable UUID id) {
        PatientId patientId = new PatientId(id);
        PatientDTO deactivatedPatient = patientService.deactivate(patientId);
        return ResponseEntity.ok(deactivatedPatient);
    }

    @GetMapping("/search")
    public ResponseEntity<List<PatientDTO>> searchPatients(
            @RequestParam(required = false) String dni,
            @RequestParam(required = false) String nombre,
            @RequestParam(required = false) String apellido,
            @RequestParam(required = false) String email) {
        List<PatientDTO> patients = patientService.search(dni, nombre, apellido, email);
        return ResponseEntity.ok(patients);
    }

    @GetMapping("/{id}/appointments")
    public ResponseEntity<List<?>> getPatientAppointments(@PathVariable UUID id) {
        PatientId patientId = new PatientId(id);
        List<?> appointments = patientService.getAppointments(patientId);
        return ResponseEntity.ok(appointments);
    }

    @GetMapping("/{id}/invoices")
    public ResponseEntity<List<?>> getPatientInvoices(@PathVariable UUID id) {
        PatientId patientId = new PatientId(id);
        List<?> invoices = patientService.getInvoices(patientId);
        return ResponseEntity.ok(invoices);
    }

    @GetMapping("/{id}/treatment-plans")
    public ResponseEntity<List<?>> getPatientTreatmentPlans(@PathVariable UUID id) {
        PatientId patientId = new PatientId(id);
        List<?> treatmentPlans = patientService.getTreatmentPlans(patientId);
        return ResponseEntity.ok(treatmentPlans);
    }

    @GetMapping("/{id}/medical-history")
    public ResponseEntity<?> getPatientMedicalHistory(@PathVariable UUID id) {
        PatientId patientId = new PatientId(id);
        return patientService.getMedicalHistory(patientId)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
}