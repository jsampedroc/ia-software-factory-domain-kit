package com.application.domain.model;

import com.application.domain.valueobject.InvoiceId;
import com.application.domain.valueobject.InvoiceStatus;
import com.application.domain.valueobject.PaymentMethod;
import com.application.domain.valueobject.PatientId;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class InvoiceTest {

    private final String NUMERO_FACTURA = "FAC-2024-001";
    private final LocalDate FECHA_EMISION = LocalDate.now();
    private final LocalDate FECHA_VENCIMIENTO = LocalDate.now().plusDays(30);
    private final BigDecimal SUBTOTAL = new BigDecimal("1000.00");
    private final BigDecimal IMPUESTOS = new BigDecimal("210.00");
    private final BigDecimal TOTAL = new BigDecimal("1210.00");
    private final PatientId PATIENT_ID = new PatientId(UUID.randomUUID());

    @Test
    void create_ShouldCreateInvoiceWithCorrectInitialState() {
        Invoice invoice = Invoice.create(NUMERO_FACTURA, FECHA_EMISION, FECHA_VENCIMIENTO, SUBTOTAL, IMPUESTOS, PATIENT_ID);

        assertNotNull(invoice.getId());
        assertEquals(NUMERO_FACTURA, invoice.getNumeroFactura());
        assertEquals(FECHA_EMISION, invoice.getFechaEmision());
        assertEquals(FECHA_VENCIMIENTO, invoice.getFechaVencimiento());
        assertEquals(SUBTOTAL, invoice.getSubtotal());
        assertEquals(IMPUESTOS, invoice.getImpuestos());
        assertEquals(TOTAL, invoice.getTotal());
        assertEquals(InvoiceStatus.PENDIENTE, invoice.getEstado());
        assertNull(invoice.getMetodoPago());
        assertEquals(PATIENT_ID, invoice.getPatientId());
    }

    @Test
    void create_ShouldThrowExceptionWhenFechaVencimientoBeforeFechaEmision() {
        LocalDate fechaVencimientoInvalida = FECHA_EMISION.minusDays(1);

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                () -> Invoice.create(NUMERO_FACTURA, FECHA_EMISION, fechaVencimientoInvalida, SUBTOTAL, IMPUESTOS, PATIENT_ID));

        assertEquals("La fecha de vencimiento no puede ser anterior a la fecha de emisión", exception.getMessage());
    }

    @Test
    void create_ShouldThrowExceptionWhenSubtotalNegative() {
        BigDecimal subtotalNegativo = new BigDecimal("-100.00");

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                () -> Invoice.create(NUMERO_FACTURA, FECHA_EMISION, FECHA_VENCIMIENTO, subtotalNegativo, IMPUESTOS, PATIENT_ID));

        assertEquals("El subtotal no puede ser negativo", exception.getMessage());
    }

    @Test
    void create_ShouldThrowExceptionWhenImpuestosNegative() {
        BigDecimal impuestosNegativos = new BigDecimal("-50.00");

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                () -> Invoice.create(NUMERO_FACTURA, FECHA_EMISION, FECHA_VENCIMIENTO, SUBTOTAL, impuestosNegativos, PATIENT_ID));

        assertEquals("Los impuestos no pueden ser negativos", exception.getMessage());
    }

    @Test
    void create_ShouldThrowExceptionWhenTotalDoesNotMatchSubtotalPlusImpuestos() {
        InvoiceId fakeId = new InvoiceId(UUID.randomUUID());
        BigDecimal totalIncorrecto = new BigDecimal("1500.00");

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                () -> new Invoice(fakeId, NUMERO_FACTURA, FECHA_EMISION, FECHA_VENCIMIENTO, SUBTOTAL, IMPUESTOS, totalIncorrecto, InvoiceStatus.PENDIENTE, null, PATIENT_ID));

        assertEquals("El total debe ser igual al subtotal más los impuestos", exception.getMessage());
    }

    @Test
    void markAsPaid_ShouldUpdateEstadoAndMetodoPago() {
        Invoice invoice = Invoice.create(NUMERO_FACTURA, FECHA_EMISION, FECHA_VENCIMIENTO, SUBTOTAL, IMPUESTOS, PATIENT_ID);
        PaymentMethod metodoPago = PaymentMethod.TARJETA;

        invoice.markAsPaid(metodoPago);

        assertEquals(InvoiceStatus.PAGADA, invoice.getEstado());
        assertEquals(metodoPago, invoice.getMetodoPago());
    }

    @Test
    void markAsPaid_ShouldThrowExceptionWhenInvoiceAlreadyPaid() {
        Invoice invoice = Invoice.create(NUMERO_FACTURA, FECHA_EMISION, FECHA_VENCIMIENTO, SUBTOTAL, IMPUESTOS, PATIENT_ID);
        invoice.markAsPaid(PaymentMethod.EFECTIVO);

        IllegalStateException exception = assertThrows(IllegalStateException.class,
                () -> invoice.markAsPaid(PaymentMethod.TRANSFERENCIA));

        assertEquals("La factura ya está pagada", exception.getMessage());
    }

    @Test
    void markAsPaid_ShouldThrowExceptionWhenInvoiceIsCanceled() {
        Invoice invoice = Invoice.create(NUMERO_FACTURA, FECHA_EMISION, FECHA_VENCIMIENTO, SUBTOTAL, IMPUESTOS, PATIENT_ID);
        invoice.cancel();

        IllegalStateException exception = assertThrows(IllegalStateException.class,
                () -> invoice.markAsPaid(PaymentMethod.TARJETA));

        assertEquals("No se puede pagar una factura cancelada", exception.getMessage());
    }

    @Test
    void markAsPaid_ShouldThrowExceptionWhenMetodoPagoIsNull() {
        Invoice invoice = Invoice.create(NUMERO_FACTURA, FECHA_EMISION, FECHA_VENCIMIENTO, SUBTOTAL, IMPUESTOS, PATIENT_ID);

        NullPointerException exception = assertThrows(NullPointerException.class,
                () -> invoice.markAsPaid(null));

        assertEquals("Método de pago no puede ser nulo", exception.getMessage());
    }

    @Test
    void markAsOverdue_ShouldSetEstadoToVencidaWhenDateIsAfterVencimiento() {
        LocalDate fechaVencimientoPasada = LocalDate.now().minusDays(1);
        Invoice invoice = Invoice.create(NUMERO_FACTURA, FECHA_EMISION, fechaVencimientoPasada, SUBTOTAL, IMPUESTOS, PATIENT_ID);

        invoice.markAsOverdue();

        assertEquals(InvoiceStatus.VENCIDA, invoice.getEstado());
    }

    @Test
    void markAsOverdue_ShouldNotChangeEstadoWhenInvoiceIsPaid() {
        Invoice invoice = Invoice.create(NUMERO_FACTURA, FECHA_EMISION, FECHA_VENCIMIENTO, SUBTOTAL, IMPUESTOS, PATIENT_ID);
        invoice.markAsPaid(PaymentMethod.EFECTIVO);
        InvoiceStatus estadoAnterior = invoice.getEstado();

        invoice.markAsOverdue();

        assertEquals(estadoAnterior, invoice.getEstado());
    }

    @Test
    void markAsOverdue_ShouldNotChangeEstadoWhenInvoiceIsCanceled() {
        Invoice invoice = Invoice.create(NUMERO_FACTURA, FECHA_EMISION, FECHA_VENCIMIENTO, SUBTOTAL, IMPUESTOS, PATIENT_ID);
        invoice.cancel();
        InvoiceStatus estadoAnterior = invoice.getEstado();

        invoice.markAsOverdue();

        assertEquals(estadoAnterior, invoice.getEstado());
    }

    @Test
    void cancel_ShouldSetEstadoToCancelada() {
        Invoice invoice = Invoice.create(NUMERO_FACTURA, FECHA_EMISION, FECHA_VENCIMIENTO, SUBTOTAL, IMPUESTOS, PATIENT_ID);

        invoice.cancel();

        assertEquals(InvoiceStatus.CANCELADA, invoice.getEstado());
    }

    @Test
    void cancel_ShouldThrowExceptionWhenInvoiceIsPaid() {
        Invoice invoice = Invoice.create(NUMERO_FACTURA, FECHA_EMISION, FECHA_VENCIMIENTO, SUBTOTAL, IMPUESTOS, PATIENT_ID);
        invoice.markAsPaid(PaymentMethod.TARJETA);

        IllegalStateException exception = assertThrows(IllegalStateException.class,
                () -> invoice.cancel());

        assertEquals("No se puede cancelar una factura ya pagada", exception.getMessage());
    }

    @Test
    void applyDiscount_ShouldReduceSubtotalAndRecalculateTotal() {
        Invoice invoice = Invoice.create(NUMERO_FACTURA, FECHA_EMISION, FECHA_VENCIMIENTO, SUBTOTAL, IMPUESTOS, PATIENT_ID);
        BigDecimal discountPercentage = new BigDecimal("10.00");
        BigDecimal expectedSubtotal = new BigDecimal("900.00");
        BigDecimal expectedTotal = new BigDecimal("1110.00");

        invoice.applyDiscount(discountPercentage);

        assertEquals(expectedSubtotal, invoice.getSubtotal());
        assertEquals(expectedTotal, invoice.getTotal());
    }

    @Test
    void applyDiscount_ShouldThrowExceptionWhenDiscountPercentageIsNegative() {
        Invoice invoice = Invoice.create(NUMERO_FACTURA, FECHA_EMISION, FECHA_VENCIMIENTO, SUBTOTAL, IMPUESTOS, PATIENT_ID);
        BigDecimal discountPercentage = new BigDecimal("-5.00");

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                () -> invoice.applyDiscount(discountPercentage));

        assertEquals("El porcentaje de descuento debe estar entre 0 y 100", exception.getMessage());
    }

    @Test
    void applyDiscount_ShouldThrowExceptionWhenDiscountPercentageIsGreaterThan100() {
        Invoice invoice = Invoice.create(NUMERO_FACTURA, FECHA_EMISION, FECHA_VENCIMIENTO, SUBTOTAL, IMPUESTOS, PATIENT_ID);
        BigDecimal discountPercentage = new BigDecimal("150.00");

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                () -> invoice.applyDiscount(discountPercentage));

        assertEquals("El porcentaje de descuento debe estar entre 0 y 100", exception.getMessage());
    }

    @Test
    void applyDiscount_ShouldThrowExceptionWhenInvoiceIsNotPending() {
        Invoice invoice = Invoice.create(NUMERO_FACTURA, FECHA_EMISION, FECHA_VENCIMIENTO, SUBTOTAL, IMPUESTOS, PATIENT_ID);
        invoice.markAsPaid(PaymentMethod.EFECTIVO);
        BigDecimal discountPercentage = new BigDecimal("10.00");

        IllegalStateException exception = assertThrows(IllegalStateException.class,
                () -> invoice.applyDiscount(discountPercentage));

        assertEquals("Solo se puede aplicar descuento a facturas pendientes", exception.getMessage());
    }

    @Test
    void isOverdue_ShouldReturnTrueWhenEstadoIsVencida() {
        Invoice invoice = Invoice.create(NUMERO_FACTURA, FECHA_EMISION, FECHA_VENCIMIENTO, SUBTOTAL, IMPUESTOS, PATIENT_ID);
        invoice.markAsOverdue();

        assertTrue(invoice.isOverdue());
    }

    @Test
    void isOverdue_ShouldReturnFalseWhenEstadoIsNotVencida() {
        Invoice invoice = Invoice.create(NUMERO_FACTURA, FECHA_EMISION, FECHA_VENCIMIENTO, SUBTOTAL, IMPUESTOS, PATIENT_ID);

        assertFalse(invoice.isOverdue());
    }

    @Test
    void canApplyPayment_ShouldReturnTrueWhenEstadoIsPending() {
        Invoice invoice = Invoice.create(NUMERO_FACTURA, FECHA_EMISION, FECHA_VENCIMIENTO, SUBTOTAL, IMPUESTOS, PATIENT_ID);

        assertTrue(invoice.canApplyPayment());
    }

    @Test
    void canApplyPayment_ShouldReturnTrueWhenEstadoIsVencida() {
        Invoice invoice = Invoice.create(NUMERO_FACTURA, FECHA_EMISION, FECHA_VENCIMIENTO, SUBTOTAL, IMPUESTOS, PATIENT_ID);
        invoice.markAsOverdue();

        assertTrue(invoice.canApplyPayment());
    }

    @Test
    void canApplyPayment_ShouldReturnFalseWhenEstadoIsPaid() {
        Invoice invoice = Invoice.create(NUMERO_FACTURA, FECHA_EMISION, FECHA_VENCIMIENTO, SUBTOTAL, IMPUESTOS, PATIENT_ID);
        invoice.markAsPaid(PaymentMethod.TRANSFERENCIA);

        assertFalse(invoice.canApplyPayment());
    }

    @Test
    void canApplyPayment_ShouldReturnFalseWhenEstadoIsCanceled() {
        Invoice invoice = Invoice.create(NUMERO_FACTURA, FECHA_EMISION, FECHA_VENCIMIENTO, SUBTOTAL, IMPUESTOS, PATIENT_ID);
        invoice.cancel();

        assertFalse(invoice.canApplyPayment());
    }
}