package com.application.domain.model;

import com.application.domain.valueobject.UserId;
import com.application.domain.enums.UserRole;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(MockitoExtension.class)
class UserTest {

    private final UserId sampleUserId = new UserId(UUID.randomUUID());
    private final LocalDateTime sampleTime = LocalDateTime.of(2024, 1, 15, 10, 0);
    private final String sampleUsername = "jdoe";
    private final String sampleEmail = "john.doe@dentalclinic.com";

    @Test
    @DisplayName("Should create a User with all properties correctly set")
    void createUser() {
        User user = new User(sampleUserId, sampleUsername, sampleEmail, UserRole.DENTIST, true, sampleTime);

        assertThat(user.getId()).isEqualTo(sampleUserId);
        assertThat(user.getUsername()).isEqualTo(sampleUsername);
        assertThat(user.getEmail()).isEqualTo(sampleEmail);
        assertThat(user.getRole()).isEqualTo(UserRole.DENTIST);
        assertThat(user.isActive()).isTrue();
        assertThat(user.getCreatedAt()).isEqualTo(sampleTime);
    }

    @Test
    @DisplayName("Two Users with same ID should be equal")
    void equalityBasedOnId() {
        User user1 = new User(sampleUserId, "user1", "email1", UserRole.RECEPTIONIST, true, sampleTime);
        User user2 = new User(sampleUserId, "user2", "email2", UserRole.ADMINISTRATOR, false, sampleTime.plusDays(1));

        assertThat(user1).isEqualTo(user2);
        assertThat(user1.hashCode()).isEqualTo(user2.hashCode());
    }

    @Test
    @DisplayName("Two Users with different IDs should not be equal")
    void inequalityBasedOnId() {
        UserId differentId = new UserId(UUID.randomUUID());
        User user1 = new User(sampleUserId, sampleUsername, sampleEmail, UserRole.DENTIST, true, sampleTime);
        User user2 = new User(differentId, sampleUsername, sampleEmail, UserRole.DENTIST, true, sampleTime);

        assertThat(user1).isNotEqualTo(user2);
    }

    @Test
    @DisplayName("toString should contain class name and ID")
    void toStringContainsRelevantInfo() {
        User user = new User(sampleUserId, sampleUsername, sampleEmail, UserRole.DENTIST, true, sampleTime);
        String toString = user.toString();

        assertThat(toString).contains("User");
        assertThat(toString).contains(sampleUserId.toString());
    }

    @Nested
    @DisplayName("Business Logic Methods")
    class BusinessLogicTests {

        @Test
        @DisplayName("deactivate should return a new User with isActive false")
        void deactivate() {
            User activeUser = new User(sampleUserId, sampleUsername, sampleEmail, UserRole.DENTIST, true, sampleTime);

            User deactivatedUser = activeUser.deactivate();

            assertThat(deactivatedUser.getId()).isEqualTo(sampleUserId);
            assertThat(deactivatedUser.isActive()).isFalse();
            assertThat(deactivatedUser.getUsername()).isEqualTo(sampleUsername);
            assertThat(deactivatedUser.getRole()).isEqualTo(UserRole.DENTIST);
            assertThat(deactivatedUser.getCreatedAt()).isEqualTo(sampleTime);
            assertThat(deactivatedUser).isNotSameAs(activeUser);
        }

        @Test
        @DisplayName("changeRole should return a new User with updated role")
        void changeRole() {
            User receptionist = new User(sampleUserId, sampleUsername, sampleEmail, UserRole.RECEPTIONIST, true, sampleTime);

            User administrator = receptionist.changeRole(UserRole.ADMINISTRATOR);

            assertThat(administrator.getId()).isEqualTo(sampleUserId);
            assertThat(administrator.getRole()).isEqualTo(UserRole.ADMINISTRATOR);
            assertThat(administrator.isActive()).isTrue();
            assertThat(administrator.getUsername()).isEqualTo(sampleUsername);
            assertThat(administrator).isNotSameAs(receptionist);
        }

