package com.application;

import com.application.domain.SchoolManagement.school.domain.School;
import com.application.domain.SchoolManagement.valueobject.SchoolId;
import com.application.infrastructure.persistence.SchoolManagement.school.entity.SchoolEntity;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class SchoolEntityMapperTest {

    private SchoolEntityMapper mapper;

    @BeforeEach
    void setUp() {
        mapper = new SchoolEntityMapper();
    }

    @Test
    void toDomain_ShouldMapEntityToDomain() {
        UUID uuid = UUID.randomUUID();
        SchoolEntity entity = new SchoolEntity();
        entity.setId(uuid);
        entity.setName("Test School");
        entity.setAddress("123 Main St");
        entity.setPhoneNumber("555-1234");
        entity.setActive(true);

        School domain = mapper.toDomain(entity);

        assertNotNull(domain);
        assertEquals(SchoolId.of(uuid), domain.getId());
        assertEquals("Test School", domain.getName());
        assertEquals("123 Main St", domain.getAddress());
        assertEquals("555-1234", domain.getPhoneNumber());
        assertTrue(domain.isActive());
    }

    @Test
    void toDomain_ShouldReturnNullWhenEntityIsNull() {
        School domain = mapper.toDomain(null);
        assertNull(domain);
    }

    @Test
    void toEntity_ShouldMapDomainToEntity() {
        UUID uuid = UUID.randomUUID();
        SchoolId schoolId = SchoolId.of(uuid);
        School domain = School.builder()
                .id(schoolId)
                .name("Test School")
                .address("123 Main St")
                .phoneNumber("555-1234")
                .active(true)
                .build();

        SchoolEntity entity = mapper.toEntity(domain);

        assertNotNull(entity);
        assertEquals(uuid, entity.getId());
        assertEquals("Test School", entity.getName());
        assertEquals("123 Main St", entity.getAddress());
        assertEquals("555-1234", entity.getPhoneNumber());
        assertTrue(entity.isActive());
    }

    @Test
    void toEntity_ShouldReturnNullWhenDomainIsNull() {
        SchoolEntity entity = mapper.toEntity(null);
        assertNull(entity);
    }
}