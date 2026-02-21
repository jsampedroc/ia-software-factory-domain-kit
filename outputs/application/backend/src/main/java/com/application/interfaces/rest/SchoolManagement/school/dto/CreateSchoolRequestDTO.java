package com.application.interfaces.rest.SchoolManagement.school.dto;

import lombok.Data;
import lombok.Builder;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CreateSchoolRequestDTO {
    private String name;
    private String address;
    private String phoneNumber;
}