package com.application;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class InvoiceItemEntityTest {

    @Test
    void testEntityCreationAndGetters() {
        UUID id = UUID.randomUUID();
        UUID monthlyInvoiceId = UUID.randomUUID();
        UUID billingConceptId = UUID.randomUUID();
        String description = "Monthly Tuition";
        Integer quantity = 1;
        BigDecimal unitPrice = new BigDecimal("500.00");
        BigDecimal subtotal = new BigDecimal("500.00");

        InvoiceItemEntity entity = new InvoiceItemEntity();
        entity.setId(id);
        entity.setMonthlyInvoiceId(monthlyInvoiceId);
        entity.setBillingConceptId(billingConceptId);
        entity.setDescription(description);
        entity.setQuantity(quantity);
        entity.setUnitPrice(unitPrice);
        entity.setSubtotal(subtotal);

        assertEquals(id, entity.getId());
        assertEquals(monthlyInvoiceId, entity.getMonthlyInvoiceId());
        assertEquals(billingConceptId, entity.getBillingConceptId());
        assertEquals(description, entity.getDescription());
        assertEquals(quantity, entity.getQuantity());
        assertEquals(unitPrice, entity.getUnitPrice());
        assertEquals(subtotal, entity.getSubtotal());
    }

    @Test
    void testEqualsAndHashCode() {
        UUID id = UUID.randomUUID();
        UUID monthlyInvoiceId = UUID.randomUUID();
        UUID billingConceptId = UUID.randomUUID();

        InvoiceItemEntity entity1 = new InvoiceItemEntity();
        entity1.setId(id);
        entity1.setMonthlyInvoiceId(monthlyInvoiceId);
        entity1.setBillingConceptId(billingConceptId);
        entity1.setDescription("Item 1");
        entity1.setQuantity(1);
        entity1.setUnitPrice(BigDecimal.TEN);
        entity1.setSubtotal(BigDecimal.TEN);

        InvoiceItemEntity entity2 = new InvoiceItemEntity();
        entity2.setId(id);
        entity2.setMonthlyInvoiceId(monthlyInvoiceId);
        entity2.setBillingConceptId(billingConceptId);
        entity2.setDescription("Item 2");
        entity2.setQuantity(2);
        entity2.setUnitPrice(BigDecimal.ONE);
        entity2.setSubtotal(BigDecimal.valueOf(2));

        InvoiceItemEntity entity3 = new InvoiceItemEntity();
        entity3.setId(UUID.randomUUID());
        entity3.setMonthlyInvoiceId(monthlyInvoiceId);
        entity3.setBillingConceptId(billingConceptId);
        entity3.setDescription("Item 1");
        entity3.setQuantity(1);
        entity3.setUnitPrice(BigDecimal.TEN);
        entity3.setSubtotal(BigDecimal.TEN);

        assertEquals(entity1, entity2);
        assertEquals(entity1.hashCode(), entity2.hashCode());
        assertNotEquals(entity1, entity3);
        assertNotEquals(entity1.hashCode(), entity3.hashCode());
    }

    @Test
    void testToString() {
        UUID id = UUID.randomUUID();
        InvoiceItemEntity entity = new InvoiceItemEntity();
        entity.setId(id);
        entity.setDescription("Test Item");

        String toStringResult = entity.toString();
        assertNotNull(toStringResult);
        assertTrue(toStringResult.contains(id.toString()));
        assertTrue(toStringResult.contains("Test Item"));
    }

    @Test
    void testNoArgsConstructor() {
        InvoiceItemEntity entity = new InvoiceItemEntity();
        assertNotNull(entity);
        assertNull(entity.getId());
        assertNull(entity.getDescription());
    }

    @Test
    void testAllArgsConstructor() {
        UUID id = UUID.randomUUID();
        UUID monthlyInvoiceId = UUID.randomUUID();
        UUID billingConceptId = UUID.randomUUID();
        String description = "Annual Fee";
        Integer quantity = 1;
        BigDecimal unitPrice = new BigDecimal("300.00");
        BigDecimal subtotal = new BigDecimal("300.00");

        InvoiceItemEntity entity = new InvoiceItemEntity(id, monthlyInvoiceId, billingConceptId, description, quantity, unitPrice, subtotal);

        assertEquals(id, entity.getId());
        assertEquals(monthlyInvoiceId, entity.getMonthlyInvoiceId());
        assertEquals(billingConceptId, entity.getBillingConceptId());
        assertEquals(description, entity.getDescription());
        assertEquals(quantity, entity.getQuantity());
        assertEquals(unitPrice, entity.getUnitPrice());
        assertEquals(subtotal, entity.getSubtotal());
    }

    @Test
    void testBuilder() {
        UUID id = UUID.randomUUID();
        UUID monthlyInvoiceId = UUID.randomUUID();
        UUID billingConceptId = UUID.randomUUID();
        String description = "Builder Item";
        Integer quantity = 5;
        BigDecimal unitPrice = new BigDecimal("20.00");
        BigDecimal subtotal = new BigDecimal("100.00");

        InvoiceItemEntity entity = InvoiceItemEntity.builder()
                .id(id)
                .monthlyInvoiceId(monthlyInvoiceId)
                .billingConceptId(billingConceptId)
                .description(description)
                .quantity(quantity)
                .unitPrice(unitPrice)
                .subtotal(subtotal)
                .build();

        assertEquals(id, entity.getId());
        assertEquals(monthlyInvoiceId, entity.getMonthlyInvoiceId());
        assertEquals(billingConceptId, entity.getBillingConceptId());
        assertEquals(description, entity.getDescription());
        assertEquals(quantity, entity.getQuantity());
        assertEquals(unitPrice, entity.getUnitPrice());
        assertEquals(subtotal, entity.getSubtotal());
    }
}