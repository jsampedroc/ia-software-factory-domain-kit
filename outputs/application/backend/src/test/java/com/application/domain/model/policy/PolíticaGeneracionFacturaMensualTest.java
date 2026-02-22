package com.application.domain.model.policy;

import com.application.domain.model.alumno.Alumno;
import com.application.domain.model.event.FacturaGenerada;
import com.application.domain.model.facturacion.Factura;
import com.application.domain.model.facturacion.Tarifa;
import com.application.domain.service.facturacion.FacturacionService;
import com.application.domain.shared.DomainEventPublisher;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PolíticaGeneracionFacturaMensualTest {

    @Mock
    private FacturacionService facturacionService;
    @Mock
    private DomainEventPublisher eventPublisher;
    @Captor
    private ArgumentCaptor<Factura> facturaCaptor;
    @Captor
    private ArgumentCaptor<FacturaGenerada> eventCaptor;

    private PolíticaGeneracionFacturaMensual politica;

    @BeforeEach
    void setUp() {
        politica = new PolíticaGeneracionFacturaMensual(facturacionService, eventPublisher);
    }

    @Test
    void ejecutar_ConAlumnosActivosYTarifas_GeneraFacturasYPublishesEventos() {
        // Given
        LocalDate ultimoDiaMes = LocalDate.of(2024, 1, 31);
        Alumno alumno1 = mock(Alumno.class);
        Alumno alumno2 = mock(Alumno.class);
        Alumno alumnoInactivo = mock(Alumno.class);
        Tarifa tarifa1 = mock(Tarifa.class);
        Tarifa tarifa2 = mock(Tarifa.class);

        when(alumno1.getId()).thenReturn(new Alumno.AlumnoId(UUID.randomUUID()));
        when(alumno1.isActivo()).thenReturn(true);
        when(alumno2.getId()).thenReturn(new Alumno.AlumnoId(UUID.randomUUID()));
        when(alumno2.isActivo()).thenReturn(true);
        when(alumnoInactivo.isActivo()).thenReturn(false);

        when(facturacionService.obtenerAlumnosActivos()).thenReturn(List.of(alumno1, alumno2, alumnoInactivo));
        when(facturacionService.obtenerTarifaVigenteParaAlumno(alumno1.getId(), ultimoDiaMes)).thenReturn(Optional.of(tarifa1));
        when(facturacionService.obtenerTarifaVigenteParaAlumno(alumno2.getId(), ultimoDiaMes)).thenReturn(Optional.of(tarifa2));

        Factura facturaMock1 = mock(Factura.class);
        Factura facturaMock2 = mock(Factura.class);
        when(facturaMock1.getId()).thenReturn(new Factura.FacturaId(UUID.randomUUID()));
        when(facturaMock1.getNumeroFactura()).thenReturn("FAC-2024-001");
        when(facturaMock1.getTotal()).thenReturn(new BigDecimal("150.00"));
        when(facturaMock1.getFechaVencimiento()).thenReturn(ultimoDiaMes.plusDays(15));
        when(facturaMock2.getId()).thenReturn(new Factura.FacturaId(UUID.randomUUID()));
        when(facturaMock2.getNumeroFactura()).thenReturn("FAC-2024-002");

        when(facturacionService.generarFacturaMensualParaAlumno(eq(alumno1.getId()), eq(tarifa1), eq(ultimoDiaMes))).thenReturn(facturaMock1);
        when(facturacionService.generarFacturaMensualParaAlumno(eq(alumno2.getId()), eq(tarifa2), eq(ultimoDiaMes))).thenReturn(facturaMock2);

        // When
        politica.ejecutar(ultimoDiaMes);

        // Then
        verify(facturacionService, times(2)).guardarFactura(facturaCaptor.capture());
        List<Factura> facturasGuardadas = facturaCaptor.getAllValues();
        assertThat(facturasGuardadas).containsExactlyInAnyOrder(facturaMock1, facturaMock2);

        verify(eventPublisher, times(2)).publish(eventCaptor.capture());
        List<FacturaGenerada> eventosPublicados = eventCaptor.getAllValues();
        assertThat(eventosPublicados).hasSize(2);
        assertThat(eventosPublicados.get(0).facturaId()).isEqualTo(facturaMock1.getId().value());
        assertThat(eventosPublicados.get(0).numeroFactura()).isEqualTo("FAC-2024-001");
        assertThat(eventosPublicados.get(0).total()).isEqualTo(new BigDecimal("150.00"));
    }

    @Test
    void ejecutar_ConAlumnoSinTarifaVigente_NoGeneraFactura() {
        // Given
        LocalDate ultimoDiaMes = LocalDate.of(2024, 1, 31);
        Alumno alumno = mock(Alumno.class);
        when(alumno.getId()).thenReturn(new Alumno.AlumnoId(UUID.randomUUID()));
        when(alumno.isActivo()).thenReturn(true);

        when(facturacionService.obtenerAlumnosActivos()).thenReturn(List.of(alumno));
        when(facturacionService.obtenerTarifaVigenteParaAlumno(alumno.getId(), ultimoDiaMes)).thenReturn(Optional.empty());

        // When
        politica.ejecutar(ultimoDiaMes);

        // Then
        verify(facturacionService, never()).generarFacturaMensualParaAlumno(any(), any(), any());
        verify(facturacionService, never()).guardarFactura(any());
        verify(eventPublisher, never()).publish(any(FacturaGenerada.class));
    }

    @Test
    void ejecutar_SinAlumnosActivos_NoGeneraFacturas() {
        // Given
        LocalDate ultimoDiaMes = LocalDate.of(2024, 1, 31);
        when(facturacionService.obtenerAlumnosActivos()).thenReturn(List.of());

        // When
        politica.ejecutar(ultimoDiaMes);

        // Then
        verify(facturacionService, never()).obtenerTarifaVigenteParaAlumno(any(), any());
        verify(facturacionService, never()).generarFacturaMensualParaAlumno(any(), any(), any());
        verify(facturacionService, never()).guardarFactura(any());
        verify(eventPublisher, never()).publish(any(FacturaGenerada.class));
    }

    @Test
    void ejecutar_ConErrorAlGuardarFactura_NoPublicaEventoParaEsaFactura() {
        // Given
        LocalDate ultimoDiaMes = LocalDate.of(2024, 1, 31);
        Alumno alumno = mock(Alumno.class);
        Tarifa tarifa = mock(Tarifa.class);
        when(alumno.getId()).thenReturn(new Alumno.AlumnoId(UUID.randomUUID()));
        when(alumno.isActivo()).thenReturn(true);

        when(facturacionService.obtenerAlumnosActivos()).thenReturn(List.of(alumno));
        when(facturacionService.obtenerTarifaVigenteParaAlumno(alumno.getId(), ultimoDiaMes)).thenReturn(Optional.of(tarifa));

        Factura facturaMock = mock(Factura.class);
        when(facturacionService.generarFacturaMensualParaAlumno(eq(alumno.getId()), eq(tarifa), eq(ultimoDiaMes))).thenReturn(facturaMock);
        doThrow(new RuntimeException("Error de persistencia")).when(facturacionService).guardarFactura(facturaMock);

        // When & Then - Se espera que la excepción sea manejada internamente y no se publique el evento
        politica.ejecutar(ultimoDiaMes);

        verify(facturacionService).guardarFactura(facturaMock);
        verify(eventPublisher, never()).publish(any(FacturaGenerada.class));
    }
}