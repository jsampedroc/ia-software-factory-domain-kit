package com.application.domain.model;

import com.application.domain.valueobject.TreatmentId;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class TreatmentTest {

    @Test
    void create_ShouldReturnActiveTreatmentWithGivenParameters() {
        // Arrange
        TreatmentId id = new TreatmentId(UUID.randomUUID());
        String codigo = "TRT-001";
        String nombre = "Limpieza Dental";
        String descripcion = "Limpieza profesional de dientes";
        Integer duracion = 45;
        BigDecimal costo = new BigDecimal("75.50");

        // Act
        Treatment treatment = Treatment.create(id, codigo, nombre, descripcion, duracion, costo);

        // Assert
        assertNotNull(treatment);
        assertEquals(id, treatment.getId());
        assertEquals(codigo, treatment.getCodigo());
        assertEquals(nombre, treatment.getNombre());
        assertEquals(descripcion, treatment.getDescripcion());
        assertEquals(duracion, treatment.getDuracionEstimadaMinutos());
        assertEquals(costo, treatment.getCostoBase());
        assertTrue(treatment.getActivo());
    }

    @Test
    void withId_ShouldReturnTreatmentWithGivenState() {
        // Arrange
        TreatmentId id = new TreatmentId(UUID.randomUUID());
        String codigo = "TRT-002";
        String nombre = "Extracción";
        String descripcion = "Extracción de muela";
        Integer duracion = 30;
        BigDecimal costo = new BigDecimal("120.00");
        Boolean activo = false;

        // Act
        Treatment treatment = Treatment.withId(id, codigo, nombre, descripcion, duracion, costo, activo);

        // Assert
        assertNotNull(treatment);
        assertEquals(id, treatment.getId());
        assertEquals(codigo, treatment.getCodigo());
        assertEquals(nombre, treatment.getNombre());
        assertEquals(descripcion, treatment.getDescripcion());
        assertEquals(duracion, treatment.getDuracionEstimadaMinutos());
        assertEquals(costo, treatment.getCostoBase());
        assertFalse(treatment.getActivo());
    }

    @Test
    void update_ShouldUpdateFieldsWhenTreatmentIsActive() {
        // Arrange
        TreatmentId id = new TreatmentId(UUID.randomUUID());
        Treatment treatment = Treatment.create(id, "OLD-001", "Old Name", "Old Desc", 30, new BigDecimal("50.00"));
        String newNombre = "New Name";
        String newDescripcion = "New Description";
        Integer newDuracion = 60;
        BigDecimal newCosto = new BigDecimal("100.00");

        // Act
        treatment.update(newNombre, newDescripcion, newDuracion, newCosto);

        // Assert
        assertEquals(newNombre, treatment.getNombre());
        assertEquals(newDescripcion, treatment.getDescripcion());
        assertEquals(newDuracion, treatment.getDuracionEstimadaMinutos());
        assertEquals(newCosto, treatment.getCostoBase());
    }

    @Test
    void update_ShouldThrowExceptionWhenTreatmentIsInactive() {
        // Arrange
        TreatmentId id = new TreatmentId(UUID.randomUUID());
        Treatment treatment = Treatment.withId(id, "CODE", "Name", "Desc", 30, new BigDecimal("50.00"), false);

        // Act & Assert
        IllegalStateException exception = assertThrows(IllegalStateException.class,
                () -> treatment.update("New Name", "New Desc", 60, new BigDecimal("100.00")));
        assertEquals("Cannot update an inactive treatment", exception.getMessage());
    }

    @Test
    void update_ShouldThrowExceptionWhenNombreIsNull() {
        // Arrange
        TreatmentId id = new TreatmentId(UUID.randomUUID());
        Treatment treatment = Treatment.create(id, "CODE", "Name", "Desc", 30, new BigDecimal("50.00"));

        // Act & Assert
        NullPointerException exception = assertThrows(NullPointerException.class,
                () -> treatment.update(null, "Desc", 30, new BigDecimal("50.00")));
        assertEquals("Nombre cannot be null", exception.getMessage());
    }

    @Test
    void update_ShouldThrowExceptionWhenDescripcionIsNull() {
        // Arrange
        TreatmentId id = new TreatmentId(UUID.randomUUID());
        Treatment treatment = Treatment.create(id, "CODE", "Name", "Desc", 30, new BigDecimal("50.00"));

        // Act & Assert
        NullPointerException exception = assertThrows(NullPointerException.class,
                () -> treatment.update("Name", null, 30, new BigDecimal("50.00")));
        assertEquals("Descripcion cannot be null", exception.getMessage());
    }

    @Test
    void update_ShouldThrowExceptionWhenDuracionIsNull() {
        // Arrange
        TreatmentId id = new TreatmentId(UUID.randomUUID());
        Treatment treatment = Treatment.create(id, "CODE", "Name", "Desc", 30, new BigDecimal("50.00"));

        // Act & Assert
        NullPointerException exception = assertThrows(NullPointerException.class,
                () -> treatment.update("Name", "Desc", null, new BigDecimal("50.00")));
        assertEquals("DuracionEstimadaMinutos cannot be null", exception.getMessage());
    }

    @Test
    void update_ShouldThrowExceptionWhenCostoIsNull() {
        // Arrange
        TreatmentId id = new TreatmentId(UUID.randomUUID());
        Treatment treatment = Treatment.create(id, "CODE", "Name", "Desc", 30, new BigDecimal("50.00"));

        // Act & Assert
        NullPointerException exception = assertThrows(NullPointerException.class,
                () -> treatment.update("Name", "Desc", 30, null));
        assertEquals("CostoBase cannot be null", exception.getMessage());
    }

    @Test
    void deactivate_ShouldSetActivoToFalse() {
        // Arrange
        TreatmentId id = new TreatmentId(UUID.randomUUID());
        Treatment treatment = Treatment.create(id, "CODE", "Name", "Desc", 30, new BigDecimal("50.00"));
        assertTrue(treatment.getActivo());

        // Act
        treatment.deactivate();

        // Assert
        assertFalse(treatment.getActivo());
    }

    @Test
    void activate_ShouldSetActivoToTrue() {
        // Arrange
        TreatmentId id = new TreatmentId(UUID.randomUUID());
        Treatment treatment = Treatment.withId(id, "CODE", "Name", "Desc", 30, new BigDecimal("50.00"), false);
        assertFalse(treatment.getActivo());

        // Act
        treatment.activate();

        // Assert
        assertTrue(treatment.getActivo());
    }

    @Test
    void validate_ShouldThrowExceptionWhenCodigoIsNull() {
        // Arrange
        TreatmentId id = new TreatmentId(UUID.randomUUID());

        // Act & Assert
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                () -> Treatment.withId(id, null, "Name", "Desc", 30, new BigDecimal("50.00"), true));
        assertEquals("Treatment code cannot be null or blank", exception.getMessage());
    }

    @Test
    void validate_ShouldThrowExceptionWhenCodigoIsBlank() {
        // Arrange
        TreatmentId id = new TreatmentId(UUID.randomUUID());

        // Act & Assert
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                () -> Treatment.withId(id, "   ", "Name", "Desc", 30, new BigDecimal("50.00"), true));
        assertEquals("Treatment code cannot be null or blank", exception.getMessage());
    }

    @Test
    void validate_ShouldThrowExceptionWhenNombreIsNull() {
        // Arrange
        TreatmentId id = new TreatmentId(UUID.randomUUID());

        // Act & Assert
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                () -> Treatment.withId(id, "CODE", null, "Desc", 30, new BigDecimal("50.00"), true));
        assertEquals("Treatment name cannot be null or blank", exception.getMessage());
    }

    @Test
    void validate_ShouldThrowExceptionWhenNombreIsBlank() {
        // Arrange
        TreatmentId id = new TreatmentId(UUID.randomUUID());

        // Act & Assert
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                () -> Treatment.withId(id, "CODE", "   ", "Desc", 30, new BigDecimal("50.00"), true));
        assertEquals("Treatment name cannot be null or blank", exception.getMessage());
    }

    @Test
    void validate_ShouldThrowExceptionWhenDuracionIsNull() {
        // Arrange
        TreatmentId id = new TreatmentId(UUID.randomUUID());

        // Act & Assert
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                () -> Treatment.withId(id, "CODE", "Name", "Desc", null, new BigDecimal("50.00"), true));
        assertEquals("Estimated duration must be positive", exception.getMessage());
    }

    @Test
    void validate_ShouldThrowExceptionWhenDuracionIsZeroOrNegative() {
        // Arrange
        TreatmentId id = new TreatmentId(UUID.randomUUID());

        // Act & Assert for zero
        IllegalArgumentException exceptionZero = assertThrows(IllegalArgumentException.class,
                () -> Treatment.withId(id, "CODE", "Name", "Desc", 0, new BigDecimal("50.00"), true));
        assertEquals("Estimated duration must be positive", exceptionZero.getMessage());

        // Act & Assert for negative
        IllegalArgumentException exceptionNegative = assertThrows(IllegalArgumentException.class,
                () -> Treatment.withId(id, "CODE", "Name", "Desc", -10, new BigDecimal("50.00"), true));
        assertEquals("Estimated duration must be positive", exceptionNegative.getMessage());
    }

    @Test
    void validate_ShouldThrowExceptionWhenCostoBaseIsNull() {
        // Arrange
        TreatmentId id = new TreatmentId(UUID.randomUUID());

        // Act & Assert
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                () -> Treatment.withId(id, "CODE", "Name", "Desc", 30, null, true));
        assertEquals("Base cost cannot be negative", exception.getMessage());
    }

    @Test
    void validate_ShouldThrowExceptionWhenCostoBaseIsNegative() {
        // Arrange
        TreatmentId id = new TreatmentId(UUID.randomUUID());

        // Act & Assert
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                () -> Treatment.withId(id, "CODE", "Name", "Desc", 30, new BigDecimal("-10.00"), true));
        assertEquals("Base cost cannot be negative", exception.getMessage());
    }

    @Test
    void validate_ShouldAcceptZeroCostoBase() {
        // Arrange
        TreatmentId id = new TreatmentId(UUID.randomUUID());

        // Act
        Treatment treatment = Treatment.withId(id, "CODE", "Name", "Desc", 30, BigDecimal.ZERO, true);

        // Assert
        assertNotNull(treatment);
        assertEquals(BigDecimal.ZERO, treatment.getCostoBase());
    }
}