package com.application.domain.SchoolManagement.student.domain.repository;

import com.application.domain.SchoolManagement.student.domain.Student;
import com.application.domain.SchoolManagement.student.valueobject.StudentId;
import com.application.domain.shared.repository.Repository;

import java.util.Optional;

public interface StudentRepository extends Repository<Student, StudentId> {
    Optional<Student> findById(StudentId id);
    Optional<Student> findByIdentificationNumber(String identificationNumber);
    boolean existsByIdentificationNumber(String identificationNumber);
    Student save(Student student);
    void delete(Student student);
}