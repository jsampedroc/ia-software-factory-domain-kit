package com.application.application.dto;

import com.application.domain.enums.StudentStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Past;
import javax.validation.constraints.Size;
import java.time.LocalDate;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class StudentDTO {
    @NotBlank(message = "Student ID cannot be blank")
    private String studentId;

    @NotBlank(message = "First name cannot be blank")
    @Size(max = 100, message = "First name must be at most 100 characters")
    private String firstName;

    @NotBlank(message = "Last name cannot be blank")
    @Size(max = 100, message = "Last name must be at most 100 characters")
    private String lastName;

    @NotNull(message = "Date of birth is required")
    @Past(message = "Date of birth must be in the past")
    private LocalDate dateOfBirth;

    @NotBlank(message = "National ID cannot be blank")
    @Size(max = 50, message = "National ID must be at most 50 characters")
    private String nationalId;

    @NotNull(message = "Enrollment date is required")
    private LocalDate enrollmentDate;

    @NotNull(message = "Status is required")
    private StudentStatus status;

    @Valid
    private List<LegalGuardianDTO> legalGuardians;

    @Valid
    private List<EnrollmentDTO> enrollments;
}