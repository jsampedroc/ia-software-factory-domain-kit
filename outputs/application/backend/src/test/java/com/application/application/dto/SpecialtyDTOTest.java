package com.application.application.dto;

import com.application.domain.valueobject.SpecialtyId;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(MockitoExtension.class)
class SpecialtyDTOTest {

    @Test
    void givenValidParameters_whenCreatingSpecialtyDTO_thenAllFieldsAreCorrectlySet() {
        // Given
        SpecialtyId id = new SpecialtyId(UUID.randomUUID());
        String codigo = "ORT-001";
        String nombre = "Ortodoncia";
        String descripcion = "Especialidad en corrección de dientes y mandíbula";

        // When
        SpecialtyDTO dto = new SpecialtyDTO(id, codigo, nombre, descripcion);

        // Then
        assertThat(dto.id()).isEqualTo(id);
        assertThat(dto.codigo()).isEqualTo(codigo);
        assertThat(dto.nombre()).isEqualTo(nombre);
        assertThat(dto.descripcion()).isEqualTo(descripcion);
    }

    @Test
    void givenTwoSpecialtyDTOsWithSameFieldValues_whenComparing_thenTheyAreEqual() {
        // Given
        UUID sameUuid = UUID.randomUUID();
        SpecialtyId id1 = new SpecialtyId(sameUuid);
        SpecialtyId id2 = new SpecialtyId(sameUuid);
        String codigo = "PER-002";
        String nombre = "Periodoncia";
        String descripcion = "Especialidad en encías y tejidos de soporte";

        SpecialtyDTO dto1 = new SpecialtyDTO(id1, codigo, nombre, descripcion);
        SpecialtyDTO dto2 = new SpecialtyDTO(id2, codigo, nombre, descripcion);

        // When & Then
        assertThat(dto1).isEqualTo(dto2);
        assertThat(dto1.hashCode()).isEqualTo(dto2.hashCode());
    }

    @Test
    void givenTwoSpecialtyDTOsWithDifferentIds_whenComparing_thenTheyAreNotEqual() {
        // Given
        SpecialtyId id1 = new SpecialtyId(UUID.randomUUID());
        SpecialtyId id2 = new SpecialtyId(UUID.randomUUID());
        String codigo = "END-003";
        String nombre = "Endodoncia";
        String descripcion = "Tratamiento de conductos radiculares";

        SpecialtyDTO dto1 = new SpecialtyDTO(id1, codigo, nombre, descripcion);
        SpecialtyDTO dto2 = new SpecialtyDTO(id2, codigo, nombre, descripcion);

        // When & Then
        assertThat(dto1).isNotEqualTo(dto2);
    }

    @Test
    void givenSpecialtyDTO_whenCallingToString_thenStringContainsFieldValues() {
        // Given
        SpecialtyId id = new SpecialtyId(UUID.fromString("123e4567-e89b-12d3-a456-426614174000"));
        String codigo = "CIR-004";
        String nombre = "Cirugía Oral";
        String descripcion = "Extracciones y cirugías bucales";

        SpecialtyDTO dto = new SpecialtyDTO(id, codigo, nombre, descripcion);

        // When
        String result = dto.toString();

        // Then
        assertThat(result).contains("id=" + id.toString());
        assertThat(result).contains("codigo=" + codigo);
        assertThat(result).contains("nombre=" + nombre);
        assertThat(result).contains("descripcion=" + descripcion);
    }

    @Test
    void givenSpecialtyDTO_whenCheckingImplementsSerializable_thenItIsSerializable() {
        // Given
        SpecialtyId id = new SpecialtyId(UUID.randomUUID());
        SpecialtyDTO dto = new SpecialtyDTO(id, "COD", "Nombre", "Descripción");

        // When & Then
        assertThat(dto).isInstanceOf(java.io.Serializable.class);
    }

    @Test
    void givenSpecialtyDTOWithNullValues_whenCreating_thenFieldsCanBeNull() {
        // Given
        SpecialtyDTO dto = new SpecialtyDTO(null, null, null, null);

        // When & Then
        assertThat(dto.id()).isNull();
        assertThat(dto.codigo()).isNull();
        assertThat(dto.nombre()).isNull();
        assertThat(dto.descripcion()).isNull();
    }

    @Test
    void givenSpecialtyDTO_whenAccessingComponents_thenComponentsAreAccessible() {
        // Given
        SpecialtyId id = new SpecialtyId(UUID.randomUUID());
        String codigo = "PROS-005";
        String nombre = "Prótesis Dental";
        String descripcion = "Diseño y colocación de prótesis";

        // When
        SpecialtyDTO dto = new SpecialtyDTO(id, codigo, nombre, descripcion);

        // Then - Record components are implicitly accessible via the component methods
        assertThat(dto.id()).isNotNull();
        assertThat(dto.codigo()).isNotNull();
        assertThat(dto.nombre()).isNotNull();
        assertThat(dto.descripcion()).isNotNull();
    }
}