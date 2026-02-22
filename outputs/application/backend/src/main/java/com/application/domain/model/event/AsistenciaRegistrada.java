package com.application.domain.model.event;

import com.application.domain.shared.Entity;
import com.application.domain.shared.ValueObject;
import com.application.domain.valueobject.asistencia.EstadoAsistencia;

import java.time.LocalDate;
import java.time.LocalDateTime;

public record AsistenciaRegistrada(
        AlumnoId alumnoId,
        RegistroAsistenciaId registroAsistenciaId,
        LocalDate fecha,
        EstadoAsistencia estado,
        LocalDateTime ocurridoEn
) implements DomainEvent {
    public AsistenciaRegistrada {
        if (alumnoId == null) {
            throw new IllegalArgumentException("El ID del alumno no puede ser nulo");
        }
        if (registroAsistenciaId == null) {
            throw new IllegalArgumentException("El ID del registro de asistencia no puede ser nulo");
        }
        if (fecha == null) {
            throw new IllegalArgumentException("La fecha no puede ser nula");
        }
        if (estado == null) {
            throw new IllegalArgumentException("El estado no puede ser nulo");
        }
        if (ocurridoEn == null) {
            ocurridoEn = LocalDateTime.now();
        }
    }

    public AsistenciaRegistrada(AlumnoId alumnoId, RegistroAsistenciaId registroAsistenciaId, LocalDate fecha, EstadoAsistencia estado) {
        this(alumnoId, registroAsistenciaId, fecha, estado, LocalDateTime.now());
    }

    public record AlumnoId(String value) implements ValueObject {
        public AlumnoId {
            if (value == null || value.isBlank()) {
                throw new IllegalArgumentException("El ID del alumno no puede estar vacío");
            }
        }
    }

    public record RegistroAsistenciaId(String value) implements ValueObject {
        public RegistroAsistenciaId {
            if (value == null || value.isBlank()) {
                throw new IllegalArgumentException("El ID del registro de asistencia no puede estar vacío");
            }
        }
    }
}