package com.application.domain.ports.out;

import com.application.domain.model.billing.FeeStructure;
import com.application.domain.model.billing.FeeStructureId;
import com.application.domain.model.schooladministration.GradeLevelId;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface FeeStructureRepository {
    FeeStructure save(FeeStructure feeStructure);
    Optional<FeeStructure> findById(FeeStructureId feeStructureId);
    List<FeeStructure> findAll();
    void deleteById(FeeStructureId feeStructureId);
    Optional<FeeStructure> findActiveByGradeLevelAndDate(GradeLevelId gradeLevelId, LocalDate date);
    List<FeeStructure> findByGradeLevel(GradeLevelId gradeLevelId);
}