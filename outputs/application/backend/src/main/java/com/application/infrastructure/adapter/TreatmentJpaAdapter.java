package com.application.infrastructure.adapter;

import com.application.domain.model.Treatment;
import com.application.domain.port.TreatmentRepositoryPort;
import com.application.domain.valueobject.TreatmentId;
import com.application.infrastructure.entity.TreatmentEntity;
import com.application.infrastructure.repository.TreatmentJpaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class TreatmentJpaAdapter implements TreatmentRepositoryPort {

    private final TreatmentJpaRepository treatmentJpaRepository;

    @Override
    public Treatment save(Treatment treatment) {
        TreatmentEntity entity = TreatmentEntity.fromDomain(treatment);
        TreatmentEntity savedEntity = treatmentJpaRepository.save(entity);
        return savedEntity.toDomain();
    }

    @Override
    public Optional<Treatment> findById(TreatmentId id) {
        return treatmentJpaRepository.findById(id.getValue())
                .map(TreatmentEntity::toDomain);
    }

    @Override
    public List<Treatment> findAll() {
        return treatmentJpaRepository.findAll().stream()
                .map(TreatmentEntity::toDomain)
                .collect(Collectors.toList());
    }

    @Override
    public List<Treatment> findAllActive() {
        return treatmentJpaRepository.findByActivoTrue().stream()
                .map(TreatmentEntity::toDomain)
                .collect(Collectors.toList());
    }

    @Override
    public void deleteById(TreatmentId id) {
        treatmentJpaRepository.deleteById(id.getValue());
    }

    @Override
    public boolean existsById(TreatmentId id) {
        return treatmentJpaRepository.existsById(id.getValue());
    }

    @Override
    public Optional<Treatment> findByCode(String code) {
        return treatmentJpaRepository.findByCodigo(code)
                .map(TreatmentEntity::toDomain);
    }
}