package com.application.domain.SchoolManagement.school.domain.repository;

import com.application.domain.SchoolManagement.school.domain.School;
import com.application.domain.SchoolManagement.valueobject.SchoolId;
import com.application.domain.shared.repository.Repository;

import java.util.Optional;

public interface SchoolRepository extends Repository<School, SchoolId> {
    Optional<School> findById(SchoolId id);
    Optional<School> findByName(String name);
    void save(School school);
    void delete(School school);
    boolean existsByName(String name);
}