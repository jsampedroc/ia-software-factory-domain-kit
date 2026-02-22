package com.application.infrastructure.persistence.adapter;

import com.application.domain.model.colegio.Colegio;
import com.application.domain.model.colegio.ColegioId;
import com.application.domain.model.colegio.Clase;
import com.application.domain.model.colegio.ClaseId;
import com.application.infrastructure.persistence.jpa.colegio.ColegioJpaEntity;
import com.application.infrastructure.persistence.jpa.colegio.ClaseJpaEntity;
import com.application.infrastructure.persistence.jpa.colegio.ColegioJpaRepository;
import com.application.domain.spi.persistence.ColegioPersistencePort;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Component
public class ColegioPersistenceAdapter implements ColegioPersistencePort {

    private final ColegioJpaRepository colegioJpaRepository;

    public ColegioPersistenceAdapter(ColegioJpaRepository colegioJpaRepository) {
        this.colegioJpaRepository = colegioJpaRepository;
    }

    @Override
    @Transactional
    public Colegio save(Colegio colegio) {
        ColegioJpaEntity jpaEntity = ColegioJpaEntity.fromDomain(colegio);
        ColegioJpaEntity savedEntity = colegioJpaRepository.save(jpaEntity);
        return savedEntity.toDomain();
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Colegio> findById(ColegioId id) {
        return colegioJpaRepository.findById(id.value())
                .map(ColegioJpaEntity::toDomain);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Colegio> findAll() {
        return colegioJpaRepository.findAll().stream()
                .map(ColegioJpaEntity::toDomain)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public void deleteById(ColegioId id) {
        colegioJpaRepository.deleteById(id.value());
    }

    @Override
    @Transactional(readOnly = true)
    public boolean existsById(ColegioId id) {
        return colegioJpaRepository.existsById(id.value());
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Colegio> findByCodigoCentro(String codigoCentro) {
        return colegioJpaRepository.findByCodigoCentro(codigoCentro)
                .map(ColegioJpaEntity::toDomain);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Clase> findClasesByColegioId(ColegioId colegioId) {
        Optional<ColegioJpaEntity> colegioOpt = colegioJpaRepository.findById(colegioId.value());
        return colegioOpt.map(colegioJpaEntity ->
                colegioJpaEntity.getClases().stream()
                        .map(ClaseJpaEntity::toDomain)
                        .collect(Collectors.toList())
        ).orElse(List.of());
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Clase> findClaseById(ClaseId claseId) {
        return colegioJpaRepository.findClaseById(claseId.value())
                .map(ClaseJpaEntity::toDomain);
    }
}