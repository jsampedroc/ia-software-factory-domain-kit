package com.application.school.infrastructure.persistence.jpa.academic;

import com.application.school.domain.academic.model.Grade;
import com.application.school.domain.academic.model.GradeId;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Entity
@Table(name = "grades")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class GradeJpaEntity {
    @Id
    @Column(name = "id")
    private UUID id;

    @Column(name = "name", nullable = false, unique = true)
    private String name;

    @Column(name = "level", nullable = false)
    private Integer level;

    public static GradeJpaEntity fromDomain(Grade grade) {
        return GradeJpaEntity.builder()
                .id(grade.getId().getValue())
                .name(grade.getName())
                .level(grade.getLevel())
                .build();
    }

    public Grade toDomain() {
        return Grade.builder()
                .id(new GradeId(this.id))
                .name(this.name)
                .level(this.level)
                .build();
    }
}