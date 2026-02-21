package com.application;

import com.application.domain.SchoolManagement.school.domain.School;
import com.application.domain.SchoolManagement.valueobject.SchoolId;
import com.application.infrastructure.persistence.SchoolManagement.school.adapter.SchoolJpaRepository;
import com.application.infrastructure.persistence.SchoolManagement.school.entity.SchoolEntity;
import com.application.infrastructure.persistence.SchoolManagement.school.mapper.SchoolEntityMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class SchoolRepositoryImplTest {

    @Mock
    private SchoolJpaRepository schoolJpaRepository;

    @Mock
    private SchoolEntityMapper schoolEntityMapper;

    @InjectMocks
    private SchoolRepositoryImpl schoolRepositoryImpl;

    private SchoolId testSchoolId;
    private School testSchool;
    private SchoolEntity testSchoolEntity;

    @BeforeEach
    void setUp() {
        testSchoolId = new SchoolId(UUID.randomUUID());
        testSchool = mock(School.class);
        testSchoolEntity = mock(SchoolEntity.class);
    }

    @Test
    void save_ShouldCallJpaRepositoryAndMapper() {
        when(schoolEntityMapper.toEntity(testSchool)).thenReturn(testSchoolEntity);
        when(schoolJpaRepository.save(testSchoolEntity)).thenReturn(testSchoolEntity);
        when(schoolEntityMapper.toDomain(testSchoolEntity)).thenReturn(testSchool);

        School result = schoolRepositoryImpl.save(testSchool);

        assertNotNull(result);
        verify(schoolEntityMapper).toEntity(testSchool);
        verify(schoolJpaRepository).save(testSchoolEntity);
        verify(schoolEntityMapper).toDomain(testSchoolEntity);
    }

    @Test
    void findById_WhenEntityExists_ShouldReturnSchool() {
        when(schoolJpaRepository.findById(testSchoolId.getValue())).thenReturn(Optional.of(testSchoolEntity));
        when(schoolEntityMapper.toDomain(testSchoolEntity)).thenReturn(testSchool);

        Optional<School> result = schoolRepositoryImpl.findById(testSchoolId);

        assertTrue(result.isPresent());
        assertEquals(testSchool, result.get());
        verify(schoolJpaRepository).findById(testSchoolId.getValue());
        verify(schoolEntityMapper).toDomain(testSchoolEntity);
    }

    @Test
    void findById_WhenEntityDoesNotExist_ShouldReturnEmpty() {
        when(schoolJpaRepository.findById(testSchoolId.getValue())).thenReturn(Optional.empty());

        Optional<School> result = schoolRepositoryImpl.findById(testSchoolId);

        assertFalse(result.isPresent());
        verify(schoolJpaRepository).findById(testSchoolId.getValue());
        verify(schoolEntityMapper, never()).toDomain(any());
    }

    @Test
    void existsById_ShouldReturnTrue_WhenJpaRepositoryReturnsTrue() {
        when(schoolJpaRepository.existsById(testSchoolId.getValue())).thenReturn(true);

        boolean result = schoolRepositoryImpl.existsById(testSchoolId);

        assertTrue(result);
        verify(schoolJpaRepository).existsById(testSchoolId.getValue());
    }

    @Test
    void existsById_ShouldReturnFalse_WhenJpaRepositoryReturnsFalse() {
        when(schoolJpaRepository.existsById(testSchoolId.getValue())).thenReturn(false);

        boolean result = schoolRepositoryImpl.existsById(testSchoolId);

        assertFalse(result);
        verify(schoolJpaRepository).existsById(testSchoolId.getValue());
    }

    @Test
    void delete_ShouldCallJpaRepository() {
        schoolRepositoryImpl.delete(testSchoolId);

        verify(schoolJpaRepository).deleteById(testSchoolId.getValue());
    }
}