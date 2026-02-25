package com.application.domain.model;

import com.application.domain.shared.ValueObject;

import java.time.LocalDate;
import java.util.Objects;

public record PatientIdentity(
        String firstName,
        String lastName,
        LocalDate dateOfBirth,
        String nationalId,
        String email,
        String phoneNumber,
        String address
) implements ValueObject {

    public PatientIdentity {
        Objects.requireNonNull(firstName, "First name cannot be null");
        Objects.requireNonNull(lastName, "Last name cannot be null");
        Objects.requireNonNull(dateOfBirth, "Date of birth cannot be null");
        Objects.requireNonNull(nationalId, "National ID cannot be null");
        Objects.requireNonNull(email, "Email cannot be null");
        Objects.requireNonNull(phoneNumber, "Phone number cannot be null");
        Objects.requireNonNull(address, "Address cannot be null");

        if (firstName.isBlank()) {
            throw new IllegalArgumentException("First name cannot be blank");
        }
        if (lastName.isBlank()) {
            throw new IllegalArgumentException("Last name cannot be blank");
        }
        if (nationalId.isBlank()) {
            throw new IllegalArgumentException("National ID cannot be blank");
        }
        if (email.isBlank()) {
            throw new IllegalArgumentException("Email cannot be blank");
        }
        if (!email.contains("@")) {
            throw new IllegalArgumentException("Email must be valid");
        }
        if (phoneNumber.isBlank()) {
            throw new IllegalArgumentException("Phone number cannot be blank");
        }
        if (address.isBlank()) {
            throw new IllegalArgumentException("Address cannot be blank");
        }
        if (dateOfBirth.isAfter(LocalDate.now())) {
            throw new IllegalArgumentException("Date of birth cannot be in the future");
        }
    }

    public String getFullName() {
        return firstName + " " + lastName;
    }

    public boolean isAdult() {
        return dateOfBirth.plusYears(18).isBefore(LocalDate.now());
    }
}