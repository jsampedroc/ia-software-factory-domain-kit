package com.application.application.Billing.invoicing.application.generate;

import com.application.domain.Billing.invoicing.domain.MonthlyInvoice;
import com.application.domain.Billing.invoicing.domain.repository.MonthlyInvoiceRepository;
import com.application.domain.Billing.invoicing.domain.valueobject.MonthlyInvoiceId;
import com.application.domain.Billing.invoicing.domain.valueobject.BillingConceptId;
import com.application.domain.Billing.invoicing.domain.InvoiceItem;
import com.application.domain.Billing.invoicing.domain.valueobject.InvoiceItemId;
import com.application.domain.SchoolManagement.student.domain.Student;
import com.application.domain.SchoolManagement.student.domain.repository.StudentRepository;
import com.application.domain.SchoolManagement.student.domain.valueobject.StudentId;
import com.application.domain.SchoolManagement.school.domain.School;
import com.application.domain.SchoolManagement.school.domain.repository.SchoolRepository;
import com.application.domain.SchoolManagement.school.domain.valueobject.SchoolId;
import com.application.domain.Billing.invoicing.domain.BillingConcept;
import com.application.domain.Billing.invoicing.domain.repository.BillingConceptRepository;
import com.application.domain.shared.valueobject.Money;
import com.application.domain.exception.DomainException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.YearMonth;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class GenerateMonthlyInvoiceCommandHandlerTest {

    @Mock
    private MonthlyInvoiceRepository monthlyInvoiceRepository;
    @Mock
    private StudentRepository studentRepository;
    @Mock
    private SchoolRepository schoolRepository;
    @Mock
    private BillingConceptRepository billingConceptRepository;

    private GenerateMonthlyInvoiceCommandHandler handler;

    @BeforeEach
    void setUp() {
        handler = new GenerateMonthlyInvoiceCommandHandler(
                monthlyInvoiceRepository,
                studentRepository,
                schoolRepository,
                billingConceptRepository
        );
    }

    @Test
    void handle_ShouldGenerateInvoiceSuccessfully() {
        // Arrange
        UUID studentId = UUID.randomUUID();
        UUID schoolId = UUID.randomUUID();
        UUID billingConceptId = UUID.randomUUID();
        YearMonth billingPeriod = YearMonth.of(2024, 1);
        LocalDate issueDate = LocalDate.of(2024, 1, 1);
        LocalDate dueDate = LocalDate.of(2024, 1, 15);

        GenerateMonthlyInvoiceCommand command = new GenerateMonthlyInvoiceCommand(
                studentId,
                billingPeriod,
                issueDate,
                dueDate,
                schoolId,
                List.of(billingConceptId)
        );

        Student mockStudent = mock(Student.class);
        when(mockStudent.getId()).thenReturn(new StudentId(studentId));
        when(mockStudent.getCurrentClassroomId()).thenReturn(null);
        when(studentRepository.findById(new StudentId(studentId))).thenReturn(Optional.of(mockStudent));

        School mockSchool = mock(School.class);
        when(mockSchool.getId()).thenReturn(new SchoolId(schoolId));
        when(schoolRepository.findById(new SchoolId(schoolId))).thenReturn(Optional.of(mockSchool));

        BillingConcept mockBillingConcept = mock(BillingConcept.class);
        when(mockBillingConcept.getId()).thenReturn(new BillingConceptId(billingConceptId));
        when(mockBillingConcept.getDefaultAmount()).thenReturn(new Money(new BigDecimal("100.00"), "USD"));
        when(mockBillingConcept.isActive()).thenReturn(true);
        when(mockBillingConcept.isApplicableForGrade(any())).thenReturn(true);
        when(billingConceptRepository.findById(new BillingConceptId(billingConceptId))).thenReturn(Optional.of(mockBillingConcept));

        when(monthlyInvoiceRepository.existsByStudentIdAndBillingPeriod(new StudentId(studentId), billingPeriod)).thenReturn(false);

        MonthlyInvoice savedInvoice = mock(MonthlyInvoice.class);
        when(monthlyInvoiceRepository.save(any(MonthlyInvoice.class))).thenReturn(savedInvoice);

        // Act
        MonthlyInvoice result = handler.handle(command);

        // Assert
        assertNotNull(result);
        verify(monthlyInvoiceRepository).save(any(MonthlyInvoice.class));
        verify(studentRepository).findById(new StudentId(studentId));
        verify(schoolRepository).findById(new SchoolId(schoolId));
        verify(billingConceptRepository).findById(new BillingConceptId(billingConceptId));
        verify(monthlyInvoiceRepository).existsByStudentIdAndBillingPeriod(new StudentId(studentId), billingPeriod);
    }

    @Test
    void handle_ShouldThrowDomainException_WhenStudentNotFound() {
        // Arrange
        UUID studentId = UUID.randomUUID();
        GenerateMonthlyInvoiceCommand command = new GenerateMonthlyInvoiceCommand(
                studentId,
                YearMonth.of(2024, 1),
                LocalDate.now(),
                LocalDate.now().plusDays(15),
                UUID.randomUUID(),
                List.of(UUID.randomUUID())
        );

        when(studentRepository.findById(new StudentId(studentId))).thenReturn(Optional.empty());

        // Act & Assert
        DomainException exception = assertThrows(DomainException.class, () -> handler.handle(command));
        assertTrue(exception.getMessage().contains("Student not found"));
        verify(studentRepository).findById(new StudentId(studentId));
        verifyNoInteractions(monthlyInvoiceRepository, schoolRepository, billingConceptRepository);
    }

    @Test
    void handle_ShouldThrowDomainException_WhenSchoolNotFound() {
        // Arrange
        UUID studentId = UUID.randomUUID();
        UUID schoolId = UUID.randomUUID();
        GenerateMonthlyInvoiceCommand command = new GenerateMonthlyInvoiceCommand(
                studentId,
                YearMonth.of(2024, 1),
                LocalDate.now(),
                LocalDate.now().plusDays(15),
                schoolId,
                List.of(UUID.randomUUID())
        );

        Student mockStudent = mock(Student.class);
        when(mockStudent.getId()).thenReturn(new StudentId(studentId));
        when(studentRepository.findById(new StudentId(studentId))).thenReturn(Optional.of(mockStudent));
        when(schoolRepository.findById(new SchoolId(schoolId))).thenReturn(Optional.empty());

        // Act & Assert
        DomainException exception = assertThrows(DomainException.class, () -> handler.handle(command));
        assertTrue(exception.getMessage().contains("School not found"));
        verify(studentRepository).findById(new StudentId(studentId));
        verify(schoolRepository).findById(new SchoolId(schoolId));
        verifyNoInteractions(monthlyInvoiceRepository, billingConceptRepository);
    }

    @Test
    void handle_ShouldThrowDomainException_WhenBillingConceptNotFound() {
        // Arrange
        UUID studentId = UUID.randomUUID();
        UUID schoolId = UUID.randomUUID();
        UUID billingConceptId = UUID.randomUUID();
        GenerateMonthlyInvoiceCommand command = new GenerateMonthlyInvoiceCommand(
                studentId,
                YearMonth.of(2024, 1),
                LocalDate.now(),
                LocalDate.now().plusDays(15),
                schoolId,
                List.of(billingConceptId)
        );

        Student mockStudent = mock(Student.class);
        when(mockStudent.getId()).thenReturn(new StudentId(studentId));
        when(studentRepository.findById(new StudentId(studentId))).thenReturn(Optional.of(mockStudent));

        School mockSchool = mock(School.class);
        when(mockSchool.getId()).thenReturn(new SchoolId(schoolId));
        when(schoolRepository.findById(new SchoolId(schoolId))).thenReturn(Optional.of(mockSchool));

        when(billingConceptRepository.findById(new BillingConceptId(billingConceptId))).thenReturn(Optional.empty());

        // Act & Assert
        DomainException exception = assertThrows(DomainException.class, () -> handler.handle(command));
        assertTrue(exception.getMessage().contains("Billing concept not found"));
        verify(studentRepository).findById(new StudentId(studentId));
        verify(schoolRepository).findById(new SchoolId(schoolId));
        verify(billingConceptRepository).findById(new BillingConceptId(billingConceptId));
        verifyNoInteractions(monthlyInvoiceRepository);
    }

    @Test
    void handle_ShouldThrowDomainException_WhenInvoiceAlreadyExistsForPeriod() {
        // Arrange
        UUID studentId = UUID.randomUUID();
        UUID schoolId = UUID.randomUUID();
        UUID billingConceptId = UUID.randomUUID();
        YearMonth billingPeriod = YearMonth.of(2024, 1);
        GenerateMonthlyInvoiceCommand command = new GenerateMonthlyInvoiceCommand(
                studentId,
                billingPeriod,
                LocalDate.now(),
                LocalDate.now().plusDays(15),
                schoolId,
                List.of(billingConceptId)
        );

        Student mockStudent = mock(Student.class);
        when(mockStudent.getId()).thenReturn(new StudentId(studentId));
        when(studentRepository.findById(new StudentId(studentId))).thenReturn(Optional.of(mockStudent));

        School mockSchool = mock(School.class);
        when(mockSchool.getId()).thenReturn(new SchoolId(schoolId));
        when(schoolRepository.findById(new SchoolId(schoolId))).thenReturn(Optional.of(mockSchool));

        BillingConcept mockBillingConcept = mock(BillingConcept.class);
        when(mockBillingConcept.getId()).thenReturn(new BillingConceptId(billingConceptId));
        when(billingConceptRepository.findById(new BillingConceptId(billingConceptId))).thenReturn(Optional.of(mockBillingConcept));

        when(monthlyInvoiceRepository.existsByStudentIdAndBillingPeriod(new StudentId(studentId), billingPeriod)).thenReturn(true);

        // Act & Assert
        DomainException exception = assertThrows(DomainException.class, () -> handler.handle(command));
        assertTrue(exception.getMessage().contains("already exists"));
        verify(monthlyInvoiceRepository).existsByStudentIdAndBillingPeriod(new StudentId(studentId), billingPeriod);
        verify(monthlyInvoiceRepository, never()).save(any());
    }

    @Test
    void handle_ShouldThrowDomainException_WhenBillingConceptNotActive() {
        // Arrange
        UUID studentId = UUID.randomUUID();
        UUID schoolId = UUID.randomUUID();
        UUID billingConceptId = UUID.randomUUID();
        GenerateMonthlyInvoiceCommand command = new GenerateMonthlyInvoiceCommand(
                studentId,
                YearMonth.of(2024, 1),
                LocalDate.now(),
                LocalDate.now().plusDays(15),
                schoolId,
                List.of(billingConceptId)
        );

        Student mockStudent = mock(Student.class);
        when(mockStudent.getId()).thenReturn(new StudentId(studentId));
        when(studentRepository.findById(new StudentId(studentId))).thenReturn(Optional.of(mockStudent));

        School mockSchool = mock(School.class);
        when(mockSchool.getId()).thenReturn(new SchoolId(schoolId));
        when(schoolRepository.findById(new SchoolId(schoolId))).thenReturn(Optional.of(mockSchool));

        BillingConcept mockBillingConcept = mock(BillingConcept.class);
        when(mockBillingConcept.getId()).thenReturn(new BillingConceptId(billingConceptId));
        when(mockBillingConcept.isActive()).thenReturn(false);
        when(billingConceptRepository.findById(new BillingConceptId(billingConceptId))).thenReturn(Optional.of(mockBillingConcept));

        when(monthlyInvoiceRepository.existsByStudentIdAndBillingPeriod(new StudentId(studentId), command.billingPeriod())).thenReturn(false);

        // Act & Assert
        DomainException exception = assertThrows(DomainException.class, () -> handler.handle(command));
        assertTrue(exception.getMessage().contains("not active"));
        verify(billingConceptRepository).findById(new BillingConceptId(billingConceptId));
        verify(monthlyInvoiceRepository, never()).save(any());
    }

    @Test
    void handle_ShouldCalculateTotalAmountCorrectly() {
        // Arrange
        UUID studentId = UUID.randomUUID();
        UUID schoolId = UUID.randomUUID();
        UUID conceptId1 = UUID.randomUUID();
        UUID conceptId2 = UUID.randomUUID();
        YearMonth billingPeriod = YearMonth.of(2024, 1);
        LocalDate issueDate = LocalDate.of(2024, 1, 1);
        LocalDate dueDate = LocalDate.of(2024, 1, 15);

        GenerateMonthlyInvoiceCommand command = new GenerateMonthlyInvoiceCommand(
                studentId,
                billingPeriod,
                issueDate,
                dueDate,
                schoolId,
                List.of(conceptId1, conceptId2)
        );

        Student mockStudent = mock(Student.class);
        when(mockStudent.getId()).thenReturn(new StudentId(studentId));
        when(mockStudent.getCurrentClassroomId()).thenReturn(null);
        when(studentRepository.findById(new StudentId(studentId))).thenReturn(Optional.of(mockStudent));

        School mockSchool = mock(School.class);
        when(mockSchool.getId()).thenReturn(new SchoolId(schoolId));
        when(schoolRepository.findById(new SchoolId(schoolId))).thenReturn(Optional.of(mockSchool));

        BillingConcept mockConcept1 = mock(BillingConcept.class);
        when(mockConcept1.getId()).thenReturn(new BillingConceptId(conceptId1));
        when(mockConcept1.getDefaultAmount()).thenReturn(new Money(new BigDecimal("150.00"), "USD"));
        when(mockConcept1.isActive()).thenReturn(true);
        when(mockConcept1.isApplicableForGrade(any())).thenReturn(true);
        when(billingConceptRepository.findById(new BillingConceptId(conceptId1))).thenReturn(Optional.of(mockConcept1));

        BillingConcept mockConcept2 = mock(BillingConcept.class);
        when(mockConcept2.getId()).thenReturn(new BillingConceptId(conceptId2));
        when(mockConcept2.getDefaultAmount()).thenReturn(new Money(new BigDecimal("75.50"), "USD"));
        when(mockConcept2.isActive()).thenReturn(true);
        when(mockConcept2.isApplicableForGrade(any())).thenReturn(true);
        when(billingConceptRepository.findById(new BillingConceptId(conceptId2))).thenReturn(Optional.of(mockConcept2));

        when(monthlyInvoiceRepository.existsByStudentIdAndBillingPeriod(new StudentId(studentId), billingPeriod)).thenReturn(false);

        MonthlyInvoice savedInvoice = mock(MonthlyInvoice.class);
        when(savedInvoice.getTotalAmount()).thenReturn(new Money(new BigDecimal("225.50"), "USD"));
        when(monthlyInvoiceRepository.save(any(MonthlyInvoice.class))).thenReturn(savedInvoice);

        // Act
        MonthlyInvoice result = handler.handle(command);

        // Assert
        assertNotNull(result);
        assertEquals(new BigDecimal("225.50"), result.getTotalAmount().amount());
        verify(monthlyInvoiceRepository).save(any(MonthlyInvoice.class));
    }
}