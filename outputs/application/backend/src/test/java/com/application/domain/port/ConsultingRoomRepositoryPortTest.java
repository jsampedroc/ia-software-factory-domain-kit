package com.application.domain.port;

import com.application.domain.model.ConsultingRoom;
import com.application.domain.valueobject.ConsultingRoomId;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ConsultingRoomRepositoryPortTest {

    @Mock
    private ConsultingRoomRepositoryPort repositoryPort;

    @Test
    void shouldSaveConsultingRoom() {
        ConsultingRoom consultingRoom = createSampleConsultingRoom();
        when(repositoryPort.save(any(ConsultingRoom.class))).thenReturn(consultingRoom);

        ConsultingRoom saved = repositoryPort.save(consultingRoom);

        assertThat(saved).isNotNull();
        assertThat(saved.getId()).isEqualTo(consultingRoom.getId());
        verify(repositoryPort).save(consultingRoom);
    }

    @Test
    void shouldFindConsultingRoomById() {
        ConsultingRoomId roomId = new ConsultingRoomId(UUID.randomUUID());
        ConsultingRoom consultingRoom = createSampleConsultingRoomWithId(roomId);
        when(repositoryPort.findById(roomId)).thenReturn(Optional.of(consultingRoom));

        Optional<ConsultingRoom> found = repositoryPort.findById(roomId);

        assertThat(found).isPresent();
        assertThat(found.get().getId()).isEqualTo(roomId);
        verify(repositoryPort).findById(roomId);
    }

    @Test
    void shouldReturnEmptyWhenConsultingRoomNotFound() {
        ConsultingRoomId roomId = new ConsultingRoomId(UUID.randomUUID());
        when(repositoryPort.findById(roomId)).thenReturn(Optional.empty());

        Optional<ConsultingRoom> found = repositoryPort.findById(roomId);

        assertThat(found).isEmpty();
        verify(repositoryPort).findById(roomId);
    }

    @Test
    void shouldFindAllConsultingRooms() {
        ConsultingRoom room1 = createSampleConsultingRoom();
        ConsultingRoom room2 = createSampleConsultingRoom();
        List<ConsultingRoom> rooms = List.of(room1, room2);
        when(repositoryPort.findAll()).thenReturn(rooms);

        List<ConsultingRoom> allRooms = repositoryPort.findAll();

        assertThat(allRooms).hasSize(2);
        verify(repositoryPort).findAll();
    }

    @Test
    void shouldDeleteConsultingRoom() {
        ConsultingRoomId roomId = new ConsultingRoomId(UUID.randomUUID());

        repositoryPort.deleteById(roomId);

        verify(repositoryPort).deleteById(roomId);
    }

    @Test
    void shouldCheckIfConsultingRoomExists() {
        ConsultingRoomId roomId = new ConsultingRoomId(UUID.randomUUID());
        when(repositoryPort.existsById(roomId)).thenReturn(true);

        boolean exists = repositoryPort.existsById(roomId);

        assertThat(exists).isTrue();
        verify(repositoryPort).existsById(roomId);
    }

    @Test
    void shouldReturnFalseWhenConsultingRoomDoesNotExist() {
        ConsultingRoomId roomId = new ConsultingRoomId(UUID.randomUUID());
        when(repositoryPort.existsById(roomId)).thenReturn(false);

        boolean exists = repositoryPort.existsById(roomId);

        assertThat(exists).isFalse();
        verify(repositoryPort).existsById(roomId);
    }

    @Test
    void shouldCountConsultingRooms() {
        when(repositoryPort.count()).thenReturn(5L);

        long count = repositoryPort.count();

        assertThat(count).isEqualTo(5L);
        verify(repositoryPort).count();
    }

    private ConsultingRoom createSampleConsultingRoom() {
        ConsultingRoomId roomId = new ConsultingRoomId(UUID.randomUUID());
        return createSampleConsultingRoomWithId(roomId);
    }

    private ConsultingRoom createSampleConsultingRoomWithId(ConsultingRoomId roomId) {
        return ConsultingRoom.create(
                roomId,
                "101",
                "Consultorio Principal",
                "Sillón dental, lámpara, equipo de rayos X",
                true
        );
    }
}