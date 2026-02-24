package com.application.infrastructure.adapter;

import com.application.domain.model.Invoice;
import com.application.domain.model.Patient;
import com.application.domain.model.Dentist;
import com.application.domain.model.Appointment;
import com.application.domain.valueobject.InvoiceId;
import com.application.domain.valueobject.PatientId;
import com.application.domain.valueobject.DentistId;
import com.application.domain.valueobject.AppointmentId;
import com.application.domain.valueobject.InvoiceStatus;
import com.application.domain.valueobject.PaymentMethod;
import com.application.infrastructure.entity.InvoiceEntity;
import com.application.infrastructure.entity.PatientEntity;
import com.application.infrastructure.entity.DentistEntity;
import com.application.infrastructure.entity.AppointmentEntity;
import com.application.infrastructure.repository.InvoiceJpaRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
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
class InvoiceJpaAdapterTest {

    @Mock
    private InvoiceJpaRepository invoiceJpaRepository;

    @Spy
    private InvoiceJpaAdapter.InvoiceEntityMapper mapper = new InvoiceJpaAdapter.InvoiceEntityMapper(
            new PatientJpaAdapter.PatientEntityMapper(),
            new DentistJpaAdapter.DentistEntityMapper(),
            new AppointmentJpaAdapter.AppointmentEntityMapper()
    );

    @InjectMocks
    private InvoiceJpaAdapter invoiceJpaAdapter;

    @Test
    void save_ShouldSaveInvoiceAndReturnDomain() {
        // Given
        InvoiceId invoiceId = new InvoiceId(UUID.randomUUID());
        PatientId patientId = new PatientId(UUID.randomUUID());
        DentistId dentistId = new DentistId(UUID.randomUUID());
        AppointmentId appointmentId = new AppointmentId(UUID.randomUUID());

        Patient patient = Patient.create(
                patientId,
                "12345678A",
                "Juan",
                "Pérez",
                LocalDate.of(1990, 1, 1),
                "600123456",
                "juan@email.com",
                "Calle Falsa 123",
                true
        );
        Dentist dentist = Dentist.create(
                dentistId,
                "LM-12345",
                "Ana",
                "García",
                "600987654",
                "ana@clinic.com",
                LocalDate.of(2020, 5, 1),
                true
        );
        Appointment appointment = Appointment.create(
                appointmentId,
                patient,
                dentist,
                null,
                null,
                null,
                null,
                null,
                null
        );

        Invoice invoice = new Invoice(
                invoiceId,
                "FAC-2024-001",
                LocalDate.now(),
                LocalDate.now().plusDays(30),
                new BigDecimal("100.00"),
                new BigDecimal("21.00"),
                new BigDecimal("121.00"),
                InvoiceStatus.PENDIENTE,
                PaymentMethod.TARJETA,
                patient,
                dentist,
                appointment
        );

        InvoiceEntity entity = new InvoiceEntity();
        entity.setId(invoiceId.getValue());
        entity.setNumeroFactura("FAC-2024-001");
        entity.setPatient(new PatientEntity());
        entity.setDentist(new DentistEntity());
        entity.setAppointment(new AppointmentEntity());

        when(mapper.toEntity(any(Invoice.class))).thenReturn(entity);
        when(invoiceJpaRepository.save(any(InvoiceEntity.class))).thenReturn(entity);
        when(mapper.toDomain(any(InvoiceEntity.class))).thenReturn(invoice);

        // When
        Invoice result = invoiceJpaAdapter.save(invoice);

        // Then
        assertThat(result).isNotNull();
        assertThat(result.getId()).isEqualTo(invoiceId);
        verify(invoiceJpaRepository, times(1)).save(entity);
    }

