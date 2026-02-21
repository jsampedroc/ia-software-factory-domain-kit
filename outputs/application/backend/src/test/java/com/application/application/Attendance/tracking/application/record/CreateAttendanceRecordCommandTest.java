package com.application.application.Attendance.tracking.application.record;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class CreateAttendanceRecordCommandTest {

    @Test
    void givenValidParameters_whenCreatingCommand_thenCommandIsCreated() {
        UUID dailyAttendanceId = UUID.randomUUID();
        UUID studentId = UUID.randomUUID();
        String status = "PRESENT";
        String notes = "Arrived on time";
        LocalDateTime recordedAt = LocalDateTime.now();

        CreateAttendanceRecordCommand command = new CreateAttendanceRecordCommand(
                dailyAttendanceId,
                studentId,
                status,
                notes,
                recordedAt
        );

        assertNotNull(command);
        assertEquals(dailyAttendanceId, command.dailyAttendanceId());
        assertEquals(studentId, command.studentId());
        assertEquals(status, command.status());
        assertEquals(notes, command.notes());
        assertEquals(recordedAt, command.recordedAt());
    }

    @Test
    void givenNullDailyAttendanceId_whenCreatingCommand_thenThrowsException() {
        UUID studentId = UUID.randomUUID();
        String status = "ABSENT";
        String notes = "No justification";
        LocalDateTime recordedAt = LocalDateTime.now();

        assertThrows(NullPointerException.class, () ->
                new CreateAttendanceRecordCommand(
                        null,
                        studentId,
                        status,
                        notes,
                        recordedAt
                )
        );
    }

    @Test
    void givenNullStudentId_whenCreatingCommand_thenThrowsException() {
        UUID dailyAttendanceId = UUID.randomUUID();
        String status = "LATE";
        String notes = "10 minutes late";
        LocalDateTime recordedAt = LocalDateTime.now();

        assertThrows(NullPointerException.class, () ->
                new CreateAttendanceRecordCommand(
                        dailyAttendanceId,
                        null,
                        status,
                        notes,
                        recordedAt
                )
        );
    }

    @Test
    void givenNullStatus_whenCreatingCommand_thenThrowsException() {
        UUID dailyAttendanceId = UUID.randomUUID();
        UUID studentId = UUID.randomUUID();
        String notes = "Excused absence";
        LocalDateTime recordedAt = LocalDateTime.now();

        assertThrows(NullPointerException.class, () ->
                new CreateAttendanceRecordCommand(
                        dailyAttendanceId,
                        studentId,
                        null,
                        notes,
                        recordedAt
                )
        );
    }

    @Test
    void givenNullNotes_whenCreatingCommand_thenCommandIsCreatedWithEmptyNotes() {
        UUID dailyAttendanceId = UUID.randomUUID();
        UUID studentId = UUID.randomUUID();
        String status = "EXCUSED";
        LocalDateTime recordedAt = LocalDateTime.now();

        CreateAttendanceRecordCommand command = new CreateAttendanceRecordCommand(
                dailyAttendanceId,
                studentId,
                status,
                null,
                recordedAt
        );

        assertNotNull(command);
        assertNull(command.notes());
    }

    @Test
    void givenNullRecordedAt_whenCreatingCommand_thenThrowsException() {
        UUID dailyAttendanceId = UUID.randomUUID();
        UUID studentId = UUID.randomUUID();
        String status = "PRESENT";
        String notes = "Good";

        assertThrows(NullPointerException.class, () ->
                new CreateAttendanceRecordCommand(
                        dailyAttendanceId,
                        studentId,
                        status,
                        notes,
                        null
                )
        );
    }

    @Test
    void givenEmptyStatus_whenCreatingCommand_thenCommandIsCreated() {
        UUID dailyAttendanceId = UUID.randomUUID();
        UUID studentId = UUID.randomUUID();
        String status = "";
        String notes = "Empty status test";
        LocalDateTime recordedAt = LocalDateTime.now();

        CreateAttendanceRecordCommand command = new CreateAttendanceRecordCommand(
                dailyAttendanceId,
                studentId,
                status,
                notes,
                recordedAt
        );

        assertNotNull(command);
        assertEquals("", command.status());
    }

    @Test
    void givenCommandInstancesWithSameValues_whenComparing_thenTheyAreEqual() {
        UUID dailyAttendanceId = UUID.randomUUID();
        UUID studentId = UUID.randomUUID();
        String status = "PRESENT";
        String notes = "Same values";
        LocalDateTime recordedAt = LocalDateTime.now();

        CreateAttendanceRecordCommand command1 = new CreateAttendanceRecordCommand(
                dailyAttendanceId,
                studentId,
                status,
                notes,
                recordedAt
        );
        CreateAttendanceRecordCommand command2 = new CreateAttendanceRecordCommand(
                dailyAttendanceId,
                studentId,
                status,
                notes,
                recordedAt
        );

        assertEquals(command1, command2);
        assertEquals(command1.hashCode(), command2.hashCode());
    }

    @Test
    void givenCommandInstancesWithDifferentValues_whenComparing_thenTheyAreNotEqual() {
        UUID dailyAttendanceId = UUID.randomUUID();
        UUID studentId = UUID.randomUUID();
        LocalDateTime recordedAt = LocalDateTime.now();

        CreateAttendanceRecordCommand command1 = new CreateAttendanceRecordCommand(
                dailyAttendanceId,
                studentId,
                "PRESENT",
                "Note 1",
                recordedAt
        );
        CreateAttendanceRecordCommand command2 = new CreateAttendanceRecordCommand(
                dailyAttendanceId,
                studentId,
                "ABSENT",
                "Note 2",
                recordedAt
        );

        assertNotEquals(command1, command2);
        assertNotEquals(command1.hashCode(), command2.hashCode());
    }

    @Test
    void givenCommand_whenCallingToString_thenStringContainsFieldValues() {
        UUID dailyAttendanceId = UUID.fromString("123e4567-e89b-12d3-a456-426614174000");
        UUID studentId = UUID.fromString("223e4567-e89b-12d3-a456-426614174001");
        String status = "LATE";
        String notes = "Test notes";
        LocalDateTime recordedAt = LocalDateTime.of(2024, 1, 15, 8, 30);

        CreateAttendanceRecordCommand command = new CreateAttendanceRecordCommand(
                dailyAttendanceId,
                studentId,
                status,
                notes,
                recordedAt
        );

        String toStringResult = command.toString();

        assertTrue(toStringResult.contains(dailyAttendanceId.toString()));
        assertTrue(toStringResult.contains(studentId.toString()));
        assertTrue(toStringResult.contains(status));
        assertTrue(toStringResult.contains(notes));
        assertTrue(toStringResult.contains("2024-01-15T08:30"));
    }
}