package com.application.application.dto;

import com.application.domain.model.ConsultingRoom;
import com.application.domain.valueobject.ConsultingRoomId;
import com.application.domain.valueobject.ClinicId;

import java.io.Serializable;

public record ConsultingRoomDTO(
        ConsultingRoomId id,
        ClinicId clinicId,
        String numero,
        String nombre,
        String equipamiento,
        Boolean disponible
) implements Serializable {
    public static ConsultingRoomDTO fromDomain(ConsultingRoom consultingRoom) {
        return new ConsultingRoomDTO(
                consultingRoom.getId(),
                consultingRoom.getClinicId(),
                consultingRoom.getNumero(),
                consultingRoom.getNombre(),
                consultingRoom.getEquipamiento(),
                consultingRoom.getDisponible()
        );
    }

    public ConsultingRoom toDomain() {
        return ConsultingRoom.builder()
                .id(id)
                .clinicId(clinicId)
                .numero(numero)
                .nombre(nombre)
                .equipamiento(equipamiento)
                .disponible(disponible)
                .build();
    }
}