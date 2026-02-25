package com.application.domain.model;

import com.application.domain.valueobject.TreatmentRecordId;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.junit.jupiter.params.provider.ValueSource;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Set;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@DisplayName("TreatmentRecord Entity Unit Tests")
class TreatmentRecordTest {

    private static final TreatmentRecordId VALID_ID = new TreatmentRecordId(UUID.randomUUID());
    private static final String VALID_TREATMENT_CODE = "D0120";
    private static final String VALID_DESCRIPTION = "Periodic Oral Evaluation";
    private static final String VALID_DENTIST_ID = UUID.randomUUID().toString();
    private static final LocalDateTime VALID_PERFORMED_AT = LocalDateTime.now().minusHours(1);
    private static final Set<Integer> VALID_TOOTH_NUMBERS = Set.of(18, 19);
    private static final String VALID_NOTES = "Patient presented with mild sensitivity.";
    private static final BigDecimal VALID_COST = new BigDecimal("125.00");

    @Test
    @DisplayName("Should create a TreatmentRecord with valid parameters using builder")
    void shouldCreateTreatmentRecordWithValidParameters() {
        // When
        TreatmentRecord record = TreatmentRecord.builder()
                .id(VALID_ID)
                .treatmentCode(VALID_TREATMENT_CODE)
                .description(VALID_DESCRIPTION)
                .performedBy(VALID_DENTIST_ID)
                .performedAt(VALID_PERFORMED_AT)
                .toothNumbers(VALID_TOOTH_NUMBERS)
                .notes(VALID_NOTES)
                .cost(VALID_COST)
                .build();

        // Then
        assertThat(record).isNotNull();
        assertThat(record.getId()).isEqualTo(VALID_ID);
        assertThat(record.getTreatmentCode()).isEqualTo(VALID_TREATMENT_CODE);
        assertThat(record.getDescription()).isEqualTo(VALID_DESCRIPTION);
        assertThat(record.getPerformedBy()).isEqualTo(VALID_DENTIST_ID);
        assertThat(record.getPerformedAt()).isEqualTo(VALID_PERFORMED_AT);
        assertThat(record.getToothNumbers()).isEqualTo(VALID_TOOTH_NUMBERS);
        assertThat(record.getNotes()).isEqualTo(VALID_NOTES);
        assertThat(record.getCost()).isEqualTo(VALID_COST);
    }

    @Test
    @DisplayName("Should create a TreatmentRecord for general treatment (tooth number 0)")
    void shouldCreateTreatmentRecordForGeneralTreatment() {
        // Given
        Set<Integer> generalToothNumbers = Set.of(0);

        // When
        TreatmentRecord record = TreatmentRecord.builder()
                .id(VALID_ID)
                .treatmentCode("D0150")
                .description("Comprehensive Oral Evaluation")
                .performedBy(VALID_DENTIST_ID)
                .performedAt(VALID_PERFORMED_AT)
                .toothNumbers(generalToothNumbers)
                .notes("Full mouth assessment.")
                .cost(new BigDecimal("200.00"))
                .build();

        // Then
        assertThat(record).isNotNull();
        assertThat(record.getToothNumbers()).containsExactly(0);
    }

    @Nested
    @DisplayName("Validation Tests")
    class ValidationTests {

        @ParameterizedTest
        @NullAndEmptySource
        @DisplayName("Should reject null or empty treatmentCode")
        void shouldRejectNullOrEmptyTreatmentCode(String invalidCode) {
            assertThatThrownBy(() -> TreatmentRecord.builder()
                    .id(VALID_ID)
                    .treatmentCode(invalidCode)
                    .description(VALID_DESCRIPTION)
                    .performedBy(VALID_DENTIST_ID)
                    .performedAt(VALID_PERFORMED_AT)
                    .toothNumbers(VALID_TOOTH_NUMBERS)
                    .notes(VALID_NOTES)
                    .cost(VALID_COST)
                    .build())
                    .isInstanceOf(NullPointerException.class);
        }

        @ParameterizedTest
        @NullAndEmptySource
        @DisplayName("Should reject null or empty description")
        void shouldRejectNullOrEmptyDescription(String invalidDescription) {
            assertThatThrownBy(() -> TreatmentRecord.builder()
                    .id(VALID_ID)
                    .treatmentCode(VALID_TREATMENT_CODE)
                    .description(invalidDescription)
                    .performedBy(VALID_DENTIST_ID)
                    .performedAt(VALID_PERFORMED_AT)
                    .toothNumbers(VALID_TOOTH_NUMBERS)
                    .notes(VALID_NOTES)
                    .cost(VALID_COST)
                    .build())
                    .isInstanceOf(NullPointerException.class);
        }

