package com.application.school.application.attendance.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RegisterAttendanceCommand {
    private String studentId;
    private LocalDate date;
    private LocalTime checkInTime;
    private LocalTime checkOutTime;
    private String status;
}