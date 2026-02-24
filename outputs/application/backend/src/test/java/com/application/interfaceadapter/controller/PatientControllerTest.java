package com.application.interfaceadapter.controller;

import com.application.application.dto.PatientDTO;
import com.application.application.service.PatientService;
import com.application.domain.valueobject.PatientId;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PatientControllerTest {

    @Mock
    private PatientService patientService;

    @InjectMocks
    private PatientController patientController;

    private PatientId samplePatientId;
    private UUID sampleUUID;
    private PatientDTO samplePatientDTO;

    @BeforeEach
    void setUp() {
        sampleUUID = UUID.randomUUID();
        samplePatientId = new PatientId(sampleUUID);
        samplePatientDTO = new PatientDTO(
                sampleUUID,
                "12345678A",
                "Juan",
                "Pérez",
                LocalDate.of(1985, 5, 15),
                "600123456",
                "juan.perez@email.com",
                "Calle Falsa 123",
                LocalDateTime.now(),
                true
        );
    }

    @Test
    void getAllPatients_shouldReturnListOfPatients() {
        List<PatientDTO> patientList = Collections.singletonList(samplePatientDTO);
        when(patientService.findAll()).thenReturn(patientList);

        ResponseEntity<List<PatientDTO>> response = patientController.getAllPatients();

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(1, response.getBody().size());
        assertEquals(samplePatientDTO, response.getBody().get(0));
        verify(patientService, times(1)).findAll();
    }

    @Test
    void getPatientById_whenPatientExists_shouldReturnPatient() {
        when(patientService.findById(samplePatientId)).thenReturn(Optional.of(samplePatientDTO));

        ResponseEntity<PatientDTO> response = patientController.getPatientById(sampleUUID);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(samplePatientDTO, response.getBody());
        verify(patientService, times(1)).findById(samplePatientId);
    }

    @Test
    void getPatientById_whenPatientDoesNotExist_shouldReturnNotFound() {
        when(patientService.findById(samplePatientId)).thenReturn(Optional.empty());

        ResponseEntity<PatientDTO> response = patientController.getPatientById(sampleUUID);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertNull(response.getBody());
        verify(patientService, times(1)).findById(samplePatientId);
    }

    @Test
    void createPatient_shouldReturnCreatedPatient() {
        when(patientService.create(any(PatientDTO.class))).thenReturn(samplePatientDTO);

        ResponseEntity<PatientDTO> response = patientController.createPatient(samplePatientDTO);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(samplePatientDTO, response.getBody());
        verify(patientService, times(1)).create(samplePatientDTO);
    }

    @Test
    void updatePatient_shouldReturnUpdatedPatient() {
        when(patientService.update(eq(samplePatientId), any(PatientDTO.class))).thenReturn(samplePatientDTO);

        ResponseEntity<PatientDTO> response = patientController.updatePatient(sampleUUID, samplePatientDTO);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(samplePatientDTO, response.getBody());
        verify(patientService, times(1)).update(samplePatientId, samplePatientDTO);
    }

    @Test
    void deletePatient_shouldCallServiceAndReturnNoContent() {
        doNothing().when(patientService).delete(samplePatientId);

        ResponseEntity<Void> response = patientController.deletePatient(sampleUUID);

        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
        assertNull(response.getBody());
        verify(patientService, times(1)).delete(samplePatientId);
    }

    @Test
    void activatePatient_shouldReturnActivatedPatient() {
        when(patientService.activate(samplePatientId)).thenReturn(samplePatientDTO);

        ResponseEntity<PatientDTO> response = patientController.activatePatient(sampleUUID);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(samplePatientDTO, response.getBody());
        verify(patientService, times(1)).activate(samplePatientId);
    }

    @Test
    void deactivatePatient_shouldReturnDeactivatedPatient() {
        when(patientService.deactivate(samplePatientId)).thenReturn(samplePatientDTO);

        ResponseEntity<PatientDTO> response = patientController.deactivatePatient(sampleUUID);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(samplePatientDTO, response.getBody());
        verify(patientService, times(1)).deactivate(samplePatientId);
    }

    @Test
    void searchPatients_shouldReturnFilteredList() {
        List<PatientDTO> patientList = Collections.singletonList(samplePatientDTO);
        when(patientService.search("12345678A", "Juan", "Pérez", "juan.perez@email.com"))
                .thenReturn(patientList);

        ResponseEntity<List<PatientDTO>> response = patientController.searchPatients(
                "12345678A", "Juan", "Pérez", "juan.perez@email.com");

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(1, response.getBody().size());
        assertEquals(samplePatientDTO, response.getBody().get(0));
        verify(patientService, times(1))
                .search("12345678A", "Juan", "Pérez", "juan.perez@email.com");
    }

    @Test
    void searchPatients_withNullParameters_shouldCallServiceWithNulls() {
        List<PatientDTO> patientList = Collections.emptyList();
        when(patientService.search(null, null, null, null)).thenReturn(patientList);

        ResponseEntity<List<PatientDTO>> response = patientController.searchPatients(null, null, null, null);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertTrue(response.getBody().isEmpty());
        verify(patientService, times(1)).search(null, null, null, null);
    }

    @Test
    void getPatientAppointments_shouldReturnAppointmentsList() {
        List<?> appointments = Collections.singletonList("Appointment1");
        when(patientService.getAppointments(samplePatientId)).thenReturn(appointments);

        ResponseEntity<List<?>> response = patientController.getPatientAppointments(sampleUUID);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(1, response.getBody().size());
        verify(patientService, times(1)).getAppointments(samplePatientId);
    }

    @Test
    void getPatientInvoices_shouldReturnInvoicesList() {
        List<?> invoices = Collections.singletonList("Invoice1");
        when(patientService.getInvoices(samplePatientId)).thenReturn(invoices);

        ResponseEntity<List<?>> response = patientController.getPatientInvoices(sampleUUID);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(1, response.getBody().size());
        verify(patientService, times(1)).getInvoices(samplePatientId);
    }

    @Test
    void getPatientTreatmentPlans_shouldReturnTreatmentPlansList() {
        List<?> treatmentPlans = Collections.singletonList("Plan1");
        when(patientService.getTreatmentPlans(samplePatientId)).thenReturn(treatmentPlans);

        ResponseEntity<List<?>> response = patientController.getPatientTreatmentPlans(sampleUUID);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(1, response.getBody().size());
        verify(patientService, times(1)).getTreatmentPlans(samplePatientId);
    }

    @Test
    void getPatientMedicalHistory_whenHistoryExists_shouldReturnHistory() {
        Object medicalHistory = "MedicalHistory1";
        when(patientService.getMedicalHistory(samplePatientId)).thenReturn(Optional.of(medicalHistory));

        ResponseEntity<?> response = patientController.getPatientMedicalHistory(sampleUUID);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(medicalHistory, response.getBody());
        verify(patientService, times(1)).getMedicalHistory(samplePatientId);
    }

    @Test
    void getPatientMedicalHistory_whenHistoryDoesNotExist_shouldReturnNotFound() {
        when(patientService.getMedicalHistory(samplePatientId)).thenReturn(Optional.empty());

        ResponseEntity<?> response = patientController.getPatientMedicalHistory(sampleUUID);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertNull(response.getBody());
        verify(patientService, times(1)).getMedicalHistory(samplePatientId);
    }
}