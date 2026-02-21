package com.application;

import com.application.application.SchoolManagement.classroom.application.create.CreateClassroomCommand;
import com.application.application.SchoolManagement.classroom.application.create.CreateClassroomCommandHandler;
import com.application.interfaces.rest.SchoolManagement.classroom.ClassroomController;
import com.application.interfaces.rest.SchoolManagement.classroom.dto.CreateClassroomRequestDTO;
import com.application.interfaces.rest.SchoolManagement.classroom.dto.ClassroomResponseDTO;
import com.application.domain.SchoolManagement.valueobject.ClassroomId;
import com.application.domain.SchoolManagement.valueobject.SchoolId;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ClassroomControllerTest {

    @Mock
    private CreateClassroomCommandHandler createClassroomCommandHandler;

    @InjectMocks
    private ClassroomController classroomController;

    private CreateClassroomRequestDTO validRequestDTO;
    private ClassroomId mockClassroomId;
    private SchoolId mockSchoolId;

    @BeforeEach
    void setUp() {
        mockClassroomId = new ClassroomId(UUID.randomUUID());
        mockSchoolId = new SchoolId(UUID.randomUUID());

        validRequestDTO = new CreateClassroomRequestDTO();
        validRequestDTO.setGradeLevel("5");
        validRequestDTO.setSection("A");
        validRequestDTO.setAcademicYear("2024-2025");
        validRequestDTO.setCapacity(30);
        validRequestDTO.setTutorTeacherId(UUID.randomUUID());
        validRequestDTO.setSchoolId(mockSchoolId.getValue());
        validRequestDTO.setActive(true);
    }

    @Test
    void createClassroom_WithValidRequest_ShouldReturnCreatedResponse() {
        when(createClassroomCommandHandler.handle(any(CreateClassroomCommand.class))).thenReturn(mockClassroomId);

        ResponseEntity<ClassroomResponseDTO> response = classroomController.createClassroom(validRequestDTO);

        assertNotNull(response);
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(mockClassroomId.getValue(), response.getBody().getId());
        verify(createClassroomCommandHandler, times(1)).handle(any(CreateClassroomCommand.class));
    }

    @Test
    void createClassroom_ShouldMapRequestToCommandCorrectly() {
        when(createClassroomCommandHandler.handle(any(CreateClassroomCommand.class))).thenReturn(mockClassroomId);

        classroomController.createClassroom(validRequestDTO);

        verify(createClassroomCommandHandler).handle(argThat(command ->
                command.gradeLevel().equals(validRequestDTO.getGradeLevel()) &&
                        command.section().equals(validRequestDTO.getSection()) &&
                        command.academicYear().equals(validRequestDTO.getAcademicYear()) &&
                        command.capacity() == validRequestDTO.getCapacity() &&
                        command.tutorTeacherId().equals(validRequestDTO.getTutorTeacherId()) &&
                        command.schoolId().getValue().equals(validRequestDTO.getSchoolId()) &&
                        command.active() == validRequestDTO.isActive()
        ));
    }

    @Test
    void createClassroom_ShouldReturnResponseWithCorrectData() {
        when(createClassroomCommandHandler.handle(any(CreateClassroomCommand.class))).thenReturn(mockClassroomId);

        ResponseEntity<ClassroomResponseDTO> response = classroomController.createClassroom(validRequestDTO);
        ClassroomResponseDTO responseBody = response.getBody();

        assertNotNull(responseBody);
        assertEquals(mockClassroomId.getValue(), responseBody.getId());
        assertEquals(validRequestDTO.getGradeLevel(), responseBody.getGradeLevel());
        assertEquals(validRequestDTO.getSection(), responseBody.getSection());
        assertEquals(validRequestDTO.getAcademicYear(), responseBody.getAcademicYear());
        assertEquals(validRequestDTO.getCapacity(), responseBody.getCapacity());
        assertEquals(validRequestDTO.getTutorTeacherId(), responseBody.getTutorTeacherId());
        assertEquals(validRequestDTO.getSchoolId(), responseBody.getSchoolId());
        assertEquals(validRequestDTO.isActive(), responseBody.isActive());
    }
}