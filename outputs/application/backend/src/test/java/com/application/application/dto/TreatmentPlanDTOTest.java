package com.application.application.dto;

import com.application.domain.model.TreatmentPlan;
import com.application.domain.valueobject.PatientId;
import com.application.domain.valueobject.PlanStatus;
import com.application.domain.valueobject.TreatmentId;
import com.application.domain.valueobject.TreatmentPlanId;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class TreatmentPlanDTOTest {

    private static final UUID PLAN_UUID = UUID.randomUUID();
    private static final UUID PATIENT_UUID = UUID.randomUUID();
    private static final UUID TREATMENT_UUID_1 = UUID.randomUUID();
    private static final UUID TREATMENT_UUID_2 = UUID.randomUUID();
    private static final LocalDate TODAY = LocalDate.now();
    private static final LocalDate NEXT_WEEK = TODAY.plusWeeks(1);
    private static final LocalDate IN_TWO_WEEKS = TODAY.plusWeeks(2);
    private static final BigDecimal COSTO_ESTIMADO = new BigDecimal("1500.75");
    private static final PlanStatus ESTADO_ACTIVO = PlanStatus.ACTIVO;

    @Test
    void givenTreatmentPlanDomain_whenFromDomain_thenDTOIsCorrectlyMapped() {
        // Given
        TreatmentPlanId planId = TreatmentPlanId.of(PLAN_UUID);
        PatientId patientId = PatientId.of(PATIENT_UUID);

        TreatmentPlan.TreatmentPlanItem item1 = new TreatmentPlan.TreatmentPlanItem(
                TreatmentId.of(TREATMENT_UUID_1),
                "TRAT-001",
                "Limpieza Dental",
                1,
                TODAY,
                false
        );

        TreatmentPlan.TreatmentPlanItem item2 = new TreatmentPlan.TreatmentPlanItem(
                TreatmentId.of(TREATMENT_UUID_2),
                "TRAT-002",
                "Ortodoncia Inicial",
                2,
                NEXT_WEEK,
                true
        );

        TreatmentPlan plan = new TreatmentPlan(
                planId,
                patientId,
                TODAY.minusDays(1),
                TODAY,
                IN_TWO_WEEKS,
                ESTADO_ACTIVO,
                COSTO_ESTIMADO,
                List.of(item1, item2)
        );

        // When
        TreatmentPlanDTO dto = TreatmentPlanDTO.fromDomain(plan);

        // Then
        assertThat(dto.planId()).isEqualTo(PLAN_UUID);
        assertThat(dto.patientId()).isEqualTo(PATIENT_UUID);
        assertThat(dto.fechaCreacion()).isEqualTo(TODAY.minusDays(1));
        assertThat(dto.fechaInicio()).isEqualTo(TODAY);
        assertThat(dto.fechaFinEstimada()).isEqualTo(IN_TWO_WEEKS);
        assertThat(dto.estado()).isEqualTo(ESTADO_ACTIVO);
        assertThat(dto.costoTotalEstimado()).isEqualByComparingTo(COSTO_ESTIMADO);
        assertThat(dto.tratamientos()).hasSize(2);

        TreatmentPlanDTO.TreatmentPlanItemDTO dtoItem1 = dto.tratamientos().get(0);
        assertThat(dtoItem1.treatmentId()).isEqualTo(TREATMENT_UUID_1);
        assertThat(dtoItem1.codigo()).isEqualTo("TRAT-001");
        assertThat(dtoItem1.nombre()).isEqualTo("Limpieza Dental");
        assertThat(dtoItem1.orden()).isEqualTo(1);
        assertThat(dtoItem1.fechaProgramada()).isEqualTo(TODAY);
        assertThat(dtoItem1.completado()).isFalse();

        TreatmentPlanDTO.TreatmentPlanItemDTO dtoItem2 = dto.tratamientos().get(1);
        assertThat(dtoItem2.treatmentId()).isEqualTo(TREATMENT_UUID_2);
        assertThat(dtoItem2.codigo()).isEqualTo("TRAT-002");
        assertThat(dtoItem2.nombre()).isEqualTo("Ortodoncia Inicial");
        assertThat(dtoItem2.orden()).isEqualTo(2);
        assertThat(dtoItem2.fechaProgramada()).isEqualTo(NEXT_WEEK);
        assertThat(dtoItem2.completado()).isTrue();
    }

    @Test
    void givenTreatmentPlanDomainWithEmptyTreatments_whenFromDomain_thenDTOHasEmptyList() {
        // Given
        TreatmentPlanId planId = TreatmentPlanId.of(PLAN_UUID);
        PatientId patientId = PatientId.of(PATIENT_UUID);

        TreatmentPlan plan = new TreatmentPlan(
                planId,
                patientId,
                TODAY,
                NEXT_WEEK,
                IN_TWO_WEEKS,
                PlanStatus.BORRADOR,
                BigDecimal.ZERO,
                List.of()
        );

        // When
        TreatmentPlanDTO dto = TreatmentPlanDTO.fromDomain(plan);

        // Then
        assertThat(dto.tratamientos()).isEmpty();
    }

    @Test
    void givenTreatmentPlanDTO_whenToDomain_thenDomainIsCorrectlyMapped() {
        // Given
        TreatmentPlanDTO.TreatmentPlanItemDTO itemDTO1 = new TreatmentPlanDTO.TreatmentPlanItemDTO(
                TREATMENT_UUID_1,
                "CODE-001",
                "Tratamiento 1",
                1,
                TODAY,
                false
        );

        TreatmentPlanDTO.TreatmentPlanItemDTO itemDTO2 = new TreatmentPlanDTO.TreatmentPlanItemDTO(
                TREATMENT_UUID_2,
                "CODE-002",
                "Tratamiento 2",
                2,
                NEXT_WEEK,
                true
        );

        TreatmentPlanDTO dto = new TreatmentPlanDTO(
                PLAN_UUID,
                PATIENT_UUID,
                TODAY.minusDays(2),
                TODAY,
                IN_TWO_WEEKS,
                PlanStatus.COMPLETADO,
                new BigDecimal("2000.50"),
                List.of(itemDTO1, itemDTO2)
        );

        // When
        TreatmentPlan domain = dto.toDomain();

        // Then
        assertThat(domain.getId().value()).isEqualTo(PLAN_UUID);
        assertThat(domain.getPatientId().value()).isEqualTo(PATIENT_UUID);
        assertThat(domain.getFechaCreacion()).isEqualTo(TODAY.minusDays(2));
        assertThat(domain.getFechaInicio()).isEqualTo(TODAY);
        assertThat(domain.getFechaFinEstimada()).isEqualTo(IN_TWO_WEEKS);
        assertThat(domain.getEstado()).isEqualTo(PlanStatus.COMPLETADO);
        assertThat(domain.getCostoTotalEstimado()).isEqualByComparingTo(new BigDecimal("2000.50"));
        assertThat(domain.getTratamientos()).hasSize(2);

        TreatmentPlan.TreatmentPlanItem domainItem1 = domain.getTratamientos().get(0);
        assertThat(domainItem1.treatmentId().value()).isEqualTo(TREATMENT_UUID_1);
        assertThat(domainItem1.codigo()).isEqualTo("CODE-001");
        assertThat(domainItem1.nombre()).isEqualTo("Tratamiento 1");
        assertThat(domainItem1.orden()).isEqualTo(1);
        assertThat(domainItem1.fechaProgramada()).isEqualTo(TODAY);
        assertThat(domainItem1.completado()).isFalse();

        TreatmentPlan.TreatmentPlanItem domainItem2 = domain.getTratamientos().get(1);
        assertThat(domainItem2.treatmentId().value()).isEqualTo(TREATMENT_UUID_2);
        assertThat(domainItem2.codigo()).isEqualTo("CODE-002");
        assertThat(domainItem2.nombre()).isEqualTo("Tratamiento 2");
        assertThat(domainItem2.orden()).isEqualTo(2);
        assertThat(domainItem2.fechaProgramada()).isEqualTo(NEXT_WEEK);
        assertThat(domainItem2.completado()).isTrue();
    }

    @Test
    void givenTreatmentPlanDTOWithNullTreatments_whenToDomain_thenDomainHasEmptyList() {
        // Given
        TreatmentPlanDTO dto = new TreatmentPlanDTO(
                PLAN_UUID,
                PATIENT_UUID,
                TODAY,
                NEXT_WEEK,
                IN_TWO_WEEKS,
                PlanStatus.CANCELADO,
                BigDecimal.TEN,
                null
        );

        // When
        TreatmentPlan domain = dto.toDomain();

        // Then
        assertThat(domain.getTratamientos()).isEmpty();
    }

    @Test
    void givenTreatmentPlanDTOWithEmptyTreatments_whenToDomain_thenDomainHasEmptyList() {
        // Given
        TreatmentPlanDTO dto = new TreatmentPlanDTO(
                PLAN_UUID,
                PATIENT_UUID,
                TODAY,
                NEXT_WEEK,
                IN_TWO_WEEKS,
                PlanStatus.ACTIVO,
                BigDecimal.ONE,
                List.of()
        );

        // When
        TreatmentPlan domain = dto.toDomain();

        // Then
        assertThat(domain.getTratamientos()).isEmpty();
    }

    @Test
    void givenTreatmentPlanItemDTO_whenFromDomain_thenItemDTOIsCorrectlyMapped() {
        // Given
        TreatmentPlan.TreatmentPlanItem domainItem = new TreatmentPlan.TreatmentPlanItem(
                TreatmentId.of(TREATMENT_UUID_1),
                "ITEM-CODE",
                "Item Name",
                5,
                IN_TWO_WEEKS,
                true
        );

        // When
        TreatmentPlanDTO.TreatmentPlanItemDTO itemDTO = TreatmentPlanDTO.TreatmentPlanItemDTO.fromDomain(domainItem);

        // Then
        assertThat(itemDTO.treatmentId()).isEqualTo(TREATMENT_UUID_1);
        assertThat(itemDTO.codigo()).isEqualTo("ITEM-CODE");
        assertThat(itemDTO.nombre()).isEqualTo("Item Name");
        assertThat(itemDTO.orden()).isEqualTo(5);
        assertThat(itemDTO.fechaProgramada()).isEqualTo(IN_TWO_WEEKS);
        assertThat(itemDTO.completado()).isTrue();
    }

    @Test
    void givenTreatmentPlanItemDTO_whenToDomain_thenDomainItemIsCorrectlyMapped() {
        // Given
        TreatmentPlanDTO.TreatmentPlanItemDTO itemDTO = new TreatmentPlanDTO.TreatmentPlanItemDTO(
                TREATMENT_UUID_2,
                "DTO-CODE",
                "DTO Name",
                10,
                TODAY,
                false
        );

        // When
        TreatmentPlan.TreatmentPlanItem domainItem = itemDTO.toDomain();

        // Then
        assertThat(domainItem.treatmentId().value()).isEqualTo(TREATMENT_UUID_2);
        assertThat(domainItem.codigo()).isEqualTo("DTO-CODE");
        assertThat(domainItem.nombre()).isEqualTo("DTO Name");
        assertThat(domainItem.orden()).isEqualTo(10);
        assertThat(domainItem.fechaProgramada()).isEqualTo(TODAY);
        assertThat(domainItem.completado()).isFalse();
    }

    @Test
    void givenTwoTreatmentPlanDTOsWithSameValues_whenEquals_thenTheyAreEqual() {
        // Given
        TreatmentPlanDTO.TreatmentPlanItemDTO item = new TreatmentPlanDTO.TreatmentPlanItemDTO(
                TREATMENT_UUID_1, "CODE", "Name", 1, TODAY, false);

        TreatmentPlanDTO dto1 = new TreatmentPlanDTO(
                PLAN_UUID,
                PATIENT_UUID,
                TODAY,
                NEXT_WEEK,
                IN_TWO_WEEKS,
                ESTADO_ACTIVO,
                COSTO_ESTIMADO,
                List.of(item)
        );

        TreatmentPlanDTO dto2 = new TreatmentPlanDTO(
                PLAN_UUID,
                PATIENT_UUID,
                TODAY,
                NEXT_WEEK,
                IN_TWO_WEEKS,
                ESTADO_ACTIVO,
                COSTO_ESTIMADO,
                List.of(item)
        );

        // Then
        assertThat(dto1).isEqualTo(dto2);
        assertThat(dto1.hashCode()).isEqualTo(dto2.hashCode());
    }

    @Test
    void givenTwoTreatmentPlanDTOsWithDifferentIds_whenEquals_thenTheyAreNotEqual() {
        // Given
        UUID otherPlanId = UUID.randomUUID();
        TreatmentPlanDTO.TreatmentPlanItemDTO item = new TreatmentPlanDTO.TreatmentPlanItemDTO(
                TREATMENT_UUID_1, "CODE", "Name", 1, TODAY, false);

        TreatmentPlanDTO dto1 = new TreatmentPlanDTO(
                PLAN_UUID,
                PATIENT_UUID,
                TODAY,
                NEXT_WEEK,
                IN_TWO_WEEKS,
                ESTADO_ACTIVO,
                COSTO_ESTIMADO,
                List.of(item)
        );

        TreatmentPlanDTO dto2 = new TreatmentPlanDTO(
                otherPlanId,
                PATIENT_UUID,
                TODAY,
                NEXT_WEEK,
                IN_TWO_WEEKS,
                ESTADO_ACTIVO,
                COSTO_ESTIMADO,
                List.of(item)
        );

        // Then
        assertThat(dto1).isNotEqualTo(dto2);
    }
}