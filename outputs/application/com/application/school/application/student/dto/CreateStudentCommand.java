package com.application.school.application.student.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CreateStudentCommand {
    private String legalId;
    private String firstName;
    private String lastName;
    private LocalDate dateOfBirth;
    private LocalDate enrollmentDate;
    private List<GuardianInfo> guardians;

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class GuardianInfo {
        private String firstName;
        private String lastName;
        private String email;
        private String phoneNumber;
        private String relationship;
    }
}