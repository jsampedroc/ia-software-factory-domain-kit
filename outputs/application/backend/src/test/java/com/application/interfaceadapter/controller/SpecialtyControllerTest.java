package com.application.interfaceadapter.controller;

import com.application.application.dto.SpecialtyDTO;
import com.application.application.service.SpecialtyService;
import com.application.domain.valueobject.SpecialtyId;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class SpecialtyControllerTest {

    @Mock
    private SpecialtyService specialtyService;

    @InjectMocks
    private SpecialtyController specialtyController;

    @Test
    void getAllSpecialties_ShouldReturnListOfSpecialties() {
        // Arrange
        SpecialtyDTO specialty1 = new SpecialtyDTO(UUID.randomUUID(), "ORT", "Ortodoncia", "Especialidad en ortodoncia");
        SpecialtyDTO specialty2 = new SpecialtyDTO(UUID.randomUUID(), "PER", "Periodoncia", "Especialidad en periodoncia");
        List<SpecialtyDTO> expectedSpecialties = List.of(specialty1, specialty2);
        when(specialtyService.findAll()).thenReturn(expectedSpecialties);

        // Act
        ResponseEntity<List<SpecialtyDTO>> response = specialtyController.getAllSpecialties();

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(2, response.getBody().size());
        assertEquals(expectedSpecialties, response.getBody());
        verify(specialtyService, times(1)).findAll();
    }

    @Test
    void getSpecialtyById_WhenSpecialtyExists_ShouldReturnSpecialty() {
        // Arrange
        UUID specialtyUuid = UUID.randomUUID();
        SpecialtyId specialtyId = new SpecialtyId(specialtyUuid);
        SpecialtyDTO expectedSpecialty = new SpecialtyDTO(specialtyUuid, "ORT", "Ortodoncia", "Descripción");
        when(specialtyService.findById(specialtyId)).thenReturn(Optional.of(expectedSpecialty));

        // Act
        ResponseEntity<SpecialtyDTO> response = specialtyController.getSpecialtyById(specialtyUuid);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(expectedSpecialty, response.getBody());
        verify(specialtyService, times(1)).findById(specialtyId);
    }

    @Test
    void getSpecialtyById_WhenSpecialtyDoesNotExist_ShouldReturnNotFound() {
        // Arrange
        UUID specialtyUuid = UUID.randomUUID();
        SpecialtyId specialtyId = new SpecialtyId(specialtyUuid);
        when(specialtyService.findById(specialtyId)).thenReturn(Optional.empty());

        // Act
        ResponseEntity<SpecialtyDTO> response = specialtyController.getSpecialtyById(specialtyUuid);

        // Assert
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertNull(response.getBody());
        verify(specialtyService, times(1)).findById(specialtyId);
    }

    @Test
    void createSpecialty_ShouldReturnCreatedSpecialty() {
        // Arrange
        SpecialtyDTO requestDto = new SpecialtyDTO(null, "END", "Endodoncia", "Tratamiento de conductos");
        SpecialtyDTO createdDto = new SpecialtyDTO(UUID.randomUUID(), "END", "Endodoncia", "Tratamiento de conductos");
        when(specialtyService.create(requestDto)).thenReturn(createdDto);

        // Act
        ResponseEntity<SpecialtyDTO> response = specialtyController.createSpecialty(requestDto);

        // Assert
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(createdDto, response.getBody());
        verify(specialtyService, times(1)).create(requestDto);
    }

    @Test
    void updateSpecialty_WhenSpecialtyExists_ShouldReturnUpdatedSpecialty() {
        // Arrange
        UUID specialtyUuid = UUID.randomUUID();
        SpecialtyId specialtyId = new SpecialtyId(specialtyUuid);
        SpecialtyDTO requestDto = new SpecialtyDTO(null, "ORT_UPD", "Ortodoncia Actualizada", "Descripción actualizada");
        SpecialtyDTO updatedDto = new SpecialtyDTO(specialtyUuid, "ORT_UPD", "Ortodoncia Actualizada", "Descripción actualizada");
        when(specialtyService.update(eq(specialtyId), any(SpecialtyDTO.class))).thenReturn(updatedDto);

        // Act
        ResponseEntity<SpecialtyDTO> response = specialtyController.updateSpecialty(specialtyUuid, requestDto);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(updatedDto, response.getBody());
        verify(specialtyService, times(1)).update(eq(specialtyId), any(SpecialtyDTO.class));
    }

    @Test
    void deleteSpecialty_ShouldCallServiceAndReturnNoContent() {
        // Arrange
        UUID specialtyUuid = UUID.randomUUID();
        SpecialtyId specialtyId = new SpecialtyId(specialtyUuid);
        doNothing().when(specialtyService).delete(specialtyId);

        // Act
        ResponseEntity<Void> response = specialtyController.deleteSpecialty(specialtyUuid);

        // Assert
        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
        assertNull(response.getBody());
        verify(specialtyService, times(1)).delete(specialtyId);
    }

    @Test
    void getActiveSpecialties_ShouldReturnListOfActiveSpecialties() {
        // Arrange
        SpecialtyDTO activeSpecialty1 = new SpecialtyDTO(UUID.randomUUID(), "ORT", "Ortodoncia", "Descripción");
        SpecialtyDTO activeSpecialty2 = new SpecialtyDTO(UUID.randomUUID(), "PER", "Periodoncia", "Descripción");
        List<SpecialtyDTO> expectedActiveSpecialties = List.of(activeSpecialty1, activeSpecialty2);
        when(specialtyService.findActiveSpecialties()).thenReturn(expectedActiveSpecialties);

        // Act
        ResponseEntity<List<SpecialtyDTO>> response = specialtyController.getActiveSpecialties();

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(2, response.getBody().size());
        assertEquals(expectedActiveSpecialties, response.getBody());
        verify(specialtyService, times(1)).findActiveSpecialties();
    }

    @Test
    void searchSpecialties_WithCodeAndName_ShouldReturnFilteredList() {
        // Arrange
        String code = "ORT";
        String name = "Ortodoncia";
        SpecialtyDTO foundSpecialty = new SpecialtyDTO(UUID.randomUUID(), code, name, "Descripción");
        List<SpecialtyDTO> expectedSpecialties = List.of(foundSpecialty);
        when(specialtyService.searchByCriteria(code, name)).thenReturn(expectedSpecialties);

        // Act
        ResponseEntity<List<SpecialtyDTO>> response = specialtyController.searchSpecialties(code, name);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(1, response.getBody().size());
        assertEquals(expectedSpecialties, response.getBody());
        verify(specialtyService, times(1)).searchByCriteria(code, name);
    }

    @Test
    void searchSpecialties_WithCodeOnly_ShouldReturnFilteredList() {
        // Arrange
        String code = "PER";
        SpecialtyDTO foundSpecialty = new SpecialtyDTO(UUID.randomUUID(), code, "Periodoncia", "Descripción");
        List<SpecialtyDTO> expectedSpecialties = List.of(foundSpecialty);
        when(specialtyService.searchByCriteria(code, null)).thenReturn(expectedSpecialties);

        // Act
        ResponseEntity<List<SpecialtyDTO>> response = specialtyController.searchSpecialties(code, null);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(1, response.getBody().size());
        assertEquals(expectedSpecialties, response.getBody());
        verify(specialtyService, times(1)).searchByCriteria(code, null);
    }

    @Test
    void searchSpecialties_WithNameOnly_ShouldReturnFilteredList() {
        // Arrange
        String name = "Endodoncia";
        SpecialtyDTO foundSpecialty = new SpecialtyDTO(UUID.randomUUID(), "END", name, "Descripción");
        List<SpecialtyDTO> expectedSpecialties = List.of(foundSpecialty);
        when(specialtyService.searchByCriteria(null, name)).thenReturn(expectedSpecialties);

        // Act
        ResponseEntity<List<SpecialtyDTO>> response = specialtyController.searchSpecialties(null, name);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(1, response.getBody().size());
        assertEquals(expectedSpecialties, response.getBody());
        verify(specialtyService, times(1)).searchByCriteria(null, name);
    }

    @Test
    void searchSpecialties_WithNoParameters_ShouldReturnAll() {
        // Arrange
        SpecialtyDTO specialty1 = new SpecialtyDTO(UUID.randomUUID(), "ORT", "Ortodoncia", "Descripción");
        SpecialtyDTO specialty2 = new SpecialtyDTO(UUID.randomUUID(), "PER", "Periodoncia", "Descripción");
        List<SpecialtyDTO> expectedSpecialties = List.of(specialty1, specialty2);
        when(specialtyService.searchByCriteria(null, null)).thenReturn(expectedSpecialties);

        // Act
        ResponseEntity<List<SpecialtyDTO>> response = specialtyController.searchSpecialties(null, null);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(2, response.getBody().size());
        assertEquals(expectedSpecialties, response.getBody());
        verify(specialtyService, times(1)).searchByCriteria(null, null);
    }
}