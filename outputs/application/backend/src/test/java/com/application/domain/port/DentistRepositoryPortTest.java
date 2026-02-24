package com.application.domain.port;

import com.application.domain.model.Dentist;
import com.application.domain.valueobject.DentistId;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class DentistRepositoryPortTest {

    @Mock
    private DentistRepositoryPort dentistRepositoryPort;

    @Test
    void testFindByMedicalLicense() {
        String medicalLicense = "MED-12345";
        DentistId dentistId = new DentistId(UUID.randomUUID());
        Dentist expectedDentist = Dentist.create(
                dentistId,
                medicalLicense,
                "Juan",
                "Pérez",
                "555-1234",
                "juan.perez@clinic.com",
                LocalDate.of(2020, 1, 15),
                true
        );

        when(dentistRepositoryPort.findByMedicalLicense(medicalLicense)).thenReturn(Optional.of(expectedDentist));

        Optional<Dentist> result = dentistRepositoryPort.findByMedicalLicense(medicalLicense);

        assertTrue(result.isPresent());
        assertEquals(expectedDentist, result.get());
        assertEquals(medicalLicense, result.get().getMedicalLicense());
        verify(dentistRepositoryPort, times(1)).findByMedicalLicense(medicalLicense);
    }

    @Test
    void testFindByMedicalLicense_NotFound() {
        String medicalLicense = "NON-EXISTENT";

        when(dentistRepositoryPort.findByMedicalLicense(medicalLicense)).thenReturn(Optional.empty());

        Optional<Dentist> result = dentistRepositoryPort.findByMedicalLicense(medicalLicense);

        assertFalse(result.isPresent());
        verify(dentistRepositoryPort, times(1)).findByMedicalLicense(medicalLicense);
    }

    @Test
    void testFindByClinicId() {
        String clinicId = "CLINIC-001";
        DentistId dentistId1 = new DentistId(UUID.randomUUID());
        DentistId dentistId2 = new DentistId(UUID.randomUUID());

        Dentist dentist1 = Dentist.create(
                dentistId1,
                "LIC-001",
                "Ana",
                "García",
                "555-1001",
                "ana.garcia@clinic.com",
                LocalDate.of(2019, 3, 10),
                true
        );
        Dentist dentist2 = Dentist.create(
                dentistId2,
                "LIC-002",
                "Carlos",
                "López",
                "555-1002",
                "carlos.lopez@clinic.com",
                LocalDate.of(2021, 7, 22),
                true
        );

        List<Dentist> expectedList = List.of(dentist1, dentist2);

        when(dentistRepositoryPort.findByClinicId(clinicId)).thenReturn(expectedList);

        List<Dentist> result = dentistRepositoryPort.findByClinicId(clinicId);

        assertNotNull(result);
        assertEquals(2, result.size());
        assertTrue(result.contains(dentist1));
        assertTrue(result.contains(dentist2));
        verify(dentistRepositoryPort, times(1)).findByClinicId(clinicId);
    }

    @Test
    void testFindBySpecialtyId() {
        String specialtyId = "SPEC-ORT-001";
        DentistId dentistId = new DentistId(UUID.randomUUID());

        Dentist dentist = Dentist.create(
                dentistId,
                "LIC-ORT-001",
                "María",
                "Rodríguez",
                "555-2001",
                "maria.rodriguez@clinic.com",
                LocalDate.of(2018, 5, 30),
                true
        );

        List<Dentist> expectedList = List.of(dentist);

        when(dentistRepositoryPort.findBySpecialtyId(specialtyId)).thenReturn(expectedList);

        List<Dentist> result = dentistRepositoryPort.findBySpecialtyId(specialtyId);

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(dentist, result.get(0));
        verify(dentistRepositoryPort, times(1)).findBySpecialtyId(specialtyId);
    }

    @Test
    void testFindActiveDentists() {
        DentistId activeId1 = new DentistId(UUID.randomUUID());
        DentistId activeId2 = new DentistId(UUID.randomUUID());
        DentistId inactiveId = new DentistId(UUID.randomUUID());

        Dentist activeDentist1 = Dentist.create(
                activeId1,
                "LIC-ACT-001",
                "Luis",
                "Martínez",
                "555-3001",
                "luis.martinez@clinic.com",
                LocalDate.of(2022, 2, 14),
                true
        );
        Dentist activeDentist2 = Dentist.create(
                activeId2,
                "LIC-ACT-002",
                "Sofía",
                "Hernández",
                "555-3002",
                "sofia.hernandez@clinic.com",
                LocalDate.of(2020, 11, 5),
                true
        );

        List<Dentist> expectedActiveList = List.of(activeDentist1, activeDentist2);

        when(dentistRepositoryPort.findActiveDentists()).thenReturn(expectedActiveList);

        List<Dentist> result = dentistRepositoryPort.findActiveDentists();

        assertNotNull(result);
        assertEquals(2, result.size());
        assertTrue(result.stream().allMatch(Dentist::isActive));
        verify(dentistRepositoryPort, times(1)).findActiveDentists();
    }

    @Test
    void testFindDentistsAvailableForAppointment() {
        String clinicId = "CLINIC-MAIN";
        String dateTime = "2024-12-01T10:00:00";
        DentistId availableId1 = new DentistId(UUID.randomUUID());
        DentistId availableId2 = new DentistId(UUID.randomUUID());

        Dentist availableDentist1 = Dentist.create(
                availableId1,
                "LIC-AVAIL-001",
                "Pedro",
                "Sánchez",
                "555-4001",
                "pedro.sanchez@clinic.com",
                LocalDate.of(2021, 4, 18),
                true
        );
        Dentist availableDentist2 = Dentist.create(
                availableId2,
                "LIC-AVAIL-002",
                "Elena",
                "Díaz",
                "555-4002",
                "elena.diaz@clinic.com",
                LocalDate.of(2019, 9, 9),
                true
        );

        List<Dentist> expectedAvailableList = List.of(availableDentist1, availableDentist2);

        when(dentistRepositoryPort.findDentistsAvailableForAppointment(clinicId, dateTime)).thenReturn(expectedAvailableList);

        List<Dentist> result = dentistRepositoryPort.findDentistsAvailableForAppointment(clinicId, dateTime);

        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals(expectedAvailableList, result);
        verify(dentistRepositoryPort, times(1)).findDentistsAvailableForAppointment(clinicId, dateTime);
    }

    @Test
    void testExistsByMedicalLicense_True() {
        String existingLicense = "LIC-EXISTS-001";

        when(dentistRepositoryPort.existsByMedicalLicense(existingLicense)).thenReturn(true);

        boolean result = dentistRepositoryPort.existsByMedicalLicense(existingLicense);

        assertTrue(result);
        verify(dentistRepositoryPort, times(1)).existsByMedicalLicense(existingLicense);
    }

    @Test
    void testExistsByMedicalLicense_False() {
        String nonExistingLicense = "LIC-NOT-EXISTS";

        when(dentistRepositoryPort.existsByMedicalLicense(nonExistingLicense)).thenReturn(false);

        boolean result = dentistRepositoryPort.existsByMedicalLicense(nonExistingLicense);

        assertFalse(result);
        verify(dentistRepositoryPort, times(1)).existsByMedicalLicense(nonExistingLicense);
    }

    @Test
    void testInheritedMethodsFromEntityRepository() {
        DentistId dentistId = new DentistId(UUID.randomUUID());
        Dentist dentist = Dentist.create(
                dentistId,
                "LIC-SAVE-001",
                "Test",
                "Save",
                "555-9999",
                "test.save@clinic.com",
                LocalDate.now(),
                true
        );

        when(dentistRepositoryPort.save(dentist)).thenReturn(dentist);
        when(dentistRepositoryPort.findById(dentistId)).thenReturn(Optional.of(dentist));
        when(dentistRepositoryPort.findAll()).thenReturn(List.of(dentist));
        when(dentistRepositoryPort.delete(dentistId)).thenReturn(true);

        Dentist saved = dentistRepositoryPort.save(dentist);
        assertEquals(dentist, saved);

        Optional<Dentist> found = dentistRepositoryPort.findById(dentistId);
        assertTrue(found.isPresent());
        assertEquals(dentistId, found.get().getId());

        List<Dentist> all = dentistRepositoryPort.findAll();
        assertEquals(1, all.size());
        assertEquals(dentist, all.get(0));

        boolean deleted = dentistRepositoryPort.delete(dentistId);
        assertTrue(deleted);

        verify(dentistRepositoryPort, times(1)).save(dentist);
        verify(dentistRepositoryPort, times(1)).findById(dentistId);
        verify(dentistRepositoryPort, times(1)).findAll();
        verify(dentistRepositoryPort, times(1)).delete(dentistId);
    }
}