package com.application.domain.model.facturacion;

import com.application.domain.shared.Entity;
import com.application.domain.valueobject.facturacion.Dinero;
import com.application.domain.valueobject.facturacion.Periodo;
import com.application.domain.model.event.FacturaGenerada;
import com.application.domain.model.event.FacturaPagada;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static lombok.AccessLevel.PROTECTED;

@Getter
@Setter
@NoArgsConstructor(access = PROTECTED)
public class Factura extends Entity<Factura.FacturaId> {

    private String numeroFactura;
    private LocalDate fechaEmision;
    private LocalDate fechaVencimiento;
    private Periodo periodoFacturado;
    private Dinero total;
    private EstadoFactura estado;
    private String concepto;
    private List<LineaFactura> lineas = new ArrayList<>();

    private UUID alumnoId;
    private UUID tutorId;

    public Factura(FacturaId id, String numeroFactura, LocalDate fechaEmision, LocalDate fechaVencimiento,
                   Periodo periodoFacturado, Dinero total, EstadoFactura estado, String concepto,
                   UUID alumnoId, UUID tutorId) {
        super(id);
        this.numeroFactura = numeroFactura;
        this.fechaEmision = fechaEmision;
        this.fechaVencimiento = fechaVencimiento;
        this.periodoFacturado = periodoFacturado;
        this.total = total;
        this.estado = estado;
        this.concepto = concepto;
        this.alumnoId = alumnoId;
        this.tutorId = tutorId;
        this.lineas = new ArrayList<>();
        validarEstadoInicial();
    }

    public static Factura generar(String numeroFactura, LocalDate fechaEmision, LocalDate fechaVencimiento,
                                  Periodo periodoFacturado, Dinero total, String concepto,
                                  UUID alumnoId, UUID tutorId) {
        FacturaId id = new FacturaId(UUID.randomUUID());
        Factura factura = new Factura(id, numeroFactura, fechaEmision, fechaVencimiento,
                periodoFacturado, total, EstadoFactura.GENERADA, concepto, alumnoId, tutorId);

        factura.registrarEvento(new FacturaGenerada(
                id.value(),
                numeroFactura,
                alumnoId,
                total,
                fechaVencimiento
        ));
        return factura;
    }

    public void agregarLinea(String conceptoLinea, BigDecimal cantidad, Dinero precioUnitario) {
        LineaFactura linea = new LineaFactura(
                new LineaFactura.LineaFacturaId(UUID.randomUUID()),
                conceptoLinea,
                cantidad,
                precioUnitario
        );
        this.lineas.add(linea);
        recalcularTotal();
    }

    private void recalcularTotal() {
        BigDecimal suma = this.lineas.stream()
                .map(LineaFactura::getImporte)
                .map(Dinero::getCantidad)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        this.total = new Dinero(suma, this.total.getDivisa());
    }

    public void marcarComoPagada(LocalDate fechaPago, Dinero importePagado) {
        if (this.estado != EstadoFactura.PAGADA) {
            this.estado = EstadoFactura.PAGADA;
            registrarEvento(new FacturaPagada(
                    this.getId().value(),
                    fechaPago,
                    importePagado
            ));
        }
    }

    public void marcarComoEnviada() {
        if (this.estado == EstadoFactura.GENERADA) {
            this.estado = EstadoFactura.ENVIADA;
        }
    }

    private void validarEstadoInicial() {
        if (fechaVencimiento.isBefore(fechaEmision)) {
            throw new IllegalArgumentException("La fecha de vencimiento debe ser posterior a la de emisión.");
        }
        if (total.getCantidad().compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException("El total debe ser mayor o igual a cero.");
        }
        if (!periodoFacturado.getFechaInicio().isBefore(periodoFacturado.getFechaFin()) &&
                !periodoFacturado.getFechaInicio().isEqual(periodoFacturado.getFechaFin())) {
            throw new IllegalArgumentException("El periodo facturado debe tener una fecha de inicio anterior o igual a la de fin.");
        }
    }

    public enum EstadoFactura {
        GENERADA, ENVIADA, PAGADA, PENDIENTE, CANCELADA
    }

    public record FacturaId(UUID value) implements com.application.domain.shared.ValueObject {
    }
}