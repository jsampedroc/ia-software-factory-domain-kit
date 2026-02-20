package com.application.domain.model.studentmanagement;

import com.application.domain.enums.StudentStatus;
import com.application.domain.valueobject.PersonName;
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
    private PersonName name;
    private LocalDate dateOfBirth;
    private String nationalId;
    private LocalDate enrollmentDate;
    private StudentStatus status;
    @Builder.Default
    private List<Guardianship> guardianships = new ArrayList<>();
    @Builder.Default
    private List<Enrollment> enrollments = new ArrayList<>();

    public boolean isActive() {
        return StudentStatus.ACTIVE.equals(this.status);
    }

    public boolean canTakeAttendance() {
        return isActive() && hasActiveEnrollment();
    }

    public boolean canBeBilled() {
        return isActive() && hasPrimaryGuardian();
    }

    private boolean hasActiveEnrollment() {
        if (enrollments == null || enrollments.isEmpty()) {
            return false;
        }
        LocalDate now = LocalDate.now();
        return enrollments.stream()
                .anyMatch(enrollment -> enrollment.isActiveForDate(now));
    }

    private boolean hasPrimaryGuardian() {
        if (guardianships == null || guardianships.isEmpty()) {
            return false;
        }
        return guardianships.stream()
                .anyMatch(Guardianship::isPrimary);
    }

    public void addGuardianship(Guardianship guardianship) {
        if (this.guardianships == null) {
            this.guardianships = new ArrayList<>();
        }
        this.guardianships.add(guardianship);
    }

    public void addEnrollment(Enrollment enrollment) {
        if (this.enrollments == null) {
            this.enrollments = new ArrayList<>();
        }
        this.enrollments.add(enrollment);
    }
}