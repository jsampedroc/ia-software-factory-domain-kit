package com.application.domain.model;

import com.application.domain.valueobject.AllergyId;
import com.application.domain.enums.AllergySeverity;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.junit.jupiter.params.provider.ValueSource;

import java.time.LocalDate;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@DisplayName("Allergy Value Object Unit Tests")
class AllergyTest {

    private final AllergyId validId = new AllergyId(UUID.randomUUID());
    private final String validSubstance = "Penicillin";
    private final String validReaction = "Anaphylaxis";
    private final AllergySeverity validSeverity = AllergySeverity.SEVERE;
    private final LocalDate validDiagnosedDate = LocalDate.now().minusDays(30);

    @Nested
    @DisplayName("Creation and Validation")
    class CreationTests {

        @Test
        @DisplayName("Should create Allergy successfully with valid parameters")
        void shouldCreateAllergySuccessfully() {
            Allergy allergy = new Allergy(validId, validSubstance, validReaction, validSeverity, validDiagnosedDate);

            assertThat(allergy).isNotNull();
            assertThat(allergy.allergyId()).isEqualTo(validId);
            assertThat(allergy.substance()).isEqualTo(validSubstance);
            assertThat(allergy.reaction()).isEqualTo(validReaction);
            assertThat(allergy.severity()).isEqualTo(validSeverity);
            assertThat(allergy.diagnosedDate()).isEqualTo(validDiagnosedDate);
        }

        @Test
        @DisplayName("Should throw IllegalArgumentException when substance is blank")
        void shouldThrowExceptionWhenSubstanceIsBlank() {
            assertThatThrownBy(() -> new Allergy(validId, "   ", validReaction, validSeverity, validDiagnosedDate))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessage("Substance cannot be blank");
        }

        @ParameterizedTest
        @NullAndEmptySource
        @ValueSource(strings = {"", "  "})
        @DisplayName("Should throw IllegalArgumentException when substance is null or blank")
        void shouldThrowExceptionWhenSubstanceIsNullOrBlank(String invalidSubstance) {
            assertThatThrownBy(() -> new Allergy(validId, invalidSubstance, validReaction, validSeverity, validDiagnosedDate))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessageContaining("Substance cannot be blank");
        }

        @Test
        @DisplayName("Should throw IllegalArgumentException when reaction is blank")
        void shouldThrowExceptionWhenReactionIsBlank() {
            assertThatThrownBy(() -> new Allergy(validId, validSubstance, "   ", validSeverity, validDiagnosedDate))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessage("Reaction cannot be blank");
        }

        @ParameterizedTest
        @NullAndEmptySource
        @ValueSource(strings = {"", "  "})
        @DisplayName("Should throw IllegalArgumentException when reaction is null or blank")
        void shouldThrowExceptionWhenReactionIsNullOrBlank(String invalidReaction) {
            assertThatThrownBy(() -> new Allergy(validId, validSubstance, invalidReaction, validSeverity, validDiagnosedDate))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessageContaining("Reaction cannot be blank");
        }

        @Test
        @DisplayName("Should throw IllegalArgumentException when diagnosed date is in the future")
        void shouldThrowExceptionWhenDiagnosedDateIsInFuture() {
            LocalDate futureDate = LocalDate.now().plusDays(1);

            assertThatThrownBy(() -> new Allergy(validId, validSubstance, validReaction, validSeverity, futureDate))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessage("Diagnosed date cannot be in the future");
        }

        @Test
        @DisplayName("Should accept diagnosed date as today")
        void shouldAcceptDiagnosedDateAsToday() {
            LocalDate today = LocalDate.now();
            Allergy allergy = new Allergy(validId, validSubstance, validReaction, validSeverity, today);

            assertThat(allergy.diagnosedDate()).isEqualTo(today);
        }

        @Test
        @DisplayName("Should throw NullPointerException when allergyId is null")
        void shouldThrowExceptionWhenAllergyIdIsNull() {
            assertThatThrownBy(() -> new Allergy(null, validSubstance, validReaction, validSeverity, validDiagnosedDate))
                    .isInstanceOf(NullPointerException.class)
                    .hasMessage("Allergy ID cannot be null");
        }

