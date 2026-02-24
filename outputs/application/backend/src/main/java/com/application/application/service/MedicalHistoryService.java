package com.application.application.service;

import com.application.domain.model.MedicalHistory;
import com.application.domain.port.MedicalHistoryRepositoryPort;
import com.application.domain.valueobject.MedicalHistoryId;
import com.application.domain.valueobject.PatientId;
import com.application.application.dto.MedicalHistoryDTO;
import com.application.domain.exception.DomainException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class MedicalHistoryService {

    private final MedicalHistoryRepositoryPort medicalHistoryRepositoryPort;

    @Transactional
    public MedicalHistoryDTO create(MedicalHistoryDTO dto) {
        validateMedicalHistoryData(dto);
        PatientId patientId = PatientId.of(dto.getPatientId());

        // Validar que no exista ya un historial para este paciente
        medicalHistoryRepositoryPort.findByPatientId(patientId).ifPresent(history -> {
            throw new DomainException("Ya existe un historial médico para el paciente con ID: " + dto.getPatientId());
        });

        MedicalHistory medicalHistory = MedicalHistory.builder()
                .patientId(patientId)
                .allergies(dto.getAllergies())
                .medicalConditions(dto.getMedicalConditions())
                .medications(dto.getMedications())
                .generalObservations(dto.getGeneralObservations())
                .build();

        medicalHistory = medicalHistoryRepositoryPort.save(medicalHistory);
        return mapToDTO(medicalHistory);
    }

    @Transactional
    public MedicalHistoryDTO update(String id, MedicalHistoryDTO dto) {
        MedicalHistoryId historyId = MedicalHistoryId.of(id);
        MedicalHistory existingHistory = medicalHistoryRepositoryPort.findById(historyId)
                .orElseThrow(() -> new DomainException("Historial médico no encontrado con ID: " + id));

        validateMedicalHistoryData(dto);

        existingHistory.update(
                dto.getAllergies(),
                dto.getMedicalConditions(),
                dto.getMedications(),
                dto.getGeneralObservations()
        );

        existingHistory = medicalHistoryRepositoryPort.save(existingHistory);
        return mapToDTO(existingHistory);
    }

    public MedicalHistoryDTO findById(String id) {
        MedicalHistoryId historyId = MedicalHistoryId.of(id);
        MedicalHistory medicalHistory = medicalHistoryRepositoryPort.findById(historyId)
                .orElseThrow(() -> new DomainException("Historial médico no encontrado con ID: " + id));
        return mapToDTO(medicalHistory);
    }

    public MedicalHistoryDTO findByPatientId(String patientId) {
        PatientId pid = PatientId.of(patientId);
        MedicalHistory medicalHistory = medicalHistoryRepositoryPort.findByPatientId(pid)
                .orElseThrow(() -> new DomainException("Historial médico no encontrado para el paciente con ID: " + patientId));
        return mapToDTO(medicalHistory);
    }

    public List<MedicalHistoryDTO> findAll() {
        return medicalHistoryRepositoryPort.findAll().stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    @Transactional
    public void delete(String id) {
        MedicalHistoryId historyId = MedicalHistoryId.of(id);
        if (!medicalHistoryRepositoryPort.existsById(historyId)) {
            throw new DomainException("Historial médico no encontrado con ID: " + id);
        }
        medicalHistoryRepositoryPort.deleteById(historyId);
    }

    @Transactional
    public MedicalHistoryDTO addObservation(String id, String observation) {
        MedicalHistoryId historyId = MedicalHistoryId.of(id);
        MedicalHistory medicalHistory = medicalHistoryRepositoryPort.findById(historyId)
                .orElseThrow(() -> new DomainException("Historial médico no encontrado con ID: " + id));

        String updatedObservations = medicalHistory.getGeneralObservations() == null ?
                observation : medicalHistory.getGeneralObservations() + "\n" + observation;

        medicalHistory.update(
                medicalHistory.getAllergies(),
                medicalHistory.getMedicalConditions(),
                medicalHistory.getMedications(),
                updatedObservations
        );

        medicalHistory = medicalHistoryRepositoryPort.save(medicalHistory);
        return mapToDTO(medicalHistory);
    }

    private void validateMedicalHistoryData(MedicalHistoryDTO dto) {
        if (dto.getPatientId() == null) {
            throw new DomainException("El ID del paciente es obligatorio");
        }
    }

    private MedicalHistoryDTO mapToDTO(MedicalHistory medicalHistory) {
        MedicalHistoryDTO dto = new MedicalHistoryDTO();
        dto.setId(medicalHistory.getId().getValue().toString());
        dto.setPatientId(medicalHistory.getPatientId().getValue().toString());
        dto.setCreationDate(medicalHistory.getCreationDate());
        dto.setLastUpdate(medicalHistory.getLastUpdate());
        dto.setAllergies(medicalHistory.getAllergies());
        dto.setMedicalConditions(medicalHistory.getMedicalConditions());
        dto.setMedications(medicalHistory.getMedications());
        dto.setGeneralObservations(medicalHistory.getGeneralObservations());
        return dto;
    }
}