package com.application.infrastructure.entity;

import com.application.domain.model.Specialty;
import com.application.domain.valueobject.SpecialtyId;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.UUID;

import static lombok.AccessLevel.PROTECTED;

@Entity
@Table(name = "specialties")
@Getter
@NoArgsConstructor(access = PROTECTED)
public class SpecialtyEntity {

    @Id
    @Column(name = "id", nullable = false, updatable = false)
    private UUID id;

    @Column(name = "codigo", nullable = false, unique = true, length = 20)
    private String codigo;

    @Column(name = "nombre", nullable = false, length = 100)
    private String nombre;

    @Column(name = "descripcion", length = 500)
    private String descripcion;

    public Specialty toDomain() {
        return new Specialty(
                new SpecialtyId(this.id),
                this.codigo,
                this.nombre,
                this.descripcion
        );
    }

    public static SpecialtyEntity fromDomain(Specialty specialty) {
        SpecialtyEntity entity = new SpecialtyEntity();
        entity.id = specialty.getId().getValue();
        entity.codigo = specialty.getCodigo();
        entity.nombre = specialty.getNombre();
        entity.descripcion = specialty.getDescripcion();
        return entity;
    }
}