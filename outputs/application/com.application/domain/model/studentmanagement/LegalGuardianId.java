package com.application.domain.model.studentmanagement;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class LegalGuardianId {
    private UUID value;

    public static LegalGuardianId generate() {
        return new LegalGuardianId(UUID.randomUUID());
    }

    public static LegalGuardianId fromString(String uuid) {
        return new LegalGuardianId(UUID.fromString(uuid));
    }
}