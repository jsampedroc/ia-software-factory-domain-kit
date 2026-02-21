package com.application.infrastructure.persistence.SchoolManagement.school.mapper;

import com.application.domain.SchoolManagement.school.domain.School;
import com.application.domain.SchoolManagement.valueobject.SchoolId;
import com.application.infrastructure.persistence.SchoolManagement.school.entity.SchoolEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class SchoolEntityMapper {

    public SchoolEntity toEntity(School school) {
        if (school == null) {
            return null;
        }

        SchoolEntity entity = new SchoolEntity();
        entity.setId(school.getId().getValue());
        entity.setName(school.getName());
        entity.setAddress(school.getAddress());
        entity.setPhoneNumber(school.getPhoneNumber());
        entity.setActive(school.isActive());
        return entity;
    }

    public School toDomain(SchoolEntity entity) {
        if (entity == null) {
            return null;
        }

        return School.builder()
                .id(new SchoolId(entity.getId()))
                .name(entity.getName())
                .address(entity.getAddress())
                .phoneNumber(entity.getPhoneNumber())
                .active(entity.isActive())
                .build();
    }
}