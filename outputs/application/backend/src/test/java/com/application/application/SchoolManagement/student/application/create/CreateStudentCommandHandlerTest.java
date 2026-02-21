package com.application;

import com.application.application.SchoolManagement.student.application.create.CreateStudentCommand;
import com.application.application.SchoolManagement.student.application.create.CreateStudentCommandHandler;
import com.application.domain.SchoolManagement.student.domain.LegalGuardian;
import com.application.domain.SchoolManagement.student.domain.Student;
import com.application.domain.SchoolManagement.student.domain.repository.StudentRepository;
import com.application.domain.SchoolManagement.student.domain.repository.LegalGuardianRepository;
import com.application.domain.SchoolManagement.school.domain.repository.ClassroomRepository;
import com.application.domain.SchoolManagement.valueobject.StudentId;
import com.application.domain.SchoolManagement.valueobject.LegalGuardianId;
import com.application.domain.SchoolManagement.valueobject.ClassroomId;
import com.application.domain.SchoolManagement.valueobject.SchoolId;
import com.application.domain.shared.valueobject.PersonName;
import com.application.domain.exception.DomainException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

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
    @Mock
    private LegalGuardianRepository legalGuardianRepository;
    @Mock
    private ClassroomRepository classroomRepository;

    private CreateStudentCommandHandler handler;

    @BeforeEach
    void setUp() {
        handler = new CreateStudentCommandHandler(studentRepository, legalGuardianRepository, classroomRepository);
    }

    @Test
    void handle_ShouldCreateStudentSuccessfully_WhenCommandIsValid() {
        UUID guardianId = UUID.randomUUID();
        UUID classroomId = UUID.randomUUID();
        UUID schoolId = UUID.randomUUID();
        CreateStudentCommand command = new CreateStudentCommand(
                guardianId,
                "John",
                "Doe",
                LocalDate.of(2015, 5, 10),
                "ID123456",
                LocalDate.now(),
                classroomId,
                schoolId
        );

        LegalGuardian mockGuardian = mock(LegalGuardian.class);
        when(legalGuardianRepository.findById(new LegalGuardianId(guardianId))).thenReturn(Optional.of(mockGuardian));
        when(classroomRepository.existsByIdAndSchoolId(new ClassroomId(classroomId), new SchoolId(schoolId))).thenReturn(true);
        when(studentRepository.existsByIdentificationNumber("ID123456")).thenReturn(false);
        when(studentRepository.save(any(Student.class))).thenAnswer(invocation -> invocation.getArgument(0));

        StudentId result = handler.handle(command);

        assertNotNull(result);
        verify(legalGuardianRepository).findById(new LegalGuardianId(guardianId));
        verify(classroomRepository).existsByIdAndSchoolId(new ClassroomId(classroomId), new SchoolId(schoolId));
        verify(studentRepository).existsByIdentificationNumber("ID123456");
        verify(studentRepository).save(any(Student.class));
    }

    @Test
    void handle_ShouldThrowDomainException_WhenLegalGuardianNotFound() {
        UUID guardianId = UUID.randomUUID();
        CreateStudentCommand command = new CreateStudentCommand(
                guardianId,
                "John",
                "Doe",
                LocalDate.of(2015, 5, 10),
                "ID123456",
                LocalDate.now(),
                UUID.randomUUID(),
                UUID.randomUUID()
        );

        when(legalGuardianRepository.findById(new LegalGuardianId(guardianId))).thenReturn(Optional.empty());

        DomainException exception = assertThrows(DomainException.class, () -> handler.handle(command));
        assertTrue(exception.getMessage().contains("LegalGuardian"));
        verify(legalGuardianRepository).findById(new LegalGuardianId(guardianId));
        verifyNoInteractions(classroomRepository, studentRepository);
    }

    @Test
    void handle_ShouldThrowDomainException_WhenClassroomDoesNotBelongToSchool() {
        UUID guardianId = UUID.randomUUID();
        UUID classroomId = UUID.randomUUID();
        UUID schoolId = UUID.randomUUID();
        CreateStudentCommand command = new CreateStudentCommand(
                guardianId,
                "John",
                "Doe",
                LocalDate.of(2015, 5, 10),
                "ID123456",
                LocalDate.now(),
                classroomId,
                schoolId
        );

        LegalGuardian mockGuardian = mock(LegalGuardian.class);
        when(legalGuardianRepository.findById(new LegalGuardianId(guardianId))).thenReturn(Optional.of(mockGuardian));
        when(classroomRepository.existsByIdAndSchoolId(new ClassroomId(classroomId), new SchoolId(schoolId))).thenReturn(false);

        DomainException exception = assertThrows(DomainException.class, () -> handler.handle(command));
        assertTrue(exception.getMessage().contains("Classroom") || exception.getMessage().contains("School"));
        verify(legalGuardianRepository).findById(new LegalGuardianId(guardianId));
        verify(classroomRepository).existsByIdAndSchoolId(new ClassroomId(classroomId), new SchoolId(schoolId));
        verifyNoInteractions(studentRepository);
    }

    @Test
    void handle_ShouldThrowDomainException_WhenIdentificationNumberAlreadyExists() {
        UUID guardianId = UUID.randomUUID();
        UUID classroomId = UUID.randomUUID();
        UUID schoolId = UUID.randomUUID();
        CreateStudentCommand command = new CreateStudentCommand(
                guardianId,
                "John",
                "Doe",
                LocalDate.of(2015, 5, 10),
                "ID123456",
                LocalDate.now(),
                classroomId,
                schoolId
        );

        LegalGuardian mockGuardian = mock(LegalGuardian.class);
        when(legalGuardianRepository.findById(new LegalGuardianId(guardianId))).thenReturn(Optional.of(mockGuardian));
        when(classroomRepository.existsByIdAndSchoolId(new ClassroomId(classroomId), new SchoolId(schoolId))).thenReturn(true);
        when(studentRepository.existsByIdentificationNumber("ID123456")).thenReturn(true);

        DomainException exception = assertThrows(DomainException.class, () -> handler.handle(command));
        assertTrue(exception.getMessage().contains("identification") || exception.getMessage().contains("unique"));
        verify(legalGuardianRepository).findById(new LegalGuardianId(guardianId));
        verify(classroomRepository).existsByIdAndSchoolId(new ClassroomId(classroomId), new SchoolId(schoolId));
        verify(studentRepository).existsByIdentificationNumber("ID123456");
        verify(studentRepository, never()).save(any());
    }

    @Test
    void handle_ShouldThrowDomainException_WhenDateOfBirthIsAfterEnrollmentDate() {
        UUID guardianId = UUID.randomUUID();
        UUID classroomId = UUID.randomUUID();
        UUID schoolId = UUID.randomUUID();
        LocalDate futureBirth = LocalDate.now().plusDays(1);
        CreateStudentCommand command = new CreateStudentCommand(
                guardianId,
                "John",
                "Doe",
                futureBirth,
                "ID123456",
                LocalDate.now(),
                classroomId,
                schoolId
        );

        LegalGuardian mockGuardian = mock(LegalGuardian.class);
        when(legalGuardianRepository.findById(new LegalGuardianId(guardianId))).thenReturn(Optional.of(mockGuardian));
        when(classroomRepository.existsByIdAndSchoolId(new ClassroomId(classroomId), new SchoolId(schoolId))).thenReturn(true);
        when(studentRepository.existsByIdentificationNumber("ID123456")).thenReturn(false);

        DomainException exception = assertThrows(DomainException.class, () -> handler.handle(command));
        assertTrue(exception.getMessage().contains("date of birth") || exception.getMessage().contains("enrollment"));
        verify(legalGuardianRepository).findById(new LegalGuardianId(guardianId));
        verify(classroomRepository).existsByIdAndSchoolId(new ClassroomId(classroomId), new SchoolId(schoolId));
        verify(studentRepository).existsByIdentificationNumber("ID123456");
        verify(studentRepository, never()).save(any());
    }
}