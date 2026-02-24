package com.application.application.service;

import com.application.application.dto.AppointmentDTO;
import com.application.application.mapper.AppointmentMapper;
import com.application.domain.exception.DomainException;
import com.application.domain.model.Appointment;
import com.application.domain.model.Clinic;
import com.application.domain.model.Dentist;
import com.application.domain.model.Patient;
import com.application.domain.model.Treatment;
import com.application.domain.port.AppointmentRepositoryPort;
import com.application.domain.port.ClinicRepositoryPort;
import com.application.domain.port.DentistRepositoryPort;
import com.application.domain.port.PatientRepositoryPort;
import com.application.domain.port.TreatmentRepositoryPort;
import com.application.domain.valueobject.AppointmentId;
import com.application.domain.valueobject.AppointmentStatus;
import com.application.domain.valueobject.ClinicId;
import com.application.domain.valueobject.DentistId;
import com.application.domain.valueobject.PatientId;
import com.application.domain.valueobject.TreatmentId;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AppointmentServiceTest {

    @Mock
    private AppointmentRepositoryPort appointmentRepositoryPort;

    @Mock
    private PatientRepositoryPort patientRepositoryPort;

    @Mock
    private DentistRepositoryPort dentistRepositoryPort;

    @Mock
    private ClinicRepositoryPort clinicRepositoryPort;

    @Mock
    private TreatmentRepositoryPort treatmentRepositoryPort;

    @Mock
    private AppointmentMapper appointmentMapper;

    @InjectMocks
    private AppointmentService appointmentService;

    private AppointmentId appointmentId;
    private PatientId patientId;
    private DentistId dentistId;
    private ClinicId clinicId;
    private TreatmentId treatmentId;
    private AppointmentDTO appointmentDTO;
    private Appointment appointment;
    private Patient patient;
    private Dentist dentist;
    private Clinic clinic;
    private Treatment treatment;
    private LocalDateTime futureDateTime;

    @BeforeEach
    void setUp() {
        appointmentId = new AppointmentId(UUID.randomUUID());
        patientId = new PatientId(UUID.randomUUID());
        dentistId = new DentistId(UUID.randomUUID());
        clinicId = new ClinicId(UUID.randomUUID());
        treatmentId = new TreatmentId(UUID.randomUUID());

        futureDateTime = LocalDateTime.now().plusDays(2).withHour(10).withMinute(0);

        appointmentDTO = new AppointmentDTO();
        appointmentDTO.setFechaHora(futureDateTime);
        appointmentDTO.setDuracionMinutos(60);
        appointmentDTO.setPatientId(patientId);
        appointmentDTO.setDentistId(dentistId);
        appointmentDTO.setClinicId(clinicId);
        appointmentDTO.setMotivo("Consulta general");
        appointmentDTO.setNotas("Primera visita");
        appointmentDTO.setTreatmentIds(Set.of(treatmentId));

        appointment = Appointment.create(
                futureDateTime,
                60,
                AppointmentStatus.PROGRAMADA,
                "Consulta general",
                "Primera visita",
                patientId,
                dentistId,
                clinicId,
                new HashSet<>()
        );

        patient = Patient.create(
                "12345678A",
                "Juan",
                "Pérez",
                LocalDate.of(1990, 1, 1),
                "600123456",
                "juan@email.com",
                "Calle Falsa 123",
                LocalDateTime.now(),
                true
        );

        dentist = Dentist.create(
                "LM-12345",
                "Carlos",
                "Gómez",
                "600987654",
                "carlos@email.com",
                LocalDate.of(2020, 1, 1),
                true
        );

        clinic = Clinic.create(
                "CLI-001",
                "Clínica Central",
                "Av. Principal 456",
                "912345678",
                "clinica@email.com",
                LocalTime.of(8, 0),
                LocalTime.of(20, 0),
                true
        );

        treatment = Treatment.create(
                "TRAT-001",
                "Limpieza dental",
                "Limpieza profesional",
                45,
                new BigDecimal("80.00"),
                true
        );
    }

    @Test
    void findById_shouldReturnAppointmentDTO_whenAppointmentExists() {
        when(appointmentRepositoryPort.findById(appointmentId)).thenReturn(Optional.of(appointment));
        when(appointmentMapper.toDTO(appointment)).thenReturn(appointmentDTO);

        AppointmentDTO result = appointmentService.findById(appointmentId);

        assertNotNull(result);
        assertEquals(appointmentDTO, result);
        verify(appointmentRepositoryPort, times(1)).findById(appointmentId);
        verify(appointmentMapper, times(1)).toDTO(appointment);
    }

    @Test
    void findById_shouldThrowDomainException_whenAppointmentNotFound() {
        when(appointmentRepositoryPort.findById(appointmentId)).thenReturn(Optional.empty());

        DomainException exception = assertThrows(DomainException.class,
                () -> appointmentService.findById(appointmentId));

        assertEquals("Appointment not found with id: " + appointmentId, exception.getMessage());
        verify(appointmentRepositoryPort, times(1)).findById(appointmentId);
        verify(appointmentMapper, never()).toDTO(any());
    }

    @Test
    void findAll_shouldReturnListOfAppointmentDTOs() {
        List<Appointment> appointments = List.of(appointment);
        when(appointmentRepositoryPort.findAll()).thenReturn(appointments);
        when(appointmentMapper.toDTO(appointment)).thenReturn(appointmentDTO);

        List<AppointmentDTO> result = appointmentService.findAll();

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(appointmentDTO, result.get(0));
        verify(appointmentRepositoryPort, times(1)).findAll();
        verify(appointmentMapper, times(1)).toDTO(appointment);
    }

    @Test
    void create_shouldCreateAppointment_whenValidData() {
        when(patientRepositoryPort.findById(patientId)).thenReturn(Optional.of(patient));
        when(dentistRepositoryPort.findById(dentistId)).thenReturn(Optional.of(dentist));
        when(clinicRepositoryPort.findById(clinicId)).thenReturn(Optional.of(clinic));
        when(treatmentRepositoryPort.findById(treatmentId)).thenReturn(Optional.of(treatment));
        when(appointmentRepositoryPort.existsByDentistAndTimeRange(eq(dentistId), any(LocalDateTime.class), any(LocalDateTime.class))).thenReturn(false);
        when(appointmentRepositoryPort.findByPatientIdAndStatus(patientId, AppointmentStatus.PROGRAMADA)).thenReturn(Collections.emptyList());
        when(patient.hasOverdueInvoices(60)).thenReturn(false);
        when(dentist.isLicenseValid()).thenReturn(true);
        when(clinic.isActiva()).thenReturn(true);
        when(clinic.isWithinWorkingHours(any(LocalTime.class))).thenReturn(true);
        when(appointmentMapper.toDomain(appointmentDTO)).thenReturn(appointment);
        when(appointmentRepositoryPort.save(any(Appointment.class))).thenReturn(appointment);
        when(appointmentMapper.toDTO(appointment)).thenReturn(appointmentDTO);

        AppointmentDTO result = appointmentService.create(appointmentDTO);

        assertNotNull(result);
        assertEquals(appointmentDTO, result);
        verify(patientRepositoryPort, times(1)).findById(patientId);
        verify(dentistRepositoryPort, times(1)).findById(dentistId);
        verify(clinicRepositoryPort, times(1)).findById(clinicId);
        verify(treatmentRepositoryPort, times(1)).findById(treatmentId);
        verify(appointmentRepositoryPort, times(1)).existsByDentistAndTimeRange(eq(dentistId), any(LocalDateTime.class), any(LocalDateTime.class));
        verify(appointmentRepositoryPort, times(1)).findByPatientIdAndStatus(patientId, AppointmentStatus.PROGRAMADA);
        verify(appointmentRepositoryPort, times(1)).save(any(Appointment.class));
        verify(appointmentMapper, times(1)).toDTO(appointment);
    }

    @Test
    void create_shouldThrowDomainException_whenPatientNotFound() {
        when(patientRepositoryPort.findById(patientId)).thenReturn(Optional.empty());

        DomainException exception = assertThrows(DomainException.class,
                () -> appointmentService.create(appointmentDTO));

        assertEquals("Patient not found with id: " + patientId, exception.getMessage());
        verify(patientRepositoryPort, times(1)).findById(patientId);
        verify(appointmentRepositoryPort, never()).save(any());
    }

    @Test
    void create_shouldThrowDomainException_whenAppointmentLessThan24HoursInAdvance() {
        appointmentDTO.setFechaHora(LocalDateTime.now().plusHours(12));
        when(patientRepositoryPort.findById(patientId)).thenReturn(Optional.of(patient));
        when(dentistRepositoryPort.findById(dentistId)).thenReturn(Optional.of(dentist));
        when(clinicRepositoryPort.findById(clinicId)).thenReturn(Optional.of(clinic));

        DomainException exception = assertThrows(DomainException.class,
                () -> appointmentService.create(appointmentDTO));

        assertEquals("Appointment must be scheduled at least 24 hours in advance", exception.getMessage());
        verify(patientRepositoryPort, times(1)).findById(patientId);
        verify(appointmentRepositoryPort, never()).save(any());
    }

    @Test
    void create_shouldThrowDomainException_whenDentistHasOverlappingAppointment() {
        when(patientRepositoryPort.findById(patientId)).thenReturn(Optional.of(patient));
        when(dentistRepositoryPort.findById(dentistId)).thenReturn(Optional.of(dentist));
        when(clinicRepositoryPort.findById(clinicId)).thenReturn(Optional.of(clinic));
        when(appointmentRepositoryPort.existsByDentistAndTimeRange(eq(dentistId), any(LocalDateTime.class), any(LocalDateTime.class))).thenReturn(true);

        DomainException exception = assertThrows(DomainException.class,
                () -> appointmentService.create(appointmentDTO));

        assertEquals("Dentist already has an appointment scheduled for this time", exception.getMessage());
        verify(appointmentRepositoryPort, times(1)).existsByDentistAndTimeRange(eq(dentistId), any(LocalDateTime.class), any(LocalDateTime.class));
        verify(appointmentRepositoryPort, never()).save(any());
    }

    @Test
    void create_shouldThrowDomainException_whenPatientHasThreePendingAppointments() {
        List<Appointment> pendingAppointments = List.of(appointment, appointment, appointment);
        when(patientRepositoryPort.findById(patientId)).thenReturn(Optional.of(patient));
        when(dentistRepositoryPort.findById(dentistId)).thenReturn(Optional.of(dentist));
        when(clinicRepositoryPort.findById(clinicId)).thenReturn(Optional.of(clinic));
        when(appointmentRepositoryPort.existsByDentistAndTimeRange(eq(dentistId), any(LocalDateTime.class), any(LocalDateTime.class))).thenReturn(false);
        when(appointmentRepositoryPort.findByPatientIdAndStatus(patientId, AppointmentStatus.PROGRAMADA)).thenReturn(pendingAppointments);

        DomainException exception = assertThrows(DomainException.class,
                () -> appointmentService.create(appointmentDTO));

        assertEquals("Patient cannot have more than 3 pending appointments", exception.getMessage());
        verify(appointmentRepositoryPort, times(1)).findByPatientIdAndStatus(patientId, AppointmentStatus.PROGRAMADA);
        verify(appointmentRepositoryPort, never()).save(any());
    }

    @Test
    void update_shouldUpdateAppointment_whenValidDataAndProgramadaStatus() {
        appointment.setStatus(AppointmentStatus.PROGRAMADA);
        when(appointmentRepositoryPort.findById(appointmentId)).thenReturn(Optional.of(appointment));
        when(patientRepositoryPort.findById(patientId)).thenReturn(Optional.of(patient));
        when(dentistRepositoryPort.findById(dentistId)).thenReturn(Optional.of(dentist));
        when(clinicRepositoryPort.findById(clinicId)).thenReturn(Optional.of(clinic));
        when(treatmentRepositoryPort.findById(treatmentId)).thenReturn(Optional.of(treatment));
        when(appointmentRepositoryPort.existsByDentistAndTimeRange(eq(dentistId), any(LocalDateTime.class), any(LocalDateTime.class))).thenReturn(false);
        when(appointmentRepositoryPort.findByPatientIdAndStatus(patientId, AppointmentStatus.PROGRAMADA)).thenReturn(Collections.emptyList());
        when(patient.hasOverdueInvoices(60)).thenReturn(false);
        when(dentist.isLicenseValid()).thenReturn(true);
        when(clinic.isActiva()).thenReturn(true);
        when(clinic.isWithinWorkingHours(any(LocalTime.class))).thenReturn(true);
        when(appointmentMapper.toDomain(appointmentDTO)).thenReturn(appointment);
        when(appointmentRepositoryPort.save(any(Appointment.class))).thenReturn(appointment);
        when(appointmentMapper.toDTO(appointment)).thenReturn(appointmentDTO);

        AppointmentDTO result = appointmentService.update(appointmentId, appointmentDTO);

        assertNotNull(result);
        assertEquals(appointmentDTO, result);
        verify(appointmentRepositoryPort, times(1)).findById(appointmentId);
        verify(appointmentRepositoryPort, times(1)).save(any(Appointment.class));
    }

    @Test
    void update_shouldThrowDomainException_whenAppointmentStatusIsEnCurso() {
        appointment.setStatus(AppointmentStatus.EN_CURSO);
        when(appointmentRepositoryPort.findById(appointmentId)).thenReturn(Optional.of(appointment));

        DomainException exception = assertThrows(DomainException.class,
                () -> appointmentService.update(appointmentId, appointmentDTO));

        assertEquals("Cannot update appointment with status: " + AppointmentStatus.EN_CURSO, exception.getMessage());
        verify(appointmentRepositoryPort, times(1)).findById(appointmentId);
        verify(appointmentRepositoryPort, never()).save(any());
    }

    @Test
    void delete_shouldDeleteAppointment_whenStatusIsProgramada() {
        appointment.setStatus(AppointmentStatus.PROGRAMADA);
        when(appointmentRepositoryPort.findById(appointmentId)).thenReturn(Optional.of(appointment));
        doNothing().when(appointmentRepositoryPort).deleteById(appointmentId);

        appointmentService.delete(appointmentId);

        verify(appointmentRepositoryPort, times(1)).findById(appointmentId);
        verify(appointmentRepositoryPort, times(1)).deleteById(appointmentId);
    }

    @Test
    void delete_shouldThrowDomainException_whenStatusIsEnCurso() {
        appointment.setStatus(AppointmentStatus.EN_CURSO);
        when(appointmentRepositoryPort.findById(appointmentId)).thenReturn(Optional.of(appointment));

        DomainException exception = assertThrows(DomainException.class,
                () -> appointmentService.delete(appointmentId));

        assertEquals("Cannot delete appointment with status: " + AppointmentStatus.EN_CURSO, exception.getMessage());
        verify(appointmentRepositoryPort, times(1)).findById(appointmentId);
        verify(appointmentRepositoryPort, never()).deleteById(any());
    }

    @Test
    void updateStatus_shouldUpdateStatusToConfirmada_whenCurrentIsProgramada() {
        appointment.setStatus(AppointmentStatus.PROGRAMADA);
        when(appointmentRepositoryPort.findById(appointmentId)).thenReturn(Optional.of(appointment));
        when(appointmentRepositoryPort.save(appointment)).thenReturn(appointment);
        when(appointmentMapper.toDTO(appointment)).thenReturn(appointmentDTO);

        AppointmentDTO result = appointmentService.updateStatus(appointmentId, AppointmentStatus.CONFIRMADA);

        assertNotNull(result);
        assertEquals(AppointmentStatus.CONFIRMADA, appointment.getStatus());
        verify(appointmentRepositoryPort, times(1)).save(appointment);
        verify(appointmentMapper, times(1)).toDTO(appointment);
    }

    @Test
    void updateStatus_shouldThrowDomainException_whenInvalidTransition() {
        appointment.setStatus(AppointmentStatus.PROGRAMADA);
        when(appointmentRepositoryPort.findById(appointmentId)).thenReturn(Optional.of(appointment));

        DomainException exception = assertThrows(DomainException.class,
                () -> appointmentService.updateStatus(appointmentId, AppointmentStatus.EN_CURSO));

        assertEquals("Invalid status transition from PROGRAMADA to " + AppointmentStatus.EN_CURSO, exception.getMessage());
        verify(appointmentRepositoryPort, never()).save(any());
    }

    @Test
    void findByPatientId_shouldReturnAppointmentsForPatient() {
        List<Appointment> appointments = List.of(appointment);
        when(appointmentRepositoryPort.findByPatientId(patientId)).thenReturn(appointments);
        when(appointmentMapper.toDTO(appointment)).thenReturn(appointmentDTO);

        List<AppointmentDTO> result = appointmentService.findByPatientId(patientId);

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(appointmentDTO, result.get(0));
        verify(appointmentRepositoryPort, times(1)).findByPatientId(patientId);
        verify(appointmentMapper, times(1)).toDTO(appointment);
    }

    @Test
    void findByDentistId_shouldReturnAppointmentsForDentist() {
        List<Appointment> appointments = List.of(appointment);
        when(appointmentRepositoryPort.findByDentistId(dentistId)).thenReturn(appointments);
        when(appointmentMapper.toDTO(appointment)).thenReturn(appointmentDTO);

        List<AppointmentDTO> result = appointmentService.findByDentistId(dentistId);

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(appointmentDTO, result.get(0));
        verify(appointmentRepositoryPort, times(1)).findByDentistId(dentistId);
        verify(appointmentMapper, times(1)).toDTO(appointment);
    }

    @Test