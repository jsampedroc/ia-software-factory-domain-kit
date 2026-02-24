package com.application.infrastructure.adapter;

import com.application.domain.model.MedicalHistory;
import com.application.domain.port.MedicalHistoryRepositoryPort;
import com.application.domain.valueobject.MedicalHistoryId;
import com.application.domain.valueobject.PatientId;
import com.application.infrastructure.entity.MedicalHistoryEntity;
import com.application.infrastructure.repository.MedicalHistoryJpaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class MedicalHistoryJpaAdapter implements MedicalHistoryRepositoryPort {

    private final MedicalHistoryJpaRepository medicalHistoryJpaRepository;

    @Override
    public MedicalHistory save(MedicalHistory medicalHistory) {
        MedicalHistoryEntity entity = MedicalHistoryEntity.fromDomain(medicalHistory);
        MedicalHistoryEntity savedEntity = medicalHistoryJpaRepository.save(entity);
        return savedEntity.toDomain();
    }

    @Override
    public Optional<MedicalHistory> findById(MedicalHistoryId id) {
        return medicalHistoryJpaRepository.findById(id.getValue())
                .map(MedicalHistoryEntity::toDomain);
    }

    @Override
    public List<MedicalHistory> findAll() {
        return medicalHistoryJpaRepository.findAll().stream()
                .map(MedicalHistoryEntity::toDomain)
                .collect(Collectors.toList());
    }

    @Override
    public void deleteById(MedicalHistoryId id) {
        medicalHistoryJpaRepository.deleteById(id.getValue());
    }

    @Override
    public boolean existsById(MedicalHistoryId id) {
        return medicalHistoryJpaRepository.existsById(id.getValue());
    }

    @Override
    public Optional<MedicalHistory> findByPatientId(PatientId patientId) {
        return medicalHistoryJpaRepository.findByPatientId(patientId.getValue())
                .map(MedicalHistoryEntity::toDomain);
    }
}