package com.onlydive.onlydive.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import static org.springframework.http.ResponseEntity.status;

@ControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(SpringOnlyDiveException.class)
    public ResponseEntity<Object> handleSpringOnlyDiveException(SpringOnlyDiveException e) {
        return status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(e.getMessage());
    }
}
