package com.application.domain.valueobject;

import com.application.domain.shared.ValueObject;

public record InsuranceClaimId(String value) implements ValueObject {
    public InsuranceClaimId {
        if (value == null || value.isBlank()) {
            throw new IllegalArgumentException("InsuranceClaimId value cannot be null or blank");
        }
    }
}