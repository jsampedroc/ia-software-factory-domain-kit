package com.application.domain.model;

import com.application.domain.valueobject.PaymentPlanId;
import com.application.domain.enums.PaymentPlanStatus;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.assertj.core.api.Assertions.catchThrowable;

@ExtendWith(MockitoExtension.class)
class PaymentPlanTest {

    private final PaymentPlanId validPlanId = new PaymentPlanId(UUID.randomUUID());
    private final UUID validInvoiceId = UUID.randomUUID();
    private final BigDecimal validTotalAmount = new BigDecimal("1500.00");
    private final Integer validInstallmentCount = 3;
    private final BigDecimal validInstallmentAmount = new BigDecimal("500.00");
    private final LocalDate validStartDate = LocalDate.now().plusDays(1);
    private final List<LocalDate> validDueDates = List.of(
            validStartDate.plusMonths(1),
            validStartDate.plusMonths(2),
            validStartDate.plusMonths(3)
    );
    private final PaymentPlanStatus validStatus = PaymentPlanStatus.ACTIVE;

    @Test
    @DisplayName("Should create a valid PaymentPlan using the constructor")
    void shouldCreateValidPaymentPlan() {
        // When
        PaymentPlan paymentPlan = new PaymentPlan(
                validPlanId,
                validInvoiceId,
                validTotalAmount,
                validInstallmentCount,
                validInstallmentAmount,
                validStartDate,
                validDueDates,
                validStatus
        );

        // Then
        assertThat(paymentPlan).isNotNull();
        assertThat(paymentPlan.getId()).isEqualTo(validPlanId);
        assertThat(paymentPlan.getInvoiceId()).isEqualTo(validInvoiceId);
        assertThat(paymentPlan.getTotalAmount()).isEqualTo(validTotalAmount);
        assertThat(paymentPlan.getInstallmentCount()).isEqualTo(validInstallmentCount);
        assertThat(paymentPlan.getInstallmentAmount()).isEqualTo(validInstallmentAmount);
        assertThat(paymentPlan.getStartDate()).isEqualTo(validStartDate);
        assertThat(paymentPlan.getDueDates()).isEqualTo(validDueDates);
        assertThat(paymentPlan.getStatus()).isEqualTo(validStatus);
        assertThat(paymentPlan.isActive()).isTrue();
    }

    @Test
    @DisplayName("Should create a valid PaymentPlan using Lombok's builder")
    void shouldCreateValidPaymentPlanUsingBuilder() {
        // When
        PaymentPlan paymentPlan = PaymentPlan.builder()
                .id(validPlanId)
                .invoiceId(validInvoiceId)
                .totalAmount(validTotalAmount)
                .installmentCount(validInstallmentCount)
                .installmentAmount(validInstallmentAmount)
                .startDate(validStartDate)
                .dueDates(validDueDates)
                .status(validStatus)
                .build();

        // Then
        assertThat(paymentPlan).isNotNull();
        assertThat(paymentPlan.getId()).isEqualTo(validPlanId);
        assertThat(paymentPlan.getInvoiceId()).isEqualTo(validInvoiceId);
        assertThat(paymentPlan.getTotalAmount()).isEqualTo(validTotalAmount);
        assertThat(paymentPlan.getInstallmentCount()).isEqualTo(validInstallmentCount);
        assertThat(paymentPlan.getInstallmentAmount()).isEqualTo(validInstallmentAmount);
        assertThat(paymentPlan.getStartDate()).isEqualTo(validStartDate);
        assertThat(paymentPlan.getDueDates()).isEqualTo(validDueDates);
        assertThat(paymentPlan.getStatus()).isEqualTo(validStatus);
    }

    @Nested
    @DisplayName("Validation Tests")
    class ValidationTests {

        @Test
        @DisplayName("Should throw exception when invoiceId is null")
        void shouldThrowExceptionWhenInvoiceIdIsNull() {
            // When & Then
            assertThatThrownBy(() -> new PaymentPlan(
                    validPlanId,
                    null,
                    validTotalAmount,
                    validInstallmentCount,
                    validInstallmentAmount,
                    validStartDate,
                    validDueDates,
                    validStatus
            )).isInstanceOf(IllegalArgumentException.class)
                    .hasMessage("Invoice ID cannot be null");
        }

        @Test
        @DisplayName("Should throw exception when totalAmount is null")
        void shouldThrowExceptionWhenTotalAmountIsNull() {
            // When & Then
            assertThatThrownBy(() -> new PaymentPlan(
                    validPlanId,
                    validInvoiceId,
                    null,
                    validInstallmentCount,
                    validInstallmentAmount,
                    validStartDate,
                    validDueDates,
                    validStatus
            )).isInstanceOf(IllegalArgumentException.class)
                    .hasMessage("Total amount must be positive");
        }

