package ru.semenov.calendar.controller.exception;

import java.util.*;

import org.springframework.http.HttpStatus;
import ru.semenov.calendar.dto.error.ErrorResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import jakarta.persistence.EntityExistsException;

@RestControllerAdvice
public class ControllerExceptionHandler {
    @ExceptionHandler(value = { NoSuchElementException.class })
    public ResponseEntity<ErrorResponse> handleNotFound(RuntimeException e) {

        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ErrorResponse(e.getMessage()));
    }

    @ExceptionHandler(value = { EntityExistsException.class })
    public ResponseEntity<ErrorResponse> handleConflict(RuntimeException e) {

        return ResponseEntity.status(HttpStatus.CONFLICT).body(new ErrorResponse(e.getMessage()));
    }

    @ExceptionHandler(value = { AccessDeniedException.class, AuthenticationException.class })
    public ResponseEntity<ErrorResponse> handleAccessDenied(RuntimeException e) {

        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(new ErrorResponse(e.getMessage()));
    }
}
