package com.application.domain.model;

import com.application.domain.valueobject.AppointmentId;
import com.application.domain.enums.AppointmentType;
import com.application.domain.enums.AppointmentStatus;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@ExtendWith(MockitoExtension.class)
class AppointmentTest {

    private final AppointmentId APPOINTMENT_ID = new AppointmentId(UUID.randomUUID());
    private final UUID PATIENT_ID = UUID.randomUUID();
    private final UUID DENTIST_ID = UUID.randomUUID();
    private final LocalDateTime BASE_TIME = LocalDateTime.of(2024, 1, 15, 10, 0);
    private final Duration ONE_HOUR = Duration.ofHours(1);
    private final String REASON = "Routine check-up";

    private Appointment createScheduledAppointment(AppointmentType type) {
        return new Appointment(
                APPOINTMENT_ID,
                PATIENT_ID,
                DENTIST_ID,
                BASE_TIME,
                ONE_HOUR,
                type,
                AppointmentStatus.SCHEDULED,
                REASON,
                BASE_TIME.minusDays(1)
        );
    }

    @Test
    @DisplayName("Should create an Appointment with correct initial state")
    void shouldCreateAppointment() {
        Appointment appointment = createScheduledAppointment(AppointmentType.CONSULTATION);

        assertThat(appointment.getId()).isEqualTo(APPOINTMENT_ID);
        assertThat(appointment.getPatientId()).isEqualTo(PATIENT_ID);
        assertThat(appointment.getDentistId()).isEqualTo(DENTIST_ID);
        assertThat(appointment.getScheduledTime()).isEqualTo(BASE_TIME);
        assertThat(appointment.getDuration()).isEqualTo(ONE_HOUR);
        assertThat(appointment.getType()).isEqualTo(AppointmentType.CONSULTATION);
        assertThat(appointment.getStatus()).isEqualTo(AppointmentStatus.SCHEDULED);
        assertThat(appointment.getReason()).isEqualTo(REASON);
        assertThat(appointment.getCreatedAt()).isEqualTo(BASE_TIME.minusDays(1));
    }

    @Test
    @DisplayName("Should calculate end time correctly")
    void getEndTime_ShouldReturnScheduledTimePlusDuration() {
        Appointment appointment = createScheduledAppointment(AppointmentType.TREATMENT);
        LocalDateTime expectedEndTime = BASE_TIME.plus(ONE_HOUR);

        assertThat(appointment.getEndTime()).isEqualTo(expectedEndTime);
    }

    @Nested
    @DisplayName("State Transition Tests")
    class StateTransitionTests {

        @Test
        @DisplayName("Should confirm a SCHEDULED appointment")
        void confirm_ShouldChangeStatusToConfirmed() {
            Appointment appointment = createScheduledAppointment(AppointmentType.FOLLOW_UP);

            appointment.confirm();

            assertThat(appointment.getStatus()).isEqualTo(AppointmentStatus.CONFIRMED);
        }

        @Test
        @DisplayName("Should throw when confirming a non-SCHEDULED appointment")
        void confirm_ShouldThrowWhenNotScheduled() {
            Appointment appointment = createScheduledAppointment(AppointmentType.CONSULTATION);
            appointment.confirm(); // Now CONFIRMED

            assertThatThrownBy(appointment::confirm)
                    .isInstanceOf(IllegalStateException.class)
                    .hasMessage("Only SCHEDULED appointments can be confirmed.");
        }

        @Test
        @DisplayName("Should start a SCHEDULED appointment")
        void start_ShouldChangeScheduledToInProgress() {
            Appointment appointment = createScheduledAppointment(AppointmentType.TREATMENT);

            appointment.start();

            assertThat(appointment.getStatus()).isEqualTo(AppointmentStatus.IN_PROGRESS);
        }

        @Test
        @DisplayName("Should start a CONFIRMED appointment")
        void start_ShouldChangeConfirmedToInProgress() {
            Appointment appointment = createScheduledAppointment(AppointmentType.CONSULTATION);
            appointment.confirm();

            appointment.start();

            assertThat(appointment.getStatus()).isEqualTo(AppointmentStatus.IN_PROGRESS);
        }

