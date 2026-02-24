package com.application.application.service;

import com.application.application.dto.ClinicDTO;
import com.application.domain.exception.DomainException;
import com.application.domain.model.Clinic;
import com.application.domain.port.ClinicRepositoryPort;
import com.application.domain.valueobject.ClinicId;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ClinicServiceTest {

    @Mock
    private ClinicRepositoryPort clinicRepositoryPort;

    @InjectMocks
    private ClinicService clinicService;

    private ClinicId clinicId;
    private ClinicDTO validClinicDTO;
    private Clinic mockClinic;

    @BeforeEach
    void setUp() {
        clinicId = new ClinicId(UUID.randomUUID());

        validClinicDTO = new ClinicDTO();
        validClinicDTO.setCodigo("CLI-001");
        validClinicDTO.setNombre("Clínica Central");
        validClinicDTO.setDireccion("Calle Principal 123");
        validClinicDTO.setTelefono("555-1234");
        validClinicDTO.setEmail("central@clinica.com");
        validClinicDTO.setHorarioApertura(LocalTime.of(8, 0));
        validClinicDTO.setHorarioCierre(LocalTime.of(18, 0));
        validClinicDTO.setActiva(true);

        mockClinic = mock(Clinic.class);
        when(mockClinic.getCodigo()).thenReturn("CLI-001");
    }

    @Test
    void createClinic_WithValidData_ShouldReturnClinicDTO() {
        when(clinicRepositoryPort.findByCodigo(validClinicDTO.getCodigo())).thenReturn(Optional.empty());
        when(clinicRepositoryPort.save(any(Clinic.class))).thenReturn(mockClinic);
        when(mockClinic.getId()).thenReturn(clinicId);

        ClinicDTO result = clinicService.createClinic(validClinicDTO);

        assertNotNull(result);
        verify(clinicRepositoryPort).findByCodigo(validClinicDTO.getCodigo());
        verify(clinicRepositoryPort).save(any(Clinic.class));
    }

    @Test
    void createClinic_WithDuplicateCode_ShouldThrowDomainException() {
        when(clinicRepositoryPort.findByCodigo(validClinicDTO.getCodigo())).thenReturn(Optional.of(mockClinic));

        DomainException exception = assertThrows(DomainException.class,
                () -> clinicService.createClinic(validClinicDTO));

        assertEquals("Ya existe una clínica con el código: " + validClinicDTO.getCodigo(), exception.getMessage());
        verify(clinicRepositoryPort, never()).save(any(Clinic.class));
    }

    @Test
    void createClinic_WithInvalidData_ShouldThrowDomainException() {
        ClinicDTO invalidDTO = new ClinicDTO();
        invalidDTO.setCodigo("");

        DomainException exception = assertThrows(DomainException.class,
                () -> clinicService.createClinic(invalidDTO));

        assertTrue(exception.getMessage().contains("obligatorio"));
        verify(clinicRepositoryPort, never()).save(any(Clinic.class));
    }

    @Test
    void createClinic_WithInvalidSchedule_ShouldThrowDomainException() {
        validClinicDTO.setHorarioApertura(LocalTime.of(19, 0));
        validClinicDTO.setHorarioCierre(LocalTime.of(8, 0));

        DomainException exception = assertThrows(DomainException.class,
                () -> clinicService.createClinic(validClinicDTO));

        assertTrue(exception.getMessage().contains("horario de apertura no puede ser después"));
        verify(clinicRepositoryPort, never()).save(any(Clinic.class));
    }

    @Test
    void getClinicById_WhenClinicExists_ShouldReturnClinicDTO() {
        when(clinicRepositoryPort.findById(clinicId)).thenReturn(Optional.of(mockClinic));
        when(mockClinic.getId()).thenReturn(clinicId);

        ClinicDTO result = clinicService.getClinicById(clinicId);

        assertNotNull(result);
        verify(clinicRepositoryPort).findById(clinicId);
    }

    @Test
    void getClinicById_WhenClinicNotExists_ShouldThrowDomainException() {
        when(clinicRepositoryPort.findById(clinicId)).thenReturn(Optional.empty());

        DomainException exception = assertThrows(DomainException.class,
                () -> clinicService.getClinicById(clinicId));

        assertEquals("Clínica no encontrada con ID: " + clinicId, exception.getMessage());
        verify(clinicRepositoryPort).findById(clinicId);
    }

    @Test
    void getAllClinics_ShouldReturnListOfClinicDTO() {
        List<Clinic> clinics = List.of(mockClinic);
        when(clinicRepositoryPort.findAll()).thenReturn(clinics);

        List<ClinicDTO> result = clinicService.getAllClinics();

        assertNotNull(result);
        assertFalse(result.isEmpty());
        verify(clinicRepositoryPort).findAll();
    }

    @Test
    void getActiveClinics_ShouldReturnListOfActiveClinicDTO() {
        List<Clinic> activeClinics = List.of(mockClinic);
        when(clinicRepositoryPort.findByActivaTrue()).thenReturn(activeClinics);

        List<ClinicDTO> result = clinicService.getActiveClinics();

        assertNotNull(result);
        assertFalse(result.isEmpty());
        verify(clinicRepositoryPort).findByActivaTrue();
    }

    @Test
    void updateClinic_WithValidData_ShouldReturnUpdatedClinicDTO() {
        when(clinicRepositoryPort.findById(clinicId)).thenReturn(Optional.of(mockClinic));
        when(mockClinic.getCodigo()).thenReturn("CLI-001");
        when(clinicRepositoryPort.save(any(Clinic.class))).thenReturn(mockClinic);

        ClinicDTO result = clinicService.updateClinic(clinicId, validClinicDTO);

        assertNotNull(result);
        verify(mockClinic).update(
                eq(validClinicDTO.getCodigo()),
                eq(validClinicDTO.getNombre()),
                eq(validClinicDTO.getDireccion()),
                eq(validClinicDTO.getTelefono()),
                eq(validClinicDTO.getEmail()),
                eq(validClinicDTO.getHorarioApertura()),
                eq(validClinicDTO.getHorarioCierre()),
                eq(validClinicDTO.getActiva())
        );
        verify(clinicRepositoryPort).save(mockClinic);
    }

    @Test
    void updateClinic_WhenClinicNotExists_ShouldThrowDomainException() {
        when(clinicRepositoryPort.findById(clinicId)).thenReturn(Optional.empty());

        DomainException exception = assertThrows(DomainException.class,
                () -> clinicService.updateClinic(clinicId, validClinicDTO));

        assertEquals("Clínica no encontrada con ID: " + clinicId, exception.getMessage());
        verify(clinicRepositoryPort, never()).save(any(Clinic.class));
    }

    @Test
    void updateClinic_WithChangedDuplicateCode_ShouldThrowDomainException() {
        when(clinicRepositoryPort.findById(clinicId)).thenReturn(Optional.of(mockClinic));
        when(mockClinic.getCodigo()).thenReturn("CLI-002");
        when(clinicRepositoryPort.findByCodigo(validClinicDTO.getCodigo())).thenReturn(Optional.of(mock(Clinic.class)));

        DomainException exception = assertThrows(DomainException.class,
                () -> clinicService.updateClinic(clinicId, validClinicDTO));

        assertEquals("Ya existe una clínica con el código: " + validClinicDTO.getCodigo(), exception.getMessage());
        verify(clinicRepositoryPort, never()).save(any(Clinic.class));
    }

    @Test
    void deactivateClinic_WhenClinicExists_ShouldDeactivate() {
        when(clinicRepositoryPort.findById(clinicId)).thenReturn(Optional.of(mockClinic));

        clinicService.deactivateClinic(clinicId);

        verify(mockClinic).deactivate();
        verify(clinicRepositoryPort).save(mockClinic);
    }

    @Test
    void deactivateClinic_WhenClinicNotExists_ShouldThrowDomainException() {
        when(clinicRepositoryPort.findById(clinicId)).thenReturn(Optional.empty());

        DomainException exception = assertThrows(DomainException.class,
                () -> clinicService.deactivateClinic(clinicId));

        assertEquals("Clínica no encontrada con ID: " + clinicId, exception.getMessage());
        verify(clinicRepositoryPort, never()).save(any(Clinic.class));
    }

    @Test
    void activateClinic_WhenClinicExists_ShouldActivate() {
        when(clinicRepositoryPort.findById(clinicId)).thenReturn(Optional.of(mockClinic));

        clinicService.activateClinic(clinicId);

        verify(mockClinic).activate();
        verify(clinicRepositoryPort).save(mockClinic);
    }

    @Test
    void activateClinic_WhenClinicNotExists_ShouldThrowDomainException() {
        when(clinicRepositoryPort.findById(clinicId)).thenReturn(Optional.empty());

        DomainException exception = assertThrows(DomainException.class,
                () -> clinicService.activateClinic(clinicId));

        assertEquals("Clínica no encontrada con ID: " + clinicId, exception.getMessage());
        verify(clinicRepositoryPort, never()).save(any(Clinic.class));
    }

    @Test
    void deleteClinic_WhenClinicExists_ShouldDelete() {
        when(clinicRepositoryPort.existsById(clinicId)).thenReturn(true);

        clinicService.deleteClinic(clinicId);

        verify(clinicRepositoryPort).deleteById(clinicId);
    }

    @Test
    void deleteClinic_WhenClinicNotExists_ShouldThrowDomainException() {
        when(clinicRepositoryPort.existsById(clinicId)).thenReturn(false);

        DomainException exception = assertThrows(DomainException.class,
                () -> clinicService.deleteClinic(clinicId));

        assertEquals("Clínica no encontrada con ID: " + clinicId, exception.getMessage());
        verify(clinicRepositoryPort, never()).deleteById(any());
    }

    @Test
    void validateClinicData_WithNullOpeningTime_ShouldThrowDomainException() {
        validClinicDTO.setHorarioApertura(null);

        DomainException exception = assertThrows(DomainException.class,
                () -> clinicService.createClinic(validClinicDTO));

        assertEquals("El horario de apertura es obligatorio", exception.getMessage());
    }

    @Test
    void validateClinicData_WithEqualOpeningAndClosingTime_ShouldThrowDomainException() {
        validClinicDTO.setHorarioApertura(LocalTime.of(9, 0));
        validClinicDTO.setHorarioCierre(LocalTime.of(9, 0));

        DomainException exception = assertThrows(DomainException.class,
                () -> clinicService.createClinic(validClinicDTO));

        assertEquals("El horario de apertura y cierre no pueden ser iguales", exception.getMessage());
    }
}