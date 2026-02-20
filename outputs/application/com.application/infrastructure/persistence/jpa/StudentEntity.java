package com.application.infrastructure.persistence.jpa;

import com.application.domain.model.studentmanagement.LegalGuardian;
import com.application.domain.model.studentmanagement.Student;
import com.application.domain.model.studentmanagement.StudentId;
import com.application.domain.enums.StudentStatus;
import com.application.domain.valueobject.PersonName;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Entity
@Table(name = "students")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class StudentEntity {

    @Id
    @Column(name = "student_id")
    private UUID studentId;

    @Column(name = "first_name", nullable = false)
    private String firstName;

    @Column(name = "last_name", nullable = false)
    private String lastName;

    @Column(name = "date_of_birth", nullable = false)
    private LocalDate dateOfBirth;

    @Column(name = "national_id", unique = true)
    private String nationalId;

    @Column(name = "enrollment_date", nullable = false)
    private LocalDate enrollmentDate;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private StudentStatus status;

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    @JoinColumn(name = "student_id")
    private List<LegalGuardianEntity> legalGuardians;

    public static StudentEntity fromDomain(Student student) {
        List<LegalGuardianEntity> guardianEntities = student.getLegalGuardians().stream()
                .map(LegalGuardianEntity::fromDomain)
                .collect(Collectors.toList());

        return StudentEntity.builder()
                .studentId(student.getStudentId().getValue())
                .firstName(student.getName().getFirstName())
                .lastName(student.getName().getLastName())
                .dateOfBirth(student.getDateOfBirth())
                .nationalId(student.getNationalId())
                .enrollmentDate(student.getEnrollmentDate())
                .status(student.getStatus())
                .createdAt(student.getCreatedAt())
                .updatedAt(student.getUpdatedAt())
                .legalGuardians(guardianEntities)
                .build();
    }

    public Student toDomain() {
        PersonName name = new PersonName(this.firstName, this.lastName);
        StudentId id = new StudentId(this.studentId);

        List<LegalGuardian> guardians = this.legalGuardians.stream()
                .map(LegalGuardianEntity::toDomain)
                .collect(Collectors.toList());

        return Student.builder()
                .studentId(id)
                .name(name)
                .dateOfBirth(this.dateOfBirth)
                .nationalId(this.nationalId)
                .enrollmentDate(this.enrollmentDate)
                .status(this.status)
                .createdAt(this.createdAt)
                .updatedAt(this.updatedAt)
                .legalGuardians(guardians)
                .build();
    }
}