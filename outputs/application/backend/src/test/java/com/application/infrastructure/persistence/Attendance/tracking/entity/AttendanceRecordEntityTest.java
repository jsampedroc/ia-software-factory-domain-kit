package com.application;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class AttendanceRecordEntityTest {

    private AttendanceRecordEntity attendanceRecordEntity;
    private final UUID id = UUID.randomUUID();
    private final UUID dailyAttendanceId = UUID.randomUUID();
    private final UUID studentId = UUID.randomUUID();
    private final String status = "PRESENT";
    private final String notes = "On time";
    private final LocalDateTime recordedAt = LocalDateTime.now();

    @BeforeEach
    void setUp() {
        attendanceRecordEntity = new AttendanceRecordEntity();
        attendanceRecordEntity.setId(id);
        attendanceRecordEntity.setDailyAttendanceId(dailyAttendanceId);
        attendanceRecordEntity.setStudentId(studentId);
        attendanceRecordEntity.setStatus(status);
        attendanceRecordEntity.setNotes(notes);
        attendanceRecordEntity.setRecordedAt(recordedAt);
    }

    @Test
    void testGettersAndSetters() {
        assertEquals(id, attendanceRecordEntity.getId());
        assertEquals(dailyAttendanceId, attendanceRecordEntity.getDailyAttendanceId());
        assertEquals(studentId, attendanceRecordEntity.getStudentId());
        assertEquals(status, attendanceRecordEntity.getStatus());
        assertEquals(notes, attendanceRecordEntity.getNotes());
        assertEquals(recordedAt, attendanceRecordEntity.getRecordedAt());
    }

    @Test
    void testNoArgsConstructor() {
        AttendanceRecordEntity entity = new AttendanceRecordEntity();
        assertNotNull(entity);
        assertNull(entity.getId());
        assertNull(entity.getDailyAttendanceId());
        assertNull(entity.getStudentId());
        assertNull(entity.getStatus());
        assertNull(entity.getNotes());
        assertNull(entity.getRecordedAt());
    }

    @Test
    void testAllArgsConstructor() {
        AttendanceRecordEntity entity = new AttendanceRecordEntity(id, dailyAttendanceId, studentId, status, notes, recordedAt);
        assertEquals(id, entity.getId());
        assertEquals(dailyAttendanceId, entity.getDailyAttendanceId());
        assertEquals(studentId, entity.getStudentId());
        assertEquals(status, entity.getStatus());
        assertEquals(notes, entity.getNotes());
        assertEquals(recordedAt, entity.getRecordedAt());
    }

    @Test
    void testEqualsAndHashCode() {
        AttendanceRecordEntity entity1 = new AttendanceRecordEntity(id, dailyAttendanceId, studentId, status, notes, recordedAt);
        AttendanceRecordEntity entity2 = new AttendanceRecordEntity(id, dailyAttendanceId, studentId, status, notes, recordedAt);
        AttendanceRecordEntity entity3 = new AttendanceRecordEntity(UUID.randomUUID(), dailyAttendanceId, studentId, status, notes, recordedAt);

        assertEquals(entity1, entity2);
        assertNotEquals(entity1, entity3);
        assertEquals(entity1.hashCode(), entity2.hashCode());
        assertNotEquals(entity1.hashCode(), entity3.hashCode());
    }

    @Test
    void testToString() {
        String toStringResult = attendanceRecordEntity.toString();
        assertTrue(toStringResult.contains(id.toString()));
        assertTrue(toStringResult.contains(status));
        assertTrue(toStringResult.contains(studentId.toString()));
    }

    @Test
    void testBuilder() {
        AttendanceRecordEntity builtEntity = AttendanceRecordEntity.builder()
                .id(id)
                .dailyAttendanceId(dailyAttendanceId)
                .studentId(studentId)
                .status(status)
                .notes(notes)
                .recordedAt(recordedAt)
                .build();

        assertEquals(id, builtEntity.getId());
        assertEquals(dailyAttendanceId, builtEntity.getDailyAttendanceId());
        assertEquals(studentId, builtEntity.getStudentId());
        assertEquals(status, builtEntity.getStatus());
        assertEquals(notes, builtEntity.getNotes());
        assertEquals(recordedAt, builtEntity.getRecordedAt());
    }
}