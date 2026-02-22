package com.application.infrastructure.persistence.jpa.alumno;

import com.application.domain.model.alumno.Tutor;
import com.application.domain.valueobject.alumno.DocumentoIdentidad;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class TutorJpaEntityTest {

    private TutorJpaEntity tutorJpaEntity;

    @Mock
    private Tutor domainTutor;

    @Mock
    private DocumentoIdentidad mockDocumento;

    private final UUID testId = UUID.randomUUID();
    private final String testNombre = "Juan";
    private final String testApellidos = "Pérez López";
    private final String testTelefono = "+34123456789";
    private final String testEmail = "juan.perez@email.com";
    private final String testRelacion = "Padre";

    @BeforeEach
    void setUp() {
        tutorJpaEntity = new TutorJpaEntity();
    }

    @Test
    void fromDomainEntity_ShouldMapAllFieldsCorrectly() {
        when(domainTutor.getId()).thenReturn(testId);
        when(domainTutor.getNombre()).thenReturn(testNombre);
        when(domainTutor.getApellidos()).thenReturn(testApellidos);
        when(domainTutor.getDocumentoIdentidad()).thenReturn(mockDocumento);
        when(domainTutor.getTelefono()).thenReturn(testTelefono);
        when(domainTutor.getEmail()).thenReturn(testEmail);
        when(domainTutor.getRelacionConAlumno()).thenReturn(testRelacion);
        when(mockDocumento.tipo()).thenReturn("DNI");
        when(mockDocumento.numero()).thenReturn("12345678A");
        when(mockDocumento.paisEmision()).thenReturn("ES");

        TutorJpaEntity result = TutorJpaEntity.fromDomainEntity(domainTutor);

        assertThat(result.getId()).isEqualTo(testId);
        assertThat(result.getNombre()).isEqualTo(testNombre);
        assertThat(result.getApellidos()).isEqualTo(testApellidos);
        assertThat(result.getTipoDocumento()).isEqualTo("DNI");
        assertThat(result.getNumeroDocumento()).isEqualTo("12345678A");
        assertThat(result.getPaisEmisionDocumento()).isEqualTo("ES");
        assertThat(result.getTelefono()).isEqualTo(testTelefono);
        assertThat(result.getEmail()).isEqualTo(testEmail);
        assertThat(result.getRelacionConAlumno()).isEqualTo(testRelacion);
    }

    @Test
    void fromDomainEntity_ShouldHandleNullDocumentoIdentidad() {
        when(domainTutor.getId()).thenReturn(testId);
        when(domainTutor.getNombre()).thenReturn(testNombre);
        when(domainTutor.getApellidos()).thenReturn(testApellidos);
        when(domainTutor.getDocumentoIdentidad()).thenReturn(null);
        when(domainTutor.getTelefono()).thenReturn(testTelefono);
        when(domainTutor.getEmail()).thenReturn(testEmail);
        when(domainTutor.getRelacionConAlumno()).thenReturn(testRelacion);

        TutorJpaEntity result = TutorJpaEntity.fromDomainEntity(domainTutor);

        assertThat(result.getId()).isEqualTo(testId);
        assertThat(result.getNombre()).isEqualTo(testNombre);
        assertThat(result.getApellidos()).isEqualTo(testApellidos);
        assertThat(result.getTipoDocumento()).isNull();
        assertThat(result.getNumeroDocumento()).isNull();
        assertThat(result.getPaisEmisionDocumento()).isNull();
        assertThat(result.getTelefono()).isEqualTo(testTelefono);
        assertThat(result.getEmail()).isEqualTo(testEmail);
        assertThat(result.getRelacionConAlumno()).isEqualTo(testRelacion);
    }

    @Test
    void toDomainEntity_ShouldMapAllFieldsCorrectly() {
        tutorJpaEntity.setId(testId);
        tutorJpaEntity.setNombre(testNombre);
        tutorJpaEntity.setApellidos(testApellidos);
        tutorJpaEntity.setTipoDocumento("PASSPORT");
        tutorJpaEntity.setNumeroDocumento("AB123456");
        tutorJpaEntity.setPaisEmisionDocumento("US");
        tutorJpaEntity.setTelefono(testTelefono);
        tutorJpaEntity.setEmail(testEmail);
        tutorJpaEntity.setRelacionConAlumno(testRelacion);

        Tutor result = tutorJpaEntity.toDomainEntity();

        assertThat(result.getId()).isEqualTo(testId);
        assertThat(result.getNombre()).isEqualTo(testNombre);
        assertThat(result.getApellidos()).isEqualTo(testApellidos);
        assertThat(result.getDocumentoIdentidad()).isNotNull();
        assertThat(result.getDocumentoIdentidad().tipo()).isEqualTo("PASSPORT");
        assertThat(result.getDocumentoIdentidad().numero()).isEqualTo("AB123456");
        assertThat(result.getDocumentoIdentidad().paisEmision()).isEqualTo("US");
        assertThat(result.getTelefono()).isEqualTo(testTelefono);
        assertThat(result.getEmail()).isEqualTo(testEmail);
        assertThat(result.getRelacionConAlumno()).isEqualTo(testRelacion);
    }

    @Test
    void toDomainEntity_ShouldHandleNullDocumentoFields() {
        tutorJpaEntity.setId(testId);
        tutorJpaEntity.setNombre(testNombre);
        tutorJpaEntity.setApellidos(testApellidos);
        tutorJpaEntity.setTipoDocumento(null);
        tutorJpaEntity.setNumeroDocumento(null);
        tutorJpaEntity.setPaisEmisionDocumento(null);
        tutorJpaEntity.setTelefono(testTelefono);
        tutorJpaEntity.setEmail(testEmail);
        tutorJpaEntity.setRelacionConAlumno(testRelacion);

        Tutor result = tutorJpaEntity.toDomainEntity();

        assertThat(result.getId()).isEqualTo(testId);
        assertThat(result.getNombre()).isEqualTo(testNombre);
        assertThat(result.getApellidos()).isEqualTo(testApellidos);
        assertThat(result.getDocumentoIdentidad()).isNull();
        assertThat(result.getTelefono()).isEqualTo(testTelefono);
        assertThat(result.getEmail()).isEqualTo(testEmail);
        assertThat(result.getRelacionConAlumno()).isEqualTo(testRelacion);
    }

    @Test
    void toDomainEntity_ShouldHandlePartialDocumentoFields() {
        tutorJpaEntity.setId(testId);
        tutorJpaEntity.setNombre(testNombre);
        tutorJpaEntity.setApellidos(testApellidos);
        tutorJpaEntity.setTipoDocumento("NIE");
        tutorJpaEntity.setNumeroDocumento("X-1234567-A");
        tutorJpaEntity.setPaisEmisionDocumento(null);
        tutorJpaEntity.setTelefono(testTelefono);
        tutorJpaEntity.setEmail(testEmail);
        tutorJpaEntity.setRelacionConAlumno(testRelacion);

        Tutor result = tutorJpaEntity.toDomainEntity();

        assertThat(result.getId()).isEqualTo(testId);
        assertThat(result.getNombre()).isEqualTo(testNombre);
        assertThat(result.getApellidos()).isEqualTo(testApellidos);
        assertThat(result.getDocumentoIdentidad()).isNotNull();
        assertThat(result.getDocumentoIdentidad().tipo()).isEqualTo("NIE");
        assertThat(result.getDocumentoIdentidad().numero()).isEqualTo("X-1234567-A");
        assertThat(result.getDocumentoIdentidad().paisEmision()).isNull();
        assertThat(result.getTelefono()).isEqualTo(testTelefono);
        assertThat(result.getEmail()).isEqualTo(testEmail);
        assertThat(result.getRelacionConAlumno()).isEqualTo(testRelacion);
    }
}