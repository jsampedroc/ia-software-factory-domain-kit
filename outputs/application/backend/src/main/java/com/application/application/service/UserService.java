package com.application.application.service;

import com.application.domain.model.User;
import com.application.domain.model.AccessLog;
import com.application.domain.valueobject.UserId;
import com.application.domain.enums.UserRole;
import com.application.domain.repository.UserRepository;
import com.application.domain.exception.DomainException;
import com.application.domain.shared.EntityRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public class UserService {

    private final UserRepository userRepository;
    private final EntityRepository<AccessLog, ?> accessLogRepository;

    public UserService(UserRepository userRepository, EntityRepository<AccessLog, ?> accessLogRepository) {
        this.userRepository = userRepository;
        this.accessLogRepository = accessLogRepository;
    }

    public User registerUser(String username, String email, UserRole role) {
        validateUsernameUnique(username);
        validateEmailUnique(email);

        User newUser = User.builder()
                .userId(new UserId(UUID.randomUUID()))
                .username(username)
                .email(email)
                .role(role)
                .isActive(true)
                .createdAt(LocalDateTime.now())
                .build();

        return userRepository.save(newUser);
    }

    public User updateUserRole(UserId userId, UserRole newRole, UserId updaterId) {
        User user = findUserById(userId);
        User updater = findUserById(updaterId);

        // Only Administrators can change roles
        if (updater.getRole() != UserRole.ADMINISTRATOR) {
            throw new DomainException("Only administrators can change user roles.");
        }

        // Cannot demote the last administrator
        if (user.getRole() == UserRole.ADMINISTRATOR && newRole != UserRole.ADMINISTRATOR) {
            long adminCount = userRepository.findAll().stream()
                    .filter(u -> u.getRole() == UserRole.ADMINISTRATOR && u.isActive())
                    .count();
            if (adminCount <= 1) {
                throw new DomainException("Cannot demote the last active administrator.");
            }
        }

        User updatedUser = User.builder()
                .userId(user.getUserId())
                .username(user.getUsername())
                .email(user.getEmail())
                .role(newRole)
                .isActive(user.isActive())
                .createdAt(user.getCreatedAt())
                .build();

        logAccess(updaterId, "UPDATE_ROLE", "User/" + userId.getValue(), "Changed role to " + newRole);
        return userRepository.save(updatedUser);
    }

    public User deactivateUser(UserId userId, UserId deactivatorId) {
        User user = findUserById(userId);
        User deactivator = findUserById(deactivatorId);

        // Only Administrators can deactivate users
        if (deactivator.getRole() != UserRole.ADMINISTRATOR) {
            throw new DomainException("Only administrators can deactivate users.");
        }

        // Cannot deactivate yourself
        if (userId.equals(deactivatorId)) {
            throw new DomainException("Users cannot deactivate their own account.");
        }

        // Cannot deactivate the last administrator
        if (user.getRole() == UserRole.ADMINISTRATOR) {
            long activeAdminCount = userRepository.findAll().stream()
                    .filter(u -> u.getRole() == UserRole.ADMINISTRATOR && u.isActive())
                    .count();
            if (activeAdminCount <= 1) {
                throw new DomainException("Cannot deactivate the last active administrator.");
            }
        }

        User deactivatedUser = User.builder()
                .userId(user.getUserId())
                .username(user.getUsername())
                .email(user.getEmail())
                .role(user.getRole())
                .isActive(false)
                .createdAt(user.getCreatedAt())
                .build();

        logAccess(deactivatorId, "DEACTIVATE", "User/" + userId.getValue(), "User deactivated");
        return userRepository.save(deactivatedUser);
    }

    public User reactivateUser(UserId userId, UserId reactivatorId) {
        User user = findUserById(userId);
        User reactivator = findUserById(reactivatorId);

        if (reactivator.getRole() != UserRole.ADMINISTRATOR) {
            throw new DomainException("Only administrators can reactivate users.");
        }

        if (user.isActive()) {
            throw new DomainException("User is already active.");
        }

        User reactivatedUser = User.builder()
                .userId(user.getUserId())
                .username(user.getUsername())
                .email(user.getEmail())
                .role(user.getRole())
                .isActive(true)
                .createdAt(user.getCreatedAt())
                .build();

        logAccess(reactivatorId, "REACTIVATE", "User/" + userId.getValue(), "User reactivated");
        return userRepository.save(reactivatedUser);
    }

    public Optional<User> authenticate(String username) {
        return userRepository.findByUsername(username)
                .filter(User::isActive);
    }

    public User findUserById(UserId userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new DomainException("User not found with ID: " + userId.getValue()));
    }

    public List<User> findAllActiveUsers() {
        return userRepository.findAll().stream()
                .filter(User::isActive)
                .toList();
    }

    public List<User> findUsersByRole(UserRole role) {
        return userRepository.findAll().stream()
                .filter(user -> user.getRole() == role && user.isActive())
                .toList();
    }

    public void logAccess(UserId userId, String action, String resource, String details) {
        AccessLog log = AccessLog.builder()
                .logId(UUID.randomUUID())
                .userId(userId.getValue())
                .action(action)
                .resource(resource)
                .timestamp(LocalDateTime.now())
                .ipAddress("system") // In infrastructure, this would be populated from request context
                .build();
        accessLogRepository.save(log);
    }

    private void validateUsernameUnique(String username) {
        userRepository.findByUsername(username).ifPresent(user -> {
            throw new DomainException("Username '" + username + "' is already taken.");
        });
    }

    private void validateEmailUnique(String email) {
        userRepository.findAll().stream()
                .filter(user -> email.equalsIgnoreCase(user.getEmail()))
                .findFirst()
                .ifPresent(user -> {
                    throw new DomainException("Email '" + email + "' is already registered.");
                });
    }
}