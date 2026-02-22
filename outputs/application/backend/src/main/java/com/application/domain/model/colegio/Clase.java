package com.application.domain.model.colegio;

import com.application.domain.shared.Entity;
import com.application.domain.model.alumno.Alumno;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Clase extends Entity<Clase.ClaseId> {

    private String nombre;
    private String nivelEducativo;
    private String anoAcademico;
    private Integer capacidadMaxima;
    private Colegio colegio;
    private Set<Alumno> alumnos = new HashSet<>();

    public Clase(ClaseId id, String nombre, String nivelEducativo, String anoAcademico, Integer capacidadMaxima, Colegio colegio) {
        super(id);
        this.nombre = nombre;
        this.nivelEducativo = nivelEducativo;
        this.anoAcademico = anoAcademico;
        this.capacidadMaxima = capacidadMaxima;
        this.colegio = colegio;
    }

    public record ClaseId(String value) implements com.application.domain.shared.ValueObject {
        @Override
        public String toString() {
            return value;
        }
    }
}