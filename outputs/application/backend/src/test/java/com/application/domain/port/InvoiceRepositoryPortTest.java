package com.application.domain.port;

import com.application.domain.model.Invoice;
import com.application.domain.valueobject.InvoiceId;
import com.application.domain.valueobject.InvoiceStatus;
import com.application.domain.valueobject.PatientId;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class InvoiceRepositoryPortTest {

    @Mock
    private InvoiceRepositoryPort invoiceRepositoryPort;

    @Test
    void testFindByInvoiceNumber() {
        String invoiceNumber = "FAC-2024-001";
        InvoiceId invoiceId = new InvoiceId(UUID.randomUUID());
        Invoice mockInvoice = mock(Invoice.class);
        when(mockInvoice.getId()).thenReturn(invoiceId);
        when(invoiceRepositoryPort.findByInvoiceNumber(invoiceNumber)).thenReturn(Optional.of(mockInvoice));

        Optional<Invoice> result = invoiceRepositoryPort.findByInvoiceNumber(invoiceNumber);

        assertTrue(result.isPresent());
        assertEquals(invoiceId, result.get().getId());
        verify(invoiceRepositoryPort, times(1)).findByInvoiceNumber(invoiceNumber);
    }

    @Test
    void testFindByInvoiceNumber_NotFound() {
        String invoiceNumber = "FAC-2024-999";
        when(invoiceRepositoryPort.findByInvoiceNumber(invoiceNumber)).thenReturn(Optional.empty());

        Optional<Invoice> result = invoiceRepositoryPort.findByInvoiceNumber(invoiceNumber);

        assertFalse(result.isPresent());
        verify(invoiceRepositoryPort, times(1)).findByInvoiceNumber(invoiceNumber);
    }

    @Test
    void testFindByPatientId() {
        PatientId patientId = new PatientId(UUID.randomUUID());
        List<Invoice> mockInvoices = List.of(mock(Invoice.class), mock(Invoice.class));
        when(invoiceRepositoryPort.findByPatientId(patientId)).thenReturn(mockInvoices);

        List<Invoice> result = invoiceRepositoryPort.findByPatientId(patientId);

        assertEquals(2, result.size());
        verify(invoiceRepositoryPort, times(1)).findByPatientId(patientId);
    }

    @Test
    void testFindByStatus() {
        InvoiceStatus status = InvoiceStatus.PENDIENTE;
        List<Invoice> mockInvoices = List.of(mock(Invoice.class));
        when(invoiceRepositoryPort.findByStatus(status)).thenReturn(mockInvoices);

        List<Invoice> result = invoiceRepositoryPort.findByStatus(status);

        assertEquals(1, result.size());
        verify(invoiceRepositoryPort, times(1)).findByStatus(status);
    }

    @Test
    void testFindByIssueDateBetween() {
        LocalDate startDate = LocalDate.of(2024, 1, 1);
        LocalDate endDate = LocalDate.of(2024, 1, 31);
        List<Invoice> mockInvoices = List.of(mock(Invoice.class), mock(Invoice.class), mock(Invoice.class));
        when(invoiceRepositoryPort.findByIssueDateBetween(startDate, endDate)).thenReturn(mockInvoices);

        List<Invoice> result = invoiceRepositoryPort.findByIssueDateBetween(startDate, endDate);

        assertEquals(3, result.size());
        verify(invoiceRepositoryPort, times(1)).findByIssueDateBetween(startDate, endDate);
    }

    @Test
    void testFindByDueDateBeforeAndStatusNot() {
        LocalDate date = LocalDate.of(2024, 2, 1);
        InvoiceStatus excludedStatus = InvoiceStatus.PAGADA;
        List<Invoice> mockInvoices = List.of(mock(Invoice.class));
        when(invoiceRepositoryPort.findByDueDateBeforeAndStatusNot(date, excludedStatus)).thenReturn(mockInvoices);

        List<Invoice> result = invoiceRepositoryPort.findByDueDateBeforeAndStatusNot(date, excludedStatus);

        assertEquals(1, result.size());
        verify(invoiceRepositoryPort, times(1)).findByDueDateBeforeAndStatusNot(date, excludedStatus);
    }

    @Test
    void testExistsByInvoiceNumber() {
        String invoiceNumber = "FAC-2024-001";
        when(invoiceRepositoryPort.existsByInvoiceNumber(invoiceNumber)).thenReturn(true);

        boolean exists = invoiceRepositoryPort.existsByInvoiceNumber(invoiceNumber);

        assertTrue(exists);
        verify(invoiceRepositoryPort, times(1)).existsByInvoiceNumber(invoiceNumber);
    }

    @Test
    void testExistsByInvoiceNumber_NotExists() {
        String invoiceNumber = "FAC-2024-999";
        when(invoiceRepositoryPort.existsByInvoiceNumber(invoiceNumber)).thenReturn(false);

        boolean exists = invoiceRepositoryPort.existsByInvoiceNumber(invoiceNumber);

        assertFalse(exists);
        verify(invoiceRepositoryPort, times(1)).existsByInvoiceNumber(invoiceNumber);
    }

    @Test
    void testSave() {
        InvoiceId invoiceId = new InvoiceId(UUID.randomUUID());
        Invoice mockInvoice = mock(Invoice.class);
        when(mockInvoice.getId()).thenReturn(invoiceId);
        when(invoiceRepositoryPort.save(mockInvoice)).thenReturn(mockInvoice);

        Invoice savedInvoice = invoiceRepositoryPort.save(mockInvoice);

        assertNotNull(savedInvoice);
        assertEquals(invoiceId, savedInvoice.getId());
        verify(invoiceRepositoryPort, times(1)).save(mockInvoice);
    }

    @Test
    void testFindById() {
        InvoiceId invoiceId = new InvoiceId(UUID.randomUUID());
        Invoice mockInvoice = mock(Invoice.class);
        when(mockInvoice.getId()).thenReturn(invoiceId);
        when(invoiceRepositoryPort.findById(invoiceId)).thenReturn(Optional.of(mockInvoice));

        Optional<Invoice> result = invoiceRepositoryPort.findById(invoiceId);

        assertTrue(result.isPresent());
        assertEquals(invoiceId, result.get().getId());
        verify(invoiceRepositoryPort, times(1)).findById(invoiceId);
    }

    @Test
    void testFindAll() {
        List<Invoice> mockInvoices = List.of(mock(Invoice.class), mock(Invoice.class));
        when(invoiceRepositoryPort.findAll()).thenReturn(mockInvoices);

        List<Invoice> result = invoiceRepositoryPort.findAll();

        assertEquals(2, result.size());
        verify(invoiceRepositoryPort, times(1)).findAll();
    }

    @Test
    void testDelete() {
        InvoiceId invoiceId = new InvoiceId(UUID.randomUUID());
        Invoice mockInvoice = mock(Invoice.class);
        when(mockInvoice.getId()).thenReturn(invoiceId);

        invoiceRepositoryPort.delete(mockInvoice);

        verify(invoiceRepositoryPort, times(1)).delete(mockInvoice);
    }

    @Test
    void testDeleteById() {
        InvoiceId invoiceId = new InvoiceId(UUID.randomUUID());

        invoiceRepositoryPort.deleteById(invoiceId);

        verify(invoiceRepositoryPort, times(1)).deleteById(invoiceId);
    }

    @Test
    void testCount() {
        when(invoiceRepositoryPort.count()).thenReturn(5L);

        long count = invoiceRepositoryPort.count();

        assertEquals(5L, count);
        verify(invoiceRepositoryPort, times(1)).count();
    }

    @Test
    void testExistsById() {
        InvoiceId invoiceId = new InvoiceId(UUID.randomUUID());
        when(invoiceRepositoryPort.existsById(invoiceId)).thenReturn(true);

        boolean exists = invoiceRepositoryPort.existsById(invoiceId);

        assertTrue(exists);
        verify(invoiceRepositoryPort, times(1)).existsById(invoiceId);
    }
}