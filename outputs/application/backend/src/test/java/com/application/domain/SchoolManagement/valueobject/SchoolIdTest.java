package com.application.domain.SchoolManagement.valueobject;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.junit.jupiter.params.provider.ValueSource;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class SchoolIdTest {

    @Test
    void givenValidUuidString_whenCreatingSchoolId_thenObjectIsCreated() {
        String validUuid = UUID.randomUUID().toString();
        SchoolId schoolId = new SchoolId(validUuid);
        assertNotNull(schoolId);
        assertEquals(validUuid, schoolId.value());
    }

    @Test
    void givenNoArguments_whenCreatingSchoolId_thenObjectIsCreatedWithNewUuid() {
        SchoolId schoolId = new SchoolId();
        assertNotNull(schoolId);
        assertNotNull(schoolId.value());
        assertDoesNotThrow(() -> UUID.fromString(schoolId.value()));
    }

    @ParameterizedTest
    @NullAndEmptySource
    @ValueSource(strings = {" ", "invalid-uuid", "12345"})
    void givenInvalidUuidString_whenCreatingSchoolId_thenThrowsDomainException(String invalidUuid) {
        DomainException exception = assertThrows(DomainException.class, () -> new SchoolId(invalidUuid));
        assertTrue(exception.getMessage().contains("SchoolId"));
    }

    @Test
    void givenTwoSchoolIdsWithSameUuid_whenComparingEquality_thenTheyAreEqual() {
        String uuid = UUID.randomUUID().toString();
        SchoolId id1 = new SchoolId(uuid);
        SchoolId id2 = new SchoolId(uuid);
        assertEquals(id1, id2);
        assertEquals(id1.hashCode(), id2.hashCode());
    }

    @Test
    void givenTwoSchoolIdsWithDifferentUuid_whenComparingEquality_thenTheyAreNotEqual() {
        SchoolId id1 = new SchoolId();
        SchoolId id2 = new SchoolId();
        assertNotEquals(id1, id2);
    }

    @Test
    void givenSchoolId_whenCallingValueObjectInterfaceMethod_thenReturnsCorrectValue() {
        String uuid = UUID.randomUUID().toString();
        SchoolId schoolId = new SchoolId(uuid);
        assertEquals(uuid, schoolId.value());
    }

    @Test
    void givenSchoolId_whenCheckingIfImplementsValueObject_thenIsTrue() {
        SchoolId schoolId = new SchoolId();
        assertTrue(schoolId instanceof ValueObject);
    }
}