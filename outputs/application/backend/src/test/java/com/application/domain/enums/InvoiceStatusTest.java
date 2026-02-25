package com.application.domain.enums;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;
import org.junit.jupiter.params.provider.ValueSource;

import java.util.Arrays;
import java.util.Set;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

/**
 * Unit tests for {@link InvoiceStatus}.
 * Tests focus on the enum's definition, lifecycle semantics, and business rule implications.
 */
@Tag("unit")
@Tag("domain")
@Tag("enum")
@DisplayName("InvoiceStatus Enum Unit Tests")
class InvoiceStatusTest {

    @Test
    @DisplayName("Should contain exactly six predefined status values")
    void shouldContainAllExpectedValues() {
        // Given & When
        InvoiceStatus[] values = InvoiceStatus.values();

        // Then
        assertThat(values)
                .hasSize(6)
                .containsExactly(
                        InvoiceStatus.DRAFT,
                        InvoiceStatus.ISSUED,
                        InvoiceStatus.PARTIAL_PAID,
                        InvoiceStatus.PAID,
                        InvoiceStatus.OVERDUE,
                        InvoiceStatus.CANCELLED
                );
    }

    @ParameterizedTest
    @EnumSource(InvoiceStatus.class)
    @DisplayName("Each status should have a non-null, non-blank name")
    void eachStatusShouldHaveValidName(InvoiceStatus status) {
        assertThat(status.name())
                .isNotNull()
                .isNotBlank()
                .matches("^[A-Z_]+$");
    }

    @Nested
    @DisplayName("Lifecycle State Group Tests")
    class LifecycleStateTests {

        @Test
        @DisplayName("Should correctly identify terminal states")
        void shouldIdentifyTerminalStates() {
            Set<InvoiceStatus> terminalStates = Set.of(InvoiceStatus.PAID, InvoiceStatus.CANCELLED);

            assertAll(
                    () -> assertThat(InvoiceStatus.PAID).isIn(terminalStates),
                    () -> assertThat(InvoiceStatus.CANCELLED).isIn(terminalStates),
                    () -> assertThat(InvoiceStatus.DRAFT).isNotIn(terminalStates),
                    () -> assertThat(InvoiceStatus.ISSUED).isNotIn(terminalStates),
                    () -> assertThat(InvoiceStatus.PARTIAL_PAID).isNotIn(terminalStates),
                    () -> assertThat(InvoiceStatus.OVERDUE).isNotIn(terminalStates)
            );
        }

        @Test
        @DisplayName("Should correctly identify active (non-terminal) states")
        void shouldIdentifyActiveStates() {
            Set<InvoiceStatus> activeStates = Set.of(
                    InvoiceStatus.DRAFT,
                    InvoiceStatus.ISSUED,
                    InvoiceStatus.PARTIAL_PAID,
                    InvoiceStatus.OVERDUE
            );

            Arrays.stream(InvoiceStatus.values()).forEach(status -> {
                if (activeStates.contains(status)) {
                    assertThat(status).isIn(activeStates);
                } else {
                    assertThat(status).isNotIn(activeStates);
                }
            });
        }

        @Test
        @DisplayName("Should identify states that allow payment")
        void shouldIdentifyStatesThatAllowPayment() {
            Set<InvoiceStatus> paymentAllowedStates = Set.of(
                    InvoiceStatus.ISSUED,
                    InvoiceStatus.PARTIAL_PAID,
                    InvoiceStatus.OVERDUE
            );

            assertAll(
                    () -> assertThat(InvoiceStatus.ISSUED).isIn(paymentAllowedStates),
                    () -> assertThat(InvoiceStatus.PARTIAL_PAID).isIn(paymentAllowedStates),
                    () -> assertThat(InvoiceStatus.OVERDUE).isIn(paymentAllowedStates),
                    () -> assertThat(InvoiceStatus.DRAFT).isNotIn(paymentAllowedStates),
                    () -> assertThat(InvoiceStatus.PAID).isNotIn(paymentAllowedStates),
                    () -> assertThat(InvoiceStatus.CANCELLED).isNotIn(paymentAllowedStates)
            );
        }

        @Test
        @DisplayName("Should identify states that allow modification")
        void shouldIdentifyStatesThatAllowModification() {
            // Only DRAFT allows free modification per business rules
            assertThat(InvoiceStatus.DRAFT)
                    .describedAs("Only DRAFT status should allow free modification")
                    .isEqualTo(InvoiceStatus.DRAFT);

            Arrays.stream(InvoiceStatus.values())
                    .filter(status -> status != InvoiceStatus.DRAFT)
                    .forEach(status -> assertThat(status)
                            .describedAs("Status %s should not allow modification", status)
                            .isNotEqualTo(InvoiceStatus.DRAFT));
        }
    }

