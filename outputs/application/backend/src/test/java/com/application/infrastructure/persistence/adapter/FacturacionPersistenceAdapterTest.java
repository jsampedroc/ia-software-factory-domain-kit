package com.application.infrastructure.persistence.adapter;

import com.application.domain.model.facturacion.Factura;
import com.application.domain.model.facturacion.FacturaId;
import com.application.domain.model.facturacion.LineaFactura;
import com.application.domain.model.facturacion.LineaFacturaId;
import com.application.domain.model.facturacion.Tarifa;
import com.application.domain.model.facturacion.TarifaId;
import com.application.infrastructure.persistence.jpa.facturacion.FacturaJpaEntity;
import com.application.infrastructure.persistence.jpa.facturacion.FacturaJpaRepository;
import com.application.infrastructure.persistence.jpa.facturacion.LineaFacturaJpaEntity;
import com.application.infrastructure.persistence.jpa.facturacion.LineaFacturaJpaRepository;
import com.application.infrastructure.persistence.jpa.facturacion.TarifaJpaEntity;
import com.application.infrastructure.persistence.jpa.facturacion.TarifaJpaRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class FacturacionPersistenceAdapterTest {

    @Mock
    private FacturaJpaRepository facturaJpaRepository;
    @Mock
    private LineaFacturaJpaRepository lineaFacturaJpaRepository;
    @Mock
    private TarifaJpaRepository tarifaJpaRepository;

    @InjectMocks
    private FacturacionPersistenceAdapter adapter;

    private FacturaId facturaId;
    private Factura factura;
    private FacturaJpaEntity facturaJpaEntity;

    private TarifaId tarifaId;
    private Tarifa tarifa;
    private TarifaJpaEntity tarifaJpaEntity;

    @BeforeEach
    void setUp() {
        facturaId = new FacturaId(UUID.randomUUID());
        tarifaId = new TarifaId(UUID.randomUUID());
    }

    @Test
    void saveFactura_shouldSaveAndReturnFactura() {
        factura = mock(Factura.class);
        facturaJpaEntity = mock(FacturaJpaEntity.class);
        when(factura.toJpaEntity()).thenReturn(facturaJpaEntity);
        when(facturaJpaRepository.save(facturaJpaEntity)).thenReturn(facturaJpaEntity);
        when(facturaJpaEntity.toDomain()).thenReturn(factura);

        Factura result = adapter.saveFactura(factura);

        assertThat(result).isEqualTo(factura);
        verify(facturaJpaRepository).save(facturaJpaEntity);
    }

    @Test
    void findFacturaById_shouldReturnFacturaWhenExists() {
        facturaJpaEntity = mock(FacturaJpaEntity.class);
        factura = mock(Factura.class);
        when(facturaJpaRepository.findById(facturaId.value())).thenReturn(Optional.of(facturaJpaEntity));
        when(facturaJpaEntity.toDomain()).thenReturn(factura);

        Optional<Factura> result = adapter.findFacturaById(facturaId);

        assertThat(result).isPresent().contains(factura);
    }

    @Test
    void findFacturaById_shouldReturnEmptyWhenNotExists() {
        when(facturaJpaRepository.findById(facturaId.value())).thenReturn(Optional.empty());

        Optional<Factura> result = adapter.findFacturaById(facturaId);

        assertThat(result).isEmpty();
    }

    @Test
    void findFacturasByAlumnoId_shouldReturnList() {
        UUID alumnoId = UUID.randomUUID();
        facturaJpaEntity = mock(FacturaJpaEntity.class);
        factura = mock(Factura.class);
        when(facturaJpaRepository.findByAlumnoId(alumnoId)).thenReturn(List.of(facturaJpaEntity));
        when(facturaJpaEntity.toDomain()).thenReturn(factura);

        List<Factura> result = adapter.findFacturasByAlumnoId(alumnoId);

        assertThat(result).hasSize(1).contains(factura);
    }

    @Test
    void findFacturasByEstado_shouldReturnList() {
        String estado = "GENERADA";
        facturaJpaEntity = mock(FacturaJpaEntity.class);
        factura = mock(Factura.class);
        when(facturaJpaRepository.findByEstado(estado)).thenReturn(List.of(facturaJpaEntity));
        when(facturaJpaEntity.toDomain()).thenReturn(factura);

        List<Factura> result = adapter.findFacturasByEstado(estado);

        assertThat(result).hasSize(1).contains(factura);
    }

    @Test
    void findFacturasByFechaVencimientoBeforeAndEstado_shouldReturnList() {
        LocalDate fecha = LocalDate.now();
        String estado = "PENDIENTE";
        facturaJpaEntity = mock(FacturaJpaEntity.class);
        factura = mock(Factura.class);
        when(facturaJpaRepository.findByFechaVencimientoBeforeAndEstado(fecha, estado)).thenReturn(List.of(facturaJpaEntity));
        when(facturaJpaEntity.toDomain()).thenReturn(factura);

        List<Factura> result = adapter.findFacturasByFechaVencimientoBeforeAndEstado(fecha, estado);

        assertThat(result).hasSize(1).contains(factura);
    }

    @Test
    void saveLineaFactura_shouldSaveAndReturnLineaFactura() {
        LineaFactura lineaFactura = mock(LineaFactura.class);
        LineaFacturaJpaEntity lineaJpaEntity = mock(LineaFacturaJpaEntity.class);
        when(lineaFactura.toJpaEntity()).thenReturn(lineaJpaEntity);
        when(lineaFacturaJpaRepository.save(lineaJpaEntity)).thenReturn(lineaJpaEntity);
        when(lineaJpaEntity.toDomain()).thenReturn(lineaFactura);

        LineaFactura result = adapter.saveLineaFactura(lineaFactura);

        assertThat(result).isEqualTo(lineaFactura);
        verify(lineaFacturaJpaRepository).save(lineaJpaEntity);
    }

    @Test
    void findLineasByFacturaId_shouldReturnList() {
        LineaFacturaJpaEntity lineaJpaEntity = mock(LineaFacturaJpaEntity.class);
        LineaFactura lineaFactura = mock(LineaFactura.class);
        when(lineaFacturaJpaRepository.findByFacturaId(facturaId.value())).thenReturn(List.of(lineaJpaEntity));
        when(lineaJpaEntity.toDomain()).thenReturn(lineaFactura);

        List<LineaFactura> result = adapter.findLineasByFacturaId(facturaId);

        assertThat(result).hasSize(1).contains(lineaFactura);
    }

    @Test
    void saveTarifa_shouldSaveAndReturnTarifa() {
        tarifa = mock(Tarifa.class);
        tarifaJpaEntity = mock(TarifaJpaEntity.class);
        when(tarifa.toJpaEntity()).thenReturn(tarifaJpaEntity);
        when(tarifaJpaRepository.save(tarifaJpaEntity)).thenReturn(tarifaJpaEntity);
        when(tarifaJpaEntity.toDomain()).thenReturn(tarifa);

        Tarifa result = adapter.saveTarifa(tarifa);

        assertThat(result).isEqualTo(tarifa);
        verify(tarifaJpaRepository).save(tarifaJpaEntity);
    }

    @Test
    void findTarifaById_shouldReturnTarifaWhenExists() {
        tarifaJpaEntity = mock(TarifaJpaEntity.class);
        tarifa = mock(Tarifa.class);
        when(tarifaJpaRepository.findById(tarifaId.value())).thenReturn(Optional.of(tarifaJpaEntity));
        when(tarifaJpaEntity.toDomain()).thenReturn(tarifa);

        Optional<Tarifa> result = adapter.findTarifaById(tarifaId);

        assertThat(result).isPresent().contains(tarifa);
    }

    @Test
    void findTarifaById_shouldReturnEmptyWhenNotExists() {
        when(tarifaJpaRepository.findById(tarifaId.value())).thenReturn(Optional.empty());

        Optional<Tarifa> result = adapter.findTarifaById(tarifaId);

        assertThat(result).isEmpty();
    }

    @Test
    void findTarifasActivas_shouldReturnList() {
        tarifaJpaEntity = mock(TarifaJpaEntity.class);
        tarifa = mock(Tarifa.class);
        when(tarifaJpaRepository.findByActivoTrue()).thenReturn(List.of(tarifaJpaEntity));
        when(tarifaJpaEntity.toDomain()).thenReturn(tarifa);

        List<Tarifa> result = adapter.findTarifasActivas();

        assertThat(result).hasSize(1).contains(tarifa);
    }

    @Test
    void findTarifaActivaByNombre_shouldReturnTarifaWhenExists() {
        String nombre = "Tarifa Básica";
        tarifaJpaEntity = mock(TarifaJpaEntity.class);
        tarifa = mock(Tarifa.class);
        when(tarifaJpaRepository.findByNombreAndActivoTrue(nombre)).thenReturn(Optional.of(tarifaJpaEntity));
        when(tarifaJpaEntity.toDomain()).thenReturn(tarifa);

        Optional<Tarifa> result = adapter.findTarifaActivaByNombre(nombre);

        assertThat(result).isPresent().contains(tarifa);
    }

    @Test
    void findTarifaActivaByNombre_shouldReturnEmptyWhenNotExists() {
        String nombre = "Inexistente";
        when(tarifaJpaRepository.findByNombreAndActivoTrue(nombre)).thenReturn(Optional.empty());

        Optional<Tarifa> result = adapter.findTarifaActivaByNombre(nombre);

        assertThat(result).isEmpty();
    }

    @Test
    void existsFacturaByNumeroFactura_shouldReturnTrueWhenExists() {
        String numero = "FAC-2024-001";
        when(facturaJpaRepository.existsByNumeroFactura(numero)).thenReturn(true);

        boolean result = adapter.existsFacturaByNumeroFactura(numero);

        assertThat(result).isTrue();
    }

    @Test
    void existsFacturaByNumeroFactura_shouldReturnFalseWhenNotExists() {
        String numero = "FAC-2024-999";
        when(facturaJpaRepository.existsByNumeroFactura(numero)).thenReturn(false);

        boolean result = adapter.existsFacturaByNumeroFactura(numero);

        assertThat(result).isFalse();
    }

    @Test
    void deleteLineasFacturaByFacturaId_shouldCallRepository() {
        adapter.deleteLineasFacturaByFacturaId(facturaId);
        verify(lineaFacturaJpaRepository).deleteByFacturaId(facturaId.value());
    }
}