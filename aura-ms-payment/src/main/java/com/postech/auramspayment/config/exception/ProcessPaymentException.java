package com.postech.auramspayment.config.exception;

public class ProcessPaymentException extends RuntimeException {
    public ProcessPaymentException(String message) {
        super(message);
    }
}
