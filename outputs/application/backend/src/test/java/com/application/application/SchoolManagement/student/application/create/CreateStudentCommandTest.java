package com.application.application.SchoolManagement.student.application.create;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import com.application.domain.SchoolManagement.student.domain.Student;
import com.application.domain.SchoolManagement.student.domain.repository.StudentRepository;
import com.application.domain.SchoolManagement.valueobject.LegalGuardianId;
import com.application.domain.SchoolManagement.valueobject.StudentId;
import com.application.domain.shared.valueobject.PersonName;
import com.application.domain.exception.DomainException;
import java.time.LocalDate;
import java.util.Optional;
import java.util.UUID;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CreateStudentCommandHandlerTest {

    @Mock
    private StudentRepository studentRepository;

    @InjectMocks
    private CreateStudentCommandHandler handler;

    @Test
    void handle_ShouldCreateStudentSuccessfully() {
        UUID legalGuardianUUID = UUID.randomUUID();
        UUID studentUUID = UUID.randomUUID();
        CreateStudentCommand command = new CreateStudentCommand(
                studentUUID,
                legalGuardianUUID,
                "John",
                "Doe",
                LocalDate.of(2015, 5, 10),
                "ID123456",
                LocalDate.now(),
                true,
                null
        );

        when(studentRepository.findByIdentificationNumber("ID123456")).thenReturn(Optional.empty());
        when(studentRepository.save(any(Student.class))).thenAnswer(invocation -> invocation.getArgument(0));

        StudentId result = handler.handle(command);

        assertNotNull(result);
        assertEquals(studentUUID, result.getValue());
        verify(studentRepository).findByIdentificationNumber("ID123456");
        verify(studentRepository).save(any(Student.class));
    }

    @Test
    void handle_ShouldThrowDomainException_WhenIdentificationNumberAlreadyExists() {
        UUID legalGuardianUUID = UUID.randomUUID();
        UUID studentUUID = UUID.randomUUID();
        CreateStudentCommand command = new CreateStudentCommand(
                studentUUID,
                legalGuardianUUID,
                "Jane",
                "Smith",
                LocalDate.of(2016, 3, 15),
                "ID789012",
                LocalDate.now(),
                true,
                null
        );

        Student existingStudent = mock(Student.class);
        when(studentRepository.findByIdentificationNumber("ID789012")).thenReturn(Optional.of(existingStudent));

        DomainException exception = assertThrows(DomainException.class, () -> handler.handle(command));
        assertTrue(exception.getMessage().contains("El número de identificación debe ser único"));
        verify(studentRepository, never()).save(any(Student.class));
    }

    @Test
    void handle_ShouldThrowDomainException_WhenDateOfBirthIsAfterEnrollmentDate() {
        UUID legalGuardianUUID = UUID.randomUUID();
        UUID studentUUID = UUID.randomUUID();
        LocalDate futureDateOfBirth = LocalDate.now().plusDays(1);
        CreateStudentCommand command = new CreateStudentCommand(
                studentUUID,
                legalGuardianUUID,
                "Alice",
                "Brown",
                futureDateOfBirth,
                "ID555555",
                LocalDate.now(),
                true,
                null
        );

        when(studentRepository.findByIdentificationNumber("ID555555")).thenReturn(Optional.empty());

        DomainException exception = assertThrows(DomainException.class, () -> handler.handle(command));
        assertTrue(exception.getMessage().contains("La fecha de nacimiento debe ser anterior a la fecha de inscripción"));
        verify(studentRepository, never()).save(any(Student.class));
    }

    @Test
    void handle_ShouldCreateStudentWithNullCurrentClassroomId() {
        UUID legalGuardianUUID = UUID.randomUUID();
        UUID studentUUID = UUID.randomUUID();
        CreateStudentCommand command = new CreateStudentCommand(
                studentUUID,
                legalGuardianUUID,
                "Bob",
                "Wilson",
                LocalDate.of(2014, 8, 22),
                "ID999999",
                LocalDate.now(),
                true,
                null
        );

        when(studentRepository.findByIdentificationNumber("ID999999")).thenReturn(Optional.empty());
        when(studentRepository.save(any(Student.class))).thenAnswer(invocation -> invocation.getArgument(0));

        StudentId result = handler.handle(command);

        assertNotNull(result);
        verify(studentRepository).save(any(Student.class));
    }

    @Test
    void handle_ShouldCreateStudentWithCurrentClassroomId() {
        UUID legalGuardianUUID = UUID.randomUUID();
        UUID studentUUID = UUID.randomUUID();
        UUID classroomUUID = UUID.randomUUID();
        CreateStudentCommand command = new CreateStudentCommand(
                studentUUID,
                legalGuardianUUID,
                "Charlie",
                "Green",
                LocalDate.of(2013, 11, 5),
                "ID111111",
                LocalDate.now(),
                true,
                classroomUUID
        );

        when(studentRepository.findByIdentificationNumber("ID111111")).thenReturn(Optional.empty());
        when(studentRepository.save(any(Student.class))).thenAnswer(invocation -> invocation.getArgument(0));

        StudentId result = handler.handle(command);

        assertNotNull(result);
        verify(studentRepository).save(any(Student.class));
    }
}