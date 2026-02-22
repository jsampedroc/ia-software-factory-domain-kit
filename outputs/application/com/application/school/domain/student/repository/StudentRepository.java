package com.application.school.domain.student.repository;

import com.application.school.domain.student.model.Student;
import com.application.school.domain.student.model.StudentId;
import java.util.List;
import java.util.Optional;

public interface StudentRepository {
    Student save(Student student);
    Optional<Student> findById(StudentId studentId);
    Optional<Student> findByLegalId(String legalId);
    List<Student> findAll();
    List<Student> findAllActive();
    void delete(Student student);
    boolean existsById(StudentId studentId);
    boolean existsByLegalId(String legalId);
}