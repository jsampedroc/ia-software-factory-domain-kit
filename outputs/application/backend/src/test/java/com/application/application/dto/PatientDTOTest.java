package com.application.application.dto;

import com.application.domain.model.Patient;
import com.application.domain.valueobject.PatientId;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class PatientDTOTest {

    @Test
    void fromDomain_ShouldMapAllFieldsCorrectly() {
        // Given
        UUID expectedId = UUID.randomUUID();
        PatientId patientId = new PatientId(expectedId);
        LocalDate expectedBirthDate = LocalDate.of(1990, 5, 15);
        LocalDateTime expectedRegistrationDate = LocalDateTime.of(2024, 1, 10, 9, 30);

        Patient patient = new Patient(
                patientId,
                "12345678A",
                "Juan",
                "Pérez",
                expectedBirthDate,
                "+34123456789",
                "juan.perez@email.com",
                "Calle Falsa 123",
                expectedRegistrationDate,
                true
        );

        // When
        PatientDTO dto = PatientDTO.fromDomain(patient);

        // Then
        assertNotNull(dto);
        assertEquals(expectedId, dto.patientId());
        assertEquals("12345678A", dto.dni());
        assertEquals("Juan", dto.nombre());
        assertEquals("Pérez", dto.apellido());
        assertEquals(expectedBirthDate, dto.fechaNacimiento());
        assertEquals("+34123456789", dto.telefono());
        assertEquals("juan.perez@email.com", dto.email());
        assertEquals("Calle Falsa 123", dto.direccion());
        assertEquals(expectedRegistrationDate, dto.fechaRegistro());
        assertTrue(dto.activo());
    }

    @Test
    void toDomain_ShouldMapAllFieldsCorrectly() {
        // Given
        UUID expectedId = UUID.randomUUID();
        LocalDate expectedBirthDate = LocalDate.of(1985, 8, 22);
        LocalDateTime expectedRegistrationDate = LocalDateTime.of(2023, 11, 5, 14, 15);

        PatientDTO dto = new PatientDTO(
                expectedId,
                "87654321B",
                "María",
                "García",
                expectedBirthDate,
                "+34987654321",
                "maria.garcia@email.com",
                "Avenida Real 456",
                expectedRegistrationDate,
                false
        );

        // When
        Patient domainPatient = dto.toDomain();

        // Then
        assertNotNull(domainPatient);
        assertEquals(expectedId, domainPatient.getId().value());
        assertEquals("87654321B", domainPatient.getDni());
        assertEquals("María", domainPatient.getNombre());
        assertEquals("García", domainPatient.getApellido());
        assertEquals(expectedBirthDate, domainPatient.getFechaNacimiento());
        assertEquals("+34987654321", domainPatient.getTelefono());
        assertEquals("maria.garcia@email.com", domainPatient.getEmail());
        assertEquals("Avenida Real 456", domainPatient.getDireccion());
        assertEquals(expectedRegistrationDate, domainPatient.getFechaRegistro());
        assertFalse(domainPatient.getActivo());
    }

    @Test
    void fromDomainAndToDomain_ShouldBeIdempotent() {
        // Given
        UUID expectedId = UUID.randomUUID();
        PatientId patientId = new PatientId(expectedId);
        LocalDate birthDate = LocalDate.of(1978, 3, 30);
        LocalDateTime registrationDate = LocalDateTime.now().minusDays(10);

        Patient originalPatient = new Patient(
                patientId,
                "11223344C",
                "Carlos",
                "López",
                birthDate,
                "+34666777888",
                "carlos.lopez@email.com",
                "Plaza Mayor 7",
                registrationDate,
                true
        );

        // When
        PatientDTO intermediateDto = PatientDTO.fromDomain(originalPatient);
        Patient reconstitutedPatient = intermediateDto.toDomain();

        // Then
        assertEquals(originalPatient.getId().value(), reconstitutedPatient.getId().value());
        assertEquals(originalPatient.getDni(), reconstitutedPatient.getDni());
        assertEquals(originalPatient.getNombre(), reconstitutedPatient.getNombre());
        assertEquals(originalPatient.getApellido(), reconstitutedPatient.getApellido());
        assertEquals(originalPatient.getFechaNacimiento(), reconstitutedPatient.getFechaNacimiento());
        assertEquals(originalPatient.getTelefono(), reconstitutedPatient.getTelefono());
        assertEquals(originalPatient.getEmail(), reconstitutedPatient.getEmail());
        assertEquals(originalPatient.getDireccion(), reconstitutedPatient.getDireccion());
        assertEquals(originalPatient.getFechaRegistro(), reconstitutedPatient.getFechaRegistro());
        assertEquals(originalPatient.getActivo(), reconstitutedPatient.getActivo());
    }

    @Test
    void toDomain_WithNullId_ShouldCreatePatientIdWithNullUUID() {
        // Given
        LocalDate birthDate = LocalDate.of(2000, 1, 1);
        LocalDateTime registrationDate = LocalDateTime.now();

        PatientDTO dto = new PatientDTO(
                null, // patientId is null
                "99999999R",
                "Nuevo",
                "Paciente",
                birthDate,
                "+34000000000",
                "nuevo@email.com",
                "Dirección Nueva",
                registrationDate,
                true
        );

        // When
        Patient domainPatient = dto.toDomain();

        // Then
        assertNotNull(domainPatient);
        assertNull(domainPatient.getId().value());
        assertEquals("99999999R", domainPatient.getDni());
        assertEquals("Nuevo", domainPatient.getNombre());
    }
}