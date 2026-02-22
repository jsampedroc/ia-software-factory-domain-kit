package com.application.infrastructure.persistence.jpa.colegio;

import com.application.domain.model.colegio.Clase;
import com.application.domain.model.colegio.ClaseId;
import com.application.domain.model.colegio.Colegio;
import com.application.domain.model.colegio.ColegioId;
import com.application.infrastructure.persistence.jpa.shared.EntityJpa;
import jakarta.persistence.*;

@Entity
@Table(name = "clases")
public class ClaseJpaEntity extends EntityJpa<ClaseId> {

    @Column(nullable = false)
    private String nombre;

    @Column(name = "nivel_educativo", nullable = false)
    private String nivelEducativo;

    @Column(name = "ano_academico", nullable = false)
    private String anoAcademico;

    @Column(name = "capacidad_maxima", nullable = false)
    private Integer capacidadMaxima;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "colegio_id", nullable = false)
    private ColegioJpaEntity colegio;

    protected ClaseJpaEntity() {}

    public static ClaseJpaEntity fromDomain(Clase clase, ColegioJpaEntity colegioJpa) {
        ClaseJpaEntity entity = new ClaseJpaEntity();
        entity.id = clase.getId().value();
        entity.nombre = clase.getNombre();
        entity.nivelEducativo = clase.getNivelEducativo();
        entity.anoAcademico = clase.getAnoAcademico();
        entity.capacidadMaxima = clase.getCapacidadMaxima();
        entity.colegio = colegioJpa;
        return entity;
    }

    public Clase toDomain() {
        Colegio colegioDomain = this.colegio.toDomain();
        return Clase.restore(
                ClaseId.from(this.id),
                this.nombre,
                this.nivelEducativo,
                this.anoAcademico,
                this.capacidadMaxima,
                ColegioId.from(this.colegio.getId())
        );
    }

    public String getNombre() {
        return nombre;
    }

    public String getNivelEducativo() {
        return nivelEducativo;
    }

    public String getAnoAcademico() {
        return anoAcademico;
    }

    public Integer getCapacidadMaxima() {
        return capacidadMaxima;
    }

    public ColegioJpaEntity getColegio() {
        return colegio;
    }
}