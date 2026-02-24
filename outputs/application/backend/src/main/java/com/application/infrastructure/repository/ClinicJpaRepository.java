package com.application.infrastructure.repository;

import com.application.infrastructure.entity.ClinicEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface ClinicJpaRepository extends JpaRepository<ClinicEntity, UUID> {
    Optional<ClinicEntity> findByCodigo(String codigo);
    Optional<ClinicEntity> findByNombre(String nombre);
    boolean existsByCodigo(String codigo);
    boolean existsByNombre(String nombre);
}