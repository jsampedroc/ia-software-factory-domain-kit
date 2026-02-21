package com.application.infrastructure.persistence.Attendance.tracking.adapter;

import com.application.domain.Attendance.tracking.domain.DailyAttendance;
import com.application.domain.Attendance.tracking.domain.repository.DailyAttendanceRepository;
import com.application.domain.Attendance.valueobject.DailyAttendanceId;
import com.application.domain.SchoolManagement.valueobject.ClassroomId;
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
class DailyAttendanceRepositoryImplTest {

    @Mock
    private DailyAttendanceJpaRepository dailyAttendanceJpaRepository;

    @Mock
    private DailyAttendanceEntityMapper dailyAttendanceEntityMapper;

    @InjectMocks
    private DailyAttendanceRepositoryImpl dailyAttendanceRepositoryImpl;

    private DailyAttendanceId dailyAttendanceId;
    private ClassroomId classroomId;
    private LocalDate date;
    private DailyAttendance domainDailyAttendance;
    private DailyAttendanceEntity entityDailyAttendance;

    @BeforeEach
    void setUp() {
        dailyAttendanceId = new DailyAttendanceId(UUID.randomUUID());
        classroomId = new ClassroomId(UUID.randomUUID());
        date = LocalDate.now();
        domainDailyAttendance = mock(DailyAttendance.class);
        entityDailyAttendance = mock(DailyAttendanceEntity.class);
    }

    @Test
    void save_ShouldCallJpaRepositoryAndMapper() {
        when(dailyAttendanceEntityMapper.toEntity(domainDailyAttendance)).thenReturn(entityDailyAttendance);
        when(dailyAttendanceJpaRepository.save(entityDailyAttendance)).thenReturn(entityDailyAttendance);
        when(dailyAttendanceEntityMapper.toDomain(entityDailyAttendance)).thenReturn(domainDailyAttendance);

        DailyAttendance result = dailyAttendanceRepositoryImpl.save(domainDailyAttendance);

        assertNotNull(result);
        verify(dailyAttendanceEntityMapper).toEntity(domainDailyAttendance);
        verify(dailyAttendanceJpaRepository).save(entityDailyAttendance);
        verify(dailyAttendanceEntityMapper).toDomain(entityDailyAttendance);
    }

    @Test
    void findById_WhenEntityExists_ShouldReturnDomainObject() {
        when(dailyAttendanceJpaRepository.findById(dailyAttendanceId.getValue())).thenReturn(Optional.of(entityDailyAttendance));
        when(dailyAttendanceEntityMapper.toDomain(entityDailyAttendance)).thenReturn(domainDailyAttendance);

        Optional<DailyAttendance> result = dailyAttendanceRepositoryImpl.findById(dailyAttendanceId);

        assertTrue(result.isPresent());
        assertEquals(domainDailyAttendance, result.get());
        verify(dailyAttendanceJpaRepository).findById(dailyAttendanceId.getValue());
        verify(dailyAttendanceEntityMapper).toDomain(entityDailyAttendance);
    }

    @Test
    void findById_WhenEntityDoesNotExist_ShouldReturnEmpty() {
        when(dailyAttendanceJpaRepository.findById(dailyAttendanceId.getValue())).thenReturn(Optional.empty());

        Optional<DailyAttendance> result = dailyAttendanceRepositoryImpl.findById(dailyAttendanceId);

        assertTrue(result.isEmpty());
        verify(dailyAttendanceJpaRepository).findById(dailyAttendanceId.getValue());
        verify(dailyAttendanceEntityMapper, never()).toDomain(any());
    }

    @Test
    void existsByClassroomIdAndDate_ShouldCallJpaRepositoryWithCorrectParameters() {
        when(dailyAttendanceJpaRepository.existsByClassroomIdAndDate(classroomId.getValue(), date)).thenReturn(true);

        boolean result = dailyAttendanceRepositoryImpl.existsByClassroomIdAndDate(classroomId, date);

        assertTrue(result);
        verify(dailyAttendanceJpaRepository).existsByClassroomIdAndDate(classroomId.getValue(), date);
    }

    @Test
    void delete_ShouldCallJpaRepository() {
        dailyAttendanceRepositoryImpl.delete(domainDailyAttendance);

        verify(dailyAttendanceEntityMapper).toEntity(domainDailyAttendance);
        verify(dailyAttendanceJpaRepository).delete(any(DailyAttendanceEntity.class));
    }
}