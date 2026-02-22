package com.application.infrastructure.persistence.jpa.facturacion;

import com.application.domain.model.facturacion.LineaFactura;
import com.application.domain.shared.Entity;
import com.application.domain.valueobject.facturacion.Dinero;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.UUID;

@Entity
@Table(name = "lineas_factura")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class LineaFacturaJpaEntity extends Entity<UUID> {

    @Column(nullable = false)
    private String concepto;

    @Column(nullable = false)
    private BigDecimal cantidad;

    @Embedded
    @AttributeOverrides({
            @AttributeOverride(name = "cantidad", column = @Column(name = "precio_unitario_cantidad", nullable = false)),
            @AttributeOverride(name = "divisa", column = @Column(name = "precio_unitario_divisa", nullable = false, length = 3))
    })
    private Dinero precioUnitario;

    @Embedded
    @AttributeOverrides({
            @AttributeOverride(name = "cantidad", column = @Column(name = "importe_cantidad", nullable = false)),
            @AttributeOverride(name = "divisa", column = @Column(name = "importe_divisa", nullable = false, length = 3))
    })
    private Dinero importe;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "factura_id", nullable = false)
    private FacturaJpaEntity factura;

    public static LineaFacturaJpaEntity fromDomain(LineaFactura lineaFactura, FacturaJpaEntity facturaJpaEntity) {
        LineaFacturaJpaEntity entity = new LineaFacturaJpaEntity();
        entity.id = lineaFactura.getId().value();
        entity.concepto = lineaFactura.getConcepto();
        entity.cantidad = lineaFactura.getCantidad();
        entity.precioUnitario = lineaFactura.getPrecioUnitario();
        entity.importe = lineaFactura.getImporte();
        entity.factura = facturaJpaEntity;
        return entity;
    }

    public LineaFactura toDomain() {
        return LineaFactura.builder()
                .id(new LineaFactura.LineaFacturaId(this.id))
                .concepto(this.concepto)
                .cantidad(this.cantidad)
                .precioUnitario(this.precioUnitario)
                .importe(this.importe)
                .build();
    }
}