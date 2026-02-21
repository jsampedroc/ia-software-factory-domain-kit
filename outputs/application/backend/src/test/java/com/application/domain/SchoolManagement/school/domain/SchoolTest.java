package com.application.domain.SchoolManagement.school.domain;

import com.application.domain.SchoolManagement.valueobject.SchoolId;
import com.application.domain.exception.DomainException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class SchoolTest {

    @Test
    void createSchool_WithValidData_ShouldCreateSchool() {
        SchoolId id = new SchoolId();
        String name = "Colegio Ejemplo";
        String address = "Calle Falsa 123";
        String phoneNumber = "+123456789";

        School school = School.create(id, name, address, phoneNumber);

        assertNotNull(school);
        assertEquals(id, school.getId());
        assertEquals(name, school.getName());
        assertEquals(address, school.getAddress());
        assertEquals(phoneNumber, school.getPhoneNumber());
        assertTrue(school.isActive());
    }

    @Test
    void createSchool_WithNullName_ShouldThrowDomainException() {
        SchoolId id = new SchoolId();
        String address = "Calle Falsa 123";
        String phoneNumber = "+123456789";

        DomainException exception = assertThrows(DomainException.class,
                () -> School.create(id, null, address, phoneNumber));
        assertTrue(exception.getMessage().contains("School name is required"));
    }

    @Test
    void createSchool_WithEmptyName_ShouldThrowDomainException() {
        SchoolId id = new SchoolId();
        String address = "Calle Falsa 123";
        String phoneNumber = "+123456789";

        DomainException exception = assertThrows(DomainException.class,
                () -> School.create(id, "", address, phoneNumber));
        assertTrue(exception.getMessage().contains("School name is required"));
    }

    @Test
    void createSchool_WithBlankName_ShouldThrowDomainException() {
        SchoolId id = new SchoolId();
        String address = "Calle Falsa 123";
        String phoneNumber = "+123456789";

        DomainException exception = assertThrows(DomainException.class,
                () -> School.create(id, "   ", address, phoneNumber));
        assertTrue(exception.getMessage().contains("School name is required"));
    }

    @Test
    void createSchool_WithNullAddress_ShouldThrowDomainException() {
        SchoolId id = new SchoolId();
        String name = "Colegio Ejemplo";
        String phoneNumber = "+123456789";

        DomainException exception = assertThrows(DomainException.class,
                () -> School.create(id, name, null, phoneNumber));
        assertTrue(exception.getMessage().contains("School address is required"));
    }

    @Test
    void createSchool_WithEmptyAddress_ShouldThrowDomainException() {
        SchoolId id = new SchoolId();
        String name = "Colegio Ejemplo";
        String phoneNumber = "+123456789";

        DomainException exception = assertThrows(DomainException.class,
                () -> School.create(id, name, "", phoneNumber));
        assertTrue(exception.getMessage().contains("School address is required"));
    }

    @Test
    void createSchool_WithNullPhoneNumber_ShouldThrowDomainException() {
        SchoolId id = new SchoolId();
        String name = "Colegio Ejemplo";
        String address = "Calle Falsa 123";

        DomainException exception = assertThrows(DomainException.class,
                () -> School.create(id, name, address, null));
        assertTrue(exception.getMessage().contains("School phone number is required"));
    }

    @Test
    void createSchool_WithEmptyPhoneNumber_ShouldThrowDomainException() {
        SchoolId id = new SchoolId();
        String name = "Colegio Ejemplo";
        String address = "Calle Falsa 123";

        DomainException exception = assertThrows(DomainException.class,
                () -> School.create(id, name, address, ""));
        assertTrue(exception.getMessage().contains("School phone number is required"));
    }

    @Test
    void deactivate_WhenSchoolIsActive_ShouldSetActiveToFalse() {
        SchoolId id = new SchoolId();
        School school = School.create(id, "Colegio", "Dir", "123");
        assertTrue(school.isActive());

        school.deactivate();

        assertFalse(school.isActive());
    }

    @Test
    void deactivate_WhenSchoolIsAlreadyInactive_ShouldRemainInactive() {
        SchoolId id = new SchoolId();
        School school = School.create(id, "Colegio", "Dir", "123");
        school.deactivate();
        assertFalse(school.isActive());

        school.deactivate();

        assertFalse(school.isActive());
    }

    @Test
    void activate_WhenSchoolIsInactive_ShouldSetActiveToTrue() {
        SchoolId id = new SchoolId();
        School school = School.create(id, "Colegio", "Dir", "123");
        school.deactivate();
        assertFalse(school.isActive());

        school.activate();

        assertTrue(school.isActive());
    }

    @Test
    void activate_WhenSchoolIsAlreadyActive_ShouldRemainActive() {
        SchoolId id = new SchoolId();
        School school = School.create(id, "Colegio", "Dir", "123");
        assertTrue(school.isActive());

        school.activate();

        assertTrue(school.isActive());
    }

    @Test
    void updateDetails_WithValidData_ShouldUpdateFields() {
        SchoolId id = new SchoolId();
        School school = School.create(id, "Old Name", "Old Address", "Old Phone");
        String newName = "New Name";
        String newAddress = "New Address";
        String newPhone = "New Phone";

        school.updateDetails(newName, newAddress, newPhone);

        assertEquals(newName, school.getName());
        assertEquals(newAddress, school.getAddress());
        assertEquals(newPhone, school.getPhoneNumber());
    }

    @Test
    void updateDetails_WithNullName_ShouldThrowDomainException() {
        SchoolId id = new SchoolId();
        School school = School.create(id, "Old Name", "Old Address", "Old Phone");

        DomainException exception = assertThrows(DomainException.class,
                () -> school.updateDetails(null, "New Address", "New Phone"));
        assertTrue(exception.getMessage().contains("School name is required"));
    }

    @Test
    void updateDetails_WithNullAddress_ShouldThrowDomainException() {
        SchoolId id = new SchoolId();
        School school = School.create(id, "Old Name", "Old Address", "Old Phone");

        DomainException exception = assertThrows(DomainException.class,
                () -> school.updateDetails("New Name", null, "New Phone"));
        assertTrue(exception.getMessage().contains("School address is required"));
    }

    @Test
    void updateDetails_WithNullPhoneNumber_ShouldThrowDomainException() {
        SchoolId id = new SchoolId();
        School school = School.create(id, "Old Name", "Old Address", "Old Phone");

        DomainException exception = assertThrows(DomainException.class,
                () -> school.updateDetails("New Name", "New Address", null));
        assertTrue(exception.getMessage().contains("School phone number is required"));
    }
}