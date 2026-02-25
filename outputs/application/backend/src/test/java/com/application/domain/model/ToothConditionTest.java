package com.application.domain.model;

import com.application.domain.enums.ToothStatus;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.ValueSource;

import java.time.LocalDate;
import java.util.Objects;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@DisplayName("ToothCondition Value Object Unit Tests")
class ToothConditionTest {

    private static final Integer VALID_TOOTH_NUMBER = 15;
    private static final ToothStatus VALID_CONDITION = ToothStatus.HEALTHY;
    private static final String VALID_NOTES = "No issues observed.";
    private static final LocalDate VALID_TREATMENT_DATE = LocalDate.of(2024, 1, 15);

    @Nested
    @DisplayName("Instantiation and Validation")
    class InstantiationTests {

        @Test
        @DisplayName("Should create a ToothCondition successfully with all valid parameters")
        void shouldCreateSuccessfully() {
            ToothCondition toothCondition = new ToothCondition(VALID_TOOTH_NUMBER, VALID_CONDITION, VALID_NOTES, VALID_TREATMENT_DATE);

            assertThat(toothCondition.toothNumber()).isEqualTo(VALID_TOOTH_NUMBER);
            assertThat(toothCondition.condition()).isEqualTo(VALID_CONDITION);
            assertThat(toothCondition.notes()).isEqualTo(VALID_NOTES);
            assertThat(toothCondition.lastTreated()).isEqualTo(VALID_TREATMENT_DATE);
        }

        @Test
        @DisplayName("Should create a ToothCondition successfully with null lastTreated")
        void shouldCreateSuccessfullyWithNullLastTreated() {
            ToothCondition toothCondition = new ToothCondition(VALID_TOOTH_NUMBER, VALID_CONDITION, VALID_NOTES, null);

            assertThat(toothCondition.toothNumber()).isEqualTo(VALID_TOOTH_NUMBER);
            assertThat(toothCondition.condition()).isEqualTo(VALID_CONDITION);
            assertThat(toothCondition.notes()).isEqualTo(VALID_NOTES);
            assertThat(toothCondition.lastTreated()).isNull();
        }

        @Test
        @DisplayName("Should throw IllegalArgumentException when toothNumber is null")
        void shouldThrowExceptionWhenToothNumberIsNull() {
            assertThatThrownBy(() -> new ToothCondition(null, VALID_CONDITION, VALID_NOTES, VALID_TREATMENT_DATE))
                    .isInstanceOf(NullPointerException.class)
                    .hasMessage("Tooth number cannot be null");
        }

        @Test
        @DisplayName("Should throw IllegalArgumentException when condition is null")
        void shouldThrowExceptionWhenConditionIsNull() {
            assertThatThrownBy(() -> new ToothCondition(VALID_TOOTH_NUMBER, null, VALID_NOTES, VALID_TREATMENT_DATE))
                    .isInstanceOf(NullPointerException.class)
                    .hasMessage("Condition cannot be null");
        }

        @Test
        @DisplayName("Should throw IllegalArgumentException when notes is null")
        void shouldThrowExceptionWhenNotesIsNull() {
            assertThatThrownBy(() -> new ToothCondition(VALID_TOOTH_NUMBER, VALID_CONDITION, null, VALID_TREATMENT_DATE))
                    .isInstanceOf(NullPointerException.class)
                    .hasMessage("Notes cannot be null");
        }

        @ParameterizedTest(name = "Tooth number {0} should be invalid")
        @ValueSource(ints = {-1, 33, 100})
        @DisplayName("Should throw IllegalArgumentException for tooth numbers outside 0-32 range")
        void shouldThrowExceptionForInvalidToothNumber(int invalidToothNumber) {
            assertThatThrownBy(() -> new ToothCondition(invalidToothNumber, VALID_CONDITION, VALID_NOTES, VALID_TREATMENT_DATE))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessage("Tooth number must be between 0 (general) and 32");
        }

        @ParameterizedTest(name = "Tooth number {0} should be valid")
        @ValueSource(ints = {0, 1, 16, 32})
        @DisplayName("Should accept tooth numbers within 0-32 range")
        void shouldAcceptValidToothNumbers(int validToothNumber) {
            ToothCondition toothCondition = new ToothCondition(validToothNumber, VALID_CONDITION, VALID_NOTES, null);
            assertThat(toothCondition.toothNumber()).isEqualTo(validToothNumber);
        }

