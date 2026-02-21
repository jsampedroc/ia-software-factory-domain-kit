package com.application.application.SchoolManagement.school.application.find;

import com.application.domain.SchoolManagement.valueobject.SchoolId;
import lombok.Data;
import lombok.Builder;

@Data
@Builder
public class FindSchoolQuery {
    private final SchoolId schoolId;
}