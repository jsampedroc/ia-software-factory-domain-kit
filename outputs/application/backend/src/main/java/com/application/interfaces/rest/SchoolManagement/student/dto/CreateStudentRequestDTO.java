package com.application.interfaces.rest.SchoolManagement.student.dto;

import lombok.Data;
import lombok.Builder;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.*;
import java.time.LocalDate;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CreateStudentRequestDTO {
    @NotBlank(message = "First name is mandatory")
    @Size(max = 100, message = "First name cannot exceed 100 characters")
    private String firstName;

    @NotBlank(message = "Last name is mandatory")
    @Size(max = 100, message = "Last name cannot exceed 100 characters")
    private String lastName;

    @NotNull(message = "Date of birth is mandatory")
    @Past(message = "Date of birth must be in the past")
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate dateOfBirth;

    @NotBlank(message = "Identification number is mandatory")
    @Size(max = 50, message = "Identification number cannot exceed 50 characters")
    private String identificationNumber;

    @NotNull(message = "Enrollment date is mandatory")
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate enrollmentDate;

    @NotNull(message = "Legal guardian ID is mandatory")
    private String legalGuardianId;

    private String currentClassroomId;
}