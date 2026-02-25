package com.application.infrastructure.rest;

import com.application.application.service.EhrService;
import com.application.domain.model.ElectronicHealthRecord;
import com.application.domain.model.ClinicalNote;
import com.application.domain.model.TreatmentRecord;
import com.application.domain.model.Odontogram;
import com.application.domain.valueobject.EhrId;
import com.application.domain.valueobject.PatientId;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/ehr")
@RequiredArgsConstructor
public class EhrController {

    private final EhrService ehrService;

    @GetMapping("/patient/{patientId}")
    public ResponseEntity<ElectronicHealthRecord> getEhrByPatientId(@PathVariable UUID patientId) {
        PatientId id = new PatientId(patientId);
        ElectronicHealthRecord ehr = ehrService.findEhrByPatientId(id);
        return ResponseEntity.ok(ehr);
    }

    @GetMapping("/{ehrId}")
    public ResponseEntity<ElectronicHealthRecord> getEhrById(@PathVariable UUID ehrId) {
        EhrId id = new EhrId(ehrId);
        ElectronicHealthRecord ehr = ehrService.findEhrById(id);
        return ResponseEntity.ok(ehr);
    }

    @PostMapping("/{ehrId}/clinical-notes")
    public ResponseEntity<ClinicalNote> addClinicalNote(@PathVariable UUID ehrId,
                                                        @RequestBody ClinicalNote clinicalNote) {
        EhrId id = new EhrId(ehrId);
        ClinicalNote savedNote = ehrService.addClinicalNote(id, clinicalNote);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedNote);
    }

    @GetMapping("/{ehrId}/clinical-notes")
    public ResponseEntity<List<ClinicalNote>> getClinicalNotes(@PathVariable UUID ehrId) {
        EhrId id = new EhrId(ehrId);
        List<ClinicalNote> notes = ehrService.getClinicalNotes(id);
        return ResponseEntity.ok(notes);
    }

    @PostMapping("/{ehrId}/treatment-records")
    public ResponseEntity<TreatmentRecord> addTreatmentRecord(@PathVariable UUID ehrId,
                                                              @RequestBody TreatmentRecord treatmentRecord) {
        EhrId id = new EhrId(ehrId);
        TreatmentRecord savedRecord = ehrService.addTreatmentRecord(id, treatmentRecord);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedRecord);
    }

    @GetMapping("/{ehrId}/treatment-records")
    public ResponseEntity<List<TreatmentRecord>> getTreatmentRecords(@PathVariable UUID ehrId) {
        EhrId id = new EhrId(ehrId);
        List<TreatmentRecord> records = ehrService.getTreatmentRecords(id);
        return ResponseEntity.ok(records);
    }

    @GetMapping("/{ehrId}/odontogram")
    public ResponseEntity<Odontogram> getOdontogram(@PathVariable UUID ehrId) {
        EhrId id = new EhrId(ehrId);
        Odontogram odontogram = ehrService.getOdontogram(id);
        return ResponseEntity.ok(odontogram);
    }

    @PutMapping("/{ehrId}/odontogram")
    public ResponseEntity<Odontogram> updateOdontogram(@PathVariable UUID ehrId,
                                                       @RequestBody Odontogram odontogram) {
        EhrId id = new EhrId(ehrId);
        Odontogram updatedOdontogram = ehrService.updateOdontogram(id, odontogram);
        return ResponseEntity.ok(updatedOdontogram);
    }

    @GetMapping("/{ehrId}/treatment-history")
    public ResponseEntity<List<TreatmentRecord>> getTreatmentHistory(@PathVariable UUID ehrId) {
        EhrId id = new EhrId(ehrId);
        List<TreatmentRecord> history = ehrService.getTreatmentHistory(id);
        return ResponseEntity.ok(history);
    }
}