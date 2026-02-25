package com.application.domain.model;

import com.application.domain.valueobject.MedicalAlertId;
import com.application.domain.enums.AlertSeverity;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@ExtendWith(MockitoExtension.class)
class MedicalAlertTest {

    private final MedicalAlertId TEST_ID = new MedicalAlertId(UUID.randomUUID());
    private final LocalDateTime TEST_CREATED_AT = LocalDateTime.of(2024, 1, 15, 10, 30);
    private final String TEST_CODE = "HEART_CONDITION";
    private final String TEST_DESCRIPTION = "Patient has a known heart condition requiring special precautions.";

    @Test
    void shouldCreateMedicalAlertSuccessfully() {
        // Given & When
        MedicalAlert alert = new MedicalAlert(
                TEST_ID,
                TEST_CODE,
                TEST_DESCRIPTION,
                AlertSeverity.HIGH,
                TEST_CREATED_AT,
                true
        );

        // Then
        assertThat(alert).isNotNull();
        assertThat(alert.alertId()).isEqualTo(TEST_ID);
        assertThat(alert.code()).isEqualTo(TEST_CODE);
        assertThat(alert.description()).isEqualTo(TEST_DESCRIPTION);
        assertThat(alert.severity()).isEqualTo(AlertSeverity.HIGH);
        assertThat(alert.createdAt()).isEqualTo(TEST_CREATED_AT);
        assertThat(alert.isActive()).isTrue();
    }

    @Test
    void shouldThrowExceptionWhenAlertIdIsNull() {
        assertThatThrownBy(() -> new MedicalAlert(
                null,
                TEST_CODE,
                TEST_DESCRIPTION,
                AlertSeverity.MEDIUM,
                TEST_CREATED_AT,
                true
        ))
                .isInstanceOf(NullPointerException.class)
                .hasMessage("Alert ID cannot be null");
    }

    @Test
    void shouldThrowExceptionWhenCodeIsNull() {
        assertThatThrownBy(() -> new MedicalAlert(
                TEST_ID,
                null,
                TEST_DESCRIPTION,
                AlertSeverity.MEDIUM,
                TEST_CREATED_AT,
                true
        ))
                .isInstanceOf(NullPointerException.class)
                .hasMessage("Code cannot be null");
    }

    @Test
    void shouldThrowExceptionWhenCodeIsBlank() {
        assertThatThrownBy(() -> new MedicalAlert(
                TEST_ID,
                "   ",
                TEST_DESCRIPTION,
                AlertSeverity.MEDIUM,
                TEST_CREATED_AT,
                true
        ))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Code cannot be blank");
    }

    @Test
    void shouldThrowExceptionWhenDescriptionIsNull() {
        assertThatThrownBy(() -> new MedicalAlert(
                TEST_ID,
                TEST_CODE,
                null,
                AlertSeverity.MEDIUM,
                TEST_CREATED_AT,
                true
        ))
                .isInstanceOf(NullPointerException.class)
                .hasMessage("Description cannot be null");
    }

    @Test
    void shouldThrowExceptionWhenDescriptionIsBlank() {
        assertThatThrownBy(() -> new MedicalAlert(
                TEST_ID,
                TEST_CODE,
                "   ",
                AlertSeverity.MEDIUM,
                TEST_CREATED_AT,
                true
        ))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Description cannot be blank");
    }

    @Test
    void shouldThrowExceptionWhenSeverityIsNull() {
        assertThatThrownBy(() -> new MedicalAlert(
                TEST_ID,
                TEST_CODE,
                TEST_DESCRIPTION,
                null,
                TEST_CREATED_AT,
                true
        ))
                .isInstanceOf(NullPointerException.class)
                .hasMessage("Severity cannot be null");
    }

    @Test
    void shouldThrowExceptionWhenCreatedAtIsNull() {
        assertThatThrownBy(() -> new MedicalAlert(
                TEST_ID,
                TEST_CODE,
                TEST_DESCRIPTION,
                AlertSeverity.LOW,
                null,
                true
        ))
                .isInstanceOf(NullPointerException.class)
                .hasMessage("Created at timestamp cannot be null");
    }

    @Test
    void shouldDeactivateAlert() {
        // Given
        MedicalAlert activeAlert = new MedicalAlert(
                TEST_ID,
                TEST_CODE,
                TEST_DESCRIPTION,
                AlertSeverity.MEDIUM,
                TEST_CREATED_AT,
                true
        );

        // When
        MedicalAlert deactivatedAlert = activeAlert.deactivate();

        // Then
        assertThat(deactivatedAlert.isActive()).isFalse();
        assertThat(deactivatedAlert.alertId()).isEqualTo(TEST_ID);
        assertThat(deactivatedAlert.code()).isEqualTo(TEST_CODE);
        assertThat(deactivatedAlert.description()).isEqualTo(TEST_DESCRIPTION);
        assertThat(deactivatedAlert.severity()).isEqualTo(AlertSeverity.MEDIUM);
        assertThat(deactivatedAlert.createdAt()).isEqualTo(TEST_CREATED_AT);
    }

    @Test
    void shouldUpdateDescriptionSuccessfully() {
        // Given
        MedicalAlert originalAlert = new MedicalAlert(
                TEST_ID,
                TEST_CODE,
                "Old description",
                AlertSeverity.LOW,
                TEST_CREATED_AT,
                true
        );
        String newDescription = "Updated description with new details.";

        // When
        MedicalAlert updatedAlert = originalAlert.updateDescription(newDescription);

        // Then
        assertThat(updatedAlert.description()).isEqualTo(newDescription);
        assertThat(updatedAlert.alertId()).isEqualTo(TEST_ID);
        assertThat(updatedAlert.code()).isEqualTo(TEST_CODE);
        assertThat(updatedAlert.severity()).isEqualTo(AlertSeverity.LOW);
        assertThat(updatedAlert.isActive()).isTrue();
        assertThat(updatedAlert.createdAt()).isAfter(TEST_CREATED_AT);
    }

