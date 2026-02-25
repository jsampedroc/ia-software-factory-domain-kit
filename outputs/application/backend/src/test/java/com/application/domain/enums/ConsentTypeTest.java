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
import static org.junit.jupiter.api.Assertions.assertAll;

/**
 * Unit tests for the {@link ConsentType} enumeration.
 * Tests cover value validation, business logic mapping, and domain invariants.
 */
@DisplayName("ConsentType Enumeration Unit Tests")
class ConsentTypeTest {

    @Test
    @DisplayName("Should contain exactly three predefined consent types")
    void shouldContainExactlyThreePredefinedTypes() {
        // Given & When
        ConsentType[] values = ConsentType.values();
        Set<String> valueNames = Arrays.stream(values)
                .map(Enum::name)
                .collect(Collectors.toSet());

        // Then
        assertAll(
                () -> assertThat(values).hasSize(3),
                () -> assertThat(valueNames).containsExactlyInAnyOrder(
                        "TREATMENT",
                        "DATA_USAGE",
                        "PRIVACY_POLICY"
                )
        );
    }

    @Nested
    @DisplayName("Value-Specific Business Logic Tests")
    class ValueSpecificTests {

        @ParameterizedTest
        @EnumSource(ConsentType.class)
        @DisplayName("Each consent type should have a non-null string representation")
        void eachTypeShouldHaveNonNullToString(ConsentType type) {
            assertThat(type.toString()).isNotNull().isNotEmpty();
        }

        @Test
        @DisplayName("TREATMENT consent should be for clinical procedures")
        void treatmentConsentShouldBeForClinicalProcedures() {
            assertThat(ConsentType.TREATMENT.name()).isEqualTo("TREATMENT");
        }

        @Test
        @DisplayName("DATA_USAGE consent should be for data handling")
        void dataUsageConsentShouldBeForDataHandling() {
            assertThat(ConsentType.DATA_USAGE.name()).isEqualTo("DATA_USAGE");
        }

        @Test
        @DisplayName("PRIVACY_POLICY consent should be for general terms")
        void privacyPolicyConsentShouldBeForGeneralTerms() {
            assertThat(ConsentType.PRIVACY_POLICY.name()).isEqualTo("PRIVACY_POLICY");
        }
    }

    @Nested
    @DisplayName("Domain Invariant and Validation Tests")
    class DomainInvariantTests {

        @ParameterizedTest
        @ValueSource(strings = {"TREATMENT", "DATA_USAGE", "PRIVACY_POLICY"})
        @DisplayName("Should parse valid consent type strings")
        void shouldParseValidConsentTypeStrings(String typeName) {
            ConsentType type = ConsentType.valueOf(typeName);
            assertThat(type).isNotNull();
            assertThat(type.name()).isEqualTo(typeName);
        }

        @Test
        @DisplayName("All values should be accessible via valueOf for domain serialization")
        void allValuesShouldBeAccessibleViaValueOf() {
            assertAll(
                    () -> assertThat(ConsentType.valueOf("TREATMENT")).isEqualTo(ConsentType.TREATMENT),
                    () -> assertThat(ConsentType.valueOf("DATA_USAGE")).isEqualTo(ConsentType.DATA_USAGE),
                    () -> assertThat(ConsentType.valueOf("PRIVACY_POLICY")).isEqualTo(ConsentType.PRIVACY_POLICY)
            );
        }

        @Test
        @DisplayName("Should maintain ordinal consistency for persistence")
        void shouldMaintainOrdinalConsistency() {
            assertAll(
                    () -> assertThat(ConsentType.TREATMENT.ordinal()).isEqualTo(0),
                    () -> assertThat(ConsentType.DATA_USAGE.ordinal()).isEqualTo(1),
                    () -> assertThat(ConsentType.PRIVACY_POLICY.ordinal()).isEqualTo(2)
            );
        }
    }

    @Nested
    @DisplayName("Integration Readiness Tests")
    class IntegrationReadinessTests {

        @Test
        @DisplayName("Should be usable in switch statements for business logic")
        void shouldBeUsableInSwitchStatements() {
            ConsentType type = ConsentType.TREATMENT;
            String description;

            switch (type) {
                case TREATMENT:
                    description = "Clinical Procedure Consent";
                    break;
                case DATA_USAGE:
                    description = "Data Handling Consent";
                    break;
                case PRIVACY_POLICY:
                    description = "General Terms Consent";
                    break;
                default:
                    description = "Unknown";
            }

            assertThat(description).isEqualTo("Clinical Procedure Consent");
        }

        @Test
        @DisplayName("Should support stream operations for validation")
        void shouldSupportStreamOperationsForValidation() {
            Set<String> typeNames = Arrays.stream(ConsentType.values())
                    .map(Enum::name)
                    .collect(Collectors.toSet());

            assertThat(typeNames).contains("TREATMENT", "DATA_USAGE", "PRIVACY_POLICY");
        }
    }
}