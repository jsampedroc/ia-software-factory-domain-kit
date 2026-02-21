package com.application;

import com.application.application.Billing.payment.application.process.ProcessPaymentCommand;
import com.application.application.Billing.payment.application.process.ProcessPaymentCommandHandler;
import com.application.domain.Billing.invoicing.domain.MonthlyInvoice;
import com.application.domain.Billing.invoicing.domain.repository.MonthlyInvoiceRepository;
import com.application.domain.Billing.payment.domain.Payment;
import com.application.domain.Billing.payment.domain.PaymentId;
import com.application.domain.Billing.payment.domain.repository.PaymentRepository;
import com.application.domain.shared.valueobject.Money;
import com.application.domain.Billing.valueobject.MonthlyInvoiceId;
import com.application.domain.Billing.valueobject.PaymentId;
import com.application.domain.exception.DomainException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ProcessPaymentCommandHandlerTest {

    @Mock
    private PaymentRepository paymentRepository;
    @Mock
    private MonthlyInvoiceRepository monthlyInvoiceRepository;

    private ProcessPaymentCommandHandler handler;

    @BeforeEach
    void setUp() {
        handler = new ProcessPaymentCommandHandler(paymentRepository, monthlyInvoiceRepository);
    }

    @Test
    void handle_ShouldProcessPaymentSuccessfully() {
        UUID invoiceId = UUID.randomUUID();
        MonthlyInvoiceId monthlyInvoiceId = new MonthlyInvoiceId(invoiceId);
        BigDecimal amount = new BigDecimal("150.50");
        String paymentMethod = "BANK_TRANSFER";
        String transactionRef = "TX-12345";

        ProcessPaymentCommand command = new ProcessPaymentCommand(
                invoiceId,
                amount,
                paymentMethod,
                transactionRef
        );

        MonthlyInvoice mockInvoice = mock(MonthlyInvoice.class);
        when(monthlyInvoiceRepository.findById(monthlyInvoiceId)).thenReturn(Optional.of(mockInvoice));
        when(mockInvoice.getId()).thenReturn(monthlyInvoiceId);
        when(mockInvoice.registerPayment(any(Money.class), any(LocalDate.class), eq(paymentMethod), eq(transactionRef)))
                .thenReturn(new Payment(
                        new PaymentId(UUID.randomUUID()),
                        monthlyInvoiceId,
                        new Money(amount, "USD"),
                        LocalDate.now(),
                        paymentMethod,
                        transactionRef,
                        true
                ));

        PaymentId result = handler.handle(command);

        assertNotNull(result);
        verify(monthlyInvoiceRepository).findById(monthlyInvoiceId);
        verify(mockInvoice).registerPayment(any(Money.class), any(LocalDate.class), eq(paymentMethod), eq(transactionRef));
        verify(paymentRepository).save(any(Payment.class));
    }

    @Test
    void handle_ShouldThrowDomainException_WhenInvoiceNotFound() {
        UUID invoiceId = UUID.randomUUID();
        MonthlyInvoiceId monthlyInvoiceId = new MonthlyInvoiceId(invoiceId);
        BigDecimal amount = new BigDecimal("100.00");

        ProcessPaymentCommand command = new ProcessPaymentCommand(
                invoiceId,
                amount,
                "CASH",
                null
        );

        when(monthlyInvoiceRepository.findById(monthlyInvoiceId)).thenReturn(Optional.empty());

        DomainException exception = assertThrows(DomainException.class, () -> handler.handle(command));
        assertTrue(exception.getMessage().contains("MonthlyInvoice not found"));

        verify(monthlyInvoiceRepository).findById(monthlyInvoiceId);
        verify(paymentRepository, never()).save(any());
    }

    @Test
    void handle_ShouldPropagateDomainException_FromInvoiceRegisterPayment() {
        UUID invoiceId = UUID.randomUUID();
        MonthlyInvoiceId monthlyInvoiceId = new MonthlyInvoiceId(invoiceId);
        BigDecimal amount = new BigDecimal("-50.00");

        ProcessPaymentCommand command = new ProcessPaymentCommand(
                invoiceId,
                amount,
                "CREDIT_CARD",
                "REF-001"
        );

        MonthlyInvoice mockInvoice = mock(MonthlyInvoice.class);
        when(monthlyInvoiceRepository.findById(monthlyInvoiceId)).thenReturn(Optional.of(mockInvoice));
        when(mockInvoice.registerPayment(any(Money.class), any(LocalDate.class), anyString(), anyString()))
                .thenThrow(new DomainException("Payment amount must be positive"));

        DomainException exception = assertThrows(DomainException.class, () -> handler.handle(command));
        assertEquals("Payment amount must be positive", exception.getMessage());

        verify(monthlyInvoiceRepository).findById(monthlyInvoiceId);
        verify(paymentRepository, never()).save(any());
    }
}