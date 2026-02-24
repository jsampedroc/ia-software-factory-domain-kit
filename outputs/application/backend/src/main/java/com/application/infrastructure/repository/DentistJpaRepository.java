package com.application.infrastructure.repository;

import com.application.infrastructure.entity.DentistEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface DentistJpaRepository extends JpaRepository<DentistEntity, UUID> {

    Optional<DentistEntity> findByLicenciaMedica(String licenciaMedica);

    List<DentistEntity> findByActivoTrue();

    List<DentistEntity> findByActivoFalse();

    List<DentistEntity> findByFechaContratacionAfter(LocalDate fecha);

    List<DentistEntity> findByFechaContratacionBefore(LocalDate fecha);

    @Query("SELECT d FROM DentistEntity d WHERE d.email = :email")
    Optional<DentistEntity> findByEmail(@Param("email") String email);

    @Query("SELECT d FROM DentistEntity d WHERE d.telefono = :telefono")
    Optional<DentistEntity> findByTelefono(@Param("telefono") String telefono);

    @Query("SELECT d FROM DentistEntity d WHERE d.nombre = :nombre AND d.apellido = :apellido")
    List<DentistEntity> findByNombreAndApellido(@Param("nombre") String nombre, @Param("apellido") String apellido);

    @Query("SELECT d FROM DentistEntity d JOIN d.especialidades ds JOIN ds.specialty s WHERE s.id = :specialtyId")
    List<DentistEntity> findBySpecialtyId(@Param("specialtyId") UUID specialtyId);

    @Query("SELECT d FROM DentistEntity d JOIN d.clinicasAsignadas dc JOIN dc.clinic c WHERE c.id = :clinicId AND d.activo = true")
    List<DentistEntity> findActiveByClinicId(@Param("clinicId") UUID clinicId);

    boolean existsByLicenciaMedica(String licenciaMedica);

    boolean existsByEmail(String email);

    boolean existsByTelefono(String telefono);
}