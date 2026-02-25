package com.application.domain.model;

import com.application.domain.exception.DomainException;
import com.application.domain.valueobject.InvoiceId;
import com.application.domain.valueobject.PatientId;
import com.application.domain.valueobject.AppointmentId;
import com.application.domain.valueobject.PaymentPlanId;
import com.application.domain.enums.InvoiceStatus;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@ExtendWith(MockitoExtension.class)
class InvoiceTest {

    private static final InvoiceId VALID_INVOICE_ID = new InvoiceId(UUID.randomUUID());
    private static final PatientId VALID_PATIENT_ID = new PatientId(UUID.randomUUID());
    private static final AppointmentId VALID_APPOINTMENT_ID = new AppointmentId(UUID.randomUUID());
    private static final PaymentPlanId VALID_PAYMENT_PLAN_ID = new PaymentPlanId(UUID.randomUUID());
    private static final String VALID_INVOICE_NUMBER = "INV-2024-001";
    private static final LocalDate TODAY = LocalDate.now();
    private static final LocalDate TOMORROW = TODAY.plusDays(1);
    private static final LocalDate YESTERDAY = TODAY.minusDays(1);
    private static final InvoiceItem VALID_ITEM = InvoiceItem.builder()
            .itemId(new InvoiceItemId(UUID.randomUUID()))
            .treatmentCode("FILLING")
            .description("Tooth Filling")
            .quantity(1)
            .unitPrice(new BigDecimal("150.00"))
            .amount(new BigDecimal("150.00"))
            .build();
    private static final List<InvoiceItem> VALID_ITEMS = List.of(VALID_ITEM);
    private static final BigDecimal VALID_SUBTOTAL = new BigDecimal("150.00");
    private static final BigDecimal VALID_TAX = new BigDecimal("15.00");
    private static final BigDecimal VALID_TOTAL = new BigDecimal("165.00");

    private Invoice createDraftInvoice() {
        return Invoice.builder()
                .id(VALID_INVOICE_ID)
                .patientId(VALID_PATIENT_ID)
                .appointmentId(VALID_APPOINTMENT_ID)
                .invoiceNumber(VALID_INVOICE_NUMBER)
                .issueDate(TODAY)
                .dueDate(TOMORROW)
                .items(VALID_ITEMS)
                .subtotal(VALID_SUBTOTAL)
                .tax(VALID_TAX)
                .total(VALID_TOTAL)
                .status(InvoiceStatus.DRAFT)
                .paymentPlanId(null)
                .build();
    }

    private Invoice createIssuedInvoice() {
        return Invoice.builder()
                .id(VALID_INVOICE_ID)
                .patientId(VALID_PATIENT_ID)
                .appointmentId(VALID_APPOINTMENT_ID)
                .invoiceNumber(VALID_INVOICE_NUMBER)
                .issueDate(TODAY)
                .dueDate(TOMORROW)
                .items(VALID_ITEMS)
                .subtotal(VALID_SUBTOTAL)
                .tax(VALID_TAX)
                .total(VALID_TOTAL)
                .status(InvoiceStatus.ISSUED)
                .paymentPlanId(null)
                .build();
    }

    @Test
    void givenValidParameters_whenCreatingInvoice_thenInvoiceIsCreated() {
        Invoice invoice = createDraftInvoice();

        assertThat(invoice).isNotNull();
        assertThat(invoice.getId()).isEqualTo(VALID_INVOICE_ID);
        assertThat(invoice.getPatientId()).isEqualTo(VALID_PATIENT_ID);
        assertThat(invoice.getAppointmentId()).isEqualTo(VALID_APPOINTMENT_ID);
        assertThat(invoice.getInvoiceNumber()).isEqualTo(VALID_INVOICE_NUMBER);
        assertThat(invoice.getIssueDate()).isEqualTo(TODAY);
        assertThat(invoice.getDueDate()).isEqualTo(TOMORROW);
        assertThat(invoice.getItems()).containsExactly(VALID_ITEM);
        assertThat(invoice.getSubtotal()).isEqualByComparingTo(VALID_SUBTOTAL);
        assertThat(invoice.getTax()).isEqualByComparingTo(VALID_TAX);
        assertThat(invoice.getTotal()).isEqualByComparingTo(VALID_TOTAL);
        assertThat(invoice.getStatus()).isEqualTo(InvoiceStatus.DRAFT);
        assertThat(invoice.getPaymentPlanId()).isNull();
    }

