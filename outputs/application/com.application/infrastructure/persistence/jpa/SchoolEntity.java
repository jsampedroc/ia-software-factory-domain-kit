package com.application.infrastructure.persistence.jpa;

import com.application.domain.model.schooladministration.School;
import com.application.domain.model.schooladministration.SchoolId;
import com.application.domain.valueobject.Address;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Entity
@Table(name = "schools")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SchoolEntity {
    @Id
    @Column(name = "school_id")
    private UUID schoolId;

    @Column(name = "name", nullable = false)
    private String name;

    @Embedded
    @AttributeOverrides({
            @AttributeOverride(name = "street", column = @Column(name = "address_street")),
            @AttributeOverride(name = "city", column = @Column(name = "address_city")),
            @AttributeOverride(name = "state", column = @Column(name = "address_state")),
            @AttributeOverride(name = "postalCode", column = @Column(name = "address_postal_code")),
            @AttributeOverride(name = "country", column = @Column(name = "address_country"))
    })
    private Address address;

    @Column(name = "phone")
    private String phone;

    @Column(name = "email")
    private String email;

    @Column(name = "academic_calendar")
    private String academicCalendar;

    public static SchoolEntity fromDomain(School school) {
        return SchoolEntity.builder()
                .schoolId(school.getSchoolId().getValue())
                .name(school.getName())
                .address(school.getAddress())
                .phone(school.getPhone())
                .email(school.getEmail())
                .academicCalendar(school.getAcademicCalendar())
                .build();
    }

    public School toDomain() {
        return School.builder()
                .schoolId(new SchoolId(this.schoolId))
                .name(this.name)
                .address(this.address)
                .phone(this.phone)
                .email(this.email)
                .academicCalendar(this.academicCalendar)
                .build();
    }
}