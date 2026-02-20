package com.application.school.infrastructure.web.rest;

import com.application.school.application.attendance.AttendanceService;
import com.application.school.application.dtos.AttendanceRecordDTO;
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

    @PostMapping
    public ResponseEntity<AttendanceRecordDTO> createAttendanceRecord(@RequestBody AttendanceRecordDTO attendanceRecordDTO) {
        AttendanceRecordDTO createdRecord = attendanceService.createAttendanceRecord(attendanceRecordDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdRecord);
    }

    @GetMapping("/{recordId}")
    public ResponseEntity<AttendanceRecordDTO> getAttendanceRecordById(@PathVariable UUID recordId) {
        return attendanceService.getAttendanceRecordById(recordId)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/student/{studentId}")
    public ResponseEntity<List<AttendanceRecordDTO>> getAttendanceRecordsByStudent(@PathVariable UUID studentId) {
        List<AttendanceRecordDTO> records = attendanceService.getAttendanceRecordsByStudent(studentId);
        return ResponseEntity.ok(records);
    }

    @GetMapping("/date/{date}")
    public ResponseEntity<List<AttendanceRecordDTO>> getAttendanceRecordsByDate(
            @PathVariable @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {
        List<AttendanceRecordDTO> records = attendanceService.getAttendanceRecordsByDate(date);
        return ResponseEntity.ok(records);
    }

    @GetMapping("/student/{studentId}/date-range")
    public ResponseEntity<List<AttendanceRecordDTO>> getAttendanceRecordsByStudentAndDateRange(
            @PathVariable UUID studentId,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate) {
        List<AttendanceRecordDTO> records = attendanceService.getAttendanceRecordsByStudentAndDateRange(studentId, startDate, endDate);
        return ResponseEntity.ok(records);
    }

    @PutMapping("/{recordId}")
    public ResponseEntity<AttendanceRecordDTO> updateAttendanceRecord(
            @PathVariable UUID recordId,
            @RequestBody AttendanceRecordDTO attendanceRecordDTO) {
        attendanceRecordDTO.setRecordId(recordId);
        AttendanceRecordDTO updatedRecord = attendanceService.updateAttendanceRecord(attendanceRecordDTO);
        return ResponseEntity.ok(updatedRecord);
    }

    @DeleteMapping("/{recordId}")
    public ResponseEntity<Void> deleteAttendanceRecord(@PathVariable UUID recordId) {
        attendanceService.deleteAttendanceRecord(recordId);
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/{recordId}/check-out")
    public ResponseEntity<AttendanceRecordDTO> registerCheckOut(
            @PathVariable UUID recordId,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.TIME) String checkOutTime) {
        AttendanceRecordDTO updatedRecord = attendanceService.registerCheckOut(recordId, checkOutTime);
        return ResponseEntity.ok(updatedRecord);
    }
}