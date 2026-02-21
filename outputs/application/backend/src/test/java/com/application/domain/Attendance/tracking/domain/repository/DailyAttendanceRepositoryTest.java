package com.application.domain.Attendance.tracking.domain.repository;

import com.application.domain.Attendance.tracking.domain.DailyAttendance;
import com.application.domain.Attendance.tracking.valueobject.DailyAttendanceId;
import com.application.domain.SchoolManagement.valueobject.ClassroomId;
import com.application.domain.SchoolManagement.valueobject.SchoolId;
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
class DailyAttendanceRepositoryTest {

    @Mock
    private DailyAttendanceRepository repository;

    private DailyAttendanceId dailyAttendanceId;
    private ClassroomId classroomId;
    private SchoolId schoolId;
    private LocalDate date;
    private DailyAttendance dailyAttendance;

    @BeforeEach
    void setUp() {
        dailyAttendanceId = new DailyAttendanceId();
        classroomId = new ClassroomId();
        schoolId = new SchoolId();
        date = LocalDate.now();
        dailyAttendance = mock(DailyAttendance.class);
        when(dailyAttendance.getId()).thenReturn(dailyAttendanceId);
        when(dailyAttendance.getClassroomId()).thenReturn(classroomId);
        when(dailyAttendance.getDate()).thenReturn(date);
        when(dailyAttendance.getSchoolId()).thenReturn(schoolId);
    }

    @Test
    void save_ShouldPersistDailyAttendance() {
        when(repository.save(dailyAttendance)).thenReturn(dailyAttendance);

        DailyAttendance saved = repository.save(dailyAttendance);

        assertNotNull(saved);
        assertEquals(dailyAttendanceId, saved.getId());
        verify(repository, times(1)).save(dailyAttendance);
    }

    @Test
    void findById_WhenExists_ShouldReturnDailyAttendance() {
        when(repository.findById(dailyAttendanceId)).thenReturn(Optional.of(dailyAttendance));

        Optional<DailyAttendance> found = repository.findById(dailyAttendanceId);

        assertTrue(found.isPresent());
        assertEquals(dailyAttendanceId, found.get().getId());
        verify(repository, times(1)).findById(dailyAttendanceId);
    }

    @Test
    void findById_WhenNotExists_ShouldReturnEmpty() {
        when(repository.findById(dailyAttendanceId)).thenReturn(Optional.empty());

        Optional<DailyAttendance> found = repository.findById(dailyAttendanceId);

        assertFalse(found.isPresent());
        verify(repository, times(1)).findById(dailyAttendanceId);
    }

    @Test
    void delete_ShouldRemoveDailyAttendance() {
        doNothing().when(repository).delete(dailyAttendance);

        repository.delete(dailyAttendance);

        verify(repository, times(1)).delete(dailyAttendance);
    }

    @Test
    void existsByClassroomIdAndDate_WhenExists_ShouldReturnTrue() {
        when(repository.existsByClassroomIdAndDate(classroomId, date)).thenReturn(true);

        boolean exists = repository.existsByClassroomIdAndDate(classroomId, date);

        assertTrue(exists);
        verify(repository, times(1)).existsByClassroomIdAndDate(classroomId, date);
    }

    @Test
    void existsByClassroomIdAndDate_WhenNotExists_ShouldReturnFalse() {
        when(repository.existsByClassroomIdAndDate(classroomId, date)).thenReturn(false);

        boolean exists = repository.existsByClassroomIdAndDate(classroomId, date);

        assertFalse(exists);
        verify(repository, times(1)).existsByClassroomIdAndDate(classroomId, date);
    }

    @Test
    void findByClassroomIdAndDate_WhenExists_ShouldReturnDailyAttendance() {
        when(repository.findByClassroomIdAndDate(classroomId, date)).thenReturn(Optional.of(dailyAttendance));

        Optional<DailyAttendance> found = repository.findByClassroomIdAndDate(classroomId, date);

        assertTrue(found.isPresent());
        assertEquals(classroomId, found.get().getClassroomId());
        assertEquals(date, found.get().getDate());
        verify(repository, times(1)).findByClassroomIdAndDate(classroomId, date);
    }

    @Test
    void findByClassroomIdAndDate_WhenNotExists_ShouldReturnEmpty() {
        when(repository.findByClassroomIdAndDate(classroomId, date)).thenReturn(Optional.empty());

        Optional<DailyAttendance> found = repository.findByClassroomIdAndDate(classroomId, date);

        assertFalse(found.isPresent());
        verify(repository, times(1)).findByClassroomIdAndDate(classroomId, date);
    }

    @Test
    void findAllBySchoolIdAndDate_ShouldReturnList() {
        when(repository.findAllBySchoolIdAndDate(schoolId, date)).thenReturn(java.util.List.of(dailyAttendance));

        java.util.List<DailyAttendance> list = repository.findAllBySchoolIdAndDate(schoolId, date);

        assertNotNull(list);
        assertFalse(list.isEmpty());
        assertEquals(1, list.size());
        assertEquals(schoolId, list.get(0).getSchoolId());
        assertEquals(date, list.get(0).getDate());
        verify(repository, times(1)).findAllBySchoolIdAndDate(schoolId, date);
    }

    @Test
    void findAllBySchoolIdAndDate_WhenNone_ShouldReturnEmptyList() {
        when(repository.findAllBySchoolIdAndDate(schoolId, date)).thenReturn(java.util.List.of());

        java.util.List<DailyAttendance> list = repository.findAllBySchoolIdAndDate(schoolId, date);

        assertNotNull(list);
        assertTrue(list.isEmpty());
        verify(repository, times(1)).findAllBySchoolIdAndDate(schoolId, date);
    }
}