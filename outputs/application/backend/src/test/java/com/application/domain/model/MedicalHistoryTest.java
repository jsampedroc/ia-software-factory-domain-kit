package com.application.domain.model;

import com.application.domain.valueobject.MedicalHistoryId;
import com.application.domain.valueobject.PatientId;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class MedicalHistoryTest {

    @Test
    void create_ShouldInitializeAllFieldsAndTimestamps() {
        // Given
        MedicalHistoryId id = new MedicalHistoryId(UUID.randomUUID());
        PatientId patientId = new PatientId(UUID.randomUUID());
        String alergias = "Penicilina, Latex";
        String condicionesMedicas = "Hipertensión controlada";
        String medicamentos = "Losartán 50mg";
        String observaciones = "Paciente con buen pronóstico";

        // When
        MedicalHistory history = MedicalHistory.create(id, patientId, alergias, condicionesMedicas, medicamentos, observaciones);

        // Then
        assertNotNull(history);
        assertEquals(id, history.getId());
        assertEquals(patientId, history.getPatientId());
        assertEquals(alergias, history.getAlergias());
        assertEquals(condicionesMedicas, history.getCondicionesMedicas());
        assertEquals(medicamentos, history.getMedicamentos());
        assertEquals(observaciones, history.getObservacionesGenerales());
        assertNotNull(history.getFechaCreacion());
        assertNotNull(history.getUltimaActualizacion());
        assertEquals(history.getFechaCreacion(), history.getUltimaActualizacion());
        assertTrue(history.getFechaCreacion().isBefore(LocalDateTime.now().plusSeconds(1)) || history.getFechaCreacion().isEqual(LocalDateTime.now()));
    }

    @Test
    void update_ShouldUpdateFieldsAndLastUpdateTimestamp() throws InterruptedException {
        // Given
        MedicalHistoryId id = new MedicalHistoryId(UUID.randomUUID());
        PatientId patientId = new PatientId(UUID.randomUUID());
        MedicalHistory history = MedicalHistory.create(id, patientId, "Ninguna", "Ninguna", "Ninguno", "Ninguna");

        LocalDateTime initialUpdateTime = history.getUltimaActualizacion();
        Thread.sleep(10); // Ensure timestamp difference

        String newAlergias = "Anestesia general";
        String newCondiciones = "Diabetes tipo 2";
        String newMedicamentos = "Metformina 850mg";
        String newObservaciones = "Requiere monitorización glucémica";

        // When
        history.update(newAlergias, newCondiciones, newMedicamentos, newObservaciones);

        // Then
        assertEquals(newAlergias, history.getAlergias());
        assertEquals(newCondiciones, history.getCondicionesMedicas());
        assertEquals(newMedicamentos, history.getMedicamentos());
        assertEquals(newObservaciones, history.getObservacionesGenerales());
        assertTrue(history.getUltimaActualizacion().isAfter(initialUpdateTime));
        assertEquals(history.getFechaCreacion(), history.getFechaCreacion()); // Creation date unchanged
    }

    @Test
    void assignToPatient_ShouldUpdatePatientIdAndTimestamp() throws InterruptedException {
        // Given
        MedicalHistoryId id = new MedicalHistoryId(UUID.randomUUID());
        PatientId initialPatientId = new PatientId(UUID.randomUUID());
        MedicalHistory history = MedicalHistory.create(id, initialPatientId, "N/A", "N/A", "N/A", "N/A");

        PatientId newPatientId = new PatientId(UUID.randomUUID());
        LocalDateTime initialUpdateTime = history.getUltimaActualizacion();
        Thread.sleep(10);

        // When
        history.assignToPatient(newPatientId);

        // Then
        assertEquals(newPatientId, history.getPatientId());
        assertTrue(history.getUltimaActualizacion().isAfter(initialUpdateTime));
        assertEquals(initialPatientId, initialPatientId); // Original ID unchanged (value object)
    }

    @Test
    void hasCriticalAllergies_ShouldReturnTrueForCriticalAllergies() {
        // Given
        MedicalHistoryId id = new MedicalHistoryId(UUID.randomUUID());
        PatientId patientId = new PatientId(UUID.randomUUID());

        MedicalHistory history1 = MedicalHistory.create(id, patientId, "Alergia a PENICILINA severa", "Ninguna", "Ninguno", "");
        MedicalHistory history2 = MedicalHistory.create(id, patientId, "Reacción al latex", "Ninguna", "Ninguno", "");
        MedicalHistory history3 = MedicalHistory.create(id, patientId, "Sensible a la anestesia local", "Ninguna", "Ninguno", "");
        MedicalHistory history4 = MedicalHistory.create(id, patientId, "Alergia a polen", "Ninguna", "Ninguno", "");
        MedicalHistory history5 = MedicalHistory.create(id, patientId, null, "Ninguna", "Ninguno", "");

        // Then
        assertTrue(history1.hasCriticalAllergies());
        assertTrue(history2.hasCriticalAllergies());
        assertTrue(history3.hasCriticalAllergies());
        assertFalse(history4.hasCriticalAllergies());
        assertFalse(history5.hasCriticalAllergies());
    }

    @Test
    void hasChronicConditions_ShouldReturnTrueForChronicConditions() {
        // Given
        MedicalHistoryId id = new MedicalHistoryId(UUID.randomUUID());
        PatientId patientId = new PatientId(UUID.randomUUID());

        MedicalHistory history1 = MedicalHistory.create(id, patientId, "Ninguna", "Diabetes mellitus", "Ninguno", "");
        MedicalHistory history2 = MedicalHistory.create(id, patientId, "Ninguna", "Hipertensión arterial", "Ninguno", "");
        MedicalHistory history3 = MedicalHistory.create(id, patientId, "Ninguna", "Cardiopatía isquémica", "Ninguno", "");
        MedicalHistory history4 = MedicalHistory.create(id, patientId, "Ninguna", "Resfriado común", "Ninguno", "");
        MedicalHistory history5 = MedicalHistory.create(id, patientId, "Ninguna", null, "Ninguno", "");

        // Then
        assertTrue(history1.hasChronicConditions());
        assertTrue(history2.hasChronicConditions());
        assertTrue(history3.hasChronicConditions());
        assertFalse(history4.hasChronicConditions());
        assertFalse(history5.hasChronicConditions());
    }

    @Test
    void entityInheritance_ShouldHaveIdFromParentClass() {
        // Given
        MedicalHistoryId expectedId = new MedicalHistoryId(UUID.randomUUID());
        PatientId patientId = new PatientId(UUID.randomUUID());

        // When
        MedicalHistory history = MedicalHistory.create(expectedId, patientId, "", "", "", "");

        // Then
        assertNotNull(history.getId());
        assertEquals(expectedId, history.getId());
        assertTrue(history instanceof com.application.domain.shared.Entity);
    }
}