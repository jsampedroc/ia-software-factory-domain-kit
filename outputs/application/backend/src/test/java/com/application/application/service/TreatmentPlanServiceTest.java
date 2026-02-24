package com.application.application.service;

import com.application.application.dto.TreatmentPlanDTO;
import com.application.application.mapper.TreatmentPlanMapper;
import com.application.domain.exception.DomainException;
import com.application.domain.model.Patient;
import com.application.domain.model.Treatment;
import com.application.domain.model.TreatmentPlan;
import com.application.domain.port.PatientRepositoryPort;
import com.application.domain.port.TreatmentPlanRepositoryPort;
import com.application.domain.port.TreatmentRepositoryPort;
import com.application.domain.valueobject.PatientId;
import com.application.domain.valueobject.PlanStatus;
import com.application.domain.valueobject.TreatmentId;
import com.application.domain.valueobject.TreatmentPlanId;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TreatmentPlanServiceTest {

    @Mock
    private TreatmentPlanRepositoryPort treatmentPlanRepositoryPort;

    @Mock
    private PatientRepositoryPort patientRepositoryPort;

    @Mock
    private TreatmentRepositoryPort treatmentRepositoryPort;

    @Mock
    private TreatmentPlanMapper treatmentPlanMapper;

    @InjectMocks
    private TreatmentPlanService treatmentPlanService;

    private TreatmentPlanId planId;
    private PatientId patientId;
    private TreatmentId treatmentId1;
    private TreatmentId treatmentId2;
    private TreatmentPlan treatmentPlan;
    private TreatmentPlanDTO treatmentPlanDTO;
    private Patient patient;
    private Treatment treatment1;
    private Treatment treatment2;

    @BeforeEach
    void setUp() {
        planId = new TreatmentPlanId(UUID.randomUUID());
        patientId = new PatientId(UUID.randomUUID());
        treatmentId1 = new TreatmentId(UUID.randomUUID());
        treatmentId2 = new TreatmentId(UUID.randomUUID());

        patient = mock(Patient.class);
        when(patient.isActivo()).thenReturn(true);

        treatment1 = mock(Treatment.class);
        when(treatment1.isActivo()).thenReturn(true);
        treatment2 = mock(Treatment.class);
        when(treatment2.isActivo()).thenReturn(true);

        treatmentPlan = mock(TreatmentPlan.class);
        when(treatmentPlan.getId()).thenReturn(planId);
        when(treatmentPlan.getStatus()).thenReturn(PlanStatus.BORRADOR);
        when(treatmentPlan.getFechaInicio()).thenReturn(LocalDate.now().plusDays(1));
        when(treatmentPlan.calculateTotalCost()).thenReturn(new BigDecimal("1000.00"));

        treatmentPlanDTO = new TreatmentPlanDTO();
        treatmentPlanDTO.setId(planId);
        treatmentPlanDTO.setPatientId(patientId);
        treatmentPlanDTO.setTreatmentIds(new HashSet<>(Arrays.asList(treatmentId1, treatmentId2)));
        treatmentPlanDTO.setStatus(PlanStatus.BORRADOR);
        treatmentPlanDTO.setFechaInicio(LocalDate.now().plusDays(1));
        treatmentPlanDTO.setCostoTotalEstimado(new BigDecimal("1000.00"));
    }

    @Test
    void findById_shouldReturnTreatmentPlanDTO_whenPlanExists() {
        when(treatmentPlanRepositoryPort.findById(planId)).thenReturn(Optional.of(treatmentPlan));
        when(treatmentPlanMapper.toDTO(treatmentPlan)).thenReturn(treatmentPlanDTO);

        TreatmentPlanDTO result = treatmentPlanService.findById(planId);

        assertNotNull(result);
        assertEquals(planId, result.getId());
        verify(treatmentPlanRepositoryPort).findById(planId);
        verify(treatmentPlanMapper).toDTO(treatmentPlan);
    }

    @Test
    void findById_shouldThrowDomainException_whenPlanNotFound() {
        when(treatmentPlanRepositoryPort.findById(planId)).thenReturn(Optional.empty());

        DomainException exception = assertThrows(DomainException.class,
                () -> treatmentPlanService.findById(planId));

        assertEquals("Treatment plan not found with id: " + planId, exception.getMessage());
        verify(treatmentPlanRepositoryPort).findById(planId);
        verify(treatmentPlanMapper, never()).toDTO(any());
    }

    @Test
    void findAll_shouldReturnListOfTreatmentPlanDTO() {
        List<TreatmentPlan> plans = Arrays.asList(treatmentPlan);
        when(treatmentPlanRepositoryPort.findAll()).thenReturn(plans);
        when(treatmentPlanMapper.toDTO(treatmentPlan)).thenReturn(treatmentPlanDTO);

        List<TreatmentPlanDTO> result = treatmentPlanService.findAll();

        assertNotNull(result);
        assertEquals(1, result.size());
        verify(treatmentPlanRepositoryPort).findAll();
        verify(treatmentPlanMapper).toDTO(treatmentPlan);
    }

    @Test
    void findByPatientId_shouldReturnListOfTreatmentPlanDTO() {
        List<TreatmentPlan> plans = Arrays.asList(treatmentPlan);
        when(treatmentPlanRepositoryPort.findByPatientId(patientId)).thenReturn(plans);
        when(treatmentPlanMapper.toDTO(treatmentPlan)).thenReturn(treatmentPlanDTO);

        List<TreatmentPlanDTO> result = treatmentPlanService.findByPatientId(patientId);

        assertNotNull(result);
        assertEquals(1, result.size());
        verify(treatmentPlanRepositoryPort).findByPatientId(patientId);
        verify(treatmentPlanMapper).toDTO(treatmentPlan);
    }

    @Test
    void findByStatus_shouldReturnListOfTreatmentPlanDTO() {
        List<TreatmentPlan> plans = Arrays.asList(treatmentPlan);
        when(treatmentPlanRepositoryPort.findByStatus(PlanStatus.BORRADOR)).thenReturn(plans);
        when(treatmentPlanMapper.toDTO(treatmentPlan)).thenReturn(treatmentPlanDTO);

        List<TreatmentPlanDTO> result = treatmentPlanService.findByStatus(PlanStatus.BORRADOR);

        assertNotNull(result);
        assertEquals(1, result.size());
        verify(treatmentPlanRepositoryPort).findByStatus(PlanStatus.BORRADOR);
        verify(treatmentPlanMapper).toDTO(treatmentPlan);
    }

    @Test
    void create_shouldReturnSavedTreatmentPlanDTO() {
        when(patientRepositoryPort.findById(patientId)).thenReturn(Optional.of(patient));
        when(treatmentRepositoryPort.findById(treatmentId1)).thenReturn(Optional.of(treatment1));
        when(treatmentRepositoryPort.findById(treatmentId2)).thenReturn(Optional.of(treatment2));
        when(treatmentPlanMapper.toDomain(treatmentPlanDTO)).thenReturn(treatmentPlan);
        when(treatmentPlanRepositoryPort.save(treatmentPlan)).thenReturn(treatmentPlan);
        when(treatmentPlanMapper.toDTO(treatmentPlan)).thenReturn(treatmentPlanDTO);

        TreatmentPlanDTO result = treatmentPlanService.create(treatmentPlanDTO);

        assertNotNull(result);
        verify(patientRepositoryPort).findById(patientId);
        verify(treatmentRepositoryPort, times(2)).findById(any(TreatmentId.class));
        verify(treatmentPlan).validate();
        verify(treatmentPlanRepositoryPort).save(treatmentPlan);
        verify(treatmentPlanMapper).toDTO(treatmentPlan);
    }

    @Test
    void create_shouldThrowDomainException_whenPatientNotFound() {
        when(patientRepositoryPort.findById(patientId)).thenReturn(Optional.empty());

        DomainException exception = assertThrows(DomainException.class,
                () -> treatmentPlanService.create(treatmentPlanDTO));

        assertEquals("Patient not found with id: " + patientId, exception.getMessage());
        verify(patientRepositoryPort).findById(patientId);
        verify(treatmentRepositoryPort, never()).findById(any());
        verify(treatmentPlanRepositoryPort, never()).save(any());
    }

    @Test
    void create_shouldThrowDomainException_whenPatientInactive() {
        when(patient.isActivo()).thenReturn(false);
        when(patientRepositoryPort.findById(patientId)).thenReturn(Optional.of(patient));

        DomainException exception = assertThrows(DomainException.class,
                () -> treatmentPlanService.create(treatmentPlanDTO));

        assertEquals("Cannot create treatment plan for inactive patient", exception.getMessage());
        verify(patientRepositoryPort).findById(patientId);
        verify(treatmentRepositoryPort, never()).findById(any());
        verify(treatmentPlanRepositoryPort, never()).save(any());
    }

    @Test
    void create_shouldThrowDomainException_whenTreatmentNotFound() {
        when(patientRepositoryPort.findById(patientId)).thenReturn(Optional.of(patient));
        when(treatmentRepositoryPort.findById(treatmentId1)).thenReturn(Optional.empty());

        DomainException exception = assertThrows(DomainException.class,
                () -> treatmentPlanService.create(treatmentPlanDTO));

        assertEquals("Treatment not found with id: " + treatmentId1, exception.getMessage());
        verify(patientRepositoryPort).findById(patientId);
        verify(treatmentRepositoryPort).findById(treatmentId1);
        verify(treatmentPlanRepositoryPort, never()).save(any());
    }

    @Test
    void create_shouldThrowDomainException_whenTreatmentInactive() {
        when(patientRepositoryPort.findById(patientId)).thenReturn(Optional.of(patient));
        when(treatmentRepositoryPort.findById(treatmentId1)).thenReturn(Optional.of(treatment1));
        when(treatment1.isActivo()).thenReturn(false);

        DomainException exception = assertThrows(DomainException.class,
                () -> treatmentPlanService.create(treatmentPlanDTO));

        assertEquals("Cannot include inactive treatment in plan: " + treatmentId1, exception.getMessage());
        verify(patientRepositoryPort).findById(patientId);
        verify(treatmentRepositoryPort).findById(treatmentId1);
        verify(treatmentPlanRepositoryPort, never()).save(any());
    }

    @Test
    void create_shouldThrowDomainException_whenNoTreatments() {
        treatmentPlanDTO.setTreatmentIds(null);

        DomainException exception = assertThrows(DomainException.class,
                () -> treatmentPlanService.create(treatmentPlanDTO));

        assertEquals("Treatment plan must have at least one treatment", exception.getMessage());
        verify(patientRepositoryPort, never()).findById(any());
        verify(treatmentRepositoryPort, never()).findById(any());
        verify(treatmentPlanRepositoryPort, never()).save(any());
    }

    @Test
    void update_shouldReturnUpdatedTreatmentPlanDTO_whenPlanIsDraft() {
        when(treatmentPlanRepositoryPort.findById(planId)).thenReturn(Optional.of(treatmentPlan));
        when(patientRepositoryPort.findById(patientId)).thenReturn(Optional.of(patient));
        when(treatmentRepositoryPort.findById(treatmentId1)).thenReturn(Optional.of(treatment1));
        when(treatmentRepositoryPort.findById(treatmentId2)).thenReturn(Optional.of(treatment2));
        when(treatmentPlanMapper.toDomain(treatmentPlanDTO)).thenReturn(treatmentPlan);
        when(treatmentPlanRepositoryPort.save(treatmentPlan)).thenReturn(treatmentPlan);
        when(treatmentPlanMapper.toDTO(treatmentPlan)).thenReturn(treatmentPlanDTO);

        TreatmentPlanDTO result = treatmentPlanService.update(planId, treatmentPlanDTO);

        assertNotNull(result);
        verify(treatmentPlanRepositoryPort).findById(planId);
        verify(patientRepositoryPort).findById(patientId);
        verify(treatmentRepositoryPort, times(2)).findById(any(TreatmentId.class));
        verify(treatmentPlan).validate();
        verify(treatmentPlanRepositoryPort).save(treatmentPlan);
        verify(treatmentPlanMapper).toDTO(treatmentPlan);
    }

    @Test
    void update_shouldThrowDomainException_whenPlanNotDraft() {
        when(treatmentPlan.getStatus()).thenReturn(PlanStatus.ACTIVO);
        when(treatmentPlanRepositoryPort.findById(planId)).thenReturn(Optional.of(treatmentPlan));

        DomainException exception = assertThrows(DomainException.class,
                () -> treatmentPlanService.update(planId, treatmentPlanDTO));

        assertEquals("Only treatment plans in DRAFT status can be modified", exception.getMessage());
        verify(treatmentPlanRepositoryPort).findById(planId);
        verify(patientRepositoryPort, never()).findById(any());
        verify(treatmentRepositoryPort, never()).findById(any());
        verify(treatmentPlanRepositoryPort, never()).save(any());
    }

    @Test
    void delete_shouldDeletePlan_whenPlanIsDraft() {
        when(treatmentPlanRepositoryPort.findById(planId)).thenReturn(Optional.of(treatmentPlan));

        treatmentPlanService.delete(planId);

        verify(treatmentPlanRepositoryPort).findById(planId);
        verify(treatmentPlanRepositoryPort).deleteById(planId);
    }

    @Test
    void delete_shouldThrowDomainException_whenPlanNotDraft() {
        when(treatmentPlan.getStatus()).thenReturn(PlanStatus.ACTIVO);
        when(treatmentPlanRepositoryPort.findById(planId)).thenReturn(Optional.of(treatmentPlan));

        DomainException exception = assertThrows(DomainException.class,
                () -> treatmentPlanService.delete(planId));

        assertEquals("Only treatment plans in DRAFT status can be deleted", exception.getMessage());
        verify(treatmentPlanRepositoryPort).findById(planId);
        verify(treatmentPlanRepositoryPort, never()).deleteById(any());
    }

    @Test
    void activatePlan_shouldReturnActivatedPlanDTO_whenConditionsMet() {
        when(treatmentPlanRepositoryPort.findById(planId)).thenReturn(Optional.of(treatmentPlan));
        when(treatmentPlanRepositoryPort.save(treatmentPlan)).thenReturn(treatmentPlan);
        when(treatmentPlanMapper.toDTO(treatmentPlan)).thenReturn(treatmentPlanDTO);

        TreatmentPlanDTO result = treatmentPlanService.activatePlan(planId);

        assertNotNull(result);
        verify(treatmentPlanRepositoryPort).findById(planId);
        verify(treatmentPlan).activate();
        verify(treatmentPlanRepositoryPort).save(treatmentPlan);
        verify(treatmentPlanMapper).toDTO(treatmentPlan);
    }

    @Test
    void activatePlan_shouldThrowDomainException_whenPlanNotDraft() {
        when(treatmentPlan.getStatus()).thenReturn(PlanStatus.ACTIVO);
        when(treatmentPlanRepositoryPort.findById(planId)).thenReturn(Optional.of(treatmentPlan));

        DomainException exception = assertThrows(DomainException.class,
                () -> treatmentPlanService.activatePlan(planId));

        assertEquals("Only treatment plans in DRAFT status can be activated", exception.getMessage());
        verify(treatmentPlanRepositoryPort).findById(planId);
        verify(treatmentPlan, never()).activate();
        verify(treatmentPlanRepositoryPort, never()).save(any());
    }

    @Test
    void activatePlan_shouldThrowDomainException_whenStartDateInPast() {
        when(treatmentPlan.getFechaInicio()).thenReturn(LocalDate.now().minusDays(1));
        when(treatmentPlanRepositoryPort.findById(planId)).thenReturn(Optional.of(treatmentPlan));

        DomainException exception = assertThrows(DomainException.class,
                () -> treatmentPlanService.activatePlan(planId));

        assertEquals("Start date must be today or in the future", exception.getMessage());
        verify(treatmentPlanRepositoryPort).findById(planId);
        verify(treatmentPlan, never()).activate();
        verify(treatmentPlanRepositoryPort, never()).save(any());
    }

    @Test
    void completePlan_shouldReturnCompletedPlanDTO_whenPlanIsActive() {
        when(treatmentPlan.getStatus()).thenReturn(PlanStatus.ACTIVO);
        when(treatmentPlanRepositoryPort.findById(planId)).thenReturn(Optional.of(treatmentPlan));
        when(treatmentPlanRepositoryPort.save(treatmentPlan)).thenReturn(treatmentPlan);
        when(treatmentPlanMapper.toDTO(treatmentPlan)).thenReturn(treatmentPlanDTO);

        TreatmentPlanDTO result = treatmentPlanService.completePlan(planId);

        assertNotNull(result);
        verify(treatmentPlanRepositoryPort).findById(planId);
        verify(treatmentPlan).complete();
        verify(treatmentPlanRepositoryPort).save(treatmentPlan);
        verify(treatmentPlanMapper).toDTO(treatmentPlan);
    }

    @Test
    void completePlan_shouldThrowDomainException_whenPlanNotActive() {
        when(treatmentPlanRepositoryPort.findById(planId)).thenReturn(Optional.of(treatmentPlan));

        DomainException exception = assertThrows(DomainException.class,
                () -> treatmentPlanService.completePlan(planId));

        assertEquals("Only ACTIVE treatment plans can be completed", exception.getMessage());
        verify(treatmentPlanRepositoryPort).findById(planId);
        verify(treatmentPlan, never()).complete();
        verify(treatmentPlanRepositoryPort, never()).save(any());
    }

    @Test
    void cancelPlan_shouldReturnCanceledPlanDTO_whenPlanNotCompleted() {
        when(treatmentPlanRepositoryPort.findById(planId)).thenReturn(Optional.of(treatmentPlan));
        when(treatmentPlanRepositoryPort.save(treatmentPlan)).thenReturn(treatmentPlan);
        when(treatmentPlanMapper.toDTO(treatmentPlan)).thenReturn(treatmentPlanDTO);

        TreatmentPlanDTO result = treatmentPlanService.cancelPlan(planId, "Patient moved");

        assertNotNull(result);
        verify(treatmentPlanRepositoryPort).findById(planId);
        verify(treatmentPlan).cancel("Patient moved");
        verify(treatmentPlanRepositoryPort).save(treatmentPlan);
        verify(treatmentPlanMapper).toDTO(treatmentPlan);
    }

    @Test
    void cancelPlan_shouldThrowDomainException_whenPlanCompleted() {
        when(treatmentPlan.getStatus()).thenReturn(PlanStatus.COMPLETADO);
        when(treatmentPlanRepositoryPort.findById(planId)).thenReturn(Optional.of(treatmentPlan));

        DomainException exception = assertThrows(DomainException.class,
                () -> treatmentPlanService.cancelPlan(planId, "Reason"));

        assertEquals("COMPLETED treatment plans cannot be canceled", exception.getMessage());
        verify(treatmentPlanRepositoryPort).findById(planId);
        verify(treatmentPlan, never()).cancel(any());
        verify(treatmentPlanRepositoryPort, never()).save(any());
    }

    @Test
    void addTreatment_shouldReturnUpdatedPlanDTO_whenConditionsMet() {
        when(treatmentPlanRepositoryPort.findById(planId)).thenReturn(Optional.of(treatmentPlan));
        when(treatmentRepositoryPort.findById(treatmentId1)).thenReturn(Optional.of(treatment1));
        when(treatmentPlanRepositoryPort.save(treatmentPlan)).thenReturn(treatmentPlan);
        when(treatmentPlanMapper.toDTO(treatmentPlan)).thenReturn(treatmentPlanDTO);

        TreatmentPlanDTO result = treatmentPlanService.addTreatment(planId, treatmentId1