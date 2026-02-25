package com.application.domain.model;

import com.application.domain.valueobject.DentistScheduleId;
import com.application.domain.valueobject.UserId;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@ExtendWith(MockitoExtension.class)
class DentistScheduleTest {

    private final UserId sampleDentistId = new UserId(UUID.randomUUID());
    private final WeeklyTimeSlot sampleSlot = WeeklyTimeSlot.builder()
            .dayOfWeek(DayOfWeek.MONDAY)
            .startTime(LocalTime.of(9, 0))
            .endTime(LocalTime.of(17, 0))
            .build();
    private final Set<WeeklyTimeSlot> sampleWorkHours = Set.of(sampleSlot);
    private final TimeOffPeriod sampleTimeOff = TimeOffPeriod.builder()
            .startDate(LocalDate.now().plusDays(5))
            .endDate(LocalDate.now().plusDays(7))
            .reason("Vacation")
            .build();

    @Nested
    @DisplayName("Creation and Instantiation")
    class CreationTests {

        @Test
        @DisplayName("Should successfully create a DentistSchedule with valid parameters")
        void create_ValidParameters_ReturnsSchedule() {
            DentistSchedule schedule = DentistSchedule.create(sampleDentistId, sampleWorkHours);

            assertThat(schedule).isNotNull();
            assertThat(schedule.getId()).isNotNull();
            assertThat(schedule.getDentistId()).isEqualTo(sampleDentistId);
            assertThat(schedule.getWorkHours()).isEqualTo(sampleWorkHours);
            assertThat(schedule.getTimeOff()).isEmpty();
            assertThat(schedule.isActive()).isTrue();
            assertThat(schedule.getCreatedAt()).isNotNull();
        }

        @Test
        @DisplayName("Should throw IllegalArgumentException when dentistId is null")
        void create_NullDentistId_ThrowsException() {
            assertThatThrownBy(() -> DentistSchedule.create(null, sampleWorkHours))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessage("Dentist ID cannot be null");
        }

        @Test
        @DisplayName("Should throw IllegalArgumentException when initialWorkHours is null")
        void create_NullWorkHours_ThrowsException() {
            assertThatThrownBy(() -> DentistSchedule.create(sampleDentistId, null))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessage("Initial work hours cannot be null or empty");
        }

        @Test
        @DisplayName("Should throw IllegalArgumentException when initialWorkHours is empty")
        void create_EmptyWorkHours_ThrowsException() {
            assertThatThrownBy(() -> DentistSchedule.create(sampleDentistId, Set.of()))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessage("Initial work hours cannot be null or empty");
        }
    }

    @Nested
    @DisplayName("Work Hours Management")
    class WorkHoursManagementTests {

        @Test
        @DisplayName("Should add new work hours to schedule")
        void addWorkHours_NewSlots_ReturnsUpdatedSchedule() {
            DentistSchedule schedule = DentistSchedule.create(sampleDentistId, sampleWorkHours);
            WeeklyTimeSlot newSlot = WeeklyTimeSlot.builder()
                    .dayOfWeek(DayOfWeek.TUESDAY)
                    .startTime(LocalTime.of(10, 0))
                    .endTime(LocalTime.of(18, 0))
                    .build();
            Set<WeeklyTimeSlot> newSlots = Set.of(newSlot);

            DentistSchedule updatedSchedule = schedule.addWorkHours(newSlots);

            assertThat(updatedSchedule).isNotSameAs(schedule);
            assertThat(updatedSchedule.getWorkHours()).hasSize(2);
            assertThat(updatedSchedule.getWorkHours()).contains(sampleSlot, newSlot);
        }

        @Test
        @DisplayName("Should return same schedule when adding null or empty slots")
        void addWorkHours_NullOrEmpty_ReturnsSameSchedule() {
            DentistSchedule schedule = DentistSchedule.create(sampleDentistId, sampleWorkHours);

            DentistSchedule updatedWithNull = schedule.addWorkHours(null);
            DentistSchedule updatedWithEmpty = schedule.addWorkHours(Set.of());

            assertThat(updatedWithNull).isSameAs(schedule);
            assertThat(updatedWithEmpty).isSameAs(schedule);
        }

