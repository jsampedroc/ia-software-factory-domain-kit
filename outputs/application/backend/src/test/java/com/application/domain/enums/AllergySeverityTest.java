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
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@DisplayName("AllergySeverity Enum Unit Tests")
class AllergySeverityTest {

    @Test
    @DisplayName("Should contain exactly three predefined severity levels")
    void shouldContainThreePredefinedSeverityLevels() {
        AllergySeverity[] values = AllergySeverity.values();

        assertThat(values).hasSize(3);
        assertThat(values).containsExactly(
                AllergySeverity.SEVERE,
                AllergySeverity.MODERATE,
                AllergySeverity.MILD
        );
    }

    @ParameterizedTest
    @EnumSource(AllergySeverity.class)
    @DisplayName("Each severity level should have a non-null name")
    void eachSeverityShouldHaveNonNullName(AllergySeverity severity) {
        assertThat(severity.name()).isNotNull().isNotEmpty();
    }

    @Test
    @DisplayName("Severity names should be in correct order of severity")
    void severityNamesShouldBeInCorrectOrder() {
        List<String> names = Arrays.stream(AllergySeverity.values())
                .map(Enum::name)
                .collect(Collectors.toList());

        assertThat(names).containsExactly("SEVERE", "MODERATE", "MILD");
    }

    @ParameterizedTest
    @ValueSource(strings = {"SEVERE", "MODERATE", "MILD"})
    @DisplayName("Should parse valid severity strings")
    void shouldParseValidSeverityStrings(String severityString) {
        AllergySeverity severity = AllergySeverity.valueOf(severityString);
        assertThat(severity).isNotNull();
        assertThat(severity.name()).isEqualTo(severityString);
    }

    @ParameterizedTest
    @ValueSource(strings = {"severe", "Moderate", "mild", "HIGH", "LOW", "UNKNOWN"})
    @DisplayName("Should throw IllegalArgumentException for invalid severity strings")
    void shouldThrowExceptionForInvalidSeverityStrings(String invalidString) {
        assertThatThrownBy(() -> AllergySeverity.valueOf(invalidString))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Nested
    @DisplayName("Business Logic Tests")
    class BusinessLogicTests {

        @Test
        @DisplayName("SEVERE should be considered highest risk")
        void severeShouldBeHighestRisk() {
            assertThat(AllergySeverity.SEVERE.ordinal()).isEqualTo(0);
        }

        @Test
        @DisplayName("MILD should be considered lowest risk")
        void mildShouldBeLowestRisk() {
            assertThat(AllergySeverity.MILD.ordinal()).isEqualTo(2);
        }

        @Test
        @DisplayName("Severity order should reflect clinical risk hierarchy")
        void severityOrderShouldReflectClinicalRisk() {
            AllergySeverity[] orderedSeverities = AllergySeverity.values();

            assertThat(orderedSeverities[0]).isEqualTo(AllergySeverity.SEVERE);
            assertThat(orderedSeverities[1]).isEqualTo(AllergySeverity.MODERATE);
            assertThat(orderedSeverities[2]).isEqualTo(AllergySeverity.MILD);
        }
    }

    @Nested
    @DisplayName("Integration Context Tests")
    class IntegrationContextTests {

        @Test
        @DisplayName("Should be usable in switch statements without default issues")
        void shouldBeUsableInSwitchStatements() {
            AllergySeverity severity = AllergySeverity.MODERATE;
            String result = switch (severity) {
                case SEVERE -> "Requires immediate attention";
                case MODERATE -> "Monitor closely";
                case MILD -> "Standard precautions";
            };

            assertThat(result).isEqualTo("Monitor closely");
        }

        @Test
        @DisplayName("Should maintain consistency with Allergy entity requirements")
        void shouldMaintainConsistencyWithAllergyEntity() {
            List<AllergySeverity> allSeverities = Arrays.asList(AllergySeverity.values());

            assertThat(allSeverities).allSatisfy(severity -> {
                assertThat(severity).isNotNull();
                assertThat(severity.name()).matches("^[A-Z]+$");
            });
        }
    }

    @Test
    @DisplayName("toString() should return the enum name")
    void toStringShouldReturnEnumName() {
        assertThat(AllergySeverity.SEVERE.toString()).isEqualTo("SEVERE");
        assertThat(AllergySeverity.MODERATE.toString()).isEqualTo("MODERATE");
        assertThat(AllergySeverity.MILD.toString()).isEqualTo("MILD");
    }

    @Test
    @DisplayName("compareTo should follow natural enum order")
    void compareToShouldFollowNaturalOrder() {
        assertThat(AllergySeverity.SEVERE.compareTo(AllergySeverity.MODERATE)).isNegative();
        assertThat(AllergySeverity.MODERATE.compareTo(AllergySeverity.MILD)).isNegative();
        assertThat(AllergySeverity.MILD.compareTo(AllergySeverity.SEVERE)).isPositive();
        assertThat(AllergySeverity.MODERATE.compareTo(AllergySeverity.MODERATE)).isZero();
    }
}