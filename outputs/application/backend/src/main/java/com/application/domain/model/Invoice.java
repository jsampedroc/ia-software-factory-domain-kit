package com.application.domain.model;

import com.application.domain.model.base.AggregateRoot;
import com.application.domain.model.valueobject.InvoiceId;
import com.application.domain.model.valueobject.PatientId;
import com.application.domain.model.valueobject.PractitionerId;
import com.application.domain.model.valueobject.Money;
import com.application.domain.model.valueobject.InvoiceStatus;
import java.time.LocalDateTime;
import java.util.List;
import java.util.ArrayList;

public final class Invoice extends AggregateRoot<InvoiceId> {
    private final PatientId patientId;
    private final PractitionerId practitionerId;
    private final LocalDateTime issueDate;
    private final LocalDateTime dueDate;
    private InvoiceStatus status;
    private final List<InvoiceLine> lines;
    private Money totalAmount;

    public Invoice(InvoiceId id, PatientId patientId, PractitionerId practitionerId,
                   LocalDateTime issueDate, LocalDateTime dueDate, InvoiceStatus status,
                   List<InvoiceLine> lines) {
        super(id);
        this.patientId = patientId;
        this.practitionerId = practitionerId;
        this.issueDate = issueDate;
        this.dueDate = dueDate;
        this.status = status;
        this.lines = new ArrayList<>(lines);
        this.totalAmount = calculateTotal();
    }

    private Money calculateTotal() {
        Money total = Money.ZERO;
        for (InvoiceLine line : lines) {
            total = total.add(line.getLineTotal());
        }
        return total;
    }

    public void addLine(InvoiceLine line) {
        this.lines.add(line);
        this.totalAmount = this.totalAmount.add(line.getLineTotal());
    }

    public void markAsPaid() {
        this.status = InvoiceStatus.PAID;
    }

    public void markAsOverdue() {
        this.status = InvoiceStatus.OVERDUE;
    }

    public PatientId getPatientId() { return patientId; }
    public PractitionerId getPractitionerId() { return practitionerId; }
    public LocalDateTime getIssueDate() { return issueDate; }
    public LocalDateTime getDueDate() { return dueDate; }
    public InvoiceStatus getStatus() { return status; }
    public List<InvoiceLine> getLines() { return new ArrayList<>(lines); }
    public Money getTotalAmount() { return totalAmount; }
}