package com.application.domain.SchoolManagement.school.domain;

import com.application.domain.shared.Entity;
import com.application.domain.SchoolManagement.valueobject.SchoolId;
import com.application.domain.shared.valueobject.PersonName;
import com.application.domain.exception.DomainException;
import lombok.Data;
import lombok.Builder;

@Data
@Builder
public class School extends Entity<SchoolId> {
    private PersonName name;
    private String address;
    private String phoneNumber;
    private boolean active;

    private School(SchoolId id, PersonName name, String address, String phoneNumber, boolean active) {
        super(id);
        this.name = name;
        this.address = address;
        this.phoneNumber = phoneNumber;
        this.active = active;
        validate();
    }

    public static School create(SchoolId id, PersonName name, String address, String phoneNumber) {
        return new School(id, name, address, phoneNumber, true);
    }

    public void updateDetails(PersonName newName, String newAddress, String newPhoneNumber) {
        this.name = newName;
        this.address = newAddress;
        this.phoneNumber = newPhoneNumber;
        validate();
    }

    public void activate() {
        this.active = true;
    }

    public void deactivate() {
        this.active = false;
    }

    private void validate() {
        if (name == null) {
            throw new DomainException("School name is required.");
        }
        if (address == null || address.isBlank()) {
            throw new DomainException("School address is required.");
        }
        if (phoneNumber == null || phoneNumber.isBlank()) {
            throw new DomainException("School phone number is required.");
        }
    }
}