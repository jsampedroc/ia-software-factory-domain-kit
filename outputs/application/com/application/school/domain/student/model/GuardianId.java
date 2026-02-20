package com.application.school.domain.student.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class GuardianId {
    private UUID value;

    public static GuardianId generate() {
        return GuardianId.builder()
                .value(UUID.randomUUID())
                .build();
    }

    public static GuardianId fromString(String uuid) {
        return GuardianId.builder()
                .value(UUID.fromString(uuid))
                .build();
    }
}