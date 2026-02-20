package com.application.school.application.mappers;

import com.application.school.application.dtos.GuardianDTO;
import com.application.school.application.dtos.StudentDTO;
import com.application.school.domain.student.model.Guardian;
import com.application.school.domain.student.model.Student;
import com.application.school.domain.student.model.StudentId;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.mapstruct.factory.Mappers;

import java.util.List;
import java.util.stream.Collectors;

@Mapper(componentModel = "spring")
public interface StudentMapper {
    StudentMapper INSTANCE = Mappers.getMapper(StudentMapper.class);

    @Mapping(source = "id.value", target = "studentId")
    @Mapping(source = "guardians", target = "guardians", qualifiedByName = "guardiansToGuardianDTOs")
    StudentDTO toDTO(Student student);

    @Mapping(source = "studentId", target = "id", qualifiedByName = "stringToStudentId")
    @Mapping(source = "guardians", target = "guardians", qualifiedByName = "guardianDTOsToGuardians")
    Student toDomain(StudentDTO studentDTO);

    @Named("stringToStudentId")
    static StudentId stringToStudentId(String value) {
        return value != null ? new StudentId(value) : null;
    }

    @Named("guardiansToGuardianDTOs")
    static List<GuardianDTO> guardiansToGuardianDTOs(List<Guardian> guardians) {
        if (guardians == null) {
            return null;
        }
        return guardians.stream()
                .map(StudentMapper.INSTANCE::guardianToDTO)
                .collect(Collectors.toList());
    }

    @Named("guardianDTOsToGuardians")
    static List<Guardian> guardianDTOsToGuardians(List<GuardianDTO> guardianDTOs) {
        if (guardianDTOs == null) {
            return null;
        }
        return guardianDTOs.stream()
                .map(StudentMapper.INSTANCE::dtoToGuardian)
                .collect(Collectors.toList());
    }

    @Mapping(source = "id.value", target = "guardianId")
    GuardianDTO guardianToDTO(Guardian guardian);

    @Mapping(source = "guardianId", target = "id", qualifiedByName = "stringToGuardianId")
    Guardian dtoToGuardian(GuardianDTO guardianDTO);

    @Named("stringToGuardianId")
    static com.application.school.domain.student.model.GuardianId stringToGuardianId(String value) {
        return value != null ? new com.application.school.domain.student.model.GuardianId(value) : null;
    }
}