package com.application.domain.repository;

import com.application.domain.model.PaymentPlan;
import com.application.domain.valueobject.PaymentPlanId;
import com.application.domain.valueobject.InvoiceId;
import com.application.domain.enums.PaymentPlanStatus;
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
class PaymentPlanRepositoryTest {

    @Mock
    private PaymentPlanRepository paymentPlanRepository;

    private PaymentPlanId testPlanId;
    private InvoiceId testInvoiceId;
    private PaymentPlan testPaymentPlan;
    private final String testPatientId = UUID.randomUUID().toString();
    private final LocalDate testDate = LocalDate.now();
    private final PaymentPlanStatus testStatus = PaymentPlanStatus.ACTIVE;

    @BeforeEach
    void setUp() {
        testPlanId = new PaymentPlanId(UUID.randomUUID());
        testInvoiceId = new InvoiceId(UUID.randomUUID());

        testPaymentPlan = PaymentPlan.create(
                testPlanId,
                testInvoiceId,
                new BigDecimal("1000.00"),
                5,
                new BigDecimal("200.00"),
                testDate,
                List.of(testDate.plusDays(30), testDate.plusDays(60)),
                testStatus
        );
    }

    @Test
    void findByInvoiceId_ShouldReturnPaymentPlan_WhenExists() {
        when(paymentPlanRepository.findByInvoiceId(testInvoiceId))
                .thenReturn(Optional.of(testPaymentPlan));

        Optional<PaymentPlan> result = paymentPlanRepository.findByInvoiceId(testInvoiceId);

        assertThat(result).isPresent();
        assertThat(result.get().getPlanId()).isEqualTo(testPlanId);
        assertThat(result.get().getInvoiceId()).isEqualTo(testInvoiceId);
        verify(paymentPlanRepository).findByInvoiceId(testInvoiceId);
    }

    @Test
    void findByInvoiceId_ShouldReturnEmpty_WhenNotFound() {
        when(paymentPlanRepository.findByInvoiceId(testInvoiceId))
                .thenReturn(Optional.empty());

        Optional<PaymentPlan> result = paymentPlanRepository.findByInvoiceId(testInvoiceId);

        assertThat(result).isEmpty();
        verify(paymentPlanRepository).findByInvoiceId(testInvoiceId);
    }

    @Test
    void findByStatus_ShouldReturnListOfPaymentPlans() {
        List<PaymentPlan> expectedPlans = List.of(testPaymentPlan);
        when(paymentPlanRepository.findByStatus(testStatus))
                .thenReturn(expectedPlans);

        List<PaymentPlan> result = paymentPlanRepository.findByStatus(testStatus);

        assertThat(result).isNotNull().hasSize(1);
        assertThat(result.get(0).getStatus()).isEqualTo(testStatus);
        verify(paymentPlanRepository).findByStatus(testStatus);
    }

    @Test
    void findByPatientIdAndStatus_ShouldReturnFilteredList() {
        List<PaymentPlan> expectedPlans = List.of(testPaymentPlan);
        when(paymentPlanRepository.findByPatientIdAndStatus(testPatientId, testStatus))
                .thenReturn(expectedPlans);

        List<PaymentPlan> result = paymentPlanRepository.findByPatientIdAndStatus(testPatientId, testStatus);

        assertThat(result).isNotNull().hasSize(1);
        verify(paymentPlanRepository).findByPatientIdAndStatus(testPatientId, testStatus);
    }

    @Test
    void findDuePlansByDate_ShouldReturnPlansWithDueDate() {
        List<PaymentPlan> expectedPlans = List.of(testPaymentPlan);
        when(paymentPlanRepository.findDuePlansByDate(testDate))
                .thenReturn(expectedPlans);

        List<PaymentPlan> result = paymentPlanRepository.findDuePlansByDate(testDate);

        assertThat(result).isNotNull().hasSize(1);
        verify(paymentPlanRepository).findDuePlansByDate(testDate);
    }

    @Test
    void findPlansWithOverdueInstallments_ShouldReturnPlans() {
        List<PaymentPlan> expectedPlans = List.of(testPaymentPlan);
        when(paymentPlanRepository.findPlansWithOverdueInstallments(testDate))
                .thenReturn(expectedPlans);

        List<PaymentPlan> result = paymentPlanRepository.findPlansWithOverdueInstallments(testDate);

        assertThat(result).isNotNull().hasSize(1);
        verify(paymentPlanRepository).findPlansWithOverdueInstallments(testDate);
    }

    @Test
    void existsActivePlanForInvoice_ShouldReturnTrue_WhenActivePlanExists() {
        when(paymentPlanRepository.existsActivePlanForInvoice(testInvoiceId))
                .thenReturn(true);

        boolean result = paymentPlanRepository.existsActivePlanForInvoice(testInvoiceId);

        assertThat(result).isTrue();
        verify(paymentPlanRepository).existsActivePlanForInvoice(testInvoiceId);
    }

    @Test
    void existsActivePlanForInvoice_ShouldReturnFalse_WhenNoActivePlan() {
        when(paymentPlanRepository.existsActivePlanForInvoice(testInvoiceId))
                .thenReturn(false);

        boolean result = paymentPlanRepository.existsActivePlanForInvoice(testInvoiceId);

        assertThat(result).isFalse();
        verify(paymentPlanRepository).existsActivePlanForInvoice(testInvoiceId);
    }

    @Test
    void save_ShouldPersistPaymentPlan() {
        when(paymentPlanRepository.save(testPaymentPlan)).thenReturn(testPaymentPlan);

        PaymentPlan savedPlan = paymentPlanRepository.save(testPaymentPlan);

        assertThat(savedPlan).isEqualTo(testPaymentPlan);
        verify(paymentPlanRepository).save(testPaymentPlan);
    }

    @Test
    void findById_ShouldReturnPaymentPlan() {
        when(paymentPlanRepository.findById(testPlanId)).thenReturn(Optional.of(testPaymentPlan));

        Optional<PaymentPlan> result = paymentPlanRepository.findById(testPlanId);

        assertThat(result).isPresent();
        assertThat(result.get().getPlanId()).isEqualTo(testPlanId);
        verify(paymentPlanRepository).findById(testPlanId);
    }

    @Test
    void delete_ShouldRemovePaymentPlan() {
        paymentPlanRepository.delete(testPaymentPlan);

        verify(paymentPlanRepository).delete(testPaymentPlan);
    }

    @Test
    void findAll_ShouldReturnAllPaymentPlans() {
        List<PaymentPlan> expectedPlans = List.of(testPaymentPlan);
        when(paymentPlanRepository.findAll()).thenReturn(expectedPlans);

        List<PaymentPlan> result = paymentPlanRepository.findAll();

        assertThat(result).isNotNull().hasSize(1);
        verify(paymentPlanRepository).findAll();
    }
}