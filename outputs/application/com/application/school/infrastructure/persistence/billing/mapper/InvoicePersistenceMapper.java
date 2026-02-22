package com.application.school.infrastructure.persistence.billing.mapper;

import com.application.school.domain.billing.model.Invoice;
import com.application.school.domain.billing.model.InvoiceId;
import com.application.school.domain.billing.model.InvoiceItem;
import com.application.school.domain.billing.model.InvoiceItemId;
import com.application.school.domain.billing.model.Payment;
import com.application.school.domain.billing.model.PaymentId;
import com.application.school.domain.shared.valueobject.Money;
import com.application.school.infrastructure.persistence.billing.entity.InvoiceEntity;
import com.application.school.infrastructure.persistence.billing.entity.InvoiceItemEntity;
import com.application.school.infrastructure.persistence.billing.entity.PaymentEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.mapstruct.factory.Mappers;

import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

@Mapper(componentModel = "spring")
public interface InvoicePersistenceMapper {

    InvoicePersistenceMapper INSTANCE = Mappers.getMapper(InvoicePersistenceMapper.class);

    @Mapping(target = "id", source = "invoiceId.value")
    @Mapping(target = "studentId", source = "student.studentId.value")
    @Mapping(target = "totalAmount", source = "totalAmount.amount")
    @Mapping(target = "currency", source = "totalAmount.currency")
    @Mapping(target = "invoiceItems", source = "items")
    @Mapping(target = "payments", source = "payments")
    InvoiceEntity toEntity(Invoice domain);

    @Mapping(target = "invoiceId", source = "id", qualifiedByName = "toInvoiceId")
    @Mapping(target = "student", ignore = true)
    @Mapping(target = "totalAmount", expression = "java(toMoney(entity.getTotalAmount(), entity.getCurrency()))")
    @Mapping(target = "items", source = "invoiceItems")
    @Mapping(target = "payments", source = "payments")
    Invoice toDomain(InvoiceEntity entity);

    @Mapping(target = "id", source = "invoiceItemId.value")
    @Mapping(target = "unitPrice", source = "unitPrice.amount")
    @Mapping(target = "currency", source = "unitPrice.currency")
    @Mapping(target = "subtotal", source = "subtotal.amount")
    InvoiceItemEntity toEntity(InvoiceItem domain);

    @Mapping(target = "invoiceItemId", source = "id", qualifiedByName = "toInvoiceItemId")
    @Mapping(target = "unitPrice", expression = "java(toMoney(entity.getUnitPrice(), entity.getCurrency()))")
    @Mapping(target = "subtotal", expression = "java(toMoney(entity.getSubtotal(), entity.getCurrency()))")
    @Mapping(target = "invoice", ignore = true)
    InvoiceItem toDomain(InvoiceItemEntity entity);

    @Mapping(target = "id", source = "paymentId.value")
    @Mapping(target = "amount", source = "amount.amount")
    @Mapping(target = "currency", source = "amount.currency")
    PaymentEntity toEntity(Payment domain);

    @Mapping(target = "paymentId", source = "id", qualifiedByName = "toPaymentId")
    @Mapping(target = "amount", expression = "java(toMoney(entity.getAmount(), entity.getCurrency()))")
    @Mapping(target = "invoice", ignore = true)
    Payment toDomain(PaymentEntity entity);

    default List<InvoiceItemEntity> invoiceItemSetToEntityList(Set<InvoiceItem> items) {
        if (items == null) {
            return null;
        }
        return items.stream()
                .map(this::toEntity)
                .collect(Collectors.toList());
    }

    default Set<InvoiceItem> invoiceItemEntityListToSet(List<InvoiceItemEntity> entities) {
        if (entities == null) {
            return null;
        }
        return entities.stream()
                .map(this::toDomain)
                .collect(Collectors.toSet());
    }

    default List<PaymentEntity> paymentSetToEntityList(Set<Payment> payments) {
        if (payments == null) {
            return null;
        }
        return payments.stream()
                .map(this::toEntity)
                .collect(Collectors.toList());
    }

    default Set<Payment> paymentEntityListToSet(List<PaymentEntity> entities) {
        if (entities == null) {
            return null;
        }
        return entities.stream()
                .map(this::toDomain)
                .collect(Collectors.toSet());
    }

    @Named("toInvoiceId")
    default InvoiceId toInvoiceId(UUID id) {
        return id != null ? new InvoiceId(id) : null;
    }

    @Named("toInvoiceItemId")
    default InvoiceItemId toInvoiceItemId(UUID id) {
        return id != null ? new InvoiceItemId(id) : null;
    }

    @Named("toPaymentId")
    default PaymentId toPaymentId(UUID id) {
        return id != null ? new PaymentId(id) : null;
    }

    default Money toMoney(Double amount, String currency) {
        if (amount == null || currency == null) {
            return null;
        }
        return new Money(amount, currency);
    }
}