    @Test
    void shouldThrowExceptionWhenUpdatingWithNullDescription() {
        // Given
        MedicalAlert alert = new MedicalAlert(
                TEST_ID,
                TEST_CODE,
                TEST_DESCRIPTION,
                AlertSeverity.HIGH,
                TEST_CREATED_AT,
                true
        );

        // When & Then
        assertThatThrownBy(() -> alert.updateDescription(null))
                .isInstanceOf(NullPointerException.class)
                .hasMessage("New description cannot be null");
    }

    @Test
    void shouldThrowExceptionWhenUpdatingWithBlankDescription() {
        // Given
        MedicalAlert alert = new MedicalAlert(
                TEST_ID,
                TEST_CODE,
                TEST_DESCRIPTION,
                AlertSeverity.HIGH,
                TEST_CREATED_AT,
                true
        );

        // When & Then
        assertThatThrownBy(() -> alert.updateDescription("   "))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("New description cannot be blank");
    }

    @Test
    void shouldIdentifyHighSeverityAlert() {
        // Given
        MedicalAlert highAlert = new MedicalAlert(
                TEST_ID,
                TEST_CODE,
                TEST_DESCRIPTION,
                AlertSeverity.HIGH,
                TEST_CREATED_AT,
                true
        );
        MedicalAlert mediumAlert = new MedicalAlert(
                new MedicalAlertId(UUID.randomUUID()),
                "DIABETES",
                "Patient has diabetes.",
                AlertSeverity.MEDIUM,
                TEST_CREATED_AT,
                true
        );
        MedicalAlert lowAlert = new MedicalAlert(
                new MedicalAlertId(UUID.randomUUID()),
                "ASTHMA",
                "Patient has mild asthma.",
                AlertSeverity.LOW,
                TEST_CREATED_AT,
                true
        );

        // Then
        assertThat(highAlert.isHighSeverity()).isTrue();
        assertThat(mediumAlert.isHighSeverity()).isFalse();
        assertThat(lowAlert.isHighSeverity()).isFalse();
    }

    @Test
    void shouldRequireAcknowledgementForActiveHighSeverityAlert() {
        // Given
        MedicalAlert activeHighAlert = new MedicalAlert(
                TEST_ID,
                TEST_CODE,
                TEST_DESCRIPTION,
                AlertSeverity.HIGH,
                TEST_CREATED_AT,
                true
        );
        MedicalAlert inactiveHighAlert = activeHighAlert.deactivate();
        MedicalAlert activeMediumAlert = new MedicalAlert(
                new MedicalAlertId(UUID.randomUUID()),
                "DIABETES",
                "Patient has diabetes.",
                AlertSeverity.MEDIUM,
                TEST_CREATED_AT,
                true
        );
        MedicalAlert inactiveLowAlert = new MedicalAlert(
                new MedicalAlertId(UUID.randomUUID()),
                "ASTHMA",
                "Patient has mild asthma.",
                AlertSeverity.LOW,
                TEST_CREATED_AT,
                false
        );

        // Then
        assertThat(activeHighAlert.requiresAcknowledgement()).isTrue();
        assertThat(inactiveHighAlert.requiresAcknowledgement()).isFalse();
        assertThat(activeMediumAlert.requiresAcknowledgement()).isFalse();
        assertThat(inactiveLowAlert.requiresAcknowledgement()).isFalse();
    }

    @Test
    void shouldImplementValueObjectEqualityBasedOnAlertId() {
        // Given
        MedicalAlertId sameId = new MedicalAlertId(UUID.fromString("123e4567-e89b-12d3-a456-426614174000"));
        MedicalAlert alert1 = new MedicalAlert(
                sameId,
                "CODE1",
                "Description 1",
                AlertSeverity.HIGH,
                TEST_CREATED_AT,
                true
        );
        MedicalAlert alert2 = new MedicalAlert(
                sameId,
                "CODE2", // Different code
                "Description 2", // Different description
                AlertSeverity.LOW, // Different severity
                TEST_CREATED_AT.plusDays(1), // Different timestamp
                false // Different active status
        );
        MedicalAlert alert3 = new MedicalAlert(
                new MedicalAlertId(UUID.randomUUID()), // Different ID
                "CODE1",
                "Description 1",
                AlertSeverity.HIGH,
                TEST_CREATED_AT,
                true
        );

        // Then
        assertThat(alert1).isEqualTo(alert2); // Same ID means equal
        assertThat(alert1).isNotEqualTo(alert3); // Different ID means not equal
        assertThat(alert1.hashCode()).isEqualTo(alert2.hashCode());
        assertThat(alert1.hashCode()).isNotEqualTo(alert3.hashCode());
    }

    @Test
    void shouldHaveConsistentToString() {
        // Given
        MedicalAlert alert = new MedicalAlert(
                TEST_ID,
                TEST_CODE,
                TEST_DESCRIPTION,
                AlertSeverity.HIGH,
                TEST_CREATED_AT,
                true
        );

        // When
        String toString = alert.toString();

        // Then
        assertThat(toString).contains(TEST_ID.toString());
        assertThat(toString).contains(TEST_CODE);
        assertThat(toString).contains("HIGH");
        assertThat(toString).contains("true");
    }
}