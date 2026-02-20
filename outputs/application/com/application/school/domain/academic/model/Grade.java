package com.application.school.domain.academic.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Grade {
    private GradeId gradeId;
    private String name;
    private Integer level;

    public static Grade create(String name, Integer level) {
        return Grade.builder()
                .gradeId(new GradeId(UUID.randomUUID()))
                .name(name)
                .level(level)
                .build();
    }

    public void update(String name, Integer level) {
        this.name = name;
        this.level = level;
    }
}