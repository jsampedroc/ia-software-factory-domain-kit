package com.application.school.infrastructure.persistence.jpa.student;

import com.application.school.domain.student.model.Guardian;
import com.application.school.domain.student.model.StudentId;
import com.application.school.domain.student.model.StudentStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import jakarta.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Entity
@Table(name = "students")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class StudentJpaEntity {

    @Id
    @Column(name = "id", nullable = false, updatable = false)
    private UUID id;

    @Column(name = "legal_id", nullable = false, unique = true)
    private String legalId;

    @Column(name = "first_name", nullable = false)
    private String firstName;

    @Column(name = "last_name", nullable = false)
    private String lastName;

    @Column(name = "date_of_birth", nullable = false)
    private LocalDate dateOfBirth;

    @Column(name = "enrollment_date", nullable = false)
    private LocalDate enrollmentDate;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private StudentStatus status;

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @ElementCollection
    @CollectionTable(name = "student_guardians", joinColumns = @JoinColumn(name = "student_id"))
    @Builder.Default
    private List<GuardianEmbeddable> guardians = new ArrayList<>();

    @PrePersist
    protected void onCreate() {
        if (id == null) {
            id = UUID.randomUUID();
        }
        createdAt = LocalDateTime.now();
    }

    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }

    @Embeddable
    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class GuardianEmbeddable {
        @Column(name = "guardian_id", nullable = false)
        private UUID guardianId;

        @Column(name = "first_name", nullable = false)
        private String firstName;

        @Column(name = "last_name", nullable = false)
        private String lastName;

        @Column(name = "email", nullable = false)
        private String email;

        @Column(name = "phone_number")
        private String phoneNumber;

        @Column(name = "relationship", nullable = false)
        private String relationship;
    }

    public static StudentJpaEntity fromDomain(com.application.school.domain.student.model.Student student) {
        List<GuardianEmbeddable> guardianEmbeddables = student.getGuardians().stream()
                .map(guardian -> GuardianEmbeddable.builder()
                        .guardianId(guardian.getGuardianId().getValue())
                        .firstName(guardian.getFirstName())
                        .lastName(guardian.getLastName())
                        .email(guardian.getEmail())
                        .phoneNumber(guardian.getPhoneNumber())
                        .relationship(guardian.getRelationship())
                        .build())
                .collect(Collectors.toList());

        return StudentJpaEntity.builder()
                .id(student.getStudentId().getValue())
                .legalId(student.getLegalId())
                .firstName(student.getFirstName())
                .lastName(student.getLastName())
                .dateOfBirth(student.getDateOfBirth())
                .enrollmentDate(student.getEnrollmentDate())
                .status(student.getStatus())
                .guardians(guardianEmbeddables)
                .createdAt(student.getCreatedAt())
                .updatedAt(student.getUpdatedAt())
                .build();
    }

    public com.application.school.domain.student.model.Student toDomain() {
        List<Guardian> domainGuardians = guardians.stream()
                .map(g -> Guardian.builder()
                        .guardianId(com.application.school.domain.student.model.GuardianId.of(g.getGuardianId()))
                        .firstName(g.getFirstName())
                        .lastName(g.getLastName())
                        .email(g.getEmail())
                        .phoneNumber(g.getPhoneNumber())
                        .relationship(g.getRelationship())
                        .build())
                .collect(Collectors.toList());

        return com.application.school.domain.student.model.Student.builder()
                .studentId(StudentId.of(id))
                .legalId(legalId)
                .firstName(firstName)
                .lastName(lastName)
                .dateOfBirth(dateOfBirth)
                .enrollmentDate(enrollmentDate)
                .status(status)
                .guardians(domainGuardians)
                .createdAt(createdAt)
                .updatedAt(updatedAt)
                .build();
    }
}