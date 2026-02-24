package com.application.infrastructure.adapter;

import com.application.domain.model.TreatmentPlan;
import com.application.domain.valueobject.PlanStatus;
import com.application.domain.valueobject.TreatmentPlanId;
import com.application.infrastructure.entity.TreatmentPlanEntity;
import com.application.infrastructure.repository.TreatmentPlanJpaRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TreatmentPlanJpaAdapterTest {

    @Mock
    private TreatmentPlanJpaRepository treatmentPlanJpaRepository;

    @Spy
    @InjectMocks
    private TreatmentPlanJpaAdapter.TreatmentPlanEntityMapper mapper;

    @InjectMocks
    private TreatmentPlanJpaAdapter treatmentPlanJpaAdapter;

    private TreatmentPlanId treatmentPlanId;
    private UUID uuid;
    private TreatmentPlan domainTreatmentPlan;
    private TreatmentPlanEntity entityTreatmentPlan;

    @BeforeEach
    void setUp() {
        uuid = UUID.randomUUID();
        treatmentPlanId = new TreatmentPlanId(uuid);

        domainTreatmentPlan = TreatmentPlan.builder()
                .id(treatmentPlanId)
                .fechaCreacion(LocalDate.now())
                .fechaInicio(LocalDate.now().plusDays(1))
                .fechaFinEstimada(LocalDate.now().plusMonths(1))
                .estado(PlanStatus.ACTIVO)
                .costoTotalEstimado(new BigDecimal("1500.00"))
                .build();

        entityTreatmentPlan = new TreatmentPlanEntity();
        entityTreatmentPlan.setId(uuid);
        entityTreatmentPlan.setFechaCreacion(domainTreatmentPlan.getFechaCreacion());
        entityTreatmentPlan.setFechaInicio(domainTreatmentPlan.getFechaInicio());
        entityTreatmentPlan.setFechaFinEstimada(domainTreatmentPlan.getFechaFinEstimada());
        entityTreatmentPlan.setEstado(domainTreatmentPlan.getEstado());
        entityTreatmentPlan.setCostoTotalEstimado(domainTreatmentPlan.getCostoTotalEstimado());
    }

    @Test
    void save_ShouldSaveAndReturnDomainObject() {
        when(mapper.toEntity(domainTreatmentPlan)).thenReturn(entityTreatmentPlan);
        when(treatmentPlanJpaRepository.save(entityTreatmentPlan)).thenReturn(entityTreatmentPlan);
        when(mapper.toDomain(entityTreatmentPlan)).thenReturn(domainTreatmentPlan);

        TreatmentPlan result = treatmentPlanJpaAdapter.save(domainTreatmentPlan);

        assertThat(result).isEqualTo(domainTreatmentPlan);
        verify(mapper).toEntity(domainTreatmentPlan);
        verify(treatmentPlanJpaRepository).save(entityTreatmentPlan);
        verify(mapper).toDomain(entityTreatmentPlan);
    }

    @Test
    void findById_WhenEntityExists_ShouldReturnOptionalOfDomainObject() {
        when(treatmentPlanJpaRepository.findById(uuid)).thenReturn(Optional.of(entityTreatmentPlan));
        when(mapper.toDomain(entityTreatmentPlan)).thenReturn(domainTreatmentPlan);

        Optional<TreatmentPlan> result = treatmentPlanJpaAdapter.findById(treatmentPlanId);

        assertThat(result).isPresent().contains(domainTreatmentPlan);
        verify(treatmentPlanJpaRepository).findById(uuid);
        verify(mapper).toDomain(entityTreatmentPlan);
    }

    @Test
    void findById_WhenEntityDoesNotExist_ShouldReturnEmptyOptional() {
        when(treatmentPlanJpaRepository.findById(uuid)).thenReturn(Optional.empty());

        Optional<TreatmentPlan> result = treatmentPlanJpaAdapter.findById(treatmentPlanId);

        assertThat(result).isEmpty();
        verify(treatmentPlanJpaRepository).findById(uuid);
        verify(mapper, never()).toDomain(any());
    }

    @Test
    void findAll_ShouldReturnListOfDomainObjects() {
        List<TreatmentPlanEntity> entityList = List.of(entityTreatmentPlan);
        when(treatmentPlanJpaRepository.findAll()).thenReturn(entityList);
        when(mapper.toDomain(entityTreatmentPlan)).thenReturn(domainTreatmentPlan);

        List<TreatmentPlan> result = treatmentPlanJpaAdapter.findAll();

        assertThat(result).hasSize(1).containsExactly(domainTreatmentPlan);
        verify(treatmentPlanJpaRepository).findAll();
        verify(mapper).toDomain(entityTreatmentPlan);
    }

    @Test
    void deleteById_ShouldCallRepositoryDelete() {
        doNothing().when(treatmentPlanJpaRepository).deleteById(uuid);

        treatmentPlanJpaAdapter.deleteById(treatmentPlanId);

        verify(treatmentPlanJpaRepository).deleteById(uuid);
    }

    @Test
    void existsById_ShouldReturnRepositoryResult() {
        when(treatmentPlanJpaRepository.existsById(uuid)).thenReturn(true);

        boolean result = treatmentPlanJpaAdapter.existsById(treatmentPlanId);

        assertThat(result).isTrue();
        verify(treatmentPlanJpaRepository).existsById(uuid);
    }

    @Test
    void findByPatientId_ShouldReturnListOfDomainObjects() {
        List<TreatmentPlanEntity> entityList = List.of(entityTreatmentPlan);
        when(treatmentPlanJpaRepository.findByPatientId(uuid)).thenReturn(entityList);
        when(mapper.toDomain(entityTreatmentPlan)).thenReturn(domainTreatmentPlan);

        List<TreatmentPlan> result = treatmentPlanJpaAdapter.findByPatientId(treatmentPlanId);

        assertThat(result).hasSize(1).containsExactly(domainTreatmentPlan);
        verify(treatmentPlanJpaRepository).findByPatientId(uuid);
        verify(mapper).toDomain(entityTreatmentPlan);
    }

    @Test
    void findByStatus_ShouldReturnListOfDomainObjects() {
        String status = "ACTIVO";
        List<TreatmentPlanEntity> entityList = List.of(entityTreatmentPlan);
        when(treatmentPlanJpaRepository.findByStatus(status)).thenReturn(entityList);
        when(mapper.toDomain(entityTreatmentPlan)).thenReturn(domainTreatmentPlan);

        List<TreatmentPlan> result = treatmentPlanJpaAdapter.findByStatus(status);

        assertThat(result).hasSize(1).containsExactly(domainTreatmentPlan);
        verify(treatmentPlanJpaRepository).findByStatus(status);
        verify(mapper).toDomain(entityTreatmentPlan);
    }

    @Test
    void mapperToEntity_ShouldMapCorrectly() {
        TreatmentPlanJpaAdapter.TreatmentPlanEntityMapper mapperInstance = new TreatmentPlanJpaAdapter.TreatmentPlanEntityMapper();

        TreatmentPlanEntity result = mapperInstance.toEntity(domainTreatmentPlan);

        assertThat(result.getId()).isEqualTo(uuid);
        assertThat(result.getFechaCreacion()).isEqualTo(domainTreatmentPlan.getFechaCreacion());
        assertThat(result.getFechaInicio()).isEqualTo(domainTreatmentPlan.getFechaInicio());
        assertThat(result.getFechaFinEstimada()).isEqualTo(domainTreatmentPlan.getFechaFinEstimada());
        assertThat(result.getEstado()).isEqualTo(domainTreatmentPlan.getEstado());
        assertThat(result.getCostoTotalEstimado()).isEqualTo(domainTreatmentPlan.getCostoTotalEstimado());
    }

    @Test
    void mapperToEntity_WhenDomainIsNull_ShouldReturnNull() {
        TreatmentPlanJpaAdapter.TreatmentPlanEntityMapper mapperInstance = new TreatmentPlanJpaAdapter.TreatmentPlanEntityMapper();

        TreatmentPlanEntity result = mapperInstance.toEntity(null);

        assertThat(result).isNull();
    }

    @Test
    void mapperToDomain_ShouldMapCorrectly() {
        TreatmentPlanJpaAdapter.TreatmentPlanEntityMapper mapperInstance = new TreatmentPlanJpaAdapter.TreatmentPlanEntityMapper();

        TreatmentPlan result = mapperInstance.toDomain(entityTreatmentPlan);

        assertThat(result.getId()).isEqualTo(TreatmentPlanId.of(uuid));
        assertThat(result.getFechaCreacion()).isEqualTo(entityTreatmentPlan.getFechaCreacion());
        assertThat(result.getFechaInicio()).isEqualTo(entityTreatmentPlan.getFechaInicio());
        assertThat(result.getFechaFinEstimada()).isEqualTo(entityTreatmentPlan.getFechaFinEstimada());
        assertThat(result.getEstado()).isEqualTo(entityTreatmentPlan.getEstado());
        assertThat(result.getCostoTotalEstimado()).isEqualTo(entityTreatmentPlan.getCostoTotalEstimado());
    }

    @Test
    void mapperToDomain_WhenEntityIsNull_ShouldReturnNull() {
        TreatmentPlanJpaAdapter.TreatmentPlanEntityMapper mapperInstance = new TreatmentPlanJpaAdapter.TreatmentPlanEntityMapper();

        TreatmentPlan result = mapperInstance.toDomain(null);

        assertThat(result).isNull();
    }
}