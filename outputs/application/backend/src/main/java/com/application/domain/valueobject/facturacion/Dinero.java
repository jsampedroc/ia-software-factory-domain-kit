package com.application.domain.valueobject.facturacion;

import com.application.domain.shared.ValueObject;
import java.util.Objects;

public record Dinero(double cantidad, String divisa) implements ValueObject {

    public Dinero {
        if (cantidad < 0) {
            throw new IllegalArgumentException("La cantidad no puede ser negativa");
        }
        Objects.requireNonNull(divisa, "La divisa no puede ser nula");
        if (!divisa.matches("[A-Z]{3}")) {
            throw new IllegalArgumentException("La divisa debe ser un código ISO válido de 3 letras (ej: EUR)");
        }
    }

    public Dinero sumar(Dinero otro) {
        if (!this.divisa.equals(otro.divisa)) {
            throw new IllegalArgumentException("No se pueden sumar cantidades de diferentes divisas");
        }
        return new Dinero(this.cantidad + otro.cantidad, this.divisa);
    }

    public Dinero multiplicar(double multiplicador) {
        return new Dinero(this.cantidad * multiplicador, this.divisa);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Dinero dinero = (Dinero) o;
        return Double.compare(cantidad, dinero.cantidad) == 0 && Objects.equals(divisa, dinero.divisa);
    }

    @Override
    public int hashCode() {
        return Objects.hash(cantidad, divisa);
    }

    @Override
    public String toString() {
        return String.format("%.2f %s", cantidad, divisa);
    }
}