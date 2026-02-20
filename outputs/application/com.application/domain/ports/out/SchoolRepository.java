package com.application.domain.ports.out;

import com.application.domain.model.schooladministration.School;
import com.application.domain.model.schooladministration.SchoolId;

import java.util.List;
import java.util.Optional;

public interface SchoolRepository {
    School save(School school);
    Optional<School> findById(SchoolId schoolId);
    List<School> findAll();
    void deleteById(SchoolId schoolId);
    boolean existsById(SchoolId schoolId);
}