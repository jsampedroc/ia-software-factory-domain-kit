package com.application.application.dto;

import com.application.domain.model.Treatment;
import com.application.domain.valueobject.TreatmentId;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class TreatmentDTOTest {

    @Test
    void givenValidTreatment_whenFromDomain_thenReturnTreatmentDTO() {
        // Given
        UUID expectedId = UUID.randomUUID();
        TreatmentId treatmentId = new TreatmentId(expectedId);
        Treatment treatment = Treatment.builder()
                .id(treatmentId)
                .codigo("TRAT-001")
                .nombre("Limpieza Dental")
                .descripcion("Limpieza profesional de dientes")
                .duracionEstimadaMinutos(45)
                .costoBase(new BigDecimal("75.50"))
                .activo(true)
                .build();

        // When
        TreatmentDTO result = TreatmentDTO.fromDomain(treatment);

        // Then
        assertNotNull(result);
        assertEquals(expectedId, result.treatmentId());
        assertEquals("TRAT-001", result.codigo());
        assertEquals("Limpieza Dental", result.nombre());
        assertEquals("Limpieza profesional de dientes", result.descripcion());
        assertEquals(45, result.duracionEstimadaMinutos());
        assertEquals(new BigDecimal("75.50"), result.costoBase());
        assertTrue(result.activo());
    }

    @Test
    void givenValidTreatmentDTO_whenToDomain_thenReturnTreatment() {
        // Given
        UUID expectedId = UUID.randomUUID();
        TreatmentDTO dto = new TreatmentDTO(
                expectedId,
                "TRAT-002",
                "Extracción Simple",
                "Extracción de diente no complicada",
                30,
                new BigDecimal("120.00"),
                true
        );

        // When
        Treatment result = dto.toDomain();

        // Then
        assertNotNull(result);
        assertEquals(expectedId, result.getId().value());
        assertEquals("TRAT-002", result.getCodigo());
        assertEquals("Extracción Simple", result.getNombre());
        assertEquals("Extracción de diente no complicada", result.getDescripcion());
        assertEquals(30, result.getDuracionEstimadaMinutos());
        assertEquals(new BigDecimal("120.00"), result.getCostoBase());
        assertTrue(result.getActivo());
    }

    @Test
    void givenTwoDTOsWithSameValues_whenEquals_thenReturnTrue() {
        // Given
        UUID id = UUID.randomUUID();
        TreatmentDTO dto1 = new TreatmentDTO(
                id,
                "TRAT-003",
                "Ortodoncia",
                "Tratamiento de ortodoncia",
                60,
                new BigDecimal("200.00"),
                true
        );

        TreatmentDTO dto2 = new TreatmentDTO(
                id,
                "TRAT-003",
                "Ortodoncia",
                "Tratamiento de ortodoncia",
                60,
                new BigDecimal("200.00"),
                true
        );

        // Then
        assertEquals(dto1, dto2);
        assertEquals(dto1.hashCode(), dto2.hashCode());
    }

    @Test
    void givenTwoDTOsWithDifferentIds_whenEquals_thenReturnFalse() {
        // Given
        TreatmentDTO dto1 = new TreatmentDTO(
                UUID.randomUUID(),
                "TRAT-004",
                "Blanqueamiento",
                "Blanqueamiento dental",
                90,
                new BigDecimal("150.00"),
                true
        );

        TreatmentDTO dto2 = new TreatmentDTO(
                UUID.randomUUID(),
                "TRAT-004",
                "Blanqueamiento",
                "Blanqueamiento dental",
                90,
                new BigDecimal("150.00"),
                true
        );

        // Then
        assertNotEquals(dto1, dto2);
    }

    @Test
    void givenDTOWithNullValues_whenToDomain_thenReturnTreatmentWithNullValues() {
        // Given
        UUID expectedId = UUID.randomUUID();
        TreatmentDTO dto = new TreatmentDTO(
                expectedId,
                null,
                null,
                null,
                null,
                null,
                null
        );

        // When
        Treatment result = dto.toDomain();

        // Then
        assertNotNull(result);
        assertEquals(expectedId, result.getId().value());
        assertNull(result.getCodigo());
        assertNull(result.getNombre());
        assertNull(result.getDescripcion());
        assertNull(result.getDuracionEstimadaMinutos());
        assertNull(result.getCostoBase());
        assertNull(result.getActivo());
    }

    @Test
    void givenTreatmentWithNullValues_whenFromDomain_thenReturnDTOWithNullValues() {
        // Given
        UUID expectedId = UUID.randomUUID();
        TreatmentId treatmentId = new TreatmentId(expectedId);
        Treatment treatment = Treatment.builder()
                .id(treatmentId)
                .codigo(null)
                .nombre(null)
                .descripcion(null)
                .duracionEstimadaMinutos(null)
                .costoBase(null)
                .activo(null)
                .build();

        // When
        TreatmentDTO result = TreatmentDTO.fromDomain(treatment);

        // Then
        assertNotNull(result);
        assertEquals(expectedId, result.treatmentId());
        assertNull(result.codigo());
        assertNull(result.nombre());
        assertNull(result.descripcion());
        assertNull(result.duracionEstimadaMinutos());
        assertNull(result.costoBase());
        assertNull(result.activo());
    }

    @Test
    void givenDTO_whenAccessorMethods_thenReturnCorrectValues() {
        // Given
        UUID expectedId = UUID.randomUUID();
        String expectedCodigo = "TRAT-005";
        String expectedNombre = "Implante Dental";
        String expectedDescripcion = "Colocación de implante dental";
        Integer expectedDuracion = 120;
        BigDecimal expectedCosto = new BigDecimal("800.00");
        Boolean expectedActivo = false;

        TreatmentDTO dto = new TreatmentDTO(
                expectedId,
                expectedCodigo,
                expectedNombre,
                expectedDescripcion,
                expectedDuracion,
                expectedCosto,
                expectedActivo
        );

        // Then
        assertEquals(expectedId, dto.treatmentId());
        assertEquals(expectedCodigo, dto.codigo());
        assertEquals(expectedNombre, dto.nombre());
        assertEquals(expectedDescripcion, dto.descripcion());
        assertEquals(expectedDuracion, dto.duracionEstimadaMinutos());
        assertEquals(expectedCosto, dto.costoBase());
        assertEquals(expectedActivo, dto.activo());
    }
}