        @Test
        @DisplayName("Should remove specified work hours from schedule")
        void removeWorkHours_ExistingSlots_ReturnsUpdatedSchedule() {
            WeeklyTimeSlot slotToRemove = WeeklyTimeSlot.builder()
                    .dayOfWeek(DayOfWeek.WEDNESDAY)
                    .startTime(LocalTime.of(8, 0))
                    .endTime(LocalTime.of(12, 0))
                    .build();
            Set<WeeklyTimeSlot> initialHours = new HashSet<>(sampleWorkHours);
            initialHours.add(slotToRemove);
            DentistSchedule schedule = DentistSchedule.create(sampleDentistId, initialHours);

            DentistSchedule updatedSchedule = schedule.removeWorkHours(Set.of(slotToRemove));

            assertThat(updatedSchedule).isNotSameAs(schedule);
            assertThat(updatedSchedule.getWorkHours()).hasSize(1);
            assertThat(updatedSchedule.getWorkHours()).contains(sampleSlot);
            assertThat(updatedSchedule.getWorkHours()).doesNotContain(slotToRemove);
        }

        @Test
        @DisplayName("Should return same schedule when removing null or empty slots")
        void removeWorkHours_NullOrEmpty_ReturnsSameSchedule() {
            DentistSchedule schedule = DentistSchedule.create(sampleDentistId, sampleWorkHours);

            DentistSchedule updatedWithNull = schedule.removeWorkHours(null);
            DentistSchedule updatedWithEmpty = schedule.removeWorkHours(Set.of());

            assertThat(updatedWithNull).isSameAs(schedule);
            assertThat(updatedWithEmpty).isSameAs(schedule);
        }
    }

    @Nested
    @DisplayName("Time Off Management")
    class TimeOffManagementTests {

        @Test
        @DisplayName("Should add time off period to schedule")
        void addTimeOff_NewPeriod_ReturnsUpdatedSchedule() {
            DentistSchedule schedule = DentistSchedule.create(sampleDentistId, sampleWorkHours);

            DentistSchedule updatedSchedule = schedule.addTimeOff(sampleTimeOff);

            assertThat(updatedSchedule).isNotSameAs(schedule);
            assertThat(updatedSchedule.getTimeOff()).hasSize(1);
            assertThat(updatedSchedule.getTimeOff()).contains(sampleTimeOff);
        }

        @Test
        @DisplayName("Should return same schedule when adding null time off")
        void addTimeOff_NullPeriod_ReturnsSameSchedule() {
            DentistSchedule schedule = DentistSchedule.create(sampleDentistId, sampleWorkHours);

            DentistSchedule updatedSchedule = schedule.addTimeOff(null);

            assertThat(updatedSchedule).isSameAs(schedule);
        }

        @Test
        @DisplayName("Should remove time off period from schedule")
        void removeTimeOff_ExistingPeriod_ReturnsUpdatedSchedule() {
            DentistSchedule schedule = DentistSchedule.create(sampleDentistId, sampleWorkHours)
                    .addTimeOff(sampleTimeOff);

            DentistSchedule updatedSchedule = schedule.removeTimeOff(sampleTimeOff);

            assertThat(updatedSchedule).isNotSameAs(schedule);
            assertThat(updatedSchedule.getTimeOff()).isEmpty();
        }

        @Test
        @DisplayName("Should return same schedule when removing null time off")
        void removeTimeOff_NullPeriod_ReturnsSameSchedule() {
            DentistSchedule schedule = DentistSchedule.create(sampleDentistId, sampleWorkHours)
                    .addTimeOff(sampleTimeOff);

            DentistSchedule updatedSchedule = schedule.removeTimeOff(null);

            assertThat(updatedSchedule).isSameAs(schedule);
        }
    }

    @Nested
    @DisplayName("Activation and Deactivation")
    class ActivationTests {

        @Test
        @DisplayName("Should deactivate an active schedule")
        void deactivate_ActiveSchedule_ReturnsInactiveSchedule() {
            DentistSchedule schedule = DentistSchedule.create(sampleDentistId, sampleWorkHours);
            assertThat(schedule.isActive()).isTrue();

            DentistSchedule deactivated = schedule.deactivate();

            assertThat(deactivated).isNotSameAs(schedule);
            assertThat(deactivated.isActive()).isFalse();
        }

