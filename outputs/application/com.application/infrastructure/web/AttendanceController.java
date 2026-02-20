package com.application.infrastructure.web;

import com.application.application.dto.AttendanceRecordDTO;
import com.application.application.dto.AttendanceSummaryDTO;
import com.application.domain.ports.in.AttendanceService;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/attendance")
@RequiredArgsConstructor
public class AttendanceController {

    private final AttendanceService attendanceService;

    @PostMapping("/record")
    public ResponseEntity<AttendanceRecordDTO> recordAttendance(@RequestBody AttendanceRecordDTO attendanceRecordDTO) {
        AttendanceRecordDTO recorded = attendanceService.recordAttendance(attendanceRecordDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(recorded);
    }

    @GetMapping("/student/{studentId}")
    public ResponseEntity<List<AttendanceRecordDTO>> getAttendanceByStudent(
            @PathVariable UUID studentId,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate) {
        List<AttendanceRecordDTO> records = attendanceService.getAttendanceByStudentAndDateRange(studentId, startDate, endDate);
        return ResponseEntity.ok(records);
    }

    @GetMapping("/summary/{studentId}")
    public ResponseEntity<AttendanceSummaryDTO> getMonthlySummary(
            @PathVariable UUID studentId,
            @RequestParam int month,
            @RequestParam int year) {
        AttendanceSummaryDTO summary = attendanceService.getMonthlySummary(studentId, month, year);
        return ResponseEntity.ok(summary);
    }

    @PutMapping("/record/{recordId}")
    public ResponseEntity<AttendanceRecordDTO> updateAttendanceRecord(
            @PathVariable UUID recordId,
            @RequestBody AttendanceRecordDTO attendanceRecordDTO) {
        attendanceRecordDTO.setRecordId(recordId);
        AttendanceRecordDTO updated = attendanceService.updateAttendanceRecord(attendanceRecordDTO);
        return ResponseEntity.ok(updated);
    }

    @DeleteMapping("/record/{recordId}")
    public ResponseEntity<Void> deleteAttendanceRecord(@PathVariable UUID recordId) {
        attendanceService.deleteAttendanceRecord(recordId);
        return ResponseEntity.noContent().build();
    }
}