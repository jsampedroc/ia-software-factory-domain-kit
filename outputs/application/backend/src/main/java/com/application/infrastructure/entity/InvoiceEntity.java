package com.application.infrastructure.entity;

import com.application.domain.model.Invoice;
import com.application.domain.valueobject.InvoiceId;
import com.application.domain.valueobject.InvoiceStatus;
import com.application.domain.valueobject.PaymentMethod;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

import static lombok.AccessLevel.PROTECTED;

@Entity
@Table(name = "invoices")
@Getter
@NoArgsConstructor(access = PROTECTED)
public class InvoiceEntity {

    @Id
    private UUID id;

    @Column(name = "invoice_number", nullable = false, unique = true)
    private String invoiceNumber;

    @Column(name = "issue_date", nullable = false)
    private LocalDate issueDate;

    @Column(name = "due_date", nullable = false)
    private LocalDate dueDate;

    @Column(name = "subtotal", nullable = false, precision = 10, scale = 2)
    private BigDecimal subtotal;

    @Column(name = "taxes", nullable = false, precision = 10, scale = 2)
    private BigDecimal taxes;

    @Column(name = "total", nullable = false, precision = 10, scale = 2)
    private BigDecimal total;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private InvoiceStatus status;

    @Enumerated(EnumType.STRING)
    @Column(name = "payment_method")
    private PaymentMethod paymentMethod;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "patient_id", nullable = false)
    private PatientEntity patient;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "appointment_id")
    private AppointmentEntity appointment;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "treatment_plan_id")
    private TreatmentPlanEntity treatmentPlan;

    public static InvoiceEntity fromDomain(Invoice invoice) {
        InvoiceEntity entity = new InvoiceEntity();
        entity.id = invoice.getId().getValue();
        entity.invoiceNumber = invoice.getInvoiceNumber();
        entity.issueDate = invoice.getIssueDate();
        entity.dueDate = invoice.getDueDate();
        entity.subtotal = invoice.getSubtotal();
        entity.taxes = invoice.getTaxes();
        entity.total = invoice.getTotal();
        entity.status = invoice.getStatus();
        entity.paymentMethod = invoice.getPaymentMethod();
        // Las relaciones se deben establecer desde fuera, no desde el dominio puro
        return entity;
    }

    public Invoice toDomain() {
        // Solo convertir los IDs de las relaciones, no las entidades completas para evitar ciclos
        return new Invoice(
                new InvoiceId(this.id),
                this.invoiceNumber,
                this.issueDate,
                this.dueDate,
                this.subtotal,
                this.taxes,
                this.total,
                this.status,
                this.paymentMethod,
                this.patient != null ? this.patient.toDomainReference() : null,
                this.appointment != null ? this.appointment.toDomainReference() : null,
                this.treatmentPlan != null ? this.treatmentPlan.toDomainReference() : null
        );
    }

    // Métodos para establecer relaciones desde los adaptadores
    public void setPatient(PatientEntity patient) {
        this.patient = patient;
    }

    public void setAppointment(AppointmentEntity appointment) {
        this.appointment = appointment;
    }

    public void setTreatmentPlan(TreatmentPlanEntity treatmentPlan) {
        this.treatmentPlan = treatmentPlan;
    }
}