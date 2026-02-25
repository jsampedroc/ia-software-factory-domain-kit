package com.application.domain.valueobject;

import com.application.domain.shared.ValueObject;

public record AccessLogId(Long value) implements ValueObject {
    public AccessLogId {
        if (value == null || value <= 0) {
            throw new IllegalArgumentException("AccessLogId value must be a positive non-null Long.");
        }
    }
}