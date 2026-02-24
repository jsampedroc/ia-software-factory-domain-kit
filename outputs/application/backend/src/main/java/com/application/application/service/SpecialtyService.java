package com.application.application.service;

import com.application.domain.model.Specialty;
import com.application.domain.valueobject.SpecialtyId;
import com.application.domain.port.SpecialtyRepositoryPort;
import com.application.application.dto.SpecialtyDTO;
import com.application.application.mapper.SpecialtyMapper;
import com.application.domain.exception.DomainException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
public class SpecialtyService {

    private final SpecialtyRepositoryPort specialtyRepositoryPort;
    private final SpecialtyMapper specialtyMapper;

    public SpecialtyService(SpecialtyRepositoryPort specialtyRepositoryPort, SpecialtyMapper specialtyMapper) {
        this.specialtyRepositoryPort = specialtyRepositoryPort;
        this.specialtyMapper = specialtyMapper;
    }

    @Transactional
    public SpecialtyDTO create(SpecialtyDTO specialtyDTO) {
        if (specialtyDTO == null) {
            throw new DomainException("SpecialtyDTO cannot be null");
        }

        Specialty specialty = specialtyMapper.toDomain(specialtyDTO);
        validateSpecialtyForCreate(specialty);

        Specialty savedSpecialty = specialtyRepositoryPort.save(specialty);
        return specialtyMapper.toDTO(savedSpecialty);
    }

    @Transactional
    public SpecialtyDTO update(SpecialtyId id, SpecialtyDTO specialtyDTO) {
        if (id == null || specialtyDTO == null) {
            throw new DomainException("ID and SpecialtyDTO cannot be null");
        }

        Specialty existingSpecialty = specialtyRepositoryPort.findById(id)
                .orElseThrow(() -> new DomainException("Specialty not found with id: " + id.value()));

        Specialty updatedSpecialty = specialtyMapper.toDomain(specialtyDTO);
        updatedSpecialty = new Specialty(
                existingSpecialty.getId(),
                updatedSpecialty.getCodigo(),
                updatedSpecialty.getNombre(),
                updatedSpecialty.getDescripcion()
        );

        validateSpecialtyForUpdate(updatedSpecialty, existingSpecialty.getCodigo());
        Specialty savedSpecialty = specialtyRepositoryPort.save(updatedSpecialty);
        return specialtyMapper.toDTO(savedSpecialty);
    }

    @Transactional
    public void delete(SpecialtyId id) {
        if (id == null) {
            throw new DomainException("Specialty ID cannot be null");
        }

        if (!specialtyRepositoryPort.existsById(id)) {
            throw new DomainException("Specialty not found with id: " + id.value());
        }

        specialtyRepositoryPort.deleteById(id);
    }

    public Optional<SpecialtyDTO> findById(SpecialtyId id) {
        if (id == null) {
            throw new DomainException("Specialty ID cannot be null");
        }

        return specialtyRepositoryPort.findById(id)
                .map(specialtyMapper::toDTO);
    }

    public List<SpecialtyDTO> findAll() {
        return specialtyRepositoryPort.findAll().stream()
                .map(specialtyMapper::toDTO)
                .collect(Collectors.toList());
    }

    public List<SpecialtyDTO> findByCodigo(String codigo) {
        if (codigo == null || codigo.trim().isEmpty()) {
            throw new DomainException("Specialty code cannot be null or empty");
        }

        return specialtyRepositoryPort.findByCodigo(codigo).stream()
                .map(specialtyMapper::toDTO)
                .collect(Collectors.toList());
    }

    public List<SpecialtyDTO> findByNombreContaining(String nombre) {
        if (nombre == null || nombre.trim().isEmpty()) {
            throw new DomainException("Specialty name cannot be null or empty");
        }

        return specialtyRepositoryPort.findByNombreContaining(nombre).stream()
                .map(specialtyMapper::toDTO)
                .collect(Collectors.toList());
    }

    public boolean existsByCodigo(String codigo) {
        if (codigo == null || codigo.trim().isEmpty()) {
            throw new DomainException("Specialty code cannot be null or empty");
        }

        return specialtyRepositoryPort.existsByCodigo(codigo);
    }

    public boolean existsByNombre(String nombre) {
        if (nombre == null || nombre.trim().isEmpty()) {
            throw new DomainException("Specialty name cannot be null or empty");
        }

        return specialtyRepositoryPort.existsByNombre(nombre);
    }

    private void validateSpecialtyForCreate(Specialty specialty) {
        validateCommonRules(specialty);
        
        if (existsByCodigo(specialty.getCodigo())) {
            throw new DomainException("Specialty with code '" + specialty.getCodigo() + "' already exists");
        }
    }

    private void validateSpecialtyForUpdate(Specialty specialty, String existingCodigo) {
        validateCommonRules(specialty);
        
        if (!specialty.getCodigo().equals(existingCodigo) && existsByCodigo(specialty.getCodigo())) {
            throw new DomainException("Specialty with code '" + specialty.getCodigo() + "' already exists");
        }
    }

    private void validateCommonRules(Specialty specialty) {
        if (specialty.getCodigo() == null || specialty.getCodigo().trim().isEmpty()) {
            throw new DomainException("Specialty code is required");
        }

        if (specialty.getNombre() == null || specialty.getNombre().trim().isEmpty()) {
            throw new DomainException("Specialty name is required");
        }

        if (specialty.getCodigo().length() > 20) {
            throw new DomainException("Specialty code cannot exceed 20 characters");
        }

        if (specialty.getNombre().length() > 100) {
            throw new DomainException("Specialty name cannot exceed 100 characters");
        }

        if (specialty.getDescripcion() != null && specialty.getDescripcion().length() > 500) {
            throw new DomainException("Specialty description cannot exceed 500 characters");
        }
    }
}