        @Test
        @DisplayName("Should throw when starting an appointment not SCHEDULED or CONFIRMED")
        void start_ShouldThrowWhenInvalidState() {
            Appointment appointment = createScheduledAppointment(AppointmentType.FOLLOW_UP);
            appointment.confirm();
            appointment.start();
            appointment.complete(); // Now COMPLETED

            assertThatThrownBy(appointment::start)
                    .isInstanceOf(IllegalStateException.class)
                    .hasMessage("Appointment must be SCHEDULED or CONFIRMED to start.");
        }

        @Test
        @DisplayName("Should complete an IN_PROGRESS appointment")
        void complete_ShouldChangeStatusToCompleted() {
            Appointment appointment = createScheduledAppointment(AppointmentType.TREATMENT);
            appointment.start(); // Now IN_PROGRESS

            appointment.complete();

            assertThat(appointment.getStatus()).isEqualTo(AppointmentStatus.COMPLETED);
        }

        @Test
        @DisplayName("Should throw when completing a non-IN_PROGRESS appointment")
        void complete_ShouldThrowWhenNotInProgress() {
            Appointment appointment = createScheduledAppointment(AppointmentType.CONSULTATION);

            assertThatThrownBy(appointment::complete)
                    .isInstanceOf(IllegalStateException.class)
                    .hasMessage("Only IN_PROGRESS appointments can be completed.");
        }

        @Test
        @DisplayName("Should mark a SCHEDULED appointment as NO_SHOW")
        void markAsNoShow_ShouldChangeScheduledToNoShow() {
            Appointment appointment = createScheduledAppointment(AppointmentType.FOLLOW_UP);

            appointment.markAsNoShow();

            assertThat(appointment.getStatus()).isEqualTo(AppointmentStatus.NO_SHOW);
        }

        @Test
        @DisplayName("Should mark a CONFIRMED appointment as NO_SHOW")
        void markAsNoShow_ShouldChangeConfirmedToNoShow() {
            Appointment appointment = createScheduledAppointment(AppointmentType.CONSULTATION);
            appointment.confirm();

            appointment.markAsNoShow();

            assertThat(appointment.getStatus()).isEqualTo(AppointmentStatus.NO_SHOW);
        }

        @Test
        @DisplayName("Should throw when marking a non-SCHEDULED/CONFIRMED appointment as NO_SHOW")
        void markAsNoShow_ShouldThrowWhenInvalidState() {
            Appointment appointment = createScheduledAppointment(AppointmentType.TREATMENT);
            appointment.start(); // Now IN_PROGRESS

            assertThatThrownBy(appointment::markAsNoShow)
                    .isInstanceOf(IllegalStateException.class)
                    .hasMessage("Only SCHEDULED or CONFIRMED appointments can be marked as NO_SHOW.");
        }
    }

    @Nested
    @DisplayName("Cancellation Tests")
    class CancellationTests {

        @Test
        @DisplayName("Should cancel a SCHEDULED non-emergency appointment >24h in advance")
        void cancel_ShouldAllowCancellationMoreThan24hBefore() {
            Appointment appointment = createScheduledAppointment(AppointmentType.CONSULTATION);
            LocalDateTime cancellationTime = BASE_TIME.minusHours(25); // 25 hours before

            appointment.cancel(cancellationTime);

            assertThat(appointment.getStatus()).isEqualTo(AppointmentStatus.CANCELLED);
        }

        @Test
        @DisplayName("Should cancel an EMERGENCY appointment even within 24h")
        void cancel_ShouldAllowEmergencyCancellationWithin24h() {
            Appointment appointment = new Appointment(
                    APPOINTMENT_ID,
                    PATIENT_ID,
                    DENTIST_ID,
                    BASE_TIME,
                    ONE_HOUR,
                    AppointmentType.EMERGENCY,
                    AppointmentStatus.SCHEDULED,
                    "Toothache",
                    BASE_TIME.minusHours(1)
            );
            LocalDateTime cancellationTime = BASE_TIME.minusMinutes(30); // 30 minutes before

            appointment.cancel(cancellationTime);

            assertThat(appointment.getStatus()).isEqualTo(AppointmentStatus.CANCELLED);
        }

