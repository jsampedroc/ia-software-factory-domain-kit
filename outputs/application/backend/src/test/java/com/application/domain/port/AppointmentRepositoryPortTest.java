package com.application.domain.port;

import com.application.domain.model.Appointment;
import com.application.domain.valueobject.AppointmentId;
import com.application.domain.valueobject.DentistId;
import com.application.domain.valueobject.PatientId;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AppointmentRepositoryPortTest {

    @Mock
    private AppointmentRepositoryPort appointmentRepositoryPort;

    @Test
    void testFindByPatientId() {
        PatientId patientId = new PatientId(UUID.randomUUID());
        List<Appointment> expectedAppointments = List.of(mock(Appointment.class));

        when(appointmentRepositoryPort.findByPatientId(patientId)).thenReturn(expectedAppointments);

        List<Appointment> result = appointmentRepositoryPort.findByPatientId(patientId);

        assertNotNull(result);
        assertEquals(expectedAppointments, result);
        verify(appointmentRepositoryPort).findByPatientId(patientId);
    }

    @Test
    void testFindByDentistId() {
        DentistId dentistId = new DentistId(UUID.randomUUID());
        List<Appointment> expectedAppointments = List.of(mock(Appointment.class));

        when(appointmentRepositoryPort.findByDentistId(dentistId)).thenReturn(expectedAppointments);

        List<Appointment> result = appointmentRepositoryPort.findByDentistId(dentistId);

        assertNotNull(result);
        assertEquals(expectedAppointments, result);
        verify(appointmentRepositoryPort).findByDentistId(dentistId);
    }

    @Test
    void testFindByStatus() {
        String status = "PROGRAMADA";
        List<Appointment> expectedAppointments = List.of(mock(Appointment.class));

        when(appointmentRepositoryPort.findByStatus(status)).thenReturn(expectedAppointments);

        List<Appointment> result = appointmentRepositoryPort.findByStatus(status);

        assertNotNull(result);
        assertEquals(expectedAppointments, result);
        verify(appointmentRepositoryPort).findByStatus(status);
    }

    @Test
    void testFindByDateRange() {
        LocalDateTime startDate = LocalDateTime.now().minusDays(1);
        LocalDateTime endDate = LocalDateTime.now().plusDays(1);
        List<Appointment> expectedAppointments = List.of(mock(Appointment.class));

        when(appointmentRepositoryPort.findByDateRange(startDate, endDate)).thenReturn(expectedAppointments);

        List<Appointment> result = appointmentRepositoryPort.findByDateRange(startDate, endDate);

        assertNotNull(result);
        assertEquals(expectedAppointments, result);
        verify(appointmentRepositoryPort).findByDateRange(startDate, endDate);
    }

    @Test
    void testFindByPatientIdAndDateRange() {
        PatientId patientId = new PatientId(UUID.randomUUID());
        LocalDateTime startDate = LocalDateTime.now().minusDays(1);
        LocalDateTime endDate = LocalDateTime.now().plusDays(1);
        List<Appointment> expectedAppointments = List.of(mock(Appointment.class));

        when(appointmentRepositoryPort.findByPatientIdAndDateRange(patientId, startDate, endDate)).thenReturn(expectedAppointments);

        List<Appointment> result = appointmentRepositoryPort.findByPatientIdAndDateRange(patientId, startDate, endDate);

        assertNotNull(result);
        assertEquals(expectedAppointments, result);
        verify(appointmentRepositoryPort).findByPatientIdAndDateRange(patientId, startDate, endDate);
    }

    @Test
    void testFindByDentistIdAndDateRange() {
        DentistId dentistId = new DentistId(UUID.randomUUID());
        LocalDateTime startDate = LocalDateTime.now().minusDays(1);
        LocalDateTime endDate = LocalDateTime.now().plusDays(1);
        List<Appointment> expectedAppointments = List.of(mock(Appointment.class));

        when(appointmentRepositoryPort.findByDentistIdAndDateRange(dentistId, startDate, endDate)).thenReturn(expectedAppointments);

        List<Appointment> result = appointmentRepositoryPort.findByDentistIdAndDateRange(dentistId, startDate, endDate);

        assertNotNull(result);
        assertEquals(expectedAppointments, result);
        verify(appointmentRepositoryPort).findByDentistIdAndDateRange(dentistId, startDate, endDate);
    }

    @Test
    void testFindOverlappingAppointment_WhenExists() {
        DentistId dentistId = new DentistId(UUID.randomUUID());
        LocalDateTime startTime = LocalDateTime.now().plusHours(1);
        LocalDateTime endTime = startTime.plusMinutes(30);
        Appointment overlappingAppointment = mock(Appointment.class);

        when(appointmentRepositoryPort.findOverlappingAppointment(dentistId, startTime, endTime))
                .thenReturn(Optional.of(overlappingAppointment));

        Optional<Appointment> result = appointmentRepositoryPort.findOverlappingAppointment(dentistId, startTime, endTime);

        assertTrue(result.isPresent());
        assertEquals(overlappingAppointment, result.get());
        verify(appointmentRepositoryPort).findOverlappingAppointment(dentistId, startTime, endTime);
    }

    @Test
    void testFindOverlappingAppointment_WhenNotExists() {
        DentistId dentistId = new DentistId(UUID.randomUUID());
        LocalDateTime startTime = LocalDateTime.now().plusHours(1);
        LocalDateTime endTime = startTime.plusMinutes(30);

        when(appointmentRepositoryPort.findOverlappingAppointment(dentistId, startTime, endTime))
                .thenReturn(Optional.empty());

        Optional<Appointment> result = appointmentRepositoryPort.findOverlappingAppointment(dentistId, startTime, endTime);

        assertFalse(result.isPresent());
        verify(appointmentRepositoryPort).findOverlappingAppointment(dentistId, startTime, endTime);
    }

    @Test
    void testExistsByPatientIdAndStatusIn_WhenExists() {
        PatientId patientId = new PatientId(UUID.randomUUID());
        List<String> statuses = List.of("PROGRAMADA", "CONFIRMADA");

        when(appointmentRepositoryPort.existsByPatientIdAndStatusIn(patientId, statuses)).thenReturn(true);

        boolean result = appointmentRepositoryPort.existsByPatientIdAndStatusIn(patientId, statuses);

        assertTrue(result);
        verify(appointmentRepositoryPort).existsByPatientIdAndStatusIn(patientId, statuses);
    }

    @Test
    void testExistsByPatientIdAndStatusIn_WhenNotExists() {
        PatientId patientId = new PatientId(UUID.randomUUID());
        List<String> statuses = List.of("PROGRAMADA", "CONFIRMADA");

        when(appointmentRepositoryPort.existsByPatientIdAndStatusIn(patientId, statuses)).thenReturn(false);

        boolean result = appointmentRepositoryPort.existsByPatientIdAndStatusIn(patientId, statuses);

        assertFalse(result);
        verify(appointmentRepositoryPort).existsByPatientIdAndStatusIn(patientId, statuses);
    }

    @Test
    void testCountByPatientIdAndStatus() {
        PatientId patientId = new PatientId(UUID.randomUUID());
        String status = "PROGRAMADA";
        long expectedCount = 3L;

        when(appointmentRepositoryPort.countByPatientIdAndStatus(patientId, status)).thenReturn(expectedCount);

        long result = appointmentRepositoryPort.countByPatientIdAndStatus(patientId, status);

        assertEquals(expectedCount, result);
        verify(appointmentRepositoryPort).countByPatientIdAndStatus(patientId, status);
    }
}