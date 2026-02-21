package com.application.infrastructure.persistence.Billing.invoicing.entity;

import com.application.domain.Billing.invoicing.domain.BillingConcept;
import com.application.domain.Billing.valueobject.BillingConceptId;
import com.application.domain.shared.valueobject.Money;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;

@ExtendWith(MockitoExtension.class)
class BillingConceptEntityTest {

    @Test
    void testToDomain_ShouldMapEntityToDomain() {
        BillingConceptEntity entity = new BillingConceptEntity();
        UUID id = UUID.randomUUID();
        entity.setId(id);
        entity.setName("Monthly Tuition");
        entity.setDescription("Monthly school fee");
        entity.setDefaultAmount(new BigDecimal("500.00"));
        entity.setCurrency("USD");
        entity.setPeriodicity("MONTHLY");
        entity.setApplicableFromGrade(1);
        entity.setApplicableToGrade(12);
        entity.setActive(true);
        entity.setCreatedAt(LocalDate.now());
        entity.setUpdatedAt(LocalDate.now());

        BillingConcept domain = entity.toDomain();

        assertNotNull(domain);
        assertEquals(BillingConceptId.of(id), domain.getId());
        assertEquals("Monthly Tuition", domain.getName());
        assertEquals("Monthly school fee", domain.getDescription());
        assertEquals(new Money(new BigDecimal("500.00"), "USD"), domain.getDefaultAmount());
        assertEquals("MONTHLY", domain.getPeriodicity());
        assertEquals(1, domain.getApplicableFromGrade());
        assertEquals(12, domain.getApplicableToGrade());
        assertTrue(domain.isActive());
    }

    @Test
    void testFromDomain_ShouldMapDomainToEntity() {
        BillingConceptId conceptId = BillingConceptId.generate();
        Money defaultAmount = new Money(new BigDecimal("300.00"), "USD");
        BillingConcept domain = BillingConcept.builder()
                .id(conceptId)
                .name("Registration Fee")
                .description("One-time registration")
                .defaultAmount(defaultAmount)
                .periodicity("ONE_TIME")
                .applicableFromGrade(1)
                .applicableToGrade(12)
                .active(true)
                .build();

        BillingConceptEntity entity = BillingConceptEntity.fromDomain(domain);

        assertNotNull(entity);
        assertEquals(conceptId.getValue(), entity.getId());
        assertEquals("Registration Fee", entity.getName());
        assertEquals("One-time registration", entity.getDescription());
        assertEquals(new BigDecimal("300.00"), entity.getDefaultAmount());
        assertEquals("USD", entity.getCurrency());
        assertEquals("ONE_TIME", entity.getPeriodicity());
        assertEquals(1, entity.getApplicableFromGrade());
        assertEquals(12, entity.getApplicableToGrade());
        assertTrue(entity.isActive());
        assertNotNull(entity.getCreatedAt());
        assertNotNull(entity.getUpdatedAt());
    }

    @Test
    void testFromDomain_WithNullDomain_ShouldReturnNull() {
        BillingConceptEntity entity = BillingConceptEntity.fromDomain(null);
        assertNull(entity);
    }

    @Test
    void testToDomain_WithNullValues_ShouldHandleGracefully() {
        BillingConceptEntity entity = new BillingConceptEntity();
        UUID id = UUID.randomUUID();
        entity.setId(id);
        entity.setName("Test");
        entity.setDefaultAmount(BigDecimal.ZERO);
        entity.setCurrency("USD");
        entity.setPeriodicity("MONTHLY");
        entity.setActive(true);

        BillingConcept domain = entity.toDomain();

        assertNotNull(domain);
        assertEquals(BillingConceptId.of(id), domain.getId());
        assertEquals("Test", domain.getName());
        assertNull(domain.getDescription());
        assertEquals(new Money(BigDecimal.ZERO, "USD"), domain.getDefaultAmount());
        assertEquals("MONTHLY", domain.getPeriodicity());
        assertNull(domain.getApplicableFromGrade());
        assertNull(domain.getApplicableToGrade());
        assertTrue(domain.isActive());
    }

    @Test
    void testEqualsAndHashCode() {
        UUID id1 = UUID.randomUUID();
        BillingConceptEntity entity1 = new BillingConceptEntity();
        entity1.setId(id1);
        entity1.setName("Concept A");

        BillingConceptEntity entity2 = new BillingConceptEntity();
        entity2.setId(id1);
        entity2.setName("Concept A");

        BillingConceptEntity entity3 = new BillingConceptEntity();
        entity3.setId(UUID.randomUUID());
        entity3.setName("Concept B");

        assertEquals(entity1, entity2);
        assertEquals(entity1.hashCode(), entity2.hashCode());
        assertNotEquals(entity1, entity3);
        assertNotEquals(entity1.hashCode(), entity3.hashCode());
    }

    @Test
    void testSettersAndGetters() {
        BillingConceptEntity entity = new BillingConceptEntity();
        UUID id = UUID.randomUUID();
        LocalDate now = LocalDate.now();

        entity.setId(id);
        entity.setName("Test Concept");
        entity.setDescription("Test Description");
        entity.setDefaultAmount(new BigDecimal("100.00"));
        entity.setCurrency("EUR");
        entity.setPeriodicity("ANNUAL");
        entity.setApplicableFromGrade(5);
        entity.setApplicableToGrade(10);
        entity.setActive(false);
        entity.setCreatedAt(now);
        entity.setUpdatedAt(now);

        assertEquals(id, entity.getId());
        assertEquals("Test Concept", entity.getName());
        assertEquals("Test Description", entity.getDescription());
        assertEquals(new BigDecimal("100.00"), entity.getDefaultAmount());
        assertEquals("EUR", entity.getCurrency());
        assertEquals("ANNUAL", entity.getPeriodicity());
        assertEquals(5, entity.getApplicableFromGrade());
        assertEquals(10, entity.getApplicableToGrade());
        assertFalse(entity.isActive());
        assertEquals(now, entity.getCreatedAt());
        assertEquals(now, entity.getUpdatedAt());
    }
}