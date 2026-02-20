package com.application.infrastructure.persistence.jpa;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface AttendanceJpaRepository extends JpaRepository<AttendanceRecordEntity, Long> {
    List<AttendanceRecordEntity> findByStudentStudentIdAndDateBetween(Long studentId, LocalDate startDate, LocalDate endDate);
    Optional<AttendanceRecordEntity> findByStudentStudentIdAndDate(Long studentId, LocalDate date);
    boolean existsByStudentStudentIdAndDate(Long studentId, LocalDate date);
    List<AttendanceRecordEntity> findByDate(LocalDate date);
    List<AttendanceRecordEntity> findByStudentStudentId(Long studentId);
}