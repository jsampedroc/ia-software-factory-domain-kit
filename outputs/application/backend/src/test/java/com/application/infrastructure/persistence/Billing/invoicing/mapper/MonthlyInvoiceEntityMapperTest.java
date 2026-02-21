package com.application;

import com.application.domain.Billing.invoicing.domain.InvoiceItem;
import com.application.domain.Billing.invoicing.domain.MonthlyInvoice;
import com.application.domain.Billing.invoicing.domain.repository.MonthlyInvoiceRepository;
import com.application.domain.Billing.invoicing.valueobject.InvoiceItemId;
import com.application.domain.Billing.invoicing.valueobject.MonthlyInvoiceId;
import com.application.domain.Billing.valueobject.BillingConceptId;
import com.application.domain.SchoolManagement.valueobject.StudentId;
import com.application.domain.shared.valueobject.Money;
import com.application.infrastructure.persistence.Billing.invoicing.entity.BillingConceptEntity;
import com.application.infrastructure.persistence.Billing.invoicing.entity.InvoiceItemEntity;
import com.application.infrastructure.persistence.Billing.invoicing.entity.MonthlyInvoiceEntity;
import com.application.infrastructure.persistence.Billing.invoicing.mapper.MonthlyInvoiceEntityMapper;
import com.application.infrastructure.persistence.SchoolManagement.student.entity.StudentEntity;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
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
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class MonthlyInvoiceEntityMapperTest {

    @Mock
    private MonthlyInvoiceRepository monthlyInvoiceRepository;

    @InjectMocks
    private MonthlyInvoiceEntityMapper mapper;

    private MonthlyInvoiceId monthlyInvoiceId;
    private StudentId studentId;
    private YearMonth billingPeriod;
    private MonthlyInvoice monthlyInvoice;
    private MonthlyInvoiceEntity monthlyInvoiceEntity;
    private InvoiceItem invoiceItem;
    private InvoiceItemEntity invoiceItemEntity;

    @BeforeEach
    void setUp() {
        monthlyInvoiceId = new MonthlyInvoiceId(UUID.randomUUID());
        studentId = new StudentId(UUID.randomUUID());
        billingPeriod = YearMonth.of(2024, 1);

        Money totalAmount = new Money(new BigDecimal("1500.00"), "USD");

        monthlyInvoice = MonthlyInvoice.builder()
                .id(monthlyInvoiceId)
                .studentId(studentId)
                .billingPeriod(billingPeriod)
                .issueDate(LocalDate.of(2024, 1, 1))
                .dueDate(LocalDate.of(2024, 1, 15))
                .totalAmount(totalAmount)
                .status(MonthlyInvoice.InvoiceStatus.PENDING)
                .schoolId(UUID.randomUUID())
                .build();

        monthlyInvoiceEntity = new MonthlyInvoiceEntity();
        monthlyInvoiceEntity.setId(monthlyInvoiceId.getValue());
        monthlyInvoiceEntity.setStudent(new StudentEntity());
        monthlyInvoiceEntity.getStudent().setId(studentId.getValue());
        monthlyInvoiceEntity.setBillingPeriod(billingPeriod);
        monthlyInvoiceEntity.setIssueDate(LocalDate.of(2024, 1, 1));
        monthlyInvoiceEntity.setDueDate(LocalDate.of(2024, 1, 15));
        monthlyInvoiceEntity.setTotalAmount(new BigDecimal("1500.00"));
        monthlyInvoiceEntity.setCurrency("USD");
        monthlyInvoiceEntity.setStatus("PENDING");
        monthlyInvoiceEntity.setSchoolId(UUID.randomUUID());

        InvoiceItemId itemId = new InvoiceItemId(UUID.randomUUID());
        BillingConceptId conceptId = new BillingConceptId(UUID.randomUUID());
        Money unitPrice = new Money(new BigDecimal("1500.00"), "USD");

        invoiceItem = InvoiceItem.builder()
                .id(itemId)
                .monthlyInvoiceId(monthlyInvoiceId)
                .billingConceptId(conceptId)
                .description("Monthly Tuition")
                .quantity(1)
                .unitPrice(unitPrice)
                .subtotal(unitPrice)
                .build();

        invoiceItemEntity = new InvoiceItemEntity();
        invoiceItemEntity.setId(itemId.getValue());
        invoiceItemEntity.setMonthlyInvoice(monthlyInvoiceEntity);
        invoiceItemEntity.setBillingConcept(new BillingConceptEntity());
        invoiceItemEntity.getBillingConcept().setId(conceptId.getValue());
        invoiceItemEntity.setDescription("Monthly Tuition");
        invoiceItemEntity.setQuantity(1);
        invoiceItemEntity.setUnitPrice(new BigDecimal("1500.00"));
        invoiceItemEntity.setCurrency("USD");
        invoiceItemEntity.setSubtotal(new BigDecimal("1500.00"));

        monthlyInvoiceEntity.setInvoiceItems(List.of(invoiceItemEntity));
    }

    @Test
    void toDomain_ShouldMapEntityToDomain() {
        when(monthlyInvoiceRepository.findInvoiceItemsByInvoiceId(monthlyInvoiceId)).thenReturn(List.of(invoiceItem));

        MonthlyInvoice result = mapper.toDomain(monthlyInvoiceEntity);

        assertNotNull(result);
        assertEquals(monthlyInvoiceId, result.getId());
        assertEquals(studentId, result.getStudentId());
        assertEquals(billingPeriod, result.getBillingPeriod());
        assertEquals(new Money(new BigDecimal("1500.00"), "USD"), result.getTotalAmount());
        assertEquals(MonthlyInvoice.InvoiceStatus.PENDING, result.getStatus());
        assertNotNull(result.getInvoiceItems());
        assertEquals(1, result.getInvoiceItems().size());
        assertEquals(invoiceItem.getId(), result.getInvoiceItems().get(0).getId());
    }

    @Test
    void toDomain_ShouldReturnNullWhenEntityIsNull() {
        MonthlyInvoice result = mapper.toDomain(null);
        assertNull(result);
    }

    @Test
    void toEntity_ShouldMapDomainToEntity() {
        MonthlyInvoiceEntity result = mapper.toEntity(monthlyInvoice);

        assertNotNull(result);
        assertEquals(monthlyInvoiceId.getValue(), result.getId());
        assertNotNull(result.getStudent());
        assertEquals(studentId.getValue(), result.getStudent().getId());
        assertEquals(billingPeriod, result.getBillingPeriod());
        assertEquals(new BigDecimal("1500.00"), result.getTotalAmount());
        assertEquals("USD", result.getCurrency());
        assertEquals("PENDING", result.getStatus());
        assertNull(result.getInvoiceItems());
    }

    @Test
    void toEntity_ShouldReturnNullWhenDomainIsNull() {
        MonthlyInvoiceEntity result = mapper.toEntity(null);
        assertNull(result);
    }

    @Test
    void toEntity_ShouldMapInvoiceItemsWhenDomainHasItems() {
        monthlyInvoice = MonthlyInvoice.builder()
                .id(monthlyInvoiceId)
                .studentId(studentId)
                .billingPeriod(billingPeriod)
                .issueDate(LocalDate.of(2024, 1, 1))
                .dueDate(LocalDate.of(2024, 1, 15))
                .totalAmount(new Money(new BigDecimal("1500.00"), "USD"))
                .status(MonthlyInvoice.InvoiceStatus.PENDING)
                .schoolId(UUID.randomUUID())
                .invoiceItems(List.of(invoiceItem))
                .build();

        MonthlyInvoiceEntity result = mapper.toEntity(monthlyInvoice);

        assertNotNull(result);
        assertNotNull(result.getInvoiceItems());
        assertEquals(1, result.getInvoiceItems().size());
        InvoiceItemEntity mappedItem = result.getInvoiceItems().get(0);
        assertEquals(invoiceItem.getId().getValue(), mappedItem.getId());
        assertEquals(invoiceItem.getDescription(), mappedItem.getDescription());
        assertEquals(invoiceItem.getQuantity(), mappedItem.getQuantity());
        assertEquals(invoiceItem.getUnitPrice().amount(), mappedItem.getUnitPrice());
        assertEquals(invoiceItem.getUnitPrice().currency(), mappedItem.getCurrency());
        assertEquals(invoiceItem.getSubtotal().amount(), mappedItem.getSubtotal());
    }
}