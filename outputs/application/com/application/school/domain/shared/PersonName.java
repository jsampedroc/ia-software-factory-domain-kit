package com.application.school.domain.shared;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public final class PersonName {
    private String firstName;
    private String lastName;

    public String getFullName() {
        return String.format("%s %s", firstName, lastName).trim();
    }

    public boolean isValid() {
        return firstName != null && !firstName.trim().isEmpty() &&
               lastName != null && !lastName.trim().isEmpty();
    }
}