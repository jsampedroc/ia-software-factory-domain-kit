package com.application.application.dto;

import com.application.domain.valueobject.DentistId;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class DentistDTOTest {

    @Test
    void givenValidParameters_whenCreatingDentistDTO_thenAllFieldsAreCorrectlySet() {
        // Given
        DentistId dentistId = new DentistId(UUID.randomUUID());
        String licenciaMedica = "LM-12345";
        String nombre = "Carlos";
        String apellido = "Gómez";
        String telefono = "+34123456789";
        String email = "carlos.gomez@clinica.com";
        LocalDate fechaContratacion = LocalDate.of(2023, 1, 15);
        Boolean activo = true;

        // When
        DentistDTO dentistDTO = new DentistDTO(
                dentistId,
                licenciaMedica,
                nombre,
                apellido,
                telefono,
                email,
                fechaContratacion,
                activo
        );

        // Then
        assertNotNull(dentistDTO);
        assertEquals(dentistId, dentistDTO.dentistId());
        assertEquals(licenciaMedica, dentistDTO.licenciaMedica());
        assertEquals(nombre, dentistDTO.nombre());
        assertEquals(apellido, dentistDTO.apellido());
        assertEquals(telefono, dentistDTO.telefono());
        assertEquals(email, dentistDTO.email());
        assertEquals(fechaContratacion, dentistDTO.fechaContratacion());
        assertEquals(activo, dentistDTO.activo());
    }

    @Test
    void givenTwoDentistDTOsWithSameFieldValues_whenComparing_thenTheyAreEqual() {
        // Given
        UUID sameUuid = UUID.randomUUID();
        DentistId dentistId1 = new DentistId(sameUuid);
        DentistId dentistId2 = new DentistId(sameUuid);
        LocalDate fecha = LocalDate.now();

        DentistDTO dto1 = new DentistDTO(
                dentistId1,
                "LM-111",
                "Ana",
                "López",
                "+34987654321",
                "ana.lopez@clinica.com",
                fecha,
                true
        );

        DentistDTO dto2 = new DentistDTO(
                dentistId2,
                "LM-111",
                "Ana",
                "López",
                "+34987654321",
                "ana.lopez@clinica.com",
                fecha,
                true
        );

        // Then
        assertEquals(dto1, dto2);
        assertEquals(dto1.hashCode(), dto2.hashCode());
    }

    @Test
    void givenTwoDentistDTOsWithDifferentIds_whenComparing_thenTheyAreNotEqual() {
        // Given
        LocalDate fecha = LocalDate.now();

        DentistDTO dto1 = new DentistDTO(
                new DentistId(UUID.randomUUID()),
                "LM-111",
                "Ana",
                "López",
                "+34987654321",
                "ana.lopez@clinica.com",
                fecha,
                true
        );

        DentistDTO dto2 = new DentistDTO(
                new DentistId(UUID.randomUUID()),
                "LM-111",
                "Ana",
                "López",
                "+34987654321",
                "ana.lopez@clinica.com",
                fecha,
                true
        );

        // Then
        assertNotEquals(dto1, dto2);
    }

    @Test
    void givenDentistDTO_whenCallingToString_thenStringContainsFieldNames() {
        // Given
        DentistId dentistId = new DentistId(UUID.fromString("123e4567-e89b-12d3-a456-426614174000"));
        LocalDate fecha = LocalDate.of(2024, 5, 20);
        DentistDTO dentistDTO = new DentistDTO(
                dentistId,
                "LM-999",
                "Luis",
                "Martínez",
                "+34111222333",
                "luis.martinez@clinica.com",
                fecha,
                false
        );

        // When
        String result = dentistDTO.toString();

        // Then
        assertNotNull(result);
        assertTrue(result.contains("dentistId=" + dentistId));
        assertTrue(result.contains("licenciaMedica=LM-999"));
        assertTrue(result.contains("nombre=Luis"));
        assertTrue(result.contains("apellido=Martínez"));
        assertTrue(result.contains("telefono=+34111222333"));
        assertTrue(result.contains("email=luis.martinez@clinica.com"));
        assertTrue(result.contains("fechaContratacion=" + fecha));
        assertTrue(result.contains("activo=false"));
    }

    @Test
    void givenDentistDTOWithNullFields_whenCreating_thenNoExceptionIsThrown() {
        // Given
        DentistId dentistId = new DentistId(UUID.randomUUID());

        // When & Then (no exception expected for nullable fields in DTO)
        DentistDTO dentistDTO = new DentistDTO(
                dentistId,
                null, // licenciaMedica
                null, // nombre
                null, // apellido
                null, // telefono
                null, // email
                null, // fechaContratacion
                null  // activo
        );

        assertNotNull(dentistDTO);
        assertEquals(dentistId, dentistDTO.dentistId());
        assertNull(dentistDTO.licenciaMedica());
        assertNull(dentistDTO.nombre());
        assertNull(dentistDTO.apellido());
        assertNull(dentistDTO.telefono());
        assertNull(dentistDTO.email());
        assertNull(dentistDTO.fechaContratacion());
        assertNull(dentistDTO.activo());
    }
}