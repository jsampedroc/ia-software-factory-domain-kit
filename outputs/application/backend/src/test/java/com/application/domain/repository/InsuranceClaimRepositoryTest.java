package com.application.domain.repository;

import com.application.domain.model.InsuranceClaim;
import com.application.domain.valueobject.InsuranceClaimId;
import com.application.domain.valueobject.PatientId;
import com.application.domain.valueobject.InvoiceId;
import com.application.domain.enums.ClaimStatus;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class InsuranceClaimRepositoryTest {

    @Mock
    private InsuranceClaimRepository repository;

    private InsuranceClaimId claimId;
    private PatientId patientId;
    private InvoiceId invoiceId;
    private InsuranceClaim claim;
    private LocalDate testDate;

    @BeforeEach
    void setUp() {
        claimId = new InsuranceClaimId(UUID.randomUUID());
        patientId = new PatientId(UUID.randomUUID());
        invoiceId = new InvoiceId(UUID.randomUUID());
        testDate = LocalDate.now();

        claim = InsuranceClaim.create(
                claimId,
                patientId,
                invoiceId,
                "Test Provider",
                "POL-12345",
                new BigDecimal("1500.00"),
                ClaimStatus.SUBMITTED,
                testDate,
                null
        );
    }

    @Test
    void findByPatientId_ShouldReturnClaimsForPatient() {
        List<InsuranceClaim> expectedClaims = List.of(claim);
        when(repository.findByPatientId(patientId)).thenReturn(expectedClaims);

        List<InsuranceClaim> result = repository.findByPatientId(patientId);

        assertThat(result).isEqualTo(expectedClaims);
        verify(repository).findByPatientId(patientId);
    }

    @Test
    void findByInvoiceId_ShouldReturnClaimsForInvoice() {
        List<InsuranceClaim> expectedClaims = List.of(claim);
        when(repository.findByInvoiceId(invoiceId)).thenReturn(expectedClaims);

        List<InsuranceClaim> result = repository.findByInvoiceId(invoiceId);

        assertThat(result).isEqualTo(expectedClaims);
        verify(repository).findByInvoiceId(invoiceId);
    }

    @Test
    void findByStatus_ShouldReturnClaimsWithGivenStatus() {
        List<InsuranceClaim> expectedClaims = List.of(claim);
        when(repository.findByStatus(ClaimStatus.SUBMITTED)).thenReturn(expectedClaims);

        List<InsuranceClaim> result = repository.findByStatus(ClaimStatus.SUBMITTED);

        assertThat(result).isEqualTo(expectedClaims);
        verify(repository).findByStatus(ClaimStatus.SUBMITTED);
    }

    @Test
    void findByInsuranceProvider_ShouldReturnClaimsForProvider() {
        List<InsuranceClaim> expectedClaims = List.of(claim);
        when(repository.findByInsuranceProvider("Test Provider")).thenReturn(expectedClaims);

        List<InsuranceClaim> result = repository.findByInsuranceProvider("Test Provider");

        assertThat(result).isEqualTo(expectedClaims);
        verify(repository).findByInsuranceProvider("Test Provider");
    }

    @Test
    void findByClaimIdAndPatientId_WhenClaimExists_ShouldReturnClaim() {
        when(repository.findByClaimIdAndPatientId(claimId, patientId)).thenReturn(Optional.of(claim));

        Optional<InsuranceClaim> result = repository.findByClaimIdAndPatientId(claimId, patientId);

        assertThat(result).isPresent().contains(claim);
        verify(repository).findByClaimIdAndPatientId(claimId, patientId);
    }

    @Test
    void findByClaimIdAndPatientId_WhenClaimDoesNotExist_ShouldReturnEmpty() {
        when(repository.findByClaimIdAndPatientId(claimId, patientId)).thenReturn(Optional.empty());

        Optional<InsuranceClaim> result = repository.findByClaimIdAndPatientId(claimId, patientId);

        assertThat(result).isEmpty();
        verify(repository).findByClaimIdAndPatientId(claimId, patientId);
    }

    @Test
    void findBySubmittedDateBetween_ShouldReturnClaimsInDateRange() {
        LocalDate startDate = testDate.minusDays(7);
        LocalDate endDate = testDate.plusDays(7);
        List<InsuranceClaim> expectedClaims = List.of(claim);
        when(repository.findBySubmittedDateBetween(startDate, endDate)).thenReturn(expectedClaims);

        List<InsuranceClaim> result = repository.findBySubmittedDateBetween(startDate, endDate);

        assertThat(result).isEqualTo(expectedClaims);
        verify(repository).findBySubmittedDateBetween(startDate, endDate);
    }

    @Test
    void existsByInvoiceIdAndStatusNot_WhenExists_ShouldReturnTrue() {
        when(repository.existsByInvoiceIdAndStatusNot(invoiceId, ClaimStatus.REJECTED)).thenReturn(true);

        boolean result = repository.existsByInvoiceIdAndStatusNot(invoiceId, ClaimStatus.REJECTED);

        assertThat(result).isTrue();
        verify(repository).existsByInvoiceIdAndStatusNot(invoiceId, ClaimStatus.REJECTED);
    }

    @Test
    void existsByInvoiceIdAndStatusNot_WhenDoesNotExist_ShouldReturnFalse() {
        when(repository.existsByInvoiceIdAndStatusNot(invoiceId, ClaimStatus.REJECTED)).thenReturn(false);

        boolean result = repository.existsByInvoiceIdAndStatusNot(invoiceId, ClaimStatus.REJECTED);

        assertThat(result).isFalse();
        verify(repository).existsByInvoiceIdAndStatusNot(invoiceId, ClaimStatus.REJECTED);
    }

    @Test
    void save_ShouldPersistClaim() {
        when(repository.save(claim)).thenReturn(claim);

        InsuranceClaim savedClaim = repository.save(claim);

        assertThat(savedClaim).isEqualTo(claim);
        verify(repository).save(claim);
    }

    @Test
    void findById_WhenClaimExists_ShouldReturnClaim() {
        when(repository.findById(claimId)).thenReturn(Optional.of(claim));

        Optional<InsuranceClaim> result = repository.findById(claimId);

        assertThat(result).isPresent().contains(claim);
        verify(repository).findById(claimId);
    }

    @Test
    void findById_WhenClaimDoesNotExist_ShouldReturnEmpty() {
        when(repository.findById(claimId)).thenReturn(Optional.empty());

        Optional<InsuranceClaim> result = repository.findById(claimId);

        assertThat(result).isEmpty();
        verify(repository).findById(claimId);
    }

    @Test
    void delete_ShouldRemoveClaim() {
        repository.delete(claim);
        verify(repository).delete(claim);
    }

    @Test
    void findAll_ShouldReturnAllClaims() {
        List<InsuranceClaim> expectedClaims = List.of(claim);
        when(repository.findAll()).thenReturn(expectedClaims);

        List<InsuranceClaim> result = repository.findAll();

        assertThat(result).isEqualTo(expectedClaims);
        verify(repository).findAll();
    }
}