package com.application.infrastructure.repository;

import com.application.infrastructure.entity.DentistSpecialtyEntity;
import com.application.infrastructure.entity.DentistSpecialtyId;
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
class DentistSpecialtyJpaRepositoryTest {

    @Mock
    private DentistSpecialtyJpaRepository dentistSpecialtyJpaRepository;

    @Test
    void shouldSaveDentistSpecialtyEntity() {
        DentistSpecialtyId id = new DentistSpecialtyId(UUID.randomUUID(), UUID.randomUUID());
        DentistSpecialtyEntity entity = mock(DentistSpecialtyEntity.class);
        when(entity.getId()).thenReturn(id);
        when(dentistSpecialtyJpaRepository.save(any(DentistSpecialtyEntity.class))).thenReturn(entity);

        DentistSpecialtyEntity savedEntity = dentistSpecialtyJpaRepository.save(entity);

        assertThat(savedEntity).isNotNull();
        assertThat(savedEntity.getId()).isEqualTo(id);
        verify(dentistSpecialtyJpaRepository, times(1)).save(entity);
    }

    @Test
    void shouldFindDentistSpecialtyEntityById() {
        DentistSpecialtyId id = new DentistSpecialtyId(UUID.randomUUID(), UUID.randomUUID());
        DentistSpecialtyEntity entity = mock(DentistSpecialtyEntity.class);
        when(entity.getId()).thenReturn(id);
        when(dentistSpecialtyJpaRepository.findById(id)).thenReturn(Optional.of(entity));

        Optional<DentistSpecialtyEntity> foundEntity = dentistSpecialtyJpaRepository.findById(id);

        assertThat(foundEntity).isPresent();
        assertThat(foundEntity.get().getId()).isEqualTo(id);
        verify(dentistSpecialtyJpaRepository, times(1)).findById(id);
    }

    @Test
    void shouldReturnEmptyWhenEntityNotFoundById() {
        DentistSpecialtyId id = new DentistSpecialtyId(UUID.randomUUID(), UUID.randomUUID());
        when(dentistSpecialtyJpaRepository.findById(id)).thenReturn(Optional.empty());

        Optional<DentistSpecialtyEntity> foundEntity = dentistSpecialtyJpaRepository.findById(id);

        assertThat(foundEntity).isEmpty();
        verify(dentistSpecialtyJpaRepository, times(1)).findById(id);
    }

    @Test
    void shouldFindAllDentistSpecialtyEntities() {
        DentistSpecialtyEntity entity1 = mock(DentistSpecialtyEntity.class);
        DentistSpecialtyEntity entity2 = mock(DentistSpecialtyEntity.class);
        List<DentistSpecialtyEntity> entities = List.of(entity1, entity2);
        when(dentistSpecialtyJpaRepository.findAll()).thenReturn(entities);

        List<DentistSpecialtyEntity> allEntities = dentistSpecialtyJpaRepository.findAll();

        assertThat(allEntities).hasSize(2).containsExactly(entity1, entity2);
        verify(dentistSpecialtyJpaRepository, times(1)).findAll();
    }

    @Test
    void shouldFindAllDentistSpecialtyEntitiesWithPageable() {
        Pageable pageable = PageRequest.of(0, 10);
        DentistSpecialtyEntity entity = mock(DentistSpecialtyEntity.class);
        Page<DentistSpecialtyEntity> page = new PageImpl<>(List.of(entity), pageable, 1);
        when(dentistSpecialtyJpaRepository.findAll(pageable)).thenReturn(page);

        Page<DentistSpecialtyEntity> resultPage = dentistSpecialtyJpaRepository.findAll(pageable);

        assertThat(resultPage).isNotNull();
        assertThat(resultPage.getContent()).hasSize(1).containsExactly(entity);
        assertThat(resultPage.getTotalElements()).isEqualTo(1);
        verify(dentistSpecialtyJpaRepository, times(1)).findAll(pageable);
    }

    @Test
    void shouldDeleteDentistSpecialtyEntityById() {
        DentistSpecialtyId id = new DentistSpecialtyId(UUID.randomUUID(), UUID.randomUUID());
        doNothing().when(dentistSpecialtyJpaRepository).deleteById(id);

        dentistSpecialtyJpaRepository.deleteById(id);

        verify(dentistSpecialtyJpaRepository, times(1)).deleteById(id);
    }

    @Test
    void shouldCheckIfDentistSpecialtyEntityExistsById() {
        DentistSpecialtyId id = new DentistSpecialtyId(UUID.randomUUID(), UUID.randomUUID());
        when(dentistSpecialtyJpaRepository.existsById(id)).thenReturn(true);

        boolean exists = dentistSpecialtyJpaRepository.existsById(id);

        assertThat(exists).isTrue();
        verify(dentistSpecialtyJpaRepository, times(1)).existsById(id);
    }

    @Test
    void shouldReturnCountOfDentistSpecialtyEntities() {
        when(dentistSpecialtyJpaRepository.count()).thenReturn(5L);

        long count = dentistSpecialtyJpaRepository.count();

        assertThat(count).isEqualTo(5L);
        verify(dentistSpecialtyJpaRepository, times(1)).count();
    }

    @Test
    void shouldDeleteDentistSpecialtyEntity() {
        DentistSpecialtyEntity entity = mock(DentistSpecialtyEntity.class);
        doNothing().when(dentistSpecialtyJpaRepository).delete(entity);

        dentistSpecialtyJpaRepository.delete(entity);

        verify(dentistSpecialtyJpaRepository, times(1)).delete(entity);
    }

    @Test
    void shouldDeleteAllDentistSpecialtyEntities() {
        doNothing().when(dentistSpecialtyJpaRepository).deleteAll();

        dentistSpecialtyJpaRepository.deleteAll();

        verify(dentistSpecialtyJpaRepository, times(1)).deleteAll();
    }
}