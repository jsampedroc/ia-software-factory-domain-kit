package com.application.domain.model;

import com.application.domain.valueobject.PaymentId;
import com.application.domain.valueobject.InvoiceId;
import com.application.domain.enums.PaymentMethod;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@ExtendWith(MockitoExtension.class)
class PaymentTest {

    private final PaymentId validPaymentId = new PaymentId(UUID.randomUUID());
    private final InvoiceId validInvoiceId = new InvoiceId(UUID.randomUUID());
    private final BigDecimal validAmount = new BigDecimal("150.75");
    private final PaymentMethod validMethod = PaymentMethod.CARD;
    private final LocalDateTime validPaidAt = LocalDateTime.now();
    private final String validReferenceNumber = "REF-2024-001";

    @Test
    void shouldCreatePaymentSuccessfully() {
        // When
        Payment payment = new Payment(validPaymentId, validInvoiceId, validAmount, validMethod, validPaidAt, validReferenceNumber);

        // Then
        assertThat(payment).isNotNull();
        assertThat(payment.getId()).isEqualTo(validPaymentId);
        assertThat(payment.getInvoiceId()).isEqualTo(validInvoiceId);
        assertThat(payment.getAmount()).isEqualTo(validAmount);
        assertThat(payment.getMethod()).isEqualTo(validMethod);
        assertThat(payment.getPaidAt()).isEqualTo(validPaidAt);
        assertThat(payment.getReferenceNumber()).isEqualTo(validReferenceNumber);
    }

    @Test
    void shouldThrowExceptionWhenInvoiceIdIsNull() {
        // When & Then
        assertThatThrownBy(() -> new Payment(validPaymentId, null, validAmount, validMethod, validPaidAt, validReferenceNumber))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Invoice ID cannot be null");
    }

    @Test
    void shouldThrowExceptionWhenAmountIsNull() {
        // When & Then
        assertThatThrownBy(() -> new Payment(validPaymentId, validInvoiceId, null, validMethod, validPaidAt, validReferenceNumber))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Payment amount must be positive");
    }

    @Test
    void shouldThrowExceptionWhenAmountIsZero() {
        // When & Then
        assertThatThrownBy(() -> new Payment(validPaymentId, validInvoiceId, BigDecimal.ZERO, validMethod, validPaidAt, validReferenceNumber))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Payment amount must be positive");
    }

    @Test
    void shouldThrowExceptionWhenAmountIsNegative() {
        // When & Then
        assertThatThrownBy(() -> new Payment(validPaymentId, validInvoiceId, new BigDecimal("-10.00"), validMethod, validPaidAt, validReferenceNumber))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Payment amount must be positive");
    }

    @Test
    void shouldThrowExceptionWhenPaymentMethodIsNull() {
        // When & Then
        assertThatThrownBy(() -> new Payment(validPaymentId, validInvoiceId, validAmount, null, validPaidAt, validReferenceNumber))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Payment method cannot be null");
    }

    @Test
    void shouldThrowExceptionWhenPaidAtIsNull() {
        // When & Then
        assertThatThrownBy(() -> new Payment(validPaymentId, validInvoiceId, validAmount, validMethod, null, validReferenceNumber))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Paid date cannot be null");
    }

    @Test
    void shouldThrowExceptionWhenReferenceNumberIsNull() {
        // When & Then
        assertThatThrownBy(() -> new Payment(validPaymentId, validInvoiceId, validAmount, validMethod, validPaidAt, null))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Reference number cannot be blank");
    }

    @Test
    void shouldThrowExceptionWhenReferenceNumberIsBlank() {
        // When & Then
        assertThatThrownBy(() -> new Payment(validPaymentId, validInvoiceId, validAmount, validMethod, validPaidAt, "   "))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Reference number cannot be blank");
    }

    @Test
    void shouldAcceptAllPaymentMethodEnums() {
        // Given
        PaymentMethod[] allMethods = PaymentMethod.values();

        for (PaymentMethod method : allMethods) {
            // When
            Payment payment = new Payment(validPaymentId, validInvoiceId, validAmount, method, validPaidAt, validReferenceNumber);

            // Then
            assertThat(payment.getMethod()).isEqualTo(method);
        }
    }

    @Test
    void shouldAcceptDifferentValidAmounts() {
        // Given
        BigDecimal smallAmount = new BigDecimal("0.01");
        BigDecimal largeAmount = new BigDecimal("999999.99");

        // When
        Payment smallPayment = new Payment(validPaymentId, validInvoiceId, smallAmount, validMethod, validPaidAt, validReferenceNumber);
        Payment largePayment = new Payment(validPaymentId, validInvoiceId, largeAmount, validMethod, validPaidAt, validReferenceNumber);

        // Then
        assertThat(smallPayment.getAmount()).isEqualTo(smallAmount);
        assertThat(largePayment.getAmount()).isEqualTo(largeAmount);
    }

    @Test
    void shouldHaveCorrectToStringRepresentation() {
        // Given
        Payment payment = new Payment(validPaymentId, validInvoiceId, validAmount, validMethod, validPaidAt, validReferenceNumber);

        // When
        String toString = payment.toString();

        // Then
        assertThat(toString).contains("Payment(");
        assertThat(toString).contains("paymentId=" + validPaymentId.getValue());
        assertThat(toString).contains("invoiceId=" + validInvoiceId.getValue());
        assertThat(toString).contains("amount=" + validAmount);
        assertThat(toString).contains("method=" + validMethod);
        assertThat(toString).contains("referenceNumber=" + validReferenceNumber);
    }

    @Test
    void shouldInheritFromEntity() {
        // Given
        Payment payment = new Payment(validPaymentId, validInvoiceId, validAmount, validMethod, validPaidAt, validReferenceNumber);

        // Then
        assertThat(payment).isInstanceOf(com.application.domain.shared.Entity.class);
        assertThat(payment.getId()).isInstanceOf(PaymentId.class);
        assertThat(payment.getId()).isEqualTo(validPaymentId);
    }

    @Test
    void shouldBeImmutable() {
        // Given
        Payment payment = new Payment(validPaymentId, validInvoiceId, validAmount, validMethod, validPaidAt, validReferenceNumber);

        // Then - Verify all fields are final by checking the class
        assertThat(Payment.class.getDeclaredFields())
                .allMatch(field -> java.lang.reflect.Modifier.isFinal(field.getModifiers()));
    }
}