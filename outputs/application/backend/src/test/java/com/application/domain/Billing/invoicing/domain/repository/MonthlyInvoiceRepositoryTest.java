package com.application.domain.Billing.invoicing.domain.repository;

import com.application.domain.Billing.invoicing.domain.MonthlyInvoice;
import com.application.domain.Billing.invoicing.valueobject.MonthlyInvoiceId;
import com.application.domain.SchoolManagement.valueobject.SchoolId;
import com.application.domain.SchoolManagement.valueobject.StudentId;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.Month;
import java.time.Year;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class MonthlyInvoiceRepositoryTest {

    @Mock
    private MonthlyInvoiceRepository monthlyInvoiceRepository;

    private MonthlyInvoiceId invoiceId;
    private StudentId studentId;
    private SchoolId schoolId;
    private MonthlyInvoice monthlyInvoice;

    @BeforeEach
    void setUp() {
        invoiceId = new MonthlyInvoiceId();
        studentId = new StudentId();
        schoolId = new SchoolId();
        monthlyInvoice = mock(MonthlyInvoice.class);
        when(monthlyInvoice.getId()).thenReturn(invoiceId);
        when(monthlyInvoice.getStudentId()).thenReturn(studentId);
        when(monthlyInvoice.getSchoolId()).thenReturn(schoolId);
    }

    @Test
    void save_ShouldPersistInvoice() {
        monthlyInvoiceRepository.save(monthlyInvoice);
        verify(monthlyInvoiceRepository, times(1)).save(monthlyInvoice);
    }

    @Test
    void findById_WhenInvoiceExists_ShouldReturnInvoice() {
        when(monthlyInvoiceRepository.findById(invoiceId)).thenReturn(Optional.of(monthlyInvoice));
        Optional<MonthlyInvoice> result = monthlyInvoiceRepository.findById(invoiceId);
        assertTrue(result.isPresent());
        assertEquals(monthlyInvoice, result.get());
    }

    @Test
    void findById_WhenInvoiceDoesNotExist_ShouldReturnEmpty() {
        when(monthlyInvoiceRepository.findById(invoiceId)).thenReturn(Optional.empty());
        Optional<MonthlyInvoice> result = monthlyInvoiceRepository.findById(invoiceId);
        assertFalse(result.isPresent());
    }

    @Test
    void findByStudentId_ShouldReturnList() {
        List<MonthlyInvoice> expectedList = List.of(monthlyInvoice);
        when(monthlyInvoiceRepository.findByStudentId(studentId)).thenReturn(expectedList);
        List<MonthlyInvoice> result = monthlyInvoiceRepository.findByStudentId(studentId);
        assertEquals(expectedList, result);
    }

    @Test
    void findBySchoolId_ShouldReturnList() {
        List<MonthlyInvoice> expectedList = List.of(monthlyInvoice);
        when(monthlyInvoiceRepository.findBySchoolId(schoolId)).thenReturn(expectedList);
        List<MonthlyInvoice> result = monthlyInvoiceRepository.findBySchoolId(schoolId);
        assertEquals(expectedList, result);
    }

    @Test
    void existsByStudentIdAndBillingPeriod_WhenExists_ShouldReturnTrue() {
        Year year = Year.of(2024);
        Month month = Month.JANUARY;
        when(monthlyInvoiceRepository.existsByStudentIdAndBillingPeriod(studentId, year, month)).thenReturn(true);
        boolean result = monthlyInvoiceRepository.existsByStudentIdAndBillingPeriod(studentId, year, month);
        assertTrue(result);
    }

    @Test
    void existsByStudentIdAndBillingPeriod_WhenNotExists_ShouldReturnFalse() {
        Year year = Year.of(2024);
        Month month = Month.JANUARY;
        when(monthlyInvoiceRepository.existsByStudentIdAndBillingPeriod(studentId, year, month)).thenReturn(false);
        boolean result = monthlyInvoiceRepository.existsByStudentIdAndBillingPeriod(studentId, year, month);
        assertFalse(result);
    }

    @Test
    void delete_ShouldRemoveInvoice() {
        monthlyInvoiceRepository.delete(monthlyInvoice);
        verify(monthlyInvoiceRepository, times(1)).delete(monthlyInvoice);
    }
}