package com.application.domain.model;

import com.application.domain.valueobject.DentistId;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class DentistTest {

    @Test
    void create_WithValidData_ShouldCreateActiveDentist() {
        // Given
        DentistId id = new DentistId(UUID.randomUUID());
        String licenciaMedica = "LM-12345";
        String nombre = "Juan";
        String apellido = "Pérez";
        String telefono = "555-1234";
        String email = "juan.perez@clinica.com";
        LocalDate fechaContratacion = LocalDate.now().minusDays(1);

        // When
        Dentist dentist = Dentist.create(id, licenciaMedica, nombre, apellido, telefono, email, fechaContratacion);

        // Then
        assertNotNull(dentist);
        assertEquals(id, dentist.getId());
        assertEquals(licenciaMedica, dentist.getLicenciaMedica());
        assertEquals(nombre, dentist.getNombre());
        assertEquals(apellido, dentist.getApellido());
        assertEquals(telefono, dentist.getTelefono());
        assertEquals(email, dentist.getEmail());
        assertEquals(fechaContratacion, dentist.getFechaContratacion());
        assertTrue(dentist.isActive());
        assertTrue(dentist.getActivo());
    }

    @Test
    void create_WithNullLicenciaMedica_ShouldThrowException() {
        // Given
        DentistId id = new DentistId(UUID.randomUUID());
        LocalDate fechaContratacion = LocalDate.now().minusDays(1);

        // When & Then
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                () -> Dentist.create(id, null, "Juan", "Pérez", "555-1234", "email@test.com", fechaContratacion));
        assertEquals("La licencia médica es obligatoria", exception.getMessage());
    }

    @Test
    void create_WithBlankLicenciaMedica_ShouldThrowException() {
        // Given
        DentistId id = new DentistId(UUID.randomUUID());
        LocalDate fechaContratacion = LocalDate.now().minusDays(1);

        // When & Then
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                () -> Dentist.create(id, " ", "Juan", "Pérez", "555-1234", "email@test.com", fechaContratacion));
        assertEquals("La licencia médica es obligatoria", exception.getMessage());
    }

    @Test
    void create_WithNullNombre_ShouldThrowException() {
        // Given
        DentistId id = new DentistId(UUID.randomUUID());
        LocalDate fechaContratacion = LocalDate.now().minusDays(1);

        // When & Then
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                () -> Dentist.create(id, "LM-12345", null, "Pérez", "555-1234", "email@test.com", fechaContratacion));
        assertEquals("El nombre es obligatorio", exception.getMessage());
    }

    @Test
    void create_WithBlankNombre_ShouldThrowException() {
        // Given
        DentistId id = new DentistId(UUID.randomUUID());
        LocalDate fechaContratacion = LocalDate.now().minusDays(1);

        // When & Then
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                () -> Dentist.create(id, "LM-12345", " ", "Pérez", "555-1234", "email@test.com", fechaContratacion));
        assertEquals("El nombre es obligatorio", exception.getMessage());
    }

    @Test
    void create_WithNullApellido_ShouldThrowException() {
        // Given
        DentistId id = new DentistId(UUID.randomUUID());
        LocalDate fechaContratacion = LocalDate.now().minusDays(1);

        // When & Then
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                () -> Dentist.create(id, "LM-12345", "Juan", null, "555-1234", "email@test.com", fechaContratacion));
        assertEquals("El apellido es obligatorio", exception.getMessage());
    }

    @Test
    void create_WithBlankApellido_ShouldThrowException() {
        // Given
        DentistId id = new DentistId(UUID.randomUUID());
        LocalDate fechaContratacion = LocalDate.now().minusDays(1);

        // When & Then
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                () -> Dentist.create(id, "LM-12345", "Juan", " ", "555-1234", "email@test.com", fechaContratacion));
        assertEquals("El apellido es obligatorio", exception.getMessage());
    }

    @Test
    void create_WithNullTelefono_ShouldThrowException() {
        // Given
        DentistId id = new DentistId(UUID.randomUUID());
        LocalDate fechaContratacion = LocalDate.now().minusDays(1);

        // When & Then
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                () -> Dentist.create(id, "LM-12345", "Juan", "Pérez", null, "email@test.com", fechaContratacion));
        assertEquals("El teléfono es obligatoria", exception.getMessage());
    }

    @Test
    void create_WithBlankTelefono_ShouldThrowException() {
        // Given
        DentistId id = new DentistId(UUID.randomUUID());
        LocalDate fechaContratacion = LocalDate.now().minusDays(1);

        // When & Then
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                () -> Dentist.create(id, "LM-12345", "Juan", "Pérez", " ", "email@test.com", fechaContratacion));
        assertEquals("El teléfono es obligatoria", exception.getMessage());
    }

    @Test
    void create_WithNullEmail_ShouldThrowException() {
        // Given
        DentistId id = new DentistId(UUID.randomUUID());
        LocalDate fechaContratacion = LocalDate.now().minusDays(1);

        // When & Then
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                () -> Dentist.create(id, "LM-12345", "Juan", "Pérez", "555-1234", null, fechaContratacion));
        assertEquals("El email es obligatorio", exception.getMessage());
    }

    @Test
    void create_WithBlankEmail_ShouldThrowException() {
        // Given
        DentistId id = new DentistId(UUID.randomUUID());
        LocalDate fechaContratacion = LocalDate.now().minusDays(1);

        // When & Then
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                () -> Dentist.create(id, "LM-12345", "Juan", "Pérez", "555-1234", " ", fechaContratacion));
        assertEquals("El email es obligatorio", exception.getMessage());
    }

    @Test
    void create_WithNullFechaContratacion_ShouldThrowException() {
        // Given
        DentistId id = new DentistId(UUID.randomUUID());

        // When & Then
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                () -> Dentist.create(id, "LM-12345", "Juan", "Pérez", "555-1234", "email@test.com", null));
        assertEquals("La fecha de contratación es obligatoria", exception.getMessage());
    }

    @Test
    void create_WithFutureFechaContratacion_ShouldThrowException() {
        // Given
        DentistId id = new DentistId(UUID.randomUUID());
        LocalDate futureDate = LocalDate.now().plusDays(1);

        // When & Then
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                () -> Dentist.create(id, "LM-12345", "Juan", "Pérez", "555-1234", "email@test.com", futureDate));
        assertEquals("La fecha de contratación no puede ser futura", exception.getMessage());
    }

    @Test
    void update_WithValidData_ShouldUpdateFields() {
        // Given
        DentistId id = new DentistId(UUID.randomUUID());
        Dentist dentist = Dentist.create(id, "LM-12345", "Juan", "Pérez", "555-1234", "juan@test.com", LocalDate.now().minusDays(10));

        String newLicencia = "LM-67890";
        String newNombre = "Carlos";
        String newApellido = "Gómez";
        String newTelefono = "555-5678";
        String newEmail = "carlos.gomez@clinica.com";
        LocalDate newFechaContratacion = LocalDate.now().minusDays(5);

        // When
        dentist.update(newLicencia, newNombre, newApellido, newTelefono, newEmail, newFechaContratacion);

        // Then
        assertEquals(newLicencia, dentist.getLicenciaMedica());
        assertEquals(newNombre, dentist.getNombre());
        assertEquals(newApellido, dentist.getApellido());
        assertEquals(newTelefono, dentist.getTelefono());
        assertEquals(newEmail, dentist.getEmail());
        assertEquals(newFechaContratacion, dentist.getFechaContratacion());
    }

    @Test
    void update_WithInvalidData_ShouldThrowException() {
        // Given
        DentistId id = new DentistId(UUID.randomUUID());
        Dentist dentist = Dentist.create(id, "LM-12345", "Juan", "Pérez", "555-1234", "juan@test.com", LocalDate.now().minusDays(10));

        // When & Then
        assertThrows(IllegalArgumentException.class,
                () -> dentist.update(null, "Carlos", "Gómez", "555-5678", "carlos@test.com", LocalDate.now().minusDays(5)));
    }

    @Test
    void deactivate_ShouldSetActivoToFalse() {
        // Given
        DentistId id = new DentistId(UUID.randomUUID());
        Dentist dentist = Dentist.create(id, "LM-12345", "Juan", "Pérez", "555-1234", "juan@test.com", LocalDate.now().minusDays(1));
        assertTrue(dentist.isActive());

        // When
        dentist.deactivate();

        // Then
        assertFalse(dentist.isActive());
        assertFalse(dentist.getActivo());
    }

    @Test
    void activate_ShouldSetActivoToTrue() {
        // Given
        DentistId id = new DentistId(UUID.randomUUID());
        Dentist dentist = Dentist.create(id, "LM-12345", "Juan", "Pérez", "555-1234", "juan@test.com", LocalDate.now().minusDays(1));
        dentist.deactivate();
        assertFalse(dentist.isActive());

        // When
        dentist.activate();

        // Then
        assertTrue(dentist.isActive());
        assertTrue(dentist.getActivo());
    }

    @Test
    void isActive_WhenActivoIsNull_ShouldReturnFalse() {
        // Given
        DentistId id = new DentistId(UUID.randomUUID());
        Dentist dentist = new Dentist(id, "LM-12345", "Juan", "Pérez", "555-1234", "juan@test.com", LocalDate.now().minusDays(1), null);

        // When
        boolean result = dentist.isActive();

        // Then
        assertFalse(result);
    }

    @Test
    void isActive_WhenActivoIsFalse_ShouldReturnFalse() {
        // Given
        DentistId id = new DentistId(UUID.randomUUID());
        Dentist dentist = new Dentist(id, "LM-12345", "Juan", "Pérez", "555-1234", "juan@test.com", LocalDate.now().minusDays(1), false);

        // When
        boolean result = dentist.isActive();

        // Then
        assertFalse(result);
    }

    @Test
    void isActive_WhenActivoIsTrue_ShouldReturnTrue() {
        // Given
        DentistId id = new DentistId(UUID.randomUUID());
        Dentist dentist = new Dentist(id, "LM-12345", "Juan", "Pérez", "555-1234", "juan@test.com", LocalDate.now().minusDays(1), true);

        // When
        boolean result = dentist.isActive();

        // Then
        assertTrue(result);
    }
}