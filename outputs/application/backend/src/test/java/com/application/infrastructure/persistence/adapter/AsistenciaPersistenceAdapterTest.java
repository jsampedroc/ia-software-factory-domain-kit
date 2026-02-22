package com.application.infrastructure.persistence.adapter;

import com.application.domain.model.asistencia.RegistroAsistencia;
import com.application.domain.valueobject.asistencia.EstadoAsistencia;
import com.application.infrastructure.persistence.jpa.asistencia.RegistroAsistenciaJpaEntity;
import com.application.infrastructure.persistence.jpa.asistencia.RegistroAsistenciaJpaRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AsistenciaPersistenceAdapterTest {

    @Mock
    private RegistroAsistenciaJpaRepository registroAsistenciaJpaRepository;

    @InjectMocks
    private AsistenciaPersistenceAdapter underTest;

    private RegistroAsistencia registroAsistencia;
    private RegistroAsistenciaJpaEntity registroAsistenciaJpaEntity;
    private final UUID registroId = UUID.randomUUID();
    private final UUID alumnoId = UUID.randomUUID();

    @BeforeEach
    void setUp() {
        registroAsistencia = RegistroAsistencia.builder()
                .id(registroId)
                .fecha(LocalDate.now())
                .horaEntrada(LocalTime.of(9, 0))
                .horaSalida(LocalTime.of(17, 0))
                .estado(new EstadoAsistencia("PRESENTE"))
                .comentarios("Sin incidencias")
                .build();

        registroAsistenciaJpaEntity = new RegistroAsistenciaJpaEntity();
        registroAsistenciaJpaEntity.setId(registroId);
        registroAsistenciaJpaEntity.setFecha(LocalDate.now());
        registroAsistenciaJpaEntity.setHoraEntrada(LocalTime.of(9, 0));
        registroAsistenciaJpaEntity.setHoraSalida(LocalTime.of(17, 0));
        registroAsistenciaJpaEntity.setEstado("PRESENTE");
        registroAsistenciaJpaEntity.setComentarios("Sin incidencias");
        registroAsistenciaJpaEntity.setAlumnoId(alumnoId);
    }

    @Test
    void save_shouldPersistAndReturnDomainEntity() {
        when(registroAsistenciaJpaRepository.save(any(RegistroAsistenciaJpaEntity.class)))
                .thenReturn(registroAsistenciaJpaEntity);

        RegistroAsistencia saved = underTest.save(registroAsistencia, alumnoId);

        assertThat(saved).isNotNull();
        assertThat(saved.getId()).isEqualTo(registroId);
        verify(registroAsistenciaJpaRepository).save(any(RegistroAsistenciaJpaEntity.class));
    }

    @Test
    void findById_shouldReturnDomainEntityWhenFound() {
        when(registroAsistenciaJpaRepository.findById(registroId))
                .thenReturn(Optional.of(registroAsistenciaJpaEntity));

        Optional<RegistroAsistencia> result = underTest.findById(registroId);

        assertThat(result).isPresent();
        assertThat(result.get().getId()).isEqualTo(registroId);
        assertThat(result.get().getEstado().valor()).isEqualTo("PRESENTE");
    }

    @Test
    void findById_shouldReturnEmptyWhenNotFound() {
        when(registroAsistenciaJpaRepository.findById(registroId))
                .thenReturn(Optional.empty());

        Optional<RegistroAsistencia> result = underTest.findById(registroId);

        assertThat(result).isEmpty();
    }

    @Test
    void findByAlumnoIdAndFecha_shouldReturnDomainEntityWhenFound() {
        LocalDate fecha = LocalDate.now();
        when(registroAsistenciaJpaRepository.findByAlumnoIdAndFecha(alumnoId, fecha))
                .thenReturn(Optional.of(registroAsistenciaJpaEntity));

        Optional<RegistroAsistencia> result = underTest.findByAlumnoIdAndFecha(alumnoId, fecha);

        assertThat(result).isPresent();
        assertThat(result.get().getId()).isEqualTo(registroId);
        assertThat(result.get().getFecha()).isEqualTo(fecha);
    }

    @Test
    void findByAlumnoIdAndFecha_shouldReturnEmptyWhenNotFound() {
        LocalDate fecha = LocalDate.now();
        when(registroAsistenciaJpaRepository.findByAlumnoIdAndFecha(alumnoId, fecha))
                .thenReturn(Optional.empty());

        Optional<RegistroAsistencia> result = underTest.findByAlumnoIdAndFecha(alumnoId, fecha);

        assertThat(result).isEmpty();
    }

    @Test
    void findAllByAlumnoIdAndFechaBetween_shouldReturnListOfDomainEntities() {
        LocalDate inicio = LocalDate.now().minusDays(7);
        LocalDate fin = LocalDate.now();
        when(registroAsistenciaJpaRepository.findAllByAlumnoIdAndFechaBetween(alumnoId, inicio, fin))
                .thenReturn(List.of(registroAsistenciaJpaEntity));

        List<RegistroAsistencia> result = underTest.findAllByAlumnoIdAndFechaBetween(alumnoId, inicio, fin);

        assertThat(result).hasSize(1);
        assertThat(result.get(0).getId()).isEqualTo(registroId);
    }

    @Test
    void delete_shouldCallRepositoryDelete() {
        underTest.delete(registroId);

        verify(registroAsistenciaJpaRepository).deleteById(registroId);
    }

    @Test
    void existsByAlumnoIdAndFecha_shouldReturnTrueWhenExists() {
        LocalDate fecha = LocalDate.now();
        when(registroAsistenciaJpaRepository.existsByAlumnoIdAndFecha(alumnoId, fecha))
                .thenReturn(true);

        boolean result = underTest.existsByAlumnoIdAndFecha(alumnoId, fecha);

        assertThat(result).isTrue();
    }

    @Test
    void existsByAlumnoIdAndFecha_shouldReturnFalseWhenNotExists() {
        LocalDate fecha = LocalDate.now();
        when(registroAsistenciaJpaRepository.existsByAlumnoIdAndFecha(alumnoId, fecha))
                .thenReturn(false);

        boolean result = underTest.existsByAlumnoIdAndFecha(alumnoId, fecha);

        assertThat(result).isFalse();
    }
}