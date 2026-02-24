package com.application.domain.model;

import com.application.domain.valueobject.ConsultingRoomId;
import com.application.domain.valueobject.ClinicId;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class ConsultingRoomTest {

    @Test
    void create_ShouldReturnNewConsultingRoomWithGivenAttributes() {
        ConsultingRoomId id = new ConsultingRoomId(UUID.randomUUID());
        ClinicId clinicId = new ClinicId(UUID.randomUUID());
        String numero = "101";
        String nombre = "Consultorio Principal";
        String equipamiento = "Sillón dental, Rayos X";
        Boolean disponible = true;

        ConsultingRoom room = ConsultingRoom.create(id, numero, nombre, equipamiento, disponible, clinicId);

        assertNotNull(room);
        assertEquals(id, room.getId());
        assertEquals(numero, room.getNumero());
        assertEquals(nombre, room.getNombre());
        assertEquals(equipamiento, room.getEquipamiento());
        assertEquals(disponible, room.getDisponible());
        assertEquals(clinicId, room.getClinicId());
    }

    @Test
    void updateInformation_ShouldUpdateAllFields() {
        ConsultingRoomId id = new ConsultingRoomId(UUID.randomUUID());
        ClinicId clinicId = new ClinicId(UUID.randomUUID());
        ConsultingRoom room = ConsultingRoom.create(id, "101", "Viejo", "Equipo viejo", true, clinicId);

        String newNumero = "202";
        String newNombre = "Consultorio Actualizado";
        String newEquipamiento = "Equipo nuevo, Láser";
        Boolean newDisponible = false;

        room.updateInformation(newNumero, newNombre, newEquipamiento, newDisponible);

        assertEquals(newNumero, room.getNumero());
        assertEquals(newNombre, room.getNombre());
        assertEquals(newEquipamiento, room.getEquipamiento());
        assertEquals(newDisponible, room.getDisponible());
        assertEquals(clinicId, room.getClinicId());
    }

    @Test
    void markAsAvailable_ShouldSetDisponibleToTrue() {
        ConsultingRoomId id = new ConsultingRoomId(UUID.randomUUID());
        ClinicId clinicId = new ClinicId(UUID.randomUUID());
        ConsultingRoom room = ConsultingRoom.create(id, "101", "Test", "Equipo", false, clinicId);

        room.markAsAvailable();

        assertTrue(room.getDisponible());
    }

    @Test
    void markAsUnavailable_ShouldSetDisponibleToFalse() {
        ConsultingRoomId id = new ConsultingRoomId(UUID.randomUUID());
        ClinicId clinicId = new ClinicId(UUID.randomUUID());
        ConsultingRoom room = ConsultingRoom.create(id, "101", "Test", "Equipo", true, clinicId);

        room.markAsUnavailable();

        assertFalse(room.getDisponible());
    }

    @Test
    void assignToClinic_ShouldUpdateClinicId() {
        ConsultingRoomId id = new ConsultingRoomId(UUID.randomUUID());
        ClinicId oldClinicId = new ClinicId(UUID.randomUUID());
        ConsultingRoom room = ConsultingRoom.create(id, "101", "Test", "Equipo", true, oldClinicId);

        ClinicId newClinicId = new ClinicId(UUID.randomUUID());
        room.assignToClinic(newClinicId);

        assertEquals(newClinicId, room.getClinicId());
    }

    @Test
    void create_WithNullId_ShouldThrowException() {
        ClinicId clinicId = new ClinicId(UUID.randomUUID());

        assertThrows(NullPointerException.class, () ->
                ConsultingRoom.create(null, "101", "Test", "Equipo", true, clinicId)
        );
    }

    @Test
    void create_WithNullClinicId_ShouldThrowException() {
        ConsultingRoomId id = new ConsultingRoomId(UUID.randomUUID());

        assertThrows(NullPointerException.class, () ->
                ConsultingRoom.create(id, "101", "Test", "Equipo", true, null)
        );
    }
}