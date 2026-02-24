package com.application.infrastructure.repository;

import com.application.infrastructure.entity.PatientEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface PatientJpaRepository extends JpaRepository<PatientEntity, UUID> {

    Optional<PatientEntity> findByDni(String dni);

    Optional<PatientEntity> findByEmail(String email);

    List<PatientEntity> findByActivoTrue();

    List<PatientEntity> findByActivoFalse();

    @Query("SELECT p FROM PatientEntity p WHERE p.fechaRegistro < :cutoffDate AND p.activo = true")
    List<PatientEntity> findInactivePatientsSince(@Param("cutoffDate") LocalDateTime cutoffDate);

    @Query("SELECT p FROM PatientEntity p WHERE LOWER(p.nombre) LIKE LOWER(CONCAT('%', :query, '%')) OR LOWER(p.apellido) LIKE LOWER(CONCAT('%', :query, '%')) OR p.dni LIKE CONCAT('%', :query, '%')")
    List<PatientEntity> searchByNombreApellidoOrDni(@Param("query") String query);

    boolean existsByDni(String dni);

    boolean existsByEmail(String email);
}