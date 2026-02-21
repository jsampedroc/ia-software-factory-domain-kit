package com.application.domain.Attendance.valueobject;

import com.application.domain.Shared.ValueObject;

public record DailyAttendanceId(String value) implements ValueObject {
    public DailyAttendanceId {
        if (value == null || value.isBlank()) {
            throw new IllegalArgumentException("DailyAttendanceId cannot be null or blank");
        }
    }
}