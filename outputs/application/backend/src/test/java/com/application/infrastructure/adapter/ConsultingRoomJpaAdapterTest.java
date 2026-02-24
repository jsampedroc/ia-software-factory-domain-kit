package com.application.infrastructure.adapter;

import com.application.domain.model.ConsultingRoom;
import com.application.domain.valueobject.ClinicId;
import com.application.domain.valueobject.ConsultingRoomId;
import com.application.infrastructure.entity.ConsultingRoomEntity;
import com.application.infrastructure.repository.ConsultingRoomJpaRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ConsultingRoomJpaAdapterTest {

    @Mock
    private ConsultingRoomJpaRepository consultingRoomJpaRepository;

    @InjectMocks
    private ConsultingRoomJpaAdapter consultingRoomJpaAdapter;

    @Test
    void save_ShouldSaveAndReturnDomain() {
        ConsultingRoomId roomId = new ConsultingRoomId(UUID.randomUUID());
        ClinicId clinicId = new ClinicId(UUID.randomUUID());
        ConsultingRoom domain = ConsultingRoom.create(
                roomId,
                "101",
                "Consultorio Principal",
                "Sillón dental, Rayos X",
                true,
                clinicId
        );
        ConsultingRoomEntity entity = ConsultingRoomEntity.fromDomain(domain);
        when(consultingRoomJpaRepository.save(any(ConsultingRoomEntity.class))).thenReturn(entity);

        ConsultingRoom result = consultingRoomJpaAdapter.save(domain);

        assertThat(result).isNotNull();
        assertThat(result.getId()).isEqualTo(roomId);
        verify(consultingRoomJpaRepository).save(any(ConsultingRoomEntity.class));
    }

    @Test
    void findById_WhenExists_ShouldReturnDomain() {
        ConsultingRoomId roomId = new ConsultingRoomId(UUID.randomUUID());
        ClinicId clinicId = new ClinicId(UUID.randomUUID());
        ConsultingRoom domain = ConsultingRoom.create(
                roomId,
                "101",
                "Consultorio Principal",
                "Sillón dental, Rayos X",
                true,
                clinicId
        );
        ConsultingRoomEntity entity = ConsultingRoomEntity.fromDomain(domain);
        when(consultingRoomJpaRepository.findById(roomId.getValue())).thenReturn(Optional.of(entity));

        Optional<ConsultingRoom> result = consultingRoomJpaAdapter.findById(roomId);

        assertThat(result).isPresent();
        assertThat(result.get().getId()).isEqualTo(roomId);
        verify(consultingRoomJpaRepository).findById(roomId.getValue());
    }

    @Test
    void findById_WhenNotExists_ShouldReturnEmpty() {
        ConsultingRoomId roomId = new ConsultingRoomId(UUID.randomUUID());
        when(consultingRoomJpaRepository.findById(roomId.getValue())).thenReturn(Optional.empty());

        Optional<ConsultingRoom> result = consultingRoomJpaAdapter.findById(roomId);

        assertThat(result).isEmpty();
        verify(consultingRoomJpaRepository).findById(roomId.getValue());
    }

    @Test
    void findAll_ShouldReturnAllDomains() {
        ConsultingRoomId roomId1 = new ConsultingRoomId(UUID.randomUUID());
        ConsultingRoomId roomId2 = new ConsultingRoomId(UUID.randomUUID());
        ClinicId clinicId = new ClinicId(UUID.randomUUID());

        ConsultingRoom domain1 = ConsultingRoom.create(
                roomId1,
                "101",
                "Consultorio 1",
                "Equipo básico",
                true,
                clinicId
        );
        ConsultingRoom domain2 = ConsultingRoom.create(
                roomId2,
                "102",
                "Consultorio 2",
                "Equipo avanzado",
                false,
                clinicId
        );
        ConsultingRoomEntity entity1 = ConsultingRoomEntity.fromDomain(domain1);
        ConsultingRoomEntity entity2 = ConsultingRoomEntity.fromDomain(domain2);
        when(consultingRoomJpaRepository.findAll()).thenReturn(List.of(entity1, entity2));

        List<ConsultingRoom> result = consultingRoomJpaAdapter.findAll();

        assertThat(result).hasSize(2);
        assertThat(result).extracting(ConsultingRoom::getId).containsExactly(roomId1, roomId2);
        verify(consultingRoomJpaRepository).findAll();
    }

    @Test
    void deleteById_ShouldCallRepositoryDelete() {
        ConsultingRoomId roomId = new ConsultingRoomId(UUID.randomUUID());

        consultingRoomJpaAdapter.deleteById(roomId);

        verify(consultingRoomJpaRepository).deleteById(roomId.getValue());
    }

    @Test
    void existsById_WhenExists_ShouldReturnTrue() {
        ConsultingRoomId roomId = new ConsultingRoomId(UUID.randomUUID());
        when(consultingRoomJpaRepository.existsById(roomId.getValue())).thenReturn(true);

        boolean result = consultingRoomJpaAdapter.existsById(roomId);

        assertThat(result).isTrue();
        verify(consultingRoomJpaRepository).existsById(roomId.getValue());
    }

    @Test
    void existsById_WhenNotExists_ShouldReturnFalse() {
        ConsultingRoomId roomId = new ConsultingRoomId(UUID.randomUUID());
        when(consultingRoomJpaRepository.existsById(roomId.getValue())).thenReturn(false);

        boolean result = consultingRoomJpaAdapter.existsById(roomId);

        assertThat(result).isFalse();
        verify(consultingRoomJpaRepository).existsById(roomId.getValue());
    }

    @Test
    void findByClinicIdAndAvailable_ShouldReturnFilteredDomains() {
        ClinicId clinicId = new ClinicId(UUID.randomUUID());
        ConsultingRoomId roomId1 = new ConsultingRoomId(UUID.randomUUID());
        ConsultingRoomId roomId2 = new ConsultingRoomId(UUID.randomUUID());

        ConsultingRoom domain1 = ConsultingRoom.create(
                roomId1,
                "101",
                "Consultorio Disponible",
                "Equipo",
                true,
                clinicId
        );
        ConsultingRoom domain2 = ConsultingRoom.create(
                roomId2,
                "102",
                "Consultorio Ocupado",
                "Equipo",
                false,
                clinicId
        );
        ConsultingRoomEntity entity1 = ConsultingRoomEntity.fromDomain(domain1);
        ConsultingRoomEntity entity2 = ConsultingRoomEntity.fromDomain(domain2);
        when(consultingRoomJpaRepository.findByClinicIdAndDisponible(clinicId.getValue(), true))
                .thenReturn(List.of(entity1));

        List<ConsultingRoom> result = consultingRoomJpaAdapter.findByClinicIdAndAvailable(clinicId, true);

        assertThat(result).hasSize(1);
        assertThat(result.get(0).getId()).isEqualTo(roomId1);
        assertThat(result.get(0).isDisponible()).isTrue();
        verify(consultingRoomJpaRepository).findByClinicIdAndDisponible(clinicId.getValue(), true);
    }

    @Test
    void findByClinicId_ShouldReturnAllRoomsForClinic() {
        ClinicId clinicId = new ClinicId(UUID.randomUUID());
        ConsultingRoomId roomId1 = new ConsultingRoomId(UUID.randomUUID());
        ConsultingRoomId roomId2 = new ConsultingRoomId(UUID.randomUUID());

        ConsultingRoom domain1 = ConsultingRoom.create(
                roomId1,
                "101",
                "Consultorio 1",
                "Equipo A",
                true,
                clinicId
        );
        ConsultingRoom domain2 = ConsultingRoom.create(
                roomId2,
                "102",
                "Consultorio 2",
                "Equipo B",
                false,
                clinicId
        );
        ConsultingRoomEntity entity1 = ConsultingRoomEntity.fromDomain(domain1);
        ConsultingRoomEntity entity2 = ConsultingRoomEntity.fromDomain(domain2);
        when(consultingRoomJpaRepository.findByClinicId(clinicId.getValue()))
                .thenReturn(List.of(entity1, entity2));

        List<ConsultingRoom> result = consultingRoomJpaAdapter.findByClinicId(clinicId);

        assertThat(result).hasSize(2);
        assertThat(result).extracting(ConsultingRoom::getId).containsExactly(roomId1, roomId2);
        verify(consultingRoomJpaRepository).findByClinicId(clinicId.getValue());
    }
}