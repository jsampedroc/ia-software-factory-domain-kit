package com.application.interfaceadapter.controller;

import com.application.application.dto.DentistDTO;
import com.application.application.service.DentistService;
import com.application.domain.valueobject.DentistId;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class DentistControllerTest {

    @Mock
    private DentistService dentistService;

    @InjectMocks
    private DentistController dentistController;

    private UUID testUuid;
    private DentistId testDentistId;
    private DentistDTO testDentistDTO;

    @BeforeEach
    void setUp() {
        testUuid = UUID.randomUUID();
        testDentistId = new DentistId(testUuid);
        testDentistDTO = new DentistDTO(
                testUuid,
                "MED-12345",
                "Juan",
                "Pérez",
                "555-1234",
                "juan.perez@clinica.com",
                LocalDate.of(2020, 1, 15),
                true
        );
    }

    @Test
    void getAllDentists_ShouldReturnListOfDentists() {
        List<DentistDTO> dentistList = List.of(testDentistDTO);
        when(dentistService.findAll()).thenReturn(dentistList);

        ResponseEntity<List<DentistDTO>> response = dentistController.getAllDentists();

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(dentistList, response.getBody());
        verify(dentistService, times(1)).findAll();
    }

    @Test
    void getActiveDentists_ShouldReturnListOfActiveDentists() {
        List<DentistDTO> activeDentistList = List.of(testDentistDTO);
        when(dentistService.findActiveDentists()).thenReturn(activeDentistList);

        ResponseEntity<List<DentistDTO>> response = dentistController.getActiveDentists();

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(activeDentistList, response.getBody());
        verify(dentistService, times(1)).findActiveDentists();
    }

    @Test
    void getDentistById_WhenDentistExists_ShouldReturnDentist() {
        when(dentistService.findById(testDentistId)).thenReturn(Optional.of(testDentistDTO));

        ResponseEntity<DentistDTO> response = dentistController.getDentistById(testUuid);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(testDentistDTO, response.getBody());
        verify(dentistService, times(1)).findById(testDentistId);
    }

    @Test
    void getDentistById_WhenDentistDoesNotExist_ShouldReturnNotFound() {
        when(dentistService.findById(testDentistId)).thenReturn(Optional.empty());

        ResponseEntity<DentistDTO> response = dentistController.getDentistById(testUuid);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertNull(response.getBody());
        verify(dentistService, times(1)).findById(testDentistId);
    }

    @Test
    void createDentist_ShouldReturnCreatedDentist() {
        when(dentistService.create(any(DentistDTO.class))).thenReturn(testDentistDTO);

        ResponseEntity<DentistDTO> response = dentistController.createDentist(testDentistDTO);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals(testDentistDTO, response.getBody());
        verify(dentistService, times(1)).create(testDentistDTO);
    }

    @Test
    void updateDentist_ShouldReturnUpdatedDentist() {
        when(dentistService.update(eq(testDentistId), any(DentistDTO.class))).thenReturn(testDentistDTO);

        ResponseEntity<DentistDTO> response = dentistController.updateDentist(testUuid, testDentistDTO);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(testDentistDTO, response.getBody());
        verify(dentistService, times(1)).update(testDentistId, testDentistDTO);
    }

    @Test
    void deleteDentist_ShouldCallServiceAndReturnNoContent() {
        doNothing().when(dentistService).delete(testDentistId);

        ResponseEntity<Void> response = dentistController.deleteDentist(testUuid);

        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
        assertNull(response.getBody());
        verify(dentistService, times(1)).delete(testDentistId);
    }

    @Test
    void activateDentist_ShouldReturnActivatedDentist() {
        when(dentistService.activate(testDentistId)).thenReturn(testDentistDTO);

        ResponseEntity<DentistDTO> response = dentistController.activateDentist(testUuid);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(testDentistDTO, response.getBody());
        verify(dentistService, times(1)).activate(testDentistId);
    }

    @Test
    void deactivateDentist_ShouldReturnDeactivatedDentist() {
        when(dentistService.deactivate(testDentistId)).thenReturn(testDentistDTO);

        ResponseEntity<DentistDTO> response = dentistController.deactivateDentist(testUuid);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(testDentistDTO, response.getBody());
        verify(dentistService, times(1)).deactivate(testDentistId);
    }

    @Test
    void searchDentists_ShouldReturnListOfDentists() {
        List<DentistDTO> searchResults = List.of(testDentistDTO);
        when(dentistService.searchDentists("MED-12345", "Juan", "Ortodoncia")).thenReturn(searchResults);

        ResponseEntity<List<DentistDTO>> response = dentistController.searchDentists("MED-12345", "Juan", "Ortodoncia");

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(searchResults, response.getBody());
        verify(dentistService, times(1)).searchDentists("MED-12345", "Juan", "Ortodoncia");
    }

    @Test
    void searchDentists_WithNullParameters_ShouldCallServiceWithNulls() {
        List<DentistDTO> searchResults = List.of(testDentistDTO);
        when(dentistService.searchDentists(null, null, null)).thenReturn(searchResults);

        ResponseEntity<List<DentistDTO>> response = dentistController.searchDentists(null, null, null);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(searchResults, response.getBody());
        verify(dentistService, times(1)).searchDentists(null, null, null);
    }

    @Test
    void getDentistsByClinic_ShouldReturnListOfDentists() {
        UUID clinicUuid = UUID.randomUUID();
        List<DentistDTO> clinicDentists = List.of(testDentistDTO);
        when(dentistService.findDentistsByClinic(clinicUuid)).thenReturn(clinicDentists);

        ResponseEntity<List<DentistDTO>> response = dentistController.getDentistsByClinic(clinicUuid);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(clinicDentists, response.getBody());
        verify(dentistService, times(1)).findDentistsByClinic(clinicUuid);
    }

    @Test
    void getDentistSpecialties_ShouldReturnListOfSpecialtyNames() {
        List<String> specialties = List.of("Ortodoncia", "Periodoncia");
        when(dentistService.getDentistSpecialties(testDentistId)).thenReturn(specialties);

        ResponseEntity<List<String>> response = dentistController.getDentistSpecialties(testUuid);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(specialties, response.getBody());
        verify(dentistService, times(1)).getDentistSpecialties(testDentistId);
    }
}