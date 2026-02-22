package com.application.school.infrastructure.persistence.attendance;

import com.application.school.infrastructure.persistence.attendance.entity.AttendanceRecordEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface AttendanceJpaRepository extends JpaRepository<AttendanceRecordEntity, Long> {
    Optional<AttendanceRecordEntity> findByStudentIdAndDate(Long studentId, LocalDate date);
    List<AttendanceRecordEntity> findByStudentId(Long studentId);
    List<AttendanceRecordEntity> findByDate(LocalDate date);
    List<AttendanceRecordEntity> findByStudentIdAndDateBetween(Long studentId, LocalDate startDate, LocalDate endDate);
}