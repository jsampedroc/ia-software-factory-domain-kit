package com.application.domain.Attendance.tracking.domain;

import com.application.domain.shared.Entity;
import com.application.domain.Attendance.tracking.valueobject.AttendanceRecordId;
import com.application.domain.Attendance.tracking.valueobject.DailyAttendanceId;
import com.application.domain.SchoolManagement.valueobject.StudentId;
import com.application.domain.exception.DomainException;
import lombok.Builder;
import lombok.Data;
import java.time.LocalDateTime;

@Data
public class AttendanceRecord extends Entity<AttendanceRecordId> {
    private DailyAttendanceId dailyAttendanceId;
    private StudentId studentId;
    private AttendanceStatus status;
    private String notes;
    private LocalDateTime recordedAt;

    @Builder
    public AttendanceRecord(AttendanceRecordId id, DailyAttendanceId dailyAttendanceId, StudentId studentId, AttendanceStatus status, String notes, LocalDateTime recordedAt) {
        super(id);
        this.dailyAttendanceId = dailyAttendanceId;
        this.studentId = studentId;
        this.status = status;
        this.notes = notes;
        this.recordedAt = recordedAt;
        validate();
    }

    private void validate() {
        if (dailyAttendanceId == null) {
            throw new DomainException("DailyAttendanceId is required for AttendanceRecord.");
        }
        if (studentId == null) {
            throw new DomainException("StudentId is required for AttendanceRecord.");
        }
        if (status == null) {
            throw new DomainException("Attendance status is required.");
        }
        if (recordedAt == null) {
            throw new DomainException("RecordedAt timestamp is required.");
        }
        if (recordedAt.isAfter(LocalDateTime.now())) {
            throw new DomainException("RecordedAt cannot be in the future.");
        }
    }

    public enum AttendanceStatus {
        PRESENT, ABSENT, LATE, EXCUSED
    }
}