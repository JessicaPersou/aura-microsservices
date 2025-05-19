package com.postech.auramsorder.config.exception;

public class FailProcessNewOrderException extends RuntimeException {
    public FailProcessNewOrderException(String message, String details) {
        super(message);
    }

}
