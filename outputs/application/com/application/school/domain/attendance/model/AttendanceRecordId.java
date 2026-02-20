package com.application.school.domain.attendance.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AttendanceRecordId {
    private UUID value;

    public static AttendanceRecordId generate() {
        return AttendanceRecordId.builder()
                .value(UUID.randomUUID())
                .build();
    }

    public static AttendanceRecordId fromString(String uuid) {
        return AttendanceRecordId.builder()
                .value(UUID.fromString(uuid))
                .build();
    }
}