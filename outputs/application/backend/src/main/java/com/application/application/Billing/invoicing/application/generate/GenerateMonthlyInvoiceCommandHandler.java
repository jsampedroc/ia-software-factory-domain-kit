package com.application.application.Billing.invoicing.application.generate;

import com.application.domain.Billing.invoicing.domain.MonthlyInvoice;
import com.application.domain.Billing.invoicing.domain.repository.MonthlyInvoiceRepository;
import com.application.domain.Billing.invoicing.domain.BillingConcept;
import com.application.domain.Billing.invoicing.domain.repository.BillingConceptRepository;
import com.application.domain.SchoolManagement.student.domain.Student;
import com.application.domain.SchoolManagement.student.domain.repository.StudentRepository;
import com.application.domain.SchoolManagement.school.domain.School;
import com.application.domain.SchoolManagement.school.domain.repository.SchoolRepository;
import com.application.domain.exception.DomainException;
import com.application.domain.shared.valueobject.Money;
import com.application.domain.Billing.valueobject.MonthlyInvoiceId;
import com.application.domain.SchoolManagement.valueobject.StudentId;
import com.application.domain.SchoolManagement.valueobject.SchoolId;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.YearMonth;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class GenerateMonthlyInvoiceCommandHandler {

    private final MonthlyInvoiceRepository monthlyInvoiceRepository;
    private final BillingConceptRepository billingConceptRepository;
    private final StudentRepository studentRepository;
    private final SchoolRepository schoolRepository;

    @Transactional
    public MonthlyInvoice handle(GenerateMonthlyInvoiceCommand command) {
        StudentId studentId = new StudentId(command.studentId());
        Student student = studentRepository.findById(studentId)
                .orElseThrow(() -> new DomainException("Student not found with id: " + command.studentId()));
        if (!student.isActive()) {
            throw new DomainException("Cannot generate invoice for an inactive student.");
        }

        SchoolId schoolId = student.getSchoolId();
        School school = schoolRepository.findById(schoolId)
                .orElseThrow(() -> new DomainException("School not found for student."));
        if (!school.isActive()) {
            throw new DomainException("Cannot generate invoice for an inactive school.");
        }

        YearMonth billingPeriod = YearMonth.parse(command.billingPeriod());
        Optional<MonthlyInvoice> existingInvoice = monthlyInvoiceRepository.findByStudentIdAndBillingPeriod(studentId, billingPeriod);
        if (existingInvoice.isPresent()) {
            throw new DomainException("An invoice already exists for student " + command.studentId() + " and period " + command.billingPeriod());
        }

        Integer studentGradeLevel = student.getCurrentClassroomId()
                .flatMap(classroomId -> school.getClassroomById(classroomId))
                .map(classroom -> classroom.getGradeLevel())
                .orElse(null);

        List<BillingConcept> applicableConcepts = billingConceptRepository.findActiveConcepts();
        List<BillingConcept> conceptsForStudent = applicableConcepts.stream()
                .filter(concept -> concept.isApplicableToGrade(studentGradeLevel))
                .toList();

        if (conceptsForStudent.isEmpty()) {
            throw new DomainException("No billing concepts found applicable for the student's grade level.");
        }

        MonthlyInvoiceId invoiceId = new MonthlyInvoiceId(UUID.randomUUID());
        LocalDate issueDate = LocalDate.now();
        LocalDate dueDate = issueDate.plusDays(10);

        MonthlyInvoice invoice = MonthlyInvoice.create(
                invoiceId,
                studentId,
                billingPeriod,
                issueDate,
                dueDate,
                schoolId
        );

        for (BillingConcept concept : conceptsForStudent) {
            int quantity = concept.isMonthly() ? 1 : 1;
            Money unitPrice = concept.getDefaultAmount();
            Money subtotal = new Money(unitPrice.amount().multiply(BigDecimal.valueOf(quantity)), unitPrice.currency());

            invoice.addItem(
                    concept.getId(),
                    concept.getName(),
                    quantity,
                    unitPrice,
                    subtotal
            );
        }

        monthlyInvoiceRepository.save(invoice);
        return invoice;
    }
}