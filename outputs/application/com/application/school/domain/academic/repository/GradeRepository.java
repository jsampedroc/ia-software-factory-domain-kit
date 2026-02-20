package com.application.school.domain.academic.repository;

import com.application.school.domain.academic.model.Grade;
import com.application.school.domain.academic.model.GradeId;

import java.util.List;
import java.util.Optional;

public interface GradeRepository {
    Grade save(Grade grade);
    Optional<Grade> findById(GradeId gradeId);
    List<Grade> findAll();
    void deleteById(GradeId gradeId);
    boolean existsById(GradeId gradeId);
    Optional<Grade> findByName(String name);
}