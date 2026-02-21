package com.application.domain.Attendance.tracking.domain.repository;

import com.application.domain.Attendance.tracking.domain.AttendanceRecord;
import com.application.domain.Attendance.tracking.valueobject.AttendanceRecordId;
import com.application.domain.Attendance.tracking.valueobject.DailyAttendanceId;
import com.application.domain.SchoolManagement.valueobject.StudentId;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AttendanceRecordRepositoryTest {

    @Mock
    private AttendanceRecordRepository attendanceRecordRepository;

    private AttendanceRecordId testRecordId;
    private DailyAttendanceId testDailyAttendanceId;
    private StudentId testStudentId;
    private AttendanceRecord testAttendanceRecord;

    @BeforeEach
    void setUp() {
        testRecordId = new AttendanceRecordId();
        testDailyAttendanceId = new DailyAttendanceId();
        testStudentId = new StudentId();
        testAttendanceRecord = mock(AttendanceRecord.class);
        when(testAttendanceRecord.getId()).thenReturn(testRecordId);
    }

    @Test
    void save_ShouldPersistRecord() {
        when(attendanceRecordRepository.save(testAttendanceRecord)).thenReturn(testAttendanceRecord);

        AttendanceRecord savedRecord = attendanceRecordRepository.save(testAttendanceRecord);

        assertNotNull(savedRecord);
        assertEquals(testRecordId, savedRecord.getId());
        verify(attendanceRecordRepository, times(1)).save(testAttendanceRecord);
    }

    @Test
    void findById_WithExistingId_ShouldReturnRecord() {
        when(attendanceRecordRepository.findById(testRecordId)).thenReturn(Optional.of(testAttendanceRecord));

        Optional<AttendanceRecord> foundRecord = attendanceRecordRepository.findById(testRecordId);

        assertTrue(foundRecord.isPresent());
        assertEquals(testRecordId, foundRecord.get().getId());
        verify(attendanceRecordRepository, times(1)).findById(testRecordId);
    }

    @Test
    void findById_WithNonExistingId_ShouldReturnEmpty() {
        when(attendanceRecordRepository.findById(testRecordId)).thenReturn(Optional.empty());

        Optional<AttendanceRecord> foundRecord = attendanceRecordRepository.findById(testRecordId);

        assertFalse(foundRecord.isPresent());
        verify(attendanceRecordRepository, times(1)).findById(testRecordId);
    }

    @Test
    void existsByDailyAttendanceIdAndStudentId_WhenExists_ShouldReturnTrue() {
        when(attendanceRecordRepository.existsByDailyAttendanceIdAndStudentId(testDailyAttendanceId, testStudentId)).thenReturn(true);

        boolean exists = attendanceRecordRepository.existsByDailyAttendanceIdAndStudentId(testDailyAttendanceId, testStudentId);

        assertTrue(exists);
        verify(attendanceRecordRepository, times(1)).existsByDailyAttendanceIdAndStudentId(testDailyAttendanceId, testStudentId);
    }

    @Test
    void existsByDailyAttendanceIdAndStudentId_WhenNotExists_ShouldReturnFalse() {
        when(attendanceRecordRepository.existsByDailyAttendanceIdAndStudentId(testDailyAttendanceId, testStudentId)).thenReturn(false);

        boolean exists = attendanceRecordRepository.existsByDailyAttendanceIdAndStudentId(testDailyAttendanceId, testStudentId);

        assertFalse(exists);
        verify(attendanceRecordRepository, times(1)).existsByDailyAttendanceIdAndStudentId(testDailyAttendanceId, testStudentId);
    }

    @Test
    void delete_ShouldRemoveRecord() {
        doNothing().when(attendanceRecordRepository).delete(testAttendanceRecord);

        attendanceRecordRepository.delete(testAttendanceRecord);

        verify(attendanceRecordRepository, times(1)).delete(testAttendanceRecord);
    }

    @Test
    void countByStudentIdAndDateRange_ShouldReturnCount() {
        LocalDate startDate = LocalDate.of(2024, 1, 1);
        LocalDate endDate = LocalDate.of(2024, 1, 31);
        long expectedCount = 5L;
        when(attendanceRecordRepository.countByStudentIdAndDateRange(testStudentId, startDate, endDate)).thenReturn(expectedCount);

        long count = attendanceRecordRepository.countByStudentIdAndDateRange(testStudentId, startDate, endDate);

        assertEquals(expectedCount, count);
        verify(attendanceRecordRepository, times(1)).countByStudentIdAndDateRange(testStudentId, startDate, endDate);
    }
}