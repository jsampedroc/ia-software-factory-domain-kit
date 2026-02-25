package com.application.infrastructure.rest;

import com.application.application.service.UserService;
import com.application.domain.model.User;
import com.application.domain.valueobject.UserId;
import com.application.domain.enums.UserRole;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/users")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping
    public ResponseEntity<User> createUser(@RequestBody CreateUserRequest request) {
        User newUser = userService.createUser(
                request.username(),
                request.email(),
                request.role(),
                request.isActive()
        );
        return ResponseEntity.status(HttpStatus.CREATED).body(newUser);
    }

    @GetMapping("/{userId}")
    public ResponseEntity<User> getUserById(@PathVariable UUID userId) {
        return userService.findById(new UserId(userId))
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping
    public ResponseEntity<List<User>> getAllUsers() {
        List<User> users = userService.findAll();
        return ResponseEntity.ok(users);
    }

    @GetMapping("/role/{role}")
    public ResponseEntity<List<User>> getUsersByRole(@PathVariable UserRole role) {
        List<User> users = userService.findByRole(role);
        return ResponseEntity.ok(users);
    }

    @PutMapping("/{userId}")
    public ResponseEntity<User> updateUser(@PathVariable UUID userId, @RequestBody UpdateUserRequest request) {
        User updatedUser = userService.updateUser(
                new UserId(userId),
                request.email(),
                request.role(),
                request.isActive()
        );
        return ResponseEntity.ok(updatedUser);
    }

    @PatchMapping("/{userId}/deactivate")
    public ResponseEntity<Void> deactivateUser(@PathVariable UUID userId) {
        userService.deactivateUser(new UserId(userId));
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/{userId}/activate")
    public ResponseEntity<Void> activateUser(@PathVariable UUID userId) {
        userService.activateUser(new UserId(userId));
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/authenticate")
    public ResponseEntity<AuthenticationResponse> authenticate(@RequestBody AuthenticationRequest request) {
        // This would typically return a JWT token or session info.
        // For now, we return a simple success/failure.
        boolean isAuthenticated = userService.authenticate(request.username(), request.password());
        if (isAuthenticated) {
            return ResponseEntity.ok(new AuthenticationResponse(true, "Authentication successful"));
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(new AuthenticationResponse(false, "Invalid credentials"));
        }
    }

    // Request and Response DTOs (could be moved to separate files)
    public record CreateUserRequest(String username, String email, UserRole role, Boolean isActive) {}
    public record UpdateUserRequest(String email, UserRole role, Boolean isActive) {}
    public record AuthenticationRequest(String username, String password) {}
    public record AuthenticationResponse(boolean success, String message) {}
}