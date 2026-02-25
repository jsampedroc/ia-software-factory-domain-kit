package com.application.domain.model;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.junit.jupiter.params.provider.ValueSource;

import java.time.LocalDate;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@DisplayName("TimeOffPeriod Value Object Unit Tests")
class TimeOffPeriodTest {

    private static final LocalDate VALID_START = LocalDate.of(2024, 1, 10);
    private static final LocalDate VALID_END = LocalDate.of(2024, 1, 15);
    private static final String VALID_REASON = "Annual Leave";

    @Nested
    @DisplayName("Instantiation and Validation")
    class InstantiationTests {

        @Test
        @DisplayName("Should create TimeOffPeriod with valid parameters")
        void shouldCreateTimeOffPeriodWithValidParameters() {
            TimeOffPeriod period = new TimeOffPeriod(VALID_START, VALID_END, VALID_REASON);

            assertThat(period.startDate()).isEqualTo(VALID_START);
            assertThat(period.endDate()).isEqualTo(VALID_END);
            assertThat(period.reason()).isEqualTo(VALID_REASON);
        }

        @Test
        @DisplayName("Should create TimeOffPeriod when start and end dates are the same")
        void shouldCreateTimeOffPeriodWithSameStartAndEndDate() {
            LocalDate sameDate = LocalDate.of(2024, 1, 10);
            TimeOffPeriod period = new TimeOffPeriod(sameDate, sameDate, "Single Day Off");

            assertThat(period.startDate()).isEqualTo(sameDate);
            assertThat(period.endDate()).isEqualTo(sameDate);
        }

        @Test
        @DisplayName("Should throw IllegalArgumentException when startDate is null")
        void shouldThrowExceptionWhenStartDateIsNull() {
            assertThatThrownBy(() -> new TimeOffPeriod(null, VALID_END, VALID_REASON))
                    .isInstanceOf(NullPointerException.class)
                    .hasMessage("Start date cannot be null");
        }

        @Test
        @DisplayName("Should throw IllegalArgumentException when endDate is null")
        void shouldThrowExceptionWhenEndDateIsNull() {
            assertThatThrownBy(() -> new TimeOffPeriod(VALID_START, null, VALID_REASON))
                    .isInstanceOf(NullPointerException.class)
                    .hasMessage("End date cannot be null");
        }

        @Test
        @DisplayName("Should throw IllegalArgumentException when reason is null")
        void shouldThrowExceptionWhenReasonIsNull() {
            assertThatThrownBy(() -> new TimeOffPeriod(VALID_START, VALID_END, null))
                    .isInstanceOf(NullPointerException.class)
                    .hasMessage("Reason cannot be null");
        }

        @ParameterizedTest
        @NullAndEmptySource
        @ValueSource(strings = {" ", "  ", "\t", "\n"})
        @DisplayName("Should throw IllegalArgumentException when reason is blank")
        void shouldThrowExceptionWhenReasonIsBlank(String blankReason) {
            assertThatThrownBy(() -> new TimeOffPeriod(VALID_START, VALID_END, blankReason))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessage("Reason cannot be blank");
        }

        @Test
        @DisplayName("Should throw IllegalArgumentException when endDate is before startDate")
        void shouldThrowExceptionWhenEndDateIsBeforeStartDate() {
            LocalDate start = LocalDate.of(2024, 1, 15);
            LocalDate end = LocalDate.of(2024, 1, 10);

            assertThatThrownBy(() -> new TimeOffPeriod(start, end, VALID_REASON))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessage("End date must be on or after start date");
        }
    }

    @Nested
    @DisplayName("Business Logic: overlapsWith(LocalDate)")
    class OverlapsWithSingleDateTests {

        private final TimeOffPeriod period = new TimeOffPeriod(
                LocalDate.of(2024, 1, 10),
                LocalDate.of(2024, 1, 15),
                VALID_REASON
        );

        @ParameterizedTest
        @CsvSource({
                "2024-01-09, false", // Day before start
                "2024-01-10, true",  // Start date
                "2024-01-12, true",  // Middle date
                "2024-01-15, true",  // End date
                "2024-01-16, false"  // Day after end
        })
        @DisplayName("Should correctly determine if a single date overlaps with the period")
        void shouldCorrectlyDetermineOverlapForSingleDate(LocalDate testDate, boolean expectedOverlap) {
            assertThat(period.overlapsWith(testDate)).isEqualTo(expectedOverlap);
        }
    }

    @Nested
    @DisplayName("Business Logic: overlapsWith(LocalDate, LocalDate)")
    class OverlapsWithDateRangeTests {

        private final TimeOffPeriod period = new TimeOffPeriod(
                LocalDate.of(2024, 1, 10),
                LocalDate.of(2024, 1, 15),
                VALID_REASON
        );

        @Test
        @DisplayName("Should throw IllegalArgumentException when checkStart is null")
        void shouldThrowExceptionWhenCheckStartIsNull() {
            assertThatThrownBy(() -> period.overlapsWith(null, LocalDate.of(2024, 1, 12)))
                    .isInstanceOf(NullPointerException.class)
                    .hasMessage("Check start date cannot be null");
        }