        @Test
        @DisplayName("Should throw NullPointerException when substance is null")
        void shouldThrowExceptionWhenSubstanceIsNull() {
            assertThatThrownBy(() -> new Allergy(validId, null, validReaction, validSeverity, validDiagnosedDate))
                    .isInstanceOf(NullPointerException.class)
                    .hasMessage("Substance cannot be null");
        }

        @Test
        @DisplayName("Should throw NullPointerException when reaction is null")
        void shouldThrowExceptionWhenReactionIsNull() {
            assertThatThrownBy(() -> new Allergy(validId, validSubstance, null, validSeverity, validDiagnosedDate))
                    .isInstanceOf(NullPointerException.class)
                    .hasMessage("Reaction cannot be null");
        }

        @Test
        @DisplayName("Should throw NullPointerException when severity is null")
        void shouldThrowExceptionWhenSeverityIsNull() {
            assertThatThrownBy(() -> new Allergy(validId, validSubstance, validReaction, null, validDiagnosedDate))
                    .isInstanceOf(NullPointerException.class)
                    .hasMessage("Severity cannot be null");
        }

        @Test
        @DisplayName("Should throw NullPointerException when diagnosedDate is null")
        void shouldThrowExceptionWhenDiagnosedDateIsNull() {
            assertThatThrownBy(() -> new Allergy(validId, validSubstance, validReaction, validSeverity, null))
                    .isInstanceOf(NullPointerException.class)
                    .hasMessage("Diagnosed date cannot be null");
        }
    }

    @Nested
    @DisplayName("Severity Check Methods")
    class SeverityCheckTests {

        @ParameterizedTest
        @EnumSource(value = AllergySeverity.class, names = {"SEVERE"})
        @DisplayName("isSevere should return true only for SEVERE severity")
        void isSevereShouldReturnTrueOnlyForSevere(AllergySeverity severity) {
            Allergy allergy = new Allergy(validId, validSubstance, validReaction, severity, validDiagnosedDate);
            assertThat(allergy.isSevere()).isTrue();
        }

        @ParameterizedTest
        @EnumSource(value = AllergySeverity.class, names = {"MODERATE", "MILD"}, mode = EnumSource.Mode.EXCLUDE)
        @DisplayName("isSevere should return false for non-SEVERE severities")
        void isSevereShouldReturnFalseForNonSevere(AllergySeverity severity) {
            if (severity != AllergySeverity.SEVERE) {
                Allergy allergy = new Allergy(validId, validSubstance, validReaction, severity, validDiagnosedDate);
                assertThat(allergy.isSevere()).isFalse();
            }
        }

        @ParameterizedTest
        @EnumSource(value = AllergySeverity.class, names = {"MODERATE"})
        @DisplayName("isModerate should return true only for MODERATE severity")
        void isModerateShouldReturnTrueOnlyForModerate(AllergySeverity severity) {
            Allergy allergy = new Allergy(validId, validSubstance, validReaction, severity, validDiagnosedDate);
            assertThat(allergy.isModerate()).isTrue();
        }

        @ParameterizedTest
        @EnumSource(value = AllergySeverity.class, names = {"SEVERE", "MILD"}, mode = EnumSource.Mode.EXCLUDE)
        @DisplayName("isModerate should return false for non-MODERATE severities")
        void isModerateShouldReturnFalseForNonModerate(AllergySeverity severity) {
            if (severity != AllergySeverity.MODERATE) {
                Allergy allergy = new Allergy(validId, validSubstance, validReaction, severity, validDiagnosedDate);
                assertThat(allergy.isModerate()).isFalse();
            }
        }

        @ParameterizedTest
        @EnumSource(value = AllergySeverity.class, names = {"MILD"})
        @DisplayName("isMild should return true only for MILD severity")
        void isMildShouldReturnTrueOnlyForMild(AllergySeverity severity) {
            Allergy allergy = new Allergy(validId, validSubstance, validReaction, severity, validDiagnosedDate);
            assertThat(allergy.isMild()).isTrue();
        }

        @ParameterizedTest
        @EnumSource(value = AllergySeverity.class, names = {"SEVERE", "MODERATE"}, mode = EnumSource.Mode.EXCLUDE)
        @DisplayName("isMild should return false for non-MILD severities")
        void isMildShouldReturnFalseForNonMild(AllergySeverity severity) {
            if (severity != AllergySeverity.MILD) {
                Allergy allergy = new Allergy(validId, validSubstance, validReaction, severity, validDiagnosedDate);
                assertThat(allergy.isMild()).isFalse();
            }
        }
    }

