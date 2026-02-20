package com.application.domain.ports.in;

import com.application.domain.model.attendance.AttendanceRecord;
import com.application.domain.model.attendance.AttendanceSummary;
import com.application.domain.model.studentmanagement.StudentId;

import java.time.LocalDate;
import java.time.YearMonth;
import java.util.List;
import java.util.Optional;

public interface AttendanceService {
    AttendanceRecord recordAttendance(AttendanceRecord record);
    Optional<AttendanceRecord> findAttendanceRecordById(Long recordId);
    List<AttendanceRecord> findAttendanceRecordsByStudentAndDateRange(StudentId studentId, LocalDate startDate, LocalDate endDate);
    AttendanceRecord updateAttendanceRecord(Long recordId, AttendanceRecord updatedRecord);
    void deleteAttendanceRecord(Long recordId);
    AttendanceSummary calculateMonthlySummary(StudentId studentId, YearMonth monthYear);
    Optional<AttendanceSummary> findSummaryByStudentAndMonth(StudentId studentId, YearMonth monthYear);
    List<AttendanceSummary> findSummariesByStudent(StudentId studentId);
}