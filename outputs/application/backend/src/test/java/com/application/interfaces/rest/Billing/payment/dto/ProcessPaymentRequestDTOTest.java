package com.application;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class ProcessPaymentRequestDTOTest {

    @Test
    void testNoArgsConstructor() {
        ProcessPaymentRequestDTO dto = new ProcessPaymentRequestDTO();
        assertNotNull(dto);
        assertNull(dto.getMonthlyInvoiceId());
        assertNull(dto.getAmount());
        assertNull(dto.getPaymentDate());
        assertNull(dto.getPaymentMethod());
        assertNull(dto.getTransactionReference());
    }

    @Test
    void testAllArgsConstructor() {
        String invoiceId = "invoice-123";
        BigDecimal amount = new BigDecimal("150.75");
        LocalDate paymentDate = LocalDate.of(2024, 1, 15);
        String paymentMethod = "BANK_TRANSFER";
        String transactionRef = "TX-987654";

        ProcessPaymentRequestDTO dto = new ProcessPaymentRequestDTO(invoiceId, amount, paymentDate, paymentMethod, transactionRef);

        assertEquals(invoiceId, dto.getMonthlyInvoiceId());
        assertEquals(amount, dto.getAmount());
        assertEquals(paymentDate, dto.getPaymentDate());
        assertEquals(paymentMethod, dto.getPaymentMethod());
        assertEquals(transactionRef, dto.getTransactionReference());
    }

    @Test
    void testBuilder() {
        String invoiceId = "invoice-456";
        BigDecimal amount = new BigDecimal("200.00");
        LocalDate paymentDate = LocalDate.now();
        String paymentMethod = "CASH";
        String transactionRef = "REF-CASH-001";

        ProcessPaymentRequestDTO dto = ProcessPaymentRequestDTO.builder()
                .monthlyInvoiceId(invoiceId)
                .amount(amount)
                .paymentDate(paymentDate)
                .paymentMethod(paymentMethod)
                .transactionReference(transactionRef)
                .build();

        assertEquals(invoiceId, dto.getMonthlyInvoiceId());
        assertEquals(amount, dto.getAmount());
        assertEquals(paymentDate, dto.getPaymentDate());
        assertEquals(paymentMethod, dto.getPaymentMethod());
        assertEquals(transactionRef, dto.getTransactionReference());
    }

    @Test
    void testSettersAndGetters() {
        ProcessPaymentRequestDTO dto = new ProcessPaymentRequestDTO();

        String invoiceId = "invoice-789";
        BigDecimal amount = new BigDecimal("99.99");
        LocalDate paymentDate = LocalDate.of(2024, 2, 28);
        String paymentMethod = "CREDIT_CARD";
        String transactionRef = "TXN-CC-112233";

        dto.setMonthlyInvoiceId(invoiceId);
        dto.setAmount(amount);
        dto.setPaymentDate(paymentDate);
        dto.setPaymentMethod(paymentMethod);
        dto.setTransactionReference(transactionRef);

        assertEquals(invoiceId, dto.getMonthlyInvoiceId());
        assertEquals(amount, dto.getAmount());
        assertEquals(paymentDate, dto.getPaymentDate());
        assertEquals(paymentMethod, dto.getPaymentMethod());
        assertEquals(transactionRef, dto.getTransactionReference());
    }

    @Test
    void testEqualsAndHashCode() {
        String invoiceId = "invoice-eq";
        BigDecimal amount = new BigDecimal("50.00");
        LocalDate paymentDate = LocalDate.of(2024, 3, 10);
        String paymentMethod = "DEBIT_CARD";
        String transactionRef = "TXN-DB-555";

        ProcessPaymentRequestDTO dto1 = ProcessPaymentRequestDTO.builder()
                .monthlyInvoiceId(invoiceId)
                .amount(amount)
                .paymentDate(paymentDate)
                .paymentMethod(paymentMethod)
                .transactionReference(transactionRef)
                .build();

        ProcessPaymentRequestDTO dto2 = ProcessPaymentRequestDTO.builder()
                .monthlyInvoiceId(invoiceId)
                .amount(amount)
                .paymentDate(paymentDate)
                .paymentMethod(paymentMethod)
                .transactionReference(transactionRef)
                .build();

        ProcessPaymentRequestDTO dto3 = ProcessPaymentRequestDTO.builder()
                .monthlyInvoiceId("different-invoice")
                .amount(new BigDecimal("100.00"))
                .paymentDate(LocalDate.of(2024, 4, 1))
                .paymentMethod("CASH")
                .transactionReference("DIFF-REF")
                .build();

        assertEquals(dto1, dto2);
        assertEquals(dto1.hashCode(), dto2.hashCode());
        assertNotEquals(dto1, dto3);
        assertNotEquals(dto1.hashCode(), dto3.hashCode());
    }

    @Test
    void testToString() {
        ProcessPaymentRequestDTO dto = ProcessPaymentRequestDTO.builder()
                .monthlyInvoiceId("inv-001")
                .amount(new BigDecimal("75.50"))
                .paymentDate(LocalDate.of(2024, 5, 20))
                .paymentMethod("BANK_TRANSFER")
                .transactionReference("BANK-REF-001")
                .build();

        String toStringResult = dto.toString();

        assertTrue(toStringResult.contains("inv-001"));
        assertTrue(toStringResult.contains("75.50"));
        assertTrue(toStringResult.contains("2024-05-20"));
        assertTrue(toStringResult.contains("BANK_TRANSFER"));
        assertTrue(toStringResult.contains("BANK-REF-001"));
    }
}