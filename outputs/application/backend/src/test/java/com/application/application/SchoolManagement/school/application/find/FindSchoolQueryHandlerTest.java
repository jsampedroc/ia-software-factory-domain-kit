package com.application;

import com.application.application.SchoolManagement.school.application.find.FindSchoolQuery;
import com.application.application.SchoolManagement.school.application.find.FindSchoolQueryHandler;
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
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class FindSchoolQueryHandlerTest {

    @Mock
    private SchoolRepository schoolRepository;

    private FindSchoolQueryHandler handler;

    @BeforeEach
    void setUp() {
        handler = new FindSchoolQueryHandler(schoolRepository);
    }

    @Test
    void handle_ShouldReturnSchool_WhenSchoolExists() {
        SchoolId schoolId = new SchoolId("550e8400-e29b-41d4-a716-446655440000");
        School expectedSchool = School.builder()
                .id(schoolId)
                .name("Test School")
                .address("123 Main St")
                .phoneNumber("555-1234")
                .active(true)
                .build();

        FindSchoolQuery query = new FindSchoolQuery(schoolId.getValue().toString());

        when(schoolRepository.findById(any(SchoolId.class))).thenReturn(Optional.of(expectedSchool));

        School result = handler.handle(query);

        assertNotNull(result);
        assertEquals(expectedSchool.getId(), result.getId());
        assertEquals(expectedSchool.getName(), result.getName());
        assertEquals(expectedSchool.getAddress(), result.getAddress());
        assertEquals(expectedSchool.getPhoneNumber(), result.getPhoneNumber());
        assertEquals(expectedSchool.isActive(), result.isActive());
    }

    @Test
    void handle_ShouldThrowDomainException_WhenSchoolNotFound() {
        SchoolId schoolId = new SchoolId("550e8400-e29b-41d4-a716-446655440000");
        FindSchoolQuery query = new FindSchoolQuery(schoolId.getValue().toString());

        when(schoolRepository.findById(any(SchoolId.class))).thenReturn(Optional.empty());

        DomainException exception = assertThrows(DomainException.class, () -> handler.handle(query));
        assertTrue(exception.getMessage().contains("School not found"));
    }

    @Test
    void handle_ShouldThrowDomainException_WhenIdIsInvalid() {
        FindSchoolQuery query = new FindSchoolQuery("invalid-uuid");

        DomainException exception = assertThrows(DomainException.class, () -> handler.handle(query));
        assertTrue(exception.getMessage().contains("Invalid school ID"));
    }
}