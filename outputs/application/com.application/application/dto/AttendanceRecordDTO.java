package com.application.application.dto;

import com.application.domain.enums.AttendanceStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AttendanceRecordDTO {
    private String recordId;
    
    @NotNull(message = "Date cannot be null")
    private LocalDate date;
    
    @NotBlank(message = "Student ID cannot be blank")
    private String studentId;
    
    @NotBlank(message = "Student name cannot be blank")
    private String studentName;
    
    @NotNull(message = "Status cannot be null")
    private AttendanceStatus status;
    
    private String notes;
    
    @NotBlank(message = "Recorded by cannot be blank")
    private String recordedBy;
    
    @NotNull(message = "Recorded at cannot be null")
    private LocalDateTime recordedAt;
}