    @Test
    void findById_WhenExists_ShouldReturnInvoice() {
        // Given
        InvoiceId invoiceId = new InvoiceId(UUID.randomUUID());
        InvoiceEntity entity = new InvoiceEntity();
        entity.setId(invoiceId.getValue());
        Invoice invoice = new Invoice(
                invoiceId,
                "FAC-2024-001",
                LocalDate.now(),
                LocalDate.now().plusDays(30),
                BigDecimal.TEN,
                BigDecimal.ONE,
                new BigDecimal("11.00"),
                InvoiceStatus.PAGADA,
                PaymentMethod.EFECTIVO,
                null,
                null,
                null
        );

        when(invoiceJpaRepository.findById(invoiceId.getValue())).thenReturn(Optional.of(entity));
        when(mapper.toDomain(entity)).thenReturn(invoice);

        // When
        Optional<Invoice> result = invoiceJpaAdapter.findById(invoiceId);

        // Then
        assertThat(result).isPresent();
        assertThat(result.get().getId()).isEqualTo(invoiceId);
        verify(invoiceJpaRepository, times(1)).findById(invoiceId.getValue());
    }

    @Test
    void findById_WhenNotExists_ShouldReturnEmpty() {
        // Given
        InvoiceId invoiceId = new InvoiceId(UUID.randomUUID());
        when(invoiceJpaRepository.findById(invoiceId.getValue())).thenReturn(Optional.empty());

        // When
        Optional<Invoice> result = invoiceJpaAdapter.findById(invoiceId);

        // Then
        assertThat(result).isEmpty();
        verify(invoiceJpaRepository, times(1)).findById(invoiceId.getValue());
        verify(mapper, never()).toDomain(any());
    }

    @Test
    void findAll_ShouldReturnListOfInvoices() {
        // Given
        InvoiceEntity entity1 = new InvoiceEntity();
        entity1.setId(UUID.randomUUID());
        InvoiceEntity entity2 = new InvoiceEntity();
        entity2.setId(UUID.randomUUID());
        List<InvoiceEntity> entities = List.of(entity1, entity2);

        Invoice invoice1 = new Invoice(
                new InvoiceId(entity1.getId()),
                "FAC-001",
                LocalDate.now(),
                LocalDate.now().plusDays(30),
                BigDecimal.TEN,
                BigDecimal.ONE,
                new BigDecimal("11.00"),
                InvoiceStatus.PENDIENTE,
                PaymentMethod.TRANSFERENCIA,
                null,
                null,
                null
        );
        Invoice invoice2 = new Invoice(
                new InvoiceId(entity2.getId()),
                "FAC-002",
                LocalDate.now(),
                LocalDate.now().plusDays(30),
                new BigDecimal("200.00"),
                new BigDecimal("42.00"),
                new BigDecimal("242.00"),
                InvoiceStatus.PAGADA,
                PaymentMethod.TARJETA,
                null,
                null,
                null
        );

        when(invoiceJpaRepository.findAll()).thenReturn(entities);
        when(mapper.toDomain(entity1)).thenReturn(invoice1);
        when(mapper.toDomain(entity2)).thenReturn(invoice2);

        // When
        List<Invoice> result = invoiceJpaAdapter.findAll();

        // Then
        assertThat(result).hasSize(2);
        assertThat(result.get(0).getNumeroFactura()).isEqualTo("FAC-001");
        assertThat(result.get(1).getNumeroFactura()).isEqualTo("FAC-002");
        verify(invoiceJpaRepository, times(1)).findAll();
    }

    @Test
    void deleteById_ShouldCallRepositoryDelete() {
        // Given
        InvoiceId invoiceId = new InvoiceId(UUID.randomUUID());

        // When
        invoiceJpaAdapter.deleteById(invoiceId);

        // Then
        verify(invoiceJpaRepository, times(1)).deleteById(invoiceId.getValue());
    }

    @Test
    void existsById_ShouldReturnTrueWhenExists() {
        // Given
        InvoiceId invoiceId = new InvoiceId(UUID.randomUUID());
        when(invoiceJpaRepository.existsById(invoiceId.getValue())).thenReturn(true);

        // When
        boolean result = invoiceJpaAdapter.existsById(invoiceId);

        // Then
        assertThat(result).isTrue();
        verify(invoiceJpaRepository, times(1)).existsById(invoiceId.getValue());
    }

