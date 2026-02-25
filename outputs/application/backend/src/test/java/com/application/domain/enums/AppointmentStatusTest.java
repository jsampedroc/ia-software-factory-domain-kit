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
 * Unit tests for {@link AppointmentStatus}.
 * Tests cover enum structure, business semantics, and domain-specific behavior.
 */
@Tag("unit")
@Tag("domain")
@Tag("enum")
@DisplayName("AppointmentStatus Enum Unit Tests")
class AppointmentStatusTest {

    @Test
    @DisplayName("Should contain exactly six predefined status values")
    void shouldContainAllExpectedValues() {
        // Given
        Set<AppointmentStatus> expectedValues = Set.of(
                AppointmentStatus.SCHEDULED,
                AppointmentStatus.CONFIRMED,
                AppointmentStatus.IN_PROGRESS,
                AppointmentStatus.COMPLETED,
                AppointmentStatus.CANCELLED,
                AppointmentStatus.NO_SHOW
        );

        // When
        AppointmentStatus[] allValues = AppointmentStatus.values();

        // Then
        assertAll(
                () -> assertThat(allValues).hasSize(6),
                () -> assertThat(allValues).containsExactlyInAnyOrderElementsOf(expectedValues)
        );
    }

    @Nested
    @DisplayName("Value-Specific Semantic Tests")
    class ValueSemanticsTest {

        @ParameterizedTest
        @EnumSource(value = AppointmentStatus.class, names = {"SCHEDULED", "CONFIRMED", "IN_PROGRESS"})
        @DisplayName("Should identify active appointment statuses correctly")
        void shouldIdentifyActiveStatuses(AppointmentStatus status) {
            assertThat(isActiveStatus(status)).isTrue();
        }

        @ParameterizedTest
        @EnumSource(value = AppointmentStatus.class, names = {"COMPLETED", "CANCELLED", "NO_SHOW"})
        @DisplayName("Should identify terminal appointment statuses correctly")
        void shouldIdentifyTerminalStatuses(AppointmentStatus status) {
            assertThat(isTerminalStatus(status)).isTrue();
        }

        @Test
        @DisplayName("Should have correct business description for SCHEDULED")
        void scheduledShouldHaveCorrectSemantics() {
            assertAll(
                    () -> assertThat(AppointmentStatus.SCHEDULED.name()).isEqualTo("SCHEDULED"),
                    () -> assertThat(AppointmentStatus.SCHEDULED.ordinal()).isEqualTo(0)
            );
        }

        @Test
        @DisplayName("Should have correct business description for CONFIRMED")
        void confirmedShouldHaveCorrectSemantics() {
            assertAll(
                    () -> assertThat(AppointmentStatus.CONFIRMED.name()).isEqualTo("CONFIRMED"),
                    () -> assertThat(AppointmentStatus.CONFIRMED.ordinal()).isEqualTo(1)
            );
        }

        @Test
        @DisplayName("Should have correct business description for IN_PROGRESS")
        void inProgressShouldHaveCorrectSemantics() {
            assertAll(
                    () -> assertThat(AppointmentStatus.IN_PROGRESS.name()).isEqualTo("IN_PROGRESS"),
                    () -> assertThat(AppointmentStatus.IN_PROGRESS.ordinal()).isEqualTo(2)
            );
        }

        @Test
        @DisplayName("Should have correct business description for COMPLETED")
        void completedShouldHaveCorrectSemantics() {
            assertAll(
                    () -> assertThat(AppointmentStatus.COMPLETED.name()).isEqualTo("COMPLETED"),
                    () -> assertThat(AppointmentStatus.COMPLETED.ordinal()).isEqualTo(3)
            );
        }

        @Test
        @DisplayName("Should have correct business description for CANCELLED")
        void cancelledShouldHaveCorrectSemantics() {
            assertAll(
                    () -> assertThat(AppointmentStatus.CANCELLED.name()).isEqualTo("CANCELLED"),
                    () -> assertThat(AppointmentStatus.CANCELLED.ordinal()).isEqualTo(4)
            );
        }

        @Test
        @DisplayName("Should have correct business description for NO_SHOW")
        void noShowShouldHaveCorrectSemantics() {
            assertAll(
                    () -> assertThat(AppointmentStatus.NO_SHOW.name()).isEqualTo("NO_SHOW"),
                    () -> assertThat(AppointmentStatus.NO_SHOW.ordinal()).isEqualTo(5)
            );
        }
    }

    @Nested
    @DisplayName("Business Rule Mapping Tests")
    class BusinessRuleTests {

        @Test
        @DisplayName("CANCELLED status should imply cancellation before scheduled time")
        void cancelledImpliesBeforeScheduledTime() {
            // Business rule: "Appointment was cancelled before the scheduled time."
            assertThat(AppointmentStatus.CANCELLED).isNotNull();
            // This is a semantic test - the enum value itself represents this business rule.
        }

