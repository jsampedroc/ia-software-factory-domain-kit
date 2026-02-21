package com.application.infrastructure.persistence.Attendance.tracking.entity;

import com.application.domain.Attendance.tracking.domain.AttendanceRecord;
import com.application.domain.Attendance.tracking.valueobject.AttendanceRecordId;
import com.application.domain.Attendance.tracking.valueobject.DailyAttendanceId;
import com.application.domain.SchoolManagement.valueobject.StudentId;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "attendance_records")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AttendanceRecordEntity {
    @Id
    private UUID id;

    @Column(name = "daily_attendance_id", nullable = false)
    private UUID dailyAttendanceId;

    @Column(name = "student_id", nullable = false)
    private UUID studentId;

    @Column(nullable = false, length = 20)
    private String status;

    @Column(length = 500)
    private String notes;

    @Column(name = "recorded_at", nullable = false)
    private LocalDateTime recordedAt;

    public static AttendanceRecordEntity fromDomain(AttendanceRecord record) {
        return AttendanceRecordEntity.builder()
                .id(record.getId().getValue())
                .dailyAttendanceId(record.getDailyAttendanceId().getValue())
                .studentId(record.getStudentId().getValue())
                .status(record.getStatus())
                .notes(record.getNotes())
                .recordedAt(record.getRecordedAt())
                .build();
    }

    public AttendanceRecord toDomain() {
        return AttendanceRecord.builder()
                .id(new AttendanceRecordId(this.id))
                .dailyAttendanceId(new DailyAttendanceId(this.dailyAttendanceId))
                .studentId(new StudentId(this.studentId))
                .status(this.status)
                .notes(this.notes)
                .recordedAt(this.recordedAt)
                .build();
    }
}