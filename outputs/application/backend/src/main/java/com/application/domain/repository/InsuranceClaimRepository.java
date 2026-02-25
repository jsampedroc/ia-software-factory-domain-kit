package com.application.domain.repository;

import com.application.domain.shared.EntityRepository;
import com.application.domain.model.InsuranceClaim;
import com.application.domain.valueobject.InsuranceClaimId;
import com.application.domain.valueobject.PatientId;
import com.application.domain.valueobject.InvoiceId;
import com.application.domain.enums.ClaimStatus;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface InsuranceClaimRepository extends EntityRepository<InsuranceClaim, InsuranceClaimId> {

    List<InsuranceClaim> findByPatientId(PatientId patientId);

    List<InsuranceClaim> findByInvoiceId(InvoiceId invoiceId);

    List<InsuranceClaim> findByStatus(ClaimStatus status);

    List<InsuranceClaim> findByInsuranceProvider(String provider);

    Optional<InsuranceClaim> findByClaimIdAndPatientId(InsuranceClaimId claimId, PatientId patientId);

    List<InsuranceClaim> findBySubmittedDateBetween(LocalDate startDate, LocalDate endDate);

    boolean existsByInvoiceIdAndStatusNot(InvoiceId invoiceId, ClaimStatus excludedStatus);
}