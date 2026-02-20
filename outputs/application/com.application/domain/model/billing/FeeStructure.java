package com.application.domain.model.billing;

import com.application.domain.model.schooladministration.GradeLevel;
import com.application.domain.model.schooladministration.GradeLevelId;
import com.application.domain.valueobject.Money;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class FeeStructure {
    private FeeStructureId feeId;
    private GradeLevelId gradeLevelId;
    private GradeLevel gradeLevel;
    private LocalDate effectiveDate;
    private Money monthlyTuition;
    private Money enrollmentFee;
    private Money otherFees;
}