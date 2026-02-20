package com.application.school.application.dtos;

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
public class StudentDTO {
    private String studentId;
    private String legalId;
    private String firstName;
    private String lastName;
    private LocalDate dateOfBirth;
    private LocalDate enrollmentDate;
    private String status;
    private List<GuardianDTO> guardians;
}