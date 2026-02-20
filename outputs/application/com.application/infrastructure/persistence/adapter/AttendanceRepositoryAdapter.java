package com.application.infrastructure.persistence.adapter;

import com.application.domain.model.attendance.AttendanceRecord;
import com.application.domain.model.attendance.AttendanceRecordId;
import com.application.domain.model.attendance.AttendanceSummary;
import com.application.domain.model.attendance.AttendanceSummaryId;
import com.application.domain.model.studentmanagement.StudentId;
import com.application.domain.ports.out.AttendanceRepository;
import com.application.infrastructure.persistence.jpa.AttendanceJpaRepository;
import com.application.infrastructure.persistence.jpa.AttendanceRecordEntity;
import com.application.infrastructure.persistence.jpa.AttendanceSummaryEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.YearMonth;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class AttendanceRepositoryAdapter implements AttendanceRepository {

    private final AttendanceJpaRepository attendanceJpaRepository;

    @Override
    public AttendanceRecord saveRecord(AttendanceRecord record) {
        AttendanceRecordEntity entity = AttendanceRecordEntity.fromDomain(record);
        AttendanceRecordEntity savedEntity = attendanceJpaRepository.saveRecord(entity);
        return savedEntity.toDomain();
    }

    @Override
    public Optional<AttendanceRecord> findRecordById(AttendanceRecordId recordId) {
        return attendanceJpaRepository.findRecordById(recordId.getValue())
                .map(AttendanceRecordEntity::toDomain);
    }

    @Override
    public List<AttendanceRecord> findRecordsByStudentAndDateRange(StudentId studentId, LocalDate startDate, LocalDate endDate) {
        return attendanceJpaRepository.findRecordsByStudentIdAndDateBetween(studentId.getValue(), startDate, endDate)
                .stream()
                .map(AttendanceRecordEntity::toDomain)
                .collect(Collectors.toList());
    }

    @Override
    public boolean existsRecordForStudentAndDate(StudentId studentId, LocalDate date) {
        return attendanceJpaRepository.existsByStudentIdAndDate(studentId.getValue(), date);
    }

    @Override
    public AttendanceSummary saveSummary(AttendanceSummary summary) {
        AttendanceSummaryEntity entity = AttendanceSummaryEntity.fromDomain(summary);
        AttendanceSummaryEntity savedEntity = attendanceJpaRepository.saveSummary(entity);
        return savedEntity.toDomain();
    }

    @Override
    public Optional<AttendanceSummary> findSummaryById(AttendanceSummaryId summaryId) {
        return attendanceJpaRepository.findSummaryById(summaryId.getValue())
                .map(AttendanceSummaryEntity::toDomain);
    }

    @Override
    public Optional<AttendanceSummary> findSummaryByStudentAndMonth(StudentId studentId, YearMonth month) {
        return attendanceJpaRepository.findByStudentIdAndMonthAndYear(studentId.getValue(), month.getMonthValue(), month.getYear())
                .map(AttendanceSummaryEntity::toDomain);
    }

    @Override
    public List<AttendanceSummary> findSummariesByStudent(StudentId studentId) {
        return attendanceJpaRepository.findSummariesByStudentId(studentId.getValue())
                .stream()
                .map(AttendanceSummaryEntity::toDomain)
                .collect(Collectors.toList());
    }

    @Override
    public void deleteRecord(AttendanceRecordId recordId) {
        attendanceJpaRepository.deleteRecordById(recordId.getValue());
    }

    @Override
    public void deleteSummary(AttendanceSummaryId summaryId) {
        attendanceJpaRepository.deleteSummaryById(summaryId.getValue());
    }
}