    @Test
    void givenNullInvoiceId_whenCreatingInvoice_thenThrowsException() {
        assertThatThrownBy(() -> Invoice.builder()
                .id(null)
                .patientId(VALID_PATIENT_ID)
                .invoiceNumber(VALID_INVOICE_NUMBER)
                .issueDate(TODAY)
                .dueDate(TOMORROW)
                .items(VALID_ITEMS)
                .subtotal(VALID_SUBTOTAL)
                .tax(VALID_TAX)
                .total(VALID_TOTAL)
                .status(InvoiceStatus.DRAFT)
                .build())
                .isInstanceOf(NullPointerException.class);
    }

    @Test
    void givenNullPatientId_whenCreatingInvoice_thenThrowsException() {
        assertThatThrownBy(() -> Invoice.builder()
                .id(VALID_INVOICE_ID)
                .patientId(null)
                .invoiceNumber(VALID_INVOICE_NUMBER)
                .issueDate(TODAY)
                .dueDate(TOMORROW)
                .items(VALID_ITEMS)
                .subtotal(VALID_SUBTOTAL)
                .tax(VALID_TAX)
                .total(VALID_TOTAL)
                .status(InvoiceStatus.DRAFT)
                .build())
                .isInstanceOf(NullPointerException.class)
                .hasMessageContaining("PatientId cannot be null");
    }

    @ParameterizedTest
    @NullAndEmptySource
    @ValueSource(strings = {" ", "  "})
    void givenInvalidInvoiceNumber_whenCreatingInvoice_thenThrowsDomainException(String invalidNumber) {
        assertThatThrownBy(() -> Invoice.builder()
                .id(VALID_INVOICE_ID)
                .patientId(VALID_PATIENT_ID)
                .invoiceNumber(invalidNumber)
                .issueDate(TODAY)
                .dueDate(TOMORROW)
                .items(VALID_ITEMS)
                .subtotal(VALID_SUBTOTAL)
                .tax(VALID_TAX)
                .total(VALID_TOTAL)
                .status(InvoiceStatus.DRAFT)
                .build())
                .isInstanceOf(DomainException.class)
                .hasMessageContaining("Invoice number cannot be null or blank");
    }

    @Test
    void givenNullIssueDate_whenCreatingInvoice_thenThrowsException() {
        assertThatThrownBy(() -> Invoice.builder()
                .id(VALID_INVOICE_ID)
                .patientId(VALID_PATIENT_ID)
                .invoiceNumber(VALID_INVOICE_NUMBER)
                .issueDate(null)
                .dueDate(TOMORROW)
                .items(VALID_ITEMS)
                .subtotal(VALID_SUBTOTAL)
                .tax(VALID_TAX)
                .total(VALID_TOTAL)
                .status(InvoiceStatus.DRAFT)
                .build())
                .isInstanceOf(NullPointerException.class)
                .hasMessageContaining("Issue date cannot be null");
    }

    @Test
    void givenNullDueDate_whenCreatingInvoice_thenThrowsException() {
        assertThatThrownBy(() -> Invoice.builder()
                .id(VALID_INVOICE_ID)
                .patientId(VALID_PATIENT_ID)
                .invoiceNumber(VALID_INVOICE_NUMBER)
                .issueDate(TODAY)
                .dueDate(null)
                .items(VALID_ITEMS)
                .subtotal(VALID_SUBTOTAL)
                .tax(VALID_TAX)
                .total(VALID_TOTAL)
                .status(InvoiceStatus.DRAFT)
                .build())
                .isInstanceOf(NullPointerException.class)
                .hasMessageContaining("Due date cannot be null");
    }

    @Test
    void givenNullItemsList_whenCreatingInvoice_thenThrowsDomainException() {
        assertThatThrownBy(() -> Invoice.builder()
                .id(VALID_INVOICE_ID)
                .patientId(VALID_PATIENT_ID)
                .invoiceNumber(VALID_INVOICE_NUMBER)
                .issueDate(TODAY)
                .dueDate(TOMORROW)
                .items(null)
                .subtotal(VALID_SUBTOTAL)
                .tax(VALID_TAX)
                .total(VALID_TOTAL)
                .status(InvoiceStatus.DRAFT)
                .build())
                .isInstanceOf(DomainException.class)
                .hasMessageContaining("Invoice must have at least one item");
    }

