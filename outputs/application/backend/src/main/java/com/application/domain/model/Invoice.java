package com.application.domain.model;

import com.application.domain.shared.Entity;
import com.application.domain.valueobject.InvoiceId;
import com.application.domain.valueobject.InvoiceStatus;
import com.application.domain.valueobject.PaymentMethod;
import com.application.domain.valueobject.PatientId;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Objects;

import static lombok.AccessLevel.PROTECTED;

@Getter
@NoArgsConstructor(access = PROTECTED)
public class Invoice extends Entity<InvoiceId> {
    private String numeroFactura;
    private LocalDate fechaEmision;
    private LocalDate fechaVencimiento;
    private BigDecimal subtotal;
    private BigDecimal impuestos;
    private BigDecimal total;
    private InvoiceStatus estado;
    private PaymentMethod metodoPago;
    private PatientId patientId;

    private Invoice(InvoiceId invoiceId,
                    String numeroFactura,
                    LocalDate fechaEmision,
                    LocalDate fechaVencimiento,
                    BigDecimal subtotal,
                    BigDecimal impuestos,
                    BigDecimal total,
                    InvoiceStatus estado,
                    PaymentMethod metodoPago,
                    PatientId patientId) {
        super(invoiceId);
        this.numeroFactura = Objects.requireNonNull(numeroFactura, "Número de factura no puede ser nulo");
        this.fechaEmision = Objects.requireNonNull(fechaEmision, "Fecha de emisión no puede ser nula");
        this.fechaVencimiento = Objects.requireNonNull(fechaVencimiento, "Fecha de vencimiento no puede ser nula");
        this.subtotal = Objects.requireNonNull(subtotal, "Subtotal no puede ser nulo");
        this.impuestos = Objects.requireNonNull(impuestos, "Impuestos no pueden ser nulos");
        this.total = Objects.requireNonNull(total, "Total no puede ser nulo");
        this.estado = Objects.requireNonNull(estado, "Estado no puede ser nulo");
        this.metodoPago = metodoPago; // Puede ser nulo si la factura está pendiente
        this.patientId = Objects.requireNonNull(patientId, "ID de paciente no puede ser nulo");
        validate();
    }

    public static Invoice create(String numeroFactura,
                                 LocalDate fechaEmision,
                                 LocalDate fechaVencimiento,
                                 BigDecimal subtotal,
                                 BigDecimal impuestos,
                                 PatientId patientId) {
        BigDecimal totalCalculado = subtotal.add(impuestos);
        return new Invoice(
                InvoiceId.generate(),
                numeroFactura,
                fechaEmision,
                fechaVencimiento,
                subtotal,
                impuestos,
                totalCalculado,
                InvoiceStatus.PENDIENTE,
                null,
                patientId
        );
    }

    public void markAsPaid(PaymentMethod metodoPago) {
        if (this.estado == InvoiceStatus.PAGADA) {
            throw new IllegalStateException("La factura ya está pagada");
        }
        if (this.estado == InvoiceStatus.CANCELADA) {
            throw new IllegalStateException("No se puede pagar una factura cancelada");
        }
        this.metodoPago = Objects.requireNonNull(metodoPago, "Método de pago no puede ser nulo");
        this.estado = InvoiceStatus.PAGADA;
    }

    public void markAsOverdue() {
        if (this.estado == InvoiceStatus.PAGADA || this.estado == InvoiceStatus.CANCELADA) {
            return;
        }
        if (LocalDate.now().isAfter(fechaVencimiento)) {
            this.estado = InvoiceStatus.VENCIDA;
        }
    }

    public void cancel() {
        if (this.estado == InvoiceStatus.PAGADA) {
            throw new IllegalStateException("No se puede cancelar una factura ya pagada");
        }
        this.estado = InvoiceStatus.CANCELADA;
    }

    public void applyDiscount(BigDecimal discountPercentage) {
        if (discountPercentage.compareTo(BigDecimal.ZERO) < 0 || discountPercentage.compareTo(new BigDecimal("100")) > 0) {
            throw new IllegalArgumentException("El porcentaje de descuento debe estar entre 0 y 100");
        }
        if (this.estado != InvoiceStatus.PENDIENTE) {
            throw new IllegalStateException("Solo se puede aplicar descuento a facturas pendientes");
        }
        BigDecimal discountFactor = discountPercentage.divide(new BigDecimal("100"));
        BigDecimal discountAmount = this.subtotal.multiply(discountFactor);
        this.subtotal = this.subtotal.subtract(discountAmount);
        recalculateTotal();
    }

    private void recalculateTotal() {
        this.total = this.subtotal.add(this.impuestos);
    }

    private void validate() {
        if (fechaVencimiento.isBefore(fechaEmision)) {
            throw new IllegalArgumentException("La fecha de vencimiento no puede ser anterior a la fecha de emisión");
        }
        if (subtotal.compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException("El subtotal no puede ser negativo");
        }
        if (impuestos.compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException("Los impuestos no pueden ser negativos");
        }
        if (total.compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException("El total no puede ser negativo");
        }
        if (!total.equals(subtotal.add(impuestos))) {
            throw new IllegalArgumentException("El total debe ser igual al subtotal más los impuestos");
        }
    }

    public boolean isOverdue() {
        return this.estado == InvoiceStatus.VENCIDA;
    }

    public boolean canApplyPayment() {
        return this.estado == InvoiceStatus.PENDIENTE || this.estado == InvoiceStatus.VENCIDA;
    }
}