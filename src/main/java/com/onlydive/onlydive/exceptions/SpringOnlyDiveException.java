package com.onlydive.onlydive.exceptions;

public class SpringOnlyDiveException extends RuntimeException {
    public SpringOnlyDiveException(String exMessage, Exception e) {
        super(exMessage, e);
    }

    public SpringOnlyDiveException(String exMessage) {
        super(exMessage);
    }
}
