package com.application.domain.model;

import com.application.domain.valueobject.EhrId;
import com.application.domain.valueobject.PatientId;
import com.application.domain.valueobject.ClinicalNoteId;
import com.application.domain.valueobject.TreatmentRecordId;
import com.application.domain.valueobject.OdontogramId;
import com.application.domain.valueobject.UserId;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@ExtendWith(MockitoExtension.class)
class ElectronicHealthRecordTest {

    @Test
    void createForNewPatient_shouldCreateEHRWithCorrectInitialState() {
        // Given
        PatientId patientId = PatientId.newId();
        LocalDateTime beforeCreation = LocalDateTime.now().minusSeconds(1);

        // When
        ElectronicHealthRecord ehr = ElectronicHealthRecord.createForNewPatient(patientId);

        // Then
        assertThat(ehr).isNotNull();
        assertThat(ehr.getId()).isNotNull().isInstanceOf(EhrId.class);
        assertThat(ehr.getPatientId()).isEqualTo(patientId);
        assertThat(ehr.getClinicalNotes()).isEmpty();
        assertThat(ehr.getOdontogram()).isNotNull();
        assertThat(ehr.getOdontogram().getToothData()).isEmpty();
        assertThat(ehr.getTreatments()).isEmpty();
        assertThat(ehr.getCreatedAt()).isAfterOrEqualTo(beforeCreation);
        assertThat(ehr.getLastUpdated()).isAfterOrEqualTo(beforeCreation);
        assertThat(ehr.getCreatedAt()).isEqualTo(ehr.getLastUpdated());
    }

    @Test
    void constructor_shouldDefensivelyCopyMutableCollections() {
        // Given
        EhrId ehrId = EhrId.newId();
        PatientId patientId = PatientId.newId();
        LocalDateTime now = LocalDateTime.now();

        List<ClinicalNote> originalNotes = new ArrayList<>();
        originalNotes.add(createTestClinicalNote());

        List<TreatmentRecord> originalTreatments = new ArrayList<>();
        originalTreatments.add(createTestTreatmentRecord());

        Odontogram odontogram = Odontogram.builder()
                .odontogramId(OdontogramId.newId())
                .toothData(new HashMap<>())
                .lastUpdated(now)
                .build();

        // When
        ElectronicHealthRecord ehr = ElectronicHealthRecord.builder()
                .id(ehrId)
                .patientId(patientId)
                .clinicalNotes(originalNotes)
                .odontogram(odontogram)
                .treatments(originalTreatments)
                .createdAt(now)
                .lastUpdated(now)
                .build();

        // Then - Modifying original lists should not affect EHR
        originalNotes.clear();
        originalTreatments.clear();

        assertThat(ehr.getClinicalNotes()).hasSize(1);
        assertThat(ehr.getTreatments()).hasSize(1);
    }

    @Test
    void constructor_withNullLists_shouldInitializeWithEmptyLists() {
        // Given
        EhrId ehrId = EhrId.newId();
        PatientId patientId = PatientId.newId();
        LocalDateTime now = LocalDateTime.now();
        Odontogram odontogram = Odontogram.builder()
                .odontogramId(OdontogramId.newId())
                .toothData(Collections.emptyMap())
                .lastUpdated(now)
                .build();

        // When
        ElectronicHealthRecord ehr = ElectronicHealthRecord.builder()
                .id(ehrId)
                .patientId(patientId)
                .clinicalNotes(null)
                .odontogram(odontogram)
                .treatments(null)
                .createdAt(now)
                .lastUpdated(now)
                .build();

        // Then
        assertThat(ehr.getClinicalNotes()).isEmpty();
        assertThat(ehr.getTreatments()).isEmpty();
    }

