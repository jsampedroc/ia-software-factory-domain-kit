package com.application.application.service;

import com.application.domain.model.Clinic;
import com.application.domain.valueobject.ClinicId;
import com.application.domain.port.ClinicRepositoryPort;
import com.application.application.dto.ClinicDTO;
import com.application.domain.exception.DomainException;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ClinicService {

    private final ClinicRepositoryPort clinicRepositoryPort;

    @Transactional
    public ClinicDTO createClinic(ClinicDTO clinicDTO) {
        validateClinicData(clinicDTO);
        checkUniqueCode(clinicDTO.getCodigo());

        Clinic clinic = Clinic.create(
                clinicDTO.getCodigo(),
                clinicDTO.getNombre(),
                clinicDTO.getDireccion(),
                clinicDTO.getTelefono(),
                clinicDTO.getEmail(),
                clinicDTO.getHorarioApertura(),
                clinicDTO.getHorarioCierre()
        );

        Clinic savedClinic = clinicRepositoryPort.save(clinic);
        return ClinicDTO.fromDomain(savedClinic);
    }

    public ClinicDTO getClinicById(ClinicId clinicId) {
        return clinicRepositoryPort.findById(clinicId)
                .map(ClinicDTO::fromDomain)
                .orElseThrow(() -> new DomainException("Clínica no encontrada con ID: " + clinicId));
    }

    public List<ClinicDTO> getAllClinics() {
        return clinicRepositoryPort.findAll().stream()
                .map(ClinicDTO::fromDomain)
                .collect(Collectors.toList());
    }

    public List<ClinicDTO> getActiveClinics() {
        return clinicRepositoryPort.findByActivaTrue().stream()
                .map(ClinicDTO::fromDomain)
                .collect(Collectors.toList());
    }

    @Transactional
    public ClinicDTO updateClinic(ClinicId clinicId, ClinicDTO clinicDTO) {
        validateClinicData(clinicDTO);

        Clinic existingClinic = clinicRepositoryPort.findById(clinicId)
                .orElseThrow(() -> new DomainException("Clínica no encontrada con ID: " + clinicId));

        if (!existingClinic.getCodigo().equals(clinicDTO.getCodigo())) {
            checkUniqueCode(clinicDTO.getCodigo());
        }

        existingClinic.update(
                clinicDTO.getCodigo(),
                clinicDTO.getNombre(),
                clinicDTO.getDireccion(),
                clinicDTO.getTelefono(),
                clinicDTO.getEmail(),
                clinicDTO.getHorarioApertura(),
                clinicDTO.getHorarioCierre(),
                clinicDTO.getActiva()
        );

        Clinic updatedClinic = clinicRepositoryPort.save(existingClinic);
        return ClinicDTO.fromDomain(updatedClinic);
    }

    @Transactional
    public void deactivateClinic(ClinicId clinicId) {
        Clinic clinic = clinicRepositoryPort.findById(clinicId)
                .orElseThrow(() -> new DomainException("Clínica no encontrada con ID: " + clinicId));

        clinic.deactivate();
        clinicRepositoryPort.save(clinic);
    }

    @Transactional
    public void activateClinic(ClinicId clinicId) {
        Clinic clinic = clinicRepositoryPort.findById(clinicId)
                .orElseThrow(() -> new DomainException("Clínica no encontrada con ID: " + clinicId));

        clinic.activate();
        clinicRepositoryPort.save(clinic);
    }

    @Transactional
    public void deleteClinic(ClinicId clinicId) {
        if (!clinicRepositoryPort.existsById(clinicId)) {
            throw new DomainException("Clínica no encontrada con ID: " + clinicId);
        }
        clinicRepositoryPort.deleteById(clinicId);
    }

    private void validateClinicData(ClinicDTO clinicDTO) {
        if (clinicDTO.getCodigo() == null || clinicDTO.getCodigo().trim().isEmpty()) {
            throw new DomainException("El código de la clínica es obligatorio");
        }
        if (clinicDTO.getNombre() == null || clinicDTO.getNombre().trim().isEmpty()) {
            throw new DomainException("El nombre de la clínica es obligatorio");
        }
        if (clinicDTO.getDireccion() == null || clinicDTO.getDireccion().trim().isEmpty()) {
            throw new DomainException("La dirección de la clínica es obligatoria");
        }
        if (clinicDTO.getTelefono() == null || clinicDTO.getTelefono().trim().isEmpty()) {
            throw new DomainException("El teléfono de la clínica es obligatorio");
        }
        if (clinicDTO.getEmail() == null || clinicDTO.getEmail().trim().isEmpty()) {
            throw new DomainException("El email de la clínica es obligatorio");
        }
        if (clinicDTO.getHorarioApertura() == null) {
            throw new DomainException("El horario de apertura es obligatorio");
        }
        if (clinicDTO.getHorarioCierre() == null) {
            throw new DomainException("El horario de cierre es obligatorio");
        }
        if (clinicDTO.getHorarioApertura().isAfter(clinicDTO.getHorarioCierre())) {
            throw new DomainException("El horario de apertura no puede ser después del horario de cierre");
        }
        if (clinicDTO.getHorarioApertura().equals(clinicDTO.getHorarioCierre())) {
            throw new DomainException("El horario de apertura y cierre no pueden ser iguales");
        }
    }

    private void checkUniqueCode(String codigo) {
        Optional<Clinic> existingClinic = clinicRepositoryPort.findByCodigo(codigo);
        if (existingClinic.isPresent()) {
            throw new DomainException("Ya existe una clínica con el código: " + codigo);
        }
    }
}