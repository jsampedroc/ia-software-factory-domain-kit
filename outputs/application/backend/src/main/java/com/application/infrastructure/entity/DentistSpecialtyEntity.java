package com.application.infrastructure.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;

import static lombok.AccessLevel.PROTECTED;

@Entity
@Table(name = "dentist_specialty")
@Getter
@NoArgsConstructor(access = PROTECTED)
public class DentistSpecialtyEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "dentist_id", nullable = false)
    private UUID dentistId;

    @Column(name = "specialty_id", nullable = false)
    private UUID specialtyId;

    @Column(name = "certification_number")
    private String certificationNumber;

    @Column(name = "certification_date")
    private LocalDateTime certificationDate;

    @Column(name = "certifying_institution")
    private String certifyingInstitution;

    @Column(name = "is_active", nullable = false)
    private Boolean isActive;

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    public DentistSpecialtyEntity(
            UUID dentistId,
            UUID specialtyId,
            String certificationNumber,
            LocalDateTime certificationDate,
            String certifyingInstitution,
            Boolean isActive,
            LocalDateTime createdAt,
            LocalDateTime updatedAt) {
        this.dentistId = dentistId;
        this.specialtyId = specialtyId;
        this.certificationNumber = certificationNumber;
        this.certificationDate = certificationDate;
        this.certifyingInstitution = certifyingInstitution;
        this.isActive = isActive;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }
}