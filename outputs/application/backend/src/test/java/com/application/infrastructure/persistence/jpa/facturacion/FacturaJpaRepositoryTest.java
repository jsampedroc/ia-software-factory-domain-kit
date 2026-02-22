package com.application.infrastructure.persistence.jpa.facturacion;

import com.application.domain.model.facturacion.Factura;
import com.application.domain.model.facturacion.FacturaId;
import com.application.domain.valueobject.facturacion.Dinero;
import com.application.domain.valueobject.facturacion.Periodo;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class FacturaJpaRepositoryTest {

    @Mock
    private FacturaJpaRepository facturaJpaRepository;

    private FacturaJpaEntity facturaEntity;
    private FacturaId facturaId;
    private UUID alumnoId;

    @BeforeEach
    void setUp() {
        facturaId = new FacturaId(UUID.randomUUID());
        alumnoId = UUID.randomUUID();

        facturaEntity = new FacturaJpaEntity();
        facturaEntity.setId(facturaId.value());
        facturaEntity.setNumeroFactura("FAC-2024-001");
        facturaEntity.setFechaEmision(LocalDate.now());
        facturaEntity.setFechaVencimiento(LocalDate.now().plusDays(30));
        facturaEntity.setPeriodoFacturadoInicio(LocalDate.now().withDayOfMonth(1));
        facturaEntity.setPeriodoFacturadoFin(LocalDate.now().withDayOfMonth(1).plusMonths(1).minusDays(1));
        facturaEntity.setTotal(new BigDecimal("150.00"));
        facturaEntity.setEstado("GENERADA");
        facturaEntity.setConcepto("Mensualidad Enero 2024");
        facturaEntity.setAlumnoId(alumnoId);
        facturaEntity.setCreatedAt(LocalDateTime.now());
    }

    @Test
    void testFindByNumeroFactura() {
        when(facturaJpaRepository.findByNumeroFactura("FAC-2024-001")).thenReturn(Optional.of(facturaEntity));

        Optional<FacturaJpaEntity> result = facturaJpaRepository.findByNumeroFactura("FAC-2024-001");

        assertThat(result).isPresent();
        assertThat(result.get().getNumeroFactura()).isEqualTo("FAC-2024-001");
        verify(facturaJpaRepository, times(1)).findByNumeroFactura("FAC-2024-001");
    }

    @Test
    void testFindByNumeroFactura_NotFound() {
        when(facturaJpaRepository.findByNumeroFactura("FAC-9999-999")).thenReturn(Optional.empty());

        Optional<FacturaJpaEntity> result = facturaJpaRepository.findByNumeroFactura("FAC-9999-999");

        assertThat(result).isEmpty();
        verify(facturaJpaRepository, times(1)).findByNumeroFactura("FAC-9999-999");
    }

    @Test
    void testFindByAlumnoId() {
        Pageable pageable = PageRequest.of(0, 10);
        Page<FacturaJpaEntity> page = new PageImpl<>(List.of(facturaEntity), pageable, 1);
        when(facturaJpaRepository.findByAlumnoId(eq(alumnoId), any(Pageable.class))).thenReturn(page);

        Page<FacturaJpaEntity> result = facturaJpaRepository.findByAlumnoId(alumnoId, pageable);

        assertThat(result).isNotNull();
        assertThat(result.getContent()).hasSize(1);
        assertThat(result.getContent().get(0).getAlumnoId()).isEqualTo(alumnoId);
        verify(facturaJpaRepository, times(1)).findByAlumnoId(alumnoId, pageable);
    }

    @Test
    void testFindByEstado() {
        Pageable pageable = PageRequest.of(0, 10);
        Page<FacturaJpaEntity> page = new PageImpl<>(List.of(facturaEntity), pageable, 1);
        when(facturaJpaRepository.findByEstado(eq("GENERADA"), any(Pageable.class))).thenReturn(page);

        Page<FacturaJpaEntity> result = facturaJpaRepository.findByEstado("GENERADA", pageable);

        assertThat(result).isNotNull();
        assertThat(result.getContent()).hasSize(1);
        assertThat(result.getContent().get(0).getEstado()).isEqualTo("GENERADA");
        verify(facturaJpaRepository, times(1)).findByEstado("GENERADA", pageable);
    }

    @Test
    void testFindByFechaVencimientoBeforeAndEstadoNot() {
        LocalDate fechaLimite = LocalDate.now().plusDays(1);
        List<FacturaJpaEntity> facturas = List.of(facturaEntity);
        when(facturaJpaRepository.findByFechaVencimientoBeforeAndEstadoNot(fechaLimite, "PAGADA")).thenReturn(facturas);

        List<FacturaJpaEntity> result = facturaJpaRepository.findByFechaVencimientoBeforeAndEstadoNot(fechaLimite, "PAGADA");

        assertThat(result).isNotNull();
        assertThat(result).hasSize(1);
        assertThat(result.get(0).getFechaVencimiento()).isBefore(fechaLimite);
        assertThat(result.get(0).getEstado()).isNotEqualTo("PAGADA");
        verify(facturaJpaRepository, times(1)).findByFechaVencimientoBeforeAndEstadoNot(fechaLimite, "PAGADA");
    }

    @Test
    void testExistsByNumeroFactura() {
        when(facturaJpaRepository.existsByNumeroFactura("FAC-2024-001")).thenReturn(true);

        boolean exists = facturaJpaRepository.existsByNumeroFactura("FAC-2024-001");

        assertThat(exists).isTrue();
        verify(facturaJpaRepository, times(1)).existsByNumeroFactura("FAC-2024-001");
    }

    @Test
    void testExistsByNumeroFactura_NotExists() {
        when(facturaJpaRepository.existsByNumeroFactura("FAC-9999-999")).thenReturn(false);

        boolean exists = facturaJpaRepository.existsByNumeroFactura("FAC-9999-999");

        assertThat(exists).isFalse();
        verify(facturaJpaRepository, times(1)).existsByNumeroFactura("FAC-9999-999");
    }

    @Test
    void testSave() {
        when(facturaJpaRepository.save(any(FacturaJpaEntity.class))).thenReturn(facturaEntity);

        FacturaJpaEntity savedEntity = facturaJpaRepository.save(facturaEntity);

        assertThat(savedEntity).isNotNull();
        assertThat(savedEntity.getId()).isEqualTo(facturaId.value());
        verify(facturaJpaRepository, times(1)).save(facturaEntity);
    }

    @Test
    void testFindById() {
        when(facturaJpaRepository.findById(facturaId.value())).thenReturn(Optional.of(facturaEntity));

        Optional<FacturaJpaEntity> result = facturaJpaRepository.findById(facturaId.value());

        assertThat(result).isPresent();
        assertThat(result.get().getId()).isEqualTo(facturaId.value());
        verify(facturaJpaRepository, times(1)).findById(facturaId.value());
    }

    @Test
    void testDeleteById() {
        doNothing().when(facturaJpaRepository).deleteById(facturaId.value());

        facturaJpaRepository.deleteById(facturaId.value());

        verify(facturaJpaRepository, times(1)).deleteById(facturaId.value());
    }
}