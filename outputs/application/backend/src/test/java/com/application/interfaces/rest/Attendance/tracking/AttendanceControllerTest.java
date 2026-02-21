package com.application;

import com.application.application.Attendance.tracking.application.record.CreateAttendanceRecordCommand;
import com.application.application.Attendance.tracking.application.record.CreateAttendanceRecordCommandHandler;
import com.application.interfaces.rest.Attendance.tracking.AttendanceController;
import com.application.interfaces.rest.Attendance.tracking.dto.CreateAttendanceRecordRequestDTO;
import com.application.interfaces.rest.Attendance.tracking.dto.AttendanceRecordResponseDTO;
import com.application.domain.Attendance.tracking.domain.AttendanceRecord;
import com.application.domain.Attendance.tracking.valueobject.AttendanceRecordId;
import com.application.domain.Attendance.tracking.valueobject.DailyAttendanceId;
import com.application.domain.SchoolManagement.valueobject.StudentId;
import com.application.domain.shared.valueobject.PersonName;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AttendanceControllerTest {

    @Mock
    private CreateAttendanceRecordCommandHandler createAttendanceRecordCommandHandler;

    @InjectMocks
    private AttendanceController attendanceController;

    private CreateAttendanceRecordRequestDTO validRequestDTO;
    private AttendanceRecord mockAttendanceRecord;
    private final UUID dailyAttendanceId = UUID.randomUUID();
    private final UUID studentId = UUID.randomUUID();
    private final UUID attendanceRecordId = UUID.randomUUID();

    @BeforeEach
    void setUp() {
        validRequestDTO = new CreateAttendanceRecordRequestDTO();
        validRequestDTO.setDailyAttendanceId(dailyAttendanceId);
        validRequestDTO.setStudentId(studentId);
        validRequestDTO.setStatus("PRESENT");
        validRequestDTO.setNotes("On time");

        mockAttendanceRecord = AttendanceRecord.builder()
                .id(new AttendanceRecordId(attendanceRecordId))
                .dailyAttendanceId(new DailyAttendanceId(dailyAttendanceId))
                .studentId(new StudentId(studentId))
                .status(AttendanceRecord.AttendanceStatus.PRESENT)
                .notes("On time")
                .recordedAt(LocalDateTime.now())
                .build();
    }

    @Test
    void createAttendanceRecord_WithValidRequest_ShouldReturnCreatedResponse() {
        when(createAttendanceRecordCommandHandler.handle(any(CreateAttendanceRecordCommand.class)))
                .thenReturn(mockAttendanceRecord);

        ResponseEntity<AttendanceRecordResponseDTO> response = attendanceController.createAttendanceRecord(validRequestDTO);

        assertNotNull(response);
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertNotNull(response.getBody());

        AttendanceRecordResponseDTO responseBody = response.getBody();
        assertEquals(attendanceRecordId, responseBody.getId());
        assertEquals(dailyAttendanceId, responseBody.getDailyAttendanceId());
        assertEquals(studentId, responseBody.getStudentId());
        assertEquals("PRESENT", responseBody.getStatus());
        assertEquals("On time", responseBody.getNotes());
        assertNotNull(responseBody.getRecordedAt());

        verify(createAttendanceRecordCommandHandler, times(1)).handle(any(CreateAttendanceRecordCommand.class));
    }

    @Test
    void createAttendanceRecord_WhenHandlerThrowsDomainException_ShouldReturnBadRequest() {
        when(createAttendanceRecordCommandHandler.handle(any(CreateAttendanceRecordCommand.class)))
                .thenThrow(new com.application.domain.exception.DomainException("Invalid status"));

        ResponseEntity<AttendanceRecordResponseDTO> response = attendanceController.createAttendanceRecord(validRequestDTO);

        assertNotNull(response);
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertNull(response.getBody());

        verify(createAttendanceRecordCommandHandler, times(1)).handle(any(CreateAttendanceRecordCommand.class));
    }

    @Test
    void createAttendanceRecord_WhenHandlerThrowsRuntimeException_ShouldReturnInternalServerError() {
        when(createAttendanceRecordCommandHandler.handle(any(CreateAttendanceRecordCommand.class)))
                .thenThrow(new RuntimeException("Database error"));

        ResponseEntity<AttendanceRecordResponseDTO> response = attendanceController.createAttendanceRecord(validRequestDTO);

        assertNotNull(response);
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        assertNull(response.getBody());

        verify(createAttendanceRecordCommandHandler, times(1)).handle(any(CreateAttendanceRecordCommand.class));
    }

    @Test
    void createAttendanceRecord_WithNullRequest_ShouldReturnBadRequest() {
        ResponseEntity<AttendanceRecordResponseDTO> response = attendanceController.createAttendanceRecord(null);

        assertNotNull(response);
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertNull(response.getBody());

        verify(createAttendanceRecordCommandHandler, never()).handle(any());
    }

    @Test
    void createAttendanceRecord_WithInvalidStatus_ShouldReturnBadRequest() {
        validRequestDTO.setStatus("INVALID_STATUS");

        when(createAttendanceRecordCommandHandler.handle(any(CreateAttendanceRecordCommand.class)))
                .thenThrow(new com.application.domain.exception.DomainException("Invalid attendance status"));

        ResponseEntity<AttendanceRecordResponseDTO> response = attendanceController.createAttendanceRecord(validRequestDTO);

        assertNotNull(response);
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertNull(response.getBody());

        verify(createAttendanceRecordCommandHandler, times(1)).handle(any(CreateAttendanceRecordCommand.class));
    }
}