        @Test
        @DisplayName("Should throw exception when totalAmount is zero or negative")
        void shouldThrowExceptionWhenTotalAmountIsZeroOrNegative() {
            // When & Then
            assertThatThrownBy(() -> new PaymentPlan(
                    validPlanId,
                    validInvoiceId,
                    BigDecimal.ZERO,
                    validInstallmentCount,
                    validInstallmentAmount,
                    validStartDate,
                    validDueDates,
                    validStatus
            )).isInstanceOf(IllegalArgumentException.class)
                    .hasMessage("Total amount must be positive");

            assertThatThrownBy(() -> new PaymentPlan(
                    validPlanId,
                    validInvoiceId,
                    new BigDecimal("-100.00"),
                    validInstallmentCount,
                    validInstallmentAmount,
                    validStartDate,
                    validDueDates,
                    validStatus
            )).isInstanceOf(IllegalArgumentException.class)
                    .hasMessage("Total amount must be positive");
        }

        @Test
        @DisplayName("Should throw exception when installmentCount is null")
        void shouldThrowExceptionWhenInstallmentCountIsNull() {
            // When & Then
            assertThatThrownBy(() -> new PaymentPlan(
                    validPlanId,
                    validInvoiceId,
                    validTotalAmount,
                    null,
                    validInstallmentAmount,
                    validStartDate,
                    validDueDates,
                    validStatus
            )).isInstanceOf(IllegalArgumentException.class)
                    .hasMessage("Installment count must be positive");
        }

        @Test
        @DisplayName("Should throw exception when installmentCount is zero or negative")
        void shouldThrowExceptionWhenInstallmentCountIsZeroOrNegative() {
            // When & Then
            assertThatThrownBy(() -> new PaymentPlan(
                    validPlanId,
                    validInvoiceId,
                    validTotalAmount,
                    0,
                    validInstallmentAmount,
                    validStartDate,
                    validDueDates,
                    validStatus
            )).isInstanceOf(IllegalArgumentException.class)
                    .hasMessage("Installment count must be positive");

            assertThatThrownBy(() -> new PaymentPlan(
                    validPlanId,
                    validInvoiceId,
                    validTotalAmount,
                    -1,
                    validInstallmentAmount,
                    validStartDate,
                    validDueDates,
                    validStatus
            )).isInstanceOf(IllegalArgumentException.class)
                    .hasMessage("Installment count must be positive");
        }

        @Test
        @DisplayName("Should throw exception when installmentAmount is null")
        void shouldThrowExceptionWhenInstallmentAmountIsNull() {
            // When & Then
            assertThatThrownBy(() -> new PaymentPlan(
                    validPlanId,
                    validInvoiceId,
                    validTotalAmount,
                    validInstallmentCount,
                    null,
                    validStartDate,
                    validDueDates,
                    validStatus
            )).isInstanceOf(IllegalArgumentException.class)
                    .hasMessage("Installment amount must be positive");
        }

        @Test
        @DisplayName("Should throw exception when installmentAmount is zero or negative")
        void shouldThrowExceptionWhenInstallmentAmountIsZeroOrNegative() {
            // When & Then
            assertThatThrownBy(() -> new PaymentPlan(
                    validPlanId,
                    validInvoiceId,
                    validTotalAmount,
                    validInstallmentCount,
                    BigDecimal.ZERO,
                    validStartDate,
                    validDueDates,
                    validStatus
            )).isInstanceOf(IllegalArgumentException.class)
                    .hasMessage("Installment amount must be positive");

            assertThatThrownBy(() -> new PaymentPlan(
                    validPlanId,
                    validInvoiceId,
                    validTotalAmount,
                    validInstallmentCount,
                    new BigDecimal("-50.00"),
                    validStartDate,
                    validDueDates,
                    validStatus
            )).isInstanceOf(IllegalArgumentException.class)
                    .hasMessage("Installment amount must be positive");
        }

        @Test
        @DisplayName("Should throw exception when startDate is null")
        void shouldThrowExceptionWhenStartDateIsNull() {
            // When & Then
            assertThatThrownBy(() -> new PaymentPlan(
                    validPlanId,
                    validInvoiceId,
                    validTotalAmount,
                    validInstallmentCount,
                    validInstallmentAmount,
                    null,
                    validDueDates,
                    validStatus
            )).isInstanceOf(IllegalArgumentException.class)
                    .hasMessage("Start date cannot be null");
        }

