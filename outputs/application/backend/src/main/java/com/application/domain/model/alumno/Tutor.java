package com.application.domain.model.alumno;

import com.application.domain.shared.Entity;
import com.application.domain.valueobject.alumno.DocumentoIdentidad;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Tutor extends Entity<Tutor.TutorId> {
    private String nombre;
    private String apellidos;
    private DocumentoIdentidad documentoIdentidad;
    private String telefono;
    private String email;
    private String relacionConAlumno;

    public Tutor(TutorId id, String nombre, String apellidos, DocumentoIdentidad documentoIdentidad, String telefono, String email, String relacionConAlumno) {
        super(id);
        this.nombre = nombre;
        this.apellidos = apellidos;
        this.documentoIdentidad = documentoIdentidad;
        this.telefono = telefono;
        this.email = email;
        this.relacionConAlumno = relacionConAlumno;
    }

    public record TutorId(String value) implements com.application.domain.shared.ValueObject {}
}