package com.application.application.service;

import com.application.application.dto.InvoiceDTO;
import com.application.application.mapper.InvoiceMapper;
import com.application.domain.exception.DomainException;
import com.application.domain.model.Invoice;
import com.application.domain.model.Patient;
import com.application.domain.port.InvoiceRepositoryPort;
import com.application.domain.port.PatientRepositoryPort;
import com.application.domain.valueobject.InvoiceId;
import com.application.domain.valueobject.InvoiceStatus;
import com.application.domain.valueobject.PatientId;
import com.application.domain.valueobject.PaymentMethod;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class InvoiceServiceTest {

    @Mock
    private InvoiceRepositoryPort invoiceRepositoryPort;

    @Mock
    private PatientRepositoryPort patientRepositoryPort;

    @Mock
    private InvoiceMapper invoiceMapper;

    @InjectMocks
    private InvoiceService invoiceService;

    private UUID testInvoiceUuid;
    private UUID testPatientUuid;
    private InvoiceId testInvoiceId;
    private PatientId testPatientId;
    private Invoice testInvoice;
    private Patient testPatient;
    private InvoiceDTO testInvoiceDTO;

    @BeforeEach
    void setUp() {
        testInvoiceUuid = UUID.randomUUID();
        testPatientUuid = UUID.randomUUID();
        testInvoiceId = new InvoiceId(testInvoiceUuid);
        testPatientId = new PatientId(testPatientUuid);

        testPatient = mock(Patient.class);
        when(testPatient.getId()).thenReturn(testPatientId);
        when(testPatient.isActive()).thenReturn(true);

        testInvoice = mock(Invoice.class);
        when(testInvoice.getId()).thenReturn(testInvoiceId);
        when(testInvoice.getStatus()).thenReturn(InvoiceStatus.PENDIENTE);
        when(testInvoice.getPatient()).thenReturn(testPatient);
        when(testInvoice.getTotal()).thenReturn(new BigDecimal("100.00"));
        when(testInvoice.getSubtotal()).thenReturn(new BigDecimal("90.00"));
        when(testInvoice.getTaxes()).thenReturn(new BigDecimal("10.00"));
        when(testInvoice.getPaidAmount()).thenReturn(BigDecimal.ZERO);
        when(testInvoice.getIssueDate()).thenReturn(LocalDate.now());
        when(testInvoice.getDueDate()).thenReturn(LocalDate.now().plusDays(30));

        testInvoiceDTO = new InvoiceDTO();
        testInvoiceDTO.setId(testInvoiceUuid);
        testInvoiceDTO.setPatientId(testPatientUuid);
        testInvoiceDTO.setSubtotal(new BigDecimal("90.00"));
        testInvoiceDTO.setTaxes(new BigDecimal("10.00"));
        testInvoiceDTO.setTotal(new BigDecimal("100.00"));
        testInvoiceDTO.setIssueDate(LocalDate.now());
        testInvoiceDTO.setDueDate(LocalDate.now().plusDays(30));
        testInvoiceDTO.setStatus(InvoiceStatus.PENDIENTE);
    }

    @Test
    void findAll_ShouldReturnListOfInvoiceDTO() {
        List<Invoice> invoices = Collections.singletonList(testInvoice);
        List<InvoiceDTO> expectedDTOs = Collections.singletonList(testInvoiceDTO);

        when(invoiceRepositoryPort.findAll()).thenReturn(invoices);
        when(invoiceMapper.toDtoList(invoices)).thenReturn(expectedDTOs);

        List<InvoiceDTO> result = invoiceService.findAll();

        assertNotNull(result);
        assertEquals(1, result.size());
        verify(invoiceRepositoryPort).findAll();
        verify(invoiceMapper).toDtoList(invoices);
    }

    @Test
    void findById_WhenInvoiceExists_ShouldReturnInvoiceDTO() {
        when(invoiceRepositoryPort.findById(testInvoiceId)).thenReturn(Optional.of(testInvoice));
        when(invoiceMapper.toDto(testInvoice)).thenReturn(testInvoiceDTO);

        Optional<InvoiceDTO> result = invoiceService.findById(testInvoiceUuid);

        assertTrue(result.isPresent());
        assertEquals(testInvoiceDTO, result.get());
        verify(invoiceRepositoryPort).findById(testInvoiceId);
        verify(invoiceMapper).toDto(testInvoice);
    }

    @Test
    void findById_WhenInvoiceDoesNotExist_ShouldReturnEmptyOptional() {
        when(invoiceRepositoryPort.findById(testInvoiceId)).thenReturn(Optional.empty());

        Optional<InvoiceDTO> result = invoiceService.findById(testInvoiceUuid);

        assertFalse(result.isPresent());
        verify(invoiceRepositoryPort).findById(testInvoiceId);
        verify(invoiceMapper, never()).toDto(any());
    }

    @Test
    void findByPatientId_ShouldReturnListOfInvoiceDTO() {
        List<Invoice> invoices = Collections.singletonList(testInvoice);
        List<InvoiceDTO> expectedDTOs = Collections.singletonList(testInvoiceDTO);

        when(invoiceRepositoryPort.findByPatientId(testPatientId)).thenReturn(invoices);
        when(invoiceMapper.toDtoList(invoices)).thenReturn(expectedDTOs);

        List<InvoiceDTO> result = invoiceService.findByPatientId(testPatientUuid);

        assertNotNull(result);
        assertEquals(1, result.size());
        verify(invoiceRepositoryPort).findByPatientId(testPatientId);
        verify(invoiceMapper).toDtoList(invoices);
    }

    @Test
    void findByStatus_ShouldReturnListOfInvoiceDTO() {
        List<Invoice> invoices = Collections.singletonList(testInvoice);
        List<InvoiceDTO> expectedDTOs = Collections.singletonList(testInvoiceDTO);

        when(invoiceRepositoryPort.findByStatus(InvoiceStatus.PENDIENTE)).thenReturn(invoices);
        when(invoiceMapper.toDtoList(invoices)).thenReturn(expectedDTOs);

        List<InvoiceDTO> result = invoiceService.findByStatus(InvoiceStatus.PENDIENTE);

        assertNotNull(result);
        assertEquals(1, result.size());
        verify(invoiceRepositoryPort).findByStatus(InvoiceStatus.PENDIENTE);
        verify(invoiceMapper).toDtoList(invoices);
    }

    @Test
    void findOverdueInvoices_ShouldReturnListOfInvoiceDTO() {
        List<Invoice> invoices = Collections.singletonList(testInvoice);
        List<InvoiceDTO> expectedDTOs = Collections.singletonList(testInvoiceDTO);

        when(invoiceRepositoryPort.findOverdueInvoices(LocalDate.now())).thenReturn(invoices);
        when(invoiceMapper.toDtoList(invoices)).thenReturn(expectedDTOs);

        List<InvoiceDTO> result = invoiceService.findOverdueInvoices();

        assertNotNull(result);
        assertEquals(1, result.size());
        verify(invoiceRepositoryPort).findOverdueInvoices(LocalDate.now());
        verify(invoiceMapper).toDtoList(invoices);
    }

    @Test
    void create_WithValidData_ShouldReturnSavedInvoiceDTO() {
        when(patientRepositoryPort.findById(testPatientId)).thenReturn(Optional.of(testPatient));
        when(invoiceRepositoryPort.findOverdueInvoicesByPatientId(eq(testPatientId), any(LocalDate.class)))
                .thenReturn(Collections.emptyList());
        when(invoiceMapper.toDomain(testInvoiceDTO)).thenReturn(testInvoice);
        when(invoiceRepositoryPort.save(testInvoice)).thenReturn(testInvoice);
        when(invoiceMapper.toDto(testInvoice)).thenReturn(testInvoiceDTO);

        InvoiceDTO result = invoiceService.create(testInvoiceDTO);

        assertNotNull(result);
        verify(patientRepositoryPort).findById(testPatientId);
        verify(invoiceRepositoryPort).findOverdueInvoicesByPatientId(eq(testPatientId), any(LocalDate.class));
        verify(invoiceMapper).toDomain(testInvoiceDTO);
        verify(testInvoice).setPatient(testPatient);
        verify(invoiceRepositoryPort).save(testInvoice);
        verify(invoiceMapper).toDto(testInvoice);
    }

    @Test
    void create_WhenPatientNotFound_ShouldThrowDomainException() {
        when(patientRepositoryPort.findById(testPatientId)).thenReturn(Optional.empty());

        DomainException exception = assertThrows(DomainException.class,
                () -> invoiceService.create(testInvoiceDTO));

        assertEquals("Patient not found with id: " + testPatientUuid, exception.getMessage());
        verify(patientRepositoryPort).findById(testPatientId);
        verify(invoiceRepositoryPort, never()).save(any());
    }

    @Test
    void create_WhenPatientIsInactive_ShouldThrowDomainException() {
        when(patientRepositoryPort.findById(testPatientId)).thenReturn(Optional.of(testPatient));
        when(testPatient.isActive()).thenReturn(false);

        DomainException exception = assertThrows(DomainException.class,
                () -> invoiceService.create(testInvoiceDTO));

        assertEquals("Cannot create invoice for inactive patient", exception.getMessage());
        verify(patientRepositoryPort).findById(testPatientId);
        verify(invoiceRepositoryPort, never()).save(any());
    }

    @Test
    void create_WhenPatientHasOverdueInvoices_ShouldThrowDomainException() {
        when(patientRepositoryPort.findById(testPatientId)).thenReturn(Optional.of(testPatient));
        when(invoiceRepositoryPort.findOverdueInvoicesByPatientId(eq(testPatientId), any(LocalDate.class)))
                .thenReturn(Collections.singletonList(testInvoice));

        DomainException exception = assertThrows(DomainException.class,
                () -> invoiceService.create(testInvoiceDTO));

        assertEquals("Patient has invoices overdue for more than 60 days. Cannot create new invoice.", exception.getMessage());
        verify(patientRepositoryPort).findById(testPatientId);
        verify(invoiceRepositoryPort).findOverdueInvoicesByPatientId(eq(testPatientId), any(LocalDate.class));
        verify(invoiceRepositoryPort, never()).save(any());
    }

    @Test
    void create_WhenInvoiceDTOIsNull_ShouldThrowDomainException() {
        DomainException exception = assertThrows(DomainException.class,
                () -> invoiceService.create(null));

        assertEquals("Invoice data cannot be null", exception.getMessage());
        verify(patientRepositoryPort, never()).findById(any());
        verify(invoiceRepositoryPort, never()).save(any());
    }

    @Test
    void update_WithValidData_ShouldReturnUpdatedInvoiceDTO() {
        when(invoiceRepositoryPort.findById(testInvoiceId)).thenReturn(Optional.of(testInvoice));
        when(invoiceRepositoryPort.save(testInvoice)).thenReturn(testInvoice);
        when(invoiceMapper.toDto(testInvoice)).thenReturn(testInvoiceDTO);

        InvoiceDTO result = invoiceService.update(testInvoiceUuid, testInvoiceDTO);

        assertNotNull(result);
        verify(invoiceRepositoryPort).findById(testInvoiceId);
        verify(invoiceMapper).updateDomainFromDto(testInvoiceDTO, testInvoice);
        verify(invoiceRepositoryPort).save(testInvoice);
        verify(invoiceMapper).toDto(testInvoice);
    }

    @Test
    void update_WhenInvoiceNotFound_ShouldThrowDomainException() {
        when(invoiceRepositoryPort.findById(testInvoiceId)).thenReturn(Optional.empty());

        DomainException exception = assertThrows(DomainException.class,
                () -> invoiceService.update(testInvoiceUuid, testInvoiceDTO));

        assertEquals("Invoice not found with id: " + testInvoiceUuid, exception.getMessage());
        verify(invoiceRepositoryPort).findById(testInvoiceId);
        verify(invoiceRepositoryPort, never()).save(any());
    }

    @Test
    void update_WhenInvoiceIsPaid_ShouldThrowDomainException() {
        when(invoiceRepositoryPort.findById(testInvoiceId)).thenReturn(Optional.of(testInvoice));
        when(testInvoice.getStatus()).thenReturn(InvoiceStatus.PAGADA);

        DomainException exception = assertThrows(DomainException.class,
                () -> invoiceService.update(testInvoiceUuid, testInvoiceDTO));

        assertEquals("Cannot update a paid invoice", exception.getMessage());
        verify(invoiceRepositoryPort).findById(testInvoiceId);
        verify(invoiceRepositoryPort, never()).save(any());
    }

    @Test
    void updateStatus_WithValidTransition_ShouldReturnUpdatedInvoiceDTO() {
        when(invoiceRepositoryPort.findById(testInvoiceId)).thenReturn(Optional.of(testInvoice));
        when(invoiceRepositoryPort.save(testInvoice)).thenReturn(testInvoice);
        when(invoiceMapper.toDto(testInvoice)).thenReturn(testInvoiceDTO);

        InvoiceDTO result = invoiceService.updateStatus(testInvoiceUuid, InvoiceStatus.PAGADA);

        assertNotNull(result);
        verify(invoiceRepositoryPort).findById(testInvoiceId);
        verify(testInvoice).setStatus(InvoiceStatus.PAGADA);
        verify(testInvoice).setPaymentDate(LocalDate.now());
        verify(invoiceRepositoryPort).save(testInvoice);
        verify(invoiceMapper).toDto(testInvoice);
    }

    @Test
    void updateStatus_WhenInvoiceNotFound_ShouldThrowDomainException() {
        when(invoiceRepositoryPort.findById(testInvoiceId)).thenReturn(Optional.empty());

        DomainException exception = assertThrows(DomainException.class,
                () -> invoiceService.updateStatus(testInvoiceUuid, InvoiceStatus.PAGADA));

        assertEquals("Invoice not found with id: " + testInvoiceUuid, exception.getMessage());
        verify(invoiceRepositoryPort).findById(testInvoiceId);
        verify(invoiceRepositoryPort, never()).save(any());
    }

    @Test
    void updateStatus_WhenInvalidTransitionFromPaid_ShouldThrowDomainException() {
        when(invoiceRepositoryPort.findById(testInvoiceId)).thenReturn(Optional.of(testInvoice));
        when(testInvoice.getStatus()).thenReturn(InvoiceStatus.PAGADA);

        DomainException exception = assertThrows(DomainException.class,
                () -> invoiceService.updateStatus(testInvoiceUuid, InvoiceStatus.PENDIENTE));

        assertEquals("Cannot change status from PAID to another status", exception.getMessage());
        verify(invoiceRepositoryPort).findById(testInvoiceId);
        verify(testInvoice, never()).setStatus(any());
        verify(invoiceRepositoryPort, never()).save(any());
    }

    @Test
    void applyPayment_WithValidAmount_ShouldReturnUpdatedInvoiceDTO() {
        BigDecimal paymentAmount = new BigDecimal("50.00");
        when(invoiceRepositoryPort.findById(testInvoiceId)).thenReturn(Optional.of(testInvoice));
        when(invoiceRepositoryPort.save(testInvoice)).thenReturn(testInvoice);
        when(invoiceMapper.toDto(testInvoice)).thenReturn(testInvoiceDTO);

        InvoiceDTO result = invoiceService.applyPayment(testInvoiceUuid, paymentAmount, PaymentMethod.TARJETA);

        assertNotNull(result);
        verify(invoiceRepositoryPort).findById(testInvoiceId);
        verify(testInvoice).applyPayment(paymentAmount, PaymentMethod.TARJETA);
        verify(invoiceRepositoryPort).save(testInvoice);
        verify(invoiceMapper).toDto(testInvoice);
    }

    @Test
    void applyPayment_WhenInvoiceIsPaid_ShouldThrowDomainException() {
        BigDecimal paymentAmount = new BigDecimal("50.00");
        when(invoiceRepositoryPort.findById(testInvoiceId)).thenReturn(Optional.of(testInvoice));
        when(testInvoice.getStatus()).thenReturn(InvoiceStatus.PAGADA);

        DomainException exception = assertThrows(DomainException.class,
                () -> invoiceService.applyPayment(testInvoiceUuid, paymentAmount, PaymentMethod.TARJETA));

        assertEquals("Invoice is already paid", exception.getMessage());
        verify(invoiceRepositoryPort).findById(testInvoiceId);
        verify(testInvoice, never()).applyPayment(any(), any());
        verify(invoiceRepositoryPort, never()).save(any());
    }

    @Test
    void applyPayment_WhenAmountExceedsRemainingBalance_ShouldThrowDomainException() {
        BigDecimal paymentAmount = new BigDecimal("150.00");
        when(invoiceRepositoryPort.findById(testInvoiceId)).thenReturn(Optional.of(testInvoice));

        DomainException exception = assertThrows(DomainException.class,
                () -> invoiceService.applyPayment(testInvoiceUuid, paymentAmount, PaymentMethod.TARJETA));

        assertEquals("Payment amount exceeds remaining balance", exception.getMessage());
        verify(invoiceRepositoryPort).findById(testInvoiceId);
        verify(testInvoice, never()).applyPayment(any(), any());
        verify(invoiceRepositoryPort, never()).save(any());
    }

    @Test
    void delete_WithValidInvoice_ShouldDeleteInvoice() {
        when(invoiceRepositoryPort.findById(testInvoiceId)).thenReturn(Optional.of(testInvoice));

        invoiceService.delete(testInvoiceUuid);

        verify(invoiceRepositoryPort).findById(testInvoiceId);
        verify(invoiceRepositoryPort).delete(testInvoiceId);
    }

    @Test
    void delete_WhenInvoiceIsPaid_ShouldThrowDomainException() {
        when(invoiceRepositoryPort.findById(testInvoiceId)).thenReturn(Optional.of(testInvoice));
        when(testInvoice.getStatus()).thenReturn(InvoiceStatus.PAGADA);

        DomainException exception = assertThrows(DomainException.class,
                () -> invoiceService.delete(testInvoiceUuid));

        assertEquals("Cannot delete a paid invoice", exception.getMessage());
        verify(invoiceRepositoryPort).findById(testInvoiceId);
        verify(invoiceRepositoryPort, never()).delete(any());
    }

    @Test
    void delete_WhenInvoiceIsOverdue_ShouldThrowDomainException() {
        when(invoiceRepositoryPort.findById(testInvoiceId)).thenReturn(Optional.of(testInvoice));
        when(testInvoice.getStatus()).thenReturn(InvoiceStatus.VENCIDA);

        DomainException exception = assertThrows(DomainException.class,
                () -> invoiceService.delete(testInvoiceUuid));

        assertEquals("Cannot delete an overdue invoice", exception.getMessage());
        verify(invoiceRepositoryPort).findById(testInvoiceId);
        verify(invoiceRepositoryPort, never()).delete(any());
    }
}