package com.application.application.SchoolManagement.school.application.find;

import com.application.domain.SchoolManagement.school.domain.School;
import com.application.domain.SchoolManagement.school.domain.repository.SchoolRepository;
import com.application.domain.SchoolManagement.valueobject.SchoolId;
import com.application.domain.exception.DomainException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class FindSchoolQueryHandler {

    private final SchoolRepository schoolRepository;

    @Transactional(readOnly = true)
    public School handle(FindSchoolQuery query) {
        SchoolId schoolId = new SchoolId(query.schoolId());
        return schoolRepository.findById(schoolId)
                .orElseThrow(() -> new DomainException("School not found with id: " + query.schoolId()));
    }
}