package com.application.school.application.attendance;

import com.application.school.domain.attendance.model.AttendanceRecord;
import com.application.school.domain.attendance.model.AttendanceRecordId;
import com.application.school.domain.attendance.model.AttendanceStatus;
import com.application.school.domain.attendance.repository.AttendanceRepository;
import com.application.school.domain.student.model.StudentId;
import com.application.school.domain.student.repository.StudentRepository;
import com.application.school.application.dtos.AttendanceRecordDTO;
import com.application.school.application.mappers.AttendanceMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class AttendanceService {

    private final AttendanceRepository attendanceRepository;
    private final StudentRepository studentRepository;
    private final AttendanceMapper attendanceMapper;

    @Transactional
    public AttendanceRecordDTO registerCheckIn(StudentId studentId, LocalDate date, LocalDateTime checkInTime) {
        log.info("Registering check-in for student {} on date {}", studentId, date);

        var student = studentRepository.findById(studentId)
                .orElseThrow(() -> new IllegalArgumentException("Student not found with id: " + studentId));
        if (!student.isActive()) {
            throw new IllegalStateException("Cannot register attendance for inactive student: " + studentId);
        }

        Optional<AttendanceRecord> existingRecord = attendanceRepository.findByStudentIdAndDate(studentId, date);
        if (existingRecord.isPresent()) {
            throw new IllegalStateException("Attendance record already exists for student " + studentId + " on date " + date);
        }

        AttendanceRecord newRecord = AttendanceRecord.builder()
                .recordId(AttendanceRecordId.generate())
                .studentId(studentId)
                .date(date)
                .checkInTime(checkInTime)
                .status(AttendanceStatus.PRESENT)
                .build();

        AttendanceRecord savedRecord = attendanceRepository.save(newRecord);
        log.debug("Check-in registered successfully with recordId: {}", savedRecord.getRecordId());
        return attendanceMapper.toDTO(savedRecord);
    }

    @Transactional
    public AttendanceRecordDTO registerCheckOut(StudentId studentId, LocalDate date, LocalDateTime checkOutTime) {
        log.info("Registering check-out for student {} on date {}", studentId, date);

        AttendanceRecord record = attendanceRepository.findByStudentIdAndDate(studentId, date)
                .orElseThrow(() -> new IllegalArgumentException("No attendance record found for student " + studentId + " on date " + date));

        if (record.getCheckOutTime() != null) {
            throw new IllegalStateException("Check-out already registered for student " + studentId + " on date " + date);
        }
        if (checkOutTime.isBefore(record.getCheckInTime())) {
            throw new IllegalArgumentException("Check-out time cannot be before check-in time");
        }

        record.setCheckOutTime(checkOutTime);
        AttendanceRecord updatedRecord = attendanceRepository.save(record);
        log.debug("Check-out registered successfully for recordId: {}", updatedRecord.getRecordId());
        return attendanceMapper.toDTO(updatedRecord);
    }

    @Transactional(readOnly = true)
    public List<AttendanceRecordDTO> getAttendanceByStudent(StudentId studentId, LocalDate startDate, LocalDate endDate) {
        log.info("Fetching attendance for student {} between {} and {}", studentId, startDate, endDate);
        List<AttendanceRecord> records = attendanceRepository.findByStudentIdAndDateRange(studentId, startDate, endDate);
        return records.stream()
                .map(attendanceMapper::toDTO)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<AttendanceRecordDTO> getAttendanceByDate(LocalDate date) {
        log.info("Fetching attendance for date {}", date);
        List<AttendanceRecord> records = attendanceRepository.findByDate(date);
        return records.stream()
                .map(attendanceMapper::toDTO)
                .collect(Collectors.toList());
    }

    @Transactional
    public AttendanceRecordDTO updateAttendanceStatus(AttendanceRecordId recordId, AttendanceStatus newStatus) {
        log.info("Updating status for record {} to {}", recordId, newStatus);
        AttendanceRecord record = attendanceRepository.findById(recordId)
                .orElseThrow(() -> new IllegalArgumentException("Attendance record not found with id: " + recordId));

        record.setStatus(newStatus);
        AttendanceRecord updatedRecord = attendanceRepository.save(record);
        log.debug("Status updated successfully for recordId: {}", updatedRecord.getRecordId());
        return attendanceMapper.toDTO(updatedRecord);
    }

    @Transactional
    public void deleteAttendanceRecord(AttendanceRecordId recordId) {
        log.info("Deleting attendance record {}", recordId);
        if (!attendanceRepository.existsById(recordId)) {
            throw new IllegalArgumentException("Attendance record not found with id: " + recordId);
        }
        attendanceRepository.deleteById(recordId);
        log.debug("Attendance record deleted successfully");
    }
}