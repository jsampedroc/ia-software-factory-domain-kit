package com.application.domain.shared;

import lombok.EqualsAndHashCode;
import lombok.ToString;

import java.io.Serializable;

@EqualsAndHashCode
@ToString
public abstract class ValueObject implements Serializable {
    // Base class for all Value Objects.
    // Value Objects are immutable and compared by their attributes.
}