        @Test
        @DisplayName("Should throw exception when dueDates is null")
        void shouldThrowExceptionWhenDueDatesIsNull() {
            // When & Then
            assertThatThrownBy(() -> new PaymentPlan(
                    validPlanId,
                    validInvoiceId,
                    validTotalAmount,
                    validInstallmentCount,
                    validInstallmentAmount,
                    validStartDate,
                    null,
                    validStatus
            )).isInstanceOf(IllegalArgumentException.class)
                    .hasMessage("Due dates cannot be null or empty");
        }

        @Test
        @DisplayName("Should throw exception when dueDates is empty")
        void shouldThrowExceptionWhenDueDatesIsEmpty() {
            // When & Then
            assertThatThrownBy(() -> new PaymentPlan(
                    validPlanId,
                    validInvoiceId,
                    validTotalAmount,
                    validInstallmentCount,
                    validInstallmentAmount,
                    validStartDate,
                    List.of(),
                    validStatus
            )).isInstanceOf(IllegalArgumentException.class)
                    .hasMessage("Due dates cannot be null or empty");
        }

        @Test
        @DisplayName("Should throw exception when dueDates size does not match installmentCount")
        void shouldThrowExceptionWhenDueDatesSizeDoesNotMatchInstallmentCount() {
            // Given
            List<LocalDate> wrongDueDates = List.of(validStartDate.plusMonths(1));

            // When & Then
            assertThatThrownBy(() -> new PaymentPlan(
                    validPlanId,
                    validInvoiceId,
                    validTotalAmount,
                    validInstallmentCount,
                    validInstallmentAmount,
                    validStartDate,
                    wrongDueDates,
                    validStatus
            )).isInstanceOf(IllegalArgumentException.class)
                    .hasMessage("Number of due dates must match installment count");
        }

        @Test
        @DisplayName("Should throw exception when status is null")
        void shouldThrowExceptionWhenStatusIsNull() {
            // When & Then
            assertThatThrownBy(() -> new PaymentPlan(
                    validPlanId,
                    validInvoiceId,
                    validTotalAmount,
                    validInstallmentCount,
                    validInstallmentAmount,
                    validStartDate,
                    validDueDates,
                    null
            )).isInstanceOf(IllegalArgumentException.class)
                    .hasMessage("Status cannot be null");
        }

        @Test
        @DisplayName("Should throw exception when total amount does not equal installment amount * installment count")
        void shouldThrowExceptionWhenTotalAmountDoesNotMatchCalculation() {
            // Given
            BigDecimal wrongTotalAmount = new BigDecimal("1499.99");

            // When & Then
            assertThatThrownBy(() -> new PaymentPlan(
                    validPlanId,
                    validInvoiceId,
                    wrongTotalAmount,
                    validInstallmentCount,
                    validInstallmentAmount,
                    validStartDate,
                    validDueDates,
                    validStatus
            )).isInstanceOf(IllegalArgumentException.class)
                    .hasMessage("Total amount must equal installment amount * installment count");
        }

        @Test
        @DisplayName("Should throw exception when due date is before start date")
        void shouldThrowExceptionWhenDueDateIsBeforeStartDate() {
            // Given
            List<LocalDate> invalidDueDates = List.of(
                    validStartDate.minusDays(1),
                    validStartDate.plusMonths(1),
                    validStartDate.plusMonths(2)
            );

            // When & Then
            assertThatThrownBy(() -> new PaymentPlan(
                    validPlanId,
                    validInvoiceId,
                    validTotalAmount,
                    validInstallmentCount,
                    validInstallmentAmount,
                    validStartDate,
                    invalidDueDates,
                    validStatus
            )).isInstanceOf(IllegalArgumentException.class)
                    .hasMessage("All due dates must be on or after the start date");
        }
    }

    @Nested
    @DisplayName("Business Logic Tests")
    class BusinessLogicTests {

        @Test
        @DisplayName("Should mark active payment plan as completed")
        void shouldMarkActivePaymentPlanAsCompleted() {
            // Given
            PaymentPlan paymentPlan = new PaymentPlan(
                    validPlanId,
                    validInvoiceId,
                    validTotalAmount,
                    validInstallmentCount,
                    validInstallmentAmount,
                    validStartDate,
                    validDueDates,
                    PaymentPlanStatus.ACTIVE
            );

            // When
            paymentPlan.markAsCompleted();

            // Then
            assertThat(paymentPlan.getStatus()).isEqualTo(PaymentPlanStatus.COMPLETED);
            assertThat(paymentPlan.isActive()).isFalse();
        }

