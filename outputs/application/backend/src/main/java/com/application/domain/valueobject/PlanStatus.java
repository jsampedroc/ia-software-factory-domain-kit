package com.application.domain.valueobject;

import com.application.domain.shared.ValueObject;

public enum PlanStatus implements ValueObject {
    BORRADOR,
    ACTIVO,
    COMPLETADO,
    CANCELADO
}