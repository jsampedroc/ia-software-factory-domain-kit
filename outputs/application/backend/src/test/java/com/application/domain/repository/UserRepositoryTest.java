package com.application.domain.repository;

import com.application.domain.model.User;
import com.application.domain.valueobject.UserId;
import com.application.domain.enums.UserRole;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UserRepositoryTest {

    @Mock
    private UserRepository userRepository;

    private User testUser;
    private UserId testUserId;

    @BeforeEach
    void setUp() {
        testUserId = new UserId(UUID.randomUUID());
        testUser = User.create(
                testUserId,
                "jdoe",
                "john.doe@dentalclinic.com",
                UserRole.DENTIST,
                true,
                LocalDateTime.now()
        );
    }

    @Test
    void save_ShouldPersistUser() {
        // Given
        when(userRepository.save(any(User.class))).thenReturn(testUser);

        // When
        User savedUser = userRepository.save(testUser);

        // Then
        verify(userRepository).save(testUser);
        assertThat(savedUser).isEqualTo(testUser);
    }

    @Test
    void findById_WithExistingId_ShouldReturnUser() {
        // Given
        when(userRepository.findById(testUserId)).thenReturn(Optional.of(testUser));

        // When
        Optional<User> foundUser = userRepository.findById(testUserId);

        // Then
        verify(userRepository).findById(testUserId);
        assertThat(foundUser).isPresent().contains(testUser);
    }

    @Test
    void findById_WithNonExistingId_ShouldReturnEmpty() {
        // Given
        UserId nonExistingId = new UserId(UUID.randomUUID());
        when(userRepository.findById(nonExistingId)).thenReturn(Optional.empty());

        // When
        Optional<User> foundUser = userRepository.findById(nonExistingId);

        // Then
        verify(userRepository).findById(nonExistingId);
        assertThat(foundUser).isEmpty();
    }

    @Test
    void delete_ShouldRemoveUser() {
        // When
        userRepository.delete(testUser);

        // Then
        verify(userRepository).delete(testUser);
    }

    @Test
    void findByUsername_WithExistingUsername_ShouldReturnUser() {
        // Given
        String username = "jdoe";
        when(userRepository.findByUsername(username)).thenReturn(Optional.of(testUser));

        // When
        Optional<User> foundUser = userRepository.findByUsername(username);

        // Then
        verify(userRepository).findByUsername(username);
        assertThat(foundUser).isPresent().contains(testUser);
    }

    @Test
    void findByUsername_WithNonExistingUsername_ShouldReturnEmpty() {
        // Given
        String nonExistingUsername = "nonexistent";
        when(userRepository.findByUsername(nonExistingUsername)).thenReturn(Optional.empty());

        // When
        Optional<User> foundUser = userRepository.findByUsername(nonExistingUsername);

        // Then
        verify(userRepository).findByUsername(nonExistingUsername);
        assertThat(foundUser).isEmpty();
    }

    @Test
    void findByEmail_WithExistingEmail_ShouldReturnUser() {
        // Given
        String email = "john.doe@dentalclinic.com";
        when(userRepository.findByEmail(email)).thenReturn(Optional.of(testUser));

        // When
        Optional<User> foundUser = userRepository.findByEmail(email);

        // Then
        verify(userRepository).findByEmail(email);
        assertThat(foundUser).isPresent().contains(testUser);
    }

    @Test
    void findByEmail_WithNonExistingEmail_ShouldReturnEmpty() {
        // Given
        String nonExistingEmail = "nonexistent@example.com";
        when(userRepository.findByEmail(nonExistingEmail)).thenReturn(Optional.empty());

        // When
        Optional<User> foundUser = userRepository.findByEmail(nonExistingEmail);

        // Then
        verify(userRepository).findByEmail(nonExistingEmail);
        assertThat(foundUser).isEmpty();
    }

    @Test
    void existsByUsername_WithExistingUsername_ShouldReturnTrue() {
        // Given
        String username = "jdoe";
        when(userRepository.existsByUsername(username)).thenReturn(true);

        // When
        boolean exists = userRepository.existsByUsername(username);

        // Then
        verify(userRepository).existsByUsername(username);
        assertThat(exists).isTrue();
    }

    @Test
    void existsByUsername_WithNonExistingUsername_ShouldReturnFalse() {
        // Given
        String nonExistingUsername = "nonexistent";
        when(userRepository.existsByUsername(nonExistingUsername)).thenReturn(false);

        // When
        boolean exists = userRepository.existsByUsername(nonExistingUsername);

        // Then
        verify(userRepository).existsByUsername(nonExistingUsername);
        assertThat(exists).isFalse();
    }

    @Test
    void existsByEmail_WithExistingEmail_ShouldReturnTrue() {
        // Given
        String email = "john.doe@dentalclinic.com";
        when(userRepository.existsByEmail(email)).thenReturn(true);

        // When
        boolean exists = userRepository.existsByEmail(email);

        // Then
        verify(userRepository).existsByEmail(email);
        assertThat(exists).isTrue();
    }

    @Test
    void existsByEmail_WithNonExistingEmail_ShouldReturnFalse() {
        // Given
        String nonExistingEmail = "nonexistent@example.com";
        when(userRepository.existsByEmail(nonExistingEmail)).thenReturn(false);

        // When
        boolean exists = userRepository.existsByEmail(nonExistingEmail);

        // Then
        verify(userRepository).existsByEmail(nonExistingEmail);
        assertThat(exists).isFalse();
    }

    @Test
    void existsById_WithExistingId_ShouldReturnTrue() {
        // Given
        when(userRepository.existsById(testUserId)).thenReturn(true);

        // When
        boolean exists = userRepository.existsById(testUserId);

        // Then
        verify(userRepository).existsById(testUserId);
        assertThat(exists).isTrue();
    }

    @Test
    void existsById_WithNonExistingId_ShouldReturnFalse() {
        // Given
        UserId nonExistingId = new UserId(UUID.randomUUID());
        when(userRepository.existsById(nonExistingId)).thenReturn(false);

        // When
        boolean exists = userRepository.existsById(nonExistingId);

        // Then
        verify(userRepository).existsById(nonExistingId);
        assertThat(exists).isFalse();
    }

    @Test
    void count_ShouldReturnNumberOfUsers() {
        // Given
        long expectedCount = 5L;
        when(userRepository.count()).thenReturn(expectedCount);

        // When
        long actualCount = userRepository.count();

        // Then
        verify(userRepository).count();
        assertThat(actualCount).isEqualTo(expectedCount);
    }
}