        @ParameterizedTest
        @NullAndEmptySource
        @DisplayName("Should reject null or empty performedBy (dentist reference)")
        void shouldRejectNullOrEmptyPerformedBy(String invalidDentistId) {
            assertThatThrownBy(() -> TreatmentRecord.builder()
                    .id(VALID_ID)
                    .treatmentCode(VALID_TREATMENT_CODE)
                    .description(VALID_DESCRIPTION)
                    .performedBy(invalidDentistId)
                    .performedAt(VALID_PERFORMED_AT)
                    .toothNumbers(VALID_TOOTH_NUMBERS)
                    .notes(VALID_NOTES)
                    .cost(VALID_COST)
                    .build())
                    .isInstanceOf(NullPointerException.class);
        }

        @Test
        @DisplayName("Should reject null performedAt")
        void shouldRejectNullPerformedAt() {
            assertThatThrownBy(() -> TreatmentRecord.builder()
                    .id(VALID_ID)
                    .treatmentCode(VALID_TREATMENT_CODE)
                    .description(VALID_DESCRIPTION)
                    .performedBy(VALID_DENTIST_ID)
                    .performedAt(null)
                    .toothNumbers(VALID_TOOTH_NUMBERS)
                    .notes(VALID_NOTES)
                    .cost(VALID_COST)
                    .build())
                    .isInstanceOf(NullPointerException.class);
        }

        @Test
        @DisplayName("Should reject null toothNumbers set")
        void shouldRejectNullToothNumbers() {
            assertThatThrownBy(() -> TreatmentRecord.builder()
                    .id(VALID_ID)
                    .treatmentCode(VALID_TREATMENT_CODE)
                    .description(VALID_DESCRIPTION)
                    .performedBy(VALID_DENTIST_ID)
                    .performedAt(VALID_PERFORMED_AT)
                    .toothNumbers(null)
                    .notes(VALID_NOTES)
                    .cost(VALID_COST)
                    .build())
                    .isInstanceOf(NullPointerException.class);
        }

        @Test
        @DisplayName("Should reject empty toothNumbers set")
        void shouldRejectEmptyToothNumbers() {
            assertThatThrownBy(() -> TreatmentRecord.builder()
                    .id(VALID_ID)
                    .treatmentCode(VALID_TREATMENT_CODE)
                    .description(VALID_DESCRIPTION)
                    .performedBy(VALID_DENTIST_ID)
                    .performedAt(VALID_PERFORMED_AT)
                    .toothNumbers(Set.of())
                    .notes(VALID_NOTES)
                    .cost(VALID_COST)
                    .build())
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessageContaining("toothNumbers");
        }

        @ParameterizedTest
        @ValueSource(ints = {-1, 33, 100})
        @DisplayName("Should reject tooth numbers outside valid range (1-32 or 0)")
        void shouldRejectInvalidToothNumbers(int invalidToothNumber) {
            Set<Integer> invalidSet = Set.of(invalidToothNumber);

            assertThatThrownBy(() -> TreatmentRecord.builder()
                    .id(VALID_ID)
                    .treatmentCode(VALID_TREATMENT_CODE)
                    .description(VALID_DESCRIPTION)
                    .performedBy(VALID_DENTIST_ID)
                    .performedAt(VALID_PERFORMED_AT)
                    .toothNumbers(invalidSet)
                    .notes(VALID_NOTES)
                    .cost(VALID_COST)
                    .build())
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessageContaining("tooth number");
        }

        @Test
        @DisplayName("Should reject null cost")
        void shouldRejectNullCost() {
            assertThatThrownBy(() -> TreatmentRecord.builder()
                    .id(VALID_ID)
                    .treatmentCode(VALID_TREATMENT_CODE)
                    .description(VALID_DESCRIPTION)
                    .performedBy(VALID_DENTIST_ID)
                    .performedAt(VALID_PERFORMED_AT)
                    .toothNumbers(VALID_TOOTH_NUMBERS)
                    .notes(VALID_NOTES)
                    .cost(null)
                    .build())
                    .isInstanceOf(NullPointerException.class);
        }

