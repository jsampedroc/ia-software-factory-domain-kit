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
public class StudentId {
    private UUID value;

    public static StudentId generate() {
        return StudentId.builder()
                .value(UUID.randomUUID())
                .build();
    }

    public static StudentId fromString(String uuid) {
        return StudentId.builder()
                .value(UUID.fromString(uuid))
                .build();
    }
}