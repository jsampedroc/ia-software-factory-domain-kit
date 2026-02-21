package com.application;

import com.application.application.SchoolManagement.school.application.create.CreateSchoolCommand;
import com.application.application.SchoolManagement.school.application.create.CreateSchoolCommandHandler;
import com.application.domain.SchoolManagement.school.domain.School;
import com.application.domain.SchoolManagement.school.domain.repository.SchoolRepository;
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
class CreateSchoolCommandHandlerTest {

    @Mock
    private SchoolRepository schoolRepository;

    private CreateSchoolCommandHandler handler;

    @BeforeEach
    void setUp() {
        handler = new CreateSchoolCommandHandler(schoolRepository);
    }

    @Test
    void handle_ShouldCreateAndSaveSchool_WhenCommandIsValid() {
        CreateSchoolCommand command = new CreateSchoolCommand(
                "Test School",
                "123 Main St",
                "555-1234"
        );

        when(schoolRepository.findByName(any(String.class))).thenReturn(Optional.empty());
        when(schoolRepository.save(any(School.class))).thenAnswer(invocation -> invocation.getArgument(0));

        SchoolId result = handler.handle(command);

        assertNotNull(result);
        verify(schoolRepository, times(1)).findByName("Test School");
        verify(schoolRepository, times(1)).save(any(School.class));
    }

    @Test
    void handle_ShouldThrowDomainException_WhenSchoolNameAlreadyExists() {
        CreateSchoolCommand command = new CreateSchoolCommand(
                "Existing School",
                "456 Oak Ave",
                "555-5678"
        );

        School existingSchool = mock(School.class);
        when(schoolRepository.findByName("Existing School")).thenReturn(Optional.of(existingSchool));

        DomainException exception = assertThrows(DomainException.class, () -> handler.handle(command));

        assertTrue(exception.getMessage().contains("único"));
        verify(schoolRepository, times(1)).findByName("Existing School");
        verify(schoolRepository, never()).save(any(School.class));
    }

    @Test
    void handle_ShouldThrowDomainException_WhenSchoolNameIsNull() {
        CreateSchoolCommand command = new CreateSchoolCommand(
                null,
                "789 Pine Rd",
                "555-9999"
        );

        DomainException exception = assertThrows(DomainException.class, () -> handler.handle(command));

        assertTrue(exception.getMessage().contains("obligatorio"));
        verify(schoolRepository, never()).findByName(any());
        verify(schoolRepository, never()).save(any(School.class));
    }

    @Test
    void handle_ShouldThrowDomainException_WhenSchoolNameIsEmpty() {
        CreateSchoolCommand command = new CreateSchoolCommand(
                "",
                "789 Pine Rd",
                "555-9999"
        );

        DomainException exception = assertThrows(DomainException.class, () -> handler.handle(command));

        assertTrue(exception.getMessage().contains("obligatorio"));
        verify(schoolRepository, never()).findByName(any());
        verify(schoolRepository, never()).save(any(School.class));
    }

    @Test
    void handle_ShouldCreateSchoolWithActiveStatusByDefault() {
        CreateSchoolCommand command = new CreateSchoolCommand(
                "New Active School",
                "101 Maple Blvd",
                "555-0000"
        );

        when(schoolRepository.findByName(any(String.class))).thenReturn(Optional.empty());
        when(schoolRepository.save(any(School.class))).thenAnswer(invocation -> {
            School savedSchool = invocation.getArgument(0);
            assertTrue(savedSchool.isActive());
            return savedSchool;
        });

        handler.handle(command);

        verify(schoolRepository, times(1)).save(any(School.class));
    }
}