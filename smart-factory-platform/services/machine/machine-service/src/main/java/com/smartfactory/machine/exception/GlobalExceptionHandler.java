package com.smartfactory.machine.exception;

import com.smartfactory.machine.util.ErrorResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.ConstraintViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.Instant;
import java.util.LinkedHashMap;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(MachineNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleMachineNotFound(MachineNotFoundException ex, HttpServletRequest request) {

        ErrorResponse response = new ErrorResponse(Instant.now(), HttpStatus.NOT_FOUND.value(), HttpStatus.NOT_FOUND.getReasonPhrase(), ex.getMessage(), request.getRequestURI(), Map.of());

        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
    }

    @ExceptionHandler(MachineAlreadyExistsException.class)
    public ResponseEntity<ErrorResponse> handleMachineAlreadyExists(MachineAlreadyExistsException ex, HttpServletRequest request) {

        ErrorResponse response = new ErrorResponse(Instant.now(), HttpStatus.CONFLICT.value(), HttpStatus.CONFLICT.getReasonPhrase(), ex.getMessage(), request.getRequestURI(), Map.of());

        return ResponseEntity.status(HttpStatus.CONFLICT).body(response);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleValidationException(MethodArgumentNotValidException ex, HttpServletRequest request) {

        Map<String, String> errors = new LinkedHashMap<>();

        ex.getBindingResult().getFieldErrors().forEach(error -> errors.put(error.getField(), error.getDefaultMessage()));

        ErrorResponse response = new ErrorResponse(Instant.now(), HttpStatus.BAD_REQUEST.value(), HttpStatus.BAD_REQUEST.getReasonPhrase(), "Validation failed", request.getRequestURI(), errors);

        return ResponseEntity.badRequest().body(response);
    }

    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<ErrorResponse> handleConstraintViolation(ConstraintViolationException ex, HttpServletRequest request) {

        Map<String, String> errors = new LinkedHashMap<>();

        ex.getConstraintViolations().forEach(violation -> errors.put(violation.getPropertyPath().toString(), violation.getMessage()));

        ErrorResponse response = new ErrorResponse(Instant.now(), HttpStatus.BAD_REQUEST.value(), HttpStatus.BAD_REQUEST.getReasonPhrase(), "Validation failed", request.getRequestURI(), errors);

        return ResponseEntity.badRequest().body(response);
    }

    @ExceptionHandler(FactoryNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleFactoryNotFound(FactoryNotFoundException ex, HttpServletRequest request) {

        ErrorResponse errorResponse = new ErrorResponse(Instant.now(), HttpStatus.NOT_FOUND.value(), "Factory Not Found", ex.getMessage(), request.getRequestURI(), Map.of());

        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorResponse);
    }

    @ExceptionHandler(FactoryServiceUnavailableException.class)
    public ResponseEntity<ErrorResponse> handleFactoryServiceUnavailable(FactoryServiceUnavailableException ex, HttpServletRequest request) {

        ErrorResponse errorResponse = new ErrorResponse(Instant.now(), HttpStatus.SERVICE_UNAVAILABLE.value(), "Factory Service Unavailable", ex.getMessage(), request.getRequestURI(), Map.of());

        return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE).body(errorResponse);
    }
}