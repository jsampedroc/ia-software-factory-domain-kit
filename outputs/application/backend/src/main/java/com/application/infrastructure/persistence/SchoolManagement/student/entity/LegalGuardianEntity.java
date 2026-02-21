package com.application.infrastructure.persistence.SchoolManagement.student.entity;

import com.application.domain.SchoolManagement.student.domain.LegalGuardian;
import com.application.domain.SchoolManagement.valueobject.LegalGuardianId;
import com.application.infrastructure.persistence.shared.valueobject.PersonNameEmbeddable;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Entity
@Table(name = "legal_guardians")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class LegalGuardianEntity {

    @Id
    private UUID id;

    @Embedded
    @AttributeOverrides({
            @AttributeOverride(name = "firstName", column = @Column(name = "first_name", nullable = false)),
            @AttributeOverride(name = "lastName", column = @Column(name = "last_name", nullable = false))
    })
    private PersonNameEmbeddable name;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(name = "primary_phone", nullable = false)
    private String primaryPhone;

    @Column(name = "secondary_phone")
    private String secondaryPhone;

    private String address;

    public static LegalGuardianEntity fromDomain(LegalGuardian guardian) {
        return LegalGuardianEntity.builder()
                .id(guardian.getId().getValue())
                .name(PersonNameEmbeddable.fromDomain(guardian.getName()))
                .email(guardian.getEmail())
                .primaryPhone(guardian.getPrimaryPhone())
                .secondaryPhone(guardian.getSecondaryPhone())
                .address(guardian.getAddress())
                .build();
    }

    public LegalGuardian toDomain() {
        return LegalGuardian.builder()
                .id(new LegalGuardianId(this.id))
                .name(this.name.toDomain())
                .email(this.email)
                .primaryPhone(this.primaryPhone)
                .secondaryPhone(this.secondaryPhone)
                .address(this.address)
                .build();
    }
}