        @Test
        @DisplayName("Should throw exception when marking non-active payment plan as completed")
        void shouldThrowExceptionWhenMarkingNonActivePaymentPlanAsCompleted() {
            // Given
            PaymentPlan completedPlan = new PaymentPlan(
                    validPlanId,
                    validInvoiceId,
                    validTotalAmount,
                    validInstallmentCount,
                    validInstallmentAmount,
                    validStartDate,
                    validDueDates,
                    PaymentPlanStatus.COMPLETED
            );

            PaymentPlan defaultedPlan = new PaymentPlan(
                    new PaymentPlanId(UUID.randomUUID()),
                    validInvoiceId,
                    validTotalAmount,
                    validInstallmentCount,
                    validInstallmentAmount,
                    validStartDate,
                    validDueDates,
                    PaymentPlanStatus.DEFAULTED
            );

            // When & Then for COMPLETED
            assertThatThrownBy(completedPlan::markAsCompleted)
                    .isInstanceOf(IllegalStateException.class)
                    .hasMessage("Only active payment plans can be marked as completed");

            // When & Then for DEFAULTED
            assertThatThrownBy(defaultedPlan::markAsCompleted)
                    .isInstanceOf(IllegalStateException.class)
                    .hasMessage("Only active payment plans can be marked as completed");
        }

        @Test
        @DisplayName("Should mark active payment plan as defaulted")
        void shouldMarkActivePaymentPlanAsDefaulted() {
            // Given
            PaymentPlan paymentPlan = new PaymentPlan(
                    validPlanId,
                    validInvoiceId,
                    validTotalAmount,
                    validInstallmentCount,
                    validInstallmentAmount,
                    validStartDate,
                    validDueDates,
                    PaymentPlanStatus.ACTIVE
            );

            // When
            paymentPlan.markAsDefaulted();

            // Then
            assertThat(paymentPlan.getStatus()).isEqualTo(PaymentPlanStatus.DEFAULTED);
            assertThat(paymentPlan.isActive()).isFalse();
        }

        @Test
        @DisplayName("Should throw exception when marking non-active payment plan as defaulted")
        void shouldThrowExceptionWhenMarkingNonActivePaymentPlanAsDefaulted() {
            // Given
            PaymentPlan completedPlan = new PaymentPlan(
                    validPlanId,
                    validInvoiceId,
                    validTotalAmount,
                    validInstallmentCount,
                    validInstallmentAmount,
                    validStartDate,
                    validDueDates,
                    PaymentPlanStatus.COMPLETED
            );

            // When & Then
            assertThatThrownBy(completedPlan::markAsDefaulted)
                    .isInstanceOf(IllegalStateException.class)
                    .hasMessage("Only active payment plans can be marked as defaulted");
        }

        @Test
        @DisplayName("Should return correct next due date")
        void shouldReturnCorrectNextDueDate() {
            // Given
            LocalDate today = LocalDate.now();
            LocalDate pastDue = today.minusDays(10);
            LocalDate nextDue = today.plusDays(5);
            LocalDate futureDue = today.plusDays(20);

            List<LocalDate> dueDates = List.of(pastDue, nextDue, futureDue);
            PaymentPlan paymentPlan = new PaymentPlan(
                    validPlanId,
                    validInvoiceId,
                    validTotalAmount,
                    3,
                    validInstallmentAmount,
                    pastDue.minusMonths(1),
                    dueDates,
                    PaymentPlanStatus.ACTIVE
            );

            // When
            LocalDate result = paymentPlan.getNextDueDate();

            // Then
            assertThat(result).isEqualTo(nextDue);
        }

        @Test
        @DisplayName("Should return null when there is no next due date")
        void shouldReturnNullWhenNoNextDueDate() {
            // Given
            LocalDate today = LocalDate.now();
            LocalDate pastDue1 = today.minusDays(20);
            LocalDate pastDue2 = today.minusDays(10);
            LocalDate pastDue3 = today.minusDays(5);

            List<LocalDate> dueDates = List.of(pastDue1, pastDue2, pastDue3);
            PaymentPlan paymentPlan = new PaymentPlan(
                    validPlanId,
                    validInvoiceId,
                    validTotalAmount,
                    3,
                    validInstallmentAmount,
                    pastDue1.minusMonths(1),
                    dueDates,
                    PaymentPlanStatus.ACTIVE
            );

            // When
            LocalDate result = paymentPlan.getNextDueDate();

            // Then
            assertThat(result).isNull();
        }

        @Test
        @DisplayName("Should return true when payment plan is overdue")
        void shouldReturnTrueWhenPaymentPlanIsOverdue() {
            // Given
            LocalDate today = LocalDate.now();
            LocalDate pastDue = today.minusDays(1);
            LocalDate futureDue = today.plusDays(30);

            List<LocalDate> dueDates =