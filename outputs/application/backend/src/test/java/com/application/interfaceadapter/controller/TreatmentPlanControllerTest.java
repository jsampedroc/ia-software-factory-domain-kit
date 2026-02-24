package com.application.interfaceadapter.controller;

import com.application.application.dto.TreatmentPlanDTO;
import com.application.application.service.TreatmentPlanService;
import com.application.domain.valueobject.PatientId;
import com.application.domain.valueobject.PlanStatus;
import com.application.domain.valueobject.TreatmentId;
import com.application.domain.valueobject.TreatmentPlanId;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TreatmentPlanControllerTest {

    @Mock
    private TreatmentPlanService treatmentPlanService;

    @InjectMocks
    private TreatmentPlanController treatmentPlanController;

    @Test
    void getAllTreatmentPlans_shouldReturnListOfTreatmentPlans() {
        TreatmentPlanDTO dto1 = createSampleTreatmentPlanDTO();
        TreatmentPlanDTO dto2 = createSampleTreatmentPlanDTO();
        List<TreatmentPlanDTO> expectedList = List.of(dto1, dto2);

        when(treatmentPlanService.findAll()).thenReturn(expectedList);

        ResponseEntity<List<TreatmentPlanDTO>> response = treatmentPlanController.getAllTreatmentPlans();

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(2, response.getBody().size());
        verify(treatmentPlanService, times(1)).findAll();
    }

    @Test
    void getTreatmentPlanById_whenExists_shouldReturnTreatmentPlan() {
        UUID uuid = UUID.randomUUID();
        TreatmentPlanId planId = new TreatmentPlanId(uuid);
        TreatmentPlanDTO expectedDTO = createSampleTreatmentPlanDTO();

        when(treatmentPlanService.findById(planId)).thenReturn(Optional.of(expectedDTO));

        ResponseEntity<TreatmentPlanDTO> response = treatmentPlanController.getTreatmentPlanById(uuid);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertSame(expectedDTO, response.getBody());
        verify(treatmentPlanService, times(1)).findById(planId);
    }

    @Test
    void getTreatmentPlanById_whenNotExists_shouldReturnNotFound() {
        UUID uuid = UUID.randomUUID();
        TreatmentPlanId planId = new TreatmentPlanId(uuid);

        when(treatmentPlanService.findById(planId)).thenReturn(Optional.empty());

        ResponseEntity<TreatmentPlanDTO> response = treatmentPlanController.getTreatmentPlanById(uuid);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertNull(response.getBody());
        verify(treatmentPlanService, times(1)).findById(planId);
    }

    @Test
    void getTreatmentPlansByPatient_shouldReturnList() {
        UUID patientUuid = UUID.randomUUID();
        PatientId patientId = new PatientId(patientUuid);
        List<TreatmentPlanDTO> expectedList = List.of(createSampleTreatmentPlanDTO());

        when(treatmentPlanService.findByPatientId(patientId)).thenReturn(expectedList);

        ResponseEntity<List<TreatmentPlanDTO>> response = treatmentPlanController.getTreatmentPlansByPatient(patientUuid);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertSame(expectedList, response.getBody());
        verify(treatmentPlanService, times(1)).findByPatientId(patientId);
    }

    @Test
    void getTreatmentPlansByStatus_shouldReturnList() {
        String status = "ACTIVE";
        PlanStatus planStatus = PlanStatus.ACTIVE;
        List<TreatmentPlanDTO> expectedList = List.of(createSampleTreatmentPlanDTO());

        when(treatmentPlanService.findByStatus(planStatus)).thenReturn(expectedList);

        ResponseEntity<List<TreatmentPlanDTO>> response = treatmentPlanController.getTreatmentPlansByStatus(status);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertSame(expectedList, response.getBody());
        verify(treatmentPlanService, times(1)).findByStatus(planStatus);
    }

    @Test
    void createTreatmentPlan_shouldReturnCreatedTreatmentPlan() {
        TreatmentPlanDTO inputDTO = createSampleTreatmentPlanDTO();
        TreatmentPlanDTO createdDTO = createSampleTreatmentPlanDTO();
        createdDTO.setPlanId(UUID.randomUUID());

        when(treatmentPlanService.create(inputDTO)).thenReturn(createdDTO);

        ResponseEntity<TreatmentPlanDTO> response = treatmentPlanController.createTreatmentPlan(inputDTO);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertSame(createdDTO, response.getBody());
        verify(treatmentPlanService, times(1)).create(inputDTO);
    }

    @Test
    void updateTreatmentPlan_shouldReturnUpdatedTreatmentPlan() {
        UUID uuid = UUID.randomUUID();
        TreatmentPlanId planId = new TreatmentPlanId(uuid);
        TreatmentPlanDTO inputDTO = createSampleTreatmentPlanDTO();
        TreatmentPlanDTO updatedDTO = createSampleTreatmentPlanDTO();

        when(treatmentPlanService.update(planId, inputDTO)).thenReturn(updatedDTO);

        ResponseEntity<TreatmentPlanDTO> response = treatmentPlanController.updateTreatmentPlan(uuid, inputDTO);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertSame(updatedDTO, response.getBody());
        verify(treatmentPlanService, times(1)).update(planId, inputDTO);
    }

    @Test
    void updateTreatmentPlanStatus_shouldReturnUpdatedTreatmentPlan() {
        UUID uuid = UUID.randomUUID();
        TreatmentPlanId planId = new TreatmentPlanId(uuid);
        String status = "COMPLETED";
        PlanStatus planStatus = PlanStatus.COMPLETED;
        TreatmentPlanDTO updatedDTO = createSampleTreatmentPlanDTO();

        when(treatmentPlanService.updateStatus(planId, planStatus)).thenReturn(updatedDTO);

        ResponseEntity<TreatmentPlanDTO> response = treatmentPlanController.updateTreatmentPlanStatus(uuid, status);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertSame(updatedDTO, response.getBody());
        verify(treatmentPlanService, times(1)).updateStatus(planId, planStatus);
    }

    @Test
    void deleteTreatmentPlan_shouldReturnNoContent() {
        UUID uuid = UUID.randomUUID();
        TreatmentPlanId planId = new TreatmentPlanId(uuid);

        doNothing().when(treatmentPlanService).delete(planId);

        ResponseEntity<Void> response = treatmentPlanController.deleteTreatmentPlan(uuid);

        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
        assertNull(response.getBody());
        verify(treatmentPlanService, times(1)).delete(planId);
    }

    @Test
    void addTreatmentToPlan_shouldReturnUpdatedPlan() {
        UUID planUuid = UUID.randomUUID();
        UUID treatmentUuid = UUID.randomUUID();
        TreatmentPlanId planId = new TreatmentPlanId(planUuid);
        TreatmentId treatmentId = new TreatmentId(treatmentUuid);
        TreatmentPlanDTO updatedDTO = createSampleTreatmentPlanDTO();

        when(treatmentPlanService.addTreatment(planId, treatmentId)).thenReturn(updatedDTO);

        ResponseEntity<TreatmentPlanDTO> response = treatmentPlanController.addTreatmentToPlan(planUuid, treatmentUuid);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertSame(updatedDTO, response.getBody());
        verify(treatmentPlanService, times(1)).addTreatment(planId, treatmentId);
    }

    @Test
    void removeTreatmentFromPlan_shouldReturnUpdatedPlan() {
        UUID planUuid = UUID.randomUUID();
        UUID treatmentUuid = UUID.randomUUID();
        TreatmentPlanId planId = new TreatmentPlanId(planUuid);
        TreatmentId treatmentId = new TreatmentId(treatmentUuid);
        TreatmentPlanDTO updatedDTO = createSampleTreatmentPlanDTO();

        when(treatmentPlanService.removeTreatment(planId, treatmentId)).thenReturn(updatedDTO);

        ResponseEntity<TreatmentPlanDTO> response = treatmentPlanController.removeTreatmentFromPlan(planUuid, treatmentUuid);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertSame(updatedDTO, response.getBody());
        verify(treatmentPlanService, times(1)).removeTreatment(planId, treatmentId);
    }

    private TreatmentPlanDTO createSampleTreatmentPlanDTO() {
        TreatmentPlanDTO dto = new TreatmentPlanDTO();
        dto.setPlanId(UUID.randomUUID());
        dto.setPatientId(UUID.randomUUID());
        dto.setDentistId(UUID.randomUUID());
        dto.setClinicId(UUID.randomUUID());
        dto.setFechaCreacion(LocalDate.now());
        dto.setFechaInicio(LocalDate.now().plusDays(1));
        dto.setFechaFinEstimada(LocalDate.now().plusMonths(1));
        dto.setEstado(PlanStatus.BORRADOR);
        dto.setCostoTotalEstimado(new BigDecimal("1500.00"));
        dto.setDescripcion("Plan de tratamiento de prueba");
        return dto;
    }
}