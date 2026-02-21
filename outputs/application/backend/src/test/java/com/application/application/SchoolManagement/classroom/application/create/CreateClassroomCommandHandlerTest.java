package com.application;

import com.application.application.SchoolManagement.classroom.application.create.CreateClassroomCommand;
import com.application.application.SchoolManagement.classroom.application.create.CreateClassroomCommandHandler;
import com.application.domain.SchoolManagement.school.domain.Classroom;
import com.application.domain.SchoolManagement.school.domain.repository.ClassroomRepository;
import com.application.domain.SchoolManagement.school.domain.School;
import com.application.domain.SchoolManagement.school.domain.repository.SchoolRepository;
import com.application.domain.SchoolManagement.valueobject.ClassroomId;
import com.application.domain.SchoolManagement.valueobject.SchoolId;
import com.application.domain.exception.DomainException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CreateClassroomCommandHandlerTest {

    @Mock
    private ClassroomRepository classroomRepository;

    @Mock
    private SchoolRepository schoolRepository;

    private CreateClassroomCommandHandler handler;

    @BeforeEach
    void setUp() {
        handler = new CreateClassroomCommandHandler(classroomRepository, schoolRepository);
    }

    @Test
    void handle_ShouldCreateClassroomSuccessfully_WhenSchoolExistsAndIsActive() {
        SchoolId schoolId = new SchoolId();
        CreateClassroomCommand command = new CreateClassroomCommand(
                "1st Grade",
                "A",
                2024,
                25,
                schoolId.getValue()
        );

        School mockSchool = mock(School.class);
        when(mockSchool.isActive()).thenReturn(true);
        when(schoolRepository.findById(schoolId)).thenReturn(Optional.of(mockSchool));

        Classroom mockClassroom = mock(Classroom.class);
        when(classroomRepository.save(any(Classroom.class))).thenReturn(mockClassroom);

        ClassroomId result = handler.handle(command);

        assertNotNull(result);
        verify(schoolRepository).findById(schoolId);
        verify(classroomRepository).save(any(Classroom.class));
    }

    @Test
    void handle_ShouldThrowDomainException_WhenSchoolNotFound() {
        SchoolId schoolId = new SchoolId();
        CreateClassroomCommand command = new CreateClassroomCommand(
                "1st Grade",
                "A",
                2024,
                25,
                schoolId.getValue()
        );

        when(schoolRepository.findById(schoolId)).thenReturn(Optional.empty());

        DomainException exception = assertThrows(DomainException.class, () -> handler.handle(command));
        assertTrue(exception.getMessage().contains("School not found"));
        verify(schoolRepository).findById(schoolId);
        verifyNoInteractions(classroomRepository);
    }

    @Test
    void handle_ShouldThrowDomainException_WhenSchoolIsNotActive() {
        SchoolId schoolId = new SchoolId();
        CreateClassroomCommand command = new CreateClassroomCommand(
                "1st Grade",
                "A",
                2024,
                25,
                schoolId.getValue()
        );

        School mockSchool = mock(School.class);
        when(mockSchool.isActive()).thenReturn(false);
        when(schoolRepository.findById(schoolId)).thenReturn(Optional.of(mockSchool));

        DomainException exception = assertThrows(DomainException.class, () -> handler.handle(command));
        assertTrue(exception.getMessage().contains("inactive"));
        verify(schoolRepository).findById(schoolId);
        verifyNoInteractions(classroomRepository);
    }

    @Test
    void handle_ShouldThrowDomainException_WhenCapacityIsNotPositive() {
        SchoolId schoolId = new SchoolId();
        CreateClassroomCommand command = new CreateClassroomCommand(
                "1st Grade",
                "A",
                2024,
                0,
                schoolId.getValue()
        );

        School mockSchool = mock(School.class);
        when(mockSchool.isActive()).thenReturn(true);
        when(schoolRepository.findById(schoolId)).thenReturn(Optional.of(mockSchool));

        DomainException exception = assertThrows(DomainException.class, () -> handler.handle(command));
        assertTrue(exception.getMessage().contains("capacity") || exception.getMessage().contains("positive"));
        verify(schoolRepository).findById(schoolId);
        verifyNoInteractions(classroomRepository);
    }

    @Test
    void handle_ShouldPropagateException_WhenRepositorySaveFails() {
        SchoolId schoolId = new SchoolId();
        CreateClassroomCommand command = new CreateClassroomCommand(
                "1st Grade",
                "A",
                2024,
                25,
                schoolId.getValue()
        );

        School mockSchool = mock(School.class);
        when(mockSchool.isActive()).thenReturn(true);
        when(schoolRepository.findById(schoolId)).thenReturn(Optional.of(mockSchool));
        when(classroomRepository.save(any(Classroom.class))).thenThrow(new RuntimeException("DB Error"));

        RuntimeException exception = assertThrows(RuntimeException.class, () -> handler.handle(command));
        assertEquals("DB Error", exception.getMessage());
        verify(schoolRepository).findById(schoolId);
        verify(classroomRepository).save(any(Classroom.class));
    }
}