package com.application.infrastructure.repository;

import com.application.infrastructure.entity.SpecialtyEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface SpecialtyJpaRepository extends JpaRepository<SpecialtyEntity, UUID> {
    Optional<SpecialtyEntity> findByCodigo(String codigo);
    boolean existsByCodigo(String codigo);
    boolean existsByNombre(String nombre);
}