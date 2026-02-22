package com.application.infrastructure.persistence.jpa.alumno;

import com.application.domain.model.alumno.Alumno;
import com.application.domain.model.alumno.Tutor;
import com.application.domain.valueobject.alumno.DocumentoIdentidad;
import com.application.infrastructure.persistence.jpa.shared.EntityJpa;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

import static lombok.AccessLevel.PROTECTED;

@Entity
@Table(name = "alumnos")
@Getter
@Setter
@NoArgsConstructor(access = PROTECTED)
public class AlumnoJpaEntity extends EntityJpa<Alumno.AlumnoId> {

    @Column(name = "numero_matricula", unique = true, nullable = false)
    private String numeroMatricula;

    @Column(nullable = false)
    private String nombre;

    @Column(nullable = false)
    private String apellidos;

    @Column(name = "fecha_nacimiento", nullable = false)
    private LocalDate fechaNacimiento;

    @Column(name = "fecha_alta", nullable = false)
    private LocalDate fechaAlta;

    @Column(nullable = false)
    private Boolean activo;

    @OneToMany(mappedBy = "alumno", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<TutorJpaEntity> tutores = new HashSet<>();

    public static AlumnoJpaEntity fromDomain(Alumno alumno) {
        AlumnoJpaEntity entity = new AlumnoJpaEntity();
        entity.setId(alumno.getId() != null ? alumno.getId().value() : null);
        entity.setNumeroMatricula(alumno.getNumeroMatricula());
        entity.setNombre(alumno.getNombre());
        entity.setApellidos(alumno.getApellidos());
        entity.setFechaNacimiento(alumno.getFechaNacimiento());
        entity.setFechaAlta(alumno.getFechaAlta());
        entity.setActivo(alumno.getActivo());

        entity.getTutores().clear();
        if (alumno.getTutores() != null) {
            for (Tutor tutor : alumno.getTutores()) {
                TutorJpaEntity tutorEntity = TutorJpaEntity.fromDomain(tutor);
                tutorEntity.setAlumno(entity);
                entity.getTutores().add(tutorEntity);
            }
        }
        return entity;
    }

    public Alumno toDomain() {
        Set<Tutor> domainTutores = this.tutores.stream()
                .map(TutorJpaEntity::toDomain)
                .collect(Collectors.toSet());

        return Alumno.reconstruir(
                new Alumno.AlumnoId(getId()),
                numeroMatricula,
                nombre,
                apellidos,
                fechaNacimiento,
                fechaAlta,
                activo,
                domainTutores
        );
    }
}