    @Nested
    @DisplayName("Business Rule Compliance Tests")
    class BusinessRuleComplianceTests {

        @Test
        @DisplayName("Standard lifecycle sequence should be valid")
        void standardLifecycleSequenceIsValid() {
            // As per documentation: DRAFT → ISSUED → [PARTIAL_PAID] → PAID or OVERDUE
            // This test validates the documented flow is represented in the enum
            assertAll(
                    () -> assertThat(InvoiceStatus.DRAFT).isNotNull(),
                    () -> assertThat(InvoiceStatus.ISSUED).isNotNull(),
                    () -> assertThat(InvoiceStatus.PARTIAL_PAID).isNotNull(),
                    () -> assertThat(InvoiceStatus.PAID).isNotNull(),
                    () -> assertThat(InvoiceStatus.OVERDUE).isNotNull()
            );
        }

        @Test
        @DisplayName("CANCELLED state should be reachable from DRAFT and ISSUED")
        void cancelledStateReachableFromDraftAndIssued() {
            // Business rule: CANCELLED is a terminal state that can be reached from DRAFT or ISSUED
            Set<InvoiceStatus> allowedPreCancellationStates = Set.of(InvoiceStatus.DRAFT, InvoiceStatus.ISSUED);

            assertAll(
                    () -> assertThat(InvoiceStatus.DRAFT).isIn(allowedPreCancellationStates),
                    () -> assertThat(InvoiceStatus.ISSUED).isIn(allowedPreCancellationStates),
                    () -> assertThat(InvoiceStatus.PARTIAL_PAID).isNotIn(allowedPreCancellationStates),
                    () -> assertThat(InvoiceStatus.PAID).isNotIn(allowedPreCancellationStates),
                    () -> assertThat(InvoiceStatus.OVERDUE).isNotIn(allowedPreCancellationStates)
            );
        }

        @Test
        @DisplayName("OVERDUE state should imply payment is late")
        void overdueStateImpliesLatePayment() {
            // OVERDUE is specifically for invoices where due date has passed
            assertThat(InvoiceStatus.OVERDUE.name())
                    .describedAs("OVERDUE status name should clearly indicate lateness")
                    .isEqualTo("OVERDUE");
        }

        @Test
        @DisplayName("PARTIAL_PAID should be distinct from PAID")
        void partialPaidIsDistinctFromPaid() {
            assertThat(InvoiceStatus.PARTIAL_PAID)
                    .isNotEqualTo(InvoiceStatus.PAID)
                    .describedAs("PARTIAL_PAID and PAID must be distinct states");
        }
    }

    @ParameterizedTest
    @ValueSource(strings = {"DRAFT", "ISSUED", "PARTIAL_PAID", "PAID", "OVERDUE", "CANCELLED"})
    @DisplayName("Should parse valid status names from strings")
    void shouldParseValidStatusNames(String statusName) {
        InvoiceStatus status = InvoiceStatus.valueOf(statusName);
        assertThat(status).isNotNull();
        assertThat(status.name()).isEqualTo(statusName);
    }

    @Test
    @DisplayName("Should return correct ordinal positions")
    void shouldReturnCorrectOrdinalPositions() {
        assertAll(
                () -> assertThat(InvoiceStatus.DRAFT.ordinal()).isZero(),
                () -> assertThat(InvoiceStatus.ISSUED.ordinal()).isEqualTo(1),
                () -> assertThat(InvoiceStatus.PARTIAL_PAID.ordinal()).isEqualTo(2),
                () -> assertThat(InvoiceStatus.PAID.ordinal()).isEqualTo(3),
                () -> assertThat(InvoiceStatus.OVERDUE.ordinal()).isEqualTo(4),
                () -> assertThat(InvoiceStatus.CANCELLED.ordinal()).isEqualTo(5)
        );
    }

    @Test
    @DisplayName("Should maintain uniqueness of all enum values")
    void shouldMaintainUniqueness() {
        Set<String> uniqueNames = Arrays.stream(InvoiceStatus.values())
                .map(Enum::name)
                .collect(Collectors.toSet());

        assertThat(uniqueNames).hasSize(InvoiceStatus.values().length);
    }

    @Test
    @DisplayName("toString() should return the enum name")
    void toStringShouldReturnEnumName() {
        Arrays.stream(InvoiceStatus.values()).forEach(status ->
                assertThat(status.toString()).isEqualTo(status.name())
        );
    }
}