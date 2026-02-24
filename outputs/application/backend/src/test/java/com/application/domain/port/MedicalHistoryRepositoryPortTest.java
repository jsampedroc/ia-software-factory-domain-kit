package com.application.domain.port;

import com.application.domain.model.MedicalHistory;
import com.application.domain.valueobject.MedicalHistoryId;
import com.application.domain.valueobject.PatientId;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class MedicalHistoryRepositoryPortTest {

    @Mock
    private MedicalHistoryRepositoryPort repositoryPort;

    @Test
    void testFindByPatientId_WhenExists_ReturnsMedicalHistory() {
        // Given
        PatientId patientId = new PatientId(UUID.randomUUID());
        MedicalHistoryId historyId = new MedicalHistoryId(UUID.randomUUID());
        MedicalHistory expectedHistory = mock(MedicalHistory.class);
        when(expectedHistory.getId()).thenReturn(historyId);
        when(repositoryPort.findByPatientId(patientId)).thenReturn(Optional.of(expectedHistory));

        // When
        Optional<MedicalHistory> result = repositoryPort.findByPatientId(patientId);

        // Then
        assertTrue(result.isPresent());
        assertEquals(expectedHistory, result.get());
        assertEquals(historyId, result.get().getId());
        verify(repositoryPort, times(1)).findByPatientId(patientId);
    }

    @Test
    void testFindByPatientId_WhenNotExists_ReturnsEmpty() {
        // Given
        PatientId patientId = new PatientId(UUID.randomUUID());
        when(repositoryPort.findByPatientId(patientId)).thenReturn(Optional.empty());

        // When
        Optional<MedicalHistory> result = repositoryPort.findByPatientId(patientId);

        // Then
        assertFalse(result.isPresent());
        verify(repositoryPort, times(1)).findByPatientId(patientId);
    }

    @Test
    void testExistsByPatientId_WhenExists_ReturnsTrue() {
        // Given
        PatientId patientId = new PatientId(UUID.randomUUID());
        when(repositoryPort.existsByPatientId(patientId)).thenReturn(true);

        // When
        boolean result = repositoryPort.existsByPatientId(patientId);

        // Then
        assertTrue(result);
        verify(repositoryPort, times(1)).existsByPatientId(patientId);
    }

    @Test
    void testExistsByPatientId_WhenNotExists_ReturnsFalse() {
        // Given
        PatientId patientId = new PatientId(UUID.randomUUID());
        when(repositoryPort.existsByPatientId(patientId)).thenReturn(false);

        // When
        boolean result = repositoryPort.existsByPatientId(patientId);

        // Then
        assertFalse(result);
        verify(repositoryPort, times(1)).existsByPatientId(patientId);
    }

    @Test
    void testInheritedMethodsFromEntityRepository() {
        // Verify that the interface extends EntityRepository and thus inherits its methods
        // This is a structural test, we just ensure the port can be used as an EntityRepository
        MedicalHistoryId id = new MedicalHistoryId(UUID.randomUUID());
        MedicalHistory history = mock(MedicalHistory.class);
        when(history.getId()).thenReturn(id);

        // Test save method (inherited)
        when(repositoryPort.save(history)).thenReturn(history);
        MedicalHistory saved = repositoryPort.save(history);
        assertNotNull(saved);
        assertEquals(id, saved.getId());
        verify(repositoryPort, times(1)).save(history);

        // Test findById method (inherited)
        when(repositoryPort.findById(id)).thenReturn(Optional.of(history));
        Optional<MedicalHistory> found = repositoryPort.findById(id);
        assertTrue(found.isPresent());
        assertEquals(history, found.get());
        verify(repositoryPort, times(1)).findById(id);

        // Test delete method (inherited)
        doNothing().when(repositoryPort).delete(history);
        repositoryPort.delete(history);
        verify(repositoryPort, times(1)).delete(history);
    }
}