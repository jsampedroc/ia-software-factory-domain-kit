package com.application.school.infrastructure.persistence.academic.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import jakarta.persistence.*;
import java.time.Year;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

@Entity
@Table(name = "class_groups")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ClassGroupEntity {
    @Id
    @Column(name = "class_group_id")
    private UUID classGroupId;

    @Column(name = "academic_year", nullable = false)
    private Year academicYear;

    @Column(name = "section", nullable = false, length = 10)
    private String section;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "grade_id", nullable = false)
    private GradeEntity grade;

    @ManyToMany
    @JoinTable(
        name = "class_group_enrollments",
        joinColumns = @JoinColumn(name = "class_group_id"),
        inverseJoinColumns = @JoinColumn(name = "student_id")
    )
    @Builder.Default
    private Set<StudentEntity> enrolledStudents = new HashSet<>();
}