    @Test
    void addClinicalNote_shouldAddNoteAndUpdateLastUpdated() throws InterruptedException {
        // Given
        ElectronicHealthRecord ehr = createTestEHR();
        ClinicalNote newNote = createTestClinicalNote();
        LocalDateTime originalLastUpdated = ehr.getLastUpdated();

        Thread.sleep(1); // Ensure timestamp difference

        // When
        ehr.addClinicalNote(newNote);

        // Then
        assertThat(ehr.getClinicalNotes()).hasSize(1).contains(newNote);
        assertThat(ehr.getLastUpdated()).isAfter(originalLastUpdated);
    }

    @Test
    void addClinicalNote_withNullNote_shouldThrowIllegalArgumentException() {
        // Given
        ElectronicHealthRecord ehr = createTestEHR();

        // When & Then
        assertThatThrownBy(() -> ehr.addClinicalNote(null))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Clinical note cannot be null");
    }

    @Test
    void addTreatmentRecord_shouldAddTreatmentAndUpdateLastUpdated() throws InterruptedException {
        // Given
        ElectronicHealthRecord ehr = createTestEHR();
        TreatmentRecord newTreatment = createTestTreatmentRecord();
        LocalDateTime originalLastUpdated = ehr.getLastUpdated();

        Thread.sleep(1); // Ensure timestamp difference

        // When
        ehr.addTreatmentRecord(newTreatment);

        // Then
        assertThat(ehr.getTreatments()).hasSize(1).contains(newTreatment);
        assertThat(ehr.getLastUpdated()).isAfter(originalLastUpdated);
    }

    @Test
    void addTreatmentRecord_withNullTreatment_shouldThrowIllegalArgumentException() {
        // Given
        ElectronicHealthRecord ehr = createTestEHR();

        // When & Then
        assertThatThrownBy(() -> ehr.addTreatmentRecord(null))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Treatment record cannot be null");
    }

    @Test
    void updateOdontogram_shouldUpdateOdontogramDataAndLastUpdated() throws InterruptedException {
        // Given
        ElectronicHealthRecord ehr = createTestEHR();
        Map<Integer, ToothCondition> newToothData = new HashMap<>();
        newToothData.put(16, ToothCondition.builder()
                .toothNumber(16)
                .condition(com.application.domain.enums.ToothStatus.CARIES)
                .notes("Cavity detected")
                .lastTreated(LocalDate.now())
                .build());

        Odontogram updatedOdontogram = Odontogram.builder()
                .odontogramId(OdontogramId.newId())
                .toothData(newToothData)
                .lastUpdated(LocalDateTime.now())
                .build();

        LocalDateTime originalLastUpdated = ehr.getLastUpdated();
        Thread.sleep(1);

        // When
        ehr.updateOdontogram(updatedOdontogram);

        // Then
        assertThat(ehr.getOdontogram().getToothData()).hasSize(1).containsKey(16);
        assertThat(ehr.getLastUpdated()).isAfter(originalLastUpdated);
    }

    @Test
    void updateOdontogram_withNullOdontogram_shouldThrowIllegalArgumentException() {
        // Given
        ElectronicHealthRecord ehr = createTestEHR();

        // When & Then
        assertThatThrownBy(() -> ehr.updateOdontogram(null))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Odontogram cannot be null");
    }

    @Test
    void getClinicalNotes_shouldReturnUnmodifiableList() {
        // Given
        ElectronicHealthRecord ehr = createTestEHR();
        ehr.addClinicalNote(createTestClinicalNote());

        // When
        List<ClinicalNote> notes = ehr.getClinicalNotes();

        // Then
        assertThatThrownBy(() -> notes.add(createTestClinicalNote()))
                .isInstanceOf(UnsupportedOperationException.class);
    }

    @Test
    void getTreatments_shouldReturnUnmodifiableList() {
        // Given
        ElectronicHealthRecord ehr = createTestEHR();
        ehr.addTreatmentRecord(createTestTreatmentRecord());

        // When
        List<TreatmentRecord> treatments = ehr.getTreatments();

        // Then
        assertThatThrownBy(() -> treatments.add(createTestTreatmentRecord()))
                .isInstanceOf(UnsupportedOperationException.class);
    }

