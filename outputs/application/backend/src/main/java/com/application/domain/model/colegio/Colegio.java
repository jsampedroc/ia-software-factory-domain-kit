package com.application.domain.model.colegio;

import com.application.domain.shared.Entity;
import com.application.domain.valueobject.colegio.CodigoCentroEducativo;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

import static lombok.AccessLevel.PROTECTED;

@Getter
@Setter
@NoArgsConstructor(access = PROTECTED)
public class Colegio extends Entity<Colegio.ColegioId> {
    private String nombre;
    private CodigoCentroEducativo codigoCentro;
    private String direccion;
    private String telefono;
    private String email;
    private List<Clase> clases = new ArrayList<>();

    public Colegio(ColegioId id, String nombre, CodigoCentroEducativo codigoCentro, String direccion, String telefono, String email) {
        super(id);
        this.nombre = nombre;
        this.codigoCentro = codigoCentro;
        this.direccion = direccion;
        this.telefono = telefono;
        this.email = email;
    }

    public void agregarClase(Clase clase) {
        this.clases.add(clase);
    }

    public void removerClase(Clase clase) {
        this.clases.remove(clase);
    }

    public record ColegioId(String value) implements com.application.domain.shared.ValueObject {}
}