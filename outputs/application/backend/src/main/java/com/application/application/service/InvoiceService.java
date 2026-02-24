package com.application.application.service;

import com.application.domain.model.Invoice;
import com.application.domain.model.Patient;
import com.application.domain.port.InvoiceRepositoryPort;
import com.application.domain.port.PatientRepositoryPort;
import com.application.domain.valueobject.InvoiceId;
import com.application.domain.valueobject.InvoiceStatus;
import com.application.domain.valueobject.PatientId;
import com.application.domain.valueobject.PaymentMethod;
import com.application.domain.exception.DomainException;
import com.application.application.dto.InvoiceDTO;
import com.application.application.mapper.InvoiceMapper;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
public class InvoiceService {

    private final InvoiceRepositoryPort invoiceRepositoryPort;
    private final PatientRepositoryPort patientRepositoryPort;
    private final InvoiceMapper invoiceMapper;

    public InvoiceService(InvoiceRepositoryPort invoiceRepositoryPort,
                          PatientRepositoryPort patientRepositoryPort,
                          InvoiceMapper invoiceMapper) {
        this.invoiceRepositoryPort = invoiceRepositoryPort;
        this.patientRepositoryPort = patientRepositoryPort;
        this.invoiceMapper = invoiceMapper;
    }

    public List<InvoiceDTO> findAll() {
        List<Invoice> invoices = invoiceRepositoryPort.findAll();
        return invoiceMapper.toDtoList(invoices);
    }

    public Optional<InvoiceDTO> findById(UUID id) {
        InvoiceId invoiceId = new InvoiceId(id);
        Optional<Invoice> invoice = invoiceRepositoryPort.findById(invoiceId);
        return invoice.map(invoiceMapper::toDto);
    }

    public List<InvoiceDTO> findByPatientId(UUID patientId) {
        PatientId id = new PatientId(patientId);
        List<Invoice> invoices = invoiceRepositoryPort.findByPatientId(id);
        return invoiceMapper.toDtoList(invoices);
    }

    public List<InvoiceDTO> findByStatus(InvoiceStatus status) {
        List<Invoice> invoices = invoiceRepositoryPort.findByStatus(status);
        return invoiceMapper.toDtoList(invoices);
    }

    public List<InvoiceDTO> findOverdueInvoices() {
        List<Invoice> invoices = invoiceRepositoryPort.findOverdueInvoices(LocalDate.now());
        return invoiceMapper.toDtoList(invoices);
    }

    @Transactional
    public InvoiceDTO create(InvoiceDTO invoiceDTO) {
        validateInvoiceDTO(invoiceDTO);
        PatientId patientId = new PatientId(invoiceDTO.getPatientId());
        Patient patient = patientRepositoryPort.findById(patientId)
                .orElseThrow(() -> new DomainException("Patient not found with id: " + invoiceDTO.getPatientId()));

        validatePatientCanReceiveInvoice(patient);

        Invoice invoice = invoiceMapper.toDomain(invoiceDTO);
        invoice.setPatient(patient);

        validateInvoiceBusinessRules(invoice);

        Invoice savedInvoice = invoiceRepositoryPort.save(invoice);
        return invoiceMapper.toDto(savedInvoice);
    }

    @Transactional
    public InvoiceDTO update(UUID id, InvoiceDTO invoiceDTO) {
        InvoiceId invoiceId = new InvoiceId(id);
        Invoice existingInvoice = invoiceRepositoryPort.findById(invoiceId)
                .orElseThrow(() -> new DomainException("Invoice not found with id: " + id));

        validateInvoiceForUpdate(existingInvoice, invoiceDTO);

        invoiceMapper.updateDomainFromDto(invoiceDTO, existingInvoice);
        validateInvoiceBusinessRules(existingInvoice);

        Invoice updatedInvoice = invoiceRepositoryPort.save(existingInvoice);
        return invoiceMapper.toDto(updatedInvoice);
    }

    @Transactional
    public InvoiceDTO updateStatus(UUID id, InvoiceStatus newStatus) {
        InvoiceId invoiceId = new InvoiceId(id);
        Invoice invoice = invoiceRepositoryPort.findById(invoiceId)
                .orElseThrow(() -> new DomainException("Invoice not found with id: " + id));

        validateStatusTransition(invoice.getStatus(), newStatus);
        invoice.setStatus(newStatus);

        if (newStatus == InvoiceStatus.PAGADA) {
            invoice.setPaymentDate(LocalDate.now());
        }

        Invoice updatedInvoice = invoiceRepositoryPort.save(invoice);
        return invoiceMapper.toDto(updatedInvoice);
    }

    @Transactional
    public InvoiceDTO applyPayment(UUID id, BigDecimal amount, PaymentMethod paymentMethod) {
        InvoiceId invoiceId = new InvoiceId(id);
        Invoice invoice = invoiceRepositoryPort.findById(invoiceId)
                .orElseThrow(() -> new DomainException("Invoice not found with id: " + id));

        validatePayment(invoice, amount);

        invoice.applyPayment(amount, paymentMethod);
        Invoice updatedInvoice = invoiceRepositoryPort.save(invoice);
        return invoiceMapper.toDto(updatedInvoice);
    }

