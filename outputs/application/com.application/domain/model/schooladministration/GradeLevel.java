package com.application.domain.model.schooladministration;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class GradeLevel {
    private GradeLevelId gradeId;
    private String internalCode;
    private String name;
    private String description;
    private Integer order;
}