        @Test
        @DisplayName("Should return same schedule when deactivating already inactive schedule")
        void deactivate_InactiveSchedule_ReturnsSameSchedule() {
            DentistSchedule schedule = DentistSchedule.create(sampleDentistId, sampleWorkHours)
                    .deactivate();

            DentistSchedule deactivatedAgain = schedule.deactivate();

            assertThat(deactivatedAgain).isSameAs(schedule);
        }

        @Test
        @DisplayName("Should activate an inactive schedule")
        void activate_InactiveSchedule_ReturnsActiveSchedule() {
            DentistSchedule schedule = DentistSchedule.create(sampleDentistId, sampleWorkHours)
                    .deactivate();

            DentistSchedule activated = schedule.activate();

            assertThat(activated).isNotSameAs(schedule);
            assertThat(activated.isActive()).isTrue();
        }

        @Test
        @DisplayName("Should return same schedule when activating already active schedule")
        void activate_ActiveSchedule_ReturnsSameSchedule() {
            DentistSchedule schedule = DentistSchedule.create(sampleDentistId, sampleWorkHours);

            DentistSchedule activatedAgain = schedule.activate();

            assertThat(activatedAgain).isSameAs(schedule);
        }
    }

    @Nested
    @DisplayName("Availability Checking")
    class AvailabilityTests {

        @Test
        @DisplayName("Should return false when schedule is inactive")
        void isAvailableAt_InactiveSchedule_ReturnsFalse() {
            DentistSchedule schedule = DentistSchedule.create(sampleDentistId, sampleWorkHours)
                    .deactivate();
            LocalDateTime mondayAt10 = LocalDateTime.of(LocalDate.now().with(DayOfWeek.MONDAY), LocalTime.of(10, 0));

            boolean available = schedule.isAvailableAt(mondayAt10);

            assertThat(available).isFalse();
        }

        @Test
        @DisplayName("Should return false when date falls within time off period")
        void isAvailableAt_DateTimeInTimeOff_ReturnsFalse() {
            LocalDate timeOffDate = sampleTimeOff.getStartDate().plusDays(1);
            LocalDateTime dateTimeInTimeOff = LocalDateTime.of(timeOffDate, LocalTime.of(10, 0));
            DentistSchedule schedule = DentistSchedule.create(sampleDentistId, sampleWorkHours)
                    .addTimeOff(sampleTimeOff);

            boolean available = schedule.isAvailableAt(dateTimeInTimeOff);

            assertThat(available).isFalse();
        }

        @Test
        @DisplayName("Should return true when date matches work hours and is not in time off")
        void isAvailableAt_DateTimeMatchesWorkHours_ReturnsTrue() {
            LocalDate nextMonday = LocalDate.now().with(DayOfWeek.MONDAY);
            if (nextMonday.isBefore(LocalDate.now())) {
                nextMonday = nextMonday.plusWeeks(1);
            }
            LocalDateTime mondayAt10 = LocalDateTime.of(nextMonday, LocalTime.of(10, 0));
            DentistSchedule schedule = DentistSchedule.create(sampleDentistId, sampleWorkHours);

            boolean available = schedule.isAvailableAt(mondayAt10);

            assertThat(available).isTrue();
        }

        @Test
        @DisplayName("Should return false when date does not match any work hours")
        void isAvailableAt_DateTimeOutsideWorkHours_ReturnsFalse() {
            LocalDate nextSunday = LocalDate.now().with(DayOfWeek.SUNDAY);
            if (nextSunday.isBefore(LocalDate.now())) {
                nextSunday = nextSunday.plusWeeks(1);
            }
            LocalDateTime sundayAt10 = LocalDateTime.of(nextSunday, LocalTime.of(10, 0));
            DentistSchedule schedule = DentistSchedule.create(sampleDentistId, sampleWorkHours);

            boolean available = schedule.isAvailableAt(sundayAt10);

            assertThat(available).isFalse();
        }
    }

    @Nested
    @DisplayName("Conflict Detection")
    class ConflictDetectionTests {

