package com.application.domain.model;

import com.application.domain.shared.ValueObject;
import com.application.domain.enums.ToothStatus;

import java.time.LocalDate;
import java.util.Objects;

public record ToothCondition(
        Integer toothNumber,
        ToothStatus condition,
        String notes,
        LocalDate lastTreated
) implements ValueObject {

    public ToothCondition {
        Objects.requireNonNull(toothNumber, "Tooth number cannot be null");
        Objects.requireNonNull(condition, "Condition cannot be null");
        Objects.requireNonNull(notes, "Notes cannot be null");
        // lastTreated can be null for teeth never treated

        validateToothNumber(toothNumber);
        validateNotes(notes);
    }

    private void validateToothNumber(Integer toothNumber) {
        if (toothNumber < 0 || toothNumber > 32) {
            throw new IllegalArgumentException("Tooth number must be between 0 (general) and 32");
        }
    }

    private void validateNotes(String notes) {
        if (notes.length() > 500) {
            throw new IllegalArgumentException("Notes cannot exceed 500 characters");
        }
    }

    public ToothCondition updateCondition(ToothStatus newCondition, String newNotes, LocalDate treatmentDate) {
        Objects.requireNonNull(newCondition, "New condition cannot be null");
        Objects.requireNonNull(newNotes, "New notes cannot be null");
        Objects.requireNonNull(treatmentDate, "Treatment date cannot be null for condition update");

        return new ToothCondition(
                this.toothNumber,
                newCondition,
                newNotes,
                treatmentDate
        );
    }

    public boolean isTreated() {
        return lastTreated != null;
    }

    public boolean requiresAttention() {
        return condition == ToothStatus.CARIES || condition == ToothStatus.MISSING;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ToothCondition that = (ToothCondition) o;
        return Objects.equals(toothNumber, that.toothNumber) &&
                condition == that.condition &&
                Objects.equals(notes, that.notes) &&
                Objects.equals(lastTreated, that.lastTreated);
    }

    @Override
    public int hashCode() {
        return Objects.hash(toothNumber, condition, notes, lastTreated);
    }
}