package com.onlydive.onlydive.exceptions;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class SpringOnlyDiveWebStatusException extends RuntimeException {
    private final HttpStatus status;

    public SpringOnlyDiveWebStatusException(String exMessage, HttpStatus status) {
        super(exMessage);
        this.status = status;
    }
}
