package com.application.domain.Attendance.tracking.domain;

import com.application.domain.Attendance.tracking.domain.AttendanceRecord;
import com.application.domain.Attendance.tracking.domain.AttendanceStatus;
import com.application.domain.Attendance.valueobject.AttendanceRecordId;
import com.application.domain.Attendance.valueobject.DailyAttendanceId;
import com.application.domain.SchoolManagement.valueobject.StudentId;
import com.application.domain.shared.Entity;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class AttendanceRecordTest {

    @Test
    void givenValidParameters_whenCreatingAttendanceRecord_thenRecordIsCreated() {
        AttendanceRecordId id = new AttendanceRecordId();
        DailyAttendanceId dailyAttendanceId = new DailyAttendanceId();
        StudentId studentId = new StudentId();
        AttendanceStatus status = AttendanceStatus.PRESENT;
        String notes = "On time";
        LocalDateTime recordedAt = LocalDateTime.now();

        AttendanceRecord record = new AttendanceRecord(id, dailyAttendanceId, studentId, status, notes, recordedAt);

        assertNotNull(record);
        assertEquals(id, record.getId());
        assertEquals(dailyAttendanceId, record.getDailyAttendanceId());
        assertEquals(studentId, record.getStudentId());
        assertEquals(status, record.getStatus());
        assertEquals(notes, record.getNotes());
        assertEquals(recordedAt, record.getRecordedAt());
        assertTrue(record instanceof Entity);
    }

    @Test
    void givenNullId_whenCreatingAttendanceRecord_thenThrowsException() {
        DailyAttendanceId dailyAttendanceId = new DailyAttendanceId();
        StudentId studentId = new StudentId();
        AttendanceStatus status = AttendanceStatus.PRESENT;
        String notes = "On time";
        LocalDateTime recordedAt = LocalDateTime.now();

        assertThrows(NullPointerException.class, () ->
                new AttendanceRecord(null, dailyAttendanceId, studentId, status, notes, recordedAt)
        );
    }

    @Test
    void givenNullDailyAttendanceId_whenCreatingAttendanceRecord_thenThrowsException() {
        AttendanceRecordId id = new AttendanceRecordId();
        StudentId studentId = new StudentId();
        AttendanceStatus status = AttendanceStatus.PRESENT;
        String notes = "On time";
        LocalDateTime recordedAt = LocalDateTime.now();

        assertThrows(NullPointerException.class, () ->
                new AttendanceRecord(id, null, studentId, status, notes, recordedAt)
        );
    }

    @Test
    void givenNullStudentId_whenCreatingAttendanceRecord_thenThrowsException() {
        AttendanceRecordId id = new AttendanceRecordId();
        DailyAttendanceId dailyAttendanceId = new DailyAttendanceId();
        AttendanceStatus status = AttendanceStatus.PRESENT;
        String notes = "On time";
        LocalDateTime recordedAt = LocalDateTime.now();

        assertThrows(NullPointerException.class, () ->
                new AttendanceRecord(id, dailyAttendanceId, null, status, notes, recordedAt)
        );
    }

    @Test
    void givenNullStatus_whenCreatingAttendanceRecord_thenThrowsException() {
        AttendanceRecordId id = new AttendanceRecordId();
        DailyAttendanceId dailyAttendanceId = new DailyAttendanceId();
        StudentId studentId = new StudentId();
        String notes = "On time";
        LocalDateTime recordedAt = LocalDateTime.now();

        assertThrows(NullPointerException.class, () ->
                new AttendanceRecord(id, dailyAttendanceId, studentId, null, notes, recordedAt)
        );
    }

    @Test
    void givenNullRecordedAt_whenCreatingAttendanceRecord_thenThrowsException() {
        AttendanceRecordId id = new AttendanceRecordId();
        DailyAttendanceId dailyAttendanceId = new DailyAttendanceId();
        StudentId studentId = new StudentId();
        AttendanceStatus status = AttendanceStatus.PRESENT;
        String notes = "On time";

        assertThrows(NullPointerException.class, () ->
                new AttendanceRecord(id, dailyAttendanceId, studentId, status, notes, null)
        );
    }

    @Test
    void givenEmptyNotes_whenCreatingAttendanceRecord_thenRecordIsCreatedWithEmptyNotes() {
        AttendanceRecordId id = new AttendanceRecordId();
        DailyAttendanceId dailyAttendanceId = new DailyAttendanceId();
        StudentId studentId = new StudentId();
        AttendanceStatus status = AttendanceStatus.PRESENT;
        String notes = "";
        LocalDateTime recordedAt = LocalDateTime.now();

        AttendanceRecord record = new AttendanceRecord(id, dailyAttendanceId, studentId, status, notes, recordedAt);

        assertNotNull(record);
        assertEquals("", record.getNotes());
    }

    @Test
    void givenTwoRecordsWithSameId_whenComparingEquality_thenTheyAreEqual() {
        AttendanceRecordId id = new AttendanceRecordId();
        DailyAttendanceId dailyAttendanceId = new DailyAttendanceId();
        StudentId studentId = new StudentId();
        AttendanceStatus status = AttendanceStatus.PRESENT;
        String notes = "Notes";
        LocalDateTime recordedAt = LocalDateTime.now();

        AttendanceRecord record1 = new AttendanceRecord(id, dailyAttendanceId, studentId, status, notes, recordedAt);
        AttendanceRecord record2 = new AttendanceRecord(id, dailyAttendanceId, studentId, status, notes, recordedAt);

        assertEquals(record1, record2);
        assertEquals(record1.hashCode(), record2.hashCode());
    }

    @Test
    void givenTwoRecordsWithDifferentId_whenComparingEquality_thenTheyAreNotEqual() {
        DailyAttendanceId dailyAttendanceId = new DailyAttendanceId();
        StudentId studentId = new StudentId();
        AttendanceStatus status = AttendanceStatus.PRESENT;
        String notes = "Notes";
        LocalDateTime recordedAt = LocalDateTime.now();

        AttendanceRecord record1 = new AttendanceRecord(new AttendanceRecordId(), dailyAttendanceId, studentId, status, notes, recordedAt);
        AttendanceRecord record2 = new AttendanceRecord(new AttendanceRecordId(), dailyAttendanceId, studentId, status, notes, recordedAt);

        assertNotEquals(record1, record2);
    }
}