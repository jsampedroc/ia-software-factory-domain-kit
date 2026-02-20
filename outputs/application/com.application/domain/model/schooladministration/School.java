package com.application.domain.model.schooladministration;

import com.application.domain.valueobject.Address;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class School {
    private SchoolId schoolId;
    private String name;
    private Address address;
    private String phone;
    private String email;
    private String academicCalendar;
    private List<GradeLevel> gradeLevels;
    private List<Classroom> classrooms;
}