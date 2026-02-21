package com.application.domain.shared.valueobject;

import com.application.domain.shared.ValueObject;

public record DateRange(java.time.LocalDate startDate, java.time.LocalDate endDate) implements ValueObject {
    public DateRange {
        if (startDate == null) {
            throw new IllegalArgumentException("Start date cannot be null");
        }
        if (endDate == null) {
            throw new IllegalArgumentException("End date cannot be null");
        }
        if (startDate.isAfter(endDate)) {
            throw new IllegalArgumentException("Start date must be before or equal to end date");
        }
    }
}