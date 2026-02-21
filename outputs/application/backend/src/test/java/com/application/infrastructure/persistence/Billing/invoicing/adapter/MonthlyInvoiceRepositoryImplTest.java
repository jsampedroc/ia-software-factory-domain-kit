package com.application;

import com.application.domain.Billing.invoicing.domain.MonthlyInvoice;
import com.application.domain.Billing.invoicing.domain.repository.MonthlyInvoiceRepository;
import com.application.domain.Billing.invoicing.valueobject.MonthlyInvoiceId;
import com.application.domain.SchoolManagement.valueobject.StudentId;
import com.application.infrastructure.persistence.Billing.invoicing.adapter.MonthlyInvoiceJpaRepository;
import com.application.infrastructure.persistence.Billing.invoicing.entity.MonthlyInvoiceEntity;
import com.application.infrastructure.persistence.Billing.invoicing.mapper.MonthlyInvoiceEntityMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.time.YearMonth;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class MonthlyInvoiceRepositoryImplTest {

    @Mock
    private MonthlyInvoiceJpaRepository monthlyInvoiceJpaRepository;

    @Mock
    private MonthlyInvoiceEntityMapper monthlyInvoiceEntityMapper;

    @InjectMocks
    private MonthlyInvoiceRepositoryImpl monthlyInvoiceRepositoryImpl;

    private MonthlyInvoice monthlyInvoice;
    private MonthlyInvoiceEntity monthlyInvoiceEntity;
    private MonthlyInvoiceId monthlyInvoiceId;
    private StudentId studentId;
    private YearMonth billingPeriod;

    @BeforeEach
    void setUp() {
        monthlyInvoiceId = new MonthlyInvoiceId(UUID.randomUUID());
        studentId = new StudentId(UUID.randomUUID());
        billingPeriod = YearMonth.of(2024, 1);

        monthlyInvoice = mock(MonthlyInvoice.class);
        monthlyInvoiceEntity = mock(MonthlyInvoiceEntity.class);

        when(monthlyInvoice.getId()).thenReturn(monthlyInvoiceId);
        when(monthlyInvoice.getStudentId()).thenReturn(studentId);
        when(monthlyInvoice.getBillingPeriod()).thenReturn(billingPeriod);
    }

    @Test
    void save_ShouldCallJpaRepositoryAndMapper() {
        when(monthlyInvoiceEntityMapper.toEntity(monthlyInvoice)).thenReturn(monthlyInvoiceEntity);
        when(monthlyInvoiceJpaRepository.save(monthlyInvoiceEntity)).thenReturn(monthlyInvoiceEntity);
        when(monthlyInvoiceEntityMapper.toDomain(monthlyInvoiceEntity)).thenReturn(monthlyInvoice);

        MonthlyInvoice result = monthlyInvoiceRepositoryImpl.save(monthlyInvoice);

        assertNotNull(result);
        verify(monthlyInvoiceEntityMapper).toEntity(monthlyInvoice);
        verify(monthlyInvoiceJpaRepository).save(monthlyInvoiceEntity);
        verify(monthlyInvoiceEntityMapper).toDomain(monthlyInvoiceEntity);
    }

    @Test
    void findById_WhenExists_ShouldReturnInvoice() {
        when(monthlyInvoiceJpaRepository.findById(monthlyInvoiceId.getValue())).thenReturn(Optional.of(monthlyInvoiceEntity));
        when(monthlyInvoiceEntityMapper.toDomain(monthlyInvoiceEntity)).thenReturn(monthlyInvoice);

        Optional<MonthlyInvoice> result = monthlyInvoiceRepositoryImpl.findById(monthlyInvoiceId);

        assertTrue(result.isPresent());
        assertEquals(monthlyInvoice, result.get());
        verify(monthlyInvoiceJpaRepository).findById(monthlyInvoiceId.getValue());
        verify(monthlyInvoiceEntityMapper).toDomain(monthlyInvoiceEntity);
    }

    @Test
    void findById_WhenNotExists_ShouldReturnEmpty() {
        when(monthlyInvoiceJpaRepository.findById(monthlyInvoiceId.getValue())).thenReturn(Optional.empty());

        Optional<MonthlyInvoice> result = monthlyInvoiceRepositoryImpl.findById(monthlyInvoiceId);

        assertFalse(result.isPresent());
        verify(monthlyInvoiceJpaRepository).findById(monthlyInvoiceId.getValue());
        verify(monthlyInvoiceEntityMapper, never()).toDomain(any());
    }

    @Test
    void findByStudentIdAndBillingPeriod_WhenExists_ShouldReturnInvoice() {
        when(monthlyInvoiceJpaRepository.findByStudentIdAndBillingPeriod(studentId.getValue(), billingPeriod)).thenReturn(Optional.of(monthlyInvoiceEntity));
        when(monthlyInvoiceEntityMapper.toDomain(monthlyInvoiceEntity)).thenReturn(monthlyInvoice);

        Optional<MonthlyInvoice> result = monthlyInvoiceRepositoryImpl.findByStudentIdAndBillingPeriod(studentId, billingPeriod);

        assertTrue(result.isPresent());
        assertEquals(monthlyInvoice, result.get());
        verify(monthlyInvoiceJpaRepository).findByStudentIdAndBillingPeriod(studentId.getValue(), billingPeriod);
        verify(monthlyInvoiceEntityMapper).toDomain(monthlyInvoiceEntity);
    }

    @Test
    void findByStudentIdAndBillingPeriod_WhenNotExists_ShouldReturnEmpty() {
        when(monthlyInvoiceJpaRepository.findByStudentIdAndBillingPeriod(studentId.getValue(), billingPeriod)).thenReturn(Optional.empty());

        Optional<MonthlyInvoice> result = monthlyInvoiceRepositoryImpl.findByStudentIdAndBillingPeriod(studentId, billingPeriod);

        assertFalse(result.isPresent());
        verify(monthlyInvoiceJpaRepository).findByStudentIdAndBillingPeriod(studentId.getValue(), billingPeriod);
        verify(monthlyInvoiceEntityMapper, never()).toDomain(any());
    }

    @Test
    void findByStudentIdAndPeriodRange_ShouldReturnList() {
        YearMonth start = YearMonth.of(2024, 1);
        YearMonth end = YearMonth.of(2024, 6);
        List<MonthlyInvoiceEntity> entityList = List.of(monthlyInvoiceEntity);
        when(monthlyInvoiceJpaRepository.findByStudentIdAndBillingPeriodBetween(studentId.getValue(), start, end)).thenReturn(entityList);
        when(monthlyInvoiceEntityMapper.toDomain(monthlyInvoiceEntity)).thenReturn(monthlyInvoice);

        List<MonthlyInvoice> result = monthlyInvoiceRepositoryImpl.findByStudentIdAndPeriodRange(studentId, start, end);

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(monthlyInvoice, result.get(0));
        verify(monthlyInvoiceJpaRepository).findByStudentIdAndBillingPeriodBetween(studentId.getValue(), start, end);
        verify(monthlyInvoiceEntityMapper).toDomain(monthlyInvoiceEntity);
    }

    @Test
    void findByDueDateBeforeAndStatus_ShouldReturnList() {
        LocalDate date = LocalDate.of(2024, 1, 31);
        String status = "PENDING";
        List<MonthlyInvoiceEntity> entityList = List.of(monthlyInvoiceEntity);
        when(monthlyInvoiceJpaRepository.findByDueDateBeforeAndStatus(date, status)).thenReturn(entityList);
        when(monthlyInvoiceEntityMapper.toDomain(monthlyInvoiceEntity)).thenReturn(monthlyInvoice);

        List<MonthlyInvoice> result = monthlyInvoiceRepositoryImpl.findByDueDateBeforeAndStatus(date, status);

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(monthlyInvoice, result.get(0));
        verify(monthlyInvoiceJpaRepository).findByDueDateBeforeAndStatus(date, status);
        verify(monthlyInvoiceEntityMapper).toDomain(monthlyInvoiceEntity);
    }

    @Test
    void delete_ShouldCallJpaRepository() {
        monthlyInvoiceRepositoryImpl.delete(monthlyInvoiceId);

        verify(monthlyInvoiceJpaRepository).deleteById(monthlyInvoiceId.getValue());
    }

    @Test
    void existsById_WhenExists_ShouldReturnTrue() {
        when(monthlyInvoiceJpaRepository.existsById(monthlyInvoiceId.getValue())).thenReturn(true);

        boolean result = monthlyInvoiceRepositoryImpl.existsById(monthlyInvoiceId);

        assertTrue(result);
        verify(monthlyInvoiceJpaRepository).existsById(monthlyInvoiceId.getValue());
    }

    @Test
    void existsById_WhenNotExists_ShouldReturnFalse() {
        when(monthlyInvoiceJpaRepository.existsById(monthlyInvoiceId.getValue())).thenReturn(false);

        boolean result = monthlyInvoiceRepositoryImpl.existsById(monthlyInvoiceId);

        assertFalse(result);
        verify(monthlyInvoiceJpaRepository).existsById(monthlyInvoiceId.getValue());
    }
}