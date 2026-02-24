package com.application.interfaceadapter.controller;

import com.application.application.dto.InvoiceDTO;
import com.application.application.service.InvoiceService;
import com.application.domain.valueobject.InvoiceId;
import com.application.domain.valueobject.InvoiceStatus;
import com.application.domain.valueobject.PaymentMethod;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class InvoiceControllerTest {

    @Mock
    private InvoiceService invoiceService;

    @InjectMocks
    private InvoiceController invoiceController;

    private InvoiceId invoiceId;
    private UUID invoiceUuid;
    private InvoiceDTO invoiceDTO;
    private UUID patientUuid;

    @BeforeEach
    void setUp() {
        invoiceUuid = UUID.randomUUID();
        invoiceId = new InvoiceId(invoiceUuid);
        patientUuid = UUID.randomUUID();

        invoiceDTO = new InvoiceDTO(
                invoiceUuid,
                "FAC-2024-001",
                LocalDate.now(),
                LocalDate.now().plusDays(30),
                new BigDecimal("100.00"),
                new BigDecimal("21.00"),
                new BigDecimal("121.00"),
                InvoiceStatus.PENDIENTE,
                PaymentMethod.TARJETA,
                patientUuid,
                null,
                null
        );
    }

    @Test
    void getById_shouldReturnInvoice_whenExists() {
        when(invoiceService.findById(invoiceId)).thenReturn(Optional.of(invoiceDTO));

        ResponseEntity<InvoiceDTO> response = invoiceController.getById(invoiceUuid);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(invoiceUuid, response.getBody().invoiceId());
        verify(invoiceService).findById(invoiceId);
    }

    @Test
    void getById_shouldReturnNotFound_whenNotExists() {
        when(invoiceService.findById(invoiceId)).thenReturn(Optional.empty());

        ResponseEntity<InvoiceDTO> response = invoiceController.getById(invoiceUuid);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertNull(response.getBody());
        verify(invoiceService).findById(invoiceId);
    }

    @Test
    void getAll_shouldReturnInvoiceList() {
        List<InvoiceDTO> invoiceList = Arrays.asList(invoiceDTO);
        when(invoiceService.findAll()).thenReturn(invoiceList);

        ResponseEntity<List<InvoiceDTO>> response = invoiceController.getAll();

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(1, response.getBody().size());
        verify(invoiceService).findAll();
    }

    @Test
    void getByPatient_shouldReturnInvoiceList() {
        List<InvoiceDTO> invoiceList = Arrays.asList(invoiceDTO);
        when(invoiceService.findByPatientId(patientUuid)).thenReturn(invoiceList);

        ResponseEntity<List<InvoiceDTO>> response = invoiceController.getByPatient(patientUuid);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(1, response.getBody().size());
        verify(invoiceService).findByPatientId(patientUuid);
    }

    @Test
    void getByStatus_shouldReturnInvoiceList() {
        List<InvoiceDTO> invoiceList = Arrays.asList(invoiceDTO);
        when(invoiceService.findByStatus(InvoiceStatus.PENDIENTE)).thenReturn(invoiceList);

        ResponseEntity<List<InvoiceDTO>> response = invoiceController.getByStatus(InvoiceStatus.PENDIENTE);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(1, response.getBody().size());
        verify(invoiceService).findByStatus(InvoiceStatus.PENDIENTE);
    }

    @Test
    void getByDateRange_shouldReturnInvoiceList() {
        LocalDate startDate = LocalDate.now().minusDays(7);
        LocalDate endDate = LocalDate.now();
        List<InvoiceDTO> invoiceList = Arrays.asList(invoiceDTO);
        when(invoiceService.findByEmissionDateBetween(startDate, endDate)).thenReturn(invoiceList);

        ResponseEntity<List<InvoiceDTO>> response = invoiceController.getByDateRange(startDate, endDate);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(1, response.getBody().size());
        verify(invoiceService).findByEmissionDateBetween(startDate, endDate);
    }

    @Test
    void getOverdueInvoices_shouldReturnInvoiceList() {
        List<InvoiceDTO> invoiceList = Arrays.asList(invoiceDTO);
        when(invoiceService.findOverdueInvoices()).thenReturn(invoiceList);

        ResponseEntity<List<InvoiceDTO>> response = invoiceController.getOverdueInvoices();

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(1, response.getBody().size());
        verify(invoiceService).findOverdueInvoices();
    }

    @Test
    void create_shouldReturnCreatedInvoice() {
        when(invoiceService.create(invoiceDTO)).thenReturn(invoiceDTO);

        ResponseEntity<InvoiceDTO> response = invoiceController.create(invoiceDTO);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(invoiceUuid, response.getBody().invoiceId());
        verify(invoiceService).create(invoiceDTO);
    }

    @Test
    void update_shouldReturnUpdatedInvoice() {
        when(invoiceService.update(eq(invoiceId), any(InvoiceDTO.class))).thenReturn(invoiceDTO);

        ResponseEntity<InvoiceDTO> response = invoiceController.update(invoiceUuid, invoiceDTO);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(invoiceUuid, response.getBody().invoiceId());
        verify(invoiceService).update(eq(invoiceId), any(InvoiceDTO.class));
    }

    @Test
    void updateStatus_shouldReturnUpdatedInvoice() {
        when(invoiceService.updateStatus(invoiceId, InvoiceStatus.PAGADA)).thenReturn(invoiceDTO);

        ResponseEntity<InvoiceDTO> response = invoiceController.updateStatus(invoiceUuid, InvoiceStatus.PAGADA);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        verify(invoiceService).updateStatus(invoiceId, InvoiceStatus.PAGADA);
    }

    @Test
    void registerPayment_shouldReturnUpdatedInvoice() {
        BigDecimal amount = new BigDecimal("121.00");
        when(invoiceService.registerPayment(invoiceId, PaymentMethod.EFECTIVO, amount)).thenReturn(invoiceDTO);

        ResponseEntity<InvoiceDTO> response = invoiceController.registerPayment(invoiceUuid, PaymentMethod.EFECTIVO, amount);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        verify(invoiceService).registerPayment(invoiceId, PaymentMethod.EFECTIVO, amount);
    }

    @Test
    void applyDiscount_shouldReturnUpdatedInvoice() {
        BigDecimal discountPercentage = new BigDecimal("10.00");
        String authorizationCode = "AUTH123";
        when(invoiceService.applyDiscount(invoiceId, discountPercentage, authorizationCode)).thenReturn(invoiceDTO);

        ResponseEntity<InvoiceDTO> response = invoiceController.applyDiscount(invoiceUuid, discountPercentage, authorizationCode);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        verify(invoiceService).applyDiscount(invoiceId, discountPercentage, authorizationCode);
    }

    @Test
    void delete_shouldReturnNoContent() {
        doNothing().when(invoiceService).delete(invoiceId);

        ResponseEntity<Void> response = invoiceController.delete(invoiceUuid);

        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
        assertNull(response.getBody());
        verify(invoiceService).delete(invoiceId);
    }

    @Test
    void generatePdf_shouldReturnPdfContent() {
        byte[] pdfContent = "PDF Content".getBytes();
        when(invoiceService.generatePdf(invoiceId)).thenReturn(pdfContent);

        ResponseEntity<byte[]> response = invoiceController.generatePdf(invoiceUuid);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("application/pdf", response.getHeaders().getFirst("Content-Type"));
        assertTrue(response.getHeaders().getFirst("Content-Disposition").contains("invoice-" + invoiceUuid + ".pdf"));
        verify(invoiceService).generatePdf(invoiceId);
    }

    @Test
    void sendByEmail_shouldReturnAccepted() {
        String email = "patient@example.com";
        doNothing().when(invoiceService).sendByEmail(invoiceId, email);

        ResponseEntity<Void> response = invoiceController.sendByEmail(invoiceUuid, email);

        assertEquals(HttpStatus.ACCEPTED, response.getStatusCode());
        assertNull(response.getBody());
        verify(invoiceService).sendByEmail(invoiceId, email);
    }

    @Test
    void getTotalAmountByPatient_shouldReturnTotalAmount() {
        BigDecimal totalAmount = new BigDecimal("500.00");
        when(invoiceService.getTotalAmountByPatient(patientUuid)).thenReturn(totalAmount);

        ResponseEntity<BigDecimal> response = invoiceController.getTotalAmountByPatient(patientUuid);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(totalAmount, response.getBody());
        verify(invoiceService).getTotalAmountByPatient(patientUuid);
    }

    @Test
    void getPendingAmountByPatient_shouldReturnPendingAmount() {
        BigDecimal pendingAmount = new BigDecimal("250.00");
        when(invoiceService.getPendingAmountByPatient(patientUuid)).thenReturn(pendingAmount);

        ResponseEntity<BigDecimal> response = invoiceController.getPendingAmountByPatient(patientUuid);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(pendingAmount, response.getBody());
        verify(invoiceService).getPendingAmountByPatient(patientUuid);
    }
}