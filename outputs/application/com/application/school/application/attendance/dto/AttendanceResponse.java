package com.application.school.application.attendance.dto;

import com.application.school.domain.shared.enumeration.AttendanceStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AttendanceResponse {
    private String recordId;
    private String studentId;
    private LocalDate date;
    private LocalDateTime checkInTime;
    private LocalDateTime checkOutTime;
    private AttendanceStatus status;
}