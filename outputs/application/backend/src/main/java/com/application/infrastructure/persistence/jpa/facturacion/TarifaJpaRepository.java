package com.application.infrastructure.persistence.jpa.facturacion;

import com.application.domain.model.facturacion.Tarifa;
import com.application.domain.model.facturacion.TarifaId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface TarifaJpaRepository extends JpaRepository<Tarifa, TarifaId> {
    Optional<Tarifa> findByNombreAndActivoTrue(String nombre);
    List<Tarifa> findByActivoTrue();
    List<Tarifa> findByFechaInicioLessThanEqualAndFechaFinGreaterThanEqual(LocalDate fechaInicio, LocalDate fechaFin);
    @Query("SELECT t FROM Tarifa t WHERE t.activo = true AND :fecha BETWEEN t.fechaInicio AND t.fechaFin")
    List<Tarifa> findTarifasActivasEnFecha(@Param("fecha") LocalDate fecha);
    boolean existsByNombreAndActivoTrue(String nombre);
}