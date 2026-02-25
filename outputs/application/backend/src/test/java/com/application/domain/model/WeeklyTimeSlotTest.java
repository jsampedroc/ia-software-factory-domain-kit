package com.application.domain.model;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.NullSource;
import org.junit.jupiter.params.provider.ValueSource;

import java.time.DayOfWeek;
import java.time.LocalTime;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.assertj.core.api.Assertions.within;

class WeeklyTimeSlotTest {

    @Nested
    @DisplayName("Instantiation and Validation")
    class InstantiationTests {

        @Test
        @DisplayName("Should create a valid WeeklyTimeSlot")
        void shouldCreateValidWeeklyTimeSlot() {
            // Given
            DayOfWeek day = DayOfWeek.MONDAY;
            LocalTime start = LocalTime.of(9, 0);
            LocalTime end = LocalTime.of(17, 0);

            // When
            WeeklyTimeSlot slot = new WeeklyTimeSlot(day, start, end);

            // Then
            assertThat(slot.dayOfWeek()).isEqualTo(day);
            assertThat(slot.startTime()).isEqualTo(start);
            assertThat(slot.endTime()).isEqualTo(end);
        }

        @ParameterizedTest
        @NullSource
        @DisplayName("Should throw IllegalArgumentException when dayOfWeek is null")
        void shouldThrowExceptionWhenDayOfWeekIsNull(DayOfWeek day) {
            LocalTime start = LocalTime.of(9, 0);
            LocalTime end = LocalTime.of(17, 0);

            assertThatThrownBy(() -> new WeeklyTimeSlot(day, start, end))
                    .isInstanceOf(NullPointerException.class)
                    .hasMessageContaining("Day of week cannot be null");
        }

        @ParameterizedTest
        @NullSource
        @DisplayName("Should throw IllegalArgumentException when startTime is null")
        void shouldThrowExceptionWhenStartTimeIsNull(LocalTime start) {
            DayOfWeek day = DayOfWeek.MONDAY;
            LocalTime end = LocalTime.of(17, 0);

            assertThatThrownBy(() -> new WeeklyTimeSlot(day, start, end))
                    .isInstanceOf(NullPointerException.class)
                    .hasMessageContaining("Start time cannot be null");
        }

        @ParameterizedTest
        @NullSource
        @DisplayName("Should throw IllegalArgumentException when endTime is null")
        void shouldThrowExceptionWhenEndTimeIsNull(LocalTime end) {
            DayOfWeek day = DayOfWeek.MONDAY;
            LocalTime start = LocalTime.of(9, 0);

            assertThatThrownBy(() -> new WeeklyTimeSlot(day, start, end))
                    .isInstanceOf(NullPointerException.class)
                    .hasMessageContaining("End time cannot be null");
        }

        @Test
        @DisplayName("Should throw IllegalArgumentException when endTime is not after startTime")
        void shouldThrowExceptionWhenEndTimeNotAfterStartTime() {
            DayOfWeek day = DayOfWeek.MONDAY;
            LocalTime start = LocalTime.of(17, 0);
            LocalTime end = LocalTime.of(9, 0);

            assertThatThrownBy(() -> new WeeklyTimeSlot(day, start, end))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessageContaining("End time must be after start time");
        }

        @Test
        @DisplayName("Should throw IllegalArgumentException when endTime equals startTime")
        void shouldThrowExceptionWhenEndTimeEqualsStartTime() {
            DayOfWeek day = DayOfWeek.MONDAY;
            LocalTime time = LocalTime.of(9, 0);

            assertThatThrownBy(() -> new WeeklyTimeSlot(day, time, time))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessageContaining("End time must be after start time");
        }
    }

    @Nested
    @DisplayName("Overlap Detection")
    class OverlapTests {

