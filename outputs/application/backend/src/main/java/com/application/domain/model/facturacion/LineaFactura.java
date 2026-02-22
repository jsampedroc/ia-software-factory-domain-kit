package com.application.domain.model.facturacion;

import com.application.domain.shared.Entity;
import com.application.domain.valueobject.facturacion.Dinero;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class LineaFactura extends Entity<LineaFactura.LineaFacturaId> {

    private String concepto;
    private int cantidad;
    private Dinero precioUnitario;
    private Dinero importe;

    public LineaFactura(LineaFacturaId id, String concepto, int cantidad, Dinero precioUnitario) {
        super(id);
        this.concepto = concepto;
        this.cantidad = cantidad;
        this.precioUnitario = precioUnitario;
        this.importe = precioUnitario.multiplicar(cantidad);
    }

    public record LineaFacturaId(UUID value) implements com.application.domain.shared.ValueObject {}
}