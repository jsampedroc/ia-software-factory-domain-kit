package com.application.school.infrastructure.persistence.academic.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "grades")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class GradeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "grade_id", nullable = false, unique = true, updatable = false)
    private String gradeId;

    @Column(nullable = false, unique = true)
    private String name;

    @Column(nullable = false)
    private Integer level;

    @OneToMany(mappedBy = "grade", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    private Set<ClassGroupEntity> classGroups = new HashSet<>();
}