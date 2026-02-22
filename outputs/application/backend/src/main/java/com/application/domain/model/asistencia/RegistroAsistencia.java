package com.application.domain.model.asistencia;

import com.application.domain.shared.Entity;
import com.application.domain.valueobject.asistencia.EstadoAsistencia;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalTime;

import static lombok.AccessLevel.PROTECTED;

@Getter
@Setter
@NoArgsConstructor(access = PROTECTED)
public class RegistroAsistencia extends Entity<RegistroAsistenciaId> {
    private LocalDate fecha;
    private LocalTime horaEntrada;
    private LocalTime horaSalida;
    private EstadoAsistencia estado;
    private String comentarios;

    public RegistroAsistencia(RegistroAsistenciaId id, LocalDate fecha, LocalTime horaEntrada, LocalTime horaSalida, EstadoAsistencia estado, String comentarios) {
        super(id);
        this.fecha = fecha;
        this.horaEntrada = horaEntrada;
        this.horaSalida = horaSalida;
        this.estado = estado;
        this.comentarios = comentarios;
    }

    public record RegistroAsistenciaId(String value) implements com.application.domain.shared.ValueObject {}
}