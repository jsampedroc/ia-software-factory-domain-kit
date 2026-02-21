package com.application;

import com.application.application.SchoolManagement.student.application.create.CreateStudentCommand;
import com.application.application.SchoolManagement.student.application.create.CreateStudentCommandHandler;
import com.application.interfaces.rest.SchoolManagement.student.StudentController;
import com.application.interfaces.rest.SchoolManagement.student.dto.CreateStudentRequestDTO;
import com.application.interfaces.rest.SchoolManagement.student.dto.StudentResponseDTO;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.time.LocalDate;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
class StudentControllerTest {

    private MockMvc mockMvc;

    @Mock
    private CreateStudentCommandHandler createStudentCommandHandler;

    @InjectMocks
    private StudentController studentController;

    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(studentController).build();
        objectMapper = new ObjectMapper();
        objectMapper.findAndRegisterModules();
    }

    @Test
    void createStudent_ValidRequest_ReturnsCreatedStudent() throws Exception {
        UUID legalGuardianId = UUID.randomUUID();
        UUID classroomId = UUID.randomUUID();
        UUID studentId = UUID.randomUUID();
        LocalDate dob = LocalDate.of(2015, 5, 10);
        LocalDate enrollmentDate = LocalDate.now();

        CreateStudentRequestDTO requestDTO = new CreateStudentRequestDTO();
        requestDTO.setLegalGuardianId(legalGuardianId);
        requestDTO.setFirstName("Juan");
        requestDTO.setLastName("Pérez");
        requestDTO.setDateOfBirth(dob);
        requestDTO.setIdentificationNumber("ID123456");
        requestDTO.setEnrollmentDate(enrollmentDate);
        requestDTO.setCurrentClassroomId(classroomId);

        StudentResponseDTO expectedResponse = new StudentResponseDTO();
        expectedResponse.setId(studentId);
        expectedResponse.setLegalGuardianId(legalGuardianId);
        expectedResponse.setFirstName("Juan");
        expectedResponse.setLastName("Pérez");
        expectedResponse.setDateOfBirth(dob);
        expectedResponse.setIdentificationNumber("ID123456");
        expectedResponse.setEnrollmentDate(enrollmentDate);
        expectedResponse.setActive(true);
        expectedResponse.setCurrentClassroomId(classroomId);

        when(createStudentCommandHandler.handle(any(CreateStudentCommand.class))).thenReturn(expectedResponse);

        mockMvc.perform(post("/api/students")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDTO)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(studentId.toString()))
                .andExpect(jsonPath("$.legalGuardianId").value(legalGuardianId.toString()))
                .andExpect(jsonPath("$.firstName").value("Juan"))
                .andExpect(jsonPath("$.lastName").value("Pérez"))
                .andExpect(jsonPath("$.identificationNumber").value("ID123456"))
                .andExpect(jsonPath("$.active").value(true))
                .andExpect(jsonPath("$.currentClassroomId").value(classroomId.toString()));
    }

    @Test
    void createStudent_InvalidRequest_ReturnsBadRequest() throws Exception {
        CreateStudentRequestDTO requestDTO = new CreateStudentRequestDTO();
        requestDTO.setFirstName(""); // Invalid empty first name
        requestDTO.setLastName("Pérez");
        requestDTO.setDateOfBirth(LocalDate.of(2015, 5, 10));
        requestDTO.setIdentificationNumber("ID123456");
        requestDTO.setEnrollmentDate(LocalDate.now());

        mockMvc.perform(post("/api/students")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDTO)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void createStudent_HandlerThrowsDomainException_ReturnsBadRequest() throws Exception {
        UUID legalGuardianId = UUID.randomUUID();
        UUID classroomId = UUID.randomUUID();
        LocalDate dob = LocalDate.of(2015, 5, 10);
        LocalDate enrollmentDate = LocalDate.now();

        CreateStudentRequestDTO requestDTO = new CreateStudentRequestDTO();
        requestDTO.setLegalGuardianId(legalGuardianId);
        requestDTO.setFirstName("Juan");
        requestDTO.setLastName("Pérez");
        requestDTO.setDateOfBirth(dob);
        requestDTO.setIdentificationNumber("ID123456");
        requestDTO.setEnrollmentDate(enrollmentDate);
        requestDTO.setCurrentClassroomId(classroomId);

        when(createStudentCommandHandler.handle(any(CreateStudentCommand.class)))
                .thenThrow(new com.application.domain.exception.DomainException("Identification number already exists"));

        mockMvc.perform(post("/api/students")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDTO)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("Identification number already exists"));
    }
}