package com.application.infrastructure.entity;

import com.application.domain.model.Treatment;
import com.application.domain.valueobject.TreatmentId;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class TreatmentEntityTest {

    @Test
    void givenValidDomain_whenFromDomain_thenEntityIsCreatedCorrectly() {
        // Given
        TreatmentId treatmentId = new TreatmentId(UUID.randomUUID());
        String code = "TREAT-001";
        String name = "Limpieza Dental";
        String description = "Limpieza profesional de dientes";
        Integer estimatedDurationMinutes = 45;
        BigDecimal baseCost = new BigDecimal("75.50");
        Boolean active = true;

        Treatment treatment = new Treatment(
                treatmentId,
                code,
                name,
                description,
                estimatedDurationMinutes,
                baseCost,
                active
        );

        // When
        TreatmentEntity entity = TreatmentEntity.fromDomain(treatment);

        // Then
        assertNotNull(entity);
        assertEquals(treatmentId.value(), entity.getId());
        assertEquals(code, entity.getCode());
        assertEquals(name, entity.getName());
        assertEquals(description, entity.getDescription());
        assertEquals(estimatedDurationMinutes, entity.getEstimatedDurationMinutes());
        assertEquals(baseCost, entity.getBaseCost());
        assertEquals(active, entity.getActive());
    }

    @Test
    void givenValidEntity_whenToDomain_thenDomainIsCreatedCorrectly() {
        // Given
        UUID id = UUID.randomUUID();
        String code = "TREAT-002";
        String name = "Extracción Simple";
        String description = "Extracción de diente no complicada";
        Integer estimatedDurationMinutes = 30;
        BigDecimal baseCost = new BigDecimal("120.00");
        Boolean active = true;

        TreatmentEntity entity = new TreatmentEntity();
        entity.id = id;
        entity.code = code;
        entity.name = name;
        entity.description = description;
        entity.estimatedDurationMinutes = estimatedDurationMinutes;
        entity.baseCost = baseCost;
        entity.active = active;

        // When
        Treatment domain = entity.toDomain();

        // Then
        assertNotNull(domain);
        assertEquals(new TreatmentId(id), domain.getId());
        assertEquals(code, domain.getCode());
        assertEquals(name, domain.getName());
        assertEquals(description, domain.getDescription());
        assertEquals(estimatedDurationMinutes, domain.getEstimatedDurationMinutes());
        assertEquals(baseCost, domain.getBaseCost());
        assertEquals(active, domain.getActive());
    }

    @Test
    void givenEntityWithNullFields_whenToDomain_thenDomainIsCreatedWithNulls() {
        // Given
        UUID id = UUID.randomUUID();
        String code = "TREAT-003";
        String name = "Tratamiento Test";
        String description = null;
        Integer estimatedDurationMinutes = 60;
        BigDecimal baseCost = new BigDecimal("200.00");
        Boolean active = false;

        TreatmentEntity entity = new TreatmentEntity();
        entity.id = id;
        entity.code = code;
        entity.name = name;
        entity.description = description;
        entity.estimatedDurationMinutes = estimatedDurationMinutes;
        entity.baseCost = baseCost;
        entity.active = active;

        // When
        Treatment domain = entity.toDomain();

        // Then
        assertNotNull(domain);
        assertEquals(new TreatmentId(id), domain.getId());
        assertEquals(code, domain.getCode());
        assertEquals(name, domain.getName());
        assertNull(domain.getDescription());
        assertEquals(estimatedDurationMinutes, domain.getEstimatedDurationMinutes());
        assertEquals(baseCost, domain.getBaseCost());
        assertEquals(active, domain.getActive());
    }

    @Test
    void givenDomainWithNullDescription_whenFromDomain_thenEntityHasNullDescription() {
        // Given
        TreatmentId treatmentId = new TreatmentId(UUID.randomUUID());
        String code = "TREAT-004";
        String name = "Consulta Inicial";
        String description = null;
        Integer estimatedDurationMinutes = 20;
        BigDecimal baseCost = new BigDecimal("50.00");
        Boolean active = true;

        Treatment treatment = new Treatment(
                treatmentId,
                code,
                name,
                description,
                estimatedDurationMinutes,
                baseCost,
                active
        );

        // When
        TreatmentEntity entity = TreatmentEntity.fromDomain(treatment);

        // Then
        assertNotNull(entity);
        assertEquals(treatmentId.value(), entity.getId());
        assertEquals(code, entity.getCode());
        assertEquals(name, entity.getName());
        assertNull(entity.getDescription());
        assertEquals(estimatedDurationMinutes, entity.getEstimatedDurationMinutes());
        assertEquals(baseCost, entity.getBaseCost());
        assertEquals(active, entity.getActive());
    }

    @Test
    void givenTwoWayConversion_whenFromDomainThenToDomain_thenOriginalDomainIsReturned() {
        // Given
        TreatmentId treatmentId = new TreatmentId(UUID.randomUUID());
        String code = "TREAT-005";
        String name = "Blanqueamiento Dental";
        String description = "Blanqueamiento con láser";
        Integer estimatedDurationMinutes = 90;
        BigDecimal baseCost = new BigDecimal("300.00");
        Boolean active = true;

        Treatment originalTreatment = new Treatment(
                treatmentId,
                code,
                name,
                description,
                estimatedDurationMinutes,
                baseCost,
                active
        );

        // When
        TreatmentEntity entity = TreatmentEntity.fromDomain(originalTreatment);
        Treatment convertedTreatment = entity.toDomain();

        // Then
        assertEquals(originalTreatment.getId(), convertedTreatment.getId());
        assertEquals(originalTreatment.getCode(), convertedTreatment.getCode());
        assertEquals(originalTreatment.getName(), convertedTreatment.getName());
        assertEquals(originalTreatment.getDescription(), convertedTreatment.getDescription());
        assertEquals(originalTreatment.getEstimatedDurationMinutes(), convertedTreatment.getEstimatedDurationMinutes());
        assertEquals(originalTreatment.getBaseCost(), convertedTreatment.getBaseCost());
        assertEquals(originalTreatment.getActive(), convertedTreatment.getActive());
    }
}