package com.application.interfaceadapter.controller;

import com.application.application.dto.ClinicDTO;
import com.application.application.service.ClinicService;
import com.application.domain.valueobject.ClinicId;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.time.LocalTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ClinicControllerTest {

    @Mock
    private ClinicService clinicService;

    @InjectMocks
    private ClinicController clinicController;

    @Test
    void getAllClinics_ShouldReturnListOfClinics() {
        // Arrange
        ClinicDTO clinic1 = createSampleClinicDTO(UUID.randomUUID(), "CLI-001");
        ClinicDTO clinic2 = createSampleClinicDTO(UUID.randomUUID(), "CLI-002");
        List<ClinicDTO> expectedClinics = List.of(clinic1, clinic2);
        when(clinicService.findAll()).thenReturn(expectedClinics);

        // Act
        ResponseEntity<List<ClinicDTO>> response = clinicController.getAllClinics();

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(2, response.getBody().size());
        assertEquals(expectedClinics, response.getBody());
        verify(clinicService, times(1)).findAll();
    }

    @Test
    void getClinicById_WhenClinicExists_ShouldReturnClinic() {
        // Arrange
        UUID clinicUuid = UUID.randomUUID();
        ClinicId clinicId = new ClinicId(clinicUuid);
        ClinicDTO expectedClinic = createSampleClinicDTO(clinicUuid, "CLI-001");
        when(clinicService.findById(clinicId)).thenReturn(Optional.of(expectedClinic));

        // Act
        ResponseEntity<ClinicDTO> response = clinicController.getClinicById(clinicUuid);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(expectedClinic, response.getBody());
        verify(clinicService, times(1)).findById(clinicId);
    }

    @Test
    void getClinicById_WhenClinicDoesNotExist_ShouldReturnNotFound() {
        // Arrange
        UUID clinicUuid = UUID.randomUUID();
        ClinicId clinicId = new ClinicId(clinicUuid);
        when(clinicService.findById(clinicId)).thenReturn(Optional.empty());

        // Act
        ResponseEntity<ClinicDTO> response = clinicController.getClinicById(clinicUuid);

        // Assert
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertNull(response.getBody());
        verify(clinicService, times(1)).findById(clinicId);
    }

    @Test
    void createClinic_ShouldReturnCreatedClinic() {
        // Arrange
        ClinicDTO inputDTO = createSampleClinicDTO(null, "CLI-NEW");
        ClinicDTO createdDTO = createSampleClinicDTO(UUID.randomUUID(), "CLI-NEW");
        when(clinicService.create(inputDTO)).thenReturn(createdDTO);

        // Act
        ResponseEntity<ClinicDTO> response = clinicController.createClinic(inputDTO);

        // Assert
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(createdDTO, response.getBody());
        verify(clinicService, times(1)).create(inputDTO);
    }

    @Test
    void updateClinic_ShouldReturnUpdatedClinic() {
        // Arrange
        UUID clinicUuid = UUID.randomUUID();
        ClinicId clinicId = new ClinicId(clinicUuid);
        ClinicDTO inputDTO = createSampleClinicDTO(clinicUuid, "CLI-UPDATED");
        ClinicDTO updatedDTO = createSampleClinicDTO(clinicUuid, "CLI-UPDATED");
        when(clinicService.update(clinicId, inputDTO)).thenReturn(updatedDTO);

        // Act
        ResponseEntity<ClinicDTO> response = clinicController.updateClinic(clinicUuid, inputDTO);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(updatedDTO, response.getBody());
        verify(clinicService, times(1)).update(clinicId, inputDTO);
    }

    @Test
    void deleteClinic_ShouldCallServiceAndReturnNoContent() {
        // Arrange
        UUID clinicUuid = UUID.randomUUID();
        ClinicId clinicId = new ClinicId(clinicUuid);
        doNothing().when(clinicService).delete(clinicId);

        // Act
        ResponseEntity<Void> response = clinicController.deleteClinic(clinicUuid);

        // Assert
        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
        assertNull(response.getBody());
        verify(clinicService, times(1)).delete(clinicId);
    }

    @Test
    void getActiveClinics_ShouldReturnListOfActiveClinics() {
        // Arrange
        ClinicDTO activeClinic1 = createSampleClinicDTO(UUID.randomUUID(), "CLI-ACT-001");
        ClinicDTO activeClinic2 = createSampleClinicDTO(UUID.randomUUID(), "CLI-ACT-002");
        List<ClinicDTO> expectedActiveClinics = List.of(activeClinic1, activeClinic2);
        when(clinicService.findActiveClinics()).thenReturn(expectedActiveClinics);

        // Act
        ResponseEntity<List<ClinicDTO>> response = clinicController.getActiveClinics();

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(2, response.getBody().size());
        assertEquals(expectedActiveClinics, response.getBody());
        verify(clinicService, times(1)).findActiveClinics();
    }

    @Test
    void activateClinic_ShouldReturnActivatedClinic() {
        // Arrange
        UUID clinicUuid = UUID.randomUUID();
        ClinicId clinicId = new ClinicId(clinicUuid);
        ClinicDTO activatedDTO = createSampleClinicDTO(clinicUuid, "CLI-001");
        activatedDTO.setActiva(true);
        when(clinicService.activate(clinicId)).thenReturn(activatedDTO);

        // Act
        ResponseEntity<ClinicDTO> response = clinicController.activateClinic(clinicUuid);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertTrue(response.getBody().isActiva());
        assertEquals(activatedDTO, response.getBody());
        verify(clinicService, times(1)).activate(clinicId);
    }

    @Test
    void deactivateClinic_ShouldReturnDeactivatedClinic() {
        // Arrange
        UUID clinicUuid = UUID.randomUUID();
        ClinicId clinicId = new ClinicId(clinicUuid);
        ClinicDTO deactivatedDTO = createSampleClinicDTO(clinicUuid, "CLI-001");
        deactivatedDTO.setActiva(false);
        when(clinicService.deactivate(clinicId)).thenReturn(deactivatedDTO);

        // Act
        ResponseEntity<ClinicDTO> response = clinicController.deactivateClinic(clinicUuid);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertFalse(response.getBody().isActiva());
        assertEquals(deactivatedDTO, response.getBody());
        verify(clinicService, times(1)).deactivate(clinicId);
    }

    private ClinicDTO createSampleClinicDTO(UUID id, String code) {
        ClinicDTO dto = new ClinicDTO();
        if (id != null) {
            dto.setId(id);
        }
        dto.setCodigo(code);
        dto.setNombre("Clínica " + code);
        dto.setDireccion("Calle Principal 123");
        dto.setTelefono("+1234567890");
        dto.setEmail("contacto@" + code.toLowerCase() + ".com");
        dto.setHorarioApertura(LocalTime.of(8, 0));
        dto.setHorarioCierre(LocalTime.of(18, 0));
        dto.setActiva(true);
        return dto;
    }
}