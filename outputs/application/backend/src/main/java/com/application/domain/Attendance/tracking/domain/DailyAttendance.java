package com.application.domain.Attendance.tracking.domain;

import com.application.domain.shared.Entity;
import com.application.domain.Attendance.tracking.valueobject.DailyAttendanceId;
import com.application.domain.SchoolManagement.valueobject.ClassroomId;
import com.application.domain.SchoolManagement.valueobject.SchoolId;
import com.application.domain.exception.DomainException;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

@Data
@Builder
public class DailyAttendance extends Entity<DailyAttendanceId> {
    private final DailyAttendanceId id;
    private final ClassroomId classroomId;
    private final LocalDate date;
    private final SchoolId schoolId;
    @Builder.Default
    private final Set<AttendanceRecord> attendanceRecords = new HashSet<>();

    public DailyAttendance(DailyAttendanceId id, ClassroomId classroomId, LocalDate date, SchoolId schoolId, Set<AttendanceRecord> attendanceRecords) {
        this.id = id;
        this.classroomId = classroomId;
        this.date = date;
        this.schoolId = schoolId;
        if (attendanceRecords != null) {
            this.attendanceRecords.addAll(attendanceRecords);
        }
        validate();
    }

    private void validate() {
        if (id == null) {
            throw new DomainException("DailyAttendance id cannot be null");
        }
        if (classroomId == null) {
            throw new DomainException("ClassroomId cannot be null");
        }
        if (date == null) {
            throw new DomainException("Date cannot be null");
        }
        if (date.isAfter(LocalDate.now())) {
            throw new DomainException("Date cannot be in the future");
        }
        if (schoolId == null) {
            throw new DomainException("SchoolId cannot be null");
        }
    }

    public void addAttendanceRecord(AttendanceRecord record) {
        if (record == null) {
            throw new DomainException("AttendanceRecord cannot be null");
        }
        boolean alreadyExists = attendanceRecords.stream()
                .anyMatch(ar -> ar.getStudentId().equals(record.getStudentId()));
        if (alreadyExists) {
            throw new DomainException("A student can only have one attendance record per day for a specific classroom");
        }
        attendanceRecords.add(record);
    }

    public Set<AttendanceRecord> getAttendanceRecords() {
        return Collections.unmodifiableSet(attendanceRecords);
    }
}