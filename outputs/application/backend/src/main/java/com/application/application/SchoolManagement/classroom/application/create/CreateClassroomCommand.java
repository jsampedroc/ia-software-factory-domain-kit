package com.application.application.SchoolManagement.classroom.application.create;

import com.application.domain.SchoolManagement.valueobject.ClassroomId;
import com.application.domain.SchoolManagement.valueobject.SchoolId;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class CreateClassroomCommand {
    private final ClassroomId classroomId;
    private final String gradeLevel;
    private final String section;
    private final String academicYear;
    private final Integer capacity;
    private final String tutorTeacherId;
    private final SchoolId schoolId;
    private final Boolean active;
}