        @Test
        @DisplayName("Should throw when cancelling a non-emergency appointment <24h in advance")
        void cancel_ShouldThrowWhenLessThan24hForNonEmergency() {
            Appointment appointment = createScheduledAppointment(AppointmentType.TREATMENT);
            LocalDateTime cancellationTime = BASE_TIME.minusHours(23); // 23 hours before

            assertThatThrownBy(() -> appointment.cancel(cancellationTime))
                    .isInstanceOf(IllegalStateException.class)
                    .hasMessage("Non-emergency appointments must be cancelled at least 24 hours in advance.");
        }

        @Test
        @DisplayName("Should throw when cancelling a COMPLETED appointment")
        void cancel_ShouldThrowWhenAppointmentCompleted() {
            Appointment appointment = createScheduledAppointment(AppointmentType.FOLLOW_UP);
            appointment.start();
            appointment.complete(); // Now COMPLETED

            assertThatThrownBy(() -> appointment.cancel(BASE_TIME.minusDays(1)))
                    .isInstanceOf(IllegalStateException.class)
                    .hasMessage("Cannot cancel a COMPLETED, CANCELLED, or NO_SHOW appointment.");
        }

        @Test
        @DisplayName("Should throw when cancelling an already CANCELLED appointment")
        void cancel_ShouldThrowWhenAppointmentAlreadyCancelled() {
            Appointment appointment = createScheduledAppointment(AppointmentType.CONSULTATION);
            appointment.cancel(BASE_TIME.minusDays(1)); // Now CANCELLED

            assertThatThrownBy(() -> appointment.cancel(BASE_TIME.minusDays(2)))
                    .isInstanceOf(IllegalStateException.class)
                    .hasMessage("Cannot cancel a COMPLETED, CANCELLED, or NO_SHOW appointment.");
        }

        @Test
        @DisplayName("Should throw when cancelling a NO_SHOW appointment")
        void cancel_ShouldThrowWhenAppointmentNoShow() {
            Appointment appointment = createScheduledAppointment(AppointmentType.TREATMENT);
            appointment.markAsNoShow(); // Now NO_SHOW

            assertThatThrownBy(() -> appointment.cancel(BASE_TIME.minusDays(1)))
                    .isInstanceOf(IllegalStateException.class)
                    .hasMessage("Cannot cancel a COMPLETED, CANCELLED, or NO_SHOW appointment.");
        }
    }

    @Nested
    @DisplayName("Overlap Detection Tests")
    class OverlapDetectionTests {

        @Test
        @DisplayName("Should detect overlapping appointments (this starts during other)")
        void isOverlapping_ShouldReturnTrueWhenThisStartsDuringOther() {
            Appointment appointment1 = new Appointment(
                    new AppointmentId(UUID.randomUUID()),
                    PATIENT_ID,
                    DENTIST_ID,
                    BASE_TIME,
                    Duration.ofHours(2),
                    AppointmentType.CONSULTATION,
                    AppointmentStatus.SCHEDULED,
                    REASON,
                    BASE_TIME.minusDays(1)
            );

            Appointment appointment2 = new Appointment(
                    new AppointmentId(UUID.randomUUID()),
                    UUID.randomUUID(),
                    DENTIST_ID,
                    BASE_TIME.plusHours(1),
                    Duration.ofHours(1),
                    AppointmentType.TREATMENT,
                    AppointmentStatus.SCHEDULED,
                    "Filling",
                    BASE_TIME.minusDays(1)
            );

            assertThat(appointment1.isOverlapping(appointment2)).isTrue();
            assertThat(appointment2.isOverlapping(appointment1)).isTrue();
        }

        @Test
        @DisplayName("Should detect overlapping appointments (other starts during this)")
        void isOverlapping_ShouldReturnTrueWhenOtherStartsDuringThis() {
            Appointment appointment1 = new Appointment(
                    new AppointmentId(UUID.randomUUID()),
                    PATIENT_ID,
                    DENTIST_ID,
                    BASE_TIME.plusHours(1),
                    Duration.ofHours(1),
                    AppointmentType.CONSULTATION,
                    AppointmentStatus.SCHEDULED,
                    REASON,
                    BASE_TIME.minusDays(1)
            );

            Appointment appointment2 = new Appointment(
                    new AppointmentId(UUID.randomUUID()),
                    UUID.randomUUID(),
                    DENTIST_ID,
                    BASE_TIME,
                    Duration.ofHours(2),
                    AppointmentType.TREATMENT,
                    AppointmentStatus.SCHEDULED,
                    "Filling",
                    BASE_TIME.minusDays(1)
            );

            assertThat(appointment1.isOverlapping(appointment2)).isTrue();
            assertThat(appointment2.isOverlapping(appointment1)).isTrue();
        }

