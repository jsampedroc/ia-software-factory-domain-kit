package com.application.interfaces.rest.Attendance.tracking.dto;

import com.application.domain.Attendance.tracking.domain.AttendanceRecord;
import com.application.domain.Attendance.tracking.valueobject.AttendanceRecordId;
import com.application.domain.SchoolManagement.valueobject.StudentId;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AttendanceRecordResponseDTO {
    private AttendanceRecordId id;
    private StudentId studentId;
    private String status;
    private String notes;
    private LocalDateTime recordedAt;

    public static AttendanceRecordResponseDTO fromDomain(AttendanceRecord attendanceRecord) {
        return AttendanceRecordResponseDTO.builder()
                .id(attendanceRecord.getId())
                .studentId(attendanceRecord.getStudentId())
                .status(attendanceRecord.getStatus().name())
                .notes(attendanceRecord.getNotes())
                .recordedAt(attendanceRecord.getRecordedAt())
                .build();
    }
}