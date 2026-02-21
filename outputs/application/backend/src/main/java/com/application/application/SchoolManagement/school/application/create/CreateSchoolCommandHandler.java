package com.application.application.SchoolManagement.school.application.create;

import com.application.domain.SchoolManagement.school.domain.School;
import com.application.domain.SchoolManagement.school.domain.repository.SchoolRepository;
import com.application.domain.SchoolManagement.valueobject.SchoolId;
import com.application.domain.shared.valueobject.PersonName;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class CreateSchoolCommandHandler {

    private final SchoolRepository schoolRepository;

    @Transactional
    public SchoolId handle(CreateSchoolCommand command) {
        PersonName schoolName = new PersonName(command.getName(), "");
        School school = School.create(
                schoolName,
                command.getAddress(),
                command.getPhoneNumber()
        );

        School savedSchool = schoolRepository.save(school);
        return savedSchool.getId();
    }
}