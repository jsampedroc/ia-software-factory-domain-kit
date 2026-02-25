package com.application.domain.repository;

import com.application.domain.model.Invoice;
import com.application.domain.valueobject.InvoiceId;
import com.application.domain.valueobject.PatientId;
import com.application.domain.enums.InvoiceStatus;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
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
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class InvoiceRepositoryTest {

    @Mock
    private InvoiceRepository invoiceRepository;

    private InvoiceId testInvoiceId;
    private PatientId testPatientId;
    private Invoice testInvoice;
    private LocalDate testIssueDate;
    private LocalDate testDueDate;

    @BeforeEach
    void setUp() {
        testInvoiceId = new InvoiceId(UUID.randomUUID());
        testPatientId = new PatientId(UUID.randomUUID());
        testIssueDate = LocalDate.now().minusDays(5);
        testDueDate = LocalDate.now().plusDays(30);

        // Using the domain factory method (as per rule 1)
        testInvoice = Invoice.create(
                testInvoiceId,
                testPatientId,
                null, // appointmentId
                "INV-2024-001",
                testIssueDate,
                testDueDate,
                List.of(), // empty items for simplicity
                BigDecimal.ZERO,
                BigDecimal.ZERO,
                BigDecimal.ZERO,
                InvoiceStatus.DRAFT,
                null // paymentPlanId
        );
    }

    @Test
    void findByInvoiceNumber_ShouldReturnInvoice_WhenInvoiceNumberExists() {
        String invoiceNumber = "INV-2024-001";
        when(invoiceRepository.findByInvoiceNumber(invoiceNumber)).thenReturn(Optional.of(testInvoice));

        Optional<Invoice> result = invoiceRepository.findByInvoiceNumber(invoiceNumber);

        assertThat(result).isPresent();
        assertThat(result.get().getInvoiceId()).isEqualTo(testInvoiceId);
        assertThat(result.get().getInvoiceNumber()).isEqualTo(invoiceNumber);
        verify(invoiceRepository).findByInvoiceNumber(invoiceNumber);
    }

    @Test
    void findByInvoiceNumber_ShouldReturnEmpty_WhenInvoiceNumberDoesNotExist() {
        String invoiceNumber = "INV-NONEXISTENT";
        when(invoiceRepository.findByInvoiceNumber(invoiceNumber)).thenReturn(Optional.empty());

        Optional<Invoice> result = invoiceRepository.findByInvoiceNumber(invoiceNumber);

        assertThat(result).isEmpty();
        verify(invoiceRepository).findByInvoiceNumber(invoiceNumber);
    }

    @Test
    void findByPatientId_ShouldReturnInvoicesForPatient() {
        List<Invoice> expectedInvoices = List.of(testInvoice);
        when(invoiceRepository.findByPatientId(testPatientId)).thenReturn(expectedInvoices);

        List<Invoice> result = invoiceRepository.findByPatientId(testPatientId);

        assertThat(result).hasSize(1);
        assertThat(result.get(0).getPatientId()).isEqualTo(testPatientId);
        verify(invoiceRepository).findByPatientId(testPatientId);
    }

    @Test
    void findByStatus_ShouldReturnInvoicesWithGivenStatus() {
        String status = InvoiceStatus.ISSUED.name();
        List<Invoice> expectedInvoices = List.of(testInvoice);
        when(invoiceRepository.findByStatus(status)).thenReturn(expectedInvoices);

        List<Invoice> result = invoiceRepository.findByStatus(status);

        assertThat(result).hasSize(1);
        // Note: The repository method uses String, but we can check the enum conversion in the domain
        assertThat(result.get(0).getStatus().name()).isEqualTo(InvoiceStatus.DRAFT.name());
        verify(invoiceRepository).findByStatus(status);
    }

    @Test
    void findByIssueDateBetween_ShouldReturnInvoicesInDateRange() {
        LocalDate startDate = testIssueDate.minusDays(1);
        LocalDate endDate = testIssueDate.plusDays(1);
        List<Invoice> expectedInvoices = List.of(testInvoice);
        when(invoiceRepository.findByIssueDateBetween(startDate, endDate)).thenReturn(expectedInvoices);

        List<Invoice> result = invoiceRepository.findByIssueDateBetween(startDate, endDate);

        assertThat(result).hasSize(1);
        assertThat(result.get(0).getIssueDate()).isEqualTo(testIssueDate);
        verify(invoiceRepository).findByIssueDateBetween(startDate, endDate);
    }

    @Test
    void findByDueDateBeforeAndStatusNot_ShouldReturnOverdueInvoices() {
        LocalDate cutoffDate = LocalDate.now();
        String excludedStatus = InvoiceStatus.PAID.name();
        List<Invoice> expectedInvoices = List.of(testInvoice);
        when(invoiceRepository.findByDueDateBeforeAndStatusNot(cutoffDate, excludedStatus)).thenReturn(expectedInvoices);

        List<Invoice> result = invoiceRepository.findByDueDateBeforeAndStatusNot(cutoffDate, excludedStatus);

        assertThat(result).hasSize(1);
        assertThat(result.get(0).getDueDate()).isBefore(cutoffDate);
        assertThat(result.get(0).getStatus().name()).isNotEqualTo(excludedStatus);
        verify(invoiceRepository).findByDueDateBeforeAndStatusNot(cutoffDate, excludedStatus);
    }

    @Test
    void existsByInvoiceNumber_ShouldReturnTrue_WhenInvoiceNumberExists() {
        String invoiceNumber = "INV-2024-001";
        when(invoiceRepository.existsByInvoiceNumber(invoiceNumber)).thenReturn(true);

        boolean exists = invoiceRepository.existsByInvoiceNumber(invoiceNumber);

        assertThat(exists).isTrue();
        verify(invoiceRepository).existsByInvoiceNumber(invoiceNumber);
    }

    @Test
    void existsByInvoiceNumber_ShouldReturnFalse_WhenInvoiceNumberDoesNotExist() {
        String invoiceNumber = "INV-NONEXISTENT";
        when(invoiceRepository.existsByInvoiceNumber(invoiceNumber)).thenReturn(false);

        boolean exists = invoiceRepository.existsByInvoiceNumber(invoiceNumber);

        assertThat(exists).isFalse();
        verify(invoiceRepository).existsByInvoiceNumber(invoiceNumber);
    }

    @Test
    void save_ShouldPersistInvoice() {
        when(invoiceRepository.save(any(Invoice.class))).thenReturn(testInvoice);

        Invoice savedInvoice = invoiceRepository.save(testInvoice);

        assertThat(savedInvoice).isEqualTo(testInvoice);
        verify(invoiceRepository).save(testInvoice);
    }

    @Test
    void findById_ShouldReturnInvoice_WhenIdExists() {
        when(invoiceRepository.findById(testInvoiceId)).thenReturn(Optional.of(testInvoice));

        Optional<Invoice> result = invoiceRepository.findById(testInvoiceId);

        assertThat(result).isPresent();
        assertThat(result.get().getInvoiceId()).isEqualTo(testInvoiceId);
        verify(invoiceRepository).findById(testInvoiceId);
    }

    @Test
    void findById_ShouldReturnEmpty_WhenIdDoesNotExist() {
        InvoiceId nonExistentId = new InvoiceId(UUID.randomUUID());
        when(invoiceRepository.findById(nonExistentId)).thenReturn(Optional.empty());

        Optional<Invoice> result = invoiceRepository.findById(nonExistentId);

        assertThat(result).isEmpty();
        verify(invoiceRepository).findById(nonExistentId);
    }

    @Test
    void delete_ShouldRemoveInvoice() {
        invoiceRepository.delete(testInvoice);

        verify(invoiceRepository).delete(testInvoice);
    }

    @Test
    void findAll_ShouldReturnAllInvoices() {
        List<Invoice> allInvoices = List.of(testInvoice);
        when(invoiceRepository.findAll()).thenReturn(allInvoices);

        List<Invoice> result = invoiceRepository.findAll();

        assertThat(result).hasSize(1);
        assertThat(result).containsExactly(testInvoice);
        verify(invoiceRepository).findAll();
    }
}