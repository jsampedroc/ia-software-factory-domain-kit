package com.application.infrastructure.adapter;

import com.application.domain.model.Specialty;
import com.application.domain.port.SpecialtyRepositoryPort;
import com.application.domain.valueobject.SpecialtyId;
import com.application.infrastructure.entity.SpecialtyEntity;
import com.application.infrastructure.repository.SpecialtyJpaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class SpecialtyJpaAdapter implements SpecialtyRepositoryPort {

    private final SpecialtyJpaRepository specialtyJpaRepository;

    @Override
    public Specialty save(Specialty specialty) {
        SpecialtyEntity entity = SpecialtyEntity.fromDomain(specialty);
        SpecialtyEntity savedEntity = specialtyJpaRepository.save(entity);
        return savedEntity.toDomain();
    }

    @Override
    public Optional<Specialty> findById(SpecialtyId specialtyId) {
        return specialtyJpaRepository.findById(specialtyId.getValue())
                .map(SpecialtyEntity::toDomain);
    }

    @Override
    public List<Specialty> findAll() {
        return specialtyJpaRepository.findAll().stream()
                .map(SpecialtyEntity::toDomain)
                .collect(Collectors.toList());
    }

    @Override
    public void deleteById(SpecialtyId specialtyId) {
        specialtyJpaRepository.deleteById(specialtyId.getValue());
    }

    @Override
    public boolean existsById(SpecialtyId specialtyId) {
        return specialtyJpaRepository.existsById(specialtyId.getValue());
    }

    @Override
    public Optional<Specialty> findByCode(String code) {
        return specialtyJpaRepository.findByCodigo(code)
                .map(SpecialtyEntity::toDomain);
    }

    @Override
    public List<Specialty> findByNameContaining(String name) {
        return specialtyJpaRepository.findByNombreContainingIgnoreCase(name).stream()
                .map(SpecialtyEntity::toDomain)
                .collect(Collectors.toList());
    }
}