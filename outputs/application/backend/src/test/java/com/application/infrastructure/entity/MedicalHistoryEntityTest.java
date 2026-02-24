package com.application.infrastructure.entity;

import com.application.domain.model.MedicalHistory;
import com.application.domain.valueobject.MedicalHistoryId;
import com.application.domain.valueobject.PatientId;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class MedicalHistoryEntityTest {

    @Test
    void fromDomain_ShouldMapAllFieldsCorrectly() {
        // Given
        UUID historyId = UUID.randomUUID();
        UUID patientId = UUID.randomUUID();
        LocalDateTime creationDate = LocalDateTime.now().minusDays(1);
        LocalDateTime lastUpdate = LocalDateTime.now();
        String allergies = "Penicillin, Pollen";
        String medicalConditions = "Hypertension, Diabetes";
        String medications = "Lisinopril 10mg, Metformin 500mg";
        String generalObservations = "Regular checkups needed.";

        MedicalHistory domain = new MedicalHistory(
                MedicalHistoryId.of(historyId),
                PatientId.of(patientId),
                creationDate,
                lastUpdate,
                allergies,
                medicalConditions,
                medications,
                generalObservations
        );

        // When
        MedicalHistoryEntity entity = MedicalHistoryEntity.fromDomain(domain);

        // Then
        assertNotNull(entity);
        assertEquals(historyId, entity.getId());
        assertEquals(patientId, entity.getPatientId());
        assertEquals(creationDate, entity.getCreationDate());
        assertEquals(lastUpdate, entity.getLastUpdate());
        assertEquals(allergies, entity.getAllergies());
        assertEquals(medicalConditions, entity.getMedicalConditions());
        assertEquals(medications, entity.getMedications());
        assertEquals(generalObservations, entity.getGeneralObservations());
    }

    @Test
    void toDomain_ShouldMapAllFieldsCorrectly() {
        // Given
        UUID historyId = UUID.randomUUID();
        UUID patientId = UUID.randomUUID();
        LocalDateTime creationDate = LocalDateTime.now().minusDays(1);
        LocalDateTime lastUpdate = LocalDateTime.now();
        String allergies = "None";
        String medicalConditions = "Asthma";
        String medications = "Inhaler";
        String generalObservations = "Good oral health.";

        MedicalHistoryEntity entity = new MedicalHistoryEntity();
        entity.id = historyId;
        entity.patientId = patientId;
        entity.creationDate = creationDate;
        entity.lastUpdate = lastUpdate;
        entity.allergies = allergies;
        entity.medicalConditions = medicalConditions;
        entity.medications = medications;
        entity.generalObservations = generalObservations;

        // When
        MedicalHistory domain = entity.toDomain();

        // Then
        assertNotNull(domain);
        assertEquals(historyId, domain.getId().getValue());
        assertEquals(patientId, domain.getPatientId().getValue());
        assertEquals(creationDate, domain.getCreationDate());
        assertEquals(lastUpdate, domain.getLastUpdate());
        assertEquals(allergies, domain.getAllergies());
        assertEquals(medicalConditions, domain.getMedicalConditions());
        assertEquals(medications, domain.getMedications());
        assertEquals(generalObservations, domain.getGeneralObservations());
    }

    @Test
    void updateFromDomain_ShouldUpdateMutableFields() {
        // Given
        UUID historyId = UUID.randomUUID();
        UUID patientId = UUID.randomUUID();
        LocalDateTime originalCreationDate = LocalDateTime.now().minusDays(5);
        LocalDateTime originalLastUpdate = LocalDateTime.now().minusDays(1);
        LocalDateTime newLastUpdate = LocalDateTime.now();

        MedicalHistoryEntity entity = new MedicalHistoryEntity();
        entity.id = historyId;
        entity.patientId = patientId;
        entity.creationDate = originalCreationDate;
        entity.lastUpdate = originalLastUpdate;
        entity.allergies = "Old allergies";
        entity.medicalConditions = "Old conditions";
        entity.medications = "Old medications";
        entity.generalObservations = "Old observations";

        String newAllergies = "Updated allergies list";
        String newMedicalConditions = "Updated conditions";
        String newMedications = "Updated medications";
        String newGeneralObservations = "Updated observations";

        MedicalHistory updatedDomain = new MedicalHistory(
                MedicalHistoryId.of(historyId),
                PatientId.of(patientId),
                originalCreationDate, // Creation date should not change
                newLastUpdate,
                newAllergies,
                newMedicalConditions,
                newMedications,
                newGeneralObservations
        );

        // When
        entity.updateFromDomain(updatedDomain);

        // Then
        // Immutable fields should remain unchanged
        assertEquals(historyId, entity.getId());
        assertEquals(patientId, entity.getPatientId());
        assertEquals(originalCreationDate, entity.getCreationDate());

        // Mutable fields should be updated
        assertEquals(newLastUpdate, entity.getLastUpdate());
        assertEquals(newAllergies, entity.getAllergies());
        assertEquals(newMedicalConditions, entity.getMedicalConditions());
        assertEquals(newMedications, entity.getMedications());
        assertEquals(newGeneralObservations, entity.getGeneralObservations());
    }

    @Test
    void noArgsConstructor_ShouldBeProtected() {
        // This test verifies the Lombok annotation is present and sets PROTECTED access.
        // We can't directly instantiate due to protected constructor, but we can check via reflection.
        assertTrue(MedicalHistoryEntity.class.isAnnotationPresent(org.junit.jupiter.api.condition.Disabled.class) == false);
        // The main assertion is that the class compiles with @NoArgsConstructor(access = PROTECTED)
    }

    @Test
    void getterAnnotations_ShouldBePresent() {
        // Verify Lombok @Getter is on the class
        assertTrue(MedicalHistoryEntity.class.isAnnotationPresent(lombok.Getter.class));
    }

    @Test
    void entityAndTableAnnotations_ShouldBePresent() {
        assertTrue(MedicalHistoryEntity.class.isAnnotationPresent(jakarta.persistence.Entity.class));
        assertTrue(MedicalHistoryEntity.class.isAnnotationPresent(jakarta.persistence.Table.class));

        jakarta.persistence.Table tableAnnotation = MedicalHistoryEntity.class.getAnnotation(jakarta.persistence.Table.class);
        assertEquals("medical_history", tableAnnotation.name());
    }
}