package com.application.interfaceadapter.controller;

import com.application.application.dto.AppointmentDTO;
import com.application.application.service.AppointmentService;
import com.application.domain.valueobject.AppointmentId;
import com.application.domain.valueobject.AppointmentStatus;
import com.application.domain.valueobject.DentistId;
import com.application.domain.valueobject.PatientId;
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
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AppointmentControllerTest {

    @Mock
    private AppointmentService appointmentService;

    @InjectMocks
    private AppointmentController appointmentController;

    private UUID testUuid;
    private AppointmentId testAppointmentId;
    private PatientId testPatientId;
    private DentistId testDentistId;
    private AppointmentDTO testAppointmentDTO;
    private List<AppointmentDTO> testAppointmentList;

    @BeforeEach
    void setUp() {
        testUuid = UUID.randomUUID();
        testAppointmentId = new AppointmentId(testUuid);
        testPatientId = new PatientId(UUID.randomUUID());
        testDentistId = new DentistId(UUID.randomUUID());

        testAppointmentDTO = new AppointmentDTO(
                testUuid,
                LocalDateTime.now().plusDays(1),
                60,
                AppointmentStatus.PROGRAMADA,
                "Limpieza dental",
                "Notas de prueba",
                testPatientId.value(),
                testDentistId.value(),
                null,
                null
        );

        testAppointmentList = List.of(testAppointmentDTO);
    }

    @Test
    void getById_shouldReturnAppointment_whenExists() {
        when(appointmentService.findById(testAppointmentId)).thenReturn(Optional.of(testAppointmentDTO));

        ResponseEntity<AppointmentDTO> response = appointmentController.getById(testUuid);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(testAppointmentDTO.id(), response.getBody().id());
        verify(appointmentService, times(1)).findById(testAppointmentId);
    }

    @Test
    void getById_shouldReturnNotFound_whenNotExists() {
        when(appointmentService.findById(testAppointmentId)).thenReturn(Optional.empty());

        ResponseEntity<AppointmentDTO> response = appointmentController.getById(testUuid);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertNull(response.getBody());
        verify(appointmentService, times(1)).findById(testAppointmentId);
    }

    @Test
    void getAll_shouldReturnListOfAppointments() {
        when(appointmentService.findAll()).thenReturn(testAppointmentList);

        ResponseEntity<List<AppointmentDTO>> response = appointmentController.getAll();

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(1, response.getBody().size());
        verify(appointmentService, times(1)).findAll();
    }

    @Test
    void create_shouldReturnCreatedAppointment() {
        when(appointmentService.create(any(AppointmentDTO.class))).thenReturn(testAppointmentDTO);

        ResponseEntity<AppointmentDTO> response = appointmentController.create(testAppointmentDTO);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(testAppointmentDTO.id(), response.getBody().id());
        verify(appointmentService, times(1)).create(testAppointmentDTO);
    }

    @Test
    void update_shouldReturnUpdatedAppointment() {
        when(appointmentService.update(eq(testAppointmentId), any(AppointmentDTO.class))).thenReturn(testAppointmentDTO);

        ResponseEntity<AppointmentDTO> response = appointmentController.update(testUuid, testAppointmentDTO);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        verify(appointmentService, times(1)).update(testAppointmentId, testAppointmentDTO);
    }

    @Test
    void delete_shouldReturnNoContent() {
        doNothing().when(appointmentService).delete(testAppointmentId);

        ResponseEntity<Void> response = appointmentController.delete(testUuid);

        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
        assertNull(response.getBody());
        verify(appointmentService, times(1)).delete(testAppointmentId);
    }

    @Test
    void getByPatient_shouldReturnAppointmentsForPatient() {
        when(appointmentService.findByPatient(testPatientId)).thenReturn(testAppointmentList);

        ResponseEntity<List<AppointmentDTO>> response = appointmentController.getByPatient(testPatientId.value());

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(1, response.getBody().size());
        verify(appointmentService, times(1)).findByPatient(testPatientId);
    }

    @Test
    void getByDentist_shouldReturnAppointmentsForDentist() {
        when(appointmentService.findByDentist(testDentistId)).thenReturn(testAppointmentList);

        ResponseEntity<List<AppointmentDTO>> response = appointmentController.getByDentist(testDentistId.value());

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(1, response.getBody().size());
        verify(appointmentService, times(1)).findByDentist(testDentistId);
    }

    @Test
    void getByStatus_shouldReturnAppointmentsByStatus() {
        AppointmentStatus status = AppointmentStatus.CONFIRMADA;
        when(appointmentService.findByStatus(status)).thenReturn(testAppointmentList);

        ResponseEntity<List<AppointmentDTO>> response = appointmentController.getByStatus(status);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        verify(appointmentService, times(1)).findByStatus(status);
    }

    @Test
    void getByDateRange_shouldReturnAppointmentsInRange() {
        LocalDateTime start = LocalDateTime.now();
        LocalDateTime end = start.plusDays(7);
        when(appointmentService.findByDateRange(start, end)).thenReturn(testAppointmentList);

        ResponseEntity<List<AppointmentDTO>> response = appointmentController.getByDateRange(start, end);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        verify(appointmentService, times(1)).findByDateRange(start, end);
    }

    @Test
    void getByDate_shouldReturnAppointmentsForDate() {
        LocalDate date = LocalDate.now();
        when(appointmentService.findByDate(date)).thenReturn(testAppointmentList);

        ResponseEntity<List<AppointmentDTO>> response = appointmentController.getByDate(date);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        verify(appointmentService, times(1)).findByDate(date);
    }

    @Test
    void updateStatus_shouldReturnUpdatedAppointment() {
        AppointmentStatus newStatus = AppointmentStatus.EN_CURSO;
        when(appointmentService.updateStatus(testAppointmentId, newStatus)).thenReturn(testAppointmentDTO);

        ResponseEntity<AppointmentDTO> response = appointmentController.updateStatus(testUuid, newStatus);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        verify(appointmentService, times(1)).updateStatus(testAppointmentId, newStatus);
    }

    @Test
    void cancel_shouldReturnCancelledAppointment() {
        String reason = "Paciente enfermo";
        when(appointmentService.cancel(testAppointmentId, reason)).thenReturn(testAppointmentDTO);

        ResponseEntity<AppointmentDTO> response = appointmentController.cancel(testUuid, reason);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        verify(appointmentService, times(1)).cancel(testAppointmentId, reason);
    }

    @Test
    void cancel_shouldReturnCancelledAppointmentWithoutReason() {
        when(appointmentService.cancel(testAppointmentId, null)).thenReturn(testAppointmentDTO);

        ResponseEntity<AppointmentDTO> response = appointmentController.cancel(testUuid, null);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        verify(appointmentService, times(1)).cancel(testAppointmentId, null);
    }

    @Test
    void confirm_shouldReturnConfirmedAppointment() {
        when(appointmentService.confirm(testAppointmentId)).thenReturn(testAppointmentDTO);

        ResponseEntity<AppointmentDTO> response = appointmentController.confirm(testUuid);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        verify(appointmentService, times(1)).confirm(testAppointmentId);
    }

    @Test
    void start_shouldReturnStartedAppointment() {
        when(appointmentService.start(testAppointmentId)).thenReturn(testAppointmentDTO);

        ResponseEntity<AppointmentDTO> response = appointmentController.start(testUuid);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        verify(appointmentService, times(1)).start(testAppointmentId);
    }

    @Test
    void complete_shouldReturnCompletedAppointment() {
        when(appointmentService.complete(testAppointmentId)).thenReturn(testAppointmentDTO);

        ResponseEntity<AppointmentDTO> response = appointmentController.complete(testUuid);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        verify(appointmentService, times(1)).complete(testAppointmentId);
    }

    @Test
    void countPendingByPatient_shouldReturnCount() {
        long expectedCount = 2L;
        when(appointmentService.countPendingByPatient(testPatientId)).thenReturn(expectedCount);

        ResponseEntity<Long> response = appointmentController.countPendingByPatient(testPatientId.value());

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(expectedCount, response.getBody());
        verify(appointmentService, times(1)).countPendingByPatient(testPatientId);
    }

    @Test
    void findOverlappingForDentist_shouldReturnOverlappingAppointments() {
        LocalDateTime start = LocalDateTime.now();
        LocalDateTime end = start.plusHours(2);
        when(appointmentService.findOverlappingForDentist(testDentistId, start, end)).thenReturn(testAppointmentList);

        ResponseEntity<List<AppointmentDTO>> response = appointmentController.findOverlappingForDentist(testDentistId.value(), start, end);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(1, response.getBody().size());
        verify(appointmentService, times(1)).findOverlappingForDentist(testDentistId, start, end);
    }
}