package com.application.domain.Attendance.tracking.domain;

import com.application.domain.Attendance.tracking.domain.repository.DailyAttendanceRepository;
import com.application.domain.Attendance.valueobject.DailyAttendanceId;
import com.application.domain.SchoolManagement.valueobject.ClassroomId;
import com.application.domain.SchoolManagement.valueobject.SchoolId;
import com.application.domain.shared.Entity;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class DailyAttendanceTest {

    @Mock
    private DailyAttendanceRepository repository;

    private DailyAttendanceId testId;
    private ClassroomId testClassroomId;
    private SchoolId testSchoolId;
    private LocalDate testDate;

    @BeforeEach
    void setUp() {
        testId = new DailyAttendanceId();
        testClassroomId = new ClassroomId();
        testSchoolId = new SchoolId();
        testDate = LocalDate.now();
    }

    @Test
    void create_ShouldCreateDailyAttendance_WhenValidParameters() {
        when(repository.findByClassroomIdAndDate(any(ClassroomId.class), any(LocalDate.class)))
                .thenReturn(Optional.empty());

        DailyAttendance result = DailyAttendance.create(testClassroomId, testDate, testSchoolId, repository);

        assertNotNull(result);
        assertNotNull(result.getId());
        assertEquals(testClassroomId, result.getClassroomId());
        assertEquals(testDate, result.getDate());
        assertEquals(testSchoolId, result.getSchoolId());
        verify(repository).findByClassroomIdAndDate(testClassroomId, testDate);
    }

    @Test
    void create_ShouldThrowDomainException_WhenAttendanceAlreadyExistsForClassroomAndDate() {
        DailyAttendance existingAttendance = mock(DailyAttendance.class);
        when(repository.findByClassroomIdAndDate(any(ClassroomId.class), any(LocalDate.class)))
                .thenReturn(Optional.of(existingAttendance));

        DomainException exception = assertThrows(DomainException.class,
                () -> DailyAttendance.create(testClassroomId, testDate, testSchoolId, repository));

        assertTrue(exception.getMessage().contains("ya existe"));
        verify(repository).findByClassroomIdAndDate(testClassroomId, testDate);
        verify(repository, never()).save(any());
    }

    @Test
    void create_ShouldThrowDomainException_WhenDateIsFuture() {
        LocalDate futureDate = LocalDate.now().plusDays(1);
        when(repository.findByClassroomIdAndDate(any(ClassroomId.class), any(LocalDate.class)))
                .thenReturn(Optional.empty());

        DomainException exception = assertThrows(DomainException.class,
                () -> DailyAttendance.create(testClassroomId, futureDate, testSchoolId, repository));

        assertTrue(exception.getMessage().contains("futura"));
        verify(repository).findByClassroomIdAndDate(testClassroomId, futureDate);
        verify(repository, never()).save(any());
    }

    @Test
    void getId_ShouldReturnId() {
        DailyAttendance attendance = new DailyAttendance(testId, testClassroomId, testDate, testSchoolId);
        assertEquals(testId, attendance.getId());
    }

    @Test
    void getClassroomId_ShouldReturnClassroomId() {
        DailyAttendance attendance = new DailyAttendance(testId, testClassroomId, testDate, testSchoolId);
        assertEquals(testClassroomId, attendance.getClassroomId());
    }

    @Test
    void getDate_ShouldReturnDate() {
        DailyAttendance attendance = new DailyAttendance(testId, testClassroomId, testDate, testSchoolId);
        assertEquals(testDate, attendance.getDate());
    }

    @Test
    void getSchoolId_ShouldReturnSchoolId() {
        DailyAttendance attendance = new DailyAttendance(testId, testClassroomId, testDate, testSchoolId);
        assertEquals(testSchoolId, attendance.getSchoolId());
    }

    @Test
    void equals_ShouldReturnTrue_WhenSameId() {
        DailyAttendance attendance1 = new DailyAttendance(testId, testClassroomId, testDate, testSchoolId);
        DailyAttendance attendance2 = new DailyAttendance(testId, new ClassroomId(), LocalDate.now().minusDays(1), new SchoolId());
        assertEquals(attendance1, attendance2);
    }

    @Test
    void equals_ShouldReturnFalse_WhenDifferentId() {
        DailyAttendance attendance1 = new DailyAttendance(testId, testClassroomId, testDate, testSchoolId);
        DailyAttendance attendance2 = new DailyAttendance(new DailyAttendanceId(), testClassroomId, testDate, testSchoolId);
        assertNotEquals(attendance1, attendance2);
    }

    @Test
    void hashCode_ShouldBeSame_WhenSameId() {
        DailyAttendance attendance1 = new DailyAttendance(testId, testClassroomId, testDate, testSchoolId);
        DailyAttendance attendance2 = new DailyAttendance(testId, new ClassroomId(), LocalDate.now().minusDays(1), new SchoolId());
        assertEquals(attendance1.hashCode(), attendance2.hashCode());
    }

    @Test
    void hashCode_ShouldBeDifferent_WhenDifferentId() {
        DailyAttendance attendance1 = new DailyAttendance(testId, testClassroomId, testDate, testSchoolId);
        DailyAttendance attendance2 = new DailyAttendance(new DailyAttendanceId(), testClassroomId, testDate, testSchoolId);
        assertNotEquals(attendance1.hashCode(), attendance2.hashCode());
    }
}