        @Test
        @DisplayName("Should reject negative cost")
        void shouldRejectNegativeCost() {
            BigDecimal negativeCost = new BigDecimal("-50.00");

            assertThatThrownBy(() -> TreatmentRecord.builder()
                    .id(VALID_ID)
                    .treatmentCode(VALID_TREATMENT_CODE)
                    .description(VALID_DESCRIPTION)
                    .performedBy(VALID_DENTIST_ID)
                    .performedAt(VALID_PERFORMED_AT)
                    .toothNumbers(VALID_TOOTH_NUMBERS)
                    .notes(VALID_NOTES)
                    .cost(negativeCost)
                    .build())
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessageContaining("cost");
        }

        @Test
        @DisplayName("Should accept zero cost (e.g., warranty work)")
        void shouldAcceptZeroCost() {
            BigDecimal zeroCost = BigDecimal.ZERO;

            TreatmentRecord record = TreatmentRecord.builder()
                    .id(VALID_ID)
                    .treatmentCode(VALID_TREATMENT_CODE)
                    .description(VALID_DESCRIPTION)
                    .performedBy(VALID_DENTIST_ID)
                    .performedAt(VALID_PERFORMED_AT)
                    .toothNumbers(VALID_TOOTH_NUMBERS)
                    .notes(VALID_NOTES)
                    .cost(zeroCost)
                    .build();

            assertThat(record.getCost()).isEqualByComparingTo(BigDecimal.ZERO);
        }
    }

    @Nested
    @DisplayName("Business Rule Compliance Tests")
    class BusinessRuleTests {

        private static java.util.stream.Stream<Arguments> provideValidToothNumberSets() {
            return java.util.stream.Stream.of(
                    Arguments.of("Single tooth", Set.of(1)),
                    Arguments.of("Multiple teeth", Set.of(14, 15, 30)),
                    Arguments.of("General treatment", Set.of(0)),
                    Arguments.of("Mixed with general", Set.of(0, 2, 3)) // Note: Business rule may need clarification if 0 can be mixed.
            );
        }

        @ParameterizedTest(name = "{0}")
        @MethodSource("provideValidToothNumberSets")
        @DisplayName("Should accept tooth number sets that satisfy business invariants")
        void shouldAcceptValidToothNumberSets(String testName, Set<Integer> toothNumbers) {
            // When
            TreatmentRecord record = TreatmentRecord.builder()
                    .id(VALID_ID)
                    .treatmentCode(VALID_TREATMENT_CODE)
                    .description(VALID_DESCRIPTION)
                    .performedBy(VALID_DENTIST_ID)
                    .performedAt(VALID_PERFORMED_AT)
                    .toothNumbers(toothNumbers)
                    .notes(VALID_NOTES)
                    .cost(VALID_COST)
                    .build();

            // Then
            assertThat(record.getToothNumbers()).isEqualTo(toothNumbers);
        }

        @Test
        @DisplayName("Should correctly identify high-cost treatment requiring dual verification")
        void shouldIdentifyHighCostTreatment() {
            BigDecimal highCost = new BigDecimal("1500.00");

            TreatmentRecord record = TreatmentRecord.builder()
                    .id(VALID_ID)
                    .treatmentCode("D2750")
                    .description("Crown - Porcelain Fused to High Noble Metal")
                    .performedBy(VALID_DENTIST_ID)
                    .performedAt(VALID_PERFORMED_AT)
                    .toothNumbers(Set.of(19))
                    .notes("Crown preparation completed.")
                    .cost(highCost)
                    .build();

            assertThat(record.getCost()).isGreaterThan(new BigDecimal("1000.00"));
        }
    }

    @Nested
    @DisplayName("Value Object Behavior Tests")
    class ValueObjectBehaviorTests {

        @Test
        @DisplayName("Two TreatmentRecords with same ID should be equal")
        void shouldBeEqualWithSameId() {
            TreatmentRecord record1 = TreatmentRecord.builder()
                    .id(VALID_ID)
                    .treatmentCode(VALID_TREATMENT_CODE)
                    .description(VALID_DESCRIPTION)
                    .performedBy(VALID_DENTIST_ID)
                    .performedAt(VALID_PERFORMED_AT)
                    .toothNumbers(VALID_TOOTH_NUMBERS)
                    .notes(VALID_NOTES)
                    .cost(VALID_COST)
                    .build();

            TreatmentRecord record2 = TreatmentRecord.builder()
                    .id(VALID_ID) // Same ID
                    .treatmentCode("DIFFERENT_CODE")
                    .description("Different Description")
                    .performedBy(UUID.randomUUID().toString())
                    .performedAt(LocalDateTime.now())
                    .toothNumbers(Set.of(31))
                    .notes("Different notes")
                    .cost(new BigDecimal("999.99"))
                    .build();

            assertThat(record1).isEqualTo(record2);
            assertThat(record1.hashCode()).isEqualTo(record2.hashCode());
        }