    @Test
    void givenEmptyItemsList_whenCreatingInvoice_thenThrowsDomainException() {
        assertThatThrownBy(() -> Invoice.builder()
                .id(VALID_INVOICE_ID)
                .patientId(VALID_PATIENT_ID)
                .invoiceNumber(VALID_INVOICE_NUMBER)
                .issueDate(TODAY)
                .dueDate(TOMORROW)
                .items(List.of())
                .subtotal(VALID_SUBTOTAL)
                .tax(VALID_TAX)
                .total(VALID_TOTAL)
                .status(InvoiceStatus.DRAFT)
                .build())
                .isInstanceOf(DomainException.class)
                .hasMessageContaining("Invoice must have at least one item");
    }

    @Test
    void givenNullSubtotal_whenCreatingInvoice_thenThrowsDomainException() {
        assertThatThrownBy(() -> Invoice.builder()
                .id(VALID_INVOICE_ID)
                .patientId(VALID_PATIENT_ID)
                .invoiceNumber(VALID_INVOICE_NUMBER)
                .issueDate(TODAY)
                .dueDate(TOMORROW)
                .items(VALID_ITEMS)
                .subtotal(null)
                .tax(VALID_TAX)
                .total(VALID_TOTAL)
                .status(InvoiceStatus.DRAFT)
                .build())
                .isInstanceOf(DomainException.class)
                .hasMessageContaining("Subtotal cannot be null");
    }

    @Test
    void givenNegativeSubtotal_whenCreatingInvoice_thenThrowsDomainException() {
        assertThatThrownBy(() -> Invoice.builder()
                .id(VALID_INVOICE_ID)
                .patientId(VALID_PATIENT_ID)
                .invoiceNumber(VALID_INVOICE_NUMBER)
                .issueDate(TODAY)
                .dueDate(TOMORROW)
                .items(VALID_ITEMS)
                .subtotal(new BigDecimal("-10.00"))
                .tax(VALID_TAX)
                .total(VALID_TOTAL)
                .status(InvoiceStatus.DRAFT)
                .build())
                .isInstanceOf(DomainException.class)
                .hasMessageContaining("Subtotal cannot be negative");
    }

    @Test
    void givenNullTax_whenCreatingInvoice_thenThrowsDomainException() {
        assertThatThrownBy(() -> Invoice.builder()
                .id(VALID_INVOICE_ID)
                .patientId(VALID_PATIENT_ID)
                .invoiceNumber(VALID_INVOICE_NUMBER)
                .issueDate(TODAY)
                .dueDate(TOMORROW)
                .items(VALID_ITEMS)
                .subtotal(VALID_SUBTOTAL)
                .tax(null)
                .total(VALID_TOTAL)
                .status(InvoiceStatus.DRAFT)
                .build())
                .isInstanceOf(DomainException.class)
                .hasMessageContaining("Tax cannot be null");
    }

    @Test
    void givenNegativeTax_whenCreatingInvoice_thenThrowsDomainException() {
        assertThatThrownBy(() -> Invoice.builder()
                .id(VALID_INVOICE_ID)
                .patientId(VALID_PATIENT_ID)
                .invoiceNumber(VALID_INVOICE_NUMBER)
                .issueDate(TODAY)
                .dueDate(TOMORROW)
                .items(VALID_ITEMS)
                .subtotal(VALID_SUBTOTAL)
                .tax(new BigDecimal("-5.00"))
                .total(VALID_TOTAL)
                .status(InvoiceStatus.DRAFT)
                .build())
                .isInstanceOf(DomainException.class)
                .hasMessageContaining("Tax cannot be negative");
    }

    @Test
    void givenNullTotal_whenCreatingInvoice_thenThrowsDomainException() {
        assertThatThrownBy(() -> Invoice.builder()
                .id(VALID_INVOICE_ID)
                .patientId(VALID_PATIENT_ID)
                .invoiceNumber(VALID_INVOICE_NUMBER)
                .issueDate(TODAY)
                .dueDate(TOMORROW)
                .items(VALID_ITEMS)
                .subtotal(VALID_SUBTOTAL)
                .tax(VALID_TAX)
                .total(null)
                .status(InvoiceStatus.DRAFT)
                .build())
                .isInstanceOf(DomainException.class)
                .hasMessageContaining("Total cannot be null");
    }

