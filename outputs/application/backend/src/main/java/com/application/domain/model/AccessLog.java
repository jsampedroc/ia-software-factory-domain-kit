package com.application.domain.model;

import com.application.domain.shared.Entity;
import com.application.domain.valueobject.AccessLogId;
import lombok.Getter;
import lombok.ToString;
import lombok.experimental.SuperBuilder;

import java.time.LocalDateTime;

@Getter
@ToString(callSuper = true)
@SuperBuilder
public class AccessLog extends Entity<AccessLogId> {
    private final AccessLogId logId;
    private final String userId;
    private final String action;
    private final String resource;
    private final LocalDateTime timestamp;
    private final String ipAddress;

    @Override
    public AccessLogId getId() {
        return logId;
    }
}