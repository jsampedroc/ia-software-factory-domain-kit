package com.application.domain.enums;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;
import org.junit.jupiter.params.provider.ValueSource;

import java.util.Arrays;
import java.util.Set;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@DisplayName("PaymentMethod Enum Unit Tests")
class PaymentMethodTest {

    @Test
    @DisplayName("Should contain exactly four predefined payment methods")
    void shouldContainAllPredefinedMethods() {
        Set<PaymentMethod> actualMethods = Arrays.stream(PaymentMethod.values())
                .collect(Collectors.toSet());

        assertThat(actualMethods)
                .hasSize(4)
                .containsExactlyInAnyOrder(
                        PaymentMethod.CASH,
                        PaymentMethod.CARD,
                        PaymentMethod.BANK_TRANSFER,
                        PaymentMethod.INSURANCE
                );
    }

    @Nested
    @DisplayName("ValueOf Validation Tests")
    class ValueOfTests {

        @ParameterizedTest(name = "Should parse valid payment method: {0}")
        @EnumSource(PaymentMethod.class)
        void shouldParseValidPaymentMethod(PaymentMethod method) {
            PaymentMethod parsed = PaymentMethod.valueOf(method.name());
            assertThat(parsed).isEqualTo(method);
        }

        @ParameterizedTest(name = "Should throw IllegalArgumentException for invalid input: {0}")
        @ValueSource(strings = {"", "CREDIT", "PAYPAL", "CHECK", "null"})
        void shouldThrowExceptionForInvalidValue(String invalidInput) {
            assertThatThrownBy(() -> PaymentMethod.valueOf(invalidInput))
                    .isInstanceOf(IllegalArgumentException.class);
        }
    }

    @Nested
    @DisplayName("Business Logic Integration Tests")
    class BusinessLogicTests {

        @Test
        @DisplayName("Should identify insurance-related payment method")
        void shouldIdentifyInsuranceMethod() {
            assertThat(PaymentMethod.INSURANCE)
                    .as("Payment method should be INSURANCE")
                    .isEqualTo(PaymentMethod.INSURANCE);

            assertThat(PaymentMethod.INSURANCE.name())
                    .isEqualTo("INSURANCE");
        }

        @Test
        @DisplayName("Should identify direct patient payment methods")
        void shouldIdentifyDirectPaymentMethods() {
            Set<PaymentMethod> directMethods = Set.of(
                    PaymentMethod.CASH,
                    PaymentMethod.CARD,
                    PaymentMethod.BANK_TRANSFER
            );

            assertThat(directMethods)
                    .hasSize(3)
                    .allSatisfy(method ->
                            assertThat(method)
                                    .as("Method %s should be a direct payment method", method)
                                    .isIn(PaymentMethod.CASH, PaymentMethod.CARD, PaymentMethod.BANK_TRANSFER)
                    );

            assertThat(directMethods)
                    .doesNotContain(PaymentMethod.INSURANCE);
        }
    }

    @Nested
    @DisplayName("Enum Properties Tests")
    class EnumPropertiesTests {

        @Test
        @DisplayName("Should maintain ordinal positions consistently")
        void shouldHaveConsistentOrdinalPositions() {
            assertThat(PaymentMethod.CASH.ordinal()).isZero();
            assertThat(PaymentMethod.CARD.ordinal()).isEqualTo(1);
            assertThat(PaymentMethod.BANK_TRANSFER.ordinal()).isEqualTo(2);
            assertThat(PaymentMethod.INSURANCE.ordinal()).isEqualTo(3);
        }

        @Test
        @DisplayName("Should return correct string representation")
        void shouldReturnCorrectToString() {
            assertThat(PaymentMethod.CASH.toString()).isEqualTo("CASH");
            assertThat(PaymentMethod.CARD.toString()).isEqualTo("CARD");
            assertThat(PaymentMethod.BANK_TRANSFER.toString()).isEqualTo("BANK_TRANSFER");
            assertThat(PaymentMethod.INSURANCE.toString()).isEqualTo("INSURANCE");
        }
    }

    @Test
    @DisplayName("Should be usable in switch statements without default issues")
    void shouldBeUsableInSwitchStatements() {
        PaymentMethod method = PaymentMethod.CARD;
        String result = switch (method) {
            case CASH -> "Cash Payment";
            case CARD -> "Card Payment";
            case BANK_TRANSFER -> "Bank Transfer";
            case INSURANCE -> "Insurance Claim";
        };

        assertThat(result).isEqualTo("Card Payment");
    }

    @Test
    @DisplayName("Values() should return array in declaration order")
    void valuesShouldReturnInDeclarationOrder() {
        PaymentMethod[] values = PaymentMethod.values();

        assertThat(values)
                .hasSize(4)
                .containsExactly(
                        PaymentMethod.CASH,
                        PaymentMethod.CARD,
                        PaymentMethod.BANK_TRANSFER,
                        PaymentMethod.INSURANCE
                );
    }
}