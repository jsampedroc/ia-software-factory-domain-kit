package com.application.domain.exception;

import lombok.Getter;

@Getter
public class DomainException extends RuntimeException {
    private final String errorCode;
    private final String domain;

    public DomainException(String message, String errorCode, String domain) {
        super(message);
        this.errorCode = errorCode;
        this.domain = domain;
    }

    public DomainException(String message, Throwable cause, String errorCode, String domain) {
        super(message, cause);
        this.errorCode = errorCode;
        this.domain = domain;
    }
}