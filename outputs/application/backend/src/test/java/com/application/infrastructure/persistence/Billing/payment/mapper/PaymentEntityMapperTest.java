package com.application.infrastructure.persistence.Billing.payment.mapper;

import com.application.domain.Billing.payment.domain.Payment;
import com.application.domain.Billing.payment.domain.PaymentStatus;
import com.application.domain.Billing.valueobject.PaymentId;
import com.application.domain.shared.valueobject.Money;
import com.application.infrastructure.persistence.Billing.payment.entity.PaymentEntity;
import com.application.infrastructure.persistence.Billing.invoicing.entity.MonthlyInvoiceEntity;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class PaymentEntityMapperTest {

    private PaymentEntityMapper mapper;

    @Mock
    private MonthlyInvoiceEntity mockMonthlyInvoiceEntity;

    private final UUID paymentUuid = UUID.randomUUID();
    private final UUID invoiceUuid = UUID.randomUUID();
    private final LocalDateTime paymentDateTime = LocalDateTime.now();
    private final LocalDate paymentDate = LocalDate.now();

    @BeforeEach
    void setUp() {
        mapper = new PaymentEntityMapper();
    }

    @Test
    void toDomain_ShouldMapEntityToDomain() {
        PaymentEntity entity = new PaymentEntity();
        entity.setId(paymentUuid);
        entity.setMonthlyInvoice(mockMonthlyInvoiceEntity);
        entity.setAmount(new BigDecimal("150.75"));
        entity.setPaymentDate(paymentDate);
        entity.setPaymentMethod("BANK_TRANSFER");
        entity.setTransactionReference("TX-12345");
        entity.setConfirmed(true);
        entity.setCreatedAt(paymentDateTime);

        when(mockMonthlyInvoiceEntity.getId()).thenReturn(invoiceUuid);

        Payment result = mapper.toDomain(entity);

        assertNotNull(result);
        assertEquals(PaymentId.of(paymentUuid), result.getId());
        assertEquals(invoiceUuid, result.getMonthlyInvoiceId().getValue());
        assertEquals(new Money(new BigDecimal("150.75")), result.getAmount());
        assertEquals(paymentDate, result.getPaymentDate());
        assertEquals(Payment.PaymentMethod.BANK_TRANSFER, result.getPaymentMethod());
        assertEquals("TX-12345", result.getTransactionReference());
        assertTrue(result.isConfirmed());
        assertEquals(PaymentStatus.CONFIRMED, result.getStatus());
    }

    @Test
    void toDomain_WhenEntityIsNull_ShouldReturnNull() {
        Payment result = mapper.toDomain(null);
        assertNull(result);
    }

    @Test
    void toDomain_WhenPaymentMethodIsInvalid_ShouldThrowException() {
        PaymentEntity entity = new PaymentEntity();
        entity.setId(paymentUuid);
        entity.setMonthlyInvoice(mockMonthlyInvoiceEntity);
        entity.setAmount(BigDecimal.TEN);
        entity.setPaymentDate(paymentDate);
        entity.setPaymentMethod("INVALID_METHOD");
        entity.setTransactionReference("REF");
        entity.setConfirmed(false);

        when(mockMonthlyInvoiceEntity.getId()).thenReturn(invoiceUuid);

        assertThrows(IllegalArgumentException.class, () -> mapper.toDomain(entity));
    }

    @Test
    void toEntity_ShouldMapDomainToEntity() {
        Payment payment = Payment.builder()
                .id(PaymentId.of(paymentUuid))
                .monthlyInvoiceId(com.application.domain.Billing.valueobject.MonthlyInvoiceId.of(invoiceUuid))
                .amount(new Money(new BigDecimal("200.50")))
                .paymentDate(paymentDate)
                .paymentMethod(Payment.PaymentMethod.CREDIT_CARD)
                .transactionReference("TX-CC-67890")
                .confirmed(true)
                .build();

        PaymentEntity result = mapper.toEntity(payment, mockMonthlyInvoiceEntity);

        assertNotNull(result);
        assertEquals(paymentUuid, result.getId());
        assertSame(mockMonthlyInvoiceEntity, result.getMonthlyInvoice());
        assertEquals(new BigDecimal("200.50"), result.getAmount());
        assertEquals(paymentDate, result.getPaymentDate());
        assertEquals("CREDIT_CARD", result.getPaymentMethod());
        assertEquals("TX-CC-67890", result.getTransactionReference());
        assertTrue(result.getConfirmed());
    }

    @Test
    void toEntity_WhenDomainIsNull_ShouldReturnNull() {
        PaymentEntity result = mapper.toEntity(null, mockMonthlyInvoiceEntity);
        assertNull(result);
    }

    @Test
    void toEntity_WhenInvoiceEntityIsNull_ShouldThrowException() {
        Payment payment = Payment.builder()
                .id(PaymentId.of(paymentUuid))
                .monthlyInvoiceId(com.application.domain.Billing.valueobject.MonthlyInvoiceId.of(invoiceUuid))
                .amount(new Money(BigDecimal.TEN))
                .paymentDate(paymentDate)
                .paymentMethod(Payment.PaymentMethod.CASH)
                .transactionReference("REF")
                .confirmed(false)
                .build();

        assertThrows(NullPointerException.class, () -> mapper.toEntity(payment, null));
    }
}