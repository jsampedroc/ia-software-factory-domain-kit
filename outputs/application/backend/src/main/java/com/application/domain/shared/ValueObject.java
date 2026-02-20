package com.application.domain.shared;

import java.util.Objects;

public abstract class ValueObject {

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        return this.hashCode() == o.hashCode();
    }

    @Override
    public abstract int hashCode();

    protected boolean areEqual(Object a, Object b) {
        return Objects.equals(a, b);
    }
}