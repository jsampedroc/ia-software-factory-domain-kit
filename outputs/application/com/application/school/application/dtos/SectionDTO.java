package com.application.school.application.dtos;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SectionDTO {
    private String sectionId;
    private String name;
    private Integer capacity;
    private String gradeId;
    private String gradeName;
}