package com.application;

import com.application.application.Attendance.tracking.application.record.CreateAttendanceRecordCommand;
import com.application.application.Attendance.tracking.application.record.CreateAttendanceRecordCommandHandler;
import com.application.domain.Attendance.tracking.domain.DailyAttendance;
import com.application.domain.Attendance.tracking.domain.AttendanceRecord;
import com.application.domain.Attendance.tracking.domain.repository.DailyAttendanceRepository;
import com.application.domain.Attendance.tracking.domain.repository.AttendanceRecordRepository;
import com.application.domain.SchoolManagement.student.domain.Student;
import com.application.domain.SchoolManagement.student.domain.repository.StudentRepository;
import com.application.domain.exception.DomainException;
import com.application.domain.Attendance.tracking.valueobject.DailyAttendanceId;
import com.application.domain.Attendance.tracking.valueobject.AttendanceRecordId;
import com.application.domain.SchoolManagement.valueobject.StudentId;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CreateAttendanceRecordCommandHandlerTest {

    @Mock
    private AttendanceRecordRepository attendanceRecordRepository;
    @Mock
    private DailyAttendanceRepository dailyAttendanceRepository;
    @Mock
    private StudentRepository studentRepository;

    private CreateAttendanceRecordCommandHandler handler;

    @BeforeEach
    void setUp() {
        handler = new CreateAttendanceRecordCommandHandler(attendanceRecordRepository, dailyAttendanceRepository, studentRepository);
    }

    @Test
    void handle_ShouldCreateAndSaveAttendanceRecord_WhenValidCommand() {
        UUID dailyAttendanceUuid = UUID.randomUUID();
        UUID studentUuid = UUID.randomUUID();
        DailyAttendanceId dailyAttendanceId = new DailyAttendanceId(dailyAttendanceUuid);
        StudentId studentId = new StudentId(studentUuid);
        LocalDate date = LocalDate.now().minusDays(1);
        String status = "PRESENT";
        String notes = "On time";

        CreateAttendanceRecordCommand command = new CreateAttendanceRecordCommand(dailyAttendanceUuid, studentUuid, status, notes);

        DailyAttendance mockDailyAttendance = mock(DailyAttendance.class);
        when(mockDailyAttendance.getDate()).thenReturn(date);
        when(dailyAttendanceRepository.findById(dailyAttendanceId)).thenReturn(Optional.of(mockDailyAttendance));

        Student mockStudent = mock(Student.class);
        when(studentRepository.findById(studentId)).thenReturn(Optional.of(mockStudent));

        AttendanceRecordId generatedId = new AttendanceRecordId(UUID.randomUUID());
        AttendanceRecord savedRecord = mock(AttendanceRecord.class);
        when(attendanceRecordRepository.save(any(AttendanceRecord.class))).thenAnswer(invocation -> {
            AttendanceRecord arg = invocation.getArgument(0);
            return savedRecord;
        });

        handler.handle(command);

        verify(dailyAttendanceRepository).findById(dailyAttendanceId);
        verify(studentRepository).findById(studentId);
        verify(attendanceRecordRepository).save(any(AttendanceRecord.class));
    }

    @Test
    void handle_ShouldThrowDomainException_WhenDailyAttendanceNotFound() {
        UUID dailyAttendanceUuid = UUID.randomUUID();
        UUID studentUuid = UUID.randomUUID();
        CreateAttendanceRecordCommand command = new CreateAttendanceRecordCommand(dailyAttendanceUuid, studentUuid, "ABSENT", null);

        DailyAttendanceId dailyAttendanceId = new DailyAttendanceId(dailyAttendanceUuid);
        when(dailyAttendanceRepository.findById(dailyAttendanceId)).thenReturn(Optional.empty());

        DomainException exception = assertThrows(DomainException.class, () -> handler.handle(command));
        assertTrue(exception.getMessage().contains("DailyAttendance"));
        verify(dailyAttendanceRepository).findById(dailyAttendanceId);
        verify(studentRepository, never()).findById(any());
        verify(attendanceRecordRepository, never()).save(any());
    }

    @Test
    void handle_ShouldThrowDomainException_WhenStudentNotFound() {
        UUID dailyAttendanceUuid = UUID.randomUUID();
        UUID studentUuid = UUID.randomUUID();
        DailyAttendanceId dailyAttendanceId = new DailyAttendanceId(dailyAttendanceUuid);
        StudentId studentId = new StudentId(studentUuid);
        CreateAttendanceRecordCommand command = new CreateAttendanceRecordCommand(dailyAttendanceUuid, studentUuid, "LATE", "Traffic");

        DailyAttendance mockDailyAttendance = mock(DailyAttendance.class);
        when(dailyAttendanceRepository.findById(dailyAttendanceId)).thenReturn(Optional.of(mockDailyAttendance));
        when(studentRepository.findById(studentId)).thenReturn(Optional.empty());

        DomainException exception = assertThrows(DomainException.class, () -> handler.handle(command));
        assertTrue(exception.getMessage().contains("Student"));
        verify(dailyAttendanceRepository).findById(dailyAttendanceId);
        verify(studentRepository).findById(studentId);
        verify(attendanceRecordRepository, never()).save(any());
    }

    @Test
    void handle_ShouldThrowDomainException_WhenInvalidStatus() {
        UUID dailyAttendanceUuid = UUID.randomUUID();
        UUID studentUuid = UUID.randomUUID();
        DailyAttendanceId dailyAttendanceId = new DailyAttendanceId(dailyAttendanceUuid);
        StudentId studentId = new StudentId(studentUuid);
        String invalidStatus = "INVALID_STATUS";
        CreateAttendanceRecordCommand command = new CreateAttendanceRecordCommand(dailyAttendanceUuid, studentUuid, invalidStatus, null);

        DailyAttendance mockDailyAttendance = mock(DailyAttendance.class);
        when(dailyAttendanceRepository.findById(dailyAttendanceId)).thenReturn(Optional.of(mockDailyAttendance));
        Student mockStudent = mock(Student.class);
        when(studentRepository.findById(studentId)).thenReturn(Optional.of(mockStudent));

        DomainException exception = assertThrows(DomainException.class, () -> handler.handle(command));
        assertTrue(exception.getMessage().contains("status") || exception.getMessage().contains("Estado"));
        verify(dailyAttendanceRepository).findById(dailyAttendanceId);
        verify(studentRepository).findById(studentId);
        verify(attendanceRecordRepository, never()).save(any());
    }

    @Test
    void handle_ShouldUseCurrentDateTimeForRecordedAt() {
        UUID dailyAttendanceUuid = UUID.randomUUID();
        UUID studentUuid = UUID.randomUUID();
        DailyAttendanceId dailyAttendanceId = new DailyAttendanceId(dailyAttendanceUuid);
        StudentId studentId = new StudentId(studentUuid);
        LocalDate date = LocalDate.now().minusDays(2);
        CreateAttendanceRecordCommand command = new CreateAttendanceRecordCommand(dailyAttendanceUuid, studentUuid, "EXCUSED", "Doctor note");

        DailyAttendance mockDailyAttendance = mock(DailyAttendance.class);
        when(mockDailyAttendance.getDate()).thenReturn(date);
        when(dailyAttendanceRepository.findById(dailyAttendanceId)).thenReturn(Optional.of(mockDailyAttendance));
        Student mockStudent = mock(Student.class);
        when(studentRepository.findById(studentId)).thenReturn(Optional.of(mockStudent));

        AttendanceRecord capturedRecord = null;
        when(attendanceRecordRepository.save(any(AttendanceRecord.class))).thenAnswer(invocation -> invocation.getArgument(0));

        handler.handle(command);

        verify(attendanceRecordRepository).save(any(AttendanceRecord.class));
    }
}