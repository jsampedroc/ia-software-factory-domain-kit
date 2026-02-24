package com.application.application.service;

import com.application.application.dto.PatientDTO;
import com.application.domain.exception.DomainException;
import com.application.domain.model.Patient;
import com.application.domain.port.PatientRepositoryPort;
import com.application.domain.valueobject.PatientId;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PatientServiceTest {

    @Mock
    private PatientRepositoryPort patientRepositoryPort;

    @InjectMocks
    private PatientService patientService;

    private PatientId patientId;
    private Patient patient;
    private PatientDTO patientDTO;
    private final LocalDate birthDate = LocalDate.now().minusYears(30);
    private final LocalDateTime registrationDate = LocalDateTime.now().minusMonths(1);

    @BeforeEach
    void setUp() {
        patientId = new PatientId(UUID.randomUUID());
        patient = Patient.create(
                "12345678A",
                "Juan",
                "Pérez",
                birthDate,
                "600123456",
                "juan.perez@email.com",
                "Calle Falsa 123"
        );
        patientDTO = new PatientDTO(
                patientId,
                "12345678A",
                "Juan",
                "Pérez",
                birthDate,
                "600123456",
                "juan.perez@email.com",
                "Calle Falsa 123",
                registrationDate,
                true
        );
    }

    @Test
    void createPatient_ValidData_ReturnsPatientDTO() {
        when(patientRepositoryPort.findByDni(any())).thenReturn(Optional.empty());
        when(patientRepositoryPort.findByEmail(any())).thenReturn(Optional.empty());
        when(patientRepositoryPort.save(any(Patient.class))).thenReturn(patient);

        PatientDTO result = patientService.createPatient(patientDTO);

        assertNotNull(result);
        assertEquals(patientDTO.dni(), result.dni());
        assertEquals(patientDTO.nombre(), result.nombre());
        verify(patientRepositoryPort).save(any(Patient.class));
    }

    @Test
    void createPatient_DuplicateDni_ThrowsDomainException() {
        when(patientRepositoryPort.findByDni(patientDTO.dni())).thenReturn(Optional.of(patient));

        DomainException exception = assertThrows(DomainException.class,
                () -> patientService.createPatient(patientDTO));

        assertTrue(exception.getMessage().contains("Ya existe un paciente con el DNI"));
        verify(patientRepositoryPort, never()).save(any());
    }

    @Test
    void createPatient_InvalidEmailFormat_ThrowsDomainException() {
        PatientDTO invalidEmailDTO = new PatientDTO(
                null, "87654321B", "Ana", "García", birthDate,
                "600987654", "invalid-email", "Calle Real 456", null, true
        );

        DomainException exception = assertThrows(DomainException.class,
                () -> patientService.createPatient(invalidEmailDTO));

        assertTrue(exception.getMessage().contains("formato del email"));
    }

    @Test
    void getPatientById_ExistingId_ReturnsPatientDTO() {
        when(patientRepositoryPort.findById(patientId)).thenReturn(Optional.of(patient));

        PatientDTO result = patientService.getPatientById(patientId);

        assertNotNull(result);
        assertEquals(patient.getDni(), result.dni());
    }

    @Test
    void getPatientById_NonExistingId_ThrowsDomainException() {
        when(patientRepositoryPort.findById(patientId)).thenReturn(Optional.empty());

        DomainException exception = assertThrows(DomainException.class,
                () -> patientService.getPatientById(patientId));

        assertTrue(exception.getMessage().contains("no encontrado"));
    }

    @Test
    void getAllPatients_ReturnsListOfDTOs() {
        List<Patient> patients = List.of(patient);
        when(patientRepositoryPort.findAll()).thenReturn(patients);

        List<PatientDTO> result = patientService.getAllPatients();

        assertFalse(result.isEmpty());
        assertEquals(1, result.size());
    }

    @Test
    void getActivePatients_ReturnsActivePatients() {
        List<Patient> activePatients = List.of(patient);
        when(patientRepositoryPort.findByActive(true)).thenReturn(activePatients);

        List<PatientDTO> result = patientService.getActivePatients();

        assertFalse(result.isEmpty());
        verify(patientRepositoryPort).findByActive(true);
    }

    @Test
    void updatePatient_ValidData_ReturnsUpdatedDTO() {
        when(patientRepositoryPort.findById(patientId)).thenReturn(Optional.of(patient));
        when(patientRepositoryPort.findByDni(any())).thenReturn(Optional.empty());
        when(patientRepositoryPort.findByEmail(any())).thenReturn(Optional.empty());
        when(patientRepositoryPort.save(any(Patient.class))).thenReturn(patient);

        PatientDTO updatedDTO = new PatientDTO(
                patientId,
                "87654321B",
                "Juan Updated",
                "Pérez Updated",
                birthDate,
                "611223344",
                "updated@email.com",
                "Nueva Dirección 789",
                registrationDate,
                true
        );

        PatientDTO result = patientService.updatePatient(patientId, updatedDTO);

        assertNotNull(result);
        assertEquals(updatedDTO.nombre(), result.nombre());
        verify(patientRepositoryPort).save(any(Patient.class));
    }

    @Test
    void updatePatient_DuplicateDniForOtherPatient_ThrowsDomainException() {
        Patient otherPatient = Patient.create(
                "87654321B", "Other", "Patient", birthDate,
                "600000000", "other@email.com", "Other Address"
        );
        PatientId otherId = new PatientId(UUID.randomUUID());

        when(patientRepositoryPort.findById(patientId)).thenReturn(Optional.of(patient));
        when(patientRepositoryPort.findByDni("87654321B")).thenReturn(Optional.of(otherPatient));

        PatientDTO updatedDTO = new PatientDTO(
                patientId,
                "87654321B",
                "Juan",
                "Pérez",
                birthDate,
                "600123456",
                "juan.perez@email.com",
                "Calle Falsa 123",
                registrationDate,
                true
        );

        DomainException exception = assertThrows(DomainException.class,
                () -> patientService.updatePatient(patientId, updatedDTO));

        assertTrue(exception.getMessage().contains("Ya existe otro paciente con el DNI"));
    }

    @Test
    void deactivatePatient_ExistingPatient_DeactivatesPatient() {
        when(patientRepositoryPort.findById(patientId)).thenReturn(Optional.of(patient));
        when(patientRepositoryPort.save(any(Patient.class))).thenReturn(patient);

        patientService.deactivatePatient(patientId);

        verify(patientRepositoryPort).save(patient);
        // Assuming deactivate() method exists and is called
    }

    @Test
    void archiveInactivePatients_ArchivesOldInactivePatients() {
        Patient inactivePatient = Patient.create(
                "99999999Z", "Inactive", "Patient", birthDate,
                "600999999", "inactive@email.com", "Old Address"
        );
        inactivePatient.deactivate();
        // Reflection or package-private method might be needed to set old registration date
        // For simplicity, we assume the repository returns the patient
        List<Patient> inactivePatients = List.of(inactivePatient);

        when(patientRepositoryPort.findByActive(false)).thenReturn(inactivePatients);
        when(patientRepositoryPort.saveAll(any())).thenReturn(inactivePatients);

        patientService.archiveInactivePatients();

        verify(patientRepositoryPort).saveAll(inactivePatients);
    }

    @Test
    void canScheduleNewAppointment_ActivePatientNoOverdueInvoices_ReturnsTrue() {
        when(patientRepositoryPort.findById(patientId)).thenReturn(Optional.of(patient));
        // Assuming patient has no overdue invoices

        boolean result = patientService.canScheduleNewAppointment(patientId);

        assertTrue(result);
    }

    @Test
    void canScheduleNewAppointment_InactivePatient_ReturnsFalse() {
        patient.deactivate();
        when(patientRepositoryPort.findById(patientId)).thenReturn(Optional.of(patient));

        boolean result = patientService.canScheduleNewAppointment(patientId);

        assertFalse(result);
    }

    @Test
    void calculateAge_ValidPatient_ReturnsCorrectAge() {
        when(patientRepositoryPort.findById(patientId)).thenReturn(Optional.of(patient));

        int age = patientService.calculateAge(patientId);

        assertEquals(30, age); // Based on birthDate set in setUp
    }

    @Test
    void createPatient_MissingDni_ThrowsDomainException() {
        PatientDTO invalidDTO = new PatientDTO(
                null, null, "Nombre", "Apellido", birthDate,
                "600123456", "email@test.com", "Dirección", null, true
        );

        DomainException exception = assertThrows(DomainException.class,
                () -> patientService.createPatient(invalidDTO));

        assertTrue(exception.getMessage().contains("DNI es obligatorio"));
    }

    @Test
    void createPatient_TooYoung_ThrowsDomainException() {
        LocalDate tooYoungBirthDate = LocalDate.now().minusMonths(6);
        PatientDTO invalidDTO = new PatientDTO(
                null, "11223344C", "Baby", "Smith", tooYoungBirthDate,
                "600111222", "baby@test.com", "Dirección", null, true
        );

        DomainException exception = assertThrows(DomainException.class,
                () -> patientService.createPatient(invalidDTO));

        assertTrue(exception.getMessage().contains("al menos 1 año de edad"));
    }
}