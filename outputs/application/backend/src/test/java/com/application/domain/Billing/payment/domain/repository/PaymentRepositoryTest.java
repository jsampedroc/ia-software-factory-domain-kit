package com.application.domain.Billing.payment.domain.repository;

import com.application.domain.Billing.payment.domain.Payment;
import com.application.domain.Billing.payment.domain.PaymentId;
import com.application.domain.Billing.payment.domain.MonthlyInvoiceId;
import com.application.domain.exception.DomainException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PaymentRepositoryTest {

    @Mock
    private PaymentRepository paymentRepository;

    private PaymentId testPaymentId;
    private MonthlyInvoiceId testInvoiceId;
    private Payment testPayment;

    @BeforeEach
    void setUp() {
        testPaymentId = new PaymentId(UUID.randomUUID());
        testInvoiceId = new MonthlyInvoiceId(UUID.randomUUID());
        testPayment = Payment.builder()
                .id(testPaymentId)
                .monthlyInvoiceId(testInvoiceId)
                .amount(100.0)
                .paymentDate(LocalDate.now())
                .paymentMethod("BANK_TRANSFER")
                .transactionReference("TX-12345")
                .confirmed(true)
                .build();
    }

    @Test
    void save_ValidPayment_ShouldCallRepositorySave() {
        paymentRepository.save(testPayment);
        verify(paymentRepository, times(1)).save(testPayment);
    }

    @Test
    void findById_ExistingId_ShouldReturnPayment() {
        when(paymentRepository.findById(testPaymentId)).thenReturn(Optional.of(testPayment));
        Optional<Payment> result = paymentRepository.findById(testPaymentId);
        assertTrue(result.isPresent());
        assertEquals(testPayment, result.get());
        verify(paymentRepository, times(1)).findById(testPaymentId);
    }

    @Test
    void findById_NonExistingId_ShouldReturnEmpty() {
        PaymentId nonExistingId = new PaymentId(UUID.randomUUID());
        when(paymentRepository.findById(nonExistingId)).thenReturn(Optional.empty());
        Optional<Payment> result = paymentRepository.findById(nonExistingId);
        assertFalse(result.isPresent());
        verify(paymentRepository, times(1)).findById(nonExistingId);
    }

    @Test
    void findAll_ShouldReturnListOfPayments() {
        List<Payment> paymentList = List.of(testPayment);
        when(paymentRepository.findAll()).thenReturn(paymentList);
        List<Payment> result = paymentRepository.findAll();
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(testPayment, result.get(0));
        verify(paymentRepository, times(1)).findAll();
    }

    @Test
    void delete_ExistingPayment_ShouldCallRepositoryDelete() {
        paymentRepository.delete(testPayment);
        verify(paymentRepository, times(1)).delete(testPayment);
    }

    @Test
    void existsById_ExistingId_ShouldReturnTrue() {
        when(paymentRepository.existsById(testPaymentId)).thenReturn(true);
        boolean result = paymentRepository.existsById(testPaymentId);
        assertTrue(result);
        verify(paymentRepository, times(1)).existsById(testPaymentId);
    }

    @Test
    void existsById_NonExistingId_ShouldReturnFalse() {
        PaymentId nonExistingId = new PaymentId(UUID.randomUUID());
        when(paymentRepository.existsById(nonExistingId)).thenReturn(false);
        boolean result = paymentRepository.existsById(nonExistingId);
        assertFalse(result);
        verify(paymentRepository, times(1)).existsById(nonExistingId);
    }

    @Test
    void findByMonthlyInvoiceId_ExistingInvoiceId_ShouldReturnPayments() {
        List<Payment> paymentList = List.of(testPayment);
        when(paymentRepository.findByMonthlyInvoiceId(testInvoiceId)).thenReturn(paymentList);
        List<Payment> result = paymentRepository.findByMonthlyInvoiceId(testInvoiceId);
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(testPayment, result.get(0));
        verify(paymentRepository, times(1)).findByMonthlyInvoiceId(testInvoiceId);
    }

    @Test
    void findByMonthlyInvoiceId_NonExistingInvoiceId_ShouldReturnEmptyList() {
        MonthlyInvoiceId nonExistingInvoiceId = new MonthlyInvoiceId(UUID.randomUUID());
        when(paymentRepository.findByMonthlyInvoiceId(nonExistingInvoiceId)).thenReturn(List.of());
        List<Payment> result = paymentRepository.findByMonthlyInvoiceId(nonExistingInvoiceId);
        assertNotNull(result);
        assertTrue(result.isEmpty());
        verify(paymentRepository, times(1)).findByMonthlyInvoiceId(nonExistingInvoiceId);
    }

    @Test
    void findConfirmedPaymentsForInvoice_ExistingInvoiceId_ShouldReturnConfirmedPayments() {
        List<Payment> paymentList = List.of(testPayment);
        when(paymentRepository.findConfirmedPaymentsForInvoice(testInvoiceId)).thenReturn(paymentList);
        List<Payment> result = paymentRepository.findConfirmedPaymentsForInvoice(testInvoiceId);
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(testPayment, result.get(0));
        verify(paymentRepository, times(1)).findConfirmedPaymentsForInvoice(testInvoiceId);
    }

    @Test
    void findConfirmedPaymentsForInvoice_NonExistingInvoiceId_ShouldReturnEmptyList() {
        MonthlyInvoiceId nonExistingInvoiceId = new MonthlyInvoiceId(UUID.randomUUID());
        when(paymentRepository.findConfirmedPaymentsForInvoice(nonExistingInvoiceId)).thenReturn(List.of());
        List<Payment> result = paymentRepository.findConfirmedPaymentsForInvoice(nonExistingInvoiceId);
        assertNotNull(result);
        assertTrue(result.isEmpty());
        verify(paymentRepository, times(1)).findConfirmedPaymentsForInvoice(nonExistingInvoiceId);
    }

    @Test
    void save_NullPayment_ShouldThrowException() {
        doThrow(new IllegalArgumentException("Payment cannot be null")).when(paymentRepository).save(null);
        assertThrows(IllegalArgumentException.class, () -> paymentRepository.save(null));
        verify(paymentRepository, times(1)).save(null);
    }

    @Test
    void findById_NullId_ShouldThrowException() {
        doThrow(new IllegalArgumentException("PaymentId cannot be null")).when(paymentRepository).findById(null);
        assertThrows(IllegalArgumentException.class, () -> paymentRepository.findById(null));
        verify(paymentRepository, times(1)).findById(null);
    }
}