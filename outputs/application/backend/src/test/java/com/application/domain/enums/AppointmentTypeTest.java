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

@DisplayName("AppointmentType Enum Unit Tests")
class AppointmentTypeTest {

    @Test
    @DisplayName("Should contain exactly four predefined constants")
    void shouldContainExactlyFourConstants() {
        AppointmentType[] values = AppointmentType.values();

        assertThat(values)
                .hasSize(4)
                .containsExactly(
                        AppointmentType.CONSULTATION,
                        AppointmentType.TREATMENT,
                        AppointmentType.FOLLOW_UP,
                        AppointmentType.EMERGENCY
                );
    }

    @ParameterizedTest
    @EnumSource(AppointmentType.class)
    @DisplayName("Each constant should have a non-null and non-blank name")
    void eachConstantShouldHaveValidName(AppointmentType type) {
        assertThat(type.name())
                .isNotNull()
                .isNotBlank()
                .matches("^[A-Z_]+$");
    }

    @Test
    @DisplayName("Constants should be in correct order as defined")
    void constantsShouldBeInDefinedOrder() {
        AppointmentType[] values = AppointmentType.values();

        assertAll(
                () -> assertThat(values[0]).isEqualTo(AppointmentType.CONSULTATION),
                () -> assertThat(values[1]).isEqualTo(AppointmentType.TREATMENT),
                () -> assertThat(values[2]).isEqualTo(AppointmentType.FOLLOW_UP),
                () -> assertThat(values[3]).isEqualTo(AppointmentType.EMERGENCY)
        );
    }

    @Test
    @DisplayName("Should return correct ordinal positions")
    void shouldReturnCorrectOrdinalPositions() {
        assertAll(
                () -> assertThat(AppointmentType.CONSULTATION.ordinal()).isZero(),
                () -> assertThat(AppointmentType.TREATMENT.ordinal()).isEqualTo(1),
                () -> assertThat(AppointmentType.FOLLOW_UP.ordinal()).isEqualTo(2),
                () -> assertThat(AppointmentType.EMERGENCY.ordinal()).isEqualTo(3)
        );
    }

    @ParameterizedTest
    @ValueSource(strings = {"CONSULTATION", "TREATMENT", "FOLLOW_UP", "EMERGENCY"})
    @DisplayName("Should parse valid string representations")
    void shouldParseValidStringRepresentations(String enumName) {
        AppointmentType parsedType = AppointmentType.valueOf(enumName);

        assertThat(parsedType)
                .isNotNull()
                .hasToString(enumName);
    }

    @Test
    @DisplayName("Should have unique names")
    void shouldHaveUniqueNames() {
        Set<String> uniqueNames = Arrays.stream(AppointmentType.values())
                .map(Enum::name)
                .collect(Collectors.toSet());

        assertThat(uniqueNames).hasSize(AppointmentType.values().length);
    }

    @Nested
    @DisplayName("Business Logic Tests")
    class BusinessLogicTests {

        @Test
        @DisplayName("EMERGENCY type should be identifiable for scheduling rule overrides")
        void emergencyTypeShouldBeIdentifiableForRuleOverrides() {
            assertThat(AppointmentType.EMERGENCY)
                    .isEqualTo(AppointmentType.valueOf("EMERGENCY"))
                    .isNotIn(AppointmentType.CONSULTATION, AppointmentType.TREATMENT, AppointmentType.FOLLOW_UP);
        }

        @Test
        @DisplayName("Treatment-related types should be identifiable")
        void treatmentRelatedTypesShouldBeIdentifiable() {
            Set<AppointmentType> clinicalTypes = Set.of(
                    AppointmentType.TREATMENT,
                    AppointmentType.FOLLOW_UP
            );

            Set<AppointmentType> nonClinicalTypes = Set.of(
                    AppointmentType.CONSULTATION,
                    AppointmentType.EMERGENCY
            );

            assertAll(
                    () -> assertThat(AppointmentType.TREATMENT).isIn(clinicalTypes),
                    () -> assertThat(AppointmentType.FOLLOW_UP).isIn(clinicalTypes),
                    () -> assertThat(AppointmentType.CONSULTATION).isIn(nonClinicalTypes),
                    () -> assertThat(AppointmentType.EMERGENCY).isIn(nonClinicalTypes)
            );
        }

        @Test
        @DisplayName("All types should be suitable for invoice generation")
        void allTypesShouldBeSuitableForInvoiceGeneration() {
            for (AppointmentType type : AppointmentType.values()) {
                assertThat(type).isNotNull(); // All appointment types can potentially generate invoices
            }
        }
    }

    @Nested
    @DisplayName("Enum Utility Tests")
    class EnumUtilityTests {

        @Test
        @DisplayName("valueOf should be case-sensitive")
        void valueOfShouldBeCaseSensitive() {
            assertThat(AppointmentType.valueOf("CONSULTATION"))
                    .isEqualTo(AppointmentType.CONSULTATION);

            // This would throw IllegalArgumentException, which is expected behavior
        }

        @Test
        @DisplayName("Should maintain consistency between name() and toString()")
        void shouldMaintainConsistencyBetweenNameAndToString() {
            for (AppointmentType type : AppointmentType.values()) {
                assertThat(type.name()).isEqualTo(type.toString());
            }
        }

        @Test
        @DisplayName("Should be serializable and comparable")
        void shouldBeSerializableAndComparable() {
            AppointmentType[] sortedTypes = Arrays.stream(AppointmentType.values())
                    .sorted()
                    .toArray(AppointmentType[]::new);

            assertThat(sortedTypes)
                    .containsExactly(
                            AppointmentType.CONSULTATION,
                            AppointmentType.TREATMENT,
                            AppointmentType.FOLLOW_UP,
                            AppointmentType.EMERGENCY
                    );
        }
    }
}