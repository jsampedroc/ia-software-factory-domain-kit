package com.application.infrastructure.persistence.SchoolManagement.school.entity;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class SchoolEntityTest {

    @Test
    void testEntityCreationAndGetters() {
        UUID id = UUID.randomUUID();
        LocalDateTime now = LocalDateTime.now();
        String name = "Test School";
        String address = "123 Main St";
        String phoneNumber = "+1234567890";

        SchoolEntity entity = new SchoolEntity();
        entity.setId(id);
        entity.setName(name);
        entity.setAddress(address);
        entity.setPhoneNumber(phoneNumber);
        entity.setActive(true);
        entity.setCreatedAt(now);
        entity.setUpdatedAt(now);

        assertEquals(id, entity.getId());
        assertEquals(name, entity.getName());
        assertEquals(address, entity.getAddress());
        assertEquals(phoneNumber, entity.getPhoneNumber());
        assertTrue(entity.isActive());
        assertEquals(now, entity.getCreatedAt());
        assertEquals(now, entity.getUpdatedAt());
    }

    @Test
    void testEqualsAndHashCode_SameId() {
        UUID id = UUID.randomUUID();
        SchoolEntity entity1 = new SchoolEntity();
        entity1.setId(id);
        entity1.setName("School A");

        SchoolEntity entity2 = new SchoolEntity();
        entity2.setId(id);
        entity2.setName("School B");

        assertEquals(entity1, entity2);
        assertEquals(entity1.hashCode(), entity2.hashCode());
    }

    @Test
    void testEqualsAndHashCode_DifferentId() {
        SchoolEntity entity1 = new SchoolEntity();
        entity1.setId(UUID.randomUUID());

        SchoolEntity entity2 = new SchoolEntity();
        entity2.setId(UUID.randomUUID());

        assertNotEquals(entity1, entity2);
        assertNotEquals(entity1.hashCode(), entity2.hashCode());
    }

    @Test
    void testEquals_WithNull() {
        SchoolEntity entity = new SchoolEntity();
        entity.setId(UUID.randomUUID());

        assertNotEquals(null, entity);
    }

    @Test
    void testEquals_DifferentClass() {
        SchoolEntity entity = new SchoolEntity();
        entity.setId(UUID.randomUUID());

        assertNotEquals("Not an entity", entity);
    }

    @Test
    void testToString() {
        UUID id = UUID.randomUUID();
        SchoolEntity entity = new SchoolEntity();
        entity.setId(id);
        entity.setName("Test School");

        String toString = entity.toString();
        assertTrue(toString.contains(id.toString()));
        assertTrue(toString.contains("Test School"));
    }

    @Test
    void testBuilder() {
        UUID id = UUID.randomUUID();
        LocalDateTime now = LocalDateTime.now();

        SchoolEntity entity = SchoolEntity.builder()
                .id(id)
                .name("Builder School")
                .address("456 Builder Ave")
                .phoneNumber("+0987654321")
                .active(false)
                .createdAt(now)
                .updatedAt(now)
                .build();

        assertEquals(id, entity.getId());
        assertEquals("Builder School", entity.getName());
        assertEquals("456 Builder Ave", entity.getAddress());
        assertEquals("+0987654321", entity.getPhoneNumber());
        assertFalse(entity.isActive());
        assertEquals(now, entity.getCreatedAt());
        assertEquals(now, entity.getUpdatedAt());
    }

    @Test
    void testNoArgsConstructor() {
        SchoolEntity entity = new SchoolEntity();
        assertNotNull(entity);
        assertNull(entity.getId());
        assertNull(entity.getName());
        assertNull(entity.getAddress());
        assertNull(entity.getPhoneNumber());
        assertNull(entity.getCreatedAt());
        assertNull(entity.getUpdatedAt());
    }

    @Test
    void testAllArgsConstructor() {
        UUID id = UUID.randomUUID();
        LocalDateTime now = LocalDateTime.now();

        SchoolEntity entity = new SchoolEntity(
                id,
                "AllArgs School",
                "789 AllArgs Blvd",
                "+1122334455",
                true,
                now,
                now
        );

        assertEquals(id, entity.getId());
        assertEquals("AllArgs School", entity.getName());
        assertEquals("789 AllArgs Blvd", entity.getAddress());
        assertEquals("+1122334455", entity.getPhoneNumber());
        assertTrue(entity.isActive());
        assertEquals(now, entity.getCreatedAt());
        assertEquals(now, entity.getUpdatedAt());
    }
}