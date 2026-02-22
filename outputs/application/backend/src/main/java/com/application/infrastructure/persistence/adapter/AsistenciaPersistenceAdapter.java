package com.application.infrastructure.persistence.adapter;

import com.application.domain.model.asistencia.RegistroAsistencia;
import com.application.domain.model.asistencia.RegistroAsistenciaId;
import com.application.domain.shared.Repository;
import com.application.infrastructure.persistence.jpa.asistencia.RegistroAsistenciaJpaEntity;
import com.application.infrastructure.persistence.jpa.asistencia.RegistroAsistenciaJpaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class AsistenciaPersistenceAdapter implements Repository<RegistroAsistencia, RegistroAsistenciaId> {

    private final RegistroAsistenciaJpaRepository jpaRepository;

    @Override
    public RegistroAsistencia save(RegistroAsistencia registro) {
        RegistroAsistenciaJpaEntity jpaEntity = RegistroAsistenciaJpaEntity.fromDomain(registro);
        RegistroAsistenciaJpaEntity savedEntity = jpaRepository.save(jpaEntity);
        return savedEntity.toDomain();
    }

    @Override
    public Optional<RegistroAsistencia> findById(RegistroAsistenciaId id) {
        return jpaRepository.findById(id.value())
                .map(RegistroAsistenciaJpaEntity::toDomain);
    }

    @Override
    public List<RegistroAsistencia> findAll() {
        return jpaRepository.findAll().stream()
                .map(RegistroAsistenciaJpaEntity::toDomain)
                .collect(Collectors.toList());
    }

    @Override
    public void delete(RegistroAsistencia registro) {
        jpaRepository.deleteById(registro.getId().value());
    }

    @Override
    public boolean existsById(RegistroAsistenciaId id) {
        return jpaRepository.existsById(id.value());
    }
}