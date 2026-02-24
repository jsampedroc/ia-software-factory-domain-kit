package com.application.interfaceadapter.controller;

import com.application.application.dto.MedicalHistoryDTO;
import com.application.application.service.MedicalHistoryService;
import com.application.domain.valueobject.MedicalHistoryId;
import com.application.domain.valueobject.PatientId;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class MedicalHistoryControllerTest {

    @Mock
    private MedicalHistoryService medicalHistoryService;

    @InjectMocks
    private MedicalHistoryController medicalHistoryController;

    private MedicalHistoryId testHistoryId;
    private PatientId testPatientId;
    private UUID testUUID;
    private UUID testPatientUUID;
    private MedicalHistoryDTO testMedicalHistoryDTO;

    @BeforeEach
    void setUp() {
        testUUID = UUID.randomUUID();
        testPatientUUID = UUID.randomUUID();
        testHistoryId = new MedicalHistoryId(testUUID);
        testPatientId = new PatientId(testPatientUUID);

        testMedicalHistoryDTO = new MedicalHistoryDTO(
                testUUID,
                LocalDateTime.now(),
                LocalDateTime.now(),
                "Alergia a penicilina",
                "Hipertensión",
                "Lisinopril 10mg",
                "Observaciones generales",
                testPatientUUID
        );
    }

    @Test
    void getById_ShouldReturnMedicalHistory_WhenExists() {
        when(medicalHistoryService.findById(testHistoryId)).thenReturn(Optional.of(testMedicalHistoryDTO));

        ResponseEntity<MedicalHistoryDTO> response = medicalHistoryController.getById(testUUID);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(testMedicalHistoryDTO.id(), response.getBody().id());
        verify(medicalHistoryService, times(1)).findById(testHistoryId);
    }

    @Test
    void getById_ShouldReturnNotFound_WhenDoesNotExist() {
        when(medicalHistoryService.findById(testHistoryId)).thenReturn(Optional.empty());

        ResponseEntity<MedicalHistoryDTO> response = medicalHistoryController.getById(testUUID);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertNull(response.getBody());
        verify(medicalHistoryService, times(1)).findById(testHistoryId);
    }

    @Test
    void getByPatientId_ShouldReturnMedicalHistory_WhenExists() {
        when(medicalHistoryService.findByPatientId(testPatientId)).thenReturn(Optional.of(testMedicalHistoryDTO));

        ResponseEntity<MedicalHistoryDTO> response = medicalHistoryController.getByPatientId(testPatientUUID);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(testMedicalHistoryDTO.patientId(), response.getBody().patientId());
        verify(medicalHistoryService, times(1)).findByPatientId(testPatientId);
    }

    @Test
    void getByPatientId_ShouldReturnNotFound_WhenDoesNotExist() {
        when(medicalHistoryService.findByPatientId(testPatientId)).thenReturn(Optional.empty());

        ResponseEntity<MedicalHistoryDTO> response = medicalHistoryController.getByPatientId(testPatientUUID);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertNull(response.getBody());
        verify(medicalHistoryService, times(1)).findByPatientId(testPatientId);
    }

    @Test
    void getAll_ShouldReturnListOfMedicalHistories() {
        List<MedicalHistoryDTO> historyList = List.of(testMedicalHistoryDTO);
        when(medicalHistoryService.findAll()).thenReturn(historyList);

        ResponseEntity<List<MedicalHistoryDTO>> response = medicalHistoryController.getAll();

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(1, response.getBody().size());
        verify(medicalHistoryService, times(1)).findAll();
    }

    @Test
    void create_ShouldReturnCreatedMedicalHistory() {
        when(medicalHistoryService.create(any(MedicalHistoryDTO.class))).thenReturn(testMedicalHistoryDTO);

        ResponseEntity<MedicalHistoryDTO> response = medicalHistoryController.create(testMedicalHistoryDTO);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(testMedicalHistoryDTO.id(), response.getBody().id());
        verify(medicalHistoryService, times(1)).create(testMedicalHistoryDTO);
    }

    @Test
    void update_ShouldReturnUpdatedMedicalHistory() {
        when(medicalHistoryService.update(eq(testHistoryId), any(MedicalHistoryDTO.class))).thenReturn(testMedicalHistoryDTO);

        ResponseEntity<MedicalHistoryDTO> response = medicalHistoryController.update(testUUID, testMedicalHistoryDTO);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(testMedicalHistoryDTO.id(), response.getBody().id());
        verify(medicalHistoryService, times(1)).update(testHistoryId, testMedicalHistoryDTO);
    }

    @Test
    void delete_ShouldReturnNoContent() {
        doNothing().when(medicalHistoryService).delete(testHistoryId);

        ResponseEntity<Void> response = medicalHistoryController.delete(testUUID);

        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
        assertNull(response.getBody());
        verify(medicalHistoryService, times(1)).delete(testHistoryId);
    }

    @Test
    void updateAllergies_ShouldReturnUpdatedMedicalHistory() {
        String newAllergies = "Alergia a látex";
        MedicalHistoryDTO updatedDTO = new MedicalHistoryDTO(
                testUUID,
                testMedicalHistoryDTO.fechaCreacion(),
                LocalDateTime.now(),
                newAllergies,
                testMedicalHistoryDTO.condicionesMedicas(),
                testMedicalHistoryDTO.medicamentos(),
                testMedicalHistoryDTO.observacionesGenerales(),
                testPatientUUID
        );
        when(medicalHistoryService.updateAllergies(testHistoryId, newAllergies)).thenReturn(updatedDTO);

        ResponseEntity<MedicalHistoryDTO> response = medicalHistoryController.updateAllergies(testUUID, newAllergies);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(newAllergies, response.getBody().alergias());
        verify(medicalHistoryService, times(1)).updateAllergies(testHistoryId, newAllergies);
    }

    @Test
    void updateMedicalConditions_ShouldReturnUpdatedMedicalHistory() {
        String newConditions = "Diabetes tipo 2";
        MedicalHistoryDTO updatedDTO = new MedicalHistoryDTO(
                testUUID,
                testMedicalHistoryDTO.fechaCreacion(),
                LocalDateTime.now(),
                testMedicalHistoryDTO.alergias(),
                newConditions,
                testMedicalHistoryDTO.medicamentos(),
                testMedicalHistoryDTO.observacionesGenerales(),
                testPatientUUID
        );
        when(medicalHistoryService.updateMedicalConditions(testHistoryId, newConditions)).thenReturn(updatedDTO);

        ResponseEntity<MedicalHistoryDTO> response = medicalHistoryController.updateMedicalConditions(testUUID, newConditions);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(newConditions, response.getBody().condicionesMedicas());
        verify(medicalHistoryService, times(1)).updateMedicalConditions(testHistoryId, newConditions);
    }

    @Test
    void updateMedications_ShouldReturnUpdatedMedicalHistory() {
        String newMedications = "Metformina 500mg";
        MedicalHistoryDTO updatedDTO = new MedicalHistoryDTO(
                testUUID,
                testMedicalHistoryDTO.fechaCreacion(),
                LocalDateTime.now(),
                testMedicalHistoryDTO.alergias(),
                testMedicalHistoryDTO.condicionesMedicas(),
                newMedications,
                testMedicalHistoryDTO.observacionesGenerales(),
                testPatientUUID
        );
        when(medicalHistoryService.updateMedications(testHistoryId, newMedications)).thenReturn(updatedDTO);

        ResponseEntity<MedicalHistoryDTO> response = medicalHistoryController.updateMedications(testUUID, newMedications);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(newMedications, response.getBody().medicamentos());
        verify(medicalHistoryService, times(1)).updateMedications(testHistoryId, newMedications);
    }

    @Test
    void updateObservations_ShouldReturnUpdatedMedicalHistory() {
        String newObservations = "Nuevas observaciones detalladas";
        MedicalHistoryDTO updatedDTO = new MedicalHistoryDTO(
                testUUID,
                testMedicalHistoryDTO.fechaCreacion(),
                LocalDateTime.now(),
                testMedicalHistoryDTO.alergias(),
                testMedicalHistoryDTO.condicionesMedicas(),
                testMedicalHistoryDTO.medicamentos(),
                newObservations,
                testPatientUUID
        );
        when(medicalHistoryService.updateObservations(testHistoryId, newObservations)).thenReturn(updatedDTO);

        ResponseEntity<MedicalHistoryDTO> response = medicalHistoryController.updateObservations(testUUID, newObservations);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(newObservations, response.getBody().observacionesGenerales());
        verify(medicalHistoryService, times(1)).updateObservations(testHistoryId, newObservations);
    }
}