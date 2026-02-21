package com.application.domain.SchoolManagement.school.domain.repository;

import com.application.domain.SchoolManagement.school.domain.School;
import com.application.domain.SchoolManagement.valueobject.SchoolId;
import com.application.domain.exception.DomainException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class SchoolRepositoryTest {

    @Mock
    private SchoolRepository schoolRepository;

    private SchoolId testSchoolId;
    private School testSchool;

    @BeforeEach
    void setUp() {
        testSchoolId = new SchoolId();
        testSchool = mock(School.class);
        when(testSchool.getId()).thenReturn(testSchoolId);
    }

    @Test
    void save_ValidSchool_SavesSuccessfully() {
        doNothing().when(schoolRepository).save(testSchool);

        assertDoesNotThrow(() -> schoolRepository.save(testSchool));
        verify(schoolRepository, times(1)).save(testSchool);
    }

    @Test
    void save_NullSchool_ThrowsException() {
        doThrow(new IllegalArgumentException("School cannot be null")).when(schoolRepository).save(null);

        Exception exception = assertThrows(IllegalArgumentException.class, () -> schoolRepository.save(null));
        assertEquals("School cannot be null", exception.getMessage());
        verify(schoolRepository, times(1)).save(null);
    }

    @Test
    void findById_ExistingId_ReturnsSchool() {
        when(schoolRepository.findById(testSchoolId)).thenReturn(Optional.of(testSchool));

        Optional<School> result = schoolRepository.findById(testSchoolId);

        assertTrue(result.isPresent());
        assertEquals(testSchool, result.get());
        verify(schoolRepository, times(1)).findById(testSchoolId);
    }

    @Test
    void findById_NonExistingId_ReturnsEmpty() {
        when(schoolRepository.findById(testSchoolId)).thenReturn(Optional.empty());

        Optional<School> result = schoolRepository.findById(testSchoolId);

        assertFalse(result.isPresent());
        verify(schoolRepository, times(1)).findById(testSchoolId);
    }

    @Test
    void findById_NullId_ThrowsException() {
        when(schoolRepository.findById(null)).thenThrow(new IllegalArgumentException("SchoolId cannot be null"));

        Exception exception = assertThrows(IllegalArgumentException.class, () -> schoolRepository.findById(null));
        assertEquals("SchoolId cannot be null", exception.getMessage());
        verify(schoolRepository, times(1)).findById(null);
    }

    @Test
    void existsById_ExistingId_ReturnsTrue() {
        when(schoolRepository.existsById(testSchoolId)).thenReturn(true);

        boolean result = schoolRepository.existsById(testSchoolId);

        assertTrue(result);
        verify(schoolRepository, times(1)).existsById(testSchoolId);
    }

    @Test
    void existsById_NonExistingId_ReturnsFalse() {
        when(schoolRepository.existsById(testSchoolId)).thenReturn(false);

        boolean result = schoolRepository.existsById(testSchoolId);

        assertFalse(result);
        verify(schoolRepository, times(1)).existsById(testSchoolId);
    }

    @Test
    void delete_ValidSchool_DeletesSuccessfully() {
        doNothing().when(schoolRepository).delete(testSchool);

        assertDoesNotThrow(() -> schoolRepository.delete(testSchool));
        verify(schoolRepository, times(1)).delete(testSchool);
    }

    @Test
    void delete_NullSchool_ThrowsException() {
        doThrow(new IllegalArgumentException("School cannot be null")).when(schoolRepository).delete(null);

        Exception exception = assertThrows(IllegalArgumentException.class, () -> schoolRepository.delete(null));
        assertEquals("School cannot be null", exception.getMessage());
        verify(schoolRepository, times(1)).delete(null);
    }

    @Test
    void findByName_ExistingName_ReturnsSchool() {
        String schoolName = "Test School";
        when(schoolRepository.findByName(schoolName)).thenReturn(Optional.of(testSchool));

        Optional<School> result = schoolRepository.findByName(schoolName);

        assertTrue(result.isPresent());
        assertEquals(testSchool, result.get());
        verify(schoolRepository, times(1)).findByName(schoolName);
    }

    @Test
    void findByName_NonExistingName_ReturnsEmpty() {
        String schoolName = "Non Existing";
        when(schoolRepository.findByName(schoolName)).thenReturn(Optional.empty());

        Optional<School> result = schoolRepository.findByName(schoolName);

        assertFalse(result.isPresent());
        verify(schoolRepository, times(1)).findByName(schoolName);
    }

    @Test
    void findByName_NullName_ThrowsException() {
        when(schoolRepository.findByName(null)).thenThrow(new IllegalArgumentException("School name cannot be null"));

        Exception exception = assertThrows(IllegalArgumentException.class, () -> schoolRepository.findByName(null));
        assertEquals("School name cannot be null", exception.getMessage());
        verify(schoolRepository, times(1)).findByName(null);
    }
}