    @Test
    void findByPatientId_ShouldReturnInvoicesForPatient() {
        // Given
        PatientId patientId = new PatientId(UUID.randomUUID());
        InvoiceEntity entity = new InvoiceEntity();
        entity.setId(UUID.randomUUID());
        List<InvoiceEntity> entities = List.of(entity);

        Invoice invoice = new Invoice(
                new InvoiceId(entity.getId()),
                "FAC-PAT-001",
                LocalDate.now(),
                LocalDate.now().plusDays(30),
                BigDecimal.TEN,
                BigDecimal.ONE,
                new BigDecimal("11.00"),
                InvoiceStatus.PENDIENTE,
                PaymentMethod.EFECTIVO,
                null,
                null,
                null
        );

        when(invoiceJpaRepository.findByPatientId(patientId.getValue())).thenReturn(entities);
        when(mapper.toDomain(entity)).thenReturn(invoice);

        // When
        List<Invoice> result = invoiceJpaAdapter.findByPatientId(patientId);

        // Then
        assertThat(result).hasSize(1);
        assertThat(result.get(0).getId().getValue()).isEqualTo(entity.getId());
        verify(invoiceJpaRepository, times(1)).findByPatientId(patientId.getValue());
    }

    @Test
    void findByStatus_ShouldReturnInvoicesWithStatus() {
        // Given
        InvoiceStatus status = InvoiceStatus.VENCIDA;
        InvoiceEntity entity = new InvoiceEntity();
        entity.setId(UUID.randomUUID());
        List<InvoiceEntity> entities = List.of(entity);

        Invoice invoice = new Invoice(
                new InvoiceId(entity.getId()),
                "FAC-VENC-001",
                LocalDate.now().minusDays(60),
                LocalDate.now().minusDays(30),
                new BigDecimal("50.00"),
                new BigDecimal("10.50"),
                new BigDecimal("60.50"),
                InvoiceStatus.VENCIDA,
                PaymentMethod.TARJETA,
                null,
                null,
                null
        );

        when(invoiceJpaRepository.findByStatus(status)).thenReturn(entities);
        when(mapper.toDomain(entity)).thenReturn(invoice);

        // When
        List<Invoice> result = invoiceJpaAdapter.findByStatus(status);

        // Then
        assertThat(result).hasSize(1);
        assertThat(result.get(0).getEstado()).isEqualTo(InvoiceStatus.VENCIDA);
        verify(invoiceJpaRepository, times(1)).findByStatus(status);
    }

    @Test
    void findByInvoiceNumber_WhenExists_ShouldReturnInvoice() {
        // Given
        String invoiceNumber = "FAC-2024-1001";
        InvoiceEntity entity = new InvoiceEntity();
        entity.setId(UUID.randomUUID());
        entity.setNumeroFactura(invoiceNumber);

        Invoice invoice = new Invoice(
                new InvoiceId(entity.getId()),
                invoiceNumber,
                LocalDate.now(),
                LocalDate.now().plusDays(30),
                new BigDecimal("150.00"),
                new BigDecimal("31.50"),
                new BigDecimal("181.50"),
                InvoiceStatus.PAGADA,
                PaymentMethod.TRANSFERENCIA,
                null,
                null,
                null
        );

        when(invoiceJpaRepository.findByNumeroFactura(invoiceNumber)).thenReturn(Optional.of(entity));
        when(mapper.toDomain(entity)).thenReturn(invoice);

        // When
        Optional<Invoice> result = invoiceJpaAdapter.findByInvoiceNumber(invoiceNumber);

        // Then
        assertThat(result).isPresent();
        assertThat(result.get().getNumeroFactura()).isEqualTo(invoiceNumber);
        verify(invoiceJpaRepository, times(1)).findByNumeroFactura(invoiceNumber);
    }

    @Test
    void findByInvoiceNumber_WhenNotExists_ShouldReturnEmpty() {
        // Given
        String invoiceNumber = "FAC-NONEXISTENT";
        when(invoiceJpaRepository.findByNumeroFactura(invoiceNumber)).thenReturn(Optional.empty());

        // When
        Optional<Invoice> result = invoiceJpaAdapter.findByInvoiceNumber(invoiceNumber);

        // Then
        assertThat(result).isEmpty();
        verify(invoiceJpaRepository, times(1)).findByNumeroFactura(invoiceNumber);
        verify(mapper, never()).toDomain(any());
    }
}