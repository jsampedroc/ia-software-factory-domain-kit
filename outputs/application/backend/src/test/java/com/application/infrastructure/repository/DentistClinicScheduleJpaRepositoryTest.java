package com.application.infrastructure.repository;

import com.application.infrastructure.entity.DentistClinicScheduleEntity;
import com.application.infrastructure.entity.DentistEntity;
import com.application.infrastructure.entity.ClinicEntity;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import java.time.LocalDate;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class DentistClinicScheduleJpaRepositoryTest {

    @Mock
    private DentistClinicScheduleJpaRepository repository;

    @Test
    void testFindByDentist() {
        DentistEntity dentist = mock(DentistEntity.class);
        List<DentistClinicScheduleEntity> expectedList = Arrays.asList(
            mock(DentistClinicScheduleEntity.class),
            mock(DentistClinicScheduleEntity.class)
        );
        when(repository.findByDentist(dentist)).thenReturn(expectedList);

        List<DentistClinicScheduleEntity> result = repository.findByDentist(dentist);

        assertNotNull(result);
        assertEquals(2, result.size());
        verify(repository).findByDentist(dentist);
    }

    @Test
    void testFindByClinic() {
        ClinicEntity clinic = mock(ClinicEntity.class);
        List<DentistClinicScheduleEntity> expectedList = Collections.singletonList(
            mock(DentistClinicScheduleEntity.class)
        );
        when(repository.findByClinic(clinic)).thenReturn(expectedList);

        List<DentistClinicScheduleEntity> result = repository.findByClinic(clinic);

        assertNotNull(result);
        assertEquals(1, result.size());
        verify(repository).findByClinic(clinic);
    }

    @Test
    void testFindByDentistAndClinicAndScheduleDate_Found() {
        DentistEntity dentist = mock(DentistEntity.class);
        ClinicEntity clinic = mock(ClinicEntity.class);
        LocalDate date = LocalDate.now();
        DentistClinicScheduleEntity expectedEntity = mock(DentistClinicScheduleEntity.class);
        when(repository.findByDentistAndClinicAndScheduleDate(dentist, clinic, date))
            .thenReturn(Optional.of(expectedEntity));

        Optional<DentistClinicScheduleEntity> result = repository.findByDentistAndClinicAndScheduleDate(dentist, clinic, date);

        assertTrue(result.isPresent());
        assertEquals(expectedEntity, result.get());
        verify(repository).findByDentistAndClinicAndScheduleDate(dentist, clinic, date);
    }

    @Test
    void testFindByDentistAndClinicAndScheduleDate_NotFound() {
        DentistEntity dentist = mock(DentistEntity.class);
        ClinicEntity clinic = mock(ClinicEntity.class);
        LocalDate date = LocalDate.now();
        when(repository.findByDentistAndClinicAndScheduleDate(dentist, clinic, date))
            .thenReturn(Optional.empty());

        Optional<DentistClinicScheduleEntity> result = repository.findByDentistAndClinicAndScheduleDate(dentist, clinic, date);

        assertFalse(result.isPresent());
        verify(repository).findByDentistAndClinicAndScheduleDate(dentist, clinic, date);
    }

    @Test
    void testFindByDentistAndDateRange() {
        DentistEntity dentist = mock(DentistEntity.class);
        LocalDate startDate = LocalDate.now().minusDays(7);
        LocalDate endDate = LocalDate.now();
        List<DentistClinicScheduleEntity> expectedList = Arrays.asList(
            mock(DentistClinicScheduleEntity.class),
            mock(DentistClinicScheduleEntity.class),
            mock(DentistClinicScheduleEntity.class)
        );
        when(repository.findByDentistAndDateRange(dentist, startDate, endDate)).thenReturn(expectedList);

        List<DentistClinicScheduleEntity> result = repository.findByDentistAndDateRange(dentist, startDate, endDate);

        assertNotNull(result);
        assertEquals(3, result.size());
        verify(repository).findByDentistAndDateRange(dentist, startDate, endDate);
    }

    @Test
    void testFindByClinicAndDateRange() {
        ClinicEntity clinic = mock(ClinicEntity.class);
        LocalDate startDate = LocalDate.now().minusDays(30);
        LocalDate endDate = LocalDate.now();
        List<DentistClinicScheduleEntity> expectedList = Collections.singletonList(
            mock(DentistClinicScheduleEntity.class)
        );
        when(repository.findByClinicAndDateRange(clinic, startDate, endDate)).thenReturn(expectedList);

        List<DentistClinicScheduleEntity> result = repository.findByClinicAndDateRange(clinic, startDate, endDate);

        assertNotNull(result);
        assertEquals(1, result.size());
        verify(repository).findByClinicAndDateRange(clinic, startDate, endDate);
    }

    @Test
    void testFindActiveSchedulesByClinicAndDate() {
        ClinicEntity clinic = mock(ClinicEntity.class);
        LocalDate date = LocalDate.now();
        List<DentistClinicScheduleEntity> expectedList = Arrays.asList(
            mock(DentistClinicScheduleEntity.class),
            mock(DentistClinicScheduleEntity.class)
        );
        when(repository.findActiveSchedulesByClinicAndDate(clinic, date)).thenReturn(expectedList);

        List<DentistClinicScheduleEntity> result = repository.findActiveSchedulesByClinicAndDate(clinic, date);

        assertNotNull(result);
        assertEquals(2, result.size());
        verify(repository).findActiveSchedulesByClinicAndDate(clinic, date);
    }

    @Test
    void testExistsByDentistAndClinicAndScheduleDateAndIsActiveTrue_Exists() {
        DentistEntity dentist = mock(DentistEntity.class);
        ClinicEntity clinic = mock(ClinicEntity.class);
        LocalDate date = LocalDate.now();
        when(repository.existsByDentistAndClinicAndScheduleDateAndIsActiveTrue(dentist, clinic, date))
            .thenReturn(true);

        boolean exists = repository.existsByDentistAndClinicAndScheduleDateAndIsActiveTrue(dentist, clinic, date);

        assertTrue(exists);
        verify(repository).existsByDentistAndClinicAndScheduleDateAndIsActiveTrue(dentist, clinic, date);
    }

    @Test
    void testExistsByDentistAndClinicAndScheduleDateAndIsActiveTrue_NotExists() {
        DentistEntity dentist = mock(DentistEntity.class);
        ClinicEntity clinic = mock(ClinicEntity.class);
        LocalDate date = LocalDate.now();
        when(repository.existsByDentistAndClinicAndScheduleDateAndIsActiveTrue(dentist, clinic, date))
            .thenReturn(false);

        boolean exists = repository.existsByDentistAndClinicAndScheduleDateAndIsActiveTrue(dentist, clinic, date);

        assertFalse(exists);
        verify(repository).existsByDentistAndClinicAndScheduleDateAndIsActiveTrue(dentist, clinic, date);
    }

    @Test
    void testDeleteByDentist() {
        DentistEntity dentist = mock(DentistEntity.class);
        doNothing().when(repository).deleteByDentist(dentist);

        repository.deleteByDentist(dentist);

        verify(repository).deleteByDentist(dentist);
    }

    @Test
    void testDeleteByClinic() {
        ClinicEntity clinic = mock(ClinicEntity.class);
        doNothing().when(repository).deleteByClinic(clinic);

        repository.deleteByClinic(clinic);

        verify(repository).deleteByClinic(clinic);
    }

    @Test
    void testJpaRepositoryMethods() {
        DentistClinicScheduleEntity entity = mock(DentistClinicScheduleEntity.class);
        UUID id = UUID.randomUUID();
        List<DentistClinicScheduleEntity> entityList = Collections.singletonList(entity);
        Example<DentistClinicScheduleEntity> example = Example.of(entity);
        Pageable pageable = mock(Pageable.class);
        Page<DentistClinicScheduleEntity> page = mock(Page.class);
        Sort sort = mock(Sort.class);

        when(repository.save(entity)).thenReturn(entity);
        when(repository.saveAll(entityList)).thenReturn(entityList);
        when(repository.findById(id)).thenReturn(Optional.of(entity));
        when(repository.existsById(id)).thenReturn(true);
        when(repository.findAll()).thenReturn(entityList);
        when(repository.findAllById(anyIterable())).thenReturn(entityList);
        when(repository.count()).thenReturn(1L);
        when(repository.deleteById(id)).thenAnswer(invocation -> null);
        when(repository.delete(entity)).thenAnswer(invocation -> null);
        when(repository.deleteAllById(anyIterable())).thenAnswer(invocation -> null);
        when(repository.deleteAll(entityList)).thenAnswer(invocation -> null);
        when(repository.deleteAll()).thenAnswer(invocation -> null);
        when(repository.findAll(sort)).thenReturn(entityList);
        when(repository.findAll(example)).thenReturn(entityList);
        when(repository.findAll(example, sort)).thenReturn(entityList);
        when(repository.findAll(example, pageable)).thenReturn(page);
        when(repository.findOne(example)).thenReturn(Optional.of(entity));
        when(repository.count(example)).thenReturn(1L);
        when(repository.exists(example)).thenReturn(true);

        assertNotNull(repository.save(entity));
        assertNotNull(repository.saveAll(entityList));
        assertTrue(repository.findById(id).isPresent());
        assertTrue(repository.existsById(id));
        assertEquals(1, repository.findAll().size());
        assertEquals(1, repository.findAllById(Collections.singletonList(id)).size());
        assertEquals(1L, repository.count());
        repository.deleteById(id);
        repository.delete(entity);
        repository.deleteAllById(Collections.singletonList(id));
        repository.deleteAll(entityList);
        repository.deleteAll();
        assertEquals(1, repository.findAll(sort).size());
        assertEquals(1, repository.findAll(example).size());
        assertEquals(1, repository.findAll(example, sort).size());
        assertNotNull(repository.findAll(example, pageable));
        assertTrue(repository.findOne(example).isPresent());
        assertEquals(1L, repository.count(example));
        assertTrue(repository.exists(example));

        verify(repository).save(entity);
        verify(repository).saveAll(entityList);
        verify(repository).findById(id);
        verify(repository).existsById(id);
        verify(repository).findAll();
        verify(repository).findAllById(anyIterable());
        verify(repository).count();
        verify(repository).deleteById(id);
        verify(repository).delete(entity);
        verify(repository).deleteAllById(anyIterable());
        verify(repository).deleteAll(entityList);
        verify(repository).deleteAll();
        verify(repository).findAll(sort);
        verify(repository).findAll(example);
        verify(repository).findAll(example, sort);
        verify(repository).findAll(example, pageable);
        verify(repository).findOne(example);
        verify(repository).count(example);
        verify(repository).exists(example);
    }
}