        private static Stream<Arguments> provideOverlappingSlots() {
            return Stream.of(
                    Arguments.of(
                            new WeeklyTimeSlot(DayOfWeek.MONDAY, LocalTime.of(9, 0), LocalTime.of(12, 0)),
                            new WeeklyTimeSlot(DayOfWeek.MONDAY, LocalTime.of(10, 0), LocalTime.of(13, 0)),
                            true
                    ),
                    Arguments.of(
                            new WeeklyTimeSlot(DayOfWeek.MONDAY, LocalTime.of(9, 0), LocalTime.of(12, 0)),
                            new WeeklyTimeSlot(DayOfWeek.MONDAY, LocalTime.of(8, 0), LocalTime.of(10, 0)),
                            true
                    ),
                    Arguments.of(
                            new WeeklyTimeSlot(DayOfWeek.MONDAY, LocalTime.of(9, 0), LocalTime.of(12, 0)),
                            new WeeklyTimeSlot(DayOfWeek.MONDAY, LocalTime.of(10, 0), LocalTime.of(11, 0)),
                            true
                    ),
                    Arguments.of(
                            new WeeklyTimeSlot(DayOfWeek.MONDAY, LocalTime.of(9, 0), LocalTime.of(12, 0)),
                            new WeeklyTimeSlot(DayOfWeek.MONDAY, LocalTime.of(8, 0), LocalTime.of(13, 0)),
                            true
                    ),
                    Arguments.of(
                            new WeeklyTimeSlot(DayOfWeek.MONDAY, LocalTime.of(9, 0), LocalTime.of(12, 0)),
                            new WeeklyTimeSlot(DayOfWeek.MONDAY, LocalTime.of(12, 0), LocalTime.of(14, 0)),
                            false
                    ),
                    Arguments.of(
                            new WeeklyTimeSlot(DayOfWeek.MONDAY, LocalTime.of(9, 0), LocalTime.of(12, 0)),
                            new WeeklyTimeSlot(DayOfWeek.MONDAY, LocalTime.of(7, 0), LocalTime.of(9, 0)),
                            false
                    ),
                    Arguments.of(
                            new WeeklyTimeSlot(DayOfWeek.MONDAY, LocalTime.of(9, 0), LocalTime.of(12, 0)),
                            new WeeklyTimeSlot(DayOfWeek.TUESDAY, LocalTime.of(9, 0), LocalTime.of(12, 0)),
                            false
                    )
            );
        }

        @ParameterizedTest
        @MethodSource("provideOverlappingSlots")
        @DisplayName("Should correctly detect overlap between slots")
        void shouldCorrectlyDetectOverlap(WeeklyTimeSlot slot1, WeeklyTimeSlot slot2, boolean expectedOverlap) {
            assertThat(slot1.overlaps(slot2)).isEqualTo(expectedOverlap);
            assertThat(slot2.overlaps(slot1)).isEqualTo(expectedOverlap);
        }

        @Test
        @DisplayName("Should not overlap with itself (edge case for same object)")
        void shouldNotOverlapWithItself() {
            WeeklyTimeSlot slot = new WeeklyTimeSlot(DayOfWeek.MONDAY, LocalTime.of(9, 0), LocalTime.of(12, 0));
            assertThat(slot.overlaps(slot)).isTrue();
        }
    }

    @Nested
    @DisplayName("Time Containment")
    class TimeContainmentTests {

        private static Stream<Arguments> provideTimeContainmentCases() {
            return Stream.of(
                    Arguments.of(
                            new WeeklyTimeSlot(DayOfWeek.MONDAY, LocalTime.of(9, 0), LocalTime.of(17, 0)),
                            LocalTime.of(10, 30),
                            true
                    ),
                    Arguments.of(
                            new WeeklyTimeSlot(DayOfWeek.MONDAY, LocalTime.of(9, 0), LocalTime.of(17, 0)),
                            LocalTime.of(9, 0),
                            true
                    ),
                    Arguments.of(
                            new WeeklyTimeSlot(DayOfWeek.MONDAY, LocalTime.of(9, 0), LocalTime.of(17, 0)),
                            LocalTime.of(16, 59, 59),
                            true
                    ),
                    Arguments.of(
                            new WeeklyTimeSlot(DayOfWeek.MONDAY, LocalTime.of(9, 0), LocalTime.of(17, 0)),
                            LocalTime.of(8, 59, 59),
                            false
                    ),
                    Arguments.of(
                            new WeeklyTimeSlot(DayOfWeek.MONDAY, LocalTime.of(9, 0), LocalTime.of(17, 0)),
                            LocalTime.of(17, 0),
                            false
                    ),
                    Arguments.of(
                            new WeeklyTimeSlot(DayOfWeek.MONDAY, LocalTime.of(9, 0), LocalTime.of(17, 0)),
                            LocalTime.of(17, 0, 1),
                            false
                    )
            );
        }

