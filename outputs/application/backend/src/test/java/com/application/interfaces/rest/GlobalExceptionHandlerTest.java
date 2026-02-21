package com.application;

import com.application.domain.exception.DomainException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import jakarta.validation.Path;
import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class GlobalExceptionHandlerTest {

    @InjectMocks
    private GlobalExceptionHandler exceptionHandler;

    @Test
    void handleDomainException_ShouldReturnBadRequest() {
        DomainException ex = new DomainException("Business rule violation");
        ResponseEntity<GlobalExceptionHandler.ErrorResponse> response = exceptionHandler.handleDomainException(ex);
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals("Business rule violation", response.getBody().getMessage());
        assertEquals("DOMAIN_ERROR", response.getBody().getErrorCode());
    }

    @Test
    void handleMethodArgumentNotValidException_ShouldReturnBadRequest() {
        MethodArgumentNotValidException ex = mock(MethodArgumentNotValidException.class);
        BindingResult bindingResult = mock(BindingResult.class);
        FieldError fieldError = new FieldError("objectName", "fieldName", "defaultMessage");
        when(ex.getBindingResult()).thenReturn(bindingResult);
        when(bindingResult.getFieldErrors()).thenReturn(List.of(fieldError));

        ResponseEntity<GlobalExceptionHandler.ErrorResponse> response = exceptionHandler.handleMethodArgumentNotValidException(ex);
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals("Validation failed for object 'objectName'. Error count: 1", response.getBody().getMessage());
        assertEquals("VALIDATION_ERROR", response.getBody().getErrorCode());
        assertNotNull(response.getBody().getDetails());
        assertFalse(response.getBody().getDetails().isEmpty());
    }

    @Test
    void handleConstraintViolationException_ShouldReturnBadRequest() {
        ConstraintViolationException ex = mock(ConstraintViolationException.class);
        ConstraintViolation<?> violation = mock(ConstraintViolation.class);
        Path path = mock(Path.class);
        when(violation.getPropertyPath()).thenReturn(path);
        when(violation.getMessage()).thenReturn("must not be null");
        when(ex.getConstraintViolations()).thenReturn(Set.of(violation));

        ResponseEntity<GlobalExceptionHandler.ErrorResponse> response = exceptionHandler.handleConstraintViolationException(ex);
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertTrue(response.getBody().getMessage().contains("Constraint violation"));
        assertEquals("VALIDATION_ERROR", response.getBody().getErrorCode());
        assertNotNull(response.getBody().getDetails());
    }

    @Test
    void handleMethodArgumentTypeMismatchException_ShouldReturnBadRequest() {
        MethodArgumentTypeMismatchException ex = mock(MethodArgumentTypeMismatchException.class);
        when(ex.getName()).thenReturn("paramName");
        when(ex.getRequiredType()).thenReturn((Class) Long.class);
        when(ex.getValue()).thenReturn("not-a-number");

        ResponseEntity<GlobalExceptionHandler.ErrorResponse> response = exceptionHandler.handleMethodArgumentTypeMismatchException(ex);
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertTrue(response.getBody().getMessage().contains("Failed to convert"));
        assertEquals("TYPE_MISMATCH", response.getBody().getErrorCode());
    }

    @Test
    void handleIllegalArgumentException_ShouldReturnBadRequest() {
        IllegalArgumentException ex = new IllegalArgumentException("Invalid argument provided");
        ResponseEntity<GlobalExceptionHandler.ErrorResponse> response = exceptionHandler.handleIllegalArgumentException(ex);
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals("Invalid argument provided", response.getBody().getMessage());
        assertEquals("ILLEGAL_ARGUMENT", response.getBody().getErrorCode());
    }

    @Test
    void handleGenericException_ShouldReturnInternalServerError() {
        Exception ex = new Exception("Unexpected error");
        ResponseEntity<GlobalExceptionHandler.ErrorResponse> response = exceptionHandler.handleGenericException(ex);
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        assertEquals("An unexpected error occurred", response.getBody().getMessage());
        assertEquals("INTERNAL_ERROR", response.getBody().getErrorCode());
    }
}