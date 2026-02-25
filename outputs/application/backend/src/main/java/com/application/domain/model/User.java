package com.application.domain.model;

import com.application.domain.shared.Entity;
import com.application.domain.valueobject.UserId;
import com.application.domain.enums.UserRole;
import lombok.Getter;
import lombok.ToString;
import lombok.experimental.SuperBuilder;

import java.time.LocalDateTime;

@Getter
@ToString(callSuper = true)
@SuperBuilder(toBuilder = true)
public class User extends Entity<UserId> {

    private final String username;
    private final String email;
    private final UserRole role;
    private final boolean isActive;
    private final LocalDateTime createdAt;

    public User(UserId userId, String username, String email, UserRole role, boolean isActive, LocalDateTime createdAt) {
        super(userId);
        this.username = username;
        this.email = email;
        this.role = role;
        this.isActive = isActive;
        this.createdAt = createdAt;
    }

    public User deactivate() {
        return this.toBuilder()
                .isActive(false)
                .build();
    }

    public User changeRole(UserRole newRole) {
        return this.toBuilder()
                .role(newRole)
                .build();
    }

    public boolean isDentist() {
        return UserRole.DENTIST.equals(this.role);
    }

    public boolean isReceptionist() {
        return UserRole.RECEPTIONIST.equals(this.role);
    }

    public boolean isAdministrator() {
        return UserRole.ADMINISTRATOR.equals(this.role);
    }
}