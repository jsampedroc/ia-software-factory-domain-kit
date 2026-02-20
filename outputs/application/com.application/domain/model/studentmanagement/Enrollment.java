package com.application.domain.model.studentmanagement;

import com.application.domain.model.schooladministration.GradeLevel;
import com.application.domain.valueobject.DateRange;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Enrollment {
    private EnrollmentId enrollmentId;
    private Student student;
    private GradeLevel gradeLevel;
    private String section;
    private String academicYear;
    private LocalDate enrolledDate;
    private DateRange academicPeriod;

    public boolean isActiveForDate(LocalDate date) {
        return academicPeriod != null && academicPeriod.contains(date);
    }
}