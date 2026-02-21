package com.application.infrastructure.persistence.SchoolManagement.school.adapter;

import com.application.domain.SchoolManagement.school.domain.School;
import com.application.domain.SchoolManagement.valueobject.SchoolId;
import com.application.infrastructure.persistence.SchoolManagement.school.entity.SchoolEntity;
import com.application.infrastructure.persistence.SchoolManagement.school.mapper.SchoolEntityMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class SchoolJpaRepositoryTest {

    @Mock
    private SchoolJpaRepository schoolJpaRepository;

    @Mock
    private SchoolEntityMapper schoolEntityMapper;

    private SchoolEntity schoolEntity;
    private School school;
    private SchoolId schoolId;

    @BeforeEach
    void setUp() {
        UUID uuid = UUID.randomUUID();
        schoolId = new SchoolId(uuid);
        school = mock(School.class);
        schoolEntity = new SchoolEntity();
        schoolEntity.setId(uuid);
        schoolEntity.setName("Test School");
        schoolEntity.setActive(true);
    }

    @Test
    void findById_WhenEntityExists_ShouldReturnOptionalOfEntity() {
        when(schoolJpaRepository.findById(schoolEntity.getId())).thenReturn(Optional.of(schoolEntity));

        Optional<SchoolEntity> result = schoolJpaRepository.findById(schoolEntity.getId());

        assertThat(result).isPresent().contains(schoolEntity);
        verify(schoolJpaRepository, times(1)).findById(schoolEntity.getId());
    }

    @Test
    void findById_WhenEntityDoesNotExist_ShouldReturnEmptyOptional() {
        when(schoolJpaRepository.findById(any(UUID.class))).thenReturn(Optional.empty());

        Optional<SchoolEntity> result = schoolJpaRepository.findById(UUID.randomUUID());

        assertThat(result).isEmpty();
        verify(schoolJpaRepository, times(1)).findById(any(UUID.class));
    }

    @Test
    void findAll_ShouldReturnPageOfEntities() {
        Pageable pageable = PageRequest.of(0, 10);
        Page<SchoolEntity> expectedPage = new PageImpl<>(List.of(schoolEntity));
        when(schoolJpaRepository.findAll(pageable)).thenReturn(expectedPage);

        Page<SchoolEntity> result = schoolJpaRepository.findAll(pageable);

        assertThat(result).isEqualTo(expectedPage);
        verify(schoolJpaRepository, times(1)).findAll(pageable);
    }

    @Test
    void save_ShouldReturnSavedEntity() {
        when(schoolJpaRepository.save(schoolEntity)).thenReturn(schoolEntity);

        SchoolEntity result = schoolJpaRepository.save(schoolEntity);

        assertThat(result).isEqualTo(schoolEntity);
        verify(schoolJpaRepository, times(1)).save(schoolEntity);
    }

    @Test
    void deleteById_ShouldCallRepositoryDelete() {
        doNothing().when(schoolJpaRepository).deleteById(schoolEntity.getId());

        schoolJpaRepository.deleteById(schoolEntity.getId());

        verify(schoolJpaRepository, times(1)).deleteById(schoolEntity.getId());
    }

    @Test
    void existsById_WhenEntityExists_ShouldReturnTrue() {
        when(schoolJpaRepository.existsById(schoolEntity.getId())).thenReturn(true);

        boolean result = schoolJpaRepository.existsById(schoolEntity.getId());

        assertThat(result).isTrue();
        verify(schoolJpaRepository, times(1)).existsById(schoolEntity.getId());
    }

    @Test
    void existsById_WhenEntityDoesNotExist_ShouldReturnFalse() {
        when(schoolJpaRepository.existsById(any(UUID.class))).thenReturn(false);

        boolean result = schoolJpaRepository.existsById(UUID.randomUUID());

        assertThat(result).isFalse();
        verify(schoolJpaRepository, times(1)).existsById(any(UUID.class));
    }

    @Test
    void findByName_WhenEntityExists_ShouldReturnOptionalOfEntity() {
        when(schoolJpaRepository.findByName("Test School")).thenReturn(Optional.of(schoolEntity));

        Optional<SchoolEntity> result = schoolJpaRepository.findByName("Test School");

        assertThat(result).isPresent().contains(schoolEntity);
        verify(schoolJpaRepository, times(1)).findByName("Test School");
    }

    @Test
    void findByName_WhenEntityDoesNotExist_ShouldReturnEmptyOptional() {
        when(schoolJpaRepository.findByName(anyString())).thenReturn(Optional.empty());

        Optional<SchoolEntity> result = schoolJpaRepository.findByName("Non-existent");

        assertThat(result).isEmpty();
        verify(schoolJpaRepository, times(1)).findByName(anyString());
    }

    @Test
    void findByActiveTrue_ShouldReturnListOfEntities() {
        List<SchoolEntity> expectedList = List.of(schoolEntity);
        when(schoolJpaRepository.findByActiveTrue()).thenReturn(expectedList);

        List<SchoolEntity> result = schoolJpaRepository.findByActiveTrue();

        assertThat(result).isEqualTo(expectedList);
        verify(schoolJpaRepository, times(1)).findByActiveTrue();
    }
}