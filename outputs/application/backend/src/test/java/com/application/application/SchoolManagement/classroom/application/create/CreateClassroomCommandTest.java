package com.application;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import com.application.domain.SchoolManagement.school.domain.School;
import com.application.domain.SchoolManagement.school.domain.repository.SchoolRepository;
import com.application.domain.SchoolManagement.classroom.domain.Classroom;
import com.application.domain.SchoolManagement.classroom.domain.repository.ClassroomRepository;
import com.application.domain.SchoolManagement.valueobject.SchoolId;
import com.application.domain.SchoolManagement.valueobject.ClassroomId;
import com.application.domain.exception.DomainException;
import java.util.Optional;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CreateClassroomCommandHandlerTest {

    @Mock
    private SchoolRepository schoolRepository;

    @Mock
    private ClassroomRepository classroomRepository;

    @InjectMocks
    private CreateClassroomCommandHandler handler;

    @Test
    void handle_ShouldCreateClassroom_WhenSchoolExistsAndIsActive() {
        SchoolId schoolId = new SchoolId("school-123");
        CreateClassroomCommand command = new CreateClassroomCommand(
                "1",
                "A",
                "2024-2025",
                30,
                "teacher-456",
                schoolId.value()
        );

        School mockSchool = mock(School.class);
        when(mockSchool.isActive()).thenReturn(true);
        when(schoolRepository.findById(schoolId)).thenReturn(Optional.of(mockSchool));
        when(classroomRepository.save(any(Classroom.class))).thenAnswer(invocation -> invocation.getArgument(0));

        ClassroomId result = handler.handle(command);

        assertNotNull(result);
        verify(schoolRepository).findById(schoolId);
        verify(classroomRepository).save(any(Classroom.class));
        verify(mockSchool).isActive();
    }

    @Test
    void handle_ShouldThrowDomainException_WhenSchoolNotFound() {
        SchoolId schoolId = new SchoolId("non-existent");
        CreateClassroomCommand command = new CreateClassroomCommand(
                "2",
                "B",
                "2024-2025",
                25,
                "teacher-789",
                schoolId.value()
        );

        when(schoolRepository.findById(schoolId)).thenReturn(Optional.empty());

        DomainException exception = assertThrows(DomainException.class, () -> handler.handle(command));
        assertTrue(exception.getMessage().contains("School not found"));
        verify(schoolRepository).findById(schoolId);
        verifyNoInteractions(classroomRepository);
    }

    @Test
    void handle_ShouldThrowDomainException_WhenSchoolIsNotActive() {
        SchoolId schoolId = new SchoolId("school-inactive");
        CreateClassroomCommand command = new CreateClassroomCommand(
                "3",
                "C",
                "2024-2025",
                20,
                "teacher-111",
                schoolId.value()
        );

        School mockSchool = mock(School.class);
        when(mockSchool.isActive()).thenReturn(false);
        when(schoolRepository.findById(schoolId)).thenReturn(Optional.of(mockSchool));

        DomainException exception = assertThrows(DomainException.class, () -> handler.handle(command));
        assertTrue(exception.getMessage().contains("School is not active"));
        verify(schoolRepository).findById(schoolId);
        verify(mockSchool).isActive();
        verifyNoInteractions(classroomRepository);
    }

    @Test
    void handle_ShouldThrowDomainException_WhenCapacityIsNotPositive() {
        SchoolId schoolId = new SchoolId("school-123");
        CreateClassroomCommand command = new CreateClassroomCommand(
                "4",
                "D",
                "2024-2025",
                0,
                "teacher-222",
                schoolId.value()
        );

        School mockSchool = mock(School.class);
        when(mockSchool.isActive()).thenReturn(true);
        when(schoolRepository.findById(schoolId)).thenReturn(Optional.of(mockSchool));

        DomainException exception = assertThrows(DomainException.class, () -> handler.handle(command));
        assertTrue(exception.getMessage().contains("Capacity must be positive"));
        verify(schoolRepository).findById(schoolId);
        verify(mockSchool).isActive();
        verifyNoInteractions(classroomRepository);
    }

    @Test
    void handle_ShouldThrowDomainException_WhenClassroomIdentifierAlreadyExistsForSchoolAndYear() {
        SchoolId schoolId = new SchoolId("school-123");
        CreateClassroomCommand command = new CreateClassroomCommand(
                "1",
                "A",
                "2024-2025",
                30,
                "teacher-456",
                schoolId.value()
        );

        School mockSchool = mock(School.class);
        when(mockSchool.isActive()).thenReturn(true);
        when(schoolRepository.findById(schoolId)).thenReturn(Optional.of(mockSchool));
        when(classroomRepository.existsByGradeLevelAndSectionAndAcademicYearAndSchoolId(
                command.gradeLevel(),
                command.section(),
                command.academicYear(),
                schoolId
        )).thenReturn(true);

        DomainException exception = assertThrows(DomainException.class, () -> handler.handle(command));
        assertTrue(exception.getMessage().contains("already exists"));
        verify(schoolRepository).findById(schoolId);
        verify(mockSchool).isActive();
        verify(classroomRepository).existsByGradeLevelAndSectionAndAcademicYearAndSchoolId(
                command.gradeLevel(),
                command.section(),
                command.academicYear(),
                schoolId
        );
        verify(classroomRepository, never()).save(any());
    }
}