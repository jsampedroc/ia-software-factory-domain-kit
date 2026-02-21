package com.application.domain.SchoolManagement.student.domain;

import com.application.domain.shared.Entity;
import com.application.domain.shared.valueobject.PersonName;
import com.application.domain.SchoolManagement.student.valueobject.LegalGuardianId;
import com.application.domain.exception.DomainException;
import lombok.Data;
import lombok.Builder;

@Data
@Builder
public class LegalGuardian extends Entity<LegalGuardianId> {
    private PersonName name;
    private String email;
    private String primaryPhone;
    private String secondaryPhone;
    private String address;

    private LegalGuardian(LegalGuardianId id, PersonName name, String email, String primaryPhone, String secondaryPhone, String address) {
        super(id);
        this.name = name;
        this.email = email;
        this.primaryPhone = primaryPhone;
        this.secondaryPhone = secondaryPhone;
        this.address = address;
        this.validate();
    }

    public static LegalGuardian create(LegalGuardianId id, PersonName name, String email, String primaryPhone, String secondaryPhone, String address) {
        return new LegalGuardian(id, name, email, primaryPhone, secondaryPhone, address);
    }

    private void validate() {
        if (this.name == null) {
            throw new DomainException("Legal guardian name is required.");
        }
        if (this.email == null || this.email.isBlank()) {
            throw new DomainException("Legal guardian email is required.");
        }
        if (!isValidEmail(this.email)) {
            throw new DomainException("Legal guardian email must have a valid format.");
        }
        if (this.primaryPhone == null || this.primaryPhone.isBlank()) {
            throw new DomainException("At least one phone number (primary) is required for legal guardian.");
        }
    }

    private boolean isValidEmail(String email) {
        String emailRegex = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$";
        return email.matches(emailRegex);
    }

    public void updateContactInfo(String email, String primaryPhone, String secondaryPhone, String address) {
        this.email = email;
        this.primaryPhone = primaryPhone;
        this.secondaryPhone = secondaryPhone;
        this.address = address;
        this.validate();
    }
}