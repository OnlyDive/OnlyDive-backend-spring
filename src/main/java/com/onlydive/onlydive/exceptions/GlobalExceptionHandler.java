package com.onlydive.onlydive.exceptions;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.validation.FieldError;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.springframework.http.ResponseEntity.status;

@ControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(SpringOnlyDiveException.class)
    public ResponseEntity<Object> handleSpringOnlyDiveException(SpringOnlyDiveException e) {
        return status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(getErrorsMap(e.getMessage()));
    }
    @ExceptionHandler(SpringOnlyDiveWebStatusException.class)
    public ResponseEntity<Object> handleSpringOnlyDiveWebStatusException(SpringOnlyDiveWebStatusException e) {
        return status(e.getStatus())
                .body(getErrorsMap(e.getMessage()));
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<Object> handleIllegalArgumentException(IllegalArgumentException e) {
        return status(HttpStatus.NOT_FOUND)
                .body(getErrorsMap(e.getMessage()));
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, List<String>>> handleValidationErrors(MethodArgumentNotValidException ex) {
        List<String> errors = ex.getBindingResult().getFieldErrors()
                .stream().map(FieldError::getDefaultMessage).toList();
        return new ResponseEntity<>(getErrorsMap(errors), new HttpHeaders(), HttpStatus.BAD_REQUEST);
    }

    private Map<String, List<String>> getErrorsMap(String error) {
        return getErrorsMap(List.of(error));
    }
    private Map<String, List<String>> getErrorsMap(List<String> errors) {
        Map<String, List<String>> errorResponse = new HashMap<>();
        errorResponse.put("errors", errors);
        return errorResponse;
    }
//    @ExceptionHandler(RuntimeException.class)
//    public ResponseEntity<Object> handleRuntimeException(RuntimeException e) {
//        return status(HttpStatus.INTERNAL_SERVER_ERROR)
//                .body(e.getMessage());
//    }
}