    @Transactional
    public void delete(UUID id) {
        InvoiceId invoiceId = new InvoiceId(id);
        Invoice invoice = invoiceRepositoryPort.findById(invoiceId)
                .orElseThrow(() -> new DomainException("Invoice not found with id: " + id));

        validateInvoiceForDeletion(invoice);
        invoiceRepositoryPort.delete(invoiceId);
    }

    private void validateInvoiceDTO(InvoiceDTO invoiceDTO) {
        if (invoiceDTO == null) {
            throw new DomainException("Invoice data cannot be null");
        }
        if (invoiceDTO.getPatientId() == null) {
            throw new DomainException("Patient ID is required");
        }
        if (invoiceDTO.getSubtotal() == null || invoiceDTO.getSubtotal().compareTo(BigDecimal.ZERO) <= 0) {
            throw new DomainException("Subtotal must be greater than zero");
        }
        if (invoiceDTO.getTotal() == null || invoiceDTO.getTotal().compareTo(BigDecimal.ZERO) <= 0) {
            throw new DomainException("Total must be greater than zero");
        }
        if (invoiceDTO.getIssueDate() == null) {
            throw new DomainException("Issue date is required");
        }
        if (invoiceDTO.getDueDate() == null) {
            throw new DomainException("Due date is required");
        }
        if (invoiceDTO.getIssueDate().isAfter(invoiceDTO.getDueDate())) {
            throw new DomainException("Issue date cannot be after due date");
        }
    }

    private void validatePatientCanReceiveInvoice(Patient patient) {
        if (!patient.isActive()) {
            throw new DomainException("Cannot create invoice for inactive patient");
        }

        List<Invoice> overdueInvoices = invoiceRepositoryPort.findOverdueInvoicesByPatientId(
                patient.getId(), LocalDate.now().minusDays(60));
        if (!overdueInvoices.isEmpty()) {
            throw new DomainException("Patient has invoices overdue for more than 60 days. Cannot create new invoice.");
        }
    }

    private void validateInvoiceBusinessRules(Invoice invoice) {
        if (invoice.getTotal().compareTo(invoice.getSubtotal().add(invoice.getTaxes())) != 0) {
            throw new DomainException("Total must equal subtotal plus taxes");
        }

        if (invoice.getDiscount() != null && invoice.getDiscount().compareTo(BigDecimal.ZERO) > 0) {
            BigDecimal discountPercentage = invoice.getDiscount()
                    .divide(invoice.getSubtotal(), 4, RoundingMode.HALF_UP)
                    .multiply(BigDecimal.valueOf(100));
            if (discountPercentage.compareTo(new BigDecimal("15")) > 0) {
                throw new DomainException("Discounts greater than 15% require clinic manager authorization");
            }
        }

        if (invoice.getDueDate().isBefore(invoice.getIssueDate())) {
            throw new DomainException("Due date cannot be before issue date");
        }
    }

    private void validateInvoiceForUpdate(Invoice existingInvoice, InvoiceDTO invoiceDTO) {
        if (existingInvoice.getStatus() == InvoiceStatus.PAGADA) {
            throw new DomainException("Cannot update a paid invoice");
        }
        if (existingInvoice.getStatus() == InvoiceStatus.CANCELADA) {
            throw new DomainException("Cannot update a cancelled invoice");
        }

        if (invoiceDTO.getStatus() != null && invoiceDTO.getStatus() != existingInvoice.getStatus()) {
            validateStatusTransition(existingInvoice.getStatus(), invoiceDTO.getStatus());
        }
    }

    private void validateStatusTransition(InvoiceStatus currentStatus, InvoiceStatus newStatus) {
        if (currentStatus == InvoiceStatus.PAGADA && newStatus != InvoiceStatus.PAGADA) {
            throw new DomainException("Cannot change status from PAID to another status");
        }
        if (currentStatus == InvoiceStatus.CANCELADA && newStatus != InvoiceStatus.CANCELADA) {
            throw new DomainException("Cannot change status from CANCELLED to another status");
        }
        if (currentStatus == InvoiceStatus.VENCIDA && newStatus == InvoiceStatus.PENDIENTE) {
            throw new DomainException("Cannot change status from OVERDUE to PENDING");
        }
    }

    private void validatePayment(Invoice invoice, BigDecimal amount) {
        if (invoice.getStatus() == InvoiceStatus.PAGADA) {
            throw new DomainException("Invoice is already paid");
        }
        if (invoice.getStatus() == InvoiceStatus.CANCELADA) {
            throw new DomainException("Cannot apply payment to cancelled invoice");
        }

        BigDecimal remainingAmount = invoice.getTotal().subtract(invoice.getPaidAmount());
        if (amount.compareTo(remainingAmount) > 0) {
            throw new DomainException("Payment amount exceeds remaining balance");
        }
        if (amount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new DomainException("Payment amount must be greater than zero");
        }
    }

    private void validateInvoiceForDeletion(Invoice invoice) {
        if (invoice.getStatus() == InvoiceStatus.PAGADA) {
            throw new DomainException("Cannot delete a paid invoice");
        }
        if (invoice.getStatus() == InvoiceStatus.VENCIDA) {
            throw new DomainException("Cannot delete an overdue invoice");
        }
    }
}