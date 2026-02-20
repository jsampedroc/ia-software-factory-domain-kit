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
public class SectionId {
    private UUID value;

    public static SectionId generate() {
        return SectionId.builder()
                .value(UUID.randomUUID())
                .build();
    }

    public static SectionId fromString(String uuid) {
        return SectionId.builder()
                .value(UUID.fromString(uuid))
                .build();
    }
}