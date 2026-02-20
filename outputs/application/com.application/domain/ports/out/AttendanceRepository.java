package com.application.domain.ports.out;

import com.application.domain.model.attendance.AttendanceRecord;
import com.application.domain.model.attendance.AttendanceRecordId;
import com.application.domain.model.attendance.AttendanceSummary;
import com.application.domain.model.attendance.AttendanceSummaryId;
import com.application.domain.model.studentmanagement.StudentId;

import java.time.LocalDate;
import java.time.YearMonth;
import java.util.List;
import java.util.Optional;

public interface AttendanceRepository {
    // AttendanceRecord operations
    AttendanceRecord saveRecord(AttendanceRecord record);
    Optional<AttendanceRecord> findRecordById(AttendanceRecordId id);
    List<AttendanceRecord> findRecordsByStudentAndDateRange(StudentId studentId, LocalDate startDate, LocalDate endDate);
    List<AttendanceRecord> findRecordsByDate(LocalDate date);
    void deleteRecord(AttendanceRecordId id);
    boolean existsRecordForStudentOnDate(StudentId studentId, LocalDate date);

    // AttendanceSummary operations
    AttendanceSummary saveSummary(AttendanceSummary summary);
    Optional<AttendanceSummary> findSummaryById(AttendanceSummaryId id);
    Optional<AttendanceSummary> findSummaryByStudentAndMonth(StudentId studentId, YearMonth month);
    List<AttendanceSummary> findSummariesByStudent(StudentId studentId);
    void deleteSummary(AttendanceSummaryId id);
}