package com.application.domain.shared;
import lombok.*;
import java.util.Objects;
@Getter @NoArgsConstructor(access=AccessLevel.PROTECTED)
public abstract class Entity<ID extends ValueObject> { protected ID id; protected Entity(ID id) { this.id = Objects.requireNonNull(id); } }