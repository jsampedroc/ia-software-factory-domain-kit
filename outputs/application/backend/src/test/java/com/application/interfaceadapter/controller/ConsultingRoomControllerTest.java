package com.application.interfaceadapter.controller;

import com.application.application.dto.ConsultingRoomDTO;
import com.application.application.service.ConsultingRoomService;
import com.application.domain.valueobject.ConsultingRoomId;
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
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ConsultingRoomControllerTest {

    @Mock
    private ConsultingRoomService consultingRoomService;

    @InjectMocks
    private ConsultingRoomController consultingRoomController;

    @Test
    void getAllConsultingRooms_ShouldReturnListOfConsultingRooms() {
        // Arrange
        ConsultingRoomDTO dto1 = mock(ConsultingRoomDTO.class);
        ConsultingRoomDTO dto2 = mock(ConsultingRoomDTO.class);
        List<ConsultingRoomDTO> expectedList = List.of(dto1, dto2);
        when(consultingRoomService.findAll()).thenReturn(expectedList);

        // Act
        ResponseEntity<List<ConsultingRoomDTO>> response = consultingRoomController.getAllConsultingRooms();

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(2, response.getBody().size());
        assertTrue(response.getBody().contains(dto1));
        assertTrue(response.getBody().contains(dto2));
        verify(consultingRoomService, times(1)).findAll();
    }

    @Test
    void getConsultingRoomById_WhenExists_ShouldReturnConsultingRoom() {
        // Arrange
        UUID uuid = UUID.randomUUID();
        ConsultingRoomId consultingRoomId = new ConsultingRoomId(uuid);
        ConsultingRoomDTO expectedDto = mock(ConsultingRoomDTO.class);
        when(consultingRoomService.findById(consultingRoomId)).thenReturn(Optional.of(expectedDto));

        // Act
        ResponseEntity<ConsultingRoomDTO> response = consultingRoomController.getConsultingRoomById(uuid);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertSame(expectedDto, response.getBody());
        verify(consultingRoomService, times(1)).findById(consultingRoomId);
    }

    @Test
    void getConsultingRoomById_WhenNotExists_ShouldReturnNotFound() {
        // Arrange
        UUID uuid = UUID.randomUUID();
        ConsultingRoomId consultingRoomId = new ConsultingRoomId(uuid);
        when(consultingRoomService.findById(consultingRoomId)).thenReturn(Optional.empty());

        // Act
        ResponseEntity<ConsultingRoomDTO> response = consultingRoomController.getConsultingRoomById(uuid);

        // Assert
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertNull(response.getBody());
        verify(consultingRoomService, times(1)).findById(consultingRoomId);
    }

    @Test
    void createConsultingRoom_ShouldReturnCreatedConsultingRoom() {
        // Arrange
        ConsultingRoomDTO inputDto = mock(ConsultingRoomDTO.class);
        ConsultingRoomDTO createdDto = mock(ConsultingRoomDTO.class);
        when(consultingRoomService.create(inputDto)).thenReturn(createdDto);

        // Act
        ResponseEntity<ConsultingRoomDTO> response = consultingRoomController.createConsultingRoom(inputDto);

        // Assert
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertSame(createdDto, response.getBody());
        verify(consultingRoomService, times(1)).create(inputDto);
    }

    @Test
    void updateConsultingRoom_ShouldReturnUpdatedConsultingRoom() {
        // Arrange
        UUID uuid = UUID.randomUUID();
        ConsultingRoomId consultingRoomId = new ConsultingRoomId(uuid);
        ConsultingRoomDTO inputDto = mock(ConsultingRoomDTO.class);
        ConsultingRoomDTO updatedDto = mock(ConsultingRoomDTO.class);
        when(consultingRoomService.update(consultingRoomId, inputDto)).thenReturn(updatedDto);

        // Act
        ResponseEntity<ConsultingRoomDTO> response = consultingRoomController.updateConsultingRoom(uuid, inputDto);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertSame(updatedDto, response.getBody());
        verify(consultingRoomService, times(1)).update(consultingRoomId, inputDto);
    }

    @Test
    void deleteConsultingRoom_ShouldCallServiceAndReturnNoContent() {
        // Arrange
        UUID uuid = UUID.randomUUID();
        ConsultingRoomId consultingRoomId = new ConsultingRoomId(uuid);
        doNothing().when(consultingRoomService).delete(consultingRoomId);

        // Act
        ResponseEntity<Void> response = consultingRoomController.deleteConsultingRoom(uuid);

        // Assert
        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
        assertNull(response.getBody());
        verify(consultingRoomService, times(1)).delete(consultingRoomId);
    }

    @Test
    void getConsultingRoomsByClinic_ShouldReturnList() {
        // Arrange
        UUID clinicUuid = UUID.randomUUID();
        ConsultingRoomDTO dto1 = mock(ConsultingRoomDTO.class);
        ConsultingRoomDTO dto2 = mock(ConsultingRoomDTO.class);
        List<ConsultingRoomDTO> expectedList = List.of(dto1, dto2);
        when(consultingRoomService.findByClinicId(clinicUuid)).thenReturn(expectedList);

        // Act
        ResponseEntity<List<ConsultingRoomDTO>> response = consultingRoomController.getConsultingRoomsByClinic(clinicUuid);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(2, response.getBody().size());
        verify(consultingRoomService, times(1)).findByClinicId(clinicUuid);
    }

    @Test
    void updateAvailability_ShouldReturnUpdatedConsultingRoom() {
        // Arrange
        UUID uuid = UUID.randomUUID();
        ConsultingRoomId consultingRoomId = new ConsultingRoomId(uuid);
        boolean newAvailability = true;
        ConsultingRoomDTO updatedDto = mock(ConsultingRoomDTO.class);
        when(consultingRoomService.updateAvailability(consultingRoomId, newAvailability)).thenReturn(updatedDto);

        // Act
        ResponseEntity<ConsultingRoomDTO> response = consultingRoomController.updateAvailability(uuid, newAvailability);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertSame(updatedDto, response.getBody());
        verify(consultingRoomService, times(1)).updateAvailability(consultingRoomId, newAvailability);
    }
}