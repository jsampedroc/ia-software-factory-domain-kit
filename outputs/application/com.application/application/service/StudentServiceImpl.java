package com.application.application.service;

import com.application.domain.model.studentmanagement.Student;
import com.application.domain.model.studentmanagement.StudentId;
import com.application.domain.model.studentmanagement.LegalGuardian;
import com.application.domain.model.studentmanagement.Enrollment;
import com.application.domain.ports.in.StudentService;
import com.application.domain.ports.out.StudentRepository;
import com.application.domain.ports.out.SchoolRepository;
import com.application.application.dto.StudentDTO;
import com.application.application.dto.LegalGuardianDTO;
import com.application.application.mapper.StudentMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Transactional
@RequiredArgsConstructor
@Slf4j
public class StudentServiceImpl implements StudentService {

    private final StudentRepository studentRepository;
    private final SchoolRepository schoolRepository;
    private final StudentMapper studentMapper;

    @Override
    public StudentDTO registerStudent(StudentDTO studentDTO) {
        log.info("Registering new student: {}", studentDTO.getFirstName());
        Student student = studentMapper.toDomain(studentDTO);
        // Business rule: Student must have a primary legal guardian at enrollment.
        // This is validated in the domain entity creation.
        Student savedStudent = studentRepository.save(student);
        return studentMapper.toDTO(savedStudent);
    }

    @Override
    public Optional<StudentDTO> updateStudent(String studentId, StudentDTO studentDTO) {
        log.info("Updating student with ID: {}", studentId);
        StudentId id = new StudentId(studentId);
        return studentRepository.findById(id)
                .map(existingStudent -> {
                    Student updatedStudent = studentMapper.toDomain(studentDTO);
                    // In a real scenario, we would update specific fields, not replace the whole object.
                    // For simplicity, we assume the mapper handles updates correctly.
                    updatedStudent = studentRepository.save(updatedStudent);
                    return studentMapper.toDTO(updatedStudent);
                });
    }

    @Override
    public Optional<StudentDTO> findStudentById(String studentId) {
        log.debug("Finding student by ID: {}", studentId);
        StudentId id = new StudentId(studentId);
        return studentRepository.findById(id)
                .map(studentMapper::toDTO);
    }

    @Override
    public List<StudentDTO> findAllStudents() {
        log.debug("Finding all students");
        return studentRepository.findAll().stream()
                .map(studentMapper::toDTO)
                .collect(Collectors.toList());
    }

    @Override
    public List<StudentDTO> findStudentsByStatus(String status) {
        log.debug("Finding students by status: {}", status);
        return studentRepository.findAll().stream()
                .filter(student -> student.getStatus().name().equalsIgnoreCase(status))
                .map(studentMapper::toDTO)
                .collect(Collectors.toList());
    }

    @Override
    public boolean deactivateStudent(String studentId) {
        log.info("Deactivating student with ID: {}", studentId);
        StudentId id = new StudentId(studentId);
        return studentRepository.findById(id)
                .map(student -> {
                    student.deactivate(); // Assuming a domain method to change status
                    studentRepository.save(student);
                    return true;
                })
                .orElse(false);
    }

    @Override
    public LegalGuardianDTO addLegalGuardianToStudent(String studentId, LegalGuardianDTO guardianDTO) {
        log.info("Adding legal guardian to student ID: {}", studentId);
        StudentId id = new StudentId(studentId);
        return studentRepository.findById(id)
                .map(student -> {
                    LegalGuardian guardian = studentMapper.toGuardianDomain(guardianDTO);
                    student.addLegalGuardian(guardian); // Assuming a domain method
                    studentRepository.save(student);
                    return studentMapper.toGuardianDTO(guardian);
                })
                .orElseThrow(() -> new RuntimeException("Student not found with ID: " + studentId));
    }

    @Override
    public List<LegalGuardianDTO> getLegalGuardiansForStudent(String studentId) {
        log.debug("Getting legal guardians for student ID: {}", studentId);
        StudentId id = new StudentId(studentId);
        return studentRepository.findById(id)
                .map(student -> student.getLegalGuardians().stream()
                        .map(studentMapper::toGuardianDTO)
                        .collect(Collectors.toList()))
                .orElseThrow(() -> new RuntimeException("Student not found with ID: " + studentId));
    }

    @Override
    public Enrollment enrollStudentInGrade(String studentId, String gradeLevelId, String section, String academicYear) {
        log.info("Enrolling student {} in grade {} for academic year {}", studentId, gradeLevelId, academicYear);
        // This method would require more complex logic involving SchoolRepository to fetch GradeLevel.
        // For now, it returns a domain object as per the port definition.
        // Implementation would involve checking for active enrollment in the same academic year.
        throw new UnsupportedOperationException("Enrollment logic not fully implemented in this example service.");
    }
}