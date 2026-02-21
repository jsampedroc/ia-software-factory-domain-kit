package com.application.infrastructure.persistence.Billing.payment.entity;

import com.application.domain.Billing.payment.domain.Payment;
import com.application.domain.Billing.payment.domain.PaymentStatus;
import com.application.domain.Billing.valueobject.MonthlyInvoiceId;
import com.application.domain.Billing.valueobject.PaymentId;
import com.application.domain.shared.valueobject.Money;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;

@ExtendWith(MockitoExtension.class)
class PaymentEntityTest {

    private PaymentEntity paymentEntity;
    private PaymentId paymentId;
    private MonthlyInvoiceId monthlyInvoiceId;
    private Money amount;
    private LocalDate paymentDate;
    private PaymentStatus status;
    private String paymentMethod;
    private String transactionReference;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    @BeforeEach
    void setUp() {
        paymentId = new PaymentId(UUID.randomUUID());
        monthlyInvoiceId = new MonthlyInvoiceId(UUID.randomUUID());
        amount = new Money(new BigDecimal("1500.50"), "USD");
        paymentDate = LocalDate.of(2024, 10, 15);
        status = PaymentStatus.CONFIRMED;
        paymentMethod = "BANK_TRANSFER";
        transactionReference = "TX-789456123";
        createdAt = LocalDateTime.of(2024, 10, 15, 10, 30);
        updatedAt = LocalDateTime.of(2024, 10, 15, 10, 35);

        paymentEntity = new PaymentEntity();
        paymentEntity.setId(paymentId.getValue());
        paymentEntity.setMonthlyInvoiceId(monthlyInvoiceId.getValue());
        paymentEntity.setAmount(amount.getAmount());
        paymentEntity.setCurrency(amount.getCurrency());
        paymentEntity.setPaymentDate(paymentDate);
        paymentEntity.setStatus(status);
        paymentEntity.setPaymentMethod(paymentMethod);
        paymentEntity.setTransactionReference(transactionReference);
        paymentEntity.setCreatedAt(createdAt);
        paymentEntity.setUpdatedAt(updatedAt);
    }

    @Test
    void testGettersAndSetters() {
        assertEquals(paymentId.getValue(), paymentEntity.getId());
        assertEquals(monthlyInvoiceId.getValue(), paymentEntity.getMonthlyInvoiceId());
        assertEquals(amount.getAmount(), paymentEntity.getAmount());
        assertEquals(amount.getCurrency(), paymentEntity.getCurrency());
        assertEquals(paymentDate, paymentEntity.getPaymentDate());
        assertEquals(status, paymentEntity.getStatus());
        assertEquals(paymentMethod, paymentEntity.getPaymentMethod());
        assertEquals(transactionReference, paymentEntity.getTransactionReference());
        assertEquals(createdAt, paymentEntity.getCreatedAt());
        assertEquals(updatedAt, paymentEntity.getUpdatedAt());
    }

    @Test
    void testNoArgsConstructor() {
        PaymentEntity newEntity = new PaymentEntity();
        assertNotNull(newEntity);
        assertNull(newEntity.getId());
        assertNull(newEntity.getMonthlyInvoiceId());
        assertNull(newEntity.getAmount());
        assertNull(newEntity.getCurrency());
        assertNull(newEntity.getPaymentDate());
        assertNull(newEntity.getStatus());
        assertNull(newEntity.getPaymentMethod());
        assertNull(newEntity.getTransactionReference());
        assertNull(newEntity.getCreatedAt());
        assertNull(newEntity.getUpdatedAt());
    }

    @Test
    void testAllArgsConstructor() {
        PaymentEntity newEntity = new PaymentEntity(
                paymentId.getValue(),
                monthlyInvoiceId.getValue(),
                amount.getAmount(),
                amount.getCurrency(),
                paymentDate,
                status,
                paymentMethod,
                transactionReference,
                createdAt,
                updatedAt
        );
        assertEquals(paymentId.getValue(), newEntity.getId());
        assertEquals(monthlyInvoiceId.getValue(), newEntity.getMonthlyInvoiceId());
        assertEquals(amount.getAmount(), newEntity.getAmount());
        assertEquals(amount.getCurrency(), newEntity.getCurrency());
        assertEquals(paymentDate, newEntity.getPaymentDate());
        assertEquals(status, newEntity.getStatus());
        assertEquals(paymentMethod, newEntity.getPaymentMethod());
        assertEquals(transactionReference, newEntity.getTransactionReference());
        assertEquals(createdAt, newEntity.getCreatedAt());
        assertEquals(updatedAt, newEntity.getUpdatedAt());
    }

