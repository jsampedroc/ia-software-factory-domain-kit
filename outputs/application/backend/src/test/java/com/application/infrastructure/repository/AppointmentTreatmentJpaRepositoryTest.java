package com.application.infrastructure.repository;

import com.application.infrastructure.entity.AppointmentTreatmentEntity;
import com.application.infrastructure.entity.AppointmentTreatmentId;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AppointmentTreatmentJpaRepositoryTest {

    @Mock
    private AppointmentTreatmentJpaRepository repository;

    @Test
    void shouldSaveAppointmentTreatmentEntity() {
        AppointmentTreatmentId id = new AppointmentTreatmentId(UUID.randomUUID(), UUID.randomUUID());
        AppointmentTreatmentEntity entity = mock(AppointmentTreatmentEntity.class);
        when(entity.getId()).thenReturn(id);
        when(repository.save(any(AppointmentTreatmentEntity.class))).thenReturn(entity);

        AppointmentTreatmentEntity saved = repository.save(entity);

        assertThat(saved).isNotNull();
        assertThat(saved.getId()).isEqualTo(id);
        verify(repository, times(1)).save(entity);
    }

    @Test
    void shouldFindAppointmentTreatmentEntityById() {
        AppointmentTreatmentId id = new AppointmentTreatmentId(UUID.randomUUID(), UUID.randomUUID());
        AppointmentTreatmentEntity entity = mock(AppointmentTreatmentEntity.class);
        when(entity.getId()).thenReturn(id);
        when(repository.findById(id)).thenReturn(Optional.of(entity));

        Optional<AppointmentTreatmentEntity> found = repository.findById(id);

        assertThat(found).isPresent();
        assertThat(found.get().getId()).isEqualTo(id);
        verify(repository, times(1)).findById(id);
    }

    @Test
    void shouldReturnEmptyWhenEntityNotFound() {
        AppointmentTreatmentId id = new AppointmentTreatmentId(UUID.randomUUID(), UUID.randomUUID());
        when(repository.findById(id)).thenReturn(Optional.empty());

        Optional<AppointmentTreatmentEntity> found = repository.findById(id);

        assertThat(found).isEmpty();
        verify(repository, times(1)).findById(id);
    }

    @Test
    void shouldFindAllAppointmentTreatmentEntities() {
        AppointmentTreatmentEntity entity1 = mock(AppointmentTreatmentEntity.class);
        AppointmentTreatmentEntity entity2 = mock(AppointmentTreatmentEntity.class);
        List<AppointmentTreatmentEntity> entities = List.of(entity1, entity2);
        when(repository.findAll()).thenReturn(entities);

        List<AppointmentTreatmentEntity> allEntities = repository.findAll();

        assertThat(allEntities).hasSize(2).containsExactly(entity1, entity2);
        verify(repository, times(1)).findAll();
    }

    @Test
    void shouldFindAllAppointmentTreatmentEntitiesWithPageable() {
        Pageable pageable = PageRequest.of(0, 10);
        AppointmentTreatmentEntity entity1 = mock(AppointmentTreatmentEntity.class);
        AppointmentTreatmentEntity entity2 = mock(AppointmentTreatmentEntity.class);
        Page<AppointmentTreatmentEntity> page = new PageImpl<>(List.of(entity1, entity2), pageable, 2);
        when(repository.findAll(pageable)).thenReturn(page);

        Page<AppointmentTreatmentEntity> result = repository.findAll(pageable);

        assertThat(result).hasSize(2);
        assertThat(result.getContent()).containsExactly(entity1, entity2);
        verify(repository, times(1)).findAll(pageable);
    }

    @Test
    void shouldDeleteAppointmentTreatmentEntityById() {
        AppointmentTreatmentId id = new AppointmentTreatmentId(UUID.randomUUID(), UUID.randomUUID());

        repository.deleteById(id);

        verify(repository, times(1)).deleteById(id);
    }

    @Test
    void shouldCheckIfAppointmentTreatmentEntityExistsById() {
        AppointmentTreatmentId id = new AppointmentTreatmentId(UUID.randomUUID(), UUID.randomUUID());
        when(repository.existsById(id)).thenReturn(true);

        boolean exists = repository.existsById(id);

        assertThat(exists).isTrue();
        verify(repository, times(1)).existsById(id);
    }

    @Test
    void shouldReturnCountOfAppointmentTreatmentEntities() {
        when(repository.count()).thenReturn(5L);

        long count = repository.count();

        assertThat(count).isEqualTo(5L);
        verify(repository, times(1)).count();
    }

    @Test
    void shouldDeleteAppointmentTreatmentEntity() {
        AppointmentTreatmentEntity entity = mock(AppointmentTreatmentEntity.class);

        repository.delete(entity);

        verify(repository, times(1)).delete(entity);
    }

    @Test
    void shouldDeleteAllAppointmentTreatmentEntities() {
        repository.deleteAll();

        verify(repository, times(1)).deleteAll();
    }

    @Test
    void shouldDeleteAllAppointmentTreatmentEntitiesById() {
        AppointmentTreatmentId id1 = new AppointmentTreatmentId(UUID.randomUUID(), UUID.randomUUID());
        AppointmentTreatmentId id2 = new AppointmentTreatmentId(UUID.randomUUID(), UUID.randomUUID());
        List<AppointmentTreatmentId> ids = List.of(id1, id2);

        repository.deleteAllById(ids);

        verify(repository, times(1)).deleteAllById(ids);
    }
}