package com.application.domain.shared;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.time.LocalDateTime;

@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public abstract class Entity<T> {
    protected T id;
    protected LocalDateTime createdAt;
    protected LocalDateTime updatedAt;
}