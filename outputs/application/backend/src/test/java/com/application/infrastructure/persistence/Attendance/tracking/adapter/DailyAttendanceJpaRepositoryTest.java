package com.application.infrastructure.persistence.Attendance.tracking.adapter;

import com.application.domain.Attendance.tracking.domain.DailyAttendance;
import com.application.domain.Attendance.tracking.domain.repository.DailyAttendanceRepository;
import com.application.domain.Attendance.valueobject.DailyAttendanceId;
import com.application.domain.SchoolManagement.valueobject.ClassroomId;
import com.application.domain.SchoolManagement.valueobject.SchoolId;
import com.application.infrastructure.persistence.Attendance.tracking.entity.DailyAttendanceEntity;
import com.application.infrastructure.persistence.Attendance.tracking.mapper.DailyAttendanceEntityMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class DailyAttendanceJpaRepositoryTest {

    @Mock
    private DailyAttendanceJpaRepository springDataRepository;

    @Mock
    private DailyAttendanceEntityMapper mapper;

    @InjectMocks
    private DailyAttendanceRepositoryImpl repository;

    private DailyAttendanceId dailyAttendanceId;
    private ClassroomId classroomId;
    private SchoolId schoolId;
    private LocalDate date;
    private DailyAttendance domainDailyAttendance;
    private DailyAttendanceEntity entity;

    @BeforeEach
    void setUp() {
        dailyAttendanceId = new DailyAttendanceId(UUID.randomUUID());
        classroomId = new ClassroomId(UUID.randomUUID());
        schoolId = new SchoolId(UUID.randomUUID());
        date = LocalDate.now();

        domainDailyAttendance = mock(DailyAttendance.class);
        entity = mock(DailyAttendanceEntity.class);
    }

    @Test
    void save_ShouldCallRepositoryAndMapper() {
        when(mapper.toEntity(domainDailyAttendance)).thenReturn(entity);
        when(springDataRepository.save(entity)).thenReturn(entity);
        when(mapper.toDomain(entity)).thenReturn(domainDailyAttendance);

        DailyAttendance result = repository.save(domainDailyAttendance);

        assertNotNull(result);
        verify(mapper).toEntity(domainDailyAttendance);
        verify(springDataRepository).save(entity);
        verify(mapper).toDomain(entity);
    }

    @Test
    void findById_WhenEntityExists_ShouldReturnDomain() {
        when(springDataRepository.findById(dailyAttendanceId.getValue())).thenReturn(Optional.of(entity));
        when(mapper.toDomain(entity)).thenReturn(domainDailyAttendance);

        Optional<DailyAttendance> result = repository.findById(dailyAttendanceId);

        assertTrue(result.isPresent());
        assertEquals(domainDailyAttendance, result.get());
        verify(springDataRepository).findById(dailyAttendanceId.getValue());
        verify(mapper).toDomain(entity);
    }

    @Test
    void findById_WhenEntityDoesNotExist_ShouldReturnEmpty() {
        when(springDataRepository.findById(dailyAttendanceId.getValue())).thenReturn(Optional.empty());

        Optional<DailyAttendance> result = repository.findById(dailyAttendanceId);

        assertFalse(result.isPresent());
        verify(springDataRepository).findById(dailyAttendanceId.getValue());
        verify(mapper, never()).toDomain(any());
    }

    @Test
    void existsByClassroomIdAndDate_ShouldCallRepositoryWithCorrectParameters() {
        when(springDataRepository.existsByClassroomIdAndDate(classroomId.getValue(), date)).thenReturn(true);

        boolean result = repository.existsByClassroomIdAndDate(classroomId, date);

        assertTrue(result);
        verify(springDataRepository).existsByClassroomIdAndDate(classroomId.getValue(), date);
    }

    @Test
    void findByClassroomIdAndDate_WhenEntityExists_ShouldReturnDomain() {
        when(springDataRepository.findByClassroomIdAndDate(classroomId.getValue(), date)).thenReturn(Optional.of(entity));
        when(mapper.toDomain(entity)).thenReturn(domainDailyAttendance);

        Optional<DailyAttendance> result = repository.findByClassroomIdAndDate(classroomId, date);

        assertTrue(result.isPresent());
        assertEquals(domainDailyAttendance, result.get());
        verify(springDataRepository).findByClassroomIdAndDate(classroomId.getValue(), date);
        verify(mapper).toDomain(entity);
    }

    @Test
    void findByClassroomIdAndDate_WhenEntityDoesNotExist_ShouldReturnEmpty() {
        when(springDataRepository.findByClassroomIdAndDate(classroomId.getValue(), date)).thenReturn(Optional.empty());

        Optional<DailyAttendance> result = repository.findByClassroomIdAndDate(classroomId, date);

        assertFalse(result.isPresent());
        verify(springDataRepository).findByClassroomIdAndDate(classroomId.getValue(), date);
        verify(mapper, never()).toDomain(any());
    }

    @Test
    void delete_ShouldCallRepository() {
        repository.delete(domainDailyAttendance);

        verify(springDataRepository).delete(any(DailyAttendanceEntity.class));
    }

    @Test
    void deleteById_ShouldCallRepository() {
        repository.deleteById(dailyAttendanceId);

        verify(springDataRepository).deleteById(dailyAttendanceId.getValue());
    }

    @Test
    void findAllBySchoolIdAndDateBetween_ShouldCallRepositoryAndMapResults() {
        LocalDate startDate = date.minusDays(1);
        LocalDate endDate = date.plusDays(1);
        when(springDataRepository.findAllBySchoolIdAndDateBetween(schoolId.getValue(), startDate, endDate))
                .thenReturn(java.util.List.of(entity));
        when(mapper.toDomain(entity)).thenReturn(domainDailyAttendance);

        java.util.List<DailyAttendance> result = repository.findAllBySchoolIdAndDateBetween(schoolId, startDate, endDate);

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(domainDailyAttendance, result.get(0));
        verify(springDataRepository).findAllBySchoolIdAndDateBetween(schoolId.getValue(), startDate, endDate);
        verify(mapper).toDomain(entity);
    }
}