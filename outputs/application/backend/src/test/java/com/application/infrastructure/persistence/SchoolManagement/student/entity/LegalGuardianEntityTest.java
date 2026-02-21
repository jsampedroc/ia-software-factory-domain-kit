package com.application;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class LegalGuardianEntityTest {

    @Test
    void givenValidData_whenCreatingEntity_thenEntityIsCreated() {
        Long id = 1L;
        String firstName = "John";
        String lastName = "Doe";
        String email = "john.doe@example.com";
        String primaryPhone = "123456789";
        String secondaryPhone = "987654321";
        String address = "123 Main St";
        LocalDateTime createdAt = LocalDateTime.now();
        LocalDateTime updatedAt = LocalDateTime.now();

        LegalGuardianEntity entity = new LegalGuardianEntity();
        entity.setId(id);
        entity.setFirstName(firstName);
        entity.setLastName(lastName);
        entity.setEmail(email);
        entity.setPrimaryPhone(primaryPhone);
        entity.setSecondaryPhone(secondaryPhone);
        entity.setAddress(address);
        entity.setCreatedAt(createdAt);
        entity.setUpdatedAt(updatedAt);

        assertEquals(id, entity.getId());
        assertEquals(firstName, entity.getFirstName());
        assertEquals(lastName, entity.getLastName());
        assertEquals(email, entity.getEmail());
        assertEquals(primaryPhone, entity.getPrimaryPhone());
        assertEquals(secondaryPhone, entity.getSecondaryPhone());
        assertEquals(address, entity.getAddress());
        assertEquals(createdAt, entity.getCreatedAt());
        assertEquals(updatedAt, entity.getUpdatedAt());
    }

    @Test
    void givenTwoEqualEntities_whenComparing_thenTheyAreEqual() {
        LegalGuardianEntity entity1 = new LegalGuardianEntity();
        entity1.setId(1L);
        entity1.setEmail("test@example.com");

        LegalGuardianEntity entity2 = new LegalGuardianEntity();
        entity2.setId(1L);
        entity2.setEmail("test@example.com");

        assertEquals(entity1, entity2);
        assertEquals(entity1.hashCode(), entity2.hashCode());
    }

    @Test
    void givenTwoDifferentEntities_whenComparing_thenTheyAreNotEqual() {
        LegalGuardianEntity entity1 = new LegalGuardianEntity();
        entity1.setId(1L);

        LegalGuardianEntity entity2 = new LegalGuardianEntity();
        entity2.setId(2L);

        assertNotEquals(entity1, entity2);
    }

    @Test
    void givenEntityWithNullId_whenComparing_thenNotEqual() {
        LegalGuardianEntity entity1 = new LegalGuardianEntity();
        entity1.setId(null);

        LegalGuardianEntity entity2 = new LegalGuardianEntity();
        entity2.setId(1L);

        assertNotEquals(entity1, entity2);
    }

    @Test
    void givenEntity_whenCallingToString_thenStringIsNotEmpty() {
        LegalGuardianEntity entity = new LegalGuardianEntity();
        entity.setId(1L);
        entity.setFirstName("Jane");
        entity.setLastName("Smith");

        String toStringResult = entity.toString();
        assertNotNull(toStringResult);
        assertFalse(toStringResult.isEmpty());
    }

    @Test
    void givenEntity_whenUsingBuilder_thenEntityIsCreated() {
        Long id = 5L;
        String email = "builder@test.com";

        LegalGuardianEntity entity = LegalGuardianEntity.builder()
                .id(id)
                .email(email)
                .build();

        assertEquals(id, entity.getId());
        assertEquals(email, entity.getEmail());
        assertNull(entity.getFirstName());
    }

    @Test
    void givenEntity_whenUsingNoArgsConstructor_thenEntityIsCreated() {
        LegalGuardianEntity entity = new LegalGuardianEntity();
        assertNotNull(entity);
        assertNull(entity.getId());
    }

    @Test
    void givenEntity_whenUsingAllArgsConstructor_thenEntityIsCreated() {
        Long id = 10L;
        String firstName = "Alice";
        String lastName = "Johnson";
        String email = "alice@example.com";
        String primaryPhone = "555-1234";
        String secondaryPhone = "555-5678";
        String address = "456 Oak Ave";
        LocalDateTime createdAt = LocalDateTime.of(2023, 1, 1, 10, 0);
        LocalDateTime updatedAt = LocalDateTime.of(2023, 1, 2, 10, 0);

        LegalGuardianEntity entity = new LegalGuardianEntity(id, firstName, lastName, email, primaryPhone, secondaryPhone, address, createdAt, updatedAt);

        assertEquals(id, entity.getId());
        assertEquals(firstName, entity.getFirstName());
        assertEquals(lastName, entity.getLastName());
        assertEquals(email, entity.getEmail());
        assertEquals(primaryPhone, entity.getPrimaryPhone());
        assertEquals(secondaryPhone, entity.getSecondaryPhone());
        assertEquals(address, entity.getAddress());
        assertEquals(createdAt, entity.getCreatedAt());
        assertEquals(updatedAt, entity.getUpdatedAt());
    }
}