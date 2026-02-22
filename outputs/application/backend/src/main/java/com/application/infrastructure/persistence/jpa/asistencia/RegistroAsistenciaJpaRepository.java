package com.application.infrastructure.persistence.jpa.asistencia;

import com.application.domain.model.asistencia.RegistroAsistencia;
import com.application.domain.shared.EntityId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface RegistroAsistenciaJpaRepository extends JpaRepository<RegistroAsistencia, EntityId> {
    @Query("SELECT r FROM RegistroAsistencia r WHERE r.alumnoId = :alumnoId AND r.fecha = :fecha")
    Optional<RegistroAsistencia> findByAlumnoIdAndFecha(@Param("alumnoId") EntityId alumnoId, @Param("fecha") LocalDate fecha);

    List<RegistroAsistencia> findByAlumnoId(EntityId alumnoId);

    List<RegistroAsistencia> findByFechaBetween(LocalDate inicio, LocalDate fin);

    List<RegistroAsistencia> findByAlumnoIdAndFechaBetween(EntityId alumnoId, LocalDate inicio, LocalDate fin);
}