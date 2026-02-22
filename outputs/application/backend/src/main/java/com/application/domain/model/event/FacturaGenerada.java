package com.application.domain.model.event;

import com.application.domain.shared.Entity;
import com.application.domain.shared.ValueObject;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

public record FacturaGenerada(
        UUID facturaId,
        String numeroFactura,
        UUID alumnoId,
        BigDecimal total,
        LocalDate fechaVencimiento
) implements ValueObject {}