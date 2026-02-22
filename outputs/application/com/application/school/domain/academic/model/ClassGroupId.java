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
public class ClassGroupId {
    private UUID value;

    public static ClassGroupId generate() {
        return ClassGroupId.builder()
                .value(UUID.randomUUID())
                .build();
    }

    public static ClassGroupId fromString(String uuid) {
        return ClassGroupId.builder()
                .value(UUID.fromString(uuid))
                .build();
    }
}