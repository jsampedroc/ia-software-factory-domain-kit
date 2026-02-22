package com.application.school.application.student.dto;

import com.application.school.domain.shared.enumeration.StudentStatus;
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
public class StudentResponse {
    private String studentId;
    private String legalId;
    private String firstName;
    private String lastName;
    private LocalDate dateOfBirth;
    private LocalDate enrollmentDate;
    private StudentStatus status;
    private List<GuardianResponse> guardians;
}