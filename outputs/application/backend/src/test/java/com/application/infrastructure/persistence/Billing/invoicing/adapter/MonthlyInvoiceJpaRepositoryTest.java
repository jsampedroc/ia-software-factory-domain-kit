package com.application.infrastructure.persistence.Billing.invoicing.adapter;

import com.application.domain.Billing.invoicing.domain.MonthlyInvoice;
import com.application.domain.Billing.invoicing.domain.repository.MonthlyInvoiceRepository;
import com.application.domain.Billing.invoicing.valueobject.MonthlyInvoiceId;
import com.application.domain.SchoolManagement.valueobject.StudentId;
import com.application.infrastructure.persistence.Billing.invoicing.entity.MonthlyInvoiceEntity;
import com.application.infrastructure.persistence.Billing.invoicing.mapper.MonthlyInvoiceEntityMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;

import java.time.LocalDate;
import java.time.YearMonth;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class MonthlyInvoiceJpaRepositoryTest {

    @Mock
    private MonthlyInvoiceRepositoryImpl delegateRepository;

    @Mock
    private MonthlyInvoiceEntityMapper mapper;

    @InjectMocks
    private MonthlyInvoiceJpaRepository monthlyInvoiceJpaRepository;

    private MonthlyInvoiceId invoiceId;
    private StudentId studentId;
    private MonthlyInvoice domainInvoice;
    private MonthlyInvoiceEntity entityInvoice;
    private YearMonth billingPeriod;
    private UUID entityId;

    @BeforeEach
    void setUp() {
        entityId = UUID.randomUUID();
        invoiceId = new MonthlyInvoiceId(entityId);
        studentId = new StudentId(UUID.randomUUID());
        billingPeriod = YearMonth.of(2024, 1);

        domainInvoice = mock(MonthlyInvoice.class);
        entityInvoice = mock(MonthlyInvoiceEntity.class);

        when(domainInvoice.getId()).thenReturn(invoiceId);
        when(entityInvoice.getId()).thenReturn(entityId);
    }

    @Test
    void findById_WhenEntityExists_ReturnsDomainObject() {
        when(delegateRepository.findById(entityId)).thenReturn(Optional.of(entityInvoice));
        when(mapper.toDomain(entityInvoice)).thenReturn(domainInvoice);

        Optional<MonthlyInvoice> result = monthlyInvoiceJpaRepository.findById(invoiceId);

        assertThat(result).isPresent().contains(domainInvoice);
        verify(delegateRepository).findById(entityId);
        verify(mapper).toDomain(entityInvoice);
    }

    @Test
    void findById_WhenEntityDoesNotExist_ReturnsEmpty() {
        when(delegateRepository.findById(entityId)).thenReturn(Optional.empty());

        Optional<MonthlyInvoice> result = monthlyInvoiceJpaRepository.findById(invoiceId);

        assertThat(result).isEmpty();
        verify(delegateRepository).findById(entityId);
        verify(mapper, never()).toDomain(any());
    }

    @Test
    void findByStudentIdAndBillingPeriod_WhenExists_ReturnsDomainObject() {
        when(delegateRepository.findByStudentIdAndBillingPeriod(studentId.getValue(), billingPeriod))
                .thenReturn(Optional.of(entityInvoice));
        when(mapper.toDomain(entityInvoice)).thenReturn(domainInvoice);

        Optional<MonthlyInvoice> result = monthlyInvoiceJpaRepository.findByStudentIdAndBillingPeriod(studentId, billingPeriod);

        assertThat(result).isPresent().contains(domainInvoice);
        verify(delegateRepository).findByStudentIdAndBillingPeriod(studentId.getValue(), billingPeriod);
        verify(mapper).toDomain(entityInvoice);
    }

    @Test
    void findByStudentIdAndBillingPeriod_WhenNotExists_ReturnsEmpty() {
        when(delegateRepository.findByStudentIdAndBillingPeriod(studentId.getValue(), billingPeriod))
                .thenReturn(Optional.empty());

        Optional<MonthlyInvoice> result = monthlyInvoiceJpaRepository.findByStudentIdAndBillingPeriod(studentId, billingPeriod);

        assertThat(result).isEmpty();
        verify(delegateRepository).findByStudentIdAndBillingPeriod(studentId.getValue(), billingPeriod);
        verify(mapper, never()).toDomain(any());
    }

    @Test
    void findAllByStudentId_ReturnsList() {
        List<MonthlyInvoiceEntity> entityList = List.of(entityInvoice);
        when(delegateRepository.findAllByStudentId(studentId.getValue())).thenReturn(entityList);
        when(mapper.toDomain(entityInvoice)).thenReturn(domainInvoice);

        List<MonthlyInvoice> result = monthlyInvoiceJpaRepository.findAllByStudentId(studentId);

        assertThat(result).hasSize(1).containsExactly(domainInvoice);
        verify(delegateRepository).findAllByStudentId(studentId.getValue());
        verify(mapper).toDomain(entityInvoice);
    }

    @Test
    void findAllByStudentIdAndStatus_ReturnsList() {
        String status = "PENDING";
        List<MonthlyInvoiceEntity> entityList = List.of(entityInvoice);
        when(delegateRepository.findAllByStudentIdAndStatus(studentId.getValue(), status)).thenReturn(entityList);
        when(mapper.toDomain(entityInvoice)).thenReturn(domainInvoice);

        List<MonthlyInvoice> result = monthlyInvoiceJpaRepository.findAllByStudentIdAndStatus(studentId, status);

        assertThat(result).hasSize(1).containsExactly(domainInvoice);
        verify(delegateRepository).findAllByStudentIdAndStatus(studentId.getValue(), status);
        verify(mapper).toDomain(entityInvoice);
    }

    @Test
    void findAllByBillingPeriod_ReturnsList() {
        List<MonthlyInvoiceEntity> entityList = List.of(entityInvoice);
        when(delegateRepository.findAllByBillingPeriod(billingPeriod)).thenReturn(entityList);
        when(mapper.toDomain(entityInvoice)).thenReturn(domainInvoice);

        List<MonthlyInvoice> result = monthlyInvoiceJpaRepository.findAllByBillingPeriod(billingPeriod);

        assertThat(result).hasSize(1).containsExactly(domainInvoice);
        verify(delegateRepository).findAllByBillingPeriod(billingPeriod);
        verify(mapper).toDomain(entityInvoice);
    }

    @Test
    void findAllByDueDateBeforeAndStatus_ReturnsList() {
        LocalDate dueDate = LocalDate.now();
        String status = "PENDING";
        List<MonthlyInvoiceEntity> entityList = List.of(entityInvoice);
        when(delegateRepository.findAllByDueDateBeforeAndStatus(dueDate, status)).thenReturn(entityList);
        when(mapper.toDomain(entityInvoice)).thenReturn(domainInvoice);

        List<MonthlyInvoice> result = monthlyInvoiceJpaRepository.findAllByDueDateBeforeAndStatus(dueDate, status);

        assertThat(result).hasSize(1).containsExactly(domainInvoice);
        verify(delegateRepository).findAllByDueDateBeforeAndStatus(dueDate, status);
        verify(mapper).toDomain(entityInvoice);
    }

    @Test
    void save_CallsDelegateAndMapper() {
        when(mapper.toEntity(domainInvoice)).thenReturn(entityInvoice);
        when(delegateRepository.save(entityInvoice)).thenReturn(entityInvoice);
        when(mapper.toDomain(entityInvoice)).thenReturn(domainInvoice);

        MonthlyInvoice result = monthlyInvoiceJpaRepository.save(domainInvoice);

        assertThat(result).isEqualTo(domainInvoice);
        verify(mapper).toEntity(domainInvoice);
        verify(delegateRepository).save(entityInvoice);
        verify(mapper).toDomain(entityInvoice);
    }

    @Test
    void deleteById_CallsDelegate() {
        monthlyInvoiceJpaRepository.deleteById(invoiceId);

        verify(delegateRepository).deleteById(entityId);
    }

    @Test
    void existsById_CallsDelegate() {
        when(delegateRepository.existsById(entityId)).thenReturn(true);

        boolean result = monthlyInvoiceJpaRepository.existsById(invoiceId);

        assertThat(result).isTrue();
        verify(delegateRepository).existsById(entityId);
    }

    @Test
    void findAll_WithPageable_ReturnsPage() {
        Pageable pageable = PageRequest.of(0, 10);
        Page<MonthlyInvoiceEntity> entityPage = new PageImpl<>(List.of(entityInvoice));
        when(delegateRepository.findAll(pageable)).thenReturn(entityPage);
        when(mapper.toDomain(entityInvoice)).thenReturn(domainInvoice);

        Page<MonthlyInvoice> result = monthlyInvoiceJpaRepository.findAll(pageable);

        assertThat(result.getContent()).hasSize(1).containsExactly(domainInvoice);
        verify(delegateRepository).findAll(pageable);
        verify(mapper).toDomain(entityInvoice);
    }

    @Test
    void findAll_WithSpecificationAndPageable_ReturnsPage() {
        Pageable pageable = PageRequest.of(0, 10);
        @SuppressWarnings("unchecked")
        Specification<MonthlyInvoiceEntity> spec = mock(Specification.class);
        Page<MonthlyInvoiceEntity> entityPage = new PageImpl<>(List.of(entityInvoice));
        when(delegateRepository.findAll(eq(spec), eq(pageable))).thenReturn(entityPage);
        when(mapper.toDomain(entityInvoice)).thenReturn(domainInvoice);

        Page<MonthlyInvoice> result = monthlyInvoiceJpaRepository.findAll(spec, pageable);

        assertThat(result.getContent()).hasSize(1).containsExactly(domainInvoice);
        verify(delegateRepository).findAll(spec, pageable);
        verify(mapper).toDomain(entityInvoice);
    }

    @Test
    void count_CallsDelegate() {
        when(delegateRepository.count()).thenReturn(5L);

        long result = monthlyInvoiceJpaRepository.count();

        assertThat(result).isEqualTo(5L);
        verify(delegateRepository).count();
    }
}