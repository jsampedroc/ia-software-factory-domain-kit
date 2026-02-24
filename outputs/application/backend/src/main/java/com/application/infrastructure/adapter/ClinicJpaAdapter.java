package com.application.infrastructure.adapter;

import com.application.domain.model.Clinic;
import com.application.domain.port.ClinicRepositoryPort;
import com.application.domain.valueobject.ClinicId;
import com.application.infrastructure.entity.ClinicEntity;
import com.application.infrastructure.repository.ClinicJpaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class ClinicJpaAdapter implements ClinicRepositoryPort {

    private final ClinicJpaRepository clinicJpaRepository;

    @Override
    public Clinic save(Clinic clinic) {
        ClinicEntity entity = ClinicEntity.fromDomain(clinic);
        ClinicEntity savedEntity = clinicJpaRepository.save(entity);
        return savedEntity.toDomain();
    }

    @Override
    public Optional<Clinic> findById(ClinicId id) {
        return clinicJpaRepository.findById(id.getValue())
                .map(ClinicEntity::toDomain);
    }

    @Override
    public List<Clinic> findAll() {
        return clinicJpaRepository.findAll().stream()
                .map(ClinicEntity::toDomain)
                .collect(Collectors.toList());
    }

    @Override
    public List<Clinic> findAllActive() {
        return clinicJpaRepository.findByActivaTrue().stream()
                .map(ClinicEntity::toDomain)
                .collect(Collectors.toList());
    }

    @Override
    public void deleteById(ClinicId id) {
        clinicJpaRepository.deleteById(id.getValue());
    }

    @Override
    public boolean existsById(ClinicId id) {
        return clinicJpaRepository.existsById(id.getValue());
    }

    @Override
    public Optional<Clinic> findByCode(String code) {
        return clinicJpaRepository.findByCodigo(code)
                .map(ClinicEntity::toDomain);
    }
}