        @Test
        @DisplayName("Should throw IllegalArgumentException when notes exceed 500 characters")
        void shouldThrowExceptionWhenNotesExceedMaxLength() {
            String longNotes = "A".repeat(501);
            assertThatThrownBy(() -> new ToothCondition(VALID_TOOTH_NUMBER, VALID_CONDITION, longNotes, VALID_TREATMENT_DATE))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessage("Notes cannot exceed 500 characters");
        }

        @Test
        @DisplayName("Should accept notes with exactly 500 characters")
        void shouldAcceptNotesWithMaxLength() {
            String maxLengthNotes = "N".repeat(500);
            ToothCondition toothCondition = new ToothCondition(VALID_TOOTH_NUMBER, VALID_CONDITION, maxLengthNotes, null);
            assertThat(toothCondition.notes()).hasSize(500);
        }
    }

    @Nested
    @DisplayName("Behavioral Methods")
    class BehavioralTests {

        @Test
        @DisplayName("updateCondition should return a new ToothCondition with updated fields")
        void updateConditionShouldReturnNewInstance() {
            ToothCondition original = new ToothCondition(VALID_TOOTH_NUMBER, ToothStatus.HEALTHY, "Initial notes", null);
            ToothStatus newCondition = ToothStatus.FILLED;
            String newNotes = "Filling completed.";
            LocalDate treatmentDate = LocalDate.now();

            ToothCondition updated = original.updateCondition(newCondition, newNotes, treatmentDate);

            assertThat(updated).isNotSameAs(original);
            assertThat(updated.toothNumber()).isEqualTo(original.toothNumber());
            assertThat(updated.condition()).isEqualTo(newCondition);
            assertThat(updated.notes()).isEqualTo(newNotes);
            assertThat(updated.lastTreated()).isEqualTo(treatmentDate);
        }

        @Test
        @DisplayName("updateCondition should throw NullPointerException when newCondition is null")
        void updateConditionShouldThrowWhenNewConditionIsNull() {
            ToothCondition original = new ToothCondition(VALID_TOOTH_NUMBER, VALID_CONDITION, VALID_NOTES, null);
            assertThatThrownBy(() -> original.updateCondition(null, "new notes", VALID_TREATMENT_DATE))
                    .isInstanceOf(NullPointerException.class)
                    .hasMessage("New condition cannot be null");
        }

        @Test
        @DisplayName("updateCondition should throw NullPointerException when newNotes is null")
        void updateConditionShouldThrowWhenNewNotesIsNull() {
            ToothCondition original = new ToothCondition(VALID_TOOTH_NUMBER, VALID_CONDITION, VALID_NOTES, null);
            assertThatThrownBy(() -> original.updateCondition(ToothStatus.CARIES, null, VALID_TREATMENT_DATE))
                    .isInstanceOf(NullPointerException.class)
                    .hasMessage("New notes cannot be null");
        }

        @Test
        @DisplayName("updateCondition should throw NullPointerException when treatmentDate is null")
        void updateConditionShouldThrowWhenTreatmentDateIsNull() {
            ToothCondition original = new ToothCondition(VALID_TOOTH_NUMBER, VALID_CONDITION, VALID_NOTES, null);
            assertThatThrownBy(() -> original.updateCondition(ToothStatus.CARIES, "new notes", null))
                    .isInstanceOf(NullPointerException.class)
                    .hasMessage("Treatment date cannot be null for condition update");
        }

        @Test
        @DisplayName("isTreated should return true when lastTreated is not null")
        void isTreatedShouldReturnTrueWhenLastTreatedExists() {
            ToothCondition treated = new ToothCondition(VALID_TOOTH_NUMBER, VALID_CONDITION, VALID_NOTES, VALID_TREATMENT_DATE);
            assertThat(treated.isTreated()).isTrue();
        }

        @Test
        @DisplayName("isTreated should return false when lastTreated is null")
        void isTreatedShouldReturnFalseWhenLastTreatedIsNull() {
            ToothCondition untreated = new ToothCondition(VALID_TOOTH_NUMBER, VALID_CONDITION, VALID_NOTES, null);
            assertThat(untreated.isTreated()).isFalse();
        }

