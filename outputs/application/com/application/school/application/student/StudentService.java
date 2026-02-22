package com.application.school.application.student;

import com.application.school.application.student.dto.CreateStudentCommand;
import com.application.school.application.student.dto.StudentResponse;
import com.application.school.domain.student.model.Guardian;
import com.application.school.domain.student.model.GuardianId;
import com.application.school.domain.student.model.Student;
import com.application.school.domain.student.model.StudentId;
import com.application.school.domain.student.repository.StudentRepository;
import com.application.school.domain.shared.enumeration.StudentStatus;
import com.application.school.domain.shared.valueobject.PersonalName;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class StudentService {

    private final StudentRepository studentRepository;

    @Transactional
    public StudentResponse createStudent(CreateStudentCommand command) {
        log.info("Creating student with legalId: {}", command.getLegalId());

        // Verificar unicidad del legalId (regla de negocio)
        studentRepository.findByLegalId(command.getLegalId())
                .ifPresent(existing -> {
                    throw new IllegalArgumentException("A student with legalId " + command.getLegalId() + " already exists.");
                });

        // Generar IDs
        StudentId studentId = StudentId.of(UUID.randomUUID());
        List<Guardian> guardians = command.getGuardians().stream()
                .map(g -> Guardian.builder()
                        .guardianId(GuardianId.of(UUID.randomUUID()))
                        .name(PersonalName.builder()
                                .firstName(g.getFirstName())
                                .lastName(g.getLastName())
                                .build())
                        .email(g.getEmail())
                        .phoneNumber(g.getPhoneNumber())
                        .relationship(g.getRelationship())
                        .build())
                .collect(Collectors.toList());

        // Construir el agregado Student
        Student student = Student.builder()
                .studentId(studentId)
                .legalId(command.getLegalId())
                .name(PersonalName.builder()
                        .firstName(command.getFirstName())
                        .lastName(command.getLastName())
                        .build())
                .dateOfBirth(command.getDateOfBirth())
                .enrollmentDate(LocalDate.now())
                .status(StudentStatus.ACTIVE)
                .guardians(guardians)
                .build();

        // Guardar
        Student savedStudent = studentRepository.save(student);
        log.info("Student created with id: {}", savedStudent.getStudentId().getValue());

        // Mapear a respuesta (en un escenario real, usaríamos un mapper dedicado)
        return mapToResponse(savedStudent);
    }

    @Transactional(readOnly = true)
    public StudentResponse getStudentById(UUID id) {
        log.info("Fetching student with id: {}", id);
        Student student = studentRepository.findById(StudentId.of(id))
                .orElseThrow(() -> new IllegalArgumentException("Student not found with id: " + id));
        return mapToResponse(student);
    }

    @Transactional(readOnly = true)
    public List<StudentResponse> getAllStudents() {
        log.info("Fetching all students");
        return studentRepository.findAll().stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    @Transactional
    public void deactivateStudent(UUID id) {
        log.info("Deactivating student with id: {}", id);
        Student student = studentRepository.findById(StudentId.of(id))
                .orElseThrow(() -> new IllegalArgumentException("Student not found with id: " + id));

        // Invariante: No se puede desactivar si tiene facturas pendientes (se delegaría a un servicio de dominio o se verificaría aquí)
        // Por simplicidad, se omite en este ejemplo, pero se lanzaría una excepción si no se cumple.

        student.deactivate();
        studentRepository.save(student);
        log.info("Student deactivated: {}", id);
    }

    private StudentResponse mapToResponse(Student student) {
        return StudentResponse.builder()
                .studentId(student.getStudentId().getValue())
                .legalId(student.getLegalId())
                .firstName(student.getName().getFirstName())
                .lastName(student.getName().getLastName())
                .dateOfBirth(student.getDateOfBirth())
                .enrollmentDate(student.getEnrollmentDate())
                .status(student.getStatus())
                .guardians(student.getGuardians().stream()
                        .map(g -> StudentResponse.GuardianResponse.builder()
                                .guardianId(g.getGuardianId().getValue())
                                .firstName(g.getName().getFirstName())
                                .lastName(g.getName().getLastName())
                                .email(g.getEmail())
                                .phoneNumber(g.getPhoneNumber())
                                .relationship(g.getRelationship())
                                .build())
                        .collect(Collectors.toList()))
                .build();
    }
}