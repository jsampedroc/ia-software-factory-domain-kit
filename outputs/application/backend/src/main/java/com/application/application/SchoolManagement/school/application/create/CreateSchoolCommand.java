package com.application.application.SchoolManagement.school.application.create;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class CreateSchoolCommand {
    private final String name;
    private final String address;
    private final String phoneNumber;
}