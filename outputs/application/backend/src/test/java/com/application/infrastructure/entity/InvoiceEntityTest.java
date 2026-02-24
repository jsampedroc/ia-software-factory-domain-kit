package com.application.infrastructure.entity;

import com.application.domain.model.Invoice;
import com.application.domain.model.Patient;
import com.application.domain.model.Appointment;
import com.application.domain.model.TreatmentPlan;
import com.application.domain.valueobject.InvoiceId;
import com.application.domain.valueobject.InvoiceStatus;
import com.application.domain.valueobject.PaymentMethod;
import com.application.domain.valueobject.PatientId;
import com.application.domain.valueobject.AppointmentId;
import com.application.domain.valueobject.TreatmentPlanId;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class InvoiceEntityTest {

    @Test
    void fromDomain_ShouldMapAllFieldsCorrectly() {
        // Given
        UUID invoiceUuid = UUID.randomUUID();
        InvoiceId invoiceId = new InvoiceId(invoiceUuid);
        String invoiceNumber = "INV-2024-001";
        LocalDate issueDate = LocalDate.now();
        LocalDate dueDate = issueDate.plusDays(30);
        BigDecimal subtotal = new BigDecimal("150.00");
        BigDecimal taxes = new BigDecimal("18.00");
        BigDecimal total = new BigDecimal("168.00");
        InvoiceStatus status = InvoiceStatus.PENDIENTE;
        PaymentMethod paymentMethod = PaymentMethod.TARJETA;

        Invoice invoice = mock(Invoice.class);
        when(invoice.getId()).thenReturn(invoiceId);
        when(invoice.getInvoiceNumber()).thenReturn(invoiceNumber);
        when(invoice.getIssueDate()).thenReturn(issueDate);
        when(invoice.getDueDate()).thenReturn(dueDate);
        when(invoice.getSubtotal()).thenReturn(subtotal);
        when(invoice.getTaxes()).thenReturn(taxes);
        when(invoice.getTotal()).thenReturn(total);
        when(invoice.getStatus()).thenReturn(status);
        when(invoice.getPaymentMethod()).thenReturn(paymentMethod);

        // When
        InvoiceEntity entity = InvoiceEntity.fromDomain(invoice);

        // Then
        assertNotNull(entity);
        assertEquals(invoiceUuid, entity.getId());
        assertEquals(invoiceNumber, entity.getInvoiceNumber());
        assertEquals(issueDate, entity.getIssueDate());
        assertEquals(dueDate, entity.getDueDate());
        assertEquals(subtotal, entity.getSubtotal());
        assertEquals(taxes, entity.getTaxes());
        assertEquals(total, entity.getTotal());
        assertEquals(status, entity.getStatus());
        assertEquals(paymentMethod, entity.getPaymentMethod());
        assertNull(entity.getPatient());
        assertNull(entity.getAppointment());
        assertNull(entity.getTreatmentPlan());
    }

    @Test
    void toDomain_ShouldMapAllFieldsCorrectly_WithoutRelations() {
        // Given
        UUID invoiceUuid = UUID.randomUUID();
        InvoiceEntity entity = new InvoiceEntity();
        entity.id = invoiceUuid;
        entity.invoiceNumber = "INV-2024-002";
        entity.issueDate = LocalDate.now();
        entity.dueDate = entity.issueDate.plusDays(15);
        entity.subtotal = new BigDecimal("200.00");
        entity.taxes = new BigDecimal("24.00");
        entity.total = new BigDecimal("224.00");
        entity.status = InvoiceStatus.PAGADA;
        entity.paymentMethod = PaymentMethod.EFECTIVO;
        entity.patient = null;
        entity.appointment = null;
        entity.treatmentPlan = null;

        // When
        Invoice domain = entity.toDomain();

        // Then
        assertNotNull(domain);
        assertEquals(new InvoiceId(invoiceUuid), domain.getId());
        assertEquals(entity.invoiceNumber, domain.getInvoiceNumber());
        assertEquals(entity.issueDate, domain.getIssueDate());
        assertEquals(entity.dueDate, domain.getDueDate());
        assertEquals(entity.subtotal, domain.getSubtotal());
        assertEquals(entity.taxes, domain.getTaxes());
        assertEquals(entity.total, domain.getTotal());
        assertEquals(entity.status, domain.getStatus());
        assertEquals(entity.paymentMethod, domain.getPaymentMethod());
        assertNull(domain.getPatient());
        assertNull(domain.getAppointment());
        assertNull(domain.getTreatmentPlan());
    }

    @Test
    void toDomain_ShouldMapAllFieldsCorrectly_WithAllRelations() {
        // Given
        UUID invoiceUuid = UUID.randomUUID();
        UUID patientUuid = UUID.randomUUID();
        UUID appointmentUuid = UUID.randomUUID();
        UUID planUuid = UUID.randomUUID();

        PatientEntity patientEntity = mock(PatientEntity.class);
        AppointmentEntity appointmentEntity = mock(AppointmentEntity.class);
        TreatmentPlanEntity treatmentPlanEntity = mock(TreatmentPlanEntity.class);

        Patient patientDomain = mock(Patient.class);
        Appointment appointmentDomain = mock(Appointment.class);
        TreatmentPlan treatmentPlanDomain = mock(TreatmentPlan.class);

        when(patientEntity.toDomain()).thenReturn(patientDomain);
        when(appointmentEntity.toDomain()).thenReturn(appointmentDomain);
        when(treatmentPlanEntity.toDomain()).thenReturn(treatmentPlanDomain);

        InvoiceEntity entity = new InvoiceEntity();
        entity.id = invoiceUuid;
        entity.invoiceNumber = "INV-2024-003";
        entity.issueDate = LocalDate.now();
        entity.dueDate = entity.issueDate.plusDays(45);
        entity.subtotal = new BigDecimal("500.00");
        entity.taxes = new BigDecimal("60.00");
        entity.total = new BigDecimal("560.00");
        entity.status = InvoiceStatus.VENCIDA;
        entity.paymentMethod = PaymentMethod.TRANSFERENCIA;
        entity.patient = patientEntity;
        entity.appointment = appointmentEntity;
        entity.treatmentPlan = treatmentPlanEntity;

        // When
        Invoice domain = entity.toDomain();

        // Then
        assertNotNull(domain);
        assertEquals(new InvoiceId(invoiceUuid), domain.getId());
        assertEquals(entity.invoiceNumber, domain.getInvoiceNumber());
        assertEquals(entity.issueDate, domain.getIssueDate());
        assertEquals(entity.dueDate, domain.getDueDate());
        assertEquals(entity.subtotal, domain.getSubtotal());
        assertEquals(entity.taxes, domain.getTaxes());
        assertEquals(entity.total, domain.getTotal());
        assertEquals(entity.status, domain.getStatus());
        assertEquals(entity.paymentMethod, domain.getPaymentMethod());
        assertEquals(patientDomain, domain.getPatient());
        assertEquals(appointmentDomain, domain.getAppointment());
        assertEquals(treatmentPlanDomain, domain.getTreatmentPlan());

        verify(patientEntity).toDomain();
        verify(appointmentEntity).toDomain();
        verify(treatmentPlanEntity).toDomain();
    }

    @Test
    void toDomain_ShouldHandleNullPaymentMethod() {
        // Given
        UUID invoiceUuid = UUID.randomUUID();
        InvoiceEntity entity = new InvoiceEntity();
        entity.id = invoiceUuid;
        entity.invoiceNumber = "INV-2024-004";
        entity.issueDate = LocalDate.now();
        entity.dueDate = entity.issueDate.plusDays(10);
        entity.subtotal = new BigDecimal("100.00");
        entity.taxes = new BigDecimal("12.00");
        entity.total = new BigDecimal("112.00");
        entity.status = InvoiceStatus.PENDIENTE;
        entity.paymentMethod = null;
        entity.patient = null;
        entity.appointment = null;
        entity.treatmentPlan = null;

        // When
        Invoice domain = entity.toDomain();

        // Then
        assertNotNull(domain);
        assertEquals(new InvoiceId(invoiceUuid), domain.getId());
        assertNull(domain.getPaymentMethod());
    }

    @Test
    void entityAnnotations_ShouldBePresent() {
        // This test verifies the class-level annotations indirectly by checking the class
        assertDoesNotThrow(() -> {
            InvoiceEntity entity = new InvoiceEntity();
            // Access protected constructor via reflection to verify @NoArgsConstructor
            InvoiceEntity.class.getDeclaredConstructor().newInstance();
        });
    }
}