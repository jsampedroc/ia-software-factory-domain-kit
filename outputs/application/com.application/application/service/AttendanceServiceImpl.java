package com.application.application.service;

import com.application.domain.model.attendance.AttendanceRecord;
import com.application.domain.model.attendance.AttendanceRecordId;
import com.application.domain.model.attendance.AttendanceSummary;
import com.application.domain.model.attendance.AttendanceSummaryId;
import com.application.domain.model.studentmanagement.StudentId;
import com.application.domain.ports.in.AttendanceService;
import com.application.domain.ports.out.AttendanceRepository;
import com.application.application.dto.AttendanceRecordDTO;
import com.application.application.dto.AttendanceSummaryDTO;
import com.application.application.mapper.AttendanceMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.YearMonth;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Transactional
@RequiredArgsConstructor
@Slf4j
public class AttendanceServiceImpl implements AttendanceService {

    private final AttendanceRepository attendanceRepository;
    private final AttendanceMapper attendanceMapper;

    @Override
    public AttendanceRecordDTO recordAttendance(AttendanceRecordDTO recordDTO) {
        log.info("Recording attendance for student ID: {} on date: {}", recordDTO.getStudentId(), recordDTO.getDate());
        AttendanceRecord domainRecord = attendanceMapper.toDomain(recordDTO);
        AttendanceRecord savedRecord = attendanceRepository.saveAttendanceRecord(domainRecord);
        return attendanceMapper.toDTO(savedRecord);
    }

    @Override
    public Optional<AttendanceRecordDTO> findAttendanceRecordById(AttendanceRecordId recordId) {
        log.debug("Finding attendance record by ID: {}", recordId);
        return attendanceRepository.findAttendanceRecordById(recordId)
                .map(attendanceMapper::toDTO);
    }

    @Override
    public List<AttendanceRecordDTO> findAttendanceRecordsByStudentAndDateRange(StudentId studentId, LocalDate startDate, LocalDate endDate) {
        log.debug("Finding attendance records for student ID: {} between {} and {}", studentId, startDate, endDate);
        List<AttendanceRecord> records = attendanceRepository.findAttendanceRecordsByStudentAndDateRange(studentId, startDate, endDate);
        return records.stream()
                .map(attendanceMapper::toDTO)
                .collect(Collectors.toList());
    }

    @Override
    public AttendanceRecordDTO updateAttendanceRecord(AttendanceRecordId recordId, AttendanceRecordDTO updateDTO) {
        log.info("Updating attendance record with ID: {}", recordId);
        AttendanceRecord existingRecord = attendanceRepository.findAttendanceRecordById(recordId)
                .orElseThrow(() -> new RuntimeException("Attendance record not found with id: " + recordId));

        AttendanceRecord updatedDomain = attendanceMapper.toDomain(updateDTO);
        updatedDomain.setId(existingRecord.getId());

        AttendanceRecord savedRecord = attendanceRepository.saveAttendanceRecord(updatedDomain);
        return attendanceMapper.toDTO(savedRecord);
    }

    @Override
    public void deleteAttendanceRecord(AttendanceRecordId recordId) {
        log.info("Deleting attendance record with ID: {}", recordId);
        attendanceRepository.deleteAttendanceRecord(recordId);
    }

    @Override
    public AttendanceSummaryDTO generateMonthlySummary(StudentId studentId, YearMonth monthYear) {
        log.info("Generating monthly attendance summary for student ID: {} and month/year: {}", studentId, monthYear);
        AttendanceSummary summary = attendanceRepository.generateOrGetMonthlySummary(studentId, monthYear);
        return attendanceMapper.toDTO(summary);
    }

    @Override
    public Optional<AttendanceSummaryDTO> findAttendanceSummaryById(AttendanceSummaryId summaryId) {
        log.debug("Finding attendance summary by ID: {}", summaryId);
        return attendanceRepository.findAttendanceSummaryById(summaryId)
                .map(attendanceMapper::toDTO);
    }

    @Override
    public List<AttendanceSummaryDTO> findAttendanceSummariesByStudent(StudentId studentId) {
        log.debug("Finding all attendance summaries for student ID: {}", studentId);
        List<AttendanceSummary> summaries = attendanceRepository.findAttendanceSummariesByStudent(studentId);
        return summaries.stream()
                .map(attendanceMapper::toDTO)
                .collect(Collectors.toList());
    }
}