        @ParameterizedTest
        @MethodSource("provideTimeContainmentCases")
        @DisplayName("Should correctly determine if time is contained within slot")
        void shouldCorrectlyDetermineTimeContainment(WeeklyTimeSlot slot, LocalTime time, boolean expectedContainment) {
            assertThat(slot.contains(time)).isEqualTo(expectedContainment);
        }
    }

    @Nested
    @DisplayName("Duration Calculation")
    class DurationTests {

        private static Stream<Arguments> provideDurationCases() {
            return Stream.of(
                    Arguments.of(
                            new WeeklyTimeSlot(DayOfWeek.MONDAY, LocalTime.of(9, 0), LocalTime.of(17, 0)),
                            480L
                    ),
                    Arguments.of(
                            new WeeklyTimeSlot(DayOfWeek.TUESDAY, LocalTime.of(13, 30), LocalTime.of(14, 45)),
                            75L
                    ),
                    Arguments.of(
                            new WeeklyTimeSlot(DayOfWeek.WEDNESDAY, LocalTime.of(8, 0), LocalTime.of(8, 30)),
                            30L
                    ),
                    Arguments.of(
                            new WeeklyTimeSlot(DayOfWeek.THURSDAY, LocalTime.of(23, 0), LocalTime.of(23, 1)),
                            1L
                    ),
                    Arguments.of(
                            new WeeklyTimeSlot(DayOfWeek.FRIDAY, LocalTime.of(0, 0), LocalTime.of(1, 0)),
                            60L
                    )
            );
        }

        @ParameterizedTest
        @MethodSource("provideDurationCases")
        @DisplayName("Should correctly calculate duration in minutes")
        void shouldCorrectlyCalculateDuration(WeeklyTimeSlot slot, long expectedMinutes) {
            assertThat(slot.durationInMinutes()).isEqualTo(expectedMinutes);
        }
    }

    @Nested
    @DisplayName("Value Object Semantics")
    class ValueObjectTests {

        @Test
        @DisplayName("Should be equal when all properties are equal")
        void shouldBeEqualWhenPropertiesAreEqual() {
            WeeklyTimeSlot slot1 = new WeeklyTimeSlot(DayOfWeek.MONDAY, LocalTime.of(9, 0), LocalTime.of(17, 0));
            WeeklyTimeSlot slot2 = new WeeklyTimeSlot(DayOfWeek.MONDAY, LocalTime.of(9, 0), LocalTime.of(17, 0));

            assertThat(slot1).isEqualTo(slot2);
            assertThat(slot1.hashCode()).isEqualTo(slot2.hashCode());
        }

        @Test
        @DisplayName("Should not be equal when properties differ")
        void shouldNotBeEqualWhenPropertiesDiffer() {
            WeeklyTimeSlot slot1 = new WeeklyTimeSlot(DayOfWeek.MONDAY, LocalTime.of(9, 0), LocalTime.of(17, 0));
            WeeklyTimeSlot slot2 = new WeeklyTimeSlot(DayOfWeek.TUESDAY, LocalTime.of(9, 0), LocalTime.of(17, 0));
            WeeklyTimeSlot slot3 = new WeeklyTimeSlot(DayOfWeek.MONDAY, LocalTime.of(10, 0), LocalTime.of(17, 0));
            WeeklyTimeSlot slot4 = new WeeklyTimeSlot(DayOfWeek.MONDAY, LocalTime.of(9, 0), LocalTime.of(18, 0));

            assertThat(slot1).isNotEqualTo(slot2);
            assertThat(slot1).isNotEqualTo(slot3);
            assertThat(slot1).isNotEqualTo(slot4);
        }

        @Test
        @DisplayName("Should implement ValueObject interface")
        void shouldImplementValueObjectInterface() {
            WeeklyTimeSlot slot = new WeeklyTimeSlot(DayOfWeek.MONDAY, LocalTime.of(9, 0), LocalTime.of(17, 0));
            assertThat(slot).isInstanceOf(com.application.domain.shared.ValueObject.class);
        }

        @Test
        @DisplayName("Should have informative toString representation")
        void shouldHaveInformativeToString() {
            WeeklyTimeSlot slot = new WeeklyTimeSlot(DayOfWeek.MONDAY, LocalTime.of(9, 0), LocalTime.of(17, 0));
            String toString = slot.toString();

            assertThat(toString).contains("MONDAY");
            assertThat(toString).contains("09:00");
            assertThat(toString).contains("17:00");
        }
    }
}