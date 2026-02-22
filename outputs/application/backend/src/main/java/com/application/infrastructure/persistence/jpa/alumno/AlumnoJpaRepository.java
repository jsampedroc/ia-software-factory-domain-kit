package com.application.infrastructure.persistence.jpa.alumno;

import com.application.domain.model.alumno.Alumno;
import com.application.domain.model.alumno.AlumnoId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface AlumnoJpaRepository extends JpaRepository<AlumnoJpaEntity, AlumnoId> {
    Optional<AlumnoJpaEntity> findByNumeroMatricula(String numeroMatricula);
    List<AlumnoJpaEntity> findByActivoTrue();
    List<AlumnoJpaEntity> findByFechaAltaAfter(LocalDate fecha);
    @Query("SELECT a FROM AlumnoJpaEntity a WHERE a.fechaNacimiento BETWEEN :start AND :end")
    List<AlumnoJpaEntity> findAlumnosByFechaNacimientoBetween(@Param("start") LocalDate start, @Param("end") LocalDate end);
}