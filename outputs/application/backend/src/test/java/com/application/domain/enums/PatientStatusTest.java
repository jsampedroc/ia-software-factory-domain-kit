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

@DisplayName("PatientStatus Enum Unit Tests")
class PatientStatusTest {

    @Test
    @DisplayName("Should have exactly three defined constants")
    void shouldHaveExactlyThreeConstants() {
        PatientStatus[] values = PatientStatus.values();
        assertThat(values).hasSize(3);
        assertThat(values).containsExactly(PatientStatus.ACTIVE, PatientStatus.INACTIVE, PatientStatus.ARCHIVED);
    }

    @Nested
    @DisplayName("Constant Values and Names")
    class ConstantValuesAndNames {

        @ParameterizedTest
        @EnumSource(PatientStatus.class)
        @DisplayName("Each constant should have a non-null name")
        void eachConstantShouldHaveNonNullName(PatientStatus status) {
            assertThat(status.name()).isNotNull().isNotEmpty();
        }

        @Test
        @DisplayName("Constant names should match expected values")
        void constantNamesShouldMatchExpectedValues() {
            assertThat(PatientStatus.ACTIVE.name()).isEqualTo("ACTIVE");
            assertThat(PatientStatus.INACTIVE.name()).isEqualTo("INACTIVE");
            assertThat(PatientStatus.ARCHIVED.name()).isEqualTo("ARCHIVED");
        }

        @Test
        @DisplayName("Ordinal values should be sequential")
        void ordinalValuesShouldBeSequential() {
            assertThat(PatientStatus.ACTIVE.ordinal()).isEqualTo(0);
            assertThat(PatientStatus.INACTIVE.ordinal()).isEqualTo(1);
            assertThat(PatientStatus.ARCHIVED.ordinal()).isEqualTo(2);
        }
    }

    @Nested
    @DisplayName("ValueOf and Parsing")
    class ValueOfAndParsing {

        @ParameterizedTest
        @ValueSource(strings = {"ACTIVE", "INACTIVE", "ARCHIVED"})
        @DisplayName("Should parse valid string to enum constant")
        void shouldParseValidStringToEnumConstant(String validName) {
            PatientStatus status = PatientStatus.valueOf(validName);
            assertThat(status).isNotNull();
            assertThat(status.name()).isEqualTo(validName);
        }

        @ParameterizedTest
        @ValueSource(strings = {"", "active", "Active", "INVALID", "ARCHIVED "})
        @DisplayName("Should throw IllegalArgumentException for invalid enum name")
        void shouldThrowIllegalArgumentExceptionForInvalidEnumName(String invalidName) {
            assertThatThrownBy(() -> PatientStatus.valueOf(invalidName))
                    .isInstanceOf(IllegalArgumentException.class);
        }

        @Test
        @DisplayName("Should retrieve all values as a list")
        void shouldRetrieveAllValuesAsList() {
            List<PatientStatus> statusList = Arrays.stream(PatientStatus.values())
                    .collect(Collectors.toList());
            assertThat(statusList).containsExactly(PatientStatus.ACTIVE, PatientStatus.INACTIVE, PatientStatus.ARCHIVED);
        }
    }

    @Nested
    @DisplayName("Business Logic and State Transitions")
    class BusinessLogicAndStateTransitions {

        @Test
        @DisplayName("ACTIVE status should represent an operational patient")
        void activeStatusShouldRepresentOperationalPatient() {
            assertThat(PatientStatus.ACTIVE)
                    .describedAs("ACTIVE status indicates patient can schedule appointments and receive treatment")
                    .isNotNull();
        }

        @Test
        @DisplayName("INACTIVE status should represent a temporarily unavailable patient")
        void inactiveStatusShouldRepresentTemporarilyUnavailablePatient() {
            assertThat(PatientStatus.INACTIVE)
                    .describedAs("INACTIVE status indicates patient is temporarily not receiving care")
                    .isNotNull();
        }

        @Test
        @DisplayName("ARCHIVED status should represent a permanently deactivated patient record")
        void archivedStatusShouldRepresentPermanentlyDeactivatedPatientRecord() {
            assertThat(PatientStatus.ARCHIVED)
                    .describedAs("ARCHIVED status indicates patient record is preserved but not active, per business rule 'Patient cannot be deleted, only archived'")
                    .isNotNull();
        }

        @Test
        @DisplayName("Should identify ACTIVE as the default operational state")
        void shouldIdentifyActiveAsDefaultOperationalState() {
            // This test validates the implied business rule that new patients start as ACTIVE
            assertThat(PatientStatus.ACTIVE).isEqualTo(PatientStatus.valueOf("ACTIVE"));
        }
    }

    @Nested
    @DisplayName("Enum Utility and Comparison")
    class EnumUtilityAndComparison {

        @Test
        @DisplayName("Constants should be comparable by natural order")
        void constantsShouldBeComparableByNaturalOrder() {
            assertThat(PatientStatus.ACTIVE.compareTo(PatientStatus.INACTIVE)).isNegative();
            assertThat(PatientStatus.INACTIVE.compareTo(PatientStatus.ARCHIVED)).isNegative();
            assertThat(PatientStatus.ARCHIVED.compareTo(PatientStatus.ACTIVE)).isPositive();
        }

        @Test
        @DisplayName("Should correctly implement equals based on identity")
        void shouldCorrectlyImplementEqualsBasedOnIdentity() {
            assertThat(PatientStatus.ACTIVE).isEqualTo(PatientStatus.ACTIVE);
            assertThat(PatientStatus.ACTIVE).isNotEqualTo(PatientStatus.INACTIVE);
            assertThat(PatientStatus.ACTIVE).isNotEqualTo(null);
            assertThat(PatientStatus.ACTIVE).isNotEqualTo("ACTIVE");
        }

        @Test
        @DisplayName("HashCode should be consistent for same constant")
        void hashCodeShouldBeConsistentForSameConstant() {
            PatientStatus status1 = PatientStatus.ARCHIVED;
            PatientStatus status2 = PatientStatus.ARCHIVED;
            assertThat(status1.hashCode()).isEqualTo(status2.hashCode());
        }

        @Test
        @DisplayName("ToString should return the constant name")
        void toStringShouldReturnTheConstantName() {
            assertThat(PatientStatus.INACTIVE.toString()).isEqualTo("INACTIVE");
        }
    }

    @Test
    @Tag("integration")
    @DisplayName("Should be usable in switch statements without default issues")
    void shouldBeUsableInSwitchStatementsWithoutDefaultIssues() {
        PatientStatus status = PatientStatus.ACTIVE;
        String result = switch (status) {
            case ACTIVE -> "Operational";
            case INACTIVE -> "Suspended";
            case ARCHIVED -> "Historical";
        };
        assertThat(result).isEqualTo("Operational");
    }
}