package com.application.school.domain.academic.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class GradeId {
    private UUID value;

    public static GradeId generate() {
        return GradeId.builder()
                .value(UUID.randomUUID())
                .build();
    }

    public static GradeId fromString(String uuid) {
        return GradeId.builder()
                .value(UUID.fromString(uuid))
                .build();
    }
}