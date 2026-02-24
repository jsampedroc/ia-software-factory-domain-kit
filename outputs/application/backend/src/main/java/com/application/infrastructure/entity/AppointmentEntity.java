package com.application.infrastructure.entity;

import com.application.domain.valueobject.AppointmentId;
import com.application.domain.valueobject.AppointmentStatus;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

import static lombok.AccessLevel.PROTECTED;

@Entity
@Table(name = "appointments")
@Getter
@NoArgsConstructor(access = PROTECTED)
public class AppointmentEntity {

    @Id
    @Column(name = "id", columnDefinition = "UUID")
    private AppointmentId id;

    @Column(name = "fecha_hora", nullable = false)
    private LocalDateTime fechaHora;

    @Column(name = "duracion_minutos", nullable = false)
    private Integer duracionMinutos;

    @Enumerated(EnumType.STRING)
    @Column(name = "estado", nullable = false)
    private AppointmentStatus estado;

    @Column(name = "motivo", length = 500)
    private String motivo;

    @Column(name = "notas", columnDefinition = "TEXT")
    private String notas;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "patient_id", nullable = false)
    private PatientEntity patient;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "dentist_id", nullable = false)
    private DentistEntity dentist;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "clinic_id", nullable = false)
    private ClinicEntity clinic;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "consulting_room_id")
    private ConsultingRoomEntity consultingRoom;

    @OneToMany(mappedBy = "appointment", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<AppointmentTreatmentEntity> appointmentTreatments = new HashSet<>();
}