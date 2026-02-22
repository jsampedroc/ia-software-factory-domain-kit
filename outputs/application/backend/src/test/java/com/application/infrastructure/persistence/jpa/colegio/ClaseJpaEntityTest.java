package com.application.infrastructure.persistence.jpa.colegio;

import com.application.domain.model.colegio.Clase;
import com.application.domain.model.colegio.Colegio;
import com.application.domain.valueobject.colegio.CodigoCentroEducativo;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;

@ExtendWith(MockitoExtension.class)
class ClaseJpaEntityTest {

    private ColegioJpaEntity colegioJpaEntity;
    private Clase domainClase;
    private UUID claseId;
    private UUID colegioId;

    @BeforeEach
    void setUp() {
        claseId = UUID.randomUUID();
        colegioId = UUID.randomUUID();

        Colegio mockColegioDomain = mock(Colegio.class);
        colegioJpaEntity = new ColegioJpaEntity();
        colegioJpaEntity.setId(colegioId);
        colegioJpaEntity.setNombre("Colegio Ejemplo");
        colegioJpaEntity.setCodigoCentro(new CodigoCentroEducativo("COD123"));
        colegioJpaEntity.setTelefono("123456789");
        colegioJpaEntity.setEmail("colegio@ejemplo.com");

        domainClase = Clase.builder()
                .id(new Clase.ClaseId(claseId))
                .nombre("1º Primaria A")
                .nivelEducativo("Primaria")
                .anoAcademico("2023-2024")
                .capacidadMaxima(25)
                .colegio(mockColegioDomain)
                .build();
    }

    @Test
    void fromDomainEntity_ShouldMapAllFieldsCorrectly() {
        ClaseJpaEntity entity = ClaseJpaEntity.fromDomainEntity(domainClase, colegioJpaEntity);

        assertNotNull(entity);
        assertEquals(claseId, entity.getId());
        assertEquals("1º Primaria A", entity.getNombre());
        assertEquals("Primaria", entity.getNivelEducativo());
        assertEquals("2023-2024", entity.getAnoAcademico());
        assertEquals(25, entity.getCapacidadMaxima());
        assertSame(colegioJpaEntity, entity.getColegio());
    }

    @Test
    void fromDomainEntity_WithNullDomain_ShouldReturnNull() {
        ClaseJpaEntity entity = ClaseJpaEntity.fromDomainEntity(null, colegioJpaEntity);
        assertNull(entity);
    }

    @Test
    void fromDomainEntity_WithNullColegioJpaEntity_ShouldSetNullColegio() {
        ClaseJpaEntity entity = ClaseJpaEntity.fromDomainEntity(domainClase, null);
        assertNotNull(entity);
        assertNull(entity.getColegio());
    }

    @Test
    void toDomainEntity_ShouldMapAllFieldsCorrectly() {
        ClaseJpaEntity jpaEntity = new ClaseJpaEntity();
        jpaEntity.setId(claseId);
        jpaEntity.setNombre("1º Primaria A");
        jpaEntity.setNivelEducativo("Primaria");
        jpaEntity.setAnoAcademico("2023-2024");
        jpaEntity.setCapacidadMaxima(25);
        jpaEntity.setColegio(colegioJpaEntity);

        Clase domain = jpaEntity.toDomainEntity();

        assertNotNull(domain);
        assertEquals(claseId, domain.getId().value());
        assertEquals("1º Primaria A", domain.getNombre());
        assertEquals("Primaria", domain.getNivelEducativo());
        assertEquals("2023-2024", domain.getAnoAcademico());
        assertEquals(25, domain.getCapacidadMaxima());
        assertNotNull(domain.getColegio());
        assertEquals(colegioId, domain.getColegio().getId().value());
    }

    @Test
    void toDomainEntity_WithNullColegio_ShouldMapDomainWithNullColegio() {
        ClaseJpaEntity jpaEntity = new ClaseJpaEntity();
        jpaEntity.setId(claseId);
        jpaEntity.setNombre("Clase Sin Colegio");
        jpaEntity.setNivelEducativo("Secundaria");
        jpaEntity.setAnoAcademico("2023-2024");
        jpaEntity.setCapacidadMaxima(30);
        jpaEntity.setColegio(null);

        Clase domain = jpaEntity.toDomainEntity();

        assertNotNull(domain);
        assertEquals(claseId, domain.getId().value());
        assertEquals("Clase Sin Colegio", domain.getNombre());
        assertNull(domain.getColegio());
    }

    @Test
    void equals_ShouldReturnTrueForSameId() {
        ClaseJpaEntity entity1 = new ClaseJpaEntity();
        entity1.setId(claseId);
        entity1.setNombre("Clase A");

        ClaseJpaEntity entity2 = new ClaseJpaEntity();
        entity2.setId(claseId);
        entity2.setNombre("Clase B");

        assertEquals(entity1, entity2);
        assertThat(entity1).isEqualTo(entity2);
    }

    @Test
    void equals_ShouldReturnFalseForDifferentId() {
        ClaseJpaEntity entity1 = new ClaseJpaEntity();
        entity1.setId(claseId);

        ClaseJpaEntity entity2 = new ClaseJpaEntity();
        entity2.setId(UUID.randomUUID());

        assertNotEquals(entity1, entity2);
        assertThat(entity1).isNotEqualTo(entity2);
    }

    @Test
    void hashCode_ShouldBeConsistentWithEquals() {
        ClaseJpaEntity entity1 = new ClaseJpaEntity();
        entity1.setId(claseId);
        ClaseJpaEntity entity2 = new ClaseJpaEntity();
        entity2.setId(claseId);

        assertEquals(entity1.hashCode(), entity2.hashCode());
        assertThat(entity1.hashCode()).isEqualTo(entity2.hashCode());
    }

    @Test
    void toString_ShouldContainRelevantInformation() {
        ClaseJpaEntity entity = new ClaseJpaEntity();
        entity.setId(claseId);
        entity.setNombre("Clase Test");
        entity.setNivelEducativo("Test Nivel");

        String toString = entity.toString();

        assertThat(toString).contains(claseId.toString());
        assertThat(toString).contains("Clase Test");
        assertThat(toString).contains("Test Nivel");
    }
}