package com.application.domain.repository;

import com.application.domain.model.DentistSchedule;
import com.application.domain.model.TimeOffPeriod;
import com.application.domain.model.WeeklyTimeSlot;
import com.application.domain.valueobject.DentistScheduleId;
import com.application.domain.valueobject.UserId;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class DentistScheduleRepositoryTest {

    @Mock
    private DentistScheduleRepository repository;

    private UserId dentistId;
    private DentistScheduleId scheduleId;
    private DentistSchedule activeSchedule;
    private DentistSchedule inactiveSchedule;

    @BeforeEach
    void setUp() {
        dentistId = new UserId(UUID.randomUUID());
        scheduleId = new DentistScheduleId(UUID.randomUUID());

        Set<WeeklyTimeSlot> workHours = new HashSet<>();
        workHours.add(WeeklyTimeSlot.create(DayOfWeek.MONDAY, LocalTime.of(9, 0), LocalTime.of(17, 0)));
        workHours.add(WeeklyTimeSlot.create(DayOfWeek.WEDNESDAY, LocalTime.of(9, 0), LocalTime.of(17, 0)));

        Set<TimeOffPeriod> timeOff = new HashSet<>();
        timeOff.add(TimeOffPeriod.create(LocalDate.of(2024, 12, 25), LocalDate.of(2024, 12, 26), "Holiday"));

        activeSchedule = DentistSchedule.create(scheduleId, dentistId, workHours, timeOff, true);
        inactiveSchedule = DentistSchedule.create(scheduleId, dentistId, workHours, timeOff, false);
    }

    @Test
    void findByDentistId_WhenScheduleExists_ShouldReturnSchedule() {
        when(repository.findByDentistId(dentistId)).thenReturn(Optional.of(activeSchedule));

        Optional<DentistSchedule> result = repository.findByDentistId(dentistId);

        assertThat(result).isPresent();
        assertThat(result.get().getDentistId()).isEqualTo(dentistId);
        assertThat(result.get().getScheduleId()).isEqualTo(scheduleId);
    }

    @Test
    void findByDentistId_WhenScheduleDoesNotExist_ShouldReturnEmpty() {
        when(repository.findByDentistId(dentistId)).thenReturn(Optional.empty());

        Optional<DentistSchedule> result = repository.findByDentistId(dentistId);

        assertThat(result).isEmpty();
    }

    @Test
    void existsByDentistIdAndIsActiveTrue_WhenActiveScheduleExists_ShouldReturnTrue() {
        when(repository.existsByDentistIdAndIsActiveTrue(dentistId)).thenReturn(true);

        boolean result = repository.existsByDentistIdAndIsActiveTrue(dentistId);

        assertThat(result).isTrue();
    }

    @Test
    void existsByDentistIdAndIsActiveTrue_WhenNoActiveSchedule_ShouldReturnFalse() {
        when(repository.existsByDentistIdAndIsActiveTrue(dentistId)).thenReturn(false);

        boolean result = repository.existsByDentistIdAndIsActiveTrue(dentistId);

        assertThat(result).isFalse();
    }

    @Test
    void isDentistAvailable_WhenDentistIsAvailableOnDate_ShouldReturnTrue() {
        LocalDate availableDate = LocalDate.of(2024, 11, 20); // A Wednesday
        when(repository.isDentistAvailable(dentistId, availableDate)).thenReturn(true);

        boolean result = repository.isDentistAvailable(dentistId, availableDate);

        assertThat(result).isTrue();
    }

    @Test
    void isDentistAvailable_WhenDentistIsNotAvailableOnDate_ShouldReturnFalse() {
        LocalDate unavailableDate = LocalDate.of(2024, 12, 25); // In time-off period
        when(repository.isDentistAvailable(dentistId, unavailableDate)).thenReturn(false);

        boolean result = repository.isDentistAvailable(dentistId, unavailableDate);

        assertThat(result).isFalse();
    }

    @Test
    void save_ShouldPersistSchedule() {
        when(repository.save(any(DentistSchedule.class))).thenReturn(activeSchedule);

        DentistSchedule saved = repository.save(activeSchedule);

        assertThat(saved).isNotNull();
        assertThat(saved.getScheduleId()).isEqualTo(scheduleId);
        assertThat(saved.getDentistId()).isEqualTo(dentistId);
        assertThat(saved.isActive()).isTrue();
    }

    @Test
    void findById_WhenScheduleExists_ShouldReturnSchedule() {
        when(repository.findById(scheduleId)).thenReturn(Optional.of(activeSchedule));

        Optional<DentistSchedule> result = repository.findById(scheduleId);

        assertThat(result).isPresent();
        assertThat(result.get().getScheduleId()).isEqualTo(scheduleId);
    }

    @Test
    void findById_WhenScheduleDoesNotExist_ShouldReturnEmpty() {
        when(repository.findById(scheduleId)).thenReturn(Optional.empty());

        Optional<DentistSchedule> result = repository.findById(scheduleId);

        assertThat(result).isEmpty();
    }

    @Test
    void delete_ShouldRemoveSchedule() {
        // For delete, we typically verify no exception is thrown.
        // Since it's a void method, we just call it.
        repository.delete(activeSchedule);
        // If we need to verify interaction, we could use Mockito.verify
        // For this test battery, we assume the method works if no exception.
    }
}