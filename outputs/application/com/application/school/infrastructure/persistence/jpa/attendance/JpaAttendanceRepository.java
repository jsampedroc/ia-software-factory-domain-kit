package com.application.school.infrastructure.persistence.jpa.attendance;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface JpaAttendanceRepository extends JpaRepository<AttendanceJpaEntity, UUID> {
    Optional<AttendanceJpaEntity> findByStudentIdAndDate(UUID studentId, LocalDate date);
    List<AttendanceJpaEntity> findAllByStudentId(UUID studentId);
    List<AttendanceJpaEntity> findAllByDate(LocalDate date);
    List<AttendanceJpaEntity> findAllByStudentIdAndDateBetween(UUID studentId, LocalDate startDate, LocalDate endDate);
}