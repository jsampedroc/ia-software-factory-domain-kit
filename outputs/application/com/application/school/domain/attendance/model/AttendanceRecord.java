package com.application.school.domain.attendance.model;

import com.application.school.domain.shared.valueobject.DateRange;
import com.application.school.domain.shared.enumeration.AttendanceStatus;
import com.application.school.domain.student.model.StudentId;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AttendanceRecord {
    private AttendanceRecordId id;
    private StudentId studentId;
    private LocalDate date;
    private LocalTime checkInTime;
    private LocalTime checkOutTime;
    private AttendanceStatus status;

    public boolean isCheckOutBeforeCheckIn() {
        if (checkInTime == null || checkOutTime == null) {
            return false;
        }
        return checkOutTime.isBefore(checkInTime);
    }

    public boolean isSameDay(LocalDate otherDate) {
        return date != null && date.equals(otherDate);
    }

    public boolean isWithinDateRange(DateRange range) {
        return range != null && range.contains(date);
    }

    public void markAsPresent() {
        this.status = AttendanceStatus.PRESENT;
    }

    public void markAsAbsent() {
        this.status = AttendanceStatus.ABSENT;
    }

    public void markAsLate() {
        this.status = AttendanceStatus.LATE;
    }

    public void markAsExcused() {
        this.status = AttendanceStatus.EXCUSED;
    }

    public void registerCheckIn(LocalTime time) {
        this.checkInTime = time;
        if (this.status == null) {
            this.status = AttendanceStatus.PRESENT;
        }
    }

    public void registerCheckOut(LocalTime time) {
        if (this.checkInTime != null && time.isBefore(this.checkInTime)) {
            throw new IllegalArgumentException("Check-out time cannot be before check-in time.");
        }
        this.checkOutTime = time;
    }

    public boolean isComplete() {
        return checkInTime != null && checkOutTime != null;
    }
}