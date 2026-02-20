package com.application.school.domain.student.model;

import com.application.school.domain.shared.PersonName;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Student {
    private StudentId studentId;
    private String legalId;
    private PersonName name;
    private LocalDate dateOfBirth;
    private LocalDate enrollmentDate;
    private StudentStatus status;
    @Builder.Default
    private List<Guardian> guardians = new ArrayList<>();

    public void addGuardian(Guardian guardian) {
        if (guardian == null) {
            throw new IllegalArgumentException("Guardian cannot be null");
        }
        this.guardians.add(guardian);
    }

    public void removeGuardian(GuardianId guardianId) {
        if (guardianId == null) {
            throw new IllegalArgumentException("GuardianId cannot be null");
        }
        this.guardians.removeIf(g -> g.getGuardianId().equals(guardianId));
    }

    public boolean isActive() {
        return this.status == StudentStatus.ACTIVE;
    }

    public void deactivate() {
        this.status = StudentStatus.INACTIVE;
    }

    public void graduate() {
        this.status = StudentStatus.GRADUATED;
    }

    public void validateLegalIdUniqueness(StudentRepository repository) {
        if (repository.existsByLegalIdAndNotId(this.legalId, this.studentId)) {
            throw new IllegalStateException("A student with the same legalId already exists.");
        }
    }
}