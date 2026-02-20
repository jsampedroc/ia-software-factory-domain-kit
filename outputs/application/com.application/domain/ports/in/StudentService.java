package com.application.domain.ports.in;

import com.application.domain.model.studentmanagement.StudentId;
import com.application.application.dto.StudentDTO;

import java.util.List;
import java.util.Optional;

public interface StudentService {
    StudentDTO createStudent(StudentDTO studentDTO);
    Optional<StudentDTO> getStudentById(StudentId studentId);
    List<StudentDTO> getAllStudents();
    StudentDTO updateStudent(StudentId studentId, StudentDTO studentDTO);
    void deleteStudent(StudentId studentId);
    StudentDTO enrollStudent(StudentId studentId, String gradeLevelCode, String section, String academicYear);
    StudentDTO assignLegalGuardian(StudentId studentId, String guardianFirstName, String guardianLastName, String relationship, String email, String phone);
    StudentDTO changeStudentStatus(StudentId studentId, String status);
}