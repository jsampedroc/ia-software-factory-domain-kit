package com.application.domain.shared;
import lombok.Getter;
import java.util.Objects;

@Getter
public abstract class Entity<ID extends ValueObject> {
    protected ID id;

    protected Entity() {} // Requerido para Lombok NoArgsConstructor en hijos

    protected Entity(ID id) {
        this.id = Objects.requireNonNull(id, "The ID cannot be null");
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Entity<?> entity = (Entity<?>) o;
        return Objects.equals(id, entity.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}