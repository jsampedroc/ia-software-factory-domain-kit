package com.application.school.infrastructure.api.attendance;

import com.application.school.application.attendance.AttendanceService;
import com.application.school.application.attendance.dto.AttendanceResponse;
import com.application.school.application.attendance.dto.RegisterAttendanceCommand;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/attendance")
@RequiredArgsConstructor
public class AttendanceController {

    private final AttendanceService attendanceService;

    @PostMapping
    public ResponseEntity<AttendanceResponse> registerAttendance(@RequestBody RegisterAttendanceCommand command) {
        AttendanceResponse response = attendanceService.registerAttendance(command);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping("/student/{studentId}")
    public ResponseEntity<List<AttendanceResponse>> getAttendanceByStudent(@PathVariable String studentId) {
        List<AttendanceResponse> response = attendanceService.getAttendanceByStudent(studentId);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/date/{date}")
    public ResponseEntity<List<AttendanceResponse>> getAttendanceByDate(@PathVariable String date) {
        List<AttendanceResponse> response = attendanceService.getAttendanceByDate(date);
        return ResponseEntity.ok(response);
    }

    @PutMapping("/{recordId}/check-out")
    public ResponseEntity<AttendanceResponse> registerCheckOut(@PathVariable String recordId) {
        AttendanceResponse response = attendanceService.registerCheckOut(recordId);
        return ResponseEntity.ok(response);
    }
}