package com.application.application.dto;

import com.application.domain.model.Invoice;
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
class InvoiceDTOTest {

    @Test
    void givenInvoiceDomain_whenFromDomain_thenReturnCorrectDTO() {
        // Given
        UUID invoiceUUID = UUID.randomUUID();
        UUID patientUUID = UUID.randomUUID();
        InvoiceId invoiceId = new InvoiceId(invoiceUUID);
        PatientId patientId = new PatientId(patientUUID);
        LocalDate fechaEmision = LocalDate.now();
        LocalDate fechaVencimiento = fechaEmision.plusDays(30);
        BigDecimal subtotal = new BigDecimal("1000.00");
        BigDecimal impuestos = new BigDecimal("160.00");
        BigDecimal total = new BigDecimal("1160.00");
        InvoiceStatus estado = InvoiceStatus.PENDIENTE;
        PaymentMethod metodoPago = PaymentMethod.TARJETA;
        String numeroFactura = "FAC-2024-00123";

        Invoice invoice = new Invoice(
                invoiceId,
                numeroFactura,
                fechaEmision,
                fechaVencimiento,
                subtotal,
                impuestos,
                total,
                estado,
                metodoPago,
                patientId
        );

        // When
        InvoiceDTO result = InvoiceDTO.fromDomain(invoice);

        // Then
        assertNotNull(result);
        assertEquals(invoiceUUID, result.invoiceId());
        assertEquals(numeroFactura, result.numeroFactura());
        assertEquals(fechaEmision, result.fechaEmision());
        assertEquals(fechaVencimiento, result.fechaVencimiento());
        assertEquals(subtotal, result.subtotal());
        assertEquals(impuestos, result.impuestos());
        assertEquals(total, result.total());
        assertEquals(estado, result.estado());
        assertEquals(metodoPago, result.metodoPago());
        assertEquals(patientUUID, result.patientId());
    }

    @Test
    void givenInvoiceDTO_whenToDomain_thenReturnCorrectDomain() {
        // Given
        UUID invoiceUUID = UUID.randomUUID();
        UUID patientUUID = UUID.randomUUID();
        LocalDate fechaEmision = LocalDate.now();
        LocalDate fechaVencimiento = fechaEmision.plusDays(30);
        BigDecimal subtotal = new BigDecimal("500.00");
        BigDecimal impuestos = new BigDecimal("80.00");
        BigDecimal total = new BigDecimal("580.00");
        InvoiceStatus estado = InvoiceStatus.PAGADA;
        PaymentMethod metodoPago = PaymentMethod.EFECTIVO;
        String numeroFactura = "FAC-2024-00567";

        InvoiceDTO dto = new InvoiceDTO(
                invoiceUUID,
                numeroFactura,
                fechaEmision,
                fechaVencimiento,
                subtotal,
                impuestos,
                total,
                estado,
                metodoPago,
                patientUUID
        );

        // When
        Invoice result = dto.toDomain();

        // Then
        assertNotNull(result);
        assertEquals(invoiceUUID, result.getId().value());
        assertEquals(numeroFactura, result.getNumeroFactura());
        assertEquals(fechaEmision, result.getFechaEmision());
        assertEquals(fechaVencimiento, result.getFechaVencimiento());
        assertEquals(subtotal, result.getSubtotal());
        assertEquals(impuestos, result.getImpuestos());
        assertEquals(total, result.getTotal());
        assertEquals(estado, result.getEstado());
        assertEquals(metodoPago, result.getMetodoPago());
        assertEquals(patientUUID, result.getPatientId().value());
    }

    @Test
    void givenTwoDTOsWithSameValues_whenEquals_thenShouldBeEqual() {
        UUID uuid = UUID.randomUUID();
        UUID patientUuid = UUID.randomUUID();
        LocalDate now = LocalDate.now();

        InvoiceDTO dto1 = new InvoiceDTO(
                uuid,
                "FAC-001",
                now,
                now.plusDays(30),
                new BigDecimal("100.00"),
                new BigDecimal("16.00"),
                new BigDecimal("116.00"),
                InvoiceStatus.PENDIENTE,
                PaymentMethod.TRANSFERENCIA,
                patientUuid
        );

        InvoiceDTO dto2 = new InvoiceDTO(
                uuid,
                "FAC-001",
                now,
                now.plusDays(30),
                new BigDecimal("100.00"),
                new BigDecimal("16.00"),
                new BigDecimal("116.00"),
                InvoiceStatus.PENDIENTE,
                PaymentMethod.TRANSFERENCIA,
                patientUuid
        );

        assertEquals(dto1, dto2);
        assertEquals(dto1.hashCode(), dto2.hashCode());
    }

    @Test
    void givenTwoDTOsWithDifferentId_whenEquals_thenShouldNotBeEqual() {
        UUID patientUuid = UUID.randomUUID();
        LocalDate now = LocalDate.now();

        InvoiceDTO dto1 = new InvoiceDTO(
                UUID.randomUUID(),
                "FAC-001",
                now,
                now.plusDays(30),
                new BigDecimal("100.00"),
                new BigDecimal("16.00"),
                new BigDecimal("116.00"),
                InvoiceStatus.PENDIENTE,
                PaymentMethod.TRANSFERENCIA,
                patientUuid
        );

        InvoiceDTO dto2 = new InvoiceDTO(
                UUID.randomUUID(),
                "FAC-001",
                now,
                now.plusDays(30),
                new BigDecimal("100.00"),
                new BigDecimal("16.00"),
                new BigDecimal("116.00"),
                InvoiceStatus.PENDIENTE,
                PaymentMethod.TRANSFERENCIA,
                patientUuid
        );

        assertNotEquals(dto1, dto2);
    }

    @Test
    void givenInvoiceDTO_whenToString_thenContainsRelevantInfo() {
        UUID invoiceUUID = UUID.randomUUID();
        UUID patientUUID = UUID.randomUUID();
        LocalDate fechaEmision = LocalDate.of(2024, 1, 15);
        String numeroFactura = "FAC-2024-00123";

        InvoiceDTO dto = new InvoiceDTO(
                invoiceUUID,
                numeroFactura,
                fechaEmision,
                fechaEmision.plusDays(30),
                new BigDecimal("200.00"),
                new BigDecimal("32.00"),
                new BigDecimal("232.00"),
                InvoiceStatus.VENCIDA,
                PaymentMethod.TARJETA,
                patientUUID
        );

        String result = dto.toString();

        assertTrue(result.contains(invoiceUUID.toString()));
        assertTrue(result.contains(numeroFactura));
        assertTrue(result.contains("VENCIDA"));
    }
}