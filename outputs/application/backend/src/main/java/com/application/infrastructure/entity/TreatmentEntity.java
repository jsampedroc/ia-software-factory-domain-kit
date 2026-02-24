package com.application.infrastructure.entity;

import com.application.domain.model.Treatment;
import com.application.domain.valueobject.TreatmentId;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.UUID;

import static lombok.AccessLevel.PROTECTED;

@Entity
@Table(name = "treatments")
@Getter
@NoArgsConstructor(access = PROTECTED)
public class TreatmentEntity {

    @Id
    private UUID id;

    @Column(name = "code", nullable = false, unique = true, length = 50)
    private String code;

    @Column(name = "name", nullable = false, length = 200)
    private String name;

    @Column(name = "description", columnDefinition = "TEXT")
    private String description;

    @Column(name = "estimated_duration_minutes", nullable = false)
    private Integer estimatedDurationMinutes;

    @Column(name = "base_cost", nullable = false, precision = 10, scale = 2)
    private BigDecimal baseCost;

    @Column(name = "active", nullable = false)
    private Boolean active;

    public Treatment toDomain() {
        return new Treatment(
                new TreatmentId(this.id),
                this.code,
                this.name,
                this.description,
                this.estimatedDurationMinutes,
                this.baseCost,
                this.active
        );
    }

    public static TreatmentEntity fromDomain(Treatment treatment) {
        TreatmentEntity entity = new TreatmentEntity();
        entity.id = treatment.getId().value();
        entity.code = treatment.getCode();
        entity.name = treatment.getName();
        entity.description = treatment.getDescription();
        entity.estimatedDurationMinutes = treatment.getEstimatedDurationMinutes();
        entity.baseCost = treatment.getBaseCost();
        entity.active = treatment.getActive();
        return entity;
    }
}