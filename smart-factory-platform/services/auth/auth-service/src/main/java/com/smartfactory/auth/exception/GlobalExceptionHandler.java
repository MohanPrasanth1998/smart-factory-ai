package com.smartfactory.auth.exception;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import com.smartfactory.auth.util.ErrorResponse;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.Instant;
import java.util.LinkedHashMap;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

        @ExceptionHandler(UserAlreadyExistsException.class)
        public ResponseEntity<ErrorResponse> handleUserAlreadyExist(UserAlreadyExistsException ex, HttpServletRequest request){

            ErrorResponse response = new ErrorResponse(
                    Instant.now(),
                    HttpStatus.CONFLICT.value(),
                    HttpStatus.CONFLICT.getReasonPhrase(),
                    ex.getMessage(),
                    request.getRequestURI(),
                    Map.of()
            );

            return ResponseEntity
                    .status(HttpStatus.CONFLICT)
                    .body(response);
        }

        @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleValidation(MethodArgumentNotValidException ex,HttpServletRequest request){
            Map<String, String> errors = new LinkedHashMap<>();

            ex.getBindingResult()
                    .getFieldErrors()
                    .forEach(error ->
                            errors.put(
                                    error.getField(),
                                    error.getDefaultMessage()
                            )
                    );

            ErrorResponse response = new ErrorResponse(
                    Instant.now(),
                    HttpStatus.BAD_REQUEST.value(),
                    HttpStatus.BAD_REQUEST.getReasonPhrase(),
                    "Validation failed",
                    request.getRequestURI(),
                    errors
            );

            return ResponseEntity
                    .badRequest()
                    .body(response);
        }
}
