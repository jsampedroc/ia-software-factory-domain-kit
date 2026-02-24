package com.application.application.dto;

import com.application.domain.model.ConsultingRoom;
import com.application.domain.valueobject.ConsultingRoomId;
import com.application.domain.valueobject.ClinicId;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class ConsultingRoomDTOTest {

    @Test
    void givenValidDomainObject_whenFromDomain_thenDTOIsCreatedCorrectly() {
        // Given
        ConsultingRoomId expectedId = new ConsultingRoomId(UUID.randomUUID());
        ClinicId expectedClinicId = new ClinicId(UUID.randomUUID());
        String expectedNumero = "101";
        String expectedNombre = "Consultorio Principal";
        String expectedEquipamiento = "Sillón dental, Rayos X, Lámpara";
        Boolean expectedDisponible = true;

        ConsultingRoom consultingRoom = ConsultingRoom.builder()
                .id(expectedId)
                .clinicId(expectedClinicId)
                .numero(expectedNumero)
                .nombre(expectedNombre)
                .equipamiento(expectedEquipamiento)
                .disponible(expectedDisponible)
                .build();

        // When
        ConsultingRoomDTO dto = ConsultingRoomDTO.fromDomain(consultingRoom);

        // Then
        assertNotNull(dto);
        assertEquals(expectedId, dto.id());
        assertEquals(expectedClinicId, dto.clinicId());
        assertEquals(expectedNumero, dto.numero());
        assertEquals(expectedNombre, dto.nombre());
        assertEquals(expectedEquipamiento, dto.equipamiento());
        assertEquals(expectedDisponible, dto.disponible());
    }

    @Test
    void givenValidDTO_whenToDomain_thenDomainObjectIsCreatedCorrectly() {
        // Given
        ConsultingRoomId expectedId = new ConsultingRoomId(UUID.randomUUID());
        ClinicId expectedClinicId = new ClinicId(UUID.randomUUID());
        String expectedNumero = "102";
        String expectedNombre = "Consultorio de Ortodoncia";
        String expectedEquipamiento = "Sillón, Brackets, Scanner";
        Boolean expectedDisponible = false;

        ConsultingRoomDTO dto = new ConsultingRoomDTO(
                expectedId,
                expectedClinicId,
                expectedNumero,
                expectedNombre,
                expectedEquipamiento,
                expectedDisponible
        );

        // When
        ConsultingRoom domain = dto.toDomain();

        // Then
        assertNotNull(domain);
        assertEquals(expectedId, domain.getId());
        assertEquals(expectedClinicId, domain.getClinicId());
        assertEquals(expectedNumero, domain.getNumero());
        assertEquals(expectedNombre, domain.getNombre());
        assertEquals(expectedEquipamiento, domain.getEquipamiento());
        assertEquals(expectedDisponible, domain.getDisponible());
    }

    @Test
    void givenDTOWithNullFields_whenToDomain_thenDomainObjectIsCreatedWithNulls() {
        // Given
        ConsultingRoomDTO dto = new ConsultingRoomDTO(
                null,
                null,
                null,
                null,
                null,
                null
        );

        // When
        ConsultingRoom domain = dto.toDomain();

        // Then
        assertNotNull(domain);
        assertNull(domain.getId());
        assertNull(domain.getClinicId());
        assertNull(domain.getNumero());
        assertNull(domain.getNombre());
        assertNull(domain.getEquipamiento());
        assertNull(domain.getDisponible());
    }

    @Test
    void givenDomainWithNullFields_whenFromDomain_thenDTOIsCreatedWithNulls() {
        // Given
        ConsultingRoom consultingRoom = ConsultingRoom.builder()
                .id(null)
                .clinicId(null)
                .numero(null)
                .nombre(null)
                .equipamiento(null)
                .disponible(null)
                .build();

        // When
        ConsultingRoomDTO dto = ConsultingRoomDTO.fromDomain(consultingRoom);

        // Then
        assertNotNull(dto);
        assertNull(dto.id());
        assertNull(dto.clinicId());
        assertNull(dto.numero());
        assertNull(dto.nombre());
        assertNull(dto.equipamiento());
        assertNull(dto.disponible());
    }

    @Test
    void givenTwoDTOsWithSameValues_whenEquals_thenTheyAreEqual() {
        // Given
        ConsultingRoomId id = new ConsultingRoomId(UUID.randomUUID());
        ClinicId clinicId = new ClinicId(UUID.randomUUID());

        ConsultingRoomDTO dto1 = new ConsultingRoomDTO(
                id,
                clinicId,
                "103",
                "Consultorio de Cirugía",
                "Equipo quirúrgico",
                true
        );

        ConsultingRoomDTO dto2 = new ConsultingRoomDTO(
                id,
                clinicId,
                "103",
                "Consultorio de Cirugía",
                "Equipo quirúrgico",
                true
        );

        // Then
        assertEquals(dto1, dto2);
        assertEquals(dto1.hashCode(), dto2.hashCode());
    }

    @Test
    void givenTwoDTOsWithDifferentIds_whenEquals_thenTheyAreNotEqual() {
        // Given
        ClinicId clinicId = new ClinicId(UUID.randomUUID());

        ConsultingRoomDTO dto1 = new ConsultingRoomDTO(
                new ConsultingRoomId(UUID.randomUUID()),
                clinicId,
                "103",
                "Consultorio de Cirugía",
                "Equipo quirúrgico",
                true
        );

        ConsultingRoomDTO dto2 = new ConsultingRoomDTO(
                new ConsultingRoomId(UUID.randomUUID()),
                clinicId,
                "103",
                "Consultorio de Cirugía",
                "Equipo quirúrgico",
                true
        );

        // Then
        assertNotEquals(dto1, dto2);
    }

    @Test
    void testDTOImplementsSerializable() {
        // Given
        ConsultingRoomDTO dto = new ConsultingRoomDTO(
                new ConsultingRoomId(UUID.randomUUID()),
                new ClinicId(UUID.randomUUID()),
                "104",
                "Consultorio de Niños",
                "Equipo pediátrico",
                true
        );

        // Then
        assertTrue(dto instanceof java.io.Serializable);
    }
}