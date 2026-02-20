package com.application.school.domain.student.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Guardian {
    private GuardianId guardianId;
    private String firstName;
    private String lastName;
    private String email;
    private String phoneNumber;
    private String relationship;

    public static Guardian create(String firstName, String lastName, String email, String phoneNumber, String relationship) {
        return Guardian.builder()
                .guardianId(new GuardianId(UUID.randomUUID()))
                .firstName(firstName)
                .lastName(lastName)
                .email(email)
                .phoneNumber(phoneNumber)
                .relationship(relationship)
                .build();
    }
}