        @Test
        @DisplayName("Should throw IllegalArgumentException when checkEnd is null")
        void shouldThrowExceptionWhenCheckEndIsNull() {
            assertThatThrownBy(() -> period.overlapsWith(LocalDate.of(2024, 1, 12), null))
                    .isInstanceOf(NullPointerException.class)
                    .hasMessage("Check end date cannot be null");
        }

        @Test
        @DisplayName("Should throw IllegalArgumentException when checkEnd is before checkStart")
        void shouldThrowExceptionWhenCheckEndIsBeforeCheckStart() {
            LocalDate start = LocalDate.of(2024, 1, 12);
            LocalDate end = LocalDate.of(2024, 1, 10);

            assertThatThrownBy(() -> period.overlapsWith(start, end))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessage("Check end date must be on or after check start date");
        }

        @ParameterizedTest
        @CsvSource({
                // Range before period
                "2024-01-05, 2024-01-09, false",
                // Range ends on period start
                "2024-01-05, 2024-01-10, true",
                // Range starts before and ends during period
                "2024-01-08, 2024-01-12, true",
                // Range completely within period
                "2024-01-11, 2024-01-14, true",
                // Range matches period exactly
                "2024-01-10, 2024-01-15, true",
                // Range starts during and ends after period
                "2024-01-12, 2024-01-18, true",
                // Range starts on period end
                "2024-01-15, 2024-01-20, true",
                // Range after period
                "2024-01-16, 2024-01-20, false",
                // Single day range before
                "2024-01-09, 2024-01-09, false",
                // Single day range within
                "2024-01-12, 2024-01-12, true",
                // Single day range after
                "2024-01-16, 2024-01-16, false"
        })
        @DisplayName("Should correctly determine if a date range overlaps with the period")
        void shouldCorrectlyDetermineOverlapForDateRange(LocalDate checkStart, LocalDate checkEnd, boolean expectedOverlap) {
            assertThat(period.overlapsWith(checkStart, checkEnd)).isEqualTo(expectedOverlap);
        }
    }

    @Nested
    @DisplayName("Business Logic: isActiveOn(LocalDate)")
    class IsActiveOnTests {

        private final TimeOffPeriod period = new TimeOffPeriod(
                LocalDate.of(2024, 1, 10),
                LocalDate.of(2024, 1, 15),
                VALID_REASON
        );

        @Test
        @DisplayName("Should return true when date is within the period")
        void shouldReturnTrueWhenDateIsWithinPeriod() {
            assertThat(period.isActiveOn(LocalDate.of(2024, 1, 12))).isTrue();
        }

        @Test
        @DisplayName("Should return false when date is before the period")
        void shouldReturnFalseWhenDateIsBeforePeriod() {
            assertThat(period.isActiveOn(LocalDate.of(2024, 1, 9))).isFalse();
        }

        @Test
        @DisplayName("Should return false when date is after the period")
        void shouldReturnFalseWhenDateIsAfterPeriod() {
            assertThat(period.isActiveOn(LocalDate.of(2024, 1, 16))).isFalse();
        }

        @Test
        @DisplayName("Should return true when date is on the start date")
        void shouldReturnTrueWhenDateIsOnStartDate() {
            assertThat(period.isActiveOn(LocalDate.of(2024, 1, 10))).isTrue();
        }

        @Test
        @DisplayName("Should return true when date is on the end date")
        void shouldReturnTrueWhenDateIsOnEndDate() {
            assertThat(period.isActiveOn(LocalDate.of(2024, 1, 15))).isTrue();
        }
    }

    @Nested
    @DisplayName("Value Object Semantics")
    class ValueObjectSemanticsTests {

        @Test
        @DisplayName("Should implement ValueObject interface")
        void shouldImplementValueObjectInterface() {
            TimeOffPeriod period = new TimeOffPeriod(VALID_START, VALID_END, VALID_REASON);
            assertThat(period).isInstanceOf(com.application.domain.shared.ValueObject.class);
        }

        @Test
        @DisplayName("Should have proper equals and hashCode based on all fields")
        void shouldHaveProperEqualsAndHashCode() {
            TimeOffPeriod period1 = new TimeOffPeriod(VALID_START, VALID_END, VALID_REASON);
            TimeOffPeriod period2 = new TimeOffPeriod(VALID_START, VALID_END, VALID_REASON);
            TimeOffPeriod period3 = new TimeOffPeriod(VALID_START.plusDays(1), VALID_END, VALID_REASON);

            assertThat(period1).isEqualTo(period2);
            assertThat(period1).isNotEqualTo(period3);
            assertThat(period1.hashCode()).isEqualTo(period2.hashCode());
        }

        @Test
        @DisplayName("Should have meaningful toString representation")
        void shouldHaveMeaningfulToString() {
            TimeOffPeriod period = new TimeOffPeriod(VALID_START, VALID_END, VALID_REASON);
            String toString = period.toString();

            assertThat(toString).contains(VALID_START.toString());
            assertThat(toString).contains(VALID_END.toString());
            assertThat(toString).contains(VALID_REASON);
        }
    }
}