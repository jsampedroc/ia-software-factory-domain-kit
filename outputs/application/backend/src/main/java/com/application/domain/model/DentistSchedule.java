package com.application.domain.model;

import com.application.domain.shared.Entity;
import com.application.domain.valueobject.DentistScheduleId;
import lombok.Getter;
import lombok.ToString;
import lombok.experimental.SuperBuilder;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

@Getter
@ToString(callSuper = true)
@SuperBuilder(toBuilder = true)
public class DentistSchedule extends Entity<DentistScheduleId> {

    private final UUID dentistId;
    private final Set<WeeklyTimeSlot> workHours;
    private final Set<TimeOffPeriod> timeOff;
    private final boolean isActive;

    public DentistSchedule(DentistScheduleId scheduleId, UUID dentistId, Set<WeeklyTimeSlot> workHours, Set<TimeOffPeriod> timeOff, boolean isActive) {
        super(scheduleId);
        this.dentistId = dentistId;
        this.workHours = workHours != null ? new HashSet<>(workHours) : new HashSet<>();
        this.timeOff = timeOff != null ? new HashSet<>(timeOff) : new HashSet<>();
        this.isActive = isActive;
    }

    public boolean isAvailableAt(LocalDateTime dateTime) {
        if (!isActive) {
            return false;
        }

        DayOfWeek dayOfWeek = dateTime.getDayOfWeek();
        LocalTime time = dateTime.toLocalTime();

        boolean withinWorkHours = workHours.stream()
                .anyMatch(slot -> slot.dayOfWeek() == dayOfWeek &&
                        !time.isBefore(slot.startTime()) &&
                        time.isBefore(slot.endTime()));

        if (!withinWorkHours) {
            return false;
        }

        LocalDate date = dateTime.toLocalDate();
        return timeOff.stream()
                .noneMatch(off -> !date.isBefore(off.startDate()) && !date.isAfter(off.endDate()));
    }

    public boolean isAvailableForPeriod(LocalDateTime start, LocalDateTime end) {
        if (!isActive) {
            return false;
        }

        LocalDateTime current = start;
        while (current.isBefore(end)) {
            if (!isAvailableAt(current)) {
                return false;
            }
            current = current.plusMinutes(30);
        }
        return true;
    }

    public void addWorkHour(WeeklyTimeSlot slot) {
        this.workHours.add(slot);
    }

    public void removeWorkHour(WeeklyTimeSlot slot) {
        this.workHours.remove(slot);
    }

    public void addTimeOff(TimeOffPeriod period) {
        this.timeOff.add(period);
    }

    public void removeTimeOff(TimeOffPeriod period) {
        this.timeOff.remove(period);
    }

    public void deactivate() {
        // Since entities are immutable, this would be handled via a new instance or a builder pattern in practice.
        // This method is a domain operation placeholder.
    }

    public record WeeklyTimeSlot(DayOfWeek dayOfWeek, LocalTime startTime, LocalTime endTime) implements com.application.domain.shared.ValueObject {
        public WeeklyTimeSlot {
            if (startTime == null || endTime == null || dayOfWeek == null) {
                throw new IllegalArgumentException("WeeklyTimeSlot fields cannot be null");
            }
            if (!endTime.isAfter(startTime)) {
                throw new IllegalArgumentException("End time must be after start time");
            }
        }
    }

    public record TimeOffPeriod(LocalDate startDate, LocalDate endDate, String reason) implements com.application.domain.shared.ValueObject {
        public TimeOffPeriod {
            if (startDate == null || endDate == null || reason == null || reason.isBlank()) {
                throw new IllegalArgumentException("TimeOffPeriod fields cannot be null or blank");
            }
            if (endDate.isBefore(startDate)) {
                throw new IllegalArgumentException("End date must be on or after start date");
            }
        }
    }
}