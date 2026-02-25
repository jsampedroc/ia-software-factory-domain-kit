package com.application.domain.model;

import com.application.domain.shared.ValueObject;
import com.application.domain.valueobject.AllergyId;
import com.application.domain.enums.AllergySeverity;

import java.time.LocalDate;
import java.util.Objects;

public record Allergy(
        AllergyId allergyId,
        String substance,
        String reaction,
        AllergySeverity severity,
        LocalDate diagnosedDate
) implements ValueObject {

    public Allergy {
        Objects.requireNonNull(allergyId, "Allergy ID cannot be null");
        Objects.requireNonNull(substance, "Substance cannot be null");
        Objects.requireNonNull(reaction, "Reaction cannot be null");
        Objects.requireNonNull(severity, "Severity cannot be null");
        Objects.requireNonNull(diagnosedDate, "Diagnosed date cannot be null");

        if (substance.isBlank()) {
            throw new IllegalArgumentException("Substance cannot be blank");
        }
        if (reaction.isBlank()) {
            throw new IllegalArgumentException("Reaction cannot be blank");
        }
        if (diagnosedDate.isAfter(LocalDate.now())) {
            throw new IllegalArgumentException("Diagnosed date cannot be in the future");
        }
    }

    public boolean isSevere() {
        return severity == AllergySeverity.SEVERE;
    }

    public boolean isModerate() {
        return severity == AllergySeverity.MODERATE;
    }

    public boolean isMild() {
        return severity == AllergySeverity.MILD;
    }
}