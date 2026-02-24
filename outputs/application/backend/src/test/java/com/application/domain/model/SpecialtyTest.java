package com.application.domain.model;

import com.application.domain.valueobject.SpecialtyId;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class SpecialtyTest {

    @Test
    void givenValidParameters_whenCreatingSpecialty_thenSpecialtyIsCreated() {
        // Given
        SpecialtyId id = new SpecialtyId(UUID.randomUUID());
        String codigo = "ORT-001";
        String nombre = "Ortodoncia";
        String descripcion = "Especialidad en corrección de dientes y mandíbula";

        // When
        Specialty specialty = new Specialty(id, codigo, nombre, descripcion);

        // Then
        assertNotNull(specialty);
        assertEquals(id, specialty.getId());
        assertEquals(codigo, specialty.getCodigo());
        assertEquals(nombre, specialty.getNombre());
        assertEquals(descripcion, specialty.getDescripcion());
    }

    @Test
    void givenNullCodigo_whenCreatingSpecialty_thenThrowsIllegalArgumentException() {
        // Given
        SpecialtyId id = new SpecialtyId(UUID.randomUUID());
        String codigo = null;
        String nombre = "Ortodoncia";
        String descripcion = "Descripción";

        // When & Then
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                () -> new Specialty(id, codigo, nombre, descripcion));
        assertEquals("El código de la especialidad no puede estar vacío", exception.getMessage());
    }

    @Test
    void givenBlankCodigo_whenCreatingSpecialty_thenThrowsIllegalArgumentException() {
        // Given
        SpecialtyId id = new SpecialtyId(UUID.randomUUID());
        String codigo = "   ";
        String nombre = "Ortodoncia";
        String descripcion = "Descripción";

        // When & Then
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                () -> new Specialty(id, codigo, nombre, descripcion));
        assertEquals("El código de la especialidad no puede estar vacío", exception.getMessage());
    }

    @Test
    void givenNullNombre_whenCreatingSpecialty_thenThrowsIllegalArgumentException() {
        // Given
        SpecialtyId id = new SpecialtyId(UUID.randomUUID());
        String codigo = "ORT-001";
        String nombre = null;
        String descripcion = "Descripción";

        // When & Then
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                () -> new Specialty(id, codigo, nombre, descripcion));
        assertEquals("El nombre de la especialidad no puede estar vacío", exception.getMessage());
    }

    @Test
    void givenBlankNombre_whenCreatingSpecialty_thenThrowsIllegalArgumentException() {
        // Given
        SpecialtyId id = new SpecialtyId(UUID.randomUUID());
        String codigo = "ORT-001";
        String nombre = "";
        String descripcion = "Descripción";

        // When & Then
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                () -> new Specialty(id, codigo, nombre, descripcion));
        assertEquals("El nombre de la especialidad no puede estar vacío", exception.getMessage());
    }

    @Test
    void givenValidParameters_whenUpdatingSpecialty_thenFieldsAreUpdated() {
        // Given
        SpecialtyId id = new SpecialtyId(UUID.randomUUID());
        Specialty specialty = new Specialty(id, "ORT-001", "Ortodoncia", "Descripción antigua");
        String nuevoNombre = "Ortodoncia Avanzada";
        String nuevaDescripcion = "Nueva descripción actualizada";

        // When
        specialty.update(nuevoNombre, nuevaDescripcion);

        // Then
        assertEquals(nuevoNombre, specialty.getNombre());
        assertEquals(nuevaDescripcion, specialty.getDescripcion());
        assertEquals("ORT-001", specialty.getCodigo()); // Código no debe cambiar
    }

    @Test
    void givenNullNombre_whenUpdatingSpecialty_thenNombreRemainsUnchanged() {
        // Given
        SpecialtyId id = new SpecialtyId(UUID.randomUUID());
        String nombreOriginal = "Ortodoncia";
        Specialty specialty = new Specialty(id, "ORT-001", nombreOriginal, "Descripción antigua");
        String nuevaDescripcion = "Nueva descripción";

        // When
        specialty.update(null, nuevaDescripcion);

        // Then
        assertEquals(nombreOriginal, specialty.getNombre());
        assertEquals(nuevaDescripcion, specialty.getDescripcion());
    }

    @Test
    void givenBlankNombre_whenUpdatingSpecialty_thenNombreRemainsUnchanged() {
        // Given
        SpecialtyId id = new SpecialtyId(UUID.randomUUID());
        String nombreOriginal = "Ortodoncia";
        Specialty specialty = new Specialty(id, "ORT-001", nombreOriginal, "Descripción antigua");
        String nuevaDescripcion = "Nueva descripción";

        // When
        specialty.update("   ", nuevaDescripcion);

        // Then
        assertEquals(nombreOriginal, specialty.getNombre());
        assertEquals(nuevaDescripcion, specialty.getDescripcion());
    }

    @Test
    void givenNullDescripcion_whenUpdatingSpecialty_thenDescripcionIsSetToNull() {
        // Given
        SpecialtyId id = new SpecialtyId(UUID.randomUUID());
        Specialty specialty = new Specialty(id, "ORT-001", "Ortodoncia", "Descripción antigua");
        String nuevoNombre = "Ortodoncia Avanzada";

        // When
        specialty.update(nuevoNombre, null);

        // Then
        assertEquals(nuevoNombre, specialty.getNombre());
        assertNull(specialty.getDescripcion());
    }

    @Test
    void givenInvalidStateAfterUpdate_whenUpdatingSpecialty_thenValidationIsTriggered() {
        // Given
        SpecialtyId id = new SpecialtyId(UUID.randomUUID());
        Specialty specialty = new Specialty(id, "ORT-001", "Ortodoncia", "Descripción");
        String nuevoNombre = ""; // Nombre inválido

        // When & Then
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                () -> specialty.update(nuevoNombre, "Nueva descripción"));
        assertEquals("El nombre de la especialidad no puede estar vacío", exception.getMessage());
    }

    @Test
    void givenSpecialtyWithNoArgsConstructor_thenProtectedConstructorIsAccessibleInSamePackage() {
        // This test verifies the protected no-args constructor is present via reflection
        // or through package access. Since we are in the same package (com.application.domain.model),
        // we can access it via inheritance or in the test class itself.
        // We'll test by creating an anonymous subclass.
        Specialty specialty = new Specialty() {};
        assertNotNull(specialty);
        // The id should be null because the protected constructor doesn't set it
        assertNull(specialty.getId());
    }
}