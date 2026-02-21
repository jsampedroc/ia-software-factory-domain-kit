package com.application.infrastructure.persistence.Attendance.tracking.adapter;

import com.application.domain.Attendance.tracking.domain.DailyAttendance;
import com.application.domain.Attendance.tracking.domain.repository.DailyAttendanceRepository;
import com.application.domain.Attendance.valueobject.DailyAttendanceId;
import com.application.domain.SchoolManagement.valueobject.ClassroomId;
import com.application.infrastructure.persistence.Attendance.tracking.entity.DailyAttendanceEntity;
import com.application.infrastructure.persistence.Attendance.tracking.mapper.DailyAttendanceEntityMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class DailyAttendanceRepositoryImpl implements DailyAttendanceRepository {

    private final DailyAttendanceJpaRepository dailyAttendanceJpaRepository;
    private final DailyAttendanceEntityMapper mapper;

    @Override
    public DailyAttendance save(DailyAttendance dailyAttendance) {
        DailyAttendanceEntity entity = mapper.toEntity(dailyAttendance);
        DailyAttendanceEntity savedEntity = dailyAttendanceJpaRepository.save(entity);
        return mapper.toDomain(savedEntity);
    }

    @Override
    public Optional<DailyAttendance> findById(DailyAttendanceId id) {
        return dailyAttendanceJpaRepository.findById(id.getValue())
                .map(mapper::toDomain);
    }

    @Override
    public boolean existsByClassroomIdAndDate(ClassroomId classroomId, LocalDate date) {
        return dailyAttendanceJpaRepository.existsByClassroomIdAndDate(classroomId.getValue(), date);
    }

    @Override
    public Optional<DailyAttendance> findByClassroomIdAndDate(ClassroomId classroomId, LocalDate date) {
        return dailyAttendanceJpaRepository.findByClassroomIdAndDate(classroomId.getValue(), date)
                .map(mapper::toDomain);
    }

    @Override
    public void delete(DailyAttendance dailyAttendance) {
        dailyAttendanceJpaRepository.deleteById(dailyAttendance.getId().getValue());
    }
}