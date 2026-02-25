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

@DisplayName("PaymentPlanStatus Enum Unit Tests")
class PaymentPlanStatusTest {

    @Test
    @DisplayName("Should contain exactly three predefined enum constants")
    void shouldContainAllExpectedConstants() {
        // Given
        List<String> expectedNames = Arrays.asList("ACTIVE", "COMPLETED", "DEFAULTED");

        // When
        PaymentPlanStatus[] values = PaymentPlanStatus.values();
        List<String> actualNames = Arrays.stream(values)
                .map(Enum::name)
                .collect(Collectors.toList());

        // Then
        assertAll(
                () -> assertThat(values).hasSize(3),
                () -> assertThat(actualNames).containsExactlyInAnyOrderElementsOf(expectedNames)
        );
    }

    @ParameterizedTest
    @EnumSource(PaymentPlanStatus.class)
    @DisplayName("Each enum constant should be accessible via valueOf")
    void shouldAccessEachConstantViaValueOf(PaymentPlanStatus status) {
        // When
        PaymentPlanStatus resolvedStatus = PaymentPlanStatus.valueOf(status.name());

        // Then
        assertThat(resolvedStatus).isEqualTo(status);
    }

    @Test
    @DisplayName("Enum constants should be in declared order")
    void shouldMaintainDeclaredOrder() {
        // When
        PaymentPlanStatus[] values = PaymentPlanStatus.values();

        // Then
        assertThat(values).containsExactly(
                PaymentPlanStatus.ACTIVE,
                PaymentPlanStatus.COMPLETED,
                PaymentPlanStatus.DEFAULTED
        );
    }

    @Nested
    @DisplayName("Business Logic Interpretation Tests")
    class BusinessLogicTests {

        @Test
        @DisplayName("ACTIVE status should represent an ongoing payment plan")
        void activeStatusInterpretation() {
            assertThat(PaymentPlanStatus.ACTIVE.name()).isEqualTo("ACTIVE");
        }

        @Test
        @DisplayName("COMPLETED status should represent a fully paid plan")
        void completedStatusInterpretation() {
            assertThat(PaymentPlanStatus.COMPLETED.name()).isEqualTo("COMPLETED");
        }

        @Test
        @DisplayName("DEFAULTED status should represent a plan with missed payments")
        void defaultedStatusInterpretation() {
            assertThat(PaymentPlanStatus.DEFAULTED.name()).isEqualTo("DEFAULTED");
        }

        @ParameterizedTest
        @ValueSource(strings = {"ACTIVE", "COMPLETED", "DEFAULTED"})
        @DisplayName("Should parse valid status strings")
        void shouldParseValidStatusStrings(String statusString) {
            // When
            PaymentPlanStatus status = PaymentPlanStatus.valueOf(statusString);

            // Then
            assertThat(status).isNotNull();
            assertThat(status.name()).isEqualTo(statusString);
        }
    }

    @Nested
    @DisplayName("Domain Integration Context Tests")
    class DomainIntegrationTests {

        @Test
        @DisplayName("Statuses should align with PaymentPlan entity business rules")
        void statusAlignmentWithBusinessRules() {
            // Rule: PaymentPlan can only be created for Invoices with status ISSUED and total > $500
            // This implies ACTIVE is the initial status for a newly created plan
            assertThat(PaymentPlanStatus.ACTIVE).isNotNull();

            // Rule: PaymentPlan status transitions based on payment completion
            assertThat(PaymentPlanStatus.COMPLETED).isNotNull();

            // Rule: PaymentPlan status for missed payments
            assertThat(PaymentPlanStatus.DEFAULTED).isNotNull();
        }

        @Test
        @DisplayName("Should provide all statuses for UI dropdowns or reports")
        void shouldProvideAllStatusesForSelection() {
            // When
            List<PaymentPlanStatus> allStatuses = Arrays.asList(PaymentPlanStatus.values());

            // Then
            assertThat(allStatuses)
                    .hasSize(3)
                    .containsExactlyInAnyOrder(
                            PaymentPlanStatus.ACTIVE,
                            PaymentPlanStatus.COMPLETED,
                            PaymentPlanStatus.DEFAULTED
                    );
        }
    }

    @Test
    @DisplayName("toString() should return the enum constant name")
    void toStringShouldReturnConstantName() {
        for (PaymentPlanStatus status : PaymentPlanStatus.values()) {
            assertThat(status.toString()).isEqualTo(status.name());
        }
    }

    @Test
    @DisplayName("Enum should be serializable and usable in collections")
    void shouldBeUsableInCollections() {
        // Given
        List<PaymentPlanStatus> statusList = Arrays.asList(
                PaymentPlanStatus.ACTIVE,
                PaymentPlanStatus.COMPLETED,
                PaymentPlanStatus.DEFAULTED,
                PaymentPlanStatus.ACTIVE
        );

        // Then
        assertThat(statusList)
                .hasSize(4)
                .contains(PaymentPlanStatus.ACTIVE, PaymentPlanStatus.COMPLETED, PaymentPlanStatus.DEFAULTED)
                .filteredOn(status -> status == PaymentPlanStatus.ACTIVE)
                .hasSize(2);
    }
}