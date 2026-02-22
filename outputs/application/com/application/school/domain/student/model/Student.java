package com.application.school.domain.student.model;

import com.application.school.domain.shared.valueobject.PersonalName;
import com.application.school.domain.shared.enumeration.StudentStatus;
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
    private PersonalName name;
    private LocalDate dateOfBirth;
    private LocalDate enrollmentDate;
    private StudentStatus status;
    @Builder.Default
    private List<Guardian> guardians = new ArrayList<>();

    public void addGuardian(Guardian guardian) {
        if (guardian != null) {
            this.guardians.add(guardian);
        }
    }

    public void removeGuardian(GuardianId guardianId) {
        if (guardianId != null) {
            this.guardians.removeIf(g -> g.getGuardianId().equals(guardianId));
        }
    }

    public void activate() {
        this.status = StudentStatus.ACTIVE;
    }

    public void deactivate() {
        this.status = StudentStatus.INACTIVE;
    }

    public void graduate() {
        this.status = StudentStatus.GRADUATED;
    }

    public boolean hasActiveStatus() {
        return this.status == StudentStatus.ACTIVE;
    }

    public boolean hasAtLeastOneGuardian() {
        return this.guardians != null && !this.guardians.isEmpty();
    }
}