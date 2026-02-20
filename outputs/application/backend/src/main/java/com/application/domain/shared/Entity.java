package com.application.domain.shared;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@EqualsAndHashCode(of = "id")
@ToString(of = "id")
public abstract class Entity<T extends Serializable> {
    protected final T id;
    protected final LocalDateTime createdAt;
    protected LocalDateTime updatedAt;

    protected Entity(T id) {
        this.id = id;
        this.createdAt = LocalDateTime.now();
        this.updatedAt = this.createdAt;
    }

    protected Entity() {
        this.id = null;
        this.createdAt = LocalDateTime.now();
        this.updatedAt = this.createdAt;
    }

    protected void markAsUpdated() {
        this.updatedAt = LocalDateTime.now();
    }

    public boolean isNew() {
        return this.id == null;
    }
}