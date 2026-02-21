package com.application.application.SchoolManagement.student.application.create;

import com.application.domain.SchoolManagement.student.domain.StudentId;
import com.application.domain.SchoolManagement.valueobject.LegalGuardianId;
import com.application.domain.SchoolManagement.valueobject.ClassroomId;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDate;

@Getter
@Builder
public class CreateStudentCommand {
    private final StudentId studentId;
    private final LegalGuardianId legalGuardianId;
    private final String firstName;
    private final String lastName;
    private final LocalDate dateOfBirth;
    private final String identificationNumber;
    private final LocalDate enrollmentDate;
    private final ClassroomId currentClassroomId;
}