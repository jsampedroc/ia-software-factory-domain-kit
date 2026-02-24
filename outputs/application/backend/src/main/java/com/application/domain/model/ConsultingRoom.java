package com.application.domain.model;

import com.application.domain.shared.Entity;
import com.application.domain.valueobject.ConsultingRoomId;
import com.application.domain.valueobject.ClinicId;
import lombok.Getter;
import lombok.NoArgsConstructor;

import static lombok.AccessLevel.PROTECTED;

@Getter
@NoArgsConstructor(access = PROTECTED)
public class ConsultingRoom extends Entity<ConsultingRoomId> {
    private String numero;
    private String nombre;
    private String equipamiento;
    private Boolean disponible;
    private ClinicId clinicId;

    private ConsultingRoom(ConsultingRoomId id,
                          String numero,
                          String nombre,
                          String equipamiento,
                          Boolean disponible,
                          ClinicId clinicId) {
        super(id);
        this.numero = numero;
        this.nombre = nombre;
        this.equipamiento = equipamiento;
        this.disponible = disponible;
        this.clinicId = clinicId;
    }

    public static ConsultingRoom create(ConsultingRoomId id,
                                        String numero,
                                        String nombre,
                                        String equipamiento,
                                        Boolean disponible,
                                        ClinicId clinicId) {
        return new ConsultingRoom(id, numero, nombre, equipamiento, disponible, clinicId);
    }

    public void updateInformation(String numero,
                                  String nombre,
                                  String equipamiento,
                                  Boolean disponible) {
        this.numero = numero;
        this.nombre = nombre;
        this.equipamiento = equipamiento;
        this.disponible = disponible;
    }

    public void markAsAvailable() {
        this.disponible = true;
    }

    public void markAsUnavailable() {
        this.disponible = false;
    }

    public void assignToClinic(ClinicId clinicId) {
        this.clinicId = clinicId;
    }
}