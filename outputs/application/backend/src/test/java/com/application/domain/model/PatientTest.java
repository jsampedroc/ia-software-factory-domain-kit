package com.application.domain.model;

import com.application.domain.valueobject.PatientId;
import com.application.domain.exception.DomainException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class PatientTest {

    @Test
    void create_WithValidData_ShouldCreatePatient() {
        // Given
        String dni = "12345678A";
        String nombre = "Juan";
        String apellido = "Pérez";
        LocalDate fechaNacimiento = LocalDate.of(1990, 1, 1);
        String telefono = "600123456";
        String email = "juan.perez@example.com";
        String direccion = "Calle Falsa 123";

        // When
        Patient patient = Patient.create(dni, nombre, apellido, fechaNacimiento, telefono, email, direccion);

        // Then
        assertNotNull(patient);
        assertNotNull(patient.getId());
        assertEquals(dni.toUpperCase(), patient.getDni());
        assertEquals(nombre, patient.getNombre());
        assertEquals(apellido, patient.getApellido());
        assertEquals(fechaNacimiento, patient.getFechaNacimiento());
        assertEquals(telefono, patient.getTelefono());
        assertEquals(email.toLowerCase(), patient.getEmail());
        assertEquals(direccion, patient.getDireccion());
        assertNotNull(patient.getFechaRegistro());
        assertTrue(patient.getActivo());
    }

    @Test
    void create_WithInvalidDni_ShouldThrowDomainException() {
        // Given
        String invalidDni = "1234567A"; // 7 dígitos en lugar de 8

        // When & Then
        DomainException exception = assertThrows(DomainException.class, () ->
                Patient.create(invalidDni, "Juan", "Pérez", LocalDate.of(1990, 1, 1),
                        "600123456", "juan@example.com", "Calle 123")
        );
        assertTrue(exception.getMessage().contains("Formato de DNI inválido"));
    }

    @Test
    void create_WithInvalidEmail_ShouldThrowDomainException() {
        // Given
        String invalidEmail = "invalid-email";

        // When & Then
        DomainException exception = assertThrows(DomainException.class, () ->
                Patient.create("12345678A", "Juan", "Pérez", LocalDate.of(1990, 1, 1),
                        "600123456", invalidEmail, "Calle 123")
        );
        assertTrue(exception.getMessage().contains("Formato de email inválido"));
    }

    @Test
    void create_WithFutureBirthDate_ShouldThrowDomainException() {
        // Given
        LocalDate futureBirthDate = LocalDate.now().plusDays(1);

        // When & Then
        DomainException exception = assertThrows(DomainException.class, () ->
                Patient.create("12345678A", "Juan", "Pérez", futureBirthDate,
                        "600123456", "juan@example.com", "Calle 123")
        );
        assertTrue(exception.getMessage().contains("no puede ser en el último año"));
    }

    @Test
    void constructor_WithBirthDateAfterRegistration_ShouldThrowDomainException() {
        // Given
        PatientId id = new PatientId(UUID.randomUUID());
        LocalDate birthDate = LocalDate.of(2020, 1, 1);
        LocalDateTime registrationDate = LocalDateTime.of(2019, 12, 31, 10, 0);

        // When & Then
        DomainException exception = assertThrows(DomainException.class, () ->
                new Patient(id, "12345678A", "Juan", "Pérez", birthDate,
                        "600123456", "juan@example.com", "Calle 123",
                        registrationDate, true)
        );
        assertTrue(exception.getMessage().contains("posterior a la fecha de registro"));
    }

    @Test
    void updatePersonalInfo_WithValidData_ShouldUpdateFields() {
        // Given
        Patient patient = Patient.create("12345678A", "Juan", "Pérez", LocalDate.of(1990, 1, 1),
                "600123456", "juan@example.com", "Calle 123");
        String newNombre = "Carlos";
        String newApellido = "Gómez";
        String newTelefono = "611223344";
        String newEmail = "carlos.gomez@example.com";
        String newDireccion = "Avenida Real 456";

        // When
        patient.updatePersonalInfo(newNombre, newApellido, newTelefono, newEmail, newDireccion);

        // Then
        assertEquals(newNombre, patient.getNombre());
        assertEquals(newApellido, patient.getApellido());
        assertEquals(newTelefono, patient.getTelefono());
        assertEquals(newEmail.toLowerCase(), patient.getEmail());
        assertEquals(newDireccion, patient.getDireccion());
        // Campos no modificados deben permanecer igual
        assertEquals("12345678A", patient.getDni());
        assertEquals(LocalDate.of(1990, 1, 1), patient.getFechaNacimiento());
    }

    @Test
    void updatePersonalInfo_WithInvalidEmail_ShouldThrowDomainException() {
        // Given
        Patient patient = Patient.create("12345678A", "Juan", "Pérez", LocalDate.of(1990, 1, 1),
                "600123456", "juan@example.com", "Calle 123");

        // When & Then
        DomainException exception = assertThrows(DomainException.class, () ->
                patient.updatePersonalInfo("Carlos", "Gómez", "611223344", "invalid-email", "Avenida 456")
        );
        assertTrue(exception.getMessage().contains("Formato de email inválido"));
    }

    @Test
    void deactivate_WhenActive_ShouldDeactivate() {
        // Given
        Patient patient = Patient.create("12345678A", "Juan", "Pérez", LocalDate.of(1990, 1, 1),
                "600123456", "juan@example.com", "Calle 123");
        assertTrue(patient.getActivo());

        // When
        patient.deactivate();

        // Then
        assertFalse(patient.getActivo());
    }

    @Test
    void deactivate_WhenAlreadyInactive_ShouldThrowDomainException() {
        // Given
        Patient patient = Patient.create("12345678A", "Juan", "Pérez", LocalDate.of(1990, 1, 1),
                "600123456", "juan@example.com", "Calle 123");
        patient.deactivate();

        // When & Then
        DomainException exception = assertThrows(DomainException.class, patient::deactivate);
        assertEquals("El paciente ya se encuentra inactivo.", exception.getMessage());
    }

    @Test
    void activate_WhenInactive_ShouldActivate() {
        // Given
        Patient patient = Patient.create("12345678A", "Juan", "Pérez", LocalDate.of(1990, 1, 1),
                "600123456", "juan@example.com", "Calle 123");
        patient.deactivate();
        assertFalse(patient.getActivo());

        // When
        patient.activate();

        // Then
        assertTrue(patient.getActivo());
    }

    @Test
    void activate_WhenAlreadyActive_ShouldThrowDomainException() {
        // Given
        Patient patient = Patient.create("12345678A", "Juan", "Pérez", LocalDate.of(1990, 1, 1),
                "600123456", "juan@example.com", "Calle 123");

        // When & Then
        DomainException exception = assertThrows(DomainException.class, patient::activate);
        assertEquals("El paciente ya se encuentra activo.", exception.getMessage());
    }

    @Test
    void isEligibleForNewAppointments_WhenActive_ShouldReturnTrue() {
        // Given
        Patient patient = Patient.create("12345678A", "Juan", "Pérez", LocalDate.of(1990, 1, 1),
                "600123456", "juan@example.com", "Calle 123");

        // When
        boolean eligible = patient.isEligibleForNewAppointments();

        // Then
        assertTrue(eligible);
    }

    @Test
    void isEligibleForNewAppointments_WhenInactive_ShouldReturnFalse() {
        // Given
        Patient patient = Patient.create("12345678A", "Juan", "Pérez", LocalDate.of(1990, 1, 1),
                "600123456", "juan@example.com", "Calle 123");
        patient.deactivate();

        // When
        boolean eligible = patient.isEligibleForNewAppointments();

        // Then
        assertFalse(eligible);
    }

    @Test
    void setNombre_WithNull_ShouldThrowDomainException() {
        // Given
        PatientId id = new PatientId(UUID.randomUUID());

        // When & Then
        DomainException exception = assertThrows(DomainException.class, () ->
                new Patient(id, "12345678A", null, "Pérez", LocalDate.of(1990, 1, 1),
                        "600123456", "juan@example.com", "Calle 123",
                        LocalDateTime.now(), true)
        );
        assertEquals("El nombre no puede estar vacío.", exception.getMessage());
    }

    @Test
    void setNombre_WithExcessiveLength_ShouldThrowDomainException() {
        // Given
        PatientId id = new PatientId(UUID.randomUUID());
        String longNombre = "A".repeat(101);

        // When & Then
        DomainException exception = assertThrows(DomainException.class, () ->
                new Patient(id, "12345678A", longNombre, "Pérez", LocalDate.of(1990, 1, 1),
                        "600123456", "juan@example.com", "Calle 123",
                        LocalDateTime.now(), true)
        );
        assertEquals("El nombre no puede exceder los 100 caracteres.", exception.getMessage());
    }

    @Test
    void setTelefono_WithExcessiveLength_ShouldThrowDomainException() {
        // Given
        PatientId id = new PatientId(UUID.randomUUID());
        String longTelefono = "1".repeat(21);

        // When & Then
        DomainException exception = assertThrows(DomainException.class, () ->
                new Patient(id, "12345678A", "Juan", "Pérez", LocalDate.of(1990, 1, 1),
                        longTelefono, "juan@example.com", "Calle 123",
                        LocalDateTime.now(), true)
        );
        assertEquals("El teléfono no puede exceder los 20 caracteres.", exception.getMessage());
    }

    @Test
    void setEmail_WithExcessiveLength_ShouldThrowDomainException() {
        // Given
        PatientId id = new PatientId(UUID.randomUUID());
        String longEmail = "a".repeat(151) + "@example.com";

        // When & Then
        DomainException exception = assertThrows(DomainException.class, () ->
                new Patient(id, "12345678A", "Juan", "Pérez", LocalDate.of(1990, 1, 1),
                        "600123456", longEmail, "Calle 123",
                        LocalDateTime.now(), true)
        );
        assertEquals("El email no puede exceder los 150 caracteres.", exception.getMessage());
    }

    @Test
    void setDireccion_WithExcessiveLength_ShouldThrowDomainException() {
        // Given
        PatientId id = new PatientId(UUID.randomUUID());
        String longDireccion = "A".repeat(256);

        // When & Then
        DomainException exception = assertThrows(DomainException.class, () ->
                new Patient(id, "12345678A", "Juan", "Pérez", LocalDate.of(1990, 1, 1),
                        "600123456", "juan@example.com", longDireccion,
                        LocalDateTime.now(), true)
        );
        assertEquals("La dirección no puede exceder los 255 caracteres.", exception.getMessage());
    }

    @Test
    void setFechaRegistro_WithFutureDate_ShouldThrowDomainException() {
        // Given
        PatientId id = new PatientId(UUID.randomUUID());
        LocalDateTime futureDate = LocalDateTime.now().plusDays(1);

        // When & Then
        DomainException exception = assertThrows(DomainException.class, () ->
                new Patient(id, "12345678A", "Juan", "Pérez", LocalDate.of(1990, 1, 1),
                        "600123456", "juan@example.com", "Calle 123",
                        futureDate, true)
        );
        assertEquals("La fecha de registro no puede ser futura.", exception.getMessage());
    }

    @Test
    void setActivo_WithNull_ShouldThrowDomainException() {
        // Given
        PatientId id = new PatientId(UUID.randomUUID());

        // When & Then
        DomainException exception = assertThrows(DomainException.class, () ->
                new Patient(id, "12345678A", "Juan", "Pérez", LocalDate.of(1990, 1, 1),
                        "600123456", "juan@example.com", "Calle 123",
                        LocalDateTime.now(), null)
        );
        assertEquals("El estado activo no puede ser nulo.", exception.getMessage());
    }

    @Test
    void create_ShouldTrimAndNormalizeFields() {
        // Given
        String dni = " 12345678a ";
        String nombre = "  Juan  ";
        String apellido = "  Pérez  ";
        String telefono = "  600123456  ";
        String email = "  Juan.Perez@Example.COM  ";
        String direccion = "  Calle Falsa 123  ";

        // When
        Patient patient = Patient.create(dni, nombre, apellido, LocalDate.of(1990, 1, 1),
                telefono, email, direccion);

        // Then
        assertEquals("12345678A", patient.getDni());
        assertEquals("Juan", patient.getNombre());
        assertEquals("Pérez", patient.getApellido());
        assertEquals("600123456", patient.getTelefono());
        assertEquals("juan.perez@example.com", patient.getEmail());
        assertEquals("Calle Falsa 123", patient.getDireccion());
    }
}