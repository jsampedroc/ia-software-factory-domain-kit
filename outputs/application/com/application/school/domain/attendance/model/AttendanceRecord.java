package com.application.school.domain.attendance.model;

import com.application.school.domain.shared.DateRange;
import com.application.school.domain.student.model.StudentId;
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

    public boolean isForSameDay(AttendanceRecord other) {
        if (other == null || other.getDate() == null || this.date == null) {
            return false;
        }
        return this.date.equals(other.getDate());
    }

    public boolean isDateInRange(DateRange range) {
        if (range == null) {
            return false;
        }
        return !this.date.isBefore(range.getStartDate()) && !this.date.isAfter(range.getEndDate());
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
        if (time == null) {
            throw new IllegalArgumentException("Check-in time cannot be null");
        }
        this.checkInTime = time;
        if (this.status == null) {
            this.status = AttendanceStatus.PRESENT;
        }
    }

    public void registerCheckOut(LocalTime time) {
        if (time == null) {
            throw new IllegalArgumentException("Check-out time cannot be null");
        }
        if (this.checkInTime != null && time.isBefore(this.checkInTime)) {
            throw new IllegalStateException("Check-out time cannot be before check-in time");
        }
        this.checkOutTime = time;
    }
}