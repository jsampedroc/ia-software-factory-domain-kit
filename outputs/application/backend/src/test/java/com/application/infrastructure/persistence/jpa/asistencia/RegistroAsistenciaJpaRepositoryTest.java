package com.application.infrastructure.persistence.jpa.asistencia;

import com.application.domain.model.asistencia.RegistroAsistencia;
import com.application.domain.valueobject.asistencia.EstadoAsistencia;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class RegistroAsistenciaJpaRepositoryTest {

    @Mock
    private RegistroAsistenciaJpaRepository repository;

    private RegistroAsistenciaJpaEntity entity;
    private RegistroAsistencia domainEntity;
    private final UUID testId = UUID.randomUUID();
    private final UUID alumnoId = UUID.randomUUID();
    private final LocalDate testDate = LocalDate.now();
    private final EstadoAsistencia testEstado = EstadoAsistencia.PRESENTE;

    @BeforeEach
    void setUp() {
        entity = new RegistroAsistenciaJpaEntity();
        entity.setId(testId);
        entity.setFecha(testDate);
        entity.setHoraEntrada(LocalTime.of(9, 0));
        entity.setHoraSalida(LocalTime.of(14, 0));
        entity.setEstado(testEstado);
        entity.setComentarios("Test comentario");
        entity.setAlumnoId(alumnoId);

        domainEntity = RegistroAsistenciaJpaEntity.toDomain(entity);
    }

    @Test
    void findById_shouldReturnEntityWhenExists() {
        when(repository.findById(testId)).thenReturn(Optional.of(entity));

        Optional<RegistroAsistenciaJpaEntity> result = repository.findById(testId);

        assertThat(result).isPresent();
        assertThat(result.get().getId()).isEqualTo(testId);
    }

    @Test
    void findById_shouldReturnEmptyWhenNotExists() {
        when(repository.findById(testId)).thenReturn(Optional.empty());

        Optional<RegistroAsistenciaJpaEntity> result = repository.findById(testId);

        assertThat(result).isEmpty();
    }

    @Test
    void save_shouldPersistEntity() {
        when(repository.save(any(RegistroAsistenciaJpaEntity.class))).thenReturn(entity);

        RegistroAsistenciaJpaEntity savedEntity = repository.save(entity);

        assertThat(savedEntity).isNotNull();
        assertThat(savedEntity.getId()).isEqualTo(testId);
        verify(repository).save(entity);
    }

    @Test
    void deleteById_shouldCallRepositoryMethod() {
        repository.deleteById(testId);
        verify(repository).deleteById(testId);
    }

    @Test
    void existsById_shouldReturnTrueWhenEntityExists() {
        when(repository.existsById(testId)).thenReturn(true);

        boolean exists = repository.existsById(testId);

        assertThat(exists).isTrue();
    }

    @Test
    void findByAlumnoIdAndFecha_shouldReturnOptionalEntity() {
        when(repository.findByAlumnoIdAndFecha(alumnoId, testDate)).thenReturn(Optional.of(entity));

        Optional<RegistroAsistenciaJpaEntity> result = repository.findByAlumnoIdAndFecha(alumnoId, testDate);

        assertThat(result).isPresent();
        assertThat(result.get().getAlumnoId()).isEqualTo(alumnoId);
        assertThat(result.get().getFecha()).isEqualTo(testDate);
    }

    @Test
    void findByAlumnoIdAndFecha_shouldReturnEmptyWhenNotFound() {
        when(repository.findByAlumnoIdAndFecha(alumnoId, testDate)).thenReturn(Optional.empty());

        Optional<RegistroAsistenciaJpaEntity> result = repository.findByAlumnoIdAndFecha(alumnoId, testDate);

        assertThat(result).isEmpty();
    }

    @Test
    void findByAlumnoIdAndFechaBetween_shouldReturnList() {
        LocalDate startDate = testDate.minusDays(7);
        LocalDate endDate = testDate.plusDays(7);
        List<RegistroAsistenciaJpaEntity> entityList = List.of(entity);
        when(repository.findByAlumnoIdAndFechaBetween(alumnoId, startDate, endDate)).thenReturn(entityList);

        List<RegistroAsistenciaJpaEntity> result = repository.findByAlumnoIdAndFechaBetween(alumnoId, startDate, endDate);

        assertThat(result).hasSize(1);
        assertThat(result.get(0).getAlumnoId()).isEqualTo(alumnoId);
        assertThat(result.get(0).getFecha()).isBetween(startDate, endDate);
    }

    @Test
    void findByEstado_shouldReturnPage() {
        Pageable pageable = PageRequest.of(0, 10);
        Page<RegistroAsistenciaJpaEntity> page = new PageImpl<>(List.of(entity));
        when(repository.findByEstado(eq(testEstado), any(Pageable.class))).thenReturn(page);

        Page<RegistroAsistenciaJpaEntity> result = repository.findByEstado(testEstado, pageable);

        assertThat(result).isNotEmpty();
        assertThat(result.getContent().get(0).getEstado()).isEqualTo(testEstado);
    }

    @Test
    void countByAlumnoIdAndEstadoAndFechaBetween_shouldReturnCount() {
        LocalDate startDate = testDate.withDayOfMonth(1);
        LocalDate endDate = testDate.withDayOfMonth(testDate.lengthOfMonth());
        long expectedCount = 5L;
        when(repository.countByAlumnoIdAndEstadoAndFechaBetween(alumnoId, EstadoAsistencia.AUSENTE, startDate, endDate)).thenReturn(expectedCount);

        long count = repository.countByAlumnoIdAndEstadoAndFechaBetween(alumnoId, EstadoAsistencia.AUSENTE, startDate, endDate);

        assertThat(count).isEqualTo(expectedCount);
    }

    @Test
    void deleteByAlumnoId_shouldCallRepositoryMethod() {
        repository.deleteByAlumnoId(alumnoId);
        verify(repository).deleteByAlumnoId(alumnoId);
    }
}