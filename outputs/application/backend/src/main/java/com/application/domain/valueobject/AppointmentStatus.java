package com.application.domain.valueobject;

import com.application.domain.shared.ValueObject;

public enum AppointmentStatus implements ValueObject {
    PROGRAMADA,
    CONFIRMADA,
    EN_CURSO,
    COMPLETADA,
    CANCELADA
}