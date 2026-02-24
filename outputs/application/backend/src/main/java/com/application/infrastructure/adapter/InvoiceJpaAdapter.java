package com.application.infrastructure.adapter;

import com.application.domain.model.Invoice;
import com.application.domain.port.InvoiceRepositoryPort;
import com.application.domain.valueobject.InvoiceId;
import com.application.domain.valueobject.InvoiceStatus;
import com.application.domain.valueobject.PatientId;
import com.application.infrastructure.entity.InvoiceEntity;
import com.application.infrastructure.repository.InvoiceJpaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class InvoiceJpaAdapter implements InvoiceRepositoryPort {

    private final InvoiceJpaRepository invoiceJpaRepository;
    private final InvoiceEntityMapper mapper;

    @Override
    public Invoice save(Invoice invoice) {
        InvoiceEntity entity = mapper.toEntity(invoice);
        InvoiceEntity savedEntity = invoiceJpaRepository.save(entity);
        return mapper.toDomain(savedEntity);
    }

    @Override
    public Optional<Invoice> findById(InvoiceId invoiceId) {
        return invoiceJpaRepository.findById(invoiceId.getValue())
                .map(mapper::toDomain);
    }

    @Override
    public List<Invoice> findAll() {
        return invoiceJpaRepository.findAll()
                .stream()
                .map(mapper::toDomain)
                .collect(Collectors.toList());
    }

    @Override
    public void deleteById(InvoiceId invoiceId) {
        invoiceJpaRepository.deleteById(invoiceId.getValue());
    }

    @Override
    public boolean existsById(InvoiceId invoiceId) {
        return invoiceJpaRepository.existsById(invoiceId.getValue());
    }

    @Override
    public List<Invoice> findByPatientId(PatientId patientId) {
        return invoiceJpaRepository.findByPatientId(patientId.getValue())
                .stream()
                .map(mapper::toDomain)
                .collect(Collectors.toList());
    }

    @Override
    public List<Invoice> findByStatus(InvoiceStatus status) {
        return invoiceJpaRepository.findByEstado(status)
                .stream()
                .map(mapper::toDomain)
                .collect(Collectors.toList());
    }

    @Override
    public Optional<Invoice> findByInvoiceNumber(String invoiceNumber) {
        return invoiceJpaRepository.findByNumeroFactura(invoiceNumber)
                .map(mapper::toDomain);
    }

    @Component
    @RequiredArgsConstructor
    static class InvoiceEntityMapper {

        private final PatientJpaAdapter.PatientEntityMapper patientMapper;
        private final DentistJpaAdapter.DentistEntityMapper dentistMapper;
        private final AppointmentJpaAdapter.AppointmentEntityMapper appointmentMapper;

        InvoiceEntity toEntity(Invoice domain) {
            if (domain == null) {
                return null;
            }

            InvoiceEntity entity = new InvoiceEntity();
            entity.setId(domain.getId().getValue());
            entity.setNumeroFactura(domain.getNumeroFactura());
            entity.setFechaEmision(domain.getFechaEmision());
            entity.setFechaVencimiento(domain.getFechaVencimiento());
            entity.setSubtotal(domain.getSubtotal());
            entity.setImpuestos(domain.getImpuestos());
            entity.setTotal(domain.getTotal());
            entity.setEstado(domain.getEstado());
            entity.setMetodoPago(domain.getMetodoPago());
            entity.setPatient(patientMapper.toEntity(domain.getPatient()));
            entity.setDentist(dentistMapper.toEntity(domain.getDentist()));
            entity.setAppointment(appointmentMapper.toEntity(domain.getAppointment()));

            return entity;
        }

        Invoice toDomain(InvoiceEntity entity) {
            if (entity == null) {
                return null;
            }

            return new Invoice(
                    new InvoiceId(entity.getId()),
                    entity.getNumeroFactura(),
                    entity.getFechaEmision(),
                    entity.getFechaVencimiento(),
                    entity.getSubtotal(),
                    entity.getImpuestos(),
                    entity.getTotal(),
                    entity.getEstado(),
                    entity.getMetodoPago(),
                    patientMapper.toDomain(entity.getPatient()),
                    dentistMapper.toDomain(entity.getDentist()),
                    appointmentMapper.toDomain(entity.getAppointment())
            );
        }
    }
}