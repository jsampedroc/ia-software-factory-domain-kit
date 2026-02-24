package com.application.domain.valueobject;

import com.application.domain.shared.ValueObject;
import java.util.UUID;

public record ConsultingRoomId(UUID value) implements ValueObject {
    public ConsultingRoomId {
        if (value == null) {
            throw new IllegalArgumentException("ConsultingRoomId value cannot be null");
        }
    }
    
    public static ConsultingRoomId generate() {
        return new ConsultingRoomId(UUID.randomUUID());
    }
    
    public static ConsultingRoomId fromString(String uuid) {
        return new ConsultingRoomId(UUID.fromString(uuid));
    }
}