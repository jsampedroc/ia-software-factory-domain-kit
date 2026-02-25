package com.application.domain.shared;
import lombok.*;
import java.util.Objects;
import lombok.experimental.SuperBuilder;
@Getter @SuperBuilder @NoArgsConstructor(access=AccessLevel.PROTECTED)
public abstract class Entity<ID extends ValueObject> { protected ID id; protected Entity(ID id) { this.id = id; } }