package com.application.infrastructure.repository;

import com.application.infrastructure.entity.ConsultingRoomEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface ConsultingRoomJpaRepository extends JpaRepository<ConsultingRoomEntity, UUID> {
    Optional<ConsultingRoomEntity> findByNumero(String numero);
    List<ConsultingRoomEntity> findByDisponibleTrue();
    List<ConsultingRoomEntity> findByClinicId(UUID clinicId);
    List<ConsultingRoomEntity> findByClinicIdAndDisponibleTrue(UUID clinicId);
    boolean existsByNumero(String numero);
}