package com.application.domain.port;

import com.application.domain.model.Patient;
import com.application.domain.valueobject.PatientId;
import com.application.domain.shared.EntityRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PatientRepositoryPortTest {

    @Mock
    private PatientRepositoryPort patientRepositoryPort;

    @Test
    void testSave() {
        PatientId patientId = new PatientId(UUID.randomUUID());
        Patient patient = Patient.create(
                patientId,
                "12345678A",
                "Juan",
                "Pérez",
                LocalDate.of(1985, 5, 15),
                "+34123456789",
                "juan.perez@email.com",
                "Calle Falsa 123",
                LocalDateTime.now(),
                true
        );

        when(patientRepositoryPort.save(patient)).thenReturn(patient);

        Patient savedPatient = patientRepositoryPort.save(patient);

        assertNotNull(savedPatient);
        assertEquals(patientId, savedPatient.getId());
        verify(patientRepositoryPort, times(1)).save(patient);
    }

    @Test
    void testFindById() {
        PatientId patientId = new PatientId(UUID.randomUUID());
        Patient patient = Patient.create(
                patientId,
                "87654321B",
                "María",
                "Gómez",
                LocalDate.of(1990, 8, 22),
                "+34987654321",
                "maria.gomez@email.com",
                "Avenida Real 456",
                LocalDateTime.now(),
                true
        );

        when(patientRepositoryPort.findById(patientId)).thenReturn(Optional.of(patient));

        Optional<Patient> foundPatient = patientRepositoryPort.findById(patientId);

        assertTrue(foundPatient.isPresent());
        assertEquals(patientId, foundPatient.get().getId());
        assertEquals("María", foundPatient.get().getNombre());
        verify(patientRepositoryPort, times(1)).findById(patientId);
    }

    @Test
    void testFindById_NotFound() {
        PatientId patientId = new PatientId(UUID.randomUUID());

        when(patientRepositoryPort.findById(patientId)).thenReturn(Optional.empty());

        Optional<Patient> foundPatient = patientRepositoryPort.findById(patientId);

        assertFalse(foundPatient.isPresent());
        verify(patientRepositoryPort, times(1)).findById(patientId);
    }

    @Test
    void testFindAll() {
        PatientId patientId1 = new PatientId(UUID.randomUUID());
        PatientId patientId2 = new PatientId(UUID.randomUUID());

        Patient patient1 = Patient.create(
                patientId1,
                "11111111H",
                "Carlos",
                "López",
                LocalDate.of(1975, 3, 10),
                "+34111111111",
                "carlos.lopez@email.com",
                "Plaza Mayor 1",
                LocalDateTime.now(),
                true
        );

        Patient patient2 = Patient.create(
                patientId2,
                "22222222J",
                "Ana",
                "Martínez",
                LocalDate.of(1988, 11, 30),
                "+34222222222",
                "ana.martinez@email.com",
                "Calle Secundaria 7",
                LocalDateTime.now(),
                false
        );

        List<Patient> patientList = List.of(patient1, patient2);

        when(patientRepositoryPort.findAll()).thenReturn(patientList);

        List<Patient> allPatients = patientRepositoryPort.findAll();

        assertNotNull(allPatients);
        assertEquals(2, allPatients.size());
        assertTrue(allPatients.contains(patient1));
        assertTrue(allPatients.contains(patient2));
        verify(patientRepositoryPort, times(1)).findAll();
    }

    @Test
    void testDelete() {
        PatientId patientId = new PatientId(UUID.randomUUID());
        Patient patient = Patient.create(
                patientId,
                "33333333P",
                "Luis",
                "Fernández",
                LocalDate.of(1965, 7, 4),
                "+34333333333",
                "luis.fernandez@email.com",
                "Camino Viejo 22",
                LocalDateTime.now(),
                true
        );

        doNothing().when(patientRepositoryPort).delete(patient);

        patientRepositoryPort.delete(patient);

        verify(patientRepositoryPort, times(1)).delete(patient);
    }

    @Test
    void testDeleteById() {
        PatientId patientId = new PatientId(UUID.randomUUID());

        doNothing().when(patientRepositoryPort).deleteById(patientId);

        patientRepositoryPort.deleteById(patientId);

        verify(patientRepositoryPort, times(1)).deleteById(patientId);
    }

    @Test
    void testExistsById() {
        PatientId patientId = new PatientId(UUID.randomUUID());

        when(patientRepositoryPort.existsById(patientId)).thenReturn(true);

        boolean exists = patientRepositoryPort.existsById(patientId);

        assertTrue(exists);
        verify(patientRepositoryPort, times(1)).existsById(patientId);
    }

    @Test
    void testCount() {
        when(patientRepositoryPort.count()).thenReturn(5L);

        long count = patientRepositoryPort.count();

        assertEquals(5L, count);
        verify(patientRepositoryPort, times(1)).count();
    }
}