package com.application.infrastructure.entity;

import com.application.domain.shared.Entity;
import com.application.domain.valueobject.DentistId;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "dentists")
@Getter
@NoArgsConstructor(access = lombok.AccessLevel.PROTECTED)
public class DentistEntity extends Entity<DentistId> {

    @Column(name = "licencia_medica", nullable = false, unique = true, length = 50)
    private String licenciaMedica;

    @Column(name = "nombre", nullable = false, length = 100)
    private String nombre;

    @Column(name = "apellido", nullable = false, length = 100)
    private String apellido;

    @Column(name = "telefono", length = 20)
    private String telefono;

    @Column(name = "email", length = 150)
    private String email;

    @Column(name = "fecha_contratacion", nullable = false)
    private LocalDate fechaContratacion;

    @Column(name = "activo", nullable = false)
    private Boolean activo;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "clinic_id", referencedColumnName = "id")
    private ClinicEntity clinic;

    @OneToMany(mappedBy = "dentist", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<AppointmentEntity> appointments = new HashSet<>();

    @OneToMany(mappedBy = "dentist", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<DentistSpecialtyEntity> dentistSpecialties = new HashSet<>();

    @OneToMany(mappedBy = "dentist", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<DentistClinicScheduleEntity> dentistClinicSchedules = new HashSet<>();

    public DentistEntity(DentistId id, String licenciaMedica, String nombre, String apellido,
                         String telefono, String email, LocalDate fechaContratacion, Boolean activo,
                         ClinicEntity clinic) {
        super(id);
        this.licenciaMedica = licenciaMedica;
        this.nombre = nombre;
        this.apellido = apellido;
        this.telefono = telefono;
        this.email = email;
        this.fechaContratacion = fechaContratacion;
        this.activo = activo;
        this.clinic = clinic;
    }
}