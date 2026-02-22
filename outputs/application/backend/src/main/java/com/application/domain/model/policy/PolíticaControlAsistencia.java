package com.application.domain.model.policy;

import com.application.domain.shared.Entity;
import com.application.domain.shared.ValueObject;
import com.application.domain.model.alumno.Alumno;
import com.application.domain.model.asistencia.RegistroAsistencia;
import com.application.domain.model.event.AsistenciaRegistrada;
import com.application.domain.model.event.DomainEvent;
import com.application.domain.model.event.DomainEventPublisher;

import java.time.LocalDate;
import java.time.YearMonth;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public final class PolíticaControlAsistencia extends Entity<PolíticaControlAsistencia.PolíticaControlAsistenciaId> {

    public record PolíticaControlAsistenciaId(String value) implements ValueObject {}

    private final int umbralAusenciasParaNotificacion;
    private final DomainEventPublisher eventPublisher;

    public PolíticaControlAsistencia(PolíticaControlAsistenciaId id, int umbralAusenciasParaNotificacion, DomainEventPublisher eventPublisher) {
        super(id);
        this.umbralAusenciasParaNotificacion = umbralAusenciasParaNotificacion;
        this.eventPublisher = eventPublisher;
    }

    protected PolíticaControlAsistencia() {
        super();
        this.umbralAusenciasParaNotificacion = 0;
        this.eventPublisher = null;
    }

    public void verificarYNotificarAusencias(List<RegistroAsistencia> registrosDelMes, Alumno alumno) {
        if (registrosDelMes == null || alumno == null || eventPublisher == null) {
            return;
        }

        YearMonth mesActual = YearMonth.now();
        Map<YearMonth, List<RegistroAsistencia>> registrosPorMes = registrosDelMes.stream()
                .filter(reg -> reg != null && reg.getFecha() != null)
                .collect(Collectors.groupingBy(reg -> YearMonth.from(reg.getFecha())));

        List<RegistroAsistencia> registrosMesActual = registrosPorMes.get(mesActual);

        if (registrosMesActual != null) {
            long conteoAusenciasNoJustificadas = registrosMesActual.stream()
                    .filter(reg -> reg.getEstado() != null && "AUSENTE".equals(reg.getEstado().getValor()))
                    .count();

            if (conteoAusenciasNoJustificadas > umbralAusenciasParaNotificacion) {
                List<DomainEvent> eventosNotificacion = alumno.generarEventosNotificacionAusencias(
                        conteoAusenciasNoJustificadas,
                        umbralAusenciasParaNotificacion,
                        mesActual
                );
                eventosNotificacion.forEach(eventPublisher::publish);
            }
        }
    }

    public void onAsistenciaRegistrada(AsistenciaRegistrada evento) {
        // Este método podría ser usado por un manejador de eventos para reaccionar
        // a cada nueva asistencia registrada y evaluar el umbral en tiempo real.
        // La lógica actual se basa en una verificación por lotes (verificarYNotificarAusencias).
    }

    public int getUmbralAusenciasParaNotificacion() {
        return umbralAusenciasParaNotificacion;
    }
}