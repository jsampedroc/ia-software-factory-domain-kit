package com.application.school.domain.academic.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import com.application.school.domain.shared.valueobject.DateRange;
import java.util.Set;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ClassGroup {
    private ClassGroupId classGroupId;
    private String academicYear;
    private String section;
    private Grade grade;
    private Set<Student> enrolledStudents;
    private DateRange academicPeriod;
}