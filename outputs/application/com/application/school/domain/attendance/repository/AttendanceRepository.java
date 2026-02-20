package com.application.school.domain.attendance.repository;

import com.application.school.domain.attendance.model.AttendanceRecord;
import com.application.school.domain.attendance.model.AttendanceRecordId;
import com.application.school.domain.student.model.StudentId;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface AttendanceRepository {
    AttendanceRecord save(AttendanceRecord attendanceRecord);
    Optional<AttendanceRecord> findById(AttendanceRecordId id);
    List<AttendanceRecord> findByStudentIdAndDateBetween(StudentId studentId, LocalDate startDate, LocalDate endDate);
    List<AttendanceRecord> findByDate(LocalDate date);
    boolean existsByStudentIdAndDate(StudentId studentId, LocalDate date);
    void delete(AttendanceRecord attendanceRecord);
}