package com.application.domain.valueobject;

import com.application.domain.shared.ValueObject;

public record ClinicalNoteId(String value) implements ValueObject {
    public ClinicalNoteId {
        if (value == null || value.isBlank()) {
            throw new IllegalArgumentException("ClinicalNoteId value cannot be null or blank");
        }
    }
}