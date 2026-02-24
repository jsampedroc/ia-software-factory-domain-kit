package com.application.infrastructure.repository;

import com.application.infrastructure.entity.TreatmentEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface TreatmentJpaRepository extends JpaRepository<TreatmentEntity, UUID> {

    Optional<TreatmentEntity> findByTreatmentId(UUID treatmentId);

    @Query("SELECT t FROM TreatmentEntity t WHERE t.codigo = :codigo")
    Optional<TreatmentEntity> findByCodigo(@Param("codigo") String codigo);

    @Query("SELECT t FROM TreatmentEntity t WHERE t.activo = true")
    List<TreatmentEntity> findAllActive();

    @Query("SELECT t FROM TreatmentEntity t WHERE t.activo = false")
    List<TreatmentEntity> findAllInactive();

    @Query("SELECT t FROM TreatmentEntity t WHERE t.nombre LIKE %:nombre%")
    List<TreatmentEntity> findByNombreContaining(@Param("nombre") String nombre);

    boolean existsByCodigo(String codigo);
}