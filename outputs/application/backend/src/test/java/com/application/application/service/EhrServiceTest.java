package com.application.application.service;

import com.application.domain.model.*;
import com.application.domain.valueobject.*;
import com.application.domain.repository.*;
import com.application.domain.exception.DomainException;
import com.application.domain.enums.UserRole;
import com.application.domain.enums.ToothStatus;
import com.application.domain.enums.PatientStatus;
import com.application.domain.enums.ConsentType;
import com.application.domain.enums.AlertSeverity;
import com.application.domain.enums.AppointmentStatus;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class EhrServiceTest {

    @Mock
    private ElectronicHealthRecordRepository ehrRepository;
    @Mock
    private PatientRepository patientRepository;
    @Mock
    private UserRepository userRepository;
    @Mock
    private AppointmentRepository appointmentRepository;

    private EhrService ehrService;

    private PatientId patientId;
    private UserId dentistId;
    private AppointmentId appointmentId;
    private Patient validPatient;
    private User dentistUser;
    private ElectronicHealthRecord ehr;
    private Appointment activeAppointment;

    @BeforeEach
    void setUp() {
        ehrService = new EhrService(ehrRepository, patientRepository, userRepository, appointmentRepository);

        patientId = new PatientId(UUID.randomUUID());
        dentistId = new UserId(UUID.randomUUID());
        appointmentId = new AppointmentId(UUID.randomUUID());

        PatientIdentity identity = PatientIdentity.builder()
                .firstName("John")
                .lastName("Doe")
                .dateOfBirth(LocalDate.of(1980, 1, 1))
                .nationalId("ID123")
                .email("john@example.com")
                .phoneNumber("1234567890")
                .address("123 Street")
                .build();

        validPatient = Patient.create(
                patientId,
                identity,
                new HashSet<>(),
                new HashSet<>(),
                List.of(DigitalConsent.builder()
                        .consentId(new ConsentId(UUID.randomUUID()))
                        .consentType(ConsentType.TREATMENT)
                        .version("1.0")
                        .content("I consent")
                        .givenBy("Patient")
                        .givenAt(LocalDateTime.now().minusDays(1))
                        .isRevoked(false)
                        .build()),
                PatientStatus.ACTIVE
        );

        dentistUser = User.builder()
                .userId(dentistId)
                .username("dentist1")
                .email("dentist@clinic.com")
                .role(UserRole.DENTIST)
                .isActive(true)
                .createdAt(LocalDateTime.now())
                .build();

        ehr = ElectronicHealthRecord.builder()
                .ehrId(new EhrId(UUID.randomUUID()))
                .patientId(patientId.value())
                .clinicalNotes(new ArrayList<>())
                .odontogram(Odontogram.builder()
                        .odontogramId(new OdontogramId(UUID.randomUUID()))
                        .toothData(new HashMap<>())
                        .lastUpdated(LocalDateTime.now())
                        .build())
                .treatments(new ArrayList<>())
                .createdAt(LocalDateTime.now())
                .lastUpdated(LocalDateTime.now())
                .build();

        activeAppointment = Appointment.builder()
                .appointmentId(appointmentId)
                .patientId(patientId.value())
                .dentistId(dentistId.value())
                .scheduledTime(LocalDateTime.now().plusHours(1))
                .duration(java.time.Duration.ofMinutes(30))
                .type(com.application.domain.enums.AppointmentType.CONSULTATION)
                .status(AppointmentStatus.CONFIRMED)
                .reason("Checkup")
                .createdAt(LocalDateTime.now())
                .build();
    }

    @Test
    void getEhrForPatient_shouldReturnEhr_whenExists() {
        when(ehrRepository.findByPatientId(patientId)).thenReturn(Optional.of(ehr));

        ElectronicHealthRecord result = ehrService.getEhrForPatient(patientId);

        assertThat(result).isEqualTo(ehr);
        verify(ehrRepository).findByPatientId(patientId);
    }

    @Test
    void getEhrForPatient_shouldThrowDomainException_whenNotFound() {
        when(ehrRepository.findByPatientId(patientId)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> ehrService.getEhrForPatient(patientId))
                .isInstanceOf(DomainException.class)
                .hasMessageContaining("EHR not found for patient ID");
    }

    @Test
    void getEhrForPatient_shouldThrowNullPointerException_whenPatientIdIsNull() {
        assertThatThrownBy(() -> ehrService.getEhrForPatient(null))
                .isInstanceOf(NullPointerException.class)
                .hasMessageContaining("Patient ID cannot be null");
    }

    @Test
    void addClinicalNote_shouldSuccessfullyAddNote() {
        when(userRepository.findById(dentistId)).thenReturn(Optional.of(dentistUser));
        when(patientRepository.findById(patientId)).thenReturn(Optional.of(validPatient));
        when(appointmentRepository.findById(appointmentId)).thenReturn(Optional.of(activeAppointment));
        when(ehrRepository.findByPatientId(patientId)).thenReturn(Optional.of(ehr));
        when(ehrRepository.save(any(ElectronicHealthRecord.class))).thenAnswer(inv -> inv.getArgument(0));

        String content = "Patient presented with mild sensitivity.";
        LocalDate date = LocalDate.now();

        ClinicalNote result = ehrService.addClinicalNote(patientId, dentistId, content, date, appointmentId);

        assertThat(result).isNotNull();
        assertThat(result.getContent()).isEqualTo(content);
        assertThat(result.getDate()).isEqualTo(date);
        assertThat(result.getDentistId()).isEqualTo(dentistId.value());
        verify(ehrRepository).save(ehr);
        assertThat(ehr.getClinicalNotes()).contains(result);
    }

    @Test
    void addClinicalNote_shouldThrow_whenDentistNotFound() {
        when(userRepository.findById(dentistId)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> ehrService.addClinicalNote(patientId, dentistId, "content", LocalDate.now(), appointmentId))
                .isInstanceOf(DomainException.class)
                .hasMessageContaining("User not found");
    }

    @Test
    void addClinicalNote_shouldThrow_whenUserIsNotDentist() {
        User receptionist = User.builder()
                .userId(dentistId)
                .username("reception")
                .role(UserRole.RECEPTIONIST)
                .isActive(true)
                .createdAt(LocalDateTime.now())
                .build();
        when(userRepository.findById(dentistId)).thenReturn(Optional.of(receptionist));

        assertThatThrownBy(() -> ehrService.addClinicalNote(patientId, dentistId, "content", LocalDate.now(), appointmentId))
                .isInstanceOf(DomainException.class)
                .hasMessageContaining("User is not a dentist");
    }

    @Test
    void addClinicalNote_shouldThrow_whenPatientArchived() {
        Patient archivedPatient = Patient.create(
                patientId,
                validPatient.getIdentity(),
                new HashSet<>(),
                new HashSet<>(),
                validPatient.getConsents(),
                PatientStatus.ARCHIVED
        );
        when(userRepository.findById(dentistId)).thenReturn(Optional.of(dentistUser));
        when(patientRepository.findById(patientId)).thenReturn(Optional.of(archivedPatient));

        assertThatThrownBy(() -> ehrService.addClinicalNote(patientId, dentistId, "content", LocalDate.now(), appointmentId))
                .isInstanceOf(DomainException.class)
                .hasMessageContaining("Cannot modify EHR for archived patient");
    }

    @Test
    void addClinicalNote_shouldThrow_whenActiveMedicalAlertsExist() {
        MedicalAlert alert = MedicalAlert.builder()
                .alertId(new MedicalAlertId(UUID.randomUUID()))
                .code("HC")
                .description("Heart Condition")
                .severity(AlertSeverity.HIGH)
                .createdAt(LocalDateTime.now())
                .isActive(true)
                .build();
        Patient patientWithAlert = Patient.create(
                patientId,
                validPatient.getIdentity(),
                Set.of(alert),
                new HashSet<>(),
                validPatient.getConsents(),
                PatientStatus.ACTIVE
        );
        when(userRepository.findById(dentistId)).thenReturn(Optional.of(dentistUser));
        when(patientRepository.findById(patientId)).thenReturn(Optional.of(patientWithAlert));

        assertThatThrownBy(() -> ehrService.addClinicalNote(patientId, dentistId, "content", LocalDate.now(), appointmentId))
                .isInstanceOf(DomainException.class)
                .hasMessageContaining("Active medical alerts must be acknowledged");
    }

    @Test
    void addClinicalNote_shouldThrow_whenNoValidTreatmentConsent() {
        Patient patientWithoutConsent = Patient.create(
                patientId,
                validPatient.getIdentity(),
                new HashSet<>(),
                new HashSet<>(),
                List.of(), // No consents
                PatientStatus.ACTIVE
        );
        when(userRepository.findById(dentistId)).thenReturn(Optional.of(dentistUser));
        when(patientRepository.findById(patientId)).thenReturn(Optional.of(patientWithoutConsent));

        assertThatThrownBy(() -> ehrService.addClinicalNote(patientId, dentistId, "content", LocalDate.now(), appointmentId))
                .isInstanceOf(DomainException.class)
                .hasMessageContaining("Patient does not have valid treatment consent");
    }

    @Test
    void recordTreatment_shouldSuccessfullyRecordTreatment() {
        when(userRepository.findById(dentistId)).thenReturn(Optional.of(dentistUser));
        when(patientRepository.findById(patientId)).thenReturn(Optional.of(validPatient));
        when(appointmentRepository.findById(appointmentId)).thenReturn(Optional.of(activeAppointment));
        when(ehrRepository.findByPatientId(patientId)).thenReturn(Optional.of(ehr));
        when(ehrRepository.save(any(ElectronicHealthRecord.class))).thenAnswer(inv -> inv.getArgument(0));

        Set<Integer> toothNumbers = Set.of(14, 15);
        BigDecimal cost = new BigDecimal("250.00");

        TreatmentRecord result = ehrService.recordTreatment(patientId, dentistId, "FILL", "Composite Filling",
                toothNumbers, "Caries on occlusal", cost, appointmentId);

        assertThat(result).isNotNull();
        assertThat(result.getTreatmentCode()).isEqualTo("FILL");
        assertThat(result.getToothNumbers()).isEqualTo(toothNumbers);
        assertThat(result.getCost()).isEqualTo(cost);
        assertThat(result.getPerformedBy()).isEqualTo(dentistId.value());
        verify(ehrRepository).save(ehr);
        assertThat(ehr.getTreatments()).contains(result);
    }

    @Test
    void recordTreatment_shouldThrow_whenToothNumbersEmpty() {
        when(userRepository.findById(dentistId)).thenReturn(Optional.of(dentistUser));
        when(patientRepository.findById(patientId)).thenReturn(Optional.of(validPatient));
        when(appointmentRepository.findById(appointmentId)).thenReturn(Optional.of(activeAppointment));

        Set<Integer> emptySet = Set.of();

        assertThatThrownBy(() -> ehrService.recordTreatment(patientId, dentistId, "CODE", "Desc",
                emptySet, "notes", new BigDecimal("100"), appointmentId))
                .isInstanceOf(DomainException.class)
                .hasMessageContaining("Treatment must reference at least one tooth number");
    }

    @Test
    void recordTreatment_shouldThrow_whenInvalidToothNumber() {
        when(userRepository.findById(dentistId)).thenReturn(Optional.of(dentistUser));
        when(patientRepository.findById(patientId)).thenReturn(Optional.of(validPatient));
        when(appointmentRepository.findById(appointmentId)).thenReturn(Optional.of(activeAppointment));

        Set<Integer> invalidTooth = Set.of(33);

        assertThatThrownBy(() -> ehrService.recordTreatment(patientId, dentistId, "CODE", "Desc",
                invalidTooth, "notes", new BigDecimal("100"), appointmentId))
                .isInstanceOf(DomainException.class)
                .hasMessageContaining("Tooth number must be between 0 (general) and 32");
    }

    @Test
    void recordTreatment_shouldThrow_whenCostExceedsThreshold() {
        when(userRepository.findById(dentistId)).thenReturn(Optional.of(dentistUser));
        when(patientRepository.findById(patientId)).thenReturn(Optional.of(validPatient));
        when(appointmentRepository.findById(appointmentId)).thenReturn(Optional.of(activeAppointment));

        BigDecimal highCost = new BigDecimal("1500.00");

        assertThatThrownBy(() -> ehrService.recordTreatment(patientId, dentistId, "CODE", "Desc",
                Set.of(14), "notes", highCost, appointmentId))
                .isInstanceOf(DomainException.class)
                .hasMessageContaining("Treatments costing over $1000 require dual verification");
    }

    @Test
    void recordTreatment_shouldAllowGeneralToothNumberZero() {
        when(userRepository.findById(dentistId)).thenReturn(Optional.of(dentistUser));
        when(patientRepository.findById(patientId)).thenReturn(Optional.of(validPatient));
        when(appointmentRepository.findById(appointmentId)).thenReturn(Optional.of(activeAppointment));
        when(ehrRepository.findByPatientId(patientId)).thenReturn(Optional.of(ehr));
        when(ehrRepository.save(any(ElectronicHealthRecord.class))).thenAnswer(inv -> inv.getArgument(0));

        Set<Integer> general = Set.of(0);
        TreatmentRecord result = ehrService.recordTreatment(patientId, dentistId, "CLEAN", "Cleaning",
                general, "General cleaning", new BigDecimal("80"), appointmentId);

        assertThat(result).isNotNull();
        assertThat(result.getToothNumbers()).containsExactly(0);
    }

    @Test
    void getOdontogram_shouldReturnOdontogram() {
        when(ehrRepository.findByPatientId(patientId)).thenReturn(Optional.of(ehr));

        Odontogram result = ehrService.getOdontogram(patientId);

        assertThat(result).isEqualTo(ehr.getOdontogram());
    }

    @Test
    void updateToothCondition_shouldSuccessfullyUpdate() {
        when(userRepository.findById(dentistId)).thenReturn(Optional.of(dentistUser));
        when(ehrRepository.findByPatientId(patientId)).thenReturn(Optional.of(ehr));
        when(ehrRepository.save(any(ElectronicHealthRecord.class))).thenAnswer(inv -> inv.getArgument(0));

        Integer toothNumber = 14;
        ToothStatus condition = ToothStatus.CARIES;
        String notes = "Initial caries detected";
        LocalDate lastTreated = LocalDate.now();

        ToothCondition result = ehrService.updateToothCondition(patientId, dentistId, toothNumber, condition, notes, lastTreated);

        assertThat(result).isNotNull();
        assertThat(result.toothNumber()).isEqualTo(toothNumber);
        assertThat(result.condition()).isEqualTo(condition);
        assertThat(result.notes()).isEqualTo(notes);
        assertThat(result.lastTreated()).isEqualTo(lastTreated);
        verify(ehrRepository).save(ehr);
    }

    @Test
    void updateToothCondition_shouldThrow_whenInvalidToothNumber() {
        when(userRepository.findById(dentistId)).thenReturn(Optional.of(dentistUser));

        assertThatThrownBy(() -> ehrService.updateToothCondition(patientId, dentistId, 50, ToothStatus.HEALTHY, "note", LocalDate.now()))
                .isInstanceOf(DomainException.class)
                .hasMessageContaining("Tooth number must be between 0 (general) and 32");
    }

    @Test
    void getClinicalNotes_shouldReturnFilteredNotes() {
        ClinicalNote note1 = ClinicalNote.builder()
                .noteId(new ClinicalNoteId(UUID.randomUUID()))
                .dentistId(dentistId.value())
                .content("Note 1")
                .date(LocalDate.of(2024, 1, 15))
                .createdAt(LocalDateTime.now())
                .build();
        ClinicalNote note2 = ClinicalNote.builder()
                .noteId(new ClinicalNoteId(UUID.randomUUID()))
                .dentistId(dentistId.value())
                .content("Note 2")
                .date(LocalDate.of(2024, 2, 20))
                .createdAt(LocalDateTime.now())
                .build();
        ehr.getClinicalNotes().addAll(List.of(note1, note2));

        when(ehrRepository.findByPatientId(patientId)).thenReturn(Optional.of(ehr));

        LocalDate fromDate = LocalDate.of(2024, 1, 20);
        LocalDate toDate = LocalDate.of(2024, 2, 28);

        List<ClinicalNote> result = ehrService.getClinicalNotes(patientId, fromDate, toDate);

        assertThat(result).containsExactly(note2);
    }

    @Test
    void getClinicalNotes_shouldReturnAllNotes_whenDatesAreNull() {
        ClinicalNote note = ClinicalNote.builder()
                .noteId(new ClinicalNoteId(UUID.randomUUID()))
                .dentistId(dentistId.value())
                .content("Note")
                .date(LocalDate.now())
                .createdAt(LocalDateTime.now())
                .build();
        ehr.getClinicalNotes().add(note);

        when(ehrRepository.findByPatientId(patientId)).thenReturn(Optional.of(ehr));

        List<ClinicalNote> result = ehrService.getClinicalNotes(patientId, null, null);

        assertThat(result).containsExactly(note);
    }

    @Test
    void getTreatmentHistory_shouldReturnFiltered