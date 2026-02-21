package com.application.interfaces.rest.SchoolManagement.school.dto;

import com.application.domain.SchoolManagement.school.domain.School;
import com.application.domain.SchoolManagement.valueobject.SchoolId;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SchoolResponseDTO {
    private SchoolId id;
    private String name;
    private String address;
    private String phoneNumber;
    private Boolean active;
    private Instant createdAt;
    private Instant updatedAt;

    public static SchoolResponseDTO fromDomain(School school) {
        return SchoolResponseDTO.builder()
                .id(school.getId())
                .name(school.getName())
                .address(school.getAddress())
                .phoneNumber(school.getPhoneNumber())
                .active(school.getActive())
                .createdAt(school.getCreatedAt())
                .updatedAt(school.getUpdatedAt())
                .build();
    }
}