package com.application.domain.ports.out;

import com.application.domain.model.studentmanagement.Student;
import com.application.domain.model.studentmanagement.StudentId;

import java.util.List;
import java.util.Optional;

public interface StudentRepository {
    Student save(Student student);
    Optional<Student> findById(StudentId studentId);
    List<Student> findAll();
    List<Student> findAllActive();
    List<Student> findByStatus(String status);
    void deleteById(StudentId studentId);
    boolean existsById(StudentId studentId);
}