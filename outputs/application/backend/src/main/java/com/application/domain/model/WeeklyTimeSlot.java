package com.application.domain.model;

import com.application.domain.shared.ValueObject;

import java.time.DayOfWeek;
import java.time.LocalTime;
import java.util.Objects;

public record WeeklyTimeSlot(
        DayOfWeek dayOfWeek,
        LocalTime startTime,
        LocalTime endTime
) implements ValueObject {

    public WeeklyTimeSlot {
        Objects.requireNonNull(dayOfWeek, "Day of week cannot be null");
        Objects.requireNonNull(startTime, "Start time cannot be null");
        Objects.requireNonNull(endTime, "End time cannot be null");

        if (!endTime.isAfter(startTime)) {
            throw new IllegalArgumentException("End time must be after start time");
        }
    }

    public boolean overlaps(WeeklyTimeSlot other) {
        if (this.dayOfWeek != other.dayOfWeek) {
            return false;
        }
        return this.startTime.isBefore(other.endTime) && other.startTime.isBefore(this.endTime);
    }

    public boolean contains(LocalTime time) {
        return !time.isBefore(startTime) && time.isBefore(endTime);
    }

    public long durationInMinutes() {
        return java.time.Duration.between(startTime, endTime).toMinutes();
    }
}