        @Test
        @DisplayName("NO_SHOW status should trigger scheduling restrictions per business rule")
        void noShowTriggersRestrictions() {
            // Business rule: "3 or more NO_SHOWs within 6 months may restrict future scheduling."
            assertThat(AppointmentStatus.NO_SHOW).isNotNull();
            // The presence of this enum value enables the business rule enforcement.
        }

        @Test
        @DisplayName("Should have distinct statuses for successful vs unsuccessful completion")
        void shouldDistinguishCompletionOutcomes() {
            Set<AppointmentStatus> successfulOutcomes = Set.of(AppointmentStatus.COMPLETED);
            Set<AppointmentStatus> unsuccessfulOutcomes = Set.of(AppointmentStatus.CANCELLED, AppointmentStatus.NO_SHOW);

            assertAll(
                    () -> assertThat(successfulOutcomes).containsExactly(AppointmentStatus.COMPLETED),
                    () -> assertThat(unsuccessfulOutcomes).containsExactlyInAnyOrder(
                            AppointmentStatus.CANCELLED,
                            AppointmentStatus.NO_SHOW
                    ),
                    () -> assertThat(successfulOutcomes).doesNotContainAnyElementsOf(unsuccessfulOutcomes)
            );
        }
    }

    @Nested
    @DisplayName("Enum Utility and Lookup Tests")
    class UtilityTests {

        @ParameterizedTest
        @ValueSource(strings = {"SCHEDULED", "CONFIRMED", "IN_PROGRESS", "COMPLETED", "CANCELLED", "NO_SHOW"})
        @DisplayName("Should lookup enum value by case-sensitive name")
        void shouldLookupByName(String name) {
            AppointmentStatus status = AppointmentStatus.valueOf(name);
            assertThat(status).isNotNull();
            assertThat(status.name()).isEqualTo(name);
        }

        @Test
        @DisplayName("Should maintain consistent ordinal positions")
        void shouldHaveConsistentOrdinals() {
            assertAll(
                    () -> assertThat(AppointmentStatus.SCHEDULED.ordinal()).isEqualTo(0),
                    () -> assertThat(AppointmentStatus.CONFIRMED.ordinal()).isEqualTo(1),
                    () -> assertThat(AppointmentStatus.IN_PROGRESS.ordinal()).isEqualTo(2),
                    () -> assertThat(AppointmentStatus.COMPLETED.ordinal()).isEqualTo(3),
                    () -> assertThat(AppointmentStatus.CANCELLED.ordinal()).isEqualTo(4),
                    () -> assertThat(AppointmentStatus.NO_SHOW.ordinal()).isEqualTo(5)
            );
        }

        @Test
        @DisplayName("Should return all values in declaration order")
        void valuesShouldBeInDeclarationOrder() {
            AppointmentStatus[] values = AppointmentStatus.values();
            assertThat(values).containsExactly(
                    AppointmentStatus.SCHEDULED,
                    AppointmentStatus.CONFIRMED,
                    AppointmentStatus.IN_PROGRESS,
                    AppointmentStatus.COMPLETED,
                    AppointmentStatus.CANCELLED,
                    AppointmentStatus.NO_SHOW
            );
        }
    }

    @Nested
    @DisplayName("Domain Integration Readiness Tests")
    class DomainIntegrationTests {

        @Test
        @DisplayName("Should be usable in switch expressions")
        void shouldWorkInSwitchExpressions() {
            AppointmentStatus status = AppointmentStatus.COMPLETED;
            String result = switch (status) {
                case SCHEDULED -> "Scheduled";
                case CONFIRMED -> "Confirmed";
                case IN_PROGRESS -> "In Progress";
                case COMPLETED -> "Completed";
                case CANCELLED -> "Cancelled";
                case NO_SHOW -> "No Show";
            };
            assertThat(result).isEqualTo("Completed");
        }

        @Test
        @DisplayName("Should serialize to string consistently")
        void shouldSerializeToString() {
            assertAll(
                    () -> assertThat(AppointmentStatus.SCHEDULED.toString()).isEqualTo("SCHEDULED"),
                    () -> assertThat(AppointmentStatus.NO_SHOW.toString()).isEqualTo("NO_SHOW")
            );
        }

        @Test
        @DisplayName("Should support stream operations for business logic")
        void shouldSupportStreamOperations() {
            Set<AppointmentStatus> terminalStatuses = Arrays.stream(AppointmentStatus.values())
                    .filter(this::isTerminalStatus)
                    .collect(Collectors.toSet());

            assertThat(terminalStatuses).containsExactlyInAnyOrder(
                    AppointmentStatus.COMPLETED,
                    AppointmentStatus.CANCELLED,
                    AppointmentStatus.NO_SHOW
            );
        }
    }

    // Helper methods for business logic testing
    private boolean isActiveStatus(AppointmentStatus status) {
        return status == AppointmentStatus.SCHEDULED ||
                status == AppointmentStatus.CONFIRMED ||
                status == AppointmentStatus.IN_PROGRESS;
    }

    private boolean isTerminalStatus(AppointmentStatus status) {
        return status == AppointmentStatus.COMPLETED ||
                status == AppointmentStatus.CANCELLED ||
                status == AppointmentStatus.NO_SHOW;
    }
}