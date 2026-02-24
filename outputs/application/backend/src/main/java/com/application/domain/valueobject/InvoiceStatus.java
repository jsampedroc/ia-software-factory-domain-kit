package com.application.domain.valueobject;

import com.application.domain.shared.ValueObject;

public enum InvoiceStatus implements ValueObject {
    PENDIENTE,
    PAGADA,
    VENCIDA,
    CANCELADA
}