package com.application.domain.valueobject;

import com.application.domain.shared.ValueObject;
import java.util.UUID;

public record AppointmentId(UUID value) implements ValueObject {

    public AppointmentId {
        if (value == null) {
            throw new IllegalArgumentException("El ID de la cita no puede ser nulo");
        }
    }

    public static AppointmentId generate() {
        return new AppointmentId(UUID.randomUUID());
    }

    public static AppointmentId fromString(String value) {
        try {
            return new AppointmentId(UUID.fromString(value));
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("ID de cita inválido: " + value, e);
        }
    }

    @Override
    public String toString() {
        return value.toString();
    }
}