package com.application.infrastructure.persistence.jpa.facturacion;

import com.application.domain.model.facturacion.Factura;
import com.application.domain.model.facturacion.FacturaId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface FacturaJpaRepository extends JpaRepository<Factura, FacturaId> {
    Optional<Factura> findByNumeroFactura(String numeroFactura);

    List<Factura> findByEstado(String estado);

    List<Factura> findByFechaEmisionBetween(LocalDate inicio, LocalDate fin);

    List<Factura> findByFechaVencimientoBeforeAndEstadoNot(LocalDate fecha, String estado);

    @Query("SELECT f FROM Factura f WHERE f.alumnoId = :alumnoId ORDER BY f.fechaEmision DESC")
    List<Factura> findByAlumnoId(@Param("alumnoId") String alumnoId);

    @Query("SELECT f FROM Factura f WHERE f.periodoFacturadoInicio <= :fecha AND f.periodoFacturadoFin >= :fecha AND f.alumnoId = :alumnoId")
    Optional<Factura> findByAlumnoIdAndPeriodo(@Param("alumnoId") String alumnoId, @Param("fecha") LocalDate fecha);

    boolean existsByNumeroFactura(String numeroFactura);
}