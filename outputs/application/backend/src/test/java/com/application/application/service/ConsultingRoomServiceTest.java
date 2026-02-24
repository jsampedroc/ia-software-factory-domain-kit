package com.application.application.service;

import com.application.application.dto.ConsultingRoomDTO;
import com.application.domain.exception.DomainException;
import com.application.domain.model.ConsultingRoom;
import com.application.domain.port.ConsultingRoomRepositoryPort;
import com.application.domain.valueobject.ClinicId;
import com.application.domain.valueobject.ConsultingRoomId;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ConsultingRoomServiceTest {

    @Mock
    private ConsultingRoomRepositoryPort consultingRoomRepository;

    @InjectMocks
    private ConsultingRoomService consultingRoomService;

    private ConsultingRoomId existingRoomId;
    private ClinicId clinicId;
    private ConsultingRoomDTO validDTO;
    private ConsultingRoomDTO validDTONoId;
    private ConsultingRoom domainEntity;

    @BeforeEach
    void setUp() {
        existingRoomId = new ConsultingRoomId(UUID.randomUUID());
        clinicId = new ClinicId(UUID.randomUUID());

        validDTO = new ConsultingRoomDTO(
                existingRoomId.value(),
                clinicId.value(),
                "101",
                "Consultorio Principal",
                "Sillón dental, Rayos X",
                true
        );

        validDTONoId = new ConsultingRoomDTO(
                null,
                clinicId.value(),
                "102",
                "Consultorio Secundario",
                "Sillón dental",
                false
        );

        domainEntity = new ConsultingRoom(
                existingRoomId,
                clinicId,
                "101",
                "Consultorio Principal",
                "Sillón dental, Rayos X",
                true
        );
    }

    @Test
    void create_WithValidDTO_ShouldReturnSavedDTO() {
        ConsultingRoom domainToSave = new ConsultingRoom(
                null,
                clinicId,
                "102",
                "Consultorio Secundario",
                "Sillón dental",
                false
        );
        ConsultingRoom savedDomain = new ConsultingRoom(
                existingRoomId,
                clinicId,
                "102",
                "Consultorio Secundario",
                "Sillón dental",
                false
        );

        when(consultingRoomRepository.save(any(ConsultingRoom.class))).thenReturn(savedDomain);

        ConsultingRoomDTO result = consultingRoomService.create(validDTONoId);

        assertNotNull(result);
        assertEquals(existingRoomId.value(), result.id());
        assertEquals("102", result.numero());
        verify(consultingRoomRepository, times(1)).save(any(ConsultingRoom.class));
    }

    @Test
    void create_WithNullDTO_ShouldThrowDomainException() {
        DomainException exception = assertThrows(DomainException.class,
                () -> consultingRoomService.create(null));
        assertEquals("ConsultingRoomDTO cannot be null", exception.getMessage());
        verify(consultingRoomRepository, never()).save(any());
    }

    @Test
    void create_WithBlankNumber_ShouldThrowDomainException() {
        ConsultingRoomDTO invalidDTO = new ConsultingRoomDTO(null, clinicId.value(), " ", "Name", "Equip", true);
        DomainException exception = assertThrows(DomainException.class,
                () -> consultingRoomService.create(invalidDTO));
        assertEquals("ConsultingRoom number is required", exception.getMessage());
        verify(consultingRoomRepository, never()).save(any());
    }

    @Test
    void create_WithBlankName_ShouldThrowDomainException() {
        ConsultingRoomDTO invalidDTO = new ConsultingRoomDTO(null, clinicId.value(), "101", " ", "Equip", true);
        DomainException exception = assertThrows(DomainException.class,
                () -> consultingRoomService.create(invalidDTO));
        assertEquals("ConsultingRoom name is required", exception.getMessage());
        verify(consultingRoomRepository, never()).save(any());
    }

    @Test
    void create_WithNullClinicId_ShouldThrowDomainException() {
        ConsultingRoomDTO invalidDTO = new ConsultingRoomDTO(null, null, "101", "Name", "Equip", true);
        DomainException exception = assertThrows(DomainException.class,
                () -> consultingRoomService.create(invalidDTO));
        assertEquals("Clinic ID is required", exception.getMessage());
        verify(consultingRoomRepository, never()).save(any());
    }

    @Test
    void update_WithValidIdAndDTO_ShouldReturnUpdatedDTO() {
        when(consultingRoomRepository.findById(existingRoomId)).thenReturn(Optional.of(domainEntity));
        ConsultingRoom updatedDomain = new ConsultingRoom(
                existingRoomId,
                clinicId,
                "101-A",
                "Consultorio Actualizado",
                "Nuevo equipamiento",
                false
        );
        when(consultingRoomRepository.save(any(ConsultingRoom.class))).thenReturn(updatedDomain);

        ConsultingRoomDTO updateDTO = new ConsultingRoomDTO(
                null,
                clinicId.value(),
                "101-A",
                "Consultorio Actualizado",
                "Nuevo equipamiento",
                false
        );

        ConsultingRoomDTO result = consultingRoomService.update(existingRoomId, updateDTO);

        assertNotNull(result);
        assertEquals(existingRoomId.value(), result.id());
        assertEquals("101-A", result.numero());
        assertEquals("Consultorio Actualizado", result.nombre());
        assertFalse(result.disponible());
        verify(consultingRoomRepository, times(1)).findById(existingRoomId);
        verify(consultingRoomRepository, times(1)).save(any(ConsultingRoom.class));
    }

    @Test
    void update_WithNonExistingId_ShouldThrowDomainException() {
        when(consultingRoomRepository.findById(existingRoomId)).thenReturn(Optional.empty());

        DomainException exception = assertThrows(DomainException.class,
                () -> consultingRoomService.update(existingRoomId, validDTO));
        assertEquals("ConsultingRoom not found with id: " + existingRoomId.value(), exception.getMessage());
        verify(consultingRoomRepository, never()).save(any());
    }

    @Test
    void delete_WithExistingId_ShouldCallRepositoryDelete() {
        when(consultingRoomRepository.existsById(existingRoomId)).thenReturn(true);
        doNothing().when(consultingRoomRepository).deleteById(existingRoomId);

        consultingRoomService.delete(existingRoomId);

        verify(consultingRoomRepository, times(1)).existsById(existingRoomId);
        verify(consultingRoomRepository, times(1)).deleteById(existingRoomId);
    }

    @Test
    void delete_WithNonExistingId_ShouldThrowDomainException() {
        when(consultingRoomRepository.existsById(existingRoomId)).thenReturn(false);

        DomainException exception = assertThrows(DomainException.class,
                () -> consultingRoomService.delete(existingRoomId));
        assertEquals("ConsultingRoom not found with id: " + existingRoomId.value(), exception.getMessage());
        verify(consultingRoomRepository, never()).deleteById(any());
    }

    @Test
    void findById_WithExistingId_ShouldReturnDTO() {
        when(consultingRoomRepository.findById(existingRoomId)).thenReturn(Optional.of(domainEntity));

        ConsultingRoomDTO result = consultingRoomService.findById(existingRoomId);

        assertNotNull(result);
        assertEquals(existingRoomId.value(), result.id());
        assertEquals("101", result.numero());
        verify(consultingRoomRepository, times(1)).findById(existingRoomId);
    }

    @Test
    void findById_WithNonExistingId_ShouldThrowDomainException() {
        when(consultingRoomRepository.findById(existingRoomId)).thenReturn(Optional.empty());

        DomainException exception = assertThrows(DomainException.class,
                () -> consultingRoomService.findById(existingRoomId));
        assertEquals("ConsultingRoom not found with id: " + existingRoomId.value(), exception.getMessage());
    }

    @Test
    void findAll_ShouldReturnListOfDTOs() {
        List<ConsultingRoom> rooms = List.of(domainEntity);
        when(consultingRoomRepository.findAll()).thenReturn(rooms);

        List<ConsultingRoomDTO> result = consultingRoomService.findAll();

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(existingRoomId.value(), result.get(0).id());
        verify(consultingRoomRepository, times(1)).findAll();
    }

    @Test
    void findByClinicId_ShouldReturnFilteredList() {
        List<ConsultingRoom> rooms = List.of(domainEntity);
        when(consultingRoomRepository.findByClinicId(clinicId)).thenReturn(rooms);

        List<ConsultingRoomDTO> result = consultingRoomService.findByClinicId(clinicId);

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(clinicId.value(), result.get(0).clinicId());
        verify(consultingRoomRepository, times(1)).findByClinicId(clinicId);
    }

    @Test
    void findAvailableByClinicId_ShouldReturnFilteredList() {
        List<ConsultingRoom> rooms = List.of(domainEntity);
        when(consultingRoomRepository.findAvailableByClinicId(clinicId)).thenReturn(rooms);

        List<ConsultingRoomDTO> result = consultingRoomService.findAvailableByClinicId(clinicId);

        assertNotNull(result);
        assertEquals(1, result.size());
        assertTrue(result.get(0).disponible());
        verify(consultingRoomRepository, times(1)).findAvailableByClinicId(clinicId);
    }

    @Test
    void markAsAvailable_WithExistingId_ShouldUpdateAndReturnDTO() {
        ConsultingRoom unavailableRoom = new ConsultingRoom(
                existingRoomId,
                clinicId,
                "101",
                "Consultorio",
                "Equip",
                false
        );
        ConsultingRoom availableRoom = new ConsultingRoom(
                existingRoomId,
                clinicId,
                "101",
                "Consultorio",
                "Equip",
                true
        );

        when(consultingRoomRepository.findById(existingRoomId)).thenReturn(Optional.of(unavailableRoom));
        when(consultingRoomRepository.save(any(ConsultingRoom.class))).thenReturn(availableRoom);

        ConsultingRoomDTO result = consultingRoomService.markAsAvailable(existingRoomId);

        assertNotNull(result);
        assertTrue(result.disponible());
        verify(consultingRoomRepository, times(1)).findById(existingRoomId);
        verify(consultingRoomRepository, times(1)).save(any(ConsultingRoom.class));
    }

    @Test
    void markAsAvailable_WithNonExistingId_ShouldThrowDomainException() {
        when(consultingRoomRepository.findById(existingRoomId)).thenReturn(Optional.empty());

        DomainException exception = assertThrows(DomainException.class,
                () -> consultingRoomService.markAsAvailable(existingRoomId));
        assertEquals("ConsultingRoom not found with id: " + existingRoomId.value(), exception.getMessage());
        verify(consultingRoomRepository, never()).save(any());
    }

    @Test
    void markAsUnavailable_WithExistingId_ShouldUpdateAndReturnDTO() {
        ConsultingRoom availableRoom = new ConsultingRoom(
                existingRoomId,
                clinicId,
                "101",
                "Consultorio",
                "Equip",
                true
        );
        ConsultingRoom unavailableRoom = new ConsultingRoom(
                existingRoomId,
                clinicId,
                "101",
                "Consultorio",
                "Equip",
                false
        );

        when(consultingRoomRepository.findById(existingRoomId)).thenReturn(Optional.of(availableRoom));
        when(consultingRoomRepository.save(any(ConsultingRoom.class))).thenReturn(unavailableRoom);

        ConsultingRoomDTO result = consultingRoomService.markAsUnavailable(existingRoomId);

        assertNotNull(result);
        assertFalse(result.disponible());
        verify(consultingRoomRepository, times(1)).findById(existingRoomId);
        verify(consultingRoomRepository, times(1)).save(any(ConsultingRoom.class));
    }

    @Test
    void markAsUnavailable_WithNonExistingId_ShouldThrowDomainException() {
        when(consultingRoomRepository.findById(existingRoomId)).thenReturn(Optional.empty());

        DomainException exception = assertThrows(DomainException.class,
                () -> consultingRoomService.markAsUnavailable(existingRoomId));
        assertEquals("ConsultingRoom not found with id: " + existingRoomId.value(), exception.getMessage());
        verify(consultingRoomRepository, never()).save(any());
    }
}