package com.application.infrastructure.persistence.jpa.facturacion;

import com.application.domain.model.facturacion.Tarifa;
import com.application.domain.valueobject.facturacion.Dinero;
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
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TarifaJpaRepositoryTest {

    @Mock
    private TarifaJpaRepository tarifaJpaRepository;

    private TarifaJpaEntity tarifaActivaEntity;
    private TarifaJpaEntity tarifaInactivaEntity;
    private Tarifa tarifaActivaDomain;
    private Tarifa tarifaInactivaDomain;

    @BeforeEach
    void setUp() {
        tarifaActivaEntity = new TarifaJpaEntity();
        tarifaActivaEntity.setId(1L);
        tarifaActivaEntity.setNombre("Tarifa Básica");
        tarifaActivaEntity.setDescripcion("Tarifa estándar");
        tarifaActivaEntity.setPrecioMensual(new BigDecimal("300.00"));
        tarifaActivaEntity.setActivo(true);
        tarifaActivaEntity.setFechaInicio(LocalDate.now().minusMonths(1));
        tarifaActivaEntity.setFechaFin(LocalDate.now().plusMonths(11));

        tarifaInactivaEntity = new TarifaJpaEntity();
        tarifaInactivaEntity.setId(2L);
        tarifaInactivaEntity.setNombre("Tarifa Antigua");
        tarifaInactivaEntity.setDescripcion("Tarifa antigua");
        tarifaInactivaEntity.setPrecioMensual(new BigDecimal("250.00"));
        tarifaInactivaEntity.setActivo(false);
        tarifaInactivaEntity.setFechaInicio(LocalDate.now().minusMonths(12));
        tarifaInactivaEntity.setFechaFin(LocalDate.now().minusMonths(1));

        Dinero dineroActivo = new Dinero(new BigDecimal("300.00"), "EUR");
        tarifaActivaDomain = Tarifa.builder()
                .id(Tarifa.TarifaId.of(1L))
                .nombre("Tarifa Básica")
                .descripcion("Tarifa estándar")
                .precioMensual(dineroActivo)
                .activo(true)
                .fechaInicio(LocalDate.now().minusMonths(1))
                .fechaFin(LocalDate.now().plusMonths(11))
                .build();

        Dinero dineroInactivo = new Dinero(new BigDecimal("250.00"), "EUR");
        tarifaInactivaDomain = Tarifa.builder()
                .id(Tarifa.TarifaId.of(2L))
                .nombre("Tarifa Antigua")
                .descripcion("Tarifa antigua")
                .precioMensual(dineroInactivo)
                .activo(false)
                .fechaInicio(LocalDate.now().minusMonths(12))
                .fechaFin(LocalDate.now().minusMonths(1))
                .build();
    }

    @Test
    void findById_shouldReturnTarifaWhenExists() {
        when(tarifaJpaRepository.findById(1L)).thenReturn(Optional.of(tarifaActivaEntity));

        Optional<TarifaJpaEntity> result = tarifaJpaRepository.findById(1L);

        assertThat(result).isPresent();
        assertThat(result.get().getId()).isEqualTo(1L);
        assertThat(result.get().getNombre()).isEqualTo("Tarifa Básica");
        verify(tarifaJpaRepository, times(1)).findById(1L);
    }

    @Test
    void findById_shouldReturnEmptyWhenNotExists() {
        when(tarifaJpaRepository.findById(999L)).thenReturn(Optional.empty());

        Optional<TarifaJpaEntity> result = tarifaJpaRepository.findById(999L);

        assertThat(result).isEmpty();
        verify(tarifaJpaRepository, times(1)).findById(999L);
    }

    @Test
    void save_shouldPersistTarifa() {
        when(tarifaJpaRepository.save(any(TarifaJpaEntity.class))).thenReturn(tarifaActivaEntity);

        TarifaJpaEntity savedEntity = tarifaJpaRepository.save(tarifaActivaEntity);

        assertThat(savedEntity).isNotNull();
        assertThat(savedEntity.getId()).isEqualTo(1L);
        verify(tarifaJpaRepository, times(1)).save(tarifaActivaEntity);
    }

    @Test
    void deleteById_shouldDeleteTarifa() {
        doNothing().when(tarifaJpaRepository).deleteById(1L);

        tarifaJpaRepository.deleteById(1L);

        verify(tarifaJpaRepository, times(1)).deleteById(1L);
    }

    @Test
    void findByActivoTrue_shouldReturnActiveTarifas() {
        List<TarifaJpaEntity> activeList = List.of(tarifaActivaEntity);
        when(tarifaJpaRepository.findByActivoTrue()).thenReturn(activeList);

        List<TarifaJpaEntity> result = tarifaJpaRepository.findByActivoTrue();

        assertThat(result).hasSize(1);
        assertThat(result.get(0).getActivo()).isTrue();
        verify(tarifaJpaRepository, times(1)).findByActivoTrue();
    }

    @Test
    void findByNombreContainingIgnoreCase_shouldReturnMatchingTarifas() {
        List<TarifaJpaEntity> matchingList = List.of(tarifaActivaEntity, tarifaInactivaEntity);
        when(tarifaJpaRepository.findByNombreContainingIgnoreCase("tarifa")).thenReturn(matchingList);

        List<TarifaJpaEntity> result = tarifaJpaRepository.findByNombreContainingIgnoreCase("tarifa");

        assertThat(result).hasSize(2);
        verify(tarifaJpaRepository, times(1)).findByNombreContainingIgnoreCase("tarifa");
    }

    @Test
    void findByPrecioMensualGreaterThanEqual_shouldReturnFilteredTarifas() {
        BigDecimal minPrice = new BigDecimal("200.00");
        List<TarifaJpaEntity> filteredList = List.of(tarifaActivaEntity, tarifaInactivaEntity);
        when(tarifaJpaRepository.findByPrecioMensualGreaterThanEqual(minPrice)).thenReturn(filteredList);

        List<TarifaJpaEntity> result = tarifaJpaRepository.findByPrecioMensualGreaterThanEqual(minPrice);

        assertThat(result).hasSize(2);
        verify(tarifaJpaRepository, times(1)).findByPrecioMensualGreaterThanEqual(minPrice);
    }

    @Test
    void findByFechaInicioBetween_shouldReturnTarifasInDateRange() {
        LocalDate start = LocalDate.now().minusMonths(2);
        LocalDate end = LocalDate.now().plusMonths(1);
        List<TarifaJpaEntity> inRangeList = List.of(tarifaActivaEntity);
        when(tarifaJpaRepository.findByFechaInicioBetween(start, end)).thenReturn(inRangeList);

        List<TarifaJpaEntity> result = tarifaJpaRepository.findByFechaInicioBetween(start, end);

        assertThat(result).hasSize(1);
        assertThat(result.get(0).getId()).isEqualTo(1L);
        verify(tarifaJpaRepository, times(1)).findByFechaInicioBetween(start, end);
    }

    @Test
    void findByActivoTrue_withPageable_shouldReturnPagedActiveTarifas() {
        Pageable pageable = PageRequest.of(0, 10);
        Page<TarifaJpaEntity> pagedResult = new PageImpl<>(List.of(tarifaActivaEntity));
        when(tarifaJpaRepository.findByActivoTrue(eq(pageable))).thenReturn(pagedResult);

        Page<TarifaJpaEntity> result = tarifaJpaRepository.findByActivoTrue(pageable);

        assertThat(result).hasSize(1);
        assertThat(result.getContent().get(0).getActivo()).isTrue();
        verify(tarifaJpaRepository, times(1)).findByActivoTrue(pageable);
    }

    @Test
    void existsByNombreAndActivoTrue_shouldReturnTrueWhenActiveTarifaWithNameExists() {
        when(tarifaJpaRepository.existsByNombreAndActivoTrue("Tarifa Básica")).thenReturn(true);

        boolean exists = tarifaJpaRepository.existsByNombreAndActivoTrue("Tarifa Básica");

        assertThat(exists).isTrue();
        verify(tarifaJpaRepository, times(1)).existsByNombreAndActivoTrue("Tarifa Básica");
    }

    @Test
    void countByActivoTrue_shouldReturnCountOfActiveTarifas() {
        when(tarifaJpaRepository.countByActivoTrue()).thenReturn(5L);

        long count = tarifaJpaRepository.countByActivoTrue();

        assertThat(count).isEqualTo(5L);
        verify(tarifaJpaRepository, times(1)).countByActivoTrue();
    }
}