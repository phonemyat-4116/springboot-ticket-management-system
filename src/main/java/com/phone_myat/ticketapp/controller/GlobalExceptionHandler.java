package com.phone_myat.ticketapp.controller;

import com.phone_myat.ticketapp.domain.dtos.ErrorDto;
import com.phone_myat.ticketapp.exceptions.UserNotFoundException;
import jakarta.validation.ConstraintViolationException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.List;

@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<ErrorDto> handleUserNotFoundException(UserNotFoundException ex) {
        log.error("Caught UserNotFoundException", ex);
        ErrorDto errorDto = new ErrorDto();
        errorDto.setError("User not found");

        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorDto);
    }

    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<ErrorDto> handleConstraintViolationException(ConstraintViolationException ex) {
        log.error("Caught ConstraintViolationException", ex);

        String errorMessage = ex.getConstraintViolations()
                .stream()
                .findFirst()
                .map(violation -> violation.getPropertyPath() + " : " + violation.getMessage())
                .orElse("A constraint violation occurred");

        ErrorDto errorDto = new ErrorDto();
        errorDto.setError(errorMessage);

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorDto);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorDto> handleMethodArgumentNotValidException(MethodArgumentNotValidException ex) {
        log.error("Caught MethodArgumentNotValidException", ex);

        List<FieldError> fieldErrors = ex.getBindingResult().getFieldErrors();
        String errorMessage = fieldErrors
                .stream()
                .findFirst()
                .map(fieldError -> fieldError.getField() + " : " +fieldError.getDefaultMessage())
                .orElse("Validation error occurred");

        ErrorDto errorDto = new ErrorDto();
        errorDto.setError(errorMessage);

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorDto);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorDto> handleException(Exception ex) {
        log.error("Caught exception {} :", ex.getClass().getSimpleName(), ex);
        ErrorDto errorDto = new ErrorDto();
        errorDto.setError("An unknown error occurred");

        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorDto);
    }
}

/**
 * MethodArgumentNotValidException
 *  └── BindingResult (The main folder)
 *       ├── FieldError 1 (e.g., "email")
 *       │    ├── getField() -> "email"
 *       │    └── getDefaultMessage() -> "must be a valid email"
 *       └── FieldError 2 (e.g., "age")
 *            ├── getField() -> "age"
 *            └── getDefaultMessage() -> "must be 18+"
 *
 *
 * log.error() -> for developer
 * errorDto -> for user
 */