        @Test
        @DisplayName("Should detect overlapping time off periods")
        void hasOverlappingTimeOff_OverlappingPeriod_ReturnsTrue() {
            DentistSchedule schedule = DentistSchedule.create(sampleDentistId, sampleWorkHours)
                    .addTimeOff(sampleTimeOff);
            TimeOffPeriod overlappingPeriod = TimeOffPeriod.builder()
                    .startDate(sampleTimeOff.getStartDate().plusDays(1))
                    .endDate(sampleTimeOff.getEndDate().plusDays(1))
                    .reason("Conference")
                    .build();

            boolean hasOverlap = schedule.hasOverlappingTimeOff(overlappingPeriod);

            assertThat(hasOverlap).isTrue();
        }

        @Test
        @DisplayName("Should return false for non-overlapping time off periods")
        void hasOverlappingTimeOff_NonOverlappingPeriod_ReturnsFalse() {
            DentistSchedule schedule = DentistSchedule.create(sampleDentistId, sampleWorkHours)
                    .addTimeOff(sampleTimeOff);
            TimeOffPeriod nonOverlappingPeriod = TimeOffPeriod.builder()
                    .startDate(sampleTimeOff.getEndDate().plusDays(10))
                    .endDate(sampleTimeOff.getEndDate().plusDays(12))
                    .reason("Training")
                    .build();

            boolean hasOverlap = schedule.hasOverlappingTimeOff(nonOverlappingPeriod);

            assertThat(hasOverlap).isFalse();
        }

        @Test
        @DisplayName("Should detect conflicting work hours")
        void hasConflictingWorkHours_OverlappingSlot_ReturnsTrue() {
            DentistSchedule schedule = DentistSchedule.create(sampleDentistId, sampleWorkHours);
            WeeklyTimeSlot conflictingSlot = WeeklyTimeSlot.builder()
                    .dayOfWeek(DayOfWeek.MONDAY)
                    .startTime(LocalTime.of(14, 0))
                    .endTime(LocalTime.of(16, 0))
                    .build();

            boolean hasConflict = schedule.hasConflictingWorkHours(conflictingSlot);

            assertThat(hasConflict).isTrue();
        }

        @Test
        @DisplayName("Should return false for non-conflicting work hours")
        void hasConflictingWorkHours_NonOverlappingSlot_ReturnsFalse() {
            DentistSchedule schedule = DentistSchedule.create(sampleDentistId, sampleWorkHours);
            WeeklyTimeSlot nonConflictingSlot = WeeklyTimeSlot.builder()
                    .dayOfWeek(DayOfWeek.TUESDAY)
                    .startTime(LocalTime.of(14, 0))
                    .endTime(LocalTime.of(16, 0))
                    .build();

            boolean hasConflict = schedule.hasConflictingWorkHours(nonConflictingSlot);

            assertThat(hasConflict).isFalse();
        }
    }

    @Nested
    @DisplayName("Immutability and Value Semantics")
    class ImmutabilityTests {

        @Test
        @DisplayName("Should return unmodifiable work hours set")
        void getWorkHours_ReturnsUnmodifiableSet() {
            DentistSchedule schedule = DentistSchedule.create(sampleDentistId, sampleWorkHours);
            Set<WeeklyTimeSlot> workHours = schedule.getWorkHours();

            assertThatThrownBy(() -> workHours.add(sampleSlot))
                    .isInstanceOf(UnsupportedOperationException.class);
        }

        @Test
        @DisplayName("Should return unmodifiable time off set")
        void getTimeOff_ReturnsUnmodifiableSet() {
            DentistSchedule schedule = DentistSchedule.create(sampleDentistId, sampleWorkHours)
                    .addTimeOff(sampleTimeOff);
            Set<TimeOffPeriod> timeOff = schedule.getTimeOff();

            assertThatThrownBy(() -> timeOff.add(sampleTimeOff))
                    .isInstanceOf(UnsupportedOperationException.class);
        }

        @Test
        @DisplayName("Should have all fields marked as final")
        void classFields_ShouldBeFinal() {
            assertThat(DentistSchedule.class.getDeclaredFields())
                    .allMatch(field -> java.lang.reflect.Modifier.isFinal(field.getModifiers()));
        }
    }
}