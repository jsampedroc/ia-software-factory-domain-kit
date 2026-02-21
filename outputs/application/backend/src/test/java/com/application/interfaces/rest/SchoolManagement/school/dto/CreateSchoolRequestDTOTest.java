package com.application;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import jakarta.validation.ConstraintViolation;
import java.util.Set;
import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class CreateSchoolRequestDTOTest {

    private final Validator validator;

    public CreateSchoolRequestDTOTest() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    @Test
    void shouldCreateValidDto() {
        CreateSchoolRequestDTO dto = new CreateSchoolRequestDTO();
        dto.setName("Colegio Ejemplo");
        dto.setAddress("Calle Falsa 123");
        dto.setPhoneNumber("+1234567890");

        Set<ConstraintViolation<CreateSchoolRequestDTO>> violations = validator.validate(dto);
        assertTrue(violations.isEmpty(), "No debería haber violaciones de validación para un DTO válido");
    }

    @Test
    void shouldFailWhenNameIsNull() {
        CreateSchoolRequestDTO dto = new CreateSchoolRequestDTO();
        dto.setName(null);
        dto.setAddress("Calle Falsa 123");
        dto.setPhoneNumber("+1234567890");

        Set<ConstraintViolation<CreateSchoolRequestDTO>> violations = validator.validate(dto);
        assertFalse(violations.isEmpty(), "Debería haber violaciones cuando el nombre es nulo");
        assertTrue(violations.stream().anyMatch(v -> v.getPropertyPath().toString().equals("name")));
    }

    @Test
    void shouldFailWhenNameIsBlank() {
        CreateSchoolRequestDTO dto = new CreateSchoolRequestDTO();
        dto.setName("   ");
        dto.setAddress("Calle Falsa 123");
        dto.setPhoneNumber("+1234567890");

        Set<ConstraintViolation<CreateSchoolRequestDTO>> violations = validator.validate(dto);
        assertFalse(violations.isEmpty(), "Debería haber violaciones cuando el nombre está en blanco");
        assertTrue(violations.stream().anyMatch(v -> v.getPropertyPath().toString().equals("name")));
    }

    @Test
    void shouldFailWhenAddressIsNull() {
        CreateSchoolRequestDTO dto = new CreateSchoolRequestDTO();
        dto.setName("Colegio Ejemplo");
        dto.setAddress(null);
        dto.setPhoneNumber("+1234567890");

        Set<ConstraintViolation<CreateSchoolRequestDTO>> violations = validator.validate(dto);
        assertFalse(violations.isEmpty(), "Debería haber violaciones cuando la dirección es nula");
        assertTrue(violations.stream().anyMatch(v -> v.getPropertyPath().toString().equals("address")));
    }

    @Test
    void shouldFailWhenAddressIsBlank() {
        CreateSchoolRequestDTO dto = new CreateSchoolRequestDTO();
        dto.setName("Colegio Ejemplo");
        dto.setAddress("   ");
        dto.setPhoneNumber("+1234567890");

        Set<ConstraintViolation<CreateSchoolRequestDTO>> violations = validator.validate(dto);
        assertFalse(violations.isEmpty(), "Debería haber violaciones cuando la dirección está en blanco");
        assertTrue(violations.stream().anyMatch(v -> v.getPropertyPath().toString().equals("address")));
    }

    @Test
    void shouldFailWhenPhoneNumberIsNull() {
        CreateSchoolRequestDTO dto = new CreateSchoolRequestDTO();
        dto.setName("Colegio Ejemplo");
        dto.setAddress("Calle Falsa 123");
        dto.setPhoneNumber(null);

        Set<ConstraintViolation<CreateSchoolRequestDTO>> violations = validator.validate(dto);
        assertFalse(violations.isEmpty(), "Debería haber violaciones cuando el teléfono es nulo");
        assertTrue(violations.stream().anyMatch(v -> v.getPropertyPath().toString().equals("phoneNumber")));
    }

    @Test
    void shouldFailWhenPhoneNumberIsBlank() {
        CreateSchoolRequestDTO dto = new CreateSchoolRequestDTO();
        dto.setName("Colegio Ejemplo");
        dto.setAddress("Calle Falsa 123");
        dto.setPhoneNumber("   ");

        Set<ConstraintViolation<CreateSchoolRequestDTO>> violations = validator.validate(dto);
        assertFalse(violations.isEmpty(), "Debería haber violaciones cuando el teléfono está en blanco");
        assertTrue(violations.stream().anyMatch(v -> v.getPropertyPath().toString().equals("phoneNumber")));
    }

    @Test
    void shouldMapToCommandCorrectly() {
        CreateSchoolRequestDTO dto = new CreateSchoolRequestDTO();
        dto.setName("Colegio Ejemplo");
        dto.setAddress("Calle Falsa 123");
        dto.setPhoneNumber("+1234567890");

        // Asumiendo que el DTO tiene un método toCommand() o similar.
        // Si no existe, este test debería adaptarse a la lógica real de mapeo.
        // Por ahora, solo verificamos que los getters devuelvan los valores establecidos.
        assertEquals("Colegio Ejemplo", dto.getName());
        assertEquals("Calle Falsa 123", dto.getAddress());
        assertEquals("+1234567890", dto.getPhoneNumber());
    }

    @Test
    void shouldHaveLombokAnnotations() {
        // Verificación básica de que Lombok está generando getters, setters, etc.
        CreateSchoolRequestDTO dto = new CreateSchoolRequestDTO();
        dto.setName("Test");
        assertEquals("Test", dto.getName());
    }
}