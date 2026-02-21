package com.application.domain.SchoolManagement.student.domain;

import com.application.domain.shared.valueobject.PersonName;
import com.application.domain.SchoolManagement.valueobject.LegalGuardianId;
import com.application.domain.exception.DomainException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class LegalGuardianTest {

    @Test
    void createLegalGuardian_WithValidData_ShouldSucceed() {
        LegalGuardianId id = new LegalGuardianId("guardian-123");
        PersonName name = new PersonName("John", "Doe");
        String email = "john.doe@example.com";
        String primaryPhone = "+1234567890";
        String secondaryPhone = "+0987654321";
        String address = "123 Main St";

        LegalGuardian guardian = new LegalGuardian(id, name, email, primaryPhone, secondaryPhone, address);

        assertNotNull(guardian);
        assertEquals(id, guardian.getId());
        assertEquals(name, guardian.getName());
        assertEquals(email, guardian.getEmail());
        assertEquals(primaryPhone, guardian.getPrimaryPhone());
        assertEquals(secondaryPhone, guardian.getSecondaryPhone());
        assertEquals(address, guardian.getAddress());
    }

    @Test
    void createLegalGuardian_WithNullId_ShouldThrowException() {
        PersonName name = new PersonName("John", "Doe");
        String email = "john.doe@example.com";
        String primaryPhone = "+1234567890";

        DomainException exception = assertThrows(DomainException.class, () ->
                new LegalGuardian(null, name, email, primaryPhone, null, null)
        );
        assertTrue(exception.getMessage().contains("LegalGuardianId"));
    }

    @Test
    void createLegalGuardian_WithNullName_ShouldThrowException() {
        LegalGuardianId id = new LegalGuardianId("guardian-123");
        String email = "john.doe@example.com";
        String primaryPhone = "+1234567890";

        DomainException exception = assertThrows(DomainException.class, () ->
                new LegalGuardian(id, null, email, primaryPhone, null, null)
        );
        assertTrue(exception.getMessage().contains("PersonName"));
    }

    @Test
    void createLegalGuardian_WithNullEmail_ShouldThrowException() {
        LegalGuardianId id = new LegalGuardianId("guardian-123");
        PersonName name = new PersonName("John", "Doe");
        String primaryPhone = "+1234567890";

        DomainException exception = assertThrows(DomainException.class, () ->
                new LegalGuardian(id, name, null, primaryPhone, null, null)
        );
        assertTrue(exception.getMessage().contains("email"));
    }

    @Test
    void createLegalGuardian_WithEmptyEmail_ShouldThrowException() {
        LegalGuardianId id = new LegalGuardianId("guardian-123");
        PersonName name = new PersonName("John", "Doe");
        String primaryPhone = "+1234567890";

        DomainException exception = assertThrows(DomainException.class, () ->
                new LegalGuardian(id, name, "", primaryPhone, null, null)
        );
        assertTrue(exception.getMessage().contains("email"));
    }

    @Test
    void createLegalGuardian_WithInvalidEmailFormat_ShouldThrowException() {
        LegalGuardianId id = new LegalGuardianId("guardian-123");
        PersonName name = new PersonName("John", "Doe");
        String primaryPhone = "+1234567890";

        DomainException exception = assertThrows(DomainException.class, () ->
                new LegalGuardian(id, name, "invalid-email", primaryPhone, null, null)
        );
        assertTrue(exception.getMessage().contains("email"));
    }

    @Test
    void createLegalGuardian_WithNullPrimaryPhone_ShouldThrowException() {
        LegalGuardianId id = new LegalGuardianId("guardian-123");
        PersonName name = new PersonName("John", "Doe");
        String email = "john.doe@example.com";

        DomainException exception = assertThrows(DomainException.class, () ->
                new LegalGuardian(id, name, email, null, null, null)
        );
        assertTrue(exception.getMessage().contains("primaryPhone"));
    }

    @Test
    void createLegalGuardian_WithEmptyPrimaryPhone_ShouldThrowException() {
        LegalGuardianId id = new LegalGuardianId("guardian-123");
        PersonName name = new PersonName("John", "Doe");
        String email = "john.doe@example.com";

        DomainException exception = assertThrows(DomainException.class, () ->
                new LegalGuardian(id, name, email, "", null, null)
        );
        assertTrue(exception.getMessage().contains("primaryPhone"));
    }

    @Test
    void createLegalGuardian_WithValidDataAndNullSecondaryPhoneAndAddress_ShouldSucceed() {
        LegalGuardianId id = new LegalGuardianId("guardian-123");
        PersonName name = new PersonName("John", "Doe");
        String email = "john.doe@example.com";
        String primaryPhone = "+1234567890";

        LegalGuardian guardian = new LegalGuardian(id, name, email, primaryPhone, null, null);

        assertNotNull(guardian);
        assertEquals(id, guardian.getId());
        assertEquals(name, guardian.getName());
        assertEquals(email, guardian.getEmail());
        assertEquals(primaryPhone, guardian.getPrimaryPhone());
        assertNull(guardian.getSecondaryPhone());
        assertNull(guardian.getAddress());
    }

    @Test
    void updateEmail_WithValidEmail_ShouldUpdate() {
        LegalGuardianId id = new LegalGuardianId("guardian-123");
        PersonName name = new PersonName("John", "Doe");
        String originalEmail = "john.doe@example.com";
        String primaryPhone = "+1234567890";
        LegalGuardian guardian = new LegalGuardian(id, name, originalEmail, primaryPhone, null, null);

        String newEmail = "new.email@example.com";
        guardian.updateEmail(newEmail);

        assertEquals(newEmail, guardian.getEmail());
    }

    @Test
    void updateEmail_WithInvalidEmail_ShouldThrowException() {
        LegalGuardianId id = new LegalGuardianId("guardian-123");
        PersonName name = new PersonName("John", "Doe");
        String originalEmail = "john.doe@example.com";
        String primaryPhone = "+1234567890";
        LegalGuardian guardian = new LegalGuardian(id, name, originalEmail, primaryPhone, null, null);

        DomainException exception = assertThrows(DomainException.class, () ->
                guardian.updateEmail("invalid")
        );
        assertTrue(exception.getMessage().contains("email"));
        assertEquals(originalEmail, guardian.getEmail());
    }

    @Test
    void updatePrimaryPhone_WithValidPhone_ShouldUpdate() {
        LegalGuardianId id = new LegalGuardianId("guardian-123");
        PersonName name = new PersonName("John", "Doe");
        String email = "john.doe@example.com";
        String originalPhone = "+1234567890";
        LegalGuardian guardian = new LegalGuardian(id, name, email, originalPhone, null, null);

        String newPhone = "+1111111111";
        guardian.updatePrimaryPhone(newPhone);

        assertEquals(newPhone, guardian.getPrimaryPhone());
    }

    @Test
    void updatePrimaryPhone_WithEmptyPhone_ShouldThrowException() {
        LegalGuardianId id = new LegalGuardianId("guardian-123");
        PersonName name = new PersonName("John", "Doe");
        String email = "john.doe@example.com";
        String originalPhone = "+1234567890";
        LegalGuardian guardian = new LegalGuardian(id, name, email, originalPhone, null, null);

        DomainException exception = assertThrows(DomainException.class, () ->
                guardian.updatePrimaryPhone("")
        );
        assertTrue(exception.getMessage().contains("primaryPhone"));
        assertEquals(originalPhone, guardian.getPrimaryPhone());
    }

    @Test
    void updateSecondaryPhone_WithValidPhone_ShouldUpdate() {
        LegalGuardianId id = new LegalGuardianId("guardian-123");
        PersonName name = new PersonName("John", "Doe");
        String email = "john.doe@example.com";
        String primaryPhone = "+1234567890";
        LegalGuardian guardian = new LegalGuardian(id, name, email, primaryPhone, null, null);

        String secondaryPhone = "+9999999999";
        guardian.updateSecondaryPhone(secondaryPhone);

        assertEquals(secondaryPhone, guardian.getSecondaryPhone());
    }

    @Test
    void updateSecondaryPhone_WithNull_ShouldUpdateToNull() {
        LegalGuardianId id = new LegalGuardianId("guardian-123");
        PersonName name = new PersonName("John", "Doe");
        String email = "john.doe@example.com";
        String primaryPhone = "+1234567890";
        String originalSecondary = "+1111111111";
        LegalGuardian guardian = new LegalGuardian(id, name, email, primaryPhone, originalSecondary, null);

        guardian.updateSecondaryPhone(null);

        assertNull(guardian.getSecondaryPhone());
    }

    @Test
    void updateAddress_WithValidAddress_ShouldUpdate() {
        LegalGuardianId id = new LegalGuardianId("guardian-123");
        PersonName name = new PersonName("John", "Doe");
        String email = "john.doe@example.com";
        String primaryPhone = "+1234567890";
        LegalGuardian guardian = new LegalGuardian(id, name, email, primaryPhone, null, null);

        String newAddress = "456 New Street";
        guardian.updateAddress(newAddress);

        assertEquals(newAddress, guardian.getAddress());
    }

    @Test
    void updateAddress_WithNull_ShouldUpdateToNull() {
        LegalGuardianId id = new LegalGuardianId("guardian-123");
        PersonName name = new PersonName("John", "Doe");
        String email = "john.doe@example.com";
        String primaryPhone = "+1234567890";
        String originalAddress = "123 Old Street";
        LegalGuardian guardian = new LegalGuardian(id, name, email, primaryPhone, null, originalAddress);

        guardian.updateAddress(null);

        assertNull(guardian.getAddress());
    }
}