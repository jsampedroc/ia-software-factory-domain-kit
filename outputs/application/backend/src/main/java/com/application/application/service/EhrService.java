package com.application.application.service;

import com.application.domain.model.ElectronicHealthRecord;
import com.application.domain.model.ClinicalNote;
import com.application.domain.model.TreatmentRecord;
import com.application.domain.model.Odontogram;
import com.application.domain.model.ToothCondition;
import com.application.domain.repository.ElectronicHealthRecordRepository;
import com.application.domain.repository.PatientRepository;
import com.application.domain.repository.UserRepository;
import com.application.domain.repository.AppointmentRepository;
import com.application.domain.valueobject.EhrId;
import com.application.domain.valueobject.PatientId;
import com.application.domain.valueobject.ClinicalNoteId;
import com.application.domain.valueobject.TreatmentRecordId;
import com.application.domain.valueobject.UserId;
import com.application.domain.valueobject.AppointmentId;
import com.application.domain.enums.UserRole;
import com.application.domain.enums.ToothStatus;
import com.application.domain.shared.exceptions.DomainException;
import com.application.domain.shared.exceptions.NotFoundException;
import com.application.domain.shared.exceptions.ValidationException;
import com.application.domain.shared.exceptions.AuthorizationException;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

public class EhrService {

    private final ElectronicHealthRecordRepository ehrRepository;
    private final PatientRepository patientRepository;
    private final UserRepository userRepository;
    private final AppointmentRepository appointmentRepository;

    public EhrService(ElectronicHealthRecordRepository ehrRepository,
                      PatientRepository patientRepository,
                      UserRepository userRepository,
                      AppointmentRepository appointmentRepository) {
        this.ehrRepository = ehrRepository;
        this.patientRepository = patientRepository;
        this.userRepository = userRepository;
        this.appointmentRepository = appointmentRepository;
    }

    public ElectronicHealthRecord getEhrByPatientId(PatientId patientId, UserId requesterId) {
        validateUserIsDentistOrAdmin(requesterId);
        validatePatientExists(patientId);

        return ehrRepository.findByPatientId(patientId)
                .orElseThrow(() -> new NotFoundException("EHR not found for patient ID: " + patientId.getValue()));
    }

    public ClinicalNote addClinicalNote(PatientId patientId, UserId dentistId, String content, LocalDate date, AppointmentId appointmentId) {
        validateUserIsDentist(dentistId);
        validatePatientExists(patientId);
        validateDentistHasCurrentAppointment(dentistId, patientId, appointmentId);

        ElectronicHealthRecord ehr = ehrRepository.findByPatientId(patientId)
                .orElseThrow(() -> new NotFoundException("EHR not found for patient ID: " + patientId.getValue()));

        ClinicalNote note = ClinicalNote.builder()
                .noteId(ClinicalNoteId.newId())
                .dentistId(dentistId.getValue())
                .content(content)
                .date(date)
                .createdAt(LocalDateTime.now())
                .build();

        ehr.addClinicalNote(note);
        ehrRepository.save(ehr);
        return note;
    }

    public ClinicalNote appendToClinicalNote(ClinicalNoteId noteId, UserId dentistId, String additionalContent) {
        validateUserIsDentist(dentistId);
        ElectronicHealthRecord ehr = findEhrContainingNote(noteId);

        ClinicalNote originalNote = ehr.getClinicalNotes().stream()
                .filter(n -> n.getNoteId().equals(noteId))
                .findFirst()
                .orElseThrow(() -> new NotFoundException("Clinical note not found: " + noteId.getValue()));

        if (!originalNote.getDentistId().equals(dentistId.getValue())) {
            throw new AuthorizationException("Only the original author can append to a clinical note.");
        }

        String updatedContent = originalNote.getContent() + "\n--- Correction/Addition [" + LocalDateTime.now() + "] ---\n" + additionalContent;
        ClinicalNote updatedNote = ClinicalNote.builder()
                .noteId(ClinicalNoteId.newId())
                .dentistId(dentistId.getValue())
                .content(updatedContent)
                .date(originalNote.getDate())
                .createdAt(LocalDateTime.now())
                .build();

        ehr.addClinicalNote(updatedNote);
        ehrRepository.save(ehr);
        return updatedNote;
    }

