package com.application.domain.SchoolManagement.student.domain;

import com.application.domain.shared.Entity;
import com.application.domain.SchoolManagement.valueobject.StudentId;
import com.application.domain.SchoolManagement.valueobject.LegalGuardianId;
import com.application.domain.SchoolManagement.valueobject.ClassroomId;
import com.application.domain.shared.valueobject.PersonName;
import com.application.domain.exception.DomainException;
import lombok.Builder;
import lombok.Data;
import java.time.LocalDate;
import java.util.Objects;

@Data
public class Student extends Entity<StudentId> {
    private LegalGuardianId legalGuardianId;
    private PersonName name;
    private LocalDate dateOfBirth;
    private String identificationNumber;
    private LocalDate enrollmentDate;
    private boolean active;
    private ClassroomId currentClassroomId;

    @Builder
    public Student(StudentId id,
                   LegalGuardianId legalGuardianId,
                   PersonName name,
                   LocalDate dateOfBirth,
                   String identificationNumber,
                   LocalDate enrollmentDate,
                   boolean active,
                   ClassroomId currentClassroomId) {
        super(id);
        this.legalGuardianId = Objects.requireNonNull(legalGuardianId, "LegalGuardianId cannot be null");
        this.name = Objects.requireNonNull(name, "Name cannot be null");
        this.dateOfBirth = Objects.requireNonNull(dateOfBirth, "Date of birth cannot be null");
        this.identificationNumber = Objects.requireNonNull(identificationNumber, "Identification number cannot be null");
        this.enrollmentDate = Objects.requireNonNull(enrollmentDate, "Enrollment date cannot be null");
        this.active = active;
        this.currentClassroomId = currentClassroomId;

        validateBusinessRules();
    }

    private void validateBusinessRules() {
        if (identificationNumber.trim().isEmpty()) {
            throw new DomainException("Student identification number cannot be empty.");
        }
        if (dateOfBirth.isAfter(enrollmentDate) || dateOfBirth.isEqual(enrollmentDate)) {
            throw new DomainException("Student date of birth must be before the enrollment date.");
        }
    }

    public void enrollInClassroom(ClassroomId newClassroomId) {
        this.currentClassroomId = Objects.requireNonNull(newClassroomId, "ClassroomId cannot be null");
    }

    public void deactivate() {
        this.active = false;
        this.currentClassroomId = null;
    }

    public void updatePersonalInfo(PersonName newName, LocalDate newDateOfBirth, String newIdentificationNumber) {
        this.name = Objects.requireNonNull(newName, "Name cannot be null");
        this.dateOfBirth = Objects.requireNonNull(newDateOfBirth, "Date of birth cannot be null");
        this.identificationNumber = Objects.requireNonNull(newIdentificationNumber, "Identification number cannot be null");
        validateBusinessRules();
    }
}