package com.application.infrastructure.entity;

import com.application.domain.model.ConsultingRoom;
import com.application.domain.valueobject.ConsultingRoomId;
import com.application.domain.valueobject.ClinicId;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.UUID;

import static lombok.AccessLevel.PROTECTED;

@Entity
@Table(name = "consulting_rooms")
@Getter
@NoArgsConstructor(access = PROTECTED)
public class ConsultingRoomEntity {

    @Id
    private UUID id;

    @Column(nullable = false, unique = true)
    private String numero;

    @Column(nullable = false)
    private String nombre;

    @Column(columnDefinition = "TEXT")
    private String equipamiento;

    @Column(nullable = false)
    private Boolean disponible;

    @Column(name = "clinic_id", nullable = false)
    private UUID clinicId;

    public static ConsultingRoomEntity fromDomain(ConsultingRoom consultingRoom) {
        ConsultingRoomEntity entity = new ConsultingRoomEntity();
        entity.id = consultingRoom.getId().getValue();
        entity.numero = consultingRoom.getNumero();
        entity.nombre = consultingRoom.getNombre();
        entity.equipamiento = consultingRoom.getEquipamiento();
        entity.disponible = consultingRoom.getDisponible();
        entity.clinicId = consultingRoom.getClinicId().getValue();
        return entity;
    }

    public ConsultingRoom toDomain() {
        return ConsultingRoom.builder()
                .id(ConsultingRoomId.of(id))
                .numero(numero)
                .nombre(nombre)
                .equipamiento(equipamiento)
                .disponible(disponible)
                .clinicId(ClinicId.of(clinicId))
                .build();
    }
}