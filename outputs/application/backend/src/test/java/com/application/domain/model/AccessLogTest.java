package com.application.domain.model;

import com.application.domain.valueobject.AccessLogId;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@ExtendWith(MockitoExtension.class)
class AccessLogTest {

    @Test
    void shouldCreateAccessLogSuccessfully() {
        // Given
        AccessLogId logId = new AccessLogId(UUID.randomUUID());
        String userId = "user-" + UUID.randomUUID();
        String action = "VIEW_PATIENT_RECORD";
        String resource = "/api/patients/123";
        LocalDateTime timestamp = LocalDateTime.now();
        String ipAddress = "192.168.1.100";

        // When
        AccessLog accessLog = AccessLog.builder()
                .logId(logId)
                .userId(userId)
                .action(action)
                .resource(resource)
                .timestamp(timestamp)
                .ipAddress(ipAddress)
                .build();

        // Then
        assertThat(accessLog).isNotNull();
        assertThat(accessLog.getId()).isEqualTo(logId);
        assertThat(accessLog.getLogId()).isEqualTo(logId);
        assertThat(accessLog.getUserId()).isEqualTo(userId);
        assertThat(accessLog.getAction()).isEqualTo(action);
        assertThat(accessLog.getResource()).isEqualTo(resource);
        assertThat(accessLog.getTimestamp()).isEqualTo(timestamp);
        assertThat(accessLog.getIpAddress()).isEqualTo(ipAddress);
    }

    @Test
    void shouldReturnCorrectIdViaGetId() {
        // Given
        AccessLogId logId = new AccessLogId(UUID.randomUUID());
        AccessLog accessLog = AccessLog.builder()
                .logId(logId)
                .userId("user-123")
                .action("LOGIN")
                .resource("/auth/login")
                .timestamp(LocalDateTime.now())
                .ipAddress("10.0.0.1")
                .build();

        // When & Then
        assertThat(accessLog.getId()).isSameAs(logId);
        assertThat(accessLog.getId()).isEqualTo(accessLog.getLogId());
    }

    @Test
    void shouldHaveNonNullFields() {
        // Given
        AccessLogId logId = new AccessLogId(UUID.randomUUID());
        String userId = "user-" + UUID.randomUUID();
        String action = "UPDATE";
        String resource = "/api/ehr/456";
        LocalDateTime timestamp = LocalDateTime.now();
        String ipAddress = "127.0.0.1";

        // When
        AccessLog accessLog = AccessLog.builder()
                .logId(logId)
                .userId(userId)
                .action(action)
                .resource(resource)
                .timestamp(timestamp)
                .ipAddress(ipAddress)
                .build();

        // Then
        assertThat(accessLog.getLogId()).isNotNull();
        assertThat(accessLog.getUserId()).isNotNull();
        assertThat(accessLog.getAction()).isNotNull();
        assertThat(accessLog.getResource()).isNotNull();
        assertThat(accessLog.getTimestamp()).isNotNull();
        assertThat(accessLog.getIpAddress()).isNotNull();
    }

    @Test
    void shouldHaveValidToStringRepresentation() {
        // Given
        AccessLogId logId = new AccessLogId(UUID.fromString("123e4567-e89b-12d3-a456-426614174000"));
        String userId = "dentist-789";
        String action = "CREATE_TREATMENT";
        String resource = "/api/treatments";
        LocalDateTime timestamp = LocalDateTime.of(2024, 1, 15, 14, 30, 0);
        String ipAddress = "172.16.254.1";

        AccessLog accessLog = AccessLog.builder()
                .logId(logId)
                .userId(userId)
                .action(action)
                .resource(resource)
                .timestamp(timestamp)
                .ipAddress(ipAddress)
                .build();

        // When
        String toStringResult = accessLog.toString();

        // Then
        assertThat(toStringResult).contains("AccessLog");
        assertThat(toStringResult).contains("logId=" + logId);
        assertThat(toStringResult).contains("userId=" + userId);
        assertThat(toStringResult).contains("action=" + action);
        assertThat(toStringResult).contains("resource=" + resource);
        assertThat(toStringResult).contains("timestamp=" + timestamp);
        assertThat(toStringResult).contains("ipAddress=" + ipAddress);
    }

    @Test
    void shouldBeEqualWhenSameId() {
        // Given
        AccessLogId logId = new AccessLogId(UUID.randomUUID());
        AccessLog log1 = AccessLog.builder()
                .logId(logId)
                .userId("user1")
                .action("ACTION_1")
                .resource("/res1")
                .timestamp(LocalDateTime.now())
                .ipAddress("1.1.1.1")
                .build();

        AccessLog log2 = AccessLog.builder()
                .logId(logId) // Same ID
                .userId("user2") // Different user
                .action("ACTION_2") // Different action
                .resource("/res2") // Different resource
                .timestamp(LocalDateTime.now().plusHours(1)) // Different timestamp
                .ipAddress("2.2.2.2") // Different IP
                .build();

        // When & Then
        assertThat(log1).isEqualTo(log2);
        assertThat(log1.hashCode()).isEqualTo(log2.hashCode());
    }

    @Test
    void shouldNotBeEqualWhenDifferentId() {
        // Given
        AccessLogId logId1 = new AccessLogId(UUID.randomUUID());
        AccessLogId logId2 = new AccessLogId(UUID.randomUUID());

        AccessLog log1 = AccessLog.builder()
                .logId(logId1)
                .userId("same-user")
                .action("SAME_ACTION")
                .resource("/same")
                .timestamp(LocalDateTime.now())
                .ipAddress("192.168.1.1")
                .build();

        AccessLog log2 = AccessLog.builder()
                .logId(logId2) // Different ID
                .userId("same-user")
                .action("SAME_ACTION")
                .resource("/same")
                .timestamp(LocalDateTime.now())
                .ipAddress("192.168.1.1")
                .build();

        // When & Then
        assertThat(log1).isNotEqualTo(log2);
        assertThat(log1.hashCode()).isNotEqualTo(log2.hashCode());
    }

    @Test
    void shouldInheritFromEntity() {
        // Given
        AccessLogId logId = new AccessLogId(UUID.randomUUID());
        AccessLog accessLog = AccessLog.builder()
                .logId(logId)
                .userId("admin-1")
                .action("AUDIT_ACCESS")
                .resource("/admin/audit")
                .timestamp(LocalDateTime.now())
                .ipAddress("10.10.10.10")
                .build();

        // Then
        assertThat(accessLog).isInstanceOf(Entity.class);
    }
}