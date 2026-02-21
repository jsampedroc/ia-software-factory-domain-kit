package com.application.infrastructure.persistence.Attendance.tracking.adapter;

import com.application.infrastructure.persistence.Attendance.tracking.entity.DailyAttendanceEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface DailyAttendanceJpaRepository extends JpaRepository<DailyAttendanceEntity, UUID> {
    Optional<DailyAttendanceEntity> findByClassroomIdAndDate(UUID classroomId, LocalDate date);
    boolean existsByClassroomIdAndDate(UUID classroomId, LocalDate date);
}