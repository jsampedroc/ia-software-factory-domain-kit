package com.application.interfaces.rest.Billing.payment.dto;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import static org.junit.jupiter.api.Assertions.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

class PaymentResponseDTOTest {

    @Test
    @DisplayName("PaymentResponseDTO builder should create object with correct values")
    void builder_ShouldCreateObjectWithCorrectValues() {
        UUID id = UUID.randomUUID();
        UUID invoiceId = UUID.randomUUID();
        BigDecimal amount = new BigDecimal("150.75");
        LocalDate paymentDate = LocalDate.of(2024, 1, 15);
        LocalDateTime createdAt = LocalDateTime.now();
        String paymentMethod = "BANK_TRANSFER";
        String transactionReference = "TX-123456";
        Boolean confirmed = true;

        PaymentResponseDTO dto = PaymentResponseDTO.builder()
                .id(id)
                .monthlyInvoiceId(invoiceId)
                .amount(amount)
                .paymentDate(paymentDate)
                .createdAt(createdAt)
                .paymentMethod(paymentMethod)
                .transactionReference(transactionReference)
                .confirmed(confirmed)
                .build();

        assertNotNull(dto);
        assertEquals(id, dto.getId());
        assertEquals(invoiceId, dto.getMonthlyInvoiceId());
        assertEquals(amount, dto.getAmount());
        assertEquals(paymentDate, dto.getPaymentDate());
        assertEquals(createdAt, dto.getCreatedAt());
        assertEquals(paymentMethod, dto.getPaymentMethod());
        assertEquals(transactionReference, dto.getTransactionReference());
        assertEquals(confirmed, dto.getConfirmed());
    }

    @Test
    @DisplayName("PaymentResponseDTO no-args constructor should create object")
    void noArgsConstructor_ShouldCreateObject() {
        PaymentResponseDTO dto = new PaymentResponseDTO();
        assertNotNull(dto);
        assertNull(dto.getId());
        assertNull(dto.getMonthlyInvoiceId());
        assertNull(dto.getAmount());
        assertNull(dto.getPaymentDate());
        assertNull(dto.getCreatedAt());
        assertNull(dto.getPaymentMethod());
        assertNull(dto.getTransactionReference());
        assertNull(dto.getConfirmed());
    }

    @Test
    @DisplayName("PaymentResponseDTO all-args constructor should create object with correct values")
    void allArgsConstructor_ShouldCreateObjectWithCorrectValues() {
        UUID id = UUID.randomUUID();
        UUID invoiceId = UUID.randomUUID();
        BigDecimal amount = new BigDecimal("99.99");
        LocalDate paymentDate = LocalDate.of(2024, 2, 1);
        LocalDateTime createdAt = LocalDateTime.of(2024, 2, 1, 10, 30);
        String paymentMethod = "CASH";
        String transactionReference = "REF-789";
        Boolean confirmed = false;

        PaymentResponseDTO dto = new PaymentResponseDTO(
                id, invoiceId, amount, paymentDate, createdAt,
                paymentMethod, transactionReference, confirmed
        );

        assertEquals(id, dto.getId());
        assertEquals(invoiceId, dto.getMonthlyInvoiceId());
        assertEquals(amount, dto.getAmount());
        assertEquals(paymentDate, dto.getPaymentDate());
        assertEquals(createdAt, dto.getCreatedAt());
        assertEquals(paymentMethod, dto.getPaymentMethod());
        assertEquals(transactionReference, dto.getTransactionReference());
        assertEquals(confirmed, dto.getConfirmed());
    }

    @Test
    @DisplayName("PaymentResponseDTO setters should update field values")
    void setters_ShouldUpdateFieldValues() {
        PaymentResponseDTO dto = new PaymentResponseDTO();

        UUID id = UUID.randomUUID();
        UUID invoiceId = UUID.randomUUID();
        BigDecimal amount = new BigDecimal("50.00");
        LocalDate paymentDate = LocalDate.now();
        LocalDateTime createdAt = LocalDateTime.now().minusHours(1);
        String paymentMethod = "CREDIT_CARD";
        String transactionReference = "TXN-ABC";
        Boolean confirmed = true;

        dto.setId(id);
        dto.setMonthlyInvoiceId(invoiceId);
        dto.setAmount(amount);
        dto.setPaymentDate(paymentDate);
        dto.setCreatedAt(createdAt);
        dto.setPaymentMethod(paymentMethod);
        dto.setTransactionReference(transactionReference);
        dto.setConfirmed(confirmed);

        assertEquals(id, dto.getId());
        assertEquals(invoiceId, dto.getMonthlyInvoiceId());
        assertEquals(amount, dto.getAmount());
        assertEquals(paymentDate, dto.getPaymentDate());
        assertEquals(createdAt, dto.getCreatedAt());
        assertEquals(paymentMethod, dto.getPaymentMethod());
        assertEquals(transactionReference, dto.getTransactionReference());
        assertEquals(confirmed, dto.getConfirmed());
    }

    @Test
    @DisplayName("PaymentResponseDTO equals and hashCode should work based on id")
    void equalsAndHashCode_ShouldWorkBasedOnId() {
        UUID id1 = UUID.randomUUID();
        UUID id2 = UUID.randomUUID();

        PaymentResponseDTO dto1 = PaymentResponseDTO.builder().id(id1).amount(new BigDecimal("100")).build();
        PaymentResponseDTO dto2 = PaymentResponseDTO.builder().id(id1).amount(new BigDecimal("200")).build();
        PaymentResponseDTO dto3 = PaymentResponseDTO.builder().id(id2).amount(new BigDecimal("100")).build();

        assertEquals(dto1, dto2);
        assertNotEquals(dto1, dto3);
        assertEquals(dto1.hashCode(), dto2.hashCode());
        assertNotEquals(dto1.hashCode(), dto3.hashCode());
    }

    @Test
    @DisplayName("PaymentResponseDTO toString should return non-null string")
    void toString_ShouldReturnNonNullString() {
        PaymentResponseDTO dto = PaymentResponseDTO.builder()
                .id(UUID.randomUUID())
                .monthlyInvoiceId(UUID.randomUUID())
                .amount(new BigDecimal("75.50"))
                .paymentDate(LocalDate.of(2024, 3, 10))
                .createdAt(LocalDateTime.of(2024, 3, 10, 9, 0))
                .paymentMethod("DEBIT_CARD")
                .transactionReference("DREF-555")
                .confirmed(true)
                .build();

        String result = dto.toString();
        assertNotNull(result);
        assertTrue(result.contains("75.50"));
        assertTrue(result.contains("DEBIT_CARD"));
    }
}