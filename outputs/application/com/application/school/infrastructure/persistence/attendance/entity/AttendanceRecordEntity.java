package com.application.school.infrastructure.persistence.attendance.entity;

import com.application.school.domain.shared.enumeration.AttendanceStatus;
import com.application.school.infrastructure.persistence.student.entity.StudentEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
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
    @Column(name = "record_id", nullable = false, updatable = false)
    private UUID recordId;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "student_id", nullable = false)
    private StudentEntity student;

    @Column(name = "attendance_date", nullable = false)
    private LocalDate date;

    @Column(name = "check_in_time")
    private LocalDateTime checkInTime;

    @Column(name = "check_out_time")
    private LocalDateTime checkOutTime;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false, length = 20)
    private AttendanceStatus status;
}