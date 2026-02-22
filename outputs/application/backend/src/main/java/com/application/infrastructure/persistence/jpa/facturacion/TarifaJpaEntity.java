package com.application.infrastructure.persistence.jpa.facturacion;

import com.application.domain.model.facturacion.Tarifa;
import com.application.domain.shared.Entity;
import com.application.domain.valueobject.facturacion.Dinero;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

import static lombok.AccessLevel.PROTECTED;

@Entity
@Table(name = "tarifas")
@Getter
@Setter
@NoArgsConstructor(access = PROTECTED)
public class TarifaJpaEntity extends Entity<Tarifa.TarifaId> {

    @Column(nullable = false, unique = true)
    private String nombre;

    private String descripcion;

    @Embedded
    @AttributeOverrides({
            @AttributeOverride(name = "cantidad", column = @Column(name = "precio_mensual_cantidad", nullable = false)),
            @AttributeOverride(name = "divisa", column = @Column(name = "precio_mensual_divisa", nullable = false, length = 3))
    })
    private Dinero precioMensual;

    @Column(nullable = false)
    private Boolean activo;

    @Column(name = "fecha_inicio", nullable = false)
    private LocalDate fechaInicio;

    @Column(name = "fecha_fin")
    private LocalDate fechaFin;
}