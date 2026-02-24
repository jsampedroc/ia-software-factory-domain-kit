package com.application.application.service;

import com.application.application.dto.MedicalHistoryDTO;
import com.application.domain.exception.DomainException;
import com.application.domain.model.MedicalHistory;
import com.application.domain.port.MedicalHistoryRepositoryPort;
import com.application.domain.valueobject.MedicalHistoryId;
import com.application.domain.valueobject.PatientId;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class MedicalHistoryServiceTest {

    @Mock
    private MedicalHistoryRepositoryPort medicalHistoryRepositoryPort;

    @InjectMocks
    private MedicalHistoryService medicalHistoryService;

    private MedicalHistoryId existingHistoryId;
    private PatientId existingPatientId;
    private MedicalHistory existingMedicalHistory;
    private MedicalHistoryDTO validMedicalHistoryDTO;

    @BeforeEach
    void setUp() {
        existingHistoryId = new MedicalHistoryId(UUID.randomUUID());
        existingPatientId = new PatientId(UUID.randomUUID());

        existingMedicalHistory = MedicalHistory.builder()
                .patientId(existingPatientId)
                .allergies("Polen")
                .medicalConditions("Hipertensión")
                .medications("Lisinopril")
                .generalObservations("Observación inicial")
                .build();

        validMedicalHistoryDTO = new MedicalHistoryDTO();
        validMedicalHistoryDTO.setPatientId(existingPatientId.getValue().toString());
        validMedicalHistoryDTO.setAllergies("Polen");
        validMedicalHistoryDTO.setMedicalConditions("Hipertensión");
        validMedicalHistoryDTO.setMedications("Lisinopril");
        validMedicalHistoryDTO.setGeneralObservations("Observación inicial");
    }

    @Test
    void create_WithValidData_ShouldReturnSavedMedicalHistoryDTO() {
        when(medicalHistoryRepositoryPort.save(any(MedicalHistory.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        MedicalHistoryDTO result = medicalHistoryService.create(validMedicalHistoryDTO);

        assertNotNull(result);
        assertEquals(validMedicalHistoryDTO.getPatientId(), result.getPatientId());
        assertEquals(validMedicalHistoryDTO.getAllergies(), result.getAllergies());
        assertEquals(validMedicalHistoryDTO.getMedicalConditions(), result.getMedicalConditions());
        assertEquals(validMedicalHistoryDTO.getMedications(), result.getMedications());
        assertEquals(validMedicalHistoryDTO.getGeneralObservations(), result.getGeneralObservations());
        assertNotNull(result.getCreationDate());
        assertNotNull(result.getLastUpdate());

        verify(medicalHistoryRepositoryPort, times(1)).save(any(MedicalHistory.class));
    }

    @Test
    void create_WithNullPatientId_ShouldThrowDomainException() {
        MedicalHistoryDTO invalidDTO = new MedicalHistoryDTO();
        invalidDTO.setPatientId(null);

        DomainException exception = assertThrows(DomainException.class,
                () -> medicalHistoryService.create(invalidDTO));

        assertEquals("El ID del paciente es obligatorio", exception.getMessage());
        verify(medicalHistoryRepositoryPort, never()).save(any());
    }

    @Test
    void update_WithExistingIdAndValidData_ShouldReturnUpdatedMedicalHistoryDTO() {
        MedicalHistoryDTO updateDTO = new MedicalHistoryDTO();
        updateDTO.setPatientId(existingPatientId.getValue().toString());
        updateDTO.setAllergies("Nuevas alergias");
        updateDTO.setMedicalConditions("Nuevas condiciones");
        updateDTO.setMedications("Nuevos medicamentos");
        updateDTO.setGeneralObservations("Observaciones actualizadas");

        when(medicalHistoryRepositoryPort.findById(existingHistoryId))
                .thenReturn(Optional.of(existingMedicalHistory));
        when(medicalHistoryRepositoryPort.save(any(MedicalHistory.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        MedicalHistoryDTO result = medicalHistoryService.update(existingHistoryId.getValue().toString(), updateDTO);

        assertNotNull(result);
        assertEquals(updateDTO.getAllergies(), result.getAllergies());
        assertEquals(updateDTO.getMedicalConditions(), result.getMedicalConditions());
        assertEquals(updateDTO.getMedications(), result.getMedications());
        assertEquals(updateDTO.getGeneralObservations(), result.getGeneralObservations());

        verify(medicalHistoryRepositoryPort, times(1)).findById(existingHistoryId);
        verify(medicalHistoryRepositoryPort, times(1)).save(any(MedicalHistory.class));
    }

    @Test
    void update_WithNonExistingId_ShouldThrowDomainException() {
        String nonExistingId = UUID.randomUUID().toString();
        when(medicalHistoryRepositoryPort.findById(any(MedicalHistoryId.class)))
                .thenReturn(Optional.empty());

        DomainException exception = assertThrows(DomainException.class,
                () -> medicalHistoryService.update(nonExistingId, validMedicalHistoryDTO));

        assertEquals("Historial médico no encontrado con ID: " + nonExistingId, exception.getMessage());
        verify(medicalHistoryRepositoryPort, times(1)).findById(any(MedicalHistoryId.class));
        verify(medicalHistoryRepositoryPort, never()).save(any());
    }

    @Test
    void findById_WithExistingId_ShouldReturnMedicalHistoryDTO() {
        when(medicalHistoryRepositoryPort.findById(existingHistoryId))
                .thenReturn(Optional.of(existingMedicalHistory));

        MedicalHistoryDTO result = medicalHistoryService.findById(existingHistoryId.getValue().toString());

        assertNotNull(result);
        assertEquals(existingPatientId.getValue().toString(), result.getPatientId());
        assertEquals(existingMedicalHistory.getAllergies(), result.getAllergies());
        assertEquals(existingMedicalHistory.getMedicalConditions(), result.getMedicalConditions());
        assertEquals(existingMedicalHistory.getMedications(), result.getMedications());
        assertEquals(existingMedicalHistory.getGeneralObservations(), result.getGeneralObservations());

        verify(medicalHistoryRepositoryPort, times(1)).findById(existingHistoryId);
    }

    @Test
    void findById_WithNonExistingId_ShouldThrowDomainException() {
        String nonExistingId = UUID.randomUUID().toString();
        when(medicalHistoryRepositoryPort.findById(any(MedicalHistoryId.class)))
                .thenReturn(Optional.empty());

        DomainException exception = assertThrows(DomainException.class,
                () -> medicalHistoryService.findById(nonExistingId));

        assertEquals("Historial médico no encontrado con ID: " + nonExistingId, exception.getMessage());
        verify(medicalHistoryRepositoryPort, times(1)).findById(any(MedicalHistoryId.class));
    }

    @Test
    void findByPatientId_WithExistingPatientId_ShouldReturnMedicalHistoryDTO() {
        when(medicalHistoryRepositoryPort.findByPatientId(existingPatientId))
                .thenReturn(Optional.of(existingMedicalHistory));

        MedicalHistoryDTO result = medicalHistoryService.findByPatientId(existingPatientId.getValue().toString());

        assertNotNull(result);
        assertEquals(existingPatientId.getValue().toString(), result.getPatientId());
        assertEquals(existingMedicalHistory.getAllergies(), result.getAllergies());
        assertEquals(existingMedicalHistory.getMedicalConditions(), result.getMedicalConditions());
        assertEquals(existingMedicalHistory.getMedications(), result.getMedications());
        assertEquals(existingMedicalHistory.getGeneralObservations(), result.getGeneralObservations());

        verify(medicalHistoryRepositoryPort, times(1)).findByPatientId(existingPatientId);
    }

    @Test
    void findByPatientId_WithNonExistingPatientId_ShouldThrowDomainException() {
        String nonExistingPatientId = UUID.randomUUID().toString();
        when(medicalHistoryRepositoryPort.findByPatientId(any(PatientId.class)))
                .thenReturn(Optional.empty());

        DomainException exception = assertThrows(DomainException.class,
                () -> medicalHistoryService.findByPatientId(nonExistingPatientId));

        assertEquals("Historial médico no encontrado para el paciente con ID: " + nonExistingPatientId, exception.getMessage());
        verify(medicalHistoryRepositoryPort, times(1)).findByPatientId(any(PatientId.class));
    }

    @Test
    void findAll_ShouldReturnListOfMedicalHistoryDTO() {
        List<MedicalHistory> histories = List.of(existingMedicalHistory);
        when(medicalHistoryRepositoryPort.findAll()).thenReturn(histories);

        List<MedicalHistoryDTO> result = medicalHistoryService.findAll();

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(existingPatientId.getValue().toString(), result.get(0).getPatientId());

        verify(medicalHistoryRepositoryPort, times(1)).findAll();
    }

    @Test
    void delete_WithExistingId_ShouldDeleteSuccessfully() {
        when(medicalHistoryRepositoryPort.existsById(existingHistoryId)).thenReturn(true);
        doNothing().when(medicalHistoryRepositoryPort).deleteById(existingHistoryId);

        medicalHistoryService.delete(existingHistoryId.getValue().toString());

        verify(medicalHistoryRepositoryPort, times(1)).existsById(existingHistoryId);
        verify(medicalHistoryRepositoryPort, times(1)).deleteById(existingHistoryId);
    }

    @Test
    void delete_WithNonExistingId_ShouldThrowDomainException() {
        String nonExistingId = UUID.randomUUID().toString();
        when(medicalHistoryRepositoryPort.existsById(any(MedicalHistoryId.class))).thenReturn(false);

        DomainException exception = assertThrows(DomainException.class,
                () -> medicalHistoryService.delete(nonExistingId));

        assertEquals("Historial médico no encontrado con ID: " + nonExistingId, exception.getMessage());
        verify(medicalHistoryRepositoryPort, times(1)).existsById(any(MedicalHistoryId.class));
        verify(medicalHistoryRepositoryPort, never()).deleteById(any());
    }

    @Test
    void addObservation_WithExistingId_ShouldAppendObservationAndReturnUpdatedDTO() {
        String newObservation = "Nueva observación agregada";
        when(medicalHistoryRepositoryPort.findById(existingHistoryId))
                .thenReturn(Optional.of(existingMedicalHistory));
        when(medicalHistoryRepositoryPort.save(any(MedicalHistory.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        MedicalHistoryDTO result = medicalHistoryService.addObservation(existingHistoryId.getValue().toString(), newObservation);

        assertNotNull(result);
        String expectedObservations = existingMedicalHistory.getGeneralObservations() + "\n" + newObservation;
        assertEquals(expectedObservations, result.getGeneralObservations());

        verify(medicalHistoryRepositoryPort, times(1)).findById(existingHistoryId);
        verify(medicalHistoryRepositoryPort, times(1)).save(any(MedicalHistory.class));
    }

    @Test
    void addObservation_WithExistingIdAndNullInitialObservations_ShouldSetObservation() {
        MedicalHistory historyWithNullObservations = MedicalHistory.builder()
                .patientId(existingPatientId)
                .allergies("Polen")
                .medicalConditions("Hipertensión")
                .medications("Lisinopril")
                .generalObservations(null)
                .build();

        String newObservation = "Primera observación";
        when(medicalHistoryRepositoryPort.findById(existingHistoryId))
                .thenReturn(Optional.of(historyWithNullObservations));
        when(medicalHistoryRepositoryPort.save(any(MedicalHistory.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        MedicalHistoryDTO result = medicalHistoryService.addObservation(existingHistoryId.getValue().toString(), newObservation);

        assertNotNull(result);
        assertEquals(newObservation, result.getGeneralObservations());

        verify(medicalHistoryRepositoryPort, times(1)).findById(existingHistoryId);
        verify(medicalHistoryRepositoryPort, times(1)).save(any(MedicalHistory.class));
    }

    @Test
    void addObservation_WithNonExistingId_ShouldThrowDomainException() {
        String nonExistingId = UUID.randomUUID().toString();
        when(medicalHistoryRepositoryPort.findById(any(MedicalHistoryId.class)))
                .thenReturn(Optional.empty());

        DomainException exception = assertThrows(DomainException.class,
                () -> medicalHistoryService.addObservation(nonExistingId, "Observación"));

        assertEquals("Historial médico no encontrado con ID: " + nonExistingId, exception.getMessage());
        verify(medicalHistoryRepositoryPort, times(1)).findById(any(MedicalHistoryId.class));
        verify(medicalHistoryRepositoryPort, never()).save(any());
    }
}