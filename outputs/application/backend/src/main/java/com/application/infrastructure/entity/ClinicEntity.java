package com.application.infrastructure.entity;

import com.application.domain.valueobject.ClinicId;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static lombok.AccessLevel.PROTECTED;

@Entity
@Table(name = "clinics")
@Getter
@NoArgsConstructor(access = PROTECTED)
public class ClinicEntity {

    @Id
    private UUID id;

    @Column(nullable = false, unique = true, length = 20)
    private String codigo;

    @Column(nullable = false, length = 100)
    private String nombre;

    @Column(nullable = false, length = 200)
    private String direccion;

    @Column(nullable = false, length = 20)
    private String telefono;

    @Column(nullable = false, length = 100)
    private String email;

    @Column(nullable = false)
    private LocalTime horarioApertura;

    @Column(nullable = false)
    private LocalTime horarioCierre;

    @Column(nullable = false)
    private Boolean activa;

    @OneToMany(mappedBy = "clinic", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ConsultingRoomEntity> consultorios = new ArrayList<>();

    @OneToMany(mappedBy = "clinic", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<DentistClinicScheduleEntity> horariosOdontologos = new ArrayList<>();

    public ClinicId getDomainId() {
        return ClinicId.of(this.id);
    }

    public void setDomainId(ClinicId clinicId) {
        this.id = clinicId.getValue();
    }
}