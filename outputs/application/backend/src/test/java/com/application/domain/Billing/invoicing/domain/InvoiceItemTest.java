package com.application.domain.Billing.invoicing.domain;

import com.application.domain.Billing.invoicing.valueobject.InvoiceItemId;
import com.application.domain.Billing.invoicing.valueobject.BillingConceptId;
import com.application.domain.shared.valueobject.Money;
import com.application.domain.shared.Entity;
import com.application.domain.exception.DomainException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class InvoiceItemTest {

    @Test
    void createInvoiceItem_WithValidData_ShouldSucceed() {
        InvoiceItemId id = new InvoiceItemId(UUID.randomUUID());
        MonthlyInvoiceId invoiceId = new MonthlyInvoiceId(UUID.randomUUID());
        BillingConceptId conceptId = new BillingConceptId(UUID.randomUUID());
        String description = "Monthly Tuition";
        int quantity = 1;
        Money unitPrice = new Money(new BigDecimal("500.00"), "USD");

        InvoiceItem item = InvoiceItem.create(id, invoiceId, conceptId, description, quantity, unitPrice);

        assertNotNull(item);
        assertEquals(id, item.getId());
        assertEquals(invoiceId, item.getMonthlyInvoiceId());
        assertEquals(conceptId, item.getBillingConceptId());
        assertEquals(description, item.getDescription());
        assertEquals(quantity, item.getQuantity());
        assertEquals(unitPrice, item.getUnitPrice());
        assertEquals(new Money(new BigDecimal("500.00"), "USD"), item.getSubtotal());
    }

    @Test
    void createInvoiceItem_WithQuantityGreaterThanOne_ShouldCalculateCorrectSubtotal() {
        InvoiceItemId id = new InvoiceItemId(UUID.randomUUID());
        MonthlyInvoiceId invoiceId = new MonthlyInvoiceId(UUID.randomUUID());
        BillingConceptId conceptId = new BillingConceptId(UUID.randomUUID());
        String description = "Book Fee";
        int quantity = 3;
        Money unitPrice = new Money(new BigDecimal("25.50"), "USD");

        InvoiceItem item = InvoiceItem.create(id, invoiceId, conceptId, description, quantity, unitPrice);

        assertNotNull(item);
        assertEquals(quantity, item.getQuantity());
        assertEquals(new Money(new BigDecimal("76.50"), "USD"), item.getSubtotal());
    }

    @Test
    void createInvoiceItem_WithZeroQuantity_ShouldThrowDomainException() {
        InvoiceItemId id = new InvoiceItemId(UUID.randomUUID());
        MonthlyInvoiceId invoiceId = new MonthlyInvoiceId(UUID.randomUUID());
        BillingConceptId conceptId = new BillingConceptId(UUID.randomUUID());
        String description = "Invalid Item";
        int quantity = 0;
        Money unitPrice = new Money(new BigDecimal("10.00"), "USD");

        DomainException exception = assertThrows(DomainException.class, () -> {
            InvoiceItem.create(id, invoiceId, conceptId, description, quantity, unitPrice);
        });

        assertTrue(exception.getMessage().contains("Quantity must be greater than zero"));
    }

    @Test
    void createInvoiceItem_WithNegativeQuantity_ShouldThrowDomainException() {
        InvoiceItemId id = new InvoiceItemId(UUID.randomUUID());
        MonthlyInvoiceId invoiceId = new MonthlyInvoiceId(UUID.randomUUID());
        BillingConceptId conceptId = new BillingConceptId(UUID.randomUUID());
        String description = "Invalid Item";
        int quantity = -2;
        Money unitPrice = new Money(new BigDecimal("10.00"), "USD");

        DomainException exception = assertThrows(DomainException.class, () -> {
            InvoiceItem.create(id, invoiceId, conceptId, description, quantity, unitPrice);
        });

        assertTrue(exception.getMessage().contains("Quantity must be greater than zero"));
    }

    @Test
    void createInvoiceItem_WithNullUnitPrice_ShouldThrowDomainException() {
        InvoiceItemId id = new InvoiceItemId(UUID.randomUUID());
        MonthlyInvoiceId invoiceId = new MonthlyInvoiceId(UUID.randomUUID());
        BillingConceptId conceptId = new BillingConceptId(UUID.randomUUID());
        String description = "Invalid Item";
        int quantity = 1;

        DomainException exception = assertThrows(DomainException.class, () -> {
            InvoiceItem.create(id, invoiceId, conceptId, description, quantity, null);
        });

        assertTrue(exception.getMessage().contains("Unit price cannot be null"));
    }

    @Test
    void createInvoiceItem_WithNullDescription_ShouldThrowDomainException() {
        InvoiceItemId id = new InvoiceItemId(UUID.randomUUID());
        MonthlyInvoiceId invoiceId = new MonthlyInvoiceId(UUID.randomUUID());
        BillingConceptId conceptId = new BillingConceptId(UUID.randomUUID());
        int quantity = 1;
        Money unitPrice = new Money(new BigDecimal("10.00"), "USD");

        DomainException exception = assertThrows(DomainException.class, () -> {
            InvoiceItem.create(id, invoiceId, conceptId, null, quantity, unitPrice);
        });

        assertTrue(exception.getMessage().contains("Description cannot be null or empty"));
    }

    @Test
    void createInvoiceItem_WithEmptyDescription_ShouldThrowDomainException() {
        InvoiceItemId id = new InvoiceItemId(UUID.randomUUID());
        MonthlyInvoiceId invoiceId = new MonthlyInvoiceId(UUID.randomUUID());
        BillingConceptId conceptId = new BillingConceptId(UUID.randomUUID());
        String description = "";
        int quantity = 1;
        Money unitPrice = new Money(new BigDecimal("10.00"), "USD");

        DomainException exception = assertThrows(DomainException.class, () -> {
            InvoiceItem.create(id, invoiceId, conceptId, description, quantity, unitPrice);
        });

        assertTrue(exception.getMessage().contains("Description cannot be null or empty"));
    }

    @Test
    void createInvoiceItem_WithBlankDescription_ShouldThrowDomainException() {
        InvoiceItemId id = new InvoiceItemId(UUID.randomUUID());
        MonthlyInvoiceId invoiceId = new MonthlyInvoiceId(UUID.randomUUID());
        BillingConceptId conceptId = new BillingConceptId(UUID.randomUUID());
        String description = "   ";
        int quantity = 1;
        Money unitPrice = new Money(new BigDecimal("10.00"), "USD");

        DomainException exception = assertThrows(DomainException.class, () -> {
            InvoiceItem.create(id, invoiceId, conceptId, description, quantity, unitPrice);
        });

        assertTrue(exception.getMessage().contains("Description cannot be null or empty"));
    }

    @Test
    void invoiceItem_ShouldExtendEntity() {
        InvoiceItemId id = new InvoiceItemId(UUID.randomUUID());
        MonthlyInvoiceId invoiceId = new MonthlyInvoiceId(UUID.randomUUID());
        BillingConceptId conceptId = new BillingConceptId(UUID.randomUUID());
        String description = "Test Item";
        int quantity = 1;
        Money unitPrice = new Money(new BigDecimal("100.00"), "USD");

        InvoiceItem item = InvoiceItem.create(id, invoiceId, conceptId, description, quantity, unitPrice);

        assertTrue(item instanceof Entity);
    }

    @Test
    void equalsAndHashCode_ShouldWorkBasedOnId() {
        UUID uuid1 = UUID.randomUUID();
        InvoiceItemId id1 = new InvoiceItemId(uuid1);
        InvoiceItemId id2 = new InvoiceItemId(uuid1);
        InvoiceItemId id3 = new InvoiceItemId(UUID.randomUUID());

        MonthlyInvoiceId invoiceId = new MonthlyInvoiceId(UUID.randomUUID());
        BillingConceptId conceptId = new BillingConceptId(UUID.randomUUID());
        String description = "Test Item";
        int quantity = 1;
        Money unitPrice = new Money(new BigDecimal("100.00"), "USD");

        InvoiceItem item1 = InvoiceItem.create(id1, invoiceId, conceptId, description, quantity, unitPrice);
        InvoiceItem item2 = InvoiceItem.create(id2, invoiceId, conceptId, description, quantity, unitPrice);
        InvoiceItem item3 = InvoiceItem.create(id3, invoiceId, conceptId, description, quantity, unitPrice);

        assertEquals(item1, item2);
        assertNotEquals(item1, item3);
        assertEquals(item1.hashCode(), item2.hashCode());
        assertNotEquals(item1.hashCode(), item3.hashCode());
    }
}