package com.application.domain.SchoolManagement.classroom.domain.repository;

import com.application.domain.SchoolManagement.classroom.domain.Classroom;
import com.application.domain.SchoolManagement.valueobject.ClassroomId;
import com.application.domain.SchoolManagement.valueobject.SchoolId;

import java.util.List;
import java.util.Optional;

public interface ClassroomRepository {
    Classroom save(Classroom classroom);
    Optional<Classroom> findById(ClassroomId id);
    List<Classroom> findBySchoolId(SchoolId schoolId);
    boolean existsById(ClassroomId id);
    void delete(Classroom classroom);
}