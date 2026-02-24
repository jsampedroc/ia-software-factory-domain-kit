package com.application.infrastructure.entity;

import com.application.domain.model.Specialty;
import com.application.domain.valueobject.SpecialtyId;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(MockitoExtension.class)
class SpecialtyEntityTest {

    @Test
    void givenSpecialtyEntity_whenToDomain_thenReturnsCorrectSpecialty() {
        // Given
        UUID id = UUID.randomUUID();
        String codigo = "ORT-001";
        String nombre = "Ortodoncia";
        String descripcion = "Especialidad en corrección de dientes y mandíbula";

        SpecialtyEntity entity = new SpecialtyEntity();
        entity.id = id;
        entity.codigo = codigo;
        entity.nombre = nombre;
        entity.descripcion = descripcion;

        // When
        Specialty result = entity.toDomain();

        // Then
        assertThat(result).isNotNull();
        assertThat(result.getId()).isEqualTo(new SpecialtyId(id));
        assertThat(result.getCodigo()).isEqualTo(codigo);
        assertThat(result.getNombre()).isEqualTo(nombre);
        assertThat(result.getDescripcion()).isEqualTo(descripcion);
    }

    @Test
    void givenSpecialty_whenFromDomain_thenReturnsCorrectSpecialtyEntity() {
        // Given
        SpecialtyId specialtyId = new SpecialtyId(UUID.randomUUID());
        Specialty specialty = new Specialty(
                specialtyId,
                "PER-002",
                "Periodoncia",
                "Especialidad en enfermedades de las encías"
        );

        // When
        SpecialtyEntity result = SpecialtyEntity.fromDomain(specialty);

        // Then
        assertThat(result).isNotNull();
        assertThat(result.getId()).isEqualTo(specialtyId.getValue());
        assertThat(result.getCodigo()).isEqualTo("PER-002");
        assertThat(result.getNombre()).isEqualTo("Periodoncia");
        assertThat(result.getDescripcion()).isEqualTo("Especialidad en enfermedades de las encías");
    }

    @Test
    void givenSpecialtyWithNullDescription_whenFromDomain_thenReturnsEntityWithNullDescription() {
        // Given
        SpecialtyId specialtyId = new SpecialtyId(UUID.randomUUID());
        Specialty specialty = new Specialty(
                specialtyId,
                "CD-003",
                "Cirugía Dental",
                null
        );

        // When
        SpecialtyEntity result = SpecialtyEntity.fromDomain(specialty);

        // Then
        assertThat(result).isNotNull();
        assertThat(result.getId()).isEqualTo(specialtyId.getValue());
        assertThat(result.getCodigo()).isEqualTo("CD-003");
        assertThat(result.getNombre()).isEqualTo("Cirugía Dental");
        assertThat(result.getDescripcion()).isNull();
    }

    @Test
    void givenSpecialtyEntityWithNullDescription_whenToDomain_thenReturnsSpecialtyWithNullDescription() {
        // Given
        UUID id = UUID.randomUUID();
        SpecialtyEntity entity = new SpecialtyEntity();
        entity.id = id;
        entity.codigo = "END-004";
        entity.nombre = "Endodoncia";
        entity.descripcion = null;

        // When
        Specialty result = entity.toDomain();

        // Then
        assertThat(result).isNotNull();
        assertThat(result.getId()).isEqualTo(new SpecialtyId(id));
        assertThat(result.getCodigo()).isEqualTo("END-004");
        assertThat(result.getNombre()).isEqualTo("Endodoncia");
        assertThat(result.getDescripcion()).isNull();
    }

    @Test
    void givenTwoSpecialtiesWithSameValues_whenFromDomain_thenCreateDifferentEntities() {
        // Given
        SpecialtyId specialtyId1 = new SpecialtyId(UUID.randomUUID());
        SpecialtyId specialtyId2 = new SpecialtyId(UUID.randomUUID());
        
        Specialty specialty1 = new Specialty(
                specialtyId1,
                "PRO-005",
                "Prótesis",
                "Especialidad en prótesis dentales"
        );
        
        Specialty specialty2 = new Specialty(
                specialtyId2,
                "PRO-005",
                "Prótesis",
                "Especialidad en prótesis dentales"
        );

        // When
        SpecialtyEntity entity1 = SpecialtyEntity.fromDomain(specialty1);
        SpecialtyEntity entity2 = SpecialtyEntity.fromDomain(specialty2);

        // Then
        assertThat(entity1).isNotSameAs(entity2);
        assertThat(entity1.getId()).isNotEqualTo(entity2.getId());
        assertThat(entity1.getCodigo()).isEqualTo(entity2.getCodigo());
        assertThat(entity1.getNombre()).isEqualTo(entity2.getNombre());
        assertThat(entity1.getDescripcion()).isEqualTo(entity2.getDescripcion());
    }
}