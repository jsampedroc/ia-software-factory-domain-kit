package com.application.domain.model.billing;

import lombok.Builder;
import lombok.Value;
import java.util.UUID;

@Value
@Builder
public class FeeStructureId {
    UUID value;

    private FeeStructureId(UUID value) {
        this.value = value;
    }

    public static FeeStructureId of(UUID value) {
        return new FeeStructureId(value);
    }

    public static FeeStructureId generate() {
        return new FeeStructureId(UUID.randomUUID());
    }
}