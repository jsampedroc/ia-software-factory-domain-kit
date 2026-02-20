package com.application.school.domain.academic.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Section {
    private SectionId sectionId;
    private String name;
    private Integer capacity;
}