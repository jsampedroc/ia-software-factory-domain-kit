package com.application.infrastructure.adapter;

import com.application.domain.model.TreatmentPlan;
import com.application.domain.port.TreatmentPlanRepositoryPort;
import com.application.domain.valueobject.PatientId;
import com.application.domain.valueobject.TreatmentPlanId;
import com.application.infrastructure.entity.TreatmentPlanEntity;
import com.application.infrastructure.repository.TreatmentPlanJpaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class TreatmentPlanJpaAdapter implements TreatmentPlanRepositoryPort {

    private final TreatmentPlanJpaRepository treatmentPlanJpaRepository;
    private final TreatmentPlanEntityMapper mapper;

    @Override
    public TreatmentPlan save(TreatmentPlan treatmentPlan) {
        TreatmentPlanEntity entity = mapper.toEntity(treatmentPlan);
        TreatmentPlanEntity savedEntity = treatmentPlanJpaRepository.save(entity);
        return mapper.toDomain(savedEntity);
    }

    @Override
    public Optional<TreatmentPlan> findById(TreatmentPlanId id) {
        return treatmentPlanJpaRepository.findById(id.getValue())
                .map(mapper::toDomain);
    }

    @Override
    public List<TreatmentPlan> findAll() {
        return treatmentPlanJpaRepository.findAll()
                .stream()
                .map(mapper::toDomain)
                .collect(Collectors.toList());
    }

    @Override
    public void deleteById(TreatmentPlanId id) {
        treatmentPlanJpaRepository.deleteById(id.getValue());
    }

    @Override
    public boolean existsById(TreatmentPlanId id) {
        return treatmentPlanJpaRepository.existsById(id.getValue());
    }

    @Override
    public List<TreatmentPlan> findByPatientId(PatientId patientId) {
        return treatmentPlanJpaRepository.findByPatientId(patientId.getValue())
                .stream()
                .map(mapper::toDomain)
                .collect(Collectors.toList());
    }

    @Override
    public List<TreatmentPlan> findByStatus(String status) {
        return treatmentPlanJpaRepository.findByStatus(status)
                .stream()
                .map(mapper::toDomain)
                .collect(Collectors.toList());
    }

    @Component
    static class TreatmentPlanEntityMapper {

        public TreatmentPlanEntity toEntity(TreatmentPlan treatmentPlan) {
            if (treatmentPlan == null) {
                return null;
            }

            TreatmentPlanEntity entity = new TreatmentPlanEntity();
            entity.setId(treatmentPlan.getId().getValue());
            entity.setFechaCreacion(treatmentPlan.getFechaCreacion());
            entity.setFechaInicio(treatmentPlan.getFechaInicio());
            entity.setFechaFinEstimada(treatmentPlan.getFechaFinEstimada());
            entity.setEstado(treatmentPlan.getEstado());
            entity.setCostoTotalEstimado(treatmentPlan.getCostoTotalEstimado());
            return entity;
        }

        public TreatmentPlan toDomain(TreatmentPlanEntity entity) {
            if (entity == null) {
                return null;
            }

            return TreatmentPlan.builder()
                    .id(TreatmentPlanId.of(entity.getId()))
                    .fechaCreacion(entity.getFechaCreacion())
                    .fechaInicio(entity.getFechaInicio())
                    .fechaFinEstimada(entity.getFechaFinEstimada())
                    .estado(entity.getEstado())
                    .costoTotalEstimado(entity.getCostoTotalEstimado())
                    .build();
        }
    }
}