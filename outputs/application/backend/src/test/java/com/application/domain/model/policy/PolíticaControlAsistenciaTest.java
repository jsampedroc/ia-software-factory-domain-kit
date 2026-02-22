package com.application.domain.model.policy;

import com.application.domain.model.alumno.Alumno;
import com.application.domain.model.alumno.Tutor;
import com.application.domain.model.asistencia.RegistroAsistencia;
import com.application.domain.model.event.AsistenciaRegistrada;
import com.application.domain.shared.DomainEventPublisher;
import com.application.domain.valueobject.asistencia.EstadoAsistencia;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PolíticaControlAsistenciaTest {

    private PolíticaControlAsistencia políticaControlAsistencia;

    @Mock
    private DomainEventPublisher domainEventPublisher;

    private Alumno alumno;
    private Tutor tutor1;
    private Tutor tutor2;

    @BeforeEach
    void setUp() {
        políticaControlAsistencia = new PolíticaControlAsistencia(domainEventPublisher);
        setupAlumnoConTutores();
    }

    private void setupAlumnoConTutores() {
        Alumno.AlumnoId alumnoId = new Alumno.AlumnoId(UUID.randomUUID());
        tutor1 = mock(Tutor.class);
        tutor2 = mock(Tutor.class);
        alumno = mock(Alumno.class);
        when(alumno.getId()).thenReturn(alumnoId);
        when(alumno.getTutores()).thenReturn(List.of(tutor1, tutor2));
        when(alumno.estaActivo()).thenReturn(true);
    }

    @Test
    void cuandoSeRegistraUnaAusenciaNoJustificada_Y_EsLaCuartaEnElMes_DeberiaNotificarATodosLosTutores() {
        LocalDate fechaEnMes = LocalDate.of(2024, 5, 15);
        RegistroAsistencia registro = mock(RegistroAsistencia.class);
        when(registro.getAlumno()).thenReturn(alumno);
        when(registro.getFecha()).thenReturn(fechaEnMes);
        when(registro.getEstado()).thenReturn(new EstadoAsistencia("AUSENTE"));

        when(alumno.contarAusenciasNoJustificadasEnMes(fechaEnMes.getYear(), fechaEnMes.getMonthValue()))
                .thenReturn(4);

        AsistenciaRegistrada evento = new AsistenciaRegistrada(
                alumno.getId(),
                new RegistroAsistencia.RegistroAsistenciaId(UUID.randomUUID()),
                fechaEnMes,
                new EstadoAsistencia("AUSENTE")
        );

        políticaControlAsistencia.handle(evento);

        ArgumentCaptor<Object> eventCaptor = ArgumentCaptor.forClass(Object.class);
        verify(domainEventPublisher, times(2)).publish(eventCaptor.capture());

        List<Object> publishedEvents = eventCaptor.getAllValues();
        assertEquals(2, publishedEvents.size());
        assertTrue(publishedEvents.get(0) instanceof PolíticaControlAsistencia.TutorNotificadoPorAusencias);
        assertTrue(publishedEvents.get(1) instanceof PolíticaControlAsistencia.TutorNotificadoPorAusencias);

        PolíticaControlAsistencia.TutorNotificadoPorAusencias notificacion1 = (PolíticaControlAsistencia.TutorNotificadoPorAusencias) publishedEvents.get(0);
        PolíticaControlAsistencia.TutorNotificadoPorAusencias notificacion2 = (PolíticaControlAsistencia.TutorNotificadoPorAusencias) publishedEvents.get(1);

        assertEquals(alumno.getId(), notificacion1.alumnoId());
        assertEquals(alumno.getId(), notificacion2.alumnoId());
        assertEquals(4, notificacion1.numeroAusencias());
        assertEquals(fechaEnMes.getYear(), notificacion1.mes());
        assertEquals(fechaEnMes.getMonthValue(), notificacion1.ano());
    }

    @Test
    void cuandoSeRegistraUnaAusenciaNoJustificada_Y_EsLaTerceraEnElMes_NoDeberiaNotificar() {
        LocalDate fechaEnMes = LocalDate.of(2024, 5, 10);
        RegistroAsistencia registro = mock(RegistroAsistencia.class);
        when(registro.getAlumno()).thenReturn(alumno);
        when(registro.getFecha()).thenReturn(fechaEnMes);
        when(registro.getEstado()).thenReturn(new EstadoAsistencia("AUSENTE"));

        when(alumno.contarAusenciasNoJustificadasEnMes(fechaEnMes.getYear(), fechaEnMes.getMonthValue()))
                .thenReturn(3);

        AsistenciaRegistrada evento = new AsistenciaRegistrada(
                alumno.getId(),
                new RegistroAsistencia.RegistroAsistenciaId(UUID.randomUUID()),
                fechaEnMes,
                new EstadoAsistencia("AUSENTE")
        );

        políticaControlAsistencia.handle(evento);

        verify(domainEventPublisher, never()).publish(any());
    }

    @Test
    void cuandoSeRegistraUnEstadoDiferenteAAusenciaNoJustificada_NoDeberiaNotificar() {
        LocalDate fecha = LocalDate.now();
        RegistroAsistencia registro = mock(RegistroAsistencia.class);
        when(registro.getAlumno()).thenReturn(alumno);
        when(registro.getFecha()).thenReturn(fecha);
        when(registro.getEstado()).thenReturn(new EstadoAsistencia("PRESENTE"));

        AsistenciaRegistrada evento = new AsistenciaRegistrada(
                alumno.getId(),
                new RegistroAsistencia.RegistroAsistenciaId(UUID.randomUUID()),
                fecha,
                new EstadoAsistencia("PRESENTE")
        );

        políticaControlAsistencia.handle(evento);

        verify(alumno, never()).contarAusenciasNoJustificadasEnMes(anyInt(), anyInt());
        verify(domainEventPublisher, never()).publish(any());
    }

    @Test
    void cuandoElAlumnoNoEstaActivo_NoDeberiaEvaluarLaPolitica() {
        LocalDate fecha = LocalDate.now();
        when(alumno.estaActivo()).thenReturn(false);
        RegistroAsistencia registro = mock(RegistroAsistencia.class);
        when(registro.getAlumno()).thenReturn(alumno);
        when(registro.getFecha()).thenReturn(fecha);
        when(registro.getEstado()).thenReturn(new EstadoAsistencia("AUSENTE"));

        AsistenciaRegistrada evento = new AsistenciaRegistrada(
                alumno.getId(),
                new RegistroAsistencia.RegistroAsistenciaId(UUID.randomUUID()),
                fecha,
                new EstadoAsistencia("AUSENTE")
        );

        políticaControlAsistencia.handle(evento);

        verify(alumno, never()).contarAusenciasNoJustificadasEnMes(anyInt(), anyInt());
        verify(domainEventPublisher, never()).publish(any());
    }

    @Test
    void cuandoElAlumnoNoTieneTutores_NoDeberiaPublicarEventosDeNotificacion() {
        LocalDate fechaEnMes = LocalDate.of(2024, 6, 20);
        when(alumno.getTutores()).thenReturn(List.of());

        RegistroAsistencia registro = mock(RegistroAsistencia.class);
        when(registro.getAlumno()).thenReturn(alumno);
        when(registro.getFecha()).thenReturn(fechaEnMes);
        when(registro.getEstado()).thenReturn(new EstadoAsistencia("AUSENTE"));

        when(alumno.contarAusenciasNoJustificadasEnMes(fechaEnMes.getYear(), fechaEnMes.getMonthValue()))
                .thenReturn(5);

        AsistenciaRegistrada evento = new AsistenciaRegistrada(
                alumno.getId(),
                new RegistroAsistencia.RegistroAsistenciaId(UUID.randomUUID()),
                fechaEnMes,
                new EstadoAsistencia("AUSENTE")
        );

        políticaControlAsistencia.handle(evento);

        verify(domainEventPublisher, never()).publish(any());
    }
}