        @Test
        @DisplayName("Should detect overlapping appointments (exact same time)")
        void isOverlapping_ShouldReturnTrueWhenExactSameTime() {
            Appointment appointment1 = createScheduledAppointment(AppointmentType.CONSULTATION);
            Appointment appointment2 = new Appointment(
                    new AppointmentId(UUID.randomUUID()),
                    UUID.randomUUID(),
                    DENTIST_ID,
                    BASE_TIME,
                    ONE_HOUR,
                    AppointmentType.TREATMENT,
                    AppointmentStatus.SCHEDULED,
                    "Filling",
                    BASE_TIME.minusDays(1)
            );

            assertThat(appointment1.isOverlapping(appointment2)).isTrue();
            assertThat(appointment2.isOverlapping(appointment1)).isTrue();
        }

        @Test
        @DisplayName("Should not detect overlap when appointments are back-to-back")
        void isOverlapping_ShouldReturnFalseWhenBackToBack() {
            Appointment appointment1 = createScheduledAppointment(AppointmentType.CONSULTATION);

            Appointment appointment2 = new Appointment(
                    new AppointmentId(UUID.randomUUID()),
                    UUID.randomUUID(),
                    DENTIST_ID,
                    BASE_TIME.plusHours(1),
                    ONE_HOUR,
                    AppointmentType.TREATMENT,
                    AppointmentStatus.SCHEDULED,
                    "Filling",
                    BASE_TIME.minusDays(1)
            );

            assertThat(appointment1.isOverlapping(appointment2)).isFalse();
            assertThat(appointment2.isOverlapping(appointment1)).isFalse();
        }

        @Test
        @DisplayName("Should not detect overlap when appointments are separate")
        void isOverlapping_ShouldReturnFalseWhenSeparate() {
            Appointment appointment1 = createScheduledAppointment(AppointmentType.CONSULTATION);

            Appointment appointment2 = new Appointment(
                    new AppointmentId(UUID.randomUUID()),
                    UUID.randomUUID(),
                    DENTIST_ID,
                    BASE_TIME.plusHours(3),
                    ONE_HOUR,
                    AppointmentType.TREATMENT,
                    AppointmentStatus.SCHEDULED,
                    "Filling",
                    BASE_TIME.minusDays(1)
            );

            assertThat(appointment1.isOverlapping(appointment2)).isFalse();
            assertThat(appointment2.isOverlapping(appointment1)).isFalse();
        }
    }

    @Test
    @DisplayName("Should use Lombok @SuperBuilder correctly")
    void shouldUseSuperBuilder() {
        Appointment original = createScheduledAppointment(AppointmentType.CONSULTATION);

        Appointment modified = original.toBuilder()
                .status(AppointmentStatus.CONFIRMED)
                .reason("Updated reason")
                .build();

        assertThat(modified.getId()).isEqualTo(original.getId());
        assertThat(modified.getPatientId()).isEqualTo(original.getPatientId());
        assertThat(modified.getDentistId()).isEqualTo(original.getDentistId());
        assertThat(modified.getScheduledTime()).isEqualTo(original.getScheduledTime());
        assertThat(modified.getDuration()).isEqualTo(original.getDuration());
        assertThat(modified.getType()).isEqualTo(original.getType());
        assertThat(modified.getStatus()).isEqualTo(AppointmentStatus.CONFIRMED);
        assertThat(modified.getReason()).isEqualTo("Updated reason");
        assertThat(modified.getCreatedAt()).isEqualTo(original.getCreatedAt());
    }

    @Test
    @DisplayName("Should use Lombok @ToString correctly")
    void toString_ShouldIncludeSuperClassAndFields() {
        Appointment appointment = createScheduledAppointment(AppointmentType.TREATMENT);
        String toString = appointment.toString();

        assertThat(toString).contains("Appointment");
        assertThat(toString).contains("patientId=" + PATIENT_ID);
        assertThat(toString).contains("dentistId=" + DENTIST_ID);
        assertThat(toString).contains("status=SCHEDULED");
        assertThat(toString).contains("type=TREATMENT");
    }
}