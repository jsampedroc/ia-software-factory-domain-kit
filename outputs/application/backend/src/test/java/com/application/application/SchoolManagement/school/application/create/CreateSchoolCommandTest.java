package com.application;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import com.application.domain.SchoolManagement.school.domain.School;
import com.application.domain.SchoolManagement.school.domain.repository.SchoolRepository;
import com.application.domain.SchoolManagement.valueobject.SchoolId;
import com.application.domain.exception.DomainException;
import java.util.Optional;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CreateSchoolCommandHandlerTest {

    @Mock
    private SchoolRepository schoolRepository;

    @InjectMocks
    private CreateSchoolCommandHandler handler;

    @Test
    void handle_ShouldCreateSchool_WhenDataIsValidAndNameIsUnique() {
        CreateSchoolCommand command = new CreateSchoolCommand("Test School", "123 Main St", "555-1234");
        when(schoolRepository.findByName("Test School")).thenReturn(Optional.empty());
        when(schoolRepository.save(any(School.class))).thenAnswer(invocation -> invocation.getArgument(0));

        SchoolId result = handler.handle(command);

        assertNotNull(result);
        verify(schoolRepository).findByName("Test School");
        verify(schoolRepository).save(any(School.class));
    }

    @Test
    void handle_ShouldThrowDomainException_WhenSchoolNameAlreadyExists() {
        CreateSchoolCommand command = new CreateSchoolCommand("Existing School", "456 Oak St", "555-5678");
        School existingSchool = mock(School.class);
        when(schoolRepository.findByName("Existing School")).thenReturn(Optional.of(existingSchool));

        DomainException exception = assertThrows(DomainException.class, () -> handler.handle(command));

        assertTrue(exception.getMessage().contains("único") || exception.getMessage().contains("unique"));
        verify(schoolRepository).findByName("Existing School");
        verify(schoolRepository, never()).save(any(School.class));
    }

    @Test
    void handle_ShouldThrowDomainException_WhenSchoolNameIsEmpty() {
        CreateSchoolCommand command = new CreateSchoolCommand("", "789 Pine St", "555-9999");

        DomainException exception = assertThrows(DomainException.class, () -> handler.handle(command));

        assertTrue(exception.getMessage().contains("obligatorio") || exception.getMessage().contains("required"));
        verify(schoolRepository, never()).findByName(anyString());
        verify(schoolRepository, never()).save(any(School.class));
    }

    @Test
    void handle_ShouldThrowDomainException_WhenSchoolNameIsNull() {
        CreateSchoolCommand command = new CreateSchoolCommand(null, "789 Pine St", "555-9999");

        DomainException exception = assertThrows(DomainException.class, () -> handler.handle(command));

        assertTrue(exception.getMessage().contains("obligatorio") || exception.getMessage().contains("required"));
        verify(schoolRepository, never()).findByName(anyString());
        verify(schoolRepository, never()).save(any(School.class));
    }
}