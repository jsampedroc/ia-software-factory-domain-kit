package com.application.domain.ports.in;

import com.application.domain.model.billing.Invoice;
import com.application.domain.model.billing.InvoiceId;
import com.application.domain.model.billing.Payment;
import com.application.domain.model.studentmanagement.StudentId;
import com.application.domain.valueobject.DateRange;

import java.math.BigDecimal;
import java.time.Month;
import java.time.Year;
import java.util.List;
import java.util.Optional;

public interface BillingService {
    Invoice generateMonthlyInvoice(StudentId studentId, Month month, Year year);
    Optional<Invoice> findInvoiceById(InvoiceId invoiceId);
    List<Invoice> findInvoicesByStudent(StudentId studentId);
    List<Invoice> findInvoicesByStatus(String status);
    List<Invoice> findOverdueInvoices();
    Invoice updateInvoiceStatus(InvoiceId invoiceId, String status);
    Invoice applyDiscount(InvoiceId invoiceId, BigDecimal discountAmount, String reason);
    Invoice applySurcharge(InvoiceId invoiceId, BigDecimal surchargeAmount, String reason);
    Payment registerPayment(InvoiceId invoiceId, BigDecimal amount, String paymentMethod, String referenceNumber, String receivedBy);
    List<Payment> getPaymentsForInvoice(InvoiceId invoiceId);
    BigDecimal calculateMonthlyRevenue(DateRange dateRange);
    void sendInvoiceReminder(InvoiceId invoiceId);
}