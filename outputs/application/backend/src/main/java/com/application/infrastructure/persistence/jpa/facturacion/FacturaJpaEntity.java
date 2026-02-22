package com.application.infrastructure.persistence.jpa.facturacion;

import com.application.domain.model.facturacion.Factura;
import com.application.domain.model.facturacion.LineaFactura;
import com.application.domain.shared.Entity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static lombok.AccessLevel.PROTECTED;

@Entity
@Table(name = "facturas")
@Getter
@Setter
@NoArgsConstructor(access = PROTECTED)
public class FacturaJpaEntity extends Entity<Factura.FacturaId> {

    @Column(name = "numero_factura", unique = true, nullable = false)
    private String numeroFactura;

    @Column(name = "fecha_emision", nullable = false)
    private LocalDate fechaEmision;

    @Column(name = "fecha_vencimiento", nullable = false)
    private LocalDate fechaVencimiento;

    @Column(name = "periodo_facturado_inicio", nullable = false)
    private LocalDate periodoFacturadoInicio;

    @Column(name = "periodo_facturado_fin", nullable = false)
    private LocalDate periodoFacturadoFin;

    @Embedded
    private DineroEmbeddable total;

    @Column(name = "estado", nullable = false)
    private String estado;

    @Column(name = "concepto")
    private String concepto;

    @Column(name = "alumno_id", nullable = false)
    private String alumnoId;

    @OneToMany(mappedBy = "factura", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<LineaFacturaJpaEntity> lineas = new ArrayList<>();

    public Factura toDomain() {
        List<LineaFactura> lineasDomain = this.lineas.stream()
                .map(LineaFacturaJpaEntity::toDomain)
                .collect(Collectors.toList());

        return new Factura(
                new Factura.FacturaId(this.getId()),
                this.numeroFactura,
                this.fechaEmision,
                this.fechaVencimiento,
                this.periodoFacturadoInicio,
                this.periodoFacturadoFin,
                this.total.toDomain(),
                Factura.Estado.valueOf(this.estado),
                this.concepto,
                lineasDomain
        );
    }

    public static FacturaJpaEntity fromDomain(Factura factura) {
        FacturaJpaEntity entity = new FacturaJpaEntity();
        entity.setId(factura.getId() != null ? factura.getId().value() : null);
        entity.numeroFactura = factura.getNumeroFactura();
        entity.fechaEmision = factura.getFechaEmision();
        entity.fechaVencimiento = factura.getFechaVencimiento();
        entity.periodoFacturadoInicio = factura.getPeriodoFacturadoInicio();
        entity.periodoFacturadoFin = factura.getPeriodoFacturadoFin();
        entity.total = DineroEmbeddable.fromDomain(factura.getTotal());
        entity.estado = factura.getEstado().name();
        entity.concepto = factura.getConcepto();
        entity.alumnoId = factura.getAlumnoId(); // Se asume que Factura tiene un método getAlumnoId()

        entity.lineas.clear();
        if (factura.getLineas() != null) {
            List<LineaFacturaJpaEntity> lineasEntities = factura.getLineas().stream()
                    .map(linea -> LineaFacturaJpaEntity.fromDomain(linea, entity))
                    .collect(Collectors.toList());
            entity.lineas.addAll(lineasEntities);
        }

        return entity;
    }
}