    @Nested
    @DisplayName("Value Object Semantics")
    class ValueObjectSemanticsTests {

        @Test
        @DisplayName("Two Allergies with same field values should be equal")
        void equalsShouldReturnTrueForSameFieldValues() {
            AllergyId sameId = new AllergyId(UUID.fromString("123e4567-e89b-12d3-a456-426614174000"));
            Allergy allergy1 = new Allergy(sameId, "Latex", "Skin Rash", AllergySeverity.MODERATE, LocalDate.of(2023, 5, 10));
            Allergy allergy2 = new Allergy(sameId, "Latex", "Skin Rash", AllergySeverity.MODERATE, LocalDate.of(2023, 5, 10));

            assertThat(allergy1).isEqualTo(allergy2);
            assertThat(allergy1.hashCode()).isEqualTo(allergy2.hashCode());
        }

        @Test
        @DisplayName("Two Allergies with different IDs should not be equal")
        void equalsShouldReturnFalseForDifferentIds() {
            AllergyId id1 = new AllergyId(UUID.randomUUID());
            AllergyId id2 = new AllergyId(UUID.randomUUID());
            Allergy allergy1 = new Allergy(id1, "Latex", "Skin Rash", AllergySeverity.MODERATE, LocalDate.of(2023, 5, 10));
            Allergy allergy2 = new Allergy(id2, "Latex", "Skin Rash", AllergySeverity.MODERATE, LocalDate.of(2023, 5, 10));

            assertThat(allergy1).isNotEqualTo(allergy2);
        }

        @Test
        @DisplayName("Two Allergies with different substances should not be equal")
        void equalsShouldReturnFalseForDifferentSubstances() {
            AllergyId sameId = new AllergyId(UUID.randomUUID());
            Allergy allergy1 = new Allergy(sameId, "Latex", "Skin Rash", AllergySeverity.MODERATE, LocalDate.of(2023, 5, 10));
            Allergy allergy2 = new Allergy(sameId, "Penicillin", "Skin Rash", AllergySeverity.MODERATE, LocalDate.of(2023, 5, 10));

            assertThat(allergy1).isNotEqualTo(allergy2);
        }

        @Test
        @DisplayName("Allergy should implement ValueObject interface")
        void shouldImplementValueObjectInterface() {
            Allergy allergy = new Allergy(validId, validSubstance, validReaction, validSeverity, validDiagnosedDate);
            assertThat(allergy).isInstanceOf(com.application.domain.shared.ValueObject.class);
        }

        @Test
        @DisplayName("toString should contain all relevant fields")
        void toStringShouldContainRelevantFields() {
            Allergy allergy = new Allergy(validId, validSubstance, validReaction, validSeverity, validDiagnosedDate);
            String toString = allergy.toString();

            assertThat(toString).contains(validSubstance);
            assertThat(toString).contains(validReaction);
            assertThat(toString).contains(validSeverity.toString());
            assertThat(toString).contains(validDiagnosedDate.toString());
        }
    }

    @Nested
    @DisplayName("Business Logic Integration")
    class BusinessLogicTests {

        @Test
        @DisplayName("Severe allergy should be considered high risk")
        void severeAllergyShouldBeHighRisk() {
            Allergy severeAllergy = new Allergy(validId, "Penicillin", "Anaphylaxis", AllergySeverity.SEVERE, validDiagnosedDate);
            assertThat(severeAllergy.isSevere()).isTrue();
        }

        @Test
        @DisplayName("Moderate allergy should not be considered severe")
        void moderateAllergyShouldNotBeSevere() {
            Allergy moderateAllergy = new Allergy(validId, "Latex", "Skin Rash", AllergySeverity.MODERATE, validDiagnosedDate);
            assertThat(moderateAllergy.isSevere()).isFalse();
            assertThat(moderateAllergy.isModerate()).isTrue();
        }

        @Test
        @DisplayName("Mild allergy should be considered low risk")
        void mildAllergyShouldBeLowRisk() {
            Allergy mildAllergy = new Allergy(validId, "Ibuprofen", "Mild Rash", AllergySeverity.MILD, validDiagnosedDate);
            assertThat(mildAllergy.isSevere()).isFalse();
            assertThat(mildAllergy.isModerate()).isFalse();
            assertThat(mildAllergy.isMild()).isTrue();
        }
    }
}