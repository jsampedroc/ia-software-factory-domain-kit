package com.application.interfaceadapter.controller;

import com.application.application.dto.TreatmentDTO;
import com.application.application.service.TreatmentService;
import com.application.domain.valueobject.TreatmentId;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TreatmentControllerTest {

    @Mock
    private TreatmentService treatmentService;

    @InjectMocks
    private TreatmentController treatmentController;

    private TreatmentId treatmentId;
    private UUID uuid;
    private TreatmentDTO treatmentDTO;

    @BeforeEach
    void setUp() {
        uuid = UUID.randomUUID();
        treatmentId = new TreatmentId(uuid);
        treatmentDTO = new TreatmentDTO(
                uuid,
                "TR-001",
                "Limpieza Dental",
                "Limpieza profesional de dientes",
                30,
                new BigDecimal("50.00"),
                true
        );
    }

    @Test
    void getAllTreatments_shouldReturnListOfTreatments() {
        List<TreatmentDTO> expectedList = List.of(treatmentDTO);
        when(treatmentService.findAll()).thenReturn(expectedList);

        ResponseEntity<List<TreatmentDTO>> response = treatmentController.getAllTreatments();

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(expectedList, response.getBody());
        verify(treatmentService, times(1)).findAll();
    }

    @Test
    void getTreatmentById_whenTreatmentExists_shouldReturnTreatment() {
        when(treatmentService.findById(treatmentId)).thenReturn(Optional.of(treatmentDTO));

        ResponseEntity<TreatmentDTO> response = treatmentController.getTreatmentById(uuid);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(treatmentDTO, response.getBody());
        verify(treatmentService, times(1)).findById(treatmentId);
    }

    @Test
    void getTreatmentById_whenTreatmentDoesNotExist_shouldReturnNotFound() {
        when(treatmentService.findById(treatmentId)).thenReturn(Optional.empty());

        ResponseEntity<TreatmentDTO> response = treatmentController.getTreatmentById(uuid);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertNull(response.getBody());
        verify(treatmentService, times(1)).findById(treatmentId);
    }

    @Test
    void getActiveTreatments_shouldReturnListOfActiveTreatments() {
        List<TreatmentDTO> expectedList = List.of(treatmentDTO);
        when(treatmentService.findActiveTreatments()).thenReturn(expectedList);

        ResponseEntity<List<TreatmentDTO>> response = treatmentController.getActiveTreatments();

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(expectedList, response.getBody());
        verify(treatmentService, times(1)).findActiveTreatments();
    }

    @Test
    void createTreatment_shouldReturnCreatedTreatment() {
        when(treatmentService.create(treatmentDTO)).thenReturn(treatmentDTO);

        ResponseEntity<TreatmentDTO> response = treatmentController.createTreatment(treatmentDTO);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals(treatmentDTO, response.getBody());
        verify(treatmentService, times(1)).create(treatmentDTO);
    }

    @Test
    void updateTreatment_shouldReturnUpdatedTreatment() {
        when(treatmentService.update(eq(treatmentId), any(TreatmentDTO.class))).thenReturn(treatmentDTO);

        ResponseEntity<TreatmentDTO> response = treatmentController.updateTreatment(uuid, treatmentDTO);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(treatmentDTO, response.getBody());
        verify(treatmentService, times(1)).update(eq(treatmentId), any(TreatmentDTO.class));
    }

    @Test
    void deleteTreatment_shouldCallServiceAndReturnNoContent() {
        doNothing().when(treatmentService).delete(treatmentId);

        ResponseEntity<Void> response = treatmentController.deleteTreatment(uuid);

        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
        assertNull(response.getBody());
        verify(treatmentService, times(1)).delete(treatmentId);
    }

    @Test
    void activateTreatment_shouldReturnActivatedTreatment() {
        when(treatmentService.activate(treatmentId)).thenReturn(treatmentDTO);

        ResponseEntity<TreatmentDTO> response = treatmentController.activateTreatment(uuid);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(treatmentDTO, response.getBody());
        verify(treatmentService, times(1)).activate(treatmentId);
    }

    @Test
    void deactivateTreatment_shouldReturnDeactivatedTreatment() {
        when(treatmentService.deactivate(treatmentId)).thenReturn(treatmentDTO);

        ResponseEntity<TreatmentDTO> response = treatmentController.deactivateTreatment(uuid);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(treatmentDTO, response.getBody());
        verify(treatmentService, times(1)).deactivate(treatmentId);
    }

    @Test
    void searchTreatments_withCodeAndName_shouldReturnFilteredList() {
        List<TreatmentDTO> expectedList = List.of(treatmentDTO);
        when(treatmentService.searchTreatments("TR-001", "Limpieza")).thenReturn(expectedList);

        ResponseEntity<List<TreatmentDTO>> response = treatmentController.searchTreatments("TR-001", "Limpieza");

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(expectedList, response.getBody());
        verify(treatmentService, times(1)).searchTreatments("TR-001", "Limpieza");
    }

    @Test
    void searchTreatments_withCodeOnly_shouldReturnFilteredList() {
        List<TreatmentDTO> expectedList = List.of(treatmentDTO);
        when(treatmentService.searchTreatments("TR-001", null)).thenReturn(expectedList);

        ResponseEntity<List<TreatmentDTO>> response = treatmentController.searchTreatments("TR-001", null);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(expectedList, response.getBody());
        verify(treatmentService, times(1)).searchTreatments("TR-001", null);
    }

    @Test
    void searchTreatments_withNameOnly_shouldReturnFilteredList() {
        List<TreatmentDTO> expectedList = List.of(treatmentDTO);
        when(treatmentService.searchTreatments(null, "Limpieza")).thenReturn(expectedList);

        ResponseEntity<List<TreatmentDTO>> response = treatmentController.searchTreatments(null, "Limpieza");

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(expectedList, response.getBody());
        verify(treatmentService, times(1)).searchTreatments(null, "Limpieza");
    }

    @Test
    void searchTreatments_withNoParameters_shouldReturnAllTreatments() {
        List<TreatmentDTO> expectedList = List.of(treatmentDTO);
        when(treatmentService.searchTreatments(null, null)).thenReturn(expectedList);

        ResponseEntity<List<TreatmentDTO>> response = treatmentController.searchTreatments(null, null);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(expectedList, response.getBody());
        verify(treatmentService, times(1)).searchTreatments(null, null);
    }
}