    @Test
    void testBuilder() {
        PaymentEntity builtEntity = PaymentEntity.builder()
                .id(paymentId.getValue())
                .monthlyInvoiceId(monthlyInvoiceId.getValue())
                .amount(amount.getAmount())
                .currency(amount.getCurrency())
                .paymentDate(paymentDate)
                .status(status)
                .paymentMethod(paymentMethod)
                .transactionReference(transactionReference)
                .createdAt(createdAt)
                .updatedAt(updatedAt)
                .build();

        assertEquals(paymentId.getValue(), builtEntity.getId());
        assertEquals(monthlyInvoiceId.getValue(), builtEntity.getMonthlyInvoiceId());
        assertEquals(amount.getAmount(), builtEntity.getAmount());
        assertEquals(amount.getCurrency(), builtEntity.getCurrency());
        assertEquals(paymentDate, builtEntity.getPaymentDate());
        assertEquals(status, builtEntity.getStatus());
        assertEquals(paymentMethod, builtEntity.getPaymentMethod());
        assertEquals(transactionReference, builtEntity.getTransactionReference());
        assertEquals(createdAt, builtEntity.getCreatedAt());
        assertEquals(updatedAt, builtEntity.getUpdatedAt());
    }

    @Test
    void testEqualsAndHashCode() {
        PaymentEntity sameEntity = new PaymentEntity();
        sameEntity.setId(paymentId.getValue());
        sameEntity.setMonthlyInvoiceId(monthlyInvoiceId.getValue());
        sameEntity.setAmount(amount.getAmount());
        sameEntity.setCurrency(amount.getCurrency());
        sameEntity.setPaymentDate(paymentDate);
        sameEntity.setStatus(status);
        sameEntity.setPaymentMethod(paymentMethod);
        sameEntity.setTransactionReference(transactionReference);
        sameEntity.setCreatedAt(createdAt);
        sameEntity.setUpdatedAt(updatedAt);

        PaymentEntity differentEntity = new PaymentEntity();
        differentEntity.setId(UUID.randomUUID());
        differentEntity.setMonthlyInvoiceId(UUID.randomUUID());
        differentEntity.setAmount(new BigDecimal("99.99"));
        differentEntity.setCurrency("EUR");
        differentEntity.setPaymentDate(LocalDate.of(2024, 9, 1));
        differentEntity.setStatus(PaymentStatus.PENDING);
        differentEntity.setPaymentMethod("CASH");
        differentEntity.setTransactionReference("TX-OTHER");
        differentEntity.setCreatedAt(LocalDateTime.of(2024, 9, 1, 9, 0));
        differentEntity.setUpdatedAt(LocalDateTime.of(2024, 9, 1, 9, 5));

        assertEquals(paymentEntity, sameEntity);
        assertEquals(paymentEntity.hashCode(), sameEntity.hashCode());
        assertNotEquals(paymentEntity, differentEntity);
        assertNotEquals(paymentEntity.hashCode(), differentEntity.hashCode());
    }

    @Test
    void testToString() {
        String toString = paymentEntity.toString();
        assertTrue(toString.contains(paymentId.getValue().toString()));
        assertTrue(toString.contains(monthlyInvoiceId.getValue().toString()));
        assertTrue(toString.contains(amount.getAmount().toString()));
        assertTrue(toString.contains(amount.getCurrency()));
        assertTrue(toString.contains(paymentDate.toString()));
        assertTrue(toString.contains(status.toString()));
        assertTrue(toString.contains(paymentMethod));
        assertTrue(toString.contains(transactionReference));
        assertTrue(toString.contains(createdAt.toString()));
        assertTrue(toString.contains(updatedAt.toString()));
    }

    @Test
    void testToDomain() {
        Payment mockPayment = mock(Payment.class);
        PaymentEntityMapper mapperMock = mock(PaymentEntityMapper.class);
        assertThrows(UnsupportedOperationException.class, () -> paymentEntity.toDomain());
    }

    @Test
    void testFromDomain() {
        Payment mockPayment = mock(Payment.class);
        PaymentEntityMapper mapperMock = mock(PaymentEntityMapper.class);
        assertThrows(UnsupportedOperationException.class, () -> PaymentEntity.fromDomain(mockPayment));
    }
}