package com.application;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class StudentEntityTest {

    @Test
    void testEntityCreationAndGetters() {
        UUID id = UUID.randomUUID();
        UUID legalGuardianId = UUID.randomUUID();
        UUID currentClassroomId = UUID.randomUUID();
        String firstName = "John";
        String lastName = "Doe";
        LocalDate dateOfBirth = LocalDate.of(2015, 5, 10);
        String identificationNumber = "ID123456";
        LocalDate enrollmentDate = LocalDate.of(2023, 1, 15);
        boolean active = true;

        StudentEntity entity = new StudentEntity();
        entity.setId(id);
        entity.setLegalGuardianId(legalGuardianId);
        entity.setFirstName(firstName);
        entity.setLastName(lastName);
        entity.setDateOfBirth(dateOfBirth);
        entity.setIdentificationNumber(identificationNumber);
        entity.setEnrollmentDate(enrollmentDate);
        entity.setActive(active);
        entity.setCurrentClassroomId(currentClassroomId);

        assertEquals(id, entity.getId());
        assertEquals(legalGuardianId, entity.getLegalGuardianId());
        assertEquals(firstName, entity.getFirstName());
        assertEquals(lastName, entity.getLastName());
        assertEquals(dateOfBirth, entity.getDateOfBirth());
        assertEquals(identificationNumber, entity.getIdentificationNumber());
        assertEquals(enrollmentDate, entity.getEnrollmentDate());
        assertEquals(active, entity.isActive());
        assertEquals(currentClassroomId, entity.getCurrentClassroomId());
    }

    @Test
    void testEqualsAndHashCode() {
        UUID id1 = UUID.randomUUID();
        UUID id2 = UUID.randomUUID();

        StudentEntity entity1 = new StudentEntity();
        entity1.setId(id1);
        entity1.setIdentificationNumber("ID1");

        StudentEntity entity2 = new StudentEntity();
        entity2.setId(id1);
        entity2.setIdentificationNumber("ID1");

        StudentEntity entity3 = new StudentEntity();
        entity3.setId(id2);
        entity3.setIdentificationNumber("ID2");

        assertEquals(entity1, entity2);
        assertNotEquals(entity1, entity3);
        assertEquals(entity1.hashCode(), entity2.hashCode());
        assertNotEquals(entity1.hashCode(), entity3.hashCode());
    }

    @Test
    void testToString() {
        UUID id = UUID.randomUUID();
        StudentEntity entity = new StudentEntity();
        entity.setId(id);
        entity.setFirstName("Test");
        entity.setLastName("Student");

        String toString = entity.toString();
        assertNotNull(toString);
        assertTrue(toString.contains(id.toString()));
        assertTrue(toString.contains("Test"));
        assertTrue(toString.contains("Student"));
    }

    @Test
    void testBuilder() {
        UUID id = UUID.randomUUID();
        UUID legalGuardianId = UUID.randomUUID();
        UUID currentClassroomId = UUID.randomUUID();
        LocalDate dateOfBirth = LocalDate.of(2016, 3, 20);
        LocalDate enrollmentDate = LocalDate.of(2024, 2, 1);

        StudentEntity entity = StudentEntity.builder()
                .id(id)
                .legalGuardianId(legalGuardianId)
                .firstName("Alice")
                .lastName("Smith")
                .dateOfBirth(dateOfBirth)
                .identificationNumber("ID789")
                .enrollmentDate(enrollmentDate)
                .active(false)
                .currentClassroomId(currentClassroomId)
                .build();

        assertEquals(id, entity.getId());
        assertEquals(legalGuardianId, entity.getLegalGuardianId());
        assertEquals("Alice", entity.getFirstName());
        assertEquals("Smith", entity.getLastName());
        assertEquals(dateOfBirth, entity.getDateOfBirth());
        assertEquals("ID789", entity.getIdentificationNumber());
        assertEquals(enrollmentDate, entity.getEnrollmentDate());
        assertFalse(entity.isActive());
        assertEquals(currentClassroomId, entity.getCurrentClassroomId());
    }

    @Test
    void testNoArgsConstructor() {
        StudentEntity entity = new StudentEntity();
        assertNotNull(entity);
        assertNull(entity.getId());
        assertNull(entity.getFirstName());
        assertNull(entity.getLastName());
        assertNull(entity.getDateOfBirth());
        assertNull(entity.getIdentificationNumber());
        assertNull(entity.getEnrollmentDate());
        assertNull(entity.getLegalGuardianId());
        assertNull(entity.getCurrentClassroomId());
        assertFalse(entity.isActive());
    }

    @Test
    void testAllArgsConstructor() {
        UUID id = UUID.randomUUID();
        UUID legalGuardianId = UUID.randomUUID();
        UUID currentClassroomId = UUID.randomUUID();
        LocalDate dateOfBirth = LocalDate.of(2014, 7, 8);
        LocalDate enrollmentDate = LocalDate.of(2022, 9, 10);

        StudentEntity entity = new StudentEntity(
                id,
                legalGuardianId,
                "Bob",
                "Brown",
                dateOfBirth,
                "ID999",
                enrollmentDate,
                true,
                currentClassroomId
        );

        assertEquals(id, entity.getId());
        assertEquals(legalGuardianId, entity.getLegalGuardianId());
        assertEquals("Bob", entity.getFirstName());
        assertEquals("Brown", entity.getLastName());
        assertEquals(dateOfBirth, entity.getDateOfBirth());
        assertEquals("ID999", entity.getIdentificationNumber());
        assertEquals(enrollmentDate, entity.getEnrollmentDate());
        assertTrue(entity.isActive());
        assertEquals(currentClassroomId, entity.getCurrentClassroomId());
    }
}