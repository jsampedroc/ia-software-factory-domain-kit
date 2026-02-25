package com.application.infrastructure.rest;

import com.application.application.service.PatientService;
import com.application.domain.model.Patient;
import com.application.domain.model.PatientIdentity;
import com.application.domain.model.MedicalAlert;
import com.application.domain.model.Allergy;
import com.application.domain.model.DigitalConsent;
import com.application.domain.valueobject.PatientId;
import com.application.domain.enums.PatientStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/patients")
@RequiredArgsConstructor
public class PatientController {

    private final PatientService patientService;

    @PostMapping
    public ResponseEntity<Patient> registerPatient(@RequestBody PatientIdentity identity) {
        Patient newPatient = patientService.registerPatient(identity);
        return ResponseEntity.status(HttpStatus.CREATED).body(newPatient);
    }

    @GetMapping("/{patientId}")
    public ResponseEntity<Patient> getPatient(@PathVariable UUID patientId) {
        PatientId id = new PatientId(patientId);
        return patientService.findPatientById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping
    public ResponseEntity<List<Patient>> getAllPatients(@RequestParam(required = false) PatientStatus status) {
        List<Patient> patients;
        if (status != null) {
            patients = patientService.findPatientsByStatus(status);
        } else {
            patients = patientService.findAllPatients();
        }
        return ResponseEntity.ok(patients);
    }

    @PutMapping("/{patientId}/identity")
    public ResponseEntity<Patient> updatePatientIdentity(@PathVariable UUID patientId,
                                                         @RequestBody PatientIdentity updatedIdentity) {
        PatientId id = new PatientId(patientId);
        Patient updatedPatient = patientService.updatePatientIdentity(id, updatedIdentity);
        return ResponseEntity.ok(updatedPatient);
    }

    @PatchMapping("/{patientId}/status")
    public ResponseEntity<Patient> updatePatientStatus(@PathVariable UUID patientId,
                                                       @RequestParam PatientStatus newStatus) {
        PatientId id = new PatientId(patientId);
        Patient updatedPatient = patientService.updatePatientStatus(id, newStatus);
        return ResponseEntity.ok(updatedPatient);
    }

    @PostMapping("/{patientId}/medical-alerts")
    public ResponseEntity<Patient> addMedicalAlert(@PathVariable UUID patientId,
                                                   @RequestBody MedicalAlert medicalAlert) {
        PatientId id = new PatientId(patientId);
        Patient updatedPatient = patientService.addMedicalAlert(id, medicalAlert);
        return ResponseEntity.ok(updatedPatient);
    }

    @DeleteMapping("/{patientId}/medical-alerts/{alertId}")
    public ResponseEntity<Patient> deactivateMedicalAlert(@PathVariable UUID patientId,
                                                          @PathVariable UUID alertId) {
        PatientId pid = new PatientId(patientId);
        Patient updatedPatient = patientService.deactivateMedicalAlert(pid, alertId);
        return ResponseEntity.ok(updatedPatient);
    }

    @PostMapping("/{patientId}/allergies")
    public ResponseEntity<Patient> addAllergy(@PathVariable UUID patientId,
                                              @RequestBody Allergy allergy) {
        PatientId id = new PatientId(patientId);
        Patient updatedPatient = patientService.addAllergy(id, allergy);
        return ResponseEntity.ok(updatedPatient);
    }

    @DeleteMapping("/{patientId}/allergies/{allergyId}")
    public ResponseEntity<Patient> removeAllergy(@PathVariable UUID patientId,
                                                 @PathVariable UUID allergyId) {
        PatientId pid = new PatientId(patientId);
        Patient updatedPatient = patientService.removeAllergy(pid, allergyId);
        return ResponseEntity.ok(updatedPatient);
    }

    @PostMapping("/{patientId}/consents")
    public ResponseEntity<Patient> recordConsent(@PathVariable UUID patientId,
                                                 @RequestBody DigitalConsent consent) {
        PatientId id = new PatientId(patientId);
        Patient updatedPatient = patientService.recordConsent(id, consent);
        return ResponseEntity.ok(updatedPatient);
    }

    @PatchMapping("/{patientId}/consents/{consentId}/revoke")
    public ResponseEntity<Patient> revokeConsent(@PathVariable UUID patientId,
                                                 @PathVariable UUID consentId) {
        PatientId pid = new PatientId(patientId);
        Patient updatedPatient = patientService.revokeConsent(pid, consentId);
        return ResponseEntity.ok(updatedPatient);
    }

    @GetMapping("/{patientId}/consents")
    public ResponseEntity<List<DigitalConsent>> getPatientConsents(@PathVariable UUID patientId,
                                                                   @RequestParam(required = false) Boolean activeOnly) {
        PatientId id = new PatientId(patientId);
        List<DigitalConsent> consents;
        if (activeOnly != null && activeOnly) {
            consents = patientService.getActiveConsents(id);
        } else {
            consents = patientService.getAllConsents(id);
        }
        return ResponseEntity.ok(consents);
    }

    @GetMapping("/search")
    public ResponseEntity<List<Patient>> searchPatients(@RequestParam(required = false) String firstName,
                                                        @RequestParam(required = false) String lastName,
                                                        @RequestParam(required = false) String nationalId) {
        List<Patient> patients = patientService.searchPatients(firstName, lastName, nationalId);
        return ResponseEntity.ok(patients);
    }
}