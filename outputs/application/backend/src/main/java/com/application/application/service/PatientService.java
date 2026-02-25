package com.application.application.service;

import com.application.domain.model.Patient;
import com.application.domain.model.PatientIdentity;
import com.application.domain.model.MedicalAlert;
import com.application.domain.model.Allergy;
import com.application.domain.model.DigitalConsent;
import com.application.domain.enums.PatientStatus;
import com.application.domain.enums.ConsentType;
import com.application.domain.valueobject.PatientId;
import com.application.domain.repository.PatientRepository;
import com.application.domain.repository.ElectronicHealthRecordRepository;
import com.application.domain.model.ElectronicHealthRecord;
import com.application.domain.valueobject.EhrId;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class PatientService {

    private final PatientRepository patientRepository;
    private final ElectronicHealthRecordRepository ehrRepository;

    @Transactional
    public Patient registerPatient(PatientIdentity identity,
                                   Set<MedicalAlert> medicalAlerts,
                                   Set<Allergy> allergies,
                                   List<DigitalConsent> initialConsents) {

        // Business Rule 1: Unique identity check
        Optional<Patient> existingPatient = patientRepository.findByIdentity(
                identity.getFirstName(),
                identity.getLastName(),
                identity.getDateOfBirth()
        );
        if (existingPatient.isPresent()) {
            throw new IllegalArgumentException("A patient with the same first name, last name, and date of birth already exists.");
        }

        // Create new patient aggregate
        PatientId patientId = PatientId.of(UUID.randomUUID());
        Patient newPatient = Patient.builder()
                .patientId(patientId)
                .identity(identity)
                .medicalAlerts(medicalAlerts != null ? medicalAlerts : new HashSet<>())
                .allergies(allergies != null ? allergies : new HashSet<>())
                .consents(initialConsents != null ? initialConsents : List.of())
                .status(PatientStatus.ACTIVE)
                .build();

        // Business Rule: EHR is created automatically on patient registration
        createElectronicHealthRecordForPatient(newPatient);

        Patient savedPatient = patientRepository.save(newPatient);
        log.info("Registered new patient with ID: {}", savedPatient.getPatientId().getValue());
        return savedPatient;
    }

    @Transactional(readOnly = true)
    public Patient getPatient(PatientId patientId) {
        return patientRepository.findById(patientId)
                .orElseThrow(() -> new IllegalArgumentException("Patient not found with ID: " + patientId));
    }

    @Transactional
    public Patient updatePatientIdentity(PatientId patientId, PatientIdentity updatedIdentity) {
        Patient patient = getPatient(patientId);

        // Business Rule: Cannot update archived patient
        if (patient.getStatus() == PatientStatus.ARCHIVED) {
            throw new IllegalStateException("Cannot update identity of an archived patient.");
        }

        // Check uniqueness again if identity fields changed
        if (!patient.getIdentity().equals(updatedIdentity)) {
            Optional<Patient> duplicate = patientRepository.findByIdentity(
                    updatedIdentity.getFirstName(),
                    updatedIdentity.getLastName(),
                    updatedIdentity.getDateOfBirth()
            );
            if (duplicate.isPresent() && !duplicate.get().getPatientId().equals(patientId)) {
                throw new IllegalArgumentException("Another patient with the same first name, last name, and date of birth already exists.");
            }
        }

        patient.updateIdentity(updatedIdentity);
        return patientRepository.save(patient);
    }

    @Transactional
    public Patient addMedicalAlert(PatientId patientId, MedicalAlert alert) {
        Patient patient = getPatient(patientId);
        patient.addMedicalAlert(alert);
        log.info("Added medical alert '{}' to patient ID: {}", alert.getCode(), patientId);
        return patientRepository.save(patient);
    }

    @Transactional
    public Patient deactivateMedicalAlert(PatientId patientId, UUID alertId) {
        Patient patient = getPatient(patientId);
        patient.deactivateMedicalAlert(alertId);
        log.info("Deactivated medical alert ID: {} for patient ID: {}", alertId, patientId);
        return patientRepository.save(patient);
    }

    @Transactional
    public Patient addAllergy(PatientId patientId, Allergy allergy) {
        Patient patient = getPatient(patientId);
        patient.addAllergy(allergy);
        log.info("Added allergy '{}' to patient ID: {}", allergy.getSubstance(), patientId);
        return patientRepository.save(patient);
    }

    @Transactional
    public Patient removeAllergy(PatientId patientId, UUID allergyId) {
        Patient patient = getPatient(patientId);
        patient.removeAllergy(allergyId);
        log.info("Removed allergy ID: {} from patient ID: {}", allergyId, patientId);
        return patientRepository.save(patient);
    }

    @Transactional
    public DigitalConsent giveConsent(PatientId patientId,
                                      ConsentType consentType,
                                      String version,
                                      String content,
                                      String givenBy) {
        Patient patient = getPatient(patientId);

        // Business Rule: Cannot give consent if patient is archived
        if (patient.getStatus() == PatientStatus.ARCHIVED) {
            throw new IllegalStateException("Archived patient cannot give consent.");
        }

        DigitalConsent consent = DigitalConsent.builder()
                .consentId(UUID.randomUUID())
                .consentType(consentType)
                .version(version)
                .content(content)
                .givenBy(givenBy)
                .givenAt(LocalDateTime.now())
                .isRevoked(false)
                .build();

        patient.addConsent(consent);
        patientRepository.save(patient);
        log.info("Patient ID: {} gave consent type: {} version: {}", patientId, consentType, version);
        return consent;
    }

    @Transactional
    public void revokeConsent(PatientId patientId, UUID consentId) {
        Patient patient = getPatient(patientId);
        patient.revokeConsent(consentId, LocalDateTime.now());
        patientRepository.save(patient);
        log.info("Patient ID: {} revoked consent ID: {}", patientId, consentId);
    }

    @Transactional
    public Patient archivePatient(PatientId patientId) {
        Patient patient = getPatient(patientId);

        // Business Rule 2: Patients are archived, not deleted
        if (patient.getStatus() == PatientStatus.ARCHIVED) {
            throw new IllegalStateException("Patient is already archived.");
        }

        // Additional business rule: Check for pending appointments or unpaid invoices?
        // This would require integration with Scheduling and Billing services.
        // For now, we just archive.

        patient.archive();
        Patient archivedPatient = patientRepository.save(patient);
        log.info("Archived patient with ID: {}", patientId);
        return archivedPatient;
    }

    @Transactional
    public Patient reactivatePatient(PatientId patientId) {
        Patient patient = getPatient(patientId);
        if (patient.getStatus() != PatientStatus.ARCHIVED) {
            throw new IllegalStateException("Only archived patients can be reactivated.");
        }
        patient.reactivate();
        Patient reactivatedPatient = patientRepository.save(patient);
        log.info("Reactivated patient with ID: {}", patientId);
        return reactivatedPatient;
    }

    @Transactional(readOnly = true)
    public List<Patient> findPatientsByStatus(PatientStatus status) {
        return patientRepository.findByStatus(status);
    }

    @Transactional(readOnly = true)
    public boolean hasActiveMedicalAlerts(PatientId patientId) {
        Patient patient = getPatient(patientId);
        return patient.getMedicalAlerts().stream()
                .anyMatch(MedicalAlert::isActive);
    }

    @Transactional(readOnly = true)
    public boolean hasValidTreatmentConsent(PatientId patientId) {
        Patient patient = getPatient(patientId);
        return patient.getConsents().stream()
                .filter(c -> c.getConsentType() == ConsentType.TREATMENT)
                .filter(c -> !c.isRevoked())
                .findFirst()
                .isPresent();
    }

    // Private helper method
    private void createElectronicHealthRecordForPatient(Patient patient) {
        EhrId ehrId = EhrId.of(UUID.randomUUID());
        ElectronicHealthRecord ehr = ElectronicHealthRecord.builder()
                .ehrId(ehrId)
                .patientId(patient.getPatientId())
                .clinicalNotes(List.of())
                .odontogram(null) // Initial odontogram is created on first dental visit
                .treatments(List.of())
                .createdAt(LocalDateTime.now())
                .lastUpdated(LocalDateTime.now())
                .build();
        ehrRepository.save(ehr);
        log.debug("Created initial EHR ID: {} for patient ID: {}", ehrId, patient.getPatientId());
    }
}