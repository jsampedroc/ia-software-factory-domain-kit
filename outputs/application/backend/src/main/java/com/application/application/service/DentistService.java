package com.application.application.service;

import com.application.domain.model.Dentist;
import com.application.domain.port.DentistRepositoryPort;
import com.application.domain.valueobject.DentistId;
import com.application.application.dto.DentistDTO;
import com.application.domain.exception.DomainException;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
public class DentistService {

    private final DentistRepositoryPort dentistRepositoryPort;

    public DentistService(DentistRepositoryPort dentistRepositoryPort) {
        this.dentistRepositoryPort = dentistRepositoryPort;
    }

    @Transactional
    public DentistDTO createDentist(DentistDTO dentistDTO) {
        validateDentistData(dentistDTO);
        checkUniqueLicense(dentistDTO.licenciaMedica());

        Dentist dentist = mapToDomain(dentistDTO);
        Dentist savedDentist = dentistRepositoryPort.save(dentist);
        return mapToDTO(savedDentist);
    }

    public Optional<DentistDTO> findById(DentistId id) {
        return dentistRepositoryPort.findById(id)
                .map(this::mapToDTO);
    }

    public List<DentistDTO> findAllActive() {
        return dentistRepositoryPort.findAll().stream()
                .filter(Dentist::isActivo)
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    public List<DentistDTO> findByClinicAndActive(com.application.domain.valueobject.ClinicId clinicId) {
        // This method assumes the repository port will be extended to support this query.
        // For now, returning all active dentists as a placeholder implementation.
        // A proper implementation would require a specific repository method.
        return findAllActive();
    }

    @Transactional
    public DentistDTO updateDentist(DentistId id, DentistDTO dentistDTO) {
        validateDentistData(dentistDTO);
        Dentist existingDentist = dentistRepositoryPort.findById(id)
                .orElseThrow(() -> new DomainException("Dentist not found with id: " + id.value()));

        // Check license uniqueness if it's being changed
        if (!existingDentist.getLicenciaMedica().equals(dentistDTO.licenciaMedica())) {
            checkUniqueLicense(dentistDTO.licenciaMedica());
        }

        Dentist updatedDentist = mapToDomain(dentistDTO, id);
        Dentist savedDentist = dentistRepositoryPort.save(updatedDentist);
        return mapToDTO(savedDentist);
    }

    @Transactional
    public void deactivateDentist(DentistId id) {
        Dentist dentist = dentistRepositoryPort.findById(id)
                .orElseThrow(() -> new DomainException("Dentist not found with id: " + id.value()));
        dentist.deactivate();
        dentistRepositoryPort.save(dentist);
    }

    @Transactional
    public void activateDentist(DentistId id) {
        Dentist dentist = dentistRepositoryPort.findById(id)
                .orElseThrow(() -> new DomainException("Dentist not found with id: " + id.value()));
        dentist.activate();
        dentistRepositoryPort.save(dentist);
    }

    public boolean isLicenseValid(String licenciaMedica) {
        // Placeholder for actual license validation logic (e.g., check with professional college)
        // For now, we assume a non-empty license is "valid"
        return licenciaMedica != null && !licenciaMedica.trim().isEmpty();
    }

    private void validateDentistData(DentistDTO dto) {
        if (dto.licenciaMedica() == null || dto.licenciaMedica().trim().isEmpty()) {
            throw new DomainException("Medical license is required");
        }
        if (dto.nombre() == null || dto.nombre().trim().isEmpty()) {
            throw new DomainException("First name is required");
        }
        if (dto.apellido() == null || dto.apellido().trim().isEmpty()) {
            throw new DomainException("Last name is required");
        }
        if (dto.email() == null || dto.email().trim().isEmpty()) {
            throw new DomainException("Email is required");
        }
        if (dto.fechaContratacion() == null || dto.fechaContratacion().isAfter(LocalDate.now())) {
            throw new DomainException("Hire date must be in the past or present");
        }
        if (!isLicenseValid(dto.licenciaMedica())) {
            throw new DomainException("Medical license is not valid");
        }
    }

    private void checkUniqueLicense(String licenciaMedica) {
        boolean exists = dentistRepositoryPort.findAll().stream()
                .anyMatch(d -> d.getLicenciaMedica().equals(licenciaMedica));
        if (exists) {
            throw new DomainException("A dentist with this medical license already exists");
        }
    }

    private Dentist mapToDomain(DentistDTO dto) {
        return Dentist.create(
                dto.licenciaMedica(),
                dto.nombre(),
                dto.apellido(),
                dto.telefono(),
                dto.email(),
                dto.fechaContratacion(),
                dto.activo() != null ? dto.activo() : true
        );
    }

    private Dentist mapToDomain(DentistDTO dto, DentistId id) {
        return Dentist.withId(
                id,
                dto.licenciaMedica(),
                dto.nombre(),
                dto.apellido(),
                dto.telefono(),
                dto.email(),
                dto.fechaContratacion(),
                dto.activo() != null ? dto.activo() : true
        );
    }

    private DentistDTO mapToDTO(Dentist dentist) {
        return new DentistDTO(
                dentist.getId() != null ? dentist.getId().value() : null,
                dentist.getLicenciaMedica(),
                dentist.getNombre(),
                dentist.getApellido(),
                dentist.getTelefono(),
                dentist.getEmail(),
                dentist.getFechaContratacion(),
                dentist.isActivo()
        );
    }
}