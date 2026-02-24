package com.application.application.service;

import com.application.domain.model.Treatment;
import com.application.domain.port.TreatmentRepositoryPort;
import com.application.domain.valueobject.TreatmentId;
import com.application.application.dto.TreatmentDTO;
import com.application.application.mapper.TreatmentMapper;
import com.application.domain.exception.DomainException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class TreatmentService {

    private final TreatmentRepositoryPort treatmentRepositoryPort;
    private final TreatmentMapper treatmentMapper;

    @Transactional
    public TreatmentDTO createTreatment(TreatmentDTO treatmentDTO) {
        validateTreatmentData(treatmentDTO);

        Treatment treatment = Treatment.create(
                treatmentDTO.codigo(),
                treatmentDTO.nombre(),
                treatmentDTO.descripcion(),
                treatmentDTO.duracionEstimadaMinutos(),
                treatmentDTO.costoBase()
        );

        Treatment savedTreatment = treatmentRepositoryPort.save(treatment);
        return treatmentMapper.toDTO(savedTreatment);
    }

    public Optional<TreatmentDTO> getTreatmentById(TreatmentId treatmentId) {
        return treatmentRepositoryPort.findById(treatmentId)
                .map(treatmentMapper::toDTO);
    }

    public List<TreatmentDTO> getAllTreatments() {
        return treatmentRepositoryPort.findAll().stream()
                .map(treatmentMapper::toDTO)
                .collect(Collectors.toList());
    }

    public List<TreatmentDTO> getActiveTreatments() {
        return treatmentRepositoryPort.findActiveTreatments().stream()
                .map(treatmentMapper::toDTO)
                .collect(Collectors.toList());
    }

    @Transactional
    public TreatmentDTO updateTreatment(TreatmentId treatmentId, TreatmentDTO treatmentDTO) {
        Treatment existingTreatment = treatmentRepositoryPort.findById(treatmentId)
                .orElseThrow(() -> new DomainException("Tratamiento no encontrado con ID: " + treatmentId.value()));

        validateTreatmentData(treatmentDTO);
        validateTreatmentNotInActivePlan(existingTreatment);

        Treatment updatedTreatment = existingTreatment.update(
                treatmentDTO.codigo(),
                treatmentDTO.nombre(),
                treatmentDTO.descripcion(),
                treatmentDTO.duracionEstimadaMinutos(),
                treatmentDTO.costoBase(),
                treatmentDTO.activo()
        );

        Treatment savedTreatment = treatmentRepositoryPort.save(updatedTreatment);
        return treatmentMapper.toDTO(savedTreatment);
    }

    @Transactional
    public void deactivateTreatment(TreatmentId treatmentId) {
        Treatment treatment = treatmentRepositoryPort.findById(treatmentId)
                .orElseThrow(() -> new DomainException("Tratamiento no encontrado con ID: " + treatmentId.value()));

        validateTreatmentNotInActivePlan(treatment);

        treatment.deactivate();
        treatmentRepositoryPort.save(treatment);
    }

    @Transactional
    public void activateTreatment(TreatmentId treatmentId) {
        Treatment treatment = treatmentRepositoryPort.findById(treatmentId)
                .orElseThrow(() -> new DomainException("Tratamiento no encontrado con ID: " + treatmentId.value()));

        treatment.activate();
        treatmentRepositoryPort.save(treatment);
    }

    private void validateTreatmentData(TreatmentDTO treatmentDTO) {
        if (treatmentDTO.codigo() == null || treatmentDTO.codigo().trim().isEmpty()) {
            throw new DomainException("El código del tratamiento es obligatorio");
        }
        if (treatmentDTO.nombre() == null || treatmentDTO.nombre().trim().isEmpty()) {
            throw new DomainException("El nombre del tratamiento es obligatorio");
        }
        if (treatmentDTO.duracionEstimadaMinutos() == null || treatmentDTO.duracionEstimadaMinutos() <= 0) {
            throw new DomainException("La duración estimada debe ser mayor a cero");
        }
        if (treatmentDTO.costoBase() == null || treatmentDTO.costoBase().compareTo(BigDecimal.ZERO) < 0) {
            throw new DomainException("El costo base no puede ser negativo");
        }
        if (treatmentDTO.costoBase().compareTo(BigDecimal.ZERO) == 0) {
            throw new DomainException("El costo base debe ser mayor a cero");
        }
    }

    private void validateTreatmentNotInActivePlan(Treatment treatment) {
        boolean isInActivePlan = treatmentRepositoryPort.isTreatmentInActivePlan(treatment.getId());
        if (isInActivePlan) {
            throw new DomainException("No se puede modificar/desactivar un tratamiento que está en un plan activo");
        }
    }
}