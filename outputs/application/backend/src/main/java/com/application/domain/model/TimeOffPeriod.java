package com.application.domain.model;

import com.application.domain.shared.ValueObject;

import java.time.LocalDate;
import java.util.Objects;

public record TimeOffPeriod(
        LocalDate startDate,
        LocalDate endDate,
        String reason
) implements ValueObject {

    public TimeOffPeriod {
        Objects.requireNonNull(startDate, "Start date cannot be null");
        Objects.requireNonNull(endDate, "End date cannot be null");
        Objects.requireNonNull(reason, "Reason cannot be null");
        if (reason.isBlank()) {
            throw new IllegalArgumentException("Reason cannot be blank");
        }
        if (endDate.isBefore(startDate)) {
            throw new IllegalArgumentException("End date must be on or after start date");
        }
    }

    public boolean overlapsWith(LocalDate date) {
        return !date.isBefore(startDate) && !date.isAfter(endDate);
    }

    public boolean overlapsWith(LocalDate checkStart, LocalDate checkEnd) {
        Objects.requireNonNull(checkStart, "Check start date cannot be null");
        Objects.requireNonNull(checkEnd, "Check end date cannot be null");
        if (checkEnd.isBefore(checkStart)) {
            throw new IllegalArgumentException("Check end date must be on or after check start date");
        }
        return !checkEnd.isBefore(startDate) && !checkStart.isAfter(endDate);
    }

    public boolean isActiveOn(LocalDate date) {
        return overlapsWith(date);
    }
}