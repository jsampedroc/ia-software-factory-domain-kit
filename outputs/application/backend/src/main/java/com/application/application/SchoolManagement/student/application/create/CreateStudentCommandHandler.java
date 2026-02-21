package com.application.application.SchoolManagement.student.application.create;

import com.application.domain.SchoolManagement.student.domain.Student;
import com.application.domain.SchoolManagement.student.domain.repository.StudentRepository;
import com.application.domain.SchoolManagement.student.domain.valueobject.LegalGuardianId;
import com.application.domain.SchoolManagement.student.domain.valueobject.StudentId;
import com.application.domain.SchoolManagement.school.domain.valueobject.ClassroomId;
import com.application.domain.shared.valueobject.PersonName;
import com.application.domain.exception.DomainException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;

@Service
@RequiredArgsConstructor
public class CreateStudentCommandHandler {

    private final StudentRepository studentRepository;

    @Transactional
    public StudentId handle(CreateStudentCommand command) {
        validateCommand(command);

        StudentId studentId = studentRepository.nextIdentity();
        LegalGuardianId legalGuardianId = new LegalGuardianId(command.getLegalGuardianId());
        PersonName name = new PersonName(command.getFirstName(), command.getLastName());
        ClassroomId currentClassroomId = command.getCurrentClassroomId() != null ?
                new ClassroomId(command.getCurrentClassroomId()) : null;

        Student student = Student.builder()
                .id(studentId)
                .legalGuardianId(legalGuardianId)
                .name(name)
                .dateOfBirth(command.getDateOfBirth())
                .identificationNumber(command.getIdentificationNumber())
                .enrollmentDate(command.getEnrollmentDate())
                .active(true)
                .currentClassroomId(currentClassroomId)
                .build();

        studentRepository.save(student);

        return studentId;
    }

    private void validateCommand(CreateStudentCommand command) {
        if (command.getFirstName() == null || command.getFirstName().trim().isEmpty()) {
            throw new DomainException("Student first name is required.");
        }
        if (command.getLastName() == null || command.getLastName().trim().isEmpty()) {
            throw new DomainException("Student last name is required.");
        }
        if (command.getDateOfBirth() == null) {
            throw new DomainException("Student date of birth is required.");
        }
        if (command.getDateOfBirth().isAfter(LocalDate.now().minusYears(1))) {
            throw new DomainException("Student date of birth must be in the past.");
        }
        if (command.getIdentificationNumber() == null || command.getIdentificationNumber().trim().isEmpty()) {
            throw new DomainException("Student identification number is required.");
        }
        if (command.getEnrollmentDate() == null) {
            throw new DomainException("Student enrollment date is required.");
        }
        if (command.getEnrollmentDate().isBefore(command.getDateOfBirth())) {
            throw new DomainException("Enrollment date must be after date of birth.");
        }
        if (command.getLegalGuardianId() == null) {
            throw new DomainException("Legal guardian ID is required.");
        }
    }
}