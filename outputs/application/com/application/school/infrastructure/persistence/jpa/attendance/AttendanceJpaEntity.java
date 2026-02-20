package com.application.school.infrastructure.persistence.jpa.attendance;

import com.application.school.domain.attendance.model.AttendanceRecordId;
import com.application.school.domain.attendance.model.AttendanceStatus;
import com.application.school.domain.student.model.StudentId;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import jakarta.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "attendance_records")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AttendanceJpaEntity {

    @Id
    @Column(name = "record_id")
    private UUID recordId;

    @Column(name = "student_id", nullable = false)
    private UUID studentId;

    @Column(name = "attendance_date", nullable = false)
    private LocalDate date;

    @Column(name = "check_in_time")
    private LocalDateTime checkInTime;

    @Column(name = "check_out_time")
    private LocalDateTime checkOutTime;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private AttendanceStatus status;

    public static AttendanceJpaEntity fromDomain(com.application.school.domain.attendance.model.AttendanceRecord record) {
        return AttendanceJpaEntity.builder()
                .recordId(record.getRecordId().getValue())
                .studentId(record.getStudentId().getValue())
                .date(record.getDate())
                .checkInTime(record.getCheckInTime())
                .checkOutTime(record.getCheckOutTime())
                .status(record.getStatus())
                .build();
    }

    public com.application.school.domain.attendance.model.AttendanceRecord toDomain() {
        return com.application.school.domain.attendance.model.AttendanceRecord.builder()
                .recordId(new AttendanceRecordId(recordId))
                .studentId(new StudentId(studentId))
                .date(date)
                .checkInTime(checkInTime)
                .checkOutTime(checkOutTime)
                .status(status)
                .build();
    }
}