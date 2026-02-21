package com.application.domain.Billing.invoicing.domain;

import com.application.domain.Billing.invoicing.valueobject.InvoiceItemId;
import com.application.domain.Billing.invoicing.valueobject.MonthlyInvoiceId;
import com.application.domain.Billing.valueobject.BillingConceptId;
import com.application.domain.SchoolManagement.valueobject.SchoolId;
import com.application.domain.SchoolManagement.valueobject.StudentId;
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
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class MonthlyInvoiceTest {

    private MonthlyInvoiceId invoiceId;
    private StudentId studentId;
    private SchoolId schoolId;
    private YearMonth billingPeriod;
    private LocalDate issueDate;
    private LocalDate dueDate;

    @Mock
    private InvoiceItem mockItem1;
    @Mock
    private InvoiceItem mockItem2;

    @BeforeEach
    void setUp() {
        invoiceId = new MonthlyInvoiceId(UUID.randomUUID());
        studentId = new StudentId(UUID.randomUUID());
        schoolId = new SchoolId(UUID.randomUUID());
        billingPeriod = YearMonth.of(2024, 1);
        issueDate = LocalDate.of(2024, 1, 1);
        dueDate = LocalDate.of(2024, 1, 15);
    }

    @Test
    void create_ShouldCreateInvoiceWithPendingStatusAndCorrectTotal() {
        Money amount1 = new Money(new BigDecimal("100.00"), "USD");
        Money amount2 = new Money(new BigDecimal("50.00"), "USD");
        Money expectedTotal = new Money(new BigDecimal("150.00"), "USD");

        when(mockItem1.getSubtotal()).thenReturn(amount1);
        when(mockItem2.getSubtotal()).thenReturn(amount2);
        List<InvoiceItem> items = List.of(mockItem1, mockItem2);

        MonthlyInvoice invoice = MonthlyInvoice.create(
                invoiceId,
                studentId,
                billingPeriod,
                issueDate,
                dueDate,
                items,
                schoolId
        );

        assertNotNull(invoice);
        assertEquals(invoiceId, invoice.getId());
        assertEquals(studentId, invoice.getStudentId());
        assertEquals(billingPeriod, invoice.getBillingPeriod());
        assertEquals(issueDate, invoice.getIssueDate());
        assertEquals(dueDate, invoice.getDueDate());
        assertEquals(expectedTotal, invoice.getTotalAmount());
        assertEquals(InvoiceStatus.PENDING, invoice.getStatus());
        assertEquals(schoolId, invoice.getSchoolId());
        assertEquals(items, invoice.getItems());
    }

    @Test
    void create_ShouldThrowExceptionWhenDueDateIsBeforeIssueDate() {
        LocalDate invalidDueDate = issueDate.minusDays(1);
        List<InvoiceItem> items = List.of();

        DomainException exception = assertThrows(DomainException.class, () ->
                MonthlyInvoice.create(
                        invoiceId,
                        studentId,
                        billingPeriod,
                        issueDate,
                        invalidDueDate,
                        items,
                        schoolId
                )
        );
        assertTrue(exception.getMessage().contains("La fecha de vencimiento debe ser posterior a la de emisión"));
    }

    @Test
    void create_ShouldThrowExceptionWhenItemsListIsNull() {
        DomainException exception = assertThrows(DomainException.class, () ->
                MonthlyInvoice.create(
                        invoiceId,
                        studentId,
                        billingPeriod,
                        issueDate,
                        dueDate,
                        null,
                        schoolId
                )
        );
        assertTrue(exception.getMessage().contains("items"));
    }

    @Test
    void addItem_ShouldAddItemAndRecalculateTotal() {
        Money initialAmount = new Money(new BigDecimal("100.00"), "USD");
        Money newItemAmount = new Money(new BigDecimal("25.00"), "USD");
        Money expectedTotal = new Money(new BigDecimal("125.00"), "USD");

        when(mockItem1.getSubtotal()).thenReturn(initialAmount);
        List<InvoiceItem> initialItems = List.of(mockItem1);
        MonthlyInvoice invoice = MonthlyInvoice.create(
                invoiceId,
                studentId,
                billingPeriod,
                issueDate,
                dueDate,
                initialItems,
                schoolId
        );

        InvoiceItemId newItemId = new InvoiceItemId(UUID.randomUUID());
        BillingConceptId conceptId = new BillingConceptId(UUID.randomUUID());
        InvoiceItem newItem = InvoiceItem.create(
                newItemId,
                invoiceId,
                conceptId,
                "Extra",
                1,
                newItemAmount
        );

        invoice.addItem(newItem);

        assertTrue(invoice.getItems().contains(newItem));
        assertEquals(expectedTotal, invoice.getTotalAmount());
    }

    @Test
    void addItem_ShouldThrowExceptionWhenItemIsNull() {
        when(mockItem1.getSubtotal()).thenReturn(new Money(BigDecimal.ONE, "USD"));
        List<InvoiceItem> initialItems = List.of(mockItem1);
        MonthlyInvoice invoice = MonthlyInvoice.create(
                invoiceId,
                studentId,
                billingPeriod,
                issueDate,
                dueDate,
                initialItems,
                schoolId
        );

        DomainException exception = assertThrows(DomainException.class, () ->
                invoice.addItem(null)
        );
        assertTrue(exception.getMessage().contains("item"));
    }

    @Test
    void markAsPaid_ShouldChangeStatusToPaid() {
        when(mockItem1.getSubtotal()).thenReturn(new Money(BigDecimal.ONE, "USD"));
        List<InvoiceItem> items = List.of(mockItem1);
        MonthlyInvoice invoice = MonthlyInvoice.create(
                invoiceId,
                studentId,
                billingPeriod,
                issueDate,
                dueDate,
                items,
                schoolId
        );

        invoice.markAsPaid();

        assertEquals(InvoiceStatus.PAID, invoice.getStatus());
    }

    @Test
    void markAsPaid_ShouldThrowExceptionWhenAlreadyPaid() {
        when(mockItem1.getSubtotal()).thenReturn(new Money(BigDecimal.ONE, "USD"));
        List<InvoiceItem> items = List.of(mockItem1);
        MonthlyInvoice invoice = MonthlyInvoice.create(
                invoiceId,
                studentId,
                billingPeriod,
                issueDate,
                dueDate,
                items,
                schoolId
        );
        invoice.markAsPaid();

        DomainException exception = assertThrows(DomainException.class, invoice::markAsPaid);
        assertTrue(exception.getMessage().contains("PAID"));
    }

    @Test
    void markAsOverdue_ShouldChangeStatusToOverdue() {
        when(mockItem1.getSubtotal()).thenReturn(new Money(BigDecimal.ONE, "USD"));
        List<InvoiceItem> items = List.of(mockItem1);
        MonthlyInvoice invoice = MonthlyInvoice.create(
                invoiceId,
                studentId,
                billingPeriod,
                issueDate,
                dueDate,
                items,
                schoolId
        );

        invoice.markAsOverdue();

        assertEquals(InvoiceStatus.OVERDUE, invoice.getStatus());
    }

    @Test
    void markAsOverdue_ShouldThrowExceptionWhenAlreadyPaid() {
        when(mockItem1.getSubtotal()).thenReturn(new Money(BigDecimal.ONE, "USD"));
        List<InvoiceItem> items = List.of(mockItem1);
        MonthlyInvoice invoice = MonthlyInvoice.create(
                invoiceId,
                studentId,
                billingPeriod,
                issueDate,
                dueDate,
                items,
                schoolId
        );
        invoice.markAsPaid();

        DomainException exception = assertThrows(DomainException.class, invoice::markAsOverdue);
        assertTrue(exception.getMessage().contains("PAID"));
    }

    @Test
    void calculateBalance_ShouldReturnCorrectBalance() {
        Money itemAmount = new Money(new BigDecimal("200.00"), "USD");
        when(mockItem1.getSubtotal()).thenReturn(itemAmount);
        List<InvoiceItem> items = List.of(mockItem1);
        MonthlyInvoice invoice = MonthlyInvoice.create(
                invoiceId,
                studentId,
                billingPeriod,
                issueDate,
                dueDate,
                items,
                schoolId
        );

        Money paymentAmount = new Money(new BigDecimal("150.00"), "USD");
        Money expectedBalance = new Money(new BigDecimal("50.00"), "USD");

        Money balance = invoice.calculateBalance(paymentAmount);

        assertEquals(expectedBalance, balance);
    }

    @Test
    void calculateBalance_ShouldThrowExceptionWhenPaymentAmountIsNull() {
        when(mockItem1.getSubtotal()).thenReturn(new Money(BigDecimal.ONE, "USD"));
        List<InvoiceItem> items = List.of(mockItem1);
        MonthlyInvoice invoice = MonthlyInvoice.create(
                invoiceId,
                studentId,
                billingPeriod,
                issueDate,
                dueDate,
                items,
                schoolId
        );

        DomainException exception = assertThrows(DomainException.class, () ->
                invoice.calculateBalance(null)
        );
        assertTrue(exception.getMessage().contains("paymentAmount"));
    }

    @Test
    void calculateBalance_ShouldThrowExceptionWhenCurrenciesMismatch() {
        Money itemAmount = new Money(new BigDecimal("200.00"), "USD");
        when(mockItem1.getSubtotal()).thenReturn(itemAmount);
        List<InvoiceItem> items = List.of(mockItem1);
        MonthlyInvoice invoice = MonthlyInvoice.create(
                invoiceId,
                studentId,
                billingPeriod,
                issueDate,
                dueDate,
                items,
                schoolId
        );

        Money paymentAmount = new Money(new BigDecimal("150.00"), "EUR");

        DomainException exception = assertThrows(DomainException.class, () ->
                invoice.calculateBalance(paymentAmount)
        );
        assertTrue(exception.getMessage().contains("moneda"));
    }
}