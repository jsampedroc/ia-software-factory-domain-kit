package com.application.infrastructure.persistence.Billing.payment.adapter;

import com.application.domain.Billing.payment.domain.Payment;
import com.application.domain.Billing.payment.domain.repository.PaymentRepository;
import com.application.domain.Billing.valueobject.PaymentId;
import com.application.domain.shared.valueobject.Money;
import com.application.infrastructure.persistence.Billing.payment.entity.PaymentEntity;
import com.application.infrastructure.persistence.Billing.payment.mapper.PaymentEntityMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PaymentJpaRepositoryTest {

    @Mock
    private PaymentJpaRepository paymentJpaRepository;

    @Mock
    private PaymentEntityMapper mapper;

    @InjectMocks
    private PaymentRepositoryImpl repository;

    private PaymentId testPaymentId;
    private Payment testPayment;
    private PaymentEntity testPaymentEntity;
    private UUID testMonthlyInvoiceId;

    @BeforeEach
    void setUp() {
        testPaymentId = new PaymentId(UUID.randomUUID());
        testMonthlyInvoiceId = UUID.randomUUID();

        testPayment = Payment.builder()
                .id(testPaymentId)
                .monthlyInvoiceId(testMonthlyInvoiceId)
                .amount(new Money(new BigDecimal("150.75"), "USD"))
                .paymentDate(LocalDate.now())
                .paymentMethod("BANK_TRANSFER")
                .transactionReference("TX-12345")
                .confirmed(true)
                .build();

        testPaymentEntity = new PaymentEntity();
        testPaymentEntity.setId(testPaymentId.getValue());
        testPaymentEntity.setMonthlyInvoiceId(testMonthlyInvoiceId);
        testPaymentEntity.setAmount(new BigDecimal("150.75"));
        testPaymentEntity.setCurrency("USD");
        testPaymentEntity.setPaymentDate(LocalDate.now());
        testPaymentEntity.setPaymentMethod("BANK_TRANSFER");
        testPaymentEntity.setTransactionReference("TX-12345");
        testPaymentEntity.setConfirmed(true);
        testPaymentEntity.setCreatedAt(LocalDateTime.now());
    }

    @Test
    void findById_WhenPaymentExists_ShouldReturnPayment() {
        when(paymentJpaRepository.findById(testPaymentId.getValue())).thenReturn(Optional.of(testPaymentEntity));
        when(mapper.toDomain(testPaymentEntity)).thenReturn(testPayment);

        Optional<Payment> result = repository.findById(testPaymentId);

        assertTrue(result.isPresent());
        assertEquals(testPaymentId, result.get().getId());
        verify(paymentJpaRepository).findById(testPaymentId.getValue());
        verify(mapper).toDomain(testPaymentEntity);
    }

    @Test
    void findById_WhenPaymentDoesNotExist_ShouldReturnEmpty() {
        when(paymentJpaRepository.findById(testPaymentId.getValue())).thenReturn(Optional.empty());

        Optional<Payment> result = repository.findById(testPaymentId);

        assertFalse(result.isPresent());
        verify(paymentJpaRepository).findById(testPaymentId.getValue());
        verify(mapper, never()).toDomain(any());
    }

    @Test
    void save_ShouldPersistAndReturnPayment() {
        when(mapper.toEntity(testPayment)).thenReturn(testPaymentEntity);
        when(paymentJpaRepository.save(testPaymentEntity)).thenReturn(testPaymentEntity);
        when(mapper.toDomain(testPaymentEntity)).thenReturn(testPayment);

        Payment savedPayment = repository.save(testPayment);

        assertNotNull(savedPayment);
        assertEquals(testPaymentId, savedPayment.getId());
        verify(mapper).toEntity(testPayment);
        verify(paymentJpaRepository).save(testPaymentEntity);
        verify(mapper).toDomain(testPaymentEntity);
    }

    @Test
    void delete_ShouldCallJpaRepositoryDelete() {
        repository.delete(testPayment);

        verify(paymentJpaRepository).deleteById(testPaymentId.getValue());
    }

    @Test
    void existsById_WhenPaymentExists_ShouldReturnTrue() {
        when(paymentJpaRepository.existsById(testPaymentId.getValue())).thenReturn(true);

        boolean exists = repository.existsById(testPaymentId);

        assertTrue(exists);
        verify(paymentJpaRepository).existsById(testPaymentId.getValue());
    }

    @Test
    void existsById_WhenPaymentDoesNotExist_ShouldReturnFalse() {
        when(paymentJpaRepository.existsById(testPaymentId.getValue())).thenReturn(false);

        boolean exists = repository.existsById(testPaymentId);

        assertFalse(exists);
        verify(paymentJpaRepository).existsById(testPaymentId.getValue());
    }

    @Test
    void findByMonthlyInvoiceId_WhenPaymentsExist_ShouldReturnList() {
        UUID invoiceId = UUID.randomUUID();
        when(paymentJpaRepository.findByMonthlyInvoiceId(invoiceId)).thenReturn(java.util.List.of(testPaymentEntity));
        when(mapper.toDomain(testPaymentEntity)).thenReturn(testPayment);

        java.util.List<Payment> result = repository.findByMonthlyInvoiceId(invoiceId);

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(testPaymentId, result.get(0).getId());
        verify(paymentJpaRepository).findByMonthlyInvoiceId(invoiceId);
        verify(mapper).toDomain(testPaymentEntity);
    }

    @Test
    void findByMonthlyInvoiceId_WhenNoPaymentsExist_ShouldReturnEmptyList() {
        UUID invoiceId = UUID.randomUUID();
        when(paymentJpaRepository.findByMonthlyInvoiceId(invoiceId)).thenReturn(java.util.List.of());

        java.util.List<Payment> result = repository.findByMonthlyInvoiceId(invoiceId);

        assertNotNull(result);
        assertTrue(result.isEmpty());
        verify(paymentJpaRepository).findByMonthlyInvoiceId(invoiceId);
        verify(mapper, never()).toDomain(any());
    }

    @Test
    void findByTransactionReference_WhenPaymentExists_ShouldReturnPayment() {
        String reference = "TX-12345";
        when(paymentJpaRepository.findByTransactionReference(reference)).thenReturn(Optional.of(testPaymentEntity));
        when(mapper.toDomain(testPaymentEntity)).thenReturn(testPayment);

        Optional<Payment> result = repository.findByTransactionReference(reference);

        assertTrue(result.isPresent());
        assertEquals(reference, result.get().getTransactionReference());
        verify(paymentJpaRepository).findByTransactionReference(reference);
        verify(mapper).toDomain(testPaymentEntity);
    }

    @Test
    void findByTransactionReference_WhenPaymentDoesNotExist_ShouldReturnEmpty() {
        String reference = "NON-EXISTENT";
        when(paymentJpaRepository.findByTransactionReference(reference)).thenReturn(Optional.empty());

        Optional<Payment> result = repository.findByTransactionReference(reference);

        assertFalse(result.isPresent());
        verify(paymentJpaRepository).findByTransactionReference(reference);
        verify(mapper, never()).toDomain(any());
    }
}