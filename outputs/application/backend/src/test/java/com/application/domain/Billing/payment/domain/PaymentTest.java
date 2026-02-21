package com.application.domain.Billing.payment.domain;

import com.application.domain.Billing.payment.domain.Payment;
import com.application.domain.Billing.payment.domain.PaymentId;
import com.application.domain.Billing.valueobject.MonthlyInvoiceId;
import com.application.domain.shared.valueobject.Money;
import com.application.domain.exception.DomainException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class PaymentTest {

    @Test
    void createPayment_WithValidData_ShouldCreatePayment() {
        PaymentId paymentId = new PaymentId(UUID.randomUUID());
        MonthlyInvoiceId invoiceId = new MonthlyInvoiceId(UUID.randomUUID());
        Money amount = new Money(new BigDecimal("150.75"), "USD");
        LocalDate paymentDate = LocalDate.now();
        String paymentMethod = "BANK_TRANSFER";
        String transactionReference = "TX-123456";

        Payment payment = Payment.create(paymentId, invoiceId, amount, paymentDate, paymentMethod, transactionReference);

        assertNotNull(payment);
        assertEquals(paymentId, payment.getId());
        assertEquals(invoiceId, payment.getMonthlyInvoiceId());
        assertEquals(amount, payment.getAmount());
        assertEquals(paymentDate, payment.getPaymentDate());
        assertEquals(paymentMethod, payment.getPaymentMethod());
        assertEquals(transactionReference, payment.getTransactionReference());
        assertFalse(payment.isConfirmed());
        assertNotNull(payment.getCreatedAt());
    }

    @Test
    void createPayment_WithNullId_ShouldThrowException() {
        MonthlyInvoiceId invoiceId = new MonthlyInvoiceId(UUID.randomUUID());
        Money amount = new Money(new BigDecimal("150.75"), "USD");
        LocalDate paymentDate = LocalDate.now();
        String paymentMethod = "BANK_TRANSFER";
        String transactionReference = "TX-123456";

        assertThrows(DomainException.class, () ->
                Payment.create(null, invoiceId, amount, paymentDate, paymentMethod, transactionReference)
        );
    }

    @Test
    void createPayment_WithNullInvoiceId_ShouldThrowException() {
        PaymentId paymentId = new PaymentId(UUID.randomUUID());
        Money amount = new Money(new BigDecimal("150.75"), "USD");
        LocalDate paymentDate = LocalDate.now();
        String paymentMethod = "BANK_TRANSFER";
        String transactionReference = "TX-123456";

        assertThrows(DomainException.class, () ->
                Payment.create(paymentId, null, amount, paymentDate, paymentMethod, transactionReference)
        );
    }

    @Test
    void createPayment_WithNullAmount_ShouldThrowException() {
        PaymentId paymentId = new PaymentId(UUID.randomUUID());
        MonthlyInvoiceId invoiceId = new MonthlyInvoiceId(UUID.randomUUID());
        LocalDate paymentDate = LocalDate.now();
        String paymentMethod = "BANK_TRANSFER";
        String transactionReference = "TX-123456";

        assertThrows(DomainException.class, () ->
                Payment.create(paymentId, invoiceId, null, paymentDate, paymentMethod, transactionReference)
        );
    }

    @Test
    void createPayment_WithZeroAmount_ShouldThrowException() {
        PaymentId paymentId = new PaymentId(UUID.randomUUID());
        MonthlyInvoiceId invoiceId = new MonthlyInvoiceId(UUID.randomUUID());
        Money zeroAmount = new Money(BigDecimal.ZERO, "USD");
        LocalDate paymentDate = LocalDate.now();
        String paymentMethod = "BANK_TRANSFER";
        String transactionReference = "TX-123456";

        assertThrows(DomainException.class, () ->
                Payment.create(paymentId, invoiceId, zeroAmount, paymentDate, paymentMethod, transactionReference)
        );
    }

    @Test
    void createPayment_WithNegativeAmount_ShouldThrowException() {
        PaymentId paymentId = new PaymentId(UUID.randomUUID());
        MonthlyInvoiceId invoiceId = new MonthlyInvoiceId(UUID.randomUUID());
        Money negativeAmount = new Money(new BigDecimal("-10.00"), "USD");
        LocalDate paymentDate = LocalDate.now();
        String paymentMethod = "BANK_TRANSFER";
        String transactionReference = "TX-123456";

        assertThrows(DomainException.class, () ->
                Payment.create(paymentId, invoiceId, negativeAmount, paymentDate, paymentMethod, transactionReference)
        );
    }

    @Test
    void createPayment_WithNullPaymentDate_ShouldThrowException() {
        PaymentId paymentId = new PaymentId(UUID.randomUUID());
        MonthlyInvoiceId invoiceId = new MonthlyInvoiceId(UUID.randomUUID());
        Money amount = new Money(new BigDecimal("150.75"), "USD");
        String paymentMethod = "BANK_TRANSFER";
        String transactionReference = "TX-123456";

        assertThrows(DomainException.class, () ->
                Payment.create(paymentId, invoiceId, amount, null, paymentMethod, transactionReference)
        );
    }

    @Test
    void createPayment_WithFuturePaymentDate_ShouldThrowException() {
        PaymentId paymentId = new PaymentId(UUID.randomUUID());
        MonthlyInvoiceId invoiceId = new MonthlyInvoiceId(UUID.randomUUID());
        Money amount = new Money(new BigDecimal("150.75"), "USD");
        LocalDate futureDate = LocalDate.now().plusDays(1);
        String paymentMethod = "BANK_TRANSFER";
        String transactionReference = "TX-123456";

        assertThrows(DomainException.class, () ->
                Payment.create(paymentId, invoiceId, amount, futureDate, paymentMethod, transactionReference)
        );
    }

    @Test
    void createPayment_WithNullPaymentMethod_ShouldThrowException() {
        PaymentId paymentId = new PaymentId(UUID.randomUUID());
        MonthlyInvoiceId invoiceId = new MonthlyInvoiceId(UUID.randomUUID());
        Money amount = new Money(new BigDecimal("150.75"), "USD");
        LocalDate paymentDate = LocalDate.now();
        String transactionReference = "TX-123456";

        assertThrows(DomainException.class, () ->
                Payment.create(paymentId, invoiceId, amount, paymentDate, null, transactionReference)
        );
    }

    @Test
    void createPayment_WithEmptyPaymentMethod_ShouldThrowException() {
        PaymentId paymentId = new PaymentId(UUID.randomUUID());
        MonthlyInvoiceId invoiceId = new MonthlyInvoiceId(UUID.randomUUID());
        Money amount = new Money(new BigDecimal("150.75"), "USD");
        LocalDate paymentDate = LocalDate.now();
        String transactionReference = "TX-123456";

        assertThrows(DomainException.class, () ->
                Payment.create(paymentId, invoiceId, amount, paymentDate, "", transactionReference)
        );
    }

    @Test
    void createPayment_WithInvalidPaymentMethod_ShouldThrowException() {
        PaymentId paymentId = new PaymentId(UUID.randomUUID());
        MonthlyInvoiceId invoiceId = new MonthlyInvoiceId(UUID.randomUUID());
        Money amount = new Money(new BigDecimal("150.75"), "USD");
        LocalDate paymentDate = LocalDate.now();
        String transactionReference = "TX-123456";

        assertThrows(DomainException.class, () ->
                Payment.create(paymentId, invoiceId, amount, paymentDate, "INVALID_METHOD", transactionReference)
        );
    }

    @Test
    void createPayment_WithValidPaymentMethods_ShouldCreatePayment() {
        PaymentId paymentId = new PaymentId(UUID.randomUUID());
        MonthlyInvoiceId invoiceId = new MonthlyInvoiceId(UUID.randomUUID());
        Money amount = new Money(new BigDecimal("150.75"), "USD");
        LocalDate paymentDate = LocalDate.now();
        String transactionReference = "TX-123456";

        String[] validMethods = {"CASH", "BANK_TRANSFER", "CREDIT_CARD", "DEBIT_CARD"};
        for (String method : validMethods) {
            Payment payment = Payment.create(paymentId, invoiceId, amount, paymentDate, method, transactionReference);
            assertEquals(method, payment.getPaymentMethod());
        }
    }

    @Test
    void createPayment_WithNullTransactionReference_ShouldCreatePayment() {
        PaymentId paymentId = new PaymentId(UUID.randomUUID());
        MonthlyInvoiceId invoiceId = new MonthlyInvoiceId(UUID.randomUUID());
        Money amount = new Money(new BigDecimal("150.75"), "USD");
        LocalDate paymentDate = LocalDate.now();
        String paymentMethod = "CASH";

        Payment payment = Payment.create(paymentId, invoiceId, amount, paymentDate, paymentMethod, null);

        assertNotNull(payment);
        assertNull(payment.getTransactionReference());
    }

    @Test
    void confirm_WhenPaymentIsNotConfirmed_ShouldConfirmPayment() {
        PaymentId paymentId = new PaymentId(UUID.randomUUID());
        MonthlyInvoiceId invoiceId = new MonthlyInvoiceId(UUID.randomUUID());
        Money amount = new Money(new BigDecimal("150.75"), "USD");
        LocalDate paymentDate = LocalDate.now();
        String paymentMethod = "BANK_TRANSFER";
        String transactionReference = "TX-123456";

        Payment payment = Payment.create(paymentId, invoiceId, amount, paymentDate, paymentMethod, transactionReference);
        assertFalse(payment.isConfirmed());

        LocalDateTime beforeConfirm = LocalDateTime.now();
        payment.confirm();
        LocalDateTime afterConfirm = LocalDateTime.now();

        assertTrue(payment.isConfirmed());
        assertNotNull(payment.getConfirmedAt());
        assertTrue(payment.getConfirmedAt().isAfter(beforeConfirm) || payment.getConfirmedAt().equals(beforeConfirm));
        assertTrue(payment.getConfirmedAt().isBefore(afterConfirm) || payment.getConfirmedAt().equals(afterConfirm));
    }

    @Test
    void confirm_WhenPaymentIsAlreadyConfirmed_ShouldThrowException() {
        PaymentId paymentId = new PaymentId(UUID.randomUUID());
        MonthlyInvoiceId invoiceId = new MonthlyInvoiceId(UUID.randomUUID());
        Money amount = new Money(new BigDecimal("150.75"), "USD");
        LocalDate paymentDate = LocalDate.now();
        String paymentMethod = "BANK_TRANSFER";
        String transactionReference = "TX-123456";

        Payment payment = Payment.create(paymentId, invoiceId, amount, paymentDate, paymentMethod, transactionReference);
        payment.confirm();
        assertTrue(payment.isConfirmed());

        assertThrows(DomainException.class, payment::confirm);
    }

    @Test
    void equals_WithSameId_ShouldReturnTrue() {
        UUID uuid = UUID.randomUUID();
        PaymentId paymentId1 = new PaymentId(uuid);
        PaymentId paymentId2 = new PaymentId(uuid);
        MonthlyInvoiceId invoiceId = new MonthlyInvoiceId(UUID.randomUUID());
        Money amount = new Money(new BigDecimal("150.75"), "USD");
        LocalDate paymentDate = LocalDate.now();
        String paymentMethod = "BANK_TRANSFER";
        String transactionReference = "TX-123456";

        Payment payment1 = Payment.create(paymentId1, invoiceId, amount, paymentDate, paymentMethod, transactionReference);
        Payment payment2 = Payment.create(paymentId2, invoiceId, amount, paymentDate, paymentMethod, transactionReference);

        assertEquals(payment1, payment2);
    }

    @Test
    void equals_WithDifferentId_ShouldReturnFalse() {
        PaymentId paymentId1 = new PaymentId(UUID.randomUUID());
        PaymentId paymentId2 = new PaymentId(UUID.randomUUID());
        MonthlyInvoiceId invoiceId = new MonthlyInvoiceId(UUID.randomUUID());
        Money amount = new Money(new BigDecimal("150.75"), "USD");
        LocalDate paymentDate = LocalDate.now();
        String paymentMethod = "BANK_TRANSFER";
        String transactionReference = "TX-123456";

        Payment payment1 = Payment.create(paymentId1, invoiceId, amount, paymentDate, paymentMethod, transactionReference);
        Payment payment2 = Payment.create(paymentId2, invoiceId, amount, paymentDate, paymentMethod, transactionReference);

        assertNotEquals(payment1, payment2);
    }
}