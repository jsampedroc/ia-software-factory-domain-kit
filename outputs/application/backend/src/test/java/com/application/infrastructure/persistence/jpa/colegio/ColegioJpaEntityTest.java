package com.application.infrastructure.persistence.jpa.colegio;

import com.application.domain.model.colegio.Colegio;
import com.application.domain.valueobject.colegio.CodigoCentroEducativo;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class ColegioJpaEntityTest {

    private ColegioJpaEntity colegioJpaEntity;
    private final UUID testId = UUID.randomUUID();
    private final String testNombre = "Colegio Test";
    private final String testCodigo = "CT-12345";
    private final String testDireccion = "Calle Falsa 123";
    private final String testTelefono = "+34123456789";
    private final String testEmail = "colegio@test.com";

    @BeforeEach
    void setUp() {
        colegioJpaEntity = new ColegioJpaEntity();
        colegioJpaEntity.setId(testId);
        colegioJpaEntity.setNombre(testNombre);
        colegioJpaEntity.setCodigoCentro(testCodigo);
        colegioJpaEntity.setDireccion(testDireccion);
        colegioJpaEntity.setTelefono(testTelefono);
        colegioJpaEntity.setEmail(testEmail);
    }

    @Test
    void testToDomainEntity() {
        Colegio domainEntity = colegioJpaEntity.toDomainEntity();

        assertThat(domainEntity).isNotNull();
        assertThat(domainEntity.getId().value()).isEqualTo(testId);
        assertThat(domainEntity.getNombre()).isEqualTo(testNombre);
        assertThat(domainEntity.getCodigoCentro().codigo()).isEqualTo(testCodigo);
        assertThat(domainEntity.getDireccion()).isEqualTo(testDireccion);
        assertThat(domainEntity.getTelefono()).isEqualTo(testTelefono);
        assertThat(domainEntity.getEmail()).isEqualTo(testEmail);
    }

    @Test
    void testFromDomainEntity() {
        Colegio domainEntity = Colegio.builder()
                .id(new Colegio.ColegioId(testId))
                .nombre(testNombre)
                .codigoCentro(new CodigoCentroEducativo(testCodigo))
                .direccion(testDireccion)
                .telefono(testTelefono)
                .email(testEmail)
                .build();

        ColegioJpaEntity resultEntity = ColegioJpaEntity.fromDomainEntity(domainEntity);

        assertThat(resultEntity).isNotNull();
        assertThat(resultEntity.getId()).isEqualTo(testId);
        assertThat(resultEntity.getNombre()).isEqualTo(testNombre);
        assertThat(resultEntity.getCodigoCentro()).isEqualTo(testCodigo);
        assertThat(resultEntity.getDireccion()).isEqualTo(testDireccion);
        assertThat(resultEntity.getTelefono()).isEqualTo(testTelefono);
        assertThat(resultEntity.getEmail()).isEqualTo(testEmail);
    }

    @Test
    void testFromDomainEntityWithNullValues() {
        Colegio domainEntity = Colegio.builder()
                .id(new Colegio.ColegioId(testId))
                .nombre(testNombre)
                .codigoCentro(new CodigoCentroEducativo(testCodigo))
                .direccion(null)
                .telefono(null)
                .email(null)
                .build();

        ColegioJpaEntity resultEntity = ColegioJpaEntity.fromDomainEntity(domainEntity);

        assertThat(resultEntity).isNotNull();
        assertThat(resultEntity.getId()).isEqualTo(testId);
        assertThat(resultEntity.getNombre()).isEqualTo(testNombre);
        assertThat(resultEntity.getCodigoCentro()).isEqualTo(testCodigo);
        assertThat(resultEntity.getDireccion()).isNull();
        assertThat(resultEntity.getTelefono()).isNull();
        assertThat(resultEntity.getEmail()).isNull();
    }

    @Test
    void testGettersAndSetters() {
        UUID newId = UUID.randomUUID();
        String newNombre = "Nuevo Colegio";
        String newCodigo = "NC-67890";
        String newDireccion = "Avenida Real 456";
        String newTelefono = "+34987654321";
        String newEmail = "nuevo@colegio.com";

        colegioJpaEntity.setId(newId);
        colegioJpaEntity.setNombre(newNombre);
        colegioJpaEntity.setCodigoCentro(newCodigo);
        colegioJpaEntity.setDireccion(newDireccion);
        colegioJpaEntity.setTelefono(newTelefono);
        colegioJpaEntity.setEmail(newEmail);

        assertThat(colegioJpaEntity.getId()).isEqualTo(newId);
        assertThat(colegioJpaEntity.getNombre()).isEqualTo(newNombre);
        assertThat(colegioJpaEntity.getCodigoCentro()).isEqualTo(newCodigo);
        assertThat(colegioJpaEntity.getDireccion()).isEqualTo(newDireccion);
        assertThat(colegioJpaEntity.getTelefono()).isEqualTo(newTelefono);
        assertThat(colegioJpaEntity.getEmail()).isEqualTo(newEmail);
    }

    @Test
    void testEqualsAndHashCode() {
        ColegioJpaEntity sameEntity = new ColegioJpaEntity();
        sameEntity.setId(testId);
        sameEntity.setNombre(testNombre);
        sameEntity.setCodigoCentro(testCodigo);

        ColegioJpaEntity differentEntity = new ColegioJpaEntity();
        differentEntity.setId(UUID.randomUUID());
        differentEntity.setNombre("Otro Colegio");
        differentEntity.setCodigoCentro("OT-11111");

        assertThat(colegioJpaEntity).isEqualTo(sameEntity);
        assertThat(colegioJpaEntity).isNotEqualTo(differentEntity);
        assertThat(colegioJpaEntity.hashCode()).isEqualTo(sameEntity.hashCode());
        assertThat(colegioJpaEntity.hashCode()).isNotEqualTo(differentEntity.hashCode());
    }

    @Test
    void testToString() {
        String toStringResult = colegioJpaEntity.toString();

        assertThat(toStringResult).isNotNull();
        assertThat(toStringResult).contains(testId.toString());
        assertThat(toStringResult).contains(testNombre);
        assertThat(toStringResult).contains(testCodigo);
    }
}