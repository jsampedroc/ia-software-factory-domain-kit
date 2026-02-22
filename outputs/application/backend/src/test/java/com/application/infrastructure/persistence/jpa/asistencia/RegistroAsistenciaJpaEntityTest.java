package com.application.infrastructure.persistence.jpa.asistencia;

import com.application.domain.model.asistencia.RegistroAsistencia;
import com.application.domain.valueobject.asistencia.EstadoAsistencia;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.time.LocalTime;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class RegistroAsistenciaJpaEntityTest {

    @Mock
    private RegistroAsistencia mockDomainEntity;

    private RegistroAsistenciaJpaEntity jpaEntity;

    @BeforeEach
    void setUp() {
        jpaEntity = new RegistroAsistenciaJpaEntity();
    }

    @Test
    void fromDomainEntity_ShouldMapAllFieldsCorrectly() {
        LocalDate testDate = LocalDate.of(2024, 5, 15);
        LocalTime testHoraEntrada = LocalTime.of(9, 0);
        LocalTime testHoraSalida = LocalTime.of(14, 30);
        EstadoAsistencia testEstado = EstadoAsistencia.PRESENTE;
        String testComentarios = "Comentario de prueba";

        when(mockDomainEntity.getId()).thenReturn(new RegistroAsistencia.RegistroAsistenciaId("test-id"));
        when(mockDomainEntity.getFecha()).thenReturn(testDate);
        when(mockDomainEntity.getHoraEntrada()).thenReturn(testHoraEntrada);
        when(mockDomainEntity.getHoraSalida()).thenReturn(testHoraSalida);
        when(mockDomainEntity.getEstado()).thenReturn(testEstado);
        when(mockDomainEntity.getComentarios()).thenReturn(testComentarios);

        RegistroAsistenciaJpaEntity result = RegistroAsistenciaJpaEntity.fromDomainEntity(mockDomainEntity);

        assertThat(result.getId()).isEqualTo("test-id");
        assertThat(result.getFecha()).isEqualTo(testDate);
        assertThat(result.getHoraEntrada()).isEqualTo(testHoraEntrada);
        assertThat(result.getHoraSalida()).isEqualTo(testHoraSalida);
        assertThat(result.getEstado()).isEqualTo(testEstado);
        assertThat(result.getComentarios()).isEqualTo(testComentarios);
    }

    @Test
    void fromDomainEntity_ShouldHandleNullHoraSalida() {
        LocalDate testDate = LocalDate.of(2024, 5, 15);
        LocalTime testHoraEntrada = LocalTime.of(9, 0);
        EstadoAsistencia testEstado = EstadoAsistencia.AUSENTE;

        when(mockDomainEntity.getId()).thenReturn(new RegistroAsistencia.RegistroAsistenciaId("test-id-2"));
        when(mockDomainEntity.getFecha()).thenReturn(testDate);
        when(mockDomainEntity.getHoraEntrada()).thenReturn(testHoraEntrada);
        when(mockDomainEntity.getHoraSalida()).thenReturn(null);
        when(mockDomainEntity.getEstado()).thenReturn(testEstado);
        when(mockDomainEntity.getComentarios()).thenReturn(null);

        RegistroAsistenciaJpaEntity result = RegistroAsistenciaJpaEntity.fromDomainEntity(mockDomainEntity);

        assertThat(result.getId()).isEqualTo("test-id-2");
        assertThat(result.getFecha()).isEqualTo(testDate);
        assertThat(result.getHoraEntrada()).isEqualTo(testHoraEntrada);
        assertThat(result.getHoraSalida()).isNull();
        assertThat(result.getEstado()).isEqualTo(testEstado);
        assertThat(result.getComentarios()).isNull();
    }

    @Test
    void toDomainEntity_ShouldMapAllFieldsCorrectly() {
        LocalDate testDate = LocalDate.of(2024, 5, 15);
        LocalTime testHoraEntrada = LocalTime.of(9, 0);
        LocalTime testHoraSalida = LocalTime.of(14, 30);
        EstadoAsistencia testEstado = EstadoAsistencia.JUSTIFICADO;
        String testComentarios = "Comentario para toDomain";

        jpaEntity.setId("test-id-3");
        jpaEntity.setFecha(testDate);
        jpaEntity.setHoraEntrada(testHoraEntrada);
        jpaEntity.setHoraSalida(testHoraSalida);
        jpaEntity.setEstado(testEstado);
        jpaEntity.setComentarios(testComentarios);

        RegistroAsistencia result = jpaEntity.toDomainEntity();

        assertThat(result.getId()).isEqualTo(new RegistroAsistencia.RegistroAsistenciaId("test-id-3"));
        assertThat(result.getFecha()).isEqualTo(testDate);
        assertThat(result.getHoraEntrada()).isEqualTo(testHoraEntrada);
        assertThat(result.getHoraSalida()).isEqualTo(testHoraSalida);
        assertThat(result.getEstado()).isEqualTo(testEstado);
        assertThat(result.getComentarios()).isEqualTo(testComentarios);
    }

    @Test
    void toDomainEntity_ShouldHandleNullHoraSalida() {
        LocalDate testDate = LocalDate.of(2024, 5, 15);
        LocalTime testHoraEntrada = LocalTime.of(9, 0);
        EstadoAsistencia testEstado = EstadoAsistencia.RETRASO;

        jpaEntity.setId("test-id-4");
        jpaEntity.setFecha(testDate);
        jpaEntity.setHoraEntrada(testHoraEntrada);
        jpaEntity.setHoraSalida(null);
        jpaEntity.setEstado(testEstado);
        jpaEntity.setComentarios(null);

        RegistroAsistencia result = jpaEntity.toDomainEntity();

        assertThat(result.getId()).isEqualTo(new RegistroAsistencia.RegistroAsistenciaId("test-id-4"));
        assertThat(result.getFecha()).isEqualTo(testDate);
        assertThat(result.getHoraEntrada()).isEqualTo(testHoraEntrada);
        assertThat(result.getHoraSalida()).isNull();
        assertThat(result.getEstado()).isEqualTo(testEstado);
        assertThat(result.getComentarios()).isNull();
    }
}