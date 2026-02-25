package com.application.domain.repository;

import com.application.domain.shared.EntityRepository;
import com.application.domain.model.PaymentPlan;
import com.application.domain.valueobject.PaymentPlanId;
import com.application.domain.valueobject.InvoiceId;
import com.application.domain.enums.PaymentPlanStatus;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface PaymentPlanRepository extends EntityRepository<PaymentPlan, PaymentPlanId> {

    Optional<PaymentPlan> findByInvoiceId(InvoiceId invoiceId);

    List<PaymentPlan> findByStatus(PaymentPlanStatus status);

    List<PaymentPlan> findByPatientIdAndStatus(String patientId, PaymentPlanStatus status);

    List<PaymentPlan> findDuePlansByDate(LocalDate dueDate);

    List<PaymentPlan> findPlansWithOverdueInstallments(LocalDate currentDate);

    boolean existsActivePlanForInvoice(InvoiceId invoiceId);
}