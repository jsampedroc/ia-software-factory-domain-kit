package com.application.application.Billing.payment.application.process;

import com.application.domain.Billing.payment.domain.Payment;
import com.application.domain.Billing.payment.domain.PaymentId;
import com.application.domain.Billing.payment.domain.repository.PaymentRepository;
import com.application.domain.Billing.invoicing.domain.MonthlyInvoice;
import com.application.domain.Billing.invoicing.domain.MonthlyInvoiceId;
import com.application.domain.Billing.invoicing.domain.repository.MonthlyInvoiceRepository;
import com.application.domain.exception.DomainException;
import com.application.domain.shared.valueobject.Money;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
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

    @Captor
    private ArgumentCaptor<Payment> paymentCaptor;

    private ProcessPaymentCommandHandler handler;

    @BeforeEach
    void setUp() {
        handler = new ProcessPaymentCommandHandler(paymentRepository, monthlyInvoiceRepository);
    }

    @Test
    void handle_ShouldCreateAndSavePayment_WhenInvoiceExistsAndDataIsValid() {
        UUID invoiceId = UUID.randomUUID();
        UUID paymentId = UUID.randomUUID();
        BigDecimal amount = new BigDecimal("150.75");
        LocalDate paymentDate = LocalDate.now();
        String paymentMethod = "BANK_TRANSFER";
        String transactionReference = "TX-123456";

        MonthlyInvoice mockInvoice = mock(MonthlyInvoice.class);
        when(mockInvoice.getId()).thenReturn(new MonthlyInvoiceId(invoiceId));
        when(mockInvoice.getTotalAmount()).thenReturn(new Money(new BigDecimal("300.00")));
        when(mockInvoice.getStatus()).thenReturn(MonthlyInvoice.InvoiceStatus.PENDING);
        when(monthlyInvoiceRepository.findById(new MonthlyInvoiceId(invoiceId))).thenReturn(Optional.of(mockInvoice));
        when(paymentRepository.generateId()).thenReturn(new PaymentId(paymentId));

        ProcessPaymentCommand command = new ProcessPaymentCommand(
                invoiceId,
                amount,
                paymentDate,
                paymentMethod,
                transactionReference
        );

        handler.handle(command);

        verify(paymentRepository).save(paymentCaptor.capture());
        Payment savedPayment = paymentCaptor.getValue();

        assertNotNull(savedPayment);
        assertEquals(paymentId, savedPayment.getId().getValue());
        assertEquals(invoiceId, savedPayment.getMonthlyInvoiceId().getValue());
        assertEquals(amount, savedPayment.getAmount().getAmount());
        assertEquals(paymentDate, savedPayment.getPaymentDate());
        assertEquals(Payment.PaymentMethod.BANK_TRANSFER, savedPayment.getPaymentMethod());
        assertEquals(transactionReference, savedPayment.getTransactionReference());
        assertTrue(savedPayment.isConfirmed());
        verify(mockInvoice).registerPayment(any(Payment.class));
        verify(monthlyInvoiceRepository).save(mockInvoice);
    }

    @Test
    void handle_ShouldThrowDomainException_WhenInvoiceNotFound() {
        UUID invoiceId = UUID.randomUUID();
        when(monthlyInvoiceRepository.findById(new MonthlyInvoiceId(invoiceId))).thenReturn(Optional.empty());

        ProcessPaymentCommand command = new ProcessPaymentCommand(
                invoiceId,
                new BigDecimal("100.00"),
                LocalDate.now(),
                "CASH",
                "REF-001"
        );

        DomainException exception = assertThrows(DomainException.class, () -> handler.handle(command));
        assertTrue(exception.getMessage().contains("MonthlyInvoice not found"));
        verify(paymentRepository, never()).save(any());
        verify(monthlyInvoiceRepository, never()).save(any());
    }

    @Test
    void handle_ShouldThrowDomainException_WhenPaymentAmountIsZero() {
        UUID invoiceId = UUID.randomUUID();
        MonthlyInvoice mockInvoice = mock(MonthlyInvoice.class);
        when(mockInvoice.getId()).thenReturn(new MonthlyInvoiceId(invoiceId));
        when(monthlyInvoiceRepository.findById(new MonthlyInvoiceId(invoiceId))).thenReturn(Optional.of(mockInvoice));

        ProcessPaymentCommand command = new ProcessPaymentCommand(
                invoiceId,
                BigDecimal.ZERO,
                LocalDate.now(),
                "CASH",
                "REF-001"
        );

        DomainException exception = assertThrows(DomainException.class, () -> handler.handle(command));
        assertTrue(exception.getMessage().contains("greater than zero"));
        verify(paymentRepository, never()).save(any());
        verify(monthlyInvoiceRepository, never()).save(any());
    }

    @Test
    void handle_ShouldThrowDomainException_WhenPaymentAmountIsNegative() {
        UUID invoiceId = UUID.randomUUID();
        MonthlyInvoice mockInvoice = mock(MonthlyInvoice.class);
        when(mockInvoice.getId()).thenReturn(new MonthlyInvoiceId(invoiceId));
        when(monthlyInvoiceRepository.findById(new MonthlyInvoiceId(invoiceId))).thenReturn(Optional.of(mockInvoice));

        ProcessPaymentCommand command = new ProcessPaymentCommand(
                invoiceId,
                new BigDecimal("-50.00"),
                LocalDate.now(),
                "CASH",
                "REF-001"
        );

        DomainException exception = assertThrows(DomainException.class, () -> handler.handle(command));
        assertTrue(exception.getMessage().contains("greater than zero"));
        verify(paymentRepository, never()).save(any());
        verify(monthlyInvoiceRepository, never()).save(any());
    }

    @Test
    void handle_ShouldThrowDomainException_WhenPaymentMethodIsInvalid() {
        UUID invoiceId = UUID.randomUUID();
        MonthlyInvoice mockInvoice = mock(MonthlyInvoice.class);
        when(mockInvoice.getId()).thenReturn(new MonthlyInvoiceId(invoiceId));
        when(monthlyInvoiceRepository.findById(new MonthlyInvoiceId(invoiceId))).thenReturn(Optional.of(mockInvoice));

        ProcessPaymentCommand command = new ProcessPaymentCommand(
                invoiceId,
                new BigDecimal("100.00"),
                LocalDate.now(),
                "INVALID_METHOD",
                "REF-001"
        );

        DomainException exception = assertThrows(DomainException.class, () -> handler.handle(command));
        assertTrue(exception.getMessage().contains("Invalid payment method"));
        verify(paymentRepository, never()).save(any());
        verify(monthlyInvoiceRepository, never()).save(any());
    }

    @Test
    void handle_ShouldUseCurrentDate_WhenPaymentDateIsNull() {
        UUID invoiceId = UUID.randomUUID();
        UUID paymentId = UUID.randomUUID();
        MonthlyInvoice mockInvoice = mock(MonthlyInvoice.class);
        when(mockInvoice.getId()).thenReturn(new MonthlyInvoiceId(invoiceId));
        when(mockInvoice.getTotalAmount()).thenReturn(new Money(new BigDecimal("200.00")));
        when(mockInvoice.getStatus()).thenReturn(MonthlyInvoice.InvoiceStatus.PENDING);
        when(monthlyInvoiceRepository.findById(new MonthlyInvoiceId(invoiceId))).thenReturn(Optional.of(mockInvoice));
        when(paymentRepository.generateId()).thenReturn(new PaymentId(paymentId));

        ProcessPaymentCommand command = new ProcessPaymentCommand(
                invoiceId,
                new BigDecimal("100.00"),
                null,
                "CREDIT_CARD",
                "TX-789"
        );

        handler.handle(command);

        verify(paymentRepository).save(paymentCaptor.capture());
        Payment savedPayment = paymentCaptor.getValue();

        assertEquals(LocalDate.now(), savedPayment.getPaymentDate());
    }
}