        @Test
        @DisplayName("updateEmail should return a new User with updated email")
        void updateEmail() {
            User originalUser = new User(sampleUserId, sampleUsername, "old@email.com", UserRole.DENTIST, true, sampleTime);
            String newEmail = "new@email.com";

            User updatedUser = originalUser.updateEmail(newEmail);

            assertThat(updatedUser.getId()).isEqualTo(sampleUserId);
            assertThat(updatedUser.getEmail()).isEqualTo(newEmail);
            assertThat(updatedUser.getUsername()).isEqualTo(sampleUsername);
            assertThat(updatedUser.getRole()).isEqualTo(UserRole.DENTIST);
            assertThat(updatedUser).isNotSameAs(originalUser);
        }

        @Test
        @DisplayName("isDentist should return true only for DENTIST role")
        void isDentist() {
            User dentist = new User(sampleUserId, sampleUsername, sampleEmail, UserRole.DENTIST, true, sampleTime);
            User receptionist = new User(sampleUserId, sampleUsername, sampleEmail, UserRole.RECEPTIONIST, true, sampleTime);
            User admin = new User(sampleUserId, sampleUsername, sampleEmail, UserRole.ADMINISTRATOR, true, sampleTime);

            assertThat(dentist.isDentist()).isTrue();
            assertThat(receptionist.isDentist()).isFalse();
            assertThat(admin.isDentist()).isFalse();
        }

        @Test
        @DisplayName("isReceptionist should return true only for RECEPTIONIST role")
        void isReceptionist() {
            User dentist = new User(sampleUserId, sampleUsername, sampleEmail, UserRole.DENTIST, true, sampleTime);
            User receptionist = new User(sampleUserId, sampleUsername, sampleEmail, UserRole.RECEPTIONIST, true, sampleTime);
            User admin = new User(sampleUserId, sampleUsername, sampleEmail, UserRole.ADMINISTRATOR, true, sampleTime);

            assertThat(dentist.isReceptionist()).isFalse();
            assertThat(receptionist.isReceptionist()).isTrue();
            assertThat(admin.isReceptionist()).isFalse();
        }

        @Test
        @DisplayName("isAdministrator should return true only for ADMINISTRATOR role")
        void isAdministrator() {
            User dentist = new User(sampleUserId, sampleUsername, sampleEmail, UserRole.DENTIST, true, sampleTime);
            User receptionist = new User(sampleUserId, sampleUsername, sampleEmail, UserRole.RECEPTIONIST, true, sampleTime);
            User admin = new User(sampleUserId, sampleUsername, sampleEmail, UserRole.ADMINISTRATOR, true, sampleTime);

            assertThat(dentist.isAdministrator()).isFalse();
            assertThat(receptionist.isAdministrator()).isFalse();
            assertThat(admin.isAdministrator()).isTrue();
        }
    }

    @Nested
    @DisplayName("Role-Based Scenarios")
    class RoleScenarioTests {

        @Test
        @DisplayName("A deactivated dentist should still be identified as a dentist by role")
        void deactivatedDentistRoleCheck() {
            User activeDentist = new User(sampleUserId, "dr_smith", "smith@clinic.com", UserRole.DENTIST, true, sampleTime);
            User deactivatedDentist = activeDentist.deactivate();

            assertThat(deactivatedDentist.isDentist()).isTrue();
            assertThat(deactivatedDentist.isActive()).isFalse();
        }

        @Test
        @DisplayName("Changing role and deactivating in sequence should produce correct state")
        void multipleOperations() {
            User original = new User(sampleUserId, "user1", "original@email.com", UserRole.RECEPTIONIST, true, sampleTime);

            User roleChanged = original.changeRole(UserRole.ADMINISTRATOR);
            User deactivated = roleChanged.deactivate();
            User emailUpdated = deactivated.updateEmail("admin@new.com");

            assertThat(emailUpdated.getId()).isEqualTo(sampleUserId);
            assertThat(emailUpdated.getRole()).isEqualTo(UserRole.ADMINISTRATOR);
            assertThat(emailUpdated.isActive()).isFalse();
            assertThat(emailUpdated.getEmail()).isEqualTo("admin@new.com");
            assertThat(emailUpdated.getUsername()).isEqualTo("user1");
            assertThat(emailUpdated.getCreatedAt()).isEqualTo(sampleTime);
        }
    }
}