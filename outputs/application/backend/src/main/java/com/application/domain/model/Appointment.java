package com.application.domain.model;

import com.application.domain.shared.Entity;
import com.application.domain.valueobject.AppointmentId;
import com.application.domain.enums.AppointmentType;
import com.application.domain.enums.AppointmentStatus;
import lombok.Getter;
import lombok.ToString;
import lombok.experimental.SuperBuilder;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@ToString(callSuper = true)
@SuperBuilder(toBuilder = true)
public class Appointment extends Entity<AppointmentId> {

    private final UUID patientId;
    private final UUID dentistId;
    private final LocalDateTime scheduledTime;
    private final Duration duration;
    private final AppointmentType type;
    private AppointmentStatus status;
    private final String reason;
    private final LocalDateTime createdAt;

    public Appointment(AppointmentId id,
                       UUID patientId,
                       UUID dentistId,
                       LocalDateTime scheduledTime,
                       Duration duration,
                       AppointmentType type,
                       AppointmentStatus status,
                       String reason,
                       LocalDateTime createdAt) {
        super(id);
        this.patientId = patientId;
        this.dentistId = dentistId;
        this.scheduledTime = scheduledTime;
        this.duration = duration;
        this.type = type;
        this.status = status;
        this.reason = reason;
        this.createdAt = createdAt;
    }

    public void confirm() {
        if (this.status != AppointmentStatus.SCHEDULED) {
            throw new IllegalStateException("Only SCHEDULED appointments can be confirmed.");
        }
        this.status = AppointmentStatus.CONFIRMED;
    }

    public void start() {
        if (this.status != AppointmentStatus.CONFIRMED && this.status != AppointmentStatus.SCHEDULED) {
            throw new IllegalStateException("Appointment must be SCHEDULED or CONFIRMED to start.");
        }
        this.status = AppointmentStatus.IN_PROGRESS;
    }

    public void complete() {
        if (this.status != AppointmentStatus.IN_PROGRESS) {
            throw new IllegalStateException("Only IN_PROGRESS appointments can be completed.");
        }
        this.status = AppointmentStatus.COMPLETED;
    }

    public void cancel(LocalDateTime cancellationTime) {
        if (this.status == AppointmentStatus.COMPLETED || this.status == AppointmentStatus.CANCELLED || this.status == AppointmentStatus.NO_SHOW) {
            throw new IllegalStateException("Cannot cancel a COMPLETED, CANCELLED, or NO_SHOW appointment.");
        }
        if (this.type != AppointmentType.EMERGENCY && scheduledTime.minusHours(24).isBefore(cancellationTime)) {
            throw new IllegalStateException("Non-emergency appointments must be cancelled at least 24 hours in advance.");
        }
        this.status = AppointmentStatus.CANCELLED;
    }

    public void markAsNoShow() {
        if (this.status != AppointmentStatus.SCHEDULED && this.status != AppointmentStatus.CONFIRMED) {
            throw new IllegalStateException("Only SCHEDULED or CONFIRMED appointments can be marked as NO_SHOW.");
        }
        this.status = AppointmentStatus.NO_SHOW;
    }

    public boolean isOverlapping(Appointment other) {
        LocalDateTime thisEnd = this.scheduledTime.plus(this.duration);
        LocalDateTime otherEnd = other.scheduledTime.plus(other.duration);
        return this.scheduledTime.isBefore(otherEnd) && other.scheduledTime.isBefore(thisEnd);
    }

    public LocalDateTime getEndTime() {
        return scheduledTime.plus(duration);
    }
}