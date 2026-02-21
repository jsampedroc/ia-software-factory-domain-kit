package com.application.domain.Attendance.tracking.domain.repository;

import com.application.domain.Attendance.tracking.domain.AttendanceRecord;
import com.application.domain.Attendance.tracking.valueobject.AttendanceRecordId;
import com.application.domain.SchoolManagement.valueobject.StudentId;
import com.application.domain.shared.repository.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface AttendanceRecordRepository extends Repository<AttendanceRecord, AttendanceRecordId> {
    Optional<AttendanceRecord> findByDailyAttendanceIdAndStudentId(AttendanceRecordId dailyAttendanceId, StudentId studentId);
    List<AttendanceRecord> findByStudentIdAndDateRange(StudentId studentId, LocalDate startDate, LocalDate endDate);
    List<AttendanceRecord> findByDailyAttendanceId(AttendanceRecordId dailyAttendanceId);
    boolean existsByDailyAttendanceIdAndStudentId(AttendanceRecordId dailyAttendanceId, StudentId studentId);
}