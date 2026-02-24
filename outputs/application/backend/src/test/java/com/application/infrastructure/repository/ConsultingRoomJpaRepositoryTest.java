package com.application.infrastructure.repository;

import com.application.infrastructure.entity.ConsultingRoomEntity;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ConsultingRoomJpaRepositoryTest {

    @Mock
    private ConsultingRoomJpaRepository consultingRoomJpaRepository;

    private final UUID clinicId = UUID.randomUUID();
    private final UUID roomId = UUID.randomUUID();
    private final String roomNumber = "101";

    @Test
    void findByNumero_shouldReturnConsultingRoomEntity() {
        ConsultingRoomEntity entity = ConsultingRoomEntity.create(
                roomId,
                roomNumber,
                "Consultorio Principal",
                "Sillón dental, lámpara, computadora",
                true,
                clinicId
        );

        when(consultingRoomJpaRepository.findByNumero(roomNumber)).thenReturn(Optional.of(entity));

        Optional<ConsultingRoomEntity> result = consultingRoomJpaRepository.findByNumero(roomNumber);

        assertThat(result).isPresent();
        assertThat(result.get().getId()).isEqualTo(roomId);
        assertThat(result.get().getNumero()).isEqualTo(roomNumber);
    }

    @Test
    void findByNumero_shouldReturnEmptyWhenNotFound() {
        when(consultingRoomJpaRepository.findByNumero(roomNumber)).thenReturn(Optional.empty());

        Optional<ConsultingRoomEntity> result = consultingRoomJpaRepository.findByNumero(roomNumber);

        assertThat(result).isEmpty();
    }

    @Test
    void findByDisponibleTrue_shouldReturnAvailableRooms() {
        ConsultingRoomEntity entity1 = ConsultingRoomEntity.create(
                UUID.randomUUID(),
                "101",
                "Consultorio 1",
                "Equipamiento básico",
                true,
                clinicId
        );
        ConsultingRoomEntity entity2 = ConsultingRoomEntity.create(
                UUID.randomUUID(),
                "102",
                "Consultorio 2",
                "Equipamiento avanzado",
                true,
                clinicId
        );

        List<ConsultingRoomEntity> availableRooms = List.of(entity1, entity2);

        when(consultingRoomJpaRepository.findByDisponibleTrue()).thenReturn(availableRooms);

        List<ConsultingRoomEntity> result = consultingRoomJpaRepository.findByDisponibleTrue();

        assertThat(result).hasSize(2);
        assertThat(result).allMatch(ConsultingRoomEntity::getDisponible);
    }

    @Test
    void findByClinicId_shouldReturnRoomsForClinic() {
        UUID otherClinicId = UUID.randomUUID();
        ConsultingRoomEntity entity1 = ConsultingRoomEntity.create(
                UUID.randomUUID(),
                "101",
                "Consultorio A",
                "Equipamiento 1",
                true,
                clinicId
        );
        ConsultingRoomEntity entity2 = ConsultingRoomEntity.create(
                UUID.randomUUID(),
                "201",
                "Consultorio B",
                "Equipamiento 2",
                false,
                clinicId
        );

        List<ConsultingRoomEntity> clinicRooms = List.of(entity1, entity2);

        when(consultingRoomJpaRepository.findByClinicId(clinicId)).thenReturn(clinicRooms);

        List<ConsultingRoomEntity> result = consultingRoomJpaRepository.findByClinicId(clinicId);

        assertThat(result).hasSize(2);
        assertThat(result).allMatch(room -> room.getClinicId().equals(clinicId));
    }

    @Test
    void findByClinicIdAndDisponibleTrue_shouldReturnAvailableRoomsForClinic() {
        ConsultingRoomEntity availableEntity = ConsultingRoomEntity.create(
                UUID.randomUUID(),
                "101",
                "Consultorio Disponible",
                "Equipamiento completo",
                true,
                clinicId
        );
        ConsultingRoomEntity unavailableEntity = ConsultingRoomEntity.create(
                UUID.randomUUID(),
                "102",
                "Consultorio No Disponible",
                "Equipamiento básico",
                false,
                clinicId
        );

        List<ConsultingRoomEntity> availableRooms = List.of(availableEntity);

        when(consultingRoomJpaRepository.findByClinicIdAndDisponibleTrue(clinicId)).thenReturn(availableRooms);

        List<ConsultingRoomEntity> result = consultingRoomJpaRepository.findByClinicIdAndDisponibleTrue(clinicId);

        assertThat(result).hasSize(1);
        assertThat(result.get(0).getClinicId()).isEqualTo(clinicId);
        assertThat(result.get(0).getDisponible()).isTrue();
    }

    @Test
    void existsByNumero_shouldReturnTrueWhenNumberExists() {
        when(consultingRoomJpaRepository.existsByNumero(roomNumber)).thenReturn(true);

        boolean result = consultingRoomJpaRepository.existsByNumero(roomNumber);

        assertThat(result).isTrue();
    }

    @Test
    void existsByNumero_shouldReturnFalseWhenNumberDoesNotExist() {
        when(consultingRoomJpaRepository.existsByNumero(roomNumber)).thenReturn(false);

        boolean result = consultingRoomJpaRepository.existsByNumero(roomNumber);

        assertThat(result).isFalse();
    }
}