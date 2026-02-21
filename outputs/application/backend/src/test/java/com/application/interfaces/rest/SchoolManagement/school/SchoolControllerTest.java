package com.application;

import com.application.application.SchoolManagement.school.application.create.CreateSchoolCommand;
import com.application.application.SchoolManagement.school.application.create.CreateSchoolCommandHandler;
import com.application.application.SchoolManagement.school.application.find.FindSchoolQuery;
import com.application.application.SchoolManagement.school.application.find.FindSchoolQueryHandler;
import com.application.interfaces.rest.SchoolManagement.school.SchoolController;
import com.application.interfaces.rest.SchoolManagement.school.dto.CreateSchoolRequestDTO;
import com.application.interfaces.rest.SchoolManagement.school.dto.SchoolResponseDTO;
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

import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
class SchoolControllerTest {

    private MockMvc mockMvc;

    @Mock
    private CreateSchoolCommandHandler createSchoolCommandHandler;

    @Mock
    private FindSchoolQueryHandler findSchoolQueryHandler;

    @InjectMocks
    private SchoolController schoolController;

    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        objectMapper = new ObjectMapper();
        mockMvc = MockMvcBuilders.standaloneSetup(schoolController).build();
    }

    @Test
    void createSchool_ValidRequest_ReturnsCreated() throws Exception {
        UUID schoolId = UUID.randomUUID();
        CreateSchoolRequestDTO requestDTO = new CreateSchoolRequestDTO();
        requestDTO.setName("Test School");
        requestDTO.setAddress("123 Main St");
        requestDTO.setPhoneNumber("555-1234");

        SchoolResponseDTO responseDTO = new SchoolResponseDTO();
        responseDTO.setId(schoolId);
        responseDTO.setName("Test School");
        responseDTO.setAddress("123 Main St");
        responseDTO.setPhoneNumber("555-1234");
        responseDTO.setActive(true);

        when(createSchoolCommandHandler.handle(any(CreateSchoolCommand.class))).thenReturn(responseDTO);

        mockMvc.perform(post("/api/schools")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDTO)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(schoolId.toString()))
                .andExpect(jsonPath("$.name").value("Test School"))
                .andExpect(jsonPath("$.address").value("123 Main St"))
                .andExpect(jsonPath("$.phoneNumber").value("555-1234"))
                .andExpect(jsonPath("$.active").value(true));
    }

    @Test
    void createSchool_InvalidRequest_ReturnsBadRequest() throws Exception {
        CreateSchoolRequestDTO requestDTO = new CreateSchoolRequestDTO();
        requestDTO.setName("");

        mockMvc.perform(post("/api/schools")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDTO)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void getSchoolById_ExistingId_ReturnsOk() throws Exception {
        UUID schoolId = UUID.randomUUID();
        SchoolResponseDTO responseDTO = new SchoolResponseDTO();
        responseDTO.setId(schoolId);
        responseDTO.setName("Test School");
        responseDTO.setAddress("123 Main St");
        responseDTO.setPhoneNumber("555-1234");
        responseDTO.setActive(true);

        when(findSchoolQueryHandler.handle(any(FindSchoolQuery.class))).thenReturn(responseDTO);

        mockMvc.perform(get("/api/schools/{id}", schoolId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(schoolId.toString()))
                .andExpect(jsonPath("$.name").value("Test School"))
                .andExpect(jsonPath("$.address").value("123 Main St"))
                .andExpect(jsonPath("$.phoneNumber").value("555-1234"))
                .andExpect(jsonPath("$.active").value(true));
    }

    @Test
    void getSchoolById_NonExistingId_ReturnsNotFound() throws Exception {
        UUID schoolId = UUID.randomUUID();
        when(findSchoolQueryHandler.handle(any(FindSchoolQuery.class))).thenReturn(null);

        mockMvc.perform(get("/api/schools/{id}", schoolId))
                .andExpect(status().isNotFound());
    }
}