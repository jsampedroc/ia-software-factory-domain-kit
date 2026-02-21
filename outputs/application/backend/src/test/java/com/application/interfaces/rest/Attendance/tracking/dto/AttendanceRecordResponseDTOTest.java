package com.application.interfaces.rest.Attendance.tracking.dto;

import com.application.domain.Attendance.tracking.domain.AttendanceRecord;
import com.application.domain.Attendance.tracking.domain.DailyAttendance;
import com.application.domain.Attendance.tracking.valueobject.AttendanceRecordId;
import com.application.domain.Attendance.tracking.valueobject.DailyAttendanceId;
import com.application.domain.SchoolManagement.student.domain.Student;
import com.application.domain.SchoolManagement.student.valueobject.StudentId;
import com.application.domain.SchoolManagement.valueobject.ClassroomId;
import com.application.domain.SchoolManagement.valueobject.SchoolId;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AttendanceRecordResponseDTOTest {

    @Test
    void fromDomain_ShouldMapAllFieldsCorrectly() {
        // Arrange
        AttendanceRecordId recordId = new AttendanceRecordId(UUID.randomUUID());
        DailyAttendanceId dailyAttendanceId = new DailyAttendanceId(UUID.randomUUID());
        StudentId studentId = new StudentId(UUID.randomUUID());
        ClassroomId classroomId = new ClassroomId(UUID.randomUUID());
        SchoolId schoolId = new SchoolId(UUID.randomUUID());

        LocalDate date = LocalDate.now().minusDays(1);
        LocalDateTime recordedAt = LocalDateTime.now().minusHours(2);

        DailyAttendance mockDailyAttendance = mock(DailyAttendance.class);
        when(mockDailyAttendance.getId()).thenReturn(dailyAttendanceId);
        when(mockDailyAttendance.getClassroomId()).thenReturn(classroomId);
        when(mockDailyAttendance.getDate()).thenReturn(date);
        when(mockDailyAttendance.getSchoolId()).thenReturn(schoolId);

        Student mockStudent = mock(Student.class);
        when(mockStudent.getId()).thenReturn(studentId);
        when(mockStudent.getFullName()).thenReturn("Juan Pérez");

        AttendanceRecord mockRecord = mock(AttendanceRecord.class);
        when(mockRecord.getId()).thenReturn(recordId);
        when(mockRecord.getDailyAttendance()).thenReturn(mockDailyAttendance);
        when(mockRecord.getStudent()).thenReturn(mockStudent);
        when(mockRecord.getStatus()).thenReturn(AttendanceRecord.AttendanceStatus.PRESENT);
        when(mockRecord.getNotes()).thenReturn("Llegó puntual");
        when(mockRecord.getRecordedAt()).thenReturn(recordedAt);

        // Act
        AttendanceRecordResponseDTO dto = AttendanceRecordResponseDTO.fromDomain(mockRecord);

        // Assert
        assertNotNull(dto);
        assertEquals(recordId.getValue(), dto.getId());
        assertEquals(dailyAttendanceId.getValue(), dto.getDailyAttendanceId());
        assertEquals(classroomId.getValue(), dto.getClassroomId());
        assertEquals(date, dto.getDate());
        assertEquals(schoolId.getValue(), dto.getSchoolId());
        assertEquals(studentId.getValue(), dto.getStudentId());
        assertEquals("Juan Pérez", dto.getStudentFullName());
        assertEquals("PRESENT", dto.getStatus());
        assertEquals("Llegó puntual", dto.getNotes());
        assertEquals(recordedAt, dto.getRecordedAt());
    }

    @Test
    void fromDomain_ShouldHandleNullNotes() {
        // Arrange
        AttendanceRecordId recordId = new AttendanceRecordId(UUID.randomUUID());
        DailyAttendanceId dailyAttendanceId = new DailyAttendanceId(UUID.randomUUID());
        StudentId studentId = new StudentId(UUID.randomUUID());
        ClassroomId classroomId = new ClassroomId(UUID.randomUUID());
        SchoolId schoolId = new SchoolId(UUID.randomUUID());

        LocalDate date = LocalDate.now().minusDays(1);
        LocalDateTime recordedAt = LocalDateTime.now().minusHours(2);

        DailyAttendance mockDailyAttendance = mock(DailyAttendance.class);
        when(mockDailyAttendance.getId()).thenReturn(dailyAttendanceId);
        when(mockDailyAttendance.getClassroomId()).thenReturn(classroomId);
        when(mockDailyAttendance.getDate()).thenReturn(date);
        when(mockDailyAttendance.getSchoolId()).thenReturn(schoolId);

        Student mockStudent = mock(Student.class);
        when(mockStudent.getId()).thenReturn(studentId);
        when(mockStudent.getFullName()).thenReturn("Ana García");

        AttendanceRecord mockRecord = mock(AttendanceRecord.class);
        when(mockRecord.getId()).thenReturn(recordId);
        when(mockRecord.getDailyAttendance()).thenReturn(mockDailyAttendance);
        when(mockRecord.getStudent()).thenReturn(mockStudent);
        when(mockRecord.getStatus()).thenReturn(AttendanceRecord.AttendanceStatus.ABSENT);
        when(mockRecord.getNotes()).thenReturn(null);
        when(mockRecord.getRecordedAt()).thenReturn(recordedAt);

        // Act
        AttendanceRecordResponseDTO dto = AttendanceRecordResponseDTO.fromDomain(mockRecord);

        // Assert
        assertNotNull(dto);
        assertEquals(recordId.getValue(), dto.getId());
        assertEquals("ABSENT", dto.getStatus());
        assertNull(dto.getNotes());
        assertEquals("Ana García", dto.getStudentFullName());
    }

    @Test
    void gettersAndSetters_ShouldWorkCorrectly() {
        // Arrange
        AttendanceRecordResponseDTO dto = new AttendanceRecordResponseDTO();
        UUID id = UUID.randomUUID();
        UUID dailyAttendanceId = UUID.randomUUID();
        UUID classroomId = UUID.randomUUID();
        LocalDate date = LocalDate.of(2024, 5, 15);
        UUID schoolId = UUID.randomUUID();
        UUID studentId = UUID.randomUUID();
        String studentFullName = "Carlos López";
        String status = "LATE";
        String notes = "Llegó 15 minutos tarde";
        LocalDateTime recordedAt = LocalDateTime.of(2024, 5, 15, 9, 15);

        // Act
        dto.setId(id);
        dto.setDailyAttendanceId(dailyAttendanceId);
        dto.setClassroomId(classroomId);
        dto.setDate(date);
        dto.setSchoolId(schoolId);
        dto.setStudentId(studentId);
        dto.setStudentFullName(studentFullName);
        dto.setStatus(status);
        dto.setNotes(notes);
        dto.setRecordedAt(recordedAt);

        // Assert
        assertEquals(id, dto.getId());
        assertEquals(dailyAttendanceId, dto.getDailyAttendanceId());
        assertEquals(classroomId, dto.getClassroomId());
        assertEquals(date, dto.getDate());
        assertEquals(schoolId, dto.getSchoolId());
        assertEquals(studentId, dto.getStudentId());
        assertEquals(studentFullName, dto.getStudentFullName());
        assertEquals(status, dto.getStatus());
        assertEquals(notes, dto.getNotes());
        assertEquals(recordedAt, dto.getRecordedAt());
    }

    @Test
    void lombokAnnotations_ShouldGenerateCorrectMethods() {
        // This test verifies Lombok's @Data annotation works (equals, hashCode, toString)
        UUID id1 = UUID.randomUUID();
        LocalDate date1 = LocalDate.of(2024, 5, 15);

        AttendanceRecordResponseDTO dto1 = new AttendanceRecordResponseDTO();
        dto1.setId(id1);
        dto1.setDate(date1);
        dto1.setStatus("PRESENT");

        AttendanceRecordResponseDTO dto2 = new AttendanceRecordResponseDTO();
        dto2.setId(id1);
        dto2.setDate(date1);
        dto2.setStatus("PRESENT");

        AttendanceRecordResponseDTO dto3 = new AttendanceRecordResponseDTO();
        dto3.setId(UUID.randomUUID());
        dto3.setDate(LocalDate.of(2024, 5, 16));
        dto3.setStatus("ABSENT");

        // Test equals and hashCode
        assertEquals(dto1, dto2);
        assertEquals(dto1.hashCode(), dto2.hashCode());
        assertNotEquals(dto1, dto3);
        assertNotEquals(dto1.hashCode(), dto3.hashCode());

        // Test toString contains relevant data (non-null check)
        String toStringOutput = dto1.toString();
        assertNotNull(toStringOutput);
        assertTrue(toStringOutput.contains(id1.toString()));
        assertTrue(toStringOutput.contains(date1.toString()));
        assertTrue(toStringOutput.contains("PRESENT"));
    }
}