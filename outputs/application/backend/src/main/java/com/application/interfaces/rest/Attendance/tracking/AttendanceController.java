package com.application.interfaces.rest.Attendance.tracking;

import com.application.application.Attendance.tracking.application.record.CreateAttendanceRecordCommand;
import com.application.application.Attendance.tracking.application.record.CreateAttendanceRecordCommandHandler;
import com.application.interfaces.rest.Attendance.tracking.dto.CreateAttendanceRecordRequestDTO;
import com.application.interfaces.rest.Attendance.tracking.dto.AttendanceRecordResponseDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/attendance")
@RequiredArgsConstructor
public class AttendanceController {

    private final CreateAttendanceRecordCommandHandler createAttendanceRecordCommandHandler;

    @PostMapping("/record")
    public ResponseEntity<AttendanceRecordResponseDTO> createAttendanceRecord(@RequestBody CreateAttendanceRecordRequestDTO request) {
        CreateAttendanceRecordCommand command = CreateAttendanceRecordCommand.builder()
                .dailyAttendanceId(request.getDailyAttendanceId())
                .studentId(request.getStudentId())
                .status(request.getStatus())
                .notes(request.getNotes())
                .build();

        AttendanceRecordResponseDTO response = createAttendanceRecordCommandHandler.handle(command);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }
}