package com.application.school.infrastructure.persistence.student.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import jakarta.persistence.*;
import java.util.UUID;

@Entity
@Table(name = "guardians")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class GuardianEntity {
    @Id
    @Column(name = "guardian_id")
    private UUID guardianId;

    @Column(name = "first_name", nullable = false)
    private String firstName;

    @Column(name = "last_name", nullable = false)
    private String lastName;

    @Column(name = "email", nullable = false)
    private String email;

    @Column(name = "phone_number")
    private String phoneNumber;

    @Column(name = "relationship")
    private String relationship;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "student_id", nullable = false)
    private StudentEntity student;
}