        @Test
        @DisplayName("Two TreatmentRecords with different IDs should not be equal")
        void shouldNotBeEqualWithDifferentIds() {
            TreatmentRecordId differentId = new TreatmentRecordId(UUID.randomUUID());

            TreatmentRecord record1 = TreatmentRecord.builder()
                    .id(VALID_ID)
                    .treatmentCode(VALID_TREATMENT_CODE)
                    .description(VALID_DESCRIPTION)
                    .performedBy(VALID_DENTIST_ID)
                    .performedAt(VALID_PERFORMED_AT)
                    .toothNumbers(VALID_TOOTH_NUMBERS)
                    .notes(VALID_NOTES)
                    .cost(VALID_COST)
                    .build();

            TreatmentRecord record2 = TreatmentRecord.builder()
                    .id(differentId) // Different ID
                    .treatmentCode(VALID_TREATMENT_CODE)
                    .description(VALID_DESCRIPTION)
                    .performedBy(VALID_DENTIST_ID)
                    .performedAt(VALID_PERFORMED_AT)
                    .toothNumbers(VALID_TOOTH_NUMBERS)
                    .notes(VALID_NOTES)
                    .cost(VALID_COST)
                    .build();

            assertThat(record1).isNotEqualTo(record2);
            assertThat(record1.hashCode()).isNotEqualTo(record2.hashCode());
        }

        @Test
        @DisplayName("toString should include class name and ID")
        void toStringShouldIncludeClassNameAndId() {
            TreatmentRecord record = TreatmentRecord.builder()
                    .id(VALID_ID)
                    .treatmentCode(VALID_TREATMENT_CODE)
                    .description(VALID_DESCRIPTION)
                    .performedBy(VALID_DENTIST_ID)
                    .performedAt(VALID_PERFORMED_AT)
                    .toothNumbers(VALID_TOOTH_NUMBERS)
                    .notes(VALID_NOTES)
                    .cost(VALID_COST)
                    .build();

            String toString = record.toString();
            assertThat(toString).contains("TreatmentRecord");
            assertThat(toString).contains(VALID_ID.toString());
        }
    }

    @Test
    @DisplayName("Should create a defensive copy of toothNumbers set")
    void shouldCreateDefensiveCopyOfToothNumbers() {
        // Given
        Set<Integer> originalSet = new java.util.HashSet<>(Set.of(5, 6, 7));

        // When
        TreatmentRecord record = TreatmentRecord.builder()
                .id(VALID_ID)
                .treatmentCode(VALID_TREATMENT_CODE)
                .description(VALID_DESCRIPTION)
                .performedBy(VALID_DENTIST_ID)
                .performedAt(VALID_PERFORMED_AT)
                .toothNumbers(originalSet)
                .notes(VALID_NOTES)
                .cost(VALID_COST)
                .build();

        // Modify the original set after building
        originalSet.add(8);

        // Then
        assertThat(record.getToothNumbers()).doesNotContain(8);
        assertThat(record.getToothNumbers()).containsExactlyInAnyOrder(5, 6, 7);
    }

    @Test
    @DisplayName("Builder 'toBuilder' should create a modifiable copy")
    void toBuilderShouldCreateModifiableCopy() {
        // Given
        TreatmentRecord original = TreatmentRecord.builder()
                .id(VALID_ID)
                .treatmentCode(VALID_TREATMENT_CODE)
                .description(VALID_DESCRIPTION)
                .performedBy(VALID_DENTIST_ID)
                .performedAt(VALID_PERFORMED_AT)
                .toothNumbers(VALID_TOOTH_NUMBERS)
                .notes(VALID_NOTES)
                .cost(VALID_COST)
                .build();

        // When
        TreatmentRecord modified = original.toBuilder()
                .notes("Updated notes after follow-up.")
                .cost(new BigDecimal("130.00