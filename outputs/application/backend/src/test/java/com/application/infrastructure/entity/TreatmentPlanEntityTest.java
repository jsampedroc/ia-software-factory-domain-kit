package com.application.infrastructure.entity;

import com.application.domain.model.TreatmentPlan;
import com.application.domain.valueobject.*;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class TreatmentPlanEntityTest {

    @Test
    void shouldCreateEntityFromDomain() {
        // Given
        UUID planId = UUID.randomUUID();
        UUID patientId = UUID.randomUUID();
        UUID dentistId = UUID.randomUUID();
        UUID clinicId = UUID.randomUUID();
        LocalDate creationDate = LocalDate.now();
        LocalDate startDate = LocalDate.now().plusDays(1);
        LocalDate estimatedEndDate = LocalDate.now().plusMonths(1);
        BigDecimal estimatedTotalCost = new BigDecimal("1500.50");
        String description = "Plan de tratamiento de ortodoncia";
        String notes = "Paciente con alergia a látex";

        TreatmentPlan domain = TreatmentPlan.builder()
                .id(TreatmentPlanId.of(planId))
                .patientId(PatientId.of(patientId))
                .dentistId(DentistId.of(dentistId))
                .clinicId(ClinicId.of(clinicId))
                .creationDate(creationDate)
                .startDate(startDate)
                .estimatedEndDate(estimatedEndDate)
                .status(PlanStatus.BORRADOR)
                .estimatedTotalCost(estimatedTotalCost)
                .description(description)
                .notes(notes)
                .build();

        // When
        TreatmentPlanEntity entity = TreatmentPlanEntity.fromDomain(domain);

        // Then
        assertThat(entity).isNotNull();
        assertThat(entity.getId()).isEqualTo(planId);
        assertThat(entity.getPatientId()).isEqualTo(patientId);
        assertThat(entity.getDentistId()).isEqualTo(dentistId);
        assertThat(entity.getClinicId()).isEqualTo(clinicId);
        assertThat(entity.getCreationDate()).isEqualTo(creationDate);
        assertThat(entity.getStartDate()).isEqualTo(startDate);
        assertThat(entity.getEstimatedEndDate()).isEqualTo(estimatedEndDate);
        assertThat(entity.getStatus()).isEqualTo(PlanStatus.BORRADOR);
        assertThat(entity.getEstimatedTotalCost()).isEqualTo(estimatedTotalCost);
        assertThat(entity.getDescription()).isEqualTo(description);
        assertThat(entity.getNotes()).isEqualTo(notes);
        assertThat(entity.getPlanTreatments()).isEmpty();
    }

    @Test
    void shouldConvertEntityToDomain() {
        // Given
        UUID planId = UUID.randomUUID();
        UUID patientId = UUID.randomUUID();
        UUID dentistId = UUID.randomUUID();
        UUID clinicId = UUID.randomUUID();
        LocalDate creationDate = LocalDate.now();
        LocalDate startDate = LocalDate.now().plusDays(1);
        LocalDate estimatedEndDate = LocalDate.now().plusMonths(1);
        BigDecimal estimatedTotalCost = new BigDecimal("2000.00");
        String description = "Plan de tratamiento de periodoncia";
        String notes = "Requiere evaluación preoperatoria";

        TreatmentPlanEntity entity = new TreatmentPlanEntity();
        entity.id = planId;
        entity.patientId = patientId;
        entity.dentistId = dentistId;
        entity.clinicId = clinicId;
        entity.creationDate = creationDate;
        entity.startDate = startDate;
        entity.estimatedEndDate = estimatedEndDate;
        entity.status = PlanStatus.ACTIVO;
        entity.estimatedTotalCost = estimatedTotalCost;
        entity.description = description;
        entity.notes = notes;

        // When
        TreatmentPlan domain = entity.toDomain();

        // Then
        assertThat(domain).isNotNull();
        assertThat(domain.getId().getValue()).isEqualTo(planId);
        assertThat(domain.getPatientId().getValue()).isEqualTo(patientId);
        assertThat(domain.getDentistId().getValue()).isEqualTo(dentistId);
        assertThat(domain.getClinicId().getValue()).isEqualTo(clinicId);
        assertThat(domain.getCreationDate()).isEqualTo(creationDate);
        assertThat(domain.getStartDate()).isEqualTo(startDate);
        assertThat(domain.getEstimatedEndDate()).isEqualTo(estimatedEndDate);
        assertThat(domain.getStatus()).isEqualTo(PlanStatus.ACTIVO);
        assertThat(domain.getEstimatedTotalCost()).isEqualTo(estimatedTotalCost);
        assertThat(domain.getDescription()).isEqualTo(description);
        assertThat(domain.getNotes()).isEqualTo(notes);
    }

    @Test
    void shouldUpdateEntityFromDomain() {
        // Given
        UUID planId = UUID.randomUUID();
        UUID originalPatientId = UUID.randomUUID();
        UUID originalDentistId = UUID.randomUUID();
        UUID originalClinicId = UUID.randomUUID();

        TreatmentPlanEntity entity = new TreatmentPlanEntity();
        entity.id = planId;
        entity.patientId = originalPatientId;
        entity.dentistId = originalDentistId;
        entity.clinicId = originalClinicId;
        entity.creationDate = LocalDate.now().minusDays(10);
        entity.startDate = LocalDate.now().minusDays(5);
        entity.estimatedEndDate = LocalDate.now().plusDays(20);
        entity.status = PlanStatus.BORRADOR;
        entity.estimatedTotalCost = new BigDecimal("1000.00");
        entity.description = "Descripción antigua";
        entity.notes = "Notas antiguas";

        UUID newPatientId = UUID.randomUUID();
        UUID newDentistId = UUID.randomUUID();
        UUID newClinicId = UUID.randomUUID();
        LocalDate newCreationDate = LocalDate.now();
        LocalDate newStartDate = LocalDate.now().plusDays(2);
        LocalDate newEstimatedEndDate = LocalDate.now().plusMonths(2);
        BigDecimal newEstimatedTotalCost = new BigDecimal("2500.75");
        String newDescription = "Descripción actualizada";
        String newNotes = "Notas actualizadas";

        TreatmentPlan updatedDomain = TreatmentPlan.builder()
                .id(TreatmentPlanId.of(planId))
                .patientId(PatientId.of(newPatientId))
                .dentistId(DentistId.of(newDentistId))
                .clinicId(ClinicId.of(newClinicId))
                .creationDate(newCreationDate)
                .startDate(newStartDate)
                .estimatedEndDate(newEstimatedEndDate)
                .status(PlanStatus.COMPLETADO)
                .estimatedTotalCost(newEstimatedTotalCost)
                .description(newDescription)
                .notes(newNotes)
                .build();

        // When
        entity.updateFromDomain(updatedDomain);

        // Then
        assertThat(entity.getId()).isEqualTo(planId); // ID no debe cambiar
        assertThat(entity.getPatientId()).isEqualTo(newPatientId);
        assertThat(entity.getDentistId()).isEqualTo(newDentistId);
        assertThat(entity.getClinicId()).isEqualTo(newClinicId);
        assertThat(entity.getCreationDate()).isEqualTo(newCreationDate);
        assertThat(entity.getStartDate()).isEqualTo(newStartDate);
        assertThat(entity.getEstimatedEndDate()).isEqualTo(newEstimatedEndDate);
        assertThat(entity.getStatus()).isEqualTo(PlanStatus.COMPLETADO);
        assertThat(entity.getEstimatedTotalCost()).isEqualTo(newEstimatedTotalCost);
        assertThat(entity.getDescription()).isEqualTo(newDescription);
        assertThat(entity.getNotes()).isEqualTo(newNotes);
    }

    @Test
    void shouldHandleNullOptionalFields() {
        // Given
        UUID planId = UUID.randomUUID();
        UUID patientId = UUID.randomUUID();
        UUID dentistId = UUID.randomUUID();
        UUID clinicId = UUID.randomUUID();
        LocalDate creationDate = LocalDate.now();

        TreatmentPlan domain = TreatmentPlan.builder()
                .id(TreatmentPlanId.of(planId))
                .patientId(PatientId.of(patientId))
                .dentistId(DentistId.of(dentistId))
                .clinicId(ClinicId.of(clinicId))
                .creationDate(creationDate)
                .startDate(null)
                .estimatedEndDate(null)
                .status(PlanStatus.BORRADOR)
                .estimatedTotalCost(null)
                .description(null)
                .notes(null)
                .build();

        // When
        TreatmentPlanEntity entity = TreatmentPlanEntity.fromDomain(domain);
        TreatmentPlan convertedDomain = entity.toDomain();

        // Then
        assertThat(entity).isNotNull();
        assertThat(entity.getStartDate()).isNull();
        assertThat(entity.getEstimatedEndDate()).isNull();
        assertThat(entity.getEstimatedTotalCost()).isNull();
        assertThat(entity.getDescription()).isNull();
        assertThat(entity.getNotes()).isNull();

        assertThat(convertedDomain.getStartDate()).isNull();
        assertThat(convertedDomain.getEstimatedEndDate()).isNull();
        assertThat(convertedDomain.getEstimatedTotalCost()).isNull();
        assertThat(convertedDomain.getDescription()).isNull();
        assertThat(convertedDomain.getNotes()).isNull();
    }

    @Test
    void shouldHaveProtectedNoArgsConstructor() {
        // This test verifies that the entity can be instantiated via reflection
        // (e.g., by JPA/Hibernate) due to the protected no-args constructor
        assertDoesNotThrow(() -> {
            TreatmentPlanEntity entity = TreatmentPlanEntity.class.getDeclaredConstructor().newInstance();
            assertThat(entity).isNotNull();
        });
    }

    @Test
    void shouldMaintainPlanTreatmentsList() {
        // Given
        TreatmentPlanEntity entity = new TreatmentPlanEntity();
        entity.id = UUID.randomUUID();
        entity.patientId = UUID.randomUUID();
        entity.dentistId = UUID.randomUUID();
        entity.clinicId = UUID.randomUUID();
        entity.creationDate = LocalDate.now();
        entity.status = PlanStatus.BORRADOR;

        // When & Then
        assertThat(entity.getPlanTreatments()).isNotNull();
        assertThat(entity.getPlanTreatments()).isEmpty();
    }
}