    @Test
    void builder_toBuilder_shouldCreateMutableCopy() {
        // Given
        ElectronicHealthRecord original = createTestEHR();
        ClinicalNote newNote = createTestClinicalNote();

        // When
        ElectronicHealthRecord copy = original.toBuilder()
                .clinicalNotes(new ArrayList<>(original.getClinicalNotes()))
                .build();

        copy.addClinicalNote(newNote);

        // Then
        assertThat(original.getClinicalNotes()).isEmpty();
        assertThat(copy.getClinicalNotes()).hasSize(1).contains(newNote);
        assertThat(copy.getId()).isEqualTo(original.getId());
        assertThat(copy.getPatientId()).isEqualTo(original.getPatientId());
    }

    @Test
    void equalsAndHashCode_shouldBeBasedOnId() {
        // Given
        EhrId sameId = EhrId.newId();
        PatientId patientId1 = PatientId.newId();
        PatientId patientId2 = PatientId.newId();
        LocalDateTime now = LocalDateTime.now();

        Odontogram odontogram = Odontogram.builder()
                .odontogramId(OdontogramId.newId())
                .toothData(Collections.emptyMap())
                .lastUpdated(now)
                .build();

        ElectronicHealthRecord ehr1 = ElectronicHealthRecord.builder()
                .id(sameId)
                .patientId(patientId1)
                .clinicalNotes(new ArrayList<>())
                .odontogram(odontogram)
                .treatments(new ArrayList<>())
                .createdAt(now)
                .lastUpdated(now)
                .build();

        ElectronicHealthRecord ehr2 = ElectronicHealthRecord.builder()
                .id(sameId)
                .patientId(patientId2) // Different patient
                .clinicalNotes(new ArrayList<>())
                .odontogram(odontogram)
                .treatments(new ArrayList<>())
                .createdAt(now)
                .lastUpdated(now)
                .build();

        ElectronicHealthRecord ehr3 = ElectronicHealthRecord.builder()
                .id(EhrId.newId()) // Different ID
                .patientId(patientId1)
                .clinicalNotes(new ArrayList<>())
                .odontogram(odontogram)
                .treatments(new ArrayList<>())
                .createdAt(now)
                .lastUpdated(now)
                .build();

        // Then
        assertThat(ehr1).isEqualTo(ehr2);
        assertThat(ehr1.hashCode()).isEqualTo(ehr2.hashCode());
        assertThat(ehr1).isNotEqualTo(ehr3);
    }

    @Test
    void toString_shouldIncludeClassAndId() {
        // Given
        ElectronicHealthRecord ehr = createTestEHR();

        // When
        String toString = ehr.toString();

        // Then
        assertThat(toString).contains("ElectronicHealthRecord");
        assertThat(toString).contains(ehr.getId().toString());
    }

    // Helper methods
    private ElectronicHealthRecord createTestEHR() {
        PatientId patientId = PatientId.newId();
        return ElectronicHealthRecord.createForNewPatient(patientId);
    }

    private ClinicalNote createTestClinicalNote() {
        return ClinicalNote.builder()
                .noteId(ClinicalNoteId.newId())
                .dentistId(UserId.newId())
                .content("Initial consultation - patient reports sensitivity in lower left molar.")
                .date(LocalDate.now())
                .createdAt(LocalDateTime.now())
                .build();
    }

    private TreatmentRecord createTestTreatmentRecord() {
        return TreatmentRecord.builder()
                .treatmentId(TreatmentRecordId.newId())
                .treatmentCode("D1110")
                .description("Adult Prophylaxis")
                .performedBy(UserId.newId())
                .performedAt(LocalDateTime.now())
                .toothNumbers(new HashSet<>(Arrays.asList(1, 2, 3)))
                .notes("Routine cleaning")
                .cost(new BigDecimal("85.00"))
                .build();
    }
}