    public TreatmentRecord recordTreatment(PatientId patientId, UserId dentistId, String treatmentCode,
                                           String description, Set<Integer> toothNumbers, String notes,
                                           BigDecimal cost, AppointmentId appointmentId, boolean requiresDualVerification) {
        validateUserIsDentist(dentistId);
        validatePatientExists(patientId);
        validateDentistHasCurrentAppointment(dentistId, patientId, appointmentId);
        validateToothNumbers(toothNumbers);
        validateTreatmentCost(cost, requiresDualVerification, dentistId);

        ElectronicHealthRecord ehr = ehrRepository.findByPatientId(patientId)
                .orElseThrow(() -> new NotFoundException("EHR not found for patient ID: " + patientId.getValue()));

        TreatmentRecord treatment = TreatmentRecord.builder()
                .treatmentId(TreatmentRecordId.newId())
                .treatmentCode(treatmentCode)
                .description(description)
                .performedBy(dentistId.getValue())
                .performedAt(LocalDateTime.now())
                .toothNumbers(toothNumbers)
                .notes(notes)
                .cost(cost)
                .build();

        ehr.addTreatmentRecord(treatment);
        updateOdontogram(ehr, toothNumbers, treatment.getTreatmentId(), description);
        ehrRepository.save(ehr);
        return treatment;
    }

    public Odontogram getOdontogram(PatientId patientId, UserId requesterId) {
        validateUserIsDentistOrAdmin(requesterId);
        validatePatientExists(patientId);

        ElectronicHealthRecord ehr = ehrRepository.findByPatientId(patientId)
                .orElseThrow(() -> new NotFoundException("EHR not found for patient ID: " + patientId.getValue()));

        return ehr.getOdontogram();
    }

    public Odontogram updateToothCondition(PatientId patientId, UserId dentistId, Integer toothNumber,
                                           ToothStatus condition, String notes, LocalDate lastTreated) {
        validateUserIsDentist(dentistId);
        validatePatientExists(patientId);
        validateToothNumber(toothNumber);

        ElectronicHealthRecord ehr = ehrRepository.findByPatientId(patientId)
                .orElseThrow(() -> new NotFoundException("EHR not found for patient ID: " + patientId.getValue()));

        ToothCondition toothCondition = new ToothCondition(toothNumber, condition, notes, lastTreated);
        ehr.getOdontogram().updateToothCondition(toothNumber, toothCondition);
        ehrRepository.save(ehr);
        return ehr.getOdontogram();
    }

    public List<TreatmentRecord> getTreatmentHistory(PatientId patientId, UserId requesterId) {
        validateUserIsDentistOrAdmin(requesterId);
        validatePatientExists(patientId);

        ElectronicHealthRecord ehr = ehrRepository.findByPatientId(patientId)
                .orElseThrow(() -> new NotFoundException("EHR not found for patient ID: " + patientId.getValue()));

        return new ArrayList<>(ehr.getTreatments());
    }

    public List<ClinicalNote> getClinicalNotes(PatientId patientId, UserId requesterId) {
        validateUserIsDentistOrAdmin(requesterId);
        validatePatientExists(patientId);

        ElectronicHealthRecord ehr = ehrRepository.findByPatientId(patientId)
                .orElseThrow(() -> new NotFoundException("EHR not found for patient ID: " + patientId.getValue()));

        return new ArrayList<>(ehr.getClinicalNotes());
    }

    // Private validation and helper methods
    private void validateUserIsDentist(UserId userId) {
        userRepository.findById(userId)
                .filter(user -> user.getRole() == UserRole.DENTIST && user.isActive())
                .orElseThrow(() -> new AuthorizationException("User is not an active dentist: " + userId.getValue()));
    }

