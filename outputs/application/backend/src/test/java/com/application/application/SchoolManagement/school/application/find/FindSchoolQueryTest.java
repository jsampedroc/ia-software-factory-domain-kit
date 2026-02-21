package com.application.application.SchoolManagement.school.application.find;

import com.application.domain.SchoolManagement.school.domain.School;
import com.application.domain.SchoolManagement.school.domain.repository.SchoolRepository;
import com.application.domain.SchoolManagement.valueobject.SchoolId;
import com.application.domain.exception.DomainException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class FindSchoolQueryHandlerTest {

    @Mock
    private SchoolRepository schoolRepository;

    @InjectMocks
    private FindSchoolQueryHandler handler;

    @Test
    void handle_WhenSchoolExists_ShouldReturnSchool() {
        SchoolId schoolId = new SchoolId("550e8400-e29b-41d4-a716-446655440000");
        School expectedSchool = mock(School.class);
        when(expectedSchool.getId()).thenReturn(schoolId);
        when(schoolRepository.findById(any(SchoolId.class))).thenReturn(Optional.of(expectedSchool));

        FindSchoolQuery query = new FindSchoolQuery(schoolId);
        School result = handler.handle(query);

        assertNotNull(result);
        assertEquals(schoolId, result.getId());
        verify(schoolRepository, times(1)).findById(schoolId);
    }

    @Test
    void handle_WhenSchoolDoesNotExist_ShouldThrowDomainException() {
        SchoolId schoolId = new SchoolId("550e8400-e29b-41d4-a716-446655440000");
        when(schoolRepository.findById(any(SchoolId.class))).thenReturn(Optional.empty());

        FindSchoolQuery query = new FindSchoolQuery(schoolId);

        DomainException exception = assertThrows(DomainException.class, () -> handler.handle(query));
        assertTrue(exception.getMessage().contains("School not found"));
        verify(schoolRepository, times(1)).findById(schoolId);
    }

    @Test
    void handle_WhenQueryIsNull_ShouldThrowNullPointerException() {
        assertThrows(NullPointerException.class, () -> handler.handle(null));
        verify(schoolRepository, never()).findById(any());
    }
}