package com.application.domain.model.attendance;

import com.application.domain.model.studentmanagement.Student;
import com.application.domain.enums.AttendanceStatus;
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
public class AttendanceRecord {
    private AttendanceRecordId recordId;
    private LocalDate date;
    private Student student;
    private AttendanceStatus status;
    private String notes;
    private String recordedBy;
    private LocalDateTime recordedAt;

    public boolean isModifiable() {
        LocalDateTime modificationDeadline = recordedAt.plusHours(24);
        return LocalDateTime.now().isBefore(modificationDeadline);
    }

    public void updateStatus(AttendanceStatus newStatus, String authorizationCode, String updatedBy) {
        if (isModifiable() || "ADMIN_OVERRIDE".equals(authorizationCode)) {
            this.status = newStatus;
            this.recordedBy = updatedBy;
            this.recordedAt = LocalDateTime.now();
        } else {
            throw new IllegalStateException("Attendance record cannot be modified after 24 hours without special authorization.");
        }
    }

    public void addNote(String note, String updatedBy) {
        this.notes = (this.notes == null ? "" : this.notes + "; ") + note;
        this.recordedBy = updatedBy;
        this.recordedAt = LocalDateTime.now();
    }
}