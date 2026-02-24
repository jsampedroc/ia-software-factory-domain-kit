package com.application.application.service;

import com.application.application.dto.DentistDTO;
import com.application.domain.exception.DomainException;
import com.application.domain.model.Dentist;
import com.application.domain.port.DentistRepositoryPort;
import com.application.domain.valueobject.DentistId;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class DentistServiceTest {

    @Mock
    private DentistRepositoryPort dentistRepositoryPort;

    @InjectMocks
    private DentistService dentistService;

    private DentistId sampleDentistId;
    private DentistDTO validDentistDTO;
    private Dentist sampleDentist;

    @BeforeEach
    void setUp() {
        sampleDentistId = new DentistId(UUID.randomUUID());
        validDentistDTO = new DentistDTO(
                null,
                "LIC-12345",
                "Juan",
                "Pérez",
                "555-1234",
                "juan.perez@clinica.com",
                LocalDate.now().minusYears(1),
                true
        );
        sampleDentist = Dentist.create(
                "LIC-12345",
                "Juan",
                "Pérez",
                "555-1234",
                "juan.perez@clinica.com",
                LocalDate.now().minusYears(1),
                true
        );
    }

    @Test
    void createDentist_WithValidData_ShouldReturnSavedDentistDTO() {
        when(dentistRepositoryPort.findAll()).thenReturn(List.of());
        when(dentistRepositoryPort.save(any(Dentist.class))).thenAnswer(invocation -> {
            Dentist saved = invocation.getArgument(0);
            return saved;
        });

        DentistDTO result = dentistService.createDentist(validDentistDTO);

        assertThat(result).isNotNull();
        assertThat(result.licenciaMedica()).isEqualTo(validDentistDTO.licenciaMedica());
        assertThat(result.nombre()).isEqualTo(validDentistDTO.nombre());
        verify(dentistRepositoryPort, times(1)).save(any(Dentist.class));
    }

    @Test
    void createDentist_WithDuplicateLicense_ShouldThrowDomainException() {
        when(dentistRepositoryPort.findAll()).thenReturn(List.of(sampleDentist));

        assertThatThrownBy(() -> dentistService.createDentist(validDentistDTO))
                .isInstanceOf(DomainException.class)
                .hasMessageContaining("A dentist with this medical license already exists");
        verify(dentistRepositoryPort, never()).save(any(Dentist.class));
    }

    @Test
    void createDentist_WithInvalidData_ShouldThrowDomainException() {
        DentistDTO invalidDTO = new DentistDTO(
                null,
                "",
                "",
                "Pérez",
                "555-1234",
                "juan.perez@clinica.com",
                LocalDate.now().minusYears(1),
                true
        );

        assertThatThrownBy(() -> dentistService.createDentist(invalidDTO))
                .isInstanceOf(DomainException.class)
                .hasMessageContaining("Medical license is required");
        verify(dentistRepositoryPort, never()).save(any(Dentist.class));
    }

    @Test
    void findById_WhenDentistExists_ShouldReturnOptionalDTO() {
        when(dentistRepositoryPort.findById(sampleDentistId)).thenReturn(Optional.of(sampleDentist));

        Optional<DentistDTO> result = dentistService.findById(sampleDentistId);

        assertThat(result).isPresent();
        assertThat(result.get().nombre()).isEqualTo(sampleDentist.getNombre());
    }

    @Test
    void findById_WhenDentistDoesNotExist_ShouldReturnEmptyOptional() {
        when(dentistRepositoryPort.findById(sampleDentistId)).thenReturn(Optional.empty());

        Optional<DentistDTO> result = dentistService.findById(sampleDentistId);

        assertThat(result).isEmpty();
    }

    @Test
    void findAllActive_ShouldReturnOnlyActiveDentists() {
        Dentist inactiveDentist = Dentist.create(
                "LIC-67890",
                "Ana",
                "Gómez",
                "555-5678",
                "ana.gomez@clinica.com",
                LocalDate.now().minusYears(2),
                false
        );
        when(dentistRepositoryPort.findAll()).thenReturn(List.of(sampleDentist, inactiveDentist));

        List<DentistDTO> result = dentistService.findAllActive();

        assertThat(result).hasSize(1);
        assertThat(result.get(0).nombre()).isEqualTo("Juan");
    }

    @Test
    void updateDentist_WithValidData_ShouldReturnUpdatedDTO() {
        when(dentistRepositoryPort.findById(sampleDentistId)).thenReturn(Optional.of(sampleDentist));
        when(dentistRepositoryPort.findAll()).thenReturn(List.of(sampleDentist));
        when(dentistRepositoryPort.save(any(Dentist.class))).thenAnswer(invocation -> invocation.getArgument(0));

        DentistDTO updateDTO = new DentistDTO(
                sampleDentistId.value(),
                "LIC-12345",
                "Juan Carlos",
                "Pérez",
                "555-9999",
                "juan.c.perez@clinica.com",
                LocalDate.now().minusYears(1),
                true
        );

        DentistDTO result = dentistService.updateDentist(sampleDentistId, updateDTO);

        assertThat(result.nombre()).isEqualTo("Juan Carlos");
        assertThat(result.telefono()).isEqualTo("555-9999");
        verify(dentistRepositoryPort, times(1)).save(any(Dentist.class));
    }

    @Test
    void updateDentist_WhenDentistNotFound_ShouldThrowDomainException() {
        when(dentistRepositoryPort.findById(sampleDentistId)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> dentistService.updateDentist(sampleDentistId, validDentistDTO))
                .isInstanceOf(DomainException.class)
                .hasMessageContaining("Dentist not found");
        verify(dentistRepositoryPort, never()).save(any(Dentist.class));
    }

    @Test
    void updateDentist_WithChangedDuplicateLicense_ShouldThrowDomainException() {
        Dentist anotherDentist = Dentist.create(
                "LIC-99999",
                "Pedro",
                "López",
                "555-0000",
                "pedro@clinica.com",
                LocalDate.now().minusYears(3),
                true
        );
        when(dentistRepositoryPort.findById(sampleDentistId)).thenReturn(Optional.of(sampleDentist));
        when(dentistRepositoryPort.findAll()).thenReturn(List.of(sampleDentist, anotherDentist));

        DentistDTO updateDTO = new DentistDTO(
                sampleDentistId.value(),
                "LIC-99999",
                "Juan",
                "Pérez",
                "555-1234",
                "juan.perez@clinica.com",
                LocalDate.now().minusYears(1),
                true
        );

        assertThatThrownBy(() -> dentistService.updateDentist(sampleDentistId, updateDTO))
                .isInstanceOf(DomainException.class)
                .hasMessageContaining("A dentist with this medical license already exists");
        verify(dentistRepositoryPort, never()).save(any(Dentist.class));
    }

    @Test
    void deactivateDentist_WhenDentistExists_ShouldDeactivateAndSave() {
        when(dentistRepositoryPort.findById(sampleDentistId)).thenReturn(Optional.of(sampleDentist));
        when(dentistRepositoryPort.save(any(Dentist.class))).thenAnswer(invocation -> invocation.getArgument(0));

        dentistService.deactivateDentist(sampleDentistId);

        verify(dentistRepositoryPort, times(1)).save(argThat(dentist -> !dentist.isActivo()));
    }

    @Test
    void deactivateDentist_WhenDentistNotFound_ShouldThrowDomainException() {
        when(dentistRepositoryPort.findById(sampleDentistId)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> dentistService.deactivateDentist(sampleDentistId))
                .isInstanceOf(DomainException.class)
                .hasMessageContaining("Dentist not found");
        verify(dentistRepositoryPort, never()).save(any(Dentist.class));
    }

    @Test
    void activateDentist_WhenDentistExists_ShouldActivateAndSave() {
        sampleDentist.deactivate();
        when(dentistRepositoryPort.findById(sampleDentistId)).thenReturn(Optional.of(sampleDentist));
        when(dentistRepositoryPort.save(any(Dentist.class))).thenAnswer(invocation -> invocation.getArgument(0));

        dentistService.activateDentist(sampleDentistId);

        verify(dentistRepositoryPort, times(1)).save(argThat(Dentist::isActivo));
    }

    @Test
    void activateDentist_WhenDentistNotFound_ShouldThrowDomainException() {
        when(dentistRepositoryPort.findById(sampleDentistId)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> dentistService.activateDentist(sampleDentistId))
                .isInstanceOf(DomainException.class)
                .hasMessageContaining("Dentist not found");
        verify(dentistRepositoryPort, never()).save(any(Dentist.class));
    }

    @Test
    void isLicenseValid_WithNonEmptyLicense_ShouldReturnTrue() {
        boolean result = dentistService.isLicenseValid("LIC-12345");
        assertThat(result).isTrue();
    }

    @Test
    void isLicenseValid_WithEmptyLicense_ShouldReturnFalse() {
        boolean result = dentistService.isLicenseValid("");
        assertThat(result).isFalse();
    }

    @Test
    void isLicenseValid_WithNullLicense_ShouldReturnFalse() {
        boolean result = dentistService.isLicenseValid(null);
        assertThat(result).isFalse();
    }

    @Test
    void findByClinicAndActive_ShouldReturnAllActiveDentists() {
        when(dentistRepositoryPort.findAll()).thenReturn(List.of(sampleDentist));

        List<DentistDTO> result = dentistService.findByClinicAndActive(any());

        assertThat(result).hasSize(1);
        assertThat(result.get(0).nombre()).isEqualTo("Juan");
    }
}