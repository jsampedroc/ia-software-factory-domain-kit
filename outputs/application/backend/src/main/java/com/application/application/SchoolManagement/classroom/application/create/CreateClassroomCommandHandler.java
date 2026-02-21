package com.application.application.SchoolManagement.classroom.application.create;

import com.application.domain.SchoolManagement.school.domain.Classroom;
import com.application.domain.SchoolManagement.school.domain.repository.ClassroomRepository;
import com.application.domain.SchoolManagement.school.domain.School;
import com.application.domain.SchoolManagement.school.domain.repository.SchoolRepository;
import com.application.domain.SchoolManagement.valueobject.ClassroomId;
import com.application.domain.SchoolManagement.valueobject.SchoolId;
import com.application.domain.exception.DomainException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class CreateClassroomCommandHandler {

    private final ClassroomRepository classroomRepository;
    private final SchoolRepository schoolRepository;

    @Transactional
    public ClassroomId handle(CreateClassroomCommand command) {
        SchoolId schoolId = new SchoolId(command.getSchoolId());
        School school = schoolRepository.findById(schoolId)
                .orElseThrow(() -> new DomainException("School not found with id: " + command.getSchoolId()));

        if (!school.isActive()) {
            throw new DomainException("Cannot create classroom for an inactive school.");
        }

        ClassroomId classroomId = classroomRepository.nextIdentity();
        Classroom classroom = Classroom.builder()
                .id(classroomId)
                .gradeLevel(command.getGradeLevel())
                .section(command.getSection())
                .academicYear(command.getAcademicYear())
                .capacity(command.getCapacity())
                .tutorTeacherId(command.getTutorTeacherId())
                .schoolId(schoolId)
                .active(true)
                .build();

        classroom.validateBusinessRules();
        classroomRepository.save(classroom);

        return classroomId;
    }
}