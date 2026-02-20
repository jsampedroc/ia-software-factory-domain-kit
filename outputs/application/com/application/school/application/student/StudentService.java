package com.application.school.application.student;

import com.application.school.domain.student.model.Student;
import com.application.school.domain.student.model.StudentId;
import com.application.school.domain.student.model.StudentStatus;
import com.application.school.domain.student.repository.StudentRepository;
import com.application.school.application.dtos.StudentDTO;
import com.application.school.application.dtos.GuardianDTO;
import com.application.school.application.mappers.StudentMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
@Slf4j
public class StudentService {

    private final StudentRepository studentRepository;
    private final StudentMapper studentMapper;

    @Transactional
    public StudentDTO enrollStudent(StudentDTO studentDTO) {
        log.info("Enrolling new student with legalId: {}", studentDTO.getLegalId());

        Student student = studentMapper.toDomain(studentDTO);
        student.enroll();

        Student savedStudent = studentRepository.save(student);
        log.info("Student enrolled successfully with id: {}", savedStudent.getId().getValue());

        return studentMapper.toDTO(savedStudent);
    }

    public Optional<StudentDTO> findById(String studentId) {
        log.debug("Finding student by id: {}", studentId);
        return studentRepository.findById(StudentId.fromString(studentId))
                .map(studentMapper::toDTO);
    }

    public List<StudentDTO> findAll() {
        log.debug("Finding all students");
        return studentRepository.findAll().stream()
                .map(studentMapper::toDTO)
                .collect(Collectors.toList());
    }

    public List<StudentDTO> findByStatus(StudentStatus status) {
        log.debug("Finding students by status: {}", status);
        return studentRepository.findByStatus(status).stream()
                .map(studentMapper::toDTO)
                .collect(Collectors.toList());
    }

    @Transactional
    public Optional<StudentDTO> updateStudent(String studentId, StudentDTO studentDTO) {
        log.info("Updating student with id: {}", studentId);
        StudentId id = StudentId.fromString(studentId);

        return studentRepository.findById(id)
                .map(existingStudent -> {
                    Student updatedData = studentMapper.toDomain(studentDTO);
                    existingStudent.updatePersonalInfo(
                            updatedData.getLegalId(),
                            updatedData.getFirstName(),
                            updatedData.getLastName(),
                            updatedData.getDateOfBirth()
                    );
                    Student saved = studentRepository.save(existingStudent);
                    log.info("Student updated successfully: {}", studentId);
                    return studentMapper.toDTO(saved);
                });
    }

    @Transactional
    public Optional<StudentDTO> updateStatus(String studentId, StudentStatus newStatus) {
        log.info("Updating status for student {} to {}", studentId, newStatus);
        StudentId id = StudentId.fromString(studentId);

        return studentRepository.findById(id)
                .map(student -> {
                    student.changeStatus(newStatus);
                    Student saved = studentRepository.save(student);
                    log.info("Status updated successfully for student: {}", studentId);
                    return studentMapper.toDTO(saved);
                });
    }

    @Transactional
    public Optional<StudentDTO> addGuardian(String studentId, GuardianDTO guardianDTO) {
        log.info("Adding guardian to student: {}", studentId);
        StudentId id = StudentId.fromString(studentId);

        return studentRepository.findById(id)
                .map(student -> {
                    student.addGuardian(
                            guardianDTO.getFirstName(),
                            guardianDTO.getLastName(),
                            guardianDTO.getEmail(),
                            guardianDTO.getPhoneNumber(),
                            guardianDTO.getRelationship()
                    );
                    Student saved = studentRepository.save(student);
                    log.info("Guardian added successfully to student: {}", studentId);
                    return studentMapper.toDTO(saved);
                });
    }

    @Transactional
    public boolean removeGuardian(String studentId, String guardianId) {
        log.info("Removing guardian {} from student: {}", guardianId, studentId);
        StudentId sid = StudentId.fromString(studentId);

        return studentRepository.findById(sid)
                .map(student -> {
                    boolean removed = student.removeGuardian(guardianId);
                    if (removed) {
                        studentRepository.save(student);
                        log.info("Guardian removed successfully");
                    } else {
                        log.warn("Guardian not found for removal");
                    }
                    return removed;
                })
                .orElse(false);
    }

    public List<GuardianDTO> getGuardians(String studentId) {
        log.debug("Retrieving guardians for student: {}", studentId);
        StudentId id = StudentId.fromString(studentId);

        return studentRepository.findById(id)
                .map(student -> student.getGuardians().stream()
                        .map(studentMapper::guardianToDTO)
                        .collect(Collectors.toList()))
                .orElse(List.of());
    }

    public boolean existsByLegalId(String legalId) {
        log.debug("Checking existence of student with legalId: {}", legalId);
        return studentRepository.existsByLegalId(legalId);
    }

    @Transactional
    public boolean deactivateStudent(String studentId) {
        log.info("Deactivating student: {}", studentId);
        StudentId id = StudentId.fromString(studentId);

        return studentRepository.findById(id)
                .map(student -> {
                    student.changeStatus(StudentStatus.INACTIVE);
                    studentRepository.save(student);
                    log.info("Student deactivated successfully: {}", studentId);
                    return true;
                })
                .orElse(false);
    }
}