package com.application.domain.valueobject;

import com.application.domain.shared.ValueObject;

public enum PaymentMethod implements ValueObject {
    EFECTIVO,
    TARJETA,
    TRANSFERENCIA
}