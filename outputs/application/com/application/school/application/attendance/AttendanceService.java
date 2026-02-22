package com.application.school.application.attendance;

import com.application.school.application.attendance.dto.RegisterAttendanceCommand;
import com.application.school.application.attendance.dto.AttendanceResponse;
import com.application.school.domain.attendance.model.AttendanceRecord;
import com.application.school.domain.attendance.model.AttendanceRecordId;
import com.application.school.domain.attendance.repository.AttendanceRepository;
import com.application.school.domain.student.model.StudentId;
import com.application.school.domain.student.repository.StudentRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class AttendanceService {

    private final AttendanceRepository attendanceRepository;
    private final StudentRepository studentRepository;

    @Transactional
    public AttendanceResponse registerAttendance(RegisterAttendanceCommand command) {
        log.info("Registering attendance for studentId: {} on date: {}", command.getStudentId(), command.getDate());

        // Validar que el estudiante exista
        StudentId studentId = new StudentId(command.getStudentId());
        studentRepository.findById(studentId)
                .orElseThrow(() -> new IllegalArgumentException("Student not found with id: " + command.getStudentId()));

        // Verificar que no exista ya un registro para el mismo estudiante y fecha
        LocalDate attendanceDate = command.getDate();
        boolean existingRecord = attendanceRepository.existsByStudentIdAndDate(studentId, attendanceDate);
        if (existingRecord) {
            throw new IllegalStateException("Attendance record already exists for student " + command.getStudentId() + " on date " + attendanceDate);
        }

        // Crear el agregado AttendanceRecord
        AttendanceRecordId recordId = new AttendanceRecordId(UUID.randomUUID());
        AttendanceRecord attendanceRecord = AttendanceRecord.builder()
                .recordId(recordId)
                .studentId(studentId)
                .date(attendanceDate)
                .checkInTime(command.getCheckInTime())
                .checkOutTime(command.getCheckOutTime())
                .status(command.getStatus())
                .build();

        // Validar reglas de negocio internas del agregado (ej: checkOut no antes de checkIn)
        attendanceRecord.validate();

        // Persistir
        AttendanceRecord savedRecord = attendanceRepository.save(attendanceRecord);
        log.info("Attendance record saved with id: {}", savedRecord.getRecordId().getValue());

        // Retornar respuesta
        return AttendanceResponse.builder()
                .recordId(savedRecord.getRecordId().getValue())
                .studentId(savedRecord.getStudentId().getValue())
                .date(savedRecord.getDate())
                .checkInTime(savedRecord.getCheckInTime())
                .checkOutTime(savedRecord.getCheckOutTime())
                .status(savedRecord.getStatus())
                .build();
    }

    @Transactional(readOnly = true)
    public List<AttendanceResponse> getAttendanceByStudent(UUID studentId) {
        log.info("Fetching attendance records for studentId: {}", studentId);
        StudentId domainStudentId = new StudentId(studentId);

        List<AttendanceRecord> records = attendanceRepository.findByStudentId(domainStudentId);
        return records.stream()
                .map(record -> AttendanceResponse.builder()
                        .recordId(record.getRecordId().getValue())
                        .studentId(record.getStudentId().getValue())
                        .date(record.getDate())
                        .checkInTime(record.getCheckInTime())
                        .checkOutTime(record.getCheckOutTime())
                        .status(record.getStatus())
                        .build())
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<AttendanceResponse> getAttendanceByDate(LocalDate date) {
        log.info("Fetching attendance records for date: {}", date);
        List<AttendanceRecord> records = attendanceRepository.findByDate(date);
        return records.stream()
                .map(record -> AttendanceResponse.builder()
                        .recordId(record.getRecordId().getValue())
                        .studentId(record.getStudentId().getValue())
                        .date(record.getDate())
                        .checkInTime(record.getCheckInTime())
                        .checkOutTime(record.getCheckOutTime())
                        .status(record.getStatus())
                        .build())
                .collect(Collectors.toList());
    }

    @Transactional
    public AttendanceResponse updateCheckOut(UUID recordId, RegisterAttendanceCommand command) {
        log.info("Updating checkOut for attendance recordId: {}", recordId);
        AttendanceRecordId domainRecordId = new AttendanceRecordId(recordId);

        AttendanceRecord record = attendanceRepository.findById(domainRecordId)
                .orElseThrow(() -> new IllegalArgumentException("Attendance record not found with id: " + recordId));

        // Actualizar checkOut
        record.updateCheckOut(command.getCheckOutTime());
        AttendanceRecord updatedRecord = attendanceRepository.save(record);

        return AttendanceResponse.builder()
                .recordId(updatedRecord.getRecordId().getValue())
                .studentId(updatedRecord.getStudentId().getValue())
                .date(updatedRecord.getDate())
                .checkInTime(updatedRecord.getCheckInTime())
                .checkOutTime(updatedRecord.getCheckOutTime())
                .status(updatedRecord.getStatus())
                .build();
    }

    @Transactional(readOnly = true)
    public Double calculateMonthlyAttendanceRate(UUID studentId, int year, int month) {
        log.info("Calculating monthly attendance rate for studentId: {}, year: {}, month: {}", studentId, year, month);
        StudentId domainStudentId = new StudentId(studentId);

        List<AttendanceRecord> monthlyRecords = attendanceRepository.findByStudentIdAndMonth(domainStudentId, year, month);
        if (monthlyRecords.isEmpty()) {
            return 0.0;
        }

        long totalDays = monthlyRecords.size();
        long presentDays = monthlyRecords.stream()
                .filter(record -> record.getStatus().isPresent())
                .count();

        return (presentDays * 100.0) / totalDays;
    }
}