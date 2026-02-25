package com.application.domain.model;

import com.application.domain.shared.ValueObject;
import com.application.domain.valueobject.MedicalAlertId;
import com.application.domain.enums.AlertSeverity;

import java.time.LocalDateTime;
import java.util.Objects;

public record MedicalAlert(
        MedicalAlertId alertId,
        String code,
        String description,
        AlertSeverity severity,
        LocalDateTime createdAt,
        boolean isActive
) implements ValueObject {

    public MedicalAlert {
        Objects.requireNonNull(alertId, "Alert ID cannot be null");
        Objects.requireNonNull(code, "Code cannot be null");
        Objects.requireNonNull(description, "Description cannot be null");
        Objects.requireNonNull(severity, "Severity cannot be null");
        Objects.requireNonNull(createdAt, "Created at timestamp cannot be null");
        if (code.isBlank()) {
            throw new IllegalArgumentException("Code cannot be blank");
        }
        if (description.isBlank()) {
            throw new IllegalArgumentException("Description cannot be blank");
        }
    }

    public MedicalAlert deactivate() {
        return new MedicalAlert(alertId, code, description, severity, createdAt, false);
    }

    public MedicalAlert updateDescription(String newDescription) {
        Objects.requireNonNull(newDescription, "New description cannot be null");
        if (newDescription.isBlank()) {
            throw new IllegalArgumentException("New description cannot be blank");
        }
        return new MedicalAlert(alertId, code, newDescription, severity, LocalDateTime.now(), isActive);
    }

    public boolean isHighSeverity() {
        return severity == AlertSeverity.HIGH;
    }

    public boolean requiresAcknowledgement() {
        return isActive && isHighSeverity();
    }
}