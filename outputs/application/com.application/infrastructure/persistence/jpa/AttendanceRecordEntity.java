package com.application.infrastructure.persistence.jpa;

import com.application.domain.model.attendance.AttendanceRecord;
import com.application.domain.model.attendance.AttendanceRecordId;
import com.application.domain.model.studentmanagement.StudentId;
import com.application.domain.enums.AttendanceStatus;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
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
    @Column(name = "record_id")
    private UUID recordId;

    @Column(name = "date", nullable = false)
    private LocalDate date;

    @Column(name = "student_id", nullable = false)
    private UUID studentId;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private AttendanceStatus status;

    @Column(name = "notes")
    private String notes;

    @Column(name = "recorded_by", nullable = false)
    private String recordedBy;

    @Column(name = "recorded_at", nullable = false)
    private LocalDateTime recordedAt;

    public static AttendanceRecordEntity fromDomain(AttendanceRecord record) {
        return AttendanceRecordEntity.builder()
                .recordId(record.getId() != null ? record.getId().getValue() : null)
                .date(record.getDate())
                .studentId(record.getStudentId().getValue())
                .status(record.getStatus())
                .notes(record.getNotes())
                .recordedBy(record.getRecordedBy())
                .recordedAt(record.getRecordedAt())
                .build();
    }

    public AttendanceRecord toDomain() {
        return AttendanceRecord.builder()
                .id(new AttendanceRecordId(this.recordId))
                .date(this.date)
                .studentId(new StudentId(this.studentId))
                .status(this.status)
                .notes(this.notes)
                .recordedBy(this.recordedBy)
                .recordedAt(this.recordedAt)
                .build();
    }
}