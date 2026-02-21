package com.application.interfaces.rest.SchoolManagement.classroom.dto;

import lombok.Data;
import lombok.Builder;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CreateClassroomRequestDTO {
    private String gradeLevel;
    private String section;
    private String academicYear;
    private Integer capacity;
    private String tutorTeacherId;
    private String schoolId;
}