package com.application.domain.model;

import com.application.domain.enums.PatientStatus;
import com.application.domain.valueobject.PatientId;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@ExtendWith(MockitoExtension.class)
class PatientTest {

    private PatientId patientId;
    private PatientIdentity validIdentity;
    private LocalDateTime fixedTimestamp;

    @BeforeEach
    void setUp() {
        patientId = new PatientId(UUID.randomUUID());
        fixedTimestamp = LocalDateTime.of(2024, 1, 15, 10, 0);
        validIdentity = PatientIdentity.builder()
                .firstName("John")
                .lastName("Doe")
                .dateOfBirth(LocalDate.of(1985, 5, 20))
                .nationalId("ID123456")
                .email("john.doe@example.com")
                .phoneNumber("+1234567890")
                .address("123 Main St")
                .build();
    }

    @Nested
    @DisplayName("Patient Creation and Instantiation")
    class CreationTests {

        @Test
        @DisplayName("Should create a Patient with required fields using builder")
        void shouldCreatePatientWithBuilder() {
            Patient patient = Patient.builder()
                    .id(patientId)
                    .identity(validIdentity)
                    .status(PatientStatus.ACTIVE)
                    .build();

            assertThat(patient).isNotNull();
            assertThat(patient.getId()).isEqualTo(patientId);
            assertThat(patient.getIdentity()).isEqualTo(validIdentity);
            assertThat(patient.getStatus()).isEqualTo(PatientStatus.ACTIVE);
            assertThat(patient.getMedicalAlerts()).isEmpty();
            assertThat(patient.getAllergies()).isEmpty();
            assertThat(patient.getConsents()).isEmpty();
            assertThat(patient.getCreatedAt()).isNotNull();
            assertThat(patient.getUpdatedAt()).isNotNull();
        }

        @Test
        @DisplayName("Should create a Patient with all collections populated")
        void shouldCreatePatientWithCollections() {
            MedicalAlert alert = MedicalAlert.builder()
                    .alertId(UUID.randomUUID())
                    .code("H001")
                    .description("Heart Condition")
                    .severity(com.application.domain.enums.AlertSeverity.HIGH)
                    .createdAt(fixedTimestamp)
                    .isActive(true)
                    .build();

            Allergy allergy = Allergy.builder()
                    .allergyId(UUID.randomUUID())
                    .substance("Penicillin")
                    .reaction("Rash")
                    .severity(com.application.domain.enums.AllergySeverity.MODERATE)
                    .diagnosedDate(LocalDate.of(2020, 3, 10))
                    .build();

            DigitalConsent consent = DigitalConsent.builder()
                    .consentId(UUID.randomUUID())
                    .consentType(com.application.domain.enums.ConsentType.TREATMENT)
                    .version("1.0")
                    .content("I consent to treatment")
                    .givenBy("John Doe")
                    .givenAt(fixedTimestamp)
                    .isRevoked(false)
                    .build();

            Set<MedicalAlert> alerts = new HashSet<>(List.of(alert));
            Set<Allergy> allergies = new HashSet<>(List.of(allergy));
            List<DigitalConsent> consents = List.of(consent);

            Patient patient = Patient.builder()
                    .id(patientId)
                    .identity(validIdentity)
                    .status(PatientStatus.ACTIVE)
                    .medicalAlerts(alerts)
                    .allergies(allergies)
                    .consents(consents)
                    .createdAt(fixedTimestamp)
                    .updatedAt(fixedTimestamp)
                    .build();

            assertThat(patient.getMedicalAlerts()).hasSize(1).contains(alert);
            assertThat(patient.getAllergies()).hasSize(1).contains(allergy);
            assertThat(patient.getConsents()).hasSize(1).contains(consent);
            assertThat(patient.getCreatedAt()).isEqualTo(fixedTimestamp);
            assertThat(patient.getUpdatedAt()).isEqualTo(fixedTimestamp);
        }

        @Test
        @DisplayName("Should throw IllegalArgumentException when identity is null")
        void shouldThrowExceptionWhenIdentityIsNull() {
            Patient.PatientBuilder builder = Patient.builder()
                    .id(patientId)
                    .status(PatientStatus.ACTIVE);

            assertThatThrownBy(builder::build)
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessageContaining("Patient identity cannot be null");
        }

        @Test
        @DisplayName("Should throw IllegalArgumentException when status is null")
        void shouldThrowExceptionWhenStatusIsNull() {
            Patient.PatientBuilder builder = Patient.builder()
                    .id(patientId)
                    .identity(validIdentity);

            assertThatThrownBy(builder::build)
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessageContaining("Patient status cannot be null");
        }

        @Test
        @DisplayName("Should initialize empty collections when null collections are provided")
        void shouldInitializeEmptyCollectionsForNullInputs() {
            Patient patient = Patient.builder()
                    .id(patientId)
                    .identity(validIdentity)
                    .status(PatientStatus.ACTIVE)
                    .medicalAlerts(null)
                    .allergies(null)
                    .consents(null)
                    .build();

            assertThat(patient.getMedicalAlerts()).isEmpty();
            assertThat(patient.getAllergies()).isEmpty();
            assertThat(patient.getConsents()).isEmpty();
        }
    }

    @Nested
    @DisplayName("Medical Alert Management")
    class MedicalAlertTests {

        private Patient patient;
        private MedicalAlert alert;

        @BeforeEach
        void setUp() {
            patient = Patient.builder()
                    .id(patientId)
                    .identity(validIdentity)
                    .status(PatientStatus.ACTIVE)
                    .build();

            alert = MedicalAlert.builder()
                    .alertId(UUID.randomUUID())
                    .code("P001")
                    .description("Pregnancy")
                    .severity(com.application.domain.enums.AlertSeverity.HIGH)
                    .createdAt(fixedTimestamp)
                    .isActive(true)
                    .build();
        }

        @Test
        @DisplayName("Should add a medical alert and update timestamp")
        void shouldAddMedicalAlert() {
            LocalDateTime beforeUpdate = patient.getUpdatedAt();

            patient.addMedicalAlert(alert);

            assertThat(patient.getMedicalAlerts()).hasSize(1).contains(alert);
            assertThat(patient.getUpdatedAt()).isAfter(beforeUpdate);
        }

        @Test
        @DisplayName("Should throw IllegalArgumentException when adding null medical alert")
        void shouldThrowExceptionWhenAddingNullAlert() {
            assertThatThrownBy(() -> patient.addMedicalAlert(null))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessageContaining("Medical alert cannot be null");
        }

        @Test
        @DisplayName("Should remove a medical alert and update timestamp")
        void shouldRemoveMedicalAlert() {
            patient.addMedicalAlert(alert);
            LocalDateTime beforeUpdate = patient.getUpdatedAt();

            patient.removeMedicalAlert(alert);

            assertThat(patient.getMedicalAlerts()).isEmpty();
            assertThat(patient.getUpdatedAt()).isAfter(beforeUpdate);
        }

        @Test
        @DisplayName("Should handle removal of non-existent medical alert gracefully")
        void shouldHandleRemovalOfNonExistentAlert() {
            MedicalAlert otherAlert = MedicalAlert.builder()
                    .alertId(UUID.randomUUID())
                    .code("D001")
                    .description("Diabetes")
                    .severity(com.application.domain.enums.AlertSeverity.MEDIUM)
                    .createdAt(fixedTimestamp)
                    .isActive(true)
                    .build();

            patient.addMedicalAlert(alert);
            int initialSize = patient.getMedicalAlerts().size();
            LocalDateTime beforeUpdate = patient.getUpdatedAt();

            patient.removeMedicalAlert(otherAlert);

            assertThat(patient.getMedicalAlerts()).hasSize(initialSize);
            assertThat(patient.getUpdatedAt()).isEqualTo(beforeUpdate);
        }

        @Test
        @DisplayName("Should not update timestamp when removing null alert")
        void shouldNotUpdateTimestampWhenRemovingNullAlert() {
            LocalDateTime beforeUpdate = patient.getUpdatedAt();

            patient.removeMedicalAlert(null);

            assertThat(patient.getUpdatedAt()).isEqualTo(beforeUpdate);
        }
    }

    @Nested
    @DisplayName("Allergy Management")
    class AllergyTests {

        private Patient patient;
        private Allergy allergy;

        @BeforeEach
        void setUp() {
            patient = Patient.builder()
                    .id(patientId)
                    .identity(validIdentity)
                    .status(PatientStatus.ACTIVE)
                    .build();

            allergy = Allergy.builder()
                    .allergyId(UUID.randomUUID())
                    .substance("Latex")
                    .reaction("Anaphylaxis")
                    .severity(com.application.domain.enums.AllergySeverity.SEVERE)
                    .diagnosedDate(LocalDate.of(2019, 7, 15))
                    .build();
        }

        @Test
        @DisplayName("Should add an allergy and update timestamp")
        void shouldAddAllergy() {
            LocalDateTime beforeUpdate = patient.getUpdatedAt();

            patient.addAllergy(allergy);

            assertThat(patient.getAllergies()).hasSize(1).contains(allergy);
            assertThat(patient.getUpdatedAt()).isAfter(beforeUpdate);
        }

        @Test
        @DisplayName("Should throw IllegalArgumentException when adding null allergy")
        void shouldThrowExceptionWhenAddingNullAllergy() {
            assertThatThrownBy(() -> patient.addAllergy(null))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessageContaining("Allergy cannot be null");
        }

        @Test
        @DisplayName("Should remove an allergy and update timestamp")
        void shouldRemoveAllergy() {
            patient.addAllergy(allergy);
            LocalDateTime beforeUpdate = patient.getUpdatedAt();

            patient.removeAllergy(allergy);

            assertThat(patient.getAllergies()).isEmpty();
            assertThat(patient.getUpdatedAt()).isAfter(beforeUpdate);
        }
    }

    @Nested
    @DisplayName("Consent Management")
    class ConsentTests {

        private Patient patient;
        private DigitalConsent consent;

        @BeforeEach
        void setUp() {
            patient = Patient.builder()
                    .id(patientId)
                    .identity(validIdentity)
                    .status(PatientStatus.ACTIVE)
                    .build();

            consent = DigitalConsent.builder()
                    .consentId(UUID.randomUUID())
                    .consentType(com.application.domain.enums.ConsentType.TREATMENT)
                    .version("2.1")
                    .content("Consent for root canal treatment")
                    .givenBy("John Doe")
                    .givenAt(fixedTimestamp)
                    .isRevoked(false)
                    .build();
        }

        @Test
        @DisplayName("Should add a consent and update timestamp")
        void shouldAddConsent() {
            LocalDateTime beforeUpdate = patient.getUpdatedAt();

            patient.addConsent(consent);

            assertThat(patient.getConsents()).hasSize(1).contains(consent);
            assertThat(patient.getUpdatedAt()).isAfter(beforeUpdate);
        }

        @Test
        @DisplayName("Should throw IllegalArgumentException when adding null consent")
        void shouldThrowExceptionWhenAddingNullConsent() {
            assertThatThrownBy(() -> patient.addConsent(null))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessageContaining("Consent cannot be null");
        }

        @Test
        @DisplayName("Should revoke a consent and update timestamp")
        void shouldRevokeConsent() {
            patient.addConsent(consent);
            LocalDateTime beforeUpdate = patient.getUpdatedAt();

            patient.revokeConsent(consent);

            assertThat(consent.isRevoked()).isTrue();
            assertThat(patient.getUpdatedAt()).isAfter(beforeUpdate);
        }

        @Test
        @DisplayName("Should not revoke consent that is not in patient's consents")
        void shouldNotRevokeConsentNotInList() {
            DigitalConsent otherConsent = DigitalConsent.builder()
                    .consentId(UUID.randomUUID())
                    .consentType(com.application.domain.enums.ConsentType.DATA_USAGE)
                    .version("1.0")
                    .content("Data usage consent")
                    .givenBy("John Doe")
                    .givenAt(fixedTimestamp)
                    .isRevoked(false)
                    .build();

            patient.addConsent(consent);
            LocalDateTime beforeUpdate = patient.getUpdatedAt();

            patient.revokeConsent(otherConsent);

            assertThat(otherConsent.isRevoked()).isFalse();
            assertThat(patient.getUpdatedAt()).isEqualTo(beforeUpdate);
        }
    }

    @Nested
    @DisplayName("Status Management")
    class StatusTests {

        private Patient patient;

        @BeforeEach
        void setUp() {
            patient = Patient.builder()
                    .id(patientId)
                    .identity(validIdentity)
                    .status(PatientStatus.ACTIVE)
                    .build();
        }

        @Test
        @DisplayName("Should archive an active patient")
        void shouldArchiveActivePatient() {
            patient.archive();

            assertThat(patient.getStatus()).isEqualTo(PatientStatus.ARCHIVED);
        }

        @Test
        @DisplayName("Should archive an inactive patient")
        void shouldArchiveInactivePatient() {
            patient.deactivate();
            patient.archive();

            assertThat(patient.getStatus()).isEqualTo(PatientStatus.ARCHIVED);
        }

        @Test
        @DisplayName("Should not update timestamp when archiving an already archived patient")
        void shouldNotUpdateWhenAlreadyArchived() {
            patient.archive();
            LocalDateTime archivedTimestamp = patient.getUpdatedAt();

            patient.archive();

            assertThat(patient.getUpdatedAt()).isEqualTo(archivedTimestamp);
        }

        @Test
        @DisplayName("Should activate an inactive patient")
        void shouldActivateInactivePatient() {
            patient.deactivate();
            patient.activate();

            assertThat(patient.getStatus()).isEqualTo(PatientStatus.ACTIVE);
        }

        @Test
        @DisplayName("Should activate an archived patient")
        void shouldActivateArchivedPatient() {
            patient.archive();
            patient.activate();

            assertThat(patient.getStatus()).isEqualTo(PatientStatus.ACTIVE);
        }

        @Test
        @DisplayName("Should not activate an already active patient")
        void shouldNotActivateActivePatient() {
            LocalDateTime beforeUpdate = patient.getUpdatedAt();
            patient.activate();

            assertThat(patient.getStatus()).isEqualTo(PatientStatus.ACTIVE);
            assertThat(patient.getUpdatedAt()).isEqualTo(beforeUpdate);
        }

        @Test
        @DisplayName("Should deactivate an active patient")
        void shouldDeactivateActivePatient() {
            patient.deactivate();

            assertThat(patient.getStatus()).isEqualTo(PatientStatus.INACTIVE);
        }

        @Test
        @DisplayName("Should not deactivate an inactive patient")
        void shouldNotDeactivateInactivePatient() {
            patient.deactivate();
            LocalDateTime beforeUpdate = patient.getUpdatedAt();
            patient.deactivate();

            assertThat(patient.getStatus()).isEqualTo(PatientStatus.INACTIVE);
            assertThat(patient.getUpdatedAt()).isEqualTo(beforeUpdate);
        }
    }

    @Nested
    @DisplayName("Business Rule Validation")
    class BusinessRuleTests {

        @Test
        @DisplayName("Should return true when patient has active high severity alerts")
        void shouldDetectActiveHighSeverityAlerts() {
            MedicalAlert highAlert = MedicalAlert.builder()
                    .alertId(UUID.randomUUID())
                    .code("H002")
                    .description("Hemophilia")
                    .severity(com.application.domain.enums.AlertSeverity.HIGH)
                    .createdAt(fixedTimestamp)
                    .isActive(true)
                    .build();

            MedicalAlert mediumAlert = MedicalAlert.builder()
                    .alertId(UUID.randomUUID())
                    .code("M001")
                    .description("Mild Asthma")
                    .severity(com.application.domain.enums.AlertSeverity.MEDIUM)
                    .createdAt(fixedTimestamp)
                    .isActive(true)
                    .build();

            Patient patient = Patient.builder()
                    .id(patientId)
                    .identity(validIdentity)
                    .status(PatientStatus.ACTIVE)
                    .medicalAlerts(Set.of(highAlert, mediumAlert))
                    .build();

            assertThat(patient.hasActiveHighSeverityAlerts()).isTrue();
        }

        @Test
        @DisplayName("Should return false when patient has no active high severity alerts")
        void shouldNotDetectHighSeverityAlertsWhenNone() {
            MedicalAlert mediumAlert = MedicalAlert.builder()
                    .alertId(UUID.randomUUID())
                    .code("M001")
                    .description("Mild Asthma")
                    .severity(com.application.domain.enums.AlertSeverity.MEDIUM)
                    .createdAt(fixedTimestamp)
                    .isActive(true)
                    .build();

            MedicalAlert lowAlert = MedicalAlert.builder()
                    .alertId(UUID.randomUUID())
                    .code("L001")
                    .description("Allergy to pollen")
                    .severity(com.application.domain.enums.AlertSeverity.LOW)
                    .createdAt(fixedTimestamp)
                    .isActive(true)
                    .build();

            Patient patient = Patient.builder()
                    .id(patientId)
                    .identity(validIdentity)
                    .status(PatientStatus.ACTIVE)
                    .medicalAlerts(Set.of(mediumAlert, lowAlert))
                    .build();

            assertThat(patient.hasActiveHighSeverityAlerts()).isFalse();
        }

        @Test
        @DisplayName("Should return false when patient has high severity alert but it's inactive")
        void shouldNotDetectInactiveHighSeverityAlerts() {
            MedicalAlert inactiveHighAlert = MedicalAlert.builder()
                    .alertId(UUID.randomUUID())
                    .code("H003")
                    .description("Previous Heart Surgery")
                    .severity(com.application.domain.enums.AlertSeverity.HIGH