package com.application;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;

import java.util.Set;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class CreateAttendanceRecordRequestDTOTest {

    private final Validator validator;

    public CreateAttendanceRecordRequestDTOTest() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    @Test
    void shouldCreateValidDTO() {
        UUID dailyAttendanceId = UUID.randomUUID();
        UUID studentId = UUID.randomUUID();
        String status = "PRESENT";
        String notes = "Arrived on time";

        CreateAttendanceRecordRequestDTO dto = new CreateAttendanceRecordRequestDTO();
        dto.setDailyAttendanceId(dailyAttendanceId);
        dto.setStudentId(studentId);
        dto.setStatus(status);
        dto.setNotes(notes);

        assertEquals(dailyAttendanceId, dto.getDailyAttendanceId());
        assertEquals(studentId, dto.getStudentId());
        assertEquals(status, dto.getStatus());
        assertEquals(notes, dto.getNotes());
    }

    @Test
    void shouldFailValidationWhenDailyAttendanceIdIsNull() {
        CreateAttendanceRecordRequestDTO dto = new CreateAttendanceRecordRequestDTO();
        dto.setDailyAttendanceId(null);
        dto.setStudentId(UUID.randomUUID());
        dto.setStatus("ABSENT");
        dto.setNotes(null);

        Set<ConstraintViolation<CreateAttendanceRecordRequestDTO>> violations = validator.validate(dto);
        assertFalse(violations.isEmpty());
        assertTrue(violations.stream().anyMatch(v -> v.getPropertyPath().toString().equals("dailyAttendanceId")));
    }

    @Test
    void shouldFailValidationWhenStudentIdIsNull() {
        CreateAttendanceRecordRequestDTO dto = new CreateAttendanceRecordRequestDTO();
        dto.setDailyAttendanceId(UUID.randomUUID());
        dto.setStudentId(null);
        dto.setStatus("LATE");
        dto.setNotes("Traffic delay");

        Set<ConstraintViolation<CreateAttendanceRecordRequestDTO>> violations = validator.validate(dto);
        assertFalse(violations.isEmpty());
        assertTrue(violations.stream().anyMatch(v -> v.getPropertyPath().toString().equals("studentId")));
    }

    @Test
    void shouldFailValidationWhenStatusIsNull() {
        CreateAttendanceRecordRequestDTO dto = new CreateAttendanceRecordRequestDTO();
        dto.setDailyAttendanceId(UUID.randomUUID());
        dto.setStudentId(UUID.randomUUID());
        dto.setStatus(null);
        dto.setNotes("No status provided");

        Set<ConstraintViolation<CreateAttendanceRecordRequestDTO>> violations = validator.validate(dto);
        assertFalse(violations.isEmpty());
        assertTrue(violations.stream().anyMatch(v -> v.getPropertyPath().toString().equals("status")));
    }

    @Test
    void shouldFailValidationWhenStatusIsEmpty() {
        CreateAttendanceRecordRequestDTO dto = new CreateAttendanceRecordRequestDTO();
        dto.setDailyAttendanceId(UUID.randomUUID());
        dto.setStudentId(UUID.randomUUID());
        dto.setStatus("");
        dto.setNotes("Empty status");

        Set<ConstraintViolation<CreateAttendanceRecordRequestDTO>> violations = validator.validate(dto);
        assertFalse(violations.isEmpty());
        assertTrue(violations.stream().anyMatch(v -> v.getPropertyPath().toString().equals("status")));
    }

    @Test
    void shouldFailValidationWhenStatusIsBlank() {
        CreateAttendanceRecordRequestDTO dto = new CreateAttendanceRecordRequestDTO();
        dto.setDailyAttendanceId(UUID.randomUUID());
        dto.setStudentId(UUID.randomUUID());
        dto.setStatus("   ");
        dto.setNotes("Blank status");

        Set<ConstraintViolation<CreateAttendanceRecordRequestDTO>> violations = validator.validate(dto);
        assertFalse(violations.isEmpty());
        assertTrue(violations.stream().anyMatch(v -> v.getPropertyPath().toString().equals("status")));
    }

    @Test
    void shouldPassValidationWhenNotesIsNull() {
        CreateAttendanceRecordRequestDTO dto = new CreateAttendanceRecordRequestDTO();
        dto.setDailyAttendanceId(UUID.randomUUID());
        dto.setStudentId(UUID.randomUUID());
        dto.setStatus("EXCUSED");
        dto.setNotes(null);

        Set<ConstraintViolation<CreateAttendanceRecordRequestDTO>> violations = validator.validate(dto);
        assertTrue(violations.isEmpty());
    }

    @Test
    void shouldPassValidationWhenNotesIsEmpty() {
        CreateAttendanceRecordRequestDTO dto = new CreateAttendanceRecordRequestDTO();
        dto.setDailyAttendanceId(UUID.randomUUID());
        dto.setStudentId(UUID.randomUUID());
        dto.setStatus("PRESENT");
        dto.setNotes("");

        Set<ConstraintViolation<CreateAttendanceRecordRequestDTO>> violations = validator.validate(dto);
        assertTrue(violations.isEmpty());
    }

    @Test
    void shouldPassValidationWithValidData() {
        CreateAttendanceRecordRequestDTO dto = new CreateAttendanceRecordRequestDTO();
        dto.setDailyAttendanceId(UUID.randomUUID());
        dto.setStudentId(UUID.randomUUID());
        dto.setStatus("ABSENT");
        dto.setNotes("Family trip");

        Set<ConstraintViolation<CreateAttendanceRecordRequestDTO>> violations = validator.validate(dto);
        assertTrue(violations.isEmpty());
    }

    @Test
    void testLombokAnnotations() {
        UUID dailyAttendanceId = UUID.randomUUID();
        UUID studentId = UUID.randomUUID();
        String status = "LATE";
        String notes = "Doctor appointment";

        CreateAttendanceRecordRequestDTO dto = CreateAttendanceRecordRequestDTO.builder()
                .dailyAttendanceId(dailyAttendanceId)
                .studentId(studentId)
                .status(status)
                .notes(notes)
                .build();

        assertNotNull(dto);
        assertEquals(dailyAttendanceId, dto.getDailyAttendanceId());
        assertEquals(studentId, dto.getStudentId());
        assertEquals(status, dto.getStatus());
        assertEquals(notes, dto.getNotes());

        CreateAttendanceRecordRequestDTO dto2 = CreateAttendanceRecordRequestDTO.builder()
                .dailyAttendanceId(dailyAttendanceId)
                .studentId(studentId)
                .status(status)
                .notes(notes)
                .build();

        assertEquals(dto, dto2);
        assertEquals(dto.hashCode(), dto2.hashCode());
        assertTrue(dto.toString().contains(status));
    }
}