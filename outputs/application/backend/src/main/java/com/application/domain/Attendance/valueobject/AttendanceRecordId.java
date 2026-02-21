package com.application.domain.Attendance.valueobject;

import com.application.domain.Shared.base.ValueObject;
import java.util.UUID;

public record AttendanceRecordId(UUID value) implements ValueObject {
    public AttendanceRecordId {
        if (value == null) {
            throw new IllegalArgumentException("AttendanceRecordId value cannot be null");
        }
    }

    public static AttendanceRecordId generate() {
        return new AttendanceRecordId(UUID.randomUUID());
    }

    public static AttendanceRecordId fromString(String uuid) {
        return new AttendanceRecordId(UUID.fromString(uuid));
    }
}