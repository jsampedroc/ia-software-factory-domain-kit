package com.application.application.dto;

import com.application.domain.valueobject.InvoiceId;
import com.application.domain.valueobject.InvoiceStatus;
import com.application.domain.valueobject.PaymentMethod;
import com.application.domain.valueobject.PatientId;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

public record InvoiceDTO(
        UUID invoiceId,
        String numeroFactura,
        LocalDate fechaEmision,
        LocalDate fechaVencimiento,
        BigDecimal subtotal,
        BigDecimal impuestos,
        BigDecimal total,
        InvoiceStatus estado,
        PaymentMethod metodoPago,
        UUID patientId
) {
    public static InvoiceDTO fromDomain(com.application.domain.model.Invoice invoice) {
        return new InvoiceDTO(
                invoice.getId().value(),
                invoice.getNumeroFactura(),
                invoice.getFechaEmision(),
                invoice.getFechaVencimiento(),
                invoice.getSubtotal(),
                invoice.getImpuestos(),
                invoice.getTotal(),
                invoice.getEstado(),
                invoice.getMetodoPago(),
                invoice.getPatientId().value()
        );
    }

    public com.application.domain.model.Invoice toDomain() {
        return new com.application.domain.model.Invoice(
                new InvoiceId(invoiceId),
                numeroFactura,
                fechaEmision,
                fechaVencimiento,
                subtotal,
                impuestos,
                total,
                estado,
                metodoPago,
                new PatientId(patientId)
        );
    }
}