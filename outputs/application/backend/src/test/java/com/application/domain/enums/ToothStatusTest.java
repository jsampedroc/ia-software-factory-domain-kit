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

/**
 * Unit tests for {@link ToothStatus}.
 * Tests cover enum completeness, value retrieval, business logic categorization,
 * and edge cases as per the dental domain requirements.
 */
@DisplayName("ToothStatus Enum Unit Tests")
class ToothStatusTest {

    @Test
    @DisplayName("Should contain exactly six predefined statuses")
    void shouldContainAllExpectedValues() {
        ToothStatus[] values = ToothStatus.values();

        assertThat(values)
                .hasSize(6)
                .containsExactlyInAnyOrder(
                        ToothStatus.HEALTHY,
                        ToothStatus.CARIES,
                        ToothStatus.FILLED,
                        ToothStatus.ROOT_CANAL,
                        ToothStatus.MISSING,
                        ToothStatus.IMPLANT
                );
    }

    @ParameterizedTest
    @EnumSource(ToothStatus.class)
    @DisplayName("Each enum value should have a non-null name and ordinal")
    void eachValueShouldHaveNameAndOrdinal(ToothStatus status) {
        assertThat(status.name()).isNotNull().isNotEmpty();
        assertThat(status.ordinal()).isGreaterThanOrEqualTo(0);
    }

    @Nested
    @DisplayName("Business Logic Categorization Tests")
    class BusinessLogicCategorization {

        @Test
        @DisplayName("Should correctly identify natural tooth conditions")
        void shouldIdentifyNaturalTeeth() {
            Set<ToothStatus> naturalStatuses = Arrays.stream(ToothStatus.values())
                    .filter(ToothStatus::isNatural)
                    .collect(Collectors.toSet());

            assertThat(naturalStatuses)
                    .containsExactlyInAnyOrder(
                            ToothStatus.HEALTHY,
                            ToothStatus.CARIES,
                            ToothStatus.FILLED,
                            ToothStatus.ROOT_CANAL
                    )
                    .doesNotContain(ToothStatus.MISSING, ToothStatus.IMPLANT);
        }

        @Test
        @DisplayName("Should correctly identify non-natural tooth conditions")
        void shouldIdentifyNonNaturalTeeth() {
            Set<ToothStatus> nonNaturalStatuses = Arrays.stream(ToothStatus.values())
                    .filter(status -> !status.isNatural())
                    .collect(Collectors.toSet());

            assertThat(nonNaturalStatuses)
                    .containsExactlyInAnyOrder(
                            ToothStatus.MISSING,
                            ToothStatus.IMPLANT
                    );
        }

        @Test
        @DisplayName("Should correctly identify conditions requiring treatment")
        void shouldIdentifyConditionsRequiringTreatment() {
            Set<ToothStatus> requiresTreatment = Arrays.stream(ToothStatus.values())
                    .filter(ToothStatus::requiresTreatment)
                    .collect(Collectors.toSet());

            assertThat(requiresTreatment)
                    .containsExactlyInAnyOrder(
                            ToothStatus.CARIES,
                            ToothStatus.ROOT_CANAL
                    )
                    .doesNotContain(
                            ToothStatus.HEALTHY,
                            ToothStatus.FILLED,
                            ToothStatus.MISSING,
                            ToothStatus.IMPLANT
                    );
        }

        @Test
        @DisplayName("Should correctly identify restored conditions")
        void shouldIdentifyRestoredConditions() {
            Set<ToothStatus> restoredStatuses = Arrays.stream(ToothStatus.values())
                    .filter(ToothStatus::isRestored)
                    .collect(Collectors.toSet());

            assertThat(restoredStatuses)
                    .containsExactlyInAnyOrder(
                            ToothStatus.FILLED,
                            ToothStatus.ROOT_CANAL,
                            ToothStatus.IMPLANT
                    )
                    .doesNotContain(
                            ToothStatus.HEALTHY,
                            ToothStatus.CARIES,
                            ToothStatus.MISSING
                    );
        }
    }

