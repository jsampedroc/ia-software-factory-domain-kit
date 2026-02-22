package com.application.infrastructure.persistence.jpa.colegio;

import com.application.domain.model.colegio.Colegio;
import com.application.domain.shared.Entity;
import com.application.domain.valueobject.colegio.CodigoCentroEducativo;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;
import java.util.stream.Collectors;

@Entity
@Table(name = "colegios")
@NoArgsConstructor(access = lombok.AccessLevel.PROTECTED)
@Getter
@Setter
public class ColegioJpaEntity extends Entity<Long> {

    @Column(nullable = false)
    private String nombre;

    @Embedded
    private CodigoCentroEducativo codigoCentro;

    @Embedded
    @AttributeOverrides({
            @AttributeOverride(name = "linea1", column = @Column(name = "direccion_linea1")),
            @AttributeOverride(name = "linea2", column = @Column(name = "direccion_linea2")),
            @AttributeOverride(name = "codigoPostal", column = @Column(name = "direccion_codigo_postal")),
            @AttributeOverride(name = "ciudad", column = @Column(name = "direccion_ciudad")),
            @AttributeOverride(name = "provincia", column = @Column(name = "direccion_provincia"))
    })
    private com.application.domain.valueobject.alumno.Direccion direccion;

    private String telefono;
    private String email;

    @OneToMany(mappedBy = "colegio", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ClaseJpaEntity> clases;

    public static ColegioJpaEntity fromDomain(Colegio colegio) {
        ColegioJpaEntity entity = new ColegioJpaEntity();
        entity.setId(colegio.getId().value());
        entity.setNombre(colegio.getNombre());
        entity.setCodigoCentro(colegio.getCodigoCentro());
        entity.setDireccion(colegio.getDireccion());
        entity.setTelefono(colegio.getTelefono());
        entity.setEmail(colegio.getEmail());
        if (colegio.getClases() != null) {
            entity.setClases(colegio.getClases().stream()
                    .map(clase -> ClaseJpaEntity.fromDomain(clase, entity))
                    .collect(Collectors.toList()));
        }
        return entity;
    }

    public Colegio toDomain() {
        return Colegio.builder()
                .id(new com.application.domain.model.colegio.ColegioId(getId()))
                .nombre(nombre)
                .codigoCentro(codigoCentro)
                .direccion(direccion)
                .telefono(telefono)
                .email(email)
                .clases(clases != null ? clases.stream()
                        .map(ClaseJpaEntity::toDomain)
                        .collect(Collectors.toList()) : null)
                .build();
    }
}