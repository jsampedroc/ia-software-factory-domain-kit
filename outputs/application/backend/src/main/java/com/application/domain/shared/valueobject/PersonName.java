package com.application.domain.shared.valueobject;

import com.application.domain.shared.ValueObject;

public record PersonName(String firstName, String lastName) implements ValueObject {
    public PersonName {
        if (firstName == null || firstName.trim().isEmpty()) {
            throw new IllegalArgumentException("First name cannot be empty.");
        }
        if (lastName == null || lastName.trim().isEmpty()) {
            throw new IllegalArgumentException("Last name cannot be empty.");
        }
        // Assuming a max length rule exists, e.g., 100 characters.
        int maxLength = 100;
        if (firstName.length() > maxLength) {
            throw new IllegalArgumentException("First name exceeds maximum length.");
        }
        if (lastName.length() > maxLength) {
            throw new IllegalArgumentException("Last name exceeds maximum length.");
        }
    }
}