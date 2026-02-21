package com.application;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import jakarta.validation.ConstraintViolation;
import java.util.Set;
import java.time.Year;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class CreateClassroomRequestDTOTest {

    private final Validator validator;

    public CreateClassroomRequestDTOTest() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    @Test
    void shouldCreateValidDTO() {
        CreateClassroomRequestDTO dto = new CreateClassroomRequestDTO();
        dto.setGradeLevel("1st Grade");
        dto.setSection("A");
        dto.setAcademicYear(Year.now().getValue());
        dto.setCapacity(25);
        dto.setTutorTeacherId("teacher-123");
        dto.setSchoolId("school-456");

        Set<ConstraintViolation<CreateClassroomRequestDTO>> violations = validator.validate(dto);
        assertTrue(violations.isEmpty(), "DTO should be valid");
    }

    @Test
    void shouldFailWhenGradeLevelIsNull() {
        CreateClassroomRequestDTO dto = new CreateClassroomRequestDTO();
        dto.setGradeLevel(null);
        dto.setSection("A");
        dto.setAcademicYear(Year.now().getValue());
        dto.setCapacity(25);
        dto.setTutorTeacherId("teacher-123");
        dto.setSchoolId("school-456");

        Set<ConstraintViolation<CreateClassroomRequestDTO>> violations = validator.validate(dto);
        assertFalse(violations.isEmpty());
        assertTrue(violations.stream().anyMatch(v -> v.getPropertyPath().toString().equals("gradeLevel")));
    }

    @Test
    void shouldFailWhenGradeLevelIsBlank() {
        CreateClassroomRequestDTO dto = new CreateClassroomRequestDTO();
        dto.setGradeLevel("   ");
        dto.setSection("A");
        dto.setAcademicYear(Year.now().getValue());
        dto.setCapacity(25);
        dto.setTutorTeacherId("teacher-123");
        dto.setSchoolId("school-456");

        Set<ConstraintViolation<CreateClassroomRequestDTO>> violations = validator.validate(dto);
        assertFalse(violations.isEmpty());
        assertTrue(violations.stream().anyMatch(v -> v.getPropertyPath().toString().equals("gradeLevel")));
    }

    @Test
    void shouldFailWhenSectionIsNull() {
        CreateClassroomRequestDTO dto = new CreateClassroomRequestDTO();
        dto.setGradeLevel("1st Grade");
        dto.setSection(null);
        dto.setAcademicYear(Year.now().getValue());
        dto.setCapacity(25);
        dto.setTutorTeacherId("teacher-123");
        dto.setSchoolId("school-456");

        Set<ConstraintViolation<CreateClassroomRequestDTO>> violations = validator.validate(dto);
        assertFalse(violations.isEmpty());
        assertTrue(violations.stream().anyMatch(v -> v.getPropertyPath().toString().equals("section")));
    }

    @Test
    void shouldFailWhenSectionIsBlank() {
        CreateClassroomRequestDTO dto = new CreateClassroomRequestDTO();
        dto.setGradeLevel("1st Grade");
        dto.setSection("");
        dto.setAcademicYear(Year.now().getValue());
        dto.setCapacity(25);
        dto.setTutorTeacherId("teacher-123");
        dto.setSchoolId("school-456");

        Set<ConstraintViolation<CreateClassroomRequestDTO>> violations = validator.validate(dto);
        assertFalse(violations.isEmpty());
        assertTrue(violations.stream().anyMatch(v -> v.getPropertyPath().toString().equals("section")));
    }

    @Test
    void shouldFailWhenAcademicYearIsNull() {
        CreateClassroomRequestDTO dto = new CreateClassroomRequestDTO();
        dto.setGradeLevel("1st Grade");
        dto.setSection("A");
        dto.setAcademicYear(null);
        dto.setCapacity(25);
        dto.setTutorTeacherId("teacher-123");
        dto.setSchoolId("school-456");

        Set<ConstraintViolation<CreateClassroomRequestDTO>> violations = validator.validate(dto);
        assertFalse(violations.isEmpty());
        assertTrue(violations.stream().anyMatch(v -> v.getPropertyPath().toString().equals("academicYear")));
    }

    @Test
    void shouldFailWhenAcademicYearIsInPast() {
        CreateClassroomRequestDTO dto = new CreateClassroomRequestDTO();
        dto.setGradeLevel("1st Grade");
        dto.setSection("A");
        dto.setAcademicYear(Year.now().getValue() - 1);
        dto.setCapacity(25);
        dto.setTutorTeacherId("teacher-123");
        dto.setSchoolId("school-456");

        Set<ConstraintViolation<CreateClassroomRequestDTO>> violations = validator.validate(dto);
        assertFalse(violations.isEmpty());
        assertTrue(violations.stream().anyMatch(v -> v.getPropertyPath().toString().equals("academicYear")));
    }

    @Test
    void shouldFailWhenAcademicYearIsInFarFuture() {
        CreateClassroomRequestDTO dto = new CreateClassroomRequestDTO();
        dto.setGradeLevel("1st Grade");
        dto.setSection("A");
        dto.setAcademicYear(Year.now().getValue() + 2);
        dto.setCapacity(25);
        dto.setTutorTeacherId("teacher-123");
        dto.setSchoolId("school-456");

        Set<ConstraintViolation<CreateClassroomRequestDTO>> violations = validator.validate(dto);
        assertFalse(violations.isEmpty());
        assertTrue(violations.stream().anyMatch(v -> v.getPropertyPath().toString().equals("academicYear")));
    }

    @Test
    void shouldFailWhenCapacityIsNull() {
        CreateClassroomRequestDTO dto = new CreateClassroomRequestDTO();
        dto.setGradeLevel("1st Grade");
        dto.setSection("A");
        dto.setAcademicYear(Year.now().getValue());
        dto.setCapacity(null);
        dto.setTutorTeacherId("teacher-123");
        dto.setSchoolId("school-456");

        Set<ConstraintViolation<CreateClassroomRequestDTO>> violations = validator.validate(dto);
        assertFalse(violations.isEmpty());
        assertTrue(violations.stream().anyMatch(v -> v.getPropertyPath().toString().equals("capacity")));
    }

    @Test
    void shouldFailWhenCapacityIsZero() {
        CreateClassroomRequestDTO dto = new CreateClassroomRequestDTO();
        dto.setGradeLevel("1st Grade");
        dto.setSection("A");
        dto.setAcademicYear(Year.now().getValue());
        dto.setCapacity(0);
        dto.setTutorTeacherId("teacher-123");
        dto.setSchoolId("school-456");

        Set<ConstraintViolation<CreateClassroomRequestDTO>> violations = validator.validate(dto);
        assertFalse(violations.isEmpty());
        assertTrue(violations.stream().anyMatch(v -> v.getPropertyPath().toString().equals("capacity")));
    }

    @Test
    void shouldFailWhenCapacityIsNegative() {
        CreateClassroomRequestDTO dto = new CreateClassroomRequestDTO();
        dto.setGradeLevel("1st Grade");
        dto.setSection("A");
        dto.setAcademicYear(Year.now().getValue());
        dto.setCapacity(-5);
        dto.setTutorTeacherId("teacher-123");
        dto.setSchoolId("school-456");

        Set<ConstraintViolation<CreateClassroomRequestDTO>> violations = validator.validate(dto);
        assertFalse(violations.isEmpty());
        assertTrue(violations.stream().anyMatch(v -> v.getPropertyPath().toString().equals("capacity")));
    }

    @Test
    void shouldFailWhenTutorTeacherIdIsNull() {
        CreateClassroomRequestDTO dto = new CreateClassroomRequestDTO();
        dto.setGradeLevel("1st Grade");
        dto.setSection("A");
        dto.setAcademicYear(Year.now().getValue());
        dto.setCapacity(25);
        dto.setTutorTeacherId(null);
        dto.setSchoolId("school-456");

        Set<ConstraintViolation<CreateClassroomRequestDTO>> violations = validator.validate(dto);
        assertFalse(violations.isEmpty());
        assertTrue(violations.stream().anyMatch(v -> v.getPropertyPath().toString().equals("tutorTeacherId")));
    }

    @Test
    void shouldFailWhenTutorTeacherIdIsBlank() {
        CreateClassroomRequestDTO dto = new CreateClassroomRequestDTO();
        dto.setGradeLevel("1st Grade");
        dto.setSection("A");
        dto.setAcademicYear(Year.now().getValue());
        dto.setCapacity(25);
        dto.setTutorTeacherId("   ");
        dto.setSchoolId("school-456");

        Set<ConstraintViolation<CreateClassroomRequestDTO>> violations = validator.validate(dto);
        assertFalse(violations.isEmpty());
        assertTrue(violations.stream().anyMatch(v -> v.getPropertyPath().toString().equals("tutorTeacherId")));
    }

    @Test
    void shouldFailWhenSchoolIdIsNull() {
        CreateClassroomRequestDTO dto = new CreateClassroomRequestDTO();
        dto.setGradeLevel("1st Grade");
        dto.setSection("A");
        dto.setAcademicYear(Year.now().getValue());
        dto.setCapacity(25);
        dto.setTutorTeacherId("teacher-123");
        dto.setSchoolId(null);

        Set<ConstraintViolation<CreateClassroomRequestDTO>> violations = validator.validate(dto);
        assertFalse(violations.isEmpty());
        assertTrue(violations.stream().anyMatch(v -> v.getPropertyPath().toString().equals("schoolId")));
    }

    @Test
    void shouldFailWhenSchoolIdIsBlank() {
        CreateClassroomRequestDTO dto = new CreateClassroomRequestDTO();
        dto.setGradeLevel("1st Grade");
        dto.setSection("A");
        dto.setAcademicYear(Year.now().getValue());
        dto.setCapacity(25);
        dto.setTutorTeacherId("teacher-123");
        dto.setSchoolId("");

        Set<ConstraintViolation<CreateClassroomRequestDTO>> violations = validator.validate(dto);
        assertFalse(violations.isEmpty());
        assertTrue(violations.stream().anyMatch(v -> v.getPropertyPath().toString().equals("schoolId")));
    }

    @Test
    void testLombokAnnotations() {
        CreateClassroomRequestDTO dto = CreateClassroomRequestDTO.builder()
                .gradeLevel("2nd Grade")
                .section("B")
                .academicYear(Year.now().getValue())
                .capacity(30)
                .tutorTeacherId("teacher-789")
                .schoolId("school-999")
                .build();

        assertEquals("2nd Grade", dto.getGradeLevel());
        assertEquals("B", dto.getSection());
        assertEquals(Year.now().getValue(), dto.getAcademicYear());
        assertEquals(30, dto.getCapacity());
        assertEquals("teacher-789", dto.getTutorTeacherId());
        assertEquals("school-999", dto.getSchoolId());

        CreateClassroomRequestDTO dto2 = new CreateClassroomRequestDTO();
        dto2.setGradeLevel("2nd Grade");
        dto2.setSection("B");
        dto2.setAcademicYear(Year.now().getValue());
        dto2.setCapacity(30);
        dto2.setTutorTeacherId("teacher-789");
        dto2.setSchoolId("school-999");

        assertEquals(dto, dto2);
        assertEquals(dto.hashCode(), dto2.hashCode());
        assertNotNull(dto.toString());
    }
}