        @ParameterizedTest(name = "Condition {0} requires attention: {1}")
        @CsvSource({
                "CARIES, true",
                "MISSING, true",
                "HEALTHY, false",
                "FILLED, false",
                "ROOT_CANAL, false",
                "IMPLANT, false"
        })
        @DisplayName("requiresAttention should return correct value based on condition")
        void requiresAttentionShouldReturnCorrectValue(ToothStatus condition, boolean expected) {
            ToothCondition toothCondition = new ToothCondition(VALID_TOOTH_NUMBER, condition, "notes", null);
            assertThat(toothCondition.requiresAttention()).isEqualTo(expected);
        }
    }

    @Nested
    @DisplayName("Value Object Semantics")
    class ValueObjectSemanticsTests {

        @Test
        @DisplayName("Equals should return true for instances with same field values")
        void equalsShouldReturnTrueForSameValues() {
            ToothCondition condition1 = new ToothCondition(12, ToothStatus.FILLED, "Amalgam filling", VALID_TREATMENT_DATE);
            ToothCondition condition2 = new ToothCondition(12, ToothStatus.FILLED, "Amalgam filling", VALID_TREATMENT_DATE);

            assertThat(condition1).isEqualTo(condition2);
            assertThat(condition1.hashCode()).isEqualTo(condition2.hashCode());
        }

        @Test
        @DisplayName("Equals should return false for instances with different tooth numbers")
        void equalsShouldReturnFalseForDifferentToothNumber() {
            ToothCondition condition1 = new ToothCondition(12, ToothStatus.FILLED, "Amalgam filling", VALID_TREATMENT_DATE);
            ToothCondition condition2 = new ToothCondition(13, ToothStatus.FILLED, "Amalgam filling", VALID_TREATMENT_DATE);

            assertThat(condition1).isNotEqualTo(condition2);
        }

        @Test
        @DisplayName("Equals should return false for instances with different conditions")
        void equalsShouldReturnFalseForDifferentCondition() {
            ToothCondition condition1 = new ToothCondition(12, ToothStatus.FILLED, "Amalgam filling", VALID_TREATMENT_DATE);
            ToothCondition condition2 = new ToothCondition(12, ToothStatus.ROOT_CANAL, "Amalgam filling", VALID_TREATMENT_DATE);

            assertThat(condition1).isNotEqualTo(condition2);
        }

        @Test
        @DisplayName("Equals should return false for instances with different notes")
        void equalsShouldReturnFalseForDifferentNotes() {
            ToothCondition condition1 = new ToothCondition(12, ToothStatus.FILLED, "Amalgam filling", VALID_TREATMENT_DATE);
            ToothCondition condition2 = new ToothCondition(12, ToothStatus.FILLED, "Composite filling", VALID_TREATMENT_DATE);

            assertThat(condition1).isNotEqualTo(condition2);
        }

        @Test
        @DisplayName("Equals should return false for instances with different lastTreated dates")
        void equalsShouldReturnFalseForDifferentLastTreated() {
            LocalDate date1 = LocalDate.of(2024, 1, 15);
            LocalDate date2 = LocalDate.of(2024, 2, 20);
            ToothCondition condition1 = new ToothCondition(12, ToothStatus.FILLED, "Amalgam filling", date1);
            ToothCondition condition2 = new ToothCondition(12, ToothStatus.FILLED, "Amalgam filling", date2);

            assertThat(condition1).isNotEqualTo(condition2);
        }

        @Test
        @DisplayName("Equals should handle null lastTreated correctly")
        void equalsShouldHandleNullLastTreated() {
            ToothCondition condition1 = new ToothCondition(12, ToothStatus.FILLED, "Amalgam filling", null);
            ToothCondition condition2 = new ToothCondition(12, ToothStatus.FILLED, "Amalgam filling", null);

            assertThat(condition1).isEqualTo(condition2);
        }

        @Test
        @DisplayName("HashCode should be consistent with equals")
        void hashCodeShouldBeConsistent() {
            ToothCondition condition = new ToothCondition(12, ToothStatus.FILLED, "Amalgam filling", VALID_TREATMENT_DATE);
            int expectedHashCode = Objects.hash(12, ToothStatus.FILLED, "Amalgam filling", VALID_TREATMENT_DATE);

            assertThat(condition.hashCode()).isEqualTo(expectedHashCode);
        }

        @Test
        @DisplayName("Should implement ValueObject interface")
        void shouldImplementValueObjectInterface() {
            ToothCondition condition = new ToothCondition(VALID_TOOTH_NUMBER, VALID_CONDITION, VALID_NOTES, null);
            assertThat(condition).isInstanceOf(com.application.domain.shared.ValueObject.class);
        }
    }
}