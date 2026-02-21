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

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PaymentRepositoryImplTest {

    @Mock
    private PaymentJpaRepository paymentJpaRepository;

    @Mock
    private PaymentEntityMapper paymentEntityMapper;

    @InjectMocks
    private PaymentRepositoryImpl paymentRepository;

    private PaymentId testPaymentId;
    private Payment testPayment;
    private PaymentEntity testPaymentEntity;

    @BeforeEach
    void setUp() {
        testPaymentId = new PaymentId(UUID.randomUUID());
        testPayment = mock(Payment.class);
        testPaymentEntity = new PaymentEntity();
        testPaymentEntity.setId(testPaymentId.getValue());
    }

    @Test
    void save_ShouldCallJpaRepositoryAndMapper() {
        when(paymentEntityMapper.toEntity(testPayment)).thenReturn(testPaymentEntity);
        when(paymentJpaRepository.save(testPaymentEntity)).thenReturn(testPaymentEntity);
        when(paymentEntityMapper.toDomain(testPaymentEntity)).thenReturn(testPayment);

        Payment result = paymentRepository.save(testPayment);

        assertThat(result).isEqualTo(testPayment);
        verify(paymentEntityMapper).toEntity(testPayment);
        verify(paymentJpaRepository).save(testPaymentEntity);
        verify(paymentEntityMapper).toDomain(testPaymentEntity);
    }

    @Test
    void findById_WhenEntityExists_ShouldReturnPayment() {
        when(paymentJpaRepository.findById(testPaymentId.getValue())).thenReturn(Optional.of(testPaymentEntity));
        when(paymentEntityMapper.toDomain(testPaymentEntity)).thenReturn(testPayment);

        Optional<Payment> result = paymentRepository.findById(testPaymentId);

        assertThat(result).isPresent().contains(testPayment);
        verify(paymentJpaRepository).findById(testPaymentId.getValue());
        verify(paymentEntityMapper).toDomain(testPaymentEntity);
    }

    @Test
    void findById_WhenEntityDoesNotExist_ShouldReturnEmpty() {
        when(paymentJpaRepository.findById(testPaymentId.getValue())).thenReturn(Optional.empty());

        Optional<Payment> result = paymentRepository.findById(testPaymentId);

        assertThat(result).isEmpty();
        verify(paymentJpaRepository).findById(testPaymentId.getValue());
        verify(paymentEntityMapper, never()).toDomain(any());
    }

    @Test
    void delete_ShouldCallJpaRepository() {
        paymentRepository.delete(testPayment);

        verify(paymentJpaRepository).delete(any(PaymentEntity.class));
    }

    @Test
    void existsById_WhenEntityExists_ShouldReturnTrue() {
        when(paymentJpaRepository.existsById(testPaymentId.getValue())).thenReturn(true);

        boolean result = paymentRepository.existsById(testPaymentId);

        assertThat(result).isTrue();
        verify(paymentJpaRepository).existsById(testPaymentId.getValue());
    }

    @Test
    void existsById_WhenEntityDoesNotExist_ShouldReturnFalse() {
        when(paymentJpaRepository.existsById(testPaymentId.getValue())).thenReturn(false);

        boolean result = paymentRepository.existsById(testPaymentId);

        assertThat(result).isFalse();
        verify(paymentJpaRepository).existsById(testPaymentId.getValue());
    }

    @Test
    void findConfirmedPaymentsForInvoice_ShouldCallJpaRepositoryAndMapResults() {
        UUID invoiceId = UUID.randomUUID();
        PaymentEntity entity1 = new PaymentEntity();
        PaymentEntity entity2 = new PaymentEntity();
        Payment payment1 = mock(Payment.class);
        Payment payment2 = mock(Payment.class);

        when(paymentJpaRepository.findByMonthlyInvoiceIdAndConfirmedIsTrue(invoiceId))
                .thenReturn(java.util.List.of(entity1, entity2));
        when(paymentEntityMapper.toDomain(entity1)).thenReturn(payment1);
        when(paymentEntityMapper.toDomain(entity2)).thenReturn(payment2);

        java.util.List<Payment> result = paymentRepository.findConfirmedPaymentsForInvoice(invoiceId);

        assertThat(result).hasSize(2).containsExactly(payment1, payment2);
        verify(paymentJpaRepository).findByMonthlyInvoiceIdAndConfirmedIsTrue(invoiceId);
        verify(paymentEntityMapper, times(2)).toDomain(any(PaymentEntity.class));
    }
}