package com.application.domain.SchoolManagement.school.domain;

import com.application.domain.shared.Entity;
import com.application.domain.SchoolManagement.valueobject.ClassroomId;
import com.application.domain.SchoolManagement.valueobject.SchoolId;
import com.application.domain.SchoolManagement.valueobject.StudentId;
import com.application.domain.exception.DomainException;
import lombok.Builder;
import lombok.Data;
import java.util.HashSet;
import java.util.Set;

@Data
public class Classroom extends Entity<ClassroomId> {
    private final String gradeLevel;
    private final String section;
    private final String academicYear;
    private final int capacity;
    private final String tutorTeacherId;
    private final SchoolId schoolId;
    private final boolean active;
    private final Set<StudentId> assignedStudentIds;

    @Builder
    public Classroom(ClassroomId id, String gradeLevel, String section, String academicYear, int capacity, String tutorTeacherId, SchoolId schoolId, boolean active, Set<StudentId> assignedStudentIds) {
        super(id);
        this.gradeLevel = gradeLevel;
        this.section = section;
        this.academicYear = academicYear;
        this.capacity = capacity;
        this.tutorTeacherId = tutorTeacherId;
        this.schoolId = schoolId;
        this.active = active;
        this.assignedStudentIds = assignedStudentIds != null ? new HashSet<>(assignedStudentIds) : new HashSet<>();

        validate();
    }

    private void validate() {
        if (gradeLevel == null || gradeLevel.isBlank()) {
            throw new DomainException("Classroom grade level cannot be null or blank.");
        }
        if (section == null || section.isBlank()) {
            throw new DomainException("Classroom section cannot be null or blank.");
        }
        if (academicYear == null || academicYear.isBlank()) {
            throw new DomainException("Classroom academic year cannot be null or blank.");
        }
        if (capacity <= 0) {
            throw new DomainException("Classroom capacity must be a positive number.");
        }
        if (schoolId == null) {
            throw new DomainException("Classroom must belong to a school.");
        }
        if (assignedStudentIds.size() > capacity) {
            throw new DomainException("Classroom cannot exceed its maximum student capacity.");
        }
    }

    public void assignStudent(StudentId studentId) {
        if (assignedStudentIds.size() >= capacity) {
            throw new DomainException("Cannot assign student. Classroom is at full capacity.");
        }
        if (!active) {
            throw new DomainException("Cannot assign student to an inactive classroom.");
        }
        assignedStudentIds.add(studentId);
    }

    public void removeStudent(StudentId studentId) {
        assignedStudentIds.remove(studentId);
    }

    public boolean isAtCapacity() {
        return assignedStudentIds.size() >= capacity;
    }
}