package com.application.infrastructure.persistence.jpa.asistencia;

import com.application.domain.model.asistencia.RegistroAsistencia;
import com.application.domain.shared.Entity;
import com.application.domain.valueobject.asistencia.EstadoAsistencia;
import com.application.infrastructure.persistence.jpa.alumno.AlumnoJpaEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalTime;

@Entity
@Table(name = "registros_asistencia")
@Getter
@Setter
@NoArgsConstructor(access = lombok.AccessLevel.PROTECTED)
public class RegistroAsistenciaJpaEntity extends Entity<RegistroAsistencia.RegistroAsistenciaId> {

    @Column(name = "fecha", nullable = false)
    private LocalDate fecha;

    @Column(name = "hora_entrada")
    private LocalTime horaEntrada;

    @Column(name = "hora_salida")
    private LocalTime horaSalida;

    @Embedded
    @AttributeOverrides({
            @AttributeOverride(name = "valor", column = @Column(name = "estado", nullable = false))
    })
    private EstadoAsistencia estado;

    @Column(name = "comentarios", length = 500)
    private String comentarios;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "alumno_id", nullable = false)
    private AlumnoJpaEntity alumno;
}