    private void validateUserIsDentistOrAdmin(UserId userId) {
        userRepository.findById(userId)
                .filter(user -> (user.getRole() == UserRole.DENTIST || user.getRole() == UserRole.ADMINISTRATOR) && user.isActive())
                .orElseThrow(() -> new AuthorizationException("User is not authorized to access EHR data: " + userId.getValue()));
    }

    private void validatePatientExists(PatientId patientId) {
        if (!patientRepository.existsById(patientId)) {
            throw new NotFoundException("Patient not found: " + patientId.getValue());
        }
    }

    private void validateDentistHasCurrentAppointment(UserId dentistId, PatientId patientId, AppointmentId appointmentId) {
        if (appointmentId != null) {
            appointmentRepository.findById(appointmentId)
                    .filter(appt -> appt.getDentistId().equals(dentistId.getValue())
                            && appt.getPatientId().equals(patientId.getValue())
                            && (appt.getStatus().isInProgress() || appt.getStatus().isCompleted()))
                    .orElseThrow(() -> new AuthorizationException("Dentist does not have a current/completed appointment with this patient."));
        }
    }

    private void validateToothNumbers(Set<Integer> toothNumbers) {
        if (toothNumbers == null || toothNumbers.isEmpty()) {
            throw new ValidationException("Treatment must reference at least one tooth number (1-32) or indicate general (0).");
        }
        for (Integer tooth : toothNumbers) {
            validateToothNumber(tooth);
        }
    }

    private void validateToothNumber(Integer toothNumber) {
        if (toothNumber == null || toothNumber < 0 || toothNumber > 32) {
            throw new ValidationException("Tooth number must be between 0 (general) and 32.");
        }
    }

    private void validateTreatmentCost(BigDecimal cost, boolean requiresDualVerification, UserId dentistId) {
        if (cost == null || cost.compareTo(BigDecimal.ZERO) < 0) {
            throw new ValidationException("Treatment cost must be a non-negative value.");
        }
        if (requiresDualVerification && cost.compareTo(new BigDecimal("1000")) > 0) {
            userRepository.findById(dentistId)
                    .filter(user -> user.getRole() == UserRole.ADMINISTRATOR)
                    .orElseThrow(() -> new AuthorizationException("Treatments over $1000 require administrator dual verification."));
        }
    }

    private ElectronicHealthRecord findEhrContainingNote(ClinicalNoteId noteId) {
        return ehrRepository.findAll().stream()
                .filter(ehr -> ehr.getClinicalNotes().stream()
                        .anyMatch(note -> note.getNoteId().equals(noteId)))
                .findFirst()
                .orElseThrow(() -> new NotFoundException("Clinical note not found in any EHR: " + noteId.getValue()));
    }

    private void updateOdontogram(ElectronicHealthRecord ehr, Set<Integer> toothNumbers, TreatmentRecordId treatmentId, String description) {
        Odontogram odontogram = ehr.getOdontogram();
        LocalDate today = LocalDate.now();
        for (Integer tooth : toothNumbers) {
            if (tooth > 0 && tooth <= 32) {
                ToothStatus newStatus = determineToothStatusFromTreatment(description);
                ToothCondition condition = new ToothCondition(tooth, newStatus, "Treated: " + description, today);
                odontogram.updateToothCondition(tooth, condition);
            }
        }
    }

    private ToothStatus determineToothStatusFromTreatment(String description) {
        String descLower = description.toLowerCase();
        if (descLower.contains("filling") || descLower.contains("restoration")) {
            return ToothStatus.FILLED;
        } else if (descLower.contains("root canal")) {
            return ToothStatus.ROOT_CANAL;
        } else if (descLower.contains("implant")) {
            return ToothStatus.IMPLANT;
        } else if (descLower.contains("extract") || descLower.contains("remove")) {
            return ToothStatus.MISSING;
        }
        return ToothStatus.CARIES;
    }
}