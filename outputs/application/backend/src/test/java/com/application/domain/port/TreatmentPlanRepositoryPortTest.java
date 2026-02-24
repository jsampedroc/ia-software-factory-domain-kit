package com.application.domain.port;

import com.application.domain.model.TreatmentPlan;
import com.application.domain.valueobject.PatientId;
import com.application.domain.valueobject.PlanStatus;
import com.application.domain.valueobject.TreatmentPlanId;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class TreatmentPlanRepositoryPortTest {

    @Mock
    private TreatmentPlanRepositoryPort repositoryPort;

    private TreatmentPlanId planId;
    private PatientId patientId;
    private PlanStatus activeStatus;
    private LocalDate startDate;
    private LocalDate endDate;
    private TreatmentPlan treatmentPlan;

    @BeforeEach
    void setUp() {
        planId = new TreatmentPlanId(UUID.randomUUID());
        patientId = new PatientId(UUID.randomUUID());
        activeStatus = PlanStatus.ACTIVO;
        startDate = LocalDate.now().minusDays(30);
        endDate = LocalDate.now().plusDays(30);
        // Asumiendo que TreatmentPlan tiene un método de fábrica create o build
        // Se usa un mock para la entidad para no depender de su constructor real
        treatmentPlan = org.mockito.Mockito.mock(TreatmentPlan.class);
        when(treatmentPlan.getId()).thenReturn(planId);
    }

    @Test
    void shouldFindByPatientId() {
        List<TreatmentPlan> expectedPlans = List.of(treatmentPlan);
        when(repositoryPort.findByPatientId(patientId)).thenReturn(expectedPlans);

        List<TreatmentPlan> result = repositoryPort.findByPatientId(patientId);

        assertThat(result).isEqualTo(expectedPlans);
        verify(repositoryPort).findByPatientId(patientId);
    }

    @Test
    void shouldFindByStatus() {
        List<TreatmentPlan> expectedPlans = List.of(treatmentPlan);
        when(repositoryPort.findByStatus(activeStatus)).thenReturn(expectedPlans);

        List<TreatmentPlan> result = repositoryPort.findByStatus(activeStatus);

        assertThat(result).isEqualTo(expectedPlans);
        verify(repositoryPort).findByStatus(activeStatus);
    }

    @Test
    void shouldFindByPatientIdAndStatus() {
        List<TreatmentPlan> expectedPlans = List.of(treatmentPlan);
        when(repositoryPort.findByPatientIdAndStatus(patientId, activeStatus)).thenReturn(expectedPlans);

        List<TreatmentPlan> result = repositoryPort.findByPatientIdAndStatus(patientId, activeStatus);

        assertThat(result).isEqualTo(expectedPlans);
        verify(repositoryPort).findByPatientIdAndStatus(patientId, activeStatus);
    }

    @Test
    void shouldFindActivePlansByDateRange() {
        List<TreatmentPlan> expectedPlans = List.of(treatmentPlan);
        when(repositoryPort.findActivePlansByDateRange(startDate, endDate)).thenReturn(expectedPlans);

        List<TreatmentPlan> result = repositoryPort.findActivePlansByDateRange(startDate, endDate);

        assertThat(result).isEqualTo(expectedPlans);
        verify(repositoryPort).findActivePlansByDateRange(startDate, endDate);
    }

    @Test
    void shouldFindActivePlanByPatient() {
        Optional<TreatmentPlan> expectedPlan = Optional.of(treatmentPlan);
        when(repositoryPort.findActivePlanByPatient(patientId)).thenReturn(expectedPlan);

        Optional<TreatmentPlan> result = repositoryPort.findActivePlanByPatient(patientId);

        assertThat(result).isEqualTo(expectedPlan);
        verify(repositoryPort).findActivePlanByPatient(patientId);
    }

    @Test
    void shouldReturnTrueWhenExistsActivePlanForPatient() {
        when(repositoryPort.existsActivePlanForPatient(patientId)).thenReturn(true);

        boolean result = repositoryPort.existsActivePlanForPatient(patientId);

        assertThat(result).isTrue();
        verify(repositoryPort).existsActivePlanForPatient(patientId);
    }

    @Test
    void shouldReturnFalseWhenNotExistsActivePlanForPatient() {
        when(repositoryPort.existsActivePlanForPatient(patientId)).thenReturn(false);

        boolean result = repositoryPort.existsActivePlanForPatient(patientId);

        assertThat(result).isFalse();
        verify(repositoryPort).existsActivePlanForPatient(patientId);
    }

    @Test
    void shouldSaveTreatmentPlan() {
        when(repositoryPort.save(treatmentPlan)).thenReturn(treatmentPlan);

        TreatmentPlan result = repositoryPort.save(treatmentPlan);

        assertThat(result).isEqualTo(treatmentPlan);
        verify(repositoryPort).save(treatmentPlan);
    }

    @Test
    void shouldFindById() {
        Optional<TreatmentPlan> expectedPlan = Optional.of(treatmentPlan);
        when(repositoryPort.findById(planId)).thenReturn(expectedPlan);

        Optional<TreatmentPlan> result = repositoryPort.findById(planId);

        assertThat(result).isEqualTo(expectedPlan);
        verify(repositoryPort).findById(planId);
    }

    @Test
    void shouldDeleteTreatmentPlan() {
        repositoryPort.delete(treatmentPlan);

        verify(repositoryPort).delete(treatmentPlan);
    }

    @Test
    void shouldDeleteById() {
        repositoryPort.deleteById(planId);

        verify(repositoryPort).deleteById(planId);
    }

    @Test
    void shouldCheckExistenceById() {
        when(repositoryPort.existsById(planId)).thenReturn(true);

        boolean result = repositoryPort.existsById(planId);

        assertThat(result).isTrue();
        verify(repositoryPort).existsById(planId);
    }

    @Test
    void shouldFindAll() {
        List<TreatmentPlan> expectedPlans = List.of(treatmentPlan);
        when(repositoryPort.findAll()).thenReturn(expectedPlans);

        List<TreatmentPlan> result = repositoryPort.findAll();

        assertThat(result).isEqualTo(expectedPlans);
        verify(repositoryPort).findAll();
    }
}