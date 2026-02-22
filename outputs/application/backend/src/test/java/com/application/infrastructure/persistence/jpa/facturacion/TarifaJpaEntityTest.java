package com.application.infrastructure.persistence.jpa.facturacion;

import com.application.domain.model.facturacion.Tarifa;
import com.application.domain.model.facturacion.TarifaId;
import com.application.domain.valueobject.facturacion.Dinero;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Currency;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class TarifaJpaEntityTest {

    private TarifaJpaEntity tarifaJpaEntity;
    private Tarifa domainTarifa;

    @BeforeEach
    void setUp() {
        tarifaJpaEntity = new TarifaJpaEntity();
        tarifaJpaEntity.setId("test-uuid-123");
        tarifaJpaEntity.setNombre("Tarifa Básica");
        tarifaJpaEntity.setDescripcion("Tarifa mensual estándar");
        tarifaJpaEntity.setPrecioMensual(new BigDecimal("350.50"));
        tarifaJpaEntity.setDivisa("EUR");
        tarifaJpaEntity.setActivo(true);
        tarifaJpaEntity.setFechaInicio(LocalDate.of(2024, 1, 1));
        tarifaJpaEntity.setFechaFin(LocalDate.of(2024, 12, 31));

        domainTarifa = Tarifa.builder()
                .id(new TarifaId("test-uuid-123"))
                .nombre("Tarifa Básica")
                .descripcion("Tarifa mensual estándar")
                .precioMensual(new Dinero(new BigDecimal("350.50"), Currency.getInstance("EUR")))
                .activo(true)
                .fechaInicio(LocalDate.of(2024, 1, 1))
                .fechaFin(LocalDate.of(2024, 12, 31))
                .build();
    }

    @Test
    void fromDomainEntity_ShouldMapAllFieldsCorrectly() {
        TarifaJpaEntity result = TarifaJpaEntity.fromDomainEntity(domainTarifa);

        assertThat(result).isNotNull();
        assertThat(result.getId()).isEqualTo(domainTarifa.getId().value());
        assertThat(result.getNombre()).isEqualTo(domainTarifa.getNombre());
        assertThat(result.getDescripcion()).isEqualTo(domainTarifa.getDescripcion());
        assertThat(result.getPrecioMensual()).isEqualTo(domainTarifa.getPrecioMensual().cantidad());
        assertThat(result.getDivisa()).isEqualTo(domainTarifa.getPrecioMensual().divisa().getCurrencyCode());
        assertThat(result.isActivo()).isEqualTo(domainTarifa.isActivo());
        assertThat(result.getFechaInicio()).isEqualTo(domainTarifa.getFechaInicio());
        assertThat(result.getFechaFin()).isEqualTo(domainTarifa.getFechaFin());
    }

    @Test
    void fromDomainEntity_WithNullDomainEntity_ShouldReturnNull() {
        TarifaJpaEntity result = TarifaJpaEntity.fromDomainEntity(null);
        assertThat(result).isNull();
    }

    @Test
    void toDomainEntity_ShouldMapAllFieldsCorrectly() {
        Tarifa result = tarifaJpaEntity.toDomainEntity();

        assertThat(result).isNotNull();
        assertThat(result.getId().value()).isEqualTo(tarifaJpaEntity.getId());
        assertThat(result.getNombre()).isEqualTo(tarifaJpaEntity.getNombre());
        assertThat(result.getDescripcion()).isEqualTo(tarifaJpaEntity.getDescripcion());
        assertThat(result.getPrecioMensual().cantidad()).isEqualTo(tarifaJpaEntity.getPrecioMensual());
        assertThat(result.getPrecioMensual().divisa().getCurrencyCode()).isEqualTo(tarifaJpaEntity.getDivisa());
        assertThat(result.isActivo()).isEqualTo(tarifaJpaEntity.isActivo());
        assertThat(result.getFechaInicio()).isEqualTo(tarifaJpaEntity.getFechaInicio());
        assertThat(result.getFechaFin()).isEqualTo(tarifaJpaEntity.getFechaFin());
    }

    @Test
    void toDomainEntity_WithNullId_ShouldThrowException() {
        tarifaJpaEntity.setId(null);
        assertThrows(IllegalArgumentException.class, () -> tarifaJpaEntity.toDomainEntity());
    }

    @Test
    void toDomainEntity_WithNullPrecioMensual_ShouldThrowException() {
        tarifaJpaEntity.setPrecioMensual(null);
        assertThrows(IllegalArgumentException.class, () -> tarifaJpaEntity.toDomainEntity());
    }

    @Test
    void toDomainEntity_WithNullDivisa_ShouldThrowException() {
        tarifaJpaEntity.setDivisa(null);
        assertThrows(IllegalArgumentException.class, () -> tarifaJpaEntity.toDomainEntity());
    }

    @Test
    void toDomainEntity_WithInvalidDivisa_ShouldThrowException() {
        tarifaJpaEntity.setDivisa("INVALID");
        assertThrows(IllegalArgumentException.class, () -> tarifaJpaEntity.toDomainEntity());
    }

    @Test
    void toDomainEntity_WithNullFechaInicio_ShouldMapCorrectly() {
        tarifaJpaEntity.setFechaInicio(null);
        Tarifa result = tarifaJpaEntity.toDomainEntity();
        assertThat(result.getFechaInicio()).isNull();
    }

    @Test
    void toDomainEntity_WithNullFechaFin_ShouldMapCorrectly() {
        tarifaJpaEntity.setFechaFin(null);
        Tarifa result = tarifaJpaEntity.toDomainEntity();
        assertThat(result.getFechaFin()).isNull();
    }

    @Test
    void equals_ShouldReturnTrueForSameId() {
        TarifaJpaEntity entity1 = new TarifaJpaEntity();
        entity1.setId("same-id");
        TarifaJpaEntity entity2 = new TarifaJpaEntity();
        entity2.setId("same-id");

        assertThat(entity1).isEqualTo(entity2);
        assertThat(entity1.hashCode()).isEqualTo(entity2.hashCode());
    }

    @Test
    void equals_ShouldReturnFalseForDifferentId() {
        TarifaJpaEntity entity1 = new TarifaJpaEntity();
        entity1.setId("id-1");
        TarifaJpaEntity entity2 = new TarifaJpaEntity();
        entity2.setId("id-2");

        assertThat(entity1).isNotEqualTo(entity2);
    }

    @Test
    void equals_ShouldReturnFalseForNull() {
        TarifaJpaEntity entity = new TarifaJpaEntity();
        entity.setId("some-id");
        assertThat(entity).isNotEqualTo(null);
    }

    @Test
    void equals_ShouldReturnFalseForDifferentClass() {
        TarifaJpaEntity entity = new TarifaJpaEntity();
        entity.setId("some-id");
        assertThat(entity).isNotEqualTo("some-string");
    }
}