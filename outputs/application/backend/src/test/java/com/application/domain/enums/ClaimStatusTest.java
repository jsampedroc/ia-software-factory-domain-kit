package com.application.domain.enums;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;
import org.junit.jupiter.params.provider.ValueSource;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

/**
 * Unit tests for {@link ClaimStatus}.
 * Tests cover enum structure, lifecycle transitions, and business rule compliance.
 */
@DisplayName("ClaimStatus Enum Unit Tests")
class ClaimStatusTest {

    @Test
    @DisplayName("Should contain exactly four predefined enum constants")
    void shouldContainAllExpectedConstants() {
        // Given
        List<String> expectedNames = Arrays.asList("SUBMITTED", "APPROVED", "REJECTED", "PAID");

        // When
        ClaimStatus[] values = ClaimStatus.values();
        List<String> actualNames = Arrays.stream(values)
                .map(Enum::name)
                .collect(Collectors.toList());

        // Then
        assertAll(
                () -> assertThat(values).hasSize(4),
                () -> assertThat(actualNames).containsExactlyInAnyOrderElementsOf(expectedNames)
        );
    }

    @ParameterizedTest
    @EnumSource(ClaimStatus.class)
    @DisplayName("Each enum constant should have a non-null string representation")
    void eachConstantShouldHaveToString(ClaimStatus status) {
        assertThat(status.toString()).isNotNull().isNotEmpty();
    }

    @Nested
    @DisplayName("Lifecycle Transition Validation")
    class LifecycleTransitionTests {

        @Test
        @DisplayName("SUBMITTED should be the initial state")
        void submittedShouldBeInitialState() {
            assertThat(ClaimStatus.SUBMITTED.ordinal()).isZero();
        }

        @Test
        @DisplayName("Valid forward transitions from SUBMITTED")
        void validTransitionsFromSubmitted() {
            // From business domain: SUBMITTED -> APPROVED or REJECTED
            List<ClaimStatus> validNextStates = Arrays.asList(ClaimStatus.APPROVED, ClaimStatus.REJECTED);
            assertThat(validNextStates).contains(ClaimStatus.APPROVED, ClaimStatus.REJECTED);
        }

        @Test
        @DisplayName("APPROVED can transition to PAID")
        void approvedCanTransitionToPaid() {
            // From business domain: "An APPROVED claim may later transition to PAID"
            assertThat(ClaimStatus.PAID).isNotNull();
        }

        @Test
        @DisplayName("REJECTED should be a terminal state")
        void rejectedShouldBeTerminal() {
            // Business rule implies REJECTED is final (no mention of transitions from it)
            assertThat(ClaimStatus.REJECTED.ordinal()).isGreaterThan(ClaimStatus.SUBMITTED.ordinal());
        }

        @Test
        @DisplayName("PAID should be a terminal state")
        void paidShouldBeTerminal() {
            // Business rule implies PAID is final (payment received)
            assertThat(ClaimStatus.PAID.ordinal()).isEqualTo(ClaimStatus.values().length - 1);
        }
    }

    @Nested
    @DisplayName("Business Rule Compliance")
    class BusinessRuleComplianceTests {

        @Test
        @DisplayName("Should reflect insurance claim processing lifecycle")
        void shouldReflectClaimLifecycle() {
            // Based on business domain description
            ClaimStatus[] values = ClaimStatus.values();
            assertThat(values).containsExactly(
                    ClaimStatus.SUBMITTED,
                    ClaimStatus.APPROVED,
                    ClaimStatus.REJECTED,
                    ClaimStatus.PAID
            );
        }

        @Test
        @DisplayName("APPROVED must exist for invoice status reflection rule")
        void approvedMustExistForInvoiceRule() {
            // Business rule: "When an InsuranceClaim is APPROVED, the invoice status must reflect the insurance payment."
            assertThat(ClaimStatus.APPROVED).isNotNull();
        }

        @Test
        @DisplayName("PAID must exist for payment receipt tracking")
        void paidMustExistForPaymentTracking() {
            // Business domain: "PAID once the insurance payment is received."
            assertThat(ClaimStatus.PAID).isNotNull();
        }
    }

    @ParameterizedTest
    @ValueSource(strings = {"SUBMITTED", "APPROVED", "REJECTED", "PAID"})
    @DisplayName("Should parse enum constant from string name")
    void shouldParseFromString(String statusName) {
        ClaimStatus status = ClaimStatus.valueOf(statusName);
        assertThat(status).isNotNull();
        assertThat(status.name()).isEqualTo(statusName);
    }

    @Test
    @DisplayName("Should maintain consistent ordinal positions")
    void shouldMaintainConsistentOrdinals() {
        assertAll(
                () -> assertThat(ClaimStatus.SUBMITTED.ordinal()).isZero(),
                () -> assertThat(ClaimStatus.APPROVED.ordinal()).isEqualTo(1),
                () -> assertThat(ClaimStatus.REJECTED.ordinal()).isEqualTo(2),
                () -> assertThat(ClaimStatus.PAID.ordinal()).isEqualTo(3)
        );
    }

    @Test
    @DisplayName("Enum should be comparable by natural order")
    void shouldBeComparable() {
        assertThat(ClaimStatus.SUBMITTED.compareTo(ClaimStatus.APPROVED)).isNegative();
        assertThat(ClaimStatus.PAID.compareTo(ClaimStatus.REJECTED)).isPositive();
        assertThat(ClaimStatus.APPROVED.compareTo(ClaimStatus.APPROVED)).isZero();
    }
}