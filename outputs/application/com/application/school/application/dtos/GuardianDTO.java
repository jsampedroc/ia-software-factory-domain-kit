package com.application.school.application.dtos;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class GuardianDTO {
    private String guardianId;
    private String firstName;
    private String lastName;
    private String email;
    private String phoneNumber;
    private String relationship;
    private String studentId;
}