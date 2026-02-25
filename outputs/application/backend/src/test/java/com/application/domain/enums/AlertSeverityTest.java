package com.application.domain.enums;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;
import org.junit.jupiter.params.provider.ValueSource;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@DisplayName("AlertSeverity Enum Unit Tests")
@Tag("unit")
@Tag("domain")
@Tag("enums")
class AlertSeverityTest {

    @Test
    @DisplayName("Should contain exactly three predefined severity levels")
    void shouldContainThreePredefinedLevels() {
        // Given & When
        AlertSeverity[] values = AlertSeverity.values();
        List<String> valueNames = Arrays.stream(values)
                .map(Enum::name)
                .collect(Collectors.toList());

        // Then
        assertThat(values).hasSize(3);
        assertThat(valueNames).containsExactlyInAnyOrder("HIGH", "MEDIUM", "LOW");
    }

    @Nested
    @DisplayName("Value-Specific Tests")
    class ValueSpecificTests {

        @ParameterizedTest
        @EnumSource(AlertSeverity.class)
        @DisplayName("Each enum value should be non-null and have a valid name")
        void eachEnumValueShouldBeValid(AlertSeverity severity) {
            assertThat(severity).isNotNull();
            assertThat(severity.name()).isNotEmpty();
        }

        @Test
        @DisplayName("HIGH should be the first ordinal value")
        void highShouldBeFirstOrdinal() {
            assertThat(AlertSeverity.HIGH.ordinal()).isZero();
        }

        @Test
        @DisplayName("MEDIUM should be the second ordinal value")
        void mediumShouldBeSecondOrdinal() {
            assertThat(AlertSeverity.MEDIUM.ordinal()).isEqualTo(1);
        }

        @Test
        @DisplayName("LOW should be the third ordinal value")
        void lowShouldBeThirdOrdinal() {
            assertThat(AlertSeverity.LOW.ordinal()).isEqualTo(2);
        }
    }

    @Nested
    @DisplayName("Business Logic and Comparison Tests")
    class BusinessLogicTests {

        @Test
        @DisplayName("HIGH severity should be considered more critical than MEDIUM")
        void highShouldBeGreaterThanMedium() {
            assertThat(AlertSeverity.HIGH.ordinal()).isLessThan(AlertSeverity.MEDIUM.ordinal());
        }

        @Test
        @DisplayName("MEDIUM severity should be considered more critical than LOW")
        void mediumShouldBeGreaterThanLow() {
            assertThat(AlertSeverity.MEDIUM.ordinal()).isLessThan(AlertSeverity.LOW.ordinal());
        }

        @Test
        @DisplayName("Severity order should be HIGH -> MEDIUM -> LOW for priority sorting")
        void severityOrderForPriority() {
            List<AlertSeverity> sortedByOrdinal = Arrays.asList(AlertSeverity.values());
            assertThat(sortedByOrdinal).containsExactly(AlertSeverity.HIGH, AlertSeverity.MEDIUM, AlertSeverity.LOW);
        }
    }

    @Nested
    @DisplayName("ValueOf and Lookup Tests")
    class ValueOfTests {

        @ParameterizedTest
        @ValueSource(strings = {"HIGH", "MEDIUM", "LOW"})
        @DisplayName("Should correctly parse valid severity strings")
        void shouldParseValidSeverityStrings(String severityString) {
            AlertSeverity severity = AlertSeverity.valueOf(severityString);
            assertThat(severity).isNotNull();
            assertThat(severity.name()).isEqualTo(severityString);
        }

        @ParameterizedTest
        @ValueSource(strings = {"high", "Medium", "low", "CRITICAL", "MINOR", ""})
        @DisplayName("Should throw IllegalArgumentException for invalid severity strings")
        void shouldThrowExceptionForInvalidStrings(String invalidString) {
            assertThatThrownBy(() -> AlertSeverity.valueOf(invalidString))
                    .isInstanceOf(IllegalArgumentException.class);
        }

        @Test
        @DisplayName("Should return correct enum from valueOf with case-sensitive match")
        void valueOfIsCaseSensitive() {
            assertThat(AlertSeverity.valueOf("HIGH")).isEqualTo(AlertSeverity.HIGH);
        }
    }

    @Nested
    @DisplayName("Domain Context and Usage Tests")
    class DomainContextTests {

        @Test
        @DisplayName("Should be usable in MedicalAlert value object context")
        void shouldBeUsableInMedicalAlertContext() {
            // This test validates the enum is appropriate for its domain purpose
            assertThat(AlertSeverity.HIGH).isNotNull();
            assertThat(AlertSeverity.MEDIUM).isNotNull();
            assertThat(AlertSeverity.LOW).isNotNull();

            // Verify all values can be used in business logic
            for (AlertSeverity severity : AlertSeverity.values()) {
                assertThat(severity).isInstanceOf(AlertSeverity.class);
            }
        }

        @Test
        @DisplayName("All values should be accessible for UI dropdowns or configuration")
        void allValuesShouldBeAccessible() {
            List<AlertSeverity> allValues = Arrays.asList(AlertSeverity.values());
            assertThat(allValues).hasSize(3);
            assertThat(allValues).containsExactlyInAnyOrder(
                    AlertSeverity.HIGH,
                    AlertSeverity.MEDIUM,
                    AlertSeverity.LOW
            );
        }
    }

    @Test
    @DisplayName("Enum should be serializable and have stable string representations")
    void shouldHaveStableStringRepresentations() {
        assertThat(AlertSeverity.HIGH.toString()).isEqualTo("HIGH");
        assertThat(AlertSeverity.MEDIUM.toString()).isEqualTo("MEDIUM");
        assertThat(AlertSeverity.LOW.toString()).isEqualTo("LOW");
    }
}