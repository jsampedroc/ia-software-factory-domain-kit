package com.application.domain.port;

import com.application.domain.model.Specialty;
import com.application.domain.valueobject.SpecialtyId;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class SpecialtyRepositoryPortTest {

    @Mock
    private SpecialtyRepositoryPort specialtyRepositoryPort;

    @Test
    void testSave() {
        SpecialtyId specialtyId = new SpecialtyId(UUID.randomUUID());
        Specialty specialty = Specialty.create(specialtyId, "ORT-01", "Ortodoncia", "Especialidad en ortodoncia");

        when(specialtyRepositoryPort.save(specialty)).thenReturn(specialty);

        Specialty savedSpecialty = specialtyRepositoryPort.save(specialty);

        assertNotNull(savedSpecialty);
        assertEquals(specialtyId, savedSpecialty.getId());
        assertEquals("ORT-01", savedSpecialty.getCodigo());
        verify(specialtyRepositoryPort, times(1)).save(specialty);
    }

    @Test
    void testFindById() {
        SpecialtyId specialtyId = new SpecialtyId(UUID.randomUUID());
        Specialty specialty = Specialty.create(specialtyId, "PER-01", "Periodoncia", "Especialidad en periodoncia");

        when(specialtyRepositoryPort.findById(specialtyId)).thenReturn(Optional.of(specialty));

        Optional<Specialty> foundSpecialty = specialtyRepositoryPort.findById(specialtyId);

        assertTrue(foundSpecialty.isPresent());
        assertEquals(specialtyId, foundSpecialty.get().getId());
        assertEquals("PER-01", foundSpecialty.get().getCodigo());
        verify(specialtyRepositoryPort, times(1)).findById(specialtyId);
    }

    @Test
    void testFindById_NotFound() {
        SpecialtyId specialtyId = new SpecialtyId(UUID.randomUUID());

        when(specialtyRepositoryPort.findById(specialtyId)).thenReturn(Optional.empty());

        Optional<Specialty> foundSpecialty = specialtyRepositoryPort.findById(specialtyId);

        assertFalse(foundSpecialty.isPresent());
        verify(specialtyRepositoryPort, times(1)).findById(specialtyId);
    }

    @Test
    void testFindAll() {
        SpecialtyId specialtyId1 = new SpecialtyId(UUID.randomUUID());
        SpecialtyId specialtyId2 = new SpecialtyId(UUID.randomUUID());
        Specialty specialty1 = Specialty.create(specialtyId1, "ORT-01", "Ortodoncia", "Descripción 1");
        Specialty specialty2 = Specialty.create(specialtyId2, "END-01", "Endodoncia", "Descripción 2");
        List<Specialty> specialties = List.of(specialty1, specialty2);

        when(specialtyRepositoryPort.findAll()).thenReturn(specialties);

        List<Specialty> allSpecialties = specialtyRepositoryPort.findAll();

        assertNotNull(allSpecialties);
        assertEquals(2, allSpecialties.size());
        assertTrue(allSpecialties.contains(specialty1));
        assertTrue(allSpecialties.contains(specialty2));
        verify(specialtyRepositoryPort, times(1)).findAll();
    }

    @Test
    void testDelete() {
        SpecialtyId specialtyId = new SpecialtyId(UUID.randomUUID());

        doNothing().when(specialtyRepositoryPort).delete(specialtyId);

        specialtyRepositoryPort.delete(specialtyId);

        verify(specialtyRepositoryPort, times(1)).delete(specialtyId);
    }

    @Test
    void testExistsById() {
        SpecialtyId specialtyId = new SpecialtyId(UUID.randomUUID());

        when(specialtyRepositoryPort.existsById(specialtyId)).thenReturn(true);

        boolean exists = specialtyRepositoryPort.existsById(specialtyId);

        assertTrue(exists);
        verify(specialtyRepositoryPort, times(1)).existsById(specialtyId);
    }

    @Test
    void testCount() {
        when(specialtyRepositoryPort.count()).thenReturn(5L);

        long count = specialtyRepositoryPort.count();

        assertEquals(5L, count);
        verify(specialtyRepositoryPort, times(1)).count();
    }
}