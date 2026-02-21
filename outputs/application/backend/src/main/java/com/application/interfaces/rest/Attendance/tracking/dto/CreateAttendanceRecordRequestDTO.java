package com.application.interfaces.rest.Attendance.tracking.dto;

import lombok.Data;
import lombok.Builder;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CreateAttendanceRecordRequestDTO {
    private String dailyAttendanceId;
    private String studentId;
    private String status;
    private String notes;
    private LocalDateTime recordedAt;
}