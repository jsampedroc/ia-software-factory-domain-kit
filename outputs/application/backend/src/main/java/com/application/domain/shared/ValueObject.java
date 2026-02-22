package com.application.domain.shared;

import lombok.EqualsAndHashCode;
import lombok.ToString;

import java.io.Serializable;

@EqualsAndHashCode
@ToString
public abstract class ValueObject implements Serializable {
    protected abstract Object[] getAtomicValues();

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;

        ValueObject other = (ValueObject) obj;
        Object[] thisValues = this.getAtomicValues();
        Object[] otherValues = other.getAtomicValues();

        if (thisValues.length != otherValues.length) return false;

        for (int i = 0; i < thisValues.length; i++) {
            if (!thisValues[i].equals(otherValues[i])) {
                return false;
            }
        }
        return true;
    }

    @Override
    public int hashCode() {
        Object[] values = getAtomicValues();
        int result = 17;
        for (Object value : values) {
            result = 31 * result + (value != null ? value.hashCode() : 0);
        }
        return result;
    }
}