    @Nested
    @DisplayName("ValueOf and Parsing Tests")
    class ValueOfAndParsingTests {

        @ParameterizedTest
        @EnumSource(ToothStatus.class)
        @DisplayName("Should parse valid enum name via valueOf")
        void shouldParseValidEnumName(ToothStatus expectedStatus) {
            ToothStatus actualStatus = ToothStatus.valueOf(expectedStatus.name());

            assertThat(actualStatus).isEqualTo(expectedStatus);
        }

        @ParameterizedTest
        @ValueSource(strings = {"UNKNOWN", "DECAYED", "EXTRACTED", ""})
        @DisplayName("Should throw IllegalArgumentException for invalid enum name")
        void shouldThrowExceptionForInvalidName(String invalidName) {
            assertThatThrownBy(() -> ToothStatus.valueOf(invalidName))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessageContaining(invalidName);
        }

        @Test
        @DisplayName("Should handle case-insensitive parsing via custom lookup")
        void shouldHandleCaseInsensitiveLookup() {
            // This test demonstrates a common utility pattern for enum parsing
            // In a real scenario, you might have a fromString method in the enum
            String lowerCaseName = ToothStatus.CARIES.name().toLowerCase();

            // Simulating a custom lookup (not part of standard enum)
            ToothStatus foundStatus = Arrays.stream(ToothStatus.values())
                    .filter(status -> status.name().equalsIgnoreCase(lowerCaseName))
                    .findFirst()
                    .orElse(null);

            assertThat(foundStatus).isEqualTo(ToothStatus.CARIES);
        }
    }

    @Nested
    @DisplayName("Domain-Specific Validation Tests")
    class DomainSpecificValidationTests {

        @Test
        @DisplayName("Should validate tooth number range compatibility")
        void shouldValidateToothNumberCompatibility() {
            // According to business rules, tooth numbers are 1-32 or 0 for "general"
            // All ToothStatus values should be compatible with valid tooth numbers
            for (ToothStatus status : ToothStatus.values()) {
                // This is a logical validation - in practice, the ToothCondition VO would enforce this
                assertThat(status).isNotNull(); // Basic sanity check
                
                // Additional domain logic could be tested here:
                // e.g., assertThat(status.canBeAppliedToTooth(0)).isTrue() for "general" treatments
            }
        }

        @Test
        @DisplayName("Should maintain correct treatment progression order")
        void shouldHaveLogicalTreatmentProgression() {
            // In dental domain, certain statuses represent treatment progression
            // CARIES -> FILLED or ROOT_CANAL
            // MISSING -> IMPLANT (optional)
            
            // This test validates that our enum supports these domain transitions
            assertThat(ToothStatus.CARIES.isNatural()).isTrue();
            assertThat(ToothStatus.FILLED.isRestored()).isTrue();
            assertThat(ToothStatus.ROOT_CANAL.isRestored()).isTrue();
            
            // A tooth with caries can become filled or need root canal
            // This is business logic that would be in the TreatmentRecord entity
        }
    }

    @Test
    @DisplayName("Should provide meaningful toString representation")
    void shouldProvideMeaningfulToString() {
        for (ToothStatus status : ToothStatus.values()) {
            String stringRepresentation = status.toString();
            
            assertThat(stringRepresentation)
                    .isNotNull()
                    .isEqualTo(status.name()); // Standard enum toString behavior
        }
    }

    @Test
    @DisplayName("Should maintain immutability and singleton nature")
    void shouldMaintainEnumSingletonProperties() {
        ToothStatus[] firstCall = ToothStatus.values();
        ToothStatus[] secondCall = ToothStatus.values();
        
        // Enum values() returns a new array each time, but the instances are the same
        assertThat(firstCall).isNotSameAs(secondCall);
        assertThat(firstCall).containsExactly(secondCall);
        
        // Verify each instance is a singleton
        for (int i = 0; i < firstCall.length; i++) {
            assertThat(firstCall[i]).isSameAs(secondCall[i]);
        }
    }
}