    @Test
    void givenNegativeTotal_whenCreatingInvoice_thenThrowsDomainException() {
        assertThatThrownBy(() -> Invoice.builder()
                .id(VALID_INVOICE_ID)
                .patientId(VALID_PATIENT_ID)
                .invoiceNumber(VALID_INVOICE_NUMBER)
                .issueDate(TODAY)
                .dueDate(TOMORROW)
                .items(VALID_ITEMS)
                .subtotal(VALID_SUBTOTAL)
                .tax(VALID_TAX)
                .total(new BigDecimal("-165.00"))
                .status(InvoiceStatus.DRAFT)
                .build())
                .isInstanceOf(DomainException.class)
                .hasMessageContaining("Total cannot be negative");
    }

    @Test
    void givenNullStatus_whenCreatingInvoice_thenThrowsException() {
        assertThatThrownBy(() -> Invoice.builder()
                .id(VALID_INVOICE_ID)
                .patientId(VALID_PATIENT_ID)
                .invoiceNumber(VALID_INVOICE_NUMBER)
                .issueDate(TODAY)
                .dueDate(TOMORROW)
                .items(VALID_ITEMS)
                .subtotal(VALID_SUBTOTAL)
                .tax(VALID_TAX)
                .total(VALID_TOTAL)
                .status(null)
                .build())
                .isInstanceOf(NullPointerException.class)
                .hasMessageContaining("Status cannot be null");
    }

    @Test
    void givenSubtotalNotMatchingSumOfItems_whenCreatingInvoice_thenThrowsDomainException() {
        BigDecimal incorrectSubtotal = new BigDecimal("200.00");

        assertThatThrownBy(() -> Invoice.builder()
                .id(VALID_INVOICE_ID)
                .patientId(VALID_PATIENT_ID)
                .invoiceNumber(VALID_INVOICE_NUMBER)
                .issueDate(TODAY)
                .dueDate(TOMORROW)
                .items(VALID_ITEMS)
                .subtotal(incorrectSubtotal)
                .tax(VALID_TAX)
                .total(VALID_TOTAL)
                .status(InvoiceStatus.DRAFT)
                .build())
                .isInstanceOf(DomainException.class)
                .hasMessageContaining("Invoice subtotal must equal sum of item amounts");
    }

    @Test
    void givenTotalNotMatchingSubtotalPlusTax_whenCreatingInvoice_thenThrowsDomainException() {
        BigDecimal incorrectTotal = new BigDecimal("200.00");

        assertThatThrownBy(() -> Invoice.builder()
                .id(VALID_INVOICE_ID)
                .patientId(VALID_PATIENT_ID)
                .invoiceNumber(VALID_INVOICE_NUMBER)
                .issueDate(TODAY)
                .dueDate(TOMORROW)
                .items(VALID_ITEMS)
                .subtotal(VALID_SUBTOTAL)
                .tax(VALID_TAX)
                .total(incorrectTotal)
                .status(InvoiceStatus.DRAFT)
                .build())
                .isInstanceOf(DomainException.class)
                .hasMessageContaining("Invoice total must equal subtotal + tax");
    }

    @Test
    void givenDraftInvoice_whenIssuing_thenStatusBecomesIssued() {
        Invoice invoice = createDraftInvoice();

        invoice.issue();

        assertThat(invoice.getStatus()).isEqualTo(InvoiceStatus.ISSUED);
    }

    @Test
    void givenNonDraftInvoice_whenIssuing_thenThrowsDomainException() {
        Invoice issuedInvoice = createIssuedInvoice();

        assertThatThrownBy(issuedInvoice::issue)
                .isInstanceOf(DomainException.class)
                .hasMessageContaining("Only DRAFT invoices can be issued");
    }

    @Test
    void givenDraftInvoiceWithDueDateBeforeIssueDate_whenIssuing_thenThrowsDomainException() {
        Invoice invoice = Invoice.builder()
                .id(VALID_INVOICE_ID)
                .patientId(VALID_PATIENT_ID)
                .invoiceNumber(VALID_INVOICE_NUMBER)
                .issueDate(TODAY)
                .dueDate(YESTERDAY)
                .items(VALID_ITEMS)
                .subtotal(VALID_SUBTOTAL)
                .tax(VALID_TAX)
                .total(VALID_TOTAL)
                .status(InvoiceStatus.DRAFT)
                .build();

        assertThatThrownBy(invoice::issue