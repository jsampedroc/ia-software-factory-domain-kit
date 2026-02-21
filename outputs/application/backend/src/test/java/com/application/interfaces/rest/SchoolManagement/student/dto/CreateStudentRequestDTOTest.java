package com.application;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import jakarta.validation.ConstraintViolation;
import java.time.LocalDate;
import java.util.Set;
import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class CreateStudentRequestDTOTest {

    private final Validator validator;

    public CreateStudentRequestDTOTest() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    @Test
    void shouldCreateValidDTO() {
        CreateStudentRequestDTO dto = CreateStudentRequestDTO.builder()
                .legalGuardianId("legal-guardian-uuid-123")
                .firstName("Juan")
                .lastName("Pérez")
                .dateOfBirth(LocalDate.of(2015, 5, 10))
                .identificationNumber("ID-123456")
                .enrollmentDate(LocalDate.of(2024, 1, 15))
                .currentClassroomId("classroom-uuid-456")
                .build();

        Set<ConstraintViolation<CreateStudentRequestDTO>> violations = validator.validate(dto);
        assertTrue(violations.isEmpty());
    }

    @Test
    void shouldFailWhenLegalGuardianIdIsNull() {
        CreateStudentRequestDTO dto = CreateStudentRequestDTO.builder()
                .legalGuardianId(null)
                .firstName("Juan")
                .lastName("Pérez")
                .dateOfBirth(LocalDate.of(2015, 5, 10))
                .identificationNumber("ID-123456")
                .enrollmentDate(LocalDate.of(2024, 1, 15))
                .currentClassroomId("classroom-uuid-456")
                .build();

        Set<ConstraintViolation<CreateStudentRequestDTO>> violations = validator.validate(dto);
        assertFalse(violations.isEmpty());
        assertTrue(violations.stream().anyMatch(v -> v.getPropertyPath().toString().equals("legalGuardianId")));
    }

    @Test
    void shouldFailWhenFirstNameIsBlank() {
        CreateStudentRequestDTO dto = CreateStudentRequestDTO.builder()
                .legalGuardianId("legal-guardian-uuid-123")
                .firstName("")
                .lastName("Pérez")
                .dateOfBirth(LocalDate.of(2015, 5, 10))
                .identificationNumber("ID-123456")
                .enrollmentDate(LocalDate.of(2024, 1, 15))
                .currentClassroomId("classroom-uuid-456")
                .build();

        Set<ConstraintViolation<CreateStudentRequestDTO>> violations = validator.validate(dto);
        assertFalse(violations.isEmpty());
        assertTrue(violations.stream().anyMatch(v -> v.getPropertyPath().toString().equals("firstName")));
    }

    @Test
    void shouldFailWhenLastNameIsBlank() {
        CreateStudentRequestDTO dto = CreateStudentRequestDTO.builder()
                .legalGuardianId("legal-guardian-uuid-123")
                .firstName("Juan")
                .lastName("")
                .dateOfBirth(LocalDate.of(2015, 5, 10))
                .identificationNumber("ID-123456")
                .enrollmentDate(LocalDate.of(2024, 1, 15))
                .currentClassroomId("classroom-uuid-456")
                .build();

        Set<ConstraintViolation<CreateStudentRequestDTO>> violations = validator.validate(dto);
        assertFalse(violations.isEmpty());
        assertTrue(violations.stream().anyMatch(v -> v.getPropertyPath().toString().equals("lastName")));
    }

    @Test
    void shouldFailWhenDateOfBirthIsNull() {
        CreateStudentRequestDTO dto = CreateStudentRequestDTO.builder()
                .legalGuardianId("legal-guardian-uuid-123")
                .firstName("Juan")
                .lastName("Pérez")
                .dateOfBirth(null)
                .identificationNumber("ID-123456")
                .enrollmentDate(LocalDate.of(2024, 1, 15))
                .currentClassroomId("classroom-uuid-456")
                .build();

        Set<ConstraintViolation<CreateStudentRequestDTO>> violations = validator.validate(dto);
        assertFalse(violations.isEmpty());
        assertTrue(violations.stream().anyMatch(v -> v.getPropertyPath().toString().equals("dateOfBirth")));
    }

    @Test
    void shouldFailWhenDateOfBirthIsInFuture() {
        CreateStudentRequestDTO dto = CreateStudentRequestDTO.builder()
                .legalGuardianId("legal-guardian-uuid-123")
                .firstName("Juan")
                .lastName("Pérez")
                .dateOfBirth(LocalDate.now().plusDays(1))
                .identificationNumber("ID-123456")
                .enrollmentDate(LocalDate.of(2024, 1, 15))
                .currentClassroomId("classroom-uuid-456")
                .build();

        Set<ConstraintViolation<CreateStudentRequestDTO>> violations = validator.validate(dto);
        assertFalse(violations.isEmpty());
        assertTrue(violations.stream().anyMatch(v -> v.getPropertyPath().toString().equals("dateOfBirth")));
    }

    @Test
    void shouldFailWhenIdentificationNumberIsBlank() {
        CreateStudentRequestDTO dto = CreateStudentRequestDTO.builder()
                .legalGuardianId("legal-guardian-uuid-123")
                .firstName("Juan")
                .lastName("Pérez")
                .dateOfBirth(LocalDate.of(2015, 5, 10))
                .identificationNumber("")
                .enrollmentDate(LocalDate.of(2024, 1, 15))
                .currentClassroomId("classroom-uuid-456")
                .build();

        Set<ConstraintViolation<CreateStudentRequestDTO>> violations = validator.validate(dto);
        assertFalse(violations.isEmpty());
        assertTrue(violations.stream().anyMatch(v -> v.getPropertyPath().toString().equals("identificationNumber")));
    }

    @Test
    void shouldFailWhenEnrollmentDateIsNull() {
        CreateStudentRequestDTO dto = CreateStudentRequestDTO.builder()
                .legalGuardianId("legal-guardian-uuid-123")
                .firstName("Juan")
                .lastName("Pérez")
                .dateOfBirth(LocalDate.of(2015, 5, 10))
                .identificationNumber("ID-123456")
                .enrollmentDate(null)
                .currentClassroomId("classroom-uuid-456")
                .build();

        Set<ConstraintViolation<CreateStudentRequestDTO>> violations = validator.validate(dto);
        assertFalse(violations.isEmpty());
        assertTrue(violations.stream().anyMatch(v -> v.getPropertyPath().toString().equals("enrollmentDate")));
    }

    @Test
    void shouldFailWhenEnrollmentDateIsBeforeDateOfBirth() {
        CreateStudentRequestDTO dto = CreateStudentRequestDTO.builder()
                .legalGuardianId("legal-guardian-uuid-123")
                .firstName("Juan")
                .lastName("Pérez")
                .dateOfBirth(LocalDate.of(2015, 5, 10))
                .identificationNumber("ID-123456")
                .enrollmentDate(LocalDate.of(2014, 1, 15))
                .currentClassroomId("classroom-uuid-456")
                .build();

        Set<ConstraintViolation<CreateStudentRequestDTO>> violations = validator.validate(dto);
        assertFalse(violations.isEmpty());
        assertTrue(violations.stream().anyMatch(v -> v.getPropertyPath().toString().equals("enrollmentDate")));
    }

    @Test
    void shouldBeValidWhenCurrentClassroomIdIsNull() {
        CreateStudentRequestDTO dto = CreateStudentRequestDTO.builder()
                .legalGuardianId("legal-guardian-uuid-123")
                .firstName("Juan")
                .lastName("Pérez")
                .dateOfBirth(LocalDate.of(2015, 5, 10))
                .identificationNumber("ID-123456")
                .enrollmentDate(LocalDate.of(2024, 1, 15))
                .currentClassroomId(null)
                .build();

        Set<ConstraintViolation<CreateStudentRequestDTO>> violations = validator.validate(dto);
        assertTrue(violations.isEmpty());
    }

    @Test
    void testLombokAnnotations() {
        CreateStudentRequestDTO dto = CreateStudentRequestDTO.builder()
                .legalGuardianId("guardian-1")
                .firstName("Ana")
                .lastName("García")
                .dateOfBirth(LocalDate.of(2016, 3, 20))
                .identificationNumber("ID-789")
                .enrollmentDate(LocalDate.of(2024, 2, 1))
                .currentClassroomId("classroom-1")
                .build();

        assertEquals("guardian-1", dto.getLegalGuardianId());
        assertEquals("Ana", dto.getFirstName());
        assertEquals("García", dto.getLastName());
        assertEquals(LocalDate.of(2016, 3, 20), dto.getDateOfBirth());
        assertEquals("ID-789", dto.getIdentificationNumber());
        assertEquals(LocalDate.of(2024, 2, 1), dto.getEnrollmentDate());
        assertEquals("classroom-1", dto.getCurrentClassroomId());

        CreateStudentRequestDTO dto2 = CreateStudentRequestDTO.builder()
                .legalGuardianId("guardian-1")
                .firstName("Ana")
                .lastName("García")
                .dateOfBirth(LocalDate.of(2016, 3, 20))
                .identificationNumber("ID-789")
                .enrollmentDate(LocalDate.of(2024, 2, 1))
                .currentClassroomId("classroom-1")
                .build();

        assertEquals(dto, dto2);
        assertEquals(dto.hashCode(), dto2.hashCode());
        assertNotNull(dto.toString());
    }
}