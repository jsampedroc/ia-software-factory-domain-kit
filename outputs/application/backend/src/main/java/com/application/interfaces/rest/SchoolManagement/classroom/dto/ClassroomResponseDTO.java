package com.application.interfaces.rest.SchoolManagement.classroom.dto;

import lombok.Data;
import lombok.Builder;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ClassroomResponseDTO {
    private String id;
    private String gradeLevel;
    private String section;
    private String academicYear;
    private Integer capacity;
    private String tutorTeacherId;
    private String schoolId;
    private Boolean active;
}