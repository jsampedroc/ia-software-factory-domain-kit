package com.application.domain.model.schooladministration;

import com.application.domain.valueobject.Address;
import com.application.domain.valueobject.PersonName;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Classroom {
    private ClassroomId classroomId;
    private String identifier;
    private GradeLevel gradeLevel;
    private Integer capacity;
    private PersonName primaryTeacher;
    private Address location;
}