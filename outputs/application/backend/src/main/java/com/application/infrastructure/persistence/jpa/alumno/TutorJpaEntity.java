package com.application.infrastructure.persistence.jpa.alumno;

import com.application.domain.model.alumno.Tutor;
import com.application.domain.shared.Entity;
import com.application.domain.valueobject.alumno.DocumentoIdentidad;
import jakarta.persistence.*;
import lombok.NoArgsConstructor;

import static lombok.AccessLevel.PROTECTED;

@Entity
@Table(name = "tutores")
@NoArgsConstructor(access = PROTECTED)
public class TutorJpaEntity extends Entity<Tutor.TutorId> {

    @Column(nullable = false)
    private String nombre;

    @Column(nullable = false)
    private String apellidos;

    @Embedded
    @AttributeOverrides({
            @AttributeOverride(name = "tipo", column = @Column(name = "doc_tipo")),
            @AttributeOverride(name = "numero", column = @Column(name = "doc_numero", unique = true)),
            @AttributeOverride(name = "paisEmision", column = @Column(name = "doc_pais_emision"))
    })
    private DocumentoIdentidad documentoIdentidad;

    private String telefono;
    private String email;

    @Column(name = "relacion_con_alumno", nullable = false)
    private String relacionConAlumno;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "alumno_id")
    private AlumnoJpaEntity alumno;

    public TutorJpaEntity(Tutor.TutorId id, String nombre, String apellidos, DocumentoIdentidad documentoIdentidad, String telefono, String email, String relacionConAlumno, AlumnoJpaEntity alumno) {
        super(id);
        this.nombre = nombre;
        this.apellidos = apellidos;
        this.documentoIdentidad = documentoIdentidad;
        this.telefono = telefono;
        this.email = email;
        this.relacionConAlumno = relacionConAlumno;
        this.alumno = alumno;
    }

    public static TutorJpaEntity fromDomain(Tutor tutor, AlumnoJpaEntity alumnoJpaEntity) {
        return new TutorJpaEntity(
                tutor.getId(),
                tutor.getNombre(),
                tutor.getApellidos(),
                tutor.getDocumentoIdentidad(),
                tutor.getTelefono(),
                tutor.getEmail(),
                tutor.getRelacionConAlumno(),
                alumnoJpaEntity
        );
    }

    public Tutor toDomain() {
        return new Tutor(
                getId(),
                nombre,
                apellidos,
                documentoIdentidad,
                telefono,
                email,
                relacionConAlumno
        );
    }

    public String getNombre() {
        return nombre;
    }

    public String getApellidos() {
        return apellidos;
    }

    public DocumentoIdentidad getDocumentoIdentidad() {
        return documentoIdentidad;
    }

    public String getTelefono() {
        return telefono;
    }

    public String getEmail() {
        return email;
    }

    public String getRelacionConAlumno() {
        return relacionConAlumno;
    }

    public AlumnoJpaEntity getAlumno() {
        return alumno;
    }
}