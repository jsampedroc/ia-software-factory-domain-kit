package com.application.application.service;

import com.application.domain.model.*;
import com.application.domain.valueobject.PatientId;
import com.application.domain.valueobject.EhrId;
import com.application.domain.enums.PatientStatus;
import com.application.domain.enums.AlertSeverity;
import com.application.domain.enums.AllergySeverity;
import com.application.domain.enums.ConsentType;
import com.application.domain.repository.PatientRepository;
import com.application.domain.repository.ElectronicHealthRecordRepository;
import com.application.domain.exception.DomainException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PatientServiceTest {

    @Mock
    private PatientRepository patientRepository;

    @Mock
    private ElectronicHealthRecordRepository ehrRepository;

    @InjectMocks
    private PatientService patientService;

    @Captor
    private ArgumentCaptor<Patient> patientCaptor;

    @Captor
    private ArgumentCaptor<ElectronicHealthRecord> ehrCaptor;

    private PatientId samplePatientId;
    private PatientIdentity sampleIdentity;
    private MedicalAlert sampleAlert;
    private Allergy sampleAllergy;
    private DigitalConsent sampleConsent;
    private Patient activePatient;

    @BeforeEach
    void setUp() {
        samplePatientId = new PatientId(UUID.randomUUID());
        sampleIdentity = PatientIdentity.builder()
                .firstName("John")
                .lastName("Doe")
                .dateOfBirth(LocalDate.of(1985, 5, 15))
                .nationalId("ID123456")
                .email("john.doe@example.com")
                .phoneNumber("+1234567890")
                .address("123 Main St")
                .build();

        sampleAlert = MedicalAlert.builder()
                .alertId(UUID.randomUUID())
                .code("HC001")
                .description("Heart Condition")
                .severity(AlertSeverity.HIGH)
                .createdAt(LocalDateTime.now())
                .isActive(true)
                .build();

        sampleAllergy = Allergy.builder()
                .allergyId(UUID.randomUUID())
                .substance("Penicillin")
                .reaction("Rash")
                .severity(AllergySeverity.SEVERE)
                .diagnosedDate(LocalDate.now())
                .build();

        sampleConsent = DigitalConsent.builder()
                .consentId(UUID.randomUUID())
                .consentType(ConsentType.TREATMENT)
                .version("1.0")
                .content("I consent to standard dental treatment.")
                .givenBy("John Doe")
                .givenAt(LocalDateTime.now())
                .isRevoked(false)
                .revokedAt(null)
                .build();

        activePatient = Patient.builder()
                .patientId(samplePatientId)
                .identity(sampleIdentity)
                .medicalAlerts(new HashSet<>())
                .allergies(new HashSet<>())
                .consents(new ArrayList<>())
                .status(PatientStatus.ACTIVE)
                .build();
    }

    @Test
    void registerPatient_SuccessfulRegistration_CreatesPatientAndEHR() {
        // Given
        Set<MedicalAlert> alerts = Set.of(sampleAlert);
        Set<Allergy> allergies = Set.of(sampleAllergy);
        List<DigitalConsent> consents = List.of(sampleConsent);

        when(patientRepository.findByIdentity(sampleIdentity)).thenReturn(Optional.empty());
        when(patientRepository.save(any(Patient.class))).thenAnswer(invocation -> invocation.getArgument(0));
        when(ehrRepository.save(any(ElectronicHealthRecord.class))).thenAnswer(invocation -> invocation.getArgument(0));

        // When
        Patient result = patientService.registerPatient(sampleIdentity, alerts, allergies, consents);

        // Then
        verify(patientRepository).findByIdentity(sampleIdentity);
        verify(patientRepository).save(patientCaptor.capture());
        verify(ehrRepository).save(ehrCaptor.capture());

        Patient savedPatient = patientCaptor.getValue();
        assertThat(savedPatient.getIdentity()).isEqualTo(sampleIdentity);
        assertThat(savedPatient.getMedicalAlerts()).containsExactly(sampleAlert);
        assertThat(savedPatient.getAllergies()).containsExactly(sampleAllergy);
        assertThat(savedPatient.getConsents()).containsExactly(sampleConsent);
        assertThat(savedPatient.getStatus()).isEqualTo(PatientStatus.ACTIVE);

        ElectronicHealthRecord savedEhr = ehrCaptor.getValue();
        assertThat(savedEhr.getPatientId()).isEqualTo(savedPatient.getPatientId().getValue());
        assertThat(savedEhr.getClinicalNotes()).isEmpty();
        assertThat(savedEhr.getTreatments()).isEmpty();
        assertThat(savedEhr.getCreatedAt()).isNotNull();
        assertThat(savedEhr.getLastUpdated()).isNotNull();

        assertThat(result).isSameAs(savedPatient);
    }

    @Test
    void registerPatient_DuplicateIdentity_ThrowsDomainException() {
        // Given
        when(patientRepository.findByIdentity(sampleIdentity)).thenReturn(Optional.of(activePatient));

        // When & Then
        assertThatThrownBy(() -> patientService.registerPatient(sampleIdentity, null, null, null))
                .isInstanceOf(DomainException.class)
                .hasMessageContaining("already exists");

        verify(patientRepository, never()).save(any());
        verify(ehrRepository, never()).save(any());
    }

    @Test
    void registerPatient_NullCollections_InitializesEmptyCollections() {
        // Given
        when(patientRepository.findByIdentity(sampleIdentity)).thenReturn(Optional.empty());
        when(patientRepository.save(any(Patient.class))).thenAnswer(invocation -> invocation.getArgument(0));
        when(ehrRepository.save(any(ElectronicHealthRecord.class))).thenAnswer(invocation -> invocation.getArgument(0));

        // When
        Patient result = patientService.registerPatient(sampleIdentity, null, null, null);

        // Then
        verify(patientRepository).save(patientCaptor.capture());
        Patient savedPatient = patientCaptor.getValue();
        assertThat(savedPatient.getMedicalAlerts()).isEmpty();
        assertThat(savedPatient.getAllergies()).isEmpty();
        assertThat(savedPatient.getConsents()).isEmpty();
        assertThat(result).isSameAs(savedPatient);
    }

    @Test
    void updatePatientIdentity_SuccessfulUpdate() {
        // Given
        PatientIdentity newIdentity = PatientIdentity.builder()
                .firstName("Jane")
                .lastName("Doe")
                .dateOfBirth(LocalDate.of(1990, 1, 1))
                .nationalId("ID654321")
                .email("jane.doe@example.com")
                .phoneNumber("+0987654321")
                .address("456 Oak Ave")
                .build();

        when(patientRepository.findById(samplePatientId)).thenReturn(Optional.of(activePatient));
        when(patientRepository.findByIdentity(newIdentity)).thenReturn(Optional.empty());
        when(patientRepository.save(any(Patient.class))).thenAnswer(invocation -> invocation.getArgument(0));

        // When
        Patient result = patientService.updatePatientIdentity(samplePatientId, newIdentity);

        // Then
        verify(patientRepository).save(patientCaptor.capture());
        Patient updatedPatient = patientCaptor.getValue();
        assertThat(updatedPatient.getIdentity()).isEqualTo(newIdentity);
        assertThat(result).isSameAs(updatedPatient);
    }

    @Test
    void updatePatientIdentity_IdentityConflictWithOtherPatient_ThrowsDomainException() {
        // Given
        PatientIdentity newIdentity = PatientIdentity.builder()
                .firstName("Jane")
                .lastName("Doe")
                .dateOfBirth(LocalDate.of(1990, 1, 1))
                .nationalId("ID654321")
                .email("jane.doe@example.com")
                .phoneNumber("+0987654321")
                .address("456 Oak Ave")
                .build();

        PatientId otherPatientId = new PatientId(UUID.randomUUID());
        Patient otherPatient = Patient.builder()
                .patientId(otherPatientId)
                .identity(newIdentity)
                .status(PatientStatus.ACTIVE)
                .build();

        when(patientRepository.findById(samplePatientId)).thenReturn(Optional.of(activePatient));
        when(patientRepository.findByIdentity(newIdentity)).thenReturn(Optional.of(otherPatient));

        // When & Then
        assertThatThrownBy(() -> patientService.updatePatientIdentity(samplePatientId, newIdentity))
                .isInstanceOf(DomainException.class)
                .hasMessageContaining("Another patient already exists");

        verify(patientRepository, never()).save(any());
    }

    @Test
    void updatePatientIdentity_PatientNotFound_ThrowsDomainException() {
        // Given
        when(patientRepository.findById(samplePatientId)).thenReturn(Optional.empty());

        // When & Then
        assertThatThrownBy(() -> patientService.updatePatientIdentity(samplePatientId, sampleIdentity))
                .isInstanceOf(DomainException.class)
                .hasMessageContaining("Patient not found");
    }

    @Test
    void updatePatientIdentity_PatientNotActive_ThrowsDomainException() {
        // Given
        Patient archivedPatient = Patient.builder()
                .patientId(samplePatientId)
                .identity(sampleIdentity)
                .status(PatientStatus.ARCHIVED)
                .build();

        when(patientRepository.findById(samplePatientId)).thenReturn(Optional.of(archivedPatient));

        // When & Then
        assertThatThrownBy(() -> patientService.updatePatientIdentity(samplePatientId, sampleIdentity))
                .isInstanceOf(DomainException.class)
                .hasMessageContaining("Patient is not active");
    }

    @Test
    void addMedicalAlert_SuccessfulAddition() {
        // Given
        when(patientRepository.findById(samplePatientId)).thenReturn(Optional.of(activePatient));
        when(patientRepository.save(any(Patient.class))).thenAnswer(invocation -> invocation.getArgument(0));

        // When
        Patient result = patientService.addMedicalAlert(samplePatientId, sampleAlert);

        // Then
        verify(patientRepository).save(patientCaptor.capture());
        Patient updatedPatient = patientCaptor.getValue();
        assertThat(updatedPatient.getMedicalAlerts()).contains(sampleAlert);
        assertThat(result).isSameAs(updatedPatient);
    }

    @Test
    void deactivateMedicalAlert_SuccessfulDeactivation() {
        // Given
        activePatient = Patient.builder()
                .patientId(samplePatientId)
                .identity(sampleIdentity)
                .medicalAlerts(new HashSet<>(Set.of(sampleAlert)))
                .status(PatientStatus.ACTIVE)
                .build();

        when(patientRepository.findById(samplePatientId)).thenReturn(Optional.of(activePatient));
        when(patientRepository.save(any(Patient.class))).thenAnswer(invocation -> invocation.getArgument(0));

        // When
        Patient result = patientService.deactivateMedicalAlert(samplePatientId, sampleAlert.getAlertId());

        // Then
        verify(patientRepository).save(patientCaptor.capture());
        Patient updatedPatient = patientCaptor.getValue();
        Optional<MedicalAlert> deactivatedAlert = updatedPatient.getMedicalAlerts().stream()
                .filter(a -> a.getAlertId().equals(sampleAlert.getAlertId()))
                .findFirst();
        assertThat(deactivatedAlert).isPresent();
        assertThat(deactivatedAlert.get().isActive()).isFalse();
        assertThat(result).isSameAs(updatedPatient);
    }

    @Test
    void addAllergy_SuccessfulAddition() {
        // Given
        when(patientRepository.findById(samplePatientId)).thenReturn(Optional.of(activePatient));
        when(patientRepository.save(any(Patient.class))).thenAnswer(invocation -> invocation.getArgument(0));

        // When
        Patient result = patientService.addAllergy(samplePatientId, sampleAllergy);

        // Then
        verify(patientRepository).save(patientCaptor.capture());
        Patient updatedPatient = patientCaptor.getValue();
        assertThat(updatedPatient.getAllergies()).contains(sampleAllergy);
        assertThat(result).isSameAs(updatedPatient);
    }

    @Test
    void removeAllergy_SuccessfulRemoval() {
        // Given
        activePatient = Patient.builder()
                .patientId(samplePatientId)
                .identity(sampleIdentity)
                .allergies(new HashSet<>(Set.of(sampleAllergy)))
                .status(PatientStatus.ACTIVE)
                .build();

        when(patientRepository.findById(samplePatientId)).thenReturn(Optional.of(activePatient));
        when(patientRepository.save(any(Patient.class))).thenAnswer(invocation -> invocation.getArgument(0));

        // When
        Patient result = patientService.removeAllergy(samplePatientId, sampleAllergy.getAllergyId());

        // Then
        verify(patientRepository).save(patientCaptor.capture());
        Patient updatedPatient = patientCaptor.getValue();
        assertThat(updatedPatient.getAllergies()).doesNotContain(sampleAllergy);
        assertThat(result).isSameAs(updatedPatient);
    }

    @Test
    void giveConsent_SuccessfulConsentCreation() {
        // Given
        when(patientRepository.findById(samplePatientId)).thenReturn(Optional.of(activePatient));
        when(patientRepository.save(any(Patient.class))).thenAnswer(invocation -> invocation.getArgument(0));

        // When
        DigitalConsent result = patientService.giveConsent(samplePatientId, ConsentType.DATA_USAGE, "2.0", "Data usage consent", "John Doe");

        // Then
        verify(patientRepository).save(patientCaptor.capture());
        Patient updatedPatient = patientCaptor.getValue();
        assertThat(updatedPatient.getConsents()).hasSize(1);
        DigitalConsent savedConsent = updatedPatient.getConsents().get(0);
        assertThat(savedConsent.getConsentType()).isEqualTo(ConsentType.DATA_USAGE);
        assertThat(savedConsent.getVersion()).isEqualTo("2.0");
        assertThat(savedConsent.getContent()).isEqualTo("Data usage consent");
        assertThat(savedConsent.getGivenBy()).isEqualTo("John Doe");
        assertThat(savedConsent.isRevoked()).isFalse();
        assertThat(result).isSameAs(savedConsent);
    }

    @Test
    void revokeConsent_SuccessfulRevocation() {
        // Given
        activePatient = Patient.builder()
                .patientId(samplePatientId)
                .identity(sampleIdentity)
                .consents(new ArrayList<>(List.of(sampleConsent)))
                .status(PatientStatus.ACTIVE)
                .build();

        when(patientRepository.findById(samplePatientId)).thenReturn(Optional.of(activePatient));
        when(patientRepository.save(any(Patient.class))).thenAnswer(invocation -> invocation.getArgument(0));

        // When
        Patient result = patientService.revokeConsent(samplePatientId, sampleConsent.getConsentId());

        // Then
        verify(patientRepository).save(patientCaptor.capture());
        Patient updatedPatient = patientCaptor.getValue();
        DigitalConsent revokedConsent = updatedPatient.getConsents().get(0);
        assertThat(revokedConsent.isRevoked()).isTrue();
        assertThat(revokedConsent.getRevokedAt()).isNotNull();
        assertThat(result).isSameAs(updatedPatient);
    }

    @Test
    void archivePatient_SuccessfulArchival() {
        // Given
        when(patientRepository.findById(samplePatientId)).thenReturn(Optional.of(activePatient));
        when(patientRepository.save(any(Patient.class))).thenAnswer(invocation -> invocation.getArgument(0));

        // When
        Patient result = patientService.archivePatient(samplePatientId);

        // Then
        verify(patientRepository).save(patientCaptor.capture());
        Patient archivedPatient = patientCaptor.getValue();
        assertThat(archivedPatient.getStatus()).isEqualTo(PatientStatus.ARCHIVED);
        assertThat(result).isSameAs(archivedPatient);
    }

    @Test
    void archivePatient_AlreadyArchived_ThrowsDomainException() {
        // Given
        Patient archivedPatient = Patient.builder()
                .patientId(samplePatientId)
                .identity(sampleIdentity)
                .status(PatientStatus.ARCHIVED)
                .build();

        when(patientRepository.findById(samplePatientId)).thenReturn(Optional.of(archivedPatient));

        // When & Then
        assertThatThrownBy(() -> patientService.archivePatient(samplePatientId))
                .isInstanceOf(DomainException.class)
                .hasMessageContaining("already archived");
        verify(patientRepository, never()).save(any());
    }

    @Test
    void reactivatePatient_FromArchived_Successful() {
        // Given
        Patient archivedPatient = Patient.builder()
                .patientId(samplePatientId)
                .identity(sampleIdentity)
                .status(PatientStatus.ARCHIVED)
                .build();

        when(patientRepository.findById(samplePatientId)).thenReturn(Optional.of(archivedPatient));
        when(patientRepository.save(any(Patient.class))).thenAnswer(invocation -> invocation.getArgument(0));

        // When
        Patient result = patientService.reactivatePatient(samplePatientId);

        // Then
        verify(patientRepository).save(patientCaptor.capture());
        Patient reactivatedPatient = patientCaptor.getValue();
        assertThat(reactivatedPatient.getStatus()).isEqualTo(PatientStatus.ACTIVE);
        assertThat(result).isSameAs(reactivatedPatient);
    }

    @Test
    void reactivatePatient_FromInactive_Successful() {
        // Given
        Patient inactivePatient = Patient.builder()
                .patientId(samplePatientId)
                .identity(sampleIdentity)
                .status(PatientStatus.INACTIVE)
                .build();

        when(patientRepository.findById(samplePatientId)